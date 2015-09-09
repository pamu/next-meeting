package com.pamu_nagarjuna.meetingroom.ui.main

import java.util.Calendar

import android.content.{Intent, ComponentName, ServiceConnection}
import android.os.{IBinder, Bundle}
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.{LinearLayoutManager, GridLayoutManager}
import android.view.{MenuItem, Menu}
import com.fortysevendeg.macroid.extras.DeviceMediaQueries._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.pamu_nagarjuna.meetingroom.R
import com.pamu_nagarjuna.meetingroom.ui.service.{MeetingRoomService, ServiceCallback}
import com.pamu_nagarjuna.meetingroom.ui.utils.Utils
import macroid.Contexts
import macroid.FullDsl._
import play.api.libs.json.{Json, JsValue}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by pnagarjuna on 14/08/15.
 */
class MainActivity
  extends AppCompatActivity
  with Contexts[AppCompatActivity]
  with Layout
  with ServiceCallback {

  override def update(): Unit = {

  }

  val serviceConnection = new ServiceConnection {

    override def onServiceDisconnected(componentName: ComponentName): Unit = {
    }

    override def onServiceConnected(componentName: ComponentName, iBinder: IBinder): Unit = {
      iBinder.asInstanceOf[MeetingRoomService#LocalBinder].getInstance().register(MainActivity.this)
    }
  }

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(layout)

    bindService(new Intent(getApplicationContext, classOf[MeetingRoomService]), serviceConnection, 0)

    val calendar = Calendar.getInstance()
    val now = calendar.getTime
    calendar.add(Calendar.HOUR, 1000)
    val after = calendar.getTime

    val slots = Utils.getEvents(now, after).flatMap { json => {
      Future {
        println(Json.prettyPrint(json))
        val items = (json \ "items").as[List[JsValue]]
        items.map { item =>
          Slot((item \ "summary").as[String],
            (item \ "description").asOpt[String].getOrElse("No Description"),
            ((item \ "start") \ "dateTime").asOpt[String].getOrElse(((item \ "start") \ "date").as[String]),
            ((item \ "end") \ "dateTime").asOpt[String].getOrElse(((item \ "end") \ "date").as[String]))
        }
      }
    }}

    //slots.recover { case th =>  List(Slot("Error occurred", th.getMessage))}

    val layoutManager =
      landscapeTablet ?
        new GridLayoutManager(this, 3) |
        tablet ?
          new GridLayoutManager(this, 2) | new LinearLayoutManager(this)

    val adapter = new SlotListAdapter(List(Slot("No Items", "Kill and relaunch the app to load", "", "")))

    runUi(
      recyclerView <~ rvLayoutManager(layoutManager) <~ rvAdapter(adapter)
    )

    slots.recover {
      case th => runUi(
        recyclerView <~ rvLayoutManager(layoutManager) <~ rvAdapter(new SlotListAdapter(List(Slot("Failed", th.getMessage, "", ""))))
      )
    }
    runUi(
      recyclerView <~ rvLayoutManager(layoutManager) <~ slots.map(slots => rvAdapter(new SlotListAdapter(slots)))
    )


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
