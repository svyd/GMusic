package com.google.android.music.download.artwork;

import com.google.android.common.base.Preconditions;
import java.util.Iterator;
import java.util.Set;

public class MissingArtHelper<ArtId>
{
  private final ArtDownloadServiceConnection<ArtId> mArtDownloadServiceConnection;
  private final ArtDownloadServiceConnection.ArtChangeListener<ArtId> mChangeListener;
  private Set<ArtId> mIds;

  public MissingArtHelper(ArtDownloadServiceConnection.ArtChangeListener<ArtId> paramArtChangeListener, ArtDownloadServiceConnection<ArtId> paramArtDownloadServiceConnection)
  {
    Object localObject1 = Preconditions.checkNotNull(paramArtChangeListener, "changeListener must not be null");
    Object localObject2 = Preconditions.checkNotNull(paramArtDownloadServiceConnection, "artConnection must not be null");
    this.mChangeListener = paramArtChangeListener;
    this.mArtDownloadServiceConnection = paramArtDownloadServiceConnection;
  }

  public void clear()
  {
    unregister();
    if (this.mIds == null)
      return;
    this.mIds.clear();
  }

  protected ArtDownloadServiceConnection.ArtChangeListener<ArtId> getChangeListener()
  {
    return this.mChangeListener;
  }

  protected boolean isEmpty()
  {
    if ((this.mIds == null) || (this.mIds.size() == 0));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void register()
  {
    if (this.mIds == null)
      return;
    if (this.mIds.isEmpty())
      return;
    this.mArtDownloadServiceConnection.register(this);
    Iterator localIterator = this.mIds.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      Object localObject = localIterator.next();
      ArtDownloadServiceConnection localArtDownloadServiceConnection = this.mArtDownloadServiceConnection;
      ArtDownloadServiceConnection.ArtChangeListener localArtChangeListener = this.mChangeListener;
      localArtDownloadServiceConnection.registerArtChangeListener(localObject, localArtChangeListener);
    }
  }

  public void removeId(ArtId paramArtId)
  {
    if (this.mIds == null)
      return;
    unregister();
    boolean bool = this.mIds.remove(paramArtId);
    register();
  }

  public void set(Set<ArtId> paramSet, boolean paramBoolean)
  {
    unregister();
    this.mIds = paramSet;
    if (!paramBoolean)
      return;
    register();
  }

  public void syncComplete()
  {
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str = super.toString();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str);
    StringBuilder localStringBuilder3 = localStringBuilder1.append("[");
    if (this.mIds != null)
    {
      int i = 1;
      Iterator localIterator = this.mIds.iterator();
      if (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        if (i != 0)
          i = 0;
        while (true)
        {
          StringBuilder localStringBuilder4 = localStringBuilder1.append(localObject);
          break;
          StringBuilder localStringBuilder5 = localStringBuilder1.append(", ");
        }
      }
    }
    StringBuilder localStringBuilder6 = localStringBuilder1.append("]");
    return localStringBuilder1.toString();
  }

  public void unregister()
  {
    if (this.mIds == null)
      return;
    if (this.mIds.isEmpty())
      return;
    this.mArtDownloadServiceConnection.unregister(this);
    Iterator localIterator = this.mIds.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      Object localObject = localIterator.next();
      ArtDownloadServiceConnection localArtDownloadServiceConnection = this.mArtDownloadServiceConnection;
      ArtDownloadServiceConnection.ArtChangeListener localArtChangeListener = this.mChangeListener;
      localArtDownloadServiceConnection.removeArtChangeListener(localObject, localArtChangeListener);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.artwork.MissingArtHelper
 * JD-Core Version:    0.6.2
 */