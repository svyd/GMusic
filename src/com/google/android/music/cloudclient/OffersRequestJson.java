package com.google.android.music.cloudclient;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.io.IOException;

public class OffersRequestJson extends GenericJson
{

  @Key("couponCode")
  public String mCouponCode;

  public static byte[] serialize(String paramString)
    throws IOException
  {
    OffersRequestJson localOffersRequestJson = new OffersRequestJson();
    if (paramString == null)
      paramString = "";
    localOffersRequestJson.mCouponCode = paramString;
    return JsonUtils.toJsonByteArray(localOffersRequestJson);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.OffersRequestJson
 * JD-Core Version:    0.6.2
 */