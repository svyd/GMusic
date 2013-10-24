package com.google.android.youtube.player;

public abstract interface YouTubePlayer
{
  public abstract void addFullscreenControlFlag(int paramInt);

  public abstract void loadVideo(String paramString);

  public abstract void setPlayerStateChangeListener(PlayerStateChangeListener paramPlayerStateChangeListener);

  public abstract void setShowFullscreenButton(boolean paramBoolean);

  public static enum ErrorReason
  {
    static
    {
      BLOCKED_FOR_APP = new ErrorReason("BLOCKED_FOR_APP", 1);
      NOT_PLAYABLE = new ErrorReason("NOT_PLAYABLE", 2);
      NETWORK_ERROR = new ErrorReason("NETWORK_ERROR", 3);
      UNAUTHORIZED_OVERLAY = new ErrorReason("UNAUTHORIZED_OVERLAY", 4);
      PLAYER_VIEW_TOO_SMALL = new ErrorReason("PLAYER_VIEW_TOO_SMALL", 5);
      EMPTY_PLAYLIST = new ErrorReason("EMPTY_PLAYLIST", 6);
      AUTOPLAY_DISABLED = new ErrorReason("AUTOPLAY_DISABLED", 7);
      USER_DECLINED_RESTRICTED_CONTENT = new ErrorReason("USER_DECLINED_RESTRICTED_CONTENT", 8);
      USER_DECLINED_HIGH_BANDWIDTH = new ErrorReason("USER_DECLINED_HIGH_BANDWIDTH", 9);
      UNEXPECTED_SERVICE_DISCONNECTION = new ErrorReason("UNEXPECTED_SERVICE_DISCONNECTION", 10);
      INTERNAL_ERROR = new ErrorReason("INTERNAL_ERROR", 11);
      UNKNOWN = new ErrorReason("UNKNOWN", 12);
      ErrorReason[] arrayOfErrorReason = new ErrorReason[13];
      ErrorReason localErrorReason1 = EMBEDDING_DISABLED;
      arrayOfErrorReason[0] = localErrorReason1;
      ErrorReason localErrorReason2 = BLOCKED_FOR_APP;
      arrayOfErrorReason[1] = localErrorReason2;
      ErrorReason localErrorReason3 = NOT_PLAYABLE;
      arrayOfErrorReason[2] = localErrorReason3;
      ErrorReason localErrorReason4 = NETWORK_ERROR;
      arrayOfErrorReason[3] = localErrorReason4;
      ErrorReason localErrorReason5 = UNAUTHORIZED_OVERLAY;
      arrayOfErrorReason[4] = localErrorReason5;
      ErrorReason localErrorReason6 = PLAYER_VIEW_TOO_SMALL;
      arrayOfErrorReason[5] = localErrorReason6;
      ErrorReason localErrorReason7 = EMPTY_PLAYLIST;
      arrayOfErrorReason[6] = localErrorReason7;
      ErrorReason localErrorReason8 = AUTOPLAY_DISABLED;
      arrayOfErrorReason[7] = localErrorReason8;
      ErrorReason localErrorReason9 = USER_DECLINED_RESTRICTED_CONTENT;
      arrayOfErrorReason[8] = localErrorReason9;
      ErrorReason localErrorReason10 = USER_DECLINED_HIGH_BANDWIDTH;
      arrayOfErrorReason[9] = localErrorReason10;
      ErrorReason localErrorReason11 = UNEXPECTED_SERVICE_DISCONNECTION;
      arrayOfErrorReason[10] = localErrorReason11;
      ErrorReason localErrorReason12 = INTERNAL_ERROR;
      arrayOfErrorReason[11] = localErrorReason12;
      ErrorReason localErrorReason13 = UNKNOWN;
      arrayOfErrorReason[12] = localErrorReason13;
    }
  }

  public static abstract interface PlayerStateChangeListener
  {
    public abstract void onAdStarted();

    public abstract void onError(YouTubePlayer.ErrorReason paramErrorReason);

    public abstract void onLoaded(String paramString);

    public abstract void onLoading();

    public abstract void onVideoEnded();

    public abstract void onVideoStarted();
  }

  public static abstract interface OnInitializedListener
  {
    public abstract void onInitializationFailure(YouTubePlayer.Provider paramProvider, YouTubeInitializationResult paramYouTubeInitializationResult);

    public abstract void onInitializationSuccess(YouTubePlayer.Provider paramProvider, YouTubePlayer paramYouTubePlayer, boolean paramBoolean);
  }

  public static abstract interface Provider
  {
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.YouTubePlayer
 * JD-Core Version:    0.6.2
 */