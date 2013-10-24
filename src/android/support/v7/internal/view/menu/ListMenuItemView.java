package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.layout;
import android.support.v7.appcompat.R.styleable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.TextView;

public class ListMenuItemView extends LinearLayout
  implements MenuView.ItemView
{
  private Drawable mBackground;
  private CheckBox mCheckBox;
  private Context mContext;
  private boolean mForceShowIcon;
  private ImageView mIconView;
  private LayoutInflater mInflater;
  private MenuItemImpl mItemData;
  private int mMenuType;
  private boolean mPreserveIconSpacing;
  private RadioButton mRadioButton;
  private TextView mShortcutView;
  private int mTextAppearance;
  private Context mTextAppearanceContext;
  private TextView mTitleView;

  public ListMenuItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public ListMenuItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    int[] arrayOfInt = R.styleable.MenuView;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt, paramInt, 0);
    Drawable localDrawable = localTypedArray.getDrawable(5);
    this.mBackground = localDrawable;
    int i = localTypedArray.getResourceId(1, -1);
    this.mTextAppearance = i;
    boolean bool = localTypedArray.getBoolean(7, false);
    this.mPreserveIconSpacing = bool;
    this.mTextAppearanceContext = paramContext;
    localTypedArray.recycle();
  }

  private LayoutInflater getInflater()
  {
    if (this.mInflater == null)
    {
      LayoutInflater localLayoutInflater = LayoutInflater.from(this.mContext);
      this.mInflater = localLayoutInflater;
    }
    return this.mInflater;
  }

  private void insertCheckBox()
  {
    LayoutInflater localLayoutInflater = getInflater();
    int i = R.layout.abc_list_menu_item_checkbox;
    CheckBox localCheckBox1 = (CheckBox)localLayoutInflater.inflate(i, this, false);
    this.mCheckBox = localCheckBox1;
    CheckBox localCheckBox2 = this.mCheckBox;
    addView(localCheckBox2);
  }

  private void insertIconView()
  {
    LayoutInflater localLayoutInflater = getInflater();
    int i = R.layout.abc_list_menu_item_icon;
    ImageView localImageView1 = (ImageView)localLayoutInflater.inflate(i, this, false);
    this.mIconView = localImageView1;
    ImageView localImageView2 = this.mIconView;
    addView(localImageView2, 0);
  }

  private void insertRadioButton()
  {
    LayoutInflater localLayoutInflater = getInflater();
    int i = R.layout.abc_list_menu_item_radio;
    RadioButton localRadioButton1 = (RadioButton)localLayoutInflater.inflate(i, this, false);
    this.mRadioButton = localRadioButton1;
    RadioButton localRadioButton2 = this.mRadioButton;
    addView(localRadioButton2);
  }

  public MenuItemImpl getItemData()
  {
    return this.mItemData;
  }

  public void initialize(MenuItemImpl paramMenuItemImpl, int paramInt)
  {
    this.mItemData = paramMenuItemImpl;
    this.mMenuType = paramInt;
    if (paramMenuItemImpl.isVisible());
    for (int i = 0; ; i = 8)
    {
      setVisibility(i);
      CharSequence localCharSequence = paramMenuItemImpl.getTitleForItemView(this);
      setTitle(localCharSequence);
      boolean bool1 = paramMenuItemImpl.isCheckable();
      setCheckable(bool1);
      boolean bool2 = paramMenuItemImpl.shouldShowShortcut();
      char c = paramMenuItemImpl.getShortcut();
      setShortcut(bool2, c);
      Drawable localDrawable = paramMenuItemImpl.getIcon();
      setIcon(localDrawable);
      boolean bool3 = paramMenuItemImpl.isEnabled();
      setEnabled(bool3);
      return;
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    Drawable localDrawable = this.mBackground;
    setBackgroundDrawable(localDrawable);
    int i = R.id.title;
    TextView localTextView1 = (TextView)findViewById(i);
    this.mTitleView = localTextView1;
    if (this.mTextAppearance != -1)
    {
      TextView localTextView2 = this.mTitleView;
      Context localContext = this.mTextAppearanceContext;
      int j = this.mTextAppearance;
      localTextView2.setTextAppearance(localContext, j);
    }
    int k = R.id.shortcut;
    TextView localTextView3 = (TextView)findViewById(k);
    this.mShortcutView = localTextView3;
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if ((this.mIconView != null) && (this.mPreserveIconSpacing))
    {
      ViewGroup.LayoutParams localLayoutParams = getLayoutParams();
      LinearLayout.LayoutParams localLayoutParams1 = (LinearLayout.LayoutParams)this.mIconView.getLayoutParams();
      if ((localLayoutParams.height > 0) && (localLayoutParams1.width <= 0))
      {
        int i = localLayoutParams.height;
        localLayoutParams1.width = i;
      }
    }
    super.onMeasure(paramInt1, paramInt2);
  }

  public boolean prefersCondensedTitle()
  {
    return false;
  }

  public void setCheckable(boolean paramBoolean)
  {
    if ((!paramBoolean) && (this.mRadioButton == null) && (this.mCheckBox == null))
      return;
    Object localObject1;
    Object localObject2;
    if (this.mItemData.isExclusiveCheckable())
    {
      if (this.mRadioButton == null)
        insertRadioButton();
      localObject1 = this.mRadioButton;
      localObject2 = this.mCheckBox;
      if (!paramBoolean)
        break label144;
      boolean bool = this.mItemData.isChecked();
      ((CompoundButton)localObject1).setChecked(bool);
      if (!paramBoolean)
        break label137;
    }
    label137: for (int i = 0; ; i = 8)
    {
      if (((CompoundButton)localObject1).getVisibility() != i)
        ((CompoundButton)localObject1).setVisibility(i);
      if (localObject2 == null)
        return;
      if (((CompoundButton)localObject2).getVisibility() == 8)
        return;
      ((CompoundButton)localObject2).setVisibility(8);
      return;
      if (this.mCheckBox == null)
        insertCheckBox();
      localObject1 = this.mCheckBox;
      localObject2 = this.mRadioButton;
      break;
    }
    label144: if (this.mCheckBox != null)
      this.mCheckBox.setVisibility(8);
    if (this.mRadioButton == null)
      return;
    this.mRadioButton.setVisibility(8);
  }

  public void setChecked(boolean paramBoolean)
  {
    if (this.mItemData.isExclusiveCheckable())
      if (this.mRadioButton == null)
        insertRadioButton();
    for (Object localObject = this.mRadioButton; ; localObject = this.mCheckBox)
    {
      ((CompoundButton)localObject).setChecked(paramBoolean);
      return;
      if (this.mCheckBox == null)
        insertCheckBox();
    }
  }

  public void setForceShowIcon(boolean paramBoolean)
  {
    this.mForceShowIcon = paramBoolean;
    this.mPreserveIconSpacing = paramBoolean;
  }

  public void setIcon(Drawable paramDrawable)
  {
    if ((this.mItemData.shouldShowIcon()) || (this.mForceShowIcon));
    for (int i = 1; (i == 0) && (!this.mPreserveIconSpacing); i = 0)
      return;
    if ((this.mIconView == null) && (paramDrawable == null) && (!this.mPreserveIconSpacing))
      return;
    if (this.mIconView == null)
      insertIconView();
    if ((paramDrawable != null) || (this.mPreserveIconSpacing))
    {
      ImageView localImageView = this.mIconView;
      if (i != 0);
      while (true)
      {
        localImageView.setImageDrawable(paramDrawable);
        if (this.mIconView.getVisibility() == 0)
          return;
        this.mIconView.setVisibility(0);
        return;
        paramDrawable = null;
      }
    }
    this.mIconView.setVisibility(8);
  }

  public void setShortcut(boolean paramBoolean, char paramChar)
  {
    if ((paramBoolean) && (this.mItemData.shouldShowShortcut()));
    for (int i = 0; ; i = 8)
    {
      if (i == 0)
      {
        TextView localTextView = this.mShortcutView;
        String str = this.mItemData.getShortcutLabel();
        localTextView.setText(str);
      }
      if (this.mShortcutView.getVisibility() != i)
        return;
      this.mShortcutView.setVisibility(i);
      return;
    }
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    if (paramCharSequence != null)
    {
      this.mTitleView.setText(paramCharSequence);
      if (this.mTitleView.getVisibility() == 0)
        return;
      this.mTitleView.setVisibility(0);
      return;
    }
    if (this.mTitleView.getVisibility() == 8)
      return;
    this.mTitleView.setVisibility(8);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.ListMenuItemView
 * JD-Core Version:    0.6.2
 */