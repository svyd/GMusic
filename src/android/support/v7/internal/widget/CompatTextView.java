package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v7.appcompat.R.styleable;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;

public class CompatTextView extends TextView
{
  public CompatTextView(Context paramContext)
  {
    this(paramContext, null);
  }

  public CompatTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public CompatTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    int[] arrayOfInt = R.styleable.CompatTextView;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt, paramInt, 0);
    boolean bool = localTypedArray.getBoolean(0, false);
    localTypedArray.recycle();
    if (!bool)
      return;
    AllCapsTransformationMethod localAllCapsTransformationMethod = new AllCapsTransformationMethod(paramContext);
    setTransformationMethod(localAllCapsTransformationMethod);
  }

  private static class AllCapsTransformationMethod
    implements TransformationMethod
  {
    private final Locale mLocale;

    public AllCapsTransformationMethod(Context paramContext)
    {
      Locale localLocale = paramContext.getResources().getConfiguration().locale;
      this.mLocale = localLocale;
    }

    public CharSequence getTransformation(CharSequence paramCharSequence, View paramView)
    {
      String str1;
      Locale localLocale;
      if (paramCharSequence != null)
      {
        str1 = paramCharSequence.toString();
        localLocale = this.mLocale;
      }
      for (String str2 = str1.toUpperCase(localLocale); ; str2 = null)
        return str2;
    }

    public void onFocusChanged(View paramView, CharSequence paramCharSequence, boolean paramBoolean, int paramInt, Rect paramRect)
    {
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.CompatTextView
 * JD-Core Version:    0.6.2
 */