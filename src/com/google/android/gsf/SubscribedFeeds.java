package com.google.android.gsf;

import android.net.Uri;
import android.provider.BaseColumns;

public class SubscribedFeeds
{
  public static final class Feeds
    implements BaseColumns
  {
    public static final Uri CONTENT_URI = Uri.parse("content://subscribedfeeds/feeds");
    public static final Uri DELETED_CONTENT_URI = Uri.parse("content://subscribedfeeds/deleted_feeds");
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.gsf.SubscribedFeeds
 * JD-Core Version:    0.6.2
 */