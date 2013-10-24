package com.google.android.music.cloudclient;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public class MusicGenreJson extends GenericJson
{

  @Key("children")
  public List<String> mChildren;

  @Key("id")
  public String mId;

  @Key("images")
  public List<ImageRefJson> mImages;

  @Key("name")
  public String mName;

  @Key("parentId")
  public String mParentId;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.MusicGenreJson
 * JD-Core Version:    0.6.2
 */