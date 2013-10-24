package com.google.android.music.tutorial;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.music.cloudclient.OffersResponseJson;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent;
import com.google.android.music.store.MusicContent.XAudio;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.SystemUtils;
import com.google.android.music.utils.async.AsyncRunner;

public class TutorialLaunchActivity extends TutorialActivity
  implements GetAccountOffersTask.Callbacks
{
  private TextView mDatabaseUpdating;
  private GetAccountOffersTask mGetSelectedAccountOffers;
  private boolean mIsFirstCheckAccount = true;
  private SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener;

  public TutorialLaunchActivity()
  {
    SharedPreferences.OnSharedPreferenceChangeListener local3 = new SharedPreferences.OnSharedPreferenceChangeListener()
    {
      public void onSharedPreferenceChanged(SharedPreferences paramAnonymousSharedPreferences, String paramAnonymousString)
      {
        TutorialLaunchActivity localTutorialLaunchActivity = TutorialLaunchActivity.this;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            if (TutorialLaunchActivity.this.getPrefs() == null)
              return;
            if (!TutorialUtils.isDatabaseUpdated(TutorialLaunchActivity.this.getPrefs()))
              return;
            MusicPreferences localMusicPreferences = TutorialLaunchActivity.this.getPrefs();
            SharedPreferences.OnSharedPreferenceChangeListener localOnSharedPreferenceChangeListener = TutorialLaunchActivity.this.mPreferenceChangeListener;
            localMusicPreferences.unregisterOnSharedPreferenceChangeListener(localOnSharedPreferenceChangeListener);
            TutorialLaunchActivity.this.checkAccount();
          }
        };
        localTutorialLaunchActivity.runOnUiThread(local1);
      }
    };
    this.mPreferenceChangeListener = local3;
  }

  private void cancelGetOffers()
  {
    if (this.mGetSelectedAccountOffers == null)
      return;
    this.mGetSelectedAccountOffers.cancel();
    this.mGetSelectedAccountOffers = null;
  }

  private void checkAccount()
  {
    if (!this.mIsFirstCheckAccount)
      return;
    this.mIsFirstCheckAccount = false;
    int i = 1;
    if ((!getIntent().getBooleanExtra("forceTutorial", false)) && (getPrefs().wasTutorialViewed()))
      i = 0;
    while (i == 0)
    {
      TutorialUtils.finishTutorialTemporarily(this);
      return;
      if (!SystemUtils.isConnectedToNetwork(this))
        i = 0;
    }
    Account localAccount = getPrefs().getSyncAccount();
    if (localAccount != null)
    {
      GetAccountOffersTask localGetAccountOffersTask = new GetAccountOffersTask(this, localAccount, this);
      this.mGetSelectedAccountOffers = localGetAccountOffersTask;
      this.mGetSelectedAccountOffers.run();
      return;
    }
    TutorialUtils.openSelectAccountActivity(this, true);
  }

  private void initTutorial()
  {
    if (TutorialUtils.isDatabaseUpdated(getPrefs()))
    {
      checkAccount();
      return;
    }
    this.mDatabaseUpdating.setText(2131231398);
    MusicPreferences localMusicPreferences = getPrefs();
    SharedPreferences.OnSharedPreferenceChangeListener localOnSharedPreferenceChangeListener = this.mPreferenceChangeListener;
    localMusicPreferences.registerOnSharedPreferenceChangeListener(localOnSharedPreferenceChangeListener);
    final Context localContext = getApplicationContext();
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      public void backgroundTask()
      {
        Context localContext = localContext;
        Uri localUri = MusicContent.XAudio.CONTENT_URI;
        boolean bool = MusicContent.existsContent(localContext, localUri);
      }

      public void taskCompleted()
      {
        if (TutorialLaunchActivity.this.getPrefs() == null)
          return;
        MusicPreferences localMusicPreferences = TutorialLaunchActivity.this.getPrefs();
        SharedPreferences.OnSharedPreferenceChangeListener localOnSharedPreferenceChangeListener = TutorialLaunchActivity.this.mPreferenceChangeListener;
        localMusicPreferences.unregisterOnSharedPreferenceChangeListener(localOnSharedPreferenceChangeListener);
        TutorialLaunchActivity.this.checkAccount();
      }
    });
  }

  String getScreenNameLogExtra()
  {
    return "signUpLaunch";
  }

  public void onAccountDisabled(Account paramAccount)
  {
    getPrefs().setInvalidStreamingAccount(paramAccount);
    MusicPreferences localMusicPreferences = getPrefs();
    TutorialUtils.finishTutorialPermanently(this, false, localMusicPreferences);
  }

  public void onAccountInvalid(Account paramAccount)
  {
    getPrefs().setInvalidStreamingAccount(paramAccount);
    MusicPreferences localMusicPreferences = getPrefs();
    TutorialUtils.finishTutorialPermanently(this, false, localMusicPreferences);
  }

  public void onAccountOffersError(Account paramAccount)
  {
    TutorialUtils.finishTutorialTemporarily(this);
  }

  public void onAccountOffersSuccess(Account paramAccount, OffersResponseJson paramOffersResponseJson)
  {
    MusicPreferences localMusicPreferences = getPrefs();
    TutorialUtils.openTryNautilusOrFinishTutorial(paramOffersResponseJson, this, localMusicPreferences);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    MusicPreferences localMusicPreferences = getPrefs();
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        TutorialLaunchActivity localTutorialLaunchActivity = TutorialLaunchActivity.this;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            if (TutorialLaunchActivity.this.getPrefs() == null)
              return;
            TutorialLaunchActivity.this.initTutorial();
          }
        };
        localTutorialLaunchActivity.runOnUiThread(local1);
      }
    };
    localMusicPreferences.runWithPreferenceService(local1);
    boolean bool = requestWindowFeature(1);
    setContentView(2130968705);
    TextView localTextView = (TextView)findViewById(2131296558);
    this.mDatabaseUpdating = localTextView;
  }

  protected void onDestroy()
  {
    cancelGetOffers();
    MusicPreferences localMusicPreferences = getPrefs();
    SharedPreferences.OnSharedPreferenceChangeListener localOnSharedPreferenceChangeListener = this.mPreferenceChangeListener;
    localMusicPreferences.unregisterOnSharedPreferenceChangeListener(localOnSharedPreferenceChangeListener);
    super.onDestroy();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.tutorial.TutorialLaunchActivity
 * JD-Core Version:    0.6.2
 */