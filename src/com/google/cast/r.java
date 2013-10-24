package com.google.cast;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

final class r
{
  public static void a(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    if (paramXmlPullParser.getEventType() != 2)
      throw new IllegalStateException();
    int i = 1;
    while (true)
    {
      if (i == 0)
        return;
      switch (paramXmlPullParser.next())
      {
      default:
        break;
      case 2:
        i += 1;
        break;
      case 3:
        i += -1;
      }
    }
  }

  public static String b(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    String str = paramXmlPullParser.nextText();
    if (paramXmlPullParser.getEventType() != 3)
      int i = paramXmlPullParser.nextTag();
    return str;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.r
 * JD-Core Version:    0.6.2
 */