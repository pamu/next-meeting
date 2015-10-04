package com.pamu_nagarjuna.meetingroom.ui.main

import java.util.{TimeZone, Calendar}

import android.content.{Intent, ComponentName, ServiceConnection}
import android.os.{IBinder, Bundle}
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.{LinearLayoutManager, GridLayoutManager}
import android.util.Log
import android.view.{MenuItem, Menu}
import com.fortysevendeg.macroid.extras.DeviceMediaQueries._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.{TextTweaks, ViewTweaks}
import com.pamu_nagarjuna.meetingroom.R
import com.pamu_nagarjuna.meetingroom.ui.service.{MeetingRoomService, ServiceCallback}
import com.pamu_nagarjuna.meetingroom.ui.utils.Utils
import macroid.Contexts
import macroid.FullDsl._
import play.api.libs.json.{JsArray, JsValue}
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

  val LOG_TAG = classOf[MainActivity].getSimpleName

  override def updateView(): Unit = {
    val layoutManager =
      landscapeTablet ?
        new GridLayoutManager(this, 3) |
        tablet ?
          new GridLayoutManager(this, 2) | new LinearLayoutManager(this)

    val startTime = Calendar.getInstance().getTime
    val now = Calendar.getInstance()
    now.add(Calendar.HOUR, 5)
    val endTime = now.getTime

    case class Event(summary: String, desc: String, startTime: String, endTime: String)

    Log.d(LOG_TAG, s"start time $startTime")
    Log.d(LOG_TAG, s"end time $endTime")

    val eventsFuture: Future[List[Slot]] = Utils.getEvents(startTime, endTime).map { resJson =>
      Log.d(LOG_TAG, resJson + " json")
      val events = (resJson \ "items").as[JsArray].value.toList
      events.map { event =>
        Log.d(LOG_TAG, "event "  +  event)
        val eventObj = Event(
          (event \ "summary").as[String],
          (event \ "description").asOpt[String].getOrElse("--------"),
          ((event \ "start") \ "dateTime").asOpt[String].getOrElse(((event \ "start") \ "date").as[String]),
          ((event \ "end") \ "dateTime").asOpt[String].getOrElse(((event \ "end") \ "date").as[String])
        )
        Log.d(LOG_TAG, s"event obj $eventObj")
        Slot(eventObj.summary, true)
      }.reverse
    }

    showError("Loading ...")

    eventsFuture.recover { case th =>
      showError(th.getMessage)
      th.printStackTrace()
      Log.d(LOG_TAG, s"excepting fetching events from sever reason ${th.getMessage}")
    }

    //val slots = List(Slot(s"Current Hour $currentHour", true), Slot("Scala", true))
    //val adapter = new SlotListAdapter(slots)

    runUi(recyclerView <~ rvLayoutManager(layoutManager) <~ eventsFuture.map { slots =>
      showList()
      if (slots.isEmpty) {
        Log.d(LOG_TAG, " slots empty")
        rvAdapter(new SlotListAdapter(List(Slot("free", true))))
      }
      else {
        Log.d(LOG_TAG, " slots not empty")
        rvAdapter(new SlotListAdapter(slots))
      }})

  }

  def showError(message: String): Unit = {
    runUi(recyclerView <~ ViewTweaks.vGone,
    errorView <~ ViewTweaks.vVisible, errorMessageView <~ TextTweaks.tvText(message))
  }

  def showList(): Unit = {
    runUi(recyclerView <~ ViewTweaks.vVisible, errorView <~ ViewTweaks.vGone)
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

  override def onDestroy(): Unit = {
    super.onDestroy()
    unbindService(serviceConnection)
  }
}
