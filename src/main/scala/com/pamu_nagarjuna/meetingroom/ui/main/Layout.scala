package com.pamu_nagarjuna.meetingroom.ui.main

import android.support.v7.widget.{CardView, RecyclerView}
import android.widget.{ImageView, TextView, LinearLayout}
import com.fortysevendeg.macroid.extras.{ViewTweaks, CardViewTweaks}
import com.pamu_nagarjuna.meetingroom.ui.commons.ToolbarLayout
import macroid.{Tweak, ActivityContextWrapper}
import macroid.FullDsl._

/**
 * Created by pnagarjuna on 14/08/15.
 */
trait Layout
  extends ToolbarLayout
  with Styles {

  var recyclerView = slot[RecyclerView]

  def layout(implicit contextWrapper: ActivityContextWrapper) = getUi(
    l[LinearLayout](
      toolBarLayout(),
      l[LinearLayout](
        l[CardView](
          l[LinearLayout](
            w[TextView] <~ wire(errorMessageView) <~ errorTextStyle
          ) <~ linearLayoutStyle
        ) <~ cardStyle
      ) <~ errorLayoutStyle <~ wire(errorView) <~ ViewTweaks.vGone,
      w[RecyclerView] <~ wire(recyclerView) <~ listStyle
    ) <~ contentStyle
  )

  var errorMessageView = slot[TextView]

  var errorView = slot[LinearLayout]
}

class Adapter(implicit context: ActivityContextWrapper)
  extends AdapterStyles {

  var title = slot[TextView]

  val content = {
    getUi(
      l[CardView](
        l[LinearLayout](
          w[TextView] <~ wire(title) <~ titleStyle,
          w[ImageView] <~ lineHorizontalStyle
        ) <~ layoutStyle
      ) <~ cardStyle
    )
  }

  def layout = content
}
