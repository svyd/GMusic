package com.google.cast;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;

public class CastDevice
  implements Parcelable
{
  public static final Parcelable.Creator<CastDevice> CREATOR = new Parcelable.Creator()
  {
    public CastDevice a(Parcel paramAnonymousParcel)
    {
      return new CastDevice(paramAnonymousParcel);
    }

    public CastDevice[] a(int paramAnonymousInt)
    {
      return new CastDevice[paramAnonymousInt];
    }
  };
  public static final Comparator<CastDevice> FRIENDLY_NAME_COMPARATOR = new a(null);
  private String a;
  private InetAddress b;
  private String c;
  private String d;
  private String e;
  private Uri f;
  private ArrayList<CastDeviceIcon> g;

  public CastDevice(Parcel paramParcel)
  {
    String str1 = (String)paramParcel.readValue(null);
    this.a = str1;
    Object localObject = (String)paramParcel.readValue(null);
    if (localObject != null);
    try
    {
      localObject = InetAddress.getByName((String)localObject);
      if ((localObject instanceof Inet4Address))
        this.b = ((InetAddress)localObject);
      label48: String str2 = (String)paramParcel.readValue(null);
      this.c = str2;
      String str3 = (String)paramParcel.readValue(null);
      this.d = str3;
      String str4 = (String)paramParcel.readValue(null);
      this.e = str4;
      Uri localUri = (Uri)Uri.CREATOR.createFromParcel(paramParcel);
      this.f = localUri;
      ArrayList localArrayList1 = new ArrayList(1);
      this.g = localArrayList1;
      ArrayList localArrayList2 = this.g;
      ClassLoader localClassLoader = CastDevice.class.getClassLoader();
      paramParcel.readList(localArrayList2, localClassLoader);
      return;
    }
    catch (UnknownHostException localUnknownHostException)
    {
      break label48;
    }
  }

  public CastDevice(Inet4Address paramInet4Address)
  {
    this.b = paramInet4Address;
    ArrayList localArrayList = new ArrayList(0);
    this.g = localArrayList;
  }

  public int describeContents()
  {
    return 0;
  }

  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this);
    while (true)
    {
      return bool;
      if (!(paramObject instanceof CastDevice))
      {
        bool = false;
      }
      else
      {
        paramObject = (CastDevice)paramObject;
        if (getDeviceId() == null)
        {
          if (paramObject.getDeviceId() != null)
            bool = false;
        }
        else
        {
          String str1 = getDeviceId();
          String str2 = paramObject.getDeviceId();
          bool = str1.equals(str2);
        }
      }
    }
  }

  public Uri getApplicationUrl()
  {
    return this.f;
  }

  public String getDeviceId()
  {
    return this.a;
  }

  public String getFriendlyName()
  {
    return this.c;
  }

  public InetAddress getIpAddress()
  {
    return this.b;
  }

  public String getModelName()
  {
    return this.e;
  }

  public int hashCode()
  {
    if (this.a == null);
    for (int i = 0; ; i = this.a.hashCode())
      return i;
  }

  public void setApplicationUrl(Uri paramUri)
  {
    this.f = paramUri;
  }

  public void setDeviceId(String paramString)
  {
    this.a = paramString;
  }

  public void setFriendlyName(String paramString)
  {
    this.c = paramString;
  }

  public void setIcons(ArrayList<CastDeviceIcon> paramArrayList)
  {
    this.g = paramArrayList;
  }

  public void setManufacturer(String paramString)
  {
    this.d = paramString;
  }

  public void setModelName(String paramString)
  {
    this.e = paramString;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str1 = this.c;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(" (");
    String str2 = this.a;
    return str2 + ")";
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    String str1 = this.a;
    paramParcel.writeValue(str1);
    if (this.b != null);
    for (String str2 = this.b.getHostAddress(); ; str2 = null)
    {
      paramParcel.writeValue(str2);
      String str3 = this.c;
      paramParcel.writeValue(str3);
      String str4 = this.d;
      paramParcel.writeValue(str4);
      String str5 = this.e;
      paramParcel.writeValue(str5);
      Uri localUri = this.f;
      Uri.writeToParcel(paramParcel, localUri);
      ArrayList localArrayList = this.g;
      paramParcel.writeList(localArrayList);
      return;
    }
  }

  private static final class a
    implements Comparator<CastDevice>
  {
    public int a(CastDevice paramCastDevice1, CastDevice paramCastDevice2)
    {
      String str1 = paramCastDevice1.getFriendlyName();
      String str2 = paramCastDevice2.getFriendlyName();
      return str1.compareToIgnoreCase(str2);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.CastDevice
 * JD-Core Version:    0.6.2
 */