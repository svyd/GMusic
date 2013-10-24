package com.google.android.music.download;

import android.util.Base64;
import android.util.Log;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.util.EncodingUtils;

public class RequestSigningUtil
{
  private static final byte[] s1 = Base64.decode("VzeC4H4h+T2f0VI180nVX8x+Mb5HiTtGnKgH52Otj8ZCGDz9jRWyHb6QXK0JskSiOgzQfwTY5xgLLSdUSreaLVMsVVWfxfa8Rw==", 0);
  private static final byte[] s2 = Base64.decode("ZAPnhUkYwQ6y5DdQxWThbvhJHN8msQ1rqJw0ggKdufQjelrKuiGGJI30aswkgCWTDyHkTGK9ynlqTkJ5L4CiGGUabGeo8M6JTQ==", 0);

  public static void appendMplayUrlSignatureParams(String paramString, StringBuilder paramStringBuilder)
  {
    try
    {
      byte[] arrayOfByte1 = new byte[s1.length];
      int i = 0;
      while (true)
      {
        int j = s1.length;
        if (i >= j)
          break;
        int k = s1[i];
        int m = s2[i];
        int n = (byte)(k ^ m);
        arrayOfByte1[i] = n;
        i += 1;
      }
      String str1 = String.valueOf(System.currentTimeMillis());
      Mac localMac = Mac.getInstance("HmacSHA1");
      String str2 = localMac.getAlgorithm();
      SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte1, str2);
      localMac.init(localSecretKeySpec);
      byte[] arrayOfByte2 = EncodingUtils.getAsciiBytes(paramString);
      localMac.update(arrayOfByte2);
      byte[] arrayOfByte3 = EncodingUtils.getAsciiBytes(str1);
      String str3 = Base64.encodeToString(localMac.doFinal(arrayOfByte3), 11);
      StringBuilder localStringBuilder1 = paramStringBuilder.append("&slt=").append(str1);
      StringBuilder localStringBuilder2 = paramStringBuilder.append("&sig=").append(str3);
      return;
    }
    catch (GeneralSecurityException localGeneralSecurityException)
    {
      int i1 = Log.e("RequestSigningUtil", "Url signing failed", localGeneralSecurityException);
      throw new RuntimeException("Url signing failed", localGeneralSecurityException);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.RequestSigningUtil
 * JD-Core Version:    0.6.2
 */