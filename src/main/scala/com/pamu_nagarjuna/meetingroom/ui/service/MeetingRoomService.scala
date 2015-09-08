package com.pamu_nagarjuna.meetingroom.ui.service

import android.app.{AlarmManager, Service}
import android.content.Intent
import android.os.{Binder, IBinder}
import com.pamu_nagarjuna.meetingroom.ui.main.MainActivity

/**
 * Created by pnagarjuna on 09/09/15.
 */
class MeetingRoomService extends Service {

  var alarmManager: Option[AlarmManager] = None

  var callback: Option[ServiceCallback]= None

  override def onBind(intent: Intent): IBinder = new LocalBinder

  class LocalBinder extends Binder {
    def getInstance() = MeetingRoomService.this
  }

  def register(mainActivity: MainActivity): Unit = callback = Some(mainActivity)


  override def onCreate(): Unit = {
    super.onCreate()
    //alarmManager.map(manager => manager.setInexactRepeating())
  }

  override def onStartCommand(intent: Intent, flags: Int, startId: Int): Int = {
    super.onStartCommand(intent, flags, startId)
  }
}

trait ServiceCallback {
  def update(): Unit
}