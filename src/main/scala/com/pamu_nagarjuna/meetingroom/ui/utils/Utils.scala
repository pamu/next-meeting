package com.pamu_nagarjuna.meetingroom.ui.utils

import android.provider.CalendarContract
import macroid.ActivityContextWrapper

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by pnagarjuna on 30/08/15.
 */
object Utils {
  def getEvents(dtstart: Long, dtend: Long)(implicit activityContent: ActivityContextWrapper): Future[List[(String, String)]] = Future {
    val contentResolver = activityContent.application.getContentResolver
    val cursor = contentResolver.query(CalendarContract.Events.CONTENT_URI,
      Array("calendar_id", "organizer", "title", "dtstart", "dtend", "description", "duration"),
    null,
      null,
      null)
    //s"((dtstart >= ${dtstart.toString}) AND (dtend <= ${dtend.toString}))",
    var tuples = List[(String, String)]()
    cursor.moveToFirst()
    while(cursor.moveToNext()) {
      val title = cursor.getString(cursor.getColumnIndex("dtstart"))
      val desc = cursor.getString(cursor.getColumnIndex("dtend"))
      tuples = title -> desc :: tuples
    }
    cursor.close()
    tuples
  }

}
