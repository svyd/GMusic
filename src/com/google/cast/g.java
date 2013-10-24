package com.google.cast;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.util.Xml;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class g extends NetworkRequest
{
  protected Uri a;
  protected Uri b;
  private ApplicationMetadata c;
  private int d;
  private Uri e;
  private Uri f;
  private String g;
  private boolean h;
  private boolean i;
  private Logger j;

  public g(CastContext paramCastContext, Uri paramUri)
  {
    this(paramCastContext, paramUri, null);
  }

  public g(CastContext paramCastContext, Uri paramUri, String paramString)
  {
    super(paramCastContext);
    if (paramString == null);
    Uri localUri;
    for (this.a = paramUri; ; this.a = localUri)
    {
      this.d = 0;
      Logger localLogger = new Logger("GetApplicationInfoRequest");
      this.j = localLogger;
      return;
      localUri = paramUri.buildUpon().appendPath(paramString).build();
    }
  }

  private void a(XmlPullParser paramXmlPullParser, ApplicationMetadata.a parama)
    throws XmlPullParserException, IOException
  {
    paramXmlPullParser.require(2, "urn:chrome.google.com:cast", "servicedata");
    while (true)
    {
      if (paramXmlPullParser.next() == 3)
        return;
      if (paramXmlPullParser.getEventType() == 2)
      {
        String str1 = paramXmlPullParser.getName();
        if (str1.equals("connectionSvcURL"))
        {
          Uri localUri = Uri.parse(r.b(paramXmlPullParser));
          this.f = localUri;
        }
        else if (str1.equals("applicationContext"))
        {
          String str2 = r.b(paramXmlPullParser);
          this.g = str2;
        }
        else if (str1.equals("protocols"))
        {
          c(paramXmlPullParser, parama);
        }
        else
        {
          r.a(paramXmlPullParser);
        }
      }
    }
  }

  private void b(XmlPullParser paramXmlPullParser, ApplicationMetadata.a parama)
    throws XmlPullParserException, IOException
  {
    paramXmlPullParser.require(2, "urn:chrome.google.com:cast", "activity-status");
    while (true)
    {
      if (paramXmlPullParser.next() == 3)
        return;
      if (paramXmlPullParser.getEventType() == 2)
      {
        String str1 = paramXmlPullParser.getName();
        if (str1.equals("description"))
        {
          String str2 = r.b(paramXmlPullParser);
          ApplicationMetadata.a locala1 = parama.a(str2);
        }
        else if (str1.equals("image"))
        {
          String str3 = paramXmlPullParser.getAttributeValue("", "src");
          if (str3 != null)
          {
            Uri localUri = Uri.parse(str3);
            ApplicationMetadata.a locala2 = parama.a(localUri);
          }
        }
        else
        {
          r.a(paramXmlPullParser);
        }
      }
    }
  }

  private void c(XmlPullParser paramXmlPullParser, ApplicationMetadata.a parama)
    throws XmlPullParserException, IOException
  {
    ArrayList localArrayList = new ArrayList();
    while (paramXmlPullParser.next() != 3)
      if (paramXmlPullParser.getEventType() == 2)
      {
        paramXmlPullParser.require(2, null, "protocol");
        String str = r.b(paramXmlPullParser);
        if (!TextUtils.isEmpty(str))
          boolean bool = localArrayList.add(str);
      }
    ApplicationMetadata.a locala = parama.a(localArrayList);
  }

  public final ApplicationMetadata a()
  {
    return this.c;
  }

  void a(Uri paramUri, String paramString)
    throws XmlPullParserException, IOException
  {
    XmlPullParser localXmlPullParser = Xml.newPullParser();
    StringReader localStringReader = new StringReader(paramString);
    localXmlPullParser.setInput(localStringReader);
    int k = localXmlPullParser.nextTag();
    localXmlPullParser.require(2, "urn:dial-multiscreen-org:schemas:dial", "service");
    ApplicationMetadata.a locala = null;
    while (localXmlPullParser.next() != 3)
      if (localXmlPullParser.getEventType() == 2)
      {
        String str1 = localXmlPullParser.getName();
        if (str1.equals("name"))
        {
          str1 = r.b(localXmlPullParser);
          if (locala != null)
            throw new XmlPullParserException("Invalid XML, multiple application name received");
          locala = new ApplicationMetadata.a(str1);
        }
        else if (str1.equals("options"))
        {
          String str2 = localXmlPullParser.getAttributeValue(null, "allowStop");
          if ("true".equalsIgnoreCase(str2))
            this.h = true;
          r.a(localXmlPullParser);
        }
        else if (str1.equals("state"))
        {
          str1 = r.b(localXmlPullParser);
          if (str1.equalsIgnoreCase("running"))
          {
            this.d = 1;
          }
          else if (str1.equalsIgnoreCase("stopped"))
          {
            this.d = 2;
          }
          else if (str1.toLowerCase().startsWith("installable"))
          {
            this.d = 3;
            int m = "installable".length();
            Uri localUri1 = Uri.parse(str1.substring(m));
            this.e = localUri1;
          }
        }
        else if (str1.equals("link"))
        {
          String str3 = localXmlPullParser.getAttributeValue(null, "href");
          Uri localUri2 = paramUri.buildUpon().appendPath(str3).build();
          this.b = localUri2;
          r.a(localXmlPullParser);
        }
        else if (str1.equals("servicedata"))
        {
          this.i = true;
          a(localXmlPullParser, locala);
        }
        else if (str1.equals("activity-status"))
        {
          if (locala == null)
            throw new XmlPullParserException("missing name element.");
          b(localXmlPullParser, locala);
        }
      }
    if (locala == null)
      throw new XmlPullParserException("missing name element.");
    ApplicationMetadata localApplicationMetadata = locala.a();
    this.c = localApplicationMetadata;
  }

  public final int b()
  {
    return this.d;
  }

  public final Uri c()
  {
    return this.b;
  }

  public final Uri d()
  {
    return this.f;
  }

  protected boolean e()
  {
    return this.i;
  }

  public final int execute()
  {
    Object localObject1 = null;
    int k = 0;
    try
    {
      while (true)
      {
        try
        {
          Uri localUri1 = this.a;
          int n = DEFAULT_TIMEOUT;
          localObject3 = performHttpGet(localUri1, n);
          i1 = ((SimpleHttpRequest)localObject3).getResponseStatus();
          if (i1 == 404)
          {
            k = -1;
            return k;
          }
          if (i1 == 204)
          {
            Logger localLogger1 = this.j;
            Object[] arrayOfObject1 = new Object[0];
            localLogger1.d("No current application running.", arrayOfObject1);
            continue;
          }
        }
        catch (TimeoutException localTimeoutException)
        {
          int i1;
          k = -1;
          continue;
          if (i1 != 200)
          {
            k = -1;
            continue;
          }
          localMimeData = ((SimpleHttpRequest)localObject3).getResponseData();
          Uri localUri2 = ((SimpleHttpRequest)localObject3).getFinalUri();
          localObject1 = localUri2;
          if (localMimeData != null)
            break;
        }
        k = -1;
      }
    }
    catch (IOException localIOException2)
    {
      while (true)
      {
        MimeData localMimeData;
        Object localObject2 = localObject1;
        continue;
        int m;
        if (localMimeData != null)
        {
          String str = localMimeData.getType();
          if ("application/xml".equals(str));
        }
        else
        {
          m = -1;
          continue;
        }
        Object localObject3 = localMimeData.getTextData();
        if (localObject3 == null)
          m = -1;
        else
          try
          {
            a(localObject1, (String)localObject3);
          }
          catch (IOException localIOException1)
          {
            Logger localLogger2 = this.j;
            Object[] arrayOfObject2 = new Object[1];
            arrayOfObject2[0] = localIOException1;
            localLogger2.e("parse error", arrayOfObject2);
            m = -1;
          }
          catch (XmlPullParserException localXmlPullParserException)
          {
            Logger localLogger3 = this.j;
            Object[] arrayOfObject3 = new Object[1];
            arrayOfObject3[0] = localXmlPullParserException;
            localLogger3.e("parse error", arrayOfObject3);
            m = -1;
          }
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.g
 * JD-Core Version:    0.6.2
 */