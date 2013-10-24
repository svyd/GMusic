package android.support.v4.content;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build.VERSION;

public class IntentCompat
{
  private static final IntentCompatImpl IMPL = new IntentCompatImplBase();

  static
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 15)
    {
      IMPL = new IntentCompatImplIcsMr1();
      return;
    }
    if (i >= 11)
    {
      IMPL = new IntentCompatImplHC();
      return;
    }
  }

  public static Intent makeMainActivity(ComponentName paramComponentName)
  {
    return IMPL.makeMainActivity(paramComponentName);
  }

  static class IntentCompatImplIcsMr1 extends IntentCompat.IntentCompatImplHC
  {
  }

  static class IntentCompatImplHC extends IntentCompat.IntentCompatImplBase
  {
    public Intent makeMainActivity(ComponentName paramComponentName)
    {
      return IntentCompatHoneycomb.makeMainActivity(paramComponentName);
    }
  }

  static class IntentCompatImplBase
    implements IntentCompat.IntentCompatImpl
  {
    public Intent makeMainActivity(ComponentName paramComponentName)
    {
      Intent localIntent1 = new Intent("android.intent.action.MAIN");
      Intent localIntent2 = localIntent1.setComponent(paramComponentName);
      Intent localIntent3 = localIntent1.addCategory("android.intent.category.LAUNCHER");
      return localIntent1;
    }
  }

  static abstract interface IntentCompatImpl
  {
    public abstract Intent makeMainActivity(ComponentName paramComponentName);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.content.IntentCompat
 * JD-Core Version:    0.6.2
 */