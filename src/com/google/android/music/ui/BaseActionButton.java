package com.google.android.music.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;

public abstract class BaseActionButton extends TextView
  implements View.OnClickListener, View.OnLongClickListener
{
  private ActionButtonListener mListener;
  private MediaList mMediaList;
  private String mTextLabel;
  private boolean mTextShown;

  public BaseActionButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet);
    Resources localResources = paramContext.getResources();
    String str1 = localResources.getString(paramInt1);
    this.mTextLabel = str1;
    setLongClickable(true);
    setClickable(true);
    setOnClickListener(this);
    setOnLongClickListener(this);
    Typeface localTypeface = Typeface.DEFAULT_BOLD;
    setTypeface(localTypeface);
    int i = localResources.getColor(2131427356);
    setTextColor(i);
    setGravity(17);
    setCompoundDrawablesWithIntrinsicBounds(paramInt2, 0, 0, 0);
    String str2 = this.mTextLabel;
    setText(str2);
    int j = localResources.getDimensionPixelSize(2131558445);
    setCompoundDrawablePadding(j);
    setBackgroundResource(2130837848);
    this.mTextShown = true;
  }

  protected abstract void handleAction(Context paramContext, MediaList paramMediaList);

  public final void onClick(View paramView)
  {
    if (this.mListener != null)
      this.mListener.onActionStart();
    MediaList localMediaList = this.mMediaList;
    MusicUtils.runAsyncWithCallback(new ClickHandlerTask(this, localMediaList));
  }

  public final boolean onLongClick(View paramView)
  {
    Context localContext = getContext();
    String str = this.mTextLabel;
    Toast.makeText(localContext, str, 0).show();
    return true;
  }

  public final void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(paramInt1), 0);
    super.onMeasure(i, paramInt2);
  }

  public void setActionButtonListener(ActionButtonListener paramActionButtonListener)
  {
    this.mListener = paramActionButtonListener;
  }

  public void setMediaList(MediaList paramMediaList)
  {
    this.mMediaList = paramMediaList;
  }

  public static abstract interface ActionButtonListener
  {
    public abstract void onActionFinish();

    public abstract void onActionStart();
  }

  public class ClickHandlerTask
    implements AsyncRunner
  {
    private BaseActionButton mButton;
    private MediaList mMediaList;

    public ClickHandlerTask(BaseActionButton paramMediaList, MediaList arg3)
    {
      this.mButton = paramMediaList;
      Object localObject;
      this.mMediaList = localObject;
    }

    public void backgroundTask()
    {
      BaseActionButton localBaseActionButton = this.mButton;
      Context localContext = this.mButton.getContext();
      MediaList localMediaList = this.mMediaList;
      localBaseActionButton.handleAction(localContext, localMediaList);
    }

    public void taskCompleted()
    {
      if (BaseActionButton.this.mListener == null)
        return;
      BaseActionButton.this.mListener.onActionFinish();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.BaseActionButton
 * JD-Core Version:    0.6.2
 */