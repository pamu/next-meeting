package com.pamu_nagarjuna.meetingroom.ui.main

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import macroid.ActivityContextWrapper
import macroid.FullDsl._
import com.fortysevendeg.macroid.extras.TextTweaks._

/**
 * Created by pnagarjuna on 28/08/15.
 */

case class Slot(title: String, description: String)

class SlotListAdapter(slotList: List[Slot])(implicit context: ActivityContextWrapper)
  extends RecyclerView.Adapter[ViewHolder] {

  override def getItemCount: Int = slotList.size

  override def onBindViewHolder(vh: ViewHolder, i: Int): Unit = {
    val slot = slotList(i)
    runUi {
      (vh.title <~ tvText(slot.title)) ~
        (vh.description <~ tvText(slot.description))
    }
  }

  override def onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder = {
    val adapter = new Adapter
    ViewHolder(adapter)
  }
}

case class ViewHolder(adapter: Adapter)(implicit context: ActivityContextWrapper)
  extends RecyclerView.ViewHolder(adapter.layout) {
  val content = adapter.content
  val title = adapter.title
  val description = adapter.description
}