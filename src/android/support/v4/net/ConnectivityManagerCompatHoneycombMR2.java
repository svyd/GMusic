package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class ConnectivityManagerCompatHoneycombMR2
{
  public static boolean isActiveNetworkMetered(ConnectivityManager paramConnectivityManager)
  {
    boolean bool = true;
    NetworkInfo localNetworkInfo = paramConnectivityManager.getActiveNetworkInfo();
    if (localNetworkInfo == null);
    while (true)
    {
      return bool;
      switch (localNetworkInfo.getType())
      {
      case 0:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 8:
      default:
        break;
      case 1:
      case 7:
      case 9:
        bool = false;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.net.ConnectivityManagerCompatHoneycombMR2
 * JD-Core Version:    0.6.2
 */