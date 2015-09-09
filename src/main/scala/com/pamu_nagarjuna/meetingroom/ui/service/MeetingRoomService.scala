package com.pamu_nagarjuna.meetingroom.ui.service

import android.app.Notification.Builder
import android.app.{PendingIntent, Notification, AlarmManager, Service}
import android.content.{Context, Intent}
import android.os.{Binder, IBinder}
import com.pamu_nagarjuna.meetingroom.ui.MeetingRoomReceiver
import com.pamu_nagarjuna.meetingroom.ui.main.MainActivity

/**
 * Created by pnagarjuna on 09/09/15.
 */
class MeetingRoomService extends Service {

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
    nBuilder.setContentInfo("App running the background")
    val intent = new Intent(getApplication, classOf[MainActivity])
    val pIntent = PendingIntent.getActivity(getApplication, 0, intent, 0)
    nBuilder.setContentIntent(pIntent)
    startForeground(1, nBuilder.build())

    alarm = Some(getApplicationContext.getSystemService(Context.ALARM_SERVICE).asInstanceOf[AlarmManager])

    alarm.map(_.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 5, MeetingRoomReceiver.getReceiverPendingIntent(getApplicationContext)))
  }

  override def onStartCommand(intent: Intent, flags: Int, startId: Int): Int = {
    if (intent.getAction == MeetingRoomService.fetchTask) {
      println("update called in the service")
      callback.map(_.update())
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
  def update(): Unit
}