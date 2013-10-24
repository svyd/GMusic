package android.support.v4.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class TaskStackBuilder
  implements Iterable<Intent>
{
  private static final TaskStackBuilderImpl IMPL = new TaskStackBuilderImplBase();
  private static final String TAG = "TaskStackBuilder";
  private final ArrayList<Intent> mIntents;
  private final Context mSourceContext;

  static
  {
    if (Build.VERSION.SDK_INT >= 11)
    {
      IMPL = new TaskStackBuilderImplHoneycomb();
      return;
    }
  }

  private TaskStackBuilder(Context paramContext)
  {
    ArrayList localArrayList = new ArrayList();
    this.mIntents = localArrayList;
    this.mSourceContext = paramContext;
  }

  public static TaskStackBuilder create(Context paramContext)
  {
    return new TaskStackBuilder(paramContext);
  }

  public static TaskStackBuilder from(Context paramContext)
  {
    return create(paramContext);
  }

  public TaskStackBuilder addNextIntent(Intent paramIntent)
  {
    boolean bool = this.mIntents.add(paramIntent);
    return this;
  }

  public TaskStackBuilder addNextIntentWithParentStack(Intent paramIntent)
  {
    ComponentName localComponentName = paramIntent.getComponent();
    if (localComponentName == null)
    {
      PackageManager localPackageManager = this.mSourceContext.getPackageManager();
      localComponentName = paramIntent.resolveActivity(localPackageManager);
    }
    if (localComponentName != null)
      TaskStackBuilder localTaskStackBuilder1 = addParentStack(localComponentName);
    TaskStackBuilder localTaskStackBuilder2 = addNextIntent(paramIntent);
    return this;
  }

  public TaskStackBuilder addParentStack(Activity paramActivity)
  {
    Intent localIntent = null;
    if ((paramActivity instanceof SupportParentable))
      localIntent = ((SupportParentable)paramActivity).getSupportParentActivityIntent();
    if (localIntent == null)
      localIntent = NavUtils.getParentActivityIntent(paramActivity);
    if (localIntent != null)
    {
      ComponentName localComponentName = localIntent.getComponent();
      if (localComponentName == null)
      {
        PackageManager localPackageManager = this.mSourceContext.getPackageManager();
        localComponentName = localIntent.resolveActivity(localPackageManager);
      }
      TaskStackBuilder localTaskStackBuilder1 = addParentStack(localComponentName);
      TaskStackBuilder localTaskStackBuilder2 = addNextIntent(localIntent);
    }
    return this;
  }

  public TaskStackBuilder addParentStack(ComponentName paramComponentName)
  {
    int i = this.mIntents.size();
    try
    {
      Intent localIntent;
      for (Object localObject = NavUtils.getParentActivityIntent(this.mSourceContext, paramComponentName); localObject != null; localObject = localIntent)
      {
        this.mIntents.add(i, localObject);
        Context localContext = this.mSourceContext;
        ComponentName localComponentName = ((Intent)localObject).getComponent();
        localIntent = NavUtils.getParentActivityIntent(localContext, localComponentName);
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      int j = Log.e("TaskStackBuilder", "Bad ComponentName while traversing activity parent metadata");
      throw new IllegalArgumentException(localNameNotFoundException);
    }
    return this;
  }

  public TaskStackBuilder addParentStack(Class<?> paramClass)
  {
    Context localContext = this.mSourceContext;
    ComponentName localComponentName = new ComponentName(localContext, paramClass);
    return addParentStack(localComponentName);
  }

  public Intent editIntentAt(int paramInt)
  {
    return (Intent)this.mIntents.get(paramInt);
  }

  public Intent getIntent(int paramInt)
  {
    return editIntentAt(paramInt);
  }

  public int getIntentCount()
  {
    return this.mIntents.size();
  }

  public Intent[] getIntents()
  {
    Intent[] arrayOfIntent = new Intent[this.mIntents.size()];
    if (arrayOfIntent.length == 0)
      return arrayOfIntent;
    Intent localIntent1 = (Intent)this.mIntents.get(0);
    Intent localIntent2 = new Intent(localIntent1).addFlags(268484608);
    arrayOfIntent[0] = localIntent2;
    int i = 1;
    while (true)
    {
      int j = arrayOfIntent.length;
      if (i >= j)
        break;
      Intent localIntent3 = (Intent)this.mIntents.get(i);
      Intent localIntent4 = new Intent(localIntent3);
      arrayOfIntent[i] = localIntent4;
      i += 1;
    }
  }

  public PendingIntent getPendingIntent(int paramInt1, int paramInt2)
  {
    return getPendingIntent(paramInt1, paramInt2, null);
  }

  public PendingIntent getPendingIntent(int paramInt1, int paramInt2, Bundle paramBundle)
  {
    if (this.mIntents.isEmpty())
      throw new IllegalStateException("No intents added to TaskStackBuilder; cannot getPendingIntent");
    ArrayList localArrayList = this.mIntents;
    Intent[] arrayOfIntent1 = new Intent[this.mIntents.size()];
    Intent[] arrayOfIntent2 = (Intent[])localArrayList.toArray(arrayOfIntent1);
    Intent localIntent1 = arrayOfIntent2[0];
    Intent localIntent2 = new Intent(localIntent1).addFlags(268484608);
    arrayOfIntent2[0] = localIntent2;
    TaskStackBuilderImpl localTaskStackBuilderImpl = IMPL;
    Context localContext = this.mSourceContext;
    int i = paramInt1;
    int j = paramInt2;
    Bundle localBundle = paramBundle;
    return localTaskStackBuilderImpl.getPendingIntent(localContext, arrayOfIntent2, i, j, localBundle);
  }

  public Iterator<Intent> iterator()
  {
    return this.mIntents.iterator();
  }

  public void startActivities()
  {
    startActivities(null);
  }

  public void startActivities(Bundle paramBundle)
  {
    if (this.mIntents.isEmpty())
      throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
    ArrayList localArrayList = this.mIntents;
    Intent[] arrayOfIntent1 = new Intent[this.mIntents.size()];
    Intent[] arrayOfIntent2 = (Intent[])localArrayList.toArray(arrayOfIntent1);
    Intent localIntent1 = arrayOfIntent2[0];
    Intent localIntent2 = new Intent(localIntent1).addFlags(268484608);
    arrayOfIntent2[0] = localIntent2;
    if (ContextCompat.startActivities(this.mSourceContext, arrayOfIntent2, paramBundle))
      return;
    int i = arrayOfIntent2.length + -1;
    Intent localIntent3 = arrayOfIntent2[i];
    Intent localIntent4 = new Intent(localIntent3);
    Intent localIntent5 = localIntent4.addFlags(268435456);
    this.mSourceContext.startActivity(localIntent4);
  }

  static class TaskStackBuilderImplJellybean
    implements TaskStackBuilder.TaskStackBuilderImpl
  {
    public PendingIntent getPendingIntent(Context paramContext, Intent[] paramArrayOfIntent, int paramInt1, int paramInt2, Bundle paramBundle)
    {
      Intent localIntent1 = paramArrayOfIntent[0];
      Intent localIntent2 = new Intent(localIntent1).addFlags(268484608);
      paramArrayOfIntent[0] = localIntent2;
      return TaskStackBuilderJellybean.getActivitiesPendingIntent(paramContext, paramInt1, paramArrayOfIntent, paramInt2, paramBundle);
    }
  }

  static class TaskStackBuilderImplHoneycomb
    implements TaskStackBuilder.TaskStackBuilderImpl
  {
    public PendingIntent getPendingIntent(Context paramContext, Intent[] paramArrayOfIntent, int paramInt1, int paramInt2, Bundle paramBundle)
    {
      Intent localIntent1 = paramArrayOfIntent[0];
      Intent localIntent2 = new Intent(localIntent1).addFlags(268484608);
      paramArrayOfIntent[0] = localIntent2;
      return TaskStackBuilderHoneycomb.getActivitiesPendingIntent(paramContext, paramInt1, paramArrayOfIntent, paramInt2);
    }
  }

  static class TaskStackBuilderImplBase
    implements TaskStackBuilder.TaskStackBuilderImpl
  {
    public PendingIntent getPendingIntent(Context paramContext, Intent[] paramArrayOfIntent, int paramInt1, int paramInt2, Bundle paramBundle)
    {
      int i = paramArrayOfIntent.length + -1;
      Intent localIntent1 = paramArrayOfIntent[i];
      Intent localIntent2 = new Intent(localIntent1);
      Intent localIntent3 = localIntent2.addFlags(268435456);
      return PendingIntent.getActivity(paramContext, paramInt1, localIntent2, paramInt2);
    }
  }

  static abstract interface TaskStackBuilderImpl
  {
    public abstract PendingIntent getPendingIntent(Context paramContext, Intent[] paramArrayOfIntent, int paramInt1, int paramInt2, Bundle paramBundle);
  }

  public static abstract interface SupportParentable
  {
    public abstract Intent getSupportParentActivityIntent();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.TaskStackBuilder
 * JD-Core Version:    0.6.2
 */