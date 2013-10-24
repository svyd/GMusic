package com.google.android.music.tutorial;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.TypefaceUtil;

public class TutorialOtherWaysToPlayActivity extends TutorialActivity
  implements View.OnClickListener
{
  private ListView mList;

  private void initListView()
  {
    ListView localListView = (ListView)findViewById(2131296553);
    this.mList = localListView;
    OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry localOtherWaysToPlayListEntry1 = new OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry(2130837759, 2131231009, 2131231020);
    OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry localOtherWaysToPlayListEntry2 = new OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry(2130837767, 2131231008, 2131231021);
    OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry localOtherWaysToPlayListEntry3 = new OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry(2130837769, 2131231010, 2131231020);
    OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry[] arrayOfOtherWaysToPlayListEntry = new OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry[3];
    arrayOfOtherWaysToPlayListEntry[0] = localOtherWaysToPlayListEntry1;
    arrayOfOtherWaysToPlayListEntry[1] = localOtherWaysToPlayListEntry2;
    arrayOfOtherWaysToPlayListEntry[2] = localOtherWaysToPlayListEntry3;
    MusicPreferences localMusicPreferences = getPrefs();
    OtherWaysToPlayListAdapter localOtherWaysToPlayListAdapter = new OtherWaysToPlayListAdapter(this, localMusicPreferences, 2130968703, arrayOfOtherWaysToPlayListEntry);
    this.mList.setAdapter(localOtherWaysToPlayListAdapter);
  }

  String getScreenNameLogExtra()
  {
    return "signUpAddMusic";
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() != 2131296522)
      return;
    MusicPreferences localMusicPreferences = getPrefs();
    TutorialUtils.finishTutorialPermanently(this, false, localMusicPreferences);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968702);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this);
    this.mTracker = localMusicEventLogger;
    TypefaceUtil.setTypeface((TextView)findViewById(2131296434), 2);
    Button localButton = (Button)findViewById(2131296522);
    localButton.setOnClickListener(this);
    TypefaceUtil.setTypeface(localButton, 3);
    initListView();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.tutorial.TutorialOtherWaysToPlayActivity
 * JD-Core Version:    0.6.2
 */