package com.pamu_nagarjuna.meetingroom.ui.utils

import android.net.Uri
import android.provider.CalendarContract.Calendars
import android.util.Log
import macroid.ActivityContextWrapper

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by pnagarjuna on 30/08/15.
 */
object Utils {
  case class Calendar(name: String)

  def getCalendars(implicit activityContent: ActivityContextWrapper): Future[List[Calendar]] = Future {
      val resolver = activityContent.application.getContentResolver
      val cursor = resolver.query(Calendars.CONTENT_URI,
        Array(Calendars.NAME), null, null, null)
      cursor.moveToFirst()
      var calendars = List.empty[Calendar]
      while (cursor.moveToNext()) {
        val name = cursor.getString(cursor.getColumnIndex(Calendars.NAME))
        calendars = Calendar(name) :: calendars
        Log.d("calendars", calendars.mkString(", "))
      }
      cursor.close()
      calendars
    }

}
