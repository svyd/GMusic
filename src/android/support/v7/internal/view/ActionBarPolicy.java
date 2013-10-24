package android.support.v7.internal.view;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.bool;
import android.support.v7.appcompat.R.dimen;
import android.support.v7.appcompat.R.integer;
import android.support.v7.appcompat.R.styleable;
import android.util.DisplayMetrics;

public class ActionBarPolicy
{
  private Context mContext;

  private ActionBarPolicy(Context paramContext)
  {
    this.mContext = paramContext;
  }

  public static ActionBarPolicy get(Context paramContext)
  {
    return new ActionBarPolicy(paramContext);
  }

  public boolean enableHomeButtonByDefault()
  {
    if (this.mContext.getApplicationInfo().targetSdkVersion < 14);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public int getEmbeddedMenuWidthLimit()
  {
    return this.mContext.getResources().getDisplayMetrics().widthPixels / 2;
  }

  public int getMaxActionButtons()
  {
    Resources localResources = this.mContext.getResources();
    int i = R.integer.abc_max_action_buttons;
    return localResources.getInteger(i);
  }

  public int getStackedTabMaxWidth()
  {
    Resources localResources = this.mContext.getResources();
    int i = R.dimen.abc_action_bar_stacked_tab_max_width;
    return localResources.getDimensionPixelSize(i);
  }

  public int getTabContainerHeight()
  {
    Context localContext = this.mContext;
    int[] arrayOfInt = R.styleable.ActionBar;
    int i = R.attr.actionBarStyle;
    TypedArray localTypedArray = localContext.obtainStyledAttributes(null, arrayOfInt, i, 0);
    int j = localTypedArray.getLayoutDimension(1, 0);
    Resources localResources = this.mContext.getResources();
    if (!hasEmbeddedTabs())
    {
      int k = R.dimen.abc_action_bar_stacked_max_height;
      int m = localResources.getDimensionPixelSize(k);
      j = Math.min(j, m);
    }
    localTypedArray.recycle();
    return j;
  }

  public boolean hasEmbeddedTabs()
  {
    Resources localResources = this.mContext.getResources();
    int i = R.bool.abc_action_bar_embed_tabs_pre_jb;
    return localResources.getBoolean(i);
  }

  public boolean showsOverflowMenuButton()
  {
    if (Build.VERSION.SDK_INT >= 11);
    for (boolean bool = true; ; bool = false)
      return bool;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.ActionBarPolicy
 * JD-Core Version:    0.6.2
 */