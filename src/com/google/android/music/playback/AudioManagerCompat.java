package com.google.android.music.playback;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import com.google.android.music.preferences.MusicPreferences;
import java.lang.reflect.Method;

public class AudioManagerCompat
{
  private static Method sRegisterRemoteControlClientMethod;
  private static boolean sRemoteControlAPIsExist = false;
  private static Method sUnregisterRemoteControlClient;
  private final AudioManager mAudioManager;

  static
  {
    if (!MusicPreferences.isICSOrGreater())
      return;
    try
    {
      Class localClass = RemoteControlClientCompat.getActualRemoteControlClientClass(AudioManagerCompat.class.getClassLoader());
      Class[] arrayOfClass1 = new Class[1];
      arrayOfClass1[0] = localClass;
      sRegisterRemoteControlClientMethod = AudioManager.class.getMethod("registerRemoteControlClient", arrayOfClass1);
      Class[] arrayOfClass2 = new Class[1];
      arrayOfClass2[0] = localClass;
      sUnregisterRemoteControlClient = AudioManager.class.getMethod("unregisterRemoteControlClient", arrayOfClass2);
      sRemoteControlAPIsExist = true;
      if (sRemoteControlAPIsExist)
        return;
      throw new RuntimeException("ICS APIs for RemoteControlClient don't exist");
    }
    catch (Exception localException)
    {
      while (true)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Could not get ICS info: ");
        String str1 = localException.getMessage();
        String str2 = str1;
        int i = Log.i("AudioManagerCompat", str2);
      }
    }
  }

  private AudioManagerCompat(Context paramContext)
  {
    AudioManager localAudioManager = (AudioManager)paramContext.getSystemService("audio");
    this.mAudioManager = localAudioManager;
  }

  public static AudioManagerCompat getAudioManagerCompat(Context paramContext)
  {
    return new AudioManagerCompat(paramContext);
  }

  public void registerRemoteControlClient(RemoteControlClientCompat paramRemoteControlClientCompat)
  {
    if (!sRemoteControlAPIsExist)
      return;
    try
    {
      Method localMethod = sRegisterRemoteControlClientMethod;
      AudioManager localAudioManager = this.mAudioManager;
      Object[] arrayOfObject = new Object[1];
      Object localObject1 = paramRemoteControlClientCompat.getActualRemoteControlClientObject();
      arrayOfObject[0] = localObject1;
      Object localObject2 = localMethod.invoke(localAudioManager, arrayOfObject);
      return;
    }
    catch (Exception localException)
    {
      String str = localException.getMessage();
      int i = Log.e("AudioManagerCompat", str, localException);
    }
  }

  public void unregisterRemoteControlClient(RemoteControlClientCompat paramRemoteControlClientCompat)
  {
    if (!sRemoteControlAPIsExist)
      return;
    try
    {
      Method localMethod = sUnregisterRemoteControlClient;
      AudioManager localAudioManager = this.mAudioManager;
      Object[] arrayOfObject = new Object[1];
      Object localObject1 = paramRemoteControlClientCompat.getActualRemoteControlClientObject();
      arrayOfObject[0] = localObject1;
      Object localObject2 = localMethod.invoke(localAudioManager, arrayOfObject);
      return;
    }
    catch (Exception localException)
    {
      String str = localException.getMessage();
      int i = Log.e("AudioManagerCompat", str, localException);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.playback.AudioManagerCompat
 * JD-Core Version:    0.6.2
 */