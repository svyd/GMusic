package com.google.android.music.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.google.android.music.NautilusStatus;
import com.google.android.music.cloudclient.OfferJson;
import com.google.android.music.cloudclient.OffersResponseJson;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.purchase.Finsky;
import com.google.android.music.store.Store;
import com.google.android.music.utils.SystemUtils;

public class TutorialUtils
{
  static void disableTutorial(MusicPreferences paramMusicPreferences)
  {
    paramMusicPreferences.setTutorialViewed(true);
  }

  static void finishTutorialPermanently(Activity paramActivity, boolean paramBoolean, MusicPreferences paramMusicPreferences)
  {
    paramMusicPreferences.setTutorialViewed(true);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(paramActivity);
    Object[] arrayOfObject = new Object[0];
    localMusicEventLogger.trackEvent("signUpFinishedPerm", arrayOfObject);
    TutorialFinishActivity.finishTutorialPermanently(paramActivity, paramBoolean);
    paramActivity.finish();
  }

  static void finishTutorialTemporarily(Activity paramActivity)
  {
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(paramActivity);
    Object[] arrayOfObject = new Object[0];
    localMusicEventLogger.trackEvent("signUpFinishedTemporary", arrayOfObject);
    TutorialFinishActivity.finishTutorialTemporary(paramActivity);
    paramActivity.finish();
  }

  public static boolean isDatabaseUpdated(MusicPreferences paramMusicPreferences)
  {
    int i = paramMusicPreferences.getDatabaseVersion();
    int j = Store.getDatabaseVersion();
    if (i != j);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static boolean launchTutorialOnDemand(Activity paramActivity)
  {
    boolean bool = false;
    MusicEventLogger localMusicEventLogger1 = MusicEventLogger.getInstance(paramActivity);
    Object[] arrayOfObject1 = new Object[bool];
    localMusicEventLogger1.trackEvent("signUpOnDemand", arrayOfObject1);
    if (!SystemUtils.isConnectedToNetwork(paramActivity))
    {
      MusicEventLogger localMusicEventLogger2 = MusicEventLogger.getInstance(paramActivity);
      Object[] arrayOfObject2 = new Object[0];
      localMusicEventLogger2.trackEvent("signUpNoNetworkError", arrayOfObject2);
      int i = Log.i("Tutorial", "Skipping tutorial - no connectivity");
    }
    while (true)
    {
      return bool;
      Intent localIntent1 = new Intent(paramActivity, TutorialLaunchActivity.class);
      Intent localIntent2 = localIntent1.addFlags(335544320);
      Intent localIntent3 = localIntent1.putExtra("forceTutorial", true);
      paramActivity.startActivity(localIntent1);
      paramActivity.finish();
      bool = true;
    }
  }

  public static boolean launchTutorialOnStartupIfNeeded(Activity paramActivity, MusicPreferences paramMusicPreferences)
  {
    boolean bool = false;
    if ((paramMusicPreferences.wasTutorialViewed()) && (isDatabaseUpdated(paramMusicPreferences)));
    while (true)
    {
      return bool;
      MusicEventLogger localMusicEventLogger1 = MusicEventLogger.getInstance(paramActivity);
      Object[] arrayOfObject1 = new Object[0];
      localMusicEventLogger1.trackEvent("signUpOnLaunch", arrayOfObject1);
      if (!SystemUtils.isConnectedToNetwork(paramActivity))
      {
        MusicEventLogger localMusicEventLogger2 = MusicEventLogger.getInstance(paramActivity);
        Object[] arrayOfObject2 = new Object[0];
        localMusicEventLogger2.trackEvent("signUpNoNetworkError", arrayOfObject2);
        int i = Log.i("Tutorial", "No connectivity");
        if (isDatabaseUpdated(paramMusicPreferences));
      }
      else
      {
        Intent localIntent1 = new Intent(paramActivity, TutorialLaunchActivity.class);
        Intent localIntent2 = localIntent1.addFlags(335544320);
        paramActivity.startActivity(localIntent1);
        paramActivity.finish();
        bool = true;
      }
    }
  }

  public static boolean launchTutorialToChooseAccount(Activity paramActivity, boolean paramBoolean)
  {
    Intent localIntent = prepareChangeAccountIntent(paramActivity, paramBoolean);
    if (localIntent == null);
    for (boolean bool = false; ; bool = true)
    {
      return bool;
      paramActivity.startActivity(localIntent);
    }
  }

  public static boolean launchTutorialToChooseAccountForResult(Activity paramActivity, boolean paramBoolean, int paramInt)
  {
    Intent localIntent = prepareChangeAccountIntent(paramActivity, paramBoolean);
    if (localIntent == null);
    for (boolean bool = false; ; bool = true)
    {
      return bool;
      paramActivity.startActivityForResult(localIntent, paramInt);
    }
  }

  static void openConfirmNautilusActivity(Activity paramActivity)
  {
    openTutorialActivity(TutorialConfirmNautilusActivity.class, "signUpConfirmNautilus", paramActivity);
  }

  static void openOtherWaysToPlayActivity(Activity paramActivity, MusicPreferences paramMusicPreferences)
  {
    disableTutorial(paramMusicPreferences);
    openTutorialActivity(TutorialOtherWaysToPlayActivity.class, "signUpAddMusic", paramActivity);
  }

  static void openSelectAccountActivity(Activity paramActivity, boolean paramBoolean)
  {
    Intent localIntent1 = new Intent(paramActivity, TutorialSelectAccountActivity.class);
    Intent localIntent2 = localIntent1.putExtra("firstTimeTutorial", paramBoolean);
    openTutorialActivity(localIntent1, "signUpSelectAccount", paramActivity);
  }

  private static void openTryNautilusActivity(OfferJson paramOfferJson, Activity paramActivity)
  {
    Intent localIntent = new Intent(paramActivity, TutorialTryNautilusActivity.class);
    TutorialTryNautilusActivity.putOfferIntoIntent(paramOfferJson, localIntent);
    openTutorialActivity(localIntent, "signUpTryNautilus", paramActivity);
  }

  static void openTryNautilusOrFinishTutorial(OffersResponseJson paramOffersResponseJson, Activity paramActivity, MusicPreferences paramMusicPreferences)
  {
    int i = 0;
    boolean bool = paramOffersResponseJson.isSignedUpForNautilus();
    if (bool)
    {
      NautilusStatus localNautilusStatus = NautilusStatus.GOT_NAUTILUS;
      paramMusicPreferences.setTempNautilusStatus(localNautilusStatus);
      paramMusicPreferences.updateNautilusTimestamp();
    }
    while (true)
    {
      if (i != 0)
        return;
      finishTutorialPermanently(paramActivity, bool, paramMusicPreferences);
      return;
      if (paramOffersResponseJson.canSignupForNautilus())
        if (Finsky.isDirectPurchaseAvailable(paramActivity, paramMusicPreferences))
        {
          i = 1;
          openTryNautilusActivity(paramOffersResponseJson.getDefaultOffer(), paramActivity);
        }
        else
        {
          MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(paramActivity);
          Object[] arrayOfObject = new Object[0];
          localMusicEventLogger.trackEvent("signUpIncompatibleStoreAppError", arrayOfObject);
        }
    }
  }

  static void openTutorialActivity(Intent paramIntent, String paramString, Activity paramActivity)
  {
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(paramActivity);
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "signUpScreenName";
    arrayOfObject[1] = paramString;
    localMusicEventLogger.trackEvent("signUpNewScreen", arrayOfObject);
    Intent localIntent = paramIntent.addFlags(335544320);
    paramActivity.startActivity(paramIntent);
    paramActivity.finish();
  }

  static void openTutorialActivity(Class<?> paramClass, String paramString, Activity paramActivity)
  {
    openTutorialActivity(new Intent(paramActivity, paramClass), paramString, paramActivity);
  }

  private static Intent prepareChangeAccountIntent(Activity paramActivity, boolean paramBoolean)
  {
    Intent localIntent1;
    if (!SystemUtils.isConnectedToNetwork(paramActivity))
    {
      MusicEventLogger localMusicEventLogger1 = MusicEventLogger.getInstance(paramActivity);
      Object[] arrayOfObject1 = new Object[0];
      localMusicEventLogger1.trackEvent("signUpNoNetworkError", arrayOfObject1);
      int i = Log.i("Tutorial", "Skipping tutorial - no connectivity");
      localIntent1 = null;
    }
    while (true)
    {
      return localIntent1;
      MusicEventLogger localMusicEventLogger2 = MusicEventLogger.getInstance(paramActivity);
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = "signUpScreenName";
      arrayOfObject2[1] = "signUpSelectAccount";
      localMusicEventLogger2.trackEvent("signUpNewScreen", arrayOfObject2);
      localIntent1 = new Intent(paramActivity, TutorialSelectAccountActivity.class);
      Intent localIntent2 = localIntent1.putExtra("firstTimeTutorial", false);
      Intent localIntent3 = localIntent1.putExtra("changeAccountOnly", paramBoolean);
      Intent localIntent4 = localIntent1.addFlags(335544320);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.tutorial.TutorialUtils
 * JD-Core Version:    0.6.2
 */