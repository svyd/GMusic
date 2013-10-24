package android.support.v4.content;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;

public class CursorLoader extends AsyncTaskLoader<Cursor>
{
  Cursor mCursor;
  final Loader<Cursor>.ForceLoadContentObserver mObserver;
  String[] mProjection;
  String mSelection;
  String[] mSelectionArgs;
  String mSortOrder;
  Uri mUri;

  public CursorLoader(Context paramContext)
  {
    super(paramContext);
    Loader.ForceLoadContentObserver localForceLoadContentObserver = new Loader.ForceLoadContentObserver(this);
    this.mObserver = localForceLoadContentObserver;
  }

  public CursorLoader(Context paramContext, Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    super(paramContext);
    Loader.ForceLoadContentObserver localForceLoadContentObserver = new Loader.ForceLoadContentObserver(this);
    this.mObserver = localForceLoadContentObserver;
    this.mUri = paramUri;
    this.mProjection = paramArrayOfString1;
    this.mSelection = paramString1;
    this.mSelectionArgs = paramArrayOfString2;
    this.mSortOrder = paramString2;
  }

  public void deliverResult(Cursor paramCursor)
  {
    if (isReset())
    {
      if (paramCursor == null)
        return;
      paramCursor.close();
      return;
    }
    Cursor localCursor = this.mCursor;
    this.mCursor = paramCursor;
    if (isStarted())
      super.deliverResult(paramCursor);
    if (localCursor == null)
      return;
    if (localCursor == paramCursor)
      return;
    if (localCursor.isClosed())
      return;
    localCursor.close();
  }

  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mUri=");
    Uri localUri = this.mUri;
    paramPrintWriter.println(localUri);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mProjection=");
    String str1 = Arrays.toString(this.mProjection);
    paramPrintWriter.println(str1);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSelection=");
    String str2 = this.mSelection;
    paramPrintWriter.println(str2);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSelectionArgs=");
    String str3 = Arrays.toString(this.mSelectionArgs);
    paramPrintWriter.println(str3);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mSortOrder=");
    String str4 = this.mSortOrder;
    paramPrintWriter.println(str4);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mCursor=");
    Cursor localCursor = this.mCursor;
    paramPrintWriter.println(localCursor);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mContentChanged=");
    boolean bool = this.mContentChanged;
    paramPrintWriter.println(bool);
  }

  public String[] getProjection()
  {
    return this.mProjection;
  }

  public Cursor loadInBackground()
  {
    ContentResolver localContentResolver = getContext().getContentResolver();
    Uri localUri = this.mUri;
    String[] arrayOfString1 = this.mProjection;
    String str1 = this.mSelection;
    String[] arrayOfString2 = this.mSelectionArgs;
    String str2 = this.mSortOrder;
    Cursor localCursor = localContentResolver.query(localUri, arrayOfString1, str1, arrayOfString2, str2);
    if (localCursor != null)
    {
      int i = localCursor.getCount();
      Loader.ForceLoadContentObserver localForceLoadContentObserver = this.mObserver;
      localCursor.registerContentObserver(localForceLoadContentObserver);
    }
    return localCursor;
  }

  public void onCanceled(Cursor paramCursor)
  {
    if (paramCursor == null)
      return;
    if (paramCursor.isClosed())
      return;
    paramCursor.close();
  }

  protected void onReset()
  {
    super.onReset();
    onStopLoading();
    if ((this.mCursor != null) && (!this.mCursor.isClosed()))
      this.mCursor.close();
    this.mCursor = null;
  }

  protected void onStartLoading()
  {
    if (this.mCursor != null)
    {
      Cursor localCursor = this.mCursor;
      deliverResult(localCursor);
    }
    if ((!takeContentChanged()) && (this.mCursor != null))
      return;
    forceLoad();
  }

  protected void onStopLoading()
  {
    boolean bool = cancelLoad();
  }

  public void setProjection(String[] paramArrayOfString)
  {
    this.mProjection = paramArrayOfString;
  }

  public void setUri(Uri paramUri)
  {
    this.mUri = paramUri;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.content.CursorLoader
 * JD-Core Version:    0.6.2
 */