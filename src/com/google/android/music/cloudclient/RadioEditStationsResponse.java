package com.google.android.music.cloudclient;

import com.google.android.music.sync.google.model.SyncableRadioStation;
import com.google.api.client.util.Key;
import java.util.ArrayList;
import java.util.List;

public class RadioEditStationsResponse
{

  @Key("mutate_response")
  public List<MutateResponse> mMutateResponses;

  public RadioEditStationsResponse()
  {
    ArrayList localArrayList = new ArrayList();
    this.mMutateResponses = localArrayList;
  }

  public static boolean validateResponse(RadioEditStationsResponse paramRadioEditStationsResponse)
  {
    boolean bool = false;
    if (paramRadioEditStationsResponse.mMutateResponses.size() != 0)
    {
      MutateResponse localMutateResponse = (MutateResponse)paramRadioEditStationsResponse.mMutateResponses.get(0);
      if ((localMutateResponse.mRadioStation != null) && (localMutateResponse.mRadioStation.mTracks != null))
      {
        String str1 = RadioEditStationsResponse.MutateResponse.ResponseCode.OK.name();
        String str2 = localMutateResponse.mResponseCode;
        if (str1.equals(str2))
          bool = true;
      }
    }
    return bool;
  }

  public static class MutateResponse
  {

    @Key("id")
    public String mId;

    @Key("station")
    public SyncableRadioStation mRadioStation;

    @Key("response_code")
    public String mResponseCode;

    public static enum ResponseCode
    {
      static
      {
        CONFLICT = new ResponseCode("CONFLICT", 1);
        INVALID_REQUEST = new ResponseCode("INVALID_REQUEST", 2);
        TOO_MANY_ITEMS = new ResponseCode("TOO_MANY_ITEMS", 3);
        ResponseCode[] arrayOfResponseCode = new ResponseCode[4];
        ResponseCode localResponseCode1 = OK;
        arrayOfResponseCode[0] = localResponseCode1;
        ResponseCode localResponseCode2 = CONFLICT;
        arrayOfResponseCode[1] = localResponseCode2;
        ResponseCode localResponseCode3 = INVALID_REQUEST;
        arrayOfResponseCode[2] = localResponseCode3;
        ResponseCode localResponseCode4 = TOO_MANY_ITEMS;
        arrayOfResponseCode[3] = localResponseCode4;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.RadioEditStationsResponse
 * JD-Core Version:    0.6.2
 */