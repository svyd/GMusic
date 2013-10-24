package com.google.android.music.cloudclient;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public class ExploreEntityGroupJson extends GenericJson
{

  @Key("description")
  public String mDescription;

  @Key("entities")
  public List<ExploreEntityJson> mEntities;
  public long mId;

  @Key("title")
  public String mTitle;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.ExploreEntityGroupJson
 * JD-Core Version:    0.6.2
 */