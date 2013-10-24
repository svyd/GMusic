package com.mobeta.android.dslv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;

public class SimpleFloatViewManager
  implements DragSortListView.FloatViewManager
{
  private int mFloatBGColor = -16777216;
  private Bitmap mFloatBitmap;
  private ImageView mImageView;
  private ListView mListView;

  public SimpleFloatViewManager(ListView paramListView)
  {
    this.mListView = paramListView;
  }

  public View onCreateFloatView(int paramInt)
  {
    ListView localListView = this.mListView;
    int i = this.mListView.getHeaderViewsCount() + paramInt;
    int j = this.mListView.getFirstVisiblePosition();
    int k = i - j;
    View localView = localListView.getChildAt(k);
    if (localView == null);
    for (Object localObject = null; ; localObject = this.mImageView)
    {
      return localObject;
      localView.setPressed(false);
      localView.setDrawingCacheEnabled(true);
      Bitmap localBitmap1 = Bitmap.createBitmap(localView.getDrawingCache());
      this.mFloatBitmap = localBitmap1;
      localView.setDrawingCacheEnabled(false);
      if (this.mImageView == null)
      {
        Context localContext = this.mListView.getContext();
        ImageView localImageView1 = new ImageView(localContext);
        this.mImageView = localImageView1;
      }
      ImageView localImageView2 = this.mImageView;
      int m = this.mFloatBGColor;
      localImageView2.setBackgroundColor(m);
      this.mImageView.setPadding(0, 0, 0, 0);
      ImageView localImageView3 = this.mImageView;
      Bitmap localBitmap2 = this.mFloatBitmap;
      localImageView3.setImageBitmap(localBitmap2);
      ImageView localImageView4 = this.mImageView;
      int n = localView.getWidth();
      int i1 = localView.getHeight();
      ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(n, i1);
      localImageView4.setLayoutParams(localLayoutParams);
    }
  }

  public void onDestroyFloatView(View paramView)
  {
    ((ImageView)paramView).setImageDrawable(null);
    this.mFloatBitmap.recycle();
    this.mFloatBitmap = null;
  }

  public void onDragFloatView(View paramView, Point paramPoint1, Point paramPoint2)
  {
  }

  public void setBackgroundColor(int paramInt)
  {
    this.mFloatBGColor = paramInt;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.mobeta.android.dslv.SimpleFloatViewManager
 * JD-Core Version:    0.6.2
 */