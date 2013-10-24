package com.google.android.music.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.ui.cardlib.model.DocumentMenuHandler.DocumentContextMenuDelegate;
import com.google.android.music.ui.cardlib.utils.Utils;
import java.util.List;

public class DocumentGridFragment extends GridFragment
{
  protected final PlayCardView.ContextMenuDelegate mCardsContextMenuDelegate;
  private boolean mClearedFromOnStop = false;
  private List<Document> mDocList;
  private boolean mIsEmulatedRadio;
  private Document.Type mType;

  public DocumentGridFragment()
  {
    DocumentMenuHandler.DocumentContextMenuDelegate localDocumentContextMenuDelegate = new DocumentMenuHandler.DocumentContextMenuDelegate(this);
    this.mCardsContextMenuDelegate = localDocumentContextMenuDelegate;
    setRetainInstance(true);
  }

  protected void initEmptyScreen()
  {
  }

  public void initialize(List<Document> paramList, Document.Type paramType, boolean paramBoolean)
  {
    this.mDocList = paramList;
    this.mType = paramType;
    this.mIsEmulatedRadio = paramBoolean;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if ((this.mDocList == null) || (this.mType == null))
    {
      int i = Log.w("DocumentGrid", "Arguments not initialized!");
      getActivity().finish();
      return;
    }
    FragmentActivity localFragmentActivity1 = getActivity();
    List localList = this.mDocList;
    Document.Type localType = this.mType;
    PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
    boolean bool = this.mIsEmulatedRadio;
    DocumentListAdapter localDocumentListAdapter = new DocumentListAdapter(localFragmentActivity1, localList, localType, localContextMenuDelegate, bool);
    int j = getScreenColumns();
    FragmentActivity localFragmentActivity2 = getActivity();
    GridAdapterWrapper localGridAdapterWrapper = new GridAdapterWrapper(localFragmentActivity2, localDocumentListAdapter, j);
    setListAdapter(localGridAdapterWrapper);
  }

  public void onStart()
  {
    super.onStart();
    if (!this.mClearedFromOnStop)
      return;
    this.mClearedFromOnStop = false;
    getListView().invalidateViews();
  }

  public void onStop()
  {
    super.onStop();
    this.mClearedFromOnStop = true;
    Utils.clearPlayCardThumbnails(getListView());
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    ListView localListView = getListView();
    int i = getActivity().getResources().getDimensionPixelSize(2131558445);
    localListView.setPadding(i, i, i, i);
    localListView.setClipToPadding(false);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.DocumentGridFragment
 * JD-Core Version:    0.6.2
 */