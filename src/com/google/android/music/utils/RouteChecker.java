package com.google.android.music.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.support.v7.media.MediaRouter.ProviderInfo;
import android.support.v7.media.MediaRouter.RouteInfo;
import com.google.android.music.log.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class RouteChecker
{
  private final boolean LOGV;
  private Context mContext;
  private final boolean mShouldValidateMediaRoutes;
  private final Set<PackageEntry> mValidPackages;

  public RouteChecker(Context paramContext)
  {
    boolean bool1 = DebugUtils.isLoggable(DebugUtils.MusicTag.CAST);
    this.LOGV = bool1;
    HashSet localHashSet = new HashSet();
    this.mValidPackages = localHashSet;
    boolean bool2 = ConfigUtils.getShouldValidateMediaRoutes();
    this.mShouldValidateMediaRoutes = bool2;
    if (!this.mShouldValidateMediaRoutes)
      Log.w("RouteChecker", "Not validating media routes");
    this.mContext = paramContext;
    if (!this.mShouldValidateMediaRoutes)
      return;
    registerPackages();
  }

  private boolean anySignaturesMatch(String paramString, Signature[] paramArrayOfSignature)
  {
    boolean bool = false;
    int j;
    PackageEntry localPackageEntry;
    try
    {
      MessageDigest localMessageDigest1 = MessageDigest.getInstance("SHA-1");
      MessageDigest localMessageDigest2 = localMessageDigest1;
      Signature[] arrayOfSignature = paramArrayOfSignature;
      int i = arrayOfSignature.length;
      j = 0;
      if (j < i)
      {
        byte[] arrayOfByte1 = arrayOfSignature[j].toByteArray();
        byte[] arrayOfByte2 = localMessageDigest2.digest(arrayOfByte1);
        SHA1 localSHA1 = new SHA1(arrayOfByte2);
        localPackageEntry = new PackageEntry(paramString, localSHA1);
        if (this.mValidPackages.contains(localPackageEntry))
          bool = true;
      }
      else
      {
        label90: return bool;
      }
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      break label90;
    }
    if (this.LOGV)
    {
      String str = "Did not find " + localPackageEntry;
      Log.v("RouteChecker", str);
    }
    while (true)
    {
      j += 1;
      break;
      Log.w("RouteChecker", "Unsupported route");
    }
  }

  private Signature[] getPackageSignatures(PackageManager paramPackageManager, String paramString)
  {
    try
    {
      PackageInfo localPackageInfo = paramPackageManager.getPackageInfo(paramString, 64);
      arrayOfSignature = localPackageInfo.signatures;
      return arrayOfSignature;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      while (true)
        Signature[] arrayOfSignature = new Signature[0];
    }
  }

  private boolean registerJsonPackageSpec(String paramString)
  {
    try
    {
      registerJsonPackageSpecImpl(paramString);
      bool = true;
      return bool;
    }
    catch (JSONException localJSONException)
    {
      while (true)
      {
        String str = "Could not register packages: " + paramString;
        Log.e("RouteChecker", str, localJSONException);
        boolean bool = false;
      }
    }
  }

  private void registerJsonPackageSpecImpl(String paramString)
    throws JSONException
  {
    JSONTokener localJSONTokener1 = new org/json/JSONTokener;
    JSONTokener localJSONTokener2 = localJSONTokener1;
    String str1 = paramString;
    localJSONTokener2.<init>(str1);
    Object localObject = localJSONTokener1.nextValue();
    if ((localObject instanceof JSONArray))
    {
      JSONArray localJSONArray1 = (JSONArray)localObject;
      int i = localJSONArray1.length();
      int j = 0;
      while (j < i)
      {
        JSONObject localJSONObject = localJSONArray1.getJSONObject(j);
        String str2 = "packages";
        JSONArray localJSONArray2 = localJSONObject.getJSONArray(str2);
        int k = localJSONArray2.length();
        String[] arrayOfString = new String[k];
        int m = 0;
        while (m < k)
        {
          String str3 = localJSONArray2.getString(m);
          arrayOfString[m] = str3;
          m += 1;
        }
        String str4 = "sha1s";
        JSONArray localJSONArray3 = localJSONObject.getJSONArray(str4);
        int n = localJSONArray3.length();
        SHA1[] arrayOfSHA1 = new SHA1[n];
        int i1 = 0;
        while (true)
        {
          int i2 = n;
          if (i1 >= i2)
            break;
          SHA1 localSHA11 = SHA1.createFromHexString(localJSONArray3.getString(i1));
          arrayOfSHA1[i1] = localSHA11;
          i1 += 1;
        }
        int i3 = 0;
        while (i3 < k)
        {
          int i4 = 0;
          while (true)
          {
            int i5 = n;
            if (i4 >= i5)
              break;
            String str5 = arrayOfString[i3];
            SHA1 localSHA12 = arrayOfSHA1[i4];
            RouteChecker localRouteChecker = this;
            String str6 = str5;
            SHA1 localSHA13 = localSHA12;
            PackageEntry localPackageEntry = new PackageEntry(str6, localSHA13);
            boolean bool = this.mValidPackages.add(localPackageEntry);
            i4 += 1;
          }
          i3 += 1;
        }
        j += 1;
      }
    }
    if (!localJSONTokener1.more())
      return;
    throw localJSONTokener1.syntaxError("Unexpected data at end of JSON string.");
  }

  private void registerPackages()
  {
    String str = ConfigUtils.getMediaRoutePackageSignatures();
    if ((str != null) && (registerJsonPackageSpec(str)))
      return;
    Log.i("RouteChecker", "Using default route package signatures.");
    boolean bool = registerJsonPackageSpec("[{\"packages\": [\"com.google.android.music\", \"com.google.android.setupwarlock\"], \"sha1s\": [\"38918A453D07199354F8B19AF05EC6562CED5788\", \"58E1C4133F7441EC3D2C270270A14802DA47BA0E\"]}]");
  }

  public boolean isAcceptableRoute(MediaRouter.RouteInfo paramRouteInfo)
  {
    boolean bool = true;
    if (!this.mShouldValidateMediaRoutes);
    while (true)
    {
      return bool;
      String str1 = paramRouteInfo.getProvider().getPackageName();
      if (!str1.equals("android"))
      {
        PackageManager localPackageManager = this.mContext.getPackageManager();
        Signature[] arrayOfSignature = getPackageSignatures(localPackageManager, str1);
        if (!anySignaturesMatch(str1, arrayOfSignature))
        {
          String str2 = "Rejecting package: " + str1;
          Log.w("RouteChecker", str2);
          bool = false;
        }
      }
    }
  }

  private class PackageEntry
  {
    private final RouteChecker.SHA1 mDigest;
    private final int mHashCode;
    private final String mName;

    public PackageEntry(String paramSHA1, RouteChecker.SHA1 arg3)
    {
      this.mName = paramSHA1;
      Object localObject;
      this.mDigest = localObject;
      int i = this.mName.hashCode();
      int j = this.mDigest.hashCode();
      int k = i ^ j;
      this.mHashCode = k;
    }

    public boolean equals(Object paramObject)
    {
      boolean bool = false;
      if (!(paramObject instanceof PackageEntry));
      while (true)
      {
        return bool;
        PackageEntry localPackageEntry = (PackageEntry)paramObject;
        int i = this.mHashCode;
        int j = localPackageEntry.mHashCode;
        if (i != j)
        {
          String str1 = this.mName;
          String str2 = localPackageEntry.mName;
          if (str1.equals(str2))
          {
            RouteChecker.SHA1 localSHA11 = this.mDigest;
            RouteChecker.SHA1 localSHA12 = localPackageEntry.mDigest;
            if (localSHA11.equals(localSHA12))
              bool = true;
          }
        }
      }
    }

    public int hashCode()
    {
      return this.mHashCode;
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("{name: ");
      String str = this.mName;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str).append(", digest: ");
      RouteChecker.SHA1 localSHA1 = this.mDigest;
      return localSHA1 + "}";
    }
  }

  private static class SHA1
  {
    private byte[] mDigest;

    public SHA1(byte[] paramArrayOfByte)
    {
      if ((paramArrayOfByte == null) || (paramArrayOfByte.length != 20))
        throw new IllegalArgumentException("Expected SHA1 digest");
      this.mDigest = paramArrayOfByte;
    }

    public static SHA1 createFromHexString(String paramString)
    {
      if ((paramString == null) || (paramString.length() != 40))
        throw new NumberFormatException("Expected SHA1 hex string");
      byte[] arrayOfByte = new byte[20];
      int i = 0;
      while (i < 20)
      {
        int j = i * 2;
        int k = parseHexDigit(paramString.charAt(j));
        int m = j + 1;
        int n = parseHexDigit(paramString.charAt(m));
        int i1 = (byte)(k << 4 | n);
        arrayOfByte[i] = i1;
        i += 1;
      }
      return new SHA1(arrayOfByte);
    }

    private static byte parseHexDigit(char paramChar)
    {
      byte b;
      if ((paramChar >= '0') && (paramChar <= '9'))
        b = (byte)(paramChar + '\0*0');
      while (true)
      {
        return b;
        if ((paramChar >= 'A') && (paramChar <= 'F'))
        {
          b = (byte)(paramChar + '\0'7' + 10);
        }
        else
        {
          if ((paramChar < 'a') || (paramChar > 'f'))
            break;
          b = (byte)(paramChar + '\0#7' + 10);
        }
      }
      String str = "Expected hex character got " + paramChar;
      throw new NumberFormatException(str);
    }

    private static char toHexDigit(byte paramByte)
    {
      if ((paramByte >= 0) && (paramByte <= 9));
      for (char c = (char)(paramByte + 48); ; c = (char)(paramByte + -10 + 65))
      {
        return c;
        if (paramByte > 15)
          break;
      }
      String str = "byte out of range 0..15: " + paramByte;
      throw new NumberFormatException(str);
    }

    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof SHA1));
      byte[] arrayOfByte1;
      byte[] arrayOfByte2;
      for (boolean bool = false; ; bool = Arrays.equals(arrayOfByte1, arrayOfByte2))
      {
        return bool;
        arrayOfByte1 = this.mDigest;
        arrayOfByte2 = ((SHA1)paramObject).mDigest;
      }
    }

    public int hashCode()
    {
      return Arrays.hashCode(this.mDigest);
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      byte[] arrayOfByte = this.mDigest;
      int i = arrayOfByte.length;
      int j = 0;
      while (j < i)
      {
        int k = arrayOfByte[j];
        byte b1 = (byte)(k >> 4 & 0xF);
        byte b2 = (byte)(k & 0xF);
        char c1 = toHexDigit(b1);
        StringBuilder localStringBuilder2 = localStringBuilder1.append(c1);
        char c2 = toHexDigit(b2);
        StringBuilder localStringBuilder3 = localStringBuilder1.append(c2);
        j += 1;
      }
      return localStringBuilder1.toString();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.RouteChecker
 * JD-Core Version:    0.6.2
 */