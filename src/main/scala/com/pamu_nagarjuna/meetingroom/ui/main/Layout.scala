package com.pamu_nagarjuna.meetingroom.ui.main

import java.util.Calendar

import android.support.v7.widget.{LinearLayoutManager, GridLayoutManager, CardView, RecyclerView}
import android.widget.{ImageView, SeekBar, TextView, LinearLayout}
import com.fortysevendeg.macroid.extras.DeviceMediaQueries._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.pamu_nagarjuna.meetingroom.ui.commons.ToolbarLayout
import com.fortysevendeg.macroid.extras.SeekBarTweaks._
import com.pamu_nagarjuna.meetingroom.ui.utils.Utils
import macroid.ActivityContextWrapper
import macroid.FullDsl._
import com.fortysevendeg.macroid.extras.TextTweaks._
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by pnagarjuna on 14/08/15.
 */
trait Layout
  extends ToolbarLayout
  with Styles {

  var statusText = slot[TextView]

  var spanBar = slot[SeekBar]

  var recyclerView = slot[RecyclerView]

  def layout(implicit contextWrapper: ActivityContextWrapper) = getUi(
    l[LinearLayout](
      toolBarLayout(),
      w[TextView] <~ wire(statusText) <~ tvText(spanBar.map(_.getProgress.toString).getOrElse("")) <~ textStyle,
      w[SeekBar] <~ wire(spanBar) <~ sbMax(10) <~ spanBarStyle <~ progressChange((seekBar, i, b) => {
        System.out.println(i.toString)
        statusText.map(_.setText(i.toString + " hours from now"))

        val calendar = Calendar.getInstance()
        val now = calendar.getTime
        calendar.add(Calendar.HOUR, i)
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

        //slots.recover { case th =>  List(Slot("Error occured", th.getMessage))}

        val layoutManager =
          landscapeTablet ?
            new GridLayoutManager(contextWrapper.application, 3) |
            tablet ?
              new GridLayoutManager(contextWrapper.application, 2) | new LinearLayoutManager(contextWrapper.application)

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

      }),
      w[RecyclerView] <~ wire(recyclerView) <~ listStyle
    ) <~ contentStyle
  )

}

class Adapter(implicit context: ActivityContextWrapper)
  extends AdapterStyles {

  var title = slot[TextView]

  var description = slot[TextView]

  var startTime = slot[TextView]

  var endTime = slot[TextView]

  val content = {
    getUi(
      l[CardView](
        l[LinearLayout](
          w[TextView] <~ wire(title) <~ titleStyle,
          w[ImageView] <~ lineHorizontalStyle,
          w[TextView] <~ wire(description) <~ descriptionStyle,
          w[TextView] <~ wire(startTime) <~ descriptionStyle,
          w[TextView] <~ wire(endTime) <~ descriptionStyle
        ) <~ layoutStyle
      ) <~ cardStyle
    )
  }

  def layout = content
}
