package com.google.cast;

import android.net.Uri;
import java.util.List;

public final class ApplicationMetadata
{
  private String a;
  private String b;
  private Uri c;
  private List<String> d;

  ApplicationMetadata(String paramString)
  {
    this.a = paramString;
  }

  private void a(Uri paramUri)
  {
    this.c = paramUri;
  }

  private void a(String paramString)
  {
    this.b = paramString;
  }

  private void a(List<String> paramList)
  {
    this.d = paramList;
  }

  public boolean areProtocolsSupported(List<String> paramList)
  {
    if ((this.d != null) && (this.d.containsAll(paramList)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public String getName()
  {
    return this.a;
  }

  public boolean isProtocolSupported(String paramString)
  {
    if ((this.d != null) && (this.d.contains(paramString)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public String toString()
  {
    return this.b;
  }

  static class a
  {
    private ApplicationMetadata a;

    public a(String paramString)
    {
      ApplicationMetadata localApplicationMetadata = new ApplicationMetadata(paramString);
      this.a = localApplicationMetadata;
    }

    public a a(Uri paramUri)
    {
      ApplicationMetadata.a(this.a, paramUri);
      return this;
    }

    public a a(String paramString)
    {
      ApplicationMetadata.a(this.a, paramString);
      return this;
    }

    public a a(List<String> paramList)
    {
      ApplicationMetadata.a(this.a, paramList);
      return this;
    }

    public ApplicationMetadata a()
    {
      return this.a;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.ApplicationMetadata
 * JD-Core Version:    0.6.2
 */