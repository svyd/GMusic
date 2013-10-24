package com.google.common.base;

public final class Throwables
{
  public static <X extends Throwable> void propagateIfInstanceOf(Throwable paramThrowable, Class<X> paramClass)
    throws Throwable
  {
    if (paramThrowable == null)
      return;
    if (!paramClass.isInstance(paramThrowable))
      return;
    throw ((Throwable)paramClass.cast(paramThrowable));
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Throwables
 * JD-Core Version:    0.6.2
 */