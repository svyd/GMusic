package android.support.v4.content;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.support.v4.util.DebugUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class Loader<D>
{
  boolean mAbandoned = false;
  boolean mContentChanged = false;
  Context mContext;
  int mId;
  OnLoadCompleteListener<D> mListener;
  boolean mProcessingChange = false;
  boolean mReset = true;
  boolean mStarted = false;

  public Loader(Context paramContext)
  {
    Context localContext = paramContext.getApplicationContext();
    this.mContext = localContext;
  }

  public void abandon()
  {
    this.mAbandoned = true;
    onAbandon();
  }

  public void commitContentChanged()
  {
    this.mProcessingChange = false;
  }

  public String dataToString(D paramD)
  {
    StringBuilder localStringBuilder1 = new StringBuilder(64);
    DebugUtils.buildShortClassTag(paramD, localStringBuilder1);
    StringBuilder localStringBuilder2 = localStringBuilder1.append("}");
    return localStringBuilder1.toString();
  }

  public void deliverResult(D paramD)
  {
    if (this.mListener == null)
      return;
    this.mListener.onLoadComplete(this, paramD);
  }

  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mId=");
    int i = this.mId;
    paramPrintWriter.print(i);
    paramPrintWriter.print(" mListener=");
    OnLoadCompleteListener localOnLoadCompleteListener = this.mListener;
    paramPrintWriter.println(localOnLoadCompleteListener);
    if ((this.mStarted) || (this.mContentChanged) || (this.mProcessingChange))
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mStarted=");
      boolean bool1 = this.mStarted;
      paramPrintWriter.print(bool1);
      paramPrintWriter.print(" mContentChanged=");
      boolean bool2 = this.mContentChanged;
      paramPrintWriter.print(bool2);
      paramPrintWriter.print(" mProcessingChange=");
      boolean bool3 = this.mProcessingChange;
      paramPrintWriter.println(bool3);
    }
    if ((!this.mAbandoned) && (!this.mReset))
      return;
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mAbandoned=");
    boolean bool4 = this.mAbandoned;
    paramPrintWriter.print(bool4);
    paramPrintWriter.print(" mReset=");
    boolean bool5 = this.mReset;
    paramPrintWriter.println(bool5);
  }

  public void forceLoad()
  {
    onForceLoad();
  }

  public Context getContext()
  {
    return this.mContext;
  }

  public int getId()
  {
    return this.mId;
  }

  public boolean isAbandoned()
  {
    return this.mAbandoned;
  }

  public boolean isReset()
  {
    return this.mReset;
  }

  public boolean isStarted()
  {
    return this.mStarted;
  }

  protected void onAbandon()
  {
  }

  public void onContentChanged()
  {
    if (this.mStarted)
    {
      forceLoad();
      return;
    }
    this.mContentChanged = true;
  }

  protected void onForceLoad()
  {
  }

  protected void onReset()
  {
  }

  protected void onStartLoading()
  {
  }

  protected void onStopLoading()
  {
  }

  public void registerListener(int paramInt, OnLoadCompleteListener<D> paramOnLoadCompleteListener)
  {
    if (this.mListener != null)
      throw new IllegalStateException("There is already a listener registered");
    this.mListener = paramOnLoadCompleteListener;
    this.mId = paramInt;
  }

  public void reset()
  {
    onReset();
    this.mReset = true;
    this.mStarted = false;
    this.mAbandoned = false;
    this.mContentChanged = false;
    this.mProcessingChange = false;
  }

  public void rollbackContentChanged()
  {
    if (!this.mProcessingChange)
      return;
    this.mContentChanged = true;
  }

  public final void startLoading()
  {
    this.mStarted = true;
    this.mReset = false;
    this.mAbandoned = false;
    onStartLoading();
  }

  public void stopLoading()
  {
    this.mStarted = false;
    onStopLoading();
  }

  public boolean takeContentChanged()
  {
    boolean bool1 = this.mContentChanged;
    this.mContentChanged = false;
    boolean bool2 = this.mProcessingChange | bool1;
    this.mProcessingChange = bool2;
    return bool1;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder(64);
    DebugUtils.buildShortClassTag(this, localStringBuilder1);
    StringBuilder localStringBuilder2 = localStringBuilder1.append(" id=");
    int i = this.mId;
    StringBuilder localStringBuilder3 = localStringBuilder1.append(i);
    StringBuilder localStringBuilder4 = localStringBuilder1.append("}");
    return localStringBuilder1.toString();
  }

  public void unregisterListener(OnLoadCompleteListener<D> paramOnLoadCompleteListener)
  {
    if (this.mListener == null)
      throw new IllegalStateException("No listener register");
    if (this.mListener != paramOnLoadCompleteListener)
      throw new IllegalArgumentException("Attempting to unregister the wrong listener");
    this.mListener = null;
  }

  public static abstract interface OnLoadCompleteListener<D>
  {
    public abstract void onLoadComplete(Loader<D> paramLoader, D paramD);
  }

  public final class ForceLoadContentObserver extends ContentObserver
  {
    public ForceLoadContentObserver()
    {
      super();
    }

    public boolean deliverSelfNotifications()
    {
      return true;
    }

    public void onChange(boolean paramBoolean)
    {
      Loader.this.onContentChanged();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.content.Loader
 * JD-Core Version:    0.6.2
 */