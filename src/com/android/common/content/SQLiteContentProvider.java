package com.android.common.content;

import android.content.ContentProvider;
import android.database.sqlite.SQLiteTransactionListener;

public abstract class SQLiteContentProvider extends ContentProvider
  implements SQLiteTransactionListener
{
  private final ThreadLocal<Boolean> mApplyingBatch;

  public SQLiteContentProvider()
  {
    ThreadLocal localThreadLocal = new ThreadLocal();
    this.mApplyingBatch = localThreadLocal;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.android.common.content.SQLiteContentProvider
 * JD-Core Version:    0.6.2
 */