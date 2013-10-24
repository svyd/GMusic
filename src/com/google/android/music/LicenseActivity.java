package com.google.android.music;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.google.android.music.eventlog.MusicEventLogger;

public class LicenseActivity extends Activity
{
  private Handler mHandler = null;
  private ProgressDialog mSpinnerDlg = null;
  private AlertDialog mTextDlg = null;
  private MusicEventLogger mTracker;
  private WebView mWebView = null;

  private void showErrorAndFinish()
  {
    this.mSpinnerDlg.dismiss();
    this.mSpinnerDlg = null;
    Toast.makeText(this, 2131231213, 1).show();
    finish();
  }

  private void showPageOfText(String paramString)
  {
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this);
    AlertDialog.Builder localBuilder2 = localBuilder1.setCancelable(true);
    WebView localWebView1 = this.mWebView;
    AlertDialog.Builder localBuilder3 = localBuilder2.setView(localWebView1).setTitle(2131231212);
    AlertDialog localAlertDialog1 = localBuilder1.create();
    this.mTextDlg = localAlertDialog1;
    AlertDialog localAlertDialog2 = this.mTextDlg;
    DialogInterface.OnDismissListener local2 = new DialogInterface.OnDismissListener()
    {
      public void onDismiss(DialogInterface paramAnonymousDialogInterface)
      {
        LicenseActivity.this.finish();
      }
    };
    localAlertDialog2.setOnDismissListener(local2);
    WebView localWebView2 = this.mWebView;
    String str1 = paramString;
    String str2 = null;
    localWebView2.loadDataWithBaseURL(null, str1, "text/html", "utf-8", str2);
    WebView localWebView3 = this.mWebView;
    WebViewClient local3 = new WebViewClient()
    {
      public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString)
      {
        LicenseActivity.this.mSpinnerDlg.dismiss();
        ProgressDialog localProgressDialog = LicenseActivity.access$202(LicenseActivity.this, null);
        LicenseActivity.this.mTextDlg.show();
        AlertDialog localAlertDialog = LicenseActivity.access$302(LicenseActivity.this, null);
      }
    };
    localWebView3.setWebViewClient(local3);
    this.mWebView = null;
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this);
    this.mTracker = localMusicEventLogger;
    setVisible(false);
    Context localContext = getApplicationContext();
    WebView localWebView = new WebView(localContext);
    this.mWebView = localWebView;
    Handler local1 = new Handler()
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        super.handleMessage(paramAnonymousMessage);
        if (paramAnonymousMessage.what == 0)
        {
          String str = (String)paramAnonymousMessage.obj;
          LicenseActivity.this.showPageOfText(str);
          return;
        }
        LicenseActivity.this.showErrorAndFinish();
      }
    };
    this.mHandler = local1;
    CharSequence localCharSequence1 = getText(2131231212);
    CharSequence localCharSequence2 = getText(2131230943);
    ProgressDialog localProgressDialog = ProgressDialog.show(this, localCharSequence1, localCharSequence2, true, false);
    localProgressDialog.setProgressStyle(0);
    this.mSpinnerDlg = localProgressDialog;
    Handler localHandler = this.mHandler;
    LicenseFileLoader localLicenseFileLoader = new LicenseFileLoader(localHandler);
    new Thread(localLicenseFileLoader).start();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.mTextDlg == null)
      return;
    this.mTextDlg.dismiss();
  }

  protected void onResume()
  {
    super.onResume();
    MusicEventLogger localMusicEventLogger = this.mTracker;
    Object[] arrayOfObject = new Object[0];
    localMusicEventLogger.trackScreenView(this, "license", arrayOfObject);
  }

  private class LicenseFileLoader
    implements Runnable
  {
    private Handler mHandler;

    public LicenseFileLoader(Handler arg2)
    {
      Object localObject;
      this.mHandler = localObject;
    }

    // ERROR //
    public void run()
    {
      // Byte code:
      //   0: iconst_0
      //   1: istore_1
      //   2: aconst_null
      //   3: astore_2
      //   4: new 28	java/lang/StringBuilder
      //   7: dup
      //   8: sipush 2048
      //   11: invokespecial 31	java/lang/StringBuilder:<init>	(I)V
      //   14: astore_3
      //   15: sipush 2048
      //   18: newarray char
      //   20: astore 4
      //   22: aload_0
      //   23: getfield 18	com/google/android/music/LicenseActivity$LicenseFileLoader:this$0	Lcom/google/android/music/LicenseActivity;
      //   26: invokevirtual 35	com/google/android/music/LicenseActivity:getResources	()Landroid/content/res/Resources;
      //   29: ldc 36
      //   31: invokevirtual 42	android/content/res/Resources:openRawResource	(I)Ljava/io/InputStream;
      //   34: astore 5
      //   36: new 44	java/util/zip/GZIPInputStream
      //   39: dup
      //   40: aload 5
      //   42: invokespecial 47	java/util/zip/GZIPInputStream:<init>	(Ljava/io/InputStream;)V
      //   45: astore 6
      //   47: new 49	java/io/InputStreamReader
      //   50: dup
      //   51: aload 6
      //   53: invokespecial 50	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
      //   56: astore 7
      //   58: aload 7
      //   60: aload 4
      //   62: invokevirtual 54	java/io/InputStreamReader:read	([C)I
      //   65: istore 8
      //   67: iload 8
      //   69: iflt +105 -> 174
      //   72: aload_3
      //   73: aload 4
      //   75: iconst_0
      //   76: iload 8
      //   78: invokevirtual 58	java/lang/StringBuilder:append	([CII)Ljava/lang/StringBuilder;
      //   81: astore 9
      //   83: goto -25 -> 58
      //   86: astore 10
      //   88: aload 7
      //   90: astore_2
      //   91: ldc 60
      //   93: ldc 62
      //   95: aload 10
      //   97: invokestatic 68	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   100: istore 11
      //   102: iconst_1
      //   103: istore_1
      //   104: aload_2
      //   105: ifnull +7 -> 112
      //   108: aload_2
      //   109: invokevirtual 71	java/io/InputStreamReader:close	()V
      //   112: iload_1
      //   113: ifne +21 -> 134
      //   116: aload_3
      //   117: invokestatic 77	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
      //   120: ifeq +14 -> 134
      //   123: ldc 60
      //   125: ldc 79
      //   127: invokestatic 82	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   130: istore 12
      //   132: iconst_2
      //   133: istore_1
      //   134: aload_0
      //   135: getfield 23	com/google/android/music/LicenseActivity$LicenseFileLoader:mHandler	Landroid/os/Handler;
      //   138: iload_1
      //   139: aconst_null
      //   140: invokevirtual 88	android/os/Handler:obtainMessage	(ILjava/lang/Object;)Landroid/os/Message;
      //   143: astore 13
      //   145: iload_1
      //   146: ifne +16 -> 162
      //   149: aload_3
      //   150: invokevirtual 92	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   153: astore 14
      //   155: aload 13
      //   157: aload 14
      //   159: putfield 98	android/os/Message:obj	Ljava/lang/Object;
      //   162: aload_0
      //   163: getfield 23	com/google/android/music/LicenseActivity$LicenseFileLoader:mHandler	Landroid/os/Handler;
      //   166: aload 13
      //   168: invokevirtual 102	android/os/Handler:sendMessage	(Landroid/os/Message;)Z
      //   171: istore 15
      //   173: return
      //   174: aload 7
      //   176: ifnull +8 -> 184
      //   179: aload 7
      //   181: invokevirtual 71	java/io/InputStreamReader:close	()V
      //   184: aload 7
      //   186: astore 16
      //   188: goto -76 -> 112
      //   191: astore 17
      //   193: aload 7
      //   195: astore 18
      //   197: goto -85 -> 112
      //   200: astore 19
      //   202: aload 20
      //   204: ifnull +8 -> 212
      //   207: aload 20
      //   209: invokevirtual 71	java/io/InputStreamReader:close	()V
      //   212: aload 19
      //   214: athrow
      //   215: astore 21
      //   217: goto -105 -> 112
      //   220: astore 22
      //   222: goto -10 -> 212
      //   225: astore 19
      //   227: aload 7
      //   229: astore 20
      //   231: goto -29 -> 202
      //   234: astore 10
      //   236: goto -145 -> 91
      //
      // Exception table:
      //   from	to	target	type
      //   58	83	86	java/io/IOException
      //   179	184	191	java/io/IOException
      //   15	58	200	finally
      //   91	102	200	finally
      //   108	112	215	java/io/IOException
      //   207	212	220	java/io/IOException
      //   58	83	225	finally
      //   15	58	234	java/io/IOException
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.LicenseActivity
 * JD-Core Version:    0.6.2
 */