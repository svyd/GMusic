package com.google.android.music.cast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.android.common.http.GoogleHttpClient;
import com.google.android.gsf.Gservices;
import com.google.android.music.cloudclient.MusicRequest;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class CastTokenClient
{
  private final BroadcastReceiver mAccountChangeReceiver;
  private final Cache mCastTokenCache;
  private final Context mContext;
  private final MusicPreferences mMusicPreferences;
  private final Object mPreferencesHolder;

  public CastTokenClient(Context paramContext)
  {
    Object localObject1 = new Object();
    this.mPreferencesHolder = localObject1;
    Cache localCache = new Cache(20);
    this.mCastTokenCache = localCache;
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        CastTokenClient.this.clearAllCachedCastTokens();
      }
    };
    this.mAccountChangeReceiver = local1;
    this.mContext = paramContext;
    Object localObject2 = this.mPreferencesHolder;
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, localObject2);
    this.mMusicPreferences = localMusicPreferences;
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.google.android.music.accountchanged");
    Context localContext = this.mContext;
    BroadcastReceiver localBroadcastReceiver = this.mAccountChangeReceiver;
    Intent localIntent = localContext.registerReceiver(localBroadcastReceiver, localIntentFilter);
  }

  private String extractToken(HttpRequestBase paramHttpRequestBase, HttpResponse paramHttpResponse)
    throws IOException
  {
    byte[] arrayOfByte = MusicRequest.readAndReleaseShortResponse(paramHttpRequestBase, paramHttpResponse, 65536);
    String str = EntityUtils.getContentCharSet(paramHttpResponse.getEntity());
    if (str == null)
      str = "UTF-8";
    return new String(arrayOfByte, str);
  }

  private String fetchCastTokenFromServer(String paramString1, String paramString2)
  {
    if (isLogVerbose())
      Log.v("MusicCast", "Fetching cast token.");
    GoogleHttpClient localGoogleHttpClient = MusicRequest.getSharedHttpClient(this.mContext);
    String str1 = String.valueOf(Gservices.getLong(this.mContext.getContentResolver(), "android_id", 0L));
    HttpPost localHttpPost = new HttpPost("https://android.clients.google.com/music/playon/createtoken");
    ArrayList localArrayList = Lists.newArrayList();
    BasicNameValuePair localBasicNameValuePair1 = new BasicNameValuePair("host_device_id", str1);
    boolean bool1 = localArrayList.add(localBasicNameValuePair1);
    String str2 = this.mMusicPreferences.getLoggingId();
    BasicNameValuePair localBasicNameValuePair2 = new BasicNameValuePair("host_device_logging_id", str2);
    boolean bool2 = localArrayList.add(localBasicNameValuePair2);
    BasicNameValuePair localBasicNameValuePair3 = new BasicNameValuePair("remote_endpoint_type", paramString1);
    boolean bool3 = localArrayList.add(localBasicNameValuePair3);
    BasicNameValuePair localBasicNameValuePair4 = new BasicNameValuePair("remote_endpoint_id", paramString2);
    boolean bool4 = localArrayList.add(localBasicNameValuePair4);
    try
    {
      UrlEncodedFormEntity localUrlEncodedFormEntity = new UrlEncodedFormEntity(localArrayList);
      localHttpPost.setEntity(localUrlEncodedFormEntity);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      try
      {
        Context localContext1 = this.mContext;
        Context localContext2 = this.mContext;
        Object localObject = this.mPreferencesHolder;
        MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(localContext2, localObject);
        HttpResponse localHttpResponse = new MusicRequest(localContext1, localMusicPreferences).sendRequest(localHttpPost, localGoogleHttpClient);
        String str3 = extractToken(localHttpPost, localHttpResponse);
        for (str4 = str3; ; str4 = null)
        {
          return str4;
          localUnsupportedEncodingException = localUnsupportedEncodingException;
          Log.w("MusicCast", "Could not encode params -- should not happen!", localUnsupportedEncodingException);
        }
      }
      catch (IOException localIOException)
      {
        while (true)
        {
          Log.w("MusicCast", "Unable to fetch cast token!", localIOException);
          str4 = null;
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        while (true)
        {
          Log.w("MusicCast", "Unable to fetch cast token!", localInterruptedException);
          String str4 = null;
        }
      }
    }
  }

  private boolean isLogVerbose()
  {
    return this.mMusicPreferences.isLogFilesEnabled();
  }

  /** @deprecated */
  public void clearAllCachedCastTokens()
  {
    try
    {
      this.mCastTokenCache.clear();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  public void clearCachedCastToken(String paramString)
  {
    try
    {
      Object localObject1 = this.mCastTokenCache.remove(paramString);
      return;
    }
    finally
    {
      localObject2 = finally;
      throw localObject2;
    }
  }

  /** @deprecated */
  public String getCachedCastToken(String paramString)
  {
    try
    {
      String str = (String)this.mCastTokenCache.get(paramString);
      return str;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  public String getCastToken(String paramString1, String paramString2)
  {
    try
    {
      if (!this.mCastTokenCache.containsKey(paramString2))
      {
        String str1 = fetchCastTokenFromServer(paramString1, paramString2);
        Object localObject1 = this.mCastTokenCache.put(paramString2, str1);
      }
      String str2 = (String)this.mCastTokenCache.get(paramString2);
      return str2;
    }
    finally
    {
      localObject2 = finally;
      throw localObject2;
    }
  }

  /** @deprecated */
  public boolean hasCachedCastToken(String paramString)
  {
    try
    {
      boolean bool1 = this.mCastTokenCache.containsKey(paramString);
      boolean bool2 = bool1;
      return bool2;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void release()
  {
    MusicPreferences.releaseMusicPreferences(this.mPreferencesHolder);
    Context localContext = this.mContext;
    BroadcastReceiver localBroadcastReceiver = this.mAccountChangeReceiver;
    localContext.unregisterReceiver(localBroadcastReceiver);
  }

  private static class Cache extends LinkedHashMap<String, String>
  {
    private final int mCapacity;

    public Cache(int paramInt)
    {
      this.mCapacity = paramInt;
    }

    protected boolean removeEldestEntry(Map.Entry<String, String> paramEntry)
    {
      int i = size();
      int j = this.mCapacity;
      if (i > j);
      for (boolean bool = true; ; bool = false)
        return bool;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cast.CastTokenClient
 * JD-Core Version:    0.6.2
 */