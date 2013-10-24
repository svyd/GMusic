package com.android.ex.editstyledtext;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.NoCopySpan.Concrete;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.ArrowKeyMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.AlignmentSpan.Standard;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.ParagraphStyle;
import android.text.style.QuoteSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class EditStyledText extends EditText
{
  private static final NoCopySpan.Concrete SELECTING = new NoCopySpan.Concrete();
  private static CharSequence STR_CLEARSTYLES;
  private static CharSequence STR_HORIZONTALLINE;
  private static CharSequence STR_PASTE;
  private StyledTextConverter mConverter;
  private Drawable mDefaultBackground;
  private StyledTextDialog mDialog;
  private ArrayList<EditStyledTextNotifier> mESTNotifiers;
  private InputConnection mInputConnection;
  private EditorManager mManager;
  private float mPaddingScale = 0.0F;

  public EditStyledText(Context paramContext)
  {
    super(paramContext);
    init();
  }

  public EditStyledText(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  public EditStyledText(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }

  private void cancelViewManagers()
  {
    if (this.mESTNotifiers == null)
      return;
    Iterator localIterator = this.mESTNotifiers.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      ((EditStyledTextNotifier)localIterator.next()).cancelViewManager();
    }
  }

  private int dipToPx(int paramInt)
  {
    if (this.mPaddingScale <= 0.0F)
    {
      float f1 = getContext().getResources().getDisplayMetrics().density;
      this.mPaddingScale = f1;
    }
    float f2 = paramInt;
    float f3 = getPaddingScale();
    return (int)(f2 * f3 + 0.5D);
  }

  private void finishComposingText()
  {
    if (this.mInputConnection == null)
      return;
    if (this.mManager.mTextIsFinishedFlag)
      return;
    boolean bool1 = this.mInputConnection.finishComposingText();
    boolean bool2 = EditorManager.access$202(this.mManager, true);
  }

  private int getMaxImageWidthDip()
  {
    return 300;
  }

  private int getMaxImageWidthPx()
  {
    return dipToPx(300);
  }

  private float getPaddingScale()
  {
    if (this.mPaddingScale <= 0.0F)
    {
      float f = getContext().getResources().getDisplayMetrics().density;
      this.mPaddingScale = f;
    }
    return this.mPaddingScale;
  }

  private void init()
  {
    StyledTextHtmlStandard localStyledTextHtmlStandard = new StyledTextHtmlStandard(null);
    StyledTextConverter localStyledTextConverter = new StyledTextConverter(this, localStyledTextHtmlStandard);
    this.mConverter = localStyledTextConverter;
    StyledTextDialog localStyledTextDialog1 = new StyledTextDialog(this);
    this.mDialog = localStyledTextDialog1;
    StyledTextDialog localStyledTextDialog2 = this.mDialog;
    EditorManager localEditorManager1 = new EditorManager(this, localStyledTextDialog2);
    this.mManager = localEditorManager1;
    EditorManager localEditorManager2 = this.mManager;
    StyledTextArrowKeyMethod localStyledTextArrowKeyMethod = new StyledTextArrowKeyMethod(localEditorManager2);
    setMovementMethod(localStyledTextArrowKeyMethod);
    Drawable localDrawable = getBackground();
    this.mDefaultBackground = localDrawable;
    boolean bool = requestFocus();
  }

  private void notifyStateChanged(int paramInt1, int paramInt2)
  {
    if (this.mESTNotifiers == null)
      return;
    Iterator localIterator = this.mESTNotifiers.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      ((EditStyledTextNotifier)localIterator.next()).onStateChanged(paramInt1, paramInt2);
    }
  }

  private void onRefreshStyles()
  {
    this.mManager.onRefreshStyles();
  }

  private void sendHintMessage(int paramInt)
  {
    if (this.mESTNotifiers == null)
      return;
    Iterator localIterator = this.mESTNotifiers.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      ((EditStyledTextNotifier)localIterator.next()).sendHintMsg(paramInt);
    }
  }

  private void sendOnTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mESTNotifiers == null)
      return;
    Iterator localIterator = this.mESTNotifiers.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      boolean bool = ((EditStyledTextNotifier)localIterator.next()).sendOnTouchEvent(paramMotionEvent);
    }
  }

  private void showInsertImageSelectAlertDialog()
  {
    if (this.mESTNotifiers == null)
      return;
    Iterator localIterator = this.mESTNotifiers.iterator();
    do
      if (!localIterator.hasNext())
        return;
    while (!((EditStyledTextNotifier)localIterator.next()).showInsertImageSelectAlertDialog());
  }

  private void showMenuAlertDialog()
  {
    if (this.mESTNotifiers == null)
      return;
    Iterator localIterator = this.mESTNotifiers.iterator();
    do
      if (!localIterator.hasNext())
        return;
    while (!((EditStyledTextNotifier)localIterator.next()).showMenuAlertDialog());
  }

  private void showPreview()
  {
    if (this.mESTNotifiers == null)
      return;
    Iterator localIterator = this.mESTNotifiers.iterator();
    do
      if (!localIterator.hasNext())
        return;
    while (!((EditStyledTextNotifier)localIterator.next()).showPreview());
  }

  private static void startSelecting(View paramView, Spannable paramSpannable)
  {
    NoCopySpan.Concrete localConcrete = SELECTING;
    paramSpannable.setSpan(localConcrete, 0, 0, 16777233);
  }

  private static void stopSelecting(View paramView, Spannable paramSpannable)
  {
    NoCopySpan.Concrete localConcrete = SELECTING;
    paramSpannable.removeSpan(localConcrete);
  }

  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    if (this.mManager == null)
      return;
    this.mManager.onRefreshStyles();
  }

  public int getBackgroundColor()
  {
    return this.mManager.getBackgroundColor();
  }

  public int getForegroundColor(int paramInt)
  {
    int j;
    if (paramInt >= 0)
    {
      int i = getText().length();
      if (paramInt <= i);
    }
    else
    {
      j = -16777216;
    }
    while (true)
    {
      return j;
      ForegroundColorSpan[] arrayOfForegroundColorSpan = (ForegroundColorSpan[])getText().getSpans(paramInt, paramInt, ForegroundColorSpan.class);
      if (arrayOfForegroundColorSpan.length > 0)
        j = arrayOfForegroundColorSpan[0].getForegroundColor();
      else
        j = -16777216;
    }
  }

  public int getSelectState()
  {
    return this.mManager.getSelectState();
  }

  public boolean isButtonsFocused()
  {
    boolean bool1 = false;
    if (this.mESTNotifiers != null)
    {
      Iterator localIterator = this.mESTNotifiers.iterator();
      while (localIterator.hasNext())
      {
        boolean bool2 = ((EditStyledTextNotifier)localIterator.next()).isButtonsFocused();
        bool1 |= bool2;
      }
    }
    return bool1;
  }

  public boolean isEditting()
  {
    return this.mManager.isEditting();
  }

  public boolean isSoftKeyBlocked()
  {
    return this.mManager.isSoftKeyBlocked();
  }

  public boolean isStyledText()
  {
    return this.mManager.isStyledText();
  }

  public void onClearStyles()
  {
    this.mManager.onClearStyles();
  }

  protected void onCreateContextMenu(ContextMenu paramContextMenu)
  {
    super.onCreateContextMenu(paramContextMenu);
    MenuHandler localMenuHandler = new MenuHandler(null);
    if (STR_HORIZONTALLINE != null)
    {
      CharSequence localCharSequence1 = STR_HORIZONTALLINE;
      MenuItem localMenuItem1 = paramContextMenu.add(0, 16776961, 0, localCharSequence1).setOnMenuItemClickListener(localMenuHandler);
    }
    if ((isStyledText()) && (STR_CLEARSTYLES != null))
    {
      CharSequence localCharSequence2 = STR_CLEARSTYLES;
      MenuItem localMenuItem2 = paramContextMenu.add(0, 16776962, 0, localCharSequence2).setOnMenuItemClickListener(localMenuHandler);
    }
    if (!this.mManager.canPaste())
      return;
    CharSequence localCharSequence3 = STR_PASTE;
    MenuItem localMenuItem3 = paramContextMenu.add(0, 16908322, 0, localCharSequence3).setOnMenuItemClickListener(localMenuHandler).setAlphabeticShortcut('v');
  }

  public InputConnection onCreateInputConnection(EditorInfo paramEditorInfo)
  {
    InputConnection localInputConnection = super.onCreateInputConnection(paramEditorInfo);
    StyledTextInputConnection localStyledTextInputConnection = new StyledTextInputConnection(localInputConnection, this);
    this.mInputConnection = localStyledTextInputConnection;
    return this.mInputConnection;
  }

  public void onEndEdit()
  {
    this.mManager.onAction(21);
  }

  public void onFixSelectedItem()
  {
    this.mManager.onFixSelectedItem();
  }

  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    if (paramBoolean)
    {
      onStartEdit();
      return;
    }
    if (isButtonsFocused())
      return;
    onEndEdit();
  }

  public void onInsertHorizontalLine()
  {
    this.mManager.onAction(12);
  }

  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!(paramParcelable instanceof SavedStyledTextState))
    {
      super.onRestoreInstanceState(paramParcelable);
      return;
    }
    SavedStyledTextState localSavedStyledTextState = (SavedStyledTextState)paramParcelable;
    Parcelable localParcelable = localSavedStyledTextState.getSuperState();
    super.onRestoreInstanceState(localParcelable);
    int i = localSavedStyledTextState.mBackgroundColor;
    setBackgroundColor(i);
  }

  public Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    SavedStyledTextState localSavedStyledTextState = new SavedStyledTextState(localParcelable);
    int i = this.mManager.getBackgroundColor();
    localSavedStyledTextState.mBackgroundColor = i;
    return localSavedStyledTextState;
  }

  public void onStartCopy()
  {
    this.mManager.onAction(1);
  }

  public void onStartCut()
  {
    this.mManager.onAction(7);
  }

  public void onStartEdit()
  {
    this.mManager.onAction(20);
  }

  public void onStartPaste()
  {
    this.mManager.onAction(2);
  }

  public void onStartSelect()
  {
    this.mManager.onStartSelect(true);
  }

  public void onStartSelectAll()
  {
    this.mManager.onStartSelectAll(true);
  }

  protected void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    if (this.mManager != null)
    {
      EditorManager localEditorManager1 = this.mManager;
      Editable localEditable1 = getText();
      localEditorManager1.updateSpanNextToCursor(localEditable1, paramInt1, paramInt2, paramInt3);
      EditorManager localEditorManager2 = this.mManager;
      Editable localEditable2 = getText();
      localEditorManager2.updateSpanPreviousFromCursor(localEditable2, paramInt1, paramInt2, paramInt3);
      if (paramInt3 <= paramInt2)
        break label116;
      EditorManager localEditorManager3 = this.mManager;
      int i = paramInt1 + paramInt3;
      localEditorManager3.setTextComposingMask(paramInt1, i);
      if (this.mManager.isWaitInput())
      {
        if (paramInt3 <= paramInt2)
          break label132;
        this.mManager.onCursorMoved();
        onFixSelectedItem();
      }
    }
    while (true)
    {
      super.onTextChanged(paramCharSequence, paramInt1, paramInt2, paramInt3);
      return;
      label116: if (paramInt2 >= paramInt3)
        break;
      this.mManager.unsetTextComposingMask();
      break;
      label132: if (paramInt3 < paramInt2)
        this.mManager.onAction(22);
    }
  }

  public boolean onTextContextMenuItem(int paramInt)
  {
    boolean bool = true;
    int i = getSelectionStart();
    int j = getSelectionEnd();
    int k;
    if (i != j)
    {
      k = 1;
      switch (paramInt)
      {
      default:
        label112: bool = super.onTextContextMenuItem(paramInt);
      case 16908319:
      case 16908328:
      case 16908329:
      case 16908322:
      case 16908321:
      case 16908320:
      case 16776961:
      case 16776962:
      case 16776963:
      case 16776964:
      }
    }
    while (true)
    {
      return bool;
      k = 0;
      break;
      onStartSelectAll();
      continue;
      onStartSelect();
      this.mManager.blockSoftKey();
      break label112;
      onFixSelectedItem();
      break label112;
      onStartPaste();
      continue;
      if (k != 0)
      {
        onStartCopy();
      }
      else
      {
        this.mManager.onStartSelectAll(false);
        onStartCopy();
        continue;
        if (k != 0)
        {
          onStartCut();
        }
        else
        {
          this.mManager.onStartSelectAll(false);
          onStartCut();
          continue;
          onInsertHorizontalLine();
          continue;
          onClearStyles();
          continue;
          onStartEdit();
          continue;
          onEndEdit();
        }
      }
    }
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i;
    int j;
    boolean bool2;
    if (paramMotionEvent.getAction() == 1)
    {
      cancelLongPress();
      boolean bool1 = isEditting();
      if (!bool1)
        onStartEdit();
      i = Selection.getSelectionStart(getText());
      j = Selection.getSelectionEnd(getText());
      bool2 = super.onTouchEvent(paramMotionEvent);
      if ((isFocused()) && (getSelectState() == 0))
      {
        if (bool1)
        {
          EditorManager localEditorManager = this.mManager;
          int k = Selection.getSelectionStart(getText());
          int m = Selection.getSelectionEnd(getText());
          localEditorManager.showSoftKey(k, m);
        }
      }
      else
      {
        this.mManager.onCursorMoved();
        this.mManager.unsetTextComposingMask();
      }
    }
    while (true)
    {
      sendOnTouchEvent(paramMotionEvent);
      return bool2;
      this.mManager.showSoftKey(i, j);
      break;
      bool2 = super.onTouchEvent(paramMotionEvent);
    }
  }

  public void setAlignAlertParams(CharSequence paramCharSequence, CharSequence[] paramArrayOfCharSequence)
  {
    this.mDialog.setAlignAlertParams(paramCharSequence, paramArrayOfCharSequence);
  }

  public void setAlignment(Layout.Alignment paramAlignment)
  {
    this.mManager.setAlignment(paramAlignment);
  }

  public void setBackgroundColor(int paramInt)
  {
    if (paramInt != 16777215)
      super.setBackgroundColor(paramInt);
    while (true)
    {
      this.mManager.setBackgroundColor(paramInt);
      onRefreshStyles();
      return;
      Drawable localDrawable = this.mDefaultBackground;
      setBackgroundDrawable(localDrawable);
    }
  }

  public void setBuilder(AlertDialog.Builder paramBuilder)
  {
    this.mDialog.setBuilder(paramBuilder);
  }

  public void setColorAlertParams(CharSequence paramCharSequence1, CharSequence[] paramArrayOfCharSequence1, CharSequence[] paramArrayOfCharSequence2, CharSequence paramCharSequence2)
  {
    this.mDialog.setColorAlertParams(paramCharSequence1, paramArrayOfCharSequence1, paramArrayOfCharSequence2, paramCharSequence2);
  }

  public void setContextMenuStrings(CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3)
  {
    STR_HORIZONTALLINE = paramCharSequence1;
    STR_CLEARSTYLES = paramCharSequence2;
    STR_PASTE = paramCharSequence3;
  }

  public void setHtml(String paramString)
  {
    this.mConverter.SetHtml(paramString);
  }

  public void setItemColor(int paramInt)
  {
    this.mManager.setItemColor(paramInt, true);
  }

  public void setItemSize(int paramInt)
  {
    this.mManager.setItemSize(paramInt, true);
  }

  public void setMarquee(int paramInt)
  {
    this.mManager.setMarquee(paramInt);
  }

  public void setMarqueeAlertParams(CharSequence paramCharSequence, CharSequence[] paramArrayOfCharSequence)
  {
    this.mDialog.setMarqueeAlertParams(paramCharSequence, paramArrayOfCharSequence);
  }

  public void setSizeAlertParams(CharSequence paramCharSequence, CharSequence[] paramArrayOfCharSequence1, CharSequence[] paramArrayOfCharSequence2, CharSequence[] paramArrayOfCharSequence3)
  {
    this.mDialog.setSizeAlertParams(paramCharSequence, paramArrayOfCharSequence1, paramArrayOfCharSequence2, paramArrayOfCharSequence3);
  }

  public void setStyledTextHtmlConverter(StyledTextHtmlConverter paramStyledTextHtmlConverter)
  {
    this.mConverter.setStyledTextHtmlConverter(paramStyledTextHtmlConverter);
  }

  public class EditModeActions
  {
    private HashMap<Integer, EditModeActionBase> mActionMap;
    private AlignAction mAlignAction;
    private BackgroundColorAction mBackgroundColorAction;
    private CancelAction mCancelEditAction;
    private ClearStylesAction mClearStylesAction;
    private ColorAction mColorAction;
    private CopyAction mCopyAction;
    private CutAction mCutAction;
    private EditStyledText.StyledTextDialog mDialog;
    private EditStyledText mEST;
    private EndEditAction mEndEditAction;
    private HorizontalLineAction mHorizontalLineAction;
    private ImageAction mImageAction;
    private EditStyledText.EditorManager mManager;
    private MarqueeDialogAction mMarqueeDialogAction;
    private int mMode = 0;
    private NothingAction mNothingAction;
    private PasteAction mPasteAction;
    private PreviewAction mPreviewAction;
    private ResetAction mResetAction;
    private SelectAction mSelectAction;
    private SelectAllAction mSelectAllAction;
    private ShowMenuAction mShowMenuAction;
    private SizeAction mSizeAction;
    private StartEditAction mStartEditAction;
    private StopSelectionAction mStopSelectionAction;
    private SwingAction mSwingAction;
    private TelopAction mTelopAction;
    private TextViewAction mTextViewAction;

    EditModeActions(EditStyledText paramEditorManager, EditStyledText.EditorManager paramStyledTextDialog, EditStyledText.StyledTextDialog arg4)
    {
      HashMap localHashMap1 = new HashMap();
      this.mActionMap = localHashMap1;
      NothingAction localNothingAction1 = new NothingAction();
      this.mNothingAction = localNothingAction1;
      CopyAction localCopyAction1 = new CopyAction();
      this.mCopyAction = localCopyAction1;
      PasteAction localPasteAction1 = new PasteAction();
      this.mPasteAction = localPasteAction1;
      SelectAction localSelectAction1 = new SelectAction();
      this.mSelectAction = localSelectAction1;
      CutAction localCutAction1 = new CutAction();
      this.mCutAction = localCutAction1;
      SelectAllAction localSelectAllAction1 = new SelectAllAction();
      this.mSelectAllAction = localSelectAllAction1;
      HorizontalLineAction localHorizontalLineAction1 = new HorizontalLineAction();
      this.mHorizontalLineAction = localHorizontalLineAction1;
      StopSelectionAction localStopSelectionAction1 = new StopSelectionAction();
      this.mStopSelectionAction = localStopSelectionAction1;
      ClearStylesAction localClearStylesAction1 = new ClearStylesAction();
      this.mClearStylesAction = localClearStylesAction1;
      ImageAction localImageAction1 = new ImageAction();
      this.mImageAction = localImageAction1;
      BackgroundColorAction localBackgroundColorAction1 = new BackgroundColorAction();
      this.mBackgroundColorAction = localBackgroundColorAction1;
      PreviewAction localPreviewAction1 = new PreviewAction();
      this.mPreviewAction = localPreviewAction1;
      CancelAction localCancelAction1 = new CancelAction();
      this.mCancelEditAction = localCancelAction1;
      TextViewAction localTextViewAction1 = new TextViewAction();
      this.mTextViewAction = localTextViewAction1;
      StartEditAction localStartEditAction1 = new StartEditAction();
      this.mStartEditAction = localStartEditAction1;
      EndEditAction localEndEditAction1 = new EndEditAction();
      this.mEndEditAction = localEndEditAction1;
      ResetAction localResetAction1 = new ResetAction();
      this.mResetAction = localResetAction1;
      ShowMenuAction localShowMenuAction1 = new ShowMenuAction();
      this.mShowMenuAction = localShowMenuAction1;
      AlignAction localAlignAction1 = new AlignAction();
      this.mAlignAction = localAlignAction1;
      TelopAction localTelopAction1 = new TelopAction();
      this.mTelopAction = localTelopAction1;
      SwingAction localSwingAction1 = new SwingAction();
      this.mSwingAction = localSwingAction1;
      MarqueeDialogAction localMarqueeDialogAction1 = new MarqueeDialogAction();
      this.mMarqueeDialogAction = localMarqueeDialogAction1;
      ColorAction localColorAction1 = new ColorAction();
      this.mColorAction = localColorAction1;
      SizeAction localSizeAction1 = new SizeAction();
      this.mSizeAction = localSizeAction1;
      this.mEST = paramEditorManager;
      this.mManager = paramStyledTextDialog;
      Object localObject1;
      this.mDialog = localObject1;
      HashMap localHashMap2 = this.mActionMap;
      Integer localInteger1 = Integer.valueOf(0);
      NothingAction localNothingAction2 = this.mNothingAction;
      Object localObject2 = localHashMap2.put(localInteger1, localNothingAction2);
      HashMap localHashMap3 = this.mActionMap;
      Integer localInteger2 = Integer.valueOf(1);
      CopyAction localCopyAction2 = this.mCopyAction;
      Object localObject3 = localHashMap3.put(localInteger2, localCopyAction2);
      HashMap localHashMap4 = this.mActionMap;
      Integer localInteger3 = Integer.valueOf(2);
      PasteAction localPasteAction2 = this.mPasteAction;
      Object localObject4 = localHashMap4.put(localInteger3, localPasteAction2);
      HashMap localHashMap5 = this.mActionMap;
      Integer localInteger4 = Integer.valueOf(5);
      SelectAction localSelectAction2 = this.mSelectAction;
      Object localObject5 = localHashMap5.put(localInteger4, localSelectAction2);
      HashMap localHashMap6 = this.mActionMap;
      Integer localInteger5 = Integer.valueOf(7);
      CutAction localCutAction2 = this.mCutAction;
      Object localObject6 = localHashMap6.put(localInteger5, localCutAction2);
      HashMap localHashMap7 = this.mActionMap;
      Integer localInteger6 = Integer.valueOf(11);
      SelectAllAction localSelectAllAction2 = this.mSelectAllAction;
      Object localObject7 = localHashMap7.put(localInteger6, localSelectAllAction2);
      HashMap localHashMap8 = this.mActionMap;
      Integer localInteger7 = Integer.valueOf(12);
      HorizontalLineAction localHorizontalLineAction2 = this.mHorizontalLineAction;
      Object localObject8 = localHashMap8.put(localInteger7, localHorizontalLineAction2);
      HashMap localHashMap9 = this.mActionMap;
      Integer localInteger8 = Integer.valueOf(13);
      StopSelectionAction localStopSelectionAction2 = this.mStopSelectionAction;
      Object localObject9 = localHashMap9.put(localInteger8, localStopSelectionAction2);
      HashMap localHashMap10 = this.mActionMap;
      Integer localInteger9 = Integer.valueOf(14);
      ClearStylesAction localClearStylesAction2 = this.mClearStylesAction;
      Object localObject10 = localHashMap10.put(localInteger9, localClearStylesAction2);
      HashMap localHashMap11 = this.mActionMap;
      Integer localInteger10 = Integer.valueOf(15);
      ImageAction localImageAction2 = this.mImageAction;
      Object localObject11 = localHashMap11.put(localInteger10, localImageAction2);
      HashMap localHashMap12 = this.mActionMap;
      Integer localInteger11 = Integer.valueOf(16);
      BackgroundColorAction localBackgroundColorAction2 = this.mBackgroundColorAction;
      Object localObject12 = localHashMap12.put(localInteger11, localBackgroundColorAction2);
      HashMap localHashMap13 = this.mActionMap;
      Integer localInteger12 = Integer.valueOf(17);
      PreviewAction localPreviewAction2 = this.mPreviewAction;
      Object localObject13 = localHashMap13.put(localInteger12, localPreviewAction2);
      HashMap localHashMap14 = this.mActionMap;
      Integer localInteger13 = Integer.valueOf(18);
      CancelAction localCancelAction2 = this.mCancelEditAction;
      Object localObject14 = localHashMap14.put(localInteger13, localCancelAction2);
      HashMap localHashMap15 = this.mActionMap;
      Integer localInteger14 = Integer.valueOf(19);
      TextViewAction localTextViewAction2 = this.mTextViewAction;
      Object localObject15 = localHashMap15.put(localInteger14, localTextViewAction2);
      HashMap localHashMap16 = this.mActionMap;
      Integer localInteger15 = Integer.valueOf(20);
      StartEditAction localStartEditAction2 = this.mStartEditAction;
      Object localObject16 = localHashMap16.put(localInteger15, localStartEditAction2);
      HashMap localHashMap17 = this.mActionMap;
      Integer localInteger16 = Integer.valueOf(21);
      EndEditAction localEndEditAction2 = this.mEndEditAction;
      Object localObject17 = localHashMap17.put(localInteger16, localEndEditAction2);
      HashMap localHashMap18 = this.mActionMap;
      Integer localInteger17 = Integer.valueOf(22);
      ResetAction localResetAction2 = this.mResetAction;
      Object localObject18 = localHashMap18.put(localInteger17, localResetAction2);
      HashMap localHashMap19 = this.mActionMap;
      Integer localInteger18 = Integer.valueOf(23);
      ShowMenuAction localShowMenuAction2 = this.mShowMenuAction;
      Object localObject19 = localHashMap19.put(localInteger18, localShowMenuAction2);
      HashMap localHashMap20 = this.mActionMap;
      Integer localInteger19 = Integer.valueOf(6);
      AlignAction localAlignAction2 = this.mAlignAction;
      Object localObject20 = localHashMap20.put(localInteger19, localAlignAction2);
      HashMap localHashMap21 = this.mActionMap;
      Integer localInteger20 = Integer.valueOf(8);
      TelopAction localTelopAction2 = this.mTelopAction;
      Object localObject21 = localHashMap21.put(localInteger20, localTelopAction2);
      HashMap localHashMap22 = this.mActionMap;
      Integer localInteger21 = Integer.valueOf(9);
      SwingAction localSwingAction2 = this.mSwingAction;
      Object localObject22 = localHashMap22.put(localInteger21, localSwingAction2);
      HashMap localHashMap23 = this.mActionMap;
      Integer localInteger22 = Integer.valueOf(10);
      MarqueeDialogAction localMarqueeDialogAction2 = this.mMarqueeDialogAction;
      Object localObject23 = localHashMap23.put(localInteger22, localMarqueeDialogAction2);
      HashMap localHashMap24 = this.mActionMap;
      Integer localInteger23 = Integer.valueOf(4);
      ColorAction localColorAction2 = this.mColorAction;
      Object localObject24 = localHashMap24.put(localInteger23, localColorAction2);
      HashMap localHashMap25 = this.mActionMap;
      Integer localInteger24 = Integer.valueOf(3);
      SizeAction localSizeAction2 = this.mSizeAction;
      Object localObject25 = localHashMap25.put(localInteger24, localSizeAction2);
    }

    private EditModeActionBase getAction(int paramInt)
    {
      HashMap localHashMap1 = this.mActionMap;
      Integer localInteger1 = Integer.valueOf(paramInt);
      HashMap localHashMap2;
      Integer localInteger2;
      if (localHashMap1.containsKey(localInteger1))
      {
        localHashMap2 = this.mActionMap;
        localInteger2 = Integer.valueOf(paramInt);
      }
      for (EditModeActionBase localEditModeActionBase = (EditModeActionBase)localHashMap2.get(localInteger2); ; localEditModeActionBase = null)
        return localEditModeActionBase;
    }

    public boolean doNext()
    {
      int i = this.mMode;
      return doNext(i);
    }

    public boolean doNext(int paramInt)
    {
      boolean bool = false;
      StringBuilder localStringBuilder = new StringBuilder().append("--- do the next action: ").append(paramInt).append(",");
      int i = this.mManager.getSelectState();
      String str = i;
      int j = Log.d("EditModeActions", str);
      EditModeActionBase localEditModeActionBase = getAction(paramInt);
      if (localEditModeActionBase == null)
        int k = Log.e("EditModeActions", "--- invalid action error.");
      while (true)
      {
        return bool;
        switch (this.mManager.getSelectState())
        {
        default:
          break;
        case 0:
          bool = localEditModeActionBase.doNotSelected();
          break;
        case 1:
          bool = localEditModeActionBase.doStartPosIsSelected();
          break;
        case 2:
          bool = localEditModeActionBase.doEndPosIsSelected();
          break;
        case 3:
          if (this.mManager.isWaitInput())
            bool = localEditModeActionBase.doSelectionIsFixedAndWaitingInput();
          else
            bool = localEditModeActionBase.doSelectionIsFixed();
          break;
        }
      }
    }

    public void onAction(int paramInt)
    {
      onAction(paramInt, null);
    }

    public void onAction(int paramInt, Object[] paramArrayOfObject)
    {
      getAction(paramInt).addParams(paramArrayOfObject);
      this.mMode = paramInt;
      boolean bool = doNext(paramInt);
    }

    public void onSelectAction()
    {
      boolean bool = doNext(5);
    }

    public class SizeAction extends EditStyledText.EditModeActions.SetSpanActionBase
    {
      public SizeAction()
      {
        super();
      }

      protected boolean doSelectionIsFixed()
      {
        if (super.doSelectionIsFixed());
        while (true)
        {
          return true;
          EditStyledText.StyledTextDialog.access$4900(EditStyledText.EditModeActions.this.mDialog);
        }
      }

      protected boolean doSelectionIsFixedAndWaitingInput()
      {
        if (super.doSelectionIsFixedAndWaitingInput());
        while (true)
        {
          return true;
          int i = EditStyledText.EditModeActions.this.mManager.getColorWaitInput();
          EditStyledText.EditorManager localEditorManager = EditStyledText.EditModeActions.this.mManager;
          int j = EditStyledText.EditModeActions.this.mManager.getSizeWaitInput();
          localEditorManager.setItemSize(j, false);
          if (!EditStyledText.EditModeActions.this.mManager.isWaitInput())
          {
            EditStyledText.EditModeActions.this.mManager.setItemColor(i, false);
            EditStyledText.EditorManager.access$2400(EditStyledText.EditModeActions.this.mManager);
          }
          else
          {
            boolean bool = fixSelection();
            EditStyledText.StyledTextDialog.access$4900(EditStyledText.EditModeActions.this.mDialog);
          }
        }
      }
    }

    public class ColorAction extends EditStyledText.EditModeActions.SetSpanActionBase
    {
      public ColorAction()
      {
        super();
      }

      protected boolean doSelectionIsFixed()
      {
        if (super.doSelectionIsFixed());
        while (true)
        {
          return true;
          EditStyledText.StyledTextDialog.access$4800(EditStyledText.EditModeActions.this.mDialog);
        }
      }

      protected boolean doSelectionIsFixedAndWaitingInput()
      {
        if (super.doSelectionIsFixedAndWaitingInput());
        while (true)
        {
          return true;
          int i = EditStyledText.EditModeActions.this.mManager.getSizeWaitInput();
          EditStyledText.EditorManager localEditorManager = EditStyledText.EditModeActions.this.mManager;
          int j = EditStyledText.EditModeActions.this.mManager.getColorWaitInput();
          localEditorManager.setItemColor(j, false);
          if (!EditStyledText.EditModeActions.this.mManager.isWaitInput())
          {
            EditStyledText.EditModeActions.this.mManager.setItemSize(i, false);
            EditStyledText.EditorManager.access$2400(EditStyledText.EditModeActions.this.mManager);
          }
          else
          {
            boolean bool = fixSelection();
            EditStyledText.StyledTextDialog.access$4800(EditStyledText.EditModeActions.this.mDialog);
          }
        }
      }
    }

    public class MarqueeDialogAction extends EditStyledText.EditModeActions.SetSpanActionBase
    {
      public MarqueeDialogAction()
      {
        super();
      }

      protected boolean doSelectionIsFixed()
      {
        if (super.doSelectionIsFixed());
        while (true)
        {
          return true;
          EditStyledText.StyledTextDialog.access$4700(EditStyledText.EditModeActions.this.mDialog);
        }
      }
    }

    public class SwingAction extends EditStyledText.EditModeActions.SetSpanActionBase
    {
      public SwingAction()
      {
        super();
      }

      protected boolean doSelectionIsFixed()
      {
        if (super.doSelectionIsFixed());
        while (true)
        {
          return true;
          EditStyledText.EditModeActions.this.mManager.setSwing();
        }
      }
    }

    public class TelopAction extends EditStyledText.EditModeActions.SetSpanActionBase
    {
      public TelopAction()
      {
        super();
      }

      protected boolean doSelectionIsFixed()
      {
        if (super.doSelectionIsFixed());
        while (true)
        {
          return true;
          EditStyledText.EditModeActions.this.mManager.setTelop();
        }
      }
    }

    public class AlignAction extends EditStyledText.EditModeActions.SetSpanActionBase
    {
      public AlignAction()
      {
        super();
      }

      protected boolean doSelectionIsFixed()
      {
        if (super.doSelectionIsFixed());
        while (true)
        {
          return true;
          EditStyledText.StyledTextDialog.access$4600(EditStyledText.EditModeActions.this.mDialog);
        }
      }
    }

    public class SetSpanActionBase extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public SetSpanActionBase()
      {
        super();
      }

      protected boolean doEndPosIsSelected()
      {
        if ((EditStyledText.EditModeActions.this.mManager.getEditMode() == 0) || (EditStyledText.EditModeActions.this.mManager.getEditMode() == 5))
        {
          EditStyledText.EditorManager localEditorManager = EditStyledText.EditModeActions.this.mManager;
          int i = EditStyledText.EditModeActions.this.mMode;
          EditStyledText.EditorManager.access$2300(localEditorManager, i);
          boolean bool1 = fixSelection();
          boolean bool2 = EditStyledText.EditModeActions.this.doNext();
        }
        for (boolean bool3 = true; ; bool3 = doStartPosIsSelected())
          return bool3;
      }

      protected boolean doNotSelected()
      {
        boolean bool1 = true;
        if ((EditStyledText.EditModeActions.this.mManager.getEditMode() == 0) || (EditStyledText.EditModeActions.this.mManager.getEditMode() == 5))
        {
          EditStyledText.EditorManager localEditorManager1 = EditStyledText.EditModeActions.this.mManager;
          int i = EditStyledText.EditModeActions.this.mMode;
          EditStyledText.EditorManager.access$2300(localEditorManager1, i);
          EditStyledText.EditorManager localEditorManager2 = EditStyledText.EditModeActions.this.mManager;
          int j = EditStyledText.this.getSelectionStart();
          int k = EditStyledText.this.getSelectionEnd();
          EditStyledText.EditorManager.access$4500(localEditorManager2, j, k);
          boolean bool2 = fixSelection();
          boolean bool3 = EditStyledText.EditModeActions.this.doNext();
        }
        while (true)
        {
          return bool1;
          int m = EditStyledText.EditModeActions.this.mManager.getEditMode();
          int n = EditStyledText.EditModeActions.this.mMode;
          if (m != n)
          {
            StringBuilder localStringBuilder1 = new StringBuilder().append("--- setspanactionbase");
            int i1 = EditStyledText.EditModeActions.this.mManager.getEditMode();
            StringBuilder localStringBuilder2 = localStringBuilder1.append(i1).append(",");
            int i2 = EditStyledText.EditModeActions.this.mMode;
            String str = i2;
            int i3 = Log.d("EditModeActions", str);
            if (!EditStyledText.EditModeActions.this.mManager.isWaitInput())
            {
              EditStyledText.EditorManager.access$2400(EditStyledText.EditModeActions.this.mManager);
              EditStyledText.EditorManager localEditorManager3 = EditStyledText.EditModeActions.this.mManager;
              int i4 = EditStyledText.EditModeActions.this.mMode;
              EditStyledText.EditorManager.access$2300(localEditorManager3, i4);
            }
            while (true)
            {
              boolean bool4 = EditStyledText.EditModeActions.this.doNext();
              break;
              EditStyledText.EditorManager.access$2300(EditStyledText.EditModeActions.this.mManager, 0);
              EditStyledText.EditorManager.access$2100(EditStyledText.EditModeActions.this.mManager, 0);
            }
          }
          bool1 = false;
        }
      }

      protected boolean doSelectionIsFixed()
      {
        boolean bool = false;
        if (doEndPosIsSelected())
          bool = true;
        while (true)
        {
          return bool;
          EditStyledText.this.sendHintMessage(0);
        }
      }

      protected boolean doStartPosIsSelected()
      {
        if ((EditStyledText.EditModeActions.this.mManager.getEditMode() == 0) || (EditStyledText.EditModeActions.this.mManager.getEditMode() == 5))
        {
          EditStyledText.EditorManager localEditorManager = EditStyledText.EditModeActions.this.mManager;
          int i = EditStyledText.EditModeActions.this.mMode;
          EditStyledText.EditorManager.access$2300(localEditorManager, i);
          EditStyledText.EditModeActions.this.onSelectAction();
        }
        for (boolean bool = true; ; bool = doNotSelected())
          return bool;
      }
    }

    public class ShowMenuAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public ShowMenuAction()
      {
        super();
      }

      protected boolean doNotSelected()
      {
        EditStyledText.this.showMenuAlertDialog();
        return true;
      }
    }

    public class ResetAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public ResetAction()
      {
        super();
      }

      protected boolean doNotSelected()
      {
        EditStyledText.EditorManager.access$2400(EditStyledText.EditModeActions.this.mManager);
        return true;
      }
    }

    public class EndEditAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public EndEditAction()
      {
        super();
      }

      protected boolean doNotSelected()
      {
        EditStyledText.EditorManager.access$4300(EditStyledText.EditModeActions.this.mManager);
        return true;
      }
    }

    public class StartEditAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public StartEditAction()
      {
        super();
      }

      protected boolean doNotSelected()
      {
        EditStyledText.EditorManager.access$4200(EditStyledText.EditModeActions.this.mManager);
        return true;
      }
    }

    public class PreviewAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public PreviewAction()
      {
        super();
      }

      protected boolean doNotSelected()
      {
        EditStyledText.this.showPreview();
        return true;
      }
    }

    public class BackgroundColorAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public BackgroundColorAction()
      {
        super();
      }

      protected boolean doNotSelected()
      {
        EditStyledText.StyledTextDialog.access$4000(EditStyledText.EditModeActions.this.mDialog);
        return true;
      }
    }

    public class ImageAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public ImageAction()
      {
        super();
      }

      protected boolean doNotSelected()
      {
        Object localObject = getParam(0);
        if (localObject != null)
          if ((localObject instanceof Uri))
          {
            EditStyledText.EditorManager localEditorManager1 = EditStyledText.EditModeActions.this.mManager;
            Uri localUri = (Uri)localObject;
            EditStyledText.EditorManager.access$3600(localEditorManager1, localUri);
          }
        while (true)
        {
          return true;
          if ((localObject instanceof Integer))
          {
            EditStyledText.EditorManager localEditorManager2 = EditStyledText.EditModeActions.this.mManager;
            int i = ((Integer)localObject).intValue();
            EditStyledText.EditorManager.access$3700(localEditorManager2, i);
            continue;
            EditStyledText.this.showInsertImageSelectAlertDialog();
          }
        }
      }
    }

    public class CancelAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public CancelAction()
      {
        super();
      }

      protected boolean doNotSelected()
      {
        EditStyledText.this.cancelViewManagers();
        return true;
      }
    }

    public class StopSelectionAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public StopSelectionAction()
      {
        super();
      }

      protected boolean doNotSelected()
      {
        EditStyledText.EditorManager.access$3400(EditStyledText.EditModeActions.this.mManager);
        return true;
      }
    }

    public class ClearStylesAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public ClearStylesAction()
      {
        super();
      }

      protected boolean doNotSelected()
      {
        EditStyledText.EditorManager.access$3300(EditStyledText.EditModeActions.this.mManager);
        return true;
      }
    }

    public class HorizontalLineAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public HorizontalLineAction()
      {
        super();
      }

      protected boolean doNotSelected()
      {
        EditStyledText.EditorManager.access$3200(EditStyledText.EditModeActions.this.mManager);
        return true;
      }
    }

    public class SelectAllAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public SelectAllAction()
      {
        super();
      }

      protected boolean doNotSelected()
      {
        EditStyledText.EditorManager.access$3100(EditStyledText.EditModeActions.this.mManager);
        return true;
      }
    }

    public class PasteAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public PasteAction()
      {
        super();
      }

      protected boolean doNotSelected()
      {
        EditStyledText.EditorManager.access$3000(EditStyledText.EditModeActions.this.mManager);
        EditStyledText.EditorManager.access$2400(EditStyledText.EditModeActions.this.mManager);
        return true;
      }
    }

    public class SelectAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public SelectAction()
      {
        super();
      }

      protected boolean doNotSelected()
      {
        if (EditStyledText.EditorManager.access$2700(EditStyledText.EditModeActions.this.mManager))
          int i = Log.e("EditModeActions", "Selection is off, but selected");
        EditStyledText.EditorManager.access$2800(EditStyledText.EditModeActions.this.mManager);
        EditStyledText.this.sendHintMessage(3);
        return true;
      }

      protected boolean doSelectionIsFixed()
      {
        return false;
      }

      protected boolean doStartPosIsSelected()
      {
        if (EditStyledText.EditorManager.access$2700(EditStyledText.EditModeActions.this.mManager))
          int i = Log.e("EditModeActions", "Selection now start, but selected");
        EditStyledText.EditorManager.access$2900(EditStyledText.EditModeActions.this.mManager);
        EditStyledText.this.sendHintMessage(4);
        if (EditStyledText.EditModeActions.this.mManager.getEditMode() != 5)
        {
          EditStyledText.EditModeActions localEditModeActions = EditStyledText.EditModeActions.this;
          int j = EditStyledText.EditModeActions.this.mManager.getEditMode();
          boolean bool = localEditModeActions.doNext(j);
        }
        return true;
      }
    }

    public class CutAction extends EditStyledText.EditModeActions.TextViewActionBase
    {
      public CutAction()
      {
        super();
      }

      protected boolean doEndPosIsSelected()
      {
        if (super.doEndPosIsSelected());
        while (true)
        {
          return true;
          EditStyledText.EditorManager.access$2600(EditStyledText.EditModeActions.this.mManager);
          EditStyledText.EditorManager.access$2400(EditStyledText.EditModeActions.this.mManager);
        }
      }
    }

    public class CopyAction extends EditStyledText.EditModeActions.TextViewActionBase
    {
      public CopyAction()
      {
        super();
      }

      protected boolean doEndPosIsSelected()
      {
        if (super.doEndPosIsSelected());
        while (true)
        {
          return true;
          EditStyledText.EditorManager.access$2500(EditStyledText.EditModeActions.this.mManager);
          EditStyledText.EditorManager.access$2400(EditStyledText.EditModeActions.this.mManager);
        }
      }
    }

    public class TextViewAction extends EditStyledText.EditModeActions.TextViewActionBase
    {
      public TextViewAction()
      {
        super();
      }

      protected boolean doEndPosIsSelected()
      {
        if (super.doEndPosIsSelected());
        while (true)
        {
          return true;
          Object localObject = getParam(0);
          if ((localObject != null) && ((localObject instanceof Integer)))
          {
            EditStyledText localEditStyledText = EditStyledText.this;
            int i = ((Integer)localObject).intValue();
            boolean bool = localEditStyledText.onTextContextMenuItem(i);
          }
          EditStyledText.EditorManager.access$2400(EditStyledText.EditModeActions.this.mManager);
        }
      }
    }

    public class TextViewActionBase extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public TextViewActionBase()
      {
        super();
      }

      protected boolean doEndPosIsSelected()
      {
        boolean bool1 = true;
        if ((EditStyledText.EditModeActions.this.mManager.getEditMode() == 0) || (EditStyledText.EditModeActions.this.mManager.getEditMode() == 5))
        {
          EditStyledText.EditorManager localEditorManager1 = EditStyledText.EditModeActions.this.mManager;
          int i = EditStyledText.EditModeActions.this.mMode;
          EditStyledText.EditorManager.access$2300(localEditorManager1, i);
          boolean bool2 = fixSelection();
          boolean bool3 = EditStyledText.EditModeActions.this.doNext();
        }
        while (true)
        {
          return bool1;
          int j = EditStyledText.EditModeActions.this.mManager.getEditMode();
          int k = EditStyledText.EditModeActions.this.mMode;
          if (j != k)
          {
            EditStyledText.EditorManager.access$2400(EditStyledText.EditModeActions.this.mManager);
            EditStyledText.EditorManager localEditorManager2 = EditStyledText.EditModeActions.this.mManager;
            int m = EditStyledText.EditModeActions.this.mMode;
            EditStyledText.EditorManager.access$2300(localEditorManager2, m);
            boolean bool4 = EditStyledText.EditModeActions.this.doNext();
          }
          else
          {
            bool1 = false;
          }
        }
      }

      protected boolean doNotSelected()
      {
        if ((EditStyledText.EditModeActions.this.mManager.getEditMode() == 0) || (EditStyledText.EditModeActions.this.mManager.getEditMode() == 5))
        {
          EditStyledText.EditorManager localEditorManager = EditStyledText.EditModeActions.this.mManager;
          int i = EditStyledText.EditModeActions.this.mMode;
          EditStyledText.EditorManager.access$2300(localEditorManager, i);
          EditStyledText.EditModeActions.this.onSelectAction();
        }
        for (boolean bool = true; ; bool = false)
          return bool;
      }
    }

    public class NothingAction extends EditStyledText.EditModeActions.EditModeActionBase
    {
      public NothingAction()
      {
        super();
      }
    }

    public class EditModeActionBase
    {
      private Object[] mParams;

      public EditModeActionBase()
      {
      }

      protected void addParams(Object[] paramArrayOfObject)
      {
        this.mParams = paramArrayOfObject;
      }

      protected boolean doEndPosIsSelected()
      {
        return doStartPosIsSelected();
      }

      protected boolean doNotSelected()
      {
        return false;
      }

      protected boolean doSelectionIsFixed()
      {
        return doEndPosIsSelected();
      }

      protected boolean doSelectionIsFixedAndWaitingInput()
      {
        return doEndPosIsSelected();
      }

      protected boolean doStartPosIsSelected()
      {
        return doNotSelected();
      }

      protected boolean fixSelection()
      {
        EditStyledText.this.finishComposingText();
        EditStyledText.EditorManager.access$2100(EditStyledText.EditModeActions.this.mManager, 3);
        return true;
      }

      protected Object getParam(int paramInt)
      {
        if (this.mParams != null)
        {
          int i = this.mParams.length;
          if (paramInt <= i);
        }
        else
        {
          int j = Log.d("EditModeActions", "--- Number of the parameter is out of bound.");
        }
        for (Object localObject = null; ; localObject = this.mParams[paramInt])
          return localObject;
      }
    }
  }

  public static class ColorPaletteDrawable extends ShapeDrawable
  {
    private Rect mRect;

    public ColorPaletteDrawable(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      super();
      int i = paramInt2 - paramInt4;
      int j = paramInt3 - paramInt4;
      Rect localRect = new Rect(paramInt4, paramInt4, i, j);
      this.mRect = localRect;
      getPaint().setColor(paramInt1);
    }

    public void draw(Canvas paramCanvas)
    {
      Rect localRect = this.mRect;
      Paint localPaint = getPaint();
      paramCanvas.drawRect(localRect, localPaint);
    }
  }

  public static class EditStyledTextSpans
  {
    public static class HorizontalLineDrawable extends ShapeDrawable
    {
      private static boolean DBG_HL = false;
      private Spannable mSpannable;
      private int mWidth;

      public HorizontalLineDrawable(int paramInt1, int paramInt2, Spannable paramSpannable)
      {
        super();
        this.mSpannable = paramSpannable;
        this.mWidth = paramInt2;
        renewColor(paramInt1);
        renewBounds(paramInt2);
      }

      private EditStyledText.EditStyledTextSpans.HorizontalLineSpan getParentSpan()
      {
        Spannable localSpannable = this.mSpannable;
        int i = localSpannable.length();
        EditStyledText.EditStyledTextSpans.HorizontalLineSpan[] arrayOfHorizontalLineSpan = (EditStyledText.EditStyledTextSpans.HorizontalLineSpan[])localSpannable.getSpans(0, i, EditStyledText.EditStyledTextSpans.HorizontalLineSpan.class);
        int k;
        EditStyledText.EditStyledTextSpans.HorizontalLineSpan localHorizontalLineSpan1;
        if (arrayOfHorizontalLineSpan.length > 0)
        {
          int j = arrayOfHorizontalLineSpan.length;
          k = 0;
          if (k < j)
          {
            localHorizontalLineSpan1 = arrayOfHorizontalLineSpan[k];
            if (localHorizontalLineSpan1.getDrawable() != this);
          }
        }
        for (EditStyledText.EditStyledTextSpans.HorizontalLineSpan localHorizontalLineSpan2 = localHorizontalLineSpan1; ; localHorizontalLineSpan2 = null)
        {
          return localHorizontalLineSpan2;
          k += 1;
          break;
          int m = Log.e("EditStyledTextSpan", "---renewBounds: Couldn't find");
        }
      }

      private void renewColor()
      {
        EditStyledText.EditStyledTextSpans.HorizontalLineSpan localHorizontalLineSpan = getParentSpan();
        Spannable localSpannable = this.mSpannable;
        int i = localSpannable.getSpanStart(localHorizontalLineSpan);
        int j = localSpannable.getSpanEnd(localHorizontalLineSpan);
        ForegroundColorSpan[] arrayOfForegroundColorSpan = (ForegroundColorSpan[])localSpannable.getSpans(i, j, ForegroundColorSpan.class);
        if (DBG_HL)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("--- renewColor:");
          int k = arrayOfForegroundColorSpan.length;
          String str = k;
          int m = Log.d("EditStyledTextSpan", str);
        }
        if (arrayOfForegroundColorSpan.length <= 0)
          return;
        int n = arrayOfForegroundColorSpan.length + -1;
        int i1 = arrayOfForegroundColorSpan[n].getForegroundColor();
        renewColor(i1);
      }

      private void renewColor(int paramInt)
      {
        if (DBG_HL)
        {
          String str = "--- renewColor:" + paramInt;
          int i = Log.d("EditStyledTextSpan", str);
        }
        getPaint().setColor(paramInt);
      }

      public void draw(Canvas paramCanvas)
      {
        renewColor();
        int i = this.mWidth;
        Rect localRect = new Rect(0, 9, i, 11);
        Paint localPaint = getPaint();
        paramCanvas.drawRect(localRect, localPaint);
      }

      public void renewBounds(int paramInt)
      {
        if (DBG_HL)
        {
          String str = "--- renewBounds:" + paramInt;
          int i = Log.d("EditStyledTextSpan", str);
        }
        if (paramInt > 20)
          paramInt -= 20;
        this.mWidth = paramInt;
        setBounds(0, 0, paramInt, 20);
      }
    }

    public static class RescalableImageSpan extends ImageSpan
    {
      private final int MAXWIDTH;
      Uri mContentUri;
      private Context mContext;
      private Drawable mDrawable;
      public int mIntrinsicHeight = -1;
      public int mIntrinsicWidth = -1;

      public RescalableImageSpan(Context paramContext, int paramInt1, int paramInt2)
      {
        super(paramInt1);
        this.mContext = paramContext;
        this.MAXWIDTH = paramInt2;
      }

      public RescalableImageSpan(Context paramContext, Uri paramUri, int paramInt)
      {
        super(paramUri);
        this.mContext = paramContext;
        this.mContentUri = paramUri;
        this.MAXWIDTH = paramInt;
      }

      private void rescaleBigImage(Drawable paramDrawable)
      {
        int i = Log.d("EditStyledTextSpan", "--- rescaleBigImage:");
        if (this.MAXWIDTH < 0)
          return;
        int j = paramDrawable.getIntrinsicWidth();
        int k = paramDrawable.getIntrinsicHeight();
        StringBuilder localStringBuilder = new StringBuilder().append("--- rescaleBigImage:").append(j).append(",").append(k).append(",");
        int m = this.MAXWIDTH;
        String str = m;
        int n = Log.d("EditStyledTextSpan", str);
        int i1 = this.MAXWIDTH;
        if (j > i1)
        {
          j = this.MAXWIDTH;
          k = this.MAXWIDTH * k / j;
        }
        paramDrawable.setBounds(0, 0, j, k);
      }

      public Uri getContentUri()
      {
        return this.mContentUri;
      }

      public Drawable getDrawable()
      {
        Drawable localDrawable1 = null;
        if (this.mDrawable != null)
        {
          localDrawable1 = this.mDrawable;
          return localDrawable1;
        }
        if (this.mContentUri != null)
          System.gc();
        while (true)
        {
          try
          {
            ContentResolver localContentResolver1 = this.mContext.getContentResolver();
            Uri localUri1 = this.mContentUri;
            InputStream localInputStream1 = localContentResolver1.openInputStream(localUri1);
            BitmapFactory.Options localOptions = new BitmapFactory.Options();
            localOptions.inJustDecodeBounds = true;
            Bitmap localBitmap1 = BitmapFactory.decodeStream(localInputStream1, null, localOptions);
            localInputStream1.close();
            ContentResolver localContentResolver2 = this.mContext.getContentResolver();
            Uri localUri2 = this.mContentUri;
            InputStream localInputStream2 = localContentResolver2.openInputStream(localUri2);
            int i = localOptions.outWidth;
            int j = localOptions.outHeight;
            this.mIntrinsicWidth = i;
            this.mIntrinsicHeight = j;
            int k = localOptions.outWidth;
            int m = this.MAXWIDTH;
            if (k > m)
            {
              i = this.MAXWIDTH;
              int n = this.MAXWIDTH * j;
              int i1 = localOptions.outWidth;
              j = n / i1;
              Rect localRect = new Rect(0, 0, i, j);
              localObject = BitmapFactory.decodeStream(localInputStream2, localRect, null);
              Resources localResources = this.mContext.getResources();
              BitmapDrawable localBitmapDrawable = new BitmapDrawable(localResources, (Bitmap)localObject);
              this.mDrawable = localBitmapDrawable;
              this.mDrawable.setBounds(0, 0, i, j);
              localInputStream2.close();
              localDrawable1 = this.mDrawable;
              break;
            }
            Bitmap localBitmap2 = BitmapFactory.decodeStream(localInputStream2);
            Object localObject = localBitmap2;
            continue;
          }
          catch (Exception localException)
          {
            StringBuilder localStringBuilder = new StringBuilder().append("Failed to loaded content ");
            Uri localUri3 = this.mContentUri;
            String str = localUri3;
            int i2 = Log.e("EditStyledTextSpan", str, localException);
          }
          catch (OutOfMemoryError localOutOfMemoryError)
          {
            int i3 = Log.e("EditStyledTextSpan", "OutOfMemoryError");
          }
          break;
          Drawable localDrawable2 = super.getDrawable();
          this.mDrawable = localDrawable2;
          Drawable localDrawable3 = this.mDrawable;
          rescaleBigImage(localDrawable3);
          int i4 = this.mDrawable.getIntrinsicWidth();
          this.mIntrinsicWidth = i4;
          int i5 = this.mDrawable.getIntrinsicHeight();
          this.mIntrinsicHeight = i5;
        }
      }
    }

    public static class HorizontalLineSpan extends DynamicDrawableSpan
    {
      EditStyledText.EditStyledTextSpans.HorizontalLineDrawable mDrawable;

      public HorizontalLineSpan(int paramInt1, int paramInt2, Spannable paramSpannable)
      {
        super();
        EditStyledText.EditStyledTextSpans.HorizontalLineDrawable localHorizontalLineDrawable = new EditStyledText.EditStyledTextSpans.HorizontalLineDrawable(paramInt1, paramInt2, paramSpannable);
        this.mDrawable = localHorizontalLineDrawable;
      }

      public Drawable getDrawable()
      {
        return this.mDrawable;
      }

      public void resetWidth(int paramInt)
      {
        this.mDrawable.renewBounds(paramInt);
      }
    }
  }

  public static class StyledTextInputConnection extends InputConnectionWrapper
  {
    EditStyledText mEST;

    public StyledTextInputConnection(InputConnection paramInputConnection, EditStyledText paramEditStyledText)
    {
      super(true);
      this.mEST = paramEditStyledText;
    }

    public boolean commitText(CharSequence paramCharSequence, int paramInt)
    {
      int i = Log.d("EditStyledText", "--- commitText:");
      this.mEST.mManager.unsetTextComposingMask();
      return super.commitText(paramCharSequence, paramInt);
    }

    public boolean finishComposingText()
    {
      int i = Log.d("EditStyledText", "--- finishcomposing:");
      if ((!this.mEST.isSoftKeyBlocked()) && (!this.mEST.isButtonsFocused()) && (!this.mEST.isEditting()))
        this.mEST.onEndEdit();
      return super.finishComposingText();
    }
  }

  private static class StyledTextArrowKeyMethod extends ArrowKeyMovementMethod
  {
    String LOG_TAG = "StyledTextArrowKeyMethod";
    EditStyledText.EditorManager mManager;

    StyledTextArrowKeyMethod(EditStyledText.EditorManager paramEditorManager)
    {
      this.mManager = paramEditorManager;
    }

    private boolean executeDown(TextView paramTextView, Spannable paramSpannable, int paramInt)
    {
      String str1 = this.LOG_TAG;
      String str2 = "--- executeDown: " + paramInt;
      int i = Log.d(str1, str2);
      boolean bool1 = false;
      switch (paramInt)
      {
      default:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      }
      while (true)
      {
        return bool1;
        boolean bool2 = up(paramTextView, paramSpannable);
        bool1 = false | bool2;
        continue;
        boolean bool3 = down(paramTextView, paramSpannable);
        bool1 = false | bool3;
        continue;
        boolean bool4 = left(paramTextView, paramSpannable);
        bool1 = false | bool4;
        continue;
        boolean bool5 = right(paramTextView, paramSpannable);
        bool1 = false | bool5;
        continue;
        this.mManager.onFixSelectedItem();
        bool1 = true;
      }
    }

    private int getEndPos(TextView paramTextView)
    {
      int i = paramTextView.getSelectionStart();
      int j = this.mManager.getSelectionStart();
      if (i != j);
      for (int k = paramTextView.getSelectionEnd(); ; k = paramTextView.getSelectionStart())
        return k;
    }

    protected boolean down(TextView paramTextView, Spannable paramSpannable)
    {
      int i = Log.d(this.LOG_TAG, "--- down:");
      Layout localLayout = paramTextView.getLayout();
      int j = getEndPos(paramTextView);
      int k = localLayout.getLineForOffset(j);
      int m = localLayout.getLineCount() + -1;
      float f;
      int i3;
      if (k < m)
      {
        int n = localLayout.getParagraphDirection(k);
        int i1 = k + 1;
        int i2 = localLayout.getParagraphDirection(i1);
        if (n == i2)
          break label124;
        f = localLayout.getPrimaryHorizontal(j);
        i3 = k + 1;
      }
      label124: int i5;
      for (int i4 = localLayout.getOffsetForHorizontal(i3, f); ; i4 = localLayout.getLineStart(i5))
      {
        this.mManager.setEndPos(i4);
        this.mManager.onCursorMoved();
        return true;
        i5 = k + 1;
      }
    }

    protected boolean left(TextView paramTextView, Spannable paramSpannable)
    {
      int i = Log.d(this.LOG_TAG, "--- left:");
      Layout localLayout = paramTextView.getLayout();
      int j = getEndPos(paramTextView);
      int k = localLayout.getOffsetToLeftOf(j);
      this.mManager.setEndPos(k);
      this.mManager.onCursorMoved();
      return true;
    }

    public boolean onKeyDown(TextView paramTextView, Spannable paramSpannable, int paramInt, KeyEvent paramKeyEvent)
    {
      String str1 = this.LOG_TAG;
      String str2 = "---onkeydown:" + paramInt;
      int i = Log.d(str1, str2);
      this.mManager.unsetTextComposingMask();
      if ((this.mManager.getSelectState() == 1) || (this.mManager.getSelectState() == 2));
      for (boolean bool = executeDown(paramTextView, paramSpannable, paramInt); ; bool = super.onKeyDown(paramTextView, paramSpannable, paramInt, paramKeyEvent))
        return bool;
    }

    protected boolean right(TextView paramTextView, Spannable paramSpannable)
    {
      int i = Log.d(this.LOG_TAG, "--- right:");
      Layout localLayout = paramTextView.getLayout();
      int j = getEndPos(paramTextView);
      int k = localLayout.getOffsetToRightOf(j);
      this.mManager.setEndPos(k);
      this.mManager.onCursorMoved();
      return true;
    }

    protected boolean up(TextView paramTextView, Spannable paramSpannable)
    {
      int i = Log.d(this.LOG_TAG, "--- up:");
      Layout localLayout = paramTextView.getLayout();
      int j = getEndPos(paramTextView);
      int k = localLayout.getLineForOffset(j);
      float f;
      int i2;
      if (k > 0)
      {
        int m = localLayout.getParagraphDirection(k);
        int n = k + -1;
        int i1 = localLayout.getParagraphDirection(n);
        if (m == i1)
          break label114;
        f = localLayout.getPrimaryHorizontal(j);
        i2 = k + -1;
      }
      label114: int i4;
      for (int i3 = localLayout.getOffsetForHorizontal(i2, f); ; i3 = localLayout.getLineStart(i4))
      {
        this.mManager.setEndPos(i3);
        this.mManager.onCursorMoved();
        return true;
        i4 = k + -1;
      }
    }
  }

  private class MenuHandler
    implements MenuItem.OnMenuItemClickListener
  {
    private MenuHandler()
    {
    }

    public boolean onMenuItemClick(MenuItem paramMenuItem)
    {
      EditStyledText localEditStyledText = EditStyledText.this;
      int i = paramMenuItem.getItemId();
      return localEditStyledText.onTextContextMenuItem(i);
    }
  }

  private static class StyledTextDialog
  {
    private AlertDialog mAlertDialog;
    private CharSequence[] mAlignNames;
    private CharSequence mAlignTitle;
    private AlertDialog.Builder mBuilder;
    private CharSequence mColorDefaultMessage;
    private CharSequence[] mColorInts;
    private CharSequence[] mColorNames;
    private CharSequence mColorTitle;
    private EditStyledText mEST;
    private CharSequence[] mMarqueeNames;
    private CharSequence mMarqueeTitle;
    private CharSequence[] mSizeDisplayInts;
    private CharSequence[] mSizeNames;
    private CharSequence[] mSizeSendInts;
    private CharSequence mSizeTitle;

    public StyledTextDialog(EditStyledText paramEditStyledText)
    {
      this.mEST = paramEditStyledText;
    }

    private void buildAndShowColorDialogue(int paramInt, CharSequence paramCharSequence, int[] paramArrayOfInt)
    {
      int i = this.mEST.dipToPx(50);
      int j = this.mEST.dipToPx(2);
      int k = this.mEST.dipToPx(15);
      AlertDialog.Builder localBuilder1 = this.mBuilder.setTitle(paramCharSequence);
      AlertDialog.Builder localBuilder2 = this.mBuilder.setIcon(0);
      AlertDialog.Builder localBuilder3 = this.mBuilder.setPositiveButton(null, null);
      AlertDialog.Builder localBuilder4 = this.mBuilder;
      DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          EditStyledText.this.onStartEdit();
        }
      };
      AlertDialog.Builder localBuilder5 = localBuilder4.setNegativeButton(17039360, local3);
      AlertDialog.Builder localBuilder6 = this.mBuilder.setItems(null, null);
      Context localContext1 = this.mEST.getContext();
      LinearLayout localLinearLayout1 = new LinearLayout(localContext1);
      localLinearLayout1.setOrientation(1);
      localLinearLayout1.setGravity(1);
      localLinearLayout1.setPadding(k, k, k, k);
      LinearLayout localLinearLayout2 = null;
      int m = 0;
      int n = paramArrayOfInt.length;
      if (m < n)
      {
        if (m % 5 == 0)
        {
          Context localContext2 = this.mEST.getContext();
          localLinearLayout2 = new LinearLayout(localContext2);
          localLinearLayout1.addView(localLinearLayout2);
        }
        Context localContext3 = this.mEST.getContext();
        Button localButton = new Button(localContext3);
        localButton.setHeight(i);
        localButton.setWidth(i);
        int i1 = paramArrayOfInt[m];
        EditStyledText.ColorPaletteDrawable localColorPaletteDrawable = new EditStyledText.ColorPaletteDrawable(i1, i, i, j);
        localButton.setBackgroundDrawable(localColorPaletteDrawable);
        int i2 = paramArrayOfInt[m];
        localButton.setDrawingCacheBackgroundColor(i2);
        if (paramInt == 0)
        {
          View.OnClickListener local4 = new View.OnClickListener()
          {
            public void onClick(View paramAnonymousView)
            {
              EditStyledText localEditStyledText = EditStyledText.this;
              int i = paramAnonymousView.getDrawingCacheBackgroundColor();
              localEditStyledText.setItemColor(i);
              if (EditStyledText.StyledTextDialog.this.mAlertDialog != null)
              {
                EditStyledText.StyledTextDialog.this.mAlertDialog.setView(null);
                EditStyledText.StyledTextDialog.this.mAlertDialog.dismiss();
                AlertDialog localAlertDialog = EditStyledText.StyledTextDialog.access$1502(EditStyledText.StyledTextDialog.this, null);
                return;
              }
              int j = Log.e("EditStyledText", "--- buildAndShowColorDialogue: can't find alertDialog");
            }
          };
          localButton.setOnClickListener(local4);
        }
        while (true)
        {
          localLinearLayout2.addView(localButton);
          m += 1;
          break;
          if (paramInt == 1)
          {
            View.OnClickListener local5 = new View.OnClickListener()
            {
              public void onClick(View paramAnonymousView)
              {
                EditStyledText localEditStyledText = EditStyledText.this;
                int i = paramAnonymousView.getDrawingCacheBackgroundColor();
                localEditStyledText.setBackgroundColor(i);
                if (EditStyledText.StyledTextDialog.this.mAlertDialog != null)
                {
                  EditStyledText.StyledTextDialog.this.mAlertDialog.setView(null);
                  EditStyledText.StyledTextDialog.this.mAlertDialog.dismiss();
                  AlertDialog localAlertDialog = EditStyledText.StyledTextDialog.access$1502(EditStyledText.StyledTextDialog.this, null);
                  return;
                }
                int j = Log.e("EditStyledText", "--- buildAndShowColorDialogue: can't find alertDialog");
              }
            };
            localButton.setOnClickListener(local5);
          }
        }
      }
      if (paramInt == 1)
      {
        AlertDialog.Builder localBuilder7 = this.mBuilder;
        CharSequence localCharSequence1 = this.mColorDefaultMessage;
        DialogInterface.OnClickListener local6 = new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
          {
            EditStyledText.this.setBackgroundColor(16777215);
          }
        };
        AlertDialog.Builder localBuilder8 = localBuilder7.setPositiveButton(localCharSequence1, local6);
      }
      while (true)
      {
        AlertDialog.Builder localBuilder9 = this.mBuilder.setView(localLinearLayout1);
        AlertDialog.Builder localBuilder10 = this.mBuilder.setCancelable(true);
        AlertDialog.Builder localBuilder11 = this.mBuilder;
        DialogInterface.OnCancelListener local8 = new DialogInterface.OnCancelListener()
        {
          public void onCancel(DialogInterface paramAnonymousDialogInterface)
          {
            EditStyledText.this.onStartEdit();
          }
        };
        AlertDialog.Builder localBuilder12 = localBuilder11.setOnCancelListener(local8);
        AlertDialog localAlertDialog = this.mBuilder.show();
        this.mAlertDialog = localAlertDialog;
        return;
        if (paramInt == 0)
        {
          AlertDialog.Builder localBuilder13 = this.mBuilder;
          CharSequence localCharSequence2 = this.mColorDefaultMessage;
          DialogInterface.OnClickListener local7 = new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
              EditStyledText.this.setItemColor(-16777216);
            }
          };
          AlertDialog.Builder localBuilder14 = localBuilder13.setPositiveButton(localCharSequence2, local7);
        }
      }
    }

    private void buildDialogue(CharSequence paramCharSequence, CharSequence[] paramArrayOfCharSequence, DialogInterface.OnClickListener paramOnClickListener)
    {
      AlertDialog.Builder localBuilder1 = this.mBuilder.setTitle(paramCharSequence);
      AlertDialog.Builder localBuilder2 = this.mBuilder.setIcon(0);
      AlertDialog.Builder localBuilder3 = this.mBuilder.setPositiveButton(null, null);
      AlertDialog.Builder localBuilder4 = this.mBuilder;
      DialogInterface.OnClickListener local1 = new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          EditStyledText.this.onStartEdit();
        }
      };
      AlertDialog.Builder localBuilder5 = localBuilder4.setNegativeButton(17039360, local1);
      AlertDialog.Builder localBuilder6 = this.mBuilder.setItems(paramArrayOfCharSequence, paramOnClickListener);
      AlertDialog.Builder localBuilder7 = this.mBuilder.setView(null);
      AlertDialog.Builder localBuilder8 = this.mBuilder.setCancelable(true);
      AlertDialog.Builder localBuilder9 = this.mBuilder;
      DialogInterface.OnCancelListener local2 = new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramAnonymousDialogInterface)
        {
          int i = Log.d("EditStyledText", "--- oncancel");
          EditStyledText.this.onStartEdit();
        }
      };
      AlertDialog.Builder localBuilder10 = localBuilder9.setOnCancelListener(local2);
      AlertDialog localAlertDialog = this.mBuilder.show();
    }

    private boolean checkAlignAlertParams()
    {
      boolean bool = false;
      int i = Log.d("EditStyledText", "--- checkAlignAlertParams");
      if (this.mBuilder == null)
        int j = Log.e("EditStyledText", "--- builder is null.");
      while (true)
      {
        return bool;
        if (this.mAlignTitle == null)
          int k = Log.e("EditStyledText", "--- align alert params are null.");
        else
          bool = true;
      }
    }

    private boolean checkColorAlertParams()
    {
      boolean bool = false;
      int i = Log.d("EditStyledText", "--- checkParams");
      if (this.mBuilder == null)
        int j = Log.e("EditStyledText", "--- builder is null.");
      while (true)
      {
        return bool;
        if ((this.mColorTitle == null) || (this.mColorNames == null) || (this.mColorInts == null))
        {
          int k = Log.e("EditStyledText", "--- color alert params are null.");
        }
        else
        {
          int m = this.mColorNames.length;
          int n = this.mColorInts.length;
          if (m != n)
            int i1 = Log.e("EditStyledText", "--- the length of color alert params are different.");
          else
            bool = true;
        }
      }
    }

    private boolean checkMarqueeAlertParams()
    {
      boolean bool = false;
      int i = Log.d("EditStyledText", "--- checkMarqueeAlertParams");
      if (this.mBuilder == null)
        int j = Log.e("EditStyledText", "--- builder is null.");
      while (true)
      {
        return bool;
        if (this.mMarqueeTitle == null)
          int k = Log.e("EditStyledText", "--- Marquee alert params are null.");
        else
          bool = true;
      }
    }

    private boolean checkSizeAlertParams()
    {
      boolean bool = false;
      int i = Log.d("EditStyledText", "--- checkParams");
      if (this.mBuilder == null)
        int j = Log.e("EditStyledText", "--- builder is null.");
      while (true)
      {
        return bool;
        if ((this.mSizeTitle == null) || (this.mSizeNames == null) || (this.mSizeDisplayInts == null) || (this.mSizeSendInts == null))
        {
          int k = Log.e("EditStyledText", "--- size alert params are null.");
        }
        else
        {
          int m = this.mSizeNames.length;
          int n = this.mSizeDisplayInts.length;
          if (m != n)
          {
            int i1 = this.mSizeSendInts.length;
            int i2 = this.mSizeDisplayInts.length;
            if (i1 != i2)
              int i3 = Log.e("EditStyledText", "--- the length of size alert params are different.");
          }
          else
          {
            bool = true;
          }
        }
      }
    }

    private void onShowAlignAlertDialog()
    {
      int i = Log.d("EditStyledText", "--- onShowAlignAlertDialog");
      if (!checkAlignAlertParams())
        return;
      CharSequence localCharSequence = this.mAlignTitle;
      CharSequence[] arrayOfCharSequence = this.mAlignNames;
      DialogInterface.OnClickListener local10 = new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          Layout.Alignment localAlignment = Layout.Alignment.ALIGN_NORMAL;
          switch (paramAnonymousInt)
          {
          default:
            int i = Log.e("EditStyledText", "--- onShowAlignAlertDialog: got illigal align.");
          case 0:
          case 1:
          case 2:
          }
          while (true)
          {
            EditStyledText.this.setAlignment(localAlignment);
            return;
            localAlignment = Layout.Alignment.ALIGN_NORMAL;
            continue;
            localAlignment = Layout.Alignment.ALIGN_CENTER;
            continue;
            localAlignment = Layout.Alignment.ALIGN_OPPOSITE;
          }
        }
      };
      buildDialogue(localCharSequence, arrayOfCharSequence, local10);
    }

    private void onShowBackgroundColorAlertDialog()
    {
      int i = Log.d("EditStyledText", "--- onShowBackgroundColorAlertDialog");
      if (!checkColorAlertParams())
        return;
      int[] arrayOfInt = new int[this.mColorInts.length];
      int j = 0;
      while (true)
      {
        int k = arrayOfInt.length;
        if (j >= k)
          break;
        int m = Integer.parseInt((String)this.mColorInts[j], 16) - 16777216;
        arrayOfInt[j] = m;
        j += 1;
      }
      CharSequence localCharSequence = this.mColorTitle;
      buildAndShowColorDialogue(1, localCharSequence, arrayOfInt);
    }

    private void onShowForegroundColorAlertDialog()
    {
      int i = Log.d("EditStyledText", "--- onShowForegroundColorAlertDialog");
      if (!checkColorAlertParams())
        return;
      int[] arrayOfInt = new int[this.mColorInts.length];
      int j = 0;
      while (true)
      {
        int k = arrayOfInt.length;
        if (j >= k)
          break;
        int m = Integer.parseInt((String)this.mColorInts[j], 16) - 16777216;
        arrayOfInt[j] = m;
        j += 1;
      }
      CharSequence localCharSequence = this.mColorTitle;
      buildAndShowColorDialogue(0, localCharSequence, arrayOfInt);
    }

    private void onShowMarqueeAlertDialog()
    {
      int i = Log.d("EditStyledText", "--- onShowMarqueeAlertDialog");
      if (!checkMarqueeAlertParams())
        return;
      CharSequence localCharSequence = this.mMarqueeTitle;
      CharSequence[] arrayOfCharSequence = this.mMarqueeNames;
      DialogInterface.OnClickListener local11 = new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          String str = "mBuilder.onclick:" + paramAnonymousInt;
          int i = Log.d("EditStyledText", str);
          EditStyledText.this.setMarquee(paramAnonymousInt);
        }
      };
      buildDialogue(localCharSequence, arrayOfCharSequence, local11);
    }

    private void onShowSizeAlertDialog()
    {
      int i = Log.d("EditStyledText", "--- onShowSizeAlertDialog");
      if (!checkSizeAlertParams())
        return;
      CharSequence localCharSequence = this.mSizeTitle;
      CharSequence[] arrayOfCharSequence = this.mSizeNames;
      DialogInterface.OnClickListener local9 = new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          String str = "mBuilder.onclick:" + paramAnonymousInt;
          int i = Log.d("EditStyledText", str);
          EditStyledText localEditStyledText = EditStyledText.this;
          int j = Integer.parseInt((String)EditStyledText.StyledTextDialog.this.mSizeDisplayInts[paramAnonymousInt]);
          int k = localEditStyledText.dipToPx(j);
          EditStyledText.this.setItemSize(k);
        }
      };
      buildDialogue(localCharSequence, arrayOfCharSequence, local9);
    }

    public void setAlignAlertParams(CharSequence paramCharSequence, CharSequence[] paramArrayOfCharSequence)
    {
      this.mAlignTitle = paramCharSequence;
      this.mAlignNames = paramArrayOfCharSequence;
    }

    public void setBuilder(AlertDialog.Builder paramBuilder)
    {
      this.mBuilder = paramBuilder;
    }

    public void setColorAlertParams(CharSequence paramCharSequence1, CharSequence[] paramArrayOfCharSequence1, CharSequence[] paramArrayOfCharSequence2, CharSequence paramCharSequence2)
    {
      this.mColorTitle = paramCharSequence1;
      this.mColorNames = paramArrayOfCharSequence1;
      this.mColorInts = paramArrayOfCharSequence2;
      this.mColorDefaultMessage = paramCharSequence2;
    }

    public void setMarqueeAlertParams(CharSequence paramCharSequence, CharSequence[] paramArrayOfCharSequence)
    {
      this.mMarqueeTitle = paramCharSequence;
      this.mMarqueeNames = paramArrayOfCharSequence;
    }

    public void setSizeAlertParams(CharSequence paramCharSequence, CharSequence[] paramArrayOfCharSequence1, CharSequence[] paramArrayOfCharSequence2, CharSequence[] paramArrayOfCharSequence3)
    {
      this.mSizeTitle = paramCharSequence;
      this.mSizeNames = paramArrayOfCharSequence1;
      this.mSizeDisplayInts = paramArrayOfCharSequence2;
      this.mSizeSendInts = paramArrayOfCharSequence3;
    }
  }

  public static class SavedStyledTextState extends View.BaseSavedState
  {
    public int mBackgroundColor;

    SavedStyledTextState(Parcelable paramParcelable)
    {
      super();
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("EditStyledText.SavedState{");
      String str = Integer.toHexString(System.identityHashCode(this));
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str).append(" bgcolor=");
      int i = this.mBackgroundColor;
      return i + "}";
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      int i = this.mBackgroundColor;
      paramParcel.writeInt(i);
    }
  }

  private static class SoftKeyReceiver extends ResultReceiver
  {
    EditStyledText mEST;
    int mNewEnd;
    int mNewStart;

    SoftKeyReceiver(EditStyledText paramEditStyledText)
    {
      super();
      this.mEST = paramEditStyledText;
    }

    protected void onReceiveResult(int paramInt, Bundle paramBundle)
    {
      if (paramInt == 2)
        return;
      Editable localEditable = this.mEST.getText();
      int i = this.mNewStart;
      int j = this.mNewEnd;
      Selection.setSelection(localEditable, i, j);
    }
  }

  private class StyledTextConverter
  {
    private EditStyledText mEST;
    private EditStyledText.StyledTextHtmlConverter mHtml;

    public StyledTextConverter(EditStyledText paramStyledTextHtmlConverter, EditStyledText.StyledTextHtmlConverter arg3)
    {
      this.mEST = paramStyledTextHtmlConverter;
      Object localObject;
      this.mHtml = localObject;
    }

    public void SetHtml(String paramString)
    {
      EditStyledText.StyledTextHtmlConverter localStyledTextHtmlConverter = this.mHtml;
      Html.ImageGetter local1 = new Html.ImageGetter()
      {
        // ERROR //
        public Drawable getDrawable(String paramAnonymousString)
        {
          // Byte code:
          //   0: new 30	java/lang/StringBuilder
          //   3: dup
          //   4: invokespecial 31	java/lang/StringBuilder:<init>	()V
          //   7: ldc 33
          //   9: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   12: aload_1
          //   13: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   16: invokevirtual 41	java/lang/StringBuilder:toString	()Ljava/lang/String;
          //   19: astore_2
          //   20: ldc 43
          //   22: aload_2
          //   23: invokestatic 49	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
          //   26: istore_3
          //   27: aload_1
          //   28: ldc 51
          //   30: invokevirtual 57	java/lang/String:startsWith	(Ljava/lang/String;)Z
          //   33: ifeq +326 -> 359
          //   36: aload_1
          //   37: invokestatic 63	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
          //   40: astore 4
          //   42: invokestatic 68	java/lang/System:gc	()V
          //   45: aload_0
          //   46: getfield 19	com/android/ex/editstyledtext/EditStyledText$StyledTextConverter$1:this$1	Lcom/android/ex/editstyledtext/EditStyledText$StyledTextConverter;
          //   49: invokestatic 72	com/android/ex/editstyledtext/EditStyledText$StyledTextConverter:access$1200	(Lcom/android/ex/editstyledtext/EditStyledText$StyledTextConverter;)Lcom/android/ex/editstyledtext/EditStyledText;
          //   52: invokevirtual 78	com/android/ex/editstyledtext/EditStyledText:getContext	()Landroid/content/Context;
          //   55: invokevirtual 84	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
          //   58: aload 4
          //   60: invokevirtual 90	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
          //   63: astore 5
          //   65: new 92	android/graphics/BitmapFactory$Options
          //   68: dup
          //   69: invokespecial 93	android/graphics/BitmapFactory$Options:<init>	()V
          //   72: astore 6
          //   74: aload 6
          //   76: iconst_1
          //   77: putfield 97	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
          //   80: aload 5
          //   82: aconst_null
          //   83: aload 6
          //   85: invokestatic 103	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
          //   88: astore 7
          //   90: aload 5
          //   92: invokevirtual 108	java/io/InputStream:close	()V
          //   95: aload_0
          //   96: getfield 19	com/android/ex/editstyledtext/EditStyledText$StyledTextConverter$1:this$1	Lcom/android/ex/editstyledtext/EditStyledText$StyledTextConverter;
          //   99: invokestatic 72	com/android/ex/editstyledtext/EditStyledText$StyledTextConverter:access$1200	(Lcom/android/ex/editstyledtext/EditStyledText$StyledTextConverter;)Lcom/android/ex/editstyledtext/EditStyledText;
          //   102: invokevirtual 78	com/android/ex/editstyledtext/EditStyledText:getContext	()Landroid/content/Context;
          //   105: invokevirtual 84	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
          //   108: aload 4
          //   110: invokevirtual 90	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
          //   113: astore 8
          //   115: aload 6
          //   117: getfield 112	android/graphics/BitmapFactory$Options:outWidth	I
          //   120: istore 9
          //   122: aload 6
          //   124: getfield 115	android/graphics/BitmapFactory$Options:outHeight	I
          //   127: istore 10
          //   129: aload 6
          //   131: getfield 112	android/graphics/BitmapFactory$Options:outWidth	I
          //   134: istore 11
          //   136: aload_0
          //   137: getfield 19	com/android/ex/editstyledtext/EditStyledText$StyledTextConverter$1:this$1	Lcom/android/ex/editstyledtext/EditStyledText$StyledTextConverter;
          //   140: getfield 119	com/android/ex/editstyledtext/EditStyledText$StyledTextConverter:this$0	Lcom/android/ex/editstyledtext/EditStyledText;
          //   143: invokestatic 123	com/android/ex/editstyledtext/EditStyledText:access$400	(Lcom/android/ex/editstyledtext/EditStyledText;)I
          //   146: istore 12
          //   148: iload 11
          //   150: iload 12
          //   152: if_icmple +124 -> 276
          //   155: aload_0
          //   156: getfield 19	com/android/ex/editstyledtext/EditStyledText$StyledTextConverter$1:this$1	Lcom/android/ex/editstyledtext/EditStyledText$StyledTextConverter;
          //   159: getfield 119	com/android/ex/editstyledtext/EditStyledText$StyledTextConverter:this$0	Lcom/android/ex/editstyledtext/EditStyledText;
          //   162: invokestatic 123	com/android/ex/editstyledtext/EditStyledText:access$400	(Lcom/android/ex/editstyledtext/EditStyledText;)I
          //   165: istore 9
          //   167: aload_0
          //   168: getfield 19	com/android/ex/editstyledtext/EditStyledText$StyledTextConverter$1:this$1	Lcom/android/ex/editstyledtext/EditStyledText$StyledTextConverter;
          //   171: getfield 119	com/android/ex/editstyledtext/EditStyledText$StyledTextConverter:this$0	Lcom/android/ex/editstyledtext/EditStyledText;
          //   174: invokestatic 123	com/android/ex/editstyledtext/EditStyledText:access$400	(Lcom/android/ex/editstyledtext/EditStyledText;)I
          //   177: iload 10
          //   179: imul
          //   180: istore 13
          //   182: aload 6
          //   184: getfield 112	android/graphics/BitmapFactory$Options:outWidth	I
          //   187: istore 14
          //   189: iload 13
          //   191: iload 14
          //   193: idiv
          //   194: istore 10
          //   196: new 125	android/graphics/Rect
          //   199: dup
          //   200: iconst_0
          //   201: iconst_0
          //   202: iload 9
          //   204: iload 10
          //   206: invokespecial 128	android/graphics/Rect:<init>	(IIII)V
          //   209: astore 15
          //   211: aload 8
          //   213: aload 15
          //   215: aconst_null
          //   216: invokestatic 103	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
          //   219: astore 16
          //   221: aload_0
          //   222: getfield 19	com/android/ex/editstyledtext/EditStyledText$StyledTextConverter$1:this$1	Lcom/android/ex/editstyledtext/EditStyledText$StyledTextConverter;
          //   225: invokestatic 72	com/android/ex/editstyledtext/EditStyledText$StyledTextConverter:access$1200	(Lcom/android/ex/editstyledtext/EditStyledText$StyledTextConverter;)Lcom/android/ex/editstyledtext/EditStyledText;
          //   228: invokevirtual 78	com/android/ex/editstyledtext/EditStyledText:getContext	()Landroid/content/Context;
          //   231: invokevirtual 132	android/content/Context:getResources	()Landroid/content/res/Resources;
          //   234: astore 17
          //   236: new 134	android/graphics/drawable/BitmapDrawable
          //   239: dup
          //   240: aload 17
          //   242: aload 16
          //   244: invokespecial 137	android/graphics/drawable/BitmapDrawable:<init>	(Landroid/content/res/Resources;Landroid/graphics/Bitmap;)V
          //   247: astore 18
          //   249: iconst_0
          //   250: istore 19
          //   252: iconst_0
          //   253: istore 20
          //   255: aload 18
          //   257: iload 19
          //   259: iload 20
          //   261: iload 9
          //   263: iload 10
          //   265: invokevirtual 142	android/graphics/drawable/Drawable:setBounds	(IIII)V
          //   268: aload 8
          //   270: invokevirtual 108	java/io/InputStream:close	()V
          //   273: aload 18
          //   275: areturn
          //   276: aload 8
          //   278: invokestatic 145	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;)Landroid/graphics/Bitmap;
          //   281: astore 21
          //   283: aload 21
          //   285: astore 16
          //   287: goto -66 -> 221
          //   290: astore 22
          //   292: new 30	java/lang/StringBuilder
          //   295: dup
          //   296: invokespecial 31	java/lang/StringBuilder:<init>	()V
          //   299: ldc 147
          //   301: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   304: aload 4
          //   306: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
          //   309: invokevirtual 41	java/lang/StringBuilder:toString	()Ljava/lang/String;
          //   312: astore 23
          //   314: ldc 43
          //   316: aload 23
          //   318: aload 22
          //   320: invokestatic 154	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
          //   323: istore 24
          //   325: aconst_null
          //   326: astore 18
          //   328: goto -55 -> 273
          //   331: astore 25
          //   333: ldc 43
          //   335: ldc 156
          //   337: invokestatic 158	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
          //   340: istore 26
          //   342: aload_0
          //   343: getfield 19	com/android/ex/editstyledtext/EditStyledText$StyledTextConverter$1:this$1	Lcom/android/ex/editstyledtext/EditStyledText$StyledTextConverter;
          //   346: invokestatic 72	com/android/ex/editstyledtext/EditStyledText$StyledTextConverter:access$1200	(Lcom/android/ex/editstyledtext/EditStyledText$StyledTextConverter;)Lcom/android/ex/editstyledtext/EditStyledText;
          //   349: iconst_5
          //   350: invokevirtual 162	com/android/ex/editstyledtext/EditStyledText:setHint	(I)V
          //   353: aconst_null
          //   354: astore 18
          //   356: goto -83 -> 273
          //   359: aconst_null
          //   360: astore 18
          //   362: goto -89 -> 273
          //   365: astore 27
          //   367: aload 18
          //   369: astore 28
          //   371: goto -38 -> 333
          //   374: astore 22
          //   376: aload 18
          //   378: astore 29
          //   380: goto -88 -> 292
          //
          // Exception table:
          //   from	to	target	type
          //   42	249	290	java/lang/Exception
          //   276	283	290	java/lang/Exception
          //   42	249	331	java/lang/OutOfMemoryError
          //   276	283	331	java/lang/OutOfMemoryError
          //   255	273	365	java/lang/OutOfMemoryError
          //   255	273	374	java/lang/Exception
        }
      };
      Spanned localSpanned = localStyledTextHtmlConverter.fromHtml(paramString, local1, null);
      this.mEST.setText(localSpanned);
    }

    public void setStyledTextHtmlConverter(EditStyledText.StyledTextHtmlConverter paramStyledTextHtmlConverter)
    {
      this.mHtml = paramStyledTextHtmlConverter;
    }
  }

  private class StyledTextHtmlStandard
    implements EditStyledText.StyledTextHtmlConverter
  {
    private StyledTextHtmlStandard()
    {
    }

    public Spanned fromHtml(String paramString, Html.ImageGetter paramImageGetter, Html.TagHandler paramTagHandler)
    {
      return Html.fromHtml(paramString, paramImageGetter, paramTagHandler);
    }
  }

  private class EditorManager
  {
    private EditStyledText.EditModeActions mActions;
    private int mBackgroundColor = 16777215;
    private int mColorWaitInput = 16777215;
    private BackgroundColorSpan mComposingTextMask;
    private SpannableStringBuilder mCopyBuffer;
    private int mCurEnd = 0;
    private int mCurStart = 0;
    private EditStyledText mEST;
    private boolean mEditFlag = false;
    private boolean mKeepNonLineSpan = false;
    private int mMode = 0;
    private int mSizeWaitInput = 0;
    private EditStyledText.SoftKeyReceiver mSkr;
    private boolean mSoftKeyBlockFlag = false;
    private int mState = 0;
    private boolean mTextIsFinishedFlag = false;
    private boolean mWaitInputFlag = false;

    EditorManager(EditStyledText paramStyledTextDialog, EditStyledText.StyledTextDialog arg3)
    {
      this.mEST = paramStyledTextDialog;
      EditStyledText localEditStyledText1 = this.mEST;
      EditStyledText.StyledTextDialog localStyledTextDialog;
      EditStyledText.EditModeActions localEditModeActions = new EditStyledText.EditModeActions(EditStyledText.this, localEditStyledText1, this, localStyledTextDialog);
      this.mActions = localEditModeActions;
      EditStyledText localEditStyledText2 = this.mEST;
      EditStyledText.SoftKeyReceiver localSoftKeyReceiver = new EditStyledText.SoftKeyReceiver(localEditStyledText2);
      this.mSkr = localSoftKeyReceiver;
    }

    private void addMarquee(int paramInt)
    {
      String str = "--- addMarquee:" + paramInt;
      int i = Log.d("EditStyledText.EditorManager", str);
      int j = this.mEST.getBackgroundColor();
      EditStyledText.EditStyledTextSpans.MarqueeSpan localMarqueeSpan = new EditStyledText.EditStyledTextSpans.MarqueeSpan(paramInt, j);
      setLineStyledTextSpan(localMarqueeSpan);
    }

    private void addSwing()
    {
      addMarquee(0);
    }

    private void addTelop()
    {
      addMarquee(1);
    }

    private void changeAlign(Layout.Alignment paramAlignment)
    {
      AlignmentSpan.Standard localStandard = new AlignmentSpan.Standard(paramAlignment);
      setLineStyledTextSpan(localStandard);
    }

    private void changeColorSelectedText(int paramInt)
    {
      int i = this.mCurStart;
      int j = this.mCurEnd;
      if (i != j)
      {
        ForegroundColorSpan localForegroundColorSpan = new ForegroundColorSpan(paramInt);
        int k = this.mCurStart;
        int m = this.mCurEnd;
        setStyledTextSpan(localForegroundColorSpan, k, m);
        return;
      }
      int n = Log.e("EditStyledText.EditorManager", "---changeColor: Size of the span is zero");
    }

    private void changeSizeSelectedText(int paramInt)
    {
      int i = this.mCurStart;
      int j = this.mCurEnd;
      if (i != j)
      {
        AbsoluteSizeSpan localAbsoluteSizeSpan = new AbsoluteSizeSpan(paramInt);
        int k = this.mCurStart;
        int m = this.mCurEnd;
        setStyledTextSpan(localAbsoluteSizeSpan, k, m);
        return;
      }
      int n = Log.e("EditStyledText.EditorManager", "---changeSize: Size of the span is zero");
    }

    private void clearStyles()
    {
      int i = Log.d("EditStyledText.EditorManager", "--- onClearStyles");
      Editable localEditable = this.mEST.getText();
      clearStyles(localEditable);
      EditStyledText localEditStyledText = this.mEST;
      Drawable localDrawable = this.mEST.mDefaultBackground;
      localEditStyledText.setBackgroundDrawable(localDrawable);
      this.mBackgroundColor = 16777215;
      onRefreshZeoWidthChar();
    }

    private void clearStyles(CharSequence paramCharSequence)
    {
      int i = 0;
      int j = Log.d("EditStyledText", "--- onClearStyles");
      int k = paramCharSequence.length();
      if (!(paramCharSequence instanceof Editable))
        return;
      paramCharSequence = (Editable)paramCharSequence;
      Object[] arrayOfObject = paramCharSequence.getSpans(0, k, Object.class);
      int m = arrayOfObject.length;
      while (true)
      {
        if (i >= m)
          return;
        Object localObject = arrayOfObject[i];
        if (((localObject instanceof ParagraphStyle)) || ((localObject instanceof QuoteSpan)) || (((localObject instanceof CharacterStyle)) && (!(localObject instanceof UnderlineSpan))))
        {
          if (((localObject instanceof ImageSpan)) || ((localObject instanceof EditStyledText.EditStyledTextSpans.HorizontalLineSpan)))
          {
            int n = paramCharSequence.getSpanStart(localObject);
            int i1 = paramCharSequence.getSpanEnd(localObject);
            Editable localEditable = paramCharSequence.replace(n, i1, "");
          }
          paramCharSequence.removeSpan(localObject);
        }
        i += 1;
      }
    }

    private void copyToClipBoard()
    {
      int i = getSelectionStart();
      int j = getSelectionEnd();
      int k = Math.min(i, j);
      int m = getSelectionStart();
      int n = getSelectionEnd();
      int i1 = Math.max(m, n);
      SpannableStringBuilder localSpannableStringBuilder1 = (SpannableStringBuilder)this.mEST.getText().subSequence(k, i1);
      this.mCopyBuffer = localSpannableStringBuilder1;
      SpannableStringBuilder localSpannableStringBuilder2 = this.mCopyBuffer;
      SpannableStringBuilder localSpannableStringBuilder3 = removeImageChar(localSpannableStringBuilder2);
      ((ClipboardManager)EditStyledText.this.getContext().getSystemService("clipboard")).setText(localSpannableStringBuilder3);
      dumpSpannableString(localSpannableStringBuilder3);
      SpannableStringBuilder localSpannableStringBuilder4 = this.mCopyBuffer;
      dumpSpannableString(localSpannableStringBuilder4);
    }

    private void cutToClipBoard()
    {
      copyToClipBoard();
      int i = getSelectionStart();
      int j = getSelectionEnd();
      int k = Math.min(i, j);
      int m = getSelectionStart();
      int n = getSelectionEnd();
      int i1 = Math.max(m, n);
      Editable localEditable = this.mEST.getText().delete(k, i1);
    }

    private void dumpSpannableString(CharSequence paramCharSequence)
    {
      int i = 0;
      if (!(paramCharSequence instanceof Spannable))
        return;
      paramCharSequence = (Spannable)paramCharSequence;
      int j = paramCharSequence.length();
      String str1 = "--- dumpSpannableString, txt:" + paramCharSequence + ", len:" + j;
      int k = Log.d("EditStyledText", str1);
      Object[] arrayOfObject = paramCharSequence.getSpans(0, j, Object.class);
      int m = arrayOfObject.length;
      while (true)
      {
        if (i >= m)
          return;
        Object localObject = arrayOfObject[i];
        StringBuilder localStringBuilder1 = new StringBuilder().append("--- dumpSpannableString, class:").append(localObject).append(",");
        int n = paramCharSequence.getSpanStart(localObject);
        StringBuilder localStringBuilder2 = localStringBuilder1.append(n).append(",");
        int i1 = paramCharSequence.getSpanEnd(localObject);
        StringBuilder localStringBuilder3 = localStringBuilder2.append(i1).append(",");
        int i2 = paramCharSequence.getSpanFlags(localObject);
        String str2 = i2;
        int i3 = Log.d("EditStyledText", str2);
        i += 1;
      }
    }

    private void endEdit()
    {
      int i = Log.d("EditStyledText.EditorManager", "--- handleCancel");
      this.mMode = 0;
      this.mState = 0;
      this.mEditFlag = false;
      this.mColorWaitInput = 16777215;
      this.mSizeWaitInput = 0;
      this.mWaitInputFlag = false;
      this.mSoftKeyBlockFlag = false;
      this.mKeepNonLineSpan = false;
      this.mTextIsFinishedFlag = false;
      unsetSelect();
      this.mEST.setOnClickListener(null);
      unblockSoftKey();
    }

    private int findLineEnd(Editable paramEditable, int paramInt)
    {
      int i = paramInt;
      while (true)
      {
        int j = paramEditable.length();
        if (i < j)
        {
          if (paramEditable.charAt(i) == '\n')
            i += 1;
        }
        else
        {
          StringBuilder localStringBuilder = new StringBuilder().append("--- findLineEnd:").append(paramInt).append(",");
          int k = paramEditable.length();
          String str = k + "," + i;
          int m = Log.d("EditStyledText.EditorManager", str);
          return i;
        }
        i += 1;
      }
    }

    private int findLineStart(Editable paramEditable, int paramInt)
    {
      int i = paramInt;
      while (true)
      {
        if (i > 0)
        {
          int j = i + -1;
          if (paramEditable.charAt(j) != '\n');
        }
        else
        {
          StringBuilder localStringBuilder = new StringBuilder().append("--- findLineStart:").append(paramInt).append(",");
          int k = paramEditable.length();
          String str = k + "," + i;
          int m = Log.d("EditStyledText.EditorManager", str);
          return i;
        }
        i += -1;
      }
    }

    private void fixSelectionAndDoNextAction()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("--- handleComplete:");
      int i = this.mCurStart;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(i).append(",");
      int j = this.mCurEnd;
      String str1 = j;
      int k = Log.d("EditStyledText.EditorManager", str1);
      if (!this.mEditFlag)
        return;
      int m = this.mCurStart;
      int n = this.mCurEnd;
      if (m != n)
      {
        StringBuilder localStringBuilder3 = new StringBuilder().append("--- cancel handle complete:");
        int i1 = this.mCurStart;
        String str2 = i1;
        int i2 = Log.d("EditStyledText.EditorManager", str2);
        resetEdit();
        return;
      }
      if (this.mState == 2)
        this.mState = 3;
      EditStyledText.EditModeActions localEditModeActions = this.mActions;
      int i3 = this.mMode;
      boolean bool = localEditModeActions.doNext(i3);
      EditStyledText localEditStyledText = this.mEST;
      Editable localEditable = this.mEST.getText();
      EditStyledText.stopSelecting(localEditStyledText, localEditable);
    }

    private void handleSelectAll()
    {
      if (!this.mEditFlag)
        return;
      this.mActions.onAction(11);
    }

    private void insertHorizontalLine()
    {
      int i = Log.d("EditStyledText.EditorManager", "--- onInsertHorizontalLine:");
      int j = this.mEST.getSelectionStart();
      if (j > 0)
      {
        Editable localEditable1 = this.mEST.getText();
        int k = j + -1;
        if (localEditable1.charAt(k) != '\n')
        {
          Editable localEditable2 = this.mEST.getText();
          int m = j + 1;
          Editable localEditable3 = localEditable2.insert(j, "\n");
          j = m;
        }
      }
      int n = this.mEST.getWidth();
      Editable localEditable4 = this.mEST.getText();
      EditStyledText.EditStyledTextSpans.HorizontalLineSpan localHorizontalLineSpan = new EditStyledText.EditStyledTextSpans.HorizontalLineSpan(-16777216, n, localEditable4);
      int i1 = j + 1;
      insertImageSpan(localHorizontalLineSpan, j);
      Editable localEditable5 = this.mEST.getText();
      int i2 = i1 + 1;
      Editable localEditable6 = localEditable5.insert(i1, "\n");
      this.mEST.setSelection(i2);
      EditStyledText localEditStyledText = this.mEST;
      int i3 = this.mMode;
      int i4 = this.mState;
      localEditStyledText.notifyStateChanged(i3, i4);
    }

    private void insertImageFromResId(int paramInt)
    {
      Context localContext = this.mEST.getContext();
      int i = this.mEST.getMaxImageWidthDip();
      EditStyledText.EditStyledTextSpans.RescalableImageSpan localRescalableImageSpan = new EditStyledText.EditStyledTextSpans.RescalableImageSpan(localContext, paramInt, i);
      int j = this.mEST.getSelectionStart();
      insertImageSpan(localRescalableImageSpan, j);
    }

    private void insertImageFromUri(Uri paramUri)
    {
      Context localContext = this.mEST.getContext();
      int i = this.mEST.getMaxImageWidthPx();
      EditStyledText.EditStyledTextSpans.RescalableImageSpan localRescalableImageSpan = new EditStyledText.EditStyledTextSpans.RescalableImageSpan(localContext, paramUri, i);
      int j = this.mEST.getSelectionStart();
      insertImageSpan(localRescalableImageSpan, j);
    }

    private void insertImageSpan(DynamicDrawableSpan paramDynamicDrawableSpan, int paramInt)
    {
      int i = Log.d("EditStyledText.EditorManager", "--- insertImageSpan:");
      if ((paramDynamicDrawableSpan != null) && (paramDynamicDrawableSpan.getDrawable() != null))
      {
        Editable localEditable1 = this.mEST.getText().insert(paramInt, "￼");
        Editable localEditable2 = this.mEST.getText();
        int j = paramInt + 1;
        localEditable2.setSpan(paramDynamicDrawableSpan, paramInt, j, 33);
        EditStyledText localEditStyledText = this.mEST;
        int k = this.mMode;
        int m = this.mState;
        localEditStyledText.notifyStateChanged(k, m);
        return;
      }
      int n = Log.e("EditStyledText.EditorManager", "--- insertImageSpan: null span was inserted");
      this.mEST.sendHintMessage(5);
    }

    private boolean isClipBoardChanged(CharSequence paramCharSequence)
    {
      boolean bool = true;
      String str1 = "--- isClipBoardChanged:" + paramCharSequence;
      int i = Log.d("EditStyledText", str1);
      if (this.mCopyBuffer == null);
      while (true)
      {
        return bool;
        int j = paramCharSequence.length();
        SpannableStringBuilder localSpannableStringBuilder1 = this.mCopyBuffer;
        SpannableStringBuilder localSpannableStringBuilder2 = removeImageChar(localSpannableStringBuilder1);
        String str2 = "--- clipBoard:" + j + "," + localSpannableStringBuilder2 + paramCharSequence;
        int k = Log.d("EditStyledText", str2);
        int m = localSpannableStringBuilder2.length();
        if (j != m)
        {
          int n = 0;
          while (true)
          {
            if (n >= j)
              break label172;
            int i1 = paramCharSequence.charAt(n);
            int i2 = localSpannableStringBuilder2.charAt(n);
            if (i1 == i2)
              break;
            n += 1;
          }
          label172: bool = false;
        }
      }
    }

    private boolean isTextSelected()
    {
      if ((this.mState == 2) || (this.mState == 3));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    private boolean isWaitingNextAction()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("--- waitingNext:");
      int i = this.mCurStart;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(i).append(",");
      int j = this.mCurEnd;
      StringBuilder localStringBuilder3 = localStringBuilder2.append(j).append(",");
      int k = this.mState;
      String str = k;
      int m = Log.d("EditStyledText.EditorManager", str);
      int n = this.mCurStart;
      int i1 = this.mCurEnd;
      if ((n != i1) && (this.mState == 3))
        waitSelection();
      for (boolean bool = true; ; bool = false)
      {
        return bool;
        resumeSelection();
      }
    }

    private void pasteFromClipboard()
    {
      int i = this.mEST.getSelectionStart();
      int j = this.mEST.getSelectionEnd();
      int k = Math.min(i, j);
      int m = this.mEST.getSelectionStart();
      int n = this.mEST.getSelectionEnd();
      int i1 = Math.max(m, n);
      Selection.setSelection(this.mEST.getText(), i1);
      ClipboardManager localClipboardManager = (ClipboardManager)EditStyledText.this.getContext().getSystemService("clipboard");
      this.mKeepNonLineSpan = true;
      Editable localEditable1 = this.mEST.getText();
      CharSequence localCharSequence1 = localClipboardManager.getText();
      Editable localEditable2 = localEditable1.replace(k, i1, localCharSequence1);
      CharSequence localCharSequence2 = localClipboardManager.getText();
      if (isClipBoardChanged(localCharSequence2))
        return;
      int i2 = Log.d("EditStyledText", "--- handlePaste: startPasteImage");
      SpannableStringBuilder localSpannableStringBuilder = this.mCopyBuffer;
      int i3 = this.mCopyBuffer.length();
      DynamicDrawableSpan[] arrayOfDynamicDrawableSpan = (DynamicDrawableSpan[])localSpannableStringBuilder.getSpans(0, i3, DynamicDrawableSpan.class);
      int i4 = arrayOfDynamicDrawableSpan.length;
      int i5 = 0;
      if (i5 >= i4)
        return;
      DynamicDrawableSpan localDynamicDrawableSpan = arrayOfDynamicDrawableSpan[i5];
      int i6 = this.mCopyBuffer.getSpanStart(localDynamicDrawableSpan);
      if ((localDynamicDrawableSpan instanceof EditStyledText.EditStyledTextSpans.HorizontalLineSpan))
      {
        int i7 = this.mEST.getWidth();
        Editable localEditable3 = this.mEST.getText();
        EditStyledText.EditStyledTextSpans.HorizontalLineSpan localHorizontalLineSpan = new EditStyledText.EditStyledTextSpans.HorizontalLineSpan(-16777216, i7, localEditable3);
        int i8 = i6 + k;
        insertImageSpan(localHorizontalLineSpan, i8);
      }
      while (true)
      {
        i5 += 1;
        break;
        if ((localDynamicDrawableSpan instanceof EditStyledText.EditStyledTextSpans.RescalableImageSpan))
        {
          Context localContext = this.mEST.getContext();
          Uri localUri = ((EditStyledText.EditStyledTextSpans.RescalableImageSpan)localDynamicDrawableSpan).getContentUri();
          int i9 = this.mEST.getMaxImageWidthPx();
          EditStyledText.EditStyledTextSpans.RescalableImageSpan localRescalableImageSpan = new EditStyledText.EditStyledTextSpans.RescalableImageSpan(localContext, localUri, i9);
          int i10 = k + i6;
          insertImageSpan(localRescalableImageSpan, i10);
        }
      }
    }

    private SpannableStringBuilder removeImageChar(SpannableStringBuilder paramSpannableStringBuilder)
    {
      int i = 0;
      SpannableStringBuilder localSpannableStringBuilder1 = new SpannableStringBuilder(paramSpannableStringBuilder);
      int j = localSpannableStringBuilder1.length();
      DynamicDrawableSpan[] arrayOfDynamicDrawableSpan = (DynamicDrawableSpan[])localSpannableStringBuilder1.getSpans(i, j, DynamicDrawableSpan.class);
      int k = arrayOfDynamicDrawableSpan.length;
      while (i < k)
      {
        DynamicDrawableSpan localDynamicDrawableSpan = arrayOfDynamicDrawableSpan[i];
        if (((localDynamicDrawableSpan instanceof EditStyledText.EditStyledTextSpans.HorizontalLineSpan)) || ((localDynamicDrawableSpan instanceof EditStyledText.EditStyledTextSpans.RescalableImageSpan)))
        {
          int m = localSpannableStringBuilder1.getSpanStart(localDynamicDrawableSpan);
          int n = localSpannableStringBuilder1.getSpanEnd(localDynamicDrawableSpan);
          SpannableStringBuilder localSpannableStringBuilder2 = localSpannableStringBuilder1.replace(m, n, "");
        }
        i += 1;
      }
      return localSpannableStringBuilder1;
    }

    private void resetEdit()
    {
      endEdit();
      this.mEditFlag = true;
      EditStyledText localEditStyledText = this.mEST;
      int i = this.mMode;
      int j = this.mState;
      localEditStyledText.notifyStateChanged(i, j);
    }

    private void resumeSelection()
    {
      int i = Log.d("EditStyledText.EditorManager", "--- resumeSelection");
      this.mWaitInputFlag = false;
      this.mState = 3;
      EditStyledText localEditStyledText = this.mEST;
      Editable localEditable = this.mEST.getText();
      EditStyledText.stopSelecting(localEditStyledText, localEditable);
    }

    private void selectAll()
    {
      Selection.selectAll(this.mEST.getText());
      int i = this.mEST.getSelectionStart();
      this.mCurStart = i;
      int j = this.mEST.getSelectionEnd();
      this.mCurEnd = j;
      this.mMode = 5;
      this.mState = 3;
    }

    private void setEditMode(int paramInt)
    {
      this.mMode = paramInt;
    }

    private void setInternalSelection(int paramInt1, int paramInt2)
    {
      this.mCurStart = paramInt1;
      this.mCurEnd = paramInt2;
    }

    private void setLineStyledTextSpan(Object paramObject)
    {
      int i = this.mCurStart;
      int j = this.mCurEnd;
      int k = Math.min(i, j);
      int m = this.mCurStart;
      int n = this.mCurEnd;
      int i1 = Math.max(m, n);
      int i2 = this.mEST.getSelectionStart();
      Editable localEditable1 = this.mEST.getText();
      int i3 = findLineStart(localEditable1, k);
      Editable localEditable2 = this.mEST.getText();
      int i4 = findLineEnd(localEditable2, i1);
      if (i3 != i4)
      {
        Editable localEditable3 = this.mEST.getText().insert(i4, "\n");
        int i5 = i4 + 1;
        setStyledTextSpan(paramObject, i3, i5);
      }
      while (true)
      {
        Selection.setSelection(this.mEST.getText(), i2);
        return;
        setStyledTextSpan(paramObject, i3, i4);
      }
    }

    private void setSelectEndPos()
    {
      int i = this.mEST.getSelectionEnd();
      int j = this.mCurStart;
      if (i != j)
      {
        int k = this.mEST.getSelectionStart();
        setEndPos(k);
        return;
      }
      int m = this.mEST.getSelectionEnd();
      setEndPos(m);
    }

    private void setSelectStartPos()
    {
      int i = Log.d("EditStyledText.EditorManager", "--- setSelectStartPos");
      int j = this.mEST.getSelectionStart();
      this.mCurStart = j;
      this.mState = 1;
    }

    private void setSelectState(int paramInt)
    {
      this.mState = paramInt;
    }

    private void setSelection()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("--- onSelect:");
      int i = this.mCurStart;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(i).append(",");
      int j = this.mCurEnd;
      String str1 = j;
      int k = Log.d("EditStyledText.EditorManager", str1);
      if (this.mCurStart >= 0)
      {
        int m = this.mCurStart;
        int n = this.mEST.getText().length();
        if ((m <= n) && (this.mCurEnd >= 0))
        {
          int i1 = this.mCurEnd;
          int i2 = this.mEST.getText().length();
          if (i1 <= i2)
          {
            int i3 = this.mCurStart;
            int i4 = this.mCurEnd;
            if (i3 < i4)
            {
              EditStyledText localEditStyledText1 = this.mEST;
              int i5 = this.mCurStart;
              int i6 = this.mCurEnd;
              localEditStyledText1.setSelection(i5, i6);
              this.mState = 2;
              return;
            }
            int i7 = this.mCurStart;
            int i8 = this.mCurEnd;
            if (i7 > i8)
            {
              EditStyledText localEditStyledText2 = this.mEST;
              int i9 = this.mCurEnd;
              int i10 = this.mCurStart;
              localEditStyledText2.setSelection(i9, i10);
              this.mState = 2;
              return;
            }
            this.mState = 1;
            return;
          }
        }
      }
      StringBuilder localStringBuilder3 = new StringBuilder().append("Select is on, but cursor positions are illigal.:");
      int i11 = this.mEST.getText().length();
      StringBuilder localStringBuilder4 = localStringBuilder3.append(i11).append(",");
      int i12 = this.mCurStart;
      StringBuilder localStringBuilder5 = localStringBuilder4.append(i12).append(",");
      int i13 = this.mCurEnd;
      String str2 = i13;
      int i14 = Log.e("EditStyledText.EditorManager", str2);
    }

    private void setStyledTextSpan(Object paramObject, int paramInt1, int paramInt2)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("--- setStyledTextSpan:");
      int i = this.mMode;
      String str = i + "," + paramInt1 + "," + paramInt2;
      int j = Log.d("EditStyledText.EditorManager", str);
      int k = Math.min(paramInt1, paramInt2);
      int m = Math.max(paramInt1, paramInt2);
      this.mEST.getText().setSpan(paramObject, k, m, 33);
      Selection.setSelection(this.mEST.getText(), m);
    }

    private void startEdit()
    {
      resetEdit();
      showSoftKey();
    }

    private void unsetSelect()
    {
      int i = Log.d("EditStyledText.EditorManager", "--- offSelect");
      EditStyledText localEditStyledText = this.mEST;
      Editable localEditable = this.mEST.getText();
      EditStyledText.stopSelecting(localEditStyledText, localEditable);
      int j = this.mEST.getSelectionStart();
      this.mEST.setSelection(j, j);
      this.mState = 0;
    }

    private void waitSelection()
    {
      int i = Log.d("EditStyledText.EditorManager", "--- waitSelection");
      this.mWaitInputFlag = true;
      int j = this.mCurStart;
      int k = this.mCurEnd;
      if (j != k);
      for (this.mState = 1; ; this.mState = 2)
      {
        EditStyledText localEditStyledText = this.mEST;
        Editable localEditable = this.mEST.getText();
        EditStyledText.startSelecting(localEditStyledText, localEditable);
        return;
      }
    }

    public void blockSoftKey()
    {
      int i = Log.d("EditStyledText.EditorManager", "--- blockSoftKey:");
      hideSoftKey();
      this.mSoftKeyBlockFlag = true;
    }

    public boolean canPaste()
    {
      if ((this.mCopyBuffer != null) && (this.mCopyBuffer.length() > 0))
      {
        SpannableStringBuilder localSpannableStringBuilder = this.mCopyBuffer;
        if (removeImageChar(localSpannableStringBuilder).length() != 0);
      }
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public int getBackgroundColor()
    {
      return this.mBackgroundColor;
    }

    public int getColorWaitInput()
    {
      return this.mColorWaitInput;
    }

    public int getEditMode()
    {
      return this.mMode;
    }

    public int getSelectState()
    {
      return this.mState;
    }

    public int getSelectionEnd()
    {
      return this.mCurEnd;
    }

    public int getSelectionStart()
    {
      return this.mCurStart;
    }

    public int getSizeWaitInput()
    {
      return this.mSizeWaitInput;
    }

    public void hideSoftKey()
    {
      int i = Log.d("EditStyledText.EditorManager", "--- hidesoftkey");
      if (!this.mEST.isFocused())
        return;
      EditStyledText.SoftKeyReceiver localSoftKeyReceiver1 = this.mSkr;
      int j = Selection.getSelectionStart(this.mEST.getText());
      localSoftKeyReceiver1.mNewStart = j;
      EditStyledText.SoftKeyReceiver localSoftKeyReceiver2 = this.mSkr;
      int k = Selection.getSelectionEnd(this.mEST.getText());
      localSoftKeyReceiver2.mNewEnd = k;
      InputMethodManager localInputMethodManager = (InputMethodManager)this.mEST.getContext().getSystemService("input_method");
      IBinder localIBinder = this.mEST.getWindowToken();
      EditStyledText.SoftKeyReceiver localSoftKeyReceiver3 = this.mSkr;
      boolean bool = localInputMethodManager.hideSoftInputFromWindow(localIBinder, 0, localSoftKeyReceiver3);
    }

    public boolean isEditting()
    {
      return this.mEditFlag;
    }

    public boolean isSoftKeyBlocked()
    {
      return this.mSoftKeyBlockFlag;
    }

    public boolean isStyledText()
    {
      Editable localEditable = this.mEST.getText();
      int i = localEditable.length();
      if ((((ParagraphStyle[])localEditable.getSpans(0, i, ParagraphStyle.class)).length > 0) || (((QuoteSpan[])localEditable.getSpans(0, i, QuoteSpan.class)).length > 0) || (((CharacterStyle[])localEditable.getSpans(0, i, CharacterStyle.class)).length > 0) || (this.mBackgroundColor != 16777215));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean isWaitInput()
    {
      return this.mWaitInputFlag;
    }

    public void onAction(int paramInt)
    {
      onAction(paramInt, true);
    }

    public void onAction(int paramInt, boolean paramBoolean)
    {
      this.mActions.onAction(paramInt);
      if (!paramBoolean)
        return;
      EditStyledText localEditStyledText = this.mEST;
      int i = this.mMode;
      int j = this.mState;
      localEditStyledText.notifyStateChanged(i, j);
    }

    public void onClearStyles()
    {
      this.mActions.onAction(14);
    }

    public void onCursorMoved()
    {
      int i = Log.d("EditStyledText.EditorManager", "--- onClickView");
      if ((this.mState != 1) && (this.mState != 2))
        return;
      this.mActions.onSelectAction();
      EditStyledText localEditStyledText = this.mEST;
      int j = this.mMode;
      int k = this.mState;
      localEditStyledText.notifyStateChanged(j, k);
    }

    public void onFixSelectedItem()
    {
      int i = Log.d("EditStyledText.EditorManager", "--- onFixSelectedItem");
      fixSelectionAndDoNextAction();
      EditStyledText localEditStyledText = this.mEST;
      int j = this.mMode;
      int k = this.mState;
      localEditStyledText.notifyStateChanged(j, k);
    }

    public void onRefreshStyles()
    {
      int i = Log.d("EditStyledText.EditorManager", "--- onRefreshStyles");
      Editable localEditable1 = this.mEST.getText();
      int j = localEditable1.length();
      int k = this.mEST.getWidth();
      EditStyledText.EditStyledTextSpans.HorizontalLineSpan[] arrayOfHorizontalLineSpan = (EditStyledText.EditStyledTextSpans.HorizontalLineSpan[])localEditable1.getSpans(0, j, EditStyledText.EditStyledTextSpans.HorizontalLineSpan.class);
      int m = arrayOfHorizontalLineSpan.length;
      int n = 0;
      while (n < m)
      {
        arrayOfHorizontalLineSpan[n].resetWidth(k);
        n += 1;
      }
      EditStyledText.EditStyledTextSpans.MarqueeSpan[] arrayOfMarqueeSpan = (EditStyledText.EditStyledTextSpans.MarqueeSpan[])localEditable1.getSpans(0, j, EditStyledText.EditStyledTextSpans.MarqueeSpan.class);
      int i1 = arrayOfMarqueeSpan.length;
      int i2 = 0;
      while (i2 < i1)
      {
        EditStyledText.EditStyledTextSpans.MarqueeSpan localMarqueeSpan = arrayOfMarqueeSpan[i2];
        int i3 = this.mEST.getBackgroundColor();
        localMarqueeSpan.resetColor(i3);
        i2 += 1;
      }
      if (arrayOfHorizontalLineSpan.length <= 0)
        return;
      StringBuilder localStringBuilder = new StringBuilder().append("");
      char c = localEditable1.charAt(0);
      String str = c;
      Editable localEditable2 = localEditable1.replace(0, 1, str);
    }

    public void onRefreshZeoWidthChar()
    {
      Editable localEditable1 = this.mEST.getText();
      int i = 0;
      while (true)
      {
        int j = localEditable1.length();
        if (i >= j)
          return;
        if (localEditable1.charAt(i) == '⁠')
        {
          int k = i + 1;
          Editable localEditable2 = localEditable1.replace(i, k, "");
          i += -1;
        }
        i += 1;
      }
    }

    public void onStartSelect(boolean paramBoolean)
    {
      int i = Log.d("EditStyledText.EditorManager", "--- onClickSelect");
      this.mMode = 5;
      if (this.mState == 0)
        this.mActions.onSelectAction();
      while (true)
      {
        if (!paramBoolean)
          return;
        EditStyledText localEditStyledText = this.mEST;
        int j = this.mMode;
        int k = this.mState;
        localEditStyledText.notifyStateChanged(j, k);
        return;
        unsetSelect();
        this.mActions.onSelectAction();
      }
    }

    public void onStartSelectAll(boolean paramBoolean)
    {
      int i = Log.d("EditStyledText.EditorManager", "--- onClickSelectAll");
      handleSelectAll();
      if (!paramBoolean)
        return;
      EditStyledText localEditStyledText = this.mEST;
      int j = this.mMode;
      int k = this.mState;
      localEditStyledText.notifyStateChanged(j, k);
    }

    public void setAlignment(Layout.Alignment paramAlignment)
    {
      if ((this.mState != 2) && (this.mState != 3))
        return;
      changeAlign(paramAlignment);
      resetEdit();
    }

    public void setBackgroundColor(int paramInt)
    {
      this.mBackgroundColor = paramInt;
    }

    public void setEndPos(int paramInt)
    {
      String str = "--- setSelectedEndPos:" + paramInt;
      int i = Log.d("EditStyledText.EditorManager", str);
      this.mCurEnd = paramInt;
      setSelection();
    }

    public void setItemColor(int paramInt, boolean paramBoolean)
    {
      int i = Log.d("EditStyledText.EditorManager", "--- setItemColor");
      if (isWaitingNextAction())
      {
        this.mColorWaitInput = paramInt;
        return;
      }
      if ((this.mState != 2) && (this.mState != 3))
        return;
      if (paramInt != 16777215)
        changeColorSelectedText(paramInt);
      if (!paramBoolean)
        return;
      resetEdit();
    }

    public void setItemSize(int paramInt, boolean paramBoolean)
    {
      int i = Log.d("EditStyledText.EditorManager", "--- setItemSize");
      if (isWaitingNextAction())
      {
        this.mSizeWaitInput = paramInt;
        return;
      }
      if ((this.mState != 2) && (this.mState != 3))
        return;
      if (paramInt > 0)
        changeSizeSelectedText(paramInt);
      if (!paramBoolean)
        return;
      resetEdit();
    }

    public void setMarquee(int paramInt)
    {
      if ((this.mState != 2) && (this.mState != 3))
        return;
      addMarquee(paramInt);
      resetEdit();
    }

    public void setSwing()
    {
      if ((this.mState != 2) && (this.mState != 3))
        return;
      addSwing();
      resetEdit();
    }

    public void setTelop()
    {
      if ((this.mState != 2) && (this.mState != 3))
        return;
      addTelop();
      resetEdit();
    }

    public void setTextComposingMask(int paramInt1, int paramInt2)
    {
      String str1 = "--- setTextComposingMask:" + paramInt1 + "," + paramInt2;
      int i = Log.d("EditStyledText", str1);
      int j = Math.min(paramInt1, paramInt2);
      int k = Math.max(paramInt1, paramInt2);
      if ((isWaitInput()) && (this.mColorWaitInput != 16777215));
      for (int m = this.mColorWaitInput; ; m = this.mEST.getForegroundColor(j))
      {
        int n = this.mEST.getBackgroundColor();
        StringBuilder localStringBuilder1 = new StringBuilder().append("--- fg:");
        String str2 = Integer.toHexString(m);
        StringBuilder localStringBuilder2 = localStringBuilder1.append(str2).append(",bg:");
        String str3 = Integer.toHexString(n);
        StringBuilder localStringBuilder3 = localStringBuilder2.append(str3).append(",");
        boolean bool = isWaitInput();
        StringBuilder localStringBuilder4 = localStringBuilder3.append(bool).append(",").append(",");
        int i1 = this.mMode;
        String str4 = i1;
        int i2 = Log.d("EditStyledText", str4);
        if (m != n)
          return;
        int i3 = (0xFF000000 | n) ^ 0xFFFFFFFF;
        int i4 = 0x80000000 | i3;
        if ((this.mComposingTextMask == null) || (this.mComposingTextMask.getBackgroundColor() != i4))
        {
          BackgroundColorSpan localBackgroundColorSpan1 = new BackgroundColorSpan(i4);
          this.mComposingTextMask = localBackgroundColorSpan1;
        }
        Editable localEditable = this.mEST.getText();
        BackgroundColorSpan localBackgroundColorSpan2 = this.mComposingTextMask;
        localEditable.setSpan(localBackgroundColorSpan2, j, k, 33);
        return;
      }
    }

    public void showSoftKey()
    {
      int i = this.mEST.getSelectionStart();
      int j = this.mEST.getSelectionEnd();
      showSoftKey(i, j);
    }

    public void showSoftKey(int paramInt1, int paramInt2)
    {
      int i = Log.d("EditStyledText.EditorManager", "--- showsoftkey");
      if (!this.mEST.isFocused())
        return;
      if (isSoftKeyBlocked())
        return;
      EditStyledText.SoftKeyReceiver localSoftKeyReceiver1 = this.mSkr;
      int j = Selection.getSelectionStart(this.mEST.getText());
      localSoftKeyReceiver1.mNewStart = j;
      EditStyledText.SoftKeyReceiver localSoftKeyReceiver2 = this.mSkr;
      int k = Selection.getSelectionEnd(this.mEST.getText());
      localSoftKeyReceiver2.mNewEnd = k;
      InputMethodManager localInputMethodManager = (InputMethodManager)EditStyledText.this.getContext().getSystemService("input_method");
      EditStyledText localEditStyledText = this.mEST;
      EditStyledText.SoftKeyReceiver localSoftKeyReceiver3 = this.mSkr;
      if (!localInputMethodManager.showSoftInput(localEditStyledText, 0, localSoftKeyReceiver3))
        return;
      if (this.mSkr == null)
        return;
      Selection.setSelection(EditStyledText.this.getText(), paramInt1, paramInt2);
    }

    public void unblockSoftKey()
    {
      int i = Log.d("EditStyledText.EditorManager", "--- unblockSoftKey:");
      this.mSoftKeyBlockFlag = false;
    }

    public void unsetTextComposingMask()
    {
      int i = Log.d("EditStyledText", "--- unsetTextComposingMask");
      if (this.mComposingTextMask == null)
        return;
      Editable localEditable = this.mEST.getText();
      BackgroundColorSpan localBackgroundColorSpan = this.mComposingTextMask;
      localEditable.removeSpan(localBackgroundColorSpan);
      this.mComposingTextMask = null;
    }

    public void updateSpanNextToCursor(Editable paramEditable, int paramInt1, int paramInt2, int paramInt3)
    {
      String str1 = "updateSpanNext:" + paramInt1 + "," + paramInt2 + "," + paramInt3;
      int i = Log.d("EditStyledText.EditorManager", str1);
      int j = paramInt1 + paramInt3;
      int k = Math.min(paramInt1, j);
      int m = Math.max(paramInt1, j);
      Object[] arrayOfObject = paramEditable.getSpans(m, m, Object.class);
      int n = arrayOfObject.length;
      int i1 = 0;
      if (i1 >= n)
        return;
      Object localObject = arrayOfObject[i1];
      int i2;
      int i3;
      Editable localEditable1;
      if (((localObject instanceof EditStyledText.EditStyledTextSpans.MarqueeSpan)) || ((localObject instanceof AlignmentSpan)))
      {
        i2 = paramEditable.getSpanStart(localObject);
        i3 = paramEditable.getSpanEnd(localObject);
        StringBuilder localStringBuilder = new StringBuilder().append("spantype:");
        Class localClass = localObject.getClass();
        String str2 = localClass + "," + i3;
        int i4 = Log.d("EditStyledText.EditorManager", str2);
        if ((!(localObject instanceof EditStyledText.EditStyledTextSpans.MarqueeSpan)) && (!(localObject instanceof AlignmentSpan)))
          break label378;
        localEditable1 = this.mEST.getText();
      }
      label378: for (int i5 = findLineStart(localEditable1, k); ; i5 = k)
      {
        if ((i5 < i2) && (paramInt2 > paramInt3))
          paramEditable.removeSpan(localObject);
        while (true)
        {
          i1 += 1;
          break;
          if (i2 > k)
          {
            paramEditable.setSpan(localObject, k, i3, 33);
            continue;
            if (((localObject instanceof EditStyledText.EditStyledTextSpans.HorizontalLineSpan)) && (paramEditable.getSpanStart(localObject) != j) && (j > 0))
            {
              Editable localEditable2 = this.mEST.getText();
              int i6 = j + -1;
              if (localEditable2.charAt(i6) != '\n')
              {
                Editable localEditable3 = this.mEST.getText().insert(j, "\n");
                this.mEST.setSelection(j);
              }
            }
          }
        }
        return;
      }
    }

    public void updateSpanPreviousFromCursor(Editable paramEditable, int paramInt1, int paramInt2, int paramInt3)
    {
      String str1 = "updateSpanPrevious:" + paramInt1 + "," + paramInt2 + "," + paramInt3;
      int i = Log.d("EditStyledText.EditorManager", str1);
      int j = paramInt1 + paramInt3;
      int k = Math.min(paramInt1, j);
      int m = Math.max(paramInt1, j);
      Object[] arrayOfObject = paramEditable.getSpans(k, k, Object.class);
      int n = arrayOfObject.length;
      int i1 = 0;
      if (i1 >= n)
        return;
      Object localObject = arrayOfObject[i1];
      int i2;
      int i3;
      int i5;
      if (((localObject instanceof ForegroundColorSpan)) || ((localObject instanceof AbsoluteSizeSpan)) || ((localObject instanceof EditStyledText.EditStyledTextSpans.MarqueeSpan)) || ((localObject instanceof AlignmentSpan)))
      {
        i2 = paramEditable.getSpanStart(localObject);
        i3 = paramEditable.getSpanEnd(localObject);
        StringBuilder localStringBuilder = new StringBuilder().append("spantype:");
        Class localClass = localObject.getClass();
        String str2 = localClass + "," + i2;
        int i4 = Log.d("EditStyledText.EditorManager", str2);
        if (((localObject instanceof EditStyledText.EditStyledTextSpans.MarqueeSpan)) || ((localObject instanceof AlignmentSpan)))
        {
          Editable localEditable1 = this.mEST.getText();
          i5 = findLineEnd(localEditable1, m);
        }
      }
      while (true)
      {
        label252: if (i3 < i5)
        {
          int i6 = Log.d("EditStyledText.EditorManager", "updateSpanPrevious: extend span");
          paramEditable.setSpan(localObject, i2, i5, 33);
        }
        while (true)
        {
          i1 += 1;
          break;
          if (!this.mKeepNonLineSpan)
            break label430;
          i5 = i3;
          break label252;
          if ((localObject instanceof EditStyledText.EditStyledTextSpans.HorizontalLineSpan))
          {
            i5 = paramEditable.getSpanStart(localObject);
            i3 = paramEditable.getSpanEnd(localObject);
            if (paramInt2 > paramInt3)
            {
              Editable localEditable2 = paramEditable.replace(i5, i3, "");
              paramEditable.removeSpan(localObject);
            }
            else if (i3 != j)
            {
              int i7 = paramEditable.length();
              if ((j < i7) && (this.mEST.getText().charAt(j) != '\n'))
                Editable localEditable3 = this.mEST.getText().insert(j, "\n");
            }
          }
        }
        return;
        label430: i5 = m;
      }
    }
  }

  public static abstract interface EditStyledTextNotifier
  {
    public abstract void cancelViewManager();

    public abstract boolean isButtonsFocused();

    public abstract void onStateChanged(int paramInt1, int paramInt2);

    public abstract void sendHintMsg(int paramInt);

    public abstract boolean sendOnTouchEvent(MotionEvent paramMotionEvent);

    public abstract boolean showInsertImageSelectAlertDialog();

    public abstract boolean showMenuAlertDialog();

    public abstract boolean showPreview();
  }

  public static abstract interface StyledTextHtmlConverter
  {
    public abstract Spanned fromHtml(String paramString, Html.ImageGetter paramImageGetter, Html.TagHandler paramTagHandler);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.android.ex.editstyledtext.EditStyledText
 * JD-Core Version:    0.6.2
 */