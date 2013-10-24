package com.google.android.music.ringtone;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.WindowManager;
import com.google.android.music.ringtone.soundfile.CheapSoundFile;

public class WaveformView extends View
{
  private static int HANDLE_TIMECODE_OFFSET_X = 10;
  private RingtoneEditActivity mActivity;
  private Paint mBlackBgPaint;
  private Paint mBorderLinePaint;
  private final float mDensity;
  private boolean mEnableZoom = true;
  private GestureDetector mGestureDetector;
  private Paint mGridPaint;
  private Paint mHandleTimecodePaint;
  private boolean mInitialized;
  private WaveformListener mListener;
  private Paint mNoWaveBgPaint;
  private int mOffset;
  private Paint mPlaybackLinePaint;
  private int mPlaybackPos;
  private int mSampleRate;
  private int mSamplesPerFrame;
  private Paint mScaleCoverPaint;
  private final ScaleGestureDetector mScaleDetector;
  private Paint mScaleLinePaint;
  private Paint mSelectedLinePaint;
  private int mSelectionEnd;
  private int mSelectionStart;
  private CheapSoundFile mSoundFile;
  private Paint mTimecodePaint;
  private Paint mUnselectedBkgndLinePaint;
  private Paint mUnselectedLinePaint;
  private int mWaveLength;
  private double[] mWaveValues;
  private double mZoomFactor;
  final int sBorderYOffset;

  public WaveformView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    ((Activity)paramContext).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
    float f1 = localDisplayMetrics.density;
    this.mDensity = f1;
    float f2 = this.mDensity;
    int i = (int)(25.0F * f2);
    this.sBorderYOffset = i;
    setFocusable(false);
    setupPaints(paramContext);
    GestureDetector.SimpleOnGestureListener local1 = new GestureDetector.SimpleOnGestureListener()
    {
      public boolean onDoubleTap(MotionEvent paramAnonymousMotionEvent)
      {
        WaveformView.WaveformListener localWaveformListener = WaveformView.this.mListener;
        float f1 = paramAnonymousMotionEvent.getX();
        float f2 = paramAnonymousMotionEvent.getY();
        localWaveformListener.waveformDoubleClick(f1, f2);
        return true;
      }

      public boolean onFling(MotionEvent paramAnonymousMotionEvent1, MotionEvent paramAnonymousMotionEvent2, float paramAnonymousFloat1, float paramAnonymousFloat2)
      {
        WaveformView.this.mListener.waveformFling(paramAnonymousFloat1);
        return true;
      }
    };
    GestureDetector localGestureDetector = new GestureDetector(paramContext, local1);
    this.mGestureDetector = localGestureDetector;
    ScaleListener localScaleListener = new ScaleListener(null);
    ScaleGestureDetector localScaleGestureDetector = new ScaleGestureDetector(paramContext, localScaleListener);
    this.mScaleDetector = localScaleGestureDetector;
    this.mSoundFile = null;
    this.mWaveValues = null;
    this.mOffset = 0;
    this.mPlaybackPos = -1;
    this.mSelectionStart = 0;
    this.mSelectionEnd = 0;
    this.mInitialized = false;
  }

  private void computeAndSmoothWaveValues()
  {
    int i = this.mSoundFile.getNumFrames();
    int[] arrayOfInt1 = this.mSoundFile.getFrameGains();
    double[] arrayOfDouble1 = new double[i];
    int j = 1;
    if (i == j)
    {
      double d1 = arrayOfInt1[0];
      arrayOfDouble1[0] = d1;
    }
    long l1;
    while (true)
    {
      l1 = 4607182418800017408L;
      int m = 0;
      while (m < i)
      {
        if (arrayOfDouble1[m] > l1)
          l1 = arrayOfDouble1[m];
        m += 1;
      }
      int k = 2;
      if (i == k)
      {
        double d2 = arrayOfInt1[0];
        arrayOfDouble1[0] = d2;
        double d3 = arrayOfInt1[1];
        arrayOfDouble1[1] = d3;
      }
      else
      {
        int n = 2;
        if (i > n)
        {
          double d4 = arrayOfInt1[0] / 2.0D;
          double d5 = arrayOfInt1[1] / 2.0D;
          double d6 = d4 + d5;
          arrayOfDouble1[0] = d6;
          int i1 = 1;
          while (true)
          {
            int i2 = i + -1;
            if (i1 >= i2)
              break;
            int i3 = i1 + -1;
            double d7 = arrayOfInt1[i3] / 3.0D;
            double d8 = arrayOfInt1[i1] / 3.0D;
            double d9 = d7 + d8;
            int i4 = i1 + 1;
            double d10 = arrayOfInt1[i4] / 3.0D;
            double d11 = d9 + d10;
            arrayOfDouble1[i1] = d11;
            i1 += 1;
          }
          int i5 = i + -1;
          int i6 = i + -2;
          double d12 = arrayOfInt1[i6] / 2.0D;
          int i7 = i + -1;
          double d13 = arrayOfInt1[i7] / 2.0D;
          double d14 = d12 + d13;
          arrayOfDouble1[i5] = d14;
        }
      }
    }
    double d15 = 1.0D;
    if (l1 > 255.0D)
      d15 = 255.0D / l1;
    double d16 = 0.0D;
    int[] arrayOfInt2 = new int[256];
    int i8 = 0;
    while (i8 < i)
    {
      int i9 = (int)(arrayOfDouble1[i8] * d15);
      if (i9 < 0)
        i9 = 0;
      int i10 = i9;
      int i11 = 255;
      if (i10 > i11)
        i9 = 255;
      if (i9 > d16)
        d16 = i9;
      int i12 = arrayOfInt2[i9] + 1;
      arrayOfInt2[i9] = i12;
      i8 += 1;
    }
    double d17 = 0.0D;
    int i13 = 0;
    while (d17 < 255.0D)
    {
      int i14 = i / 20;
      int i15 = i13;
      int i16 = i14;
      if (i15 >= i16)
        break;
      int i17 = (int)d17;
      int i18 = arrayOfInt2[i17];
      i13 += i18;
      d17 += 1.0D;
    }
    int i19 = 0;
    while (d16 > 2.0D)
    {
      int i20 = i / 100;
      int i21 = i19;
      int i22 = i20;
      if (i21 >= i22)
        break;
      int i23 = (int)d16;
      int i24 = arrayOfInt2[i23];
      i19 += i24;
      d16 -= 1.0D;
    }
    double[] arrayOfDouble2 = new double[i];
    double d18 = d16 - d17;
    int i25 = 0;
    while (i25 < i)
    {
      double d19 = (arrayOfDouble1[i25] * d15 - d17) / d18;
      if (d19 < 0.0D)
        d19 = 0.0D;
      if (d19 > 1.0D)
        d19 = 1.0D;
      float f = d19 * d19;
      arrayOfDouble2[i25] = f;
      i25 += 1;
    }
    this.mWaveLength = i;
    double[] arrayOfDouble3 = new double[this.mWaveLength];
    this.mWaveValues = arrayOfDouble3;
    double d20 = 1.0D;
    this.mZoomFactor = d20;
    int i26 = 0;
    while (true)
    {
      int i27 = this.mWaveLength;
      if (i26 >= i27)
        break;
      double[] arrayOfDouble4 = this.mWaveValues;
      long l2 = arrayOfDouble2[i26];
      arrayOfDouble4[i26] = l2;
      i26 += 1;
    }
    boolean bool = true;
    this.mInitialized = bool;
  }

  private void drawBackground(Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    if (this.mOffset < 0)
      i = -this.mOffset;
    int j = paramInt1;
    if (paramInt3 < paramInt1)
      j = paramInt3;
    if (i > 0)
    {
      float f1 = this.sBorderYOffset;
      float f2 = i;
      int k = this.sBorderYOffset;
      float f3 = paramInt2 - k;
      Paint localPaint1 = this.mNoWaveBgPaint;
      paramCanvas.drawRect(0.0F, f1, f2, f3, localPaint1);
    }
    float f4 = i;
    float f5 = this.sBorderYOffset;
    float f6 = j;
    int m = this.sBorderYOffset;
    float f7 = paramInt2 - m;
    Paint localPaint2 = this.mUnselectedBkgndLinePaint;
    paramCanvas.drawRect(f4, f5, f6, f7, localPaint2);
    int n = this.mSelectionStart;
    int i1 = this.mOffset;
    float f8 = n - i1;
    float f9 = this.sBorderYOffset;
    int i2 = this.mSelectionEnd;
    int i3 = this.mOffset;
    float f10 = i2 - i3;
    int i4 = this.sBorderYOffset;
    float f11 = paramInt2 - i4;
    Paint localPaint3 = this.mGridPaint;
    paramCanvas.drawRect(f8, f9, f10, f11, localPaint3);
    if (j >= paramInt1)
      return;
    float f12 = j;
    float f13 = this.sBorderYOffset;
    float f14 = paramInt1;
    int i5 = this.sBorderYOffset;
    float f15 = paramInt2 - i5;
    Paint localPaint4 = this.mNoWaveBgPaint;
    paramCanvas.drawRect(f12, f13, f14, f15, localPaint4);
  }

  private void drawHandleTimeCode(Canvas paramCanvas)
  {
    double d = pixelsToSeconds(1);
    int i = (int)(this.mSelectionStart * d);
    String str1 = getTimecodeFromSeconds(i);
    int j = (int)this.mHandleTimecodePaint.getTextSize();
    int k = this.mSelectionStart;
    int m = this.mOffset;
    int n = k - m - j;
    int i1 = HANDLE_TIMECODE_OFFSET_X;
    float f1 = n - i1;
    int i2 = waveTop();
    int i3 = j * 2;
    float f2 = i2 + i3;
    Paint localPaint1 = this.mHandleTimecodePaint;
    paramCanvas.drawText(str1, f1, f2, localPaint1);
    int i4 = (int)(this.mSelectionEnd * d);
    String str2 = getTimecodeFromSeconds(i4);
    int i5 = this.mSelectionEnd;
    int i6 = this.mOffset;
    int i7 = i5 - i6 + j;
    int i8 = HANDLE_TIMECODE_OFFSET_X;
    float f3 = i7 + i8;
    int i9 = waveTop();
    int i10 = j * 2;
    float f4 = i9 + i10;
    Paint localPaint2 = this.mHandleTimecodePaint;
    paramCanvas.drawText(str2, f3, f4, localPaint2);
  }

  private int getIntWaveValue(int paramInt)
  {
    int i = getHeight() / 2;
    int j = this.sBorderYOffset;
    int k = i - j + -1;
    double d1 = paramInt;
    double d2 = this.mZoomFactor;
    int m = (int)(d1 / d2);
    long l;
    double d3;
    if (m >= 0)
    {
      int n = this.mWaveLength;
      if (m < n)
      {
        l = this.mWaveValues[m];
        d3 = k;
      }
    }
    for (int i1 = (int)(l * d3); ; i1 = 0)
      return i1;
  }

  private String getTimecodeFromSeconds(int paramInt)
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append("");
    int i = paramInt / 60;
    String str1 = i;
    StringBuilder localStringBuilder2 = new StringBuilder().append("");
    int j = paramInt % 60;
    String str2 = j;
    if (paramInt % 60 < 10)
      str2 = "0" + str2;
    return str1 + ":" + str2;
  }

  private void setupPaints(Context paramContext)
  {
    Resources localResources = getResources();
    Paint localPaint1 = new Paint();
    this.mBlackBgPaint = localPaint1;
    Paint localPaint2 = this.mBlackBgPaint;
    int i = localResources.getColor(17170445);
    localPaint2.setColor(i);
    Paint localPaint3 = new Paint();
    this.mNoWaveBgPaint = localPaint3;
    Paint localPaint4 = this.mNoWaveBgPaint;
    int j = localResources.getColor(2131427343);
    localPaint4.setColor(j);
    Paint localPaint5 = new Paint();
    this.mGridPaint = localPaint5;
    Paint localPaint6 = this.mGridPaint;
    int k = localResources.getColor(2131427348);
    localPaint6.setColor(k);
    Paint localPaint7 = new Paint();
    this.mSelectedLinePaint = localPaint7;
    Paint localPaint8 = this.mSelectedLinePaint;
    int m = localResources.getColor(2131427344);
    localPaint8.setColor(m);
    Paint localPaint9 = new Paint();
    this.mUnselectedLinePaint = localPaint9;
    Paint localPaint10 = this.mUnselectedLinePaint;
    int n = localResources.getColor(2131427345);
    localPaint10.setColor(n);
    Paint localPaint11 = new Paint();
    this.mUnselectedBkgndLinePaint = localPaint11;
    Paint localPaint12 = this.mUnselectedBkgndLinePaint;
    int i1 = localResources.getColor(2131427346);
    localPaint12.setColor(i1);
    Paint localPaint13 = new Paint();
    this.mScaleCoverPaint = localPaint13;
    Paint localPaint14 = this.mScaleCoverPaint;
    int i2 = localResources.getColor(2131427350);
    localPaint14.setColor(i2);
    Paint localPaint15 = new Paint();
    this.mScaleLinePaint = localPaint15;
    this.mScaleLinePaint.setStrokeWidth(3.0F);
    Paint localPaint16 = this.mScaleLinePaint;
    int i3 = localResources.getColor(2131427352);
    localPaint16.setColor(i3);
    Paint localPaint17 = new Paint();
    this.mBorderLinePaint = localPaint17;
    this.mBorderLinePaint.setStrokeWidth(4.0F);
    Paint localPaint18 = this.mBorderLinePaint;
    int i4 = localResources.getColor(2131427351);
    localPaint18.setColor(i4);
    Paint localPaint19 = new Paint();
    this.mPlaybackLinePaint = localPaint19;
    Paint localPaint20 = this.mPlaybackLinePaint;
    int i5 = localResources.getColor(2131427347);
    localPaint20.setColor(i5);
    Paint localPaint21 = new Paint();
    this.mTimecodePaint = localPaint21;
    Paint localPaint22 = this.mTimecodePaint;
    float f1 = this.mDensity * 12.0F;
    localPaint22.setTextSize(f1);
    Paint localPaint23 = this.mTimecodePaint;
    Paint.Align localAlign1 = Paint.Align.CENTER;
    localPaint23.setTextAlign(localAlign1);
    this.mTimecodePaint.setAntiAlias(true);
    Paint localPaint24 = this.mTimecodePaint;
    int i6 = localResources.getColor(2131427349);
    localPaint24.setColor(i6);
    Paint localPaint25 = new Paint();
    this.mHandleTimecodePaint = localPaint25;
    Paint localPaint26 = this.mHandleTimecodePaint;
    float f2 = this.mDensity * 12.0F;
    localPaint26.setTextSize(f2);
    Paint localPaint27 = this.mHandleTimecodePaint;
    Paint.Align localAlign2 = Paint.Align.CENTER;
    localPaint27.setTextAlign(localAlign2);
    this.mHandleTimecodePaint.setAntiAlias(true);
    this.mHandleTimecodePaint.setFakeBoldText(true);
    Paint localPaint28 = this.mHandleTimecodePaint;
    int i7 = localResources.getColor(17170443);
    localPaint28.setColor(i7);
  }

  protected void drawWaveformLine(Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3, Paint paramPaint)
  {
    float f1 = paramInt1;
    float f2 = paramInt2;
    float f3 = paramInt1;
    float f4 = paramInt3;
    Canvas localCanvas = paramCanvas;
    Paint localPaint = paramPaint;
    localCanvas.drawLine(f1, f2, f3, f4, localPaint);
  }

  public int getEnd()
  {
    return this.mSelectionEnd;
  }

  public int getStart()
  {
    return this.mSelectionStart;
  }

  int getXFocusPoint(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getPointerCount();
    int j = 0;
    int k = 0;
    while (k < i)
    {
      float f1 = j;
      float f2 = paramMotionEvent.getX(k);
      j = (int)(f1 + f2);
      k += 1;
    }
    return j / i;
  }

  public double getZoomFactor()
  {
    return this.mZoomFactor;
  }

  public int getmaxPos()
  {
    double d1 = this.mWaveLength;
    double d2 = this.mZoomFactor;
    return (int)(d1 * d2);
  }

  public boolean isInitialized()
  {
    return this.mInitialized;
  }

  boolean isTouchInWaveRegion(float paramFloat)
  {
    float f1 = waveTop();
    if (paramFloat > f1)
    {
      float f2 = waveBottom();
      if (paramFloat < f2)
        break label30;
    }
    label30: for (boolean bool = false; ; bool = true)
      return bool;
  }

  public int millisecsToPixels(int paramInt)
  {
    double d1 = this.mZoomFactor;
    double d2 = paramInt * 1.0D;
    double d3 = this.mSampleRate;
    double d4 = d2 * d3 * d1;
    double d5 = this.mSamplesPerFrame;
    double d6 = 1000.0D * d5;
    return (int)Math.round(d4 / d6);
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (this.mSoundFile == null)
      return;
    if (this.mWaveValues == null)
      computeAndSmoothWaveValues();
    int i = getWidth();
    int j = getHeight();
    int k = this.mOffset;
    int m = getmaxPos() - k;
    int n = j / 2;
    int i1 = m;
    int i2 = i;
    if (i1 > i2)
      m = i;
    WaveformView localWaveformView1 = this;
    Canvas localCanvas1 = paramCanvas;
    int i3 = i;
    int i4 = j;
    int i5 = m;
    localWaveformView1.drawBackground(localCanvas1, i3, i4, i5);
    int i6 = 0;
    while (true)
    {
      int i7 = m;
      if (i6 >= i7)
        break label314;
      if (i6 + k >= 0)
        break;
      i6 += 1;
    }
    int i8 = i6 + k;
    int i9 = this.mSelectionStart;
    if (i8 >= i9)
    {
      int i10 = i6 + k;
      int i11 = this.mSelectionEnd;
      if (i10 >= i11);
    }
    for (Paint localPaint1 = this.mSelectedLinePaint; ; localPaint1 = this.mUnselectedLinePaint)
    {
      int i12 = k + i6;
      int i13 = getIntWaveValue(i12);
      int i14 = n - i13;
      int i15 = n + 1;
      int i16 = k + i6;
      int i17 = getIntWaveValue(i16);
      int i18 = i15 + i17;
      WaveformView localWaveformView2 = this;
      Canvas localCanvas2 = paramCanvas;
      localWaveformView2.drawWaveformLine(localCanvas2, i6, i14, i18, localPaint1);
      int i19 = i6 + k;
      int i20 = this.mPlaybackPos;
      if (i19 == i20)
        break;
      float f1 = i6;
      float f2 = i6;
      float f3 = j;
      Paint localPaint2 = this.mPlaybackLinePaint;
      paramCanvas.drawLine(f1, 0.0F, f2, f3, localPaint2);
      break;
    }
    label314: double d1 = pixelsToSeconds(1);
    double d2 = 1.0D;
    if (d2 / d1 < 50.0D)
      d2 = 5.0D;
    if (d2 / d1 < 50.0D)
      d2 = 15.0D;
    double d3 = this.mOffset * d1;
    int i21 = (int)d3;
    int i22 = (int)Math.floor(d3 / d2);
    int i23 = 0;
    while (true)
    {
      int i24 = m;
      if (i23 >= i24)
        break;
      i23 += 1;
      d3 += d1;
      if (this.mOffset + i23 >= 0)
      {
        int i25 = (int)d3;
        int i26 = (int)Math.floor(d3 / d2);
        int i27 = i26;
        int i28 = i22;
        if (i27 != i28)
        {
          i22 = i26;
          WaveformView localWaveformView3 = this;
          int i29 = i25;
          String str1 = localWaveformView3.getTimecodeFromSeconds(i29);
          float f4 = i23;
          float f5 = this.mTimecodePaint.getTextSize();
          float f6 = this.mDensity;
          float f7 = f5 * f6 / 2.0F + 5.0F;
          Paint localPaint3 = this.mTimecodePaint;
          Canvas localCanvas3 = paramCanvas;
          String str2 = str1;
          localCanvas3.drawText(str2, f4, f7, localPaint3);
        }
      }
    }
    if (this.mActivity.getMarkerTouched())
      drawHandleTimeCode(paramCanvas);
    if (this.mListener == null)
      return;
    this.mListener.waveformDraw();
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    float f1 = paramMotionEvent.getY();
    boolean bool1;
    if (!isTouchInWaveRegion(f1))
    {
      bool1 = false;
      return bool1;
    }
    int i = getXFocusPoint(paramMotionEvent);
    if (this.mEnableZoom)
      boolean bool2 = this.mScaleDetector.onTouchEvent(paramMotionEvent);
    boolean bool3 = this.mGestureDetector.onTouchEvent(paramMotionEvent);
    switch (paramMotionEvent.getAction())
    {
    default:
    case 0:
    case 2:
    case 1:
    }
    while (true)
    {
      bool1 = true;
      break;
      WaveformListener localWaveformListener1 = this.mListener;
      float f2 = i;
      localWaveformListener1.waveformTouchStart(f2);
      continue;
      WaveformListener localWaveformListener2 = this.mListener;
      float f3 = i;
      localWaveformListener2.waveformTouchMove(f3);
      continue;
      this.mListener.waveformTouchEnd();
    }
  }

  public int pixelsToMillisecs(int paramInt)
  {
    double d1 = this.mZoomFactor;
    double d2 = paramInt;
    double d3 = this.mSamplesPerFrame;
    double d4 = 1000.0D * d3;
    double d5 = d2 * d4;
    double d6 = this.mSampleRate * d1;
    return (int)Math.round(d5 / d6);
  }

  public double pixelsToSeconds(int paramInt)
  {
    double d1 = this.mZoomFactor;
    double d2 = paramInt;
    double d3 = this.mSamplesPerFrame;
    double d4 = d2 * d3;
    double d5 = this.mSampleRate * d1;
    return d4 / d5;
  }

  public void recomputeHeights()
  {
    this.mWaveValues = null;
    invalidate();
  }

  public int secondsToFrames(double paramDouble)
  {
    double d1 = 1.0D * paramDouble;
    double d2 = this.mSampleRate;
    double d3 = d1 * d2;
    double d4 = this.mSamplesPerFrame;
    return (int)Math.round(d3 / d4);
  }

  public int secondsToPixels(double paramDouble)
  {
    float f = this.mZoomFactor * paramDouble;
    double d1 = this.mSampleRate;
    double d2 = f * d1;
    double d3 = this.mSamplesPerFrame;
    return (int)Math.round(d2 / d3);
  }

  void setActivity(RingtoneEditActivity paramRingtoneEditActivity)
  {
    this.mActivity = paramRingtoneEditActivity;
  }

  public void setListener(WaveformListener paramWaveformListener)
  {
    this.mListener = paramWaveformListener;
  }

  public void setParameters(int paramInt1, int paramInt2, int paramInt3)
  {
    this.mSelectionStart = paramInt1;
    this.mSelectionEnd = paramInt2;
    this.mOffset = paramInt3;
  }

  public void setPlayback(int paramInt)
  {
    this.mPlaybackPos = paramInt;
  }

  public void setSoundFile(CheapSoundFile paramCheapSoundFile)
  {
    this.mSoundFile = paramCheapSoundFile;
    int i = this.mSoundFile.getSampleRate();
    this.mSampleRate = i;
    int j = this.mSoundFile.getSamplesPerFrame();
    this.mSamplesPerFrame = j;
    computeAndSmoothWaveValues();
    this.mWaveValues = null;
  }

  int waveBottom()
  {
    int i = getHeight();
    int j = this.sBorderYOffset;
    return i - j;
  }

  int waveTop()
  {
    return this.sBorderYOffset;
  }

  private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
  {
    private int mSelectionEndOnStart;
    private int mSelectionStartOnStart;
    private int mSpanOnScaleBegin;
    private double mZoomFactorOnStart;

    private ScaleListener()
    {
    }

    private void updateScalePosition(ScaleGestureDetector paramScaleGestureDetector)
    {
      double d1 = Math.abs(paramScaleGestureDetector.getCurrentSpan());
      double d2 = WaveformView.this.getWidth();
      double d3 = d2 + d1;
      double d4 = this.mSpanOnScaleBegin;
      double d5 = (d3 - d4) / d2;
      WaveformView localWaveformView1 = WaveformView.this;
      double d6 = this.mZoomFactorOnStart * d5;
      double d7 = WaveformView.access$202(localWaveformView1, d6);
      WaveformView localWaveformView2 = WaveformView.this;
      int i = (int)(this.mSelectionStartOnStart * d5);
      int j = WaveformView.access$302(localWaveformView2, i);
      WaveformView localWaveformView3 = WaveformView.this;
      int k = (int)(this.mSelectionEndOnStart * d5);
      int m = WaveformView.access$402(localWaveformView3, k);
      WaveformView.WaveformListener localWaveformListener = WaveformView.this.mListener;
      float f = (float)d5;
      localWaveformListener.waveformScale(f);
      WaveformView.this.mActivity.updatePosition();
    }

    public boolean onScale(ScaleGestureDetector paramScaleGestureDetector)
    {
      updateScalePosition(paramScaleGestureDetector);
      return super.onScale(paramScaleGestureDetector);
    }

    public boolean onScaleBegin(ScaleGestureDetector paramScaleGestureDetector)
    {
      int i = Math.abs((int)paramScaleGestureDetector.getCurrentSpan());
      this.mSpanOnScaleBegin = i;
      double d = WaveformView.this.mZoomFactor;
      this.mZoomFactorOnStart = d;
      int j = WaveformView.this.mSelectionStart;
      this.mSelectionStartOnStart = j;
      int k = WaveformView.this.mSelectionEnd;
      this.mSelectionEndOnStart = k;
      return super.onScaleBegin(paramScaleGestureDetector);
    }

    public void onScaleEnd(ScaleGestureDetector paramScaleGestureDetector)
    {
      updateScalePosition(paramScaleGestureDetector);
    }
  }

  public static abstract interface WaveformListener
  {
    public abstract void waveformDoubleClick(float paramFloat1, float paramFloat2);

    public abstract void waveformDraw();

    public abstract void waveformFling(float paramFloat);

    public abstract void waveformScale(float paramFloat);

    public abstract void waveformTouchEnd();

    public abstract void waveformTouchMove(float paramFloat);

    public abstract void waveformTouchStart(float paramFloat);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ringtone.WaveformView
 * JD-Core Version:    0.6.2
 */