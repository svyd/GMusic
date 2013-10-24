package android.support.v7.widget;

import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.dimen;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.layout;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.view.CollapsibleActionView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

public class SearchView extends LinearLayout
  implements CollapsibleActionView
{
  static final AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER = new AutoCompleteTextViewReflector();
  private Bundle mAppSearchData;
  private boolean mClearingFocus;
  private ImageView mCloseButton;
  private int mCollapsedImeOptions;
  private View mDropDownAnchor;
  private boolean mExpandedInActionView;
  private boolean mIconified;
  private boolean mIconifiedByDefault;
  private int mMaxWidth;
  private CharSequence mOldQueryText;
  private final View.OnClickListener mOnClickListener;
  private OnCloseListener mOnCloseListener;
  private final TextView.OnEditorActionListener mOnEditorActionListener;
  private final AdapterView.OnItemClickListener mOnItemClickListener;
  private final AdapterView.OnItemSelectedListener mOnItemSelectedListener;
  private OnQueryTextListener mOnQueryChangeListener;
  private View.OnFocusChangeListener mOnQueryTextFocusChangeListener;
  private View.OnClickListener mOnSearchClickListener;
  private OnSuggestionListener mOnSuggestionListener;
  private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache;
  private CharSequence mQueryHint;
  private boolean mQueryRefinement;
  private SearchAutoComplete mQueryTextView;
  private Runnable mReleaseCursorRunnable;
  private View mSearchButton;
  private View mSearchEditFrame;
  private ImageView mSearchHintIcon;
  private View mSearchPlate;
  private SearchableInfo mSearchable;
  private Runnable mShowImeRunnable;
  private View mSubmitArea;
  private View mSubmitButton;
  private boolean mSubmitButtonEnabled;
  private CursorAdapter mSuggestionsAdapter;
  View.OnKeyListener mTextKeyListener;
  private TextWatcher mTextWatcher;
  private Runnable mUpdateDrawableStateRunnable;
  private CharSequence mUserQuery;
  private final Intent mVoiceAppSearchIntent;
  private View mVoiceButton;
  private boolean mVoiceButtonEnabled;
  private final Intent mVoiceWebSearchIntent;

  public SearchView(Context paramContext)
  {
    this(paramContext, null);
  }

  public SearchView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        InputMethodManager localInputMethodManager = (InputMethodManager)SearchView.this.getContext().getSystemService("input_method");
        if (localInputMethodManager == null)
          return;
        SearchView.AutoCompleteTextViewReflector localAutoCompleteTextViewReflector = SearchView.HIDDEN_METHOD_INVOKER;
        SearchView localSearchView = SearchView.this;
        localAutoCompleteTextViewReflector.showSoftInputUnchecked(localInputMethodManager, localSearchView, 0);
      }
    };
    this.mShowImeRunnable = local1;
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        SearchView.this.updateFocusedState();
      }
    };
    this.mUpdateDrawableStateRunnable = local2;
    Runnable local3 = new Runnable()
    {
      public void run()
      {
        if (SearchView.this.mSuggestionsAdapter == null)
          return;
        if (!(SearchView.this.mSuggestionsAdapter instanceof SuggestionsAdapter))
          return;
        SearchView.this.mSuggestionsAdapter.changeCursor(null);
      }
    };
    this.mReleaseCursorRunnable = local3;
    WeakHashMap localWeakHashMap = new WeakHashMap();
    this.mOutsideDrawablesCache = localWeakHashMap;
    View.OnClickListener local7 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        View localView1 = SearchView.this.mSearchButton;
        if (paramAnonymousView == localView1)
        {
          SearchView.this.onSearchClicked();
          return;
        }
        ImageView localImageView = SearchView.this.mCloseButton;
        if (paramAnonymousView == localImageView)
        {
          SearchView.this.onCloseClicked();
          return;
        }
        View localView2 = SearchView.this.mSubmitButton;
        if (paramAnonymousView == localView2)
        {
          SearchView.this.onSubmitQuery();
          return;
        }
        View localView3 = SearchView.this.mVoiceButton;
        if (paramAnonymousView == localView3)
        {
          SearchView.this.onVoiceClicked();
          return;
        }
        SearchView.SearchAutoComplete localSearchAutoComplete = SearchView.this.mQueryTextView;
        if (paramAnonymousView != localSearchAutoComplete)
          return;
        SearchView.this.forceSuggestionQuery();
      }
    };
    this.mOnClickListener = local7;
    View.OnKeyListener local8 = new View.OnKeyListener()
    {
      public boolean onKey(View paramAnonymousView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
      {
        boolean bool = false;
        if (SearchView.this.mSearchable == null);
        while (true)
        {
          return bool;
          if ((SearchView.this.mQueryTextView.isPopupShowing()) && (SearchView.this.mQueryTextView.getListSelection() != -1))
          {
            bool = SearchView.this.onSuggestionsKey(paramAnonymousView, paramAnonymousInt, paramAnonymousKeyEvent);
          }
          else if ((!SearchView.SearchAutoComplete.access$1600(SearchView.this.mQueryTextView)) && (KeyEventCompat.hasNoModifiers(paramAnonymousKeyEvent)) && (paramAnonymousKeyEvent.getAction() == 1) && (paramAnonymousInt == 66))
          {
            paramAnonymousView.cancelLongPress();
            SearchView localSearchView = SearchView.this;
            String str = SearchView.this.mQueryTextView.getText().toString();
            localSearchView.launchQuerySearch(0, null, str);
            bool = true;
          }
        }
      }
    };
    this.mTextKeyListener = local8;
    TextView.OnEditorActionListener local9 = new TextView.OnEditorActionListener()
    {
      public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
      {
        SearchView.this.onSubmitQuery();
        return true;
      }
    };
    this.mOnEditorActionListener = local9;
    AdapterView.OnItemClickListener local10 = new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        boolean bool = SearchView.this.onItemClicked(paramAnonymousInt, 0, null);
      }
    };
    this.mOnItemClickListener = local10;
    AdapterView.OnItemSelectedListener local11 = new AdapterView.OnItemSelectedListener()
    {
      public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        boolean bool = SearchView.this.onItemSelected(paramAnonymousInt);
      }

      public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView)
      {
      }
    };
    this.mOnItemSelectedListener = local11;
    TextWatcher local12 = new TextWatcher()
    {
      public void afterTextChanged(Editable paramAnonymousEditable)
      {
      }

      public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
      }

      public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
        SearchView.this.onTextChanged(paramAnonymousCharSequence);
      }
    };
    this.mTextWatcher = local12;
    LayoutInflater localLayoutInflater = (LayoutInflater)paramContext.getSystemService("layout_inflater");
    int i = R.layout.abc_search_view;
    View localView1 = localLayoutInflater.inflate(i, this, true);
    int j = R.id.search_button;
    View localView2 = findViewById(j);
    this.mSearchButton = localView2;
    int k = R.id.search_src_text;
    SearchAutoComplete localSearchAutoComplete1 = (SearchAutoComplete)findViewById(k);
    this.mQueryTextView = localSearchAutoComplete1;
    this.mQueryTextView.setSearchView(this);
    int m = R.id.search_edit_frame;
    View localView3 = findViewById(m);
    this.mSearchEditFrame = localView3;
    int n = R.id.search_plate;
    View localView4 = findViewById(n);
    this.mSearchPlate = localView4;
    int i1 = R.id.submit_area;
    View localView5 = findViewById(i1);
    this.mSubmitArea = localView5;
    int i2 = R.id.search_go_btn;
    View localView6 = findViewById(i2);
    this.mSubmitButton = localView6;
    int i3 = R.id.search_close_btn;
    ImageView localImageView1 = (ImageView)findViewById(i3);
    this.mCloseButton = localImageView1;
    int i4 = R.id.search_voice_btn;
    View localView7 = findViewById(i4);
    this.mVoiceButton = localView7;
    int i5 = R.id.search_mag_icon;
    ImageView localImageView2 = (ImageView)findViewById(i5);
    this.mSearchHintIcon = localImageView2;
    View localView8 = this.mSearchButton;
    View.OnClickListener localOnClickListener1 = this.mOnClickListener;
    localView8.setOnClickListener(localOnClickListener1);
    ImageView localImageView3 = this.mCloseButton;
    View.OnClickListener localOnClickListener2 = this.mOnClickListener;
    localImageView3.setOnClickListener(localOnClickListener2);
    View localView9 = this.mSubmitButton;
    View.OnClickListener localOnClickListener3 = this.mOnClickListener;
    localView9.setOnClickListener(localOnClickListener3);
    View localView10 = this.mVoiceButton;
    View.OnClickListener localOnClickListener4 = this.mOnClickListener;
    localView10.setOnClickListener(localOnClickListener4);
    SearchAutoComplete localSearchAutoComplete2 = this.mQueryTextView;
    View.OnClickListener localOnClickListener5 = this.mOnClickListener;
    localSearchAutoComplete2.setOnClickListener(localOnClickListener5);
    SearchAutoComplete localSearchAutoComplete3 = this.mQueryTextView;
    TextWatcher localTextWatcher = this.mTextWatcher;
    localSearchAutoComplete3.addTextChangedListener(localTextWatcher);
    SearchAutoComplete localSearchAutoComplete4 = this.mQueryTextView;
    TextView.OnEditorActionListener localOnEditorActionListener = this.mOnEditorActionListener;
    localSearchAutoComplete4.setOnEditorActionListener(localOnEditorActionListener);
    SearchAutoComplete localSearchAutoComplete5 = this.mQueryTextView;
    AdapterView.OnItemClickListener localOnItemClickListener = this.mOnItemClickListener;
    localSearchAutoComplete5.setOnItemClickListener(localOnItemClickListener);
    SearchAutoComplete localSearchAutoComplete6 = this.mQueryTextView;
    AdapterView.OnItemSelectedListener localOnItemSelectedListener = this.mOnItemSelectedListener;
    localSearchAutoComplete6.setOnItemSelectedListener(localOnItemSelectedListener);
    SearchAutoComplete localSearchAutoComplete7 = this.mQueryTextView;
    View.OnKeyListener localOnKeyListener = this.mTextKeyListener;
    localSearchAutoComplete7.setOnKeyListener(localOnKeyListener);
    SearchAutoComplete localSearchAutoComplete8 = this.mQueryTextView;
    View.OnFocusChangeListener local4 = new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramAnonymousView, boolean paramAnonymousBoolean)
      {
        if (SearchView.this.mOnQueryTextFocusChangeListener == null)
          return;
        View.OnFocusChangeListener localOnFocusChangeListener = SearchView.this.mOnQueryTextFocusChangeListener;
        SearchView localSearchView = SearchView.this;
        localOnFocusChangeListener.onFocusChange(localSearchView, paramAnonymousBoolean);
      }
    };
    localSearchAutoComplete8.setOnFocusChangeListener(local4);
    int[] arrayOfInt1 = R.styleable.SearchView;
    TypedArray localTypedArray1 = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt1, 0, 0);
    boolean bool1 = localTypedArray1.getBoolean(3, true);
    setIconifiedByDefault(bool1);
    int i6 = localTypedArray1.getDimensionPixelSize(0, -1);
    if (i6 != -1)
      setMaxWidth(i6);
    CharSequence localCharSequence = localTypedArray1.getText(4);
    if (!TextUtils.isEmpty(localCharSequence))
      setQueryHint(localCharSequence);
    int i7 = localTypedArray1.getInt(2, -1);
    if (i7 != -1)
      setImeOptions(i7);
    int i8 = localTypedArray1.getInt(1, -1);
    if (i8 != -1)
      setInputType(i8);
    localTypedArray1.recycle();
    int[] arrayOfInt2 = R.styleable.View;
    TypedArray localTypedArray2 = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt2, 0, 0);
    boolean bool2 = localTypedArray2.getBoolean(0, true);
    localTypedArray2.recycle();
    setFocusable(bool2);
    Intent localIntent1 = new Intent("android.speech.action.WEB_SEARCH");
    this.mVoiceWebSearchIntent = localIntent1;
    Intent localIntent2 = this.mVoiceWebSearchIntent.addFlags(268435456);
    Intent localIntent3 = this.mVoiceWebSearchIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
    Intent localIntent4 = new Intent("android.speech.action.RECOGNIZE_SPEECH");
    this.mVoiceAppSearchIntent = localIntent4;
    Intent localIntent5 = this.mVoiceAppSearchIntent.addFlags(268435456);
    int i9 = this.mQueryTextView.getDropDownAnchor();
    View localView11 = findViewById(i9);
    this.mDropDownAnchor = localView11;
    if (this.mDropDownAnchor != null)
    {
      if (Build.VERSION.SDK_INT < 11)
        break label888;
      addOnLayoutChangeListenerToDropDownAnchorSDK11();
    }
    while (true)
    {
      boolean bool3 = this.mIconifiedByDefault;
      updateViewsVisibility(bool3);
      updateQueryHint();
      return;
      label888: addOnLayoutChangeListenerToDropDownAnchorBase();
    }
  }

  private void addOnLayoutChangeListenerToDropDownAnchorBase()
  {
    ViewTreeObserver localViewTreeObserver = this.mDropDownAnchor.getViewTreeObserver();
    ViewTreeObserver.OnGlobalLayoutListener local6 = new ViewTreeObserver.OnGlobalLayoutListener()
    {
      public void onGlobalLayout()
      {
        SearchView.this.adjustDropDownSizeAndPosition();
      }
    };
    localViewTreeObserver.addOnGlobalLayoutListener(local6);
  }

  private void addOnLayoutChangeListenerToDropDownAnchorSDK11()
  {
    View localView = this.mDropDownAnchor;
    View.OnLayoutChangeListener local5 = new View.OnLayoutChangeListener()
    {
      public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8)
      {
        SearchView.this.adjustDropDownSizeAndPosition();
      }
    };
    localView.addOnLayoutChangeListener(local5);
  }

  private void adjustDropDownSizeAndPosition()
  {
    if (this.mDropDownAnchor.getWidth() <= 1)
      return;
    Resources localResources = getContext().getResources();
    int i = this.mSearchPlate.getPaddingLeft();
    Rect localRect = new Rect();
    int k;
    int n;
    if (this.mIconifiedByDefault)
    {
      int j = R.dimen.abc_dropdownitem_icon_width;
      k = localResources.getDimensionPixelSize(j);
      int m = R.dimen.abc_dropdownitem_text_padding_left;
      n = localResources.getDimensionPixelSize(m);
    }
    for (int i1 = k + n; ; i1 = 0)
    {
      boolean bool = this.mQueryTextView.getDropDownBackground().getPadding(localRect);
      int i2 = localRect.left + i1;
      int i3 = i - i2;
      this.mQueryTextView.setDropDownHorizontalOffset(i3);
      int i4 = this.mDropDownAnchor.getWidth();
      int i5 = localRect.left;
      int i6 = i4 + i5;
      int i7 = localRect.right;
      int i8 = i6 + i7 + i1 - i;
      this.mQueryTextView.setDropDownWidth(i8);
      return;
    }
  }

  private Intent createIntent(String paramString1, Uri paramUri, String paramString2, String paramString3, int paramInt, String paramString4)
  {
    Intent localIntent1 = new Intent(paramString1);
    Intent localIntent2 = localIntent1.addFlags(268435456);
    if (paramUri != null)
      Intent localIntent3 = localIntent1.setData(paramUri);
    CharSequence localCharSequence = this.mUserQuery;
    Intent localIntent4 = localIntent1.putExtra("user_query", localCharSequence);
    if (paramString3 != null)
      Intent localIntent5 = localIntent1.putExtra("query", paramString3);
    if (paramString2 != null)
      Intent localIntent6 = localIntent1.putExtra("intent_extra_data_key", paramString2);
    if (this.mAppSearchData != null)
    {
      Bundle localBundle = this.mAppSearchData;
      Intent localIntent7 = localIntent1.putExtra("app_data", localBundle);
    }
    if (paramInt != 0)
    {
      Intent localIntent8 = localIntent1.putExtra("action_key", paramInt);
      Intent localIntent9 = localIntent1.putExtra("action_msg", paramString4);
    }
    ComponentName localComponentName = this.mSearchable.getSearchActivity();
    Intent localIntent10 = localIntent1.setComponent(localComponentName);
    return localIntent1;
  }

  private Intent createIntentFromSuggestion(Cursor paramCursor, int paramInt, String paramString)
  {
    while (true)
    {
      Object localObject1;
      try
      {
        String str1 = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_action");
        if (str1 == null)
          str1 = this.mSearchable.getSuggestIntentAction();
        if (str1 == null)
          str1 = "android.intent.action.SEARCH";
        localObject1 = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_data");
        if (localObject1 == null)
          localObject1 = this.mSearchable.getSuggestIntentData();
        if (localObject1 != null)
        {
          localObject2 = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_data_id");
          if (localObject2 != null)
          {
            StringBuilder localStringBuilder = new StringBuilder().append((String)localObject1).append("/");
            String str2 = Uri.encode((String)localObject2);
            localObject1 = str2;
          }
        }
        if (localObject1 == null)
        {
          localObject2 = null;
          String str3 = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_query");
          String str4 = SuggestionsAdapter.getColumnString(paramCursor, "suggest_intent_extra_data");
          SearchView localSearchView = this;
          int i = paramInt;
          String str5 = paramString;
          localObject1 = localSearchView.createIntent(str1, (Uri)localObject2, str4, str3, i, str5);
          return localObject1;
        }
        Uri localUri = Uri.parse((String)localObject1);
        Object localObject2 = localUri;
        continue;
      }
      catch (RuntimeException localRuntimeException1)
      {
      }
      try
      {
        int j = paramCursor.getPosition();
        k = j;
        String str6 = "Search suggestions cursor at row " + k + " returned exception.";
        int m = Log.w("SearchView", str6, localRuntimeException1);
        localObject1 = null;
      }
      catch (RuntimeException localRuntimeException2)
      {
        while (true)
          int k = -1;
      }
    }
  }

  private Intent createVoiceAppSearchIntent(Intent paramIntent, SearchableInfo paramSearchableInfo)
  {
    String str1 = null;
    ComponentName localComponentName = paramSearchableInfo.getSearchActivity();
    Intent localIntent1 = new Intent("android.intent.action.SEARCH");
    Intent localIntent2 = localIntent1.setComponent(localComponentName);
    PendingIntent localPendingIntent = PendingIntent.getActivity(getContext(), 0, localIntent1, 1073741824);
    Bundle localBundle1 = new Bundle();
    if (this.mAppSearchData != null)
    {
      Bundle localBundle2 = this.mAppSearchData;
      localBundle1.putParcelable("app_data", localBundle2);
    }
    Intent localIntent3 = new Intent(paramIntent);
    String str2 = "free_form";
    int i = 1;
    Resources localResources = getResources();
    if (paramSearchableInfo.getVoiceLanguageModeId() != 0)
    {
      int j = paramSearchableInfo.getVoiceLanguageModeId();
      str2 = localResources.getString(j);
    }
    int k;
    if (paramSearchableInfo.getVoicePromptTextId() != 0)
      k = paramSearchableInfo.getVoicePromptTextId();
    for (String str3 = localResources.getString(k); ; str3 = null)
    {
      int m;
      if (paramSearchableInfo.getVoiceLanguageId() != 0)
        m = paramSearchableInfo.getVoiceLanguageId();
      for (String str4 = localResources.getString(m); ; str4 = null)
      {
        if (paramSearchableInfo.getVoiceMaxResults() != 0)
          i = paramSearchableInfo.getVoiceMaxResults();
        Intent localIntent4 = localIntent3.putExtra("android.speech.extra.LANGUAGE_MODEL", str2);
        Intent localIntent5 = localIntent3.putExtra("android.speech.extra.PROMPT", str3);
        Intent localIntent6 = localIntent3.putExtra("android.speech.extra.LANGUAGE", str4);
        Intent localIntent7 = localIntent3.putExtra("android.speech.extra.MAX_RESULTS", i);
        String str5 = "calling_package";
        if (localComponentName == null);
        while (true)
        {
          Intent localIntent8 = localIntent3.putExtra(str5, str1);
          Intent localIntent9 = localIntent3.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", localPendingIntent);
          Intent localIntent10 = localIntent3.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", localBundle1);
          return localIntent3;
          str1 = localComponentName.flattenToShortString();
        }
      }
    }
  }

  private Intent createVoiceWebSearchIntent(Intent paramIntent, SearchableInfo paramSearchableInfo)
  {
    Intent localIntent1 = new Intent(paramIntent);
    ComponentName localComponentName = paramSearchableInfo.getSearchActivity();
    if (localComponentName == null);
    for (String str = null; ; str = localComponentName.flattenToShortString())
    {
      Intent localIntent2 = localIntent1.putExtra("calling_package", str);
      return localIntent1;
    }
  }

  private void dismissSuggestions()
  {
    this.mQueryTextView.dismissDropDown();
  }

  private void forceSuggestionQuery()
  {
    AutoCompleteTextViewReflector localAutoCompleteTextViewReflector1 = HIDDEN_METHOD_INVOKER;
    SearchAutoComplete localSearchAutoComplete1 = this.mQueryTextView;
    localAutoCompleteTextViewReflector1.doBeforeTextChanged(localSearchAutoComplete1);
    AutoCompleteTextViewReflector localAutoCompleteTextViewReflector2 = HIDDEN_METHOD_INVOKER;
    SearchAutoComplete localSearchAutoComplete2 = this.mQueryTextView;
    localAutoCompleteTextViewReflector2.doAfterTextChanged(localSearchAutoComplete2);
  }

  private CharSequence getDecoratedHint(CharSequence paramCharSequence)
  {
    if (!this.mIconifiedByDefault);
    while (true)
    {
      return paramCharSequence;
      SpannableStringBuilder localSpannableStringBuilder1 = new SpannableStringBuilder("   ");
      SpannableStringBuilder localSpannableStringBuilder2 = localSpannableStringBuilder1.append(paramCharSequence);
      Resources localResources = getContext().getResources();
      int i = getSearchIconId();
      Drawable localDrawable = localResources.getDrawable(i);
      int j = (int)(this.mQueryTextView.getTextSize() * 1.25D);
      localDrawable.setBounds(0, 0, j, j);
      ImageSpan localImageSpan = new ImageSpan(localDrawable);
      localSpannableStringBuilder1.setSpan(localImageSpan, 1, 2, 33);
      paramCharSequence = localSpannableStringBuilder1;
    }
  }

  private int getPreferredWidth()
  {
    Resources localResources = getContext().getResources();
    int i = R.dimen.abc_search_view_preferred_width;
    return localResources.getDimensionPixelSize(i);
  }

  private int getSearchIconId()
  {
    TypedValue localTypedValue = new TypedValue();
    Resources.Theme localTheme = getContext().getTheme();
    int i = R.attr.searchViewSearchIcon;
    boolean bool = localTheme.resolveAttribute(i, localTypedValue, true);
    return localTypedValue.resourceId;
  }

  private boolean hasVoiceSearch()
  {
    boolean bool = false;
    Intent localIntent;
    if ((this.mSearchable != null) && (this.mSearchable.getVoiceSearchEnabled()))
    {
      localIntent = null;
      if (!this.mSearchable.getVoiceSearchLaunchWebSearch())
        break label61;
      localIntent = this.mVoiceWebSearchIntent;
    }
    while (true)
    {
      if ((localIntent != null) && (getContext().getPackageManager().resolveActivity(localIntent, 65536) != null))
        bool = true;
      return bool;
      label61: if (this.mSearchable.getVoiceSearchLaunchRecognizer())
        localIntent = this.mVoiceAppSearchIntent;
    }
  }

  static boolean isLandscapeMode(Context paramContext)
  {
    if (paramContext.getResources().getConfiguration().orientation == 2);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private boolean isSubmitAreaEnabled()
  {
    if (((this.mSubmitButtonEnabled) || (this.mVoiceButtonEnabled)) && (!isIconified()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private void launchIntent(Intent paramIntent)
  {
    if (paramIntent == null)
      return;
    try
    {
      getContext().startActivity(paramIntent);
      return;
    }
    catch (RuntimeException localRuntimeException)
    {
      String str = "Failed launch activity: " + paramIntent;
      int i = Log.e("SearchView", str, localRuntimeException);
    }
  }

  private void launchQuerySearch(int paramInt, String paramString1, String paramString2)
  {
    SearchView localSearchView = this;
    String str1 = null;
    String str2 = paramString2;
    int i = paramInt;
    String str3 = paramString1;
    Intent localIntent = localSearchView.createIntent("android.intent.action.SEARCH", null, str1, str2, i, str3);
    getContext().startActivity(localIntent);
  }

  private boolean launchSuggestion(int paramInt1, int paramInt2, String paramString)
  {
    Cursor localCursor = this.mSuggestionsAdapter.getCursor();
    if ((localCursor != null) && (localCursor.moveToPosition(paramInt1)))
    {
      Intent localIntent = createIntentFromSuggestion(localCursor, paramInt2, paramString);
      launchIntent(localIntent);
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private void onCloseClicked()
  {
    if (TextUtils.isEmpty(this.mQueryTextView.getText()))
    {
      if (!this.mIconifiedByDefault)
        return;
      if ((this.mOnCloseListener != null) && (this.mOnCloseListener.onClose()))
        return;
      clearFocus();
      updateViewsVisibility(true);
      return;
    }
    this.mQueryTextView.setText("");
    boolean bool = this.mQueryTextView.requestFocus();
    setImeVisibility(true);
  }

  private boolean onItemClicked(int paramInt1, int paramInt2, String paramString)
  {
    boolean bool1 = false;
    if ((this.mOnSuggestionListener == null) || (!this.mOnSuggestionListener.onSuggestionClick(paramInt1)))
    {
      boolean bool2 = launchSuggestion(paramInt1, 0, null);
      setImeVisibility(false);
      dismissSuggestions();
      bool1 = true;
    }
    return bool1;
  }

  private boolean onItemSelected(int paramInt)
  {
    if ((this.mOnSuggestionListener == null) || (!this.mOnSuggestionListener.onSuggestionSelect(paramInt)))
      rewriteQueryFromSuggestion(paramInt);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private void onSearchClicked()
  {
    updateViewsVisibility(false);
    boolean bool = this.mQueryTextView.requestFocus();
    setImeVisibility(true);
    if (this.mOnSearchClickListener == null)
      return;
    this.mOnSearchClickListener.onClick(this);
  }

  private void onSubmitQuery()
  {
    Editable localEditable = this.mQueryTextView.getText();
    if (localEditable == null)
      return;
    if (TextUtils.getTrimmedLength(localEditable) <= 0)
      return;
    if (this.mOnQueryChangeListener != null)
    {
      OnQueryTextListener localOnQueryTextListener = this.mOnQueryChangeListener;
      String str1 = localEditable.toString();
      if (localOnQueryTextListener.onQueryTextSubmit(str1))
        return;
    }
    if (this.mSearchable != null)
    {
      String str2 = localEditable.toString();
      launchQuerySearch(0, null, str2);
      setImeVisibility(false);
    }
    dismissSuggestions();
  }

  private boolean onSuggestionsKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = false;
    if (this.mSearchable == null);
    while (true)
    {
      return bool;
      if ((this.mSuggestionsAdapter != null) && (paramKeyEvent.getAction() == 0) && (KeyEventCompat.hasNoModifiers(paramKeyEvent)))
        if ((paramInt == 66) || (paramInt == 84) || (paramInt == 61))
        {
          int i = this.mQueryTextView.getListSelection();
          bool = onItemClicked(i, 0, null);
        }
        else
        {
          if ((paramInt == 21) || (paramInt == 22))
          {
            if (paramInt == 21);
            for (int j = 0; ; j = this.mQueryTextView.length())
            {
              this.mQueryTextView.setSelection(j);
              this.mQueryTextView.setListSelection(0);
              this.mQueryTextView.clearListSelection();
              AutoCompleteTextViewReflector localAutoCompleteTextViewReflector = HIDDEN_METHOD_INVOKER;
              SearchAutoComplete localSearchAutoComplete = this.mQueryTextView;
              localAutoCompleteTextViewReflector.ensureImeVisible(localSearchAutoComplete, true);
              bool = true;
              break;
            }
          }
          if ((paramInt != 19) || (this.mQueryTextView.getListSelection() != 0));
        }
    }
  }

  private void onTextChanged(CharSequence paramCharSequence)
  {
    boolean bool1 = true;
    Editable localEditable = this.mQueryTextView.getText();
    this.mUserQuery = localEditable;
    boolean bool2;
    if (!TextUtils.isEmpty(localEditable))
    {
      bool2 = true;
      updateSubmitButton(bool2);
      if (bool2)
        break label113;
    }
    while (true)
    {
      updateVoiceButton(bool1);
      updateCloseButton();
      updateSubmitArea();
      if (this.mOnQueryChangeListener != null)
      {
        CharSequence localCharSequence = this.mOldQueryText;
        if (!TextUtils.equals(paramCharSequence, localCharSequence))
        {
          OnQueryTextListener localOnQueryTextListener = this.mOnQueryChangeListener;
          String str1 = paramCharSequence.toString();
          boolean bool3 = localOnQueryTextListener.onQueryTextChange(str1);
        }
      }
      String str2 = paramCharSequence.toString();
      this.mOldQueryText = str2;
      return;
      bool2 = false;
      break;
      label113: bool1 = false;
    }
  }

  private void onVoiceClicked()
  {
    if (this.mSearchable == null)
      return;
    SearchableInfo localSearchableInfo = this.mSearchable;
    try
    {
      if (localSearchableInfo.getVoiceSearchLaunchWebSearch())
      {
        Intent localIntent1 = this.mVoiceWebSearchIntent;
        Intent localIntent2 = createVoiceWebSearchIntent(localIntent1, localSearchableInfo);
        getContext().startActivity(localIntent2);
        return;
      }
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      int i = Log.w("SearchView", "Could not find voice search activity");
      return;
    }
    if (!localSearchableInfo.getVoiceSearchLaunchRecognizer())
      return;
    Intent localIntent3 = this.mVoiceAppSearchIntent;
    Intent localIntent4 = createVoiceAppSearchIntent(localIntent3, localSearchableInfo);
    getContext().startActivity(localIntent4);
  }

  private void postUpdateFocusedState()
  {
    Runnable localRunnable = this.mUpdateDrawableStateRunnable;
    boolean bool = post(localRunnable);
  }

  private void rewriteQueryFromSuggestion(int paramInt)
  {
    Editable localEditable = this.mQueryTextView.getText();
    Cursor localCursor = this.mSuggestionsAdapter.getCursor();
    if (localCursor == null)
      return;
    if (localCursor.moveToPosition(paramInt))
    {
      CharSequence localCharSequence = this.mSuggestionsAdapter.convertToString(localCursor);
      if (localCharSequence != null)
      {
        setQuery(localCharSequence);
        return;
      }
      setQuery(localEditable);
      return;
    }
    setQuery(localEditable);
  }

  private void setImeVisibility(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      Runnable localRunnable1 = this.mShowImeRunnable;
      boolean bool1 = post(localRunnable1);
      return;
    }
    Runnable localRunnable2 = this.mShowImeRunnable;
    boolean bool2 = removeCallbacks(localRunnable2);
    InputMethodManager localInputMethodManager = (InputMethodManager)getContext().getSystemService("input_method");
    if (localInputMethodManager == null)
      return;
    IBinder localIBinder = getWindowToken();
    boolean bool3 = localInputMethodManager.hideSoftInputFromWindow(localIBinder, 0);
  }

  private void setQuery(CharSequence paramCharSequence)
  {
    this.mQueryTextView.setText(paramCharSequence);
    SearchAutoComplete localSearchAutoComplete = this.mQueryTextView;
    if (TextUtils.isEmpty(paramCharSequence));
    for (int i = 0; ; i = paramCharSequence.length())
    {
      localSearchAutoComplete.setSelection(i);
      return;
    }
  }

  private void updateCloseButton()
  {
    int i = 1;
    int j = 0;
    int k;
    label37: label47: Drawable localDrawable;
    if (!TextUtils.isEmpty(this.mQueryTextView.getText()))
    {
      k = 1;
      if ((k == 0) && ((!this.mIconifiedByDefault) || (this.mExpandedInActionView)))
        break label86;
      ImageView localImageView = this.mCloseButton;
      if (i == 0)
        break label91;
      localImageView.setVisibility(j);
      localDrawable = this.mCloseButton.getDrawable();
      if (k == 0)
        break label97;
    }
    label86: label91: label97: for (int[] arrayOfInt = ENABLED_STATE_SET; ; arrayOfInt = EMPTY_STATE_SET)
    {
      boolean bool = localDrawable.setState(arrayOfInt);
      return;
      k = 0;
      break;
      i = 0;
      break label37;
      j = 8;
      break label47;
    }
  }

  private void updateFocusedState()
  {
    boolean bool1 = this.mQueryTextView.hasFocus();
    Drawable localDrawable1 = this.mSearchPlate.getBackground();
    int[] arrayOfInt1;
    Drawable localDrawable2;
    if (bool1)
    {
      arrayOfInt1 = FOCUSED_STATE_SET;
      boolean bool2 = localDrawable1.setState(arrayOfInt1);
      localDrawable2 = this.mSubmitArea.getBackground();
      if (!bool1)
        break label70;
    }
    label70: for (int[] arrayOfInt2 = FOCUSED_STATE_SET; ; arrayOfInt2 = EMPTY_STATE_SET)
    {
      boolean bool3 = localDrawable2.setState(arrayOfInt2);
      invalidate();
      return;
      arrayOfInt1 = EMPTY_STATE_SET;
      break;
    }
  }

  private void updateQueryHint()
  {
    if (this.mQueryHint != null)
    {
      SearchAutoComplete localSearchAutoComplete1 = this.mQueryTextView;
      CharSequence localCharSequence1 = this.mQueryHint;
      CharSequence localCharSequence2 = getDecoratedHint(localCharSequence1);
      localSearchAutoComplete1.setHint(localCharSequence2);
      return;
    }
    if (this.mSearchable != null)
    {
      String str = null;
      int i = this.mSearchable.getHintId();
      if (i != 0)
        str = getContext().getString(i);
      if (str == null)
        return;
      SearchAutoComplete localSearchAutoComplete2 = this.mQueryTextView;
      CharSequence localCharSequence3 = getDecoratedHint(str);
      localSearchAutoComplete2.setHint(localCharSequence3);
      return;
    }
    SearchAutoComplete localSearchAutoComplete3 = this.mQueryTextView;
    CharSequence localCharSequence4 = getDecoratedHint("");
    localSearchAutoComplete3.setHint(localCharSequence4);
  }

  private void updateSearchAutoComplete()
  {
    int i = 1;
    SearchAutoComplete localSearchAutoComplete1 = this.mQueryTextView;
    int j = this.mSearchable.getSuggestThreshold();
    localSearchAutoComplete1.setThreshold(j);
    SearchAutoComplete localSearchAutoComplete2 = this.mQueryTextView;
    int k = this.mSearchable.getImeOptions();
    localSearchAutoComplete2.setImeOptions(k);
    int m = this.mSearchable.getInputType();
    if ((m & 0xF) != i)
    {
      m &= -65537;
      if (this.mSearchable.getSuggestAuthority() != null)
        m = m | 0x10000 | 0x80000;
    }
    this.mQueryTextView.setInputType(m);
    if (this.mSuggestionsAdapter != null)
      this.mSuggestionsAdapter.changeCursor(null);
    if (this.mSearchable.getSuggestAuthority() == null)
      return;
    Context localContext = getContext();
    SearchableInfo localSearchableInfo = this.mSearchable;
    WeakHashMap localWeakHashMap = this.mOutsideDrawablesCache;
    SuggestionsAdapter localSuggestionsAdapter1 = new SuggestionsAdapter(localContext, this, localSearchableInfo, localWeakHashMap);
    this.mSuggestionsAdapter = localSuggestionsAdapter1;
    SearchAutoComplete localSearchAutoComplete3 = this.mQueryTextView;
    CursorAdapter localCursorAdapter = this.mSuggestionsAdapter;
    localSearchAutoComplete3.setAdapter(localCursorAdapter);
    SuggestionsAdapter localSuggestionsAdapter2 = (SuggestionsAdapter)this.mSuggestionsAdapter;
    if (this.mQueryRefinement)
      i = 2;
    localSuggestionsAdapter2.setQueryRefinement(i);
  }

  private void updateSubmitArea()
  {
    int i = 8;
    if ((isSubmitAreaEnabled()) && ((this.mSubmitButton.getVisibility() == 0) || (this.mVoiceButton.getVisibility() == 0)))
      i = 0;
    this.mSubmitArea.setVisibility(i);
  }

  private void updateSubmitButton(boolean paramBoolean)
  {
    int i = 8;
    if ((this.mSubmitButtonEnabled) && (isSubmitAreaEnabled()) && (hasFocus()) && ((paramBoolean) || (!this.mVoiceButtonEnabled)))
      i = 0;
    this.mSubmitButton.setVisibility(i);
  }

  private void updateViewsVisibility(boolean paramBoolean)
  {
    boolean bool1 = true;
    int i = 8;
    this.mIconified = paramBoolean;
    int j;
    boolean bool2;
    label33: int k;
    if (paramBoolean)
    {
      j = 0;
      if (TextUtils.isEmpty(this.mQueryTextView.getText()))
        break label114;
      bool2 = true;
      this.mSearchButton.setVisibility(j);
      updateSubmitButton(bool2);
      View localView = this.mSearchEditFrame;
      if (!paramBoolean)
        break label120;
      k = 8;
      label62: localView.setVisibility(k);
      ImageView localImageView = this.mSearchHintIcon;
      if (!this.mIconifiedByDefault)
        break label126;
      label82: localImageView.setVisibility(i);
      updateCloseButton();
      if (bool2)
        break label131;
    }
    while (true)
    {
      updateVoiceButton(bool1);
      updateSubmitArea();
      return;
      j = 8;
      break;
      label114: bool2 = false;
      break label33;
      label120: k = 0;
      break label62;
      label126: i = 0;
      break label82;
      label131: bool1 = false;
    }
  }

  private void updateVoiceButton(boolean paramBoolean)
  {
    int i = 8;
    if ((this.mVoiceButtonEnabled) && (!isIconified()) && (paramBoolean))
    {
      i = 0;
      this.mSubmitButton.setVisibility(8);
    }
    this.mVoiceButton.setVisibility(i);
  }

  public void clearFocus()
  {
    this.mClearingFocus = true;
    setImeVisibility(false);
    super.clearFocus();
    this.mQueryTextView.clearFocus();
    this.mClearingFocus = false;
  }

  public int getImeOptions()
  {
    return this.mQueryTextView.getImeOptions();
  }

  public int getInputType()
  {
    return this.mQueryTextView.getInputType();
  }

  public int getMaxWidth()
  {
    return this.mMaxWidth;
  }

  public CharSequence getQuery()
  {
    return this.mQueryTextView.getText();
  }

  public CharSequence getQueryHint()
  {
    Object localObject;
    if (this.mQueryHint != null)
      localObject = this.mQueryHint;
    while (true)
    {
      return localObject;
      if (this.mSearchable != null)
      {
        localObject = null;
        int i = this.mSearchable.getHintId();
        if (i != 0)
          localObject = getContext().getString(i);
      }
      else
      {
        localObject = null;
      }
    }
  }

  public CursorAdapter getSuggestionsAdapter()
  {
    return this.mSuggestionsAdapter;
  }

  public boolean isIconfiedByDefault()
  {
    return this.mIconifiedByDefault;
  }

  public boolean isIconified()
  {
    return this.mIconified;
  }

  public boolean isQueryRefinementEnabled()
  {
    return this.mQueryRefinement;
  }

  public boolean isSubmitButtonEnabled()
  {
    return this.mSubmitButtonEnabled;
  }

  public void onActionViewCollapsed()
  {
    clearFocus();
    updateViewsVisibility(true);
    SearchAutoComplete localSearchAutoComplete = this.mQueryTextView;
    int i = this.mCollapsedImeOptions;
    localSearchAutoComplete.setImeOptions(i);
    this.mExpandedInActionView = false;
  }

  public void onActionViewExpanded()
  {
    if (this.mExpandedInActionView)
      return;
    this.mExpandedInActionView = true;
    int i = this.mQueryTextView.getImeOptions();
    this.mCollapsedImeOptions = i;
    SearchAutoComplete localSearchAutoComplete = this.mQueryTextView;
    int j = this.mCollapsedImeOptions | 0x2000000;
    localSearchAutoComplete.setImeOptions(j);
    this.mQueryTextView.setText("");
    setIconified(false);
  }

  protected void onDetachedFromWindow()
  {
    Runnable localRunnable1 = this.mUpdateDrawableStateRunnable;
    boolean bool1 = removeCallbacks(localRunnable1);
    Runnable localRunnable2 = this.mReleaseCursorRunnable;
    boolean bool2 = post(localRunnable2);
    super.onDetachedFromWindow();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (this.mSearchable == null);
    for (boolean bool = false; ; bool = super.onKeyDown(paramInt, paramKeyEvent))
      return bool;
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (isIconified())
    {
      super.onMeasure(paramInt1, paramInt2);
      return;
    }
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt1);
    switch (i)
    {
    default:
    case -2147483648:
    case 1073741824:
      while (true)
      {
        int k = View.MeasureSpec.makeMeasureSpec(j, 1073741824);
        super.onMeasure(k, paramInt2);
        return;
        if (this.mMaxWidth > 0)
        {
          j = Math.min(this.mMaxWidth, j);
        }
        else
        {
          j = Math.min(getPreferredWidth(), j);
          continue;
          if (this.mMaxWidth > 0)
            j = Math.min(this.mMaxWidth, j);
        }
      }
    case 0:
    }
    if (this.mMaxWidth > 0);
    for (j = this.mMaxWidth; ; j = getPreferredWidth())
      break;
  }

  void onQueryRefine(CharSequence paramCharSequence)
  {
    setQuery(paramCharSequence);
  }

  void onTextFocusChanged()
  {
    boolean bool = isIconified();
    updateViewsVisibility(bool);
    postUpdateFocusedState();
    if (!this.mQueryTextView.hasFocus())
      return;
    forceSuggestionQuery();
  }

  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    postUpdateFocusedState();
  }

  public boolean requestFocus(int paramInt, Rect paramRect)
  {
    boolean bool;
    if (this.mClearingFocus)
      bool = false;
    while (true)
    {
      return bool;
      if (!isFocusable())
      {
        bool = false;
      }
      else if (!isIconified())
      {
        bool = this.mQueryTextView.requestFocus(paramInt, paramRect);
        if (bool)
          updateViewsVisibility(false);
      }
      else
      {
        bool = super.requestFocus(paramInt, paramRect);
      }
    }
  }

  public void setAppSearchData(Bundle paramBundle)
  {
    this.mAppSearchData = paramBundle;
  }

  public void setIconified(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      onCloseClicked();
      return;
    }
    onSearchClicked();
  }

  public void setIconifiedByDefault(boolean paramBoolean)
  {
    if (this.mIconifiedByDefault != paramBoolean)
      return;
    this.mIconifiedByDefault = paramBoolean;
    updateViewsVisibility(paramBoolean);
    updateQueryHint();
  }

  public void setImeOptions(int paramInt)
  {
    this.mQueryTextView.setImeOptions(paramInt);
  }

  public void setInputType(int paramInt)
  {
    this.mQueryTextView.setInputType(paramInt);
  }

  public void setMaxWidth(int paramInt)
  {
    this.mMaxWidth = paramInt;
    requestLayout();
  }

  public void setOnCloseListener(OnCloseListener paramOnCloseListener)
  {
    this.mOnCloseListener = paramOnCloseListener;
  }

  public void setOnQueryTextFocusChangeListener(View.OnFocusChangeListener paramOnFocusChangeListener)
  {
    this.mOnQueryTextFocusChangeListener = paramOnFocusChangeListener;
  }

  public void setOnQueryTextListener(OnQueryTextListener paramOnQueryTextListener)
  {
    this.mOnQueryChangeListener = paramOnQueryTextListener;
  }

  public void setOnSearchClickListener(View.OnClickListener paramOnClickListener)
  {
    this.mOnSearchClickListener = paramOnClickListener;
  }

  public void setOnSuggestionListener(OnSuggestionListener paramOnSuggestionListener)
  {
    this.mOnSuggestionListener = paramOnSuggestionListener;
  }

  public void setQuery(CharSequence paramCharSequence, boolean paramBoolean)
  {
    this.mQueryTextView.setText(paramCharSequence);
    if (paramCharSequence != null)
    {
      SearchAutoComplete localSearchAutoComplete = this.mQueryTextView;
      int i = this.mQueryTextView.length();
      localSearchAutoComplete.setSelection(i);
      this.mUserQuery = paramCharSequence;
    }
    if (!paramBoolean)
      return;
    if (TextUtils.isEmpty(paramCharSequence))
      return;
    onSubmitQuery();
  }

  public void setQueryHint(CharSequence paramCharSequence)
  {
    this.mQueryHint = paramCharSequence;
    updateQueryHint();
  }

  public void setQueryRefinementEnabled(boolean paramBoolean)
  {
    this.mQueryRefinement = paramBoolean;
    if (!(this.mSuggestionsAdapter instanceof SuggestionsAdapter))
      return;
    SuggestionsAdapter localSuggestionsAdapter = (SuggestionsAdapter)this.mSuggestionsAdapter;
    if (paramBoolean);
    for (int i = 2; ; i = 1)
    {
      localSuggestionsAdapter.setQueryRefinement(i);
      return;
    }
  }

  public void setSearchableInfo(SearchableInfo paramSearchableInfo)
  {
    this.mSearchable = paramSearchableInfo;
    if (this.mSearchable != null)
    {
      updateSearchAutoComplete();
      updateQueryHint();
    }
    boolean bool1 = hasVoiceSearch();
    this.mVoiceButtonEnabled = bool1;
    if (this.mVoiceButtonEnabled)
      this.mQueryTextView.setPrivateImeOptions("nm");
    boolean bool2 = isIconified();
    updateViewsVisibility(bool2);
  }

  public void setSubmitButtonEnabled(boolean paramBoolean)
  {
    this.mSubmitButtonEnabled = paramBoolean;
    boolean bool = isIconified();
    updateViewsVisibility(bool);
  }

  public void setSuggestionsAdapter(CursorAdapter paramCursorAdapter)
  {
    this.mSuggestionsAdapter = paramCursorAdapter;
    SearchAutoComplete localSearchAutoComplete = this.mQueryTextView;
    CursorAdapter localCursorAdapter = this.mSuggestionsAdapter;
    localSearchAutoComplete.setAdapter(localCursorAdapter);
  }

  private static class AutoCompleteTextViewReflector
  {
    private Method doAfterTextChanged;
    private Method doBeforeTextChanged;
    private Method ensureImeVisible;
    private Method showSoftInputUnchecked;

    AutoCompleteTextViewReflector()
    {
      try
      {
        Class[] arrayOfClass1 = new Class[0];
        Method localMethod1 = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged", arrayOfClass1);
        this.doBeforeTextChanged = localMethod1;
        this.doBeforeTextChanged.setAccessible(true);
        try
        {
          label31: Class[] arrayOfClass2 = new Class[0];
          Method localMethod2 = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged", arrayOfClass2);
          this.doAfterTextChanged = localMethod2;
          this.doAfterTextChanged.setAccessible(true);
          try
          {
            label60: Class[] arrayOfClass3 = new Class[1];
            Class localClass1 = Boolean.TYPE;
            arrayOfClass3[0] = localClass1;
            Method localMethod3 = AutoCompleteTextView.class.getMethod("ensureImeVisible", arrayOfClass3);
            this.ensureImeVisible = localMethod3;
            this.ensureImeVisible.setAccessible(true);
            try
            {
              label102: Class[] arrayOfClass4 = new Class[2];
              Class localClass2 = Integer.TYPE;
              arrayOfClass4[0] = localClass2;
              arrayOfClass4[1] = ResultReceiver.class;
              Method localMethod4 = InputMethodManager.class.getMethod("showSoftInputUnchecked", arrayOfClass4);
              this.showSoftInputUnchecked = localMethod4;
              this.showSoftInputUnchecked.setAccessible(true);
              return;
            }
            catch (NoSuchMethodException localNoSuchMethodException1)
            {
            }
          }
          catch (NoSuchMethodException localNoSuchMethodException2)
          {
            break label102;
          }
        }
        catch (NoSuchMethodException localNoSuchMethodException3)
        {
          break label60;
        }
      }
      catch (NoSuchMethodException localNoSuchMethodException4)
      {
        break label31;
      }
    }

    void doAfterTextChanged(AutoCompleteTextView paramAutoCompleteTextView)
    {
      if (this.doAfterTextChanged == null)
        return;
      try
      {
        Method localMethod = this.doAfterTextChanged;
        Object[] arrayOfObject = new Object[0];
        Object localObject = localMethod.invoke(paramAutoCompleteTextView, arrayOfObject);
        return;
      }
      catch (Exception localException)
      {
      }
    }

    void doBeforeTextChanged(AutoCompleteTextView paramAutoCompleteTextView)
    {
      if (this.doBeforeTextChanged == null)
        return;
      try
      {
        Method localMethod = this.doBeforeTextChanged;
        Object[] arrayOfObject = new Object[0];
        Object localObject = localMethod.invoke(paramAutoCompleteTextView, arrayOfObject);
        return;
      }
      catch (Exception localException)
      {
      }
    }

    void ensureImeVisible(AutoCompleteTextView paramAutoCompleteTextView, boolean paramBoolean)
    {
      if (this.ensureImeVisible == null)
        return;
      try
      {
        Method localMethod = this.ensureImeVisible;
        Object[] arrayOfObject = new Object[1];
        Boolean localBoolean = Boolean.valueOf(paramBoolean);
        arrayOfObject[0] = localBoolean;
        Object localObject = localMethod.invoke(paramAutoCompleteTextView, arrayOfObject);
        return;
      }
      catch (Exception localException)
      {
      }
    }

    void showSoftInputUnchecked(InputMethodManager paramInputMethodManager, View paramView, int paramInt)
    {
      if (this.showSoftInputUnchecked != null)
        try
        {
          Method localMethod = this.showSoftInputUnchecked;
          Object[] arrayOfObject = new Object[2];
          Integer localInteger = Integer.valueOf(paramInt);
          arrayOfObject[0] = localInteger;
          arrayOfObject[1] = null;
          Object localObject = localMethod.invoke(paramInputMethodManager, arrayOfObject);
          return;
        }
        catch (Exception localException)
        {
        }
      boolean bool = paramInputMethodManager.showSoftInput(paramView, paramInt);
    }
  }

  public static class SearchAutoComplete extends AutoCompleteTextView
  {
    private SearchView mSearchView;
    private int mThreshold;

    public SearchAutoComplete(Context paramContext)
    {
      super();
      int i = getThreshold();
      this.mThreshold = i;
    }

    public SearchAutoComplete(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      int i = getThreshold();
      this.mThreshold = i;
    }

    public SearchAutoComplete(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
      super(paramAttributeSet, paramInt);
      int i = getThreshold();
      this.mThreshold = i;
    }

    private boolean isEmpty()
    {
      if (TextUtils.getTrimmedLength(getText()) == 0);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean enoughToFilter()
    {
      if ((this.mThreshold <= 0) || (super.enoughToFilter()));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
    {
      super.onFocusChanged(paramBoolean, paramInt, paramRect);
      this.mSearchView.onTextFocusChanged();
    }

    public boolean onKeyPreIme(int paramInt, KeyEvent paramKeyEvent)
    {
      boolean bool = true;
      KeyEvent.DispatcherState localDispatcherState;
      if (paramInt == 4)
        if ((paramKeyEvent.getAction() == 0) && (paramKeyEvent.getRepeatCount() == 0))
        {
          localDispatcherState = getKeyDispatcherState();
          if (localDispatcherState != null)
            localDispatcherState.startTracking(paramKeyEvent, this);
        }
      while (true)
      {
        return bool;
        if (paramKeyEvent.getAction() == 1)
        {
          localDispatcherState = getKeyDispatcherState();
          if (localDispatcherState != null)
            localDispatcherState.handleUpEvent(paramKeyEvent);
          if ((paramKeyEvent.isTracking()) && (!paramKeyEvent.isCanceled()))
          {
            this.mSearchView.clearFocus();
            this.mSearchView.setImeVisibility(false);
          }
        }
        else
        {
          bool = super.onKeyPreIme(paramInt, paramKeyEvent);
        }
      }
    }

    public void onWindowFocusChanged(boolean paramBoolean)
    {
      super.onWindowFocusChanged(paramBoolean);
      if (!paramBoolean)
        return;
      if (!this.mSearchView.hasFocus())
        return;
      if (getVisibility() != 0)
        return;
      boolean bool = ((InputMethodManager)getContext().getSystemService("input_method")).showSoftInput(this, 0);
      if (!SearchView.isLandscapeMode(getContext()))
        return;
      SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(this, true);
    }

    public void performCompletion()
    {
    }

    protected void replaceText(CharSequence paramCharSequence)
    {
    }

    void setSearchView(SearchView paramSearchView)
    {
      this.mSearchView = paramSearchView;
    }

    public void setThreshold(int paramInt)
    {
      super.setThreshold(paramInt);
      this.mThreshold = paramInt;
    }
  }

  public static abstract interface OnSuggestionListener
  {
    public abstract boolean onSuggestionClick(int paramInt);

    public abstract boolean onSuggestionSelect(int paramInt);
  }

  public static abstract interface OnCloseListener
  {
    public abstract boolean onClose();
  }

  public static abstract interface OnQueryTextListener
  {
    public abstract boolean onQueryTextChange(String paramString);

    public abstract boolean onQueryTextSubmit(String paramString);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.widget.SearchView
 * JD-Core Version:    0.6.2
 */