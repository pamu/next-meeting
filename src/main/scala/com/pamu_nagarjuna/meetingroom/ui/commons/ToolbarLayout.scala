package com.pamu_nagarjuna.meetingroom.ui.commons

import android.support.v7.widget.Toolbar
import android.view.{ContextThemeWrapper, View}
import com.fortysevendeg.android.scaladays.ui.commons.ToolbarStyles
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.pamu_nagarjuna.meetingroom.R
import macroid.FullDsl._
import macroid.{ActivityContextWrapper, Ui, ContextWrapper}

/**
 * Created by pnagarjuna on 19/08/15.
 */
trait ToolbarLayout extends ToolbarStyles {

  var toolBar = slot[Toolbar]

  def toolBarLayout(children: Ui[View]*)(implicit appContext: ContextWrapper, activityContext: ActivityContextWrapper): Ui[Toolbar] =
    Ui {
      val darkToolBar = getToolbarThemeDarkActionBar
      children foreach (uiView => darkToolBar.addView(uiView.get))
      toolBar = Some(darkToolBar)
      darkToolBar
    } <~ toolbarStyle(resGetDimensionPixelSize(R.dimen.height_toolbar))

  def expandedToolBarLayout(children: Ui[View]*)
                           (height: Int)
                           (implicit appContext: ContextWrapper, activityContext: ActivityContextWrapper): Ui[Toolbar] =
    Ui {
      val darkToolBar = getToolbarThemeDarkActionBar
      children foreach (uiView => darkToolBar.addView(uiView.get))
      toolBar = Some(darkToolBar)
      darkToolBar
    } <~ toolbarStyle(height)


  private def getToolbarThemeDarkActionBar(implicit activityContext: ActivityContextWrapper) = {
    val contextTheme = new ContextThemeWrapper(activityContext.getOriginal, R.style.ThemeOverlay_AppCompat_Dark_ActionBar)
    val darkToolBar = new Toolbar(contextTheme)
    darkToolBar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light)
    darkToolBar
  }

}
