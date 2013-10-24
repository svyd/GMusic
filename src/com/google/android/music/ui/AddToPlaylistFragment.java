package com.google.android.music.ui;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.google.android.music.medialist.SongList;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;

public class AddToPlaylistFragment extends PlaylistDialogFragment
  implements LoaderManager.LoaderCallbacks<Cursor>
{
  final AdapterView.OnItemClickListener mItemClickListener;
  private SongList mSongsToAdd;

  public AddToPlaylistFragment()
  {
    super(2131230899, true);
    AdapterView.OnItemClickListener local1 = new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        AddToPlaylistFragment.this.dismiss();
        final PlaylistDialogFragment.PlayListInfo localPlayListInfo = (PlaylistDialogFragment.PlayListInfo)AddToPlaylistFragment.this.getAdapterItem(paramAnonymousInt);
        if (localPlayListInfo.id == 65535L)
        {
          FragmentActivity localFragmentActivity = AddToPlaylistFragment.this.getActivity();
          CreatePlaylistFragment localCreatePlaylistFragment = new CreatePlaylistFragment();
          Bundle localBundle = CreatePlaylistFragment.createArgs(AddToPlaylistFragment.this.mSongsToAdd);
          FragmentUtils.addFragment(localFragmentActivity, localCreatePlaylistFragment, localBundle);
          return;
        }
        final Context localContext = paramAnonymousView.getContext();
        MusicUtils.runAsyncWithCallback(new AsyncRunner()
        {
          private int mSongsAddedCount = 0;

          public void backgroundTask()
          {
            SongList localSongList = AddToPlaylistFragment.this.mSongsToAdd;
            Context localContext = localContext;
            long l = localPlayListInfo.id;
            int i = localSongList.appendToPlaylist(localContext, l);
            this.mSongsAddedCount = i;
          }

          public void taskCompleted()
          {
            Context localContext = localContext;
            int i = this.mSongsAddedCount;
            String str = localPlayListInfo.name;
            MusicUtils.showSongsAddedToPlaylistToast(localContext, i, str);
          }
        });
      }
    };
    this.mItemClickListener = local1;
    AdapterView.OnItemClickListener localOnItemClickListener = this.mItemClickListener;
    setOnItemClickListener(localOnItemClickListener);
  }

  public Dialog onCreateDialog(Bundle paramBundle)
  {
    Bundle localBundle = getArguments();
    if (localBundle != null)
    {
      SongList localSongList = (SongList)localBundle.getParcelable("songList");
      this.mSongsToAdd = localSongList;
    }
    while (true)
    {
      return super.onCreateDialog(paramBundle);
      int i = Log.e("AddToPlaylistFragment", "Created without arguments, exiting");
      dismiss();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.AddToPlaylistFragment
 * JD-Core Version:    0.6.2
 */