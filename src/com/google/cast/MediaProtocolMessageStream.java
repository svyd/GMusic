package com.google.cast;

import android.net.Uri;
import android.os.SystemClock;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MediaProtocolMessageStream extends MessageStream
{
  private static final Logger a = new Logger("MediaProtocolMessageStream");
  private PlayerState b;
  private String c;
  private JSONObject d;
  private double e;
  private double f;
  private double g;
  private boolean h;
  private long i;
  private boolean j;
  private String k;
  private Map<Long, MediaTrack> l;
  private Uri m;
  private long n;
  private long o;
  private Map<Long, MediaProtocolCommand> p;
  private long q;

  public MediaProtocolMessageStream()
  {
    super("ramp");
    HashMap localHashMap1 = new HashMap();
    this.l = localHashMap1;
    this.o = 65535L;
    HashMap localHashMap2 = new HashMap();
    this.p = localHashMap2;
    this.q = 65535L;
  }

  private double a(double paramDouble)
  {
    if (paramDouble < 0.0D)
      paramDouble = 0.0D;
    while (true)
    {
      return paramDouble;
      if (paramDouble > 1.0D)
        paramDouble = 1.0D;
    }
  }

  private void a(JSONObject paramJSONObject)
    throws JSONException
  {
    int i1 = 0L;
    long l1 = 0;
    if (paramJSONObject.has("event_sequence"))
    {
      long l2 = paramJSONObject.getLong("event_sequence");
      long l3 = this.o;
      if (l2 < l3)
        return;
      this.o = l2;
    }
    int i3;
    if (paramJSONObject.has("state"))
    {
      PlayerState localPlayerState1 = PlayerState.IDLE;
      i3 = paramJSONObject.getInt("state");
    }
    PlayerState localPlayerState2;
    Object localObject1;
    switch (i3)
    {
    default:
      String str1 = "invalid state value: " + i3;
      throw new JSONException(str1);
    case 0:
      localPlayerState2 = PlayerState.IDLE;
      this.b = localPlayerState2;
      String str2 = paramJSONObject.optString("content_id", null);
      if (((this.c == null) || (!this.c.equals(str2))) && ((this.c != null) || (str2 != null)))
      {
        localObject1 = null;
        label182: this.c = str2;
        JSONObject localJSONObject = paramJSONObject.optJSONObject("content_info");
        this.d = localJSONObject;
        double d1 = paramJSONObject.optDouble("current_time", 0.0D);
        this.e = d1;
        long l4 = SystemClock.elapsedRealtime();
        this.i = l4;
        double d2 = paramJSONObject.optDouble("duration", 0.0D);
        this.f = d2;
        boolean bool1 = paramJSONObject.optBoolean("time_progress", false);
        this.j = bool1;
        String str3 = paramJSONObject.optString("title", null);
        this.k = str3;
        if (this.q == 65535L)
        {
          double d3 = paramJSONObject.optDouble("volume", 0.0D);
          double d4 = a(d3);
          this.g = d4;
        }
        boolean bool2 = paramJSONObject.optBoolean("muted");
        this.h = bool2;
        if (localObject1 == null)
          break label784;
      }
      break;
    case 1:
    case 2:
    }
    while (true)
    {
      Uri localUri;
      synchronized (this.l)
      {
        this.l.clear();
        localObject3 = null;
        if (paramJSONObject.has("image_url"))
        {
          localUri = Uri.parse(paramJSONObject.optString("image_url"));
          label376: this.m = localUri;
          if (!paramJSONObject.has("tracks"))
            break label777;
        }
      }
      Object localObject4;
      Object localObject6;
      while (true)
      {
        Object localObject5;
        boolean bool3;
        String str4;
        synchronized (this.l)
        {
          JSONArray localJSONArray = paramJSONObject.getJSONArray("tracks");
          int i2 = 0;
          localObject4 = localObject3;
          int i4 = localJSONArray.length();
          if (i2 >= i4)
            break label747;
          localObject5 = localJSONArray.getJSONObject(i2);
          l1 = ((JSONObject)localObject5).getLong("id");
          bool3 = ((JSONObject)localObject5).optBoolean("selected", false);
          Map localMap3 = this.l;
          Long localLong1 = Long.valueOf(l1);
          localObject6 = (MediaTrack)localMap3.get(localLong1);
          if (localObject6 != null)
          {
            if (((MediaTrack)localObject6).isEnabled() == bool3)
              break label770;
            ((MediaTrack)localObject6).a(bool3);
            localObject6 = null;
            i2 += 1;
            localObject4 = localObject6;
            continue;
            localPlayerState2 = PlayerState.STOPPED;
            break;
            localPlayerState2 = PlayerState.PLAYING;
            break;
            localObject1 = null;
            break label182;
            localObject2 = finally;
            throw localObject2;
            localUri = null;
            break label376;
          }
          localObject6 = ((JSONObject)localObject5).getString("type");
          str4 = ((JSONObject)localObject5).optString("name", null);
          localObject5 = ((JSONObject)localObject5).optString("lang", null);
          if (((String)localObject6).equalsIgnoreCase("subtitles"))
          {
            localObject4 = MediaTrack.Type.SUBTITLES;
            label595: if (localObject4 != null)
              break label698;
            String str5 = "invalid track type: " + (String)localObject6;
            throw new JSONException(str5);
          }
        }
        if (((String)localObject6).equalsIgnoreCase("captions"))
        {
          localObject4 = MediaTrack.Type.CAPTIONS;
        }
        else if (((String)localObject6).equalsIgnoreCase("audio"))
        {
          localObject4 = MediaTrack.Type.AUDIO;
        }
        else
        {
          if (!((String)localObject6).equalsIgnoreCase("video"))
            break label764;
          localObject4 = MediaTrack.Type.VIDEO;
          continue;
          label698: Map localMap4 = this.l;
          Long localLong2 = Long.valueOf(l1);
          MediaTrack localMediaTrack = new MediaTrack(l1, (MediaTrack.Type)localObject4, str4, (String)localObject5, bool3);
          Object localObject8 = localMap4.put(localLong2, localMediaTrack);
          localObject6 = null;
        }
      }
      while (true)
      {
        label747: if (localObject4 != null)
          onTrackListUpdated();
        onStatusUpdated();
        return;
        label764: localObject4 = null;
        break label595;
        label770: localObject6 = localObject4;
        break;
        label777: localObject4 = localObject3;
      }
      label784: Object localObject3 = null;
    }
  }

  public final String getContentId()
  {
    return this.c;
  }

  public final JSONObject getContentInfo()
  {
    return this.d;
  }

  public final Uri getImageUrl()
  {
    return this.m;
  }

  public final PlayerState getPlayerState()
  {
    return this.b;
  }

  public final double getStreamDuration()
  {
    return this.f;
  }

  public final double getStreamPosition()
  {
    double d1 = 0.0D;
    if (this.i == 0L);
    while (true)
    {
      return d1;
      if (this.j)
        break;
      d1 = this.e;
    }
    long l1 = SystemClock.elapsedRealtime();
    long l2 = this.i;
    double d2 = (l1 - l2) / 1000.0D;
    if (d2 < 0.0D);
    while (true)
    {
      double d3 = this.e;
      d1 += d3;
      double d4 = this.f;
      if (d1 <= d4)
        break;
      d1 = this.f;
      break;
      d1 = d2;
    }
  }

  public final String getTitle()
  {
    return this.k;
  }

  public final double getVolume()
  {
    return this.g;
  }

  public final boolean isStreamProgressing()
  {
    return this.j;
  }

  public final MediaProtocolCommand loadMedia(String paramString, ContentMetadata paramContentMetadata, boolean paramBoolean)
    throws IOException, IllegalStateException
  {
    long l1 = this.n + 1L;
    this.n = l1;
    MediaProtocolCommand localMediaProtocolCommand = new MediaProtocolCommand(l1, "LOAD");
    JSONObject localJSONObject1 = new JSONObject();
    try
    {
      long l2 = localMediaProtocolCommand.a();
      JSONObject localJSONObject2 = localJSONObject1.put("cmd_id", l2);
      String str1 = localMediaProtocolCommand.getType();
      JSONObject localJSONObject3 = localJSONObject1.put("type", str1);
      JSONObject localJSONObject4 = localJSONObject1.put("src", paramString);
      if (paramBoolean)
        JSONObject localJSONObject5 = localJSONObject1.put("autoplay", paramBoolean);
      if (paramContentMetadata != null)
      {
        if (paramContentMetadata.getContentInfo() == null)
          break label255;
        JSONObject localJSONObject6 = paramContentMetadata.getContentInfo();
        this.d = localJSONObject6;
        JSONObject localJSONObject7 = this.d;
        JSONObject localJSONObject8 = localJSONObject1.put("content_info", localJSONObject7);
      }
      while (paramContentMetadata.getTitle() != null)
      {
        String str2 = paramContentMetadata.getTitle();
        this.k = str2;
        String str3 = this.k;
        JSONObject localJSONObject9 = localJSONObject1.put("title", str3);
        if (paramContentMetadata.getImageUrl() == null)
          break label280;
        Uri localUri = paramContentMetadata.getImageUrl();
        this.m = localUri;
        String str4 = this.m.toString();
        JSONObject localJSONObject10 = localJSONObject1.put("image_url", str4);
        sendMessage(localJSONObject1);
        Map localMap = this.p;
        Long localLong = Long.valueOf(localMediaProtocolCommand.a());
        Object localObject1 = localMap.put(localLong, localMediaProtocolCommand);
        return localMediaProtocolCommand;
        label255: Object localObject2 = null;
        this.d = localObject2;
      }
    }
    catch (JSONException localJSONException)
    {
      while (true)
      {
        continue;
        this.k = null;
        continue;
        label280: this.m = null;
      }
    }
  }

  public void onDetached()
  {
    Iterator localIterator = this.p.values().iterator();
    while (localIterator.hasNext())
      ((MediaProtocolCommand)localIterator.next()).c();
    PlayerState localPlayerState = PlayerState.IDLE;
    this.b = localPlayerState;
    this.c = null;
    this.d = null;
    this.e = 0.0D;
    this.f = 0.0D;
    this.g = 0.0D;
    this.h = false;
    this.i = 0L;
    this.j = false;
    this.k = null;
    this.l.clear();
    this.m = null;
    this.o = 0L;
    this.p.clear();
    this.q = 65535L;
  }

  protected void onError(String paramString, long paramLong, JSONObject paramJSONObject)
  {
  }

  protected void onKeyRequested(long paramLong, String paramString, String[] paramArrayOfString)
  {
  }

  public final void onMessageReceived(JSONObject paramJSONObject)
  {
    Logger localLogger1 = a;
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = paramJSONObject;
    localLogger1.v("Received message: \"%s\"", arrayOfObject1);
    Object localObject;
    long l3;
    while (true)
    {
      try
      {
        int i1 = paramJSONObject.getString("type");
        if (!"RESPONSE".equals(i1))
          break;
        long l1 = paramJSONObject.getLong("cmd_id");
        Map localMap = this.p;
        Long localLong = Long.valueOf(l1);
        localObject = (MediaProtocolCommand)localMap.remove(localLong);
        long l2 = this.q;
        if (l1 == l2)
          this.q = 65535L;
        if (localObject == null)
        {
          Logger localLogger2 = a;
          Object[] arrayOfObject2 = new Object[1];
          arrayOfObject2[0] = paramJSONObject;
          localLogger2.w("Got a response to an unknown request: %s", arrayOfObject2);
          return;
        }
        if (paramJSONObject.has("status"))
        {
          l3 = paramJSONObject.getJSONObject("status");
          a(l3);
          if (l3.has("error"))
          {
            Logger localLogger3 = a;
            Object[] arrayOfObject3 = new Object[0];
            localLogger3.d("message has an error!", arrayOfObject3);
            JSONObject localJSONObject1 = l3.getJSONObject("error");
            String str1 = localJSONObject1.getString("domain");
            long l4 = localJSONObject1.getLong("code");
            JSONObject localJSONObject2 = localJSONObject1.optJSONObject("error_info");
            ((MediaProtocolCommand)localObject).a(str1, l4, localJSONObject2);
          }
        }
        else
        {
          ((MediaProtocolCommand)localObject).b();
          return;
        }
      }
      catch (JSONException localJSONException)
      {
        Logger localLogger4 = a;
        Object[] arrayOfObject4 = new Object[2];
        arrayOfObject4[0] = paramJSONObject;
        arrayOfObject4[1] = localJSONException;
        localLogger4.e("error parsing message: %s %s", arrayOfObject4);
        return;
      }
      if (((MediaProtocolCommand)localObject).getType().equals("LOAD"))
      {
        this.e = 0.0D;
        this.i = 0L;
      }
    }
    if ("STATUS".equals(localObject))
    {
      localObject = paramJSONObject.getJSONObject("status");
      a((JSONObject)localObject);
      if (!((JSONObject)localObject).has("error"))
        return;
      JSONObject localJSONObject3 = ((JSONObject)localObject).getJSONObject("error");
      String str2 = localJSONObject3.getString("domain");
      long l5 = localJSONObject3.getLong("code");
      JSONObject localJSONObject4 = localJSONObject3.optJSONObject("error_info");
      onError(str2, l5, localJSONObject4);
      return;
    }
    int i2;
    if ("KEY_REQUEST".equals(localObject))
    {
      l3 = paramJSONObject.getLong("cmd_id");
      String str3 = paramJSONObject.getString("method");
      JSONArray localJSONArray = paramJSONObject.getJSONArray("requests");
      int i3 = localJSONArray.length();
      String[] arrayOfString = new String[i3];
      i2 = 0;
      while (i2 < i3)
      {
        String str4 = localJSONArray.get(i2).toString();
        arrayOfString[i2] = str4;
        i2 += 1;
      }
      onKeyRequested(l3, str3, arrayOfString);
      return;
    }
    Logger localLogger5 = a;
    String str5 = "Ignoring message. Got a request with unknown request type=" + i2;
    Object[] arrayOfObject5 = new Object[0];
    localLogger5.w(str5, arrayOfObject5);
  }

  protected void onStatusUpdated()
  {
  }

  protected void onTrackListUpdated()
  {
  }

  public final MediaProtocolCommand playFrom(double paramDouble)
    throws IOException, IllegalStateException
  {
    long l1 = this.n + 1L;
    this.n = l1;
    MediaProtocolCommand localMediaProtocolCommand = new MediaProtocolCommand(l1, "PLAY");
    JSONObject localJSONObject1 = new JSONObject();
    try
    {
      long l2 = localMediaProtocolCommand.a();
      JSONObject localJSONObject2 = localJSONObject1.put("cmd_id", l2);
      String str = localMediaProtocolCommand.getType();
      JSONObject localJSONObject3 = localJSONObject1.put("type", str);
      if (paramDouble >= 0.0D)
        JSONObject localJSONObject4 = localJSONObject1.put("position", paramDouble);
      label92: sendMessage(localJSONObject1);
      Map localMap = this.p;
      Long localLong = Long.valueOf(localMediaProtocolCommand.a());
      Object localObject = localMap.put(localLong, localMediaProtocolCommand);
      return localMediaProtocolCommand;
    }
    catch (JSONException localJSONException)
    {
      break label92;
    }
  }

  public final MediaProtocolCommand requestStatus()
    throws IOException, IllegalStateException
  {
    long l1 = this.n + 1L;
    this.n = l1;
    MediaProtocolCommand localMediaProtocolCommand = new MediaProtocolCommand(l1, "INFO");
    JSONObject localJSONObject1 = new JSONObject();
    try
    {
      long l2 = localMediaProtocolCommand.a();
      JSONObject localJSONObject2 = localJSONObject1.put("cmd_id", l2);
      String str = localMediaProtocolCommand.getType();
      JSONObject localJSONObject3 = localJSONObject1.put("type", str);
      label70: sendMessage(localJSONObject1);
      Map localMap = this.p;
      Long localLong = Long.valueOf(localMediaProtocolCommand.a());
      Object localObject = localMap.put(localLong, localMediaProtocolCommand);
      return localMediaProtocolCommand;
    }
    catch (JSONException localJSONException)
    {
      break label70;
    }
  }

  public final MediaProtocolCommand resume()
    throws IOException, IllegalStateException
  {
    return playFrom(-1.0D);
  }

  protected void sendMessage(JSONObject paramJSONObject)
    throws IOException, IllegalStateException
  {
    Logger localLogger = a;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramJSONObject;
    localLogger.v("Sending message: \"%s\"", arrayOfObject);
    super.sendMessage(paramJSONObject);
  }

  public final MediaProtocolCommand setVolume(double paramDouble)
    throws IOException, IllegalStateException
  {
    long l1 = this.n + 1L;
    this.n = l1;
    MediaProtocolCommand localMediaProtocolCommand = new MediaProtocolCommand(l1, "VOLUME");
    JSONObject localJSONObject1 = new JSONObject();
    try
    {
      long l2 = localMediaProtocolCommand.a();
      JSONObject localJSONObject2 = localJSONObject1.put("cmd_id", l2);
      String str = localMediaProtocolCommand.getType();
      JSONObject localJSONObject3 = localJSONObject1.put("type", str);
      double d1 = a(paramDouble);
      JSONObject localJSONObject4 = localJSONObject1.put("volume", d1);
      label91: sendMessage(localJSONObject1);
      Map localMap = this.p;
      Long localLong = Long.valueOf(localMediaProtocolCommand.a());
      Object localObject = localMap.put(localLong, localMediaProtocolCommand);
      long l3 = localMediaProtocolCommand.a();
      this.q = l3;
      this.g = paramDouble;
      return localMediaProtocolCommand;
    }
    catch (JSONException localJSONException)
    {
      break label91;
    }
  }

  public final void stop()
    throws IOException, IllegalStateException
  {
    JSONObject localJSONObject1 = new JSONObject();
    try
    {
      long l1 = this.n + 1L;
      this.n = l1;
      JSONObject localJSONObject2 = localJSONObject1.put("cmd_id", l1);
      JSONObject localJSONObject3 = localJSONObject1.put("type", "STOP");
      label43: sendMessage(localJSONObject1);
      return;
    }
    catch (JSONException localJSONException)
    {
      break label43;
    }
  }

  public static enum PlayerState
  {
    static
    {
      PLAYING = new PlayerState("PLAYING", 2);
      PlayerState[] arrayOfPlayerState = new PlayerState[3];
      PlayerState localPlayerState1 = IDLE;
      arrayOfPlayerState[0] = localPlayerState1;
      PlayerState localPlayerState2 = STOPPED;
      arrayOfPlayerState[1] = localPlayerState2;
      PlayerState localPlayerState3 = PLAYING;
      arrayOfPlayerState[2] = localPlayerState3;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.MediaProtocolMessageStream
 * JD-Core Version:    0.6.2
 */