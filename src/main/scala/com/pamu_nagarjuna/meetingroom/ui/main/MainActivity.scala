package com.pamu_nagarjuna.meetingroom.ui.main

import java.util.Calendar

import android.content.{Intent, ComponentName, ServiceConnection}
import android.os.{IBinder, Bundle}
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.{LinearLayoutManager, GridLayoutManager}
import android.view.{MenuItem, Menu}
import com.fortysevendeg.macroid.extras.DeviceMediaQueries._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.{TextTweaks, ViewTweaks}
import com.pamu_nagarjuna.meetingroom.R
import com.pamu_nagarjuna.meetingroom.ui.service.{MeetingRoomService, ServiceCallback}
import macroid.Contexts
import macroid.FullDsl._

/**
 * Created by pnagarjuna on 14/08/15.
 */
class MainActivity
  extends AppCompatActivity
  with Contexts[AppCompatActivity]
  with Layout
  with ServiceCallback {

  override def updateView(): Unit = {
    val layoutManager =
      landscapeTablet ?
        new GridLayoutManager(this, 3) |
        tablet ?
          new GridLayoutManager(this, 2) | new LinearLayoutManager(this)

    val currentHour = Calendar.getInstance().get(Calendar.HOUR)
    val slots = List(Slot(s"Current Hour $currentHour", true), Slot("Scala", true))
    val adapter = new SlotListAdapter(slots)

    runUi(
      recyclerView <~ rvLayoutManager(layoutManager)
        <~  rvAdapter(adapter)
    )

   runUi(recyclerView <~ ViewTweaks.vInvisible)

    runUi(errorView <~ ViewTweaks.vVisible, errorMessageView <~ TextTweaks.tvText("no network"))

  }

  val serviceConnection = new ServiceConnection {
    override def onServiceDisconnected(componentName: ComponentName): Unit = {}
    override def onServiceConnected(componentName: ComponentName, iBinder: IBinder): Unit = {
      iBinder.asInstanceOf[MeetingRoomService#LocalBinder].getInstance().register(MainActivity.this)
    }
  }

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(layout)

    bindService(new Intent(getApplicationContext, classOf[MeetingRoomService]), serviceConnection, 0)
    startService(new Intent(getApplication, classOf[MeetingRoomService]))

    updateView()

    toolBar map setSupportActionBar

    //getSupportActionBar.setDisplayHomeAsUpEnabled(true)
    //getSupportActionBar.setHomeButtonEnabled(true)
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.menu, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.settings =>
        startActivity(new Intent(getApplicationContext, classOf[MeetingRoomPrefs]))
        true
      case _ => true
    }
  }


}
