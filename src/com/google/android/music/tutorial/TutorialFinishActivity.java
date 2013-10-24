package com.google.android.music.tutorial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.music.NautilusStatus;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.AppNavigation;

public class TutorialFinishActivity extends TutorialActivity
{
  private static void finishTutorial(Context paramContext, boolean paramBoolean1, boolean paramBoolean2)
  {
    Intent localIntent1 = new Intent(paramContext, TutorialFinishActivity.class);
    Intent localIntent2 = localIntent1.addFlags(335544320);
    Intent localIntent3 = localIntent1.putExtra("turnOffTutorial", paramBoolean1);
    Intent localIntent4 = localIntent1.putExtra("forceNautilus", paramBoolean2);
    paramContext.startActivity(localIntent1);
  }

  static final void finishTutorialPermanently(Context paramContext, boolean paramBoolean)
  {
    finishTutorial(paramContext, true, paramBoolean);
  }

  static final void finishTutorialTemporary(Context paramContext)
  {
    finishTutorial(paramContext, false, false);
  }

  private void launchHome()
  {
    AppNavigation.goHome(this);
    finish();
  }

  String getScreenNameLogExtra()
  {
    return "TutorialFinishActivity";
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    final boolean bool1 = getIntent().getBooleanExtra("turnOffTutorial", false);
    final boolean bool2 = getIntent().getBooleanExtra("forceNautilus", false);
    MusicPreferences localMusicPreferences = getPrefs();
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        TutorialFinishActivity localTutorialFinishActivity = TutorialFinishActivity.this;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            MusicPreferences localMusicPreferences = TutorialFinishActivity.this.getPrefs();
            if (localMusicPreferences == null)
              return;
            if (TutorialFinishActivity.1.this.val$turnOffTutorial)
              localMusicPreferences.setTutorialViewed(true);
            if (TutorialFinishActivity.1.this.val$forceNautilus)
            {
              NautilusStatus localNautilusStatus = NautilusStatus.GOT_NAUTILUS;
              localMusicPreferences.setTempNautilusStatus(localNautilusStatus);
            }
            TutorialFinishActivity.this.launchHome();
          }
        };
        localTutorialFinishActivity.runOnUiThread(local1);
      }
    };
    localMusicPreferences.runWithPreferenceService(local1);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.tutorial.TutorialFinishActivity
 * JD-Core Version:    0.6.2
 */