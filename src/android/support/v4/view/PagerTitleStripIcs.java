package android.support.v4.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;

class PagerTitleStripIcs
{
  public static void setSingleLineAllCaps(TextView paramTextView)
  {
    Context localContext = paramTextView.getContext();
    SingleLineAllCapsTransform localSingleLineAllCapsTransform = new SingleLineAllCapsTransform(localContext);
    paramTextView.setTransformationMethod(localSingleLineAllCapsTransform);
  }

  private static class SingleLineAllCapsTransform extends SingleLineTransformationMethod
  {
    private Locale mLocale;

    public SingleLineAllCapsTransform(Context paramContext)
    {
      Locale localLocale = paramContext.getResources().getConfiguration().locale;
      this.mLocale = localLocale;
    }

    public CharSequence getTransformation(CharSequence paramCharSequence, View paramView)
    {
      CharSequence localCharSequence = super.getTransformation(paramCharSequence, paramView);
      String str1;
      Locale localLocale;
      if (localCharSequence != null)
      {
        str1 = localCharSequence.toString();
        localLocale = this.mLocale;
      }
      for (String str2 = str1.toUpperCase(localLocale); ; str2 = null)
        return str2;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.PagerTitleStripIcs
 * JD-Core Version:    0.6.2
 */