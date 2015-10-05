package com.pamu_nagarjuna.meetingroom.ui.utils

import java.io.{InputStreamReader, BufferedReader, InputStream}
import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone, Date}

import android.preference.PreferenceManager
import macroid.ContextWrapper
import org.apache.commons.lang3.time.DateUtils
import org.apache.http.entity.StringEntity
import org.apache.http.protocol.HTTP
import org.apache.http.{HttpEntity, HttpResponse}
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

/**
 * Created by pnagarjuna on 30/08/15.
 */

case class Event(summary: String, desc: String, startTime: String, endTime: String)

sealed trait Box
case object One extends Box
case object Two extends Box
case object Three extends Box
case object OtherBox extends Box
case object AllDay extends Box

object Utils {

  val eventsURL = "http://busyboy.herokuapp.com/events"
  val key = "pamu"

  def getEvents(timeMin: Date, timeMax: Date)(implicit context: ContextWrapper): Future[JsValue] = {
    val key = PreferenceManager.getDefaultSharedPreferences(context.application).getString("key", "").trim
    val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    format.setTimeZone(TimeZone.getTimeZone("UTC"))
    Future {
      scala.concurrent.blocking {
        val httpClient: DefaultHttpClient = new DefaultHttpClient
        val httpPost: HttpPost = new HttpPost(eventsURL)
        httpPost.setHeader("Content-Type", "application/json")
        val data = Json.obj(
          "key" -> key,
          "timeMin" -> format.format(timeMin),
          "timeMax" -> format.format(timeMax)
        )
        val postEntity = new StringEntity(data.toString(), HTTP.UTF_8)
        httpPost.setEntity(postEntity)
        val httpResponse: HttpResponse = httpClient.execute(httpPost)
        val httpEntity: HttpEntity = httpResponse.getEntity
        val is: InputStream = httpEntity.getContent
        val reader: BufferedReader = new BufferedReader(new InputStreamReader(is))
        val sb: StringBuilder = new StringBuilder
        var line: String = null
        while ( {
          line = reader.readLine
          line
        } != null) {
          sb.append(line + "\n")
        }
        is.close()
        Json.parse(sb.toString())
      }
    }
  }

  def currentHour: Date = {
    val currentHour = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    currentHour.set(Calendar.MINUTE, 0)
    currentHour.set(Calendar.SECOND, 0)
    currentHour.set(Calendar.MILLISECOND, 0)
    currentHour.getTime
  }

  def nextHour: Date = {
    val nextHour = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    nextHour.add(Calendar.HOUR, 1)
    nextHour.getTime
  }

  def thirdHour: Date = {
    val thirdHour = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    thirdHour.add(Calendar.HOUR, 2)
    thirdHour.getTime
  }

  def cal(datetime: String): Try[Date] =
    Try(DateUtils.parseDate(datetime, "yyyy-MM-dd'T'HH:mm:ssZZ"))

  def date(date: String): Date =
    DateUtils.parseDate(date, "yyyy-MM-dd")

  def box(event: Event): Box = {
    cal(event.startTime) match {
      case Success(startTimeCal) =>
        cal(event.endTime) match {
          case Success(endTimeCal) =>
            if ((currentHour.equals(startTimeCal) || currentHour.after(startTimeCal))
              && (currentHour.equals(endTimeCal) || currentHour.before(endTimeCal))) One
            else if ((nextHour.equals(startTimeCal) || nextHour.after(startTimeCal))
              && (nextHour.equals(endTimeCal) || nextHour.before(endTimeCal))) Two
            else if ((thirdHour.equals(startTimeCal) || thirdHour.after(startTimeCal))
              && (thirdHour.equals(endTimeCal) || thirdHour.before(endTimeCal))) Three
            else OtherBox
          case Failure(th) => AllDay
        }
      case Failure(th) => AllDay
    }
  }

}
