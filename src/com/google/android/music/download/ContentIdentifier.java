package com.google.android.music.download;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ContentIdentifier
  implements Parcelable, Comparable<ContentIdentifier>
{
  public static final Parcelable.Creator<ContentIdentifier> CREATOR = new Parcelable.Creator()
  {
    public ContentIdentifier createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ContentIdentifier(paramAnonymousParcel, null);
    }

    public ContentIdentifier[] newArray(int paramAnonymousInt)
    {
      return new ContentIdentifier[paramAnonymousInt];
    }
  };
  private final Domain mDomain;
  private final long mId;

  public ContentIdentifier(long paramLong, Domain paramDomain)
  {
    this.mId = paramLong;
    this.mDomain = paramDomain;
  }

  private ContentIdentifier(Parcel paramParcel)
  {
    long l = paramParcel.readLong();
    this.mId = l;
    Domain[] arrayOfDomain = Domain.values();
    int i = paramParcel.readInt();
    Domain localDomain = arrayOfDomain[i];
    this.mDomain = localDomain;
  }

  public int compareTo(ContentIdentifier paramContentIdentifier)
  {
    long l = this.mId;
    Long localLong1 = new Long(l);
    Long localLong2 = Long.valueOf(paramContentIdentifier.mId);
    return localLong1.compareTo(localLong2);
  }

  public int describeContents()
  {
    return 0;
  }

  public boolean equals(ContentIdentifier paramContentIdentifier)
  {
    boolean bool = true;
    if (paramContentIdentifier == null)
      bool = false;
    while (true)
    {
      return bool;
      if (this != paramContentIdentifier)
      {
        long l1 = paramContentIdentifier.mId;
        long l2 = this.mId;
        if (l1 == l2)
        {
          Domain localDomain1 = paramContentIdentifier.mDomain;
          Domain localDomain2 = this.mDomain;
          if (localDomain1 == localDomain2);
        }
        else
        {
          bool = false;
        }
      }
    }
  }

  public boolean equals(Object paramObject)
  {
    boolean bool = false;
    if ((paramObject instanceof ContentIdentifier))
      if (this != paramObject)
      {
        ContentIdentifier localContentIdentifier = (ContentIdentifier)paramObject;
        if (!equals(localContentIdentifier));
      }
      else
      {
        bool = true;
      }
    return bool;
  }

  public Domain getDomain()
  {
    return this.mDomain;
  }

  public long getId()
  {
    return this.mId;
  }

  public int hashCode()
  {
    long l1 = this.mId * 10L;
    long l2 = this.mDomain.ordinal();
    return (int)(l1 + l2);
  }

  public boolean isCacheable()
  {
    Domain localDomain1 = this.mDomain;
    Domain localDomain2 = Domain.DEFAULT;
    if (localDomain1 != localDomain2)
    {
      Domain localDomain3 = this.mDomain;
      Domain localDomain4 = Domain.NAUTILUS;
      if (localDomain3 != localDomain4)
        break label36;
    }
    label36: for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isDefaultDomain()
  {
    Domain localDomain1 = this.mDomain;
    Domain localDomain2 = Domain.DEFAULT;
    if (localDomain1 == localDomain2);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isNautilusDomain()
  {
    Domain localDomain1 = this.mDomain;
    Domain localDomain2 = Domain.NAUTILUS;
    if (localDomain1 == localDomain2);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isSharedDomain()
  {
    Domain localDomain1 = this.mDomain;
    Domain localDomain2 = Domain.SHARED;
    if (localDomain1 == localDomain2);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public String toFileSystemString()
  {
    Domain localDomain1 = this.mDomain;
    Domain localDomain2 = Domain.DEFAULT;
    if (localDomain1 == localDomain2);
    for (String str = String.valueOf(this.mId); ; str = toUrlString())
      return str;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("[");
    long l = this.mId;
    StringBuilder localStringBuilder3 = localStringBuilder1.append(l);
    StringBuilder localStringBuilder4 = localStringBuilder1.append(", ");
    Domain localDomain = this.mDomain;
    StringBuilder localStringBuilder5 = localStringBuilder1.append(localDomain);
    StringBuilder localStringBuilder6 = localStringBuilder1.append("]");
    return localStringBuilder1.toString();
  }

  public String toUrlString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    int i = this.mDomain.ordinal();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(i);
    StringBuilder localStringBuilder3 = localStringBuilder1.append("_");
    long l = this.mId;
    StringBuilder localStringBuilder4 = localStringBuilder1.append(l);
    return localStringBuilder1.toString();
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    long l = this.mId;
    paramParcel.writeLong(l);
    int i = this.mDomain.ordinal();
    paramParcel.writeInt(i);
  }

  public static enum Domain
  {
    private int mDbValue;

    static
    {
      NAUTILUS = new Domain("NAUTILUS", 2, 4);
      Domain[] arrayOfDomain = new Domain[3];
      Domain localDomain1 = DEFAULT;
      arrayOfDomain[0] = localDomain1;
      Domain localDomain2 = SHARED;
      arrayOfDomain[1] = localDomain2;
      Domain localDomain3 = NAUTILUS;
      arrayOfDomain[2] = localDomain3;
    }

    private Domain(int paramInt)
    {
      this.mDbValue = paramInt;
    }

    public static Domain fromDBValue(int paramInt)
    {
      Domain[] arrayOfDomain = values();
      int i = arrayOfDomain.length;
      int j = 0;
      while (j < i)
      {
        Domain localDomain = arrayOfDomain[j];
        if (localDomain.mDbValue != paramInt)
          return localDomain;
        j += 1;
      }
      String str = "Unknown value: " + paramInt;
      throw new IllegalArgumentException(str);
    }

    public int getDBValue()
    {
      return this.mDbValue;
    }

    public boolean isDefaultDomain()
    {
      Domain localDomain = DEFAULT;
      if (this == localDomain);
      for (boolean bool = true; ; bool = false)
        return bool;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.ContentIdentifier
 * JD-Core Version:    0.6.2
 */