package com.google.android.music;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PassTouchEvents extends View
{
  private View mSendEventsTo = null;

  public PassTouchEvents(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.mSendEventsTo != null) && (this.mSendEventsTo.onTouchEvent(paramMotionEvent)));
    for (boolean bool = true; ; bool = false)
    {
      return bool;
      if (super.onTouchEvent(paramMotionEvent))
        break;
    }
  }

  public void setSendEventsTo(View paramView)
  {
    this.mSendEventsTo = paramView;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.PassTouchEvents
 * JD-Core Version:    0.6.2
 */