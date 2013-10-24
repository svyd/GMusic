package com.google.android.music.ui.cardlib.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.music.ui.cardlib.PlayIconAndTextListAdapter.IconAndTextListEntry;
import com.google.android.music.utils.TypefaceUtil;

public class PlayTutorialHeader extends LinearLayout
{
  private ViewGroup mActionsView;
  private View mBodyView;
  private TextView mTitleView;

  public PlayTutorialHeader(Context paramContext)
  {
    super(paramContext);
  }

  public PlayTutorialHeader(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public static PlayTutorialHeader create(Context paramContext, ViewGroup paramViewGroup)
  {
    return (PlayTutorialHeader)LayoutInflater.from(paramContext).inflate(2130968667, paramViewGroup, false);
  }

  private void inflateIconsAndTextList(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, int paramInt1, PlayIconAndTextListAdapter.IconAndTextListEntry[] paramArrayOfIconAndTextListEntry, AdapterView.OnItemClickListener paramOnItemClickListener, int paramInt2)
  {
    int i = 0;
    PlayIconAndTextListAdapter.IconAndTextListEntry[] arrayOfIconAndTextListEntry = paramArrayOfIconAndTextListEntry;
    int j = arrayOfIconAndTextListEntry.length;
    int k = 0;
    while (true)
    {
      int m = j;
      if (k >= m)
        return;
      PlayIconAndTextListAdapter.IconAndTextListEntry localIconAndTextListEntry = arrayOfIconAndTextListEntry[k];
      if (paramInt2 != 0)
      {
        Context localContext = getContext();
        View localView1 = new View(localContext);
        int n = paramInt2;
        localView1.setBackgroundResource(n);
        ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, -1);
        paramViewGroup.addView(localView1, localLayoutParams);
      }
      LayoutInflater localLayoutInflater = paramLayoutInflater;
      int i1 = paramInt1;
      ViewGroup localViewGroup = paramViewGroup;
      final View localView2 = localLayoutInflater.inflate(i1, localViewGroup, false);
      ImageView localImageView = (ImageView)localView2.findViewById(2131296494);
      TextView localTextView = (TextView)localView2.findViewById(2131296495);
      TypefaceUtil.setTypeface(localTextView, 1);
      int i2 = localIconAndTextListEntry.mIconId;
      localImageView.setImageResource(i2);
      int i3 = localIconAndTextListEntry.mDescriptionId;
      localTextView.setText(i3);
      if (paramOnItemClickListener != null)
      {
        final int i4 = i;
        final long l = localIconAndTextListEntry.mDescriptionId;
        PlayTutorialHeader localPlayTutorialHeader = this;
        final AdapterView.OnItemClickListener localOnItemClickListener = paramOnItemClickListener;
        View.OnClickListener local1 = new View.OnClickListener()
        {
          public void onClick(View paramAnonymousView)
          {
            AdapterView.OnItemClickListener localOnItemClickListener = localOnItemClickListener;
            View localView = localView2;
            int i = i4;
            long l = l;
            localOnItemClickListener.onItemClick(null, localView, i, l);
          }
        };
        localView2.setOnClickListener(local1);
      }
      paramViewGroup.addView(localView2);
      i += 1;
      k += 1;
    }
  }

  public View inflateBody(int paramInt)
  {
    ViewStub localViewStub = (ViewStub)findViewById(2131296497);
    localViewStub.setLayoutResource(paramInt);
    View localView = localViewStub.inflate();
    this.mBodyView = localView;
    return this.mBodyView;
  }

  public void inflateBody(PlayIconAndTextListAdapter.IconAndTextListEntry[] paramArrayOfIconAndTextListEntry)
  {
    ViewGroup localViewGroup = (ViewGroup)inflateBody(2130968664);
    LayoutInflater localLayoutInflater = LayoutInflater.from(getContext());
    PlayTutorialHeader localPlayTutorialHeader = this;
    PlayIconAndTextListAdapter.IconAndTextListEntry[] arrayOfIconAndTextListEntry = paramArrayOfIconAndTextListEntry;
    localPlayTutorialHeader.inflateIconsAndTextList(localLayoutInflater, localViewGroup, 2130968666, arrayOfIconAndTextListEntry, null, 0);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    TextView localTextView = (TextView)findViewById(2131296496);
    this.mTitleView = localTextView;
    TypefaceUtil.setTypeface(this.mTitleView, 2);
    ViewGroup localViewGroup = (ViewGroup)findViewById(2131296499);
    this.mActionsView = localViewGroup;
  }

  public void setTitleResId(int paramInt)
  {
    this.mTitleView.setText(paramInt);
  }

  public void setupActionsList(PlayIconAndTextListAdapter.IconAndTextListEntry[] paramArrayOfIconAndTextListEntry, AdapterView.OnItemClickListener paramOnItemClickListener)
  {
    LayoutInflater localLayoutInflater = LayoutInflater.from(getContext());
    ViewGroup localViewGroup = this.mActionsView;
    PlayTutorialHeader localPlayTutorialHeader = this;
    PlayIconAndTextListAdapter.IconAndTextListEntry[] arrayOfIconAndTextListEntry = paramArrayOfIconAndTextListEntry;
    AdapterView.OnItemClickListener localOnItemClickListener = paramOnItemClickListener;
    localPlayTutorialHeader.inflateIconsAndTextList(localLayoutInflater, localViewGroup, 2130968663, arrayOfIconAndTextListEntry, localOnItemClickListener, 2130837860);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.layout.PlayTutorialHeader
 * JD-Core Version:    0.6.2
 */