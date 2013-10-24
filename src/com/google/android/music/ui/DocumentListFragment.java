package com.google.android.music.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.ListView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.ui.cardlib.model.DocumentMenuHandler.DocumentContextMenuDelegate;
import java.util.List;

public class DocumentListFragment extends BaseListFragment
{
  protected final PlayCardView.ContextMenuDelegate mCardsContextMenuDelegate;
  private View.OnClickListener mClickListener;
  private List<Document> mDocList;
  private Document.Type mType;

  public DocumentListFragment()
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
    if (!paramBoolean)
      return;
    List localList = this.mDocList;
    PlayTrackDocumentsClickListener localPlayTrackDocumentsClickListener = new PlayTrackDocumentsClickListener(localList);
    this.mClickListener = localPlayTrackDocumentsClickListener;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if ((this.mDocList == null) || (this.mType == null))
    {
      int i = Log.w("DocumentList", "Arguments not initialized!");
      getActivity().finish();
      return;
    }
    getListView().setDivider(null);
    FragmentActivity localFragmentActivity = getActivity();
    List localList = this.mDocList;
    Document.Type localType = this.mType;
    PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
    View.OnClickListener localOnClickListener = this.mClickListener;
    DocumentListAdapter localDocumentListAdapter = new DocumentListAdapter(localFragmentActivity, localList, localType, localContextMenuDelegate, false, localOnClickListener);
    setListAdapter(localDocumentListAdapter);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.DocumentListFragment
 * JD-Core Version:    0.6.2
 */