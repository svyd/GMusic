package com.google.android.music.sync.google.model;

import com.google.android.music.store.InvalidDataException;
import com.google.android.music.sync.api.BadRequestException;
import com.google.android.music.sync.api.ConflictException;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class BatchMutateResponse extends GenericJson
{

  @Key("mutate_response")
  public List<MutateResponse> mMutateResponses;

  public BatchMutateResponse()
  {
    ArrayList localArrayList = new ArrayList();
    this.mMutateResponses = localArrayList;
  }

  public static BatchMutateResponse parseFromJsonInputStream(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      BatchMutateResponse localBatchMutateResponse = (BatchMutateResponse)Json.parse(localJsonParser, BatchMutateResponse.class, null);
      return localBatchMutateResponse;
    }
    catch (JsonParseException localJsonParseException)
    {
      String str = localJsonParseException.getMessage();
      throw new IOException(str);
    }
  }

  public static class MutateResponse
  {

    @Key("id")
    public String mId;

    @Key("response_code")
    public String mResponseCode;

    public void throwExceptionFromResponseCode()
      throws ConflictException, BadRequestException
    {
      if (this.mResponseCode == null)
        throw new InvalidDataException("Response code is null");
      String str1 = ResponseCode.CONFLICT.name();
      String str2 = this.mResponseCode;
      if (str1.equals(str2))
        throw new ConflictException();
      String str3 = ResponseCode.INVALID_REQUEST.name();
      String str4 = this.mResponseCode;
      if (str3.equals(str4))
        throw new BadRequestException("INVALID_REQUEST");
      String str5 = ResponseCode.TOO_MANY_ITEMS.name();
      String str6 = this.mResponseCode;
      if (!str5.equals(str6))
        return;
      throw new BadRequestException("TOO_MANY_ITEMS");
    }

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
 * Qualified Name:     com.google.android.music.sync.google.model.BatchMutateResponse
 * JD-Core Version:    0.6.2
 */