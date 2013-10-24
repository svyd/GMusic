package com.google.android.music.ui.cardlib.model;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.music.ui.DocumentListActivity;
import java.util.ArrayList;

public class MoreClusterClickHandler
  implements View.OnClickListener
{
  private final Activity mActivity;
  private final ArrayList<Document> mDocList;
  private final boolean mIsEmulateRadio;
  private final boolean mPlayAll;
  private final String mTitle;
  private final Document.Type mType;

  public MoreClusterClickHandler(Activity paramActivity, String paramString, ArrayList<Document> paramArrayList, Document.Type paramType, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mActivity = paramActivity;
    this.mTitle = paramString;
    this.mDocList = paramArrayList;
    this.mType = paramType;
    this.mIsEmulateRadio = paramBoolean1;
    this.mPlayAll = paramBoolean2;
  }

  public void onClick(View paramView)
  {
    if (this.mDocList == null)
      return;
    if (this.mDocList.isEmpty())
      return;
    Activity localActivity = this.mActivity;
    String str = this.mTitle;
    ArrayList localArrayList = this.mDocList;
    Document.Type localType = this.mType;
    boolean bool1 = this.mIsEmulateRadio;
    boolean bool2 = this.mPlayAll;
    Intent localIntent = DocumentListActivity.buildStartIntent(localActivity, str, localArrayList, localType, bool1, bool2);
    this.mActivity.startActivity(localIntent);
    this.mActivity.overridePendingTransition(2131034134, 2131034135);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.model.MoreClusterClickHandler
 * JD-Core Version:    0.6.2
 */