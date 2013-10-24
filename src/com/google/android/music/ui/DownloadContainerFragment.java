package com.google.android.music.ui;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.music.AsyncAlbumArtImageView;
import com.google.android.music.KeepOnView;
import com.google.android.music.medialist.AlbumSongList;
import com.google.android.music.medialist.AutoPlaylist;
import com.google.android.music.medialist.PlaylistSongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.ViewUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class DownloadContainerFragment extends BaseListFragment
  implements LoaderManager.LoaderCallbacks<Cursor>
{
  private static final String[] QUERY_PROJECTION = arrayOfString;
  private DownloadListAdapter mAdapter = null;
  private ViewGroup mButtonContainer;
  private TextView mDownloadButton;
  private ImageView mHeaderIcon;
  private int mIdIdx;
  private List<ListItemEntry> mListBackingData;
  private int mListTypeIdx;
  private final LinkedHashSet<ListItemEntry> mProcessedData;
  private int mSubTitleIdx;
  private int mTitleIdx;
  private float mUnavailableCardOpacity;

  static
  {
    String[] arrayOfString = new String[5];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "_title";
    arrayOfString[2] = "_subtitle";
    arrayOfString[3] = "_type";
    arrayOfString[4] = "DateAdded";
  }

  public DownloadContainerFragment()
  {
    LinkedHashSet localLinkedHashSet = new LinkedHashSet();
    this.mProcessedData = localLinkedHashSet;
    ArrayList localArrayList = new ArrayList();
    this.mListBackingData = localArrayList;
  }

  private void addManageDownloadButton()
  {
    if (showMenuInActionBar())
      return;
    LayoutInflater localLayoutInflater = getActivity().getLayoutInflater();
    ListView localListView = getListView();
    ViewGroup localViewGroup1 = (ViewGroup)localLayoutInflater.inflate(2130968626, localListView, false);
    this.mButtonContainer = localViewGroup1;
    ViewGroup localViewGroup2 = this.mButtonContainer;
    localListView.addHeaderView(localViewGroup2, null, false);
    localListView.setHeaderDividersEnabled(false);
    ViewGroup localViewGroup3 = (ViewGroup)this.mButtonContainer.findViewById(2131296408);
    TextView localTextView = (TextView)this.mButtonContainer.findViewById(2131296409);
    this.mDownloadButton = localTextView;
    ImageView localImageView = (ImageView)this.mButtonContainer.findViewById(2131296410);
    this.mHeaderIcon = localImageView;
    this.mHeaderIcon.setVisibility(0);
    onDownloadButtonClickListener localonDownloadButtonClickListener = new onDownloadButtonClickListener();
    localViewGroup3.setOnClickListener(localonDownloadButtonClickListener);
    updateHeaderButton();
    if (!MusicUtils.isLandscape(getActivity()))
      return;
    int i = ViewUtils.setWidthToShortestEdge(getActivity(), localViewGroup3);
  }

  private void applyDifferences(Pair<List<ListItemEntry>, List<ListItemEntry>> paramPair)
  {
    final List localList1 = (List)paramPair.first;
    List localList2 = (List)paramPair.second;
    Iterator localIterator = localList1.iterator();
    while (localIterator.hasNext())
      ((ListItemEntry)localIterator.next()).setRemoving(true);
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        LinkedHashSet localLinkedHashSet1 = DownloadContainerFragment.this.mProcessedData;
        List localList1 = localList1;
        boolean bool = localLinkedHashSet1.removeAll(localList1);
        DownloadContainerFragment localDownloadContainerFragment = DownloadContainerFragment.this;
        LinkedHashSet localLinkedHashSet2 = DownloadContainerFragment.this.mProcessedData;
        ArrayList localArrayList = new ArrayList(localLinkedHashSet2);
        List localList2 = DownloadContainerFragment.access$302(localDownloadContainerFragment, localArrayList);
        DownloadContainerFragment.this.mAdapter.notifyDataSetChanged();
        DownloadContainerFragment.this.updateButtonAndMenuVisibility();
      }
    };
    FragmentActivity localFragmentActivity = getActivity();
    Handler localHandler = MusicUtils.runDelayedOnUiThread(local1, localFragmentActivity, 3000);
    boolean bool = this.mProcessedData.addAll(localList2);
    if ((!localList1.isEmpty()) && (localList2.isEmpty()))
      this.mAdapter.notifyDataSetInvalidated();
    while (true)
    {
      LinkedHashSet localLinkedHashSet = this.mProcessedData;
      ArrayList localArrayList = new ArrayList(localLinkedHashSet);
      this.mListBackingData = localArrayList;
      return;
      if (!localList2.isEmpty())
        this.mAdapter.notifyDataSetChanged();
    }
  }

  private Pair<List<ListItemEntry>, List<ListItemEntry>> calculateDifferences(LinkedHashSet<ListItemEntry> paramLinkedHashSet)
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    LinkedHashSet localLinkedHashSet1 = new LinkedHashSet();
    LinkedHashSet localLinkedHashSet2 = this.mProcessedData;
    boolean bool1 = localLinkedHashSet1.addAll(localLinkedHashSet2);
    boolean bool2 = localLinkedHashSet1.removeAll(paramLinkedHashSet);
    boolean bool3 = localArrayList1.addAll(localLinkedHashSet1);
    localLinkedHashSet1.clear();
    boolean bool4 = localLinkedHashSet1.addAll(paramLinkedHashSet);
    LinkedHashSet localLinkedHashSet3 = this.mProcessedData;
    boolean bool5 = localLinkedHashSet1.removeAll(localLinkedHashSet3);
    boolean bool6 = localArrayList2.addAll(localLinkedHashSet1);
    return new Pair(localArrayList1, localArrayList2);
  }

  private LinkedHashSet<ListItemEntry> loadFromCursor(Cursor paramCursor)
  {
    int i = paramCursor.getColumnIndex("_id");
    this.mIdIdx = i;
    int j = paramCursor.getColumnIndex("_title");
    this.mTitleIdx = j;
    int k = paramCursor.getColumnIndex("_subtitle");
    this.mSubTitleIdx = k;
    int m = paramCursor.getColumnIndex("_type");
    this.mListTypeIdx = m;
    LinkedHashSet localLinkedHashSet = new LinkedHashSet();
    boolean bool1 = paramCursor.moveToPosition(-1);
    while (paramCursor.moveToNext())
    {
      int n = this.mListTypeIdx;
      ListItemEntry localListItemEntry1;
      if (paramCursor.getString(n) == null)
      {
        int i1 = this.mIdIdx;
        long l1 = paramCursor.getLong(i1);
        int i2 = this.mTitleIdx;
        String str1 = paramCursor.getString(i2);
        int i3 = this.mSubTitleIdx;
        String str2 = paramCursor.getString(i3);
        ContainerType localContainerType1 = ContainerType.ALBUM;
        localListItemEntry1 = new ListItemEntry(l1, -2147483648, str1, str2, localContainerType1);
        boolean bool2 = localLinkedHashSet.add(localListItemEntry1);
      }
      else
      {
        int i4 = this.mListTypeIdx;
        int i5 = paramCursor.getInt(i4);
        int i6 = this.mIdIdx;
        long l2 = paramCursor.getLong(i6);
        int i7 = this.mTitleIdx;
        String str3 = paramCursor.getString(i7);
        if ((i5 == 100) || (l2 == 65535L) || (l2 == 65532L) || (l2 == 65533L));
        for (ContainerType localContainerType2 = ContainerType.AUTO_PLAYLIST; ; localContainerType2 = ContainerType.USER_PLAYLIST)
        {
          String str4 = getActivity().getResources().getString(2131231396);
          localListItemEntry1 = new com/google/android/music/ui/DownloadContainerFragment$ListItemEntry;
          ListItemEntry localListItemEntry2 = localListItemEntry1;
          int i8 = i5;
          localListItemEntry2.<init>(l2, i8, str3, str4, localContainerType2);
          break;
        }
      }
    }
    return localLinkedHashSet;
  }

  private boolean showMenuInActionBar()
  {
    return UIStateManager.getInstance().getPrefs().isTabletMusicExperience();
  }

  private void updateButtonAndMenuVisibility()
  {
    boolean bool;
    if ((this.mAdapter == null) || (this.mAdapter.getCount() == 0))
    {
      bool = true;
      setEmptyScreenVisible(bool);
      if (this.mButtonContainer != null)
      {
        if ((this.mAdapter != null) && (this.mAdapter.getCount() != 0))
          break label86;
        this.mButtonContainer.setVisibility(8);
      }
    }
    while (true)
    {
      if (!showMenuInActionBar())
        return;
      if (getActivity() == null)
        return;
      getActivity().supportInvalidateOptionsMenu();
      return;
      bool = false;
      break;
      label86: this.mButtonContainer.setVisibility(0);
    }
  }

  private void updateHeaderButton()
  {
    if (getPreferences().isKeepOnDownloadingPaused())
    {
      this.mDownloadButton.setText(2131231394);
      this.mHeaderIcon.setImageResource(2130837748);
      return;
    }
    this.mDownloadButton.setText(2131231392);
    this.mHeaderIcon.setImageResource(2130837726);
  }

  private void updateList(Cursor paramCursor)
  {
    LinkedHashSet localLinkedHashSet = loadFromCursor(paramCursor);
    Pair localPair = calculateDifferences(localLinkedHashSet);
    applyDifferences(localPair);
    this.mAdapter.notifyDataSetChanged();
  }

  protected void initEmptyScreen()
  {
    super.initEmptyScreen();
    setEmptyScreenText(2131230836);
    setEmptyImageView(2130837708);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setHasOptionsMenu(true);
    float f = getResources().getInteger(2131492875) / 100.0F;
    this.mUnavailableCardOpacity = f;
  }

  public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    BaseActivity localBaseActivity = getBaseActivity();
    String[] arrayOfString = QUERY_PROJECTION;
    return new KeepOnDownloadCursorLoader(localBaseActivity, arrayOfString);
  }

  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    if (this.mAdapter == null)
      return;
    if (!showMenuInActionBar())
      return;
    if (getBaseActivity().isSideDrawerOpen())
      return;
    paramMenuInflater.inflate(2131820544, paramMenu);
    if (getPreferences().isKeepOnDownloadingPaused())
    {
      MenuItem localMenuItem1 = paramMenu.findItem(2131296581).setTitle(2131231395);
      MenuItem localMenuItem2 = paramMenu.findItem(2131296581).setIcon(2130837747);
    }
    while (true)
    {
      if (this.mAdapter.getCount() != 0)
        return;
      MenuItem localMenuItem3 = paramMenu.findItem(2131296581).setVisible(false);
      return;
      MenuItem localMenuItem4 = paramMenu.findItem(2131296581).setTitle(2131231393);
      MenuItem localMenuItem5 = paramMenu.findItem(2131296581).setIcon(2130837725);
    }
  }

  public void onDestroyView()
  {
    setListAdapter(null);
    super.onDestroyView();
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    if (getActivity().isFinishing())
      return;
    updateList(paramCursor);
    updateButtonAndMenuVisibility();
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
    this.mListBackingData.clear();
    this.mProcessedData.clear();
    this.mAdapter.notifyDataSetChanged();
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    boolean bool1 = true;
    switch (paramMenuItem.getItemId())
    {
    default:
      bool1 = super.onOptionsItemSelected(paramMenuItem);
      return bool1;
    case 2131296581:
    }
    boolean bool2 = getPreferences().isKeepOnDownloadingPaused();
    MusicPreferences localMusicPreferences = getPreferences();
    if (!bool2);
    for (boolean bool3 = true; ; bool3 = false)
    {
      localMusicPreferences.setKeepOnDownloadPaused(bool3);
      getBaseActivity().supportInvalidateOptionsMenu();
      break;
    }
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    ListView localListView = getListView();
    localListView.setCacheColorHint(0);
    localListView.setHeaderDividersEnabled(false);
    addManageDownloadButton();
    DownloadListAdapter localDownloadListAdapter1 = new DownloadListAdapter(null);
    this.mAdapter = localDownloadListAdapter1;
    DownloadListAdapter localDownloadListAdapter2 = this.mAdapter;
    setListAdapter(localDownloadListAdapter2);
    Loader localLoader = getLoaderManager().initLoader(0, null, this);
  }

  private class DownloadListAdapter extends BaseAdapter
  {
    private DownloadListAdapter()
    {
    }

    private View newView(ViewGroup paramViewGroup)
    {
      ViewGroup localViewGroup = (ViewGroup)DownloadContainerFragment.this.getLayoutInflater(null).inflate(2130968612, paramViewGroup, false);
      DownloadContainerFragment.ViewHolder localViewHolder = new DownloadContainerFragment.ViewHolder(null);
      AsyncAlbumArtImageView localAsyncAlbumArtImageView = (AsyncAlbumArtImageView)localViewGroup.findViewById(2131296325);
      localViewHolder.art = localAsyncAlbumArtImageView;
      TextView localTextView1 = (TextView)localViewGroup.findViewById(2131296347);
      localViewHolder.title = localTextView1;
      TextView localTextView2 = (TextView)localViewGroup.findViewById(2131296348);
      localViewHolder.subtitle = localTextView2;
      KeepOnView localKeepOnView = (KeepOnView)localViewGroup.findViewById(2131296390);
      localViewHolder.keepOn = localKeepOnView;
      localViewHolder.keepOn.setVisibility(0);
      localViewGroup.setTag(localViewHolder);
      return localViewGroup;
    }

    public int getCount()
    {
      return DownloadContainerFragment.this.mListBackingData.size();
    }

    public DownloadContainerFragment.ListItemEntry getItem(int paramInt)
    {
      return (DownloadContainerFragment.ListItemEntry)DownloadContainerFragment.this.mListBackingData.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return getItem(paramInt).id;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        DownloadListAdapter localDownloadListAdapter = this;
        ViewGroup localViewGroup = paramViewGroup;
        paramView = localDownloadListAdapter.newView(localViewGroup);
      }
      DownloadContainerFragment.ViewHolder localViewHolder = (DownloadContainerFragment.ViewHolder)paramView.getTag();
      DownloadContainerFragment.ListItemEntry localListItemEntry = getItem(paramInt);
      if (localListItemEntry.isRemoving)
      {
        float f = DownloadContainerFragment.this.mUnavailableCardOpacity;
        ViewUtils.setAlpha(paramView, f);
        DownloadContainerFragment.ContainerType localContainerType1 = localListItemEntry.containerType;
        DownloadContainerFragment.ContainerType localContainerType2 = DownloadContainerFragment.ContainerType.ALBUM;
        if (localContainerType1 != localContainerType2)
          break label275;
        long l1 = localListItemEntry.id;
        localViewHolder.id = l1;
        DownloadContainerFragment.ContainerType localContainerType3 = localListItemEntry.containerType;
        localViewHolder.containerType = localContainerType3;
        TextView localTextView1 = localViewHolder.title;
        String str1 = localListItemEntry.title;
        localTextView1.setText(str1);
        TextView localTextView2 = localViewHolder.subtitle;
        String str2 = localListItemEntry.subtitle;
        localTextView2.setText(str2);
        long l2 = localListItemEntry.id;
        String str3 = localListItemEntry.title;
        String str4 = localListItemEntry.subtitle;
        AlbumSongList localAlbumSongList = new AlbumSongList(l2, str3, str4, true);
        AsyncAlbumArtImageView localAsyncAlbumArtImageView1 = localViewHolder.art;
        long l3 = localListItemEntry.id;
        String str5 = localListItemEntry.title;
        String str6 = localListItemEntry.subtitle;
        localAsyncAlbumArtImageView1.setAlbumId(l3, str5, str6);
        localViewHolder.keepOn.setSongList(localAlbumSongList);
      }
      while (true)
      {
        DownloadContainerFragment localDownloadContainerFragment = DownloadContainerFragment.this;
        DownloadContainerFragment.onContainerRowClickListener localonContainerRowClickListener = new DownloadContainerFragment.onContainerRowClickListener(localDownloadContainerFragment);
        paramView.setOnClickListener(localonContainerRowClickListener);
        localViewHolder.keepOn.setOnClick(true);
        return paramView;
        ViewUtils.setAlpha(paramView, 1.0F);
        break;
        label275: long l4 = localListItemEntry.id;
        localViewHolder.id = l4;
        DownloadContainerFragment.ContainerType localContainerType4 = localListItemEntry.containerType;
        localViewHolder.containerType = localContainerType4;
        TextView localTextView3 = localViewHolder.title;
        String str7 = localListItemEntry.title;
        localTextView3.setText(str7);
        TextView localTextView4 = localViewHolder.subtitle;
        String str8 = localListItemEntry.subtitle;
        localTextView4.setText(str8);
        AsyncAlbumArtImageView localAsyncAlbumArtImageView2 = localViewHolder.art;
        long l5 = localListItemEntry.id;
        String str9 = localListItemEntry.title;
        int i = localListItemEntry.listType;
        localAsyncAlbumArtImageView2.setPlaylistAlbumArt(l5, str9, i);
        DownloadContainerFragment.ContainerType localContainerType5 = localViewHolder.containerType;
        DownloadContainerFragment.ContainerType localContainerType6 = DownloadContainerFragment.ContainerType.USER_PLAYLIST;
        if (localContainerType5 == localContainerType6)
        {
          long l6 = localListItemEntry.id;
          String str10 = localListItemEntry.title;
          int j = localListItemEntry.listType;
          PlaylistSongList localPlaylistSongList = new PlaylistSongList(l6, str10, j, null, null, null, null, null, true);
          localViewHolder.keepOn.setSongList(localPlaylistSongList);
        }
        else
        {
          long l7 = localListItemEntry.id;
          MusicPreferences localMusicPreferences = UIStateManager.getInstance().getPrefs();
          AutoPlaylist localAutoPlaylist = AutoPlaylist.getAutoPlaylist(l7, false, localMusicPreferences);
          localViewHolder.keepOn.setSongList(localAutoPlaylist);
        }
      }
    }
  }

  private static class ListItemEntry
  {
    DownloadContainerFragment.ContainerType containerType;
    long id;
    boolean isRemoving;
    int listType;
    String subtitle;
    String title;

    ListItemEntry(long paramLong, int paramInt, String paramString1, String paramString2, DownloadContainerFragment.ContainerType paramContainerType)
    {
      this.id = paramLong;
      this.listType = paramInt;
      this.title = paramString1;
      this.subtitle = paramString2;
      this.containerType = paramContainerType;
      this.isRemoving = false;
    }

    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject);
      ListItemEntry localListItemEntry;
      String str3;
      String str4;
      do
      {
        while (true)
        {
          return bool;
          if (paramObject != null)
          {
            Class localClass1 = getClass();
            Class localClass2 = paramObject.getClass();
            if (localClass1 == localClass2);
          }
          else
          {
            bool = false;
            continue;
          }
          localListItemEntry = (ListItemEntry)paramObject;
          long l1 = this.id;
          long l2 = localListItemEntry.id;
          if (l1 != l2)
          {
            bool = false;
          }
          else
          {
            int i = this.listType;
            int j = localListItemEntry.listType;
            if (i != j)
            {
              bool = false;
            }
            else
            {
              DownloadContainerFragment.ContainerType localContainerType1 = this.containerType;
              DownloadContainerFragment.ContainerType localContainerType2 = localListItemEntry.containerType;
              if (localContainerType1 == localContainerType2)
                break;
              bool = false;
            }
          }
        }
        if (this.subtitle != null)
        {
          str1 = this.subtitle;
          str2 = localListItemEntry.subtitle;
          if (str1.equals(str2));
        }
        else
        {
          while (localListItemEntry.subtitle != null)
          {
            String str1;
            String str2;
            bool = false;
            break;
          }
        }
        if (this.title == null)
          break;
        str3 = this.title;
        str4 = localListItemEntry.title;
      }
      while (str3.equals(str4));
      while (true)
      {
        bool = false;
        break;
        if (localListItemEntry.title == null)
          break;
      }
    }

    public int hashCode()
    {
      int i = 0;
      long l1 = this.id;
      long l2 = this.id >>> 32;
      int j = (int)(l1 ^ l2) * 31;
      int k = this.listType;
      int m = (j + k) * 31;
      int n = this.containerType.ordinal();
      int i1 = (m + n) * 31;
      if (this.title != null);
      for (int i2 = this.title.hashCode(); ; i2 = 0)
      {
        int i3 = (i1 + i2) * 31;
        if (this.subtitle != null)
          i = this.subtitle.hashCode();
        return i3 + i;
      }
    }

    void setRemoving(boolean paramBoolean)
    {
      this.isRemoving = paramBoolean;
    }
  }

  private static class ViewHolder
  {
    AsyncAlbumArtImageView art;
    DownloadContainerFragment.ContainerType containerType;
    long id;
    KeepOnView keepOn;
    TextView subtitle;
    TextView title;
  }

  final class onContainerRowClickListener
    implements View.OnClickListener
  {
    onContainerRowClickListener()
    {
    }

    public void onClick(View paramView)
    {
      DownloadContainerFragment.ViewHolder localViewHolder = (DownloadContainerFragment.ViewHolder)paramView.getTag();
      int[] arrayOfInt = DownloadContainerFragment.2.$SwitchMap$com$google$android$music$ui$DownloadContainerFragment$ContainerType;
      int i = localViewHolder.containerType.ordinal();
      switch (arrayOfInt[i])
      {
      default:
        int j = Log.w("DownloadContainer", "Invalid download type");
        return;
      case 1:
        FragmentActivity localFragmentActivity = DownloadContainerFragment.this.getActivity();
        long l1 = localViewHolder.id;
        TrackContainerActivity.showAlbum(localFragmentActivity, l1, null, false);
        return;
      case 2:
        long l2 = localViewHolder.id;
        String str1 = (String)localViewHolder.title.getText();
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        boolean bool = false;
        PlaylistSongList localPlaylistSongList = new PlaylistSongList(l2, str1, 0, null, str2, str3, str4, str5, bool);
        TrackContainerActivity.showPlaylist(DownloadContainerFragment.this.getActivity(), localPlaylistSongList, null);
        return;
      case 3:
      }
      long l3 = localViewHolder.id;
      MusicPreferences localMusicPreferences = DownloadContainerFragment.this.getPreferences();
      AutoPlaylist localAutoPlaylist = AutoPlaylist.getAutoPlaylist(l3, false, localMusicPreferences);
      TrackContainerActivity.showPlaylist(DownloadContainerFragment.this.getActivity(), localAutoPlaylist, null);
    }
  }

  final class onDownloadButtonClickListener
    implements View.OnClickListener
  {
    onDownloadButtonClickListener()
    {
    }

    public void onClick(View paramView)
    {
      boolean bool1 = DownloadContainerFragment.this.getPreferences().isKeepOnDownloadingPaused();
      MusicPreferences localMusicPreferences = DownloadContainerFragment.this.getPreferences();
      if (!bool1);
      for (boolean bool2 = true; ; bool2 = false)
      {
        localMusicPreferences.setKeepOnDownloadPaused(bool2);
        DownloadContainerFragment.this.updateHeaderButton();
        return;
      }
    }
  }

  private static enum ContainerType
  {
    static
    {
      AUTO_PLAYLIST = new ContainerType("AUTO_PLAYLIST", 2);
      ContainerType[] arrayOfContainerType = new ContainerType[3];
      ContainerType localContainerType1 = ALBUM;
      arrayOfContainerType[0] = localContainerType1;
      ContainerType localContainerType2 = USER_PLAYLIST;
      arrayOfContainerType[1] = localContainerType2;
      ContainerType localContainerType3 = AUTO_PLAYLIST;
      arrayOfContainerType[2] = localContainerType3;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.DownloadContainerFragment
 * JD-Core Version:    0.6.2
 */