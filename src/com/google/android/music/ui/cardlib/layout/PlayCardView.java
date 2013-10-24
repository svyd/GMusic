package com.google.android.music.ui.cardlib.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import com.google.android.music.KeepOnView;
import com.google.android.music.KeepOnViewSmall;
import com.google.android.music.KeepOnViewSmall.KeepOnVisibilityCallback;
import com.google.android.music.medialist.SongList;
import com.google.android.music.ui.cardlib.PlayDocument;
import com.google.android.music.ui.cardlib.layout.accessible.AccessibleRelativeLayout;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.utils.ViewUtils;

public class PlayCardView extends AccessibleRelativeLayout
  implements View.OnClickListener, KeepOnViewSmall.KeepOnVisibilityCallback
{
  protected View mAccessibilityOverlay;
  protected TextView mDescription;
  private PlayDocument mDocument;
  protected KeepOnViewSmall mKeepOn;
  protected final Rect mOldOverflowArea;
  private int mOriginalRightMargin = 0;
  protected PlayCardOverflowView mOverflow;
  protected final Rect mOverflowArea;
  protected final int mOverflowTouchExtend;
  protected TextView mPrice;
  protected PlayCardReason mReason1;
  protected PlayCardReason mReason2;
  protected TextView mSubtitle;
  protected PlayCardThumbnail mThumbnail;
  protected float mThumbnailAspectRatio;
  protected TextView mTitle;
  protected float mUnavailableCardOpacity;

  public PlayCardView(Context paramContext)
  {
    this(paramContext, null);
  }

  public PlayCardView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Resources localResources = paramContext.getResources();
    int i = localResources.getDimensionPixelSize(2131558480);
    this.mOverflowTouchExtend = i;
    float f = localResources.getInteger(2131492875) / 100.0F;
    this.mUnavailableCardOpacity = f;
    Rect localRect1 = new Rect();
    this.mOverflowArea = localRect1;
    Rect localRect2 = new Rect();
    this.mOldOverflowArea = localRect2;
  }

  private void setupKeepOn(Document paramDocument, KeepOnView paramKeepOnView)
  {
    Object localObject = null;
    Document.Type localType1;
    if ((paramKeepOnView != null) && (paramDocument.getType() != null))
    {
      localType1 = paramDocument.getType();
      Document.Type localType2 = Document.Type.ALBUM;
      if (localType1 != localType2)
        break label55;
      Context localContext1 = getContext();
      localObject = paramDocument.getSongList(localContext1);
    }
    while (true)
    {
      if (paramKeepOnView == null)
        return;
      paramKeepOnView.setSongList((SongList)localObject);
      return;
      label55: Document.Type localType3 = Document.Type.PLAYLIST;
      if (localType1 == localType3)
      {
        Context localContext2 = getContext();
        SongList localSongList = paramDocument.getSongList(localContext2);
        int i = paramDocument.getPlaylistType();
        if ((i == 100) || (i == 0))
          localObject = localSongList;
      }
    }
  }

  public void bind(Document paramDocument, final ContextMenuDelegate paramContextMenuDelegate)
  {
    this.mDocument = paramDocument;
    if (paramDocument != null)
    {
      label69: String str5;
      label147: Object localObject;
      label192: label203: ViewGroup.MarginLayoutParams localMarginLayoutParams;
      if (paramDocument.getTitle() != null)
      {
        this.mTitle.setVisibility(0);
        TextView localTextView = this.mTitle;
        String str1 = paramDocument.getTitle();
        localTextView.setText(str1);
        String str2 = paramDocument.getSubTitle();
        if (str2 == null)
          break label497;
        this.mSubtitle.setVisibility(0);
        this.mSubtitle.setText(str2);
        this.mAccessibilityOverlay.setOnClickListener(this);
        this.mThumbnail.setVisibility(0);
        this.mThumbnail.bind(paramDocument);
        if (this.mPrice != null)
          this.mPrice.setVisibility(8);
        if (this.mReason1 != null)
        {
          if (paramDocument.getReason1() == null)
            break label509;
          this.mReason1.setVisibility(0);
          String str3 = paramDocument.getReason1();
          this.mReason1.bind(str3, null);
        }
        if (this.mReason2 != null)
        {
          if (paramDocument.getReason2() == null)
            break label591;
          this.mReason2.setVisibility(0);
          String str4 = "https://lh3.googleusercontent.com/-mGTYed3Uh-E/AAAAAAAAAAI/AAAAAAAACLY/mZFGQ0Q69c4/photo.jpg";
          str5 = "Recommendation";
          if (!TextUtils.isEmpty(str5))
            break label521;
          localObject = Html.fromHtml("<b>Popular with</b> similar users in your area.");
          this.mReason2.bind((CharSequence)localObject, str4);
        }
        if (this.mDescription != null)
        {
          this.mDescription.setVisibility(0);
          this.mDescription.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec ut sem nec lacus posuere fermentum. Suspendisse a magna quam. Suspendisse potenti. Nam venenatis, nisl et convallis suscipit, nisl nisl tincidunt erat, a vulputate lorem libero vel augue. Curabitur quis molestie nibh. Mauris nec velit justo, nec pellentesque augue. Donec lorem nunc, viverra nec sollicitudin eu, fermentum non lectus. Nam eleifend, orci eu tincidunt dictum, eros nisl vestibulum magna, vel aliquet metus risus id lorem. Nam neque ligula, iaculis eget mollis et, tincidunt sed nisi. Ut eu dui ipsum, ut tempor justo. ");
        }
        KeepOnViewSmall localKeepOnViewSmall = this.mKeepOn;
        setupKeepOn(paramDocument, localKeepOnViewSmall);
        StringBuilder localStringBuilder1 = new StringBuilder();
        if (!TextUtils.isEmpty(paramDocument.getTitle()))
        {
          String str6 = paramDocument.getTitle();
          StringBuilder localStringBuilder2 = localStringBuilder1.append(str6).append(" ");
        }
        if (!TextUtils.isEmpty(paramDocument.getSubTitle()))
        {
          String str7 = paramDocument.getSubTitle();
          StringBuilder localStringBuilder3 = localStringBuilder1.append(str7).append(" ");
        }
        if (!TextUtils.isEmpty(paramDocument.getReason1()))
        {
          String str8 = paramDocument.getReason1();
          StringBuilder localStringBuilder4 = localStringBuilder1.append(str8);
        }
        View localView = this.mAccessibilityOverlay;
        String str9 = localStringBuilder1.toString();
        localView.setContentDescription(str9);
        if (paramContextMenuDelegate != null)
        {
          PlayCardOverflowView localPlayCardOverflowView = this.mOverflow;
          View.OnClickListener local1 = new View.OnClickListener()
          {
            public void onClick(View paramAnonymousView)
            {
              PlayCardView.ContextMenuDelegate localContextMenuDelegate = paramContextMenuDelegate;
              PlayCardView localPlayCardView = PlayCardView.this;
              PlayCardOverflowView localPlayCardOverflowView = PlayCardView.this.mOverflow;
              localContextMenuDelegate.onContextMenuPressed(localPlayCardView, localPlayCardOverflowView);
            }
          };
          localPlayCardOverflowView.setOnClickListener(local1);
        }
        setVisibility(0);
        if (!paramDocument.getIsAvailable())
          break label603;
        ViewUtils.setAlpha(this, 1.0F);
        label400: localMarginLayoutParams = (ViewGroup.MarginLayoutParams)this.mTitle.getLayoutParams();
        if (paramDocument.shouldShowContextMenu())
          break label618;
        this.mOverflow.setVisibility(8);
      }
      label497: label509: int j;
      for (localMarginLayoutParams.rightMargin = 0; ; localMarginLayoutParams.rightMargin = j)
      {
        if (paramDocument.getType() == null)
          return;
        Document.Type localType1 = paramDocument.getType();
        Document.Type localType2 = Document.Type.ARTIST;
        if (localType1 != localType2)
          return;
        if (paramDocument.shouldShowContextMenuForArtist())
          return;
        this.mOverflow.setVisibility(8);
        localMarginLayoutParams.rightMargin = 0;
        return;
        this.mTitle.setVisibility(8);
        break;
        this.mSubtitle.setVisibility(8);
        break label69;
        this.mReason1.setVisibility(8);
        break label147;
        label521: int i = str5.indexOf(' ');
        if (i > 0)
        {
          SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(str5);
          Context localContext = getContext();
          TextAppearanceSpan localTextAppearanceSpan = new TextAppearanceSpan(localContext, 2131755069);
          localSpannableStringBuilder.setSpan(localTextAppearanceSpan, 0, i, 0);
          localObject = localSpannableStringBuilder;
          break label192;
        }
        localObject = str5;
        break label192;
        label591: this.mReason2.setVisibility(8);
        break label203;
        label603: float f = this.mUnavailableCardOpacity;
        ViewUtils.setAlpha(this, f);
        break label400;
        label618: this.mOverflow.setVisibility(0);
        j = this.mOriginalRightMargin;
      }
    }
    bindNoDocument();
  }

  public void bindFakeData()
  {
    this.mTitle.setVisibility(0);
    this.mTitle.setText("Title");
    this.mSubtitle.setVisibility(0);
    this.mSubtitle.setText("Creator");
    this.mThumbnail.setVisibility(0);
    if (this.mReason1 != null)
    {
      this.mReason1.setVisibility(0);
      Spanned localSpanned = Html.fromHtml("<b>Recommended</b> by 27 people in your circles.");
      this.mReason1.bind(localSpanned, "https://lh6.googleusercontent.com/-WKYEInY8h0k/AAAAAAAAAAI/AAAAAAAB0o4/sCcEGZsOQjE/photo.jpg");
    }
    String str1;
    String str2;
    Object localObject;
    if (this.mReason2 != null)
    {
      this.mReason2.setVisibility(0);
      str1 = "https://lh3.googleusercontent.com/-mGTYed3Uh-E/AAAAAAAAAAI/AAAAAAAACLY/mZFGQ0Q69c4/photo.jpg";
      str2 = "Recommendation";
      if (!TextUtils.isEmpty(str2))
        break label167;
      localObject = Html.fromHtml("<b>Popular with</b> similar users in your area.");
    }
    while (true)
    {
      this.mReason2.bind((CharSequence)localObject, str1);
      if (this.mDescription != null)
      {
        this.mDescription.setVisibility(0);
        this.mDescription.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec ut sem nec lacus posuere fermentum. Suspendisse a magna quam. Suspendisse potenti. Nam venenatis, nisl et convallis suscipit, nisl nisl tincidunt erat, a vulputate lorem libero vel augue. Curabitur quis molestie nibh. Mauris nec velit justo, nec pellentesque augue. Donec lorem nunc, viverra nec sollicitudin eu, fermentum non lectus. Nam eleifend, orci eu tincidunt dictum, eros nisl vestibulum magna, vel aliquet metus risus id lorem. Nam neque ligula, iaculis eget mollis et, tincidunt sed nisi. Ut eu dui ipsum, ut tempor justo. ");
      }
      this.mOverflow.setVisibility(0);
      setContentDescription("Need to do accessibility");
      setVisibility(0);
      return;
      label167: int i = str2.indexOf(' ');
      if (i > 0)
      {
        SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(str2);
        Context localContext = getContext();
        TextAppearanceSpan localTextAppearanceSpan = new TextAppearanceSpan(localContext, 2131755069);
        localSpannableStringBuilder.setSpan(localTextAppearanceSpan, 0, i, 0);
        localObject = localSpannableStringBuilder;
      }
      else
      {
        localObject = str2;
      }
    }
  }

  public void bindLoading()
  {
    this.mAccessibilityOverlay.setContentDescription(null);
    this.mAccessibilityOverlay.setOnClickListener(null);
    this.mAccessibilityOverlay.setClickable(false);
    this.mTitle.setVisibility(0);
    this.mSubtitle.setVisibility(8);
    this.mThumbnail.setVisibility(0);
    if (this.mPrice != null)
      this.mPrice.setVisibility(8);
    if (this.mDescription != null)
      this.mDescription.setVisibility(8);
    if (this.mReason1 != null)
      this.mReason1.setVisibility(8);
    if (this.mReason2 != null)
      this.mReason2.setVisibility(8);
    this.mOverflow.setVisibility(8);
    setVisibility(0);
  }

  public void bindNoDocument()
  {
    setVisibility(4);
  }

  public void clearThumbnail()
  {
    this.mThumbnail.clear();
  }

  public PlayDocument getDocument()
  {
    return this.mDocument;
  }

  public void onClick(View paramView)
  {
    boolean bool = performClick();
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (this.mKeepOn == null)
      return;
    this.mKeepOn.unregisterCallback();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    PlayCardThumbnail localPlayCardThumbnail = (PlayCardThumbnail)findViewById(2131296470);
    this.mThumbnail = localPlayCardThumbnail;
    TextView localTextView1 = (TextView)findViewById(2131296472);
    this.mTitle = localTextView1;
    TextView localTextView2 = (TextView)findViewById(2131296473);
    this.mSubtitle = localTextView2;
    PlayCardReason localPlayCardReason1 = (PlayCardReason)findViewById(2131296474);
    this.mReason1 = localPlayCardReason1;
    PlayCardReason localPlayCardReason2 = (PlayCardReason)findViewById(2131296479);
    this.mReason2 = localPlayCardReason2;
    PlayCardOverflowView localPlayCardOverflowView = (PlayCardOverflowView)findViewById(2131296476);
    this.mOverflow = localPlayCardOverflowView;
    TextView localTextView3 = (TextView)findViewById(2131296477);
    this.mPrice = localTextView3;
    TextView localTextView4 = (TextView)findViewById(2131296481);
    this.mDescription = localTextView4;
    View localView = findViewById(2131296411);
    this.mAccessibilityOverlay = localView;
    KeepOnViewSmall localKeepOnViewSmall = (KeepOnViewSmall)findViewById(2131296475);
    this.mKeepOn = localKeepOnViewSmall;
    if (this.mKeepOn != null)
      this.mKeepOn.registerCallback(this);
    int i = ((ViewGroup.MarginLayoutParams)this.mTitle.getLayoutParams()).rightMargin;
    this.mOriginalRightMargin = i;
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = true;
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    float f3 = this.mOverflowArea.right;
    int i;
    if (f1 <= f3)
    {
      float f4 = this.mOverflowArea.left;
      if (f1 >= f4)
      {
        float f5 = this.mOverflowArea.top;
        if (f2 >= f5)
        {
          float f6 = this.mOverflowArea.bottom;
          if (f2 <= f6)
          {
            i = 1;
            if (i == 0)
              break label99;
          }
        }
      }
    }
    while (true)
    {
      return bool;
      i = 0;
      break;
      label99: bool = false;
    }
  }

  @SuppressLint({"DrawAllocation"})
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    int i = getPaddingLeft();
    int j = getPaddingTop();
    ViewGroup.MarginLayoutParams localMarginLayoutParams1 = (ViewGroup.MarginLayoutParams)this.mAccessibilityOverlay.getLayoutParams();
    View localView = this.mAccessibilityOverlay;
    int k = localMarginLayoutParams1.leftMargin + i;
    int m = localMarginLayoutParams1.topMargin + j;
    int n = localMarginLayoutParams1.leftMargin + i;
    int i1 = this.mAccessibilityOverlay.getMeasuredWidth();
    int i2 = n + i1;
    int i3 = localMarginLayoutParams1.topMargin + j;
    int i4 = this.mAccessibilityOverlay.getMeasuredHeight();
    int i5 = i3 + i4;
    localView.layout(k, m, i2, i5);
    PlayCardOverflowView localPlayCardOverflowView1 = this.mOverflow;
    Rect localRect1 = this.mOverflowArea;
    localPlayCardOverflowView1.getHitRect(localRect1);
    Rect localRect2 = this.mOverflowArea;
    int i6 = localRect2.top;
    int i7 = this.mOverflowTouchExtend;
    int i8 = i6 - i7;
    localRect2.top = i8;
    Rect localRect3 = this.mOverflowArea;
    int i9 = localRect3.bottom;
    int i10 = this.mOverflowTouchExtend;
    int i11 = i9 + i10;
    localRect3.bottom = i11;
    Rect localRect4 = this.mOverflowArea;
    int i12 = localRect4.left;
    int i13 = this.mOverflowTouchExtend;
    int i14 = i12 - i13;
    localRect4.left = i14;
    Rect localRect5 = this.mOverflowArea;
    int i15 = localRect5.right;
    int i16 = this.mOverflowTouchExtend;
    int i17 = i15 + i16;
    localRect5.right = i17;
    int i18 = this.mOverflowArea.top;
    int i19 = this.mOldOverflowArea.top;
    if (i18 != i19)
    {
      int i20 = this.mOverflowArea.bottom;
      int i21 = this.mOldOverflowArea.bottom;
      if (i20 != i21)
      {
        int i22 = this.mOverflowArea.left;
        int i23 = this.mOldOverflowArea.left;
        if (i22 != i23)
        {
          int i24 = this.mOverflowArea.right;
          int i25 = this.mOldOverflowArea.right;
          if (i24 != i25)
            return;
        }
      }
    }
    Rect localRect6 = this.mOverflowArea;
    PlayCardOverflowView localPlayCardOverflowView2 = this.mOverflow;
    TouchDelegate localTouchDelegate = new TouchDelegate(localRect6, localPlayCardOverflowView2);
    setTouchDelegate(localTouchDelegate);
    Rect localRect7 = this.mOldOverflowArea;
    Rect localRect8 = this.mOverflowArea;
    localRect7.set(localRect8);
    if (this.mKeepOn == null)
      return;
    int i26 = getWidth();
    int i27 = getHeight();
    ViewGroup.MarginLayoutParams localMarginLayoutParams2 = (ViewGroup.MarginLayoutParams)this.mKeepOn.getLayoutParams();
    int i28 = getPaddingBottom();
    int i29 = i27 - i28;
    int i30 = localMarginLayoutParams2.bottomMargin;
    int i31 = i29 - i30;
    int i32 = this.mKeepOn.getMeasuredHeight();
    int i33 = i31 - i32;
    int i34 = getPaddingRight();
    int i35 = i26 - i34;
    int i36 = localMarginLayoutParams2.rightMargin;
    int i37 = i35 - i36;
    int i38 = this.mKeepOn.getMeasuredWidth();
    int i39 = i37 - i38;
    this.mKeepOn.layout(i39, i33, i37, i31);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (this.mDescription != null)
      this.mDescription.setMaxLines(100);
    super.onMeasure(paramInt1, paramInt2);
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)this.mAccessibilityOverlay.getLayoutParams();
    int i = getMeasuredWidth();
    int j = getPaddingLeft();
    int k = i - j;
    int m = getPaddingRight();
    int n = k - m;
    int i1 = localMarginLayoutParams.leftMargin;
    int i2 = n - i1;
    int i3 = localMarginLayoutParams.rightMargin;
    int i4 = i2 - i3;
    int i5 = getMeasuredHeight();
    int i6 = getPaddingTop();
    int i7 = i5 - i6;
    int i8 = getPaddingBottom();
    int i9 = i7 - i8;
    int i10 = localMarginLayoutParams.topMargin;
    int i11 = i9 - i10;
    int i12 = localMarginLayoutParams.bottomMargin;
    int i13 = i11 - i12;
    View localView = this.mAccessibilityOverlay;
    int i14 = View.MeasureSpec.makeMeasureSpec(i4, 1073741824);
    int i15 = View.MeasureSpec.makeMeasureSpec(i13, 1073741824);
    localView.measure(i14, i15);
    int i16;
    Layout localLayout;
    int i17;
    if (this.mDescription != null)
    {
      i16 = this.mDescription.getMeasuredHeight();
      localLayout = this.mDescription.getLayout();
      i17 = 0;
    }
    while (true)
    {
      int i18 = localLayout.getLineCount();
      TextView localTextView;
      if (i17 < i18)
      {
        if (localLayout.getLineBottom(i17) <= i16)
          break label293;
        this.mDescription.setMaxLines(i17);
        localTextView = this.mDescription;
        if (i17 < 2)
          break label287;
      }
      label287: for (int i19 = 0; ; i19 = 4)
      {
        localTextView.setVisibility(i19);
        if (this.mKeepOn == null)
          return;
        this.mKeepOn.measure(0, 0);
        return;
      }
      label293: i17 += 1;
    }
  }

  public void setThumbnailAspectRatio(float paramFloat)
  {
    this.mThumbnailAspectRatio = paramFloat;
  }

  public void updateKeepOnViewSmallVisibility(boolean paramBoolean)
  {
    if ((paramBoolean) && (this.mKeepOn.getVisibility() != 0))
    {
      this.mKeepOn.setVisibility(0);
      return;
    }
    if (paramBoolean)
      return;
    if (this.mKeepOn.getVisibility() == 4)
      return;
    this.mKeepOn.setVisibility(4);
  }

  public static abstract interface ContextMenuDelegate
  {
    public abstract void onContextMenuPressed(PlayCardView paramPlayCardView, View paramView);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.layout.PlayCardView
 * JD-Core Version:    0.6.2
 */