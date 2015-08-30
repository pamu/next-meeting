package com.pamu_nagarjuna.meetingroom.ui.main

import android.support.v7.widget.{CardView, RecyclerView}
import android.widget.{SeekBar, TextView, LinearLayout}
import com.fortysevendeg.macroid.extras.FrameLayoutTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ThemeExtras._
import com.fortysevendeg.macroid.extras.ViewGroupTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.pamu_nagarjuna.meetingroom.R
import macroid.{ActivityContextWrapper, ContextWrapper, Tweak}
import scala.language.postfixOps

/**
 * Created by pnagarjuna on 21/08/15.
 */
trait Styles {

  def contentStyle(implicit context: ContextWrapper): Tweak[LinearLayout] =
    llVertical +
      vMatchParent

  def listStyle(implicit context: ContextWrapper): Tweak[RecyclerView] =
    llMatchWeightVertical +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default)) +
      vgClipToPadding(false)


  def textStyle(implicit context: ContextWrapper): Tweak[TextView] =
    vMatchWidth +
      tvSizeResource(R.dimen.text_size) +
      tvColorResource(R.color.primary)

  def spanBarStyle(implicit context: ContextWrapper): Tweak[SeekBar] =
    vMatchWidth +
      vPadding(20, 10, 20, 10)

}

trait AdapterStyles {
  def cardStyle(implicit activityContext: ActivityContextWrapper): Tweak[CardView] =
    vMatchWidth +
      (themeGetDrawable(android.R.attr.selectableItemBackground) map flForeground getOrElse Tweak.blank)

  def layoutStyle(implicit context: ContextWrapper): Tweak[LinearLayout] =
    vMatchParent +
      llVertical

  def titleStyle(implicit context: ContextWrapper): Tweak[TextView] =
    vMatchWidth +
      tvSizeResource(R.dimen.text_size) +
      tvColorResource(R.color.primary) +
      vPadding(20, 10, 10, 10)

  def descriptionStyle(implicit context: ContextWrapper): Tweak[TextView] =
    vMatchWidth +
    tvColorResource(R.color.accent) +
    vPadding(30, 10, 10, 10)

}
