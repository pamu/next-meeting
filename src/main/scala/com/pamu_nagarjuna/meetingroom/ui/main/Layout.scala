package com.pamu_nagarjuna.meetingroom.ui.main

import android.support.v7.widget.{CardView, RecyclerView}
import android.widget.{SeekBar, TextView, LinearLayout}
import com.pamu_nagarjuna.meetingroom.ui.commons.ToolbarLayout
import com.fortysevendeg.macroid.extras.SeekBarTweaks._
import macroid.ActivityContextWrapper
import macroid.FullDsl._

/**
 * Created by pnagarjuna on 14/08/15.
 */
trait Layout
  extends ToolbarLayout
  with Styles {

  var spanBar = slot[SeekBar]

  var recyclerView = slot[RecyclerView]

  def layout(implicit contextWrapper: ActivityContextWrapper) = getUi(
    l[LinearLayout](
      toolBarLayout(),
      w[SeekBar] <~ wire(spanBar) <~ sbMax(12) <~ spanBarStyle,
      w[RecyclerView] <~ wire(recyclerView) <~ listStyle
    ) <~ contentStyle
  )

}

class Adapter(implicit context: ActivityContextWrapper)
  extends AdapterStyles {

  var title = slot[TextView]

  var description = slot[TextView]

  val content = {
    getUi(
      l[CardView](
        l[LinearLayout](
          w[TextView] <~ wire(title) <~ titleStyle,
          w[TextView] <~ wire(description) <~ descriptionStyle
        ) <~ layoutStyle
      ) <~ cardStyle
    )
  }

  def layout = content
}
