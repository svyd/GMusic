package com.google.android.music;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import java.lang.ref.SoftReference;

public class KeepOnViewSmall extends KeepOnView
{
  private static boolean sResourcesInitialized = false;
  private static SoftReference<KeepOnView.SharedResources> sSharedResourcesRef = new SoftReference(null);
  private KeepOnVisibilityCallback mCallback;

  public KeepOnViewSmall(Context paramContext)
  {
    this(paramContext, null, 0);
  }

  public KeepOnViewSmall(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public KeepOnViewSmall(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private KeepOnView.SharedResources computeSharedResources()
  {
    Resources localResources = getContext().getResources();
    Bitmap localBitmap1 = BitmapFactory.decodeResource(localResources, 2130837642);
    Bitmap localBitmap2 = BitmapFactory.decodeResource(localResources, 2130837638);
    Bitmap localBitmap3 = BitmapFactory.decodeResource(localResources, 2130837634);
    Bitmap localBitmap4 = BitmapFactory.decodeResource(localResources, 2130837640);
    Bitmap localBitmap5 = BitmapFactory.decodeResource(localResources, 2130837636);
    return new KeepOnView.SharedResources(localBitmap1, localBitmap2, localBitmap3, localBitmap4, localBitmap5);
  }

  private void setKeepOnResources(KeepOnView.SharedResources paramSharedResources)
  {
    sSharedResourcesRef = new SoftReference(paramSharedResources);
  }

  protected KeepOnView.SharedResources getKeepOnResources()
  {
    if ((sResourcesInitialized) && (sSharedResourcesRef.get() != null));
    KeepOnView.SharedResources localSharedResources;
    for (Object localObject = (KeepOnView.SharedResources)sSharedResourcesRef.get(); ; localObject = localSharedResources)
    {
      return localObject;
      localSharedResources = computeSharedResources();
      setKeepOnResources(localSharedResources);
      sResourcesInitialized = true;
    }
  }

  public void registerCallback(KeepOnVisibilityCallback paramKeepOnVisibilityCallback)
  {
    this.mCallback = paramKeepOnVisibilityCallback;
  }

  public void setPinned(boolean paramBoolean)
  {
    super.setPinned(paramBoolean);
    if (this.mCallback == null)
      return;
    this.mCallback.updateKeepOnViewSmallVisibility(paramBoolean);
  }

  public void unregisterCallback()
  {
    this.mCallback = null;
  }

  public static abstract interface KeepOnVisibilityCallback
  {
    public abstract void updateKeepOnViewSmallVisibility(boolean paramBoolean);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.KeepOnViewSmall
 * JD-Core Version:    0.6.2
 */