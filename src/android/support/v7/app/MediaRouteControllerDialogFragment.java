package android.support.v7.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

public class MediaRouteControllerDialogFragment extends DialogFragment
{
  public MediaRouteControllerDialogFragment()
  {
    setCancelable(true);
  }

  public MediaRouteControllerDialog onCreateControllerDialog(Context paramContext, Bundle paramBundle)
  {
    return new MediaRouteControllerDialog(paramContext);
  }

  public Dialog onCreateDialog(Bundle paramBundle)
  {
    FragmentActivity localFragmentActivity = getActivity();
    return onCreateControllerDialog(localFragmentActivity, paramBundle);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.MediaRouteControllerDialogFragment
 * JD-Core Version:    0.6.2
 */