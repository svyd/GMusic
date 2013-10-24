package com.google.android.music.download;

import java.io.IOException;

public class ServerRejectionException extends IOException
{
  private final RejectionReason mRejectionReason;

  public ServerRejectionException(RejectionReason paramRejectionReason)
  {
    this.mRejectionReason = paramRejectionReason;
  }

  public RejectionReason getRejectionReason()
  {
    return this.mRejectionReason;
  }

  public static enum RejectionReason
  {
    static
    {
      DEVICE_NOT_AUTHORIZED = new RejectionReason("DEVICE_NOT_AUTHORIZED", 2);
      TRACK_NOT_IN_SUBSCRIPTION = new RejectionReason("TRACK_NOT_IN_SUBSCRIPTION", 3);
      RejectionReason[] arrayOfRejectionReason = new RejectionReason[4];
      RejectionReason localRejectionReason1 = ANOTHER_STREAM_BEING_PLAYED;
      arrayOfRejectionReason[0] = localRejectionReason1;
      RejectionReason localRejectionReason2 = STREAM_RATE_LIMIT_REACHED;
      arrayOfRejectionReason[1] = localRejectionReason2;
      RejectionReason localRejectionReason3 = DEVICE_NOT_AUTHORIZED;
      arrayOfRejectionReason[2] = localRejectionReason3;
      RejectionReason localRejectionReason4 = TRACK_NOT_IN_SUBSCRIPTION;
      arrayOfRejectionReason[3] = localRejectionReason4;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.ServerRejectionException
 * JD-Core Version:    0.6.2
 */