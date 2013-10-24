package com.google.android.music.sync.google;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.android.gsf.Gservices;
import com.google.android.music.sync.common.AuthInfo;
import java.io.IOException;

public class MusicAuthInfo
  implements AuthInfo
{
  private final Context mContext;

  public MusicAuthInfo(Context paramContext)
  {
    this.mContext = paramContext;
  }

  public static String getAuthTokenType(Context paramContext)
  {
    return Gservices.getString(paramContext.getContentResolver(), "music_auth_token", "sj");
  }

  public String getAuthToken(Account paramAccount)
    throws AuthenticatorException
  {
    if (paramAccount == null)
    {
      Throwable localThrowable = new Throwable();
      int i = Log.e("MusicAuthInfo", "Given null account to MusicAuthInfo.getAuthToken()", localThrowable);
      throw new AuthenticatorException("Given null account to MusicAuthInfo.getAuthToken()");
    }
    String str1 = getAuthTokenType(this.mContext);
    String str2;
    try
    {
      str2 = AccountManager.get(this.mContext).blockingGetAuthToken(paramAccount, str1, true);
      if (str2 == null)
        throw new AuthenticatorException("Received null auth token.");
    }
    catch (OperationCanceledException localOperationCanceledException)
    {
      throw new AuthenticatorException(localOperationCanceledException);
    }
    catch (IOException localIOException)
    {
      throw new AuthenticatorException(localIOException);
    }
    return str2;
  }

  public AccountManagerFuture<Bundle> getAuthTokenForeground(Activity paramActivity, Account paramAccount, AccountManagerCallback<Bundle> paramAccountManagerCallback, Handler paramHandler)
    throws AuthenticatorException
  {
    String str = Gservices.getString(this.mContext.getContentResolver(), "music_auth_token", "sj");
    AccountManager localAccountManager = AccountManager.get(this.mContext);
    Bundle localBundle = new Bundle();
    Account localAccount = paramAccount;
    Activity localActivity = paramActivity;
    AccountManagerCallback<Bundle> localAccountManagerCallback = paramAccountManagerCallback;
    Handler localHandler = paramHandler;
    AccountManagerFuture localAccountManagerFuture = localAccountManager.getAuthToken(localAccount, str, localBundle, localActivity, localAccountManagerCallback, localHandler);
    if (localAccountManagerFuture == null)
      throw new AuthenticatorException("Received null auth token.");
    return localAccountManagerFuture;
  }

  public void invalidateAuthToken(Account paramAccount, String paramString)
  {
    String str1 = Gservices.getString(this.mContext.getContentResolver(), "music_auth_token", "sj");
    AccountManager localAccountManager = AccountManager.get(this.mContext);
    String str2 = paramAccount.type;
    localAccountManager.invalidateAuthToken(str2, paramString);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.MusicAuthInfo
 * JD-Core Version:    0.6.2
 */