package com.google.android.music.tutorial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.music.download.IntentConstants;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.purchase.Finsky;
import com.google.android.music.ui.AppNavigation;
import com.google.android.music.utils.TypefaceUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class OtherWaysToPlayListAdapter extends ArrayAdapter<OtherWaysToPlayListEntry>
{
  private Context mContext;
  private List<OtherWaysToPlayListEntry> mData;
  private int mLayoutResouceId;
  private View.OnClickListener mOnClickListener;
  private final MusicPreferences mPrefs;
  private final MusicEventLogger mTracker;

  public OtherWaysToPlayListAdapter(Context paramContext, MusicPreferences paramMusicPreferences, int paramInt, ArrayList<OtherWaysToPlayListEntry> paramArrayList)
  {
    super(paramContext, paramInt, paramArrayList);
    View.OnClickListener local1 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        int i = ((Integer)((OtherWaysToPlayListAdapter.OtherWaysToPlayListEntryHolder)paramAnonymousView.getTag()).mIcon.getTag()).intValue();
        if (i == 2130837759)
        {
          String str1 = OtherWaysToPlayListAdapter.this.mContext.getString(2131230832);
          AppNavigation.openHelpLink(OtherWaysToPlayListAdapter.this.mContext, str1);
          MusicEventLogger localMusicEventLogger1 = OtherWaysToPlayListAdapter.this.mTracker;
          Object[] arrayOfObject1 = new Object[0];
          localMusicEventLogger1.trackEvent("signUpGeneralLearnMore", arrayOfObject1);
          return;
        }
        if (i == 2130837767)
        {
          Context localContext1 = OtherWaysToPlayListAdapter.this.mContext;
          MusicPreferences localMusicPreferences = OtherWaysToPlayListAdapter.this.mPrefs;
          if (!Finsky.isPlayStoreAvailable(localContext1, localMusicPreferences))
            return;
          Context localContext2 = OtherWaysToPlayListAdapter.this.mContext;
          Intent localIntent = IntentConstants.getMusicStoreIntent(OtherWaysToPlayListAdapter.this.mPrefs);
          localContext2.startActivity(localIntent);
          MusicEventLogger localMusicEventLogger2 = OtherWaysToPlayListAdapter.this.mTracker;
          Object[] arrayOfObject2 = new Object[0];
          localMusicEventLogger2.trackEvent("signUpShopPlayStore", arrayOfObject2);
          return;
        }
        if (i == 2130837769)
        {
          Context localContext3 = OtherWaysToPlayListAdapter.this.mContext;
          Object[] arrayOfObject3 = new Object[1];
          String str2 = Locale.getDefault().getLanguage().toLowerCase();
          arrayOfObject3[0] = str2;
          String str3 = localContext3.getString(2131230827, arrayOfObject3);
          AppNavigation.openHelpLink(OtherWaysToPlayListAdapter.this.mContext, str3);
          MusicEventLogger localMusicEventLogger3 = OtherWaysToPlayListAdapter.this.mTracker;
          Object[] arrayOfObject4 = new Object[0];
          localMusicEventLogger3.trackEvent("signUpFileTransferLearnMore", arrayOfObject4);
          return;
        }
        MusicEventLogger localMusicEventLogger4 = OtherWaysToPlayListAdapter.this.mTracker;
        Object[] arrayOfObject5 = new Object[0];
        localMusicEventLogger4.trackEvent("emptyScreenSignupForNautilusFree", arrayOfObject5);
        boolean bool = TutorialUtils.launchTutorialOnDemand((Activity)OtherWaysToPlayListAdapter.this.mContext);
      }
    };
    this.mOnClickListener = local1;
    this.mContext = paramContext;
    this.mLayoutResouceId = paramInt;
    this.mData = paramArrayList;
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this.mContext);
    this.mTracker = localMusicEventLogger;
    this.mPrefs = paramMusicPreferences;
  }

  public OtherWaysToPlayListAdapter(Context paramContext, MusicPreferences paramMusicPreferences, int paramInt, OtherWaysToPlayListEntry[] paramArrayOfOtherWaysToPlayListEntry)
  {
    super(paramContext, paramInt, paramArrayOfOtherWaysToPlayListEntry);
    View.OnClickListener local1 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        int i = ((Integer)((OtherWaysToPlayListAdapter.OtherWaysToPlayListEntryHolder)paramAnonymousView.getTag()).mIcon.getTag()).intValue();
        if (i == 2130837759)
        {
          String str1 = OtherWaysToPlayListAdapter.this.mContext.getString(2131230832);
          AppNavigation.openHelpLink(OtherWaysToPlayListAdapter.this.mContext, str1);
          MusicEventLogger localMusicEventLogger1 = OtherWaysToPlayListAdapter.this.mTracker;
          Object[] arrayOfObject1 = new Object[0];
          localMusicEventLogger1.trackEvent("signUpGeneralLearnMore", arrayOfObject1);
          return;
        }
        if (i == 2130837767)
        {
          Context localContext1 = OtherWaysToPlayListAdapter.this.mContext;
          MusicPreferences localMusicPreferences = OtherWaysToPlayListAdapter.this.mPrefs;
          if (!Finsky.isPlayStoreAvailable(localContext1, localMusicPreferences))
            return;
          Context localContext2 = OtherWaysToPlayListAdapter.this.mContext;
          Intent localIntent = IntentConstants.getMusicStoreIntent(OtherWaysToPlayListAdapter.this.mPrefs);
          localContext2.startActivity(localIntent);
          MusicEventLogger localMusicEventLogger2 = OtherWaysToPlayListAdapter.this.mTracker;
          Object[] arrayOfObject2 = new Object[0];
          localMusicEventLogger2.trackEvent("signUpShopPlayStore", arrayOfObject2);
          return;
        }
        if (i == 2130837769)
        {
          Context localContext3 = OtherWaysToPlayListAdapter.this.mContext;
          Object[] arrayOfObject3 = new Object[1];
          String str2 = Locale.getDefault().getLanguage().toLowerCase();
          arrayOfObject3[0] = str2;
          String str3 = localContext3.getString(2131230827, arrayOfObject3);
          AppNavigation.openHelpLink(OtherWaysToPlayListAdapter.this.mContext, str3);
          MusicEventLogger localMusicEventLogger3 = OtherWaysToPlayListAdapter.this.mTracker;
          Object[] arrayOfObject4 = new Object[0];
          localMusicEventLogger3.trackEvent("signUpFileTransferLearnMore", arrayOfObject4);
          return;
        }
        MusicEventLogger localMusicEventLogger4 = OtherWaysToPlayListAdapter.this.mTracker;
        Object[] arrayOfObject5 = new Object[0];
        localMusicEventLogger4.trackEvent("emptyScreenSignupForNautilusFree", arrayOfObject5);
        boolean bool = TutorialUtils.launchTutorialOnDemand((Activity)OtherWaysToPlayListAdapter.this.mContext);
      }
    };
    this.mOnClickListener = local1;
    this.mContext = paramContext;
    this.mLayoutResouceId = paramInt;
    List localList = Arrays.asList(paramArrayOfOtherWaysToPlayListEntry);
    this.mData = localList;
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this.mContext);
    this.mTracker = localMusicEventLogger;
    this.mPrefs = paramMusicPreferences;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = paramView;
    OtherWaysToPlayListEntryHolder localOtherWaysToPlayListEntryHolder;
    if (paramView == null)
    {
      LayoutInflater localLayoutInflater = ((Activity)this.mContext).getLayoutInflater();
      int i = this.mLayoutResouceId;
      localView = localLayoutInflater.inflate(i, paramViewGroup, false);
      localOtherWaysToPlayListEntryHolder = new OtherWaysToPlayListEntryHolder();
      ImageView localImageView1 = (ImageView)localView.findViewById(2131296436);
      localOtherWaysToPlayListEntryHolder.mIcon = localImageView1;
      TextView localTextView1 = (TextView)localView.findViewById(2131296437);
      localOtherWaysToPlayListEntryHolder.mTextView_1 = localTextView1;
      TextView localTextView2 = (TextView)localView.findViewById(2131296438);
      localOtherWaysToPlayListEntryHolder.mTextView_2 = localTextView2;
      TypefaceUtil.setTypeface(localOtherWaysToPlayListEntryHolder.mTextView_1, 1);
      TypefaceUtil.setTypeface(localOtherWaysToPlayListEntryHolder.mTextView_2, 1);
      localView.setTag(localOtherWaysToPlayListEntryHolder);
      OtherWaysToPlayListEntry localOtherWaysToPlayListEntry = (OtherWaysToPlayListEntry)this.mData.get(paramInt);
      ImageView localImageView2 = localOtherWaysToPlayListEntryHolder.mIcon;
      int j = localOtherWaysToPlayListEntry.mIconId;
      localImageView2.setImageResource(j);
      ImageView localImageView3 = localOtherWaysToPlayListEntryHolder.mIcon;
      Integer localInteger = Integer.valueOf(localOtherWaysToPlayListEntry.mIconId);
      localImageView3.setTag(localInteger);
      TextView localTextView3 = localOtherWaysToPlayListEntryHolder.mTextView_1;
      int k = localOtherWaysToPlayListEntry.mFirstTextId;
      localTextView3.setText(k);
      TextView localTextView4 = localOtherWaysToPlayListEntryHolder.mTextView_2;
      int m = localOtherWaysToPlayListEntry.mSecondTextId;
      localTextView4.setText(m);
      if (localOtherWaysToPlayListEntry.mSecondTextId != 2131231021)
        break label301;
      Context localContext = this.mContext;
      MusicPreferences localMusicPreferences = this.mPrefs;
      if (Finsky.isPlayStoreAvailable(localContext, localMusicPreferences))
        break label301;
      localOtherWaysToPlayListEntryHolder.mTextView_2.setVisibility(8);
    }
    while (true)
    {
      View.OnClickListener localOnClickListener = this.mOnClickListener;
      localView.setOnClickListener(localOnClickListener);
      return localView;
      localOtherWaysToPlayListEntryHolder = (OtherWaysToPlayListEntryHolder)localView.getTag();
      break;
      label301: localOtherWaysToPlayListEntryHolder.mTextView_2.setVisibility(0);
    }
  }

  public class OtherWaysToPlayListEntryHolder
  {
    ImageView mIcon;
    TextView mTextView_1;
    TextView mTextView_2;

    public OtherWaysToPlayListEntryHolder()
    {
    }
  }

  public static class OtherWaysToPlayListEntry
  {
    int mFirstTextId;
    int mIconId;
    int mSecondTextId;

    public OtherWaysToPlayListEntry(int paramInt1, int paramInt2, int paramInt3)
    {
      this.mIconId = paramInt1;
      this.mFirstTextId = paramInt2;
      this.mSecondTextId = paramInt3;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.tutorial.OtherWaysToPlayListAdapter
 * JD-Core Version:    0.6.2
 */