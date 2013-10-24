package com.google.android.music.medialist;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.android.music.AsyncCursor;
import com.google.android.music.cloudclient.JsonUtils;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.download.artwork.AlbumIdSink;
import com.google.android.music.store.MusicContent;
import com.google.android.music.store.MusicContent.XAudio;
import com.google.android.music.store.Store;
import com.google.android.music.sync.google.model.Track;
import com.google.android.music.utils.MusicUtils;
import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public abstract class MediaList
  implements Parcelable
{
  public static final Parcelable.Creator<MediaList> CREATOR = null;
  private final ContentIdentifier.Domain mDomain;
  private int mFlags = 2147483647;
  protected final boolean mShouldFilter;
  protected final boolean mShouldIncludeExternal;

  MediaList(ContentIdentifier.Domain paramDomain, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mDomain = paramDomain;
    this.mShouldFilter = paramBoolean1;
    this.mShouldIncludeExternal = paramBoolean2;
    if ((!isDefaultDomain()) && (!(this instanceof ExternalSongList)) && (!(this instanceof NautilusMediaList)))
      throw new IllegalStateException("Media list must be ExternalSongList or NautilusMediaList if domain is not default");
    Class localClass = getClass();
    Constructor[] arrayOfConstructor = localClass.getConstructors();
    int i = 0;
    while (true)
    {
      int j = arrayOfConstructor.length;
      if (i >= j)
        return;
      if (arrayOfConstructor[i].getParameterTypes().length != 0);
      try
      {
        Class[] arrayOfClass = (Class[])null;
        Method localMethod = localClass.getDeclaredMethod("getArgs", arrayOfClass);
        i += 1;
      }
      catch (SecurityException localSecurityException)
      {
        StringBuilder localStringBuilder1 = new StringBuilder();
        String str1 = localClass.getName();
        String str2 = str1 + " must implement getArgs";
        throw new RuntimeException(str2, localSecurityException);
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        StringBuilder localStringBuilder2 = new StringBuilder();
        String str3 = localClass.getName();
        String str4 = str3 + " must implement getArgs";
        throw new RuntimeException(str4, localNoSuchMethodException);
      }
    }
  }

  private static long[] decodeLongsArg(String paramString)
  {
    int i;
    if ((paramString == null) || ("<null>".equals(paramString)))
      i = 0;
    while (true)
    {
      return i;
      if (paramString.length() == 0)
      {
        Object localObject = null;
      }
      else
      {
        String str = Character.toString(':');
        String[] arrayOfString = paramString.split(str);
        int k = arrayOfString.length;
        int j = arrayOfString.length;
        int m = 0;
        while (m < k)
        {
          long l = Long.parseLong(arrayOfString[m]);
          j[m] = l;
          m += 1;
        }
      }
    }
  }

  private static String[] decodeStringsArg(String paramString)
  {
    return MusicUtils.decodeStringArray(paramString);
  }

  protected static String encodeArg(long[] paramArrayOfLong)
  {
    String str;
    if (paramArrayOfLong == null)
      str = "<null>";
    while (true)
    {
      return str;
      if (paramArrayOfLong.length == 0)
        str = "";
      else
        str = Longs.join(Character.toString(':'), paramArrayOfLong);
    }
  }

  protected static String encodeArg(String[] paramArrayOfString)
  {
    return MusicUtils.encodeStringArray(paramArrayOfString);
  }

  public static MediaList thaw(String paramString)
  {
    Object localObject1 = null;
    int i;
    Class localClass1;
    String[] arrayOfString1;
    Object localObject2;
    int n;
    label89: label96: label119: int m;
    label142: Object localObject3;
    try
    {
      i = paramString.indexOf(',');
      int j;
      if (i > 0)
      {
        localObject1 = paramString.substring(0, i);
        localClass1 = thawClass((String)localObject1);
        localObject1 = (Constructor[])localClass1.getConstructors();
        j = localObject1.length;
        if (i >= 0)
          break label96;
      }
      int i1;
      for (arrayOfString1 = null; ; arrayOfString1 = paramString.substring(i1).split(",", -1))
      {
        if (j != 1)
          break label119;
        localObject2 = localObject1[0];
        int k = localObject2.getParameterTypes();
        n = k.length;
        if (n != 0)
          break label343;
        Object[] arrayOfObject2 = (Object[])null;
        localObject1 = (MediaList)localObject2.newInstance(arrayOfObject2);
        return localObject1;
        localObject1 = paramString;
        break;
        i1 = i + 1;
      }
      localTreeSet = Sets.newTreeSet();
      if (arrayOfString1 == null)
      {
        n = 0;
        int i2 = localObject1.length;
        m = 0;
        localObject2 = null;
        if (m >= i2)
          break label292;
        localObject3 = localObject1[m];
        i4 = localObject3.getParameterTypes().length;
        Integer localInteger1 = Integer.valueOf(i4);
        if (!localTreeSet.contains(localInteger1))
          break label254;
        String str1 = "Class: " + localClass1 + " has multiple " + "constructors with the same number of parameters: " + i4;
        throw new IllegalArgumentException(str1);
      }
    }
    catch (Exception localException)
    {
      TreeSet localTreeSet;
      int i4;
      while (true)
      {
        int i5 = Log.e("MediaList", "Error thawing medialist", localException);
        localObject1 = null;
        continue;
        n = arrayOfString1.length;
      }
      label254: Integer localInteger2 = Integer.valueOf(i4);
      boolean bool = localTreeSet.add(localInteger2);
      if (n == i4);
    }
    while (true)
    {
      m += 1;
      localObject2 = localObject3;
      break label142;
      label292: if (localObject2 != null)
        break;
      String str2 = "Could not find a constructor for class: " + localClass1 + " with " + n + " arguments";
      throw new IllegalArgumentException(str2);
      label343: if (i < 0)
        throw new IllegalArgumentException("no args found");
      int i6 = arrayOfString1.length;
      if (n != i6)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Argument count mismatch: got ");
        int i7 = arrayOfString1.length;
        String str3 = i7 + ", expected " + n;
        int i8 = Log.e("MediaList", str3);
        localObject1 = null;
        break label89;
      }
      Object[] arrayOfObject1 = new Object[n];
      localObject3 = null;
      if (localObject3 < n)
      {
        localObject1 = Uri.decode(arrayOfString1[localObject3]);
        if (((String)localObject1).equals("<null>"))
          localObject1 = null;
        Object localObject5 = m[localObject3];
        Class localClass2 = Long.TYPE;
        if (localObject5 == localClass2)
        {
          Long localLong = Long.valueOf((String)localObject1);
          arrayOfObject1[localObject3] = localLong;
        }
        int i3;
        label638: 
        do
        {
          while (true)
          {
            i3 = localObject3 + 1;
            break;
            if (m[i3] == String.class)
            {
              arrayOfObject1[i3] = localObject1;
            }
            else
            {
              Object localObject6 = m[i3];
              Class localClass3 = Integer.TYPE;
              if (localObject6 == localClass3)
              {
                Integer localInteger3 = Integer.valueOf((String)localObject1);
                arrayOfObject1[i3] = localInteger3;
              }
              else if (m[i3] == [J.class)
              {
                long[] arrayOfLong = decodeLongsArg((String)localObject1);
                arrayOfObject1[i3] = arrayOfLong;
              }
              else if (m[i3] == [Ljava.lang.String.class)
              {
                String[] arrayOfString2 = decodeStringsArg((String)localObject1);
                arrayOfObject1[i3] = arrayOfString2;
              }
              else
              {
                Object localObject7 = m[i3];
                Class localClass4 = Boolean.TYPE;
                if (localObject7 != localClass4)
                  break label638;
                Boolean localBoolean = Boolean.valueOf((String)localObject1);
                arrayOfObject1[i3] = localBoolean;
              }
            }
          }
          if (m[i3] == Uri.class)
          {
            if (localObject1 == null);
            for (localObject1 = null; ; localObject1 = Uri.parse((String)localObject1))
            {
              arrayOfObject1[i3] = localObject1;
              break;
            }
          }
          if (m[i3] == SongData.class)
          {
            if (localObject1 == null);
            for (localObject1 = null; ; localObject1 = SongData.parseFromJson((String)localObject1))
            {
              arrayOfObject1[i3] = localObject1;
              break;
            }
          }
          if (m[i3] == SongDataList.class)
          {
            if (localObject1 == null);
            for (localObject1 = null; ; localObject1 = SongDataList.parseFromJson((String)localObject1))
            {
              arrayOfObject1[i3] = localObject1;
              break;
            }
          }
        }
        while (m[i3] != Track.class);
        if (localObject1 == null);
        for (localObject1 = null; ; localObject1 = (Track)JsonUtils.parseFromJsonString(Track.class, (String)localObject1))
        {
          arrayOfObject1[i3] = localObject1;
          break;
        }
      }
      localObject1 = (MediaList)localObject2.newInstance(arrayOfObject1);
      break label89;
      Object localObject4 = localObject2;
    }
  }

  static Class<MediaList> thawClass(String paramString)
    throws ClassNotFoundException
  {
    try
    {
      Class localClass1 = Class.forName(paramString);
      localClass2 = localClass1;
      return localClass2;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      Class localClass2;
      while (paramString.startsWith("com.android.music."))
      {
        StringBuilder localStringBuilder = new StringBuilder().append("com.google.android.music.");
        int i = "com.android.music.".length();
        String str = paramString.substring(i);
        localClass2 = Class.forName(str);
      }
      throw localClassNotFoundException;
    }
  }

  protected final void clearFlag(int paramInt)
  {
    int i = this.mFlags;
    int j = paramInt ^ 0xFFFFFFFF;
    int k = i & j;
    this.mFlags = k;
  }

  protected AsyncCursor createAsyncCursor(Context paramContext, String[] paramArrayOfString, String paramString, boolean paramBoolean)
  {
    AsyncCursor localAsyncCursor = null;
    Uri localUri1 = getContentUri(paramContext);
    if (localUri1 == null);
    while (true)
    {
      return localAsyncCursor;
      Uri localUri2 = filterUri(localUri1, paramString);
      boolean bool1 = this.mShouldFilter;
      boolean bool2 = this.mShouldIncludeExternal;
      Context localContext = paramContext;
      String[] arrayOfString1 = paramArrayOfString;
      String[] arrayOfString2 = null;
      String str = null;
      boolean bool3 = paramBoolean;
      localAsyncCursor = new AsyncCursor(localContext, localUri2, arrayOfString1, null, arrayOfString2, str, bool3, bool1, bool2);
    }
  }

  protected MediaCursor createMediaCursor(Context paramContext, Cursor paramCursor)
  {
    return new MediaCursor(paramCursor);
  }

  protected Cursor createSyncCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    Cursor localCursor = null;
    Uri localUri1 = getContentUri(paramContext);
    if (localUri1 == null);
    while (true)
    {
      return localCursor;
      Uri localUri2 = filterUri(localUri1, paramString);
      boolean bool1 = this.mShouldFilter;
      boolean bool2 = this.mShouldIncludeExternal;
      Context localContext = paramContext;
      String[] arrayOfString1 = paramArrayOfString;
      String[] arrayOfString2 = null;
      String str = null;
      localCursor = MusicUtils.query(localContext, localUri2, arrayOfString1, null, arrayOfString2, str, bool1, bool2);
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public void dump(PrintWriter paramPrintWriter)
  {
    StringBuilder localStringBuilder = new StringBuilder().append("  class=");
    String str1 = getClass().getName();
    String str2 = str1;
    paramPrintWriter.println(str2);
    String[] arrayOfString = getArgs();
    if (arrayOfString == null)
      return;
    if (arrayOfString.length == 0)
      return;
    int i = 0;
    int j = arrayOfString.length;
    if (i >= j)
      return;
    paramPrintWriter.print("    ");
    paramPrintWriter.print(i);
    paramPrintWriter.print(": ");
    String str3 = arrayOfString[i];
    if (str3 == null)
      paramPrintWriter.println("    <null>");
    while (true)
    {
      i += 1;
      break;
      paramPrintWriter.println(str3);
    }
  }

  public boolean equals(Object paramObject)
  {
    boolean bool;
    if ((paramObject == null) || (!(paramObject instanceof MediaList)))
      bool = false;
    while (true)
    {
      return bool;
      if (paramObject == this)
      {
        bool = true;
      }
      else
      {
        String str1 = ((MediaList)paramObject).freeze();
        String str2 = freeze();
        bool = str1.equals(str2);
      }
    }
  }

  protected Uri filterUri(Uri paramUri, String paramString)
  {
    return paramUri;
  }

  public final String freeze()
  {
    String[] arrayOfString = getArgs();
    if ((arrayOfString == null) || (arrayOfString.length == 0));
    StringBuilder localStringBuilder1;
    for (String str1 = getClass().getName(); ; str1 = localStringBuilder1.toString())
    {
      return str1;
      localStringBuilder1 = new StringBuilder();
      String str2 = getClass().getName();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str2);
      int i = 0;
      while (true)
      {
        int j = arrayOfString.length;
        if (i >= j)
          break;
        StringBuilder localStringBuilder3 = localStringBuilder1.append(',');
        String str3 = arrayOfString[i];
        if (str3 == null)
          str3 = "<null>";
        String str4 = Uri.encode(str3);
        StringBuilder localStringBuilder4 = localStringBuilder1.append(str4);
        i += 1;
      }
    }
  }

  public long getAlbumId(Context paramContext)
  {
    return 65535L;
  }

  public String[] getArgs()
  {
    return new String[0];
  }

  public long getArtistId(Context paramContext)
  {
    return 65535L;
  }

  protected abstract Uri getContentUri(Context paramContext);

  public final MediaCursor getCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    return getCursor(paramContext, paramArrayOfString, paramString, false);
  }

  public final MediaCursor getCursor(Context paramContext, String[] paramArrayOfString, String paramString, boolean paramBoolean)
  {
    Object localObject = createAsyncCursor(paramContext, paramArrayOfString, paramString, paramBoolean);
    if (localObject == null)
      localObject = createSyncCursor(paramContext, paramArrayOfString, paramString);
    if (localObject != null);
    for (MediaCursor localMediaCursor = createMediaCursor(paramContext, (Cursor)localObject); ; localMediaCursor = null)
      return localMediaCursor;
  }

  public ContentIdentifier.Domain getDomain()
  {
    return this.mDomain;
  }

  public int getFlags()
  {
    return this.mFlags;
  }

  public final Uri getFullContentUri(Context paramContext)
  {
    Uri localUri1 = getContentUri(paramContext);
    if (localUri1 == null);
    boolean bool1;
    boolean bool2;
    for (Uri localUri2 = null; ; localUri2 = MusicContent.appendFilter(paramContext, localUri1, bool1, bool2))
    {
      return localUri2;
      bool1 = this.mShouldFilter;
      bool2 = this.mShouldIncludeExternal;
    }
  }

  public Bitmap getImage(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, boolean paramBoolean)
  {
    return null;
  }

  public String getName(Context paramContext)
  {
    return null;
  }

  public String getSecondaryName(Context paramContext)
  {
    return null;
  }

  public boolean getShouldFilter()
  {
    return this.mShouldFilter;
  }

  public Cursor getSongCursor(Context paramContext, ContentIdentifier paramContentIdentifier, String[] paramArrayOfString)
  {
    Object localObject = null;
    Cursor localCursor = null;
    if (paramContentIdentifier == null);
    while (true)
    {
      return localObject;
      if (paramContentIdentifier.isDefaultDomain())
      {
        Uri localUri1 = MusicContent.XAudio.getRemoteAudio(paramContentIdentifier.getId());
        Context localContext1 = paramContext;
        String[] arrayOfString1 = paramArrayOfString;
        String[] arrayOfString2 = null;
        String str1 = null;
        localCursor = MusicUtils.query(localContext1, localUri1, arrayOfString1, null, arrayOfString2, str1);
      }
      if ((localCursor == null) || (localCursor.getCount() < 1))
      {
        Store.safeClose(localCursor);
        Uri localUri2 = MusicContent.XAudio.getAudioUri(paramContentIdentifier.getId());
        Context localContext2 = paramContext;
        String[] arrayOfString3 = paramArrayOfString;
        String[] arrayOfString4 = null;
        String str2 = null;
        localCursor = MusicUtils.query(localContext2, localUri2, arrayOfString3, null, arrayOfString4, str2);
      }
      localObject = localCursor;
    }
  }

  public String getStoreUrl()
  {
    return null;
  }

  public final MediaCursor getSyncMediaCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    Cursor localCursor = createSyncCursor(paramContext, paramArrayOfString, paramString);
    if (localCursor == null);
    for (MediaCursor localMediaCursor = null; ; localMediaCursor = createMediaCursor(paramContext, localCursor))
      return localMediaCursor;
  }

  public boolean hasDifferentTrackArtists(Context paramContext)
  {
    return true;
  }

  public int hashCode()
  {
    return freeze().hashCode();
  }

  public boolean isDefaultDomain()
  {
    ContentIdentifier.Domain localDomain1 = this.mDomain;
    ContentIdentifier.Domain localDomain2 = ContentIdentifier.Domain.DEFAULT;
    if (localDomain1 == localDomain2);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isEditable()
  {
    return false;
  }

  public boolean isFlagSet(int paramInt)
  {
    if ((getFlags() & paramInt) != 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isSharedDomain()
  {
    ContentIdentifier.Domain localDomain1 = this.mDomain;
    ContentIdentifier.Domain localDomain2 = ContentIdentifier.Domain.SHARED;
    if (localDomain1 == localDomain2);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str1 = getClass().getSimpleName();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(": ");
    String str2 = Arrays.toString(getArgs());
    return str2;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
  }

  public static class MediaCursor extends CursorWrapper
  {
    private final Cursor mCursor;

    public MediaCursor(Cursor paramCursor)
    {
      super();
      if (paramCursor == null)
        throw new RuntimeException("Wrapped cursor cannot be null");
      this.mCursor = paramCursor;
    }

    public int getCountSync()
    {
      if ((this.mCursor instanceof AsyncCursor));
      for (int i = ((AsyncCursor)this.mCursor).getCountSync(); ; i = getCount())
        return i;
    }

    public boolean hasCount()
    {
      if ((this.mCursor instanceof AsyncCursor));
      for (boolean bool = ((AsyncCursor)this.mCursor).hasCount(); ; bool = true)
        return bool;
    }

    public void moveItem(int paramInt1, int paramInt2)
    {
      throw new UnsupportedOperationException();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.MediaList
 * JD-Core Version:    0.6.2
 */