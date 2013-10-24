package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R.bool;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.internal.widget.CompatTextView;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Toast;
import java.util.Locale;

public class ActionMenuItemView extends CompatTextView
  implements ActionMenuView.ActionMenuChildView, MenuView.ItemView, View.OnClickListener, View.OnLongClickListener
{
  private boolean mAllowTextWithIcon;
  private boolean mExpandedFormat;
  private Drawable mIcon;
  private MenuItemImpl mItemData;
  private MenuBuilder.ItemInvoker mItemInvoker;
  private int mMinWidth;
  private int mSavedPaddingLeft;
  private CharSequence mTitle;

  public ActionMenuItemView(Context paramContext)
  {
    this(paramContext, null);
  }

  public ActionMenuItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public ActionMenuItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    Resources localResources = paramContext.getResources();
    int i = R.bool.abc_config_allowActionMenuItemTextWithIcon;
    boolean bool = localResources.getBoolean(i);
    this.mAllowTextWithIcon = bool;
    int[] arrayOfInt = R.styleable.ActionMenuItemView;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt, 0, 0);
    int j = localTypedArray.getDimensionPixelSize(0, 0);
    this.mMinWidth = j;
    localTypedArray.recycle();
    setOnClickListener(this);
    setOnLongClickListener(this);
    AllCapsTransformationMethod localAllCapsTransformationMethod = new AllCapsTransformationMethod();
    setTransformationMethod(localAllCapsTransformationMethod);
    this.mSavedPaddingLeft = -1;
  }

  private void updateTextButtonVisibility()
  {
    int i = 0;
    int j;
    if (!TextUtils.isEmpty(this.mTitle))
    {
      j = 1;
      if ((this.mIcon == null) || ((this.mItemData.showsTextAsAction()) && ((this.mAllowTextWithIcon) || (this.mExpandedFormat))))
        i = 1;
      if ((j & i) == 0)
        break label69;
    }
    label69: for (CharSequence localCharSequence = this.mTitle; ; localCharSequence = null)
    {
      setText(localCharSequence);
      return;
      j = 0;
      break;
    }
  }

  public MenuItemImpl getItemData()
  {
    return this.mItemData;
  }

  public boolean hasText()
  {
    if (!TextUtils.isEmpty(getText()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void initialize(MenuItemImpl paramMenuItemImpl, int paramInt)
  {
    this.mItemData = paramMenuItemImpl;
    Drawable localDrawable = paramMenuItemImpl.getIcon();
    setIcon(localDrawable);
    CharSequence localCharSequence = paramMenuItemImpl.getTitleForItemView(this);
    setTitle(localCharSequence);
    int i = paramMenuItemImpl.getItemId();
    setId(i);
    if (paramMenuItemImpl.isVisible());
    for (int j = 0; ; j = 8)
    {
      setVisibility(j);
      boolean bool = paramMenuItemImpl.isEnabled();
      setEnabled(bool);
      return;
    }
  }

  public boolean needsDividerAfter()
  {
    return hasText();
  }

  public boolean needsDividerBefore()
  {
    if ((hasText()) && (this.mItemData.getIcon() == null));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void onClick(View paramView)
  {
    if (this.mItemInvoker == null)
      return;
    MenuBuilder.ItemInvoker localItemInvoker = this.mItemInvoker;
    MenuItemImpl localMenuItemImpl = this.mItemData;
    boolean bool = localItemInvoker.invokeItem(localMenuItemImpl);
  }

  public boolean onLongClick(View paramView)
  {
    boolean bool = false;
    if (hasText())
      return bool;
    int[] arrayOfInt = new int[2];
    Rect localRect = new Rect();
    getLocationOnScreen(arrayOfInt);
    getWindowVisibleDisplayFrame(localRect);
    Context localContext = getContext();
    int j = getWidth();
    int k = getHeight();
    int m = arrayOfInt[1];
    int n = k / 2;
    int i1 = m + n;
    int i2 = localContext.getResources().getDisplayMetrics().widthPixels;
    CharSequence localCharSequence = this.mItemData.getTitle();
    Toast localToast = Toast.makeText(localContext, localCharSequence, 0);
    int i3 = localRect.height();
    if (i1 < i3)
    {
      int i4 = arrayOfInt[0];
      int i5 = i2 - i4;
      int i6 = j / 2;
      int i = i5 - i6;
      localToast.setGravity(53, i, k);
    }
    while (true)
    {
      localToast.show();
      int i7 = 1;
      break;
      localToast.setGravity(81, 0, k);
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    boolean bool = hasText();
    if ((bool) && (this.mSavedPaddingLeft >= 0))
    {
      int i = this.mSavedPaddingLeft;
      int j = getPaddingTop();
      int k = getPaddingRight();
      int m = getPaddingBottom();
      super.setPadding(i, j, k, m);
    }
    super.onMeasure(paramInt1, paramInt2);
    int n = View.MeasureSpec.getMode(paramInt1);
    int i1 = View.MeasureSpec.getSize(paramInt1);
    int i2 = getMeasuredWidth();
    int i3;
    if (n == -2147483648)
      i3 = this.mMinWidth;
    for (int i4 = Math.min(i1, i3); ; i4 = this.mMinWidth)
    {
      if ((n != 1073741824) && (this.mMinWidth > 0) && (i2 < i4))
      {
        int i5 = View.MeasureSpec.makeMeasureSpec(i4, 1073741824);
        super.onMeasure(i5, paramInt2);
      }
      if (bool)
        return;
      if (this.mIcon == null)
        return;
      int i6 = getMeasuredWidth();
      int i7 = this.mIcon.getIntrinsicWidth();
      int i8 = (i6 - i7) / 2;
      int i9 = getPaddingTop();
      int i10 = getPaddingRight();
      int i11 = getPaddingBottom();
      super.setPadding(i8, i9, i10, i11);
      return;
    }
  }

  public boolean prefersCondensedTitle()
  {
    return true;
  }

  public void setCheckable(boolean paramBoolean)
  {
  }

  public void setChecked(boolean paramBoolean)
  {
  }

  public void setExpandedFormat(boolean paramBoolean)
  {
    if (this.mExpandedFormat != paramBoolean)
      return;
    this.mExpandedFormat = paramBoolean;
    if (this.mItemData == null)
      return;
    this.mItemData.actionFormatChanged();
  }

  public void setIcon(Drawable paramDrawable)
  {
    this.mIcon = paramDrawable;
    setCompoundDrawablesWithIntrinsicBounds(paramDrawable, null, null, null);
    updateTextButtonVisibility();
  }

  public void setItemInvoker(MenuBuilder.ItemInvoker paramItemInvoker)
  {
    this.mItemInvoker = paramItemInvoker;
  }

  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mSavedPaddingLeft = paramInt1;
    super.setPadding(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void setShortcut(boolean paramBoolean, char paramChar)
  {
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    this.mTitle = paramCharSequence;
    CharSequence localCharSequence = this.mTitle;
    setContentDescription(localCharSequence);
    updateTextButtonVisibility();
  }

  private class AllCapsTransformationMethod
    implements TransformationMethod
  {
    private Locale mLocale;

    public AllCapsTransformationMethod()
    {
      Locale localLocale = ActionMenuItemView.this.getContext().getResources().getConfiguration().locale;
      this.mLocale = localLocale;
    }

    public CharSequence getTransformation(CharSequence paramCharSequence, View paramView)
    {
      String str1;
      Locale localLocale;
      if (paramCharSequence != null)
      {
        str1 = paramCharSequence.toString();
        localLocale = this.mLocale;
      }
      for (String str2 = str1.toUpperCase(localLocale); ; str2 = null)
        return str2;
    }

    public void onFocusChanged(View paramView, CharSequence paramCharSequence, boolean paramBoolean, int paramInt, Rect paramRect)
    {
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.ActionMenuItemView
 * JD-Core Version:    0.6.2
 */