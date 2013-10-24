package com.google.android.youtube.player;

import android.app.Activity;
import android.os.Bundle;

public class YouTubeBaseActivity extends Activity
{
  private a a;
  private YouTubePlayerView b;
  private int c;
  private Bundle d;

  final YouTubePlayerView.b a()
  {
    return this.a;
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    a locala = new a((byte)0);
    this.a = locala;
    if (paramBundle != null);
    for (Bundle localBundle = paramBundle.getBundle("YouTubeBaseActivity.KEY_PLAYER_VIEW_STATE"); ; localBundle = null)
    {
      this.d = localBundle;
      return;
    }
  }

  protected void onDestroy()
  {
    if (this.b != null)
    {
      YouTubePlayerView localYouTubePlayerView = this.b;
      boolean bool = isFinishing();
      localYouTubePlayerView.a(bool);
    }
    super.onDestroy();
  }

  protected void onPause()
  {
    this.c = 1;
    if (this.b != null)
      this.b.c();
    super.onPause();
  }

  protected void onResume()
  {
    super.onResume();
    this.c = 2;
    if (this.b == null)
      return;
    this.b.b();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.b != null);
    for (Bundle localBundle = this.b.e(); ; localBundle = this.d)
    {
      paramBundle.putBundle("YouTubeBaseActivity.KEY_PLAYER_VIEW_STATE", localBundle);
      return;
    }
  }

  protected void onStart()
  {
    super.onStart();
    this.c = 1;
    if (this.b == null)
      return;
    this.b.a();
  }

  protected void onStop()
  {
    this.c = 0;
    if (this.b != null)
      this.b.d();
    super.onStop();
  }

  private final class a
    implements YouTubePlayerView.b
  {
    private a()
    {
    }

    public final void a(YouTubePlayerView paramYouTubePlayerView)
    {
      if ((YouTubeBaseActivity.c(YouTubeBaseActivity.this) != null) && (YouTubeBaseActivity.c(YouTubeBaseActivity.this) != paramYouTubePlayerView))
        YouTubeBaseActivity.c(YouTubeBaseActivity.this).b(true);
      YouTubePlayerView localYouTubePlayerView = YouTubeBaseActivity.a(YouTubeBaseActivity.this, paramYouTubePlayerView);
      if (YouTubeBaseActivity.d(YouTubeBaseActivity.this) > 0)
        paramYouTubePlayerView.a();
      if (YouTubeBaseActivity.d(YouTubeBaseActivity.this) < 2)
        return;
      paramYouTubePlayerView.b();
    }

    public final void a(YouTubePlayerView paramYouTubePlayerView, String paramString, YouTubePlayer.OnInitializedListener paramOnInitializedListener)
    {
      YouTubeBaseActivity localYouTubeBaseActivity = YouTubeBaseActivity.this;
      Bundle localBundle1 = YouTubeBaseActivity.a(YouTubeBaseActivity.this);
      YouTubePlayerView localYouTubePlayerView1 = paramYouTubePlayerView;
      YouTubePlayerView localYouTubePlayerView2 = paramYouTubePlayerView;
      String str = paramString;
      YouTubePlayer.OnInitializedListener localOnInitializedListener = paramOnInitializedListener;
      localYouTubePlayerView1.a(localYouTubeBaseActivity, localYouTubePlayerView2, str, localOnInitializedListener, localBundle1);
      Bundle localBundle2 = YouTubeBaseActivity.b(YouTubeBaseActivity.this);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.YouTubeBaseActivity
 * JD-Core Version:    0.6.2
 */