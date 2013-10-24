package com.google.android.music.ui.cardlib.utils;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Utils
{
  public static void clearPlayCardThumbnails(ListView paramListView)
  {
    if (paramListView == null)
      return;
    int i = 0;
    while (true)
    {
      int j = paramListView.getChildCount();
      if (i >= j)
        return;
      View localView = paramListView.getChildAt(i);
      if ((localView instanceof ViewGroup))
      {
        ViewGroup localViewGroup = (ViewGroup)localView;
        int k = 0;
        while (true)
        {
          int m = localViewGroup.getChildCount();
          if (k >= m)
            break;
          localView = localViewGroup.getChildAt(k);
          if ((localView instanceof PlayCardView))
            ((PlayCardView)localView).clearThumbnail();
          k += 1;
        }
      }
      if ((localView instanceof PlayCardView))
        ((PlayCardView)localView).clearThumbnail();
      i += 1;
    }
  }

  public static void ensureOnMainThread()
  {
    Looper localLooper1 = Looper.myLooper();
    Looper localLooper2 = Looper.getMainLooper();
    if (localLooper1 == localLooper2)
      return;
    throw new IllegalStateException("This method must be called from the UI thread.");
  }

  public static String urlDecode(String paramString)
  {
    try
    {
      String str1 = URLDecoder.decode(paramString, "UTF-8");
      str2 = str1;
      return str2;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      while (true)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Unable to parse ").append(paramString).append(" - ");
        String str3 = localIllegalArgumentException.getMessage();
        String str4 = str3;
        int i = Log.d("CarLib.Utils", str4);
        String str2 = null;
      }
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      int j = Log.wtf("CarLib.Utils", "%s", localUnsupportedEncodingException);
      throw new RuntimeException(localUnsupportedEncodingException);
    }
  }

  public static String urlEncode(String paramString)
  {
    try
    {
      String str = URLEncoder.encode(paramString, "UTF-8");
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      int i = Log.wtf("CarLib.Utils", "%s", localUnsupportedEncodingException);
      throw new RuntimeException(localUnsupportedEncodingException);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.utils.Utils
 * JD-Core Version:    0.6.2
 */