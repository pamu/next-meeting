package com.pamu_nagarjuna.meetingroom.ui.utils

import java.io.{InputStreamReader, BufferedReader, InputStream}
import java.text.SimpleDateFormat
import java.util.{TimeZone, Date}

import android.preference.PreferenceManager
import macroid.ContextWrapper
import org.apache.http.entity.StringEntity
import org.apache.http.protocol.HTTP
import org.apache.http.{HttpEntity, HttpResponse}
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by pnagarjuna on 30/08/15.
 */
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
        val data = Json.obj("key" -> key, "timeMin" -> format.format(timeMin), "timeMax" -> format.format(timeMax))
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

}
