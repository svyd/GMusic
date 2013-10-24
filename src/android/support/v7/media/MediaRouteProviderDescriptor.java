package android.support.v7.media;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class MediaRouteProviderDescriptor
{
  private final Bundle mBundle;
  private List<MediaRouteDescriptor> mRoutes;

  private MediaRouteProviderDescriptor(Bundle paramBundle, List<MediaRouteDescriptor> paramList)
  {
    this.mBundle = paramBundle;
    this.mRoutes = paramList;
  }

  private void ensureRoutes()
  {
    if (this.mRoutes != null)
      return;
    ArrayList localArrayList1 = this.mBundle.getParcelableArrayList("routes");
    if ((localArrayList1 == null) || (localArrayList1.isEmpty()))
    {
      List localList1 = Collections.emptyList();
      this.mRoutes = localList1;
      return;
    }
    int i = localArrayList1.size();
    ArrayList localArrayList2 = new ArrayList(i);
    this.mRoutes = localArrayList2;
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      List localList2 = this.mRoutes;
      MediaRouteDescriptor localMediaRouteDescriptor = MediaRouteDescriptor.fromBundle((Bundle)localArrayList1.get(j));
      boolean bool = localList2.add(localMediaRouteDescriptor);
      j += 1;
    }
  }

  public static MediaRouteProviderDescriptor fromBundle(Bundle paramBundle)
  {
    if (paramBundle != null);
    for (MediaRouteProviderDescriptor localMediaRouteProviderDescriptor = new MediaRouteProviderDescriptor(paramBundle, null); ; localMediaRouteProviderDescriptor = null)
      return localMediaRouteProviderDescriptor;
  }

  public Bundle asBundle()
  {
    return this.mBundle;
  }

  public List<MediaRouteDescriptor> getRoutes()
  {
    ensureRoutes();
    return this.mRoutes;
  }

  public boolean isValid()
  {
    ensureRoutes();
    int i = this.mRoutes.size();
    int j = 0;
    if (j < i)
    {
      MediaRouteDescriptor localMediaRouteDescriptor = (MediaRouteDescriptor)this.mRoutes.get(j);
      if ((localMediaRouteDescriptor != null) && (localMediaRouteDescriptor.isValid()));
    }
    for (boolean bool = false; ; bool = true)
    {
      return bool;
      j += 1;
      break;
    }
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("MediaRouteProviderDescriptor{ ");
    StringBuilder localStringBuilder3 = localStringBuilder1.append("routes=");
    String str = Arrays.toString(getRoutes().toArray());
    StringBuilder localStringBuilder4 = localStringBuilder3.append(str);
    StringBuilder localStringBuilder5 = localStringBuilder1.append(", isValid=");
    boolean bool = isValid();
    StringBuilder localStringBuilder6 = localStringBuilder5.append(bool);
    StringBuilder localStringBuilder7 = localStringBuilder1.append(" }");
    return localStringBuilder1.toString();
  }

  public static final class Builder
  {
    private final Bundle mBundle;
    private ArrayList<MediaRouteDescriptor> mRoutes;

    public Builder()
    {
      Bundle localBundle = new Bundle();
      this.mBundle = localBundle;
    }

    public Builder addRoute(MediaRouteDescriptor paramMediaRouteDescriptor)
    {
      if (paramMediaRouteDescriptor == null)
        throw new IllegalArgumentException("route must not be null");
      if (this.mRoutes == null)
      {
        localArrayList = new ArrayList();
        this.mRoutes = localArrayList;
      }
      while (!this.mRoutes.contains(paramMediaRouteDescriptor))
      {
        ArrayList localArrayList;
        boolean bool = this.mRoutes.add(paramMediaRouteDescriptor);
        return this;
      }
      throw new IllegalArgumentException("route descriptor already added");
    }

    public Builder addRoutes(Collection<MediaRouteDescriptor> paramCollection)
    {
      if (paramCollection == null)
        throw new IllegalArgumentException("routes must not be null");
      if (!paramCollection.isEmpty())
      {
        Iterator localIterator = paramCollection.iterator();
        while (localIterator.hasNext())
        {
          MediaRouteDescriptor localMediaRouteDescriptor = (MediaRouteDescriptor)localIterator.next();
          Builder localBuilder = addRoute(localMediaRouteDescriptor);
        }
      }
      return this;
    }

    public MediaRouteProviderDescriptor build()
    {
      if (this.mRoutes != null)
      {
        int i = this.mRoutes.size();
        ArrayList localArrayList1 = new ArrayList(i);
        int j = 0;
        while (j < i)
        {
          Bundle localBundle1 = ((MediaRouteDescriptor)this.mRoutes.get(j)).asBundle();
          boolean bool = localArrayList1.add(localBundle1);
          j += 1;
        }
        this.mBundle.putParcelableArrayList("routes", localArrayList1);
      }
      Bundle localBundle2 = this.mBundle;
      ArrayList localArrayList2 = this.mRoutes;
      return new MediaRouteProviderDescriptor(localBundle2, localArrayList2, null);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.MediaRouteProviderDescriptor
 * JD-Core Version:    0.6.2
 */