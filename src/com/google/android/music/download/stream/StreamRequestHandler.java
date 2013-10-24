package com.google.android.music.download.stream;

import android.content.Context;
import com.google.android.music.download.DownloadRequest;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.common.io.Closeables;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.RequestLine;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

public class StreamRequestHandler
  implements HttpRequestHandler
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private static final Pattern RANGE_PATTERN = Pattern.compile("bytes=(\\d+)-?(\\d*)");
  private final AllowedStreams mAllowedStreams;
  private volatile StreamingContent mStreamingContent;

  public StreamRequestHandler(AllowedStreams paramAllowedStreams)
  {
    this.mAllowedStreams = paramAllowedStreams;
  }

  public DownloadRequest getDownloadRequest()
  {
    if (this.mStreamingContent == null);
    for (DownloadRequest localDownloadRequest = null; ; localDownloadRequest = this.mStreamingContent.getDownloadRequest())
      return localDownloadRequest;
  }

  public void handle(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, HttpContext paramHttpContext)
  {
    RequestLine localRequestLine = paramHttpRequest.getRequestLine();
    String str1 = localRequestLine.getUri();
    if (LOGV)
    {
      String str2 = "handle: " + localRequestLine;
      Log.d("StreamRequestHandler", str2);
      Header[] arrayOfHeader = paramHttpRequest.getAllHeaders();
      int i = arrayOfHeader.length;
      int j = 0;
      while (j < i)
      {
        Header localHeader1 = arrayOfHeader[j];
        Object[] arrayOfObject1 = new Object[2];
        String str3 = localHeader1.getName();
        arrayOfObject1[0] = str3;
        String str4 = localHeader1.getValue();
        arrayOfObject1[1] = str4;
        String str5 = String.format("Header: %s: %s", arrayOfObject1);
        Log.d("StreamRequestHandler", str5);
        j += 1;
      }
    }
    int k = 47;
    int m = str1.lastIndexOf(k);
    int n = 65535;
    if (m == n)
    {
      String str6 = "Unknown URL requested: " + str1;
      throw new IllegalArgumentException(str6);
    }
    int i1 = m + 1;
    try
    {
      int i2 = str1.length();
      int i3 = i1;
      int i4 = i2;
      long l1 = Long.parseLong(str1.substring(i3, i4));
      long l2 = l1;
      AllowedStreams localAllowedStreams1 = this.mAllowedStreams;
      long l3 = l2;
      StreamingContent localStreamingContent1 = localAllowedStreams1.findStreamBySecureId(l3);
      this.mStreamingContent = localStreamingContent1;
      if (this.mStreamingContent == null)
      {
        if (LOGV)
        {
          StringBuilder localStringBuilder1 = new StringBuilder().append("Requesting file which is not allowed to be streamed: ");
          AllowedStreams localAllowedStreams2 = this.mAllowedStreams;
          String str7 = localAllowedStreams2;
          Log.w("StreamRequestHandler", str7);
        }
        HttpResponse localHttpResponse1 = paramHttpResponse;
        int i5 = 404;
        localHttpResponse1.setStatusCode(i5);
        return;
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      String str8 = "Unknown URL requested: " + str1;
      throw new IllegalArgumentException(str8);
    }
    HttpRequest localHttpRequest = paramHttpRequest;
    String str9 = "Range";
    Header localHeader2 = localHttpRequest.getLastHeader(str9);
    int i6 = 0;
    long l4 = 0L;
    if (localHeader2 != null)
    {
      Pattern localPattern = RANGE_PATTERN;
      String str10 = localHeader2.getValue();
      Matcher localMatcher = localPattern.matcher(str10);
      if (localMatcher.matches())
      {
        int i7 = 1;
        l4 = Long.parseLong(localMatcher.group(i7));
        if (LOGV)
        {
          StringBuilder localStringBuilder2 = new StringBuilder().append("Server requesting byte: ");
          long l5 = l4;
          String str11 = l5;
          Log.i("StreamRequestHandler", str11);
        }
        i6 = 1;
      }
    }
    Object localObject = null;
    if (MusicPreferences.isGingerbreadOrGreater())
      try
      {
        String str12 = this.mStreamingContent.waitForContentType();
        localObject = str12;
        long l6 = this.mStreamingContent.getStartReadPoint();
        l4 += l6;
        if (localObject == null)
        {
          Object[] arrayOfObject2 = new Object[1];
          StreamingContent localStreamingContent2 = this.mStreamingContent;
          arrayOfObject2[0] = localStreamingContent2;
          String str13 = String.format("Missing content type - exiting content=%s", arrayOfObject2);
          Log.e("StreamRequestHandler", str13);
          HttpResponse localHttpResponse2 = paramHttpResponse;
          int i8 = 404;
          localHttpResponse2.setStatusCode(i8);
          return;
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        while (true)
          Log.e("StreamRequestHandler", "Failed to retrieve content type");
      }
    if (LOGV)
    {
      String str14 = "The content type is: " + localObject;
      Log.d("StreamRequestHandler", str14);
    }
    if (localObject != null)
    {
      HttpResponse localHttpResponse3 = paramHttpResponse;
      String str15 = "Content-Type";
      localHttpResponse3.addHeader(str15, localObject);
    }
    HttpResponse localHttpResponse4 = paramHttpResponse;
    String str16 = "X-SocketTimeout";
    String str17 = "60";
    localHttpResponse4.addHeader(str16, str17);
    HttpResponse localHttpResponse5 = paramHttpResponse;
    String str18 = "Pragma";
    String str19 = "no-cache";
    localHttpResponse5.addHeader(str18, str19);
    HttpResponse localHttpResponse6 = paramHttpResponse;
    String str20 = "Cache-Control";
    String str21 = "no-cache";
    localHttpResponse6.addHeader(str20, str21);
    TailStream localTailStream1 = new com/google/android/music/download/stream/TailStream;
    Context localContext1 = this.mStreamingContent.getContext();
    StreamingContent localStreamingContent3 = this.mStreamingContent;
    TailStream localTailStream2 = localTailStream1;
    Context localContext2 = localContext1;
    StreamingContent localStreamingContent4 = localStreamingContent3;
    long l7 = l4;
    localTailStream2.<init>(localContext2, localStreamingContent4, l7);
    InputStreamEntity localInputStreamEntity1 = new com/google/android/music/download/stream/StreamRequestHandler$InputStreamEntity;
    String str22 = this.mStreamingContent.toString();
    InputStreamEntity localInputStreamEntity2 = localInputStreamEntity1;
    String str23 = str22;
    TailStream localTailStream3 = localTailStream1;
    Object local1 = null;
    localInputStreamEntity2.<init>(str23, localTailStream3, local1);
    HttpResponse localHttpResponse7 = paramHttpResponse;
    InputStreamEntity localInputStreamEntity3 = localInputStreamEntity1;
    localHttpResponse7.setEntity(localInputStreamEntity3);
    if (i6 != 0)
    {
      HttpResponse localHttpResponse8 = paramHttpResponse;
      int i9 = 206;
      localHttpResponse8.setStatusCode(i9);
      return;
    }
    HttpResponse localHttpResponse9 = paramHttpResponse;
    int i10 = 200;
    localHttpResponse9.setStatusCode(i10);
  }

  private static class InputStreamEntity extends AbstractHttpEntity
  {
    private boolean mConsumed = false;
    private final String mDebugPrefix;
    private InputStream mReadStream;

    private InputStreamEntity(String paramString, InputStream paramInputStream)
    {
      this.mDebugPrefix = paramString;
      this.mReadStream = paramInputStream;
    }

    public void consumeContent()
    {
      throw new UnsupportedOperationException();
    }

    public InputStream getContent()
    {
      return this.mReadStream;
    }

    public long getContentLength()
    {
      return 65535L;
    }

    public boolean isRepeatable()
    {
      return false;
    }

    public boolean isStreaming()
    {
      if (!this.mConsumed);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public void writeTo(OutputStream paramOutputStream)
      throws IOException
    {
      boolean bool = true;
      long l1 = 0L;
      try
      {
        paramOutputStream.flush();
        byte[] arrayOfByte = new byte[2048];
        while (true)
        {
          int i = this.mReadStream.read(arrayOfByte);
          if (i < 0)
            break;
          paramOutputStream.write(arrayOfByte, 0, i);
          long l2 = i;
          l1 += l2;
        }
        paramOutputStream.flush();
        StringBuilder localStringBuilder = new StringBuilder().append("Finished writeTo(");
        String str1 = this.mDebugPrefix;
        String str2 = str1 + ") " + l1;
        Log.i("StreamRequestHandler", str2);
        this.mConsumed = true;
        InputStream localInputStream1;
        return;
        bool = false;
      }
      finally
      {
        InputStream localInputStream2 = this.mReadStream;
        if (0 == 0)
          Closeables.close(localInputStream2, bool);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.stream.StreamRequestHandler
 * JD-Core Version:    0.6.2
 */