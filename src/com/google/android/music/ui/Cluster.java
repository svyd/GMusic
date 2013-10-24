package com.google.android.music.ui;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.ui.cardlib.model.MoreClusterClickHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cluster
{
  private final Activity mActivity;
  private final PlayCardClusterMetadata.CardMetadata mCardType;
  private final ArrayList<Document> mContent;
  private final Document.Type mDocType;
  private final boolean mIsEmulateRadio;
  private final View.OnClickListener mItemClickListener;
  private final String mMoreTitle;
  private final int mNbColumns;
  private final int mNbRows;
  private final String mTitle;

  public Cluster(Activity paramActivity, PlayCardClusterMetadata.CardMetadata paramCardMetadata, String paramString1, String paramString2, ArrayList<Document> paramArrayList, Document.Type paramType, int paramInt1, int paramInt2, View.OnClickListener paramOnClickListener)
  {
  }

  public Cluster(Activity paramActivity, PlayCardClusterMetadata.CardMetadata paramCardMetadata, String paramString1, String paramString2, ArrayList<Document> paramArrayList, Document.Type paramType, int paramInt1, int paramInt2, View.OnClickListener paramOnClickListener, boolean paramBoolean)
  {
    this.mActivity = paramActivity;
    this.mCardType = paramCardMetadata;
    this.mTitle = paramString1;
    this.mMoreTitle = paramString2;
    this.mContent = paramArrayList;
    this.mDocType = paramType;
    this.mNbColumns = paramInt1;
    this.mNbRows = paramInt2;
    this.mItemClickListener = paramOnClickListener;
    this.mIsEmulateRadio = paramBoolean;
  }

  public PlayCardClusterMetadata.CardMetadata getCardType()
  {
    return this.mCardType;
  }

  public List<Document> getFullContent()
  {
    return Collections.unmodifiableList(this.mContent);
  }

  public View.OnClickListener getItemOnClickListener()
  {
    return this.mItemClickListener;
  }

  public MoreClusterClickHandler getMoreOnClickListener()
  {
    boolean bool1;
    MoreClusterClickHandler localMoreClusterClickHandler;
    Activity localActivity;
    if (this.mItemClickListener != null)
    {
      bool1 = true;
      localMoreClusterClickHandler = new com/google/android/music/ui/cardlib/model/MoreClusterClickHandler;
      localActivity = this.mActivity;
      if (TextUtils.isEmpty(this.mMoreTitle))
        break label73;
    }
    label73: for (String str = this.mMoreTitle; ; str = this.mTitle)
    {
      ArrayList localArrayList = this.mContent;
      Document.Type localType = this.mDocType;
      boolean bool2 = this.mIsEmulateRadio;
      localMoreClusterClickHandler.<init>(localActivity, str, localArrayList, localType, bool2, bool1);
      return localMoreClusterClickHandler;
      bool1 = false;
      break;
    }
  }

  public int getNbColumns()
  {
    return this.mNbColumns;
  }

  public String getTitle()
  {
    return this.mTitle;
  }

  public List<Document> getVisibleContent()
  {
    int i = this.mNbColumns;
    int j = this.mNbRows;
    int k = i * j;
    int m = Math.min(this.mContent.size(), k);
    return Collections.unmodifiableList(this.mContent).subList(0, m);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.Cluster
 * JD-Core Version:    0.6.2
 */