package com.google.cast;

import android.os.Looper;

final class p
{
  public static void a()
    throws IllegalStateException
  {
    Looper localLooper1 = Looper.myLooper();
    Looper localLooper2 = Looper.getMainLooper();
    if (localLooper1 == localLooper2)
      return;
    throw new IllegalStateException("This method may only be called on the application's main thread.");
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.p
 * JD-Core Version:    0.6.2
 */