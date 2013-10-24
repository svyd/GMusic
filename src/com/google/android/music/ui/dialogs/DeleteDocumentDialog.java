package com.google.android.music.ui.dialogs;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.common.base.Preconditions;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.store.MusicContent.RadioStations;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.utils.MusicUtils;

public class DeleteDocumentDialog extends TwoButtonsDialog
{
  public Bundle createArgs(Context paramContext, Document paramDocument)
  {
    Document.Type localType1 = paramDocument.getType();
    Document.Type localType2 = Document.Type.PLAYLIST;
    if (localType1 != localType2)
    {
      Document.Type localType3 = paramDocument.getType();
      Document.Type localType4 = Document.Type.RADIO;
      if (localType3 != localType4)
        break label168;
    }
    label168: for (boolean bool = true; ; bool = false)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("invalid doc type: ");
      Document.Type localType5 = paramDocument.getType();
      String str1 = localType5;
      Preconditions.checkArgument(bool, str1);
      String str2 = paramDocument.getTitle();
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = str2;
      String str3 = paramContext.getString(2131231171, arrayOfObject);
      String str4 = paramContext.getString(17039370);
      String str5 = paramContext.getString(17039360);
      Bundle localBundle = super.createArgs(str3, str4, str5);
      int i = paramDocument.getType().ordinal();
      localBundle.putInt("deleteType", i);
      long l = paramDocument.getId();
      localBundle.putLong("deleteId", l);
      return localBundle;
    }
  }

  protected void onOkClicked()
  {
    Bundle localBundle = getArguments();
    final int i = localBundle.getInt("deleteType");
    final long l = localBundle.getLong("deleteId");
    ContentResolver localContentResolver = getActivity().getContentResolver();
    DeleteDocumentDialog localDeleteDocumentDialog = this;
    MusicUtils.runAsync(new Runnable()
    {
      public void run()
      {
        Document.Type localType1 = Document.Type.fromOrdinal(i);
        Document.Type localType2 = Document.Type.PLAYLIST;
        if (localType1 == localType2)
        {
          Uri localUri1 = MusicContent.Playlists.getPlaylistUri(l);
          int i = this.val$resolver.delete(localUri1, null, null);
          return;
        }
        Document.Type localType3 = Document.Type.RADIO;
        if (localType1 != localType3)
          return;
        Uri localUri2 = MusicContent.RadioStations.getRadioStationUri(l);
        int j = this.val$resolver.delete(localUri2, null, null);
      }
    });
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.dialogs.DeleteDocumentDialog
 * JD-Core Version:    0.6.2
 */