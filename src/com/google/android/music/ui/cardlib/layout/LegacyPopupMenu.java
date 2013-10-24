package com.google.android.music.ui.cardlib.layout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.google.android.music.ui.cardlib.utils.Lists;
import com.google.android.music.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;

public class LegacyPopupMenu
  implements DialogInterface.OnDismissListener, PlayPopupMenu
{
  private final boolean mAllowCustomView;
  private final View mAnchor;
  private final Context mContext;
  private DialogInterface.OnDismissListener mOnDismissListener;
  private final List<PopupAction> mPopupActions;
  private PopupListAdapter mPopupListAdapter;
  private PopupSelector mPopupSelector;

  public LegacyPopupMenu(Context paramContext, View paramView, boolean paramBoolean)
  {
    this.mContext = paramContext;
    this.mAnchor = paramView;
    this.mAllowCustomView = paramBoolean;
    ArrayList localArrayList = Lists.newArrayList();
    this.mPopupActions = localArrayList;
  }

  public void addMenuItem(CharSequence paramCharSequence, boolean paramBoolean, View paramView, PlayPopupMenu.OnActionSelectedListener paramOnActionSelectedListener)
  {
    List localList = this.mPopupActions;
    PopupAction localPopupAction = new PopupAction(paramCharSequence, paramBoolean, paramView, paramOnActionSelectedListener);
    boolean bool = localList.add(localPopupAction);
  }

  public void addSpinnerItem()
  {
    View localView = LayoutInflater.from(this.mContext).inflate(2130968628, null, false);
    addMenuItem("", false, localView, null);
  }

  public void clearSpinnerItem()
  {
    Object localObject = this.mPopupActions.remove(0);
  }

  public void dismiss()
  {
    if (this.mPopupSelector == null)
      return;
    this.mPopupSelector.dismiss();
  }

  public void onDismiss(DialogInterface paramDialogInterface)
  {
    this.mPopupSelector = null;
    if (this.mOnDismissListener == null)
      return;
    this.mOnDismissListener.onDismiss(paramDialogInterface);
  }

  public void setOnDismissListener(DialogInterface.OnDismissListener paramOnDismissListener)
  {
    this.mOnDismissListener = paramOnDismissListener;
  }

  public void show()
  {
    if (this.mPopupListAdapter != null)
    {
      this.mPopupListAdapter.notifyDataSetChanged();
      return;
    }
    Context localContext1 = this.mContext;
    boolean bool = this.mAllowCustomView;
    List localList = this.mPopupActions;
    PopupListAdapter localPopupListAdapter1 = new PopupListAdapter(localContext1, bool, localList);
    this.mPopupListAdapter = localPopupListAdapter1;
    Context localContext2 = this.mContext;
    View localView = this.mAnchor;
    PopupListAdapter localPopupListAdapter2 = this.mPopupListAdapter;
    PopupSelector localPopupSelector = new PopupSelector(localContext2, localView, localPopupListAdapter2);
    this.mPopupSelector = localPopupSelector;
    this.mPopupSelector.setOnDismissListener(this);
    this.mPopupSelector.show();
  }

  public static class PopupListAdapter extends ArrayAdapter<LegacyPopupMenu.PopupAction>
  {
    private final boolean mAllowCustomView;

    public PopupListAdapter(Context paramContext, boolean paramBoolean, List<LegacyPopupMenu.PopupAction> paramList)
    {
      super(2130968660, 16908308, paramList);
      this.mAllowCustomView = paramBoolean;
    }

    public boolean areAllItemsEnabled()
    {
      return true;
    }

    public int getItemViewType(int paramInt)
    {
      if (LegacyPopupMenu.PopupAction.access$000((LegacyPopupMenu.PopupAction)getItem(paramInt)) != null);
      for (int i = 1; ; i = 0)
        return i;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      LegacyPopupMenu.PopupAction localPopupAction = (LegacyPopupMenu.PopupAction)getItem(paramInt);
      if ((this.mAllowCustomView) && (LegacyPopupMenu.PopupAction.access$000(localPopupAction) != null))
        if (LegacyPopupMenu.PopupAction.access$200(localPopupAction) == null)
        {
          View localView1 = LayoutInflater.from(paramViewGroup.getContext()).inflate(2130968659, null);
          TextView localTextView = (TextView)localView1.findViewById(16908308);
          String str = localPopupAction.toString();
          localTextView.setText(str);
          ViewGroup localViewGroup = (ViewGroup)localView1.findViewById(16908290);
          ViewUtils.removeViewFromParent(LegacyPopupMenu.PopupAction.access$000(localPopupAction));
          LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -1);
          localLayoutParams.gravity = 21;
          View localView2 = LegacyPopupMenu.PopupAction.access$000(localPopupAction);
          localViewGroup.addView(localView2, localLayoutParams);
          View localView3 = LegacyPopupMenu.PopupAction.access$202(localPopupAction, localView1);
        }
      for (View localView4 = LegacyPopupMenu.PopupAction.access$200(localPopupAction); ; localView4 = super.getView(paramInt, paramView, paramViewGroup))
        return localView4;
    }

    public int getViewTypeCount()
    {
      if (this.mAllowCustomView);
      for (int i = 2; ; i = 1)
        return i;
    }

    public boolean isEnabled(int paramInt)
    {
      return LegacyPopupMenu.PopupAction.access$100((LegacyPopupMenu.PopupAction)getItem(paramInt));
    }

    public void onSelect(int paramInt)
    {
      PlayPopupMenu.OnActionSelectedListener localOnActionSelectedListener = LegacyPopupMenu.PopupAction.access$300((LegacyPopupMenu.PopupAction)getItem(paramInt));
      if (localOnActionSelectedListener == null)
        return;
      localOnActionSelectedListener.onActionSelected();
    }
  }

  private static class PopupAction
  {
    private final PlayPopupMenu.OnActionSelectedListener mActionListener;
    private View mCachedCustomRow;
    private final View mCustomView;
    private final boolean mIsEnabled;
    private final String mTitle;

    public PopupAction(CharSequence paramCharSequence, boolean paramBoolean, View paramView, PlayPopupMenu.OnActionSelectedListener paramOnActionSelectedListener)
    {
      String str = paramCharSequence.toString();
      this.mTitle = str;
      this.mIsEnabled = paramBoolean;
      this.mCustomView = paramView;
      this.mActionListener = paramOnActionSelectedListener;
    }

    public String toString()
    {
      return this.mTitle;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.layout.LegacyPopupMenu
 * JD-Core Version:    0.6.2
 */