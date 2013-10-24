package com.google.android.music.ui.dialogs;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

public abstract class TwoButtonsDialog extends DialogFragment
{
  protected Bundle createArgs(String paramString1, String paramString2, String paramString3)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("message", paramString1);
    localBundle.putString("okLabel", paramString2);
    if (paramString3 != null)
      localBundle.putString("cancelLabel", paramString3);
    return localBundle;
  }

  public Dialog onCreateDialog(Bundle paramBundle)
  {
    DialogInterface.OnClickListener local1 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        TwoButtonsDialog.this.onOkClicked();
        TwoButtonsDialog.this.dismiss();
      }
    };
    FragmentActivity localFragmentActivity = getActivity();
    Bundle localBundle = getArguments();
    String str1 = localBundle.getString("message");
    String str2 = localBundle.getString("okLabel");
    String str3 = localBundle.getString("cancelLabel");
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(localFragmentActivity);
    AlertDialog.Builder localBuilder2 = localBuilder1.setMessage(str1);
    AlertDialog.Builder localBuilder3 = localBuilder1.setPositiveButton(str2, local1);
    if (str3 != null)
      AlertDialog.Builder localBuilder4 = localBuilder1.setNegativeButton(str3, null);
    return localBuilder1.create();
  }

  protected abstract void onOkClicked();
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.dialogs.TwoButtonsDialog
 * JD-Core Version:    0.6.2
 */