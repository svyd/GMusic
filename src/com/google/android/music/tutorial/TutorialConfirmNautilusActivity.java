package com.google.android.music.tutorial;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.TypefaceUtil;

public class TutorialConfirmNautilusActivity extends TutorialActivity
  implements View.OnClickListener
{
  public String getPlayMusicLink()
  {
    String str = getResources().getString(2131231005);
    Resources localResources = getResources();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = str;
    return localResources.getString(2131231004, arrayOfObject);
  }

  String getScreenNameLogExtra()
  {
    return "signUpConfirmNautilus";
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() != 2131296560)
      return;
    MusicPreferences localMusicPreferences = getPrefs();
    TutorialUtils.finishTutorialPermanently(this, true, localMusicPreferences);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968706);
    TypefaceUtil.setTypeface((TextView)findViewById(2131296434), 2);
    TypefaceUtil.setTypeface((TextView)findViewById(2131296554), 2);
    Button localButton = (Button)findViewById(2131296560);
    boolean bool = localButton.requestFocus();
    localButton.setOnClickListener(this);
    TypefaceUtil.setTypeface(localButton, 3);
    TextView localTextView = (TextView)findViewById(2131296559);
    String str = getPlayMusicLink();
    localTextView.setText(str);
    TypefaceUtil.setTypeface(localTextView, 1);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.tutorial.TutorialConfirmNautilusActivity
 * JD-Core Version:    0.6.2
 */