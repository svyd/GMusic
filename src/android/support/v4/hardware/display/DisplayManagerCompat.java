package android.support.v4.hardware.display;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.WindowManager;
import java.util.WeakHashMap;

public abstract class DisplayManagerCompat
{
  private static final WeakHashMap<Context, DisplayManagerCompat> sInstances = new WeakHashMap();

  public static DisplayManagerCompat getInstance(Context paramContext)
  {
    synchronized (sInstances)
    {
      Object localObject1 = (DisplayManagerCompat)sInstances.get(paramContext);
      if (localObject1 == null)
      {
        if (Build.VERSION.SDK_INT >= 17)
        {
          localObject1 = new JellybeanMr1Impl(paramContext);
          Object localObject2 = sInstances.put(paramContext, localObject1);
        }
      }
      else
        return localObject1;
      localObject1 = new LegacyImpl(paramContext);
    }
  }

  private static class JellybeanMr1Impl extends DisplayManagerCompat
  {
    private final Object mDisplayManagerObj;

    public JellybeanMr1Impl(Context paramContext)
    {
      Object localObject = DisplayManagerJellybeanMr1.getDisplayManager(paramContext);
      this.mDisplayManagerObj = localObject;
    }
  }

  private static class LegacyImpl extends DisplayManagerCompat
  {
    private final WindowManager mWindowManager;

    public LegacyImpl(Context paramContext)
    {
      WindowManager localWindowManager = (WindowManager)paramContext.getSystemService("window");
      this.mWindowManager = localWindowManager;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.hardware.display.DisplayManagerCompat
 * JD-Core Version:    0.6.2
 */