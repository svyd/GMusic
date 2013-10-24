package com.google.android.music.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.music.download.cache.CacheUtils;
import com.google.android.music.download.cache.StorageLocation;
import com.google.android.music.download.cache.StorageProbeUtils;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SDCardSelectorFragment extends DialogFragment
  implements AdapterView.OnItemClickListener
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private StorageLocation mCurrentLocation;
  private BaseAdapter mListAdapter;
  private ListView mListView;
  private List<StorageLocation> mLocationList;
  private String[] mUnitStrings;

  public SDCardSelectorFragment()
  {
    BaseAdapter local2 = new BaseAdapter()
    {
      private String formatStorageSize(StorageLocation paramAnonymousStorageLocation)
      {
        long l1 = paramAnonymousStorageLocation.mFreeSpace;
        Pair localPair1 = readableFileSize(l1);
        long l2 = paramAnonymousStorageLocation.mTotalSpace;
        Pair localPair2 = readableFileSize(l2);
        SDCardSelectorFragment localSDCardSelectorFragment = SDCardSelectorFragment.this;
        Object[] arrayOfObject = new Object[4];
        Object localObject1 = localPair1.first;
        arrayOfObject[0] = localObject1;
        Object localObject2 = localPair1.second;
        arrayOfObject[1] = localObject2;
        Object localObject3 = localPair2.first;
        arrayOfObject[2] = localObject3;
        Object localObject4 = localPair2.second;
        arrayOfObject[3] = localObject4;
        return localSDCardSelectorFragment.getString(2131231381, arrayOfObject);
      }

      private Pair<String, String> readableFileSize(long paramAnonymousLong)
      {
        if (paramAnonymousLong <= 0L);
        String str1;
        String str2;
        for (Pair localPair = new Pair("0", ""); ; localPair = new Pair(str1, str2))
        {
          return localPair;
          double d1 = Math.log10(paramAnonymousLong);
          double d2 = Math.log10(1024.0D);
          int i = (int)(d1 / d2);
          DecimalFormat localDecimalFormat = new DecimalFormat("#,##0.#");
          double d3 = paramAnonymousLong;
          double d4 = i;
          double d5 = Math.pow(1024.0D, d4);
          double d6 = d3 / d5;
          str1 = localDecimalFormat.format(d6);
          str2 = SDCardSelectorFragment.this.mUnitStrings[i];
        }
      }

      public int getCount()
      {
        if (SDCardSelectorFragment.this.mLocationList != null);
        for (int i = SDCardSelectorFragment.this.mLocationList.size() + 1; ; i = 1)
          return i;
      }

      public Object getItem(int paramAnonymousInt)
      {
        if (paramAnonymousInt == 0);
        List localList;
        int i;
        for (Object localObject = StorageLocation.getInternal(SDCardSelectorFragment.this.getActivity()); ; localObject = localList.get(i))
        {
          return localObject;
          localList = SDCardSelectorFragment.this.mLocationList;
          i = paramAnonymousInt + -1;
        }
      }

      public long getItemId(int paramAnonymousInt)
      {
        return getItem(paramAnonymousInt).hashCode();
      }

      public View getView(int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
      {
        if (paramAnonymousView == null)
        {
          paramAnonymousView = LayoutInflater.from(SDCardSelectorFragment.this.getActivity()).inflate(2130968681, paramAnonymousViewGroup, false);
          ViewHolder localViewHolder1 = new ViewHolder();
          TextView localTextView1 = (TextView)paramAnonymousView.findViewById(16908308);
          localViewHolder1.mDescriptionView = localTextView1;
          TextView localTextView2 = (TextView)paramAnonymousView.findViewById(16908309);
          localViewHolder1.mSizeView = localTextView2;
          RadioButton localRadioButton1 = (RadioButton)paramAnonymousView.findViewById(2131296330);
          localViewHolder1.mRadioButton = localRadioButton1;
          paramAnonymousView.setTag(localViewHolder1);
        }
        StorageLocation localStorageLocation1 = (StorageLocation)getItem(paramAnonymousInt);
        ViewHolder localViewHolder2 = (ViewHolder)paramAnonymousView.getTag();
        TextView localTextView3 = localViewHolder2.mDescriptionView;
        String str1 = localStorageLocation1.mDescription;
        localTextView3.setText(str1);
        TextView localTextView4 = localViewHolder2.mSizeView;
        String str2 = formatStorageSize(localStorageLocation1);
        localTextView4.setText(str2);
        RadioButton localRadioButton2 = localViewHolder2.mRadioButton;
        StorageLocation localStorageLocation2 = SDCardSelectorFragment.this.mCurrentLocation;
        boolean bool = localStorageLocation1.equals(localStorageLocation2);
        localRadioButton2.setChecked(bool);
        return paramAnonymousView;
      }

      class ViewHolder
      {
        TextView mDescriptionView;
        RadioButton mRadioButton;
        TextView mSizeView;

        ViewHolder()
        {
        }
      }
    };
    this.mListAdapter = local2;
  }

  private boolean checkLocation(StorageLocation paramStorageLocation)
  {
    if ((!paramStorageLocation.mIsEmulated) && (paramStorageLocation.mIsRemovable) && (paramStorageLocation.mFreeSpace >> 20 > 500L));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private void init()
  {
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      List<StorageLocation> mLocations;

      public void backgroundTask()
      {
        ArrayList localArrayList = new ArrayList();
        this.mLocations = localArrayList;
        Iterator localIterator = StorageProbeUtils.getPossibleSDCardLocations(SDCardSelectorFragment.this.getActivity()).iterator();
        while (localIterator.hasNext())
        {
          StorageLocation localStorageLocation = (StorageLocation)localIterator.next();
          if (SDCardSelectorFragment.this.checkLocation(localStorageLocation))
            boolean bool = this.mLocations.add(localStorageLocation);
        }
        List localList = this.mLocations;
        Comparator localComparator = StorageLocation.SIZE_SORT_COMPARATOR;
        Collections.sort(localList, localComparator);
      }

      public void taskCompleted()
      {
        SDCardSelectorFragment localSDCardSelectorFragment = SDCardSelectorFragment.this;
        List localList1 = this.mLocations;
        List localList2 = SDCardSelectorFragment.access$102(localSDCardSelectorFragment, localList1);
        SDCardSelectorFragment.this.updateList();
      }
    });
  }

  private void updateList()
  {
    this.mListAdapter.notifyDataSetChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    init();
    String[] arrayOfString = new String[5];
    String str1 = getString(2131231382);
    arrayOfString[0] = str1;
    String str2 = getString(2131231383);
    arrayOfString[1] = str2;
    String str3 = getString(2131231384);
    arrayOfString[2] = str3;
    String str4 = getString(2131231385);
    arrayOfString[3] = str4;
    String str5 = getString(2131231386);
    arrayOfString[4] = str5;
    this.mUnitStrings = arrayOfString;
  }

  public Dialog onCreateDialog(Bundle paramBundle)
  {
    View localView = LayoutInflater.from(getActivity()).inflate(2130968677, null);
    MusicPreferences localMusicPreferences = UIStateManager.getInstance().getPrefs();
    StorageLocation localStorageLocation1 = StorageProbeUtils.getSecondaryExternalStorageLocation(getActivity(), localMusicPreferences);
    this.mCurrentLocation = localStorageLocation1;
    if (this.mCurrentLocation == null)
    {
      StorageLocation localStorageLocation2 = StorageLocation.getInternal(getActivity());
      this.mCurrentLocation = localStorageLocation2;
    }
    ListView localListView1 = (ListView)localView.findViewById(2131296530);
    this.mListView = localListView1;
    ListView localListView2 = this.mListView;
    BaseAdapter localBaseAdapter = this.mListAdapter;
    localListView2.setAdapter(localBaseAdapter);
    this.mListView.setOnItemClickListener(this);
    FragmentActivity localFragmentActivity = getActivity();
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(localFragmentActivity);
    AlertDialog.Builder localBuilder2 = localBuilder1.setTitle(2131231377);
    AlertDialog.Builder localBuilder3 = localBuilder1.setView(localView);
    if (LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("current secondary external storage is ");
      String str1 = localMusicPreferences.getSecondaryExternalStorageMountPoint();
      String str2 = str1;
      int i = Log.v("SDCardSelectorFragment", str2);
    }
    AlertDialog localAlertDialog = localBuilder1.create();
    localAlertDialog.setCanceledOnTouchOutside(true);
    return localAlertDialog;
  }

  public void onDismiss(DialogInterface paramDialogInterface)
  {
    super.onDismiss(paramDialogInterface);
    FragmentActivity localFragmentActivity = getActivity();
    if (localFragmentActivity == null)
      return;
    localFragmentActivity.finish();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    StorageLocation localStorageLocation = (StorageLocation)this.mListAdapter.getItem(paramInt);
    String str1 = "Storage selected: " + localStorageLocation;
    int i = Log.i("SDCardSelectorFragment", str1);
    MusicPreferences localMusicPreferences = UIStateManager.getInstance().getPrefs();
    if (localStorageLocation.mIsInternal)
    {
      localMusicPreferences.setSecondaryExternalStoragePath(null);
      dismiss();
      return;
    }
    if (CacheUtils.setupSecondaryStorageLocation(getActivity(), localStorageLocation))
    {
      String str2 = localStorageLocation.mMountPoint;
      localMusicPreferences.setSecondaryExternalStoragePath(str2);
      dismiss();
      return;
    }
    Toast.makeText(getActivity(), 2131231380, 1).show();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.SDCardSelectorFragment
 * JD-Core Version:    0.6.2
 */