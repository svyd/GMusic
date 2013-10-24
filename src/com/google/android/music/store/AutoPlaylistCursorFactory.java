package com.google.android.music.store;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AutoPlaylistCursorFactory
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.STORE);
  private final Set<Long> mAutoPlaylistIds;
  private final Context mContext;
  private boolean mHasStore = false;

  public AutoPlaylistCursorFactory(Context paramContext, long[] paramArrayOfLong)
  {
    this.mContext = paramContext;
    HashSet localHashSet = new HashSet();
    this.mAutoPlaylistIds = localHashSet;
    long[] arrayOfLong = paramArrayOfLong;
    int i = arrayOfLong.length;
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      long l = arrayOfLong[j];
      Set localSet = this.mAutoPlaylistIds;
      Long localLong = Long.valueOf(l);
      boolean bool = localSet.add(localLong);
      j += 1;
    }
  }

  private ArrayList<Object> createRow(Store paramStore, String[] paramArrayOfString, long paramLong)
  {
    int i = paramArrayOfString.length;
    ArrayList localArrayList = new ArrayList(i);
    Long localLong1 = null;
    int j = 0;
    int k = 0;
    int m = 0;
    while (true)
    {
      String str1;
      try
      {
        localCursor = paramStore.getKeeponAutoListInfo(paramLong);
        if ((localCursor != null) && (localCursor.moveToFirst()))
        {
          localLong1 = Long.valueOf(localCursor.getLong(0));
          j = localCursor.getInt(1);
          k = localCursor.getInt(2);
          int n = localCursor.getInt(3);
          m = n;
        }
        Store.safeClose(localCursor);
        int i1 = 0;
        int i2 = paramArrayOfString.length;
        if (i1 >= i2)
          break;
        str1 = paramArrayOfString[i1];
        if (str1.equals("_id"))
        {
          Long localLong2 = Long.valueOf(paramLong);
          boolean bool1 = localArrayList.add(localLong2);
          i1 += 1;
        }
      }
      finally
      {
        Cursor localCursor;
        Store.safeClose(localCursor);
      }
      Long localLong3 = Long.valueOf(paramLong);
      boolean bool2 = localArrayList.add(localLong3);
      continue;
      if (str1.equals("playlist_name"))
      {
        String str2 = getAutoPlaylistName(paramLong);
        boolean bool3 = localArrayList.add(str2);
      }
      else if (str1.equals("playlist_description"))
      {
        boolean bool4 = localArrayList.add(null);
      }
      else if (str1.equals("playlist_owner_name"))
      {
        boolean bool5 = localArrayList.add(null);
      }
      else if (str1.equals("playlist_share_token"))
      {
        boolean bool6 = localArrayList.add(null);
      }
      else if (str1.equals("playlist_art_url"))
      {
        boolean bool7 = localArrayList.add(null);
      }
      else if (str1.equals("playlist_owner_profile_photo_url"))
      {
        boolean bool8 = localArrayList.add(null);
      }
      else if (str1.equals("playlist_type"))
      {
        Integer localInteger1 = Integer.valueOf(100);
        boolean bool9 = localArrayList.add(localInteger1);
      }
      else if (str1.equals("KeepOnId"))
      {
        boolean bool10 = localArrayList.add(localLong1);
      }
      else
      {
        if (str1.equals("hasRemote"))
        {
          Store.ItemType localItemType1 = Store.ItemType.REMOTE;
          if (paramStore.getAutoPlaylistContains(paramLong, localItemType1));
          for (int i3 = 1; ; i3 = 0)
          {
            Integer localInteger2 = Integer.valueOf(i3);
            boolean bool11 = localArrayList.add(localInteger2);
            break;
          }
        }
        if (str1.equals("hasLocal"))
        {
          Store.ItemType localItemType2 = Store.ItemType.LOCAL;
          if (paramStore.getAutoPlaylistContains(paramLong, localItemType2));
          for (int i4 = 1; ; i4 = 0)
          {
            Integer localInteger3 = Integer.valueOf(i4);
            boolean bool12 = localArrayList.add(localInteger3);
            break;
          }
        }
        if (str1.equals("isAllLocal"))
        {
          Boolean localBoolean = Boolean.valueOf(paramStore.getAutoPlayIsAllLocal(paramLong));
          boolean bool13 = localArrayList.add(localBoolean);
        }
        else if (str1.equals("keeponSongCount"))
        {
          Integer localInteger4 = Integer.valueOf(j);
          boolean bool14 = localArrayList.add(localInteger4);
        }
        else if (str1.equals("keeponDownloadedSongCount"))
        {
          Integer localInteger5 = Integer.valueOf(k);
          boolean bool15 = localArrayList.add(localInteger5);
        }
        else if (str1.equals("DateAdded"))
        {
          Integer localInteger6 = Integer.valueOf(m);
          boolean bool16 = localArrayList.add(localInteger6);
        }
        else
        {
          String str3 = "Ignoring projection: " + str1;
          int i5 = Log.w("AutoPlaylistFactory", str3);
        }
      }
    }
    return localArrayList;
  }

  private String getAutoPlaylistName(long paramLong)
  {
    String str;
    if (paramLong == 65532L)
      str = this.mContext.getString(2131230888);
    while (true)
    {
      return str;
      if (paramLong == 65535L)
      {
        str = this.mContext.getString(2131230880);
      }
      else
      {
        if (paramLong == 65533L)
        {
          Context localContext = this.mContext;
          if (this.mHasStore);
          for (int i = 2131230884; ; i = 2131230886)
          {
            str = localContext.getString(i);
            break;
          }
        }
        if (paramLong == 65534L)
          str = this.mContext.getString(2131230882);
        else
          str = null;
      }
    }
  }

  private boolean showLastAddedSongs()
  {
    Context localContext = this.mContext;
    Uri localUri = MusicContent.XAudio.CONTENT_URI;
    return MusicContent.existsContent(localContext, localUri);
  }

  private boolean showStoreSongs()
  {
    Context localContext = this.mContext;
    Uri localUri = MusicContent.AutoPlaylists.Members.getAutoPlaylistItemsUri(65533L, null);
    return MusicContent.existsContent(localContext, localUri);
  }

  private boolean showThumbsSongs()
  {
    Context localContext = this.mContext;
    Uri localUri = MusicContent.AutoPlaylists.Members.getAutoPlaylistItemsUri(65532L, null);
    return MusicContent.existsContent(localContext, localUri);
  }

  public MatrixCursor buildCursor(String[] paramArrayOfString)
  {
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this.mContext, this);
    while (true)
    {
      try
      {
        boolean bool1 = localMusicPreferences.getStoreAvailable();
        this.mHasStore = bool1;
        MusicPreferences.releaseMusicPreferences(this);
        Set localSet1 = this.mAutoPlaylistIds;
        Long localLong1 = Long.valueOf(65535L);
        if ((localSet1.contains(localLong1)) && (showLastAddedSongs()))
        {
          i = 1;
          Set localSet2 = this.mAutoPlaylistIds;
          Long localLong2 = Long.valueOf(65533L);
          if ((!localSet2.contains(localLong2)) || (!showStoreSongs()))
            break label289;
          j = 1;
          Set localSet3 = this.mAutoPlaylistIds;
          Long localLong3 = Long.valueOf(65532L);
          if ((!localSet3.contains(localLong3)) || (!showThumbsSongs()))
            break label295;
          k = 1;
          Set localSet4 = this.mAutoPlaylistIds;
          Long localLong4 = Long.valueOf(65534L);
          boolean bool2 = localSet4.contains(localLong4);
          MatrixCursor localMatrixCursor = new MatrixCursor(paramArrayOfString);
          Store localStore = Store.getInstance(this.mContext);
          if (k != 0)
          {
            ArrayList localArrayList1 = createRow(localStore, paramArrayOfString, 65532L);
            localMatrixCursor.addRow(localArrayList1);
          }
          if (i != 0)
          {
            ArrayList localArrayList2 = createRow(localStore, paramArrayOfString, 65535L);
            localMatrixCursor.addRow(localArrayList2);
          }
          if (j != 0)
          {
            ArrayList localArrayList3 = createRow(localStore, paramArrayOfString, 65533L);
            localMatrixCursor.addRow(localArrayList3);
          }
          if (bool2)
          {
            ArrayList localArrayList4 = createRow(localStore, paramArrayOfString, 65534L);
            localMatrixCursor.addRow(localArrayList4);
          }
          return localMatrixCursor;
        }
      }
      finally
      {
        MusicPreferences.releaseMusicPreferences(this);
      }
      int i = 0;
      continue;
      label289: int j = 0;
      continue;
      label295: int k = 0;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.AutoPlaylistCursorFactory
 * JD-Core Version:    0.6.2
 */