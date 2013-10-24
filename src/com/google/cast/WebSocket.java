package com.google.cast;

import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class WebSocket
{
  private static final byte[] a = { 13, 10, 13, 10 };
  private static Logger p;
  private Uri b;
  private int c;
  private String d;
  private String e;
  private Listener f;
  private SocketChannel g;
  private int h;
  private int i;
  private e j;
  private e k;
  private q l;
  private Charset m;
  private CharsetDecoder n;
  private boolean o;
  private int q;
  private byte r;
  private boolean s;
  private boolean t;
  private boolean u;
  private byte[] v;
  private String w;
  private int x;
  private long y;
  private long z;

  public WebSocket(String paramString1, String paramString2, int paramInt)
    throws IllegalArgumentException
  {
    if (paramInt < 4096)
      throw new IllegalArgumentException("bufferSize < MIN_BUFFER_SIZE");
    this.q = 0;
    this.d = paramString1;
    this.e = paramString2;
    q localq = q.a();
    this.l = localq;
    this.h = paramInt;
    int i1 = this.h + -14;
    this.i = i1;
    Charset localCharset = this.l.f();
    this.m = localCharset;
    CharsetDecoder localCharsetDecoder = this.m.newDecoder();
    this.n = localCharsetDecoder;
    this.z = 5000L;
    p = new Logger("WebSocket");
  }

  static String a(String paramString, Charset paramCharset)
  {
    try
    {
      String str1 = paramString + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
      MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-1");
      byte[] arrayOfByte1 = str1.getBytes(paramCharset);
      localMessageDigest.update(arrayOfByte1);
      byte[] arrayOfByte2 = localMessageDigest.digest();
      int i1 = arrayOfByte2.length;
      String str2 = Base64.encodeToString(arrayOfByte2, 0, i1, 2);
      str3 = str2;
      return str3;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      while (true)
      {
        Logger localLogger = p;
        Object[] arrayOfObject = new Object[0];
        localLogger.e(localNoSuchAlgorithmException, "unexpected Base64 encoding error", arrayOfObject);
        String str3 = null;
      }
    }
  }

  private void a(byte paramByte, byte[] paramArrayOfByte, boolean paramBoolean)
    throws IOException
  {
    if (paramArrayOfByte == null);
    for (int i1 = 0; ; i1 = paramArrayOfByte.length)
    {
      int i2 = this.k.h();
      int i3 = i1 + 14;
      if (i2 >= i3)
        break;
      throw new IOException("no room in buffer");
    }
    if (paramBoolean)
      paramByte = (byte)(paramByte | 0xFFFFFF80);
    boolean bool1 = this.k.a(paramByte);
    if (paramArrayOfByte == null)
      boolean bool2 = this.k.a(65408);
    while (true)
    {
      byte[] arrayOfByte = this.l.e();
      boolean bool3 = this.k.b(arrayOfByte, null);
      if (paramArrayOfByte == null)
        return;
      boolean bool4 = this.k.b(paramArrayOfByte, arrayOfByte);
      return;
      if (paramArrayOfByte.length < 126)
      {
        e locale1 = this.k;
        byte b1 = (byte)(paramArrayOfByte.length | 0xFFFFFF80);
        boolean bool5 = locale1.a(b1);
      }
      else if (paramArrayOfByte.length < 65536)
      {
        boolean bool6 = this.k.a((byte)-1);
        e locale2 = this.k;
        int i4 = paramArrayOfByte.length;
        boolean bool7 = locale2.b(i4);
      }
      else
      {
        boolean bool8 = this.k.a((byte)-1);
        e locale3 = this.k;
        long l1 = paramArrayOfByte.length;
        boolean bool9 = locale3.a(l1);
      }
    }
  }

  private void a(short paramShort)
    throws IOException
  {
    Logger localLogger = p;
    Object[] arrayOfObject = new Object[0];
    localLogger.d("writing closing handshake", arrayOfObject);
    byte[] arrayOfByte = new byte[2];
    int i1 = (byte)(paramShort >> 8 & 0xFF);
    arrayOfByte[0] = i1;
    int i2 = (byte)(paramShort & 0xFF);
    arrayOfByte[1] = i2;
    a((byte)8, arrayOfByte, true);
    this.s = true;
  }

  private static InetAddress b(String paramString)
    throws ClassNotFoundException, IOException
  {
    InetAddress localInetAddress = InetAddress.getByName(paramString);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localByteArrayOutputStream);
    localObjectOutputStream.writeObject(localInetAddress);
    localObjectOutputStream.close();
    byte[] arrayOfByte1 = localByteArrayOutputStream.toByteArray();
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length + 2];
    int i1 = arrayOfByte1.length + -2;
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i1);
    int i2 = i1 + 1;
    arrayOfByte2[i1] = 116;
    int i3 = i2 + 1;
    arrayOfByte2[i2] = 0;
    int i4 = i3 + 1;
    arrayOfByte2[i3] = 0;
    arrayOfByte2[i4] = 120;
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte2);
    ObjectInputStream localObjectInputStream = new ObjectInputStream(localByteArrayInputStream);
    Inet4Address localInet4Address = (Inet4Address)localObjectInputStream.readObject();
    localObjectInputStream.close();
    return localInet4Address;
  }

  private void b(int paramInt)
  {
    Logger localLogger = p;
    Object[] arrayOfObject = new Object[1];
    Integer localInteger = Integer.valueOf(paramInt);
    arrayOfObject[0] = localInteger;
    localLogger.d("doTeardown with error=%d", arrayOfObject);
    if (this.g != null);
    try
    {
      this.g.close();
      label41: this.g = null;
      this.b = null;
      this.j = null;
      this.k = null;
      if ((this.q == 1) || (this.q == 2));
      for (int i1 = 1; ; i1 = 0)
      {
        this.q = 0;
        this.o = true;
        if (this.f == null)
          return;
        if (i1 == 0)
          break;
        this.f.onConnectionFailed(paramInt);
        return;
      }
      Listener localListener = this.f;
      int i2 = this.x;
      localListener.onDisconnected(paramInt, i2);
      return;
    }
    catch (IOException localIOException)
    {
      break label41;
    }
  }

  private void j()
    throws IOException
  {
    this.q = 2;
    String str1 = this.l.d();
    this.w = str1;
    StringBuilder localStringBuilder1 = new StringBuilder();
    Object[] arrayOfObject1 = new Object[4];
    StringBuilder localStringBuilder2 = new StringBuilder();
    String str2 = this.b.getPath();
    StringBuilder localStringBuilder3 = localStringBuilder2.append(str2).append("?");
    String str3 = this.b.getQuery();
    String str4 = str3;
    arrayOfObject1[0] = str4;
    StringBuilder localStringBuilder4 = new StringBuilder();
    String str5 = this.b.getHost();
    StringBuilder localStringBuilder5 = localStringBuilder4.append(str5).append(":");
    int i1 = this.c;
    String str6 = i1;
    arrayOfObject1[1] = str6;
    String str7 = this.w;
    arrayOfObject1[2] = str7;
    String str8 = this.d;
    arrayOfObject1[3] = str8;
    String str9 = String.format("GET %s HTTP/1.1\r\nHost: %s\r\nUpgrade: WebSocket\r\nConnection: Upgrade\r\nSec-WebSocket-Key: %s\r\nOrigin: %s\r\nSec-WebSocket-Version: 13\r\n", arrayOfObject1);
    StringBuilder localStringBuilder6 = localStringBuilder1.append(str9);
    if (!TextUtils.isEmpty(this.e))
    {
      StringBuilder localStringBuilder7 = localStringBuilder1.append("Sec-WebSocket-Protocol").append(": ");
      String str10 = this.e;
      StringBuilder localStringBuilder8 = localStringBuilder7.append(str10).append("\r\n");
    }
    StringBuilder localStringBuilder9 = localStringBuilder1.append("\r\n");
    String str11 = localStringBuilder1.toString();
    Logger localLogger = p;
    Object[] arrayOfObject2 = new Object[1];
    arrayOfObject2[0] = str11;
    localLogger.d("sending handshake:\n%s", arrayOfObject2);
    e locale = this.k;
    Charset localCharset = this.m;
    boolean bool = locale.a(str11, null, localCharset);
  }

  private boolean k()
    throws IOException
  {
    boolean bool1 = true;
    e locale1 = this.j;
    byte[] arrayOfByte = a;
    int i1 = locale1.a(arrayOfByte);
    if (i1 < 0);
    while (true)
    {
      return bool1;
      String str1;
      try
      {
        e locale2 = this.j;
        CharsetDecoder localCharsetDecoder = this.n;
        str1 = locale2.a(i1, null, localCharsetDecoder);
        if (str1 == null)
          throw new IOException("Invalid opening handshake reply");
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        String str2 = localUnsupportedEncodingException.toString();
        throw new f(str2);
      }
      Logger localLogger1 = p;
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = str1;
      localLogger1.d("handshake reply:\n%s", arrayOfObject1);
      String[] arrayOfString = str1.split("\r\n");
      if (arrayOfString.length == 0)
        throw new IOException("Invalid opening handshake reply");
      if (!arrayOfString[0].toUpperCase().matches("^HTTP\\/[0-9\\.]+\\s+101\\s+.*$"))
      {
        Logger localLogger2 = p;
        Object[] arrayOfObject2 = new Object[1];
        String str3 = arrayOfString[0];
        arrayOfObject2[0] = str3;
        localLogger2.w("Unexpected HTTP status: %s", arrayOfObject2);
        bool1 = false;
      }
      else
      {
        str1 = null;
        boolean bool2 = false;
        int i2 = arrayOfString.length;
        if (str1 < i2)
        {
          String str4 = arrayOfString[str1].toLowerCase();
          String str6;
          String str8;
          if (str4.startsWith("sec-websocket-accept: "))
          {
            String str5 = arrayOfString[str1];
            int i3 = "sec-websocket-accept: ".length();
            str6 = str5.substring(i3).trim();
            String str7 = this.w;
            Charset localCharset = this.m;
            str8 = a(str7, localCharset);
            if (str6.equals(str8))
              int i4 = 1;
          }
          while (true)
          {
            int i5 = str1 + 1;
            break;
            Logger localLogger3 = p;
            Object[] arrayOfObject3 = new Object[2];
            arrayOfObject3[0] = str8;
            arrayOfObject3[1] = str6;
            localLogger3.w("Expected accept value <%s> but got <%s>", arrayOfObject3);
            continue;
            if (str4.startsWith("sec-websocket-protocol: "))
            {
              String str9 = arrayOfString[str1];
              int i6 = "sec-websocket-protocol: ".length();
              String str10 = str9.substring(i6).trim();
              this.e = str10;
            }
          }
        }
        if (bool2)
        {
          Logger localLogger4 = p;
          Object[] arrayOfObject4 = new Object[0];
          localLogger4.d("Switching to STATE_CONNECTED", arrayOfObject4);
          this.q = 3;
          if (this.f != null)
            this.f.onConnected();
        }
        bool1 = bool2;
      }
    }
  }

  private void l()
    throws IOException
  {
    byte[] arrayOfByte = this.v;
    a((byte)10, arrayOfByte, true);
    this.v = null;
  }

  private boolean m()
    throws IOException
  {
    boolean bool = false;
    Object localObject1;
    if (!this.j.j())
    {
      this.j.e();
      localObject1 = this.j.a();
      if (localObject1 != null)
        break label48;
      int i1 = 1;
    }
    label33: label44: label46: label48: label95: Object localObject3;
    label155: label217: label255: int i5;
    int i8;
    while (true)
      if (bool)
      {
        this.j.g();
        bool = true;
        return bool;
        byte b1 = (byte)(((Byte)localObject1).byteValue() & 0xF);
        if ((((Byte)localObject1).byteValue() & 0xFFFFFF80) != 0);
        for (localObject1 = null; ; localObject1 = null)
        {
          localObject2 = this.j.a();
          if (localObject2 != null)
            break label95;
          int i2 = 1;
          break;
        }
        if ((((Byte)localObject2).byteValue() & 0xFFFFFF80) != 0);
        for (localObject3 = null; ; localObject3 = null)
        {
          l1 = ((Byte)localObject2).byteValue() & 0x7F;
          if (l1 != 126L)
            break label217;
          localObject2 = this.j.b();
          if (localObject2 != null)
            break label155;
          int i3 = 1;
          break;
        }
        long l1 = ((Integer)localObject2).intValue();
        while (true)
        {
          long l2 = this.i;
          if (l1 <= l2)
            break label255;
          String str = "message too large: " + l1 + " bytes";
          throw new IOException(str);
          if (l1 == 127L)
          {
            localObject2 = this.j.c();
            if (localObject2 == null)
            {
              bool = true;
              break;
            }
            l1 = ((Long)localObject2).longValue();
          }
        }
        i5 = (int)l1;
        Object localObject2 = null;
        if (localObject3 != null)
        {
          localObject3 = new byte[4];
          if (!this.j.a((byte[])localObject3, null))
            int i4 = 1;
        }
        else
        {
          localObject3 = localObject2;
          if (this.j.i() < i5)
            i5 = 1;
          else
            switch (b1)
            {
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            default:
              if (!this.j.a(i5))
                int i6 = 1;
              break;
            case 0:
              if (this.r == 1)
              {
                e locale1 = this.j;
                CharsetDecoder localCharsetDecoder1 = this.n;
                localObject3 = locale1.a(i5, (byte[])localObject3, localCharsetDecoder1);
                if (localObject3 == null)
                {
                  bool = true;
                  continue;
                }
                if (this.f == null)
                  break;
                this.f.onContinuationMessageReceived((String)localObject3, ()localObject1);
                break;
              }
              if (this.r == 2)
              {
                localObject2 = new byte[i5];
                if (!this.j.a((byte[])localObject2, (byte[])localObject3))
                {
                  bool = true;
                  continue;
                }
                if (this.f == null)
                  break;
                this.f.onContinuationMessageReceived((byte[])localObject2, ()localObject1);
                break;
              }
              throw new IOException("Unexpected continuation frame received");
            case 1:
              Logger localLogger = p;
              Object[] arrayOfObject = new Object[2];
              Integer localInteger = Integer.valueOf(i5);
              arrayOfObject[0] = localInteger;
              arrayOfObject[1] = localObject3;
              localLogger.d("reading a text message of length %d, mask %h", arrayOfObject);
              e locale2 = this.j;
              CharsetDecoder localCharsetDecoder2 = this.n;
              localObject3 = locale2.a(i5, (byte[])localObject3, localCharsetDecoder2);
              if (localObject3 == null)
              {
                bool = true;
              }
              else
              {
                if (this.f != null)
                  this.f.onMessageReceived((String)localObject3, ()localObject1);
                this.r = b1;
              }
              break;
            case 2:
              localObject2 = new byte[i5];
              if (!this.j.a((byte[])localObject2, (byte[])localObject3))
              {
                bool = true;
              }
              else
              {
                if (this.f != null)
                  this.f.onMessageReceived((byte[])localObject2, ()localObject1);
                this.r = b1;
              }
              break;
            case 8:
              this.q = 4;
              if (i5 < 2)
                break label838;
              localObject1 = this.j.b();
              if (localObject1 == null)
              {
                int i7 = 1;
              }
              else
              {
                i8 = ((Integer)localObject1).intValue();
                this.x = i8;
              }
              break;
            case 9:
            }
        }
      }
    label838: for (int i9 = i5 + -2; ; i9 = i5)
    {
      if ((i9 > 0) && (!this.j.a(i9)))
      {
        i8 = 1;
        break label33;
      }
      this.t = true;
      if (!this.s)
        break;
      break label46;
      byte[] arrayOfByte1 = new byte[i5];
      this.v = arrayOfByte1;
      e locale3 = this.j;
      byte[] arrayOfByte2 = this.v;
      if (!locale3.a(arrayOfByte2, (byte[])localObject3))
      {
        int i10 = 1;
        break label33;
      }
      this.u = true;
      this.r = -1;
      break;
      this.r = -1;
      break;
      this.j.f();
      break label44;
    }
  }

  private void n()
    throws IllegalStateException
  {
    if (this.q == 3)
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("not connected; state=");
    int i1 = this.q;
    String str = i1;
    throw new IllegalStateException(str);
  }

  /** @deprecated */
  public void a()
    throws IOException
  {
    try
    {
      if (this.q == 3)
      {
        this.q = 4;
        if (!this.s)
          a((short)1000);
      }
      while (true)
      {
        this.l.c();
        return;
        Logger localLogger = p;
        Object[] arrayOfObject = new Object[0];
        localLogger.d("force-closing socket in disconnect()", arrayOfObject);
        b(0);
      }
    }
    finally
    {
    }
  }

  /** @deprecated */
  public void a(int paramInt)
  {
    try
    {
      b(paramInt);
      this.l.c();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void a(Uri paramUri)
    throws IOException
  {
    a(paramUri, 5000);
  }

  /** @deprecated */
  public void a(Uri paramUri, int paramInt)
    throws IOException
  {
    int i1 = 1;
    InetSocketAddress localInetSocketAddress = null;
    try
    {
      if (this.g != null)
        throw new IllegalStateException("already connected");
    }
    finally
    {
    }
    if (!paramUri.getScheme().equals("ws"))
      throw new IllegalArgumentException("scheme must be ws");
    int i3;
    if (paramInt >= 1000)
    {
      int i2 = paramInt;
      this.z = i2;
      long l1 = System.currentTimeMillis();
      this.y = l1;
      this.b = paramUri;
      SocketChannel localSocketChannel = SocketChannel.open();
      this.g = localSocketChannel;
      SelectableChannel localSelectableChannel = this.g.configureBlocking(false);
      int i6 = this.b.getPort();
      this.c = i6;
      if (this.c == -1)
        this.c = 80;
      this.q = 1;
      i3 = Build.VERSION.SDK_INT;
      if (i3 > 13)
        break label385;
    }
    while (true)
    {
      try
      {
        int i7 = b(this.b.getHost());
        i3 = i7;
        if (i3 != 0)
        {
          int i8 = this.c;
          localInetSocketAddress = new InetSocketAddress(i3, i8);
          if (!this.g.connect(localInetSocketAddress))
            continue;
          int i9 = this.h;
          e locale1 = new e(i9);
          this.j = locale1;
          int i10 = this.h;
          e locale2 = new e(i10);
          this.k = locale2;
          this.r = -1;
          this.u = false;
          this.t = false;
          this.s = false;
          this.v = null;
          this.x = 0;
          this.l.b();
          this.l.a(this);
          if (i1 != 0)
            j();
          return;
          int i4 = 1000L;
        }
      }
      catch (Exception localException)
      {
        Logger localLogger = p;
        Object[] arrayOfObject = new Object[0];
        localLogger.w("Unable to create InetAddress object.", arrayOfObject);
        i5 = 0;
        continue;
        String str = this.b.getHost();
        int i11 = this.c;
        localInetSocketAddress = new InetSocketAddress(str, i11);
        continue;
        i1 = 0;
        continue;
      }
      label385: int i5 = 0;
    }
  }

  public void a(Listener paramListener)
  {
    this.f = paramListener;
  }

  /** @deprecated */
  public void a(String paramString)
    throws IOException
  {
    try
    {
      n();
      if (paramString == null)
        throw new IllegalArgumentException("message cannot be null");
    }
    finally
    {
    }
    Charset localCharset = this.m;
    byte[] arrayOfByte = paramString.getBytes(localCharset);
    a((byte)1, arrayOfByte, true);
    this.l.c();
  }

  /** @deprecated */
  boolean a(SelectionKey paramSelectionKey, long paramLong)
  {
    boolean bool1 = false;
    try
    {
      if (c())
      {
        long l1 = this.y;
        long l2 = paramLong - l1;
        long l3 = this.z;
        if (l2 > l3)
          b(-1);
      }
      while (true)
      {
        return bool1;
        if (!this.o)
          break;
        Logger localLogger = p;
        Object[] arrayOfObject = new Object[0];
        localLogger.w("Socket is no longer connected", arrayOfObject);
        this.o = false;
      }
    }
    finally
    {
    }
    int i1;
    if ((this.q == 1) && (!this.g.isConnected()))
      i1 = 8;
    while (true)
    {
      SelectionKey localSelectionKey = paramSelectionKey.interestOps(i1);
      i1 = 1;
      break;
      if ((this.q != 0) && (this.q != 1))
      {
        if (!this.j.k())
          i1 = 1;
        boolean bool2 = this.k.j();
        if (!bool2)
          i1 |= 4;
      }
    }
  }

  /** @deprecated */
  public boolean c()
  {
    boolean bool = true;
    try
    {
      if (this.q != 1)
      {
        int i1 = this.q;
        if (i1 != 2);
      }
      else
      {
        return bool;
      }
      bool = false;
    }
    finally
    {
    }
  }

  /** @deprecated */
  // ERROR //
  boolean f()
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: monitorenter
    //   4: getstatic 104	com/google/cast/WebSocket:p	Lcom/google/cast/Logger;
    //   7: astore_2
    //   8: iconst_0
    //   9: anewarray 4	java/lang/Object
    //   12: astore_3
    //   13: aload_2
    //   14: ldc_w 557
    //   17: aload_3
    //   18: invokevirtual 188	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   21: aload_0
    //   22: getfield 251	com/google/cast/WebSocket:g	Ljava/nio/channels/SocketChannel;
    //   25: invokevirtual 560	java/nio/channels/SocketChannel:finishConnect	()Z
    //   28: istore 4
    //   30: aload_0
    //   31: invokespecial 527	com/google/cast/WebSocket:j	()V
    //   34: iconst_1
    //   35: istore_1
    //   36: aload_0
    //   37: monitorexit
    //   38: iload_1
    //   39: ireturn
    //   40: astore 5
    //   42: aload_0
    //   43: monitorexit
    //   44: aload 5
    //   46: athrow
    //   47: astore 6
    //   49: goto -13 -> 36
    //
    // Exception table:
    //   from	to	target	type
    //   4	21	40	finally
    //   21	34	40	finally
    //   21	34	47	java/io/IOException
  }

  /** @deprecated */
  // ERROR //
  boolean g()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: bipush 255
    //   4: istore_2
    //   5: iconst_0
    //   6: istore_3
    //   7: aload_0
    //   8: monitorenter
    //   9: getstatic 104	com/google/cast/WebSocket:p	Lcom/google/cast/Logger;
    //   12: astore 4
    //   14: iconst_0
    //   15: anewarray 4	java/lang/Object
    //   18: astore 5
    //   20: aload 4
    //   22: ldc_w 564
    //   25: aload 5
    //   27: invokevirtual 188	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   30: aload_0
    //   31: getfield 258	com/google/cast/WebSocket:j	Lcom/google/cast/e;
    //   34: astore 6
    //   36: aload_0
    //   37: getfield 251	com/google/cast/WebSocket:g	Ljava/nio/channels/SocketChannel;
    //   40: astore 7
    //   42: aload 6
    //   44: aload 7
    //   46: invokevirtual 567	com/google/cast/e:b	(Ljava/nio/channels/SocketChannel;)I
    //   49: istore 8
    //   51: aload_0
    //   52: getfield 65	com/google/cast/WebSocket:q	I
    //   55: iconst_2
    //   56: if_icmpne +41 -> 97
    //   59: aload_0
    //   60: invokespecial 568	com/google/cast/WebSocket:k	()Z
    //   63: ifne +148 -> 211
    //   66: getstatic 104	com/google/cast/WebSocket:p	Lcom/google/cast/Logger;
    //   69: astore 9
    //   71: iconst_0
    //   72: anewarray 4	java/lang/Object
    //   75: astore 10
    //   77: aload 9
    //   79: ldc_w 570
    //   82: aload 10
    //   84: invokevirtual 352	com/google/cast/Logger:w	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   87: aload_0
    //   88: bipush 255
    //   90: invokespecial 471	com/google/cast/WebSocket:b	(I)V
    //   93: aload_0
    //   94: monitorexit
    //   95: iload_3
    //   96: ireturn
    //   97: aload_0
    //   98: invokespecial 572	com/google/cast/WebSocket:m	()Z
    //   101: ifne +110 -> 211
    //   104: getstatic 104	com/google/cast/WebSocket:p	Lcom/google/cast/Logger;
    //   107: astore 11
    //   109: iconst_0
    //   110: anewarray 4	java/lang/Object
    //   113: astore 12
    //   115: aload 11
    //   117: ldc_w 574
    //   120: aload 12
    //   122: invokevirtual 352	com/google/cast/Logger:w	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   125: aload_0
    //   126: getfield 65	com/google/cast/WebSocket:q	I
    //   129: iconst_4
    //   130: if_icmpne +76 -> 206
    //   133: aconst_null
    //   134: astore_1
    //   135: aload_0
    //   136: aload_1
    //   137: invokespecial 471	com/google/cast/WebSocket:b	(I)V
    //   140: goto -47 -> 93
    //   143: astore_1
    //   144: getstatic 104	com/google/cast/WebSocket:p	Lcom/google/cast/Logger;
    //   147: astore 13
    //   149: iconst_1
    //   150: anewarray 4	java/lang/Object
    //   153: astore 14
    //   155: aload_0
    //   156: getfield 65	com/google/cast/WebSocket:q	I
    //   159: invokestatic 247	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   162: astore 15
    //   164: aload 14
    //   166: iconst_0
    //   167: aload 15
    //   169: aastore
    //   170: aload 13
    //   172: aload_1
    //   173: ldc_w 576
    //   176: aload 14
    //   178: invokevirtual 578	com/google/cast/Logger:d	(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V
    //   181: aload_0
    //   182: getfield 65	com/google/cast/WebSocket:q	I
    //   185: iconst_4
    //   186: if_icmpne +5 -> 191
    //   189: iconst_0
    //   190: istore_2
    //   191: aload_0
    //   192: iload_2
    //   193: invokespecial 471	com/google/cast/WebSocket:b	(I)V
    //   196: goto -103 -> 93
    //   199: astore 16
    //   201: aload_0
    //   202: monitorexit
    //   203: aload 16
    //   205: athrow
    //   206: aconst_null
    //   207: astore_1
    //   208: goto -73 -> 135
    //   211: iconst_1
    //   212: istore_3
    //   213: goto -120 -> 93
    //   216: astore 17
    //   218: aload_0
    //   219: getfield 65	com/google/cast/WebSocket:q	I
    //   222: iconst_4
    //   223: if_icmpne +13 -> 236
    //   226: aconst_null
    //   227: astore_1
    //   228: aload_0
    //   229: aload_1
    //   230: invokespecial 471	com/google/cast/WebSocket:b	(I)V
    //   233: goto -140 -> 93
    //   236: aconst_null
    //   237: astore_1
    //   238: goto -10 -> 228
    //
    // Exception table:
    //   from	to	target	type
    //   30	93	143	java/nio/channels/ClosedChannelException
    //   97	140	143	java/nio/channels/ClosedChannelException
    //   9	30	199	finally
    //   30	93	199	finally
    //   97	140	199	finally
    //   144	196	199	finally
    //   218	233	199	finally
    //   30	93	216	java/io/IOException
    //   97	140	216	java/io/IOException
  }

  /** @deprecated */
  // ERROR //
  boolean h()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: bipush 255
    //   4: istore_2
    //   5: iconst_0
    //   6: istore_3
    //   7: aload_0
    //   8: monitorenter
    //   9: getstatic 104	com/google/cast/WebSocket:p	Lcom/google/cast/Logger;
    //   12: astore 4
    //   14: iconst_0
    //   15: anewarray 4	java/lang/Object
    //   18: astore 5
    //   20: aload 4
    //   22: ldc_w 580
    //   25: aload 5
    //   27: invokevirtual 188	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   30: aload_0
    //   31: getfield 158	com/google/cast/WebSocket:k	Lcom/google/cast/e;
    //   34: astore 6
    //   36: aload_0
    //   37: getfield 251	com/google/cast/WebSocket:g	Ljava/nio/channels/SocketChannel;
    //   40: astore 7
    //   42: aload 6
    //   44: aload 7
    //   46: invokevirtual 582	com/google/cast/e:a	(Ljava/nio/channels/SocketChannel;)I
    //   49: istore 8
    //   51: aload_0
    //   52: getfield 158	com/google/cast/WebSocket:k	Lcom/google/cast/e;
    //   55: invokevirtual 389	com/google/cast/e:j	()Z
    //   58: ifeq +25 -> 83
    //   61: aload_0
    //   62: getfield 65	com/google/cast/WebSocket:q	I
    //   65: iconst_4
    //   66: if_icmpne +122 -> 188
    //   69: aload_0
    //   70: getfield 192	com/google/cast/WebSocket:s	Z
    //   73: ifne +16 -> 89
    //   76: aload_0
    //   77: sipush 1000
    //   80: invokespecial 465	com/google/cast/WebSocket:a	(S)V
    //   83: iconst_1
    //   84: istore_3
    //   85: aload_0
    //   86: monitorexit
    //   87: iload_3
    //   88: ireturn
    //   89: aload_0
    //   90: getfield 454	com/google/cast/WebSocket:t	Z
    //   93: ifeq -10 -> 83
    //   96: getstatic 104	com/google/cast/WebSocket:p	Lcom/google/cast/Logger;
    //   99: astore 9
    //   101: iconst_0
    //   102: anewarray 4	java/lang/Object
    //   105: astore 10
    //   107: aload 9
    //   109: ldc_w 584
    //   112: aload 10
    //   114: invokevirtual 188	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   117: aload_0
    //   118: iconst_0
    //   119: invokespecial 471	com/google/cast/WebSocket:b	(I)V
    //   122: goto -37 -> 85
    //   125: astore_1
    //   126: getstatic 104	com/google/cast/WebSocket:p	Lcom/google/cast/Logger;
    //   129: astore 11
    //   131: iconst_1
    //   132: anewarray 4	java/lang/Object
    //   135: astore 12
    //   137: aload_0
    //   138: getfield 65	com/google/cast/WebSocket:q	I
    //   141: invokestatic 247	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   144: astore 13
    //   146: aload 12
    //   148: iconst_0
    //   149: aload 13
    //   151: aastore
    //   152: aload 11
    //   154: aload_1
    //   155: ldc_w 576
    //   158: aload 12
    //   160: invokevirtual 578	com/google/cast/Logger:d	(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V
    //   163: aload_0
    //   164: getfield 65	com/google/cast/WebSocket:q	I
    //   167: iconst_4
    //   168: if_icmpne +5 -> 173
    //   171: iconst_0
    //   172: istore_2
    //   173: aload_0
    //   174: iload_2
    //   175: invokespecial 471	com/google/cast/WebSocket:b	(I)V
    //   178: goto -93 -> 85
    //   181: astore 14
    //   183: aload_0
    //   184: monitorexit
    //   185: aload 14
    //   187: athrow
    //   188: aload_0
    //   189: getfield 65	com/google/cast/WebSocket:q	I
    //   192: iconst_3
    //   193: if_icmpne -110 -> 83
    //   196: aload_0
    //   197: getfield 456	com/google/cast/WebSocket:u	Z
    //   200: ifeq -117 -> 83
    //   203: aload_0
    //   204: invokespecial 586	com/google/cast/WebSocket:l	()V
    //   207: aload_0
    //   208: iconst_0
    //   209: putfield 456	com/google/cast/WebSocket:u	Z
    //   212: goto -129 -> 83
    //   215: astore 15
    //   217: aload_0
    //   218: getfield 65	com/google/cast/WebSocket:q	I
    //   221: iconst_4
    //   222: if_icmpne +13 -> 235
    //   225: aconst_null
    //   226: astore_1
    //   227: aload_0
    //   228: aload_1
    //   229: invokespecial 471	com/google/cast/WebSocket:b	(I)V
    //   232: goto -147 -> 85
    //   235: aconst_null
    //   236: astore_1
    //   237: goto -10 -> 227
    //
    // Exception table:
    //   from	to	target	type
    //   30	83	125	java/nio/channels/ClosedChannelException
    //   89	122	125	java/nio/channels/ClosedChannelException
    //   188	212	125	java/nio/channels/ClosedChannelException
    //   9	30	181	finally
    //   30	83	181	finally
    //   89	122	181	finally
    //   126	178	181	finally
    //   188	212	181	finally
    //   217	232	181	finally
    //   30	83	215	java/io/IOException
    //   89	122	215	java/io/IOException
    //   188	212	215	java/io/IOException
  }

  /** @deprecated */
  SocketChannel i()
  {
    try
    {
      SocketChannel localSocketChannel = this.g;
      return localSocketChannel;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static abstract interface Listener
  {
    public abstract void onConnected();

    public abstract void onConnectionFailed(int paramInt);

    public abstract void onContinuationMessageReceived(String paramString, boolean paramBoolean);

    public abstract void onContinuationMessageReceived(byte[] paramArrayOfByte, boolean paramBoolean);

    public abstract void onDisconnected(int paramInt1, int paramInt2);

    public abstract void onMessageReceived(String paramString, boolean paramBoolean);

    public abstract void onMessageReceived(byte[] paramArrayOfByte, boolean paramBoolean);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.WebSocket
 * JD-Core Version:    0.6.2
 */