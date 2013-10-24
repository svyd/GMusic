package com.google.cast;

import java.util.HashMap;
import org.json.JSONObject;

public final class MediaProtocolCommand
{
  private long a;
  private String b;
  private Listener c;
  private boolean d;
  private boolean e;
  private boolean f;
  private String g;
  private long h;
  private JSONObject i;
  private HashMap<String, Object> j;

  MediaProtocolCommand(long paramLong, String paramString)
  {
    this.a = paramLong;
    this.b = paramString;
    HashMap localHashMap = new HashMap();
    this.j = localHashMap;
  }

  long a()
  {
    return this.a;
  }

  void a(String paramString, long paramLong, JSONObject paramJSONObject)
  {
    this.g = paramString;
    this.h = paramLong;
    this.i = paramJSONObject;
    this.f = true;
  }

  void b()
  {
    this.d = true;
    if (this.c == null)
      return;
    this.c.onCompleted(this);
  }

  void c()
  {
    this.e = true;
    if (this.c == null)
      return;
    this.c.onCancelled(this);
  }

  public String getErrorDomain()
  {
    return this.g;
  }

  public JSONObject getErrorInfo()
  {
    return this.i;
  }

  public String getType()
  {
    return this.b;
  }

  public Object getUserObject(String paramString)
  {
    return this.j.get(paramString);
  }

  public boolean hasError()
  {
    return this.f;
  }

  public Object putUserObject(String paramString, Object paramObject)
  {
    return this.j.put(paramString, paramObject);
  }

  public void setListener(Listener paramListener)
  {
    this.c = paramListener;
  }

  public static abstract interface Listener
  {
    public abstract void onCancelled(MediaProtocolCommand paramMediaProtocolCommand);

    public abstract void onCompleted(MediaProtocolCommand paramMediaProtocolCommand);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.MediaProtocolCommand
 * JD-Core Version:    0.6.2
 */