package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.KeyEvent;

public class KeyEventCompat
{
  static final KeyEventVersionImpl IMPL = new BaseKeyEventVersionImpl();

  static
  {
    if (Build.VERSION.SDK_INT >= 11)
    {
      IMPL = new HoneycombKeyEventVersionImpl();
      return;
    }
  }

  public static boolean hasModifiers(KeyEvent paramKeyEvent, int paramInt)
  {
    KeyEventVersionImpl localKeyEventVersionImpl = IMPL;
    int i = paramKeyEvent.getMetaState();
    return localKeyEventVersionImpl.metaStateHasModifiers(i, paramInt);
  }

  public static boolean hasNoModifiers(KeyEvent paramKeyEvent)
  {
    KeyEventVersionImpl localKeyEventVersionImpl = IMPL;
    int i = paramKeyEvent.getMetaState();
    return localKeyEventVersionImpl.metaStateHasNoModifiers(i);
  }

  public static void startTracking(KeyEvent paramKeyEvent)
  {
    IMPL.startTracking(paramKeyEvent);
  }

  static class HoneycombKeyEventVersionImpl extends KeyEventCompat.EclairKeyEventVersionImpl
  {
    public boolean metaStateHasModifiers(int paramInt1, int paramInt2)
    {
      return KeyEventCompatHoneycomb.metaStateHasModifiers(paramInt1, paramInt2);
    }

    public boolean metaStateHasNoModifiers(int paramInt)
    {
      return KeyEventCompatHoneycomb.metaStateHasNoModifiers(paramInt);
    }

    public int normalizeMetaState(int paramInt)
    {
      return KeyEventCompatHoneycomb.normalizeMetaState(paramInt);
    }
  }

  static class EclairKeyEventVersionImpl extends KeyEventCompat.BaseKeyEventVersionImpl
  {
    public void startTracking(KeyEvent paramKeyEvent)
    {
      KeyEventCompatEclair.startTracking(paramKeyEvent);
    }
  }

  static class BaseKeyEventVersionImpl
    implements KeyEventCompat.KeyEventVersionImpl
  {
    private static int metaStateFilterDirectionalModifiers(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      int i = 1;
      int j;
      int k;
      if ((paramInt2 & paramInt3) != 0)
      {
        j = 1;
        k = paramInt4 | paramInt5;
        if ((paramInt2 & k) == 0)
          break label51;
      }
      while (true)
        if (j != 0)
        {
          if (i != 0)
          {
            throw new IllegalArgumentException("bad arguments");
            j = 0;
            break;
            label51: i = 0;
            continue;
          }
          int m = k ^ 0xFFFFFFFF;
          paramInt1 &= m;
        }
      while (true)
      {
        return paramInt1;
        if (i != 0)
        {
          int n = paramInt3 ^ 0xFFFFFFFF;
          paramInt1 &= n;
        }
      }
    }

    public boolean metaStateHasModifiers(int paramInt1, int paramInt2)
    {
      int i = 1;
      if (metaStateFilterDirectionalModifiers(metaStateFilterDirectionalModifiers(normalizeMetaState(paramInt1) & 0xF7, paramInt2, i, 64, 128), paramInt2, 2, 16, 32) != paramInt2);
      while (true)
      {
        return i;
        int j = 0;
      }
    }

    public boolean metaStateHasNoModifiers(int paramInt)
    {
      if ((normalizeMetaState(paramInt) & 0xF7) == 0);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public int normalizeMetaState(int paramInt)
    {
      if ((paramInt & 0xC0) != 0)
        paramInt |= 1;
      if ((paramInt & 0x30) != 0)
        paramInt |= 2;
      return paramInt & 0xF7;
    }

    public void startTracking(KeyEvent paramKeyEvent)
    {
    }
  }

  static abstract interface KeyEventVersionImpl
  {
    public abstract boolean metaStateHasModifiers(int paramInt1, int paramInt2);

    public abstract boolean metaStateHasNoModifiers(int paramInt);

    public abstract void startTracking(KeyEvent paramKeyEvent);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.KeyEventCompat
 * JD-Core Version:    0.6.2
 */