package org.codehaus.jackson;

public enum JsonEncoding
{
  final boolean mBigEndian;
  final String mJavaName;

  static
  {
    UTF16_BE = new JsonEncoding("UTF16_BE", 1, "UTF-16BE", true);
    UTF16_LE = new JsonEncoding("UTF16_LE", 2, "UTF-16LE", false);
    UTF32_BE = new JsonEncoding("UTF32_BE", 3, "UTF-32BE", true);
    UTF32_LE = new JsonEncoding("UTF32_LE", 4, "UTF-32LE", false);
    JsonEncoding[] arrayOfJsonEncoding = new JsonEncoding[5];
    JsonEncoding localJsonEncoding1 = UTF8;
    arrayOfJsonEncoding[0] = localJsonEncoding1;
    JsonEncoding localJsonEncoding2 = UTF16_BE;
    arrayOfJsonEncoding[1] = localJsonEncoding2;
    JsonEncoding localJsonEncoding3 = UTF16_LE;
    arrayOfJsonEncoding[2] = localJsonEncoding3;
    JsonEncoding localJsonEncoding4 = UTF32_BE;
    arrayOfJsonEncoding[3] = localJsonEncoding4;
    JsonEncoding localJsonEncoding5 = UTF32_LE;
    arrayOfJsonEncoding[4] = localJsonEncoding5;
  }

  private JsonEncoding(String paramString, boolean paramBoolean)
  {
    this.mJavaName = paramString;
    this.mBigEndian = paramBoolean;
  }

  public String getJavaName()
  {
    return this.mJavaName;
  }

  public boolean isBigEndian()
  {
    return this.mBigEndian;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.JsonEncoding
 * JD-Core Version:    0.6.2
 */