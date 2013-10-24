package android.support.v7.widget;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.widget.ResourceCursorAdapter;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.id;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.WeakHashMap;

class SuggestionsAdapter extends ResourceCursorAdapter
  implements View.OnClickListener
{
  private boolean mClosed = false;
  private int mFlagsCol = -1;
  private int mIconName1Col = -1;
  private int mIconName2Col = -1;
  private WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache;
  private Context mProviderContext;
  private int mQueryRefinement = 1;
  private SearchManager mSearchManager;
  private SearchView mSearchView;
  private SearchableInfo mSearchable;
  private int mText1Col = -1;
  private int mText2Col = -1;
  private int mText2UrlCol = -1;
  private ColorStateList mUrlColor;

  public SuggestionsAdapter(Context paramContext, SearchView paramSearchView, SearchableInfo paramSearchableInfo, WeakHashMap<String, Drawable.ConstantState> paramWeakHashMap)
  {
    super(paramContext, i, null, true);
    SearchManager localSearchManager = (SearchManager)this.mContext.getSystemService("search");
    this.mSearchManager = localSearchManager;
    this.mSearchView = paramSearchView;
    this.mSearchable = paramSearchableInfo;
    this.mProviderContext = paramContext;
    this.mOutsideDrawablesCache = paramWeakHashMap;
  }

  private Drawable checkIconCache(String paramString)
  {
    Drawable.ConstantState localConstantState = (Drawable.ConstantState)this.mOutsideDrawablesCache.get(paramString);
    if (localConstantState == null);
    for (Drawable localDrawable = null; ; localDrawable = localConstantState.newDrawable())
      return localDrawable;
  }

  private CharSequence formatUrl(CharSequence paramCharSequence)
  {
    if (this.mUrlColor == null)
    {
      TypedValue localTypedValue = new TypedValue();
      Resources.Theme localTheme = this.mContext.getTheme();
      int i = R.attr.textColorSearchUrl;
      boolean bool = localTheme.resolveAttribute(i, localTypedValue, true);
      Resources localResources = this.mContext.getResources();
      int j = localTypedValue.resourceId;
      ColorStateList localColorStateList1 = localResources.getColorStateList(j);
      this.mUrlColor = localColorStateList1;
    }
    SpannableString localSpannableString = new SpannableString(paramCharSequence);
    ColorStateList localColorStateList2 = this.mUrlColor;
    int k = 0;
    ColorStateList localColorStateList3 = null;
    TextAppearanceSpan localTextAppearanceSpan = new TextAppearanceSpan(null, 0, k, localColorStateList2, localColorStateList3);
    int m = paramCharSequence.length();
    localSpannableString.setSpan(localTextAppearanceSpan, 0, m, 33);
    return localSpannableString;
  }

  private Drawable getActivityIcon(ComponentName paramComponentName)
  {
    PackageManager localPackageManager = this.mContext.getPackageManager();
    try
    {
      ActivityInfo localActivityInfo1 = localPackageManager.getActivityInfo(paramComponentName, 128);
      localActivityInfo2 = localActivityInfo1;
      i = localActivityInfo2.getIconResource();
      if (i == 0)
      {
        localDrawable = null;
        return localDrawable;
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      while (true)
      {
        ActivityInfo localActivityInfo2;
        int i;
        String str1 = localNameNotFoundException.toString();
        int j = Log.w("SuggestionsAdapter", str1);
        Drawable localDrawable = null;
        continue;
        String str2 = paramComponentName.getPackageName();
        ApplicationInfo localApplicationInfo = localActivityInfo2.applicationInfo;
        localDrawable = localPackageManager.getDrawable(str2, i, localApplicationInfo);
        if (localDrawable == null)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("Invalid icon resource ").append(i).append(" for ");
          String str3 = paramComponentName.flattenToShortString();
          String str4 = str3;
          int k = Log.w("SuggestionsAdapter", str4);
          localDrawable = null;
        }
      }
    }
  }

  private Drawable getActivityIconWithCache(ComponentName paramComponentName)
  {
    Object localObject1 = null;
    String str = paramComponentName.flattenToShortString();
    if (this.mOutsideDrawablesCache.containsKey(str))
    {
      Drawable.ConstantState localConstantState = (Drawable.ConstantState)this.mOutsideDrawablesCache.get(str);
      if (localConstantState == null);
      while (true)
      {
        return localObject1;
        Resources localResources = this.mProviderContext.getResources();
        localObject1 = localConstantState.newDrawable(localResources);
      }
    }
    Drawable localDrawable = getActivityIcon(paramComponentName);
    if (localDrawable == null);
    for (Object localObject2 = null; ; localObject2 = localDrawable.getConstantState())
    {
      Object localObject3 = this.mOutsideDrawablesCache.put(str, localObject2);
      localObject1 = localDrawable;
      break;
    }
  }

  public static String getColumnString(Cursor paramCursor, String paramString)
  {
    int i = paramCursor.getColumnIndex(paramString);
    return getStringOrNull(paramCursor, i);
  }

  private Drawable getDefaultIcon1(Cursor paramCursor)
  {
    ComponentName localComponentName = this.mSearchable.getSearchActivity();
    Drawable localDrawable = getActivityIconWithCache(localComponentName);
    if (localDrawable != null);
    while (true)
    {
      return localDrawable;
      localDrawable = this.mContext.getPackageManager().getDefaultActivityIcon();
    }
  }

  // ERROR //
  private Drawable getDrawable(Uri paramUri)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 272	android/net/Uri:getScheme	()Ljava/lang/String;
    //   4: astore_2
    //   5: ldc_w 274
    //   8: aload_2
    //   9: invokevirtual 279	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   12: astore_3
    //   13: aload_3
    //   14: ifnull +110 -> 124
    //   17: aload_0
    //   18: aload_1
    //   19: invokevirtual 282	android/support/v7/widget/SuggestionsAdapter:getDrawableFromResourceUri	(Landroid/net/Uri;)Landroid/graphics/drawable/Drawable;
    //   22: astore_3
    //   23: aload_3
    //   24: astore 4
    //   26: aload 4
    //   28: areturn
    //   29: astore 5
    //   31: new 199	java/lang/StringBuilder
    //   34: dup
    //   35: invokespecial 200	java/lang/StringBuilder:<init>	()V
    //   38: ldc_w 284
    //   41: invokevirtual 206	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   44: aload_1
    //   45: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   48: invokevirtual 215	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   51: astore 6
    //   53: new 263	java/io/FileNotFoundException
    //   56: dup
    //   57: aload 6
    //   59: invokespecial 290	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
    //   62: athrow
    //   63: astore 7
    //   65: new 199	java/lang/StringBuilder
    //   68: dup
    //   69: invokespecial 200	java/lang/StringBuilder:<init>	()V
    //   72: ldc_w 292
    //   75: invokevirtual 206	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   78: aload_1
    //   79: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   82: ldc_w 294
    //   85: invokevirtual 206	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   88: astore 8
    //   90: aload 7
    //   92: invokevirtual 297	java/io/FileNotFoundException:getMessage	()Ljava/lang/String;
    //   95: astore 9
    //   97: aload 8
    //   99: aload 9
    //   101: invokevirtual 206	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: invokevirtual 215	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   107: astore 10
    //   109: ldc 178
    //   111: aload 10
    //   113: invokestatic 184	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   116: istore 11
    //   118: aconst_null
    //   119: astore 4
    //   121: goto -95 -> 26
    //   124: aload_0
    //   125: getfield 80	android/support/v7/widget/SuggestionsAdapter:mProviderContext	Landroid/content/Context;
    //   128: invokevirtual 301	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   131: aload_1
    //   132: invokevirtual 307	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
    //   135: astore 12
    //   137: aload 12
    //   139: ifnonnull +35 -> 174
    //   142: new 199	java/lang/StringBuilder
    //   145: dup
    //   146: invokespecial 200	java/lang/StringBuilder:<init>	()V
    //   149: ldc_w 309
    //   152: invokevirtual 206	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: aload_1
    //   156: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   159: invokevirtual 215	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   162: astore 13
    //   164: new 263	java/io/FileNotFoundException
    //   167: dup
    //   168: aload 13
    //   170: invokespecial 290	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
    //   173: athrow
    //   174: aconst_null
    //   175: astore 14
    //   177: aload 12
    //   179: aload 14
    //   181: invokestatic 313	android/graphics/drawable/Drawable:createFromStream	(Ljava/io/InputStream;Ljava/lang/String;)Landroid/graphics/drawable/Drawable;
    //   184: astore_3
    //   185: aload_3
    //   186: astore 4
    //   188: aload 12
    //   190: invokevirtual 318	java/io/InputStream:close	()V
    //   193: goto -167 -> 26
    //   196: astore 15
    //   198: new 199	java/lang/StringBuilder
    //   201: dup
    //   202: invokespecial 200	java/lang/StringBuilder:<init>	()V
    //   205: ldc_w 320
    //   208: invokevirtual 206	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   211: aload_1
    //   212: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   215: invokevirtual 215	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   218: astore 16
    //   220: ldc 178
    //   222: aload 16
    //   224: aload 15
    //   226: invokestatic 324	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   229: istore 17
    //   231: goto -205 -> 26
    //   234: astore 18
    //   236: aload 12
    //   238: invokevirtual 318	java/io/InputStream:close	()V
    //   241: aload 18
    //   243: athrow
    //   244: astore 19
    //   246: new 199	java/lang/StringBuilder
    //   249: dup
    //   250: invokespecial 200	java/lang/StringBuilder:<init>	()V
    //   253: ldc_w 320
    //   256: invokevirtual 206	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   259: aload_1
    //   260: invokevirtual 287	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   263: invokevirtual 215	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   266: astore 20
    //   268: ldc 178
    //   270: aload 20
    //   272: aload 19
    //   274: invokestatic 324	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   277: istore 21
    //   279: goto -38 -> 241
    //
    // Exception table:
    //   from	to	target	type
    //   17	23	29	android/content/res/Resources$NotFoundException
    //   0	13	63	java/io/FileNotFoundException
    //   17	23	63	java/io/FileNotFoundException
    //   31	63	63	java/io/FileNotFoundException
    //   124	174	63	java/io/FileNotFoundException
    //   188	193	63	java/io/FileNotFoundException
    //   198	231	63	java/io/FileNotFoundException
    //   236	241	63	java/io/FileNotFoundException
    //   241	279	63	java/io/FileNotFoundException
    //   188	193	196	java/io/IOException
    //   177	185	234	finally
    //   236	241	244	java/io/IOException
  }

  private Drawable getDrawableFromResourceValue(String paramString)
  {
    Drawable localDrawable;
    if ((paramString == null) || (paramString.length() == 0) || ("0".equals(paramString)))
      localDrawable = null;
    while (true)
    {
      return localDrawable;
      try
      {
        int i = Integer.parseInt(paramString);
        StringBuilder localStringBuilder = new StringBuilder().append("android.resource://");
        String str1 = this.mProviderContext.getPackageName();
        String str2 = str1 + "/" + i;
        localDrawable = checkIconCache(str2);
        if (localDrawable == null)
        {
          localDrawable = this.mProviderContext.getResources().getDrawable(i);
          storeInIconCache(str2, localDrawable);
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        localDrawable = checkIconCache(paramString);
        if (localDrawable == null)
        {
          Uri localUri = Uri.parse(paramString);
          localDrawable = getDrawable(localUri);
          storeInIconCache(paramString, localDrawable);
        }
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        String str3 = "Icon resource not found: " + paramString;
        int j = Log.w("SuggestionsAdapter", str3);
        localDrawable = null;
      }
    }
  }

  private Drawable getIcon1(Cursor paramCursor)
  {
    Drawable localDrawable;
    if (this.mIconName1Col == -1)
      localDrawable = null;
    while (true)
    {
      return localDrawable;
      int i = this.mIconName1Col;
      String str = paramCursor.getString(i);
      localDrawable = getDrawableFromResourceValue(str);
      if (localDrawable == null)
        localDrawable = getDefaultIcon1(paramCursor);
    }
  }

  private Drawable getIcon2(Cursor paramCursor)
  {
    if (this.mIconName2Col == -1);
    String str;
    for (Drawable localDrawable = null; ; localDrawable = getDrawableFromResourceValue(str))
    {
      return localDrawable;
      int i = this.mIconName2Col;
      str = paramCursor.getString(i);
    }
  }

  private static String getStringOrNull(Cursor paramCursor, int paramInt)
  {
    Object localObject = null;
    if (paramInt == -1);
    while (true)
    {
      return localObject;
      try
      {
        String str = paramCursor.getString(paramInt);
        localObject = str;
      }
      catch (Exception localException)
      {
        int i = Log.e("SuggestionsAdapter", "unexpected error retrieving valid column from cursor, did the remote process die?", localException);
      }
    }
  }

  private void setViewDrawable(ImageView paramImageView, Drawable paramDrawable, int paramInt)
  {
    paramImageView.setImageDrawable(paramDrawable);
    if (paramDrawable == null)
    {
      paramImageView.setVisibility(paramInt);
      return;
    }
    paramImageView.setVisibility(0);
    boolean bool1 = paramDrawable.setVisible(false, false);
    boolean bool2 = paramDrawable.setVisible(true, false);
  }

  private void setViewText(TextView paramTextView, CharSequence paramCharSequence)
  {
    paramTextView.setText(paramCharSequence);
    if (TextUtils.isEmpty(paramCharSequence))
    {
      paramTextView.setVisibility(8);
      return;
    }
    paramTextView.setVisibility(0);
  }

  private void storeInIconCache(String paramString, Drawable paramDrawable)
  {
    if (paramDrawable == null)
      return;
    WeakHashMap localWeakHashMap = this.mOutsideDrawablesCache;
    Drawable.ConstantState localConstantState = paramDrawable.getConstantState();
    Object localObject = localWeakHashMap.put(paramString, localConstantState);
  }

  private void updateSpinnerState(Cursor paramCursor)
  {
    if (paramCursor != null);
    for (Bundle localBundle = paramCursor.getExtras(); ; localBundle = null)
    {
      if (localBundle == null)
        return;
      if (!localBundle.getBoolean("in_progress"))
        return;
      return;
    }
  }

  public void bindView(View paramView, Context paramContext, Cursor paramCursor)
  {
    ChildViewCache localChildViewCache = (ChildViewCache)paramView.getTag();
    int i = 0;
    if (this.mFlagsCol != -1)
    {
      int j = this.mFlagsCol;
      i = paramCursor.getInt(j);
    }
    if (localChildViewCache.mText1 != null)
    {
      int k = this.mText1Col;
      String str1 = getStringOrNull(paramCursor, k);
      TextView localTextView1 = localChildViewCache.mText1;
      setViewText(localTextView1, str1);
    }
    Object localObject;
    if (localChildViewCache.mText2 != null)
    {
      int m = this.mText2UrlCol;
      String str2 = getStringOrNull(paramCursor, m);
      if (str2 == null)
        break label287;
      localObject = formatUrl(str2);
      if (!TextUtils.isEmpty((CharSequence)localObject))
        break label304;
      if (localChildViewCache.mText1 != null)
      {
        localChildViewCache.mText1.setSingleLine(false);
        localChildViewCache.mText1.setMaxLines(2);
      }
    }
    while (true)
    {
      TextView localTextView2 = localChildViewCache.mText2;
      setViewText(localTextView2, (CharSequence)localObject);
      if (localChildViewCache.mIcon1 != null)
      {
        ImageView localImageView1 = localChildViewCache.mIcon1;
        Drawable localDrawable1 = getIcon1(paramCursor);
        setViewDrawable(localImageView1, localDrawable1, 4);
      }
      if (localChildViewCache.mIcon2 != null)
      {
        ImageView localImageView2 = localChildViewCache.mIcon2;
        Drawable localDrawable2 = getIcon2(paramCursor);
        setViewDrawable(localImageView2, localDrawable2, 8);
      }
      if ((this.mQueryRefinement != 2) && ((this.mQueryRefinement != 1) || ((i & 0x1) == 0)))
        break label333;
      localChildViewCache.mIconRefine.setVisibility(0);
      ImageView localImageView3 = localChildViewCache.mIconRefine;
      CharSequence localCharSequence = localChildViewCache.mText1.getText();
      localImageView3.setTag(localCharSequence);
      localChildViewCache.mIconRefine.setOnClickListener(this);
      return;
      label287: int n = this.mText2Col;
      localObject = getStringOrNull(paramCursor, n);
      break;
      label304: if (localChildViewCache.mText1 != null)
      {
        localChildViewCache.mText1.setSingleLine(true);
        localChildViewCache.mText1.setMaxLines(1);
      }
    }
    label333: localChildViewCache.mIconRefine.setVisibility(8);
  }

  public void changeCursor(Cursor paramCursor)
  {
    if (this.mClosed)
    {
      int i = Log.w("SuggestionsAdapter", "Tried to change cursor after adapter was closed.");
      if (paramCursor == null)
        return;
      paramCursor.close();
      return;
    }
    try
    {
      super.changeCursor(paramCursor);
      if (paramCursor == null)
        return;
      int j = paramCursor.getColumnIndex("suggest_text_1");
      this.mText1Col = j;
      int k = paramCursor.getColumnIndex("suggest_text_2");
      this.mText2Col = k;
      int m = paramCursor.getColumnIndex("suggest_text_2_url");
      this.mText2UrlCol = m;
      int n = paramCursor.getColumnIndex("suggest_icon_1");
      this.mIconName1Col = n;
      int i1 = paramCursor.getColumnIndex("suggest_icon_2");
      this.mIconName2Col = i1;
      int i2 = paramCursor.getColumnIndex("suggest_flags");
      this.mFlagsCol = i2;
      return;
    }
    catch (Exception localException)
    {
      int i3 = Log.e("SuggestionsAdapter", "error changing cursor and caching columns", localException);
    }
  }

  public CharSequence convertToString(Cursor paramCursor)
  {
    Object localObject;
    if (paramCursor == null)
      localObject = null;
    while (true)
    {
      return localObject;
      localObject = getColumnString(paramCursor, "suggest_intent_query");
      if (localObject == null)
        if (this.mSearchable.shouldRewriteQueryFromData())
        {
          String str1 = getColumnString(paramCursor, "suggest_intent_data");
          if (str1 != null)
            localObject = str1;
        }
        else if (this.mSearchable.shouldRewriteQueryFromText())
        {
          String str2 = getColumnString(paramCursor, "suggest_text_1");
          if (str2 != null)
            localObject = str2;
        }
        else
        {
          localObject = null;
        }
    }
  }

  Drawable getDrawableFromResourceUri(Uri paramUri)
    throws FileNotFoundException
  {
    String str1 = paramUri.getAuthority();
    if (TextUtils.isEmpty(str1))
    {
      String str2 = "No authority: " + paramUri;
      throw new FileNotFoundException(str2);
    }
    Resources localResources2;
    List localList;
    try
    {
      Resources localResources1 = this.mContext.getPackageManager().getResourcesForApplication(str1);
      localResources2 = localResources1;
      localList = paramUri.getPathSegments();
      if (localList == null)
      {
        String str3 = "No path: " + paramUri;
        throw new FileNotFoundException(str3);
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      String str4 = "No package found for authority: " + paramUri;
      throw new FileNotFoundException(str4);
    }
    int i = localList.size();
    int j;
    if (i == 1)
      j = 0;
    int m;
    while (true)
    {
      try
      {
        int k = Integer.parseInt((String)localList.get(j));
        m = k;
        if (m != 0)
          break;
        String str5 = "No resource found for: " + paramUri;
        throw new FileNotFoundException(str5);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        String str6 = "Single path segment is not a resource ID: " + paramUri;
        throw new FileNotFoundException(str6);
      }
      if (i == 2)
      {
        String str7 = (String)localList.get(1);
        String str8 = (String)localList.get(0);
        m = localResources2.getIdentifier(str7, str8, str1);
      }
      else
      {
        String str9 = "More than two path segments: " + paramUri;
        throw new FileNotFoundException(str9);
      }
    }
    return localResources2.getDrawable(m);
  }

  Cursor getSearchManagerSuggestions(SearchableInfo paramSearchableInfo, String paramString, int paramInt)
  {
    Cursor localCursor = null;
    if (paramSearchableInfo == null);
    String str1;
    do
    {
      return localCursor;
      str1 = paramSearchableInfo.getSuggestAuthority();
    }
    while (str1 == null);
    Uri.Builder localBuilder1 = new Uri.Builder().scheme("content").authority(str1).query("").fragment("");
    String str2 = paramSearchableInfo.getSuggestPath();
    if (str2 != null)
      Uri.Builder localBuilder2 = localBuilder1.appendEncodedPath(str2);
    Uri.Builder localBuilder3 = localBuilder1.appendPath("search_suggest_query");
    String str3 = paramSearchableInfo.getSuggestSelection();
    String[] arrayOfString = null;
    if (str3 != null)
    {
      arrayOfString = new String[1];
      arrayOfString[0] = paramString;
    }
    while (true)
    {
      if (paramInt > 0)
      {
        String str4 = String.valueOf(paramInt);
        Uri.Builder localBuilder4 = localBuilder1.appendQueryParameter("limit", str4);
      }
      Uri localUri = localBuilder1.build();
      ContentResolver localContentResolver = this.mContext.getContentResolver();
      String str5 = null;
      localCursor = localContentResolver.query(localUri, null, str3, arrayOfString, str5);
      break;
      Uri.Builder localBuilder5 = localBuilder1.appendPath(paramString);
    }
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    try
    {
      View localView1 = super.getView(paramInt, paramView, paramViewGroup);
      localView2 = localView1;
      return localView2;
    }
    catch (RuntimeException localRuntimeException)
    {
      while (true)
      {
        int i = Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", localRuntimeException);
        Context localContext = this.mContext;
        Cursor localCursor = this.mCursor;
        View localView2 = newView(localContext, localCursor, paramViewGroup);
        if (localView2 != null)
        {
          TextView localTextView = ((ChildViewCache)localView2.getTag()).mText1;
          String str = localRuntimeException.toString();
          localTextView.setText(str);
        }
      }
    }
  }

  public boolean hasStableIds()
  {
    return false;
  }

  public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
  {
    View localView = super.newView(paramContext, paramCursor, paramViewGroup);
    ChildViewCache localChildViewCache = new ChildViewCache(localView);
    localView.setTag(localChildViewCache);
    return localView;
  }

  public void notifyDataSetChanged()
  {
    super.notifyDataSetChanged();
    Cursor localCursor = getCursor();
    updateSpinnerState(localCursor);
  }

  public void notifyDataSetInvalidated()
  {
    super.notifyDataSetInvalidated();
    Cursor localCursor = getCursor();
    updateSpinnerState(localCursor);
  }

  public void onClick(View paramView)
  {
    Object localObject = paramView.getTag();
    if (!(localObject instanceof CharSequence))
      return;
    SearchView localSearchView = this.mSearchView;
    CharSequence localCharSequence = (CharSequence)localObject;
    localSearchView.onQueryRefine(localCharSequence);
  }

  public Cursor runQueryOnBackgroundThread(CharSequence paramCharSequence)
  {
    Object localObject = null;
    String str;
    if (paramCharSequence == null)
    {
      str = "";
      if ((this.mSearchView.getVisibility() == 0) && (this.mSearchView.getWindowVisibility() == 0))
        break label40;
    }
    while (true)
    {
      return localObject;
      str = paramCharSequence.toString();
      break;
      try
      {
        label40: SearchableInfo localSearchableInfo = this.mSearchable;
        Cursor localCursor = getSearchManagerSuggestions(localSearchableInfo, str, 50);
        if (localCursor != null)
        {
          int i = localCursor.getCount();
          localObject = localCursor;
        }
      }
      catch (RuntimeException localRuntimeException)
      {
        int j = Log.w("SuggestionsAdapter", "Search suggestions query threw an exception.", localRuntimeException);
      }
    }
  }

  public void setQueryRefinement(int paramInt)
  {
    this.mQueryRefinement = paramInt;
  }

  private static final class ChildViewCache
  {
    public final ImageView mIcon1;
    public final ImageView mIcon2;
    public final ImageView mIconRefine;
    public final TextView mText1;
    public final TextView mText2;

    public ChildViewCache(View paramView)
    {
      TextView localTextView1 = (TextView)paramView.findViewById(16908308);
      this.mText1 = localTextView1;
      TextView localTextView2 = (TextView)paramView.findViewById(16908309);
      this.mText2 = localTextView2;
      ImageView localImageView1 = (ImageView)paramView.findViewById(16908295);
      this.mIcon1 = localImageView1;
      ImageView localImageView2 = (ImageView)paramView.findViewById(16908296);
      this.mIcon2 = localImageView2;
      int i = R.id.edit_query;
      ImageView localImageView3 = (ImageView)paramView.findViewById(i);
      this.mIconRefine = localImageView3;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.widget.SuggestionsAdapter
 * JD-Core Version:    0.6.2
 */