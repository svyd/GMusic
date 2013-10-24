package com.google.android.music.ui.cardlib.layout;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.music.AsyncAlbumArtImageView;

public class PlayCardReason extends LinearLayout
{
  private TextView mReason;
  private AsyncAlbumArtImageView mReasonImage;

  public PlayCardReason(Context paramContext)
  {
    super(paramContext);
  }

  public PlayCardReason(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public static String getReasonString(Context paramContext, int paramInt)
  {
    String str;
    switch (paramInt)
    {
    default:
      str = paramContext.getResources().getString(2131231289);
    case 1:
    case 3:
    case 2:
    case 100:
    case 4:
    }
    while (true)
    {
      return str;
      str = paramContext.getResources().getString(2131231290);
      continue;
      str = paramContext.getResources().getString(2131231291);
      continue;
      str = paramContext.getResources().getString(2131231292);
      continue;
      str = paramContext.getResources().getString(2131231293);
      continue;
      str = paramContext.getResources().getString(2131231294);
    }
  }

  public void bind(CharSequence paramCharSequence, String paramString)
  {
    this.mReason.setText(paramCharSequence);
    if ((paramString != null) && (!paramString.isEmpty()))
    {
      this.mReasonImage.setVisibility(0);
      this.mReasonImage.setExternalAlbumArt(paramString);
      return;
    }
    this.mReasonImage.setVisibility(8);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    TextView localTextView = (TextView)findViewById(2131296484);
    this.mReason = localTextView;
    AsyncAlbumArtImageView localAsyncAlbumArtImageView = (AsyncAlbumArtImageView)findViewById(2131296483);
    this.mReasonImage = localAsyncAlbumArtImageView;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getHeight();
    int j = getWidth();
    int k = this.mReason.getMeasuredHeight();
    int m = this.mReasonImage.getMeasuredHeight();
    int n = this.mReasonImage.getMeasuredWidth();
    if (m > k)
    {
      int i1 = (i - m) / 2;
      AsyncAlbumArtImageView localAsyncAlbumArtImageView1 = this.mReasonImage;
      int i2 = i1 + m;
      localAsyncAlbumArtImageView1.layout(0, i1, n, i2);
      int i3 = (i - k) / 2;
      TextView localTextView1 = this.mReason;
      int i4 = i3 + k;
      localTextView1.layout(n, i3, j, i4);
      return;
    }
    int i5 = (i - k) / 2;
    AsyncAlbumArtImageView localAsyncAlbumArtImageView2 = this.mReasonImage;
    int i6 = i5 + k;
    localAsyncAlbumArtImageView2.layout(0, i5, n, i6);
    TextView localTextView2 = this.mReason;
    int i7 = i5 + k;
    localTextView2.layout(n, i5, j, i7);
  }

  public void setReasonMaxLines(int paramInt)
  {
    this.mReason.setMaxLines(paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.layout.PlayCardReason
 * JD-Core Version:    0.6.2
 */