package android.support.v7.internal.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SpinnerAdapter;

class SpinnerICS$DropdownPopup extends ListPopupWindow
  implements SpinnerICS.SpinnerPopup
{
  private ListAdapter mAdapter;
  private CharSequence mHintText;

  public SpinnerICS$DropdownPopup(final SpinnerICS paramSpinnerICS, Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    setAnchorView(paramSpinnerICS);
    setModal(true);
    setPromptPosition(0);
    AdapterViewICS.OnItemClickListener local1 = new AdapterViewICS.OnItemClickListener()
    {
      public void onItemClick(AdapterViewICS paramAnonymousAdapterViewICS, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        SpinnerICS.DropdownPopup.this.this$0.setSelection(paramAnonymousInt);
        if (SpinnerICS.DropdownPopup.this.this$0.mOnItemClickListener != null)
        {
          SpinnerICS localSpinnerICS = SpinnerICS.DropdownPopup.this.this$0;
          long l = SpinnerICS.DropdownPopup.this.mAdapter.getItemId(paramAnonymousInt);
          boolean bool = localSpinnerICS.performItemClick(paramAnonymousView, paramAnonymousInt, l);
        }
        SpinnerICS.DropdownPopup.this.dismiss();
      }
    };
    AdapterViewICS.OnItemClickListenerWrapper localOnItemClickListenerWrapper = new AdapterViewICS.OnItemClickListenerWrapper(paramSpinnerICS, local1);
    setOnItemClickListener(localOnItemClickListenerWrapper);
  }

  public void setAdapter(ListAdapter paramListAdapter)
  {
    super.setAdapter(paramListAdapter);
    this.mAdapter = paramListAdapter;
  }

  public void setPromptText(CharSequence paramCharSequence)
  {
    this.mHintText = paramCharSequence;
  }

  public void show()
  {
    int i = this.this$0.getPaddingLeft();
    if (this.this$0.mDropDownWidth == -1)
    {
      int j = this.this$0.getWidth();
      int k = this.this$0.getPaddingRight();
      SpinnerICS localSpinnerICS = this.this$0;
      SpinnerAdapter localSpinnerAdapter = (SpinnerAdapter)this.mAdapter;
      Drawable localDrawable1 = getBackground();
      int m = localSpinnerICS.measureContentWidth(localSpinnerAdapter, localDrawable1);
      int n = j - i - k;
      int i1 = Math.max(m, n);
      setContentWidth(i1);
    }
    while (true)
    {
      Drawable localDrawable2 = getBackground();
      int i2 = 0;
      if (localDrawable2 != null)
      {
        Rect localRect = SpinnerICS.access$200(this.this$0);
        boolean bool = localDrawable2.getPadding(localRect);
        i2 = -SpinnerICS.access$200(this.this$0).left;
      }
      int i3 = i2 + i;
      setHorizontalOffset(i3);
      setInputMethodMode(2);
      super.show();
      getListView().setChoiceMode(1);
      int i4 = this.this$0.getSelectedItemPosition();
      setSelection(i4);
      return;
      if (this.this$0.mDropDownWidth == -1)
      {
        int i5 = this.this$0.getWidth();
        int i6 = this.this$0.getPaddingRight();
        int i7 = i5 - i - i6;
        setContentWidth(i7);
      }
      else
      {
        int i8 = this.this$0.mDropDownWidth;
        setContentWidth(i8);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.SpinnerICS.DropdownPopup
 * JD-Core Version:    0.6.2
 */