package com.pamu_nagarjuna.meetingroom.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.{LinearLayoutManager, GridLayoutManager}
import com.fortysevendeg.macroid.extras.DeviceMediaQueries._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.pamu_nagarjuna.meetingroom.ui.utils.Utils
import macroid.Contexts
import macroid.FullDsl._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
 * Created by pnagarjuna on 14/08/15.
 */
class MainActivity
  extends AppCompatActivity
  with Contexts[AppCompatActivity]
  with Layout  {

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(layout)

    val moreSlots = List.fill(100)(Slot("Scala days", "Scala language ecosystem and best practices"))

    val fslots = for(calendarList <- Utils.getCalendars) yield (
      for(calendar <- calendarList) yield Slot(calendar.calendarId, calendar.displayName + " " + calendar.locaion))

    val adapter = new SlotListAdapter(moreSlots)

    fslots onComplete {
      case Success(list) => {
        if (list.isEmpty) {
          println("list is empty")
        }
        println(list.mkString(", "))
      }
      case Failure(th) =>  println(th.getMessage + " ")
    }

    fslots.recover{case th => List(Slot("Failed", th.getMessage))}

    val layoutManager =
      landscapeTablet ?
        new GridLayoutManager(this, 3) |
        tablet ?
          new GridLayoutManager(this, 2) | new LinearLayoutManager(this)

    runUi(
      recyclerView <~ rvLayoutManager(layoutManager) <~ rvAdapter(adapter)
    )

    toolBar map setSupportActionBar

    //getSupportActionBar.setDisplayHomeAsUpEnabled(true)
    //getSupportActionBar.setHomeButtonEnabled(true)
  }

}
