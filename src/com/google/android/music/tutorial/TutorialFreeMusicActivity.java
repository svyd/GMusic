package com.google.android.music.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.preferences.MusicPreferences;

public class TutorialFreeMusicActivity extends Activity
  implements View.OnClickListener
{
  private MusicEventLogger mTracker;

  public void freeMusicOnClick()
  {
    MusicEventLogger localMusicEventLogger = this.mTracker;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "tutorialAction";
    arrayOfObject[1] = "getFreeMusic";
    localMusicEventLogger.trackEvent("tutorial", arrayOfObject);
    Intent localIntent1 = new Intent("android.intent.action.VIEW");
    Uri localUri = Uri.parse(getString(2131230998));
    Intent localIntent2 = localIntent1.setData(localUri);
    Intent localIntent3 = localIntent1.addFlags(268435456);
    Intent localIntent4 = localIntent1.addFlags(32768);
    Intent localIntent5 = localIntent1.addFlags(524288);
    startActivity(localIntent1);
    finish();
  }

  public String getPageUrlForTracking()
  {
    return "tutorialFreeMusic";
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default:
      return;
    case 2131296556:
      skipOnClick();
      return;
    case 2131296555:
    }
    freeMusicOnClick();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this);
    this.mTracker = localMusicEventLogger;
    boolean bool1 = requestWindowFeature(1);
    setContentView(2130968704);
    findViewById(2131296555).setOnClickListener(this);
    Button localButton = (Button)findViewById(2131296556);
    boolean bool2 = localButton.requestFocus();
    localButton.setOnClickListener(this);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    MusicPreferences.releaseMusicPreferences(this);
  }

  protected void onResume()
  {
    super.onResume();
    MusicEventLogger localMusicEventLogger = this.mTracker;
    String str = getPageUrlForTracking();
    Object[] arrayOfObject = new Object[0];
    localMusicEventLogger.trackScreenView(this, str, arrayOfObject);
  }

  public void skipOnClick()
  {
    MusicEventLogger localMusicEventLogger = this.mTracker;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "tutorialAction";
    arrayOfObject[1] = "skipFreeMusic";
    localMusicEventLogger.trackEvent("tutorial", arrayOfObject);
    finish();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.tutorial.TutorialFreeMusicActivity
 * JD-Core Version:    0.6.2
 */