package com.pamu_nagarjuna.meetingroom.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.{LinearLayoutManager, GridLayoutManager}
import com.fortysevendeg.macroid.extras.DeviceMediaQueries._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import macroid.Contexts
import macroid.FullDsl._

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

    val slots = List(
      Slot("Slot1", "Scala session"),
      Slot("Slot2", "Scala js session"),
      Slot("Slot3", "Scala Android Session")
    )

    val adapter = new SlotListAdapter(slots)

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
