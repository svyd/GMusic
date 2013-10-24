package com.google.android.youtube.player;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import com.google.android.youtube.player.internal.ac;
import com.google.android.youtube.player.internal.m;
import com.google.android.youtube.player.internal.z;

public enum YouTubeInitializationResult
{
  static
  {
    INTERNAL_ERROR = new YouTubeInitializationResult("INTERNAL_ERROR", 1);
    UNKNOWN_ERROR = new YouTubeInitializationResult("UNKNOWN_ERROR", 2);
    SERVICE_MISSING = new YouTubeInitializationResult("SERVICE_MISSING", 3);
    SERVICE_VERSION_UPDATE_REQUIRED = new YouTubeInitializationResult("SERVICE_VERSION_UPDATE_REQUIRED", 4);
    SERVICE_DISABLED = new YouTubeInitializationResult("SERVICE_DISABLED", 5);
    SERVICE_INVALID = new YouTubeInitializationResult("SERVICE_INVALID", 6);
    ERROR_CONNECTING_TO_SERVICE = new YouTubeInitializationResult("ERROR_CONNECTING_TO_SERVICE", 7);
    CLIENT_LIBRARY_UPDATE_REQUIRED = new YouTubeInitializationResult("CLIENT_LIBRARY_UPDATE_REQUIRED", 8);
    NETWORK_ERROR = new YouTubeInitializationResult("NETWORK_ERROR", 9);
    DEVELOPER_KEY_INVALID = new YouTubeInitializationResult("DEVELOPER_KEY_INVALID", 10);
    INVALID_APPLICATION_SIGNATURE = new YouTubeInitializationResult("INVALID_APPLICATION_SIGNATURE", 11);
    YouTubeInitializationResult[] arrayOfYouTubeInitializationResult = new YouTubeInitializationResult[12];
    YouTubeInitializationResult localYouTubeInitializationResult1 = SUCCESS;
    arrayOfYouTubeInitializationResult[0] = localYouTubeInitializationResult1;
    YouTubeInitializationResult localYouTubeInitializationResult2 = INTERNAL_ERROR;
    arrayOfYouTubeInitializationResult[1] = localYouTubeInitializationResult2;
    YouTubeInitializationResult localYouTubeInitializationResult3 = UNKNOWN_ERROR;
    arrayOfYouTubeInitializationResult[2] = localYouTubeInitializationResult3;
    YouTubeInitializationResult localYouTubeInitializationResult4 = SERVICE_MISSING;
    arrayOfYouTubeInitializationResult[3] = localYouTubeInitializationResult4;
    YouTubeInitializationResult localYouTubeInitializationResult5 = SERVICE_VERSION_UPDATE_REQUIRED;
    arrayOfYouTubeInitializationResult[4] = localYouTubeInitializationResult5;
    YouTubeInitializationResult localYouTubeInitializationResult6 = SERVICE_DISABLED;
    arrayOfYouTubeInitializationResult[5] = localYouTubeInitializationResult6;
    YouTubeInitializationResult localYouTubeInitializationResult7 = SERVICE_INVALID;
    arrayOfYouTubeInitializationResult[6] = localYouTubeInitializationResult7;
    YouTubeInitializationResult localYouTubeInitializationResult8 = ERROR_CONNECTING_TO_SERVICE;
    arrayOfYouTubeInitializationResult[7] = localYouTubeInitializationResult8;
    YouTubeInitializationResult localYouTubeInitializationResult9 = CLIENT_LIBRARY_UPDATE_REQUIRED;
    arrayOfYouTubeInitializationResult[8] = localYouTubeInitializationResult9;
    YouTubeInitializationResult localYouTubeInitializationResult10 = NETWORK_ERROR;
    arrayOfYouTubeInitializationResult[9] = localYouTubeInitializationResult10;
    YouTubeInitializationResult localYouTubeInitializationResult11 = DEVELOPER_KEY_INVALID;
    arrayOfYouTubeInitializationResult[10] = localYouTubeInitializationResult11;
    YouTubeInitializationResult localYouTubeInitializationResult12 = INVALID_APPLICATION_SIGNATURE;
    arrayOfYouTubeInitializationResult[11] = localYouTubeInitializationResult12;
  }

  public final Dialog getErrorDialog(Activity paramActivity, int paramInt)
  {
    return getErrorDialog(paramActivity, paramInt, null);
  }

  public final Dialog getErrorDialog(Activity paramActivity, int paramInt, DialogInterface.OnCancelListener paramOnCancelListener)
  {
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(paramActivity);
    if (paramOnCancelListener != null)
      AlertDialog.Builder localBuilder2 = localBuilder1.setOnCancelListener(paramOnCancelListener);
    int[] arrayOfInt1 = 1.a;
    int i = ordinal();
    Intent localIntent;
    switch (arrayOfInt1[i])
    {
    default:
      localIntent = null;
    case 1:
    case 3:
    case 2:
    }
    a locala;
    m localm;
    while (true)
    {
      locala = new a(paramActivity, localIntent, paramInt);
      localm = new m(paramActivity);
      int[] arrayOfInt2 = 1.a;
      int j = ordinal();
      switch (arrayOfInt2[j])
      {
      default:
        StringBuilder localStringBuilder = new StringBuilder("Unexpected errorReason: ");
        String str1 = name();
        String str2 = str1;
        throw new IllegalArgumentException(str2);
        localIntent = z.b(z.a(paramActivity));
        continue;
        localIntent = z.a(z.a(paramActivity));
      case 1:
      case 2:
      case 3:
      }
    }
    String str3 = localm.b;
    AlertDialog.Builder localBuilder3 = localBuilder1.setTitle(str3);
    String str4 = localm.c;
    AlertDialog.Builder localBuilder4 = localBuilder3.setMessage(str4);
    String str5 = localm.d;
    AlertDialog localAlertDialog = localBuilder4.setPositiveButton(str5, locala).create();
    while (true)
    {
      return localAlertDialog;
      String str6 = localm.e;
      AlertDialog.Builder localBuilder5 = localBuilder1.setTitle(str6);
      String str7 = localm.f;
      AlertDialog.Builder localBuilder6 = localBuilder5.setMessage(str7);
      String str8 = localm.g;
      localAlertDialog = localBuilder6.setPositiveButton(str8, locala).create();
      continue;
      String str9 = localm.h;
      AlertDialog.Builder localBuilder7 = localBuilder1.setTitle(str9);
      String str10 = localm.i;
      AlertDialog.Builder localBuilder8 = localBuilder7.setMessage(str10);
      String str11 = localm.j;
      localAlertDialog = localBuilder8.setPositiveButton(str11, locala).create();
    }
  }

  public final boolean isUserRecoverableError()
  {
    int[] arrayOfInt = 1.a;
    int i = ordinal();
    switch (arrayOfInt[i])
    {
    default:
    case 1:
    case 2:
    case 3:
    }
    for (boolean bool = false; ; bool = true)
      return bool;
  }

  private static final class a
    implements DialogInterface.OnClickListener
  {
    private final Activity a;
    private final Intent b;
    private final int c;

    public a(Activity paramActivity, Intent paramIntent, int paramInt)
    {
      Activity localActivity = (Activity)ac.a(paramActivity, "activity cannot be null");
      this.a = localActivity;
      Intent localIntent = (Intent)ac.a(paramIntent, "intent cannot be null");
      this.b = localIntent;
      int i = ((Integer)ac.a(Integer.valueOf(paramInt), "requestCode cannot be null")).intValue();
      this.c = i;
    }

    public final void onClick(DialogInterface paramDialogInterface, int paramInt)
    {
      try
      {
        Activity localActivity = this.a;
        Intent localIntent = this.b;
        int i = this.c;
        localActivity.startActivityForResult(localIntent, i);
        paramDialogInterface.dismiss();
        return;
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = localActivityNotFoundException;
        String str = String.format("Can't perform resolution for YouTubeInitalizationError", arrayOfObject);
        int j = Log.e("YouTubeAndroidPlayerAPI", str);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.YouTubeInitializationResult
 * JD-Core Version:    0.6.2
 */