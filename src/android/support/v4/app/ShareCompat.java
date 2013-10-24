package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;

public class ShareCompat
{
  public static final String EXTRA_CALLING_ACTIVITY = "android.support.v4.app.EXTRA_CALLING_ACTIVITY";
  public static final String EXTRA_CALLING_PACKAGE = "android.support.v4.app.EXTRA_CALLING_PACKAGE";
  private static ShareCompatImpl IMPL = new ShareCompatImplBase();

  static
  {
    if (Build.VERSION.SDK_INT >= 16)
    {
      IMPL = new ShareCompatImplJB();
      return;
    }
    if (Build.VERSION.SDK_INT >= 14)
    {
      IMPL = new ShareCompatImplICS();
      return;
    }
  }

  public static void configureMenuItem(Menu paramMenu, int paramInt, IntentBuilder paramIntentBuilder)
  {
    MenuItem localMenuItem = paramMenu.findItem(paramInt);
    if (localMenuItem == null)
    {
      String str = "Could not find menu item with id " + paramInt + " in the supplied menu";
      throw new IllegalArgumentException(str);
    }
    configureMenuItem(localMenuItem, paramIntentBuilder);
  }

  public static void configureMenuItem(MenuItem paramMenuItem, IntentBuilder paramIntentBuilder)
  {
    IMPL.configureMenuItem(paramMenuItem, paramIntentBuilder);
  }

  public static ComponentName getCallingActivity(Activity paramActivity)
  {
    ComponentName localComponentName = paramActivity.getCallingActivity();
    if (localComponentName == null)
      localComponentName = (ComponentName)paramActivity.getIntent().getParcelableExtra("android.support.v4.app.EXTRA_CALLING_ACTIVITY");
    return localComponentName;
  }

  public static String getCallingPackage(Activity paramActivity)
  {
    String str = paramActivity.getCallingPackage();
    if (str == null)
      str = paramActivity.getIntent().getStringExtra("android.support.v4.app.EXTRA_CALLING_PACKAGE");
    return str;
  }

  public static class IntentReader
  {
    private static final String TAG = "IntentReader";
    private Activity mActivity;
    private ComponentName mCallingActivity;
    private String mCallingPackage;
    private Intent mIntent;
    private ArrayList<Uri> mStreams;

    private IntentReader(Activity paramActivity)
    {
      this.mActivity = paramActivity;
      Intent localIntent = paramActivity.getIntent();
      this.mIntent = localIntent;
      String str = ShareCompat.getCallingPackage(paramActivity);
      this.mCallingPackage = str;
      ComponentName localComponentName = ShareCompat.getCallingActivity(paramActivity);
      this.mCallingActivity = localComponentName;
    }

    public static IntentReader from(Activity paramActivity)
    {
      return new IntentReader(paramActivity);
    }

    public ComponentName getCallingActivity()
    {
      return this.mCallingActivity;
    }

    public Drawable getCallingActivityIcon()
    {
      Object localObject = null;
      if (this.mCallingActivity == null);
      while (true)
      {
        return localObject;
        PackageManager localPackageManager = this.mActivity.getPackageManager();
        try
        {
          ComponentName localComponentName = this.mCallingActivity;
          Drawable localDrawable = localPackageManager.getActivityIcon(localComponentName);
          localObject = localDrawable;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          int i = Log.e("IntentReader", "Could not retrieve icon for calling activity", localNameNotFoundException);
        }
      }
    }

    public Drawable getCallingApplicationIcon()
    {
      Object localObject = null;
      if (this.mCallingPackage == null);
      while (true)
      {
        return localObject;
        PackageManager localPackageManager = this.mActivity.getPackageManager();
        try
        {
          String str = this.mCallingPackage;
          Drawable localDrawable = localPackageManager.getApplicationIcon(str);
          localObject = localDrawable;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          int i = Log.e("IntentReader", "Could not retrieve icon for calling application", localNameNotFoundException);
        }
      }
    }

    public CharSequence getCallingApplicationLabel()
    {
      Object localObject = null;
      if (this.mCallingPackage == null);
      while (true)
      {
        return localObject;
        PackageManager localPackageManager = this.mActivity.getPackageManager();
        try
        {
          String str = this.mCallingPackage;
          ApplicationInfo localApplicationInfo = localPackageManager.getApplicationInfo(str, 0);
          CharSequence localCharSequence = localPackageManager.getApplicationLabel(localApplicationInfo);
          localObject = localCharSequence;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          int i = Log.e("IntentReader", "Could not retrieve label for calling application", localNameNotFoundException);
        }
      }
    }

    public String getCallingPackage()
    {
      return this.mCallingPackage;
    }

    public String[] getEmailBcc()
    {
      return this.mIntent.getStringArrayExtra("android.intent.extra.BCC");
    }

    public String[] getEmailCc()
    {
      return this.mIntent.getStringArrayExtra("android.intent.extra.CC");
    }

    public String[] getEmailTo()
    {
      return this.mIntent.getStringArrayExtra("android.intent.extra.EMAIL");
    }

    public String getHtmlText()
    {
      String str = this.mIntent.getStringExtra("android.intent.extra.HTML_TEXT");
      CharSequence localCharSequence;
      if (str == null)
      {
        localCharSequence = getText();
        if (!(localCharSequence instanceof Spanned))
          break label36;
        str = Html.toHtml((Spanned)localCharSequence);
      }
      while (true)
      {
        return str;
        label36: if (localCharSequence != null)
          str = ShareCompat.IMPL.escapeHtml(localCharSequence);
      }
    }

    public Uri getStream()
    {
      return (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
    }

    public Uri getStream(int paramInt)
    {
      if ((this.mStreams == null) && (isMultipleShare()))
      {
        ArrayList localArrayList = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
        this.mStreams = localArrayList;
      }
      if (this.mStreams != null);
      for (Uri localUri = (Uri)this.mStreams.get(paramInt); ; localUri = (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM"))
      {
        return localUri;
        if (paramInt != 0)
          break;
      }
      StringBuilder localStringBuilder = new StringBuilder().append("Stream items available: ");
      int i = getStreamCount();
      String str = i + " index requested: " + paramInt;
      throw new IndexOutOfBoundsException(str);
    }

    public int getStreamCount()
    {
      if ((this.mStreams == null) && (isMultipleShare()))
      {
        ArrayList localArrayList = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
        this.mStreams = localArrayList;
      }
      int i;
      if (this.mStreams != null)
        i = this.mStreams.size();
      while (true)
      {
        return i;
        if (this.mIntent.hasExtra("android.intent.extra.STREAM"))
          i = 1;
        else
          i = 0;
      }
    }

    public String getSubject()
    {
      return this.mIntent.getStringExtra("android.intent.extra.SUBJECT");
    }

    public CharSequence getText()
    {
      return this.mIntent.getCharSequenceExtra("android.intent.extra.TEXT");
    }

    public String getType()
    {
      return this.mIntent.getType();
    }

    public boolean isMultipleShare()
    {
      String str = this.mIntent.getAction();
      return "android.intent.action.SEND_MULTIPLE".equals(str);
    }

    public boolean isShareIntent()
    {
      String str = this.mIntent.getAction();
      if (("android.intent.action.SEND".equals(str)) || ("android.intent.action.SEND_MULTIPLE".equals(str)));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean isSingleShare()
    {
      String str = this.mIntent.getAction();
      return "android.intent.action.SEND".equals(str);
    }
  }

  public static class IntentBuilder
  {
    private Activity mActivity;
    private ArrayList<String> mBccAddresses;
    private ArrayList<String> mCcAddresses;
    private CharSequence mChooserTitle;
    private Intent mIntent;
    private ArrayList<Uri> mStreams;
    private ArrayList<String> mToAddresses;

    private IntentBuilder(Activity paramActivity)
    {
      this.mActivity = paramActivity;
      Intent localIntent1 = new Intent().setAction("android.intent.action.SEND");
      this.mIntent = localIntent1;
      Intent localIntent2 = this.mIntent;
      String str = paramActivity.getPackageName();
      Intent localIntent3 = localIntent2.putExtra("android.support.v4.app.EXTRA_CALLING_PACKAGE", str);
      Intent localIntent4 = this.mIntent;
      ComponentName localComponentName = paramActivity.getComponentName();
      Intent localIntent5 = localIntent4.putExtra("android.support.v4.app.EXTRA_CALLING_ACTIVITY", localComponentName);
      Intent localIntent6 = this.mIntent.addFlags(524288);
    }

    private void combineArrayExtra(String paramString, ArrayList<String> paramArrayList)
    {
      String[] arrayOfString1 = this.mIntent.getStringArrayExtra(paramString);
      if (arrayOfString1 != null);
      for (int i = arrayOfString1.length; ; i = 0)
      {
        String[] arrayOfString2 = new String[paramArrayList.size() + i];
        Object[] arrayOfObject = paramArrayList.toArray(arrayOfString2);
        if (arrayOfString1 != null)
        {
          int j = paramArrayList.size();
          System.arraycopy(arrayOfString1, 0, arrayOfString2, j, i);
        }
        Intent localIntent = this.mIntent.putExtra(paramString, arrayOfString2);
        return;
      }
    }

    private void combineArrayExtra(String paramString, String[] paramArrayOfString)
    {
      Intent localIntent1 = getIntent();
      String[] arrayOfString1 = localIntent1.getStringArrayExtra(paramString);
      if (arrayOfString1 != null);
      for (int i = arrayOfString1.length; ; i = 0)
      {
        String[] arrayOfString2 = new String[paramArrayOfString.length + i];
        if (arrayOfString1 != null)
          System.arraycopy(arrayOfString1, 0, arrayOfString2, 0, i);
        int j = paramArrayOfString.length;
        System.arraycopy(paramArrayOfString, 0, arrayOfString2, i, j);
        Intent localIntent2 = localIntent1.putExtra(paramString, arrayOfString2);
        return;
      }
    }

    public static IntentBuilder from(Activity paramActivity)
    {
      return new IntentBuilder(paramActivity);
    }

    public IntentBuilder addEmailBcc(String paramString)
    {
      if (this.mBccAddresses == null)
      {
        ArrayList localArrayList = new ArrayList();
        this.mBccAddresses = localArrayList;
      }
      boolean bool = this.mBccAddresses.add(paramString);
      return this;
    }

    public IntentBuilder addEmailBcc(String[] paramArrayOfString)
    {
      combineArrayExtra("android.intent.extra.BCC", paramArrayOfString);
      return this;
    }

    public IntentBuilder addEmailCc(String paramString)
    {
      if (this.mCcAddresses == null)
      {
        ArrayList localArrayList = new ArrayList();
        this.mCcAddresses = localArrayList;
      }
      boolean bool = this.mCcAddresses.add(paramString);
      return this;
    }

    public IntentBuilder addEmailCc(String[] paramArrayOfString)
    {
      combineArrayExtra("android.intent.extra.CC", paramArrayOfString);
      return this;
    }

    public IntentBuilder addEmailTo(String paramString)
    {
      if (this.mToAddresses == null)
      {
        ArrayList localArrayList = new ArrayList();
        this.mToAddresses = localArrayList;
      }
      boolean bool = this.mToAddresses.add(paramString);
      return this;
    }

    public IntentBuilder addEmailTo(String[] paramArrayOfString)
    {
      combineArrayExtra("android.intent.extra.EMAIL", paramArrayOfString);
      return this;
    }

    public IntentBuilder addStream(Uri paramUri)
    {
      Uri localUri = (Uri)this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
      if (localUri == null)
        this = setStream(paramUri);
      while (true)
      {
        return this;
        if (this.mStreams == null)
        {
          ArrayList localArrayList = new ArrayList();
          this.mStreams = localArrayList;
        }
        if (localUri != null)
        {
          this.mIntent.removeExtra("android.intent.extra.STREAM");
          boolean bool1 = this.mStreams.add(localUri);
        }
        boolean bool2 = this.mStreams.add(paramUri);
      }
    }

    public Intent createChooserIntent()
    {
      Intent localIntent = getIntent();
      CharSequence localCharSequence = this.mChooserTitle;
      return Intent.createChooser(localIntent, localCharSequence);
    }

    Activity getActivity()
    {
      return this.mActivity;
    }

    public Intent getIntent()
    {
      int i = 1;
      if (this.mToAddresses != null)
      {
        ArrayList localArrayList1 = this.mToAddresses;
        combineArrayExtra("android.intent.extra.EMAIL", localArrayList1);
        this.mToAddresses = null;
      }
      if (this.mCcAddresses != null)
      {
        ArrayList localArrayList2 = this.mCcAddresses;
        combineArrayExtra("android.intent.extra.CC", localArrayList2);
        this.mCcAddresses = null;
      }
      if (this.mBccAddresses != null)
      {
        ArrayList localArrayList3 = this.mBccAddresses;
        combineArrayExtra("android.intent.extra.BCC", localArrayList3);
        this.mBccAddresses = null;
      }
      if ((this.mStreams != null) && (this.mStreams.size() > 1))
      {
        boolean bool = this.mIntent.getAction().equals("android.intent.action.SEND_MULTIPLE");
        if ((i == 0) && (bool))
        {
          Intent localIntent1 = this.mIntent.setAction("android.intent.action.SEND");
          if ((this.mStreams == null) || (this.mStreams.isEmpty()))
            break label250;
          Intent localIntent2 = this.mIntent;
          Parcelable localParcelable = (Parcelable)this.mStreams.get(0);
          Intent localIntent3 = localIntent2.putExtra("android.intent.extra.STREAM", localParcelable);
          label175: this.mStreams = null;
        }
        if ((i != 0) && (!bool))
        {
          Intent localIntent4 = this.mIntent.setAction("android.intent.action.SEND_MULTIPLE");
          if ((this.mStreams == null) || (this.mStreams.isEmpty()))
            break label262;
          Intent localIntent5 = this.mIntent;
          ArrayList localArrayList4 = this.mStreams;
          Intent localIntent6 = localIntent5.putParcelableArrayListExtra("android.intent.extra.STREAM", localArrayList4);
        }
      }
      while (true)
      {
        return this.mIntent;
        i = 0;
        break;
        label250: this.mIntent.removeExtra("android.intent.extra.STREAM");
        break label175;
        label262: this.mIntent.removeExtra("android.intent.extra.STREAM");
      }
    }

    public IntentBuilder setChooserTitle(int paramInt)
    {
      CharSequence localCharSequence = this.mActivity.getText(paramInt);
      return setChooserTitle(localCharSequence);
    }

    public IntentBuilder setChooserTitle(CharSequence paramCharSequence)
    {
      this.mChooserTitle = paramCharSequence;
      return this;
    }

    public IntentBuilder setEmailBcc(String[] paramArrayOfString)
    {
      Intent localIntent = this.mIntent.putExtra("android.intent.extra.BCC", paramArrayOfString);
      return this;
    }

    public IntentBuilder setEmailCc(String[] paramArrayOfString)
    {
      Intent localIntent = this.mIntent.putExtra("android.intent.extra.CC", paramArrayOfString);
      return this;
    }

    public IntentBuilder setEmailTo(String[] paramArrayOfString)
    {
      if (this.mToAddresses != null)
        this.mToAddresses = null;
      Intent localIntent = this.mIntent.putExtra("android.intent.extra.EMAIL", paramArrayOfString);
      return this;
    }

    public IntentBuilder setHtmlText(String paramString)
    {
      Intent localIntent = this.mIntent.putExtra("android.intent.extra.HTML_TEXT", paramString);
      if (!this.mIntent.hasExtra("android.intent.extra.TEXT"))
      {
        Spanned localSpanned = Html.fromHtml(paramString);
        IntentBuilder localIntentBuilder = setText(localSpanned);
      }
      return this;
    }

    public IntentBuilder setStream(Uri paramUri)
    {
      if (!this.mIntent.getAction().equals("android.intent.action.SEND"))
        Intent localIntent1 = this.mIntent.setAction("android.intent.action.SEND");
      this.mStreams = null;
      Intent localIntent2 = this.mIntent.putExtra("android.intent.extra.STREAM", paramUri);
      return this;
    }

    public IntentBuilder setSubject(String paramString)
    {
      Intent localIntent = this.mIntent.putExtra("android.intent.extra.SUBJECT", paramString);
      return this;
    }

    public IntentBuilder setText(CharSequence paramCharSequence)
    {
      Intent localIntent = this.mIntent.putExtra("android.intent.extra.TEXT", paramCharSequence);
      return this;
    }

    public IntentBuilder setType(String paramString)
    {
      Intent localIntent = this.mIntent.setType(paramString);
      return this;
    }

    public void startChooser()
    {
      Activity localActivity = this.mActivity;
      Intent localIntent = createChooserIntent();
      localActivity.startActivity(localIntent);
    }
  }

  static class ShareCompatImplJB extends ShareCompat.ShareCompatImplICS
  {
    public String escapeHtml(CharSequence paramCharSequence)
    {
      return ShareCompatJB.escapeHtml(paramCharSequence);
    }

    boolean shouldAddChooserIntent(MenuItem paramMenuItem)
    {
      return false;
    }
  }

  static class ShareCompatImplICS extends ShareCompat.ShareCompatImplBase
  {
    public void configureMenuItem(MenuItem paramMenuItem, ShareCompat.IntentBuilder paramIntentBuilder)
    {
      Activity localActivity = paramIntentBuilder.getActivity();
      Intent localIntent1 = paramIntentBuilder.getIntent();
      ShareCompatICS.configureMenuItem(paramMenuItem, localActivity, localIntent1);
      if (!shouldAddChooserIntent(paramMenuItem))
        return;
      Intent localIntent2 = paramIntentBuilder.createChooserIntent();
      MenuItem localMenuItem = paramMenuItem.setIntent(localIntent2);
    }

    boolean shouldAddChooserIntent(MenuItem paramMenuItem)
    {
      if (!paramMenuItem.hasSubMenu());
      for (boolean bool = true; ; bool = false)
        return bool;
    }
  }

  static class ShareCompatImplBase
    implements ShareCompat.ShareCompatImpl
  {
    private static void withinStyle(StringBuilder paramStringBuilder, CharSequence paramCharSequence, int paramInt1, int paramInt2)
    {
      int i = paramInt1;
      if (i >= paramInt2)
        return;
      char c = paramCharSequence.charAt(i);
      if (c == '<')
        StringBuilder localStringBuilder1 = paramStringBuilder.append("&lt;");
      while (true)
      {
        i += 1;
        break;
        if (c == '>')
        {
          StringBuilder localStringBuilder2 = paramStringBuilder.append("&gt;");
        }
        else if (c == '&')
        {
          StringBuilder localStringBuilder3 = paramStringBuilder.append("&amp;");
        }
        else if ((c > '~') || (c < ' '))
        {
          String str = "&#" + c + ";";
          StringBuilder localStringBuilder4 = paramStringBuilder.append(str);
        }
        else if (c == ' ')
        {
          while (i + 1 < paramInt2)
          {
            int j = i + 1;
            if (paramCharSequence.charAt(j) != ' ')
              break;
            StringBuilder localStringBuilder5 = paramStringBuilder.append("&nbsp;");
            i += 1;
          }
          StringBuilder localStringBuilder6 = paramStringBuilder.append(' ');
        }
        else
        {
          StringBuilder localStringBuilder7 = paramStringBuilder.append(c);
        }
      }
    }

    public void configureMenuItem(MenuItem paramMenuItem, ShareCompat.IntentBuilder paramIntentBuilder)
    {
      Intent localIntent = paramIntentBuilder.createChooserIntent();
      MenuItem localMenuItem = paramMenuItem.setIntent(localIntent);
    }

    public String escapeHtml(CharSequence paramCharSequence)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      int i = paramCharSequence.length();
      withinStyle(localStringBuilder, paramCharSequence, 0, i);
      return localStringBuilder.toString();
    }
  }

  static abstract interface ShareCompatImpl
  {
    public abstract void configureMenuItem(MenuItem paramMenuItem, ShareCompat.IntentBuilder paramIntentBuilder);

    public abstract String escapeHtml(CharSequence paramCharSequence);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.ShareCompat
 * JD-Core Version:    0.6.2
 */