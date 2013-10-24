package com.google.android.music.purchase;

import android.accounts.Account;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Log;
import com.google.android.music.download.IntentConstants;
import com.google.android.music.preferences.MusicPreferences;

public class Finsky
{
  public static boolean doesSupportNautilusCancelation(Context paramContext, MusicPreferences paramMusicPreferences)
  {
    if ((getPlayStoreVersion(paramContext) > 80210000) && (isDirectPurchaseAvailable(paramContext, paramMusicPreferences)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static Uri getBuyAlbumUri(Context paramContext, String paramString1, String paramString2)
  {
    Uri.Builder localBuilder1 = Uri.parse("https://play.google.com/store/music/").buildUpon();
    Uri.Builder localBuilder2 = localBuilder1.appendPath("album");
    Uri.Builder localBuilder3 = localBuilder1.appendQueryParameter("id", paramString1);
    if (paramString2 != null)
    {
      Uri.Builder localBuilder4 = localBuilder1.appendQueryParameter("pcampaignid", "android_music_buy_track");
      String str = "song-" + paramString2;
      Uri.Builder localBuilder5 = localBuilder1.appendQueryParameter("tid", str);
    }
    while (true)
    {
      return localBuilder1.build();
      Uri.Builder localBuilder6 = localBuilder1.appendQueryParameter("pcampaignid", "android_music_buy_album");
    }
  }

  private static int getPlayStoreVersion(Context paramContext)
  {
    PackageManager localPackageManager = paramContext.getPackageManager();
    try
    {
      i = localPackageManager.getPackageInfo("com.android.vending", 0).versionCode;
      return i;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      while (true)
      {
        int j = Log.e("MusicFinsky", "Failed to get store app version", localNameNotFoundException);
        int i = -1;
      }
    }
  }

  public static boolean isDirectPurchaseAvailable(Context paramContext, MusicPreferences paramMusicPreferences)
  {
    int i = 0;
    Intent localIntent = makeDirectPurchaseIntent(paramMusicPreferences);
    if (paramContext.getPackageManager().resolveActivity(localIntent, i) != null)
      i = 1;
    return i;
  }

  public static boolean isPlayStoreAvailable(Context paramContext, MusicPreferences paramMusicPreferences)
  {
    int i = 0;
    Intent localIntent = IntentConstants.getMusicStoreIntent(paramMusicPreferences);
    if (paramContext.getPackageManager().resolveActivity(localIntent, i) != null)
      i = 1;
    return i;
  }

  private static Intent makeCancelNautilusIntent()
  {
    Intent localIntent1 = new Intent("android.intent.action.VIEW");
    Intent localIntent2 = localIntent1.addFlags(268435456);
    Intent localIntent3 = localIntent1.addFlags(32768);
    Intent localIntent4 = localIntent1.addFlags(524288);
    Uri localUri = Uri.parse("https://market.android.com/details?id=com.google.android.music&rdid=com.google.android.music&rdot=1");
    Intent localIntent5 = localIntent1.setData(localUri);
    Intent localIntent6 = localIntent1.setPackage("com.android.vending");
    return localIntent1;
  }

  private static Intent makeDirectPurchaseIntent(MusicPreferences paramMusicPreferences)
  {
    Intent localIntent1 = new Intent("com.android.vending.billing.PURCHASE");
    Intent localIntent2 = localIntent1.addCategory("android.intent.category.DEFAULT");
    Intent localIntent3 = localIntent1.setPackage("com.android.vending");
    Intent localIntent4 = localIntent1.putExtra("backend", 2);
    Intent localIntent5 = localIntent1.putExtra("offer_type", 1);
    Account localAccount = paramMusicPreferences.getSyncAccount();
    if (localAccount != null)
    {
      String str = localAccount.name;
      Intent localIntent6 = localIntent1.putExtra("authAccount", str);
    }
    return localIntent1;
  }

  private static Intent makeDirectPurchaseIntent(MusicPreferences paramMusicPreferences, int paramInt, String paramString1, String paramString2)
  {
    Intent localIntent1 = makeDirectPurchaseIntent(paramMusicPreferences);
    Intent localIntent2 = localIntent1.putExtra("document_type", 15);
    Intent localIntent3 = localIntent1.putExtra("backend_docid", paramString1);
    Intent localIntent4 = localIntent1.putExtra("full_docid", paramString2);
    return localIntent1;
  }

  private static Intent makeNautilusPurchaseIntent(MusicPreferences paramMusicPreferences, String paramString)
  {
    String str = "music-subscription_" + paramString;
    return makeDirectPurchaseIntent(paramMusicPreferences, 15, paramString, str);
  }

  public static boolean startCancelNautilusActivity(Activity paramActivity)
  {
    try
    {
      Intent localIntent = makeCancelNautilusIntent();
      paramActivity.startActivity(localIntent);
      bool = true;
      return bool;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      while (true)
        boolean bool = false;
    }
  }

  public static boolean startNautilusPurchaseActivityForResult(Activity paramActivity, MusicPreferences paramMusicPreferences, int paramInt, String paramString)
  {
    try
    {
      Intent localIntent = makeNautilusPurchaseIntent(paramMusicPreferences, paramString);
      paramActivity.startActivityForResult(localIntent, paramInt);
      bool = true;
      return bool;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      while (true)
        boolean bool = false;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.purchase.Finsky
 * JD-Core Version:    0.6.2
 */