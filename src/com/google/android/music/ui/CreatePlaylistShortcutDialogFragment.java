package com.google.android.music.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class CreatePlaylistShortcutDialogFragment extends PlaylistDialogFragment
{
  private AdapterView.OnItemClickListener mItemClickListener;

  public CreatePlaylistShortcutDialogFragment()
  {
    super(2131230924, false);
    AdapterView.OnItemClickListener local1 = new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        PlaylistDialogFragment.PlayListInfo localPlayListInfo = (PlaylistDialogFragment.PlayListInfo)CreatePlaylistShortcutDialogFragment.this.getAdapterItem(paramAnonymousInt);
        Intent localIntent1 = new Intent();
        Intent localIntent2 = localIntent1.setAction("android.intent.action.VIEW");
        Uri localUri = Uri.EMPTY;
        Intent localIntent3 = localIntent1.setDataAndType(localUri, "vnd.android.cursor.dir/vnd.google.music.playlist");
        Intent localIntent4 = localIntent1.setFlags(67108864);
        String str1 = String.valueOf(localPlayListInfo.id);
        Intent localIntent5 = localIntent1.putExtra("playlist", str1);
        Intent localIntent6 = new Intent();
        Intent localIntent7 = localIntent6.putExtra("android.intent.extra.shortcut.INTENT", localIntent1);
        String str2 = localPlayListInfo.name;
        Intent localIntent8 = localIntent6.putExtra("android.intent.extra.shortcut.NAME", str2);
        Intent.ShortcutIconResource localShortcutIconResource = Intent.ShortcutIconResource.fromContext(CreatePlaylistShortcutDialogFragment.this.getActivity(), 2130903041);
        Intent localIntent9 = localIntent6.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", localShortcutIconResource);
        CreatePlaylistShortcutDialogFragment.this.getActivity().setResult(-1, localIntent6);
        CreatePlaylistShortcutDialogFragment.this.dismiss();
      }
    };
    this.mItemClickListener = local1;
    AdapterView.OnItemClickListener localOnItemClickListener = this.mItemClickListener;
    setOnItemClickListener(localOnItemClickListener);
    setShowNewPlaylist(false);
  }

  public void onDismiss(DialogInterface paramDialogInterface)
  {
    super.onDismiss(paramDialogInterface);
    FragmentActivity localFragmentActivity = getActivity();
    if (localFragmentActivity == null)
      return;
    localFragmentActivity.finish();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.CreatePlaylistShortcutDialogFragment
 * JD-Core Version:    0.6.2
 */