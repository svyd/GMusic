package android.support.v4.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider
{
  private static final String[] COLUMNS = arrayOfString;
  private static final File DEVICE_ROOT = new File("/");
  private static HashMap<String, PathStrategy> sCache = new HashMap();
  private PathStrategy mStrategy;

  static
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "_display_name";
    arrayOfString[1] = "_size";
  }

  private static File buildPath(File paramFile, String[] paramArrayOfString)
  {
    File localFile = paramFile;
    String[] arrayOfString = paramArrayOfString;
    int i = arrayOfString.length;
    int j = 0;
    Object localObject1 = localFile;
    String str;
    if (j < i)
    {
      str = arrayOfString[j];
      if (str == null)
        break label61;
    }
    label61: for (Object localObject2 = new File((File)localObject1, str); ; localObject2 = localObject1)
    {
      j += 1;
      localObject1 = localObject2;
      break;
      return localObject1;
    }
  }

  private static Object[] copyOf(Object[] paramArrayOfObject, int paramInt)
  {
    Object[] arrayOfObject = new Object[paramInt];
    System.arraycopy(paramArrayOfObject, 0, arrayOfObject, 0, paramInt);
    return arrayOfObject;
  }

  private static String[] copyOf(String[] paramArrayOfString, int paramInt)
  {
    String[] arrayOfString = new String[paramInt];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramInt);
    return arrayOfString;
  }

  private static PathStrategy getPathStrategy(Context paramContext, String paramString)
  {
    Object localObject1;
    synchronized (sCache)
    {
      localObject1 = (PathStrategy)sCache.get(paramString);
      if (localObject1 != null);
    }
    try
    {
      PathStrategy localPathStrategy = parsePathStrategy(paramContext, paramString);
      localObject1 = localPathStrategy;
      Object localObject2 = sCache.put(paramString, localObject1);
      return localObject1;
    }
    catch (IOException localIOException)
    {
      throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", localIOException);
      localObject3 = finally;
      throw localObject3;
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
      throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", localXmlPullParserException);
    }
  }

  private static int modeToMode(String paramString)
  {
    int i;
    if ("r".equals(paramString))
      i = 268435456;
    while (true)
    {
      return i;
      if (("w".equals(paramString)) || ("wt".equals(paramString)))
      {
        i = 738197504;
      }
      else if ("wa".equals(paramString))
      {
        i = 704643072;
      }
      else if ("rw".equals(paramString))
      {
        i = 939524096;
      }
      else
      {
        if (!"rwt".equals(paramString))
          break;
        i = 1006632960;
      }
    }
    String str = "Invalid mode: " + paramString;
    throw new IllegalArgumentException(str);
  }

  private static PathStrategy parsePathStrategy(Context paramContext, String paramString)
    throws IOException, XmlPullParserException
  {
    SimplePathStrategy localSimplePathStrategy = new SimplePathStrategy(paramString);
    ProviderInfo localProviderInfo = paramContext.getPackageManager().resolveContentProvider(paramString, 128);
    PackageManager localPackageManager = paramContext.getPackageManager();
    XmlResourceParser localXmlResourceParser = localProviderInfo.loadXmlMetaData(localPackageManager, "android.support.FILE_PROVIDER_PATHS");
    if (localXmlResourceParser == null)
      throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
    label278: 
    while (true)
    {
      int i = localXmlResourceParser.next();
      if (i == 1)
        break;
      if (i == 2)
      {
        String str1 = localXmlResourceParser.getName();
        String str2 = localXmlResourceParser.getAttributeValue(null, "name");
        String str3 = localXmlResourceParser.getAttributeValue(null, "path");
        File localFile1 = null;
        if ("root-path".equals(str1))
        {
          File localFile2 = DEVICE_ROOT;
          String[] arrayOfString1 = new String[1];
          arrayOfString1[0] = str3;
          localFile1 = buildPath(localFile2, arrayOfString1);
        }
        while (true)
        {
          if (localFile1 == null)
            break label278;
          localSimplePathStrategy.addRoot(str2, localFile1);
          break;
          if ("files-path".equals(str1))
          {
            File localFile3 = paramContext.getFilesDir();
            String[] arrayOfString2 = new String[1];
            arrayOfString2[0] = str3;
            localFile1 = buildPath(localFile3, arrayOfString2);
          }
          else if ("cache-path".equals(str1))
          {
            File localFile4 = paramContext.getCacheDir();
            String[] arrayOfString3 = new String[1];
            arrayOfString3[0] = str3;
            localFile1 = buildPath(localFile4, arrayOfString3);
          }
          else if ("external-path".equals(str1))
          {
            File localFile5 = Environment.getExternalStorageDirectory();
            String[] arrayOfString4 = new String[1];
            arrayOfString4[0] = str3;
            localFile1 = buildPath(localFile5, arrayOfString4);
          }
        }
      }
    }
    return localSimplePathStrategy;
  }

  public void attachInfo(Context paramContext, ProviderInfo paramProviderInfo)
  {
    super.attachInfo(paramContext, paramProviderInfo);
    if (paramProviderInfo.exported)
      throw new SecurityException("Provider must not be exported");
    if (!paramProviderInfo.grantUriPermissions)
      throw new SecurityException("Provider must grant uri permissions");
    String str = paramProviderInfo.authority;
    PathStrategy localPathStrategy = getPathStrategy(paramContext, str);
    this.mStrategy = localPathStrategy;
  }

  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    if (this.mStrategy.getFileForUri(paramUri).delete());
    for (int i = 1; ; i = 0)
      return i;
  }

  public String getType(Uri paramUri)
  {
    File localFile = this.mStrategy.getFileForUri(paramUri);
    int i = localFile.getName().lastIndexOf('.');
    String str3;
    if (i >= 0)
    {
      String str1 = localFile.getName();
      int j = i + 1;
      String str2 = str1.substring(j);
      str3 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(str2);
      if (str3 == null);
    }
    while (true)
    {
      return str3;
      str3 = "application/octet-stream";
    }
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    throw new UnsupportedOperationException("No external inserts");
  }

  public boolean onCreate()
  {
    return true;
  }

  public ParcelFileDescriptor openFile(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    File localFile = this.mStrategy.getFileForUri(paramUri);
    int i = modeToMode(paramString);
    return ParcelFileDescriptor.open(localFile, i);
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    File localFile = this.mStrategy.getFileForUri(paramUri);
    if (paramArrayOfString1 == null)
      paramArrayOfString1 = COLUMNS;
    String[] arrayOfString1 = new String[paramArrayOfString1.length];
    Object[] arrayOfObject1 = new Object[paramArrayOfString1.length];
    String[] arrayOfString2 = paramArrayOfString1;
    int i = arrayOfString2.length;
    int j = 0;
    int k = 0;
    String str1;
    int m;
    if (j < i)
    {
      str1 = arrayOfString2[j];
      if ("_display_name".equals(str1))
      {
        arrayOfString1[k] = "_display_name";
        m = k + 1;
        String str2 = localFile.getName();
        arrayOfObject1[k] = str2;
      }
    }
    while (true)
    {
      j += 1;
      k = m;
      break;
      if ("_size".equals(str1))
      {
        arrayOfString1[k] = "_size";
        m = k + 1;
        Long localLong = Long.valueOf(localFile.length());
        arrayOfObject1[k] = localLong;
        continue;
        String[] arrayOfString3 = copyOf(arrayOfString1, k);
        Object[] arrayOfObject2 = copyOf(arrayOfObject1, k);
        MatrixCursor localMatrixCursor = new MatrixCursor(arrayOfString3, 1);
        localMatrixCursor.addRow(arrayOfObject2);
        return localMatrixCursor;
      }
      else
      {
        m = k;
      }
    }
  }

  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException("No external updates");
  }

  static class SimplePathStrategy
    implements FileProvider.PathStrategy
  {
    private final String mAuthority;
    private final HashMap<String, File> mRoots;

    public SimplePathStrategy(String paramString)
    {
      HashMap localHashMap = new HashMap();
      this.mRoots = localHashMap;
      this.mAuthority = paramString;
    }

    public void addRoot(String paramString, File paramFile)
    {
      if (TextUtils.isEmpty(paramString))
        throw new IllegalArgumentException("Name must not be empty");
      try
      {
        File localFile1 = paramFile.getCanonicalFile();
        File localFile2 = localFile1;
        Object localObject = this.mRoots.put(paramString, localFile2);
        return;
      }
      catch (IOException localIOException)
      {
        String str = "Failed to resolve canonical path for " + paramFile;
        throw new IllegalArgumentException(str, localIOException);
      }
    }

    public File getFileForUri(Uri paramUri)
    {
      String str1 = paramUri.getEncodedPath();
      int i = str1.indexOf('/', 1);
      String str2 = Uri.decode(str1.substring(1, i));
      int j = i + 1;
      String str3 = Uri.decode(str1.substring(j));
      File localFile1 = (File)this.mRoots.get(str2);
      if (localFile1 == null)
      {
        String str4 = "Unable to find configured root for " + paramUri;
        throw new IllegalArgumentException(str4);
      }
      File localFile2 = new File(localFile1, str3);
      File localFile4;
      try
      {
        File localFile3 = localFile2.getCanonicalFile();
        localFile4 = localFile3;
        String str5 = localFile4.getPath();
        String str6 = localFile1.getPath();
        if (!str5.startsWith(str6))
          throw new SecurityException("Resolved path jumped beyond configured root");
      }
      catch (IOException localIOException)
      {
        String str7 = "Failed to resolve canonical path for " + localFile2;
        throw new IllegalArgumentException(str7);
      }
      return localFile4;
    }
  }

  static abstract interface PathStrategy
  {
    public abstract File getFileForUri(Uri paramUri);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.content.FileProvider
 * JD-Core Version:    0.6.2
 */