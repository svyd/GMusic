package com.google.common.base;

import java.util.Iterator;
import java.util.NoSuchElementException;

abstract class AbstractIterator<T>
  implements Iterator<T>
{
  private T next;
  private State state;

  protected AbstractIterator()
  {
    State localState = State.NOT_READY;
    this.state = localState;
  }

  private boolean tryToComputeNext()
  {
    State localState1 = State.FAILED;
    this.state = localState1;
    Object localObject = computeNext();
    this.next = localObject;
    State localState2 = this.state;
    State localState3 = State.DONE;
    if (localState2 != localState3)
    {
      State localState4 = State.READY;
      this.state = localState4;
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  protected abstract T computeNext();

  protected final T endOfData()
  {
    State localState = State.DONE;
    this.state = localState;
    return null;
  }

  public final boolean hasNext()
  {
    boolean bool1 = false;
    State localState1 = this.state;
    State localState2 = State.FAILED;
    boolean bool2;
    if (localState1 != localState2)
    {
      bool2 = true;
      Preconditions.checkState(bool2);
      int[] arrayOfInt = 1.$SwitchMap$com$google$common$base$AbstractIterator$State;
      int i = this.state.ordinal();
      switch (arrayOfInt[i])
      {
      default:
      case 1:
      case 2:
      }
    }
    for (bool1 = tryToComputeNext(); ; bool1 = true)
    {
      return bool1;
      bool2 = false;
      break;
    }
  }

  public final T next()
  {
    if (!hasNext())
      throw new NoSuchElementException();
    State localState = State.NOT_READY;
    this.state = localState;
    return this.next;
  }

  public final void remove()
  {
    throw new UnsupportedOperationException();
  }

  private static enum State
  {
    static
    {
      NOT_READY = new State("NOT_READY", 1);
      DONE = new State("DONE", 2);
      FAILED = new State("FAILED", 3);
      State[] arrayOfState = new State[4];
      State localState1 = READY;
      arrayOfState[0] = localState1;
      State localState2 = NOT_READY;
      arrayOfState[1] = localState2;
      State localState3 = DONE;
      arrayOfState[2] = localState3;
      State localState4 = FAILED;
      arrayOfState[3] = localState4;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.AbstractIterator
 * JD-Core Version:    0.6.2
 */