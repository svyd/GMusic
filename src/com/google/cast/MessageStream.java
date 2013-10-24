package com.google.cast;

import java.io.IOException;
import org.json.JSONObject;

public abstract class MessageStream
{
  private i a;
  private String b;

  protected MessageStream(String paramString)
    throws IllegalArgumentException
  {
    if (paramString == null)
      throw new IllegalArgumentException("Namespace cannot be null");
    String str = paramString.trim();
    if (str.isEmpty())
      throw new IllegalArgumentException("Namespace cannot be an empty string");
    this.b = str;
  }

  void a(i parami)
  {
    this.a = parami;
  }

  public String getNamespace()
  {
    return this.b;
  }

  public void onAttached()
  {
  }

  public void onDetached()
  {
  }

  public abstract void onMessageReceived(JSONObject paramJSONObject);

  protected void sendMessage(JSONObject paramJSONObject)
    throws IOException, IllegalStateException
  {
    if (this.a == null)
      throw new IllegalStateException("Not attached to a channel");
    i locali = this.a;
    String str = this.b;
    locali.a(str, paramJSONObject);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.MessageStream
 * JD-Core Version:    0.6.2
 */