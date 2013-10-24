package com.mobeta.android.dslv;

import android.content.Context;
import android.view.View;
import android.widget.Checkable;

public class DragSortItemViewCheckable extends DragSortItemView
  implements Checkable
{
  public DragSortItemViewCheckable(Context paramContext)
  {
    super(paramContext);
  }

  public boolean isChecked()
  {
    int i = 0;
    View localView = getChildAt(i);
    boolean bool;
    if ((localView instanceof Checkable))
      bool = ((Checkable)localView).isChecked();
    return bool;
  }

  public void setChecked(boolean paramBoolean)
  {
    View localView = getChildAt(0);
    if (!(localView instanceof Checkable))
      return;
    ((Checkable)localView).setChecked(paramBoolean);
  }

  public void toggle()
  {
    View localView = getChildAt(0);
    if (!(localView instanceof Checkable))
      return;
    ((Checkable)localView).toggle();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.mobeta.android.dslv.DragSortItemViewCheckable
 * JD-Core Version:    0.6.2
 */