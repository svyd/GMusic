package android.support.v7.internal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.MenuItemWrapperICS;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SupportMenuInflater extends MenuInflater
{
  private static final Class<?>[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE = ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
  private static final Class<?>[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
  private final Object[] mActionProviderConstructorArguments;
  private final Object[] mActionViewConstructorArguments;
  private Context mContext;
  private Object mRealOwner;

  static
  {
    Class[] arrayOfClass = new Class[1];
    arrayOfClass[0] = Context.class;
    ACTION_VIEW_CONSTRUCTOR_SIGNATURE = arrayOfClass;
  }

  public SupportMenuInflater(Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
    this.mRealOwner = paramContext;
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = paramContext;
    this.mActionViewConstructorArguments = arrayOfObject1;
    Object[] arrayOfObject2 = this.mActionViewConstructorArguments;
    this.mActionProviderConstructorArguments = arrayOfObject2;
  }

  private void parseMenu(XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Menu paramMenu)
    throws XmlPullParserException, IOException
  {
    MenuState localMenuState = new MenuState(paramMenu);
    int i = paramXmlPullParser.getEventType();
    String str1;
    int j;
    label51: int k;
    if (i == 2)
    {
      str1 = paramXmlPullParser.getName();
      if (str1.equals("menu"))
      {
        j = paramXmlPullParser.next();
        k = 0;
        label54: if (k != 0)
          return;
      }
    }
    switch (j)
    {
    default:
    case 2:
    case 3:
      while (true)
      {
        j = paramXmlPullParser.next();
        break label54;
        String str2 = "Expecting menu, got " + str1;
        throw new RuntimeException(str2);
        j = paramXmlPullParser.next();
        if (j != 1)
          break;
        break label51;
        if (0 == 0)
        {
          str1 = paramXmlPullParser.getName();
          if (str1.equals("group"))
          {
            localMenuState.readGroup(paramAttributeSet);
          }
          else if (str1.equals("item"))
          {
            localMenuState.readItem(paramAttributeSet);
          }
          else if (str1.equals("menu"))
          {
            SubMenu localSubMenu1 = localMenuState.addSubMenuItem();
            parseMenu(paramXmlPullParser, paramAttributeSet, localSubMenu1);
          }
          else
          {
            String str3 = str1;
            continue;
            str1 = paramXmlPullParser.getName();
            if ((0 == 0) || (!str1.equals(null)))
              if (str1.equals("group"))
                localMenuState.resetGroup();
              else if (str1.equals("item"))
              {
                if (!localMenuState.hasAddedItem())
                  if ((localMenuState.itemActionProvider != null) && (localMenuState.itemActionProvider.hasSubMenu()))
                    SubMenu localSubMenu2 = localMenuState.addSubMenuItem();
                  else
                    localMenuState.addItem();
              }
              else if (str1.equals("menu"))
                k = 1;
          }
        }
      }
    case 1:
    }
    throw new RuntimeException("Unexpected end of document");
  }

  // ERROR //
  public void inflate(int paramInt, Menu paramMenu)
  {
    // Byte code:
    //   0: aload_2
    //   1: instanceof 146
    //   4: ifne +10 -> 14
    //   7: aload_0
    //   8: iload_1
    //   9: aload_2
    //   10: invokespecial 148	android/view/MenuInflater:inflate	(ILandroid/view/Menu;)V
    //   13: return
    //   14: aload_0
    //   15: getfield 38	android/support/v7/internal/view/SupportMenuInflater:mContext	Landroid/content/Context;
    //   18: invokevirtual 152	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   21: iload_1
    //   22: invokevirtual 158	android/content/res/Resources:getLayout	(I)Landroid/content/res/XmlResourceParser;
    //   25: astore_3
    //   26: aload_3
    //   27: invokestatic 164	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   30: astore 4
    //   32: aload_0
    //   33: aload_3
    //   34: aload 4
    //   36: aload_2
    //   37: invokespecial 121	android/support/v7/internal/view/SupportMenuInflater:parseMenu	(Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;Landroid/view/Menu;)V
    //   40: aload_3
    //   41: ifnonnull +4 -> 45
    //   44: return
    //   45: aload_3
    //   46: invokeinterface 169 1 0
    //   51: return
    //   52: astore 5
    //   54: new 171	android/view/InflateException
    //   57: dup
    //   58: ldc 173
    //   60: aload 5
    //   62: invokespecial 176	android/view/InflateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   65: athrow
    //   66: astore 6
    //   68: aload_3
    //   69: ifnull +9 -> 78
    //   72: aload_3
    //   73: invokeinterface 169 1 0
    //   78: aload 6
    //   80: athrow
    //   81: astore 5
    //   83: new 171	android/view/InflateException
    //   86: dup
    //   87: ldc 173
    //   89: aload 5
    //   91: invokespecial 176	android/view/InflateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   94: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   14	40	52	org/xmlpull/v1/XmlPullParserException
    //   14	40	66	finally
    //   54	66	66	finally
    //   83	95	66	finally
    //   14	40	81	java/io/IOException
  }

  private class MenuState
  {
    private int groupCategory;
    private int groupCheckable;
    private boolean groupEnabled;
    private int groupId;
    private int groupOrder;
    private boolean groupVisible;
    private ActionProvider itemActionProvider;
    private String itemActionProviderClassName;
    private String itemActionViewClassName;
    private int itemActionViewLayout;
    private boolean itemAdded;
    private char itemAlphabeticShortcut;
    private int itemCategoryOrder;
    private int itemCheckable;
    private boolean itemChecked;
    private boolean itemEnabled;
    private int itemIconResId;
    private int itemId;
    private String itemListenerMethodName;
    private char itemNumericShortcut;
    private int itemShowAsAction;
    private CharSequence itemTitle;
    private CharSequence itemTitleCondensed;
    private boolean itemVisible;
    private Menu menu;

    public MenuState(Menu arg2)
    {
      Object localObject;
      this.menu = localObject;
      resetGroup();
    }

    private char getShortcut(String paramString)
    {
      char c = '\000';
      if (paramString == null);
      while (true)
      {
        return c;
        c = paramString.charAt(0);
      }
    }

    private <T> T newInstance(String paramString, Class<?>[] paramArrayOfClass, Object[] paramArrayOfObject)
    {
      try
      {
        Object localObject1 = SupportMenuInflater.this.mContext.getClassLoader().loadClass(paramString).getConstructor(paramArrayOfClass).newInstance(paramArrayOfObject);
        localObject2 = localObject1;
        return localObject2;
      }
      catch (Exception localException)
      {
        while (true)
        {
          String str = "Cannot instantiate class: " + paramString;
          int i = Log.w("SupportMenuInflater", str, localException);
          Object localObject2 = null;
        }
      }
    }

    private void setItem(MenuItem paramMenuItem)
    {
      boolean bool1 = this.itemChecked;
      MenuItem localMenuItem1 = paramMenuItem.setChecked(bool1);
      boolean bool2 = this.itemVisible;
      MenuItem localMenuItem2 = localMenuItem1.setVisible(bool2);
      boolean bool3 = this.itemEnabled;
      MenuItem localMenuItem3 = localMenuItem2.setEnabled(bool3);
      if (this.itemCheckable >= 1);
      for (boolean bool4 = true; ; bool4 = false)
      {
        MenuItem localMenuItem4 = localMenuItem3.setCheckable(bool4);
        CharSequence localCharSequence = this.itemTitleCondensed;
        MenuItem localMenuItem5 = localMenuItem4.setTitleCondensed(localCharSequence);
        int i = this.itemIconResId;
        MenuItem localMenuItem6 = localMenuItem5.setIcon(i);
        char c1 = this.itemAlphabeticShortcut;
        MenuItem localMenuItem7 = localMenuItem6.setAlphabeticShortcut(c1);
        char c2 = this.itemNumericShortcut;
        MenuItem localMenuItem8 = localMenuItem7.setNumericShortcut(c2);
        if (this.itemShowAsAction >= 0)
        {
          int j = this.itemShowAsAction;
          MenuItemCompat.setShowAsAction(paramMenuItem, j);
        }
        if (this.itemListenerMethodName == null)
          break label229;
        if (!SupportMenuInflater.this.mContext.isRestricted())
          break;
        throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
      }
      Object localObject = SupportMenuInflater.this.mRealOwner;
      String str1 = this.itemListenerMethodName;
      SupportMenuInflater.InflatedOnMenuItemClickListener localInflatedOnMenuItemClickListener = new SupportMenuInflater.InflatedOnMenuItemClickListener(localObject, str1);
      MenuItem localMenuItem9 = paramMenuItem.setOnMenuItemClickListener(localInflatedOnMenuItemClickListener);
      label229: if ((paramMenuItem instanceof MenuItemImpl))
      {
        MenuItemImpl localMenuItemImpl = (MenuItemImpl)paramMenuItem;
        if (this.itemCheckable >= 2)
        {
          if (!(paramMenuItem instanceof MenuItemImpl))
            break label373;
          ((MenuItemImpl)paramMenuItem).setExclusiveCheckable(true);
        }
        int k = 0;
        if (this.itemActionViewClassName != null)
        {
          String str2 = this.itemActionViewClassName;
          Class[] arrayOfClass = SupportMenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
          Object[] arrayOfObject = SupportMenuInflater.this.mActionViewConstructorArguments;
          View localView = (View)newInstance(str2, arrayOfClass, arrayOfObject);
          MenuItem localMenuItem10 = MenuItemCompat.setActionView(paramMenuItem, localView);
          k = 1;
        }
        if (this.itemActionViewLayout > 0)
        {
          if (k != 0)
            break label391;
          int m = this.itemActionViewLayout;
          MenuItem localMenuItem11 = MenuItemCompat.setActionView(paramMenuItem, m);
        }
      }
      while (true)
      {
        label265: if (this.itemActionProvider == null)
          return;
        ActionProvider localActionProvider = this.itemActionProvider;
        MenuItem localMenuItem12 = MenuItemCompat.setActionProvider(paramMenuItem, localActionProvider);
        return;
        break;
        label373: if (!(paramMenuItem instanceof MenuItemWrapperICS))
          break label265;
        ((MenuItemWrapperICS)paramMenuItem).setExclusiveCheckable(true);
        break label265;
        label391: int n = Log.w("SupportMenuInflater", "Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
      }
    }

    public void addItem()
    {
      this.itemAdded = true;
      Menu localMenu = this.menu;
      int i = this.groupId;
      int j = this.itemId;
      int k = this.itemCategoryOrder;
      CharSequence localCharSequence = this.itemTitle;
      MenuItem localMenuItem = localMenu.add(i, j, k, localCharSequence);
      setItem(localMenuItem);
    }

    public SubMenu addSubMenuItem()
    {
      this.itemAdded = true;
      Menu localMenu = this.menu;
      int i = this.groupId;
      int j = this.itemId;
      int k = this.itemCategoryOrder;
      CharSequence localCharSequence = this.itemTitle;
      SubMenu localSubMenu = localMenu.addSubMenu(i, j, k, localCharSequence);
      MenuItem localMenuItem = localSubMenu.getItem();
      setItem(localMenuItem);
      return localSubMenu;
    }

    public boolean hasAddedItem()
    {
      return this.itemAdded;
    }

    public void readGroup(AttributeSet paramAttributeSet)
    {
      Context localContext = SupportMenuInflater.this.mContext;
      int[] arrayOfInt = R.styleable.MenuGroup;
      TypedArray localTypedArray = localContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt);
      int i = localTypedArray.getResourceId(1, 0);
      this.groupId = i;
      int j = localTypedArray.getInt(3, 0);
      this.groupCategory = j;
      int k = localTypedArray.getInt(4, 0);
      this.groupOrder = k;
      int m = localTypedArray.getInt(5, 0);
      this.groupCheckable = m;
      boolean bool1 = localTypedArray.getBoolean(2, true);
      this.groupVisible = bool1;
      boolean bool2 = localTypedArray.getBoolean(0, true);
      this.groupEnabled = bool2;
      localTypedArray.recycle();
    }

    public void readItem(AttributeSet paramAttributeSet)
    {
      Context localContext = SupportMenuInflater.this.mContext;
      int[] arrayOfInt = R.styleable.MenuItem;
      TypedArray localTypedArray = localContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt);
      int i = localTypedArray.getResourceId(2, 0);
      this.itemId = i;
      int j = this.groupCategory;
      int k = localTypedArray.getInt(5, j);
      int m = this.groupOrder;
      int n = localTypedArray.getInt(6, m);
      int i1 = 0xFFFF0000 & k;
      int i2 = 0xFFFF & n;
      int i3 = i1 | i2;
      this.itemCategoryOrder = i3;
      CharSequence localCharSequence1 = localTypedArray.getText(7);
      this.itemTitle = localCharSequence1;
      CharSequence localCharSequence2 = localTypedArray.getText(8);
      this.itemTitleCondensed = localCharSequence2;
      int i4 = localTypedArray.getResourceId(0, 0);
      this.itemIconResId = i4;
      String str1 = localTypedArray.getString(9);
      char c1 = getShortcut(str1);
      this.itemAlphabeticShortcut = c1;
      String str2 = localTypedArray.getString(10);
      char c2 = getShortcut(str2);
      this.itemNumericShortcut = c2;
      int i5;
      label218: int i8;
      label365: ActionProvider localActionProvider;
      if (localTypedArray.hasValue(11))
        if (localTypedArray.getBoolean(11, false))
        {
          i5 = 1;
          this.itemCheckable = i5;
          boolean bool1 = localTypedArray.getBoolean(3, false);
          this.itemChecked = bool1;
          boolean bool2 = this.groupVisible;
          boolean bool3 = localTypedArray.getBoolean(4, bool2);
          this.itemVisible = bool3;
          boolean bool4 = this.groupEnabled;
          boolean bool5 = localTypedArray.getBoolean(1, bool4);
          this.itemEnabled = bool5;
          int i6 = localTypedArray.getInt(13, -1);
          this.itemShowAsAction = i6;
          String str3 = localTypedArray.getString(12);
          this.itemListenerMethodName = str3;
          int i7 = localTypedArray.getResourceId(14, 0);
          this.itemActionViewLayout = i7;
          String str4 = localTypedArray.getString(15);
          this.itemActionViewClassName = str4;
          String str5 = localTypedArray.getString(16);
          this.itemActionProviderClassName = str5;
          if (this.itemActionProviderClassName == null)
            break label457;
          i8 = 1;
          if ((i8 == 0) || (this.itemActionViewLayout != 0) || (this.itemActionViewClassName != null))
            break label463;
          String str6 = this.itemActionProviderClassName;
          Class[] arrayOfClass = SupportMenuInflater.ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE;
          Object[] arrayOfObject = SupportMenuInflater.this.mActionProviderConstructorArguments;
          localActionProvider = (ActionProvider)newInstance(str6, arrayOfClass, arrayOfObject);
        }
      for (this.itemActionProvider = localActionProvider; ; this.itemActionProvider = null)
      {
        localTypedArray.recycle();
        this.itemAdded = false;
        return;
        i5 = 0;
        break;
        int i9 = this.groupCheckable;
        this.itemCheckable = i9;
        break label218;
        label457: i8 = 0;
        break label365;
        label463: if (i8 != 0)
          int i10 = Log.w("SupportMenuInflater", "Ignoring attribute 'actionProviderClass'. Action view already specified.");
      }
    }

    public void resetGroup()
    {
      this.groupId = 0;
      this.groupCategory = 0;
      this.groupOrder = 0;
      this.groupCheckable = 0;
      this.groupVisible = true;
      this.groupEnabled = true;
    }
  }

  private static class InflatedOnMenuItemClickListener
    implements MenuItem.OnMenuItemClickListener
  {
    private static final Class<?>[] PARAM_TYPES = arrayOfClass;
    private Method mMethod;
    private Object mRealOwner;

    static
    {
      Class[] arrayOfClass = new Class[1];
      arrayOfClass[0] = MenuItem.class;
    }

    public InflatedOnMenuItemClickListener(Object paramObject, String paramString)
    {
      this.mRealOwner = paramObject;
      Class localClass = paramObject.getClass();
      try
      {
        Class[] arrayOfClass = PARAM_TYPES;
        Method localMethod = localClass.getMethod(paramString, arrayOfClass);
        this.mMethod = localMethod;
        return;
      }
      catch (Exception localException)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Couldn't resolve menu item onClick handler ").append(paramString).append(" in class ");
        String str1 = localClass.getName();
        String str2 = str1;
        InflateException localInflateException = new InflateException(str2);
        Throwable localThrowable = localInflateException.initCause(localException);
        throw localInflateException;
      }
    }

    public boolean onMenuItemClick(MenuItem paramMenuItem)
    {
      boolean bool = true;
      try
      {
        Class localClass1 = this.mMethod.getReturnType();
        Class localClass2 = Boolean.TYPE;
        if (localClass1 == localClass2)
        {
          Method localMethod1 = this.mMethod;
          Object localObject1 = this.mRealOwner;
          Object[] arrayOfObject1 = new Object[1];
          arrayOfObject1[0] = paramMenuItem;
          bool = ((Boolean)localMethod1.invoke(localObject1, arrayOfObject1)).booleanValue();
        }
        while (true)
        {
          return bool;
          Method localMethod2 = this.mMethod;
          Object localObject2 = this.mRealOwner;
          Object[] arrayOfObject2 = new Object[1];
          arrayOfObject2[0] = paramMenuItem;
          Object localObject3 = localMethod2.invoke(localObject2, arrayOfObject2);
        }
      }
      catch (Exception localException)
      {
        throw new RuntimeException(localException);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.SupportMenuInflater
 * JD-Core Version:    0.6.2
 */