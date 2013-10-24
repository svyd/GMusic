package com.google.android.music.utils;

import android.database.AbstractCursor;
import android.database.CharArrayBuffer;
import android.database.CrossProcessCursor;
import android.database.CursorWindow;
import android.os.Bundle;

public abstract class RequeriableCursorWrapper extends AbstractCursor
{
  private volatile CrossProcessCursor mInner;

  public RequeriableCursorWrapper(CrossProcessCursor paramCrossProcessCursor)
  {
    this.mInner = paramCrossProcessCursor;
  }

  private void setInnerCursor(CrossProcessCursor paramCrossProcessCursor)
  {
    if (paramCrossProcessCursor == null)
      throw new NullPointerException("Inner cursor cannot be null");
    if (this.mInner != paramCrossProcessCursor)
    {
      this.mInner.close();
      this.mInner = paramCrossProcessCursor;
    }
    int i = this.mInner.getPosition();
    this.mPos = i;
  }

  public final void close()
  {
    this.mInner.close();
    super.close();
  }

  public final void copyStringToBuffer(int paramInt, CharArrayBuffer paramCharArrayBuffer)
  {
    this.mInner.copyStringToBuffer(paramInt, paramCharArrayBuffer);
  }

  public final void deactivate()
  {
    this.mInner.deactivate();
    super.deactivate();
  }

  public final void fillWindow(int paramInt, CursorWindow paramCursorWindow)
  {
    this.mInner.fillWindow(paramInt, paramCursorWindow);
  }

  public final byte[] getBlob(int paramInt)
  {
    return this.mInner.getBlob(paramInt);
  }

  public final String getColumnName(int paramInt)
  {
    return this.mInner.getColumnName(paramInt);
  }

  public final String[] getColumnNames()
  {
    return this.mInner.getColumnNames();
  }

  public final int getCount()
  {
    return this.mInner.getCount();
  }

  public final double getDouble(int paramInt)
  {
    return this.mInner.getDouble(paramInt);
  }

  public final Bundle getExtras()
  {
    return this.mInner.getExtras();
  }

  public final float getFloat(int paramInt)
  {
    return this.mInner.getFloat(paramInt);
  }

  public final int getInt(int paramInt)
  {
    return this.mInner.getInt(paramInt);
  }

  public final long getLong(int paramInt)
  {
    return this.mInner.getLong(paramInt);
  }

  public final short getShort(int paramInt)
  {
    return this.mInner.getShort(paramInt);
  }

  public final String getString(int paramInt)
  {
    return this.mInner.getString(paramInt);
  }

  public final int getType(int paramInt)
  {
    return this.mInner.getType(paramInt);
  }

  protected abstract CrossProcessCursor getUpdatedCursor();

  public final boolean getWantsAllOnMoveCalls()
  {
    return true;
  }

  public final CursorWindow getWindow()
  {
    return this.mInner.getWindow();
  }

  public final boolean isNull(int paramInt)
  {
    return this.mInner.isNull(paramInt);
  }

  public final boolean onMove(int paramInt1, int paramInt2)
  {
    return this.mInner.moveToPosition(paramInt2);
  }

  public final boolean requery()
  {
    CrossProcessCursor localCrossProcessCursor = getUpdatedCursor();
    if (localCrossProcessCursor == null);
    for (boolean bool = false; ; bool = super.requery())
    {
      return bool;
      setInnerCursor(localCrossProcessCursor);
    }
  }

  public final Bundle respond(Bundle paramBundle)
  {
    return this.mInner.respond(paramBundle);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.RequeriableCursorWrapper
 * JD-Core Version:    0.6.2
 */