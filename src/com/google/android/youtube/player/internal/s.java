package com.google.android.youtube.player.internal;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.View;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;

public final class s
  implements YouTubePlayer
{
  private b a;
  private d b;

  public s(b paramb, d paramd)
  {
    b localb = (b)ac.a(paramb, "connectionClient cannot be null");
    this.a = localb;
    d locald = (d)ac.a(paramd, "embeddedPlayer cannot be null");
    this.b = locald;
  }

  public final View a()
  {
    try
    {
      View localView = (View)v.a(this.b.s());
      return localView;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final void a(Configuration paramConfiguration)
  {
    try
    {
      this.b.a(paramConfiguration);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final void a(boolean paramBoolean)
  {
    try
    {
      this.b.a(paramBoolean);
      this.a.a(paramBoolean);
      this.a.d();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final boolean a(int paramInt, KeyEvent paramKeyEvent)
  {
    try
    {
      boolean bool = this.b.a(paramInt, paramKeyEvent);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final boolean a(Bundle paramBundle)
  {
    try
    {
      boolean bool = this.b.a(paramBundle);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final void addFullscreenControlFlag(int paramInt)
  {
    try
    {
      this.b.d(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final void b()
  {
    try
    {
      this.b.m();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final void b(boolean paramBoolean)
  {
    try
    {
      this.b.e(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final boolean b(int paramInt, KeyEvent paramKeyEvent)
  {
    try
    {
      boolean bool = this.b.b(paramInt, paramKeyEvent);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final void c()
  {
    try
    {
      this.b.n();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final void d()
  {
    try
    {
      this.b.o();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final void e()
  {
    try
    {
      this.b.p();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final void f()
  {
    try
    {
      this.b.q();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final void g()
  {
    try
    {
      this.b.l();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final Bundle h()
  {
    try
    {
      Bundle localBundle = this.b.r();
      return localBundle;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final void loadVideo(String paramString)
  {
    loadVideo(paramString, 0);
  }

  public final void loadVideo(String paramString, int paramInt)
  {
    try
    {
      this.b.b(paramString, paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final void setPlayerStateChangeListener(final YouTubePlayer.PlayerStateChangeListener paramPlayerStateChangeListener)
  {
    try
    {
      d locald = this.b;
      g.a local3 = new g.a()
      {
        public final void a()
        {
          paramPlayerStateChangeListener.onLoading();
        }

        public final void a(String paramAnonymousString)
        {
          paramPlayerStateChangeListener.onLoaded(paramAnonymousString);
        }

        public final void b()
        {
          paramPlayerStateChangeListener.onAdStarted();
        }

        public final void b(String paramAnonymousString)
        {
          try
          {
            YouTubePlayer.ErrorReason localErrorReason1 = YouTubePlayer.ErrorReason.valueOf(paramAnonymousString);
            localErrorReason2 = localErrorReason1;
            paramPlayerStateChangeListener.onError(localErrorReason2);
            return;
          }
          catch (IllegalArgumentException localIllegalArgumentException)
          {
            while (true)
              localErrorReason2 = YouTubePlayer.ErrorReason.UNKNOWN;
          }
          catch (NullPointerException localNullPointerException)
          {
            while (true)
              YouTubePlayer.ErrorReason localErrorReason2 = YouTubePlayer.ErrorReason.UNKNOWN;
          }
        }

        public final void c()
        {
          paramPlayerStateChangeListener.onVideoStarted();
        }

        public final void d()
        {
          paramPlayerStateChangeListener.onVideoEnded();
        }
      };
      locald.a(local3);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }

  public final void setShowFullscreenButton(boolean paramBoolean)
  {
    try
    {
      this.b.c(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new q(localRemoteException);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.s
 * JD-Core Version:    0.6.2
 */