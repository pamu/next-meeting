package com.pamu_nagarjuna.meetingroom.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import macroid.Contexts

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

    toolBar map setSupportActionBar

    //getSupportActionBar.setDisplayHomeAsUpEnabled(true)
    //getSupportActionBar.setHomeButtonEnabled(true)
  }

}
