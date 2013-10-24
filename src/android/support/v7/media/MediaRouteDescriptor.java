package android.support.v7.media;

import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class MediaRouteDescriptor
{
  private final Bundle mBundle;
  private List<IntentFilter> mControlFilters;

  private MediaRouteDescriptor(Bundle paramBundle, List<IntentFilter> paramList)
  {
    this.mBundle = paramBundle;
    this.mControlFilters = paramList;
  }

  private void ensureControlFilters()
  {
    if (this.mControlFilters != null)
      return;
    ArrayList localArrayList = this.mBundle.getParcelableArrayList("controlFilters");
    this.mControlFilters = localArrayList;
    if (this.mControlFilters != null)
      return;
    List localList = Collections.emptyList();
    this.mControlFilters = localList;
  }

  public static MediaRouteDescriptor fromBundle(Bundle paramBundle)
  {
    if (paramBundle != null);
    for (MediaRouteDescriptor localMediaRouteDescriptor = new MediaRouteDescriptor(paramBundle, null); ; localMediaRouteDescriptor = null)
      return localMediaRouteDescriptor;
  }

  public Bundle asBundle()
  {
    return this.mBundle;
  }

  public List<IntentFilter> getControlFilters()
  {
    ensureControlFilters();
    return this.mControlFilters;
  }

  public String getDescription()
  {
    return this.mBundle.getString("status");
  }

  public Bundle getExtras()
  {
    return this.mBundle.getBundle("extras");
  }

  public String getId()
  {
    return this.mBundle.getString("id");
  }

  public String getName()
  {
    return this.mBundle.getString("name");
  }

  public int getPlaybackStream()
  {
    return this.mBundle.getInt("playbackStream", -1);
  }

  public int getPlaybackType()
  {
    return this.mBundle.getInt("playbackType", 1);
  }

  public int getPresentationDisplayId()
  {
    return this.mBundle.getInt("presentationDisplayId", -1);
  }

  public int getVolume()
  {
    return this.mBundle.getInt("volume");
  }

  public int getVolumeHandling()
  {
    return this.mBundle.getInt("volumeHandling", 0);
  }

  public int getVolumeMax()
  {
    return this.mBundle.getInt("volumeMax");
  }

  public boolean isConnecting()
  {
    return this.mBundle.getBoolean("connecting", false);
  }

  public boolean isEnabled()
  {
    return this.mBundle.getBoolean("enabled", true);
  }

  public boolean isValid()
  {
    ensureControlFilters();
    if ((TextUtils.isEmpty(getId())) || (TextUtils.isEmpty(getName())) || (this.mControlFilters.contains(null)));
    for (boolean bool = false; ; bool = true)
      return bool;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("MediaRouteDescriptor{ ");
    StringBuilder localStringBuilder3 = localStringBuilder1.append("id=");
    String str1 = getId();
    StringBuilder localStringBuilder4 = localStringBuilder3.append(str1);
    StringBuilder localStringBuilder5 = localStringBuilder1.append(", name=");
    String str2 = getName();
    StringBuilder localStringBuilder6 = localStringBuilder5.append(str2);
    StringBuilder localStringBuilder7 = localStringBuilder1.append(", description=");
    String str3 = getDescription();
    StringBuilder localStringBuilder8 = localStringBuilder7.append(str3);
    StringBuilder localStringBuilder9 = localStringBuilder1.append(", isEnabled=");
    boolean bool1 = isEnabled();
    StringBuilder localStringBuilder10 = localStringBuilder9.append(bool1);
    StringBuilder localStringBuilder11 = localStringBuilder1.append(", isConnecting=");
    boolean bool2 = isConnecting();
    StringBuilder localStringBuilder12 = localStringBuilder11.append(bool2);
    StringBuilder localStringBuilder13 = localStringBuilder1.append(", controlFilters=");
    String str4 = Arrays.toString(getControlFilters().toArray());
    StringBuilder localStringBuilder14 = localStringBuilder13.append(str4);
    StringBuilder localStringBuilder15 = localStringBuilder1.append(", playbackType=");
    int i = getPlaybackType();
    StringBuilder localStringBuilder16 = localStringBuilder15.append(i);
    StringBuilder localStringBuilder17 = localStringBuilder1.append(", playbackStream=");
    int j = getPlaybackStream();
    StringBuilder localStringBuilder18 = localStringBuilder17.append(j);
    StringBuilder localStringBuilder19 = localStringBuilder1.append(", volume=");
    int k = getVolume();
    StringBuilder localStringBuilder20 = localStringBuilder19.append(k);
    StringBuilder localStringBuilder21 = localStringBuilder1.append(", volumeMax=");
    int m = getVolumeMax();
    StringBuilder localStringBuilder22 = localStringBuilder21.append(m);
    StringBuilder localStringBuilder23 = localStringBuilder1.append(", volumeHandling=");
    int n = getVolumeHandling();
    StringBuilder localStringBuilder24 = localStringBuilder23.append(n);
    StringBuilder localStringBuilder25 = localStringBuilder1.append(", presentationDisplayId=");
    int i1 = getPresentationDisplayId();
    StringBuilder localStringBuilder26 = localStringBuilder25.append(i1);
    StringBuilder localStringBuilder27 = localStringBuilder1.append(", extras=");
    Bundle localBundle = getExtras();
    StringBuilder localStringBuilder28 = localStringBuilder27.append(localBundle);
    StringBuilder localStringBuilder29 = localStringBuilder1.append(", isValid=");
    boolean bool3 = isValid();
    StringBuilder localStringBuilder30 = localStringBuilder29.append(bool3);
    StringBuilder localStringBuilder31 = localStringBuilder1.append(" }");
    return localStringBuilder1.toString();
  }

  public static final class Builder
  {
    private final Bundle mBundle;
    private ArrayList<IntentFilter> mControlFilters;

    public Builder(MediaRouteDescriptor paramMediaRouteDescriptor)
    {
      if (paramMediaRouteDescriptor == null)
        throw new IllegalArgumentException("descriptor must not be null");
      Bundle localBundle1 = paramMediaRouteDescriptor.mBundle;
      Bundle localBundle2 = new Bundle(localBundle1);
      this.mBundle = localBundle2;
      paramMediaRouteDescriptor.ensureControlFilters();
      if (paramMediaRouteDescriptor.mControlFilters.isEmpty())
        return;
      List localList = paramMediaRouteDescriptor.mControlFilters;
      ArrayList localArrayList = new ArrayList(localList);
      this.mControlFilters = localArrayList;
    }

    public Builder(String paramString1, String paramString2)
    {
      Bundle localBundle = new Bundle();
      this.mBundle = localBundle;
      Builder localBuilder1 = setId(paramString1);
      Builder localBuilder2 = setName(paramString2);
    }

    public Builder addControlFilter(IntentFilter paramIntentFilter)
    {
      if (paramIntentFilter == null)
        throw new IllegalArgumentException("filter must not be null");
      if (this.mControlFilters == null)
      {
        ArrayList localArrayList = new ArrayList();
        this.mControlFilters = localArrayList;
      }
      if (!this.mControlFilters.contains(paramIntentFilter))
        boolean bool = this.mControlFilters.add(paramIntentFilter);
      return this;
    }

    public Builder addControlFilters(Collection<IntentFilter> paramCollection)
    {
      if (paramCollection == null)
        throw new IllegalArgumentException("filters must not be null");
      if (!paramCollection.isEmpty())
      {
        Iterator localIterator = paramCollection.iterator();
        while (localIterator.hasNext())
        {
          IntentFilter localIntentFilter = (IntentFilter)localIterator.next();
          Builder localBuilder = addControlFilter(localIntentFilter);
        }
      }
      return this;
    }

    public MediaRouteDescriptor build()
    {
      if (this.mControlFilters != null)
      {
        Bundle localBundle1 = this.mBundle;
        ArrayList localArrayList1 = this.mControlFilters;
        localBundle1.putParcelableArrayList("controlFilters", localArrayList1);
      }
      Bundle localBundle2 = this.mBundle;
      ArrayList localArrayList2 = this.mControlFilters;
      return new MediaRouteDescriptor(localBundle2, localArrayList2, null);
    }

    public Builder setConnecting(boolean paramBoolean)
    {
      this.mBundle.putBoolean("connecting", paramBoolean);
      return this;
    }

    public Builder setDescription(String paramString)
    {
      this.mBundle.putString("status", paramString);
      return this;
    }

    public Builder setEnabled(boolean paramBoolean)
    {
      this.mBundle.putBoolean("enabled", paramBoolean);
      return this;
    }

    public Builder setId(String paramString)
    {
      this.mBundle.putString("id", paramString);
      return this;
    }

    public Builder setName(String paramString)
    {
      this.mBundle.putString("name", paramString);
      return this;
    }

    public Builder setPlaybackStream(int paramInt)
    {
      this.mBundle.putInt("playbackStream", paramInt);
      return this;
    }

    public Builder setPlaybackType(int paramInt)
    {
      this.mBundle.putInt("playbackType", paramInt);
      return this;
    }

    public Builder setPresentationDisplayId(int paramInt)
    {
      this.mBundle.putInt("presentationDisplayId", paramInt);
      return this;
    }

    public Builder setVolume(int paramInt)
    {
      this.mBundle.putInt("volume", paramInt);
      return this;
    }

    public Builder setVolumeHandling(int paramInt)
    {
      this.mBundle.putInt("volumeHandling", paramInt);
      return this;
    }

    public Builder setVolumeMax(int paramInt)
    {
      this.mBundle.putInt("volumeMax", paramInt);
      return this;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.MediaRouteDescriptor
 * JD-Core Version:    0.6.2
 */