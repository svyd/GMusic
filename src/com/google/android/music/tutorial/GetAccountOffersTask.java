package com.google.android.music.tutorial;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.google.android.music.cloudclient.MusicCloud;
import com.google.android.music.cloudclient.MusicCloudImpl;
import com.google.android.music.cloudclient.OffersResponseJson;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.utils.GoogleEduUtils;
import com.google.android.music.utils.LoggableHandler;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;

public class GetAccountOffersTask
{
  private final Account mAccount;
  private final Runnable mAccountDisabledCallbackRunnable;
  private LoggableHandler mBackgroundHandler;
  private final Runnable mBackgroundRunnable;
  private Handler mCallbackHandler;
  private final Runnable mCallbackRunnable;
  private Callbacks mCallbacks;
  private final Context mContext;
  private volatile boolean mIsCanceled = false;
  private OffersResponseJson mResponse;

  GetAccountOffersTask(Context paramContext, Account paramAccount, Callbacks paramCallbacks)
  {
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        if (GetAccountOffersTask.this.mIsCanceled)
          return;
        MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(GetAccountOffersTask.this.mContext);
        if (GoogleEduUtils.isEduDevice(GetAccountOffersTask.this.mContext))
        {
          Handler localHandler1 = GetAccountOffersTask.this.mCallbackHandler;
          Runnable localRunnable1 = GetAccountOffersTask.this.mAccountDisabledCallbackRunnable;
          boolean bool1 = localHandler1.post(localRunnable1);
          Object[] arrayOfObject1 = new Object[0];
          localMusicEventLogger.trackEvent("signUpGoogleEduDevice", arrayOfObject1);
          return;
        }
        OffersResponseJson localOffersResponseJson1 = GetAccountOffersTask.access$402(GetAccountOffersTask.this, null);
        String str1 = "";
        Context localContext = GetAccountOffersTask.this.mContext;
        MusicCloudImpl localMusicCloudImpl = new MusicCloudImpl(localContext);
        try
        {
          GetAccountOffersTask localGetAccountOffersTask = GetAccountOffersTask.this;
          Account localAccount = GetAccountOffersTask.this.mAccount;
          OffersResponseJson localOffersResponseJson2 = localMusicCloudImpl.getOffersForAccount(localAccount);
          OffersResponseJson localOffersResponseJson3 = GetAccountOffersTask.access$402(localGetAccountOffersTask, localOffersResponseJson2);
          if (GetAccountOffersTask.this.mResponse != null)
          {
            Object[] arrayOfObject2 = new Object[2];
            arrayOfObject2[0] = "signUpOffersResponse";
            String str2 = GetAccountOffersTask.this.mResponse.toString();
            arrayOfObject2[1] = str2;
            localMusicEventLogger.trackEvent("signUpGetOffersSuccess", arrayOfObject2);
            if (!GetAccountOffersTask.this.mResponse.isValidMusicAccount())
            {
              Object[] arrayOfObject3 = new Object[0];
              localMusicEventLogger.trackEvent("signUpInvalidMusicAccountError", arrayOfObject3);
            }
            if (GetAccountOffersTask.this.mIsCanceled)
              return;
            Handler localHandler2 = GetAccountOffersTask.this.mCallbackHandler;
            Runnable localRunnable2 = GetAccountOffersTask.this.mCallbackRunnable;
            boolean bool2 = localHandler2.post(localRunnable2);
            return;
          }
        }
        catch (IOException localIOException)
        {
          while (true)
          {
            int i = Log.e("AccountOffers", "getOffersFailed", localIOException);
            str1 = localIOException.getMessage();
          }
        }
        catch (Exception localException)
        {
          while (true)
          {
            int j = Log.e("AccountOffers", "getOffersFailed", localException);
            str1 = localException.getMessage();
            continue;
            Object[] arrayOfObject4 = new Object[2];
            arrayOfObject4[0] = "signUpErrorDetail";
            arrayOfObject4[1] = str1;
            localMusicEventLogger.trackEvent("signUpGetOffersError", arrayOfObject4);
          }
        }
      }
    };
    this.mBackgroundRunnable = local1;
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        if (GetAccountOffersTask.this.mIsCanceled)
          return;
        while (true)
        {
          try
          {
            if (GetAccountOffersTask.this.mResponse != null)
            {
              if (!GetAccountOffersTask.this.mResponse.isValidMusicAccount())
              {
                GetAccountOffersTask.Callbacks localCallbacks1 = GetAccountOffersTask.this.mCallbacks;
                Account localAccount1 = GetAccountOffersTask.this.mAccount;
                localCallbacks1.onAccountInvalid(localAccount1);
                return;
              }
              GetAccountOffersTask.Callbacks localCallbacks2 = GetAccountOffersTask.this.mCallbacks;
              Account localAccount2 = GetAccountOffersTask.this.mAccount;
              OffersResponseJson localOffersResponseJson = GetAccountOffersTask.this.mResponse;
              localCallbacks2.onAccountOffersSuccess(localAccount2, localOffersResponseJson);
              continue;
            }
          }
          finally
          {
            GetAccountOffersTask.this.cleanup();
          }
          GetAccountOffersTask.Callbacks localCallbacks3 = GetAccountOffersTask.this.mCallbacks;
          Account localAccount3 = GetAccountOffersTask.this.mAccount;
          localCallbacks3.onAccountOffersError(localAccount3);
        }
      }
    };
    this.mCallbackRunnable = local2;
    Runnable local3 = new Runnable()
    {
      public void run()
      {
        if (GetAccountOffersTask.this.mIsCanceled)
          return;
        try
        {
          GetAccountOffersTask.Callbacks localCallbacks = GetAccountOffersTask.this.mCallbacks;
          Account localAccount = GetAccountOffersTask.this.mAccount;
          localCallbacks.onAccountDisabled(localAccount);
          return;
        }
        finally
        {
          GetAccountOffersTask.this.cleanup();
        }
      }
    };
    this.mAccountDisabledCallbackRunnable = local3;
    this.mContext = paramContext;
    this.mAccount = paramAccount;
    this.mCallbacks = paramCallbacks;
  }

  private void cleanup()
  {
    if (this.mBackgroundHandler != null)
    {
      this.mBackgroundHandler.quit();
      this.mBackgroundHandler = null;
    }
    this.mCallbacks = null;
  }

  static Account[] getAvailableAccounts(Context paramContext)
  {
    Account[] arrayOfAccount1 = AccountManager.get(paramContext).getAccountsByType("com.google");
    ArrayList localArrayList;
    Account[] arrayOfAccount3;
    if ((arrayOfAccount1 != null) && (arrayOfAccount1.length != 0))
    {
      localArrayList = Lists.newArrayList();
      Account[] arrayOfAccount2 = arrayOfAccount1;
      int i = arrayOfAccount2.length;
      int j = 0;
      while (j < i)
      {
        Account localAccount = arrayOfAccount2[j];
        if (!localAccount.name.endsWith("@youtube.com"))
          boolean bool = localArrayList.add(localAccount);
        j += 1;
      }
      int k = localArrayList.size();
      int m = arrayOfAccount1.length;
      if (k != m)
      {
        if (localArrayList.size() <= 0)
          break label120;
        arrayOfAccount3 = new Account[localArrayList.size()];
      }
    }
    label120: for (arrayOfAccount1 = (Account[])localArrayList.toArray(arrayOfAccount3); ; arrayOfAccount1 = null)
      return arrayOfAccount1;
  }

  void cancel()
  {
    this.mIsCanceled = true;
    if ((this.mBackgroundHandler != null) && (this.mBackgroundRunnable != null))
    {
      LoggableHandler localLoggableHandler = this.mBackgroundHandler;
      Runnable localRunnable1 = this.mBackgroundRunnable;
      localLoggableHandler.removeCallbacks(localRunnable1);
    }
    if ((this.mCallbackHandler != null) && (this.mCallbackRunnable != null))
    {
      Handler localHandler = this.mCallbackHandler;
      Runnable localRunnable2 = this.mCallbackRunnable;
      localHandler.removeCallbacks(localRunnable2);
    }
    cleanup();
  }

  void run()
  {
    if (this.mCallbackHandler != null)
      throw new IllegalStateException("AccountSelectionTask can only run once");
    Handler localHandler = new Handler();
    this.mCallbackHandler = localHandler;
    LoggableHandler localLoggableHandler1 = new LoggableHandler("AccountSelection");
    this.mBackgroundHandler = localLoggableHandler1;
    LoggableHandler localLoggableHandler2 = this.mBackgroundHandler;
    Runnable localRunnable = this.mBackgroundRunnable;
    boolean bool = localLoggableHandler2.post(localRunnable);
  }

  static abstract interface Callbacks
  {
    public abstract void onAccountDisabled(Account paramAccount);

    public abstract void onAccountInvalid(Account paramAccount);

    public abstract void onAccountOffersError(Account paramAccount);

    public abstract void onAccountOffersSuccess(Account paramAccount, OffersResponseJson paramOffersResponseJson);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.tutorial.GetAccountOffersTask
 * JD-Core Version:    0.6.2
 */