package com.google.android.music.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.music.NautilusStatus;
import com.google.android.music.cloudclient.JsonUtils;
import com.google.android.music.cloudclient.OfferJson;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.purchase.Finsky;
import com.google.android.music.ui.cardlib.PlayIconAndTextListAdapter;
import com.google.android.music.ui.cardlib.PlayIconAndTextListAdapter.IconAndTextListEntry;
import com.google.android.music.utils.TypefaceUtil;
import java.io.IOException;

public class TutorialTryNautilusActivity extends TutorialActivity
  implements View.OnClickListener
{
  private ListView mList;
  private OfferJson mOffer;
  private TextView mOfferDescription;

  private void initListView()
  {
    ListView localListView = (ListView)findViewById(2131296564);
    this.mList = localListView;
    PlayIconAndTextListAdapter.IconAndTextListEntry localIconAndTextListEntry1 = new PlayIconAndTextListAdapter.IconAndTextListEntry(2130837758, 2131230990);
    PlayIconAndTextListAdapter.IconAndTextListEntry localIconAndTextListEntry2 = new PlayIconAndTextListAdapter.IconAndTextListEntry(2130837766, 2131230991);
    PlayIconAndTextListAdapter.IconAndTextListEntry localIconAndTextListEntry3 = new PlayIconAndTextListAdapter.IconAndTextListEntry(2130837760, 2131230992);
    PlayIconAndTextListAdapter.IconAndTextListEntry[] arrayOfIconAndTextListEntry = new PlayIconAndTextListAdapter.IconAndTextListEntry[3];
    arrayOfIconAndTextListEntry[0] = localIconAndTextListEntry1;
    arrayOfIconAndTextListEntry[1] = localIconAndTextListEntry2;
    arrayOfIconAndTextListEntry[2] = localIconAndTextListEntry3;
    PlayIconAndTextListAdapter localPlayIconAndTextListAdapter = new PlayIconAndTextListAdapter(this, 2130968709, arrayOfIconAndTextListEntry);
    this.mList.setAdapter(localPlayIconAndTextListAdapter);
  }

  static void putOfferIntoIntent(OfferJson paramOfferJson, Intent paramIntent)
  {
    String str = JsonUtils.toJsonString(paramOfferJson);
    Intent localIntent = paramIntent.putExtra("offer", str);
  }

  String getScreenNameLogExtra()
  {
    return "signUpTryNautilus";
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (1 != paramInt1)
      return;
    if (paramInt2 == -1)
    {
      MusicEventLogger localMusicEventLogger1 = this.mTracker;
      Object[] arrayOfObject1 = new Object[0];
      localMusicEventLogger1.trackEvent("signUpPurchaseSuccess", arrayOfObject1);
      NautilusStatus localNautilusStatus = NautilusStatus.GOT_NAUTILUS;
      getPrefs().setTempNautilusStatus(localNautilusStatus);
      TutorialUtils.openConfirmNautilusActivity(this);
      return;
    }
    int i = Log.e("TutorialTryNautilusActivity", "purchase error");
    MusicEventLogger localMusicEventLogger2 = this.mTracker;
    Object[] arrayOfObject2 = new Object[0];
    localMusicEventLogger2.trackEvent("signUpPurchaseError", arrayOfObject2);
  }

  public void onClick(View paramView)
  {
    MusicPreferences localMusicPreferences = getPrefs();
    if (localMusicPreferences == null)
      return;
    switch (paramView.getId())
    {
    default:
      int i = Log.wtf("TutorialTryNautilusActivity", "Unexpected onClick()");
      return;
    case 2131296566:
      MusicEventLogger localMusicEventLogger1 = this.mTracker;
      Object[] arrayOfObject1 = new Object[0];
      localMusicEventLogger1.trackEvent("signUpTryNautilusClicked", arrayOfObject1);
      if (TextUtils.isEmpty(this.mOffer.mStoreDocId))
      {
        MusicEventLogger localMusicEventLogger2 = this.mTracker;
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = "signUpOffer";
        String str1 = this.mOffer.toString();
        arrayOfObject2[1] = str1;
        localMusicEventLogger2.trackEvent("signUpInvalidOfferIdError", arrayOfObject2);
        TutorialUtils.finishTutorialPermanently(this, false, localMusicPreferences);
        return;
      }
      String str2 = this.mOffer.mStoreDocId;
      if (Finsky.startNautilusPurchaseActivityForResult(this, localMusicPreferences, 1, str2))
        return;
      MusicEventLogger localMusicEventLogger3 = this.mTracker;
      Object[] arrayOfObject3 = new Object[0];
      localMusicEventLogger3.trackEvent("signUpFailedToOpenStoreAppError", arrayOfObject3);
      TutorialUtils.finishTutorialPermanently(this, false, localMusicPreferences);
      return;
    case 2131296565:
    }
    MusicEventLogger localMusicEventLogger4 = this.mTracker;
    Object[] arrayOfObject4 = new Object[0];
    localMusicEventLogger4.trackEvent("signUpSkipTryNautilus", arrayOfObject4);
    TutorialUtils.openOtherWaysToPlayActivity(this, localMusicPreferences);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968708);
    String str1 = getIntent().getStringExtra("offer");
    while (true)
    {
      try
      {
        OfferJson localOfferJson = (OfferJson)JsonUtils.parseFromJsonString(OfferJson.class, str1);
        this.mOffer = localOfferJson;
        TypefaceUtil.setTypeface((TextView)findViewById(2131296434), 2);
        Button localButton1 = (Button)findViewById(2131296566);
        if (this.mOffer.mHasFreeTrialPeriod)
        {
          i = 2131231017;
          localButton1.setText(i);
          localButton1.setOnClickListener(this);
          TypefaceUtil.setTypeface(localButton1, 3);
          Button localButton2 = (Button)findViewById(2131296565);
          localButton2.setOnClickListener(this);
          TypefaceUtil.setTypeface(localButton2, 1);
          initListView();
          TextView localTextView1 = (TextView)findViewById(2131296554);
          this.mOfferDescription = localTextView1;
          TextView localTextView2 = this.mOfferDescription;
          String str2 = this.mOffer.mDescription;
          localTextView2.setText(str2);
          TypefaceUtil.setTypeface(this.mOfferDescription, 1);
          return;
        }
      }
      catch (IOException localIOException)
      {
        MusicEventLogger localMusicEventLogger = this.mTracker;
        Object[] arrayOfObject = new Object[0];
        localMusicEventLogger.trackEvent("signUpMissingOfferError", arrayOfObject);
        TutorialUtils.finishTutorialTemporarily(this);
        int j = Log.wtf("TutorialTryNautilusActivity", "Failure to parse offers json", localIOException);
        return;
      }
      int i = 2131231018;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.tutorial.TutorialTryNautilusActivity
 * JD-Core Version:    0.6.2
 */