package org.codehaus.jackson.util;

import java.util.LinkedList;

public final class ByteArrayBuilder
{
  private static final byte[] NO_BYTES = new byte[0];
  private byte[] _currBlock;
  private LinkedList<byte[]> _pastBlocks;

  public ByteArrayBuilder()
  {
    this(500);
  }

  public ByteArrayBuilder(int paramInt)
  {
    LinkedList localLinkedList = new LinkedList();
    this._pastBlocks = localLinkedList;
    byte[] arrayOfByte = new byte[paramInt];
    this._currBlock = arrayOfByte;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.util.ByteArrayBuilder
 * JD-Core Version:    0.6.2
 */