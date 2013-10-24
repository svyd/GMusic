package com.google.android.music.ringtone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MarkerView extends ImageView
{
  private MarkerListener mListener;
  private int mVelocity;

  public MarkerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setFocusable(true);
    this.mVelocity = 0;
    this.mListener = null;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (this.mListener == null)
      return;
    this.mListener.markerDraw();
  }

  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    if ((paramBoolean) && (this.mListener != null))
      this.mListener.markerFocus(this);
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = true;
    int i = this.mVelocity + 1;
    this.mVelocity = i;
    int j = (int)Math.sqrt(this.mVelocity / 2 + 1);
    if (this.mListener != null)
      if (paramInt == 21)
        this.mListener.markerLeft(this, j);
    while (true)
    {
      return bool;
      if (paramInt == 22)
        this.mListener.markerRight(this, j);
      else if (paramInt == 23)
        this.mListener.markerEnter(this);
      else
        bool = super.onKeyDown(paramInt, paramKeyEvent);
    }
  }

  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    this.mVelocity = 0;
    if (this.mListener != null)
      this.mListener.markerKeyUp();
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = true;
    switch (paramMotionEvent.getAction())
    {
    default:
    case 0:
    case 2:
    case 1:
    }
    while (true)
    {
      return bool1;
      boolean bool2 = requestFocus();
      MarkerListener localMarkerListener1 = this.mListener;
      float f1 = paramMotionEvent.getRawX();
      float f2 = paramMotionEvent.getY();
      bool1 = localMarkerListener1.markerTouchStart(this, f1, f2);
      continue;
      MarkerListener localMarkerListener2 = this.mListener;
      float f3 = paramMotionEvent.getRawX();
      float f4 = paramMotionEvent.getY();
      bool1 = localMarkerListener2.markerTouchMove(this, f3, f4);
      continue;
      MarkerListener localMarkerListener3 = this.mListener;
      float f5 = paramMotionEvent.getY();
      bool1 = localMarkerListener3.markerTouchEnd(this, f5);
    }
  }

  public void setListener(MarkerListener paramMarkerListener)
  {
    this.mListener = paramMarkerListener;
  }

  public static abstract interface MarkerListener
  {
    public abstract void markerDraw();

    public abstract void markerEnter(MarkerView paramMarkerView);

    public abstract void markerFocus(MarkerView paramMarkerView);

    public abstract void markerKeyUp();

    public abstract void markerLeft(MarkerView paramMarkerView, int paramInt);

    public abstract void markerRight(MarkerView paramMarkerView, int paramInt);

    public abstract boolean markerTouchEnd(MarkerView paramMarkerView, float paramFloat);

    public abstract boolean markerTouchMove(MarkerView paramMarkerView, float paramFloat1, float paramFloat2);

    public abstract boolean markerTouchStart(MarkerView paramMarkerView, float paramFloat1, float paramFloat2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ringtone.MarkerView
 * JD-Core Version:    0.6.2
 */