package android.support.v7.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.Callback;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.support.v7.mediarouter.R.attr;
import android.support.v7.mediarouter.R.id;
import android.support.v7.mediarouter.R.layout;
import android.support.v7.mediarouter.R.string;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Comparator;
import java.util.List;

public class MediaRouteChooserDialog extends Dialog
{
  private RouteAdapter mAdapter;
  private boolean mAttachedToWindow;
  private final MediaRouterCallback mCallback;
  private ListView mListView;
  private final MediaRouter mRouter;
  private MediaRouteSelector mSelector;

  public MediaRouteChooserDialog(Context paramContext)
  {
    this(paramContext, 0);
  }

  public MediaRouteChooserDialog(Context paramContext, int paramInt)
  {
    super(localContext, paramInt);
    MediaRouteSelector localMediaRouteSelector = MediaRouteSelector.EMPTY;
    this.mSelector = localMediaRouteSelector;
    MediaRouter localMediaRouter = MediaRouter.getInstance(getContext());
    this.mRouter = localMediaRouter;
    MediaRouterCallback localMediaRouterCallback = new MediaRouterCallback(null);
    this.mCallback = localMediaRouterCallback;
  }

  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mAttachedToWindow = true;
    MediaRouter localMediaRouter = this.mRouter;
    MediaRouteSelector localMediaRouteSelector = this.mSelector;
    MediaRouterCallback localMediaRouterCallback = this.mCallback;
    localMediaRouter.addCallback(localMediaRouteSelector, localMediaRouterCallback, 1);
    refreshRoutes();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    boolean bool = getWindow().requestFeature(3);
    int i = R.layout.mr_media_route_chooser_dialog;
    setContentView(i);
    int j = R.string.mr_media_route_chooser_title;
    setTitle(j);
    Window localWindow = getWindow();
    Context localContext1 = getContext();
    int k = R.attr.mediaRouteOffDrawable;
    int m = MediaRouterThemeHelper.getThemeResource(localContext1, k);
    localWindow.setFeatureDrawableResource(3, m);
    Context localContext2 = getContext();
    RouteAdapter localRouteAdapter1 = new RouteAdapter(localContext2);
    this.mAdapter = localRouteAdapter1;
    int n = R.id.media_route_list;
    ListView localListView1 = (ListView)findViewById(n);
    this.mListView = localListView1;
    ListView localListView2 = this.mListView;
    RouteAdapter localRouteAdapter2 = this.mAdapter;
    localListView2.setAdapter(localRouteAdapter2);
    ListView localListView3 = this.mListView;
    RouteAdapter localRouteAdapter3 = this.mAdapter;
    localListView3.setOnItemClickListener(localRouteAdapter3);
    ListView localListView4 = this.mListView;
    View localView = findViewById(16908292);
    localListView4.setEmptyView(localView);
  }

  public void onDetachedFromWindow()
  {
    this.mAttachedToWindow = false;
    MediaRouter localMediaRouter = this.mRouter;
    MediaRouterCallback localMediaRouterCallback = this.mCallback;
    localMediaRouter.removeCallback(localMediaRouterCallback);
    super.onDetachedFromWindow();
  }

  public boolean onFilterRoute(MediaRouter.RouteInfo paramRouteInfo)
  {
    if (!paramRouteInfo.isDefault())
    {
      MediaRouteSelector localMediaRouteSelector = this.mSelector;
      if (!paramRouteInfo.matchesSelector(localMediaRouteSelector));
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void refreshRoutes()
  {
    if (!this.mAttachedToWindow)
      return;
    this.mAdapter.update();
  }

  public void setRouteSelector(MediaRouteSelector paramMediaRouteSelector)
  {
    if (paramMediaRouteSelector == null)
      throw new IllegalArgumentException("selector must not be null");
    if (this.mSelector.equals(paramMediaRouteSelector))
      return;
    this.mSelector = paramMediaRouteSelector;
    if (this.mAttachedToWindow)
    {
      MediaRouter localMediaRouter1 = this.mRouter;
      MediaRouterCallback localMediaRouterCallback1 = this.mCallback;
      localMediaRouter1.removeCallback(localMediaRouterCallback1);
      MediaRouter localMediaRouter2 = this.mRouter;
      MediaRouterCallback localMediaRouterCallback2 = this.mCallback;
      localMediaRouter2.addCallback(paramMediaRouteSelector, localMediaRouterCallback2, 1);
    }
    refreshRoutes();
  }

  private static final class RouteComparator
    implements Comparator<MediaRouter.RouteInfo>
  {
    public static final RouteComparator sInstance = new RouteComparator();

    public int compare(MediaRouter.RouteInfo paramRouteInfo1, MediaRouter.RouteInfo paramRouteInfo2)
    {
      String str1 = paramRouteInfo1.getName();
      String str2 = paramRouteInfo2.getName();
      return str1.compareTo(str2);
    }
  }

  private final class MediaRouterCallback extends MediaRouter.Callback
  {
    private MediaRouterCallback()
    {
    }

    public void onRouteAdded(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteChooserDialog.this.refreshRoutes();
    }

    public void onRouteChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteChooserDialog.this.refreshRoutes();
    }

    public void onRouteRemoved(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteChooserDialog.this.refreshRoutes();
    }

    public void onRouteSelected(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteChooserDialog.this.dismiss();
    }
  }

  private final class RouteAdapter extends ArrayAdapter<MediaRouter.RouteInfo>
    implements AdapterView.OnItemClickListener
  {
    private final LayoutInflater mInflater;

    public RouteAdapter(Context arg2)
    {
      super(0);
      LayoutInflater localLayoutInflater = LayoutInflater.from(localContext);
      this.mInflater = localLayoutInflater;
    }

    public boolean areAllItemsEnabled()
    {
      return false;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView = paramView;
      if (localView == null)
      {
        LayoutInflater localLayoutInflater = this.mInflater;
        int i = R.layout.mr_media_route_list_item;
        localView = localLayoutInflater.inflate(i, paramViewGroup, false);
      }
      MediaRouter.RouteInfo localRouteInfo = (MediaRouter.RouteInfo)getItem(paramInt);
      TextView localTextView1 = (TextView)localView.findViewById(16908308);
      TextView localTextView2 = (TextView)localView.findViewById(16908309);
      String str1 = localRouteInfo.getName();
      localTextView1.setText(str1);
      String str2 = localRouteInfo.getDescription();
      if (TextUtils.isEmpty(str2))
      {
        localTextView2.setVisibility(8);
        localTextView2.setText("");
      }
      while (true)
      {
        boolean bool = localRouteInfo.isEnabled();
        localView.setEnabled(bool);
        return localView;
        localTextView2.setVisibility(0);
        localTextView2.setText(str2);
      }
    }

    public boolean isEnabled(int paramInt)
    {
      return ((MediaRouter.RouteInfo)getItem(paramInt)).isEnabled();
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      MediaRouter.RouteInfo localRouteInfo = (MediaRouter.RouteInfo)getItem(paramInt);
      if (!localRouteInfo.isEnabled())
        return;
      localRouteInfo.select();
      MediaRouteChooserDialog.this.dismiss();
    }

    public void update()
    {
      clear();
      List localList = MediaRouteChooserDialog.this.mRouter.getRoutes();
      int i = localList.size();
      int j = 0;
      while (j < i)
      {
        MediaRouter.RouteInfo localRouteInfo = (MediaRouter.RouteInfo)localList.get(j);
        if (MediaRouteChooserDialog.this.onFilterRoute(localRouteInfo))
          add(localRouteInfo);
        j += 1;
      }
      MediaRouteChooserDialog.RouteComparator localRouteComparator = MediaRouteChooserDialog.RouteComparator.sInstance;
      sort(localRouteComparator);
      notifyDataSetChanged();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.MediaRouteChooserDialog
 * JD-Core Version:    0.6.2
 */