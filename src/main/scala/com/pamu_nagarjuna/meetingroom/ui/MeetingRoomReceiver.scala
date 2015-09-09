package com.pamu_nagarjuna.meetingroom.ui

import android.app.PendingIntent
import android.content.{Intent, Context, BroadcastReceiver}
import com.pamu_nagarjuna.meetingroom.ui.service.MeetingRoomService

/**
 * Created by pnagarjuna on 09/09/15.
 */
class MeetingRoomReceiver extends BroadcastReceiver {
  override def onReceive(context: Context, intent: Intent): Unit = {
    if (intent.getAction == MeetingRoomReceiver.receiverAction) {
      System.out.println("receiver called")
      val sIntent = new Intent(context, classOf[MeetingRoomService])
      sIntent.setAction(MeetingRoomService.fetchTask)
      context.startService(sIntent)
    }
  }
}

object MeetingRoomReceiver {
  val receiverAction = "receiver_action"

  def getReceiverPendingIntent(context: Context): PendingIntent = {
    val intent = new Intent(context, classOf[MeetingRoomReceiver])
    intent.setAction(receiverAction)
    PendingIntent.getBroadcast(context, 0, intent, 0)
  }
}
