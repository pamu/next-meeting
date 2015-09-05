package com.pamu_nagarjuna.meetingroom.ui.main

import java.util.{Calendar, Date}

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.{LinearLayoutManager, GridLayoutManager}
import com.fortysevendeg.macroid.extras.DeviceMediaQueries._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.pamu_nagarjuna.meetingroom.ui.utils.Utils
import macroid.Contexts
import macroid.FullDsl._

import scala.concurrent.ExecutionContext.Implicits.global

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

    val adapter = new SlotListAdapter(moreSlots)

    val calendar = Calendar.getInstance()
    val now = calendar.getTimeInMillis
    calendar.add(Calendar.HOUR, 100)
    val after = calendar.getTimeInMillis

    val slots = for(list <- Utils.getEvents(now, after)) yield {
      for(tuple <- list) yield Slot(tuple._1, tuple._2)
    }

    val layoutManager =
      landscapeTablet ?
        new GridLayoutManager(this, 3) |
        tablet ?
          new GridLayoutManager(this, 2) | new LinearLayoutManager(this)

    runUi(
      recyclerView <~ rvLayoutManager(layoutManager) <~ rvAdapter(adapter)
    )

    slots.recover {
      case th => runUi(
        recyclerView <~ rvLayoutManager(layoutManager) <~ rvAdapter(new SlotListAdapter(List(Slot("Failed", th.getMessage))))
      )
    }
    runUi(
      recyclerView <~ rvLayoutManager(layoutManager) <~ slots.map(slots => rvAdapter(new SlotListAdapter(slots)))
    )


    toolBar map setSupportActionBar

    //getSupportActionBar.setDisplayHomeAsUpEnabled(true)
    //getSupportActionBar.setHomeButtonEnabled(true)
  }

}
