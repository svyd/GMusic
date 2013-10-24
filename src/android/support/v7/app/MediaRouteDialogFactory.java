package android.support.v7.app;

public class MediaRouteDialogFactory
{
  private static final MediaRouteDialogFactory sDefault = new MediaRouteDialogFactory();

  public static MediaRouteDialogFactory getDefault()
  {
    return sDefault;
  }

  public MediaRouteChooserDialogFragment onCreateChooserDialogFragment()
  {
    return new MediaRouteChooserDialogFragment();
  }

  public MediaRouteControllerDialogFragment onCreateControllerDialogFragment()
  {
    return new MediaRouteControllerDialogFragment();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.MediaRouteDialogFactory
 * JD-Core Version:    0.6.2
 */