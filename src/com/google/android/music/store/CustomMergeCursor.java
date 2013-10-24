package com.google.android.music.store;

import android.database.Cursor;
import android.database.MergeCursor;

public class CustomMergeCursor extends MergeCursor
{
  public CustomMergeCursor(Cursor[] paramArrayOfCursor)
  {
    super(paramArrayOfCursor);
  }

  public boolean requery()
  {
    this.mPos = -1;
    return super.requery();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.CustomMergeCursor
 * JD-Core Version:    0.6.2
 */