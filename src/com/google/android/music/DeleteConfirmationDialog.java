package com.google.android.music;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;
import com.google.android.gsf.Gservices;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.store.MusicContent.XAudio;
import com.google.android.music.utils.MusicUtils;

public class DeleteConfirmationDialog extends AlertDialog
  implements DialogInterface.OnClickListener
{
  private String mArtistName;
  private Context mContext;
  private boolean mHasRemote;
  private String mItemTitle;
  private long mPrimaryId;
  private long mSecondaryId;
  private DeletionType mType;

  public DeleteConfirmationDialog(Context paramContext, DeletionType paramDeletionType, long paramLong, CharSequence paramCharSequence1, CharSequence paramCharSequence2, boolean paramBoolean)
  {
    super(paramContext);
    DeleteConfirmationDialog localDeleteConfirmationDialog = this;
    DeletionType localDeletionType = paramDeletionType;
    long l = paramLong;
    CharSequence localCharSequence1 = paramCharSequence1;
    CharSequence localCharSequence2 = paramCharSequence2;
    boolean bool = paramBoolean;
    localDeleteConfirmationDialog.init(localDeletionType, l, 65535L, localCharSequence1, localCharSequence2, bool);
  }

  private void init(DeletionType paramDeletionType, long paramLong1, long paramLong2, CharSequence paramCharSequence1, CharSequence paramCharSequence2, boolean paramBoolean)
  {
    Context localContext1 = getContext();
    this.mContext = localContext1;
    this.mPrimaryId = paramLong1;
    if (paramLong2 != 65535L)
    {
      this.mSecondaryId = paramLong2;
      this.mType = paramDeletionType;
      boolean bool1 = paramBoolean;
      this.mHasRemote = bool1;
      String str1 = paramCharSequence1.toString();
      this.mItemTitle = str1;
      String str2 = paramCharSequence2.toString();
      this.mArtistName = str2;
      boolean bool2 = Gservices.getBoolean(this.mContext.getContentResolver(), "music_enable_tracks_upsync_deletion", false);
      if ((!this.mHasRemote) || (bool2))
        break label236;
      Context localContext2 = this.mContext;
      Object[] arrayOfObject1 = new Object[1];
      String str3 = this.mItemTitle;
      arrayOfObject1[0] = str3;
      String str4 = localContext2.getString(2131231172, arrayOfObject1);
      setMessage(str4);
    }
    while (true)
    {
      Resources localResources = getContext().getResources();
      String str5 = localResources.getString(2131230741);
      setButton(-1, str5, this);
      String str6 = localResources.getString(2131230742);
      DialogInterface.OnClickListener localOnClickListener = (DialogInterface.OnClickListener)null;
      setButton(-1, str6, localOnClickListener);
      return;
      DeletionType localDeletionType = DeletionType.ALBUM_BY_ARTIST;
      if (paramDeletionType != localDeletionType)
        break;
      String str7 = "secondary Id required for type: " + paramDeletionType;
      throw new IllegalArgumentException(str7);
      label236: if (this.mHasRemote)
      {
        setTitle(2131231168);
        Object[] arrayOfObject2 = new Object[3];
        String str8 = this.mItemTitle;
        arrayOfObject2[0] = str8;
        String str9 = this.mArtistName;
        arrayOfObject2[1] = str9;
        String str10 = this.mContext.getString(2131231169);
        arrayOfObject2[2] = str10;
        Spanned localSpanned = Html.fromHtml(String.format("<b>%s<br/>%s</b><br/><br/>%s", arrayOfObject2));
        setMessage(localSpanned);
      }
      else
      {
        Context localContext3 = this.mContext;
        Object[] arrayOfObject3 = new Object[1];
        String str11 = this.mItemTitle;
        arrayOfObject3[0] = str11;
        String str12 = localContext3.getString(2131231170, arrayOfObject3);
        setMessage(str12);
      }
    }
  }

  private void performDelete()
  {
    DeletionType localDeletionType1 = this.mType;
    DeletionType localDeletionType2 = DeletionType.SONG;
    if (localDeletionType1 != localDeletionType2)
    {
      DeletionType localDeletionType3 = this.mType;
      DeletionType localDeletionType4 = DeletionType.PLAYLIST;
      if (localDeletionType3 != localDeletionType4)
        Toast.makeText(this.mContext, "TODO: Peform Delete", 1).show();
    }
    MusicUtils.runAsync(new Runnable()
    {
      public void run()
      {
        int[] arrayOfInt = DeleteConfirmationDialog.2.$SwitchMap$com$google$android$music$DeleteConfirmationDialog$DeletionType;
        int i = DeleteConfirmationDialog.this.mType.ordinal();
        switch (arrayOfInt[i])
        {
        default:
          return;
        case 1:
          Uri localUri1 = MusicContent.XAudio.getAudioUri(DeleteConfirmationDialog.this.mPrimaryId);
          int j = DeleteConfirmationDialog.this.mContext.getContentResolver().delete(localUri1, null, null);
          return;
        case 2:
        }
        Uri localUri2 = MusicContent.Playlists.getPlaylistUri(DeleteConfirmationDialog.this.mPrimaryId);
        int k = DeleteConfirmationDialog.this.mContext.getContentResolver().delete(localUri2, null, null);
      }
    });
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    performDelete();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null)
      return;
    long l1 = paramBundle.getLong("primaryId");
    long l2 = paramBundle.getLong("secondartId");
    DeletionType[] arrayOfDeletionType = DeletionType.values();
    int i = paramBundle.getInt("type");
    DeletionType localDeletionType = arrayOfDeletionType[i];
    boolean bool = paramBundle.getBoolean("hasRemote");
    String str1 = paramBundle.getString("itemTitle");
    String str2 = paramBundle.getString("artistName");
    init(localDeletionType, l1, l2, str1, str2, bool);
  }

  public Bundle onSaveInstanceState()
  {
    Bundle localBundle = super.onSaveInstanceState();
    long l1 = this.mPrimaryId;
    localBundle.putLong("primaryId", l1);
    long l2 = this.mSecondaryId;
    localBundle.putLong("secondartId", l2);
    int i = this.mType.ordinal();
    localBundle.putInt("type", i);
    boolean bool = this.mHasRemote;
    localBundle.putBoolean("hasRemote", bool);
    String str1 = this.mArtistName;
    localBundle.putString("artistName", str1);
    String str2 = this.mItemTitle;
    localBundle.putString("itemTitle", str2);
    return localBundle;
  }

  public static enum DeletionType
  {
    static
    {
      ALBUM = new DeletionType("ALBUM", 1);
      ALBUM_BY_ARTIST = new DeletionType("ALBUM_BY_ARTIST", 2);
      SONG = new DeletionType("SONG", 3);
      PLAYLIST = new DeletionType("PLAYLIST", 4);
      DeletionType[] arrayOfDeletionType = new DeletionType[5];
      DeletionType localDeletionType1 = ARTIST;
      arrayOfDeletionType[0] = localDeletionType1;
      DeletionType localDeletionType2 = ALBUM;
      arrayOfDeletionType[1] = localDeletionType2;
      DeletionType localDeletionType3 = ALBUM_BY_ARTIST;
      arrayOfDeletionType[2] = localDeletionType3;
      DeletionType localDeletionType4 = SONG;
      arrayOfDeletionType[3] = localDeletionType4;
      DeletionType localDeletionType5 = PLAYLIST;
      arrayOfDeletionType[4] = localDeletionType5;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.DeleteConfirmationDialog
 * JD-Core Version:    0.6.2
 */