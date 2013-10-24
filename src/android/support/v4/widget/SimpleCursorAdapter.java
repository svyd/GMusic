package android.support.v4.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleCursorAdapter extends ResourceCursorAdapter
{
  private CursorToStringConverter mCursorToStringConverter;
  protected int[] mFrom;
  String[] mOriginalFrom;
  private int mStringConversionColumn = -1;
  protected int[] mTo;
  private ViewBinder mViewBinder;

  @Deprecated
  public SimpleCursorAdapter(Context paramContext, int paramInt, Cursor paramCursor, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    super(paramContext, paramInt, paramCursor);
    this.mTo = paramArrayOfInt;
    this.mOriginalFrom = paramArrayOfString;
    findColumns(paramArrayOfString);
  }

  public SimpleCursorAdapter(Context paramContext, int paramInt1, Cursor paramCursor, String[] paramArrayOfString, int[] paramArrayOfInt, int paramInt2)
  {
    super(paramContext, paramInt1, paramCursor, paramInt2);
    this.mTo = paramArrayOfInt;
    this.mOriginalFrom = paramArrayOfString;
    findColumns(paramArrayOfString);
  }

  private void findColumns(String[] paramArrayOfString)
  {
    if (this.mCursor != null)
    {
      int i = paramArrayOfString.length;
      if ((this.mFrom == null) || (this.mFrom.length != i))
      {
        int[] arrayOfInt1 = new int[i];
        this.mFrom = arrayOfInt1;
      }
      int j = 0;
      while (true)
      {
        if (j >= i)
          return;
        int[] arrayOfInt2 = this.mFrom;
        Cursor localCursor = this.mCursor;
        String str = paramArrayOfString[j];
        int k = localCursor.getColumnIndexOrThrow(str);
        arrayOfInt2[j] = k;
        j += 1;
      }
    }
    this.mFrom = null;
  }

  public void bindView(View paramView, Context paramContext, Cursor paramCursor)
  {
    ViewBinder localViewBinder = this.mViewBinder;
    int i = this.mTo.length;
    int[] arrayOfInt1 = this.mFrom;
    int[] arrayOfInt2 = this.mTo;
    int j = 0;
    if (j >= i)
      return;
    int k = arrayOfInt2[j];
    View localView = paramView.findViewById(k);
    String str1;
    if (localView != null)
    {
      boolean bool = false;
      if (localViewBinder != null)
      {
        int m = arrayOfInt1[j];
        bool = localViewBinder.setViewValue(localView, paramCursor, m);
      }
      if (!bool)
      {
        int n = arrayOfInt1[j];
        str1 = paramCursor.getString(n);
        if (str1 == null)
          str1 = "";
        if (!(localView instanceof TextView))
          break label148;
        TextView localTextView = (TextView)localView;
        setViewText(localTextView, str1);
      }
    }
    while (true)
    {
      j += 1;
      break;
      label148: if (!(localView instanceof ImageView))
        break label174;
      ImageView localImageView = (ImageView)localView;
      setViewImage(localImageView, str1);
    }
    label174: StringBuilder localStringBuilder = new StringBuilder();
    String str2 = localView.getClass().getName();
    String str3 = str2 + " is not a " + " view that can be bounds by this SimpleCursorAdapter";
    throw new IllegalStateException(str3);
  }

  public CharSequence convertToString(Cursor paramCursor)
  {
    Object localObject;
    if (this.mCursorToStringConverter != null)
      localObject = this.mCursorToStringConverter.convertToString(paramCursor);
    while (true)
    {
      return localObject;
      if (this.mStringConversionColumn > -1)
      {
        int i = this.mStringConversionColumn;
        localObject = paramCursor.getString(i);
      }
      else
      {
        localObject = super.convertToString(paramCursor);
      }
    }
  }

  public void setViewImage(ImageView paramImageView, String paramString)
  {
    try
    {
      int i = Integer.parseInt(paramString);
      paramImageView.setImageResource(i);
      return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      Uri localUri = Uri.parse(paramString);
      paramImageView.setImageURI(localUri);
    }
  }

  public void setViewText(TextView paramTextView, String paramString)
  {
    paramTextView.setText(paramString);
  }

  public Cursor swapCursor(Cursor paramCursor)
  {
    Cursor localCursor = super.swapCursor(paramCursor);
    String[] arrayOfString = this.mOriginalFrom;
    findColumns(arrayOfString);
    return localCursor;
  }

  public static abstract interface CursorToStringConverter
  {
    public abstract CharSequence convertToString(Cursor paramCursor);
  }

  public static abstract interface ViewBinder
  {
    public abstract boolean setViewValue(View paramView, Cursor paramCursor, int paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.widget.SimpleCursorAdapter
 * JD-Core Version:    0.6.2
 */