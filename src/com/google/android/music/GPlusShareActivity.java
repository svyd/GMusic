package com.google.android.music;

import android.accounts.Account;
import android.accounts.AuthenticatorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.common.http.GoogleHttpClient;
import com.google.android.gsf.Gservices;
import com.google.android.music.download.DownloadUtils;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.sync.google.MusicAuthInfo;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;

public class GPlusShareActivity extends Activity
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);
  private boolean mCanceled = false;
  private MusicPreferences mMusicPreferences;
  private AsyncShareWorker mShareWorker;
  private String mStoreId;
  private Account mStreamingAccount;
  private TextView mTextView;
  private MusicEventLogger mTracker;

  public static boolean isSharingSupported(Context paramContext)
  {
    int i = 0;
    Intent localIntent1 = new Intent("com.google.android.apps.plus.SHARE_GOOGLE");
    Uri localUri = Uri.parse("https://music.google.com/music/playpreview");
    Intent localIntent2 = localIntent1.setData(localUri);
    Intent localIntent3 = localIntent1.putExtra("authAccount", "deadbeef@non-existent-email.com");
    Intent localIntent4 = localIntent1.putExtra("com.google.android.apps.plus.VERSION", "1.00");
    if (paramContext.getPackageManager().queryIntentActivities(localIntent1, i).size() != 0)
      i = 1;
    return i;
  }

  private void launchShare(SharePreviewResponse paramSharePreviewResponse)
  {
    Intent localIntent1 = new Intent("com.google.android.apps.plus.SHARE_GOOGLE");
    Uri localUri = Uri.parse(paramSharePreviewResponse.mUrl);
    Intent localIntent2 = localIntent1.setData(localUri);
    String str1 = this.mStreamingAccount.name;
    Intent localIntent3 = localIntent1.putExtra("authAccount", str1);
    Intent localIntent4 = localIntent1.putExtra("com.google.android.apps.plus.VERSION", "1.00");
    String str2 = paramSharePreviewResponse.mExternalId;
    Intent localIntent5 = localIntent1.putExtra("com.google.android.apps.plus.EXTERNAL_ID", str2);
    Intent localIntent6 = localIntent1.putExtra("com.google.android.apps.plus.FOOTER", "");
    Intent localIntent7 = localIntent1.addFlags(524288);
    if (LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Launching intent: ").append(localIntent1).append(" Extras: ");
      String str3 = localIntent1.getExtras().toString();
      String str4 = str3;
      int i = Log.i("GPlusShareMusic", str4);
    }
    startActivityForResult(localIntent1, 0);
    finish();
  }

  public static void share(Context paramContext, String paramString1, String paramString2)
  {
    Intent localIntent1 = new Intent(paramContext, GPlusShareActivity.class);
    Intent localIntent2 = localIntent1.putExtra("title", paramString2);
    Intent localIntent3 = localIntent1.putExtra("storeId", paramString1);
    paramContext.startActivity(localIntent1);
  }

  private void transitionToError()
  {
    MusicEventLogger localMusicEventLogger = this.mTracker;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "actionArea";
    arrayOfObject[1] = "gplusShare";
    localMusicEventLogger.trackEvent("failure", arrayOfObject);
    findViewById(2131296354).setVisibility(8);
    this.mTextView.setText(2131231175);
  }

  public String getPageUrlForTracking()
  {
    return "share";
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    finish();
  }

  public void onBackPressed()
  {
    this.mCanceled = true;
    super.onBackPressed();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this);
    this.mTracker = localMusicEventLogger;
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this, this);
    this.mMusicPreferences = localMusicPreferences;
    Account localAccount = this.mMusicPreferences.getStreamingAccount();
    this.mStreamingAccount = localAccount;
    if (this.mStreamingAccount == null)
    {
      int i = Log.e("GPlusShareMusic", "Asked to share a song/album, but not streaming account selected");
      finish();
      return;
    }
    setContentView(2130968713);
    Bundle localBundle = getIntent().getExtras();
    String str1 = localBundle.getString("storeId");
    this.mStoreId = str1;
    TextView localTextView1 = (TextView)findViewById(2131296386);
    this.mTextView = localTextView1;
    TextView localTextView2 = this.mTextView;
    Object[] arrayOfObject = new Object[1];
    String str2 = localBundle.getString("title");
    arrayOfObject[0] = str2;
    String str3 = getString(2131231173, arrayOfObject);
    localTextView2.setText(str3);
    Button localButton = (Button)findViewById(2131296572);
    if (localButton != null)
    {
      View.OnClickListener local1 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          boolean bool = GPlusShareActivity.access$002(GPlusShareActivity.this, true);
          GPlusShareActivity.this.finish();
        }
      };
      localButton.setOnClickListener(local1);
    }
    AsyncShareWorker localAsyncShareWorker = new AsyncShareWorker();
    this.mShareWorker = localAsyncShareWorker;
    boolean bool = this.mShareWorker.sendEmptyMessage(1);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    MusicPreferences.releaseMusicPreferences(this);
    this.mShareWorker.quit();
  }

  protected void onResume()
  {
    super.onResume();
    MusicEventLogger localMusicEventLogger = this.mTracker;
    String str = getPageUrlForTracking();
    Object[] arrayOfObject = new Object[0];
    localMusicEventLogger.trackScreenView(this, str, arrayOfObject);
  }

  private class AsyncShareWorker extends LoggableHandler
  {
    public AsyncShareWorker()
    {
      super();
    }

    private SharePreviewResponse createShareUrl()
    {
      GPlusShareActivity localGPlusShareActivity = GPlusShareActivity.this;
      MusicAuthInfo localMusicAuthInfo = new MusicAuthInfo(localGPlusShareActivity);
      String str1 = String.valueOf(Gservices.getLong(GPlusShareActivity.this.getApplicationContext().getContentResolver(), "android_id", 0L));
      String str2 = GPlusShareActivity.this.mMusicPreferences.getLoggingId();
      String str3 = DownloadUtils.getUserAgent(localGPlusShareActivity);
      GoogleHttpClient localGoogleHttpClient = new GoogleHttpClient(localGPlusShareActivity, str3, true);
      try
      {
        String str4 = DebugUtils.HTTP_TAG;
        if (DebugUtils.isAutoLogAll());
        HttpResponse localHttpResponse;
        int j;
        for (int i = 3; ; i = 2)
        {
          localGoogleHttpClient.enableCurlLogging(str4, i);
          Account localAccount = GPlusShareActivity.this.mStreamingAccount;
          String str5 = localMusicAuthInfo.getAuthToken(localAccount);
          Object[] arrayOfObject = new Object[1];
          String str6 = GPlusShareActivity.this.mStoreId;
          arrayOfObject[0] = str6;
          String str7 = String.format("https://music.google.com/music/sharepreview?storeId=%s&source=music-mobile&u=0", arrayOfObject);
          HttpGet localHttpGet = new HttpGet(str7);
          String str8 = "GoogleLogin auth=" + str5;
          localHttpGet.addHeader("Authorization", str8);
          localHttpGet.addHeader("X-Device-Logging-ID", str2);
          localHttpGet.addHeader("X-Device-ID", str1);
          localHttpResponse = localGoogleHttpClient.execute(localHttpGet);
          j = localHttpResponse.getStatusLine().getStatusCode();
          if ((j < 200) || (j >= 300))
            break;
          localSharePreviewResponse = SharePreviewResponse.parseFromJsonInputStream(localHttpResponse.getEntity().getContent());
          if (GPlusShareActivity.LOGV)
          {
            StringBuilder localStringBuilder = new StringBuilder().append("Got share url: ");
            String str9 = localSharePreviewResponse.mUrl;
            String str10 = str9;
            int k = Log.i("GPlusShareMusic", str10);
          }
          return localSharePreviewResponse;
        }
        String str11 = "Got invalid response from server: " + j;
        int m = Log.w("GPlusShareMusic", str11);
        InputStream localInputStream = localHttpResponse.getEntity().getContent();
        InputStreamReader localInputStreamReader = new InputStreamReader(localInputStream);
        localBufferedReader = new BufferedReader(localInputStreamReader);
        while (true)
        {
          String str12 = localBufferedReader.readLine();
          if (str12 == null)
            break;
          String str13 = "Response: " + str12;
          int n = Log.i("GPlusShareMusic", str13);
        }
      }
      catch (AuthenticatorException localAuthenticatorException)
      {
        SharePreviewResponse localSharePreviewResponse;
        BufferedReader localBufferedReader;
        String str14 = localAuthenticatorException.getMessage();
        int i1 = Log.e("GPlusShareMusic", str14, localAuthenticatorException);
        localGoogleHttpClient.close();
        while (true)
        {
          localSharePreviewResponse = null;
          break;
          localBufferedReader.close();
          localGoogleHttpClient.close();
        }
      }
      catch (IOException localIOException)
      {
        while (true)
        {
          String str15 = localIOException.getMessage();
          int i2 = Log.e("GPlusShareMusic", str15, localIOException);
          localGoogleHttpClient.close();
        }
      }
      finally
      {
        localGoogleHttpClient.close();
      }
    }

    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown message type: ");
        int i = paramMessage.what;
        String str = i;
        throw new IllegalArgumentException(str);
      case 1:
      }
      final SharePreviewResponse localSharePreviewResponse = createShareUrl();
      GPlusShareActivity localGPlusShareActivity = GPlusShareActivity.this;
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          if (!GPlusShareActivity.this.mCanceled)
          {
            if (localSharePreviewResponse == null)
            {
              GPlusShareActivity.this.transitionToError();
              return;
            }
            GPlusShareActivity localGPlusShareActivity = GPlusShareActivity.this;
            SharePreviewResponse localSharePreviewResponse = localSharePreviewResponse;
            localGPlusShareActivity.launchShare(localSharePreviewResponse);
            return;
          }
          if (!GPlusShareActivity.LOGV)
            return;
          int i = Log.i("GPlusShareMusic", "Ignoring share url after cancel");
        }
      };
      localGPlusShareActivity.runOnUiThread(local1);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.GPlusShareActivity
 * JD-Core Version:    0.6.2
 */