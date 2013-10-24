package com.google.android.music.ui.cardlib.layout;

import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

public class PopupSelector extends Dialog
{
  private final int mAnchorX;
  private final int mAnchorY;
  private final LegacyPopupMenu.PopupListAdapter mListAdapter;
  private ListView mListView;
  private ViewGroup mMeasureParent;

  public PopupSelector(Context paramContext, View paramView, LegacyPopupMenu.PopupListAdapter paramPopupListAdapter)
  {
    super(paramContext, 2131755077);
    int[] arrayOfInt = new int[2];
    paramView.getLocationOnScreen(arrayOfInt);
    int i = arrayOfInt[0];
    this.mAnchorX = i;
    int j = arrayOfInt[1];
    int k = paramView.getHeight();
    int m = j + k;
    this.mAnchorY = m;
    this.mListAdapter = paramPopupListAdapter;
  }

  private void layoutListView()
  {
    Window localWindow = getWindow();
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    localWindow.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
    int i = localDisplayMetrics.widthPixels;
    View localView = findViewById(2131296500);
    int j = measureContentWidth();
    int k = this.mListView.getPaddingLeft();
    int m = j + k;
    int n = this.mListView.getPaddingRight();
    int i1 = m + n;
    int i2 = i * 4 / 5;
    int i3 = View.MeasureSpec.makeMeasureSpec(Math.min(i1, i2), 1073741824);
    localView.measure(i3, -1);
    int i4 = localView.getMeasuredWidth();
    int i5 = localView.getMeasuredHeight();
    localView.layout(0, 0, i4, i5);
    WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
    int i6 = localView.getMeasuredWidth();
    localLayoutParams.width = i6;
    int i7 = localView.getMeasuredHeight();
    localLayoutParams.height = i7;
    int i8 = this.mAnchorX;
    localLayoutParams.x = i8;
    int i9 = this.mAnchorY;
    localLayoutParams.y = i9;
    localLayoutParams.gravity = 51;
    int i10 = localLayoutParams.flags | 0x20100;
    localLayoutParams.flags = i10;
    localWindow.setAttributes(localLayoutParams);
  }

  private int measureContentWidth()
  {
    int i = 0;
    View localView = null;
    int j = 0;
    int k = View.MeasureSpec.makeMeasureSpec(0, 0);
    int m = View.MeasureSpec.makeMeasureSpec(0, 0);
    int n = this.mListAdapter.getCount();
    int i1 = 0;
    while (i1 < n)
    {
      int i2 = this.mListAdapter.getItemViewType(i1);
      if (i2 != j)
      {
        j = i2;
        localView = null;
      }
      if (this.mMeasureParent == null)
      {
        Context localContext = getContext();
        FrameLayout localFrameLayout = new FrameLayout(localContext);
        this.mMeasureParent = localFrameLayout;
      }
      LegacyPopupMenu.PopupListAdapter localPopupListAdapter = this.mListAdapter;
      ViewGroup localViewGroup = this.mMeasureParent;
      localView = localPopupListAdapter.getView(i1, localView, localViewGroup);
      localView.measure(k, m);
      int i3 = localView.getMeasuredWidth();
      i = Math.max(i, i3);
      i1 += 1;
    }
    return i;
  }

  protected void onCreate(Bundle paramBundle)
  {
    setContentView(2130968668);
    ListView localListView1 = (ListView)findViewById(2131296501);
    this.mListView = localListView1;
    ListView localListView2 = this.mListView;
    AdapterView.OnItemClickListener local1 = new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        if (paramAnonymousInt < 0)
          return;
        PopupSelector.this.dismiss();
        PopupSelector.this.mListAdapter.onSelect(paramAnonymousInt);
      }
    };
    localListView2.setOnItemClickListener(local1);
    if (this.mListAdapter != null)
    {
      ListView localListView3 = this.mListView;
      LegacyPopupMenu.PopupListAdapter localPopupListAdapter1 = this.mListAdapter;
      localListView3.setAdapter(localPopupListAdapter1);
      LegacyPopupMenu.PopupListAdapter localPopupListAdapter2 = this.mListAdapter;
      DataSetObserver local2 = new DataSetObserver()
      {
        public void onChanged()
        {
          PopupSelector.this.layoutListView();
        }
      };
      localPopupListAdapter2.registerDataSetObserver(local2);
    }
    layoutListView();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = super.onKeyDown(paramInt, paramKeyEvent);
    if (bool);
    while (true)
    {
      return bool;
      if (paramInt == 19)
      {
        cancel();
        bool = true;
      }
      else
      {
        bool = false;
      }
    }
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 0)
      cancel();
    for (boolean bool = true; ; bool = false)
      return bool;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.layout.PopupSelector
 * JD-Core Version:    0.6.2
 */