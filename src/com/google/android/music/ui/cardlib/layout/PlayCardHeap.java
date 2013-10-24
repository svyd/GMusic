package com.google.android.music.ui.cardlib.layout;

import android.view.LayoutInflater;
import com.google.android.music.ui.cardlib.utils.Maps;
import com.google.android.music.ui.cardlib.utils.Utils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayCardHeap
{
  private Map<PlayCardClusterMetadata.CardMetadata, List<PlayCardView>> mHeap;

  public PlayCardHeap()
  {
    HashMap localHashMap = Maps.newHashMap();
    this.mHeap = localHashMap;
  }

  public PlayCardView getCard(PlayCardClusterMetadata.CardMetadata paramCardMetadata, LayoutInflater paramLayoutInflater)
  {
    Utils.ensureOnMainThread();
    List localList = (List)this.mHeap.get(paramCardMetadata);
    PlayCardView localPlayCardView;
    if ((localList == null) || (localList.isEmpty()))
    {
      int i = paramCardMetadata.getLayoutId();
      localPlayCardView = (PlayCardView)paramLayoutInflater.inflate(i, null, false);
    }
    while (true)
    {
      return localPlayCardView;
      localPlayCardView = (PlayCardView)localList.remove(0);
      if (localList.isEmpty())
        Object localObject = this.mHeap.remove(paramCardMetadata);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.layout.PlayCardHeap
 * JD-Core Version:    0.6.2
 */