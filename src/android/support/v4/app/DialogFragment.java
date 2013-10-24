package android.support.v4.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public class DialogFragment extends Fragment
  implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener
{
  private static final String SAVED_BACK_STACK_ID = "android:backStackId";
  private static final String SAVED_CANCELABLE = "android:cancelable";
  private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";
  private static final String SAVED_SHOWS_DIALOG = "android:showsDialog";
  private static final String SAVED_STYLE = "android:style";
  private static final String SAVED_THEME = "android:theme";
  public static final int STYLE_NORMAL = 0;
  public static final int STYLE_NO_FRAME = 2;
  public static final int STYLE_NO_INPUT = 3;
  public static final int STYLE_NO_TITLE = 1;
  int mBackStackId = -1;
  boolean mCancelable = true;
  Dialog mDialog;
  boolean mDismissed;
  boolean mShownByMe;
  boolean mShowsDialog = true;
  int mStyle = 0;
  int mTheme = 0;
  boolean mViewDestroyed;

  public void dismiss()
  {
    dismissInternal(false);
  }

  public void dismissAllowingStateLoss()
  {
    dismissInternal(true);
  }

  void dismissInternal(boolean paramBoolean)
  {
    if (this.mDismissed)
      return;
    this.mDismissed = true;
    this.mShownByMe = false;
    if (this.mDialog != null)
    {
      this.mDialog.dismiss();
      this.mDialog = null;
    }
    this.mViewDestroyed = true;
    if (this.mBackStackId >= 0)
    {
      FragmentManager localFragmentManager = getFragmentManager();
      int i = this.mBackStackId;
      localFragmentManager.popBackStack(i, 1);
      this.mBackStackId = -1;
      return;
    }
    FragmentTransaction localFragmentTransaction1 = getFragmentManager().beginTransaction();
    FragmentTransaction localFragmentTransaction2 = localFragmentTransaction1.remove(this);
    if (paramBoolean)
    {
      int j = localFragmentTransaction1.commitAllowingStateLoss();
      return;
    }
    int k = localFragmentTransaction1.commit();
  }

  public Dialog getDialog()
  {
    return this.mDialog;
  }

  public LayoutInflater getLayoutInflater(Bundle paramBundle)
  {
    LayoutInflater localLayoutInflater;
    if (!this.mShowsDialog)
      localLayoutInflater = super.getLayoutInflater(paramBundle);
    while (true)
    {
      return localLayoutInflater;
      Dialog localDialog = onCreateDialog(paramBundle);
      this.mDialog = localDialog;
      switch (this.mStyle)
      {
      default:
      case 3:
      case 1:
      case 2:
      }
      while (true)
      {
        if (this.mDialog == null)
          break label107;
        localLayoutInflater = (LayoutInflater)this.mDialog.getContext().getSystemService("layout_inflater");
        break;
        this.mDialog.getWindow().addFlags(24);
        boolean bool = this.mDialog.requestWindowFeature(1);
      }
      label107: localLayoutInflater = (LayoutInflater)this.mActivity.getSystemService("layout_inflater");
    }
  }

  public boolean getShowsDialog()
  {
    return this.mShowsDialog;
  }

  public int getTheme()
  {
    return this.mTheme;
  }

  public boolean isCancelable()
  {
    return this.mCancelable;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (!this.mShowsDialog)
      return;
    View localView = getView();
    if (localView != null)
    {
      if (localView.getParent() != null)
        throw new IllegalStateException("DialogFragment can not be attached to a container view");
      this.mDialog.setContentView(localView);
    }
    Dialog localDialog1 = this.mDialog;
    FragmentActivity localFragmentActivity = getActivity();
    localDialog1.setOwnerActivity(localFragmentActivity);
    Dialog localDialog2 = this.mDialog;
    boolean bool = this.mCancelable;
    localDialog2.setCancelable(bool);
    this.mDialog.setOnCancelListener(this);
    this.mDialog.setOnDismissListener(this);
    if (paramBundle == null)
      return;
    Bundle localBundle = paramBundle.getBundle("android:savedDialogState");
    if (localBundle == null)
      return;
    this.mDialog.onRestoreInstanceState(localBundle);
  }

  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    if (this.mShownByMe)
      return;
    this.mDismissed = false;
  }

  public void onCancel(DialogInterface paramDialogInterface)
  {
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (this.mContainerId == 0);
    for (boolean bool1 = true; ; bool1 = false)
    {
      this.mShowsDialog = bool1;
      if (paramBundle == null)
        return;
      int i = paramBundle.getInt("android:style", 0);
      this.mStyle = i;
      int j = paramBundle.getInt("android:theme", 0);
      this.mTheme = j;
      boolean bool2 = paramBundle.getBoolean("android:cancelable", true);
      this.mCancelable = bool2;
      boolean bool3 = this.mShowsDialog;
      boolean bool4 = paramBundle.getBoolean("android:showsDialog", bool3);
      this.mShowsDialog = bool4;
      int k = paramBundle.getInt("android:backStackId", -1);
      this.mBackStackId = k;
      return;
    }
  }

  public Dialog onCreateDialog(Bundle paramBundle)
  {
    FragmentActivity localFragmentActivity = getActivity();
    int i = getTheme();
    return new Dialog(localFragmentActivity, i);
  }

  public void onDestroyView()
  {
    super.onDestroyView();
    if (this.mDialog == null)
      return;
    this.mViewDestroyed = true;
    this.mDialog.dismiss();
    this.mDialog = null;
  }

  public void onDetach()
  {
    super.onDetach();
    if (this.mShownByMe)
      return;
    if (this.mDismissed)
      return;
    this.mDismissed = true;
  }

  public void onDismiss(DialogInterface paramDialogInterface)
  {
    if (this.mViewDestroyed)
      return;
    dismissInternal(true);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.mDialog != null)
    {
      Bundle localBundle = this.mDialog.onSaveInstanceState();
      if (localBundle != null)
        paramBundle.putBundle("android:savedDialogState", localBundle);
    }
    if (this.mStyle != 0)
    {
      int i = this.mStyle;
      paramBundle.putInt("android:style", i);
    }
    if (this.mTheme != 0)
    {
      int j = this.mTheme;
      paramBundle.putInt("android:theme", j);
    }
    if (!this.mCancelable)
    {
      boolean bool1 = this.mCancelable;
      paramBundle.putBoolean("android:cancelable", bool1);
    }
    if (!this.mShowsDialog)
    {
      boolean bool2 = this.mShowsDialog;
      paramBundle.putBoolean("android:showsDialog", bool2);
    }
    if (this.mBackStackId == -1)
      return;
    int k = this.mBackStackId;
    paramBundle.putInt("android:backStackId", k);
  }

  public void onStart()
  {
    super.onStart();
    if (this.mDialog == null)
      return;
    this.mViewDestroyed = false;
    this.mDialog.show();
  }

  public void onStop()
  {
    super.onStop();
    if (this.mDialog == null)
      return;
    this.mDialog.hide();
  }

  public void setCancelable(boolean paramBoolean)
  {
    this.mCancelable = paramBoolean;
    if (this.mDialog == null)
      return;
    this.mDialog.setCancelable(paramBoolean);
  }

  public void setShowsDialog(boolean paramBoolean)
  {
    this.mShowsDialog = paramBoolean;
  }

  public void setStyle(int paramInt1, int paramInt2)
  {
    this.mStyle = paramInt1;
    if ((this.mStyle == 2) || (this.mStyle == 3))
      this.mTheme = 16973913;
    if (paramInt2 == 0)
      return;
    this.mTheme = paramInt2;
  }

  public int show(FragmentTransaction paramFragmentTransaction, String paramString)
  {
    this.mDismissed = false;
    this.mShownByMe = true;
    FragmentTransaction localFragmentTransaction = paramFragmentTransaction.add(this, paramString);
    this.mViewDestroyed = false;
    int i = paramFragmentTransaction.commit();
    this.mBackStackId = i;
    return this.mBackStackId;
  }

  public void show(FragmentManager paramFragmentManager, String paramString)
  {
    this.mDismissed = false;
    this.mShownByMe = true;
    FragmentTransaction localFragmentTransaction1 = paramFragmentManager.beginTransaction();
    FragmentTransaction localFragmentTransaction2 = localFragmentTransaction1.add(this, paramString);
    int i = localFragmentTransaction1.commit();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.DialogFragment
 * JD-Core Version:    0.6.2
 */