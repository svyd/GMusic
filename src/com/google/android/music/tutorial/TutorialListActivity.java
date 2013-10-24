package com.google.android.music.tutorial;

import android.app.ListActivity;
import android.os.Bundle;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.preferences.MusicPreferences;

abstract class TutorialListActivity extends ListActivity
{
  private boolean isDestroyed = false;
  private MusicPreferences mPrefs;
  protected MusicEventLogger mTracker;

  protected MusicPreferences getPrefs()
  {
    return this.mPrefs;
  }

  abstract String getScreenNameLogExtra();

  public boolean isActivityDestroyed()
  {
    return this.isDestroyed;
  }

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
    this.isDestroyed = true;
    MusicPreferences.releaseMusicPreferences(this);
    this.mPrefs = null;
    super.onDestroy();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.tutorial.TutorialListActivity
 * JD-Core Version:    0.6.2
 */