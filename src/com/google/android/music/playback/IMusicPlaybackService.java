package com.google.android.music.playback;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.IDownloadProgressListener;
import com.google.android.music.download.IDownloadProgressListener.Stub;
import com.google.android.music.medialist.SongList;
import com.google.android.music.mix.MixDescriptor;
import com.google.android.music.mix.MixGenerationState;

public abstract interface IMusicPlaybackService extends IInterface
{
  public abstract boolean addDownloadProgressListener(ContentIdentifier paramContentIdentifier, IDownloadProgressListener paramIDownloadProgressListener)
    throws RemoteException;

  public abstract void cancelMix()
    throws RemoteException;

  public abstract void clearQueue()
    throws RemoteException;

  public abstract void disableGroupPlay()
    throws RemoteException;

  public abstract long duration()
    throws RemoteException;

  public abstract String getAlbumArtUrl(long paramLong)
    throws RemoteException;

  public abstract long getAlbumId()
    throws RemoteException;

  public abstract String getAlbumName()
    throws RemoteException;

  public abstract long getArtistId()
    throws RemoteException;

  public abstract String getArtistName()
    throws RemoteException;

  public abstract ContentIdentifier getAudioId()
    throws RemoteException;

  public abstract int getAudioSessionId()
    throws RemoteException;

  public abstract TrackInfo getCurrentTrackInfo()
    throws RemoteException;

  public abstract int getErrorType()
    throws RemoteException;

  public abstract long getLastUserInteractionTime()
    throws RemoteException;

  public abstract SongList getMediaList()
    throws RemoteException;

  public abstract MixGenerationState getMixState()
    throws RemoteException;

  public abstract PlaybackState getPlaybackState()
    throws RemoteException;

  public abstract int getPreviewPlayType()
    throws RemoteException;

  public abstract int getQueuePosition()
    throws RemoteException;

  public abstract int getQueueSize()
    throws RemoteException;

  public abstract int getRating()
    throws RemoteException;

  public abstract int getRepeatMode()
    throws RemoteException;

  public abstract String getSelectedMediaRouteId()
    throws RemoteException;

  public abstract int getShuffleMode()
    throws RemoteException;

  public abstract String getSongStoreId()
    throws RemoteException;

  public abstract String getSortableAlbumArtistName()
    throws RemoteException;

  public abstract String getTrackName()
    throws RemoteException;

  public abstract boolean hasLocal()
    throws RemoteException;

  public abstract boolean hasRemote()
    throws RemoteException;

  public abstract boolean hasValidPlaylist()
    throws RemoteException;

  public abstract boolean isAlbumArtInService()
    throws RemoteException;

  public abstract boolean isCurrentSongLoaded()
    throws RemoteException;

  public abstract boolean isInErrorState()
    throws RemoteException;

  public abstract boolean isInFatalErrorState()
    throws RemoteException;

  public abstract boolean isInIniniteMixMode()
    throws RemoteException;

  public abstract boolean isPlaying()
    throws RemoteException;

  public abstract boolean isPlaylistLoading()
    throws RemoteException;

  public abstract boolean isPreparing()
    throws RemoteException;

  public abstract boolean isStreaming()
    throws RemoteException;

  public abstract boolean isStreamingFullyBuffered()
    throws RemoteException;

  public abstract void next()
    throws RemoteException;

  public abstract void open(SongList paramSongList, int paramInt, boolean paramBoolean)
    throws RemoteException;

  public abstract void openAndQueue(SongList paramSongList, int paramInt)
    throws RemoteException;

  public abstract void openMix(MixDescriptor paramMixDescriptor)
    throws RemoteException;

  public abstract void pause()
    throws RemoteException;

  public abstract void play()
    throws RemoteException;

  public abstract long position()
    throws RemoteException;

  public abstract void prev()
    throws RemoteException;

  public abstract void refreshRadio()
    throws RemoteException;

  public abstract void registerMusicCastMediaRouterCallback(IMusicCastMediaRouterCallback paramIMusicCastMediaRouterCallback)
    throws RemoteException;

  public abstract void removeDownloadProgressListener(IDownloadProgressListener paramIDownloadProgressListener)
    throws RemoteException;

  public abstract long seek(long paramLong)
    throws RemoteException;

  public abstract void setMediaRoute(boolean paramBoolean, String paramString)
    throws RemoteException;

  public abstract void setMediaRouteVolume(String paramString, double paramDouble)
    throws RemoteException;

  public abstract void setQueuePosition(int paramInt)
    throws RemoteException;

  public abstract void setRating(int paramInt)
    throws RemoteException;

  public abstract void setRepeatMode(int paramInt)
    throws RemoteException;

  public abstract void setShuffleMode(int paramInt)
    throws RemoteException;

  public abstract void setUIVisible(boolean paramBoolean)
    throws RemoteException;

  public abstract void shuffleAll()
    throws RemoteException;

  public abstract void shuffleOnDevice()
    throws RemoteException;

  public abstract void shuffleSongs(SongList paramSongList)
    throws RemoteException;

  public abstract void stop()
    throws RemoteException;

  public abstract boolean supportsRating()
    throws RemoteException;

  public abstract void updateMediaRouteVolume(String paramString, double paramDouble)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IMusicPlaybackService
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.music.playback.IMusicPlaybackService");
    }

    public static IMusicPlaybackService asInterface(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.music.playback.IMusicPlaybackService");
        if ((localIInterface != null) && ((localIInterface instanceof IMusicPlaybackService)))
          localObject = (IMusicPlaybackService)localIInterface;
        else
          localObject = new Proxy(paramIBinder);
      }
    }

    public IBinder asBinder()
    {
      return this;
    }

    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      int i = 0;
      boolean bool1 = true;
      switch (paramInt1)
      {
      default:
        bool1 = super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      case 1598968902:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 37:
      case 38:
      case 39:
      case 40:
      case 41:
      case 42:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      }
      while (true)
      {
        return bool1;
        paramParcel2.writeString("com.google.android.music.playback.IMusicPlaybackService");
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        if (paramParcel1.readInt() != 0);
        for (Object localObject1 = (SongList)SongList.CREATOR.createFromParcel(paramParcel1); ; localObject1 = null)
        {
          int k = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0)
            i = 1;
          open((SongList)localObject1, k, i);
          paramParcel2.writeNoException();
          break;
        }
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        if (paramParcel1.readInt() != 0);
        for (localObject1 = (SongList)SongList.CREATOR.createFromParcel(paramParcel1); ; localObject1 = null)
        {
          int m = paramParcel1.readInt();
          openAndQueue((SongList)localObject1, m);
          paramParcel2.writeNoException();
          break;
        }
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        clearQueue();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        int n = getQueuePosition();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(n);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        int i1 = paramParcel1.readInt();
        setQueuePosition(i1);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        int i2 = getQueueSize();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(i2);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        boolean bool2 = isPlaying();
        paramParcel2.writeNoException();
        if (bool2)
          i = 1;
        paramParcel2.writeInt(i);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        pause();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        play();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        stop();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        prev();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        next();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        boolean bool3 = isCurrentSongLoaded();
        paramParcel2.writeNoException();
        int j;
        if (bool3)
          j = 1;
        paramParcel2.writeInt(j);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        String str1 = getTrackName();
        paramParcel2.writeNoException();
        paramParcel2.writeString(str1);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        String str2 = getAlbumName();
        paramParcel2.writeNoException();
        paramParcel2.writeString(str2);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        long l1 = getAlbumId();
        paramParcel2.writeNoException();
        paramParcel2.writeLong(l1);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        String str3 = getArtistName();
        paramParcel2.writeNoException();
        paramParcel2.writeString(str3);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        long l2 = getArtistId();
        paramParcel2.writeNoException();
        paramParcel2.writeLong(l2);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        String str4 = getSortableAlbumArtistName();
        paramParcel2.writeNoException();
        paramParcel2.writeString(str4);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        String str5 = getSongStoreId();
        paramParcel2.writeNoException();
        paramParcel2.writeString(str5);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
        Object localObject2 = getMediaList();
        paramParcel2.writeNoException();
        if (localObject2 != null)
        {
          paramParcel2.writeInt(1);
          ((SongList)localObject2).writeToParcel(paramParcel2, 1);
        }
        else
        {
          paramParcel2.writeInt(0);
          continue;
          paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
          localObject2 = getAudioId();
          paramParcel2.writeNoException();
          if (localObject2 != null)
          {
            paramParcel2.writeInt(1);
            ((ContentIdentifier)localObject2).writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            boolean bool4 = supportsRating();
            paramParcel2.writeNoException();
            if (bool4)
              j = 1;
            paramParcel2.writeInt(j);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            int i3 = getRating();
            paramParcel2.writeNoException();
            paramParcel2.writeInt(i3);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            int i4 = paramParcel1.readInt();
            setRating(i4);
            paramParcel2.writeNoException();
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            long l3 = position();
            paramParcel2.writeNoException();
            paramParcel2.writeLong(l3);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            long l4 = duration();
            paramParcel2.writeNoException();
            paramParcel2.writeLong(l4);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            long l5 = paramParcel1.readLong();
            long l6 = seek(l5);
            paramParcel2.writeNoException();
            paramParcel2.writeLong(l6);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            int i5 = paramParcel1.readInt();
            setShuffleMode(i5);
            paramParcel2.writeNoException();
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            int i6 = getShuffleMode();
            paramParcel2.writeNoException();
            paramParcel2.writeInt(i6);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            int i7 = paramParcel1.readInt();
            setRepeatMode(i7);
            paramParcel2.writeNoException();
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            int i8 = getRepeatMode();
            paramParcel2.writeNoException();
            paramParcel2.writeInt(i8);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            boolean bool5 = isPreparing();
            paramParcel2.writeNoException();
            if (bool5)
              j = 1;
            paramParcel2.writeInt(j);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            boolean bool6 = isStreaming();
            paramParcel2.writeNoException();
            if (bool6)
              j = 1;
            paramParcel2.writeInt(j);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            boolean bool7 = isInErrorState();
            paramParcel2.writeNoException();
            if (bool7)
              j = 1;
            paramParcel2.writeInt(j);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            int i9 = getErrorType();
            paramParcel2.writeNoException();
            paramParcel2.writeInt(i9);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            boolean bool8 = isStreamingFullyBuffered();
            paramParcel2.writeNoException();
            if (bool8)
              j = 1;
            paramParcel2.writeInt(j);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            boolean bool9 = isInFatalErrorState();
            paramParcel2.writeNoException();
            if (bool9)
              j = 1;
            paramParcel2.writeInt(j);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            boolean bool10 = hasValidPlaylist();
            paramParcel2.writeNoException();
            if (bool10)
              j = 1;
            paramParcel2.writeInt(j);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            boolean bool11 = isPlaylistLoading();
            paramParcel2.writeNoException();
            if (bool11)
              j = 1;
            paramParcel2.writeInt(j);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            boolean bool12 = hasLocal();
            paramParcel2.writeNoException();
            if (bool12)
              j = 1;
            paramParcel2.writeInt(j);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            boolean bool13 = hasRemote();
            paramParcel2.writeNoException();
            if (bool13)
              j = 1;
            paramParcel2.writeInt(j);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            if (paramParcel1.readInt() != 0);
            for (localObject1 = (ContentIdentifier)ContentIdentifier.CREATOR.createFromParcel(paramParcel1); ; localObject1 = null)
            {
              IDownloadProgressListener localIDownloadProgressListener1 = IDownloadProgressListener.Stub.asInterface(paramParcel1.readStrongBinder());
              boolean bool14 = addDownloadProgressListener((ContentIdentifier)localObject1, localIDownloadProgressListener1);
              paramParcel2.writeNoException();
              if (bool14)
                j = 1;
              paramParcel2.writeInt(j);
              break;
            }
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            IDownloadProgressListener localIDownloadProgressListener2 = IDownloadProgressListener.Stub.asInterface(paramParcel1.readStrongBinder());
            removeDownloadProgressListener(localIDownloadProgressListener2);
            paramParcel2.writeNoException();
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            if (paramParcel1.readInt() != 0);
            for (localObject1 = null; ; localObject1 = null)
            {
              setUIVisible(()localObject1);
              paramParcel2.writeNoException();
              break;
            }
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            int i10 = getPreviewPlayType();
            paramParcel2.writeNoException();
            paramParcel2.writeInt(i10);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            int i11 = getAudioSessionId();
            paramParcel2.writeNoException();
            paramParcel2.writeInt(i11);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            boolean bool15 = isAlbumArtInService();
            paramParcel2.writeNoException();
            if (bool15)
              j = 1;
            paramParcel2.writeInt(j);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            long l7 = paramParcel1.readLong();
            String str6 = getAlbumArtUrl(l7);
            paramParcel2.writeNoException();
            paramParcel2.writeString(str6);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            String str7 = getSelectedMediaRouteId();
            paramParcel2.writeNoException();
            paramParcel2.writeString(str7);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            if (paramParcel1.readInt() != 0);
            for (localObject1 = null; ; localObject1 = null)
            {
              String str8 = paramParcel1.readString();
              setMediaRoute(()localObject1, str8);
              paramParcel2.writeNoException();
              break;
            }
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            String str9 = paramParcel1.readString();
            double d1 = paramParcel1.readDouble();
            setMediaRouteVolume(str9, d1);
            paramParcel2.writeNoException();
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            String str10 = paramParcel1.readString();
            double d2 = paramParcel1.readDouble();
            updateMediaRouteVolume(str10, d2);
            paramParcel2.writeNoException();
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            IMusicCastMediaRouterCallback localIMusicCastMediaRouterCallback = IMusicCastMediaRouterCallback.Stub.asInterface(paramParcel1.readStrongBinder());
            registerMusicCastMediaRouterCallback(localIMusicCastMediaRouterCallback);
            paramParcel2.writeNoException();
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            long l8 = getLastUserInteractionTime();
            paramParcel2.writeNoException();
            paramParcel2.writeLong(l8);
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            disableGroupPlay();
            paramParcel2.writeNoException();
            continue;
            paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
            localObject2 = getCurrentTrackInfo();
            paramParcel2.writeNoException();
            if (localObject2 != null)
            {
              paramParcel2.writeInt(1);
              ((TrackInfo)localObject2).writeToParcel(paramParcel2, 1);
            }
            else
            {
              paramParcel2.writeInt(0);
              continue;
              paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
              localObject2 = getPlaybackState();
              paramParcel2.writeNoException();
              if (localObject2 != null)
              {
                paramParcel2.writeInt(1);
                ((PlaybackState)localObject2).writeToParcel(paramParcel2, 1);
              }
              else
              {
                paramParcel2.writeInt(0);
                continue;
                paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
                if (paramParcel1.readInt() != 0);
                for (localObject1 = (MixDescriptor)MixDescriptor.CREATOR.createFromParcel(paramParcel1); ; localObject1 = null)
                {
                  openMix((MixDescriptor)localObject1);
                  paramParcel2.writeNoException();
                  break;
                }
                paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
                cancelMix();
                paramParcel2.writeNoException();
                continue;
                paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
                localObject2 = getMixState();
                paramParcel2.writeNoException();
                if (localObject2 != null)
                {
                  paramParcel2.writeInt(1);
                  ((MixGenerationState)localObject2).writeToParcel(paramParcel2, 1);
                }
                else
                {
                  paramParcel2.writeInt(0);
                  continue;
                  paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
                  boolean bool16 = isInIniniteMixMode();
                  paramParcel2.writeNoException();
                  if (bool16)
                    j = 1;
                  paramParcel2.writeInt(j);
                  continue;
                  paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
                  shuffleAll();
                  paramParcel2.writeNoException();
                  continue;
                  paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
                  shuffleOnDevice();
                  paramParcel2.writeNoException();
                  continue;
                  paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
                  if (paramParcel1.readInt() != 0);
                  for (localObject1 = (SongList)SongList.CREATOR.createFromParcel(paramParcel1); ; localObject1 = null)
                  {
                    shuffleSongs((SongList)localObject1);
                    paramParcel2.writeNoException();
                    break;
                  }
                  paramParcel1.enforceInterface("com.google.android.music.playback.IMusicPlaybackService");
                  refreshRadio();
                  paramParcel2.writeNoException();
                }
              }
            }
          }
        }
      }
    }

    private static class Proxy
      implements IMusicPlaybackService
    {
      private IBinder mRemote;

      Proxy(IBinder paramIBinder)
      {
        this.mRemote = paramIBinder;
      }

      public boolean addDownloadProgressListener(ContentIdentifier paramContentIdentifier, IDownloadProgressListener paramIDownloadProgressListener)
        throws RemoteException
      {
        boolean bool1 = true;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        while (true)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
            if (paramContentIdentifier != null)
            {
              localParcel1.writeInt(1);
              paramContentIdentifier.writeToParcel(localParcel1, 0);
              if (paramIDownloadProgressListener != null)
              {
                localIBinder = paramIDownloadProgressListener.asBinder();
                localParcel1.writeStrongBinder(localIBinder);
                boolean bool2 = this.mRemote.transact(43, localParcel1, localParcel2, 0);
                localParcel2.readException();
                int i = localParcel2.readInt();
                if (i == 0)
                  break label136;
                return bool1;
              }
            }
            else
            {
              localIBinder = null;
              localParcel1.writeInt(localIBinder);
              continue;
            }
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
          IBinder localIBinder = null;
          continue;
          label136: bool1 = false;
        }
      }

      public IBinder asBinder()
      {
        return this.mRemote;
      }

      public void cancelMix()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(60, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void clearQueue()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void disableGroupPlay()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(56, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public long duration()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l1 = localParcel2.readLong();
          long l2 = l1;
          return l2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public String getAlbumArtUrl(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          localParcel1.writeLong(paramLong);
          boolean bool = this.mRemote.transact(49, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str1 = localParcel2.readString();
          String str2 = str1;
          return str2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public long getAlbumId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l1 = localParcel2.readLong();
          long l2 = l1;
          return l2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public String getAlbumName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str1 = localParcel2.readString();
          String str2 = str1;
          return str2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public long getArtistId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l1 = localParcel2.readLong();
          long l2 = l1;
          return l2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public String getArtistName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str1 = localParcel2.readString();
          String str2 = str1;
          return str2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public ContentIdentifier getAudioId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0)
          {
            localContentIdentifier = (ContentIdentifier)ContentIdentifier.CREATOR.createFromParcel(localParcel2);
            return localContentIdentifier;
          }
          ContentIdentifier localContentIdentifier = null;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public int getAudioSessionId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          int j = i;
          return j;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public TrackInfo getCurrentTrackInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(57, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0)
          {
            localTrackInfo = (TrackInfo)TrackInfo.CREATOR.createFromParcel(localParcel2);
            return localTrackInfo;
          }
          TrackInfo localTrackInfo = null;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public int getErrorType()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          int j = i;
          return j;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public long getLastUserInteractionTime()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(55, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l1 = localParcel2.readLong();
          long l2 = l1;
          return l2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public SongList getMediaList()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0)
          {
            localSongList = (SongList)SongList.CREATOR.createFromParcel(localParcel2);
            return localSongList;
          }
          SongList localSongList = null;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public MixGenerationState getMixState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(61, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0)
          {
            localMixGenerationState = (MixGenerationState)MixGenerationState.CREATOR.createFromParcel(localParcel2);
            return localMixGenerationState;
          }
          MixGenerationState localMixGenerationState = null;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public PlaybackState getPlaybackState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(58, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0)
          {
            localPlaybackState = (PlaybackState)PlaybackState.CREATOR.createFromParcel(localParcel2);
            return localPlaybackState;
          }
          PlaybackState localPlaybackState = null;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public int getPreviewPlayType()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          int j = i;
          return j;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public int getQueuePosition()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          int j = i;
          return j;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public int getQueueSize()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          int j = i;
          return j;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public int getRating()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          int j = i;
          return j;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public int getRepeatMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          int j = i;
          return j;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public String getSelectedMediaRouteId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str1 = localParcel2.readString();
          String str2 = str1;
          return str2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public int getShuffleMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          int j = i;
          return j;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public String getSongStoreId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str1 = localParcel2.readString();
          String str2 = str1;
          return str2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public String getSortableAlbumArtistName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str1 = localParcel2.readString();
          String str2 = str1;
          return str2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public String getTrackName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str1 = localParcel2.readString();
          String str2 = str1;
          return str2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean hasLocal()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool2 = this.mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean hasRemote()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool2 = this.mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean hasValidPlaylist()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool2 = this.mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean isAlbumArtInService()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool2 = this.mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean isCurrentSongLoaded()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool2 = this.mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean isInErrorState()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool2 = this.mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean isInFatalErrorState()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool2 = this.mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean isInIniniteMixMode()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool2 = this.mRemote.transact(62, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean isPlaying()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool2 = this.mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean isPlaylistLoading()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool2 = this.mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean isPreparing()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool2 = this.mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean isStreaming()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool2 = this.mRemote.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean isStreamingFullyBuffered()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool2 = this.mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void next()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void open(SongList paramSongList, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        int i = 1;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        while (true)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
            if (paramSongList != null)
            {
              localParcel1.writeInt(1);
              paramSongList.writeToParcel(localParcel1, 0);
              localParcel1.writeInt(paramInt);
              if (paramBoolean)
              {
                localParcel1.writeInt(i);
                boolean bool = this.mRemote.transact(1, localParcel1, localParcel2, 0);
                localParcel2.readException();
              }
            }
            else
            {
              int j = 0;
              localParcel1.writeInt(j);
              continue;
            }
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
          i = 0;
        }
      }

      public void openAndQueue(SongList paramSongList, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          if (paramSongList != null)
          {
            localParcel1.writeInt(1);
            paramSongList.writeToParcel(localParcel1, 0);
          }
          while (true)
          {
            localParcel1.writeInt(paramInt);
            boolean bool = this.mRemote.transact(2, localParcel1, localParcel2, 0);
            localParcel2.readException();
            return;
            int i = 0;
            localParcel1.writeInt(i);
          }
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void openMix(MixDescriptor paramMixDescriptor)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          if (paramMixDescriptor != null)
          {
            localParcel1.writeInt(1);
            paramMixDescriptor.writeToParcel(localParcel1, 0);
          }
          while (true)
          {
            boolean bool = this.mRemote.transact(59, localParcel1, localParcel2, 0);
            localParcel2.readException();
            return;
            int i = 0;
            localParcel1.writeInt(i);
          }
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void pause()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void play()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public long position()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l1 = localParcel2.readLong();
          long l2 = l1;
          return l2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void prev()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void refreshRadio()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(66, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void registerMusicCastMediaRouterCallback(IMusicCastMediaRouterCallback paramIMusicCastMediaRouterCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          if (paramIMusicCastMediaRouterCallback != null)
          {
            localIBinder = paramIMusicCastMediaRouterCallback.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            boolean bool = this.mRemote.transact(54, localParcel1, localParcel2, 0);
            localParcel2.readException();
            return;
          }
          IBinder localIBinder = null;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void removeDownloadProgressListener(IDownloadProgressListener paramIDownloadProgressListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          if (paramIDownloadProgressListener != null)
          {
            localIBinder = paramIDownloadProgressListener.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            boolean bool = this.mRemote.transact(44, localParcel1, localParcel2, 0);
            localParcel2.readException();
            return;
          }
          IBinder localIBinder = null;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public long seek(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          localParcel1.writeLong(paramLong);
          boolean bool = this.mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l1 = localParcel2.readLong();
          long l2 = l1;
          return l2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void setMediaRoute(boolean paramBoolean, String paramString)
        throws RemoteException
      {
        int i = 0;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          if (paramBoolean)
            i = 1;
          localParcel1.writeInt(i);
          localParcel1.writeString(paramString);
          boolean bool = this.mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void setMediaRouteVolume(String paramString, double paramDouble)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          localParcel1.writeString(paramString);
          localParcel1.writeDouble(paramDouble);
          boolean bool = this.mRemote.transact(52, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void setQueuePosition(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          localParcel1.writeInt(paramInt);
          boolean bool = this.mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void setRating(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          localParcel1.writeInt(paramInt);
          boolean bool = this.mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void setRepeatMode(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          localParcel1.writeInt(paramInt);
          boolean bool = this.mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void setShuffleMode(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          localParcel1.writeInt(paramInt);
          boolean bool = this.mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void setUIVisible(boolean paramBoolean)
        throws RemoteException
      {
        int i = 0;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          if (paramBoolean)
            i = 1;
          localParcel1.writeInt(i);
          boolean bool = this.mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void shuffleAll()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(63, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void shuffleOnDevice()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(64, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void shuffleSongs(SongList paramSongList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          if (paramSongList != null)
          {
            localParcel1.writeInt(1);
            paramSongList.writeToParcel(localParcel1, 0);
          }
          while (true)
          {
            boolean bool = this.mRemote.transact(65, localParcel1, localParcel2, 0);
            localParcel2.readException();
            return;
            int i = 0;
            localParcel1.writeInt(i);
          }
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void stop()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool = this.mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean supportsRating()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          boolean bool2 = this.mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void updateMediaRouteVolume(String paramString, double paramDouble)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicPlaybackService");
          localParcel1.writeString(paramString);
          localParcel1.writeDouble(paramDouble);
          boolean bool = this.mRemote.transact(53, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.playback.IMusicPlaybackService
 * JD-Core Version:    0.6.2
 */