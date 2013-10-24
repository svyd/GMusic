package com.google.common.io.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class ProtoBuf
{
  public static final Boolean FALSE = new Boolean(false);
  private static Long[] SMALL_NUMBERS = arrayOfLong;
  public static final Boolean TRUE = new Boolean(true);
  private ProtoBufType msgType;
  private final Vector values;
  private final StringBuffer wireTypes;

  static
  {
    Long[] arrayOfLong = new Long[16];
    Long localLong1 = new Long(0L);
    arrayOfLong[0] = localLong1;
    Long localLong2 = new Long(1L);
    arrayOfLong[1] = localLong2;
    Long localLong3 = new Long(2L);
    arrayOfLong[2] = localLong3;
    Long localLong4 = new Long(3L);
    arrayOfLong[3] = localLong4;
    Long localLong5 = new Long(4L);
    arrayOfLong[4] = localLong5;
    Long localLong6 = new Long(5L);
    arrayOfLong[5] = localLong6;
    Long localLong7 = new Long(6L);
    arrayOfLong[6] = localLong7;
    Long localLong8 = new Long(7L);
    arrayOfLong[7] = localLong8;
    Long localLong9 = new Long(8L);
    arrayOfLong[8] = localLong9;
    Long localLong10 = new Long(9L);
    arrayOfLong[9] = localLong10;
    Long localLong11 = new Long(10L);
    arrayOfLong[10] = localLong11;
    Long localLong12 = new Long(11L);
    arrayOfLong[11] = localLong12;
    Long localLong13 = new Long(12L);
    arrayOfLong[12] = localLong13;
    Long localLong14 = new Long(13L);
    arrayOfLong[13] = localLong14;
    Long localLong15 = new Long(14L);
    arrayOfLong[14] = localLong15;
    Long localLong16 = new Long(15L);
    arrayOfLong[15] = localLong16;
  }

  public ProtoBuf(ProtoBufType paramProtoBufType)
  {
    Vector localVector = new Vector();
    this.values = localVector;
    StringBuffer localStringBuffer = new StringBuffer();
    this.wireTypes = localStringBuffer;
    this.msgType = paramProtoBufType;
  }

  private void assertTypeMatch(int paramInt, Object paramObject)
  {
    int i = getType(paramInt);
    if ((i == 16) && (this.msgType == null))
      return;
    if ((paramObject instanceof Boolean))
    {
      if (i == 24)
        return;
      if (i == 0)
        return;
    }
    while (true)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Type mismatch type:");
      ProtoBufType localProtoBufType1 = this.msgType;
      String str = localProtoBufType1 + " tag:" + paramInt;
      throw new IllegalArgumentException(str);
      if ((paramObject instanceof Long))
        switch (i)
        {
        case 0:
        case 1:
        case 5:
        case 17:
        case 18:
        case 19:
        case 20:
        case 21:
        case 22:
        case 23:
        case 24:
        case 29:
        case 30:
        case 31:
        case 32:
        case 33:
        case 34:
        case 2:
        case 3:
        case 4:
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
        case 14:
        case 15:
        case 16:
        case 25:
        case 26:
        case 27:
        case 28:
        }
      else if ((paramObject instanceof byte[]))
        switch (i)
        {
        case 2:
        case 25:
        case 27:
        case 28:
        case 35:
        case 36:
        }
      else if ((paramObject instanceof ProtoBuf))
        switch (i)
        {
        default:
          break;
        case 2:
        case 3:
        case 25:
        case 26:
        case 27:
        case 28:
          if (this.msgType == null)
            return;
          if (this.msgType.getData(paramInt) == null)
            return;
          if (((ProtoBuf)paramObject).msgType == null)
            return;
          ProtoBufType localProtoBufType2 = ((ProtoBuf)paramObject).msgType;
          Object localObject = this.msgType.getData(paramInt);
          if (localProtoBufType2 != localObject)
            continue;
          return;
        }
      else if ((paramObject instanceof String))
        switch (i)
        {
        case 2:
        case 25:
        case 28:
        case 36:
        }
    }
  }

  private Object convert(Object paramObject, int paramInt)
  {
    int i = 1;
    switch (paramInt)
    {
    case 17:
    case 18:
    case 20:
    case 29:
    case 30:
    default:
      throw new RuntimeException("Unsupp.Type");
    case 24:
      if (!(paramObject instanceof Boolean))
        break;
    case 16:
    case 19:
    case 21:
    case 22:
    case 23:
    case 31:
    case 32:
    case 33:
    case 34:
    case 25:
    case 35:
    case 28:
    case 36:
    case 26:
    case 27:
    }
    while (true)
    {
      return paramObject;
      switch ((int)((Long)paramObject).longValue())
      {
      default:
        throw new IllegalArgumentException("Type mismatch");
      case 0:
        paramObject = FALSE;
        break;
      case 1:
        paramObject = TRUE;
        continue;
        if (!(paramObject instanceof Boolean))
          continue;
        Long[] arrayOfLong = SMALL_NUMBERS;
        if (((Boolean)paramObject).booleanValue());
        while (true)
        {
          paramObject = arrayOfLong[i];
          break;
          i = 0;
        }
        if ((paramObject instanceof String))
        {
          paramObject = encodeUtf8((String)paramObject);
          continue;
        }
        if (!(paramObject instanceof ProtoBuf))
          continue;
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        Object localObject;
        try
        {
          ((ProtoBuf)paramObject).outputTo(localByteArrayOutputStream);
          localObject = localByteArrayOutputStream.toByteArray();
          paramObject = localObject;
        }
        catch (IOException localIOException1)
        {
          String str1 = localIOException1.toString();
          throw new RuntimeException(str1);
        }
        if (!(paramObject instanceof byte[]))
          continue;
        byte[] arrayOfByte1 = (byte[])paramObject;
        int j = arrayOfByte1.length;
        paramObject = decodeUtf8(arrayOfByte1, 0, j, true);
        continue;
        if (!(paramObject instanceof byte[]))
          continue;
        try
        {
          ProtoBuf localProtoBuf = new ProtoBuf(null);
          byte[] arrayOfByte2 = (byte[])paramObject;
          localObject = localProtoBuf.parse(arrayOfByte2);
          paramObject = localObject;
        }
        catch (IOException localIOException2)
        {
          String str2 = localIOException2.toString();
          throw new RuntimeException(str2);
        }
      }
    }
  }

  static String decodeUtf8(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = paramInt2 - paramInt1;
    StringBuffer localStringBuffer1 = new StringBuffer(i);
    int j = paramInt1;
    int k;
    label108: int i2;
    int i5;
    int i6;
    if (j < paramInt2)
    {
      k = j + 1;
      int m = paramArrayOfByte[j] & 0xFF;
      if (m <= 127)
      {
        char c1 = (char)m;
        StringBuffer localStringBuffer2 = localStringBuffer1.append(c1);
      }
      while (true)
      {
        j = k;
        break;
        if (m < 245)
          break label108;
        if (!paramBoolean)
          throw new IllegalArgumentException("Invalid UTF8");
        char c2 = (char)m;
        StringBuffer localStringBuffer3 = localStringBuffer1.append(c2);
      }
      int n = 224;
      int i1 = 1;
      i2 = 128;
      int i3 = 31;
      if (m >= n)
      {
        n = n >> 1 | 0x80;
        if (i1 == 1);
        for (int i4 = 4; ; i4 = 5)
        {
          i2 <<= i4;
          i1 += 1;
          i3 >>= 1;
          break;
        }
      }
      i5 = m & i3;
      i6 = 0;
      j = k;
      label193: if (i6 < i1)
      {
        i5 <<= 6;
        if (j >= paramInt2)
        {
          if (paramBoolean)
            break label422;
          throw new IllegalArgumentException("Invalid UTF8");
        }
        if ((!paramBoolean) && ((paramArrayOfByte[j] & 0xC0) != 128))
          throw new IllegalArgumentException("Invalid UTF8");
        k = j + 1;
        int i7 = paramArrayOfByte[j] & 0x3F;
        i5 |= i7;
      }
    }
    while (true)
    {
      i6 += 1;
      int i8 = k;
      break label193;
      if (((!paramBoolean) && (i5 < i2)) || ((i5 >= 55296) && (i5 <= 57343)))
        throw new IllegalArgumentException("Invalid UTF8");
      if (i5 <= 65535)
      {
        char c3 = (char)i5;
        StringBuffer localStringBuffer4 = localStringBuffer1.append(c3);
        k = j;
        break;
      }
      int i9 = i5 - 65536;
      int i10 = i9 >> 10;
      char c4 = (char)(0xD800 | i10);
      StringBuffer localStringBuffer5 = localStringBuffer1.append(c4);
      int i11 = i9 & 0x3FF;
      char c5 = (char)(0xDC00 | i11);
      StringBuffer localStringBuffer6 = localStringBuffer1.append(c5);
      k = j;
      break;
      return localStringBuffer1.toString();
      label422: k = j;
    }
  }

  static int encodeUtf8(String paramString, byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramString.length();
    int j = 0;
    if (j < i)
    {
      int k = paramString.charAt(j);
      int i3;
      if ((k >= 55296) && (k <= 57343) && (j + 1 < i))
      {
        int m = j + 1;
        int n = paramString.charAt(m);
        int i1 = n & 0xFC00;
        int i2 = k & 0xFC00;
        if ((i1 ^ i2) == 1024)
        {
          j += 1;
          if ((n & 0xFC00) != 55296)
            break label170;
          i3 = n;
          n = k;
          label107: int i4 = (i3 & 0x3FF) << 10;
          int i5 = n & 0x3FF;
          k = (i4 | i5) + 65536;
        }
      }
      if (k <= 127)
      {
        if (paramArrayOfByte != null)
        {
          int i6 = (byte)k;
          paramArrayOfByte[paramInt] = i6;
        }
        paramInt += 1;
      }
      while (true)
      {
        j += 1;
        break;
        label170: i3 = k;
        break label107;
        if (k <= 2047)
        {
          if (paramArrayOfByte != null)
          {
            int i7 = (byte)(k >> 6 | 0xC0);
            paramArrayOfByte[paramInt] = i7;
            int i8 = paramInt + 1;
            int i9 = (byte)(k & 0x3F | 0x80);
            paramArrayOfByte[i8] = i9;
          }
          paramInt += 2;
        }
        else if (k <= 65535)
        {
          if (paramArrayOfByte != null)
          {
            int i10 = (byte)(k >> 12 | 0xE0);
            paramArrayOfByte[paramInt] = i10;
            int i11 = paramInt + 1;
            int i12 = (byte)(k >> 6 & 0x3F | 0x80);
            paramArrayOfByte[i11] = i12;
            int i13 = paramInt + 2;
            int i14 = (byte)(k & 0x3F | 0x80);
            paramArrayOfByte[i13] = i14;
          }
          paramInt += 3;
        }
        else
        {
          if (paramArrayOfByte != null)
          {
            int i15 = (byte)(k >> 18 | 0xF0);
            paramArrayOfByte[paramInt] = i15;
            int i16 = paramInt + 1;
            int i17 = (byte)(k >> 12 & 0x3F | 0x80);
            paramArrayOfByte[i16] = i17;
            int i18 = paramInt + 2;
            int i19 = (byte)(k >> 6 & 0x3F | 0x80);
            paramArrayOfByte[i18] = i19;
            int i20 = paramInt + 3;
            int i21 = (byte)(k & 0x3F | 0x80);
            paramArrayOfByte[i20] = i21;
          }
          paramInt += 4;
        }
      }
    }
    return paramInt;
  }

  static byte[] encodeUtf8(String paramString)
  {
    byte[] arrayOfByte = new byte[encodeUtf8(paramString, null, 0)];
    int i = encodeUtf8(paramString, arrayOfByte, 0);
    return arrayOfByte;
  }

  private int getDataSize(int paramInt1, int paramInt2)
  {
    int i = getVarIntSize(paramInt1 << 3);
    Object localObject;
    int j;
    switch (getWireType(paramInt1))
    {
    case 2:
    case 4:
    default:
      localObject = getObject(paramInt1, paramInt2, 16);
      if ((localObject instanceof byte[]))
        j = ((byte[])localObject).length;
      break;
    case 5:
    case 1:
    case 0:
    case 3:
    }
    while (true)
    {
      int k = getVarIntSize(j) + i + j;
      while (true)
      {
        return k;
        k = i + 4;
        continue;
        k = i + 8;
        continue;
        long l = getLong(paramInt1, paramInt2);
        if (isZigZagEncodedType(paramInt1))
          l = zigZagEncode(l);
        k = getVarIntSize(l) + i;
        continue;
        k = getProtoBuf(paramInt1, paramInt2).getDataSize() + i + i;
      }
      if ((localObject instanceof String))
        j = encodeUtf8((String)localObject, null, 0);
      else
        j = ((ProtoBuf)localObject).getDataSize();
    }
  }

  private Object getDefault(int paramInt)
  {
    switch (getType(paramInt))
    {
    default:
    case 16:
    case 26:
    case 27:
    }
    for (Object localObject = this.msgType.getData(paramInt); ; localObject = null)
      return localObject;
  }

  private Object getObject(int paramInt1, int paramInt2)
  {
    int i = getCount(paramInt1);
    if (i == 0);
    for (Object localObject = getDefault(paramInt1); ; localObject = getObject(paramInt1, 0, paramInt2))
    {
      return localObject;
      if (i > 1)
        throw new IllegalArgumentException();
    }
  }

  private Object getObject(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = getCount(paramInt1);
    if (paramInt2 >= i)
      throw new ArrayIndexOutOfBoundsException();
    Object localObject1 = this.values.elementAt(paramInt1);
    Vector localVector = null;
    if ((localObject1 instanceof Vector))
    {
      localVector = (Vector)localObject1;
      localObject1 = localVector.elementAt(paramInt2);
    }
    Object localObject2 = convert(localObject1, paramInt3);
    if ((localObject2 != localObject1) && (localObject1 != null))
    {
      if (localVector != null)
        break label93;
      setObject(paramInt1, localObject2);
    }
    while (true)
    {
      return localObject2;
      label93: localVector.setElementAt(localObject2, paramInt2);
    }
  }

  private static int getVarIntSize(long paramLong)
  {
    int i;
    if (paramLong < 0L)
      i = 10;
    while (true)
    {
      return i;
      i = 1;
      while (paramLong >= 128L)
      {
        i += 1;
        paramLong >>= 7;
      }
    }
  }

  private final int getWireType(int paramInt)
  {
    int i = getType(paramInt);
    switch (i)
    {
    case 4:
    case 6:
    case 7:
    case 8:
    case 9:
    case 10:
    case 11:
    case 12:
    case 13:
    case 14:
    case 15:
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("Unsupp.Type:");
      ProtoBufType localProtoBufType = this.msgType;
      String str = localProtoBufType + '/' + paramInt + '/' + i;
      throw new RuntimeException(str);
    case 19:
    case 20:
    case 21:
    case 24:
    case 29:
    case 30:
    case 33:
    case 34:
      i = 0;
    case 0:
    case 1:
    case 2:
    case 3:
    case 5:
    case 16:
    case 25:
    case 27:
    case 28:
    case 35:
    case 36:
    case 17:
    case 22:
    case 32:
    case 18:
    case 23:
    case 31:
    case 26:
    }
    while (true)
    {
      return i;
      i = 2;
      continue;
      i = 1;
      continue;
      i = 5;
      continue;
      i = 3;
    }
  }

  private void insertObject(int paramInt1, int paramInt2, Object paramObject)
  {
    assertTypeMatch(paramInt1, paramObject);
    if (getCount(paramInt1) == 0)
    {
      setObject(paramInt1, paramObject);
      return;
    }
    Object localObject = this.values.elementAt(paramInt1);
    Vector localVector;
    if ((localObject instanceof Vector))
      localVector = (Vector)localObject;
    while (true)
    {
      localVector.insertElementAt(paramObject, paramInt2);
      return;
      localVector = new Vector();
      localVector.addElement(localObject);
      this.values.setElementAt(localVector, paramInt1);
    }
  }

  private boolean isZigZagEncodedType(int paramInt)
  {
    int i = getType(paramInt);
    if ((i == 33) || (i == 34));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  static long readVarInt(InputStream paramInputStream, boolean paramBoolean)
    throws IOException
  {
    long l1 = 0L;
    int i = 0;
    int j = 0;
    while (true)
    {
      int k;
      if (j < 10)
      {
        k = paramInputStream.read();
        if (k == -1)
          if ((j != 0) || (!paramBoolean));
      }
      for (long l2 = 65535L; ; l2 = l1)
      {
        return l2;
        throw new IOException("EOF");
        long l3 = (k & 0x7F) << i;
        l1 |= l3;
        if ((k & 0x80) != 0)
          break;
      }
      i += 7;
      j += 1;
    }
  }

  private void setObject(int paramInt, Object paramObject)
  {
    if (this.values.size() <= paramInt)
    {
      Vector localVector = this.values;
      int i = paramInt + 1;
      localVector.setSize(i);
    }
    if (paramObject != null)
      assertTypeMatch(paramInt, paramObject);
    this.values.setElementAt(paramObject, paramInt);
  }

  static void writeVarInt(OutputStream paramOutputStream, long paramLong)
    throws IOException
  {
    int i = 0;
    while (true)
    {
      if (i >= 10)
        return;
      int j = (int)(0x7F & paramLong);
      paramLong >>>= 7;
      if (paramLong == 0L)
      {
        paramOutputStream.write(j);
        return;
      }
      int k = j | 0x80;
      paramOutputStream.write(k);
      i += 1;
    }
  }

  private static long zigZagDecode(long paramLong)
  {
    long l1 = paramLong >>> 1;
    long l2 = -(0x1 & paramLong);
    return l1 ^ l2;
  }

  private static long zigZagEncode(long paramLong)
  {
    long l1 = paramLong << 1;
    long l2 = -(paramLong >>> 63);
    return l1 ^ l2;
  }

  public void addInt(int paramInt1, int paramInt2)
  {
    int i = getCount(paramInt1);
    insertInt(paramInt1, i, paramInt2);
  }

  public void addLong(int paramInt, long paramLong)
  {
    int i = getCount(paramInt);
    insertLong(paramInt, i, paramLong);
  }

  public void addString(int paramInt, String paramString)
  {
    int i = getCount(paramInt);
    insertString(paramInt, i, paramString);
  }

  public void clear()
  {
    this.values.setSize(0);
    this.wireTypes.setLength(0);
  }

  public int getCount(int paramInt)
  {
    int i = 0;
    int j = this.values.size();
    if (paramInt >= j);
    while (true)
    {
      return i;
      Object localObject = this.values.elementAt(paramInt);
      if (localObject != null)
        if ((localObject instanceof Vector))
          i = ((Vector)localObject).size();
        else
          i = 1;
    }
  }

  public int getDataSize()
  {
    int i = 0;
    int j = 0;
    while (true)
    {
      int k = maxTag();
      if (j > k)
        break;
      int m = 0;
      while (true)
      {
        int n = getCount(j);
        if (m >= n)
          break;
        int i1 = getDataSize(j, m);
        i += i1;
        m += 1;
      }
      j += 1;
    }
    return i;
  }

  public int getInt(int paramInt)
  {
    return (int)((Long)getObject(paramInt, 21)).longValue();
  }

  public long getLong(int paramInt)
  {
    return ((Long)getObject(paramInt, 19)).longValue();
  }

  public long getLong(int paramInt1, int paramInt2)
  {
    return ((Long)getObject(paramInt1, paramInt2, 19)).longValue();
  }

  public ProtoBuf getProtoBuf(int paramInt1, int paramInt2)
  {
    return (ProtoBuf)getObject(paramInt1, paramInt2, 26);
  }

  public String getString(int paramInt)
  {
    return (String)getObject(paramInt, 28);
  }

  public int getType(int paramInt)
  {
    int i = 16;
    if (this.msgType != null)
      i = this.msgType.getType(paramInt);
    if (i == 16)
    {
      int j = this.wireTypes.length();
      if (paramInt < j)
        i = this.wireTypes.charAt(paramInt);
    }
    if ((i == 16) && (getCount(paramInt) > 0))
    {
      Object localObject = getObject(paramInt, 0, 16);
      if ((!(localObject instanceof Long)) && (!(localObject instanceof Boolean)))
        break label91;
    }
    label91: for (i = 0; ; i = 2)
      return i;
  }

  public boolean has(int paramInt)
  {
    if ((getCount(paramInt) > 0) || (getDefault(paramInt) != null));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void insertInt(int paramInt1, int paramInt2, int paramInt3)
  {
    long l = paramInt3;
    insertLong(paramInt1, paramInt2, l);
  }

  public void insertLong(int paramInt1, int paramInt2, long paramLong)
  {
    Long[] arrayOfLong;
    int i;
    if (paramLong >= 0L)
    {
      long l = SMALL_NUMBERS.length;
      if (paramLong < l)
      {
        arrayOfLong = SMALL_NUMBERS;
        i = (int)paramLong;
      }
    }
    for (Long localLong = arrayOfLong[i]; ; localLong = new Long(paramLong))
    {
      insertObject(paramInt1, paramInt2, localLong);
      return;
    }
  }

  public void insertString(int paramInt1, int paramInt2, String paramString)
  {
    insertObject(paramInt1, paramInt2, paramString);
  }

  public int maxTag()
  {
    return this.values.size() + -1;
  }

  public void outputTo(OutputStream paramOutputStream)
    throws IOException
  {
    int i = 0;
    while (true)
    {
      int j = maxTag();
      if (i > j)
        return;
      int k = getCount(i);
      int m = getWireType(i);
      int n = 0;
      if (n < k)
      {
        long l1 = i << 3 | m;
        writeVarInt(paramOutputStream, l1);
        long l2;
        switch (m)
        {
        case 4:
        default:
          throw new IllegalArgumentException();
        case 1:
        case 5:
          l2 = ((Long)getObject(i, n, 19)).longValue();
          if (m == 5);
          for (int i1 = 4; ; i1 = 8)
          {
            int i2 = 0;
            while (i2 < i1)
            {
              int i3 = (int)(0xFF & l2);
              paramOutputStream.write(i3);
              l2 >>= 8;
              i2 += 1;
            }
          }
        case 0:
          l2 = ((Long)getObject(i, n, 19)).longValue();
          if (isZigZagEncodedType(i))
            long l3 = zigZagEncode(l2);
          writeVarInt(paramOutputStream, l2);
        case 2:
        case 3:
        }
        while (true)
        {
          n += 1;
          break;
          if (getType(i) == 27);
          Object localObject;
          for (int i4 = 16; ; i4 = 25)
          {
            localObject = getObject(i, n, i4);
            if (!(localObject instanceof byte[]))
              break label292;
            byte[] arrayOfByte = (byte[])localObject;
            long l4 = arrayOfByte.length;
            writeVarInt(paramOutputStream, l4);
            paramOutputStream.write(arrayOfByte);
            break;
          }
          label292: ProtoBuf localProtoBuf = (ProtoBuf)localObject;
          long l5 = localProtoBuf.getDataSize();
          writeVarInt(paramOutputStream, l5);
          localProtoBuf.outputTo(paramOutputStream);
          continue;
          ((ProtoBuf)getObject(i, n, 26)).outputTo(paramOutputStream);
          long l6 = i << 3 | 0x4;
          writeVarInt(paramOutputStream, l6);
        }
      }
      i += 1;
    }
  }

  public int parse(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    clear();
    long l1;
    if (paramInt > 0)
    {
      InputStream localInputStream1 = paramInputStream;
      boolean bool1 = true;
      l1 = readVarInt(localInputStream1, bool1);
      if (l1 != 65535L)
        break label42;
    }
    while (paramInt < 0)
    {
      throw new IOException();
      label42: int i = getVarIntSize(l1);
      paramInt -= i;
      int j = (int)l1 & 0x7;
      int k = j;
      k = 4;
      if (k != k)
      {
        int m = (int)(l1 >>> 3);
        while (this.wireTypes.length() <= m)
          StringBuffer localStringBuffer1 = this.wireTypes.append('\020');
        StringBuffer localStringBuffer2 = this.wireTypes;
        char c1 = (char)j;
        StringBuffer localStringBuffer3 = localStringBuffer2;
        char c2 = c1;
        localStringBuffer3.setCharAt(m, c2);
        long l2;
        Object localObject1;
        switch (j)
        {
        case 4:
        default:
          StringBuilder localStringBuilder = new StringBuilder().append("Unsupp.Type");
          int n = j;
          String str = n;
          throw new RuntimeException(str);
        case 0:
          InputStream localInputStream2 = paramInputStream;
          boolean bool2 = false;
          l2 = readVarInt(localInputStream2, bool2);
          int i1 = getVarIntSize(l2);
          paramInt -= i1;
          if (isZigZagEncodedType(m))
            long l3 = zigZagDecode(l2);
          if (l2 >= 0L)
          {
            long l4 = SMALL_NUMBERS.length;
            if (l2 < l4)
            {
              Long[] arrayOfLong1 = SMALL_NUMBERS;
              int i2 = (int)l2;
              localObject1 = arrayOfLong1[i2];
            }
          }
        case 1:
        case 5:
        case 2:
          while (true)
          {
            int i3 = getCount(m);
            ProtoBuf localProtoBuf1 = this;
            int i4 = i3;
            Object localObject2 = localObject1;
            localProtoBuf1.insertObject(m, i4, localObject2);
            break;
            localObject1 = new java/lang/Long;
            Object localObject3 = localObject1;
            long l5 = l2;
            localObject3.<init>(l5);
            continue;
            l2 = 0L;
            int i6 = 0;
            int i7 = j;
            int i5 = 5;
            if (i7 == i5);
            for (int i8 = 4; ; i8 = 8)
            {
              paramInt -= i8;
              for (int i9 = i8; ; i9 = i8)
              {
                i8 = i9 + -1;
                if (i9 <= 0)
                  break;
                long l6 = paramInputStream.read() << i6;
                l2 |= l6;
                i6 += 8;
              }
            }
            if (l2 >= 0L)
            {
              long l7 = SMALL_NUMBERS.length;
              if (l2 < l7)
              {
                Long[] arrayOfLong2 = SMALL_NUMBERS;
                int i10 = (int)l2;
                localObject1 = arrayOfLong2[i10];
              }
            }
            while (true)
            {
              break;
              localObject1 = new java/lang/Long;
              Object localObject4 = localObject1;
              long l8 = l2;
              localObject4.<init>(l8);
            }
            InputStream localInputStream3 = paramInputStream;
            boolean bool3 = false;
            int i12 = (int)readVarInt(localInputStream3, bool3);
            int i13 = getVarIntSize(i12);
            paramInt = paramInt - i13 - i12;
            int i14 = getType(m);
            int i11 = 27;
            if (i14 == i11)
            {
              ProtoBufType localProtoBufType1 = (ProtoBufType)this.msgType.getData(m);
              ProtoBuf localProtoBuf2 = new ProtoBuf(localProtoBufType1);
              InputStream localInputStream4 = paramInputStream;
              int i15 = localProtoBuf2.parse(localInputStream4, i12);
              localObject1 = localProtoBuf2;
            }
            else
            {
              byte[] arrayOfByte = new byte[i12];
              int i16 = 0;
              while (i16 < i12)
              {
                int i17 = i12 - i16;
                InputStream localInputStream5 = paramInputStream;
                int i18 = i17;
                int i19 = localInputStream5.read(arrayOfByte, i16, i18);
                if (i19 <= 0)
                  throw new IOException("Unexp.EOF");
                i16 += i19;
              }
              localObject1 = arrayOfByte;
            }
          }
        case 3:
        }
        ProtoBuf localProtoBuf3 = new com/google/common/io/protocol/ProtoBuf;
        if (this.msgType == null);
        for (ProtoBufType localProtoBufType2 = null; ; localProtoBufType2 = (ProtoBufType)this.msgType.getData(m))
        {
          ProtoBufType localProtoBufType3 = localProtoBufType2;
          localProtoBuf3.<init>(localProtoBufType3);
          InputStream localInputStream6 = paramInputStream;
          int i20 = paramInt;
          paramInt = localProtoBuf3.parse(localInputStream6, i20);
          localObject1 = localProtoBuf3;
          break;
        }
      }
    }
    return paramInt;
  }

  public ProtoBuf parse(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    int i = paramArrayOfByte.length;
    int j = parse(localByteArrayInputStream, i);
    return this;
  }

  public byte[] toByteArray()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    outputTo(localByteArrayOutputStream);
    return localByteArrayOutputStream.toByteArray();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.protocol.ProtoBuf
 * JD-Core Version:    0.6.2
 */