package android.support.v7.media;

import android.os.Bundle;

public final class MediaRouteDiscoveryRequest
{
  private final Bundle mBundle;
  private MediaRouteSelector mSelector;

  private MediaRouteDiscoveryRequest(Bundle paramBundle)
  {
    this.mBundle = paramBundle;
  }

  public MediaRouteDiscoveryRequest(MediaRouteSelector paramMediaRouteSelector, boolean paramBoolean)
  {
    if (paramMediaRouteSelector == null)
      throw new IllegalArgumentException("selector must not be null");
    Bundle localBundle1 = new Bundle();
    this.mBundle = localBundle1;
    this.mSelector = paramMediaRouteSelector;
    Bundle localBundle2 = this.mBundle;
    Bundle localBundle3 = paramMediaRouteSelector.asBundle();
    localBundle2.putBundle("selector", localBundle3);
    this.mBundle.putBoolean("activeScan", paramBoolean);
  }

  private void ensureSelector()
  {
    if (this.mSelector != null)
      return;
    MediaRouteSelector localMediaRouteSelector1 = MediaRouteSelector.fromBundle(this.mBundle.getBundle("selector"));
    this.mSelector = localMediaRouteSelector1;
    if (this.mSelector != null)
      return;
    MediaRouteSelector localMediaRouteSelector2 = MediaRouteSelector.EMPTY;
    this.mSelector = localMediaRouteSelector2;
  }

  public static MediaRouteDiscoveryRequest fromBundle(Bundle paramBundle)
  {
    if (paramBundle != null);
    for (MediaRouteDiscoveryRequest localMediaRouteDiscoveryRequest = new MediaRouteDiscoveryRequest(paramBundle); ; localMediaRouteDiscoveryRequest = null)
      return localMediaRouteDiscoveryRequest;
  }

  public Bundle asBundle()
  {
    return this.mBundle;
  }

  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if ((paramObject instanceof MediaRouteDiscoveryRequest))
    {
      MediaRouteDiscoveryRequest localMediaRouteDiscoveryRequest = (MediaRouteDiscoveryRequest)paramObject;
      MediaRouteSelector localMediaRouteSelector1 = getSelector();
      MediaRouteSelector localMediaRouteSelector2 = localMediaRouteDiscoveryRequest.getSelector();
      if (localMediaRouteSelector1.equals(localMediaRouteSelector2))
      {
        boolean bool2 = isActiveScan();
        boolean bool3 = localMediaRouteDiscoveryRequest.isActiveScan();
        if (bool2 != bool3)
          bool1 = true;
      }
    }
    return bool1;
  }

  public MediaRouteSelector getSelector()
  {
    ensureSelector();
    return this.mSelector;
  }

  public int hashCode()
  {
    int i = getSelector().hashCode();
    if (isActiveScan());
    for (int j = 1; ; j = 0)
      return j ^ i;
  }

  public boolean isActiveScan()
  {
    return this.mBundle.getBoolean("activeScan");
  }

  public boolean isValid()
  {
    ensureSelector();
    return this.mSelector.isValid();
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("DiscoveryRequest{ selector=");
    MediaRouteSelector localMediaRouteSelector = getSelector();
    StringBuilder localStringBuilder3 = localStringBuilder2.append(localMediaRouteSelector);
    StringBuilder localStringBuilder4 = localStringBuilder1.append(", activeScan=");
    boolean bool1 = isActiveScan();
    StringBuilder localStringBuilder5 = localStringBuilder4.append(bool1);
    StringBuilder localStringBuilder6 = localStringBuilder1.append(", isValid=");
    boolean bool2 = isValid();
    StringBuilder localStringBuilder7 = localStringBuilder6.append(bool2);
    StringBuilder localStringBuilder8 = localStringBuilder1.append(" }");
    return localStringBuilder1.toString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.MediaRouteDiscoveryRequest
 * JD-Core Version:    0.6.2
 */