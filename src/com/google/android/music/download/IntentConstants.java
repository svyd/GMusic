package com.google.android.music.download;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.google.android.gsf.Gservices;
import com.google.android.music.preferences.MusicPreferences;
import java.util.List;

public class IntentConstants
{
  public static Intent getMusicStoreIntent(MusicPreferences paramMusicPreferences)
  {
    Intent localIntent1 = new Intent("android.intent.action.VIEW");
    Intent localIntent2 = localIntent1.setPackage("com.android.vending");
    Uri localUri = Uri.parse("https://play.google.com/store/music/");
    Intent localIntent3 = localIntent1.setData(localUri);
    Intent localIntent4 = localIntent1.addFlags(268435456);
    Intent localIntent5 = localIntent1.addFlags(32768);
    Intent localIntent6 = localIntent1.addFlags(524288);
    Account localAccount = paramMusicPreferences.getSyncAccount();
    if (localAccount != null)
    {
      String str1 = localAccount.name;
      Intent localIntent7 = localIntent1.putExtra("authAccount", str1);
      String str2 = localAccount.type;
      Intent localIntent8 = localIntent1.putExtra("accountType", str2);
    }
    return localIntent1;
  }

  public static Intent getShopForArtistIntent(Context paramContext, String paramString)
  {
    String str1 = Gservices.getString(paramContext.getApplicationContext().getContentResolver(), "music_shop_url");
    if (str1 == null)
      str1 = "https://market.android.com/search?q=%s&c=music&featured=MUSIC_STORE_SEARCH";
    Object[] arrayOfObject = new Object[1];
    String str2 = Uri.encode(paramString);
    arrayOfObject[0] = str2;
    Uri localUri = Uri.parse(String.format(str1, arrayOfObject));
    Intent localIntent1 = new Intent("android.intent.action.VIEW", localUri);
    Intent localIntent2 = localIntent1.setPackage("com.android.vending");
    Intent localIntent3 = localIntent1.addFlags(268435456);
    Intent localIntent4 = localIntent1.addFlags(32768);
    Intent localIntent5 = localIntent1.addFlags(524288);
    if (paramContext.getPackageManager().resolveActivity(localIntent1, 65536) == null)
      Intent localIntent6 = localIntent1.setPackage(null);
    return localIntent1;
  }

  public static Intent getStoreBuyIntent(Context paramContext, String paramString)
  {
    int i = 0;
    Intent localIntent1 = new Intent("android.intent.action.VIEW");
    Intent localIntent2 = localIntent1.setPackage("com.android.vending");
    Uri localUri = Uri.parse(paramString);
    Intent localIntent3 = localIntent1.setData(localUri);
    List localList = paramContext.getPackageManager().queryIntentActivities(localIntent1, i);
    if ((localList != null) && (localList.size() != 0))
      i = 1;
    if (i == 0)
      Intent localIntent4 = localIntent1.setPackage(null);
    return localIntent1;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.IntentConstants
 * JD-Core Version:    0.6.2
 */