package com.google.android.music.sync.google.model;

import com.google.android.music.cloudclient.JsonUtils;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RadioEditStationsRequest extends GenericJson
{

  @Key("mutations")
  public List<MutateRadioStationRequest> mMutableRadioStationRequests;

  public RadioEditStationsRequest()
  {
    ArrayList localArrayList = new ArrayList();
    this.mMutableRadioStationRequests = localArrayList;
  }

  public static byte[] serialize(SyncableRadioStation paramSyncableRadioStation, boolean paramBoolean, int paramInt1, int paramInt2)
    throws IOException
  {
    RadioEditStationsRequest localRadioEditStationsRequest = new RadioEditStationsRequest();
    MutateRadioStationRequest localMutateRadioStationRequest = new MutateRadioStationRequest();
    localMutateRadioStationRequest.mCreateRadioStation = paramSyncableRadioStation;
    localMutateRadioStationRequest.mIncludeFeed = paramBoolean;
    localMutateRadioStationRequest.mNumEntries = paramInt1;
    UserFeedParams localUserFeedParams = new UserFeedParams();
    localMutateRadioStationRequest.mUserFeedParams = localUserFeedParams;
    localMutateRadioStationRequest.mUserFeedParams.mContentFilter = paramInt2;
    boolean bool = localRadioEditStationsRequest.mMutableRadioStationRequests.add(localMutateRadioStationRequest);
    return JsonUtils.toJsonByteArray(localRadioEditStationsRequest);
  }

  public void addRequest(MutateRadioStationRequest paramMutateRadioStationRequest)
  {
    boolean bool = this.mMutableRadioStationRequests.add(paramMutateRadioStationRequest);
  }

  public static class UserFeedParams extends GenericJson
  {

    @Key("contentFilter")
    public int mContentFilter;

    @Key("languageCode")
    public String mLanguageCode;
  }

  public static class MutateRadioStationRequest extends GenericJson
  {

    @Key("create")
    public SyncableRadioStation mCreateRadioStation;

    @Key("delete")
    public String mDeleteId;

    @Key("includeFeed")
    public boolean mIncludeFeed;

    @Key("numEntries")
    public int mNumEntries;

    @Key("update")
    public SyncableRadioStation mUpdateRadioStation;

    @Key("params")
    public RadioEditStationsRequest.UserFeedParams mUserFeedParams;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.RadioEditStationsRequest
 * JD-Core Version:    0.6.2
 */