package com.google.android.youtube.player;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import com.google.android.youtube.player.internal.ab;
import com.google.android.youtube.player.internal.ac;
import com.google.android.youtube.player.internal.b;
import com.google.android.youtube.player.internal.n;
import com.google.android.youtube.player.internal.s;
import com.google.android.youtube.player.internal.t.a;
import com.google.android.youtube.player.internal.t.b;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class YouTubePlayerView extends ViewGroup
  implements YouTubePlayer.Provider
{
  private final a a;
  private final Set<View> b;
  private final b c;
  private b d;
  private s e;
  private View f;
  private n g;
  private YouTubePlayer.Provider h;
  private Bundle i;
  private YouTubePlayer.OnInitializedListener j;
  private boolean k;

  public YouTubePlayerView(Context paramContext)
  {
    this(paramContext, null);
  }

  public YouTubePlayerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public YouTubePlayerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, localb);
  }

  YouTubePlayerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt, b paramb)
  {
    super(localContext, paramAttributeSet, paramInt);
    b localb = (b)ac.a(paramb, "listener cannot be null");
    this.c = localb;
    if (getBackground() == null)
      setBackgroundColor(-16777216);
    setClipToPadding(false);
    n localn1 = new n(paramContext);
    this.g = localn1;
    n localn2 = this.g;
    requestTransparentRegion(localn2);
    n localn3 = this.g;
    addView(localn3);
    HashSet localHashSet = new HashSet();
    this.b = localHashSet;
    a locala = new a((byte)0);
    this.a = locala;
  }

  private void a(View paramView)
  {
    n localn = this.g;
    if (paramView != localn)
    {
      if (this.e == null)
        break label46;
      View localView = this.f;
      if (paramView != localView)
        break label46;
    }
    label46: for (int m = 1; ; m = 0)
    {
      if (m != 0)
        return;
      throw new UnsupportedOperationException("No views can be added on top of the player");
    }
  }

  private void a(YouTubeInitializationResult paramYouTubeInitializationResult)
  {
    this.e = null;
    this.g.c();
    if (this.j == null)
      return;
    YouTubePlayer.OnInitializedListener localOnInitializedListener = this.j;
    YouTubePlayer.Provider localProvider = this.h;
    localOnInitializedListener.onInitializationFailure(localProvider, paramYouTubeInitializationResult);
    this.j = null;
  }

  final void a()
  {
    if (this.e == null)
      return;
    this.e.b();
  }

  final void a(final Activity paramActivity, YouTubePlayer.Provider paramProvider, String paramString, YouTubePlayer.OnInitializedListener paramOnInitializedListener, Bundle paramBundle)
  {
    if (this.e != null)
      return;
    if (this.j != null)
      return;
    Object localObject = ac.a(paramActivity, "activity cannot be null");
    YouTubePlayer.Provider localProvider = (YouTubePlayer.Provider)ac.a(paramProvider, "provider cannot be null");
    this.h = localProvider;
    YouTubePlayer.OnInitializedListener localOnInitializedListener = (YouTubePlayer.OnInitializedListener)ac.a(paramOnInitializedListener, "listener cannot be null");
    this.j = localOnInitializedListener;
    this.i = paramBundle;
    this.g.b();
    ab localab = ab.a();
    Context localContext = getContext();
    t.a local1 = new t.a()
    {
      public final void a()
      {
        if (YouTubePlayerView.a(YouTubePlayerView.this) != null)
        {
          YouTubePlayerView localYouTubePlayerView = YouTubePlayerView.this;
          Activity localActivity = paramActivity;
          YouTubePlayerView.a(localYouTubePlayerView, localActivity);
        }
        b localb = YouTubePlayerView.b(YouTubePlayerView.this);
      }

      public final void b()
      {
        if ((!YouTubePlayerView.c(YouTubePlayerView.this)) && (YouTubePlayerView.d(YouTubePlayerView.this) != null))
          YouTubePlayerView.d(YouTubePlayerView.this).f();
        YouTubePlayerView.e(YouTubePlayerView.this).a();
        YouTubePlayerView localYouTubePlayerView1 = YouTubePlayerView.this;
        n localn1 = YouTubePlayerView.e(YouTubePlayerView.this);
        if (localYouTubePlayerView1.indexOfChild(localn1) < 0)
        {
          YouTubePlayerView localYouTubePlayerView2 = YouTubePlayerView.this;
          n localn2 = YouTubePlayerView.e(YouTubePlayerView.this);
          localYouTubePlayerView2.addView(localn2);
          YouTubePlayerView localYouTubePlayerView3 = YouTubePlayerView.this;
          View localView1 = YouTubePlayerView.f(YouTubePlayerView.this);
          localYouTubePlayerView3.removeView(localView1);
        }
        View localView2 = YouTubePlayerView.g(YouTubePlayerView.this);
        s locals = YouTubePlayerView.h(YouTubePlayerView.this);
        b localb = YouTubePlayerView.b(YouTubePlayerView.this);
      }
    };
    t.b local2 = new t.b()
    {
      public final void a(YouTubeInitializationResult paramAnonymousYouTubeInitializationResult)
      {
        YouTubePlayerView.a(YouTubePlayerView.this, paramAnonymousYouTubeInitializationResult);
        b localb = YouTubePlayerView.b(YouTubePlayerView.this);
      }
    };
    b localb = localab.a(localContext, paramString, local1, local2);
    this.d = localb;
    this.d.e();
  }

  final void a(boolean paramBoolean)
  {
    if (this.e == null)
      return;
    this.e.b(paramBoolean);
    b(paramBoolean);
  }

  public final void addFocusables(ArrayList<View> paramArrayList, int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    super.addFocusables(localArrayList, paramInt);
    boolean bool1 = paramArrayList.addAll(localArrayList);
    this.b.clear();
    boolean bool2 = this.b.addAll(localArrayList);
  }

  public final void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    ArrayList localArrayList = new ArrayList();
    super.addFocusables(localArrayList, paramInt1, paramInt2);
    boolean bool1 = paramArrayList.addAll(localArrayList);
    this.b.clear();
    boolean bool2 = this.b.addAll(localArrayList);
  }

  public final void addView(View paramView)
  {
    a(paramView);
    super.addView(paramView);
  }

  public final void addView(View paramView, int paramInt)
  {
    a(paramView);
    super.addView(paramView, paramInt);
  }

  public final void addView(View paramView, int paramInt1, int paramInt2)
  {
    a(paramView);
    super.addView(paramView, paramInt1, paramInt2);
  }

  public final void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    a(paramView);
    super.addView(paramView, paramInt, paramLayoutParams);
  }

  public final void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    a(paramView);
    super.addView(paramView, paramLayoutParams);
  }

  final void b()
  {
    if (this.e == null)
      return;
    this.e.c();
  }

  final void b(boolean paramBoolean)
  {
    this.k = true;
    if (this.e == null)
      return;
    this.e.a(paramBoolean);
  }

  final void c()
  {
    if (this.e == null)
      return;
    this.e.d();
  }

  public final void clearChildFocus(View paramView)
  {
    if (hasFocusable())
    {
      boolean bool = requestFocus();
      return;
    }
    super.clearChildFocus(paramView);
  }

  final void d()
  {
    if (this.e == null)
      return;
    this.e.e();
  }

  public final boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    boolean bool = false;
    if (this.e != null)
      if (paramKeyEvent.getAction() == 0)
      {
        s locals1 = this.e;
        int m = paramKeyEvent.getKeyCode();
        if ((locals1.a(m, paramKeyEvent)) || (super.dispatchKeyEvent(paramKeyEvent)))
          bool = true;
      }
    while (true)
    {
      return bool;
      if (paramKeyEvent.getAction() == 1)
      {
        s locals2 = this.e;
        int n = paramKeyEvent.getKeyCode();
        if ((locals2.b(n, paramKeyEvent)) || (super.dispatchKeyEvent(paramKeyEvent)))
          bool = true;
      }
      else
      {
        bool = super.dispatchKeyEvent(paramKeyEvent);
      }
    }
  }

  final Bundle e()
  {
    if (this.e == null);
    for (Bundle localBundle = this.i; ; localBundle = this.e.h())
      return localBundle;
  }

  public final void focusableViewAvailable(View paramView)
  {
    super.focusableViewAvailable(paramView);
    boolean bool = this.b.add(paramView);
  }

  public final void initialize(String paramString, YouTubePlayer.OnInitializedListener paramOnInitializedListener)
  {
    String str = ac.a(paramString, "Developer key cannot be null or empty");
    this.c.a(this, paramString, paramOnInitializedListener);
  }

  protected final void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
    a locala = this.a;
    localViewTreeObserver.addOnGlobalFocusChangeListener(locala);
  }

  public final void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    if (this.e == null)
      return;
    this.e.a(paramConfiguration);
  }

  protected final void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
    a locala = this.a;
    localViewTreeObserver.removeOnGlobalFocusChangeListener(locala);
  }

  protected final void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (getChildCount() <= 0)
      return;
    View localView = getChildAt(0);
    int m = paramInt3 - paramInt1;
    int n = paramInt4 - paramInt2;
    localView.layout(0, 0, m, n);
  }

  protected final void onMeasure(int paramInt1, int paramInt2)
  {
    if (getChildCount() > 0)
    {
      View localView = getChildAt(0);
      localView.measure(paramInt1, paramInt2);
      int m = localView.getMeasuredWidth();
      int n = localView.getMeasuredHeight();
      setMeasuredDimension(m, n);
      return;
    }
    setMeasuredDimension(0, 0);
  }

  public final boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = super.onTouchEvent(paramMotionEvent);
    return true;
  }

  public final void requestChildFocus(View paramView1, View paramView2)
  {
    super.requestChildFocus(paramView1, paramView2);
    boolean bool = this.b.add(paramView2);
  }

  public final void setClipToPadding(boolean paramBoolean)
  {
  }

  public final void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
  }

  private final class a
    implements ViewTreeObserver.OnGlobalFocusChangeListener
  {
    private a()
    {
    }

    public final void onGlobalFocusChanged(View paramView1, View paramView2)
    {
      if (YouTubePlayerView.d(YouTubePlayerView.this) == null)
        return;
      if (!YouTubePlayerView.i(YouTubePlayerView.this).contains(paramView2))
        return;
      if (YouTubePlayerView.i(YouTubePlayerView.this).contains(paramView1))
        return;
      YouTubePlayerView.d(YouTubePlayerView.this).g();
    }
  }

  static abstract interface b
  {
    public abstract void a(YouTubePlayerView paramYouTubePlayerView);

    public abstract void a(YouTubePlayerView paramYouTubePlayerView, String paramString, YouTubePlayer.OnInitializedListener paramOnInitializedListener);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.YouTubePlayerView
 * JD-Core Version:    0.6.2
 */