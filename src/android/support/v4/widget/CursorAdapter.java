package android.support.v4.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;

public abstract class CursorAdapter extends BaseAdapter
  implements CursorFilter.CursorFilterClient, Filterable
{
  protected boolean mAutoRequery;
  protected ChangeObserver mChangeObserver;
  protected Context mContext;
  protected Cursor mCursor;
  protected CursorFilter mCursorFilter;
  protected DataSetObserver mDataSetObserver;
  protected boolean mDataValid;
  protected FilterQueryProvider mFilterQueryProvider;
  protected int mRowIDColumn;

  @Deprecated
  public CursorAdapter(Context paramContext, Cursor paramCursor)
  {
    init(paramContext, paramCursor, 1);
  }

  public CursorAdapter(Context paramContext, Cursor paramCursor, int paramInt)
  {
    init(paramContext, paramCursor, paramInt);
  }

  public CursorAdapter(Context paramContext, Cursor paramCursor, boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = 1; ; i = 2)
    {
      init(paramContext, paramCursor, i);
      return;
    }
  }

  public abstract void bindView(View paramView, Context paramContext, Cursor paramCursor);

  public void changeCursor(Cursor paramCursor)
  {
    Cursor localCursor = swapCursor(paramCursor);
    if (localCursor == null)
      return;
    localCursor.close();
  }

  public CharSequence convertToString(Cursor paramCursor)
  {
    if (paramCursor == null);
    for (String str = ""; ; str = paramCursor.toString())
      return str;
  }

  public int getCount()
  {
    if ((this.mDataValid) && (this.mCursor != null));
    for (int i = this.mCursor.getCount(); ; i = 0)
      return i;
  }

  public Cursor getCursor()
  {
    return this.mCursor;
  }

  public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView;
    if (this.mDataValid)
    {
      boolean bool = this.mCursor.moveToPosition(paramInt);
      if (paramView == null)
      {
        Context localContext1 = this.mContext;
        Cursor localCursor1 = this.mCursor;
        localView = newDropDownView(localContext1, localCursor1, paramViewGroup);
        Context localContext2 = this.mContext;
        Cursor localCursor2 = this.mCursor;
        bindView(localView, localContext2, localCursor2);
      }
    }
    while (true)
    {
      return localView;
      localView = paramView;
      break;
      localView = null;
    }
  }

  public Filter getFilter()
  {
    if (this.mCursorFilter == null)
    {
      CursorFilter localCursorFilter = new CursorFilter(this);
      this.mCursorFilter = localCursorFilter;
    }
    return this.mCursorFilter;
  }

  public Object getItem(int paramInt)
  {
    if ((this.mDataValid) && (this.mCursor != null))
      boolean bool = this.mCursor.moveToPosition(paramInt);
    for (Cursor localCursor = this.mCursor; ; localCursor = null)
      return localCursor;
  }

  public long getItemId(int paramInt)
  {
    long l = 0L;
    if ((this.mDataValid) && (this.mCursor != null) && (this.mCursor.moveToPosition(paramInt)))
    {
      Cursor localCursor = this.mCursor;
      int i = this.mRowIDColumn;
      l = localCursor.getLong(i);
    }
    return l;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (!this.mDataValid)
      throw new IllegalStateException("this should only be called when the cursor is valid");
    if (!this.mCursor.moveToPosition(paramInt))
    {
      String str = "couldn't move cursor to position " + paramInt;
      throw new IllegalStateException(str);
    }
    Context localContext1;
    Cursor localCursor1;
    if (paramView == null)
    {
      localContext1 = this.mContext;
      localCursor1 = this.mCursor;
    }
    for (View localView = newView(localContext1, localCursor1, paramViewGroup); ; localView = paramView)
    {
      Context localContext2 = this.mContext;
      Cursor localCursor2 = this.mCursor;
      bindView(localView, localContext2, localCursor2);
      return localView;
    }
  }

  public boolean hasStableIds()
  {
    return true;
  }

  void init(Context paramContext, Cursor paramCursor, int paramInt)
  {
    int i = 1;
    label24: int j;
    label55: MyDataSetObserver localMyDataSetObserver;
    if ((paramInt & 0x1) != i)
    {
      paramInt |= 2;
      this.mAutoRequery = true;
      if (paramCursor == null)
        break label159;
      this.mCursor = paramCursor;
      this.mDataValid = i;
      this.mContext = paramContext;
      if (i == 0)
        break label165;
      j = paramCursor.getColumnIndexOrThrow("_id");
      this.mRowIDColumn = j;
      if ((paramInt & 0x2) != 2)
        break label172;
      ChangeObserver localChangeObserver1 = new ChangeObserver();
      this.mChangeObserver = localChangeObserver1;
      localMyDataSetObserver = new MyDataSetObserver(null);
    }
    for (this.mDataSetObserver = localMyDataSetObserver; ; this.mDataSetObserver = null)
    {
      if (i == 0)
        return;
      if (this.mChangeObserver != null)
      {
        ChangeObserver localChangeObserver2 = this.mChangeObserver;
        paramCursor.registerContentObserver(localChangeObserver2);
      }
      if (this.mDataSetObserver == null)
        return;
      DataSetObserver localDataSetObserver = this.mDataSetObserver;
      paramCursor.registerDataSetObserver(localDataSetObserver);
      return;
      this.mAutoRequery = false;
      break;
      label159: i = 0;
      break label24;
      label165: j = -1;
      break label55;
      label172: this.mChangeObserver = null;
    }
  }

  public View newDropDownView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
  {
    return newView(paramContext, paramCursor, paramViewGroup);
  }

  public abstract View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup);

  protected void onContentChanged()
  {
    if (!this.mAutoRequery)
      return;
    if (this.mCursor == null)
      return;
    if (this.mCursor.isClosed())
      return;
    boolean bool = this.mCursor.requery();
    this.mDataValid = bool;
  }

  public Cursor runQueryOnBackgroundThread(CharSequence paramCharSequence)
  {
    if (this.mFilterQueryProvider != null);
    for (Cursor localCursor = this.mFilterQueryProvider.runQuery(paramCharSequence); ; localCursor = this.mCursor)
      return localCursor;
  }

  public Cursor swapCursor(Cursor paramCursor)
  {
    Cursor localCursor1 = this.mCursor;
    Cursor localCursor2;
    if (paramCursor == localCursor1)
      localCursor2 = null;
    while (true)
    {
      return localCursor2;
      localCursor2 = this.mCursor;
      if (localCursor2 != null)
      {
        if (this.mChangeObserver != null)
        {
          ChangeObserver localChangeObserver1 = this.mChangeObserver;
          localCursor2.unregisterContentObserver(localChangeObserver1);
        }
        if (this.mDataSetObserver != null)
        {
          DataSetObserver localDataSetObserver1 = this.mDataSetObserver;
          localCursor2.unregisterDataSetObserver(localDataSetObserver1);
        }
      }
      this.mCursor = paramCursor;
      if (paramCursor != null)
      {
        if (this.mChangeObserver != null)
        {
          ChangeObserver localChangeObserver2 = this.mChangeObserver;
          paramCursor.registerContentObserver(localChangeObserver2);
        }
        if (this.mDataSetObserver != null)
        {
          DataSetObserver localDataSetObserver2 = this.mDataSetObserver;
          paramCursor.registerDataSetObserver(localDataSetObserver2);
        }
        int i = paramCursor.getColumnIndexOrThrow("_id");
        this.mRowIDColumn = i;
        this.mDataValid = true;
        notifyDataSetChanged();
      }
      else
      {
        this.mRowIDColumn = -1;
        this.mDataValid = false;
        notifyDataSetInvalidated();
      }
    }
  }

  private class MyDataSetObserver extends DataSetObserver
  {
    private MyDataSetObserver()
    {
    }

    public void onChanged()
    {
      CursorAdapter.this.mDataValid = true;
      CursorAdapter.this.notifyDataSetChanged();
    }

    public void onInvalidated()
    {
      CursorAdapter.this.mDataValid = false;
      CursorAdapter.this.notifyDataSetInvalidated();
    }
  }

  private class ChangeObserver extends ContentObserver
  {
    public ChangeObserver()
    {
      super();
    }

    public boolean deliverSelfNotifications()
    {
      return true;
    }

    public void onChange(boolean paramBoolean)
    {
      CursorAdapter.this.onContentChanged();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.widget.CursorAdapter
 * JD-Core Version:    0.6.2
 */