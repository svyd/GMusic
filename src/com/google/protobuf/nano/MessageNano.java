package com.google.protobuf.nano;

import java.io.IOException;

public abstract class MessageNano
{
  public static final MessageNano mergeFrom(MessageNano paramMessageNano, byte[] paramArrayOfByte)
    throws InvalidProtocolBufferNanoException
  {
    int i = paramArrayOfByte.length;
    return mergeFrom(paramMessageNano, paramArrayOfByte, 0, i);
  }

  public static final MessageNano mergeFrom(MessageNano paramMessageNano, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidProtocolBufferNanoException
  {
    try
    {
      CodedInputByteBufferNano localCodedInputByteBufferNano = CodedInputByteBufferNano.newInstance(paramArrayOfByte, paramInt1, paramInt2);
      MessageNano localMessageNano = paramMessageNano.mergeFrom(localCodedInputByteBufferNano);
      localCodedInputByteBufferNano.checkLastTagWas(0);
      return paramMessageNano;
    }
    catch (InvalidProtocolBufferNanoException localInvalidProtocolBufferNanoException)
    {
      throw localInvalidProtocolBufferNanoException;
    }
    catch (IOException localIOException)
    {
    }
    throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).");
  }

  public static final void toByteArray(MessageNano paramMessageNano, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    try
    {
      CodedOutputByteBufferNano localCodedOutputByteBufferNano = CodedOutputByteBufferNano.newInstance(paramArrayOfByte, paramInt1, paramInt2);
      paramMessageNano.writeTo(localCodedOutputByteBufferNano);
      localCodedOutputByteBufferNano.checkNoSpaceLeft();
      return;
    }
    catch (IOException localIOException)
    {
    }
    throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).");
  }

  public static final byte[] toByteArray(MessageNano paramMessageNano)
  {
    byte[] arrayOfByte = new byte[paramMessageNano.getSerializedSize()];
    int i = arrayOfByte.length;
    toByteArray(paramMessageNano, arrayOfByte, 0, i);
    return arrayOfByte;
  }

  public abstract int getCachedSize();

  public abstract int getSerializedSize();

  public abstract MessageNano mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
    throws IOException;

  public abstract void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
    throws IOException;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.protobuf.nano.MessageNano
 * JD-Core Version:    0.6.2
 */