package android.support.v7.media;

import android.content.IntentFilter;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class MediaRouteSelector
{
  public static final MediaRouteSelector EMPTY = new MediaRouteSelector(localBundle, null);
  private final Bundle mBundle;
  private List<String> mControlCategories;

  static
  {
    Bundle localBundle = new Bundle();
  }

  private MediaRouteSelector(Bundle paramBundle, List<String> paramList)
  {
    this.mBundle = paramBundle;
    this.mControlCategories = paramList;
  }

  private void ensureControlCategories()
  {
    if (this.mControlCategories != null)
      return;
    ArrayList localArrayList = this.mBundle.getStringArrayList("controlCategories");
    this.mControlCategories = localArrayList;
    if ((this.mControlCategories != null) && (!this.mControlCategories.isEmpty()))
      return;
    List localList = Collections.emptyList();
    this.mControlCategories = localList;
  }

  public static MediaRouteSelector fromBundle(Bundle paramBundle)
  {
    if (paramBundle != null);
    for (MediaRouteSelector localMediaRouteSelector = new MediaRouteSelector(paramBundle, null); ; localMediaRouteSelector = null)
      return localMediaRouteSelector;
  }

  public Bundle asBundle()
  {
    return this.mBundle;
  }

  public boolean contains(MediaRouteSelector paramMediaRouteSelector)
  {
    List localList1;
    List localList2;
    if (paramMediaRouteSelector != null)
    {
      ensureControlCategories();
      paramMediaRouteSelector.ensureControlCategories();
      localList1 = this.mControlCategories;
      localList2 = paramMediaRouteSelector.mControlCategories;
    }
    for (boolean bool = localList1.containsAll(localList2); ; bool = false)
      return bool;
  }

  public boolean equals(Object paramObject)
  {
    List localList1;
    List localList2;
    if ((paramObject instanceof MediaRouteSelector))
    {
      MediaRouteSelector localMediaRouteSelector = (MediaRouteSelector)paramObject;
      ensureControlCategories();
      localMediaRouteSelector.ensureControlCategories();
      localList1 = this.mControlCategories;
      localList2 = localMediaRouteSelector.mControlCategories;
    }
    for (boolean bool = localList1.equals(localList2); ; bool = false)
      return bool;
  }

  public List<String> getControlCategories()
  {
    ensureControlCategories();
    return this.mControlCategories;
  }

  public int hashCode()
  {
    ensureControlCategories();
    return this.mControlCategories.hashCode();
  }

  public boolean isEmpty()
  {
    ensureControlCategories();
    return this.mControlCategories.isEmpty();
  }

  public boolean isValid()
  {
    ensureControlCategories();
    if (this.mControlCategories.contains(null));
    for (boolean bool = false; ; bool = true)
      return bool;
  }

  public boolean matchesControlFilters(List<IntentFilter> paramList)
  {
    int k;
    int m;
    if (paramList != null)
    {
      ensureControlCategories();
      int i = this.mControlCategories.size();
      if (i != 0)
      {
        int j = paramList.size();
        k = 0;
        if (k < j)
        {
          IntentFilter localIntentFilter = (IntentFilter)paramList.get(k);
          if (localIntentFilter != null)
          {
            m = 0;
            label59: if (m < i)
            {
              String str = (String)this.mControlCategories.get(m);
              if (!localIntentFilter.hasCategory(str));
            }
          }
        }
      }
    }
    for (boolean bool = true; ; bool = false)
    {
      return bool;
      m += 1;
      break label59;
      k += 1;
      break;
    }
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("MediaRouteSelector{ ");
    StringBuilder localStringBuilder3 = localStringBuilder1.append("controlCategories=");
    String str = Arrays.toString(getControlCategories().toArray());
    StringBuilder localStringBuilder4 = localStringBuilder3.append(str);
    StringBuilder localStringBuilder5 = localStringBuilder1.append(" }");
    return localStringBuilder1.toString();
  }

  public static final class Builder
  {
    private ArrayList<String> mControlCategories;

    public Builder()
    {
    }

    public Builder(MediaRouteSelector paramMediaRouteSelector)
    {
      if (paramMediaRouteSelector == null)
        throw new IllegalArgumentException("selector must not be null");
      paramMediaRouteSelector.ensureControlCategories();
      if (paramMediaRouteSelector.mControlCategories.isEmpty())
        return;
      List localList = paramMediaRouteSelector.mControlCategories;
      ArrayList localArrayList = new ArrayList(localList);
      this.mControlCategories = localArrayList;
    }

    public Builder addControlCategories(Collection<String> paramCollection)
    {
      if (paramCollection == null)
        throw new IllegalArgumentException("categories must not be null");
      if (!paramCollection.isEmpty())
      {
        Iterator localIterator = paramCollection.iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          Builder localBuilder = addControlCategory(str);
        }
      }
      return this;
    }

    public Builder addControlCategory(String paramString)
    {
      if (paramString == null)
        throw new IllegalArgumentException("category must not be null");
      if (this.mControlCategories == null)
      {
        ArrayList localArrayList = new ArrayList();
        this.mControlCategories = localArrayList;
      }
      if (!this.mControlCategories.contains(paramString))
        boolean bool = this.mControlCategories.add(paramString);
      return this;
    }

    public Builder addSelector(MediaRouteSelector paramMediaRouteSelector)
    {
      if (paramMediaRouteSelector == null)
        throw new IllegalArgumentException("selector must not be null");
      List localList = paramMediaRouteSelector.getControlCategories();
      Builder localBuilder = addControlCategories(localList);
      return this;
    }

    public MediaRouteSelector build()
    {
      if (this.mControlCategories == null);
      Bundle localBundle;
      ArrayList localArrayList2;
      for (MediaRouteSelector localMediaRouteSelector = MediaRouteSelector.EMPTY; ; localMediaRouteSelector = new MediaRouteSelector(localBundle, localArrayList2, null))
      {
        return localMediaRouteSelector;
        localBundle = new Bundle();
        ArrayList localArrayList1 = this.mControlCategories;
        localBundle.putStringArrayList("controlCategories", localArrayList1);
        localArrayList2 = this.mControlCategories;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.MediaRouteSelector
 * JD-Core Version:    0.6.2
 */