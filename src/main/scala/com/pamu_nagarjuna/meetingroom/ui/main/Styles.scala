package com.pamu_nagarjuna.meetingroom.ui.main

import android.support.v7.widget.{CardView, RecyclerView}
import android.view.Gravity
import android.view.ViewGroup.LayoutParams._
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.{ImageView, SeekBar, TextView, LinearLayout}
import com.fortysevendeg.macroid.extras.FrameLayoutTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.SeekBarEventsExtras.OnSeekBarChangeListenerHandler
import com.fortysevendeg.macroid.extras.TextTweaks
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ThemeExtras._
import com.fortysevendeg.macroid.extras.ViewGroupTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.pamu_nagarjuna.meetingroom.R
import macroid.FullDsl._
import macroid.{ActivityContextWrapper, ContextWrapper, Tweak}
import scala.language.postfixOps

/**
 * Created by pnagarjuna on 21/08/15.
 */
trait Styles {

  def progressChange(f: (SeekBar, Int, Boolean) => Unit)(implicit contextWrapper: ContextWrapper): Tweak[SeekBar] =
    Tweak[SeekBar](_.setOnSeekBarChangeListener(new OnSeekBarChangeListener {
      override def onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean): Unit = {
        f(seekBar, i, b)
      }

      override def onStopTrackingTouch(seekBar: SeekBar): Unit = {}

      override def onStartTrackingTouch(seekBar: SeekBar): Unit = {}
    }))

  def contentStyle(implicit context: ContextWrapper): Tweak[LinearLayout] =
    llVertical +
      vMatchParent

  def listStyle(implicit context: ContextWrapper): Tweak[RecyclerView] =
    llMatchWeightVertical +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default)) +
      vgClipToPadding(false)


  def textStyle(implicit context: ContextWrapper): Tweak[TextView] =
    vMatchWidth +
      TextTweaks.tvGravity(Gravity.CENTER_HORIZONTAL) +
      tvSizeResource(R.dimen.text_size) +
      tvColorResource(R.color.primary)

  def spanBarStyle(implicit context: ContextWrapper): Tweak[SeekBar] =
    vMatchWidth +
      vPadding(20, 10, 20, 10)

}

trait AdapterStyles {
  def cardStyle(implicit activityContext: ActivityContextWrapper): Tweak[CardView] =
    vMatchWidth +
      vMinHeight(resGetDimensionPixelSize(R.dimen.card_height)) +
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


  def lineHorizontalStyle(implicit context: ContextWrapper): Tweak[ImageView] =
    lp[LinearLayout](MATCH_PARENT, resGetDimensionPixelSize(R.dimen.line_size)) +
      vBackgroundColorResource(R.color.primary)
}
