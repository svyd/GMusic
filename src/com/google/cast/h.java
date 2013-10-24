package com.google.cast;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Xml;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class h extends NetworkRequest
{
  private Uri a;
  private CastDevice b;
  private List<String> c;
  private Logger d;

  public h(CastContext paramCastContext, Uri paramUri, CastDevice paramCastDevice)
  {
    super(paramCastContext);
    this.a = paramUri;
    this.b = paramCastDevice;
    Logger localLogger = new Logger("GetDeviceDescriptorRequest");
    this.d = localLogger;
  }

  private void a(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    paramXmlPullParser.require(2, null, "root");
    while (true)
    {
      if (paramXmlPullParser.next() == 3)
        return;
      if (paramXmlPullParser.getEventType() == 2)
        if (paramXmlPullParser.getName().equals("device"))
          b(paramXmlPullParser);
        else
          r.a(paramXmlPullParser);
    }
  }

  private void b(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    paramXmlPullParser.require(2, null, "device");
    while (true)
    {
      if (paramXmlPullParser.next() == 3)
        return;
      if (paramXmlPullParser.getEventType() == 2)
      {
        String str1 = paramXmlPullParser.getName();
        if (str1.equalsIgnoreCase("friendlyName"))
        {
          CastDevice localCastDevice1 = this.b;
          String str2 = r.b(paramXmlPullParser);
          localCastDevice1.setFriendlyName(str2);
        }
        else if (str1.equalsIgnoreCase("manufacturer"))
        {
          CastDevice localCastDevice2 = this.b;
          String str3 = r.b(paramXmlPullParser);
          localCastDevice2.setManufacturer(str3);
        }
        else if (str1.equalsIgnoreCase("modelName"))
        {
          CastDevice localCastDevice3 = this.b;
          String str4 = r.b(paramXmlPullParser);
          localCastDevice3.setModelName(str4);
        }
        else if (str1.equalsIgnoreCase("UDN"))
        {
          str1 = r.b(paramXmlPullParser);
          if (str1.startsWith("uuid:"))
          {
            int i = "uuid:".length();
            String str5 = str1.substring(i);
          }
          this.b.setDeviceId(str1);
        }
        else if (str1.equalsIgnoreCase("serviceList"))
        {
          c(paramXmlPullParser);
        }
        else if (str1.equalsIgnoreCase("iconList"))
        {
          e(paramXmlPullParser);
        }
        else
        {
          r.a(paramXmlPullParser);
        }
      }
    }
  }

  private void c(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    ArrayList localArrayList = new ArrayList();
    this.c = localArrayList;
    paramXmlPullParser.require(2, null, "serviceList");
    while (true)
    {
      if (paramXmlPullParser.next() == 3)
        return;
      if (paramXmlPullParser.getEventType() == 2)
        if (paramXmlPullParser.getName().equalsIgnoreCase("service"))
        {
          String str = d(paramXmlPullParser);
          if (str != null)
            boolean bool = this.c.add(str);
        }
        else
        {
          r.a(paramXmlPullParser);
        }
    }
  }

  private String d(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    String str = null;
    paramXmlPullParser.require(2, str, "service");
    while (paramXmlPullParser.next() != 3)
      if (paramXmlPullParser.getEventType() == 2)
        if (paramXmlPullParser.getName().equalsIgnoreCase("serviceType"))
          str = r.b(paramXmlPullParser);
        else
          r.a(paramXmlPullParser);
    return str;
  }

  private void e(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    paramXmlPullParser.require(2, null, "iconList");
    ArrayList localArrayList = new ArrayList(1);
    while (paramXmlPullParser.next() != 3)
      if (paramXmlPullParser.getEventType() == 2)
        if (paramXmlPullParser.getName().equalsIgnoreCase("icon"))
        {
          CastDeviceIcon localCastDeviceIcon = f(paramXmlPullParser);
          if (localCastDeviceIcon != null)
            boolean bool = localArrayList.add(localCastDeviceIcon);
        }
        else
        {
          r.a(paramXmlPullParser);
        }
    this.b.setIcons(localArrayList);
  }

  private CastDeviceIcon f(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = null;
    paramXmlPullParser.require(2, (String)localObject1, "icon");
    int i = 0;
    int j = 0;
    int k = 0;
    Object localObject2 = localObject1;
    while (paramXmlPullParser.next() != 3)
      if (paramXmlPullParser.getEventType() == 2)
      {
        String str = paramXmlPullParser.getName();
        if (str.equalsIgnoreCase("width"))
          int m = Integer.parseInt(r.b(paramXmlPullParser));
        else if (str.equalsIgnoreCase("height"))
          int n = Integer.parseInt(r.b(paramXmlPullParser));
        else if (str.equalsIgnoreCase("depth"))
          int i1 = Integer.parseInt(r.b(paramXmlPullParser));
        else if (str.equalsIgnoreCase("url"))
          localObject2 = r.b(paramXmlPullParser);
        else
          r.a(paramXmlPullParser);
      }
    if ((TextUtils.isEmpty((CharSequence)localObject2)) || (k <= 0) || (j <= 0) || (i <= 0))
    {
      Logger localLogger = this.d;
      Object[] arrayOfObject = new Object[0];
      localLogger.d("Ignoring invalid icon entry", arrayOfObject);
    }
    while (true)
    {
      return localObject1;
      Uri localUri = Uri.parse(URI.create(this.a.toString()).resolve((String)localObject2).toString());
      localObject1 = new CastDeviceIcon(k, j, i, localUri);
    }
  }

  public final int execute()
  {
    int i = -1;
    try
    {
      Uri localUri1 = this.a;
      int j = DEFAULT_TIMEOUT;
      Object localObject = performHttpGet(localUri1, j);
      if (((SimpleHttpRequest)localObject).getResponseStatus() != 200);
      while (true)
      {
        label33: return i;
        localMimeData = ((SimpleHttpRequest)localObject).getResponseData();
        localObject = ((SimpleHttpRequest)localObject).getResponseHeader("application-url");
        if (localObject == null)
        {
          i = -1;
        }
        else
        {
          CastDevice localCastDevice = this.b;
          Uri localUri2 = Uri.parse((String)localObject);
          localCastDevice.setApplicationUrl(localUri2);
          if (!isCancelled())
            break;
          i = 65437;
        }
      }
    }
    catch (TimeoutException localTimeoutException)
    {
      while (true)
      {
        MimeData localMimeData;
        i = -1;
        continue;
        if (localMimeData != null)
        {
          String str1 = localMimeData.getType();
          if ("application/xml".equals(str1));
        }
        else
        {
          i = -1;
          continue;
        }
        if (localMimeData.getTextData() == null)
          i = -1;
        else
          try
          {
            XmlPullParser localXmlPullParser = Xml.newPullParser();
            String str2 = localMimeData.getTextData();
            StringReader localStringReader = new StringReader(str2);
            localXmlPullParser.setInput(localStringReader);
            int k = localXmlPullParser.nextTag();
            a(localXmlPullParser);
            i = 0;
          }
          catch (IOException localIOException1)
          {
            Logger localLogger1 = this.d;
            Object[] arrayOfObject1 = new Object[0];
            localLogger1.d(localIOException1, "exception", arrayOfObject1);
            i = -1;
          }
          catch (XmlPullParserException localXmlPullParserException)
          {
            Logger localLogger2 = this.d;
            Object[] arrayOfObject2 = new Object[0];
            localLogger2.d(localXmlPullParserException, "exception", arrayOfObject2);
            i = -1;
          }
      }
    }
    catch (IOException localIOException2)
    {
      break label33;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.h
 * JD-Core Version:    0.6.2
 */