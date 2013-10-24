package com.google.android.music.mix;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public class MixDescriptor
  implements Parcelable
{
  public static final Parcelable.Creator<MixDescriptor> CREATOR = new Parcelable.Creator()
  {
    public MixDescriptor createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MixDescriptor(paramAnonymousParcel, null);
    }

    public MixDescriptor[] newArray(int paramAnonymousInt)
    {
      return new MixDescriptor[paramAnonymousInt];
    }
  };
  private final String mArtLocation;
  private final long mLocalRadioId;
  private final long mLocalSeedId;
  private final String mName;
  private final String mRemoteSeedId;
  private final Type mType;

  public MixDescriptor(long paramLong, Type paramType, String paramString1, String paramString2)
  {
    if (paramLong < 0L)
      throw new IllegalArgumentException("Invalid seed id");
    this.mRemoteSeedId = "";
    if (paramType == null)
      throw new IllegalArgumentException("Missing type");
    Type localType = Type.RADIO;
    if (paramType == localType)
      this.mLocalSeedId = 65535L;
    for (this.mLocalRadioId = paramLong; ; this.mLocalRadioId = 65535L)
    {
      this.mType = paramType;
      this.mName = paramString1;
      this.mArtLocation = paramString2;
      return;
      this.mLocalSeedId = paramLong;
    }
  }

  private MixDescriptor(Parcel paramParcel)
  {
    long l1 = paramParcel.readLong();
    this.mLocalSeedId = l1;
    String str1 = paramParcel.readString();
    this.mRemoteSeedId = str1;
    long l2 = paramParcel.readLong();
    this.mLocalRadioId = l2;
    Type[] arrayOfType = Type.values();
    int i = paramParcel.readInt();
    Type localType = arrayOfType[i];
    this.mType = localType;
    String str2 = paramParcel.readString();
    this.mName = str2;
    String str3 = paramParcel.readString();
    this.mArtLocation = str3;
  }

  private MixDescriptor(String paramString)
  {
    Type localType = Type.LUCKY_RADIO;
    this.mType = localType;
    this.mLocalRadioId = 65535L;
    this.mRemoteSeedId = "";
    this.mLocalSeedId = 65535L;
    this.mName = paramString;
    this.mArtLocation = null;
  }

  public MixDescriptor(String paramString1, Type paramType, String paramString2, String paramString3)
  {
    if (paramString1 == null)
      throw new IllegalArgumentException("Missing remote seed id");
    this.mLocalSeedId = 65535L;
    this.mRemoteSeedId = paramString1;
    this.mLocalRadioId = 65535L;
    if (paramType == null)
      throw new IllegalArgumentException("Missing type");
    this.mType = paramType;
    this.mName = paramString2;
    this.mArtLocation = paramString3;
  }

  public static MixDescriptor getLuckyRadio(Context paramContext)
  {
    String str = paramContext.getString(2131231422);
    return new MixDescriptor(str);
  }

  public int describeContents()
  {
    return 0;
  }

  public boolean equals(Object paramObject)
  {
    boolean bool = false;
    if (!(paramObject instanceof MixDescriptor));
    while (true)
    {
      return bool;
      MixDescriptor localMixDescriptor = (MixDescriptor)paramObject;
      long l1 = this.mLocalSeedId;
      long l2 = localMixDescriptor.getLocalSeedId();
      if (l1 == l2)
      {
        String str1 = this.mRemoteSeedId;
        String str2 = localMixDescriptor.getRemoteSeedId();
        if (str1.equals(str2))
        {
          long l3 = this.mLocalRadioId;
          long l4 = localMixDescriptor.mLocalRadioId;
          if (l3 == l4)
          {
            Type localType1 = this.mType;
            Type localType2 = localMixDescriptor.getType();
            if (localType1.equals(localType2))
            {
              String str3 = this.mName;
              String str4 = localMixDescriptor.getName();
              if (str3.equals(str4))
                bool = true;
            }
          }
        }
      }
    }
  }

  public String getArtLocation()
  {
    return this.mArtLocation;
  }

  public long getLocalRadioId()
  {
    return this.mLocalRadioId;
  }

  public long getLocalSeedId()
  {
    return this.mLocalSeedId;
  }

  public String getName()
  {
    return this.mName;
  }

  public String getRemoteSeedId()
  {
    return this.mRemoteSeedId;
  }

  public Type getType()
  {
    return this.mType;
  }

  public boolean hasLocalSeedId()
  {
    if (this.mLocalSeedId >= 0L);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean hasRemoteSeedId()
  {
    if (!TextUtils.isEmpty(this.mRemoteSeedId));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public int hashCode()
  {
    long l1 = 0;
    long l2 = 0;
    long l3 = this.mLocalSeedId;
    long l4 = l2 + l3;
    int i = (int)(l1 + l4);
    int j = i * 31;
    int k = this.mRemoteSeedId.hashCode();
    int m = j + k;
    int n = i + m;
    long l5 = n;
    long l6 = n * 31;
    long l7 = this.mLocalRadioId;
    long l8 = l6 + l7;
    int i1 = (int)(l5 + l8);
    int i2 = i1 * 31;
    int i3 = this.mType.ordinal();
    int i4 = i2 + i3;
    int i5 = i1 + i4;
    int i6 = i5 * 31;
    int i7 = this.mName.hashCode();
    int i8 = i6 + i7;
    return i5 + i8;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("[");
    long l1 = this.mLocalSeedId;
    StringBuilder localStringBuilder3 = localStringBuilder1.append(l1);
    StringBuilder localStringBuilder4 = localStringBuilder1.append(", ");
    String str1 = this.mRemoteSeedId;
    StringBuilder localStringBuilder5 = localStringBuilder1.append(str1);
    long l2 = this.mLocalRadioId;
    StringBuilder localStringBuilder6 = localStringBuilder1.append(l2);
    StringBuilder localStringBuilder7 = localStringBuilder1.append(", ");
    StringBuilder localStringBuilder8 = localStringBuilder1.append(", ");
    Type localType = this.mType;
    StringBuilder localStringBuilder9 = localStringBuilder1.append(localType);
    StringBuilder localStringBuilder10 = localStringBuilder1.append(", ");
    String str2 = this.mName;
    StringBuilder localStringBuilder11 = localStringBuilder1.append(str2);
    StringBuilder localStringBuilder12 = localStringBuilder1.append(", ");
    String str3 = this.mArtLocation;
    StringBuilder localStringBuilder13 = localStringBuilder1.append(str3);
    StringBuilder localStringBuilder14 = localStringBuilder1.append("]");
    return localStringBuilder1.toString();
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    long l1 = this.mLocalSeedId;
    paramParcel.writeLong(l1);
    String str1 = this.mRemoteSeedId;
    paramParcel.writeString(str1);
    long l2 = this.mLocalRadioId;
    paramParcel.writeLong(l2);
    int i = this.mType.ordinal();
    paramParcel.writeInt(i);
    String str2 = this.mName;
    paramParcel.writeString(str2);
    String str3 = this.mArtLocation;
    paramParcel.writeString(str3);
  }

  public static enum Type
  {
    static
    {
      ALBUM_SEED = new Type("ALBUM_SEED", 2);
      ARTIST_SEED = new Type("ARTIST_SEED", 3);
      GENRE_SEED = new Type("GENRE_SEED", 4);
      LUCKY_RADIO = new Type("LUCKY_RADIO", 5);
      Type[] arrayOfType = new Type[6];
      Type localType1 = RADIO;
      arrayOfType[0] = localType1;
      Type localType2 = TRACK_SEED;
      arrayOfType[1] = localType2;
      Type localType3 = ALBUM_SEED;
      arrayOfType[2] = localType3;
      Type localType4 = ARTIST_SEED;
      arrayOfType[3] = localType4;
      Type localType5 = GENRE_SEED;
      arrayOfType[4] = localType5;
      Type localType6 = LUCKY_RADIO;
      arrayOfType[5] = localType6;
    }

    public boolean isSeed()
    {
      Type localType1 = TRACK_SEED;
      if (this != localType1)
      {
        Type localType2 = ALBUM_SEED;
        if (this != localType2)
        {
          Type localType3 = ARTIST_SEED;
          if (this != localType3)
          {
            Type localType4 = GENRE_SEED;
            if (this != localType4)
              break label44;
          }
        }
      }
      label44: for (boolean bool = true; ; bool = false)
        return bool;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.mix.MixDescriptor
 * JD-Core Version:    0.6.2
 */