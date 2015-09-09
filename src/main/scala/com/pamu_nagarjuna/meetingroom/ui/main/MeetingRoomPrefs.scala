package com.pamu_nagarjuna.meetingroom.ui.main

import android.os.Bundle
import android.preference.PreferenceActivity
import com.pamu_nagarjuna.meetingroom.R

/**
 * Created by pnagarjuna on 09/09/15.
 */
class MeetingRoomPrefs extends PreferenceActivity {
  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.prefs)
  }
}
