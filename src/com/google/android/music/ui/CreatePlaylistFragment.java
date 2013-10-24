package com.google.android.music.ui;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.music.medialist.SongList;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;

public class CreatePlaylistFragment extends DialogFragment
{
  private EditText mPlaylistNameEditor;
  final DialogInterface.OnClickListener mPositiveListener;
  private SongList mSongsToAdd;

  public CreatePlaylistFragment()
  {
    DialogInterface.OnClickListener local2 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        final String str = CreatePlaylistFragment.this.mPlaylistNameEditor.getText().toString();
        if ((str != null) && (str.length() > 0))
        {
          final Context localContext = CreatePlaylistFragment.this.getActivity().getApplicationContext();
          MusicUtils.runAsyncWithCallback(new AsyncRunner()
          {
            private long mPlaylistId;
            private String mPlaylistName;
            private int mSongsAddedCount = 0;

            public void backgroundTask()
            {
              String str1 = str;
              this.mPlaylistName = str1;
              CreatePlaylistFragment localCreatePlaylistFragment = CreatePlaylistFragment.this;
              Context localContext1 = localContext;
              String str2 = str;
              long l1 = localCreatePlaylistFragment.idForplaylist(localContext1, str2);
              this.mPlaylistId = l1;
              if (this.mPlaylistId >= 0L);
              while (true)
              {
                if (CreatePlaylistFragment.this.mSongsToAdd == null)
                  return;
                SongList localSongList = CreatePlaylistFragment.this.mSongsToAdd;
                Context localContext2 = localContext;
                long l2 = this.mPlaylistId;
                int i = localSongList.appendToPlaylist(localContext2, l2);
                this.mSongsAddedCount = i;
                return;
                ContentResolver localContentResolver = localContext.getContentResolver();
                String str3 = this.mPlaylistName;
                long l3 = Long.parseLong(MusicContent.Playlists.createPlaylist(localContentResolver, str3).getLastPathSegment());
                this.mPlaylistId = l3;
              }
            }

            public void taskCompleted()
            {
              if (CreatePlaylistFragment.this.mSongsToAdd == null)
              {
                String str1 = localContext.getString(2131230878);
                Object[] arrayOfObject = new Object[1];
                String str2 = this.mPlaylistName;
                arrayOfObject[0] = str2;
                String str3 = String.format(str1, arrayOfObject);
                Toast.makeText(localContext, str3, 0).show();
                return;
              }
              Context localContext = localContext;
              int i = this.mSongsAddedCount;
              String str4 = this.mPlaylistName;
              MusicUtils.showSongsAddedToPlaylistToast(localContext, i, str4);
            }
          });
        }
        CreatePlaylistFragment.this.dismiss();
      }
    };
    this.mPositiveListener = local2;
  }

  public static Bundle createArgs(SongList paramSongList)
  {
    Bundle localBundle = new Bundle();
    if (paramSongList != null)
      localBundle.putParcelable("songList", paramSongList);
    return localBundle;
  }

  private int idForplaylist(Context paramContext, String paramString)
  {
    int i = -1;
    try
    {
      Uri localUri = MusicContent.Playlists.getPlaylistUri(paramString);
      String[] arrayOfString = new String[1];
      arrayOfString[0] = "_id";
      localCursor = MusicUtils.query(paramContext, localUri, arrayOfString, null, null, null);
      if (localCursor != null)
      {
        boolean bool = localCursor.moveToFirst();
        if (!localCursor.isAfterLast())
        {
          int j = localCursor.getInt(0);
          i = j;
        }
      }
      return i;
    }
    finally
    {
      Cursor localCursor;
      if (localCursor != null)
        localCursor.close();
    }
  }

  private void launchMakePlaylistNameTask()
  {
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      private String defaultname = null;

      public void backgroundTask()
      {
        String str = CreatePlaylistFragment.this.makePlaylistName();
        this.defaultname = str;
      }

      public void taskCompleted()
      {
        if (CreatePlaylistFragment.this.mPlaylistNameEditor.getText().length() > 0);
        for (int i = 1; ; i = 0)
        {
          if (i != 0)
            return;
          EditText localEditText = CreatePlaylistFragment.this.mPlaylistNameEditor;
          String str = this.defaultname;
          localEditText.setText(str);
          CreatePlaylistFragment.this.mPlaylistNameEditor.selectAll();
          return;
        }
      }
    });
  }

  // ERROR //
  private String makePlaylistName()
  {
    // Byte code:
    //   0: iconst_1
    //   1: istore_1
    //   2: iconst_1
    //   3: anewarray 63	java/lang/String
    //   6: astore_2
    //   7: aload_2
    //   8: iconst_0
    //   9: ldc 95
    //   11: aastore
    //   12: getstatic 99	com/google/android/music/store/MusicContent$Playlists:CONTENT_URI	Landroid/net/Uri;
    //   15: astore_3
    //   16: aload_0
    //   17: invokevirtual 103	com/google/android/music/ui/CreatePlaylistFragment:getActivity	()Landroid/support/v4/app/FragmentActivity;
    //   20: aload_3
    //   21: aload_2
    //   22: aconst_null
    //   23: aconst_null
    //   24: aconst_null
    //   25: iconst_0
    //   26: iconst_1
    //   27: invokestatic 106	com/google/android/music/utils/MusicUtils:query	(Landroid/content/Context;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;ZZ)Landroid/database/Cursor;
    //   30: astore 4
    //   32: aload 4
    //   34: ifnonnull +9 -> 43
    //   37: aconst_null
    //   38: astore 5
    //   40: aload 5
    //   42: areturn
    //   43: aload_0
    //   44: invokevirtual 103	com/google/android/music/ui/CreatePlaylistFragment:getActivity	()Landroid/support/v4/app/FragmentActivity;
    //   47: invokevirtual 112	android/support/v4/app/FragmentActivity:getResources	()Landroid/content/res/Resources;
    //   50: ldc 113
    //   52: invokevirtual 119	android/content/res/Resources:getString	(I)Ljava/lang/String;
    //   55: astore 6
    //   57: iconst_1
    //   58: anewarray 121	java/lang/Object
    //   61: astore 7
    //   63: iconst_0
    //   64: istore 8
    //   66: iconst_1
    //   67: iconst_1
    //   68: iadd
    //   69: istore 9
    //   71: iconst_1
    //   72: invokestatic 127	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   75: astore 10
    //   77: aload 7
    //   79: iload 8
    //   81: aload 10
    //   83: aastore
    //   84: aload 6
    //   86: aload 7
    //   88: invokestatic 131	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   91: astore 11
    //   93: aload 11
    //   95: astore 5
    //   97: iconst_0
    //   98: istore 12
    //   100: iload 9
    //   102: istore_1
    //   103: iload 12
    //   105: ifne +96 -> 201
    //   108: iconst_1
    //   109: istore 12
    //   111: aload 4
    //   113: invokeinterface 77 1 0
    //   118: istore 13
    //   120: iload_1
    //   121: istore 9
    //   123: aload 4
    //   125: invokeinterface 80 1 0
    //   130: ifne +108 -> 238
    //   133: aload 4
    //   135: iconst_0
    //   136: invokeinterface 132 2 0
    //   141: aload 5
    //   143: invokevirtual 136	java/lang/String:compareToIgnoreCase	(Ljava/lang/String;)I
    //   146: ifne +86 -> 232
    //   149: iconst_1
    //   150: anewarray 121	java/lang/Object
    //   153: astore 7
    //   155: iconst_0
    //   156: istore 8
    //   158: iload 9
    //   160: iconst_1
    //   161: iadd
    //   162: istore_1
    //   163: iload 9
    //   165: invokestatic 127	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   168: astore 14
    //   170: aload 7
    //   172: iload 8
    //   174: aload 14
    //   176: aastore
    //   177: aload 6
    //   179: aload 7
    //   181: invokestatic 131	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   184: astore 15
    //   186: aload 4
    //   188: invokeinterface 139 1 0
    //   193: istore 16
    //   195: iload_1
    //   196: istore 9
    //   198: goto -75 -> 123
    //   201: aload 4
    //   203: invokeinterface 87 1 0
    //   208: goto -168 -> 40
    //   211: astore 17
    //   213: aload 4
    //   215: invokeinterface 87 1 0
    //   220: aload 17
    //   222: athrow
    //   223: astore 17
    //   225: iload 9
    //   227: istore 18
    //   229: goto -16 -> 213
    //   232: iload 9
    //   234: istore_1
    //   235: goto -49 -> 186
    //   238: iload 9
    //   240: istore_1
    //   241: goto -138 -> 103
    //
    // Exception table:
    //   from	to	target	type
    //   43	63	211	finally
    //   111	120	211	finally
    //   163	195	211	finally
    //   71	93	223	finally
    //   123	155	223	finally
  }

  public Dialog onCreateDialog(Bundle paramBundle)
  {
    SongList localSongList = (SongList)getArguments().getParcelable("songList");
    this.mSongsToAdd = localSongList;
    View localView = getActivity().getLayoutInflater().inflate(2130968611, null);
    EditText localEditText = (EditText)localView.findViewById(2131296388);
    this.mPlaylistNameEditor = localEditText;
    FragmentActivity localFragmentActivity = getActivity();
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(localFragmentActivity);
    Resources localResources = getResources();
    if (this.mSongsToAdd == null);
    for (int i = 2131230771; ; i = 2131230772)
    {
      CharSequence localCharSequence1 = localResources.getText(i);
      AlertDialog.Builder localBuilder2 = localBuilder1.setTitle(localCharSequence1);
      CharSequence localCharSequence2 = localResources.getText(17039370);
      DialogInterface.OnClickListener localOnClickListener = this.mPositiveListener;
      AlertDialog.Builder localBuilder3 = localBuilder1.setPositiveButton(localCharSequence2, localOnClickListener);
      CharSequence localCharSequence3 = localResources.getText(17039360);
      AlertDialog.Builder localBuilder4 = localBuilder1.setNegativeButton(localCharSequence3, null);
      AlertDialog.Builder localBuilder5 = localBuilder1.setView(localView);
      launchMakePlaylistNameTask();
      return localBuilder1.create();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.CreatePlaylistFragment
 * JD-Core Version:    0.6.2
 */