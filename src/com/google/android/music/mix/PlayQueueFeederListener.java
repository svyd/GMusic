package com.google.android.music.mix;

import com.google.android.music.medialist.SongList;

public abstract interface PlayQueueFeederListener
{
  public abstract void onFailure(MixDescriptor paramMixDescriptor, PlayQueueFeeder.PostProcessingAction paramPostProcessingAction);

  public abstract void onSuccess(MixDescriptor paramMixDescriptor1, MixDescriptor paramMixDescriptor2, SongList paramSongList, PlayQueueFeeder.PostProcessingAction paramPostProcessingAction);
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.mix.PlayQueueFeederListener
 * JD-Core Version:    0.6.2
 */