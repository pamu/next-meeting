package com.pamu_nagarjuna.meetingroom.ui.service

import android.app.Notification.Builder
import android.app.{PendingIntent, AlarmManager, Service}
import android.content.{Context, Intent}
import android.os.{Binder, IBinder}
import android.util.Log
import com.pamu_nagarjuna.meetingroom.R
import com.pamu_nagarjuna.meetingroom.ui.MeetingRoomReceiver
import com.pamu_nagarjuna.meetingroom.ui.main.MainActivity

import scala.util.Random

/**
 * Created by pnagarjuna on 09/09/15.
 */
class MeetingRoomService extends Service {

  val LOG_TAG = classOf[MeetingRoomService].getSimpleName

  var alarm: Option[AlarmManager] = None

  var callback: Option[ServiceCallback]= None

  override def onBind(intent: Intent): IBinder = new LocalBinder

  class LocalBinder extends Binder {
    def getInstance() = MeetingRoomService.this
  }

  def register(mainActivity: MainActivity): Unit = callback = Some(mainActivity)

  override def onCreate(): Unit = {
    super.onCreate()
    val nBuilder = new Builder(getApplication);
    nBuilder.setContentTitle("Meeting Room App")
    nBuilder.setContentText("running ...")
    nBuilder.setSmallIcon(R.drawable.ic_launcher)
    //nBuilder.setContentInfo("App running in the background")
    val intent = new Intent(getApplication, classOf[MainActivity])
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    val pIntent = PendingIntent.getActivity(getApplication, System.currentTimeMillis().asInstanceOf[Int], intent, PendingIntent.FLAG_ONE_SHOT)
    nBuilder.setContentIntent(pIntent)
    startForeground(Random.nextInt(), nBuilder.build())

    alarm = Some(getApplicationContext.getSystemService(Context.ALARM_SERVICE).asInstanceOf[AlarmManager])

    alarm.map(_.setRepeating(
      AlarmManager.RTC_WAKEUP,
      System.currentTimeMillis(),
      1000 * 60 * 5,
      MeetingRoomReceiver.getReceiverPendingIntent(getApplicationContext)))
  }

  override def onStartCommand(intent: Intent, flags: Int, startId: Int): Int = {
    if (intent.getAction == MeetingRoomService.fetchTask) {
      Log.d(LOG_TAG, "")
      callback.map(_.updateView())
    }
    super.onStartCommand(intent, flags, startId)
  }
}

object MeetingRoomService {
  val fetchTask = "events_fetch"

  def getMeetingRoomIntent: Intent = {
    val intent = new Intent();
    intent.setAction(fetchTask)
    intent
  }

}

trait ServiceCallback {
  def updateView(): Unit
}