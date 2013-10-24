package com.google.android.music.cloudclient;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public class MusicGenresResponseJson extends GenericJson
{

  @Key("genres")
  public List<MusicGenreJson> mGenres;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.MusicGenresResponseJson
 * JD-Core Version:    0.6.2
 */