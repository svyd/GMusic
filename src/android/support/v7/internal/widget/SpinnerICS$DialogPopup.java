package android.support.v7.internal.widget;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.ListAdapter;

class SpinnerICS$DialogPopup
  implements DialogInterface.OnClickListener, SpinnerICS.SpinnerPopup
{
  private ListAdapter mListAdapter;
  private AlertDialog mPopup;
  private CharSequence mPrompt;

  private SpinnerICS$DialogPopup(SpinnerICS paramSpinnerICS)
  {
  }

  public void dismiss()
  {
    this.mPopup.dismiss();
    this.mPopup = null;
  }

  public boolean isShowing()
  {
    if (this.mPopup != null);
    for (boolean bool = this.mPopup.isShowing(); ; bool = false)
      return bool;
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    this.this$0.setSelection(paramInt);
    if (this.this$0.mOnItemClickListener != null)
    {
      SpinnerICS localSpinnerICS = this.this$0;
      long l = this.mListAdapter.getItemId(paramInt);
      boolean bool = localSpinnerICS.performItemClick(null, paramInt, l);
    }
    dismiss();
  }

  public void setAdapter(ListAdapter paramListAdapter)
  {
    this.mListAdapter = paramListAdapter;
  }

  public void setPromptText(CharSequence paramCharSequence)
  {
    this.mPrompt = paramCharSequence;
  }

  public void show()
  {
    Context localContext = this.this$0.getContext();
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(localContext);
    if (this.mPrompt != null)
    {
      CharSequence localCharSequence = this.mPrompt;
      AlertDialog.Builder localBuilder2 = localBuilder1.setTitle(localCharSequence);
    }
    ListAdapter localListAdapter = this.mListAdapter;
    int i = this.this$0.getSelectedItemPosition();
    AlertDialog localAlertDialog = localBuilder1.setSingleChoiceItems(localListAdapter, i, this).show();
    this.mPopup = localAlertDialog;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.SpinnerICS.DialogPopup
 * JD-Core Version:    0.6.2
 */