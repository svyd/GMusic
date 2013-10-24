package android.support.v4.util;

public class DebugUtils
{
  public static void buildShortClassTag(Object paramObject, StringBuilder paramStringBuilder)
  {
    if (paramObject == null)
    {
      StringBuilder localStringBuilder1 = paramStringBuilder.append("null");
      return;
    }
    String str1 = paramObject.getClass().getSimpleName();
    if ((str1 == null) || (str1.length() <= 0))
    {
      str1 = paramObject.getClass().getName();
      int i = str1.lastIndexOf('.');
      if (i > 0)
      {
        int j = i + 1;
        str1 = str1.substring(j);
      }
    }
    StringBuilder localStringBuilder2 = paramStringBuilder.append(str1);
    StringBuilder localStringBuilder3 = paramStringBuilder.append('{');
    String str2 = Integer.toHexString(System.identityHashCode(paramObject));
    StringBuilder localStringBuilder4 = paramStringBuilder.append(str2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.util.DebugUtils
 * JD-Core Version:    0.6.2
 */