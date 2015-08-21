package com.pamu_nagarjuna.meetingroom.ui.main

import android.widget.{Button, LinearLayout}
import com.pamu_nagarjuna.meetingroom.ui.commons.ToolbarLayout
import macroid.ActivityContextWrapper
import macroid.FullDsl._

/**
 * Created by pnagarjuna on 14/08/15.
 */
trait Layout extends ToolbarLayout with Styles {
  def layout(implicit contextWrapper: ActivityContextWrapper) = getUi(
    l[LinearLayout](
      toolBarLayout(),
      w[Button] <~ text("click") <~ buttonStyle
    ) <~ contentStyle
  )
}
