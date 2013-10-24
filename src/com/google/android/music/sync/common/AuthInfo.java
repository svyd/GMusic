package com.google.android.music.sync.common;

import android.accounts.Account;
import android.accounts.AuthenticatorException;

public abstract interface AuthInfo
{
  public abstract String getAuthToken(Account paramAccount)
    throws AuthenticatorException;

  public abstract void invalidateAuthToken(Account paramAccount, String paramString)
    throws AuthenticatorException;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.common.AuthInfo
 * JD-Core Version:    0.6.2
 */