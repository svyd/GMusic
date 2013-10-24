package com.google.android.music.cloudclient;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public class TabJson extends GenericJson
{

  @Key("data_status")
  public String mDataStatus;

  @Key("groups")
  public List<ExploreEntityGroupJson> mGroups;

  @Key("tab_type")
  public String mTabType;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.TabJson
 * JD-Core Version:    0.6.2
 */