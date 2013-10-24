package com.google.android.music.xdi;

import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Binder;

public abstract class XdiCursor extends AbstractCursor
{
  private final Context mContext;
  private Object[] mCurrentRow = null;
  private final ProjectionMap mProjectionMap;
  private final Cursor mWrappedCursor;

  public XdiCursor(Context paramContext, String[] paramArrayOfString, Cursor paramCursor)
  {
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    this.mWrappedCursor = paramCursor;
  }

  private boolean clearCallingIdentityAndExtractDataForCurrentRow(Object[] paramArrayOfObject)
  {
    long l = Binder.clearCallingIdentity();
    try
    {
      boolean bool1 = extractDataForCurrentRow(paramArrayOfObject);
      boolean bool2 = bool1;
      return bool2;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }

  private Object get(int paramInt)
  {
    if (this.mCurrentRow == null)
      throw new CursorIndexOutOfBoundsException("Not on a row");
    return this.mCurrentRow[paramInt];
  }

  public void close()
  {
    super.close();
    this.mCurrentRow = null;
    if (this.mWrappedCursor == null)
      return;
    this.mWrappedCursor.close();
  }

  protected Object[] createArrayForRow()
  {
    return new Object[this.mProjectionMap.getColumnNames().length];
  }

  public void deactivate()
  {
    super.deactivate();
    this.mCurrentRow = null;
    if (this.mWrappedCursor == null)
      return;
    this.mWrappedCursor.deactivate();
  }

  protected abstract boolean extractDataForCurrentRow(Object[] paramArrayOfObject);

  public String[] getColumnNames()
  {
    return this.mProjectionMap.getColumnNames();
  }

  protected Context getContext()
  {
    return this.mContext;
  }

  public int getCount()
  {
    if (this.mWrappedCursor == null);
    for (int i = 0; ; i = this.mWrappedCursor.getCount())
      return i;
  }

  public double getDouble(int paramInt)
  {
    Object localObject = get(paramInt);
    double d;
    if (localObject == null)
      d = 0.0D;
    while (true)
    {
      return d;
      if ((localObject instanceof Number))
        d = ((Number)localObject).doubleValue();
      else
        d = Double.parseDouble(localObject.toString());
    }
  }

  public float getFloat(int paramInt)
  {
    Object localObject = get(paramInt);
    float f;
    if (localObject == null)
      f = 0.0F;
    while (true)
    {
      return f;
      if ((localObject instanceof Number))
        f = ((Number)localObject).floatValue();
      else
        f = Float.parseFloat(localObject.toString());
    }
  }

  public int getInt(int paramInt)
  {
    Object localObject = get(paramInt);
    int i;
    if (localObject == null)
      i = 0;
    while (true)
    {
      return i;
      if ((localObject instanceof Number))
        i = ((Number)localObject).intValue();
      else
        i = Integer.parseInt(localObject.toString());
    }
  }

  public long getLong(int paramInt)
  {
    Object localObject = get(paramInt);
    long l;
    if (localObject == null)
      l = 0L;
    while (true)
    {
      return l;
      if ((localObject instanceof Number))
        l = ((Number)localObject).longValue();
      else
        l = Long.parseLong(localObject.toString());
    }
  }

  public short getShort(int paramInt)
  {
    Object localObject = get(paramInt);
    short s;
    if (localObject == null)
      s = 0;
    while (true)
    {
      return s;
      if ((localObject instanceof Number))
        s = ((Number)localObject).shortValue();
      else
        s = Short.parseShort(localObject.toString());
    }
  }

  public String getString(int paramInt)
  {
    Object localObject = get(paramInt);
    if (localObject == null);
    for (String str = null; ; str = localObject.toString())
      return str;
  }

  protected Cursor getWrappedCursor()
  {
    return this.mWrappedCursor;
  }

  public boolean isNull(int paramInt)
  {
    if (get(paramInt) == null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean onMove(int paramInt1, int paramInt2)
  {
    if ((this.mWrappedCursor != null) && (!this.mWrappedCursor.moveToPosition(paramInt2)));
    Object[] arrayOfObject2;
    for (boolean bool = false; ; bool = clearCallingIdentityAndExtractDataForCurrentRow(arrayOfObject2))
    {
      return bool;
      Object[] arrayOfObject1 = createArrayForRow();
      this.mCurrentRow = arrayOfObject1;
      arrayOfObject2 = this.mCurrentRow;
    }
  }

  protected void writeValueToArray(Object[] paramArrayOfObject, String paramString, Object paramObject)
  {
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, paramString, paramObject);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.XdiCursor
 * JD-Core Version:    0.6.2
 */