package com.google.android.music.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.google.android.music.medialist.AlbumSongList;
import com.google.android.music.medialist.AutoPlaylist;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.NautilusAlbumSongList;
import com.google.android.music.medialist.PlaylistSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent.AutoPlaylists;
import com.google.android.music.ui.cardlib.model.Document;
import java.util.UUID;

public class TrackContainerActivity extends BaseActivity
{
  public static final Intent buildStartIntent(Context paramContext, SongList paramSongList, Document paramDocument)
  {
    Intent localIntent1 = new Intent(paramContext, TrackContainerActivity.class);
    String str = UUID.randomUUID().toString();
    Uri localUri = Uri.fromParts("data", str, null);
    Intent localIntent2 = localIntent1.setData(localUri);
    Intent localIntent3 = localIntent1.putExtra("medialist", paramSongList);
    if (paramDocument != null)
      Intent localIntent4 = localIntent1.putExtra("document", paramDocument);
    return localIntent1;
  }

  private TrackContainerFragment makeTrackContainerFragment(Intent paramIntent)
  {
    String str1 = paramIntent.getAction();
    String str2 = paramIntent.getType();
    long l;
    Object localObject;
    label84: TrackContainerFragment localTrackContainerFragment;
    if ("android.intent.action.VIEW".equalsIgnoreCase(str1))
      if (("vnd.android.cursor.dir/vnd.google.music.playlist".equals(str2)) || ("vnd.android.cursor.dir/playlist".equals(str2)))
      {
        String str3 = paramIntent.getExtras().getString("playlist");
        if (str3 != null)
        {
          l = Long.parseLong(str3);
          if (!MusicContent.AutoPlaylists.isAutoPlaylistId(l))
            break label109;
          MusicPreferences localMusicPreferences = getPreferences();
          localObject = AutoPlaylist.getAutoPlaylist(l, true, localMusicPreferences);
          localTrackContainerFragment = TrackContainerFragment.newInstance((MediaList)localObject, null);
        }
      }
    while (true)
    {
      return localTrackContainerFragment;
      l = paramIntent.getLongExtra("playlistId", 65535L);
      break;
      label109: localObject = new PlaylistSongList(l, null, 0, null, null, null, null, null, true);
      break label84;
      int i = Log.wtf("TrackContainerActivity", "ACTION_VIEW with unregistered type requested. Finishing early.");
      localTrackContainerFragment = null;
      continue;
      MediaList localMediaList = (MediaList)paramIntent.getParcelableExtra("medialist");
      Document localDocument = (Document)paramIntent.getParcelableExtra("document");
      localTrackContainerFragment = TrackContainerFragment.newInstance(localMediaList, localDocument);
    }
  }

  public static final void showAlbum(Context paramContext, long paramLong, Document paramDocument, boolean paramBoolean)
  {
    AlbumSongList localAlbumSongList = new AlbumSongList(paramLong, true);
    Intent localIntent = buildStartIntent(paramContext, localAlbumSongList, paramDocument);
    paramContext.startActivity(localIntent);
  }

  public static final void showNautilusAlbum(Context paramContext, String paramString, Document paramDocument)
  {
    NautilusAlbumSongList localNautilusAlbumSongList = new NautilusAlbumSongList(paramString);
    Intent localIntent = buildStartIntent(paramContext, localNautilusAlbumSongList, paramDocument);
    paramContext.startActivity(localIntent);
  }

  public static final void showPlaylist(Context paramContext, SongList paramSongList, Document paramDocument)
  {
    if (paramSongList == null)
      return;
    Intent localIntent = buildStartIntent(paramContext, paramSongList, paramDocument);
    paramContext.startActivity(localIntent);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getContent() != null)
      return;
    Intent localIntent = getIntent();
    TrackContainerFragment localTrackContainerFragment = makeTrackContainerFragment(localIntent);
    if (localTrackContainerFragment != null)
    {
      replaceContent(localTrackContainerFragment, false);
      return;
    }
    finish();
  }

  public void onNewIntent(Intent paramIntent)
  {
    TrackContainerFragment localTrackContainerFragment = makeTrackContainerFragment(paramIntent);
    if (localTrackContainerFragment != null)
    {
      replaceContent(localTrackContainerFragment, false);
      return;
    }
    finish();
  }

  protected boolean usesActionBarOverlay()
  {
    return true;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.TrackContainerActivity
 * JD-Core Version:    0.6.2
 */