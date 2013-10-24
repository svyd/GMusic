package com.google.android.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.google.android.gsf.Gservices;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.playback.IMusicPlaybackService.Stub;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.UIStateManager;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.MusicUtils.ServiceToken;
import com.google.android.music.utils.async.AsyncWorkers;
import java.util.HashMap;
import java.util.Map;

public class RatingSelector extends FrameLayout
  implements ServiceConnection, View.OnClickListener
{
  private static final HashMap<Integer, Integer> NORMAL_ASSETS;
  private static final HashMap<Integer, Integer> TABLET_ASSETS = new HashMap();
  private final HashMap<Integer, Integer> mAssets;
  private Context mContext;
  private boolean mIsAttached = false;
  private int mRating;
  private Map<ImageView, Integer> mRatingsMap;
  private IMusicPlaybackService mService;
  private ImageView[] mStars;
  private View mStarsView;
  private ImageView mThumbsDown;
  private ImageView mThumbsUp;
  private View mThumbsView;
  private MusicUtils.ServiceToken mToken;
  private boolean mUseStars;

  static
  {
    NORMAL_ASSETS = new HashMap();
    HashMap localHashMap1 = TABLET_ASSETS;
    Integer localInteger1 = Integer.valueOf(2130837774);
    Integer localInteger2 = Integer.valueOf(2130837773);
    Object localObject1 = localHashMap1.put(localInteger1, localInteger2);
    HashMap localHashMap2 = TABLET_ASSETS;
    Integer localInteger3 = Integer.valueOf(2130837776);
    Integer localInteger4 = Integer.valueOf(2130837775);
    Object localObject2 = localHashMap2.put(localInteger3, localInteger4);
    HashMap localHashMap3 = TABLET_ASSETS;
    Integer localInteger5 = Integer.valueOf(2130837778);
    Integer localInteger6 = Integer.valueOf(2130837777);
    Object localObject3 = localHashMap3.put(localInteger5, localInteger6);
    HashMap localHashMap4 = TABLET_ASSETS;
    Integer localInteger7 = Integer.valueOf(2130837780);
    Integer localInteger8 = Integer.valueOf(2130837779);
    Object localObject4 = localHashMap4.put(localInteger7, localInteger8);
    HashMap localHashMap5 = NORMAL_ASSETS;
    Integer localInteger9 = Integer.valueOf(2130837780);
    Integer localInteger10 = Integer.valueOf(2130837780);
    Object localObject5 = localHashMap5.put(localInteger9, localInteger10);
    HashMap localHashMap6 = NORMAL_ASSETS;
    Integer localInteger11 = Integer.valueOf(2130837778);
    Integer localInteger12 = Integer.valueOf(2130837778);
    Object localObject6 = localHashMap6.put(localInteger11, localInteger12);
    HashMap localHashMap7 = NORMAL_ASSETS;
    Integer localInteger13 = Integer.valueOf(2130837776);
    Integer localInteger14 = Integer.valueOf(2130837776);
    Object localObject7 = localHashMap7.put(localInteger13, localInteger14);
    HashMap localHashMap8 = NORMAL_ASSETS;
    Integer localInteger15 = Integer.valueOf(2130837774);
    Integer localInteger16 = Integer.valueOf(2130837774);
    Object localObject8 = localHashMap8.put(localInteger15, localInteger16);
  }

  public RatingSelector(Context paramContext)
  {
    this(paramContext, null);
  }

  public RatingSelector(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    if (UIStateManager.getInstance().getPrefs().isTabletMusicExperience())
    {
      HashMap localHashMap1 = TABLET_ASSETS;
      this.mAssets = localHashMap1;
      return;
    }
    HashMap localHashMap2 = NORMAL_ASSETS;
    this.mAssets = localHashMap2;
  }

  private void changeRating(int paramInt)
  {
    if (this.mRating != paramInt);
    for (this.mRating = 0; ; this.mRating = paramInt)
    {
      LoggableHandler localLoggableHandler = AsyncWorkers.sUIBackgroundWorker;
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          if (RatingSelector.this.mService == null)
            return;
          try
          {
            IMusicPlaybackService localIMusicPlaybackService = RatingSelector.this.mService;
            int i = RatingSelector.this.mRating;
            localIMusicPlaybackService.setRating(i);
            return;
          }
          catch (RemoteException localRemoteException)
          {
            String str = localRemoteException.getMessage();
            int j = Log.e("RatingSelector", str, localRemoteException);
          }
        }
      };
      AsyncWorkers.runAsync(localLoggableHandler, local1);
      updateImages();
      return;
    }
  }

  public static int convertRatingToThumbs(int paramInt)
  {
    int i;
    switch (paramInt)
    {
    case 3:
    default:
      i = 0;
    case 1:
    case 2:
    case 4:
    case 5:
    }
    while (true)
    {
      return i;
      i = 1;
      continue;
      i = 5;
    }
  }

  private void prepButton(ImageView paramImageView, int paramInt, MusicPreferences paramMusicPreferences)
  {
    paramImageView.setOnClickListener(this);
    Context localContext = this.mContext;
    FadingColorDrawable localFadingColorDrawable = new FadingColorDrawable(localContext, paramImageView);
    paramImageView.setBackgroundDrawable(localFadingColorDrawable);
    if (paramMusicPreferences.isTabletMusicExperience())
      return;
    paramImageView.setPadding(paramInt, paramInt, paramInt, paramInt);
  }

  private void updateImages()
  {
    if (this.mThumbsView != null);
    int k;
    label131: ImageView localImageView3;
    switch (this.mRating)
    {
    case 3:
    default:
      ImageView localImageView1 = this.mThumbsDown;
      HashMap localHashMap1 = this.mAssets;
      Integer localInteger1 = Integer.valueOf(2130837776);
      int i = ((Integer)localHashMap1.get(localInteger1)).intValue();
      localImageView1.setImageResource(i);
      ImageView localImageView2 = this.mThumbsUp;
      HashMap localHashMap2 = this.mAssets;
      Integer localInteger2 = Integer.valueOf(2130837780);
      int j = ((Integer)localHashMap2.get(localInteger2)).intValue();
      localImageView2.setImageResource(j);
      if (this.mStarsView == null)
        return;
      k = 0;
      if (k >= 5)
        return;
      localImageView3 = this.mStars[k];
      if (this.mRating <= k)
        break;
    case 1:
    case 2:
    case 4:
    case 5:
    }
    for (int m = 2130837652; ; m = 2130837651)
    {
      localImageView3.setImageResource(m);
      k += 1;
      break label131;
      ImageView localImageView4 = this.mThumbsDown;
      HashMap localHashMap3 = this.mAssets;
      Integer localInteger3 = Integer.valueOf(2130837774);
      int n = ((Integer)localHashMap3.get(localInteger3)).intValue();
      localImageView4.setImageResource(n);
      ImageView localImageView5 = this.mThumbsUp;
      HashMap localHashMap4 = this.mAssets;
      Integer localInteger4 = Integer.valueOf(2130837780);
      int i1 = ((Integer)localHashMap4.get(localInteger4)).intValue();
      localImageView5.setImageResource(i1);
      break;
      ImageView localImageView6 = this.mThumbsDown;
      HashMap localHashMap5 = this.mAssets;
      Integer localInteger5 = Integer.valueOf(2130837776);
      int i2 = ((Integer)localHashMap5.get(localInteger5)).intValue();
      localImageView6.setImageResource(i2);
      ImageView localImageView7 = this.mThumbsUp;
      HashMap localHashMap6 = this.mAssets;
      Integer localInteger6 = Integer.valueOf(2130837778);
      int i3 = ((Integer)localHashMap6.get(localInteger6)).intValue();
      localImageView7.setImageResource(i3);
      break;
    }
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mIsAttached = true;
    MusicUtils.ServiceToken localServiceToken = MusicUtils.bindToService(getContext(), this);
    this.mToken = localServiceToken;
  }

  public void onClick(View paramView)
  {
    int i = ((Integer)this.mRatingsMap.get(paramView)).intValue();
    if (paramView != null)
    {
      changeRating(i);
      return;
    }
    String str = "Unknown view clicked on: " + paramView;
    throw new RuntimeException(str);
  }

  protected void onDetachedFromWindow()
  {
    if (this.mIsAttached)
    {
      this.mIsAttached = false;
      MusicUtils.unbindFromService(this.mToken);
    }
    super.onDetachedFromWindow();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    HashMap localHashMap = new HashMap();
    this.mRatingsMap = localHashMap;
    View localView1 = findViewById(2131296508);
    this.mThumbsView = localView1;
    ImageView localImageView1 = (ImageView)findViewById(2131296490);
    this.mThumbsUp = localImageView1;
    ImageView localImageView2 = (ImageView)findViewById(2131296509);
    this.mThumbsDown = localImageView2;
    int i = (int)getResources().getDimension(2131558425);
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this.mContext, this);
    try
    {
      ImageView localImageView3 = this.mThumbsUp;
      prepButton(localImageView3, i, localMusicPreferences);
      Map localMap1 = this.mRatingsMap;
      ImageView localImageView4 = this.mThumbsUp;
      Integer localInteger1 = Integer.valueOf(5);
      Object localObject1 = localMap1.put(localImageView4, localInteger1);
      ImageView localImageView5 = this.mThumbsDown;
      prepButton(localImageView5, i, localMusicPreferences);
      Map localMap2 = this.mRatingsMap;
      ImageView localImageView6 = this.mThumbsDown;
      Integer localInteger2 = Integer.valueOf(1);
      Object localObject2 = localMap2.put(localImageView6, localInteger2);
      View localView2 = findViewById(2131296502);
      this.mStarsView = localView2;
      ImageView[] arrayOfImageView1 = new ImageView[5];
      this.mStars = arrayOfImageView1;
      ImageView[] arrayOfImageView2 = this.mStars;
      ImageView localImageView7 = (ImageView)findViewById(2131296503);
      arrayOfImageView2[0] = localImageView7;
      ImageView[] arrayOfImageView3 = this.mStars;
      ImageView localImageView8 = (ImageView)findViewById(2131296504);
      arrayOfImageView3[1] = localImageView8;
      ImageView[] arrayOfImageView4 = this.mStars;
      ImageView localImageView9 = (ImageView)findViewById(2131296505);
      arrayOfImageView4[2] = localImageView9;
      ImageView[] arrayOfImageView5 = this.mStars;
      ImageView localImageView10 = (ImageView)findViewById(2131296506);
      arrayOfImageView5[3] = localImageView10;
      ImageView[] arrayOfImageView6 = this.mStars;
      ImageView localImageView11 = (ImageView)findViewById(2131296507);
      arrayOfImageView6[4] = localImageView11;
      int j = 0;
      while (j < 5)
      {
        ImageView localImageView12 = this.mStars[j];
        prepButton(localImageView12, 0, localMusicPreferences);
        Map localMap3 = this.mRatingsMap;
        ImageView localImageView13 = this.mStars[j];
        Integer localInteger3 = Integer.valueOf(j + 1);
        Object localObject3 = localMap3.put(localImageView13, localInteger3);
        j += 1;
      }
      MusicPreferences.releaseMusicPreferences(this);
      boolean bool = Gservices.getBoolean(this.mContext.getApplicationContext().getContentResolver(), "music_use_star_ratings", false);
      this.mUseStars = bool;
      if (this.mUseStars)
      {
        this.mStarsView.setVisibility(0);
        this.mThumbsView.setVisibility(8);
        return;
      }
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(this);
    }
    this.mThumbsView.setVisibility(0);
    this.mStarsView.setVisibility(8);
  }

  public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
  {
    IMusicPlaybackService localIMusicPlaybackService = IMusicPlaybackService.Stub.asInterface(paramIBinder);
    this.mService = localIMusicPlaybackService;
  }

  public void onServiceDisconnected(ComponentName paramComponentName)
  {
    this.mService = null;
  }

  public void setRating(int paramInt)
  {
    if (this.mUseStars);
    while (true)
    {
      this.mRating = paramInt;
      updateImages();
      return;
      paramInt = convertRatingToThumbs(paramInt);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.RatingSelector
 * JD-Core Version:    0.6.2
 */