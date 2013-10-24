package com.google.android.music.cloudclient;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class OfferJson extends GenericJson
{

  @Key("description")
  public String mDescription;

  @Key("has_trial")
  public boolean mHasFreeTrialPeriod;

  @Key("offer_id")
  public String mStoreDocId;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.OfferJson
 * JD-Core Version:    0.6.2
 */