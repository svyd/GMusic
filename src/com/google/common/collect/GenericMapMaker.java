package com.google.common.collect;

import com.google.common.base.Objects;

public abstract class GenericMapMaker<K0, V0>
{
  MapMaker.RemovalListener<K0, V0> removalListener;

  <K extends K0, V extends V0> MapMaker.RemovalListener<K, V> getRemovalListener()
  {
    MapMaker.RemovalListener localRemovalListener = this.removalListener;
    NullListener localNullListener = NullListener.INSTANCE;
    return (MapMaker.RemovalListener)Objects.firstNonNull(localRemovalListener, localNullListener);
  }

  static enum NullListener
    implements MapMaker.RemovalListener<Object, Object>
  {
    static
    {
      NullListener[] arrayOfNullListener = new NullListener[1];
      NullListener localNullListener = INSTANCE;
      arrayOfNullListener[0] = localNullListener;
    }

    public void onRemoval(MapMaker.RemovalNotification<Object, Object> paramRemovalNotification)
    {
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.GenericMapMaker
 * JD-Core Version:    0.6.2
 */