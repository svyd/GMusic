package android.support.v4.internal.view;

import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.View;

public abstract interface SupportMenuItem extends MenuItem
{
  public abstract boolean expandActionView();

  public abstract View getActionView();

  public abstract ActionProvider getSupportActionProvider();

  public abstract MenuItem setActionView(int paramInt);

  public abstract MenuItem setActionView(View paramView);

  public abstract void setShowAsAction(int paramInt);

  public abstract SupportMenuItem setSupportActionProvider(ActionProvider paramActionProvider);
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.internal.view.SupportMenuItem
 * JD-Core Version:    0.6.2
 */