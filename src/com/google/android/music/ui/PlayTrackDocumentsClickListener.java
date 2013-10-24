package com.google.android.music.ui;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.music.medialist.NautilusSelectedSongList;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.utils.MusicUtils;
import java.util.Iterator;
import java.util.List;

public class PlayTrackDocumentsClickListener
  implements View.OnClickListener
{
  private List<Document> mTrackList;

  public PlayTrackDocumentsClickListener(List<Document> paramList)
  {
    this.mTrackList = paramList;
  }

  public void onClick(View paramView)
  {
    if ((paramView.getTag() instanceof Document))
    {
      Document localDocument = (Document)paramView.getTag();
      int i = 0;
      int j = 0;
      Iterator localIterator = this.mTrackList.iterator();
      String[] arrayOfString;
      while (true)
      {
        if (localIterator.hasNext())
        {
          if ((Document)localIterator.next() == localDocument)
            j = 1;
        }
        else
        {
          if (j == 0)
            break label151;
          arrayOfString = new String[this.mTrackList.size()];
          int k = 0;
          while (true)
          {
            int m = arrayOfString.length;
            if (k >= m)
              break;
            String str1 = ((Document)this.mTrackList.get(k)).getTrackMetajamId();
            arrayOfString[k] = str1;
            k += 1;
          }
        }
        i += 1;
      }
      MusicUtils.playMediaList(new NautilusSelectedSongList(arrayOfString), i);
      return;
      label151: String str2 = "Couldn't find the selected Document in the list: " + localDocument;
      int n = Log.w("PlayTrackDocumentsClickListener", str2);
      return;
    }
    int i1 = Log.w("PlayTrackDocumentsClickListener", "The clicked view's tag wasn't a Document");
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.PlayTrackDocumentsClickListener
 * JD-Core Version:    0.6.2
 */