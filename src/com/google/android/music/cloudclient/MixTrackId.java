package com.google.android.music.cloudclient;

import android.text.TextUtils;

public class MixTrackId
{
  private final String mRemoteId;
  private final Type mType;

  public MixTrackId(String paramString, Type paramType)
  {
    this.mRemoteId = paramString;
    this.mType = paramType;
  }

  public static MixTrackId createTrackId(String paramString, int paramInt)
  {
    MixTrackId localMixTrackId = null;
    if (TextUtils.isEmpty(paramString));
    while (true)
    {
      return localMixTrackId;
      Type localType1 = Type.LOCKER;
      switch (paramInt)
      {
      default:
      case 2:
      case 3:
      }
    }
    for (Type localType2 = Type.LOCKER; ; localType2 = Type.NAUTILUS)
    {
      localMixTrackId = new MixTrackId(paramString, localType2);
      break;
    }
  }

  public static int trackIdTypeToServerType(Type paramType)
  {
    int[] arrayOfInt = 1.$SwitchMap$com$google$android$music$cloudclient$MixTrackId$Type;
    int i = paramType.ordinal();
    switch (arrayOfInt[i])
    {
    default:
      throw new IllegalStateException("Unhandled track id type");
    case 1:
    case 2:
    }
    for (int j = 0; ; j = 1)
      return j;
  }

  public boolean equals(Object paramObject)
  {
    boolean bool = false;
    if (!(paramObject instanceof MixTrackId));
    while (true)
    {
      return bool;
      MixTrackId localMixTrackId = (MixTrackId)paramObject;
      String str1 = this.mRemoteId;
      String str2 = localMixTrackId.getRemoteId();
      if (str1.equals(str2))
      {
        Type localType1 = this.mType;
        Type localType2 = localMixTrackId.getType();
        if (localType1.equals(localType2))
          bool = true;
      }
    }
  }

  public String getRemoteId()
  {
    return this.mRemoteId;
  }

  public Type getType()
  {
    return this.mType;
  }

  public int hashCode()
  {
    int i = this.mRemoteId.hashCode() + 0;
    int j = 0 + i;
    int k = j * 31;
    int m = this.mType.ordinal();
    int n = k + m;
    return j + n;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("[");
    String str = this.mRemoteId;
    StringBuilder localStringBuilder3 = localStringBuilder1.append(str);
    StringBuilder localStringBuilder4 = localStringBuilder1.append(", ");
    Type localType = this.mType;
    StringBuilder localStringBuilder5 = localStringBuilder1.append(localType);
    StringBuilder localStringBuilder6 = localStringBuilder1.append("]");
    return localStringBuilder1.toString();
  }

  public static enum Type
  {
    static
    {
      Type[] arrayOfType = new Type[2];
      Type localType1 = LOCKER;
      arrayOfType[0] = localType1;
      Type localType2 = NAUTILUS;
      arrayOfType[1] = localType2;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.MixTrackId
 * JD-Core Version:    0.6.2
 */