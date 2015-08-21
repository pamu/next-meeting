package com.pamu_nagarjuna.meetingroom.ui.main

import android.view.View
import android.widget.{Button, LinearLayout}
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import macroid.{ContextWrapper, Tweak}

/**
 * Created by pnagarjuna on 21/08/15.
 */
trait Styles {
  def contentStyle(implicit context: ContextWrapper): Tweak[LinearLayout] =
    llVertical +
      vMatchParent
  def buttonStyle(implicit context: ContextWrapper): Tweak[View] =
    vMatchWidth

}
