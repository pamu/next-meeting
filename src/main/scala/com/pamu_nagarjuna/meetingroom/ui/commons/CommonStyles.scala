package com.fortysevendeg.android.scaladays.ui.commons

import android.support.v7.widget.{RecyclerView, Toolbar}
import android.view.ViewGroup.LayoutParams._
import android.view.{ViewGroup, Gravity}
import android.widget._
import com.fortysevendeg.macroid.extras.FrameLayoutTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.pamu_nagarjuna.meetingroom.R
import macroid.Tweak
import macroid.ContextWrapper
import macroid.FullDsl._

import scala.language.postfixOps

trait ToolbarStyles {

  def toolbarStyle(height: Int)(implicit appContext: ContextWrapper): Tweak[Toolbar] =
    vContentSizeMatchWidth(height) +
      vBackground(R.color.primary)

}

trait PlaceHolderStyles {

  val placeholderContentStyle: Tweak[LinearLayout] =
    vWrapContent +
      flLayoutGravity(Gravity.CENTER) +
      llGravity(Gravity.CENTER_HORIZONTAL) +
      llVertical +
      vGone

  val placeholderImageStyle: Tweak[ImageView] =
    vWrapContent

  def placeholderMessageStyle(implicit appContext: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      tvGravity(Gravity.CENTER) +
      tvColorResource(R.color.text_error_message) +
      tvSize(resGetInteger(R.integer.text_big)) +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default_big))

  def placeholderButtonStyle(implicit appContext: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      vMinWidth(resGetDimensionPixelSize(R.dimen.width_button)) +
      tvText(R.string.reload) +
      tvColorResource(R.color.text_error_button) +
      vBackground(R.drawable.background_error_button) +
      tvAllCaps +
      tvSize(resGetInteger(R.integer.text_medium)) +
      tvGravity(Gravity.CENTER)

}

trait HeaderAdapterStyles {

  def headerContentStyle(implicit appContext: ContextWrapper): Tweak[LinearLayout] =
    lp[ViewGroup](MATCH_PARENT, resGetDimensionPixelSize(R.dimen.height_header)) +
      llHorizontal +
      vBackgroundColorResource(R.color.background_list_schedule_header)

  def headerNameStyle(implicit appContext: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      tvSize(resGetInteger(R.integer.text_medium)) +
      llLayoutGravity(Gravity.CENTER_VERTICAL) +
      tvColorResource(R.color.text_schedule_name) +
      tvBold +
      tvAllCaps +
      vPadding(resGetDimensionPixelSize(R.dimen.padding_default), 0, 0, 0)

}

trait ListStyles {

  def rootStyle(implicit appContext: ContextWrapper): Tweak[FrameLayout] = vMatchParent

  val recyclerViewStyle: Tweak[RecyclerView] =
    vMatchParent +
      rvNoFixedSize

  val progressBarStyle: Tweak[ProgressBar] =
    vWrapContent +
      flLayoutGravity(Gravity.CENTER)
}