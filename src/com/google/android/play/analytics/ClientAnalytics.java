package com.google.android.play.analytics;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
import java.util.Arrays;

public final class ClientAnalytics
{
  public static final class LogResponse extends MessageNano
  {
    public static final LogResponse[] EMPTY_ARRAY = new LogResponse[0];
    private int cachedSize = -1;
    public ClientAnalytics.ExperimentIdList experiments = null;
    public long nextRequestWaitMillis = 65535L;

    public static LogResponse parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (LogResponse)MessageNano.mergeFrom(new LogResponse(), paramArrayOfByte);
    }

    public int getCachedSize()
    {
      if (this.cachedSize < 0)
        int i = getSerializedSize();
      return this.cachedSize;
    }

    public int getSerializedSize()
    {
      int i = 0;
      if (this.nextRequestWaitMillis != 65535L)
      {
        long l = this.nextRequestWaitMillis;
        int j = CodedOutputByteBufferNano.computeInt64Size(1, l);
        i = 0 + j;
      }
      if (this.experiments != null)
      {
        ClientAnalytics.ExperimentIdList localExperimentIdList = this.experiments;
        int k = CodedOutputByteBufferNano.computeMessageSize(2, localExperimentIdList);
        i += k;
      }
      this.cachedSize = i;
      return i;
    }

    public LogResponse mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      while (true)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default:
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i))
            continue;
        case 0:
          return this;
        case 8:
          long l = paramCodedInputByteBufferNano.readInt64();
          this.nextRequestWaitMillis = l;
          break;
        case 18:
        }
        ClientAnalytics.ExperimentIdList localExperimentIdList1 = new ClientAnalytics.ExperimentIdList();
        this.experiments = localExperimentIdList1;
        ClientAnalytics.ExperimentIdList localExperimentIdList2 = this.experiments;
        paramCodedInputByteBufferNano.readMessage(localExperimentIdList2);
      }
    }

    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (this.nextRequestWaitMillis != 65535L)
      {
        long l = this.nextRequestWaitMillis;
        paramCodedOutputByteBufferNano.writeInt64(1, l);
      }
      if (this.experiments == null)
        return;
      ClientAnalytics.ExperimentIdList localExperimentIdList = this.experiments;
      paramCodedOutputByteBufferNano.writeMessage(2, localExperimentIdList);
    }
  }

  public static final class LogRequest extends MessageNano
  {
    public static final LogRequest[] EMPTY_ARRAY = new LogRequest[0];
    private int cachedSize;
    public ClientAnalytics.ClientInfo clientInfo = null;
    public ClientAnalytics.LogEvent[] logEvent;
    public int logSource = -1;
    public long requestTimeMs = 0L;
    public byte[][] serializedLogEvents;

    public LogRequest()
    {
      ClientAnalytics.LogEvent[] arrayOfLogEvent = ClientAnalytics.LogEvent.EMPTY_ARRAY;
      this.logEvent = arrayOfLogEvent;
      byte[][] arrayOfByte = WireFormatNano.EMPTY_BYTES_ARRAY;
      this.serializedLogEvents = arrayOfByte;
      this.cachedSize = -1;
    }

    public int getCachedSize()
    {
      if (this.cachedSize < 0)
        int i = getSerializedSize();
      return this.cachedSize;
    }

    public int getSerializedSize()
    {
      int i = 0;
      if (this.clientInfo != null)
      {
        ClientAnalytics.ClientInfo localClientInfo = this.clientInfo;
        int j = CodedOutputByteBufferNano.computeMessageSize(1, localClientInfo);
        i = 0 + j;
      }
      if (this.logSource != -1)
      {
        int k = this.logSource;
        int m = CodedOutputByteBufferNano.computeInt32Size(2, k);
        i += m;
      }
      Object localObject = this.logEvent;
      int n = localObject.length;
      int i1 = 0;
      while (i1 < n)
      {
        MessageNano localMessageNano = localObject[i1];
        int i2 = CodedOutputByteBufferNano.computeMessageSize(3, localMessageNano);
        i += i2;
        i1 += 1;
      }
      if (this.requestTimeMs != 0L)
      {
        long l = this.requestTimeMs;
        int i3 = CodedOutputByteBufferNano.computeInt64Size(4, l);
        i += i3;
      }
      if (this.serializedLogEvents.length > 0)
      {
        int i4 = 0;
        localObject = this.serializedLogEvents;
        n = localObject.length;
        i1 = 0;
        while (i1 < n)
        {
          int i5 = CodedOutputByteBufferNano.computeBytesSizeNoTag(localObject[i1]);
          i4 += i5;
          int i6 = i1 + 1;
        }
        int i7 = i + i4;
        int i8 = this.serializedLogEvents.length * 1;
        i = i7 + i8;
      }
      this.cachedSize = i;
      return i;
    }

    public LogRequest mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      while (true)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default:
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i))
            continue;
        case 0:
          return this;
        case 10:
          ClientAnalytics.ClientInfo localClientInfo1 = new ClientAnalytics.ClientInfo();
          this.clientInfo = localClientInfo1;
          ClientAnalytics.ClientInfo localClientInfo2 = this.clientInfo;
          paramCodedInputByteBufferNano.readMessage(localClientInfo2);
          break;
        case 16:
          int j = paramCodedInputByteBufferNano.readInt32();
          this.logSource = j;
          break;
        case 26:
          int k = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 26);
          m = this.logEvent.length;
          ClientAnalytics.LogEvent[] arrayOfLogEvent1 = new ClientAnalytics.LogEvent[m + k];
          System.arraycopy(this.logEvent, 0, arrayOfLogEvent1, 0, m);
          this.logEvent = arrayOfLogEvent1;
          while (true)
          {
            int n = this.logEvent.length + -1;
            if (m >= n)
              break;
            ClientAnalytics.LogEvent[] arrayOfLogEvent2 = this.logEvent;
            ClientAnalytics.LogEvent localLogEvent1 = new ClientAnalytics.LogEvent();
            arrayOfLogEvent2[m] = localLogEvent1;
            ClientAnalytics.LogEvent localLogEvent2 = this.logEvent[m];
            paramCodedInputByteBufferNano.readMessage(localLogEvent2);
            int i1 = paramCodedInputByteBufferNano.readTag();
            int i2 = m + 1;
          }
          ClientAnalytics.LogEvent[] arrayOfLogEvent3 = this.logEvent;
          ClientAnalytics.LogEvent localLogEvent3 = new ClientAnalytics.LogEvent();
          arrayOfLogEvent3[m] = localLogEvent3;
          ClientAnalytics.LogEvent localLogEvent4 = this.logEvent[m];
          paramCodedInputByteBufferNano.readMessage(localLogEvent4);
          break;
        case 32:
          long l = paramCodedInputByteBufferNano.readInt64();
          this.requestTimeMs = l;
          break;
        case 42:
        }
        int i3 = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 42);
        int m = this.serializedLogEvents.length;
        byte[] arrayOfByte1 = new byte[m + i3];
        System.arraycopy(this.serializedLogEvents, 0, arrayOfByte1, 0, m);
        this.serializedLogEvents = arrayOfByte1;
        while (true)
        {
          int i4 = this.serializedLogEvents.length + -1;
          if (m >= i4)
            break;
          byte[][] arrayOfByte2 = this.serializedLogEvents;
          byte[] arrayOfByte3 = paramCodedInputByteBufferNano.readBytes();
          arrayOfByte2[m] = arrayOfByte3;
          int i5 = paramCodedInputByteBufferNano.readTag();
          m += 1;
        }
        byte[][] arrayOfByte4 = this.serializedLogEvents;
        byte[] arrayOfByte5 = paramCodedInputByteBufferNano.readBytes();
        arrayOfByte4[m] = arrayOfByte5;
      }
    }

    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (this.clientInfo != null)
      {
        ClientAnalytics.ClientInfo localClientInfo = this.clientInfo;
        paramCodedOutputByteBufferNano.writeMessage(1, localClientInfo);
      }
      if (this.logSource != -1)
      {
        int i = this.logSource;
        paramCodedOutputByteBufferNano.writeInt32(2, i);
      }
      ClientAnalytics.LogEvent[] arrayOfLogEvent = this.logEvent;
      int j = arrayOfLogEvent.length;
      int k = 0;
      while (k < j)
      {
        ClientAnalytics.LogEvent localLogEvent = arrayOfLogEvent[k];
        paramCodedOutputByteBufferNano.writeMessage(3, localLogEvent);
        k += 1;
      }
      if (this.requestTimeMs != 0L)
      {
        long l = this.requestTimeMs;
        paramCodedOutputByteBufferNano.writeInt64(4, l);
      }
      byte[][] arrayOfByte = this.serializedLogEvents;
      int m = arrayOfByte.length;
      int n = 0;
      while (true)
      {
        if (n >= m)
          return;
        byte[] arrayOfByte1 = arrayOfByte[n];
        paramCodedOutputByteBufferNano.writeBytes(5, arrayOfByte1);
        n += 1;
      }
    }
  }

  public static final class ExperimentIdList extends MessageNano
  {
    public static final ExperimentIdList[] EMPTY_ARRAY = new ExperimentIdList[0];
    private int cachedSize;
    public String[] id;

    public ExperimentIdList()
    {
      String[] arrayOfString = WireFormatNano.EMPTY_STRING_ARRAY;
      this.id = arrayOfString;
      this.cachedSize = -1;
    }

    public int getCachedSize()
    {
      if (this.cachedSize < 0)
        int i = getSerializedSize();
      return this.cachedSize;
    }

    public int getSerializedSize()
    {
      int i = 0;
      if (this.id.length > 0)
      {
        int j = 0;
        String[] arrayOfString = this.id;
        int k = arrayOfString.length;
        int m = 0;
        while (m < k)
        {
          int n = CodedOutputByteBufferNano.computeStringSizeNoTag(arrayOfString[m]);
          j += n;
          m += 1;
        }
        int i1 = 0 + j;
        int i2 = this.id.length * 1;
        i = i1 + i2;
      }
      this.cachedSize = i;
      return i;
    }

    public ExperimentIdList mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      while (true)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default:
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i))
            continue;
        case 0:
          return this;
        case 10:
        }
        int j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 10);
        int k = this.id.length;
        String[] arrayOfString1 = new String[k + j];
        System.arraycopy(this.id, 0, arrayOfString1, 0, k);
        this.id = arrayOfString1;
        while (true)
        {
          int m = this.id.length + -1;
          if (k >= m)
            break;
          String[] arrayOfString2 = this.id;
          String str1 = paramCodedInputByteBufferNano.readString();
          arrayOfString2[k] = str1;
          int n = paramCodedInputByteBufferNano.readTag();
          k += 1;
        }
        String[] arrayOfString3 = this.id;
        String str2 = paramCodedInputByteBufferNano.readString();
        arrayOfString3[k] = str2;
      }
    }

    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      String[] arrayOfString = this.id;
      int i = arrayOfString.length;
      int j = 0;
      while (true)
      {
        if (j >= i)
          return;
        String str = arrayOfString[j];
        paramCodedOutputByteBufferNano.writeString(1, str);
        j += 1;
      }
    }
  }

  public static final class ClientInfo extends MessageNano
  {
    public static final ClientInfo[] EMPTY_ARRAY = new ClientInfo[0];
    public ClientAnalytics.AndroidClientInfo androidClientInfo = null;
    private int cachedSize = -1;
    public int clientType = 0;
    public ClientAnalytics.DesktopClientInfo desktopClientInfo = null;
    public ClientAnalytics.IosClientInfo iosClientInfo = null;

    public int getCachedSize()
    {
      if (this.cachedSize < 0)
        int i = getSerializedSize();
      return this.cachedSize;
    }

    public int getSerializedSize()
    {
      int i = 0;
      if (this.clientType != 0)
      {
        int j = this.clientType;
        int k = CodedOutputByteBufferNano.computeInt32Size(1, j);
        i = 0 + k;
      }
      if (this.androidClientInfo != null)
      {
        ClientAnalytics.AndroidClientInfo localAndroidClientInfo = this.androidClientInfo;
        int m = CodedOutputByteBufferNano.computeMessageSize(2, localAndroidClientInfo);
        i += m;
      }
      if (this.desktopClientInfo != null)
      {
        ClientAnalytics.DesktopClientInfo localDesktopClientInfo = this.desktopClientInfo;
        int n = CodedOutputByteBufferNano.computeMessageSize(3, localDesktopClientInfo);
        i += n;
      }
      if (this.iosClientInfo != null)
      {
        ClientAnalytics.IosClientInfo localIosClientInfo = this.iosClientInfo;
        int i1 = CodedOutputByteBufferNano.computeMessageSize(4, localIosClientInfo);
        i += i1;
      }
      this.cachedSize = i;
      return i;
    }

    public ClientInfo mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      while (true)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default:
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i))
            continue;
        case 0:
          return this;
        case 8:
          int j = paramCodedInputByteBufferNano.readInt32();
          this.clientType = j;
          break;
        case 18:
          ClientAnalytics.AndroidClientInfo localAndroidClientInfo1 = new ClientAnalytics.AndroidClientInfo();
          this.androidClientInfo = localAndroidClientInfo1;
          ClientAnalytics.AndroidClientInfo localAndroidClientInfo2 = this.androidClientInfo;
          paramCodedInputByteBufferNano.readMessage(localAndroidClientInfo2);
          break;
        case 26:
          ClientAnalytics.DesktopClientInfo localDesktopClientInfo1 = new ClientAnalytics.DesktopClientInfo();
          this.desktopClientInfo = localDesktopClientInfo1;
          ClientAnalytics.DesktopClientInfo localDesktopClientInfo2 = this.desktopClientInfo;
          paramCodedInputByteBufferNano.readMessage(localDesktopClientInfo2);
          break;
        case 34:
        }
        ClientAnalytics.IosClientInfo localIosClientInfo1 = new ClientAnalytics.IosClientInfo();
        this.iosClientInfo = localIosClientInfo1;
        ClientAnalytics.IosClientInfo localIosClientInfo2 = this.iosClientInfo;
        paramCodedInputByteBufferNano.readMessage(localIosClientInfo2);
      }
    }

    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (this.clientType != 0)
      {
        int i = this.clientType;
        paramCodedOutputByteBufferNano.writeInt32(1, i);
      }
      if (this.androidClientInfo != null)
      {
        ClientAnalytics.AndroidClientInfo localAndroidClientInfo = this.androidClientInfo;
        paramCodedOutputByteBufferNano.writeMessage(2, localAndroidClientInfo);
      }
      if (this.desktopClientInfo != null)
      {
        ClientAnalytics.DesktopClientInfo localDesktopClientInfo = this.desktopClientInfo;
        paramCodedOutputByteBufferNano.writeMessage(3, localDesktopClientInfo);
      }
      if (this.iosClientInfo == null)
        return;
      ClientAnalytics.IosClientInfo localIosClientInfo = this.iosClientInfo;
      paramCodedOutputByteBufferNano.writeMessage(4, localIosClientInfo);
    }
  }

  public static final class IosClientInfo extends MessageNano
  {
    public static final IosClientInfo[] EMPTY_ARRAY = new IosClientInfo[0];
    public String applicationBuild = "";
    private int cachedSize = -1;
    public String clientId = "";
    public String loggingId = "";
    public String osFullVersion = "";
    public String osMajorVersion = "";

    public int getCachedSize()
    {
      if (this.cachedSize < 0)
        int i = getSerializedSize();
      return this.cachedSize;
    }

    public int getSerializedSize()
    {
      int i = 0;
      if (!this.clientId.equals(""))
      {
        String str1 = this.clientId;
        int j = CodedOutputByteBufferNano.computeStringSize(1, str1);
        i = 0 + j;
      }
      if (!this.loggingId.equals(""))
      {
        String str2 = this.loggingId;
        int k = CodedOutputByteBufferNano.computeStringSize(2, str2);
        i += k;
      }
      if (!this.osMajorVersion.equals(""))
      {
        String str3 = this.osMajorVersion;
        int m = CodedOutputByteBufferNano.computeStringSize(3, str3);
        i += m;
      }
      if (!this.osFullVersion.equals(""))
      {
        String str4 = this.osFullVersion;
        int n = CodedOutputByteBufferNano.computeStringSize(4, str4);
        i += n;
      }
      if (!this.applicationBuild.equals(""))
      {
        String str5 = this.applicationBuild;
        int i1 = CodedOutputByteBufferNano.computeStringSize(5, str5);
        i += i1;
      }
      this.cachedSize = i;
      return i;
    }

    public IosClientInfo mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      while (true)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default:
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i))
            continue;
        case 0:
          return this;
        case 10:
          String str1 = paramCodedInputByteBufferNano.readString();
          this.clientId = str1;
          break;
        case 18:
          String str2 = paramCodedInputByteBufferNano.readString();
          this.loggingId = str2;
          break;
        case 26:
          String str3 = paramCodedInputByteBufferNano.readString();
          this.osMajorVersion = str3;
          break;
        case 34:
          String str4 = paramCodedInputByteBufferNano.readString();
          this.osFullVersion = str4;
          break;
        case 42:
        }
        String str5 = paramCodedInputByteBufferNano.readString();
        this.applicationBuild = str5;
      }
    }

    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (!this.clientId.equals(""))
      {
        String str1 = this.clientId;
        paramCodedOutputByteBufferNano.writeString(1, str1);
      }
      if (!this.loggingId.equals(""))
      {
        String str2 = this.loggingId;
        paramCodedOutputByteBufferNano.writeString(2, str2);
      }
      if (!this.osMajorVersion.equals(""))
      {
        String str3 = this.osMajorVersion;
        paramCodedOutputByteBufferNano.writeString(3, str3);
      }
      if (!this.osFullVersion.equals(""))
      {
        String str4 = this.osFullVersion;
        paramCodedOutputByteBufferNano.writeString(4, str4);
      }
      if (this.applicationBuild.equals(""))
        return;
      String str5 = this.applicationBuild;
      paramCodedOutputByteBufferNano.writeString(5, str5);
    }
  }

  public static final class DesktopClientInfo extends MessageNano
  {
    public static final DesktopClientInfo[] EMPTY_ARRAY = new DesktopClientInfo[0];
    public String applicationBuild = "";
    private int cachedSize = -1;
    public String clientId = "";
    public String loggingId = "";
    public String os = "";
    public String osFullVersion = "";
    public String osMajorVersion = "";

    public int getCachedSize()
    {
      if (this.cachedSize < 0)
        int i = getSerializedSize();
      return this.cachedSize;
    }

    public int getSerializedSize()
    {
      int i = 0;
      if (!this.clientId.equals(""))
      {
        String str1 = this.clientId;
        int j = CodedOutputByteBufferNano.computeStringSize(1, str1);
        i = 0 + j;
      }
      if (!this.loggingId.equals(""))
      {
        String str2 = this.loggingId;
        int k = CodedOutputByteBufferNano.computeStringSize(2, str2);
        i += k;
      }
      if (!this.os.equals(""))
      {
        String str3 = this.os;
        int m = CodedOutputByteBufferNano.computeStringSize(3, str3);
        i += m;
      }
      if (!this.osMajorVersion.equals(""))
      {
        String str4 = this.osMajorVersion;
        int n = CodedOutputByteBufferNano.computeStringSize(4, str4);
        i += n;
      }
      if (!this.osFullVersion.equals(""))
      {
        String str5 = this.osFullVersion;
        int i1 = CodedOutputByteBufferNano.computeStringSize(5, str5);
        i += i1;
      }
      if (!this.applicationBuild.equals(""))
      {
        String str6 = this.applicationBuild;
        int i2 = CodedOutputByteBufferNano.computeStringSize(6, str6);
        i += i2;
      }
      this.cachedSize = i;
      return i;
    }

    public DesktopClientInfo mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      while (true)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default:
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i))
            continue;
        case 0:
          return this;
        case 10:
          String str1 = paramCodedInputByteBufferNano.readString();
          this.clientId = str1;
          break;
        case 18:
          String str2 = paramCodedInputByteBufferNano.readString();
          this.loggingId = str2;
          break;
        case 26:
          String str3 = paramCodedInputByteBufferNano.readString();
          this.os = str3;
          break;
        case 34:
          String str4 = paramCodedInputByteBufferNano.readString();
          this.osMajorVersion = str4;
          break;
        case 42:
          String str5 = paramCodedInputByteBufferNano.readString();
          this.osFullVersion = str5;
          break;
        case 50:
        }
        String str6 = paramCodedInputByteBufferNano.readString();
        this.applicationBuild = str6;
      }
    }

    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (!this.clientId.equals(""))
      {
        String str1 = this.clientId;
        paramCodedOutputByteBufferNano.writeString(1, str1);
      }
      if (!this.loggingId.equals(""))
      {
        String str2 = this.loggingId;
        paramCodedOutputByteBufferNano.writeString(2, str2);
      }
      if (!this.os.equals(""))
      {
        String str3 = this.os;
        paramCodedOutputByteBufferNano.writeString(3, str3);
      }
      if (!this.osMajorVersion.equals(""))
      {
        String str4 = this.osMajorVersion;
        paramCodedOutputByteBufferNano.writeString(4, str4);
      }
      if (!this.osFullVersion.equals(""))
      {
        String str5 = this.osFullVersion;
        paramCodedOutputByteBufferNano.writeString(5, str5);
      }
      if (this.applicationBuild.equals(""))
        return;
      String str6 = this.applicationBuild;
      paramCodedOutputByteBufferNano.writeString(6, str6);
    }
  }

  public static final class AndroidClientInfo extends MessageNano
  {
    public static final AndroidClientInfo[] EMPTY_ARRAY = new AndroidClientInfo[0];
    public long androidId = 0L;
    public String applicationBuild = "";
    private int cachedSize = -1;
    public String country = "";
    public String device = "";
    public String hardware = "";
    public String locale = "";
    public String loggingId = "";
    public String manufacturer = "";
    public String mccMnc = "";
    public String model = "";
    public String osBuild = "";
    public String product = "";
    public int sdkVersion = 0;

    public int getCachedSize()
    {
      if (this.cachedSize < 0)
        int i = getSerializedSize();
      return this.cachedSize;
    }

    public int getSerializedSize()
    {
      int i = 0;
      if (this.androidId != 0L)
      {
        long l = this.androidId;
        int j = CodedOutputByteBufferNano.computeInt64Size(1, l);
        i = 0 + j;
      }
      if (!this.loggingId.equals(""))
      {
        String str1 = this.loggingId;
        int k = CodedOutputByteBufferNano.computeStringSize(2, str1);
        i += k;
      }
      if (this.sdkVersion != 0)
      {
        int m = this.sdkVersion;
        int n = CodedOutputByteBufferNano.computeInt32Size(3, m);
        i += n;
      }
      if (!this.model.equals(""))
      {
        String str2 = this.model;
        int i1 = CodedOutputByteBufferNano.computeStringSize(4, str2);
        i += i1;
      }
      if (!this.product.equals(""))
      {
        String str3 = this.product;
        int i2 = CodedOutputByteBufferNano.computeStringSize(5, str3);
        i += i2;
      }
      if (!this.osBuild.equals(""))
      {
        String str4 = this.osBuild;
        int i3 = CodedOutputByteBufferNano.computeStringSize(6, str4);
        i += i3;
      }
      if (!this.applicationBuild.equals(""))
      {
        String str5 = this.applicationBuild;
        int i4 = CodedOutputByteBufferNano.computeStringSize(7, str5);
        i += i4;
      }
      if (!this.hardware.equals(""))
      {
        String str6 = this.hardware;
        int i5 = CodedOutputByteBufferNano.computeStringSize(8, str6);
        i += i5;
      }
      if (!this.device.equals(""))
      {
        String str7 = this.device;
        int i6 = CodedOutputByteBufferNano.computeStringSize(9, str7);
        i += i6;
      }
      if (!this.mccMnc.equals(""))
      {
        String str8 = this.mccMnc;
        int i7 = CodedOutputByteBufferNano.computeStringSize(10, str8);
        i += i7;
      }
      if (!this.locale.equals(""))
      {
        String str9 = this.locale;
        int i8 = CodedOutputByteBufferNano.computeStringSize(11, str9);
        i += i8;
      }
      if (!this.country.equals(""))
      {
        String str10 = this.country;
        int i9 = CodedOutputByteBufferNano.computeStringSize(12, str10);
        i += i9;
      }
      if (!this.manufacturer.equals(""))
      {
        String str11 = this.manufacturer;
        int i10 = CodedOutputByteBufferNano.computeStringSize(13, str11);
        i += i10;
      }
      this.cachedSize = i;
      return i;
    }

    public AndroidClientInfo mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      while (true)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default:
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i))
            continue;
        case 0:
          return this;
        case 8:
          long l = paramCodedInputByteBufferNano.readInt64();
          this.androidId = l;
          break;
        case 18:
          String str1 = paramCodedInputByteBufferNano.readString();
          this.loggingId = str1;
          break;
        case 24:
          int j = paramCodedInputByteBufferNano.readInt32();
          this.sdkVersion = j;
          break;
        case 34:
          String str2 = paramCodedInputByteBufferNano.readString();
          this.model = str2;
          break;
        case 42:
          String str3 = paramCodedInputByteBufferNano.readString();
          this.product = str3;
          break;
        case 50:
          String str4 = paramCodedInputByteBufferNano.readString();
          this.osBuild = str4;
          break;
        case 58:
          String str5 = paramCodedInputByteBufferNano.readString();
          this.applicationBuild = str5;
          break;
        case 66:
          String str6 = paramCodedInputByteBufferNano.readString();
          this.hardware = str6;
          break;
        case 74:
          String str7 = paramCodedInputByteBufferNano.readString();
          this.device = str7;
          break;
        case 82:
          String str8 = paramCodedInputByteBufferNano.readString();
          this.mccMnc = str8;
          break;
        case 90:
          String str9 = paramCodedInputByteBufferNano.readString();
          this.locale = str9;
          break;
        case 98:
          String str10 = paramCodedInputByteBufferNano.readString();
          this.country = str10;
          break;
        case 106:
        }
        String str11 = paramCodedInputByteBufferNano.readString();
        this.manufacturer = str11;
      }
    }

    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (this.androidId != 0L)
      {
        long l = this.androidId;
        paramCodedOutputByteBufferNano.writeInt64(1, l);
      }
      if (!this.loggingId.equals(""))
      {
        String str1 = this.loggingId;
        paramCodedOutputByteBufferNano.writeString(2, str1);
      }
      if (this.sdkVersion != 0)
      {
        int i = this.sdkVersion;
        paramCodedOutputByteBufferNano.writeInt32(3, i);
      }
      if (!this.model.equals(""))
      {
        String str2 = this.model;
        paramCodedOutputByteBufferNano.writeString(4, str2);
      }
      if (!this.product.equals(""))
      {
        String str3 = this.product;
        paramCodedOutputByteBufferNano.writeString(5, str3);
      }
      if (!this.osBuild.equals(""))
      {
        String str4 = this.osBuild;
        paramCodedOutputByteBufferNano.writeString(6, str4);
      }
      if (!this.applicationBuild.equals(""))
      {
        String str5 = this.applicationBuild;
        paramCodedOutputByteBufferNano.writeString(7, str5);
      }
      if (!this.hardware.equals(""))
      {
        String str6 = this.hardware;
        paramCodedOutputByteBufferNano.writeString(8, str6);
      }
      if (!this.device.equals(""))
      {
        String str7 = this.device;
        paramCodedOutputByteBufferNano.writeString(9, str7);
      }
      if (!this.mccMnc.equals(""))
      {
        String str8 = this.mccMnc;
        paramCodedOutputByteBufferNano.writeString(10, str8);
      }
      if (!this.locale.equals(""))
      {
        String str9 = this.locale;
        paramCodedOutputByteBufferNano.writeString(11, str9);
      }
      if (!this.country.equals(""))
      {
        String str10 = this.country;
        paramCodedOutputByteBufferNano.writeString(12, str10);
      }
      if (this.manufacturer.equals(""))
        return;
      String str11 = this.manufacturer;
      paramCodedOutputByteBufferNano.writeString(13, str11);
    }
  }

  public static final class LogEvent extends MessageNano
  {
    public static final LogEvent[] EMPTY_ARRAY = new LogEvent[0];
    private int cachedSize;
    public long eventTimeMs = 0L;
    public ClientAnalytics.ActiveExperiments exp;
    public byte[] sourceExtension;
    public byte[] sourceExtensionJs;
    public String tag = "";
    public ClientAnalytics.LogEventKeyValues[] value;

    public LogEvent()
    {
      ClientAnalytics.LogEventKeyValues[] arrayOfLogEventKeyValues = ClientAnalytics.LogEventKeyValues.EMPTY_ARRAY;
      this.value = arrayOfLogEventKeyValues;
      byte[] arrayOfByte1 = WireFormatNano.EMPTY_BYTES;
      this.sourceExtension = arrayOfByte1;
      byte[] arrayOfByte2 = WireFormatNano.EMPTY_BYTES;
      this.sourceExtensionJs = arrayOfByte2;
      this.exp = null;
      this.cachedSize = -1;
    }

    public final LogEvent clear()
    {
      this.eventTimeMs = 0L;
      this.tag = "";
      ClientAnalytics.LogEventKeyValues[] arrayOfLogEventKeyValues = ClientAnalytics.LogEventKeyValues.EMPTY_ARRAY;
      this.value = arrayOfLogEventKeyValues;
      byte[] arrayOfByte1 = WireFormatNano.EMPTY_BYTES;
      this.sourceExtension = arrayOfByte1;
      byte[] arrayOfByte2 = WireFormatNano.EMPTY_BYTES;
      this.sourceExtensionJs = arrayOfByte2;
      this.exp = null;
      this.cachedSize = -1;
      return this;
    }

    public int getCachedSize()
    {
      if (this.cachedSize < 0)
        int i = getSerializedSize();
      return this.cachedSize;
    }

    public int getSerializedSize()
    {
      int i = 0;
      if (this.eventTimeMs != 0L)
      {
        long l = this.eventTimeMs;
        int j = CodedOutputByteBufferNano.computeInt64Size(1, l);
        i = 0 + j;
      }
      if (!this.tag.equals(""))
      {
        String str = this.tag;
        int k = CodedOutputByteBufferNano.computeStringSize(2, str);
        i += k;
      }
      ClientAnalytics.LogEventKeyValues[] arrayOfLogEventKeyValues = this.value;
      int m = arrayOfLogEventKeyValues.length;
      int n = 0;
      while (n < m)
      {
        ClientAnalytics.LogEventKeyValues localLogEventKeyValues = arrayOfLogEventKeyValues[n];
        int i1 = CodedOutputByteBufferNano.computeMessageSize(3, localLogEventKeyValues);
        i += i1;
        n += 1;
      }
      byte[] arrayOfByte1 = this.sourceExtension;
      byte[] arrayOfByte2 = WireFormatNano.EMPTY_BYTES;
      if (!Arrays.equals(arrayOfByte1, arrayOfByte2))
      {
        byte[] arrayOfByte3 = this.sourceExtension;
        int i2 = CodedOutputByteBufferNano.computeBytesSize(6, arrayOfByte3);
        i += i2;
      }
      if (this.exp != null)
      {
        ClientAnalytics.ActiveExperiments localActiveExperiments = this.exp;
        int i3 = CodedOutputByteBufferNano.computeMessageSize(7, localActiveExperiments);
        i += i3;
      }
      byte[] arrayOfByte4 = this.sourceExtensionJs;
      byte[] arrayOfByte5 = WireFormatNano.EMPTY_BYTES;
      if (!Arrays.equals(arrayOfByte4, arrayOfByte5))
      {
        byte[] arrayOfByte6 = this.sourceExtensionJs;
        int i4 = CodedOutputByteBufferNano.computeBytesSize(8, arrayOfByte6);
        i += i4;
      }
      this.cachedSize = i;
      return i;
    }

    public LogEvent mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      while (true)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default:
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i))
            continue;
        case 0:
          return this;
        case 8:
          long l = paramCodedInputByteBufferNano.readInt64();
          this.eventTimeMs = l;
          break;
        case 18:
          String str = paramCodedInputByteBufferNano.readString();
          this.tag = str;
          break;
        case 26:
          int j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 26);
          int k = this.value.length;
          ClientAnalytics.LogEventKeyValues[] arrayOfLogEventKeyValues1 = new ClientAnalytics.LogEventKeyValues[k + j];
          System.arraycopy(this.value, 0, arrayOfLogEventKeyValues1, 0, k);
          this.value = arrayOfLogEventKeyValues1;
          while (true)
          {
            int m = this.value.length + -1;
            if (k >= m)
              break;
            ClientAnalytics.LogEventKeyValues[] arrayOfLogEventKeyValues2 = this.value;
            ClientAnalytics.LogEventKeyValues localLogEventKeyValues1 = new ClientAnalytics.LogEventKeyValues();
            arrayOfLogEventKeyValues2[k] = localLogEventKeyValues1;
            ClientAnalytics.LogEventKeyValues localLogEventKeyValues2 = this.value[k];
            paramCodedInputByteBufferNano.readMessage(localLogEventKeyValues2);
            int n = paramCodedInputByteBufferNano.readTag();
            k += 1;
          }
          ClientAnalytics.LogEventKeyValues[] arrayOfLogEventKeyValues3 = this.value;
          ClientAnalytics.LogEventKeyValues localLogEventKeyValues3 = new ClientAnalytics.LogEventKeyValues();
          arrayOfLogEventKeyValues3[k] = localLogEventKeyValues3;
          ClientAnalytics.LogEventKeyValues localLogEventKeyValues4 = this.value[k];
          paramCodedInputByteBufferNano.readMessage(localLogEventKeyValues4);
          break;
        case 50:
          byte[] arrayOfByte1 = paramCodedInputByteBufferNano.readBytes();
          this.sourceExtension = arrayOfByte1;
          break;
        case 58:
          ClientAnalytics.ActiveExperiments localActiveExperiments1 = new ClientAnalytics.ActiveExperiments();
          this.exp = localActiveExperiments1;
          ClientAnalytics.ActiveExperiments localActiveExperiments2 = this.exp;
          paramCodedInputByteBufferNano.readMessage(localActiveExperiments2);
          break;
        case 66:
        }
        byte[] arrayOfByte2 = paramCodedInputByteBufferNano.readBytes();
        this.sourceExtensionJs = arrayOfByte2;
      }
    }

    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (this.eventTimeMs != 0L)
      {
        long l = this.eventTimeMs;
        paramCodedOutputByteBufferNano.writeInt64(1, l);
      }
      if (!this.tag.equals(""))
      {
        String str = this.tag;
        paramCodedOutputByteBufferNano.writeString(2, str);
      }
      ClientAnalytics.LogEventKeyValues[] arrayOfLogEventKeyValues = this.value;
      int i = arrayOfLogEventKeyValues.length;
      int j = 0;
      while (j < i)
      {
        ClientAnalytics.LogEventKeyValues localLogEventKeyValues = arrayOfLogEventKeyValues[j];
        paramCodedOutputByteBufferNano.writeMessage(3, localLogEventKeyValues);
        j += 1;
      }
      byte[] arrayOfByte1 = this.sourceExtension;
      byte[] arrayOfByte2 = WireFormatNano.EMPTY_BYTES;
      if (!Arrays.equals(arrayOfByte1, arrayOfByte2))
      {
        byte[] arrayOfByte3 = this.sourceExtension;
        paramCodedOutputByteBufferNano.writeBytes(6, arrayOfByte3);
      }
      if (this.exp != null)
      {
        ClientAnalytics.ActiveExperiments localActiveExperiments = this.exp;
        paramCodedOutputByteBufferNano.writeMessage(7, localActiveExperiments);
      }
      byte[] arrayOfByte4 = this.sourceExtensionJs;
      byte[] arrayOfByte5 = WireFormatNano.EMPTY_BYTES;
      if (Arrays.equals(arrayOfByte4, arrayOfByte5))
        return;
      byte[] arrayOfByte6 = this.sourceExtensionJs;
      paramCodedOutputByteBufferNano.writeBytes(8, arrayOfByte6);
    }
  }

  public static final class ActiveExperiments extends MessageNano
  {
    public static final ActiveExperiments[] EMPTY_ARRAY = new ActiveExperiments[0];
    private int cachedSize;
    public String[] clientAlteringExperiment;
    public int[] gwsExperiment;
    public String[] otherExperiment;

    public ActiveExperiments()
    {
      String[] arrayOfString1 = WireFormatNano.EMPTY_STRING_ARRAY;
      this.clientAlteringExperiment = arrayOfString1;
      String[] arrayOfString2 = WireFormatNano.EMPTY_STRING_ARRAY;
      this.otherExperiment = arrayOfString2;
      int[] arrayOfInt = WireFormatNano.EMPTY_INT_ARRAY;
      this.gwsExperiment = arrayOfInt;
      this.cachedSize = -1;
    }

    public int getCachedSize()
    {
      if (this.cachedSize < 0)
        int i = getSerializedSize();
      return this.cachedSize;
    }

    public int getSerializedSize()
    {
      int i = 0;
      int j;
      Object localObject;
      int k;
      int m;
      if (this.clientAlteringExperiment.length > 0)
      {
        j = 0;
        localObject = this.clientAlteringExperiment;
        k = localObject.length;
        m = 0;
        while (m < k)
        {
          int n = CodedOutputByteBufferNano.computeStringSizeNoTag(localObject[m]);
          j += n;
          m += 1;
        }
        int i1 = 0 + j;
        int i2 = this.clientAlteringExperiment.length * 1;
        i = i1 + i2;
      }
      if (this.otherExperiment.length > 0)
      {
        j = 0;
        localObject = this.otherExperiment;
        k = localObject.length;
        m = 0;
        while (m < k)
        {
          int i3 = CodedOutputByteBufferNano.computeStringSizeNoTag(localObject[m]);
          int i4 = j + i3;
          int i5 = m + 1;
        }
        int i6 = i + j;
        int i7 = this.otherExperiment.length * 1;
        i = i6 + i7;
      }
      if (this.gwsExperiment.length > 0)
      {
        j = 0;
        localObject = this.gwsExperiment;
        k = localObject.length;
        m = 0;
        while (m < k)
        {
          int i8 = CodedOutputByteBufferNano.computeInt32SizeNoTag(localObject[m]);
          int i9 = j + i8;
          int i10 = m + 1;
        }
        int i11 = i + j;
        int i12 = this.gwsExperiment.length * 1;
        i = i11 + i12;
      }
      this.cachedSize = i;
      return i;
    }

    public ActiveExperiments mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      while (true)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default:
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i))
            continue;
        case 0:
          return this;
        case 10:
          int j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 10);
          k = this.clientAlteringExperiment.length;
          String[] arrayOfString1 = new String[k + j];
          System.arraycopy(this.clientAlteringExperiment, 0, arrayOfString1, 0, k);
          this.clientAlteringExperiment = arrayOfString1;
          while (true)
          {
            int m = this.clientAlteringExperiment.length + -1;
            if (k >= m)
              break;
            String[] arrayOfString2 = this.clientAlteringExperiment;
            String str1 = paramCodedInputByteBufferNano.readString();
            arrayOfString2[k] = str1;
            int n = paramCodedInputByteBufferNano.readTag();
            int i1 = k + 1;
          }
          String[] arrayOfString3 = this.clientAlteringExperiment;
          String str2 = paramCodedInputByteBufferNano.readString();
          arrayOfString3[k] = str2;
          break;
        case 18:
          int i2 = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 18);
          k = this.otherExperiment.length;
          String[] arrayOfString4 = new String[k + i2];
          System.arraycopy(this.otherExperiment, 0, arrayOfString4, 0, k);
          this.otherExperiment = arrayOfString4;
          while (true)
          {
            int i3 = this.otherExperiment.length + -1;
            if (k >= i3)
              break;
            String[] arrayOfString5 = this.otherExperiment;
            String str3 = paramCodedInputByteBufferNano.readString();
            arrayOfString5[k] = str3;
            int i4 = paramCodedInputByteBufferNano.readTag();
            int i5 = k + 1;
          }
          String[] arrayOfString6 = this.otherExperiment;
          String str4 = paramCodedInputByteBufferNano.readString();
          arrayOfString6[k] = str4;
          break;
        case 24:
        }
        int i6 = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 24);
        int k = this.gwsExperiment.length;
        int[] arrayOfInt1 = new int[k + i6];
        System.arraycopy(this.gwsExperiment, 0, arrayOfInt1, 0, k);
        this.gwsExperiment = arrayOfInt1;
        while (true)
        {
          int i7 = this.gwsExperiment.length + -1;
          if (k >= i7)
            break;
          int[] arrayOfInt2 = this.gwsExperiment;
          int i8 = paramCodedInputByteBufferNano.readInt32();
          arrayOfInt2[k] = i8;
          int i9 = paramCodedInputByteBufferNano.readTag();
          k += 1;
        }
        int[] arrayOfInt3 = this.gwsExperiment;
        int i10 = paramCodedInputByteBufferNano.readInt32();
        arrayOfInt3[k] = i10;
      }
    }

    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      String[] arrayOfString1 = this.clientAlteringExperiment;
      int i = arrayOfString1.length;
      int j = 0;
      while (j < i)
      {
        String str1 = arrayOfString1[j];
        paramCodedOutputByteBufferNano.writeString(1, str1);
        j += 1;
      }
      String[] arrayOfString2 = this.otherExperiment;
      int k = arrayOfString2.length;
      int m = 0;
      while (m < k)
      {
        String str2 = arrayOfString2[m];
        paramCodedOutputByteBufferNano.writeString(2, str2);
        m += 1;
      }
      int[] arrayOfInt = this.gwsExperiment;
      int n = arrayOfInt.length;
      int i1 = 0;
      while (true)
      {
        if (i1 >= n)
          return;
        int i2 = arrayOfInt[i1];
        paramCodedOutputByteBufferNano.writeInt32(3, i2);
        i1 += 1;
      }
    }
  }

  public static final class LogEventKeyValues extends MessageNano
  {
    public static final LogEventKeyValues[] EMPTY_ARRAY = new LogEventKeyValues[0];
    private int cachedSize = -1;
    public String key = "";
    public String value = "";

    public final LogEventKeyValues clear()
    {
      this.key = "";
      this.value = "";
      this.cachedSize = -1;
      return this;
    }

    public int getCachedSize()
    {
      if (this.cachedSize < 0)
        int i = getSerializedSize();
      return this.cachedSize;
    }

    public int getSerializedSize()
    {
      int i = 0;
      if (!this.key.equals(""))
      {
        String str1 = this.key;
        int j = CodedOutputByteBufferNano.computeStringSize(1, str1);
        i = 0 + j;
      }
      if (!this.value.equals(""))
      {
        String str2 = this.value;
        int k = CodedOutputByteBufferNano.computeStringSize(2, str2);
        i += k;
      }
      this.cachedSize = i;
      return i;
    }

    public LogEventKeyValues mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      while (true)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default:
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i))
            continue;
        case 0:
          return this;
        case 10:
          String str1 = paramCodedInputByteBufferNano.readString();
          this.key = str1;
          break;
        case 18:
        }
        String str2 = paramCodedInputByteBufferNano.readString();
        this.value = str2;
      }
    }

    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      if (!this.key.equals(""))
      {
        String str1 = this.key;
        paramCodedOutputByteBufferNano.writeString(1, str1);
      }
      if (this.value.equals(""))
        return;
      String str2 = this.value;
      paramCodedOutputByteBufferNano.writeString(2, str2);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.play.analytics.ClientAnalytics
 * JD-Core Version:    0.6.2
 */