package com.google.android.music;

public abstract interface AdapterObservable
{
  public abstract void registerAdapterObserver(AdapterObserver paramAdapterObserver);

  public abstract void unregisterAdapterObserver(AdapterObserver paramAdapterObserver);

  public static abstract interface AdapterObserver
  {
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.AdapterObservable
 * JD-Core Version:    0.6.2
 */