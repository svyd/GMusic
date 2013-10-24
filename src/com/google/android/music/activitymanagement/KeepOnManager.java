package com.google.android.music.activitymanagement;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import com.google.android.music.AvailableSpaceTracker;
import com.google.android.music.animator.Animator;
import com.google.android.music.animator.AnimatorListener;
import com.google.android.music.animator.AnimatorUpdateListener;
import com.google.android.music.medialist.AlbumSongList;
import com.google.android.music.medialist.AutoPlaylist;
import com.google.android.music.medialist.NautilusAlbumSongList;
import com.google.android.music.medialist.PlaylistSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.IStoreService;
import com.google.android.music.store.KeepOnUpdater;
import com.google.android.music.ui.UIStateManager;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;
import java.util.HashMap;
import java.util.Map;

public class KeepOnManager
{
  private static KeepOnManager sKeepOnManager;
  private AvailableSpaceTracker mAvailableSpaceTracker;
  private Map<Long, Boolean> mIsKeptAlbum;
  private Map<Long, Boolean> mIsKeptAutoPlaylist;
  private Map<Long, Boolean> mIsKeptPlaylist;
  private boolean mNeedToShowWillDownloadLaterDialogIfNecessary = false;
  private Map<Long, Animator> mUnwindAlbumAnination;
  private Map<Long, Animator> mUnwindAutoPlaylistAnination;
  private Map<Long, Animator> mUnwindPlaylistAnination;
  private boolean mWillDownloadLaterDialogIfNecessaryShown = false;

  private KeepOnManager()
  {
    HashMap localHashMap1 = new HashMap();
    this.mIsKeptAlbum = localHashMap1;
    HashMap localHashMap2 = new HashMap();
    this.mIsKeptPlaylist = localHashMap2;
    HashMap localHashMap3 = new HashMap();
    this.mIsKeptAutoPlaylist = localHashMap3;
    HashMap localHashMap4 = new HashMap();
    this.mUnwindAlbumAnination = localHashMap4;
    HashMap localHashMap5 = new HashMap();
    this.mUnwindPlaylistAnination = localHashMap5;
    HashMap localHashMap6 = new HashMap();
    this.mUnwindAutoPlaylistAnination = localHashMap6;
  }

  public static KeepOnManager getInstance()
  {
    MusicUtils.assertUiThread();
    if (sKeepOnManager == null)
      sKeepOnManager = new KeepOnManager();
    return sKeepOnManager;
  }

  private void initKeepOnViewStatusHelper(long paramLong1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt1, int paramInt2, KeepOnState paramKeepOnState, AnimatorUpdateListener paramAnimatorUpdateListener, long paramLong2, Map<Long, Boolean> paramMap, Map<Long, Animator> paramMap1)
  {
    MusicUtils.assertUiThread();
    if (paramAnimatorUpdateListener != null)
    {
      Long localLong1 = Long.valueOf(paramLong2);
      Animator localAnimator1 = (Animator)paramMap1.get(localLong1);
      if (localAnimator1 != null)
      {
        AnimatorUpdateListener localAnimatorUpdateListener1 = paramAnimatorUpdateListener;
        localAnimator1.removeUpdateListener(localAnimatorUpdateListener1);
      }
    }
    Long localLong2 = Long.valueOf(paramLong1);
    Boolean localBoolean1 = (Boolean)paramMap.get(localLong2);
    Animator localAnimator2;
    if (localBoolean1 == null)
    {
      Long localLong3 = Long.valueOf(paramLong1);
      Boolean localBoolean2 = Boolean.valueOf(paramBoolean2);
      Object localObject = paramMap.put(localLong3, localBoolean2);
      if (!paramBoolean1)
        break label272;
      paramKeepOnState.setPinnable(true);
      if (paramAnimatorUpdateListener == null)
        break label226;
      Long localLong4 = Long.valueOf(paramLong1);
      localAnimator2 = (Animator)paramMap1.get(localLong4);
      label133: if (localAnimator2 == null)
        break label232;
      AnimatorUpdateListener localAnimatorUpdateListener2 = paramAnimatorUpdateListener;
      localAnimator2.addUpdateListener(localAnimatorUpdateListener2);
      paramKeepOnState.setPinned(false);
      label157: paramKeepOnState.setCached(false);
      if ((paramInt2 <= 0) || (paramInt1 == -1))
        break label244;
      float f1 = paramInt1;
      float f2 = paramInt2;
      float f3 = Math.max(f1 / f2, 0.0F);
      paramKeepOnState.setDownloadFraction(f3);
    }
    while (true)
    {
      paramKeepOnState.invalidate();
      return;
      paramBoolean2 = localBoolean1.booleanValue();
      break;
      label226: localAnimator2 = null;
      break label133;
      label232: paramKeepOnState.setPinned(paramBoolean2);
      break label157;
      label244: if (paramBoolean2);
      for (float f4 = 0.0F; ; f4 = 0.0F)
      {
        paramKeepOnState.setDownloadFraction(f4);
        break;
      }
      label272: paramKeepOnState.setCached(false);
      paramKeepOnState.setPinned(false);
      paramKeepOnState.setDownloadFraction(1.0F);
      paramKeepOnState.setPinnable(false);
    }
  }

  private void toggleKeepOnHelper(int paramInt, long paramLong, KeepOnState paramKeepOnState, AnimatorUpdateListener paramAnimatorUpdateListener, Map<Long, Boolean> paramMap, Map<Long, Animator> paramMap1, Context paramContext)
  {
    MusicUtils.assertUiThread();
    Long localLong1 = Long.valueOf(paramLong);
    Boolean localBoolean = (Boolean)paramMap.get(localLong1);
    if (localBoolean == null)
      localBoolean = Boolean.valueOf(paramKeepOnState.isPinned());
    if (paramKeepOnState.isPinnable())
    {
      if (localBoolean.booleanValue())
      {
        float f = paramKeepOnState.getDownloadFraction();
        if (!localBoolean.booleanValue());
        for (boolean bool1 = true; ; bool1 = false)
        {
          paramKeepOnState.setPinned(bool1);
          KeepOnManager localKeepOnManager1 = this;
          int i = paramInt;
          long l1 = paramLong;
          Context localContext1 = paramContext;
          localKeepOnManager1.toggleKeepOnUpdaterForItem(i, l1, false, localContext1);
          if (paramAnimatorUpdateListener != null)
          {
            int j = (int)(3000.0F * f);
            Animator localAnimator1 = new Animator(j, f, 0.0F);
            Long localLong2 = Long.valueOf(paramLong);
            Object localObject = paramMap1.put(localLong2, localAnimator1);
            AnimatorUpdateListener localAnimatorUpdateListener1 = paramAnimatorUpdateListener;
            localAnimator1.addUpdateListener(localAnimatorUpdateListener1);
            Map<Long, Animator> localMap = paramMap1;
            KeepOnViewAnimatorListener localKeepOnViewAnimatorListener = new KeepOnViewAnimatorListener(localMap, paramLong);
            localAnimator1.addListener(localKeepOnViewAnimatorListener);
            localAnimator1.start();
          }
          paramKeepOnState.invalidate();
          return;
        }
      }
      UIStateManager.getInstance().getPrefs().setKeepOnDownloadPaused(false);
      if (!localBoolean.booleanValue());
      for (boolean bool2 = true; ; bool2 = false)
      {
        paramKeepOnState.setPinned(bool2);
        paramKeepOnState.setDownloadFraction(0.0F);
        KeepOnManager localKeepOnManager2 = this;
        int k = paramInt;
        long l2 = paramLong;
        Context localContext2 = paramContext;
        localKeepOnManager2.toggleKeepOnUpdaterForItem(k, l2, true, localContext2);
        if (paramAnimatorUpdateListener == null)
          break;
        Long localLong3 = Long.valueOf(paramLong);
        Animator localAnimator2 = (Animator)paramMap1.remove(localLong3);
        if (localAnimator2 == null)
          break;
        localAnimator2.cancel();
        AnimatorUpdateListener localAnimatorUpdateListener2 = paramAnimatorUpdateListener;
        localAnimator2.removeUpdateListener(localAnimatorUpdateListener2);
        break;
      }
    }
    Toast.makeText(paramContext, 2131231050, 1).show();
  }

  public static void toggleSonglistKeepOn(Context paramContext, SongList paramSongList, KeepOnState paramKeepOnState, AnimatorUpdateListener paramAnimatorUpdateListener)
  {
    KeepOnManager localKeepOnManager1 = getInstance();
    if (localKeepOnManager1 == null)
      return;
    if ((paramSongList instanceof AlbumSongList))
    {
      SongList localSongList1 = paramSongList;
      Context localContext1 = paramContext;
      long l1 = localSongList1.getAlbumId(localContext1);
      KeepOnState localKeepOnState1 = paramKeepOnState;
      AnimatorUpdateListener localAnimatorUpdateListener1 = paramAnimatorUpdateListener;
      Context localContext2 = paramContext;
      localKeepOnManager1.toggleAlbumKeepOn(l1, localKeepOnState1, localAnimatorUpdateListener1, localContext2);
    }
    while (true)
    {
      if (!paramKeepOnState.isPinnable())
        return;
      if (!paramKeepOnState.isPinned())
        return;
      Context localContext3 = paramContext;
      localKeepOnManager1.showWillDownloadLaterDialogIfNecessary(localContext3);
      return;
      if ((paramSongList instanceof PlaylistSongList))
      {
        long l2 = ((PlaylistSongList)paramSongList).getPlaylistId();
        KeepOnManager localKeepOnManager2 = localKeepOnManager1;
        KeepOnState localKeepOnState2 = paramKeepOnState;
        AnimatorUpdateListener localAnimatorUpdateListener2 = paramAnimatorUpdateListener;
        Context localContext4 = paramContext;
        localKeepOnManager2.togglePlaylistKeepOn(l2, localKeepOnState2, localAnimatorUpdateListener2, localContext4);
      }
      else if ((paramSongList instanceof AutoPlaylist))
      {
        long l3 = ((AutoPlaylist)paramSongList).getId();
        KeepOnManager localKeepOnManager3 = localKeepOnManager1;
        KeepOnState localKeepOnState3 = paramKeepOnState;
        AnimatorUpdateListener localAnimatorUpdateListener3 = paramAnimatorUpdateListener;
        Context localContext5 = paramContext;
        localKeepOnManager3.toggleAutoPlaylistKeepOn(l3, localKeepOnState3, localAnimatorUpdateListener3, localContext5);
      }
      else if ((paramSongList instanceof NautilusAlbumSongList))
      {
        SongList localSongList2 = paramSongList;
        final Context localContext6 = paramContext;
        final KeepOnManager localKeepOnManager4 = localKeepOnManager1;
        final KeepOnState localKeepOnState4 = paramKeepOnState;
        final AnimatorUpdateListener localAnimatorUpdateListener4 = paramAnimatorUpdateListener;
        MusicUtils.runAsyncWithCallback(new AsyncRunner()
        {
          long albumId = 65535L;

          public void backgroundTask()
          {
            SongList localSongList = KeepOnManager.this;
            Context localContext = localContext6;
            long l = localSongList.getAlbumId(localContext);
            this.albumId = l;
          }

          public void taskCompleted()
          {
            KeepOnManager localKeepOnManager = localKeepOnManager4;
            long l = this.albumId;
            KeepOnManager.KeepOnState localKeepOnState = localKeepOnState4;
            AnimatorUpdateListener localAnimatorUpdateListener = localAnimatorUpdateListener4;
            Context localContext = localContext6;
            localKeepOnManager.toggleAlbumKeepOn(l, localKeepOnState, localAnimatorUpdateListener, localContext);
          }
        });
      }
    }
  }

  public void initAlbumKeepOnViewStatus(long paramLong1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt1, int paramInt2, KeepOnState paramKeepOnState, AnimatorUpdateListener paramAnimatorUpdateListener, long paramLong2)
  {
    Map localMap1 = this.mIsKeptAlbum;
    Map localMap2 = this.mUnwindAlbumAnination;
    KeepOnManager localKeepOnManager = this;
    long l1 = paramLong1;
    boolean bool1 = paramBoolean1;
    boolean bool2 = paramBoolean2;
    boolean bool3 = paramBoolean3;
    int i = paramInt1;
    int j = paramInt2;
    KeepOnState localKeepOnState = paramKeepOnState;
    AnimatorUpdateListener localAnimatorUpdateListener = paramAnimatorUpdateListener;
    long l2 = paramLong2;
    localKeepOnManager.initKeepOnViewStatusHelper(l1, bool1, bool2, bool3, i, j, localKeepOnState, localAnimatorUpdateListener, l2, localMap1, localMap2);
  }

  public void initAutoPlaylistKeepOnViewStatus(long paramLong1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt1, int paramInt2, KeepOnState paramKeepOnState, AnimatorUpdateListener paramAnimatorUpdateListener, long paramLong2)
  {
    Map localMap1 = this.mIsKeptAutoPlaylist;
    Map localMap2 = this.mUnwindAutoPlaylistAnination;
    KeepOnManager localKeepOnManager = this;
    long l1 = paramLong1;
    boolean bool1 = paramBoolean1;
    boolean bool2 = paramBoolean2;
    boolean bool3 = paramBoolean3;
    int i = paramInt1;
    int j = paramInt2;
    KeepOnState localKeepOnState = paramKeepOnState;
    AnimatorUpdateListener localAnimatorUpdateListener = paramAnimatorUpdateListener;
    long l2 = paramLong2;
    localKeepOnManager.initKeepOnViewStatusHelper(l1, bool1, bool2, bool3, i, j, localKeepOnState, localAnimatorUpdateListener, l2, localMap1, localMap2);
  }

  public void initPlaylistKeepOnViewStatus(long paramLong1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt1, int paramInt2, KeepOnState paramKeepOnState, AnimatorUpdateListener paramAnimatorUpdateListener, long paramLong2)
  {
    Map localMap1 = this.mIsKeptPlaylist;
    Map localMap2 = this.mUnwindPlaylistAnination;
    KeepOnManager localKeepOnManager = this;
    long l1 = paramLong1;
    boolean bool1 = paramBoolean1;
    boolean bool2 = paramBoolean2;
    boolean bool3 = paramBoolean3;
    int i = paramInt1;
    int j = paramInt2;
    KeepOnState localKeepOnState = paramKeepOnState;
    AnimatorUpdateListener localAnimatorUpdateListener = paramAnimatorUpdateListener;
    long l2 = paramLong2;
    localKeepOnManager.initKeepOnViewStatusHelper(l1, bool1, bool2, bool3, i, j, localKeepOnState, localAnimatorUpdateListener, l2, localMap1, localMap2);
  }

  public void showWillDownloadLaterDialogIfNecessary(Context paramContext)
  {
    if (paramContext == null)
      return;
    NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    boolean bool1 = false;
    int i = 0;
    int j;
    label83: AlertDialog.Builder localBuilder1;
    if (localNetworkInfo != null)
    {
      bool1 = localNetworkInfo.isConnectedOrConnecting();
      if ((localNetworkInfo.getType() == 1) || (localNetworkInfo.getType() == 9))
        i = 1;
    }
    else
    {
      boolean bool2 = MusicPreferences.getMusicPreferences(paramContext, this).isOfflineDLOnlyOnWifi();
      MusicPreferences.releaseMusicPreferences(this);
      if ((bool1) && ((!bool2) || (i != 0)))
        break label162;
      j = 1;
      if (j == 0)
        return;
      localBuilder1 = new AlertDialog.Builder(paramContext);
      AlertDialog.Builder localBuilder2 = localBuilder1.setTitle(2131231049);
      if (!bool2)
        break label168;
    }
    label162: label168: for (int k = 2131231052; ; k = 2131231051)
    {
      AlertDialog.Builder localBuilder3 = localBuilder1.setMessage(k);
      AlertDialog.Builder localBuilder4 = localBuilder1.setCancelable(true);
      AlertDialog.Builder localBuilder5 = localBuilder1.setPositiveButton(2131230741, null);
      localBuilder1.create().show();
      return;
      i = 0;
      break;
      j = 0;
      break label83;
    }
  }

  public void toggleAlbumKeepOn(long paramLong, KeepOnState paramKeepOnState, AnimatorUpdateListener paramAnimatorUpdateListener, Context paramContext)
  {
    Map localMap1 = this.mIsKeptAlbum;
    Map localMap2 = this.mUnwindAlbumAnination;
    KeepOnManager localKeepOnManager = this;
    long l = paramLong;
    KeepOnState localKeepOnState = paramKeepOnState;
    AnimatorUpdateListener localAnimatorUpdateListener = paramAnimatorUpdateListener;
    Context localContext = paramContext;
    localKeepOnManager.toggleKeepOnHelper(0, l, localKeepOnState, localAnimatorUpdateListener, localMap1, localMap2, localContext);
  }

  public void toggleAutoPlaylistKeepOn(long paramLong, KeepOnState paramKeepOnState, AnimatorUpdateListener paramAnimatorUpdateListener, Context paramContext)
  {
    Map localMap1 = this.mIsKeptAutoPlaylist;
    Map localMap2 = this.mUnwindAutoPlaylistAnination;
    KeepOnManager localKeepOnManager = this;
    long l = paramLong;
    KeepOnState localKeepOnState = paramKeepOnState;
    AnimatorUpdateListener localAnimatorUpdateListener = paramAnimatorUpdateListener;
    Context localContext = paramContext;
    localKeepOnManager.toggleKeepOnHelper(2, l, localKeepOnState, localAnimatorUpdateListener, localMap1, localMap2, localContext);
  }

  public void toggleKeepOnUpdaterForItem(int paramInt, long paramLong, boolean paramBoolean, Context paramContext)
  {
    MusicUtils.assertUiThread();
    Map localMap = null;
    if (paramBoolean)
      switch (paramInt)
      {
      default:
        if ((this.mNeedToShowWillDownloadLaterDialogIfNecessary) && (!this.mWillDownloadLaterDialogIfNecessaryShown))
        {
          showWillDownloadLaterDialogIfNecessary(paramContext);
          this.mWillDownloadLaterDialogIfNecessaryShown = true;
        }
        break;
      case 0:
      case 1:
      case 2:
      }
    while (true)
    {
      if (localMap == null)
        return;
      Long localLong = Long.valueOf(paramLong);
      Boolean localBoolean = Boolean.valueOf(paramBoolean);
      Object localObject = localMap.put(localLong, localBoolean);
      return;
      localMap = this.mIsKeptAlbum;
      KeepOnUpdater.selectAlbumUpdateKeepOn(paramContext, paramLong);
      if (this.mAvailableSpaceTracker == null)
        break;
      this.mAvailableSpaceTracker.selectAlbum(paramLong);
      break;
      localMap = this.mIsKeptPlaylist;
      KeepOnUpdater.selectPlaylistUpdateKeepOn(paramContext, paramLong);
      if (this.mAvailableSpaceTracker == null)
        break;
      this.mAvailableSpaceTracker.selectPlaylist(paramLong);
      break;
      localMap = this.mIsKeptAutoPlaylist;
      KeepOnUpdater.selectAutoPlaylistUpdateKeepOn(paramContext, paramLong);
      if (this.mAvailableSpaceTracker == null)
        break;
      this.mAvailableSpaceTracker.selectAutoPlaylist(paramLong);
      break;
      switch (paramInt)
      {
      default:
        break;
      case 0:
        localMap = this.mIsKeptAlbum;
        KeepOnUpdater.deselectAlbumUpdateKeepOn(paramContext, paramLong);
        if (this.mAvailableSpaceTracker != null)
          this.mAvailableSpaceTracker.deselectAlbum(paramLong);
        break;
      case 1:
        localMap = this.mIsKeptPlaylist;
        KeepOnUpdater.deselectPlaylistUpdateKeepOn(paramContext, paramLong);
        if (this.mAvailableSpaceTracker != null)
          this.mAvailableSpaceTracker.deselectPlaylist(paramLong);
        break;
      case 2:
        localMap = this.mIsKeptAutoPlaylist;
        KeepOnUpdater.deselectAutoPlaylistUpdateKeepOn(paramContext, paramLong);
        if (this.mAvailableSpaceTracker != null)
          this.mAvailableSpaceTracker.deselectAutoPlaylist(paramLong);
        break;
      }
    }
  }

  public void togglePlaylistKeepOn(long paramLong, KeepOnState paramKeepOnState, AnimatorUpdateListener paramAnimatorUpdateListener, Context paramContext)
  {
    Map localMap1 = this.mIsKeptPlaylist;
    Map localMap2 = this.mUnwindPlaylistAnination;
    KeepOnManager localKeepOnManager = this;
    long l = paramLong;
    KeepOnState localKeepOnState = paramKeepOnState;
    AnimatorUpdateListener localAnimatorUpdateListener = paramAnimatorUpdateListener;
    Context localContext = paramContext;
    localKeepOnManager.toggleKeepOnHelper(1, l, localKeepOnState, localAnimatorUpdateListener, localMap1, localMap2, localContext);
  }

  public static abstract class UpdateKeepOnStatusTask
    implements AsyncRunner
  {
    private final Context mContext;
    private int mDownloadedSongCount = -1;
    private Boolean mHasRemote = null;
    private Boolean mIsAllLocal = null;
    private Boolean mIsKeptInDb = null;
    private int mKeepOnSoungCount = -1;
    private long mSavedLocalId = -2147483648L;
    private final SongList mSavedSongList;

    protected UpdateKeepOnStatusTask(Context paramContext, SongList paramSongList)
    {
      this.mContext = paramContext;
      this.mSavedSongList = paramSongList;
    }

    public void backgroundTask()
    {
      IStoreService localIStoreService = UIStateManager.getInstance().getStoreService();
      if (localIStoreService == null)
        return;
      if ((this.mSavedSongList instanceof AlbumSongList))
      {
        SongList localSongList1 = this.mSavedSongList;
        Context localContext1 = this.mContext;
        long l1 = localSongList1.getAlbumId(localContext1);
        this.mSavedLocalId = l1;
      }
      while (this.mSavedLocalId == -2147483648L)
      {
        return;
        if ((this.mSavedSongList instanceof PlaylistSongList))
        {
          long l2 = ((PlaylistSongList)this.mSavedSongList).getPlaylistId();
          this.mSavedLocalId = l2;
        }
        else if ((this.mSavedSongList instanceof AutoPlaylist))
        {
          long l3 = ((AutoPlaylist)this.mSavedSongList).getId();
          this.mSavedLocalId = l3;
        }
        else if ((this.mSavedSongList instanceof NautilusAlbumSongList))
        {
          SongList localSongList2 = this.mSavedSongList;
          Context localContext2 = this.mContext;
          long l4 = localSongList2.getAlbumId(localContext2);
          this.mSavedLocalId = l4;
        }
      }
      SongList localSongList3 = this.mSavedSongList;
      Context localContext3 = this.mContext;
      Boolean localBoolean1 = Boolean.valueOf(localSongList3.isSelectedForOfflineCaching(localContext3, localIStoreService));
      this.mIsKeptInDb = localBoolean1;
      SongList localSongList4 = this.mSavedSongList;
      Context localContext4 = this.mContext;
      Boolean localBoolean2 = Boolean.valueOf(localSongList4.containsRemoteItems(localContext4));
      this.mHasRemote = localBoolean2;
      SongList localSongList5 = this.mSavedSongList;
      Context localContext5 = this.mContext;
      Boolean localBoolean3 = Boolean.valueOf(localSongList5.isAllLocal(localContext5));
      this.mIsAllLocal = localBoolean3;
      SongList localSongList6 = this.mSavedSongList;
      Context localContext6 = this.mContext;
      int i = localSongList6.getDownloadedSongCount(localContext6);
      this.mDownloadedSongCount = i;
      SongList localSongList7 = this.mSavedSongList;
      Context localContext7 = this.mContext;
      int j = localSongList7.getKeepOnSongCount(localContext7);
      this.mKeepOnSoungCount = j;
    }

    protected abstract AnimatorUpdateListener getAnimatorUpdateListener();

    protected abstract SongList getCurrentSongList();

    protected abstract KeepOnManager.KeepOnState getKeepOnState();

    public void taskCompleted()
    {
      SongList localSongList1 = this.mSavedSongList;
      SongList localSongList2 = getCurrentSongList();
      if (localSongList1 != localSongList2)
        return;
      if (this.mSavedLocalId == -2147483648L)
        return;
      KeepOnManager localKeepOnManager = KeepOnManager.getInstance();
      if (this.mIsKeptInDb == null)
        return;
      if (this.mIsAllLocal == null)
        return;
      if ((this.mSavedSongList instanceof AlbumSongList))
      {
        long l1 = this.mSavedLocalId;
        boolean bool1 = this.mHasRemote.booleanValue();
        boolean bool2 = this.mIsKeptInDb.booleanValue();
        boolean bool3 = this.mIsAllLocal.booleanValue();
        int i = this.mDownloadedSongCount;
        int j = this.mKeepOnSoungCount;
        KeepOnManager.KeepOnState localKeepOnState1 = getKeepOnState();
        AnimatorUpdateListener localAnimatorUpdateListener1 = getAnimatorUpdateListener();
        localKeepOnManager.initAlbumKeepOnViewStatus(l1, bool1, bool2, bool3, i, j, localKeepOnState1, localAnimatorUpdateListener1, 65535L);
        return;
      }
      if ((this.mSavedSongList instanceof PlaylistSongList))
      {
        long l2 = this.mSavedLocalId;
        boolean bool4 = this.mHasRemote.booleanValue();
        boolean bool5 = this.mIsKeptInDb.booleanValue();
        boolean bool6 = this.mIsAllLocal.booleanValue();
        int k = this.mDownloadedSongCount;
        int m = this.mKeepOnSoungCount;
        KeepOnManager.KeepOnState localKeepOnState2 = getKeepOnState();
        AnimatorUpdateListener localAnimatorUpdateListener2 = getAnimatorUpdateListener();
        localKeepOnManager.initPlaylistKeepOnViewStatus(l2, bool4, bool5, bool6, k, m, localKeepOnState2, localAnimatorUpdateListener2, 65535L);
        return;
      }
      if ((this.mSavedSongList instanceof AutoPlaylist))
      {
        long l3 = this.mSavedLocalId;
        boolean bool7 = this.mHasRemote.booleanValue();
        boolean bool8 = this.mIsKeptInDb.booleanValue();
        boolean bool9 = this.mIsAllLocal.booleanValue();
        int n = this.mDownloadedSongCount;
        int i1 = this.mKeepOnSoungCount;
        KeepOnManager.KeepOnState localKeepOnState3 = getKeepOnState();
        AnimatorUpdateListener localAnimatorUpdateListener3 = getAnimatorUpdateListener();
        localKeepOnManager.initAutoPlaylistKeepOnViewStatus(l3, bool7, bool8, bool9, n, i1, localKeepOnState3, localAnimatorUpdateListener3, 65535L);
        return;
      }
      if (!(this.mSavedSongList instanceof NautilusAlbumSongList))
        return;
      long l4 = this.mSavedLocalId;
      boolean bool10 = this.mHasRemote.booleanValue();
      boolean bool11 = this.mIsKeptInDb.booleanValue();
      boolean bool12 = this.mIsAllLocal.booleanValue();
      int i2 = this.mDownloadedSongCount;
      int i3 = this.mKeepOnSoungCount;
      KeepOnManager.KeepOnState localKeepOnState4 = getKeepOnState();
      AnimatorUpdateListener localAnimatorUpdateListener4 = getAnimatorUpdateListener();
      localKeepOnManager.initAlbumKeepOnViewStatus(l4, bool10, bool11, bool12, i2, i3, localKeepOnState4, localAnimatorUpdateListener4, 65535L);
    }
  }

  private static class KeepOnViewAnimatorListener
    implements AnimatorListener
  {
    private long mKey;
    private Map<Long, Animator> mMap;

    public KeepOnViewAnimatorListener(Map<Long, Animator> paramMap, long paramLong)
    {
      this.mMap = paramMap;
      this.mKey = paramLong;
    }

    public void onAnimationEnd(Animator paramAnimator)
    {
      Map localMap = this.mMap;
      Long localLong = Long.valueOf(this.mKey);
      Object localObject = localMap.remove(localLong);
    }

    public void onAnimationRepeat(Animator paramAnimator)
    {
    }

    public void onAnimationStart(Animator paramAnimator)
    {
    }
  }

  public static abstract interface KeepOnState
  {
    public abstract float getDownloadFraction();

    public abstract void invalidate();

    public abstract boolean isPinnable();

    public abstract boolean isPinned();

    public abstract void setCached(boolean paramBoolean);

    public abstract void setDownloadFraction(float paramFloat);

    public abstract void setPinnable(boolean paramBoolean);

    public abstract void setPinned(boolean paramBoolean);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.activitymanagement.KeepOnManager
 * JD-Core Version:    0.6.2
 */