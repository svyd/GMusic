package android.support.v4.widget;

import android.database.Cursor;
import android.widget.Filter;
import android.widget.Filter.FilterResults;

class CursorFilter extends Filter
{
  CursorFilterClient mClient;

  CursorFilter(CursorFilterClient paramCursorFilterClient)
  {
    this.mClient = paramCursorFilterClient;
  }

  public CharSequence convertResultToString(Object paramObject)
  {
    CursorFilterClient localCursorFilterClient = this.mClient;
    Cursor localCursor = (Cursor)paramObject;
    return localCursorFilterClient.convertToString(localCursor);
  }

  protected Filter.FilterResults performFiltering(CharSequence paramCharSequence)
  {
    Cursor localCursor = this.mClient.runQueryOnBackgroundThread(paramCharSequence);
    Filter.FilterResults localFilterResults = new Filter.FilterResults();
    if (localCursor != null)
    {
      int i = localCursor.getCount();
      localFilterResults.count = i;
    }
    for (localFilterResults.values = localCursor; ; localFilterResults.values = null)
    {
      return localFilterResults;
      localFilterResults.count = 0;
    }
  }

  protected void publishResults(CharSequence paramCharSequence, Filter.FilterResults paramFilterResults)
  {
    Cursor localCursor1 = this.mClient.getCursor();
    if (paramFilterResults.values == null)
      return;
    if (paramFilterResults.values == localCursor1)
      return;
    CursorFilterClient localCursorFilterClient = this.mClient;
    Cursor localCursor2 = (Cursor)paramFilterResults.values;
    localCursorFilterClient.changeCursor(localCursor2);
  }

  static abstract interface CursorFilterClient
  {
    public abstract void changeCursor(Cursor paramCursor);

    public abstract CharSequence convertToString(Cursor paramCursor);

    public abstract Cursor getCursor();

    public abstract Cursor runQueryOnBackgroundThread(CharSequence paramCharSequence);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.widget.CursorFilter
 * JD-Core Version:    0.6.2
 */