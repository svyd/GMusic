package com.google.android.music.tutorial;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.preferences.MusicPreferences;

abstract class TutorialActivity extends Activity
{
  private MusicPreferences mPrefs;
  protected MusicEventLogger mTracker;

  protected MusicPreferences getPrefs()
  {
    return this.mPrefs;
  }

  abstract String getScreenNameLogExtra();

  public void onBackPressed()
  {
    MusicEventLogger localMusicEventLogger = this.mTracker;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "signUpScreenName";
    String str = getScreenNameLogExtra();
    arrayOfObject[1] = str;
    localMusicEventLogger.trackEvent("signUpOnBackPressed", arrayOfObject);
    super.onBackPressed();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this);
    this.mTracker = localMusicEventLogger;
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this, this);
    this.mPrefs = localMusicPreferences;
    boolean bool = requestWindowFeature(1);
  }

  protected void onDestroy()
  {
    MusicPreferences.releaseMusicPreferences(this);
    this.mPrefs = null;
    super.onDestroy();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.tutorial.TutorialActivity
 * JD-Core Version:    0.6.2
 */