package com.google.android.music.tutorial;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.music.cloudclient.OffersResponseJson;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.sync.google.MusicAuthInfo;
import com.google.android.music.utils.TypefaceUtil;
import java.io.IOException;

public class TutorialSelectAccountActivity extends TutorialListActivity
  implements View.OnClickListener, GetAccountOffersTask.Callbacks
{
  private Account[] mAccounts;
  private AccountsAdapter mAdapter;
  private Button mAddAccountButton;
  private AccountManagerFuture<Bundle> mAuthTask;
  private View mContainer;
  private String mCreatedAccountName;
  private String mCreatedAccountType;
  private ProgressBar mGetOfferSpinner;
  private GetAccountOffersTask mGetSelectedAccountOffers;
  private ListView mList;
  private Button mNotNowButton;
  private Account mSelectedAccount;

  private void addAccountClick()
  {
    MusicEventLogger localMusicEventLogger = this.mTracker;
    Object[] arrayOfObject = new Object[0];
    localMusicEventLogger.trackEvent("signUpAddAccount", arrayOfObject);
    Bundle localBundle = new Bundle();
    String str = getString(2131230995);
    localBundle.putCharSequence("introMessage", str);
    localBundle.putBoolean("allowSkip", true);
    AccountManager localAccountManager = AccountManager.get(this);
    AccountManagerCallback local1 = new AccountManagerCallback()
    {
      // ERROR //
      public void run(AccountManagerFuture<Bundle> paramAnonymousAccountManagerFuture)
      {
        // Byte code:
        //   0: aload_1
        //   1: invokeinterface 36 1 0
        //   6: ifeq +4 -> 10
        //   9: return
        //   10: aload_1
        //   11: invokeinterface 40 1 0
        //   16: checkcast 42	android/os/Bundle
        //   19: astore_2
        //   20: aload_2
        //   21: ldc 44
        //   23: invokevirtual 48	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
        //   26: astore_3
        //   27: aload_2
        //   28: ldc 50
        //   30: invokevirtual 48	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
        //   33: astore 4
        //   35: aload_2
        //   36: ldc 52
        //   38: invokevirtual 56	android/os/Bundle:getBoolean	(Ljava/lang/String;)Z
        //   41: ifne +37 -> 78
        //   44: aload_3
        //   45: ifnull +33 -> 78
        //   48: aload 4
        //   50: ifnull +28 -> 78
        //   53: aload_0
        //   54: getfield 20	com/google/android/music/tutorial/TutorialSelectAccountActivity$1:this$0	Lcom/google/android/music/tutorial/TutorialSelectAccountActivity;
        //   57: aload_3
        //   58: invokestatic 60	com/google/android/music/tutorial/TutorialSelectAccountActivity:access$002	(Lcom/google/android/music/tutorial/TutorialSelectAccountActivity;Ljava/lang/String;)Ljava/lang/String;
        //   61: astore 5
        //   63: aload_0
        //   64: getfield 20	com/google/android/music/tutorial/TutorialSelectAccountActivity$1:this$0	Lcom/google/android/music/tutorial/TutorialSelectAccountActivity;
        //   67: aload 4
        //   69: invokestatic 63	com/google/android/music/tutorial/TutorialSelectAccountActivity:access$102	(Lcom/google/android/music/tutorial/TutorialSelectAccountActivity;Ljava/lang/String;)Ljava/lang/String;
        //   72: astore 6
        //   74: return
        //   75: astore 7
        //   77: return
        //   78: aload_0
        //   79: getfield 20	com/google/android/music/tutorial/TutorialSelectAccountActivity$1:this$0	Lcom/google/android/music/tutorial/TutorialSelectAccountActivity;
        //   82: aconst_null
        //   83: invokestatic 60	com/google/android/music/tutorial/TutorialSelectAccountActivity:access$002	(Lcom/google/android/music/tutorial/TutorialSelectAccountActivity;Ljava/lang/String;)Ljava/lang/String;
        //   86: astore 8
        //   88: aload_0
        //   89: getfield 20	com/google/android/music/tutorial/TutorialSelectAccountActivity$1:this$0	Lcom/google/android/music/tutorial/TutorialSelectAccountActivity;
        //   92: aconst_null
        //   93: invokestatic 63	com/google/android/music/tutorial/TutorialSelectAccountActivity:access$102	(Lcom/google/android/music/tutorial/TutorialSelectAccountActivity;Ljava/lang/String;)Ljava/lang/String;
        //   96: astore 9
        //   98: return
        //   99: astore 10
        //   101: return
        //   102: athrow
        //   103: astore 11
        //   105: return
        //
        // Exception table:
        //   from	to	target	type
        //   10	75	75	android/accounts/OperationCanceledException
        //   10	98	99	java/io/IOException
        //   10	98	102	finally
        //   10	98	103	android/accounts/AuthenticatorException
      }
    };
    TutorialSelectAccountActivity localTutorialSelectAccountActivity = this;
    Handler localHandler = null;
    AccountManagerFuture localAccountManagerFuture = localAccountManager.addAccount("com.google", "sj", null, localBundle, localTutorialSelectAccountActivity, local1, localHandler);
  }

  private void authenticationFailed(final int paramInt1, final int paramInt2)
  {
    int i = Log.e("MusicAccountSelect", "authenticationFailed");
    MusicEventLogger localMusicEventLogger = this.mTracker;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "signUpErrorDetail";
    String str = getString(paramInt2);
    arrayOfObject[1] = str;
    localMusicEventLogger.trackEvent("signUpAuthenticationError", arrayOfObject);
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        TutorialSelectAccountActivity localTutorialSelectAccountActivity1 = TutorialSelectAccountActivity.this;
        AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(localTutorialSelectAccountActivity1);
        int i = paramInt1;
        AlertDialog.Builder localBuilder2 = localBuilder1.setTitle(i);
        TutorialSelectAccountActivity localTutorialSelectAccountActivity2 = TutorialSelectAccountActivity.this;
        int j = paramInt2;
        String str = localTutorialSelectAccountActivity2.getString(j);
        AlertDialog localAlertDialog = localBuilder2.setMessage(str).setPositiveButton(2131230741, null).show();
      }
    };
    runOnUiThread(local2);
  }

  private void authenticationSuccess(Account paramAccount)
  {
    cancelGetOffers();
    GetAccountOffersTask localGetAccountOffersTask = new GetAccountOffersTask(this, paramAccount, this);
    this.mGetSelectedAccountOffers = localGetAccountOffersTask;
    this.mGetSelectedAccountOffers.run();
    updateSpinner(true, false);
  }

  private void cancelAuthTask()
  {
    if (this.mAuthTask == null)
      return;
    boolean bool = this.mAuthTask.cancel(false);
    this.mAuthTask = null;
  }

  private void cancelGetOffers()
  {
    if (this.mGetSelectedAccountOffers == null)
      return;
    this.mGetSelectedAccountOffers.cancel();
    this.mGetSelectedAccountOffers = null;
  }

  private boolean isFirstTimeTutorial()
  {
    return getIntent().getBooleanExtra("firstTimeTutorial", false);
  }

  private void onAccountsChanged()
  {
    this.mAdapter.clear();
    if (this.mAccounts == null)
      return;
    if (this.mAccounts.length == 0)
      return;
    if (this.mAccounts.length == 1)
    {
      Account localAccount1 = this.mAccounts[0];
      this.mSelectedAccount = localAccount1;
    }
    Account[] arrayOfAccount = this.mAccounts;
    int i = arrayOfAccount.length;
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      Account localAccount2 = arrayOfAccount[j];
      this.mAdapter.add(localAccount2);
      j += 1;
    }
  }

  private void refreshAccounts()
  {
    Account[] arrayOfAccount1 = GetAccountOffersTask.getAvailableAccounts(this);
    this.mAccounts = arrayOfAccount1;
    Account localAccount1 = getPrefs().getSyncAccount();
    this.mSelectedAccount = localAccount1;
    if (this.mAdapter == null)
      return;
    onAccountsChanged();
    if (this.mCreatedAccountName == null)
      return;
    if (this.mCreatedAccountType == null)
      return;
    if (this.mAccounts == null)
      return;
    Account[] arrayOfAccount2 = this.mAccounts;
    int i = arrayOfAccount2.length;
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      Account localAccount2 = arrayOfAccount2[j];
      String str1 = this.mCreatedAccountName;
      String str2 = localAccount2.name;
      if (str1.equals(str2))
      {
        String str3 = this.mCreatedAccountType;
        String str4 = localAccount2.type;
        if (str3.equals(str4))
        {
          this.mSelectedAccount = localAccount2;
          this.mAdapter.notifyDataSetChanged();
          selectAccount();
        }
      }
      j += 1;
    }
  }

  private void selectAccount()
  {
    cancelGetOffers();
    cancelAuthTask();
    MusicAuthInfo localMusicAuthInfo = new MusicAuthInfo(this);
    try
    {
      Account localAccount1 = this.mSelectedAccount;
      Account localAccount2 = this.mSelectedAccount;
      GetAuthTokenCallback localGetAuthTokenCallback = new GetAuthTokenCallback(localAccount2);
      Handler localHandler = new Handler();
      AccountManagerFuture localAccountManagerFuture = localMusicAuthInfo.getAuthTokenForeground(this, localAccount1, localGetAuthTokenCallback, localHandler);
      this.mAuthTask = localAccountManagerFuture;
      return;
    }
    catch (AuthenticatorException localAuthenticatorException)
    {
      String str = "getAuthToken failed " + localAuthenticatorException;
      int i = Log.d("MusicAccountSelect", str);
    }
  }

  private void updateSpinner(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean1)
    {
      this.mContainer.setVisibility(8);
      this.mGetOfferSpinner.setVisibility(0);
      return;
    }
    if (paramBoolean2)
      return;
    this.mContainer.setVisibility(0);
    this.mGetOfferSpinner.setVisibility(8);
  }

  String getScreenNameLogExtra()
  {
    return "signUpSelectAccount";
  }

  public void onAccountDisabled(Account paramAccount)
  {
    updateSpinner(false, true);
    setResult(-1);
    getPrefs().setInvalidStreamingAccount(paramAccount);
    MusicPreferences localMusicPreferences = getPrefs();
    TutorialUtils.finishTutorialPermanently(this, false, localMusicPreferences);
  }

  public void onAccountInvalid(Account paramAccount)
  {
    updateSpinner(false, true);
    setResult(-1);
    getPrefs().setInvalidStreamingAccount(paramAccount);
    MusicPreferences localMusicPreferences = getPrefs();
    TutorialUtils.finishTutorialPermanently(this, false, localMusicPreferences);
  }

  public void onAccountOffersError(Account paramAccount)
  {
    updateSpinner(false, false);
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this).setTitle(2131230732).setMessage(2131230733);
    DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        TutorialUtils.finishTutorialTemporarily(TutorialSelectAccountActivity.this);
      }
    };
    AlertDialog localAlertDialog = localBuilder.setPositiveButton(2131230741, local3).show();
  }

  public void onAccountOffersSuccess(Account paramAccount, OffersResponseJson paramOffersResponseJson)
  {
    updateSpinner(false, true);
    setResult(-1);
    getPrefs().setStreamingAccount(paramAccount);
    if (getIntent().getBooleanExtra("changeAccountOnly", false))
    {
      TutorialUtils.finishTutorialTemporarily(this);
      return;
    }
    MusicPreferences localMusicPreferences = getPrefs();
    TutorialUtils.openTryNautilusOrFinishTutorial(paramOffersResponseJson, this, localMusicPreferences);
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default:
      return;
    case 2131296562:
      addAccountClick();
      return;
    case 2131296556:
    }
    MusicEventLogger localMusicEventLogger = this.mTracker;
    Object[] arrayOfObject = new Object[0];
    localMusicEventLogger.trackEvent("signUpSkipSelectAccount", arrayOfObject);
    setResult(0);
    MusicPreferences localMusicPreferences = getPrefs();
    TutorialUtils.finishTutorialPermanently(this, false, localMusicPreferences);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968707);
    TypefaceUtil.setTypeface((TextView)findViewById(2131296434), 2);
    TypefaceUtil.setTypeface((TextView)findViewById(2131296554), 1);
    ListView localListView1 = getListView();
    this.mList = localListView1;
    AccountsAdapter localAccountsAdapter1 = new AccountsAdapter(this);
    this.mAdapter = localAccountsAdapter1;
    ListView localListView2 = this.mList;
    AccountsAdapter localAccountsAdapter2 = this.mAdapter;
    localListView2.setAdapter(localAccountsAdapter2);
    this.mList.setCacheColorHint(0);
    View localView = findViewById(2131296561);
    this.mContainer = localView;
    ProgressBar localProgressBar = (ProgressBar)findViewById(2131296563);
    this.mGetOfferSpinner = localProgressBar;
    Button localButton1 = (Button)findViewById(2131296562);
    this.mAddAccountButton = localButton1;
    this.mAddAccountButton.setOnClickListener(this);
    this.mAddAccountButton.setEnabled(true);
    TypefaceUtil.setTypeface(this.mAddAccountButton, 1);
    Button localButton2 = (Button)findViewById(2131296556);
    this.mNotNowButton = localButton2;
    this.mNotNowButton.setOnClickListener(this);
    this.mNotNowButton.setEnabled(true);
    TypefaceUtil.setTypeface(this.mNotNowButton, 1);
    getPrefs().disableAutoSelect();
    setResult(0);
  }

  protected void onDestroy()
  {
    cancelGetOffers();
    cancelAuthTask();
    super.onDestroy();
  }

  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    if (paramView.getTag() == null)
      return;
    Account localAccount1 = getPrefs().getSyncAccount();
    Account localAccount2 = ((TutorialSelectAccountActivity.AccountsAdapter.ViewHolder)paramView.getTag()).account;
    this.mSelectedAccount = localAccount2;
    this.mAdapter.notifyDataSetChanged();
    if (this.mSelectedAccount == null)
      return;
    if ((isFirstTimeTutorial()) || (localAccount1 == null) || (this.mSelectedAccount.equals(localAccount1)))
    {
      selectAccount();
      return;
    }
    showAccountSwitchWarning();
  }

  public void onPause()
  {
    super.onPause();
  }

  public void onResume()
  {
    super.onResume();
    refreshAccounts();
  }

  public void showAccountSwitchWarning()
  {
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this);
    AlertDialog.Builder localBuilder2 = localBuilder1.setMessage(2131231217);
    DialogInterface.OnClickListener local4 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        TutorialSelectAccountActivity.this.finish();
      }
    };
    AlertDialog.Builder localBuilder3 = localBuilder2.setNegativeButton(2131230742, local4);
    DialogInterface.OnClickListener local5 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        if (TutorialSelectAccountActivity.this.mSelectedAccount == null)
          return;
        TutorialSelectAccountActivity.this.selectAccount();
      }
    };
    AlertDialog.Builder localBuilder4 = localBuilder1.setPositiveButton(2131231218, local5);
    AlertDialog localAlertDialog = localBuilder1.show();
  }

  private class AccountsAdapter extends ArrayAdapter<Account>
  {
    private final LayoutInflater mInflater;

    public AccountsAdapter(Context arg2)
    {
      super(2130968701, -1);
      LayoutInflater localLayoutInflater = LayoutInflater.from(localContext);
      this.mInflater = localLayoutInflater;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView;
      if (paramView == null)
      {
        localView = this.mInflater.inflate(2130968701, paramViewGroup, false);
        ViewHolder localViewHolder1 = new ViewHolder(localView);
        localView.setTag(localViewHolder1);
      }
      while (true)
      {
        ViewHolder localViewHolder2 = (ViewHolder)localView.getTag();
        Account localAccount1 = (Account)getItem(paramInt);
        Account localAccount2 = ViewHolder.access$402(localViewHolder2, localAccount1);
        TextView localTextView = localViewHolder2.title;
        String str = localAccount1.name;
        localTextView.setText(str);
        TypefaceUtil.setTypeface(localViewHolder2.title, 2);
        return localView;
        localView = paramView;
      }
    }

    private class ViewHolder
    {
      private Account account;
      private TextView title;

      public ViewHolder(View arg2)
      {
        Object localObject;
        TextView localTextView = (TextView)localObject.findViewById(2131296326);
        this.title = localTextView;
      }
    }
  }

  public class GetAuthTokenCallback
    implements AccountManagerCallback<Bundle>
  {
    private Account mAccount;

    public GetAuthTokenCallback(Account arg2)
    {
      Object localObject;
      this.mAccount = localObject;
    }

    public void run(AccountManagerFuture<Bundle> paramAccountManagerFuture)
    {
      if (paramAccountManagerFuture == null)
        return;
      if (paramAccountManagerFuture.isCancelled())
        return;
      if (TutorialSelectAccountActivity.this.isActivityDestroyed())
        return;
      try
      {
        if ((Bundle)paramAccountManagerFuture.getResult() != null)
        {
          TutorialSelectAccountActivity localTutorialSelectAccountActivity = TutorialSelectAccountActivity.this;
          Account localAccount = this.mAccount;
          localTutorialSelectAccountActivity.authenticationSuccess(localAccount);
          return;
        }
        TutorialSelectAccountActivity.this.authenticationFailed(2131230728, 2131230729);
        return;
      }
      catch (OperationCanceledException localOperationCanceledException)
      {
        String str1 = "getResult failed " + localOperationCanceledException;
        int i = Log.d("MusicAccountSelect", str1);
        return;
      }
      catch (AuthenticatorException localAuthenticatorException)
      {
        String str2 = "getResult failed " + localAuthenticatorException;
        int j = Log.d("MusicAccountSelect", str2);
        TutorialSelectAccountActivity.this.authenticationFailed(2131230728, 2131230729);
        return;
      }
      catch (IOException localIOException)
      {
        String str3 = "getResult failed " + localIOException;
        int k = Log.d("MusicAccountSelect", str3);
        TutorialSelectAccountActivity.this.authenticationFailed(2131230726, 2131230727);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.tutorial.TutorialSelectAccountActivity
 * JD-Core Version:    0.6.2
 */