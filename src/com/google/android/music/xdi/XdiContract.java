package com.google.android.music.xdi;

import android.content.Intent;
import android.net.Uri;

public final class XdiContract
{
  public static Intent getBrowseIntent(Uri paramUri, int paramInt)
  {
    Intent localIntent1 = new Intent("com.google.android.xdi.action.BROWSE");
    Intent localIntent2 = localIntent1.setData(paramUri);
    Intent localIntent3 = localIntent1.putExtra("start_index", paramInt);
    return localIntent1;
  }

  public static Intent getDetailsIntent(Uri paramUri)
  {
    Intent localIntent1 = new Intent("com.google.android.xdi.action.DETAIL");
    Intent localIntent2 = localIntent1.setData(paramUri);
    return localIntent1;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.XdiContract
 * JD-Core Version:    0.6.2
 */