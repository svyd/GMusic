package android.support.v7.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.media.MediaRouteSelector;

public class MediaRouteChooserDialogFragment extends DialogFragment
{
  private final String ARGUMENT_SELECTOR = "selector";
  private MediaRouteSelector mSelector;

  public MediaRouteChooserDialogFragment()
  {
    setCancelable(true);
  }

  private void ensureRouteSelector()
  {
    if (this.mSelector != null)
      return;
    Bundle localBundle = getArguments();
    if (localBundle != null)
    {
      MediaRouteSelector localMediaRouteSelector1 = MediaRouteSelector.fromBundle(localBundle.getBundle("selector"));
      this.mSelector = localMediaRouteSelector1;
    }
    if (this.mSelector != null)
      return;
    MediaRouteSelector localMediaRouteSelector2 = MediaRouteSelector.EMPTY;
    this.mSelector = localMediaRouteSelector2;
  }

  public MediaRouteSelector getRouteSelector()
  {
    ensureRouteSelector();
    return this.mSelector;
  }

  public MediaRouteChooserDialog onCreateChooserDialog(Context paramContext, Bundle paramBundle)
  {
    return new MediaRouteChooserDialog(paramContext);
  }

  public Dialog onCreateDialog(Bundle paramBundle)
  {
    FragmentActivity localFragmentActivity = getActivity();
    MediaRouteChooserDialog localMediaRouteChooserDialog = onCreateChooserDialog(localFragmentActivity, paramBundle);
    MediaRouteSelector localMediaRouteSelector = getRouteSelector();
    localMediaRouteChooserDialog.setRouteSelector(localMediaRouteSelector);
    return localMediaRouteChooserDialog;
  }

  public void setRouteSelector(MediaRouteSelector paramMediaRouteSelector)
  {
    if (paramMediaRouteSelector == null)
      throw new IllegalArgumentException("selector must not be null");
    ensureRouteSelector();
    if (this.mSelector.equals(paramMediaRouteSelector))
      return;
    this.mSelector = paramMediaRouteSelector;
    Bundle localBundle1 = getArguments();
    if (localBundle1 == null)
      localBundle1 = new Bundle();
    Bundle localBundle2 = paramMediaRouteSelector.asBundle();
    localBundle1.putBundle("selector", localBundle2);
    setArguments(localBundle1);
    MediaRouteChooserDialog localMediaRouteChooserDialog = (MediaRouteChooserDialog)getDialog();
    if (localMediaRouteChooserDialog == null)
      return;
    localMediaRouteChooserDialog.setRouteSelector(paramMediaRouteSelector);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.MediaRouteChooserDialogFragment
 * JD-Core Version:    0.6.2
 */