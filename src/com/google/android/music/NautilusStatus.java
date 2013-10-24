package com.google.android.music;

public enum NautilusStatus
{
  static
  {
    TRIAL_AVAILABLE = new NautilusStatus("TRIAL_AVAILABLE", 1);
    PURCHASE_AVAILABLE_NO_TRIAL = new NautilusStatus("PURCHASE_AVAILABLE_NO_TRIAL", 2);
    GOT_NAUTILUS = new NautilusStatus("GOT_NAUTILUS", 3);
    NautilusStatus[] arrayOfNautilusStatus = new NautilusStatus[4];
    NautilusStatus localNautilusStatus1 = UNAVAILABLE;
    arrayOfNautilusStatus[0] = localNautilusStatus1;
    NautilusStatus localNautilusStatus2 = TRIAL_AVAILABLE;
    arrayOfNautilusStatus[1] = localNautilusStatus2;
    NautilusStatus localNautilusStatus3 = PURCHASE_AVAILABLE_NO_TRIAL;
    arrayOfNautilusStatus[2] = localNautilusStatus3;
    NautilusStatus localNautilusStatus4 = GOT_NAUTILUS;
    arrayOfNautilusStatus[3] = localNautilusStatus4;
  }

  public boolean isFreeTrialAvailable()
  {
    NautilusStatus localNautilusStatus = TRIAL_AVAILABLE;
    if (this == localNautilusStatus);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isNautilusEnabled()
  {
    NautilusStatus localNautilusStatus = GOT_NAUTILUS;
    if (this == localNautilusStatus);
    for (boolean bool = true; ; bool = false)
      return bool;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.NautilusStatus
 * JD-Core Version:    0.6.2
 */