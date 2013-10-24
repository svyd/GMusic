package android.support.v7.internal.view;

import android.content.Context;
import android.view.MenuInflater;

public class ActionModeWrapper extends android.support.v7.view.ActionMode
{
  final MenuInflater mInflater;
  final android.view.ActionMode mWrappedObject;

  public ActionModeWrapper(Context paramContext, android.view.ActionMode paramActionMode)
  {
    this.mWrappedObject = paramActionMode;
    SupportMenuInflater localSupportMenuInflater = new SupportMenuInflater(paramContext);
    this.mInflater = localSupportMenuInflater;
  }

  public void finish()
  {
    this.mWrappedObject.finish();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.ActionModeWrapper
 * JD-Core Version:    0.6.2
 */