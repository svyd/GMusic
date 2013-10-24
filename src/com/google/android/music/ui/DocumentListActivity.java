package com.google.android.music.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import java.util.ArrayList;

public class DocumentListActivity extends BaseActivity
{
  public static final Intent buildStartIntent(Context paramContext, String paramString, ArrayList<Document> paramArrayList, Document.Type paramType, boolean paramBoolean1, boolean paramBoolean2)
  {
    Intent localIntent1 = new Intent(paramContext, DocumentListActivity.class);
    Intent localIntent2 = localIntent1.putExtra("title", paramString);
    Intent localIntent3 = localIntent1.putParcelableArrayListExtra("documentList", paramArrayList);
    int i = paramType.ordinal();
    Intent localIntent4 = localIntent1.putExtra("docType", i);
    Intent localIntent5 = localIntent1.putExtra("isEmulatedRadio", paramBoolean1);
    Intent localIntent6 = localIntent1.putExtra("playAll", paramBoolean2);
    return localIntent1;
  }

  public void finish()
  {
    super.finish();
    overridePendingTransition(17432578, 17432579);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Intent localIntent = getIntent();
    String str = localIntent.getStringExtra("title");
    setActionBarTitle(str);
    if (getContent() != null)
      return;
    int i = localIntent.getIntExtra("docType", -1);
    if (i == -1)
    {
      int j = Log.w("DocumentListActivity", "Document type not specified");
      finish();
      return;
    }
    Document.Type localType = Document.Type.values()[i];
    ArrayList localArrayList = localIntent.getParcelableArrayListExtra("documentList");
    if (localArrayList == null)
    {
      int k = Log.w("DocumentListActivity", "Document list not specified");
      finish();
      return;
    }
    boolean bool1 = localIntent.getBooleanExtra("isEmulatedRadio", false);
    boolean bool2 = localIntent.getBooleanExtra("playAll", false);
    Object localObject;
    if (bool1)
    {
      DocumentGridFragment localDocumentGridFragment1 = new DocumentGridFragment();
      localDocumentGridFragment1.initialize(localArrayList, localType, true);
      localObject = localDocumentGridFragment1;
    }
    while (true)
    {
      replaceContent((Fragment)localObject, false);
      return;
      int[] arrayOfInt = 1.$SwitchMap$com$google$android$music$ui$cardlib$model$Document$Type;
      int m = localType.ordinal();
      switch (arrayOfInt[m])
      {
      default:
        DocumentGridFragment localDocumentGridFragment2 = new DocumentGridFragment();
        localDocumentGridFragment2.initialize(localArrayList, localType, false);
        localObject = localDocumentGridFragment2;
        break;
      case 1:
        DocumentListFragment localDocumentListFragment = new DocumentListFragment();
        localDocumentListFragment.initialize(localArrayList, localType, bool2);
        localObject = localDocumentListFragment;
      }
    }
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    return false;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.DocumentListActivity
 * JD-Core Version:    0.6.2
 */