package ca.mcgill.ecse429.conformancetest.ccoinbox;

public class CCoinBox
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CCoinBox Attributes
  private int totalQtrs;
  private int curQtrs;
  private boolean allowVend;

  //CCoinBox State Machines
  enum State { empty, notAllowed, allowed }
  private State state;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CCoinBox()
  {
    totalQtrs = 0;
    curQtrs = 0;
    allowVend = false;
    setState(State.empty);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTotalQtrs(int aTotalQtrs)
  {
    boolean wasSet = false;
    totalQtrs = aTotalQtrs;
    wasSet = true;
    return wasSet;
  }

  public boolean setCurQtrs(int aCurQtrs)
  {
    boolean wasSet = false;
    curQtrs = aCurQtrs;
    wasSet = true;
    return wasSet;
  }

  public boolean setAllowVend(boolean aAllowVend)
  {
    boolean wasSet = false;
    allowVend = aAllowVend;
    wasSet = true;
    return wasSet;
  }

  public int getTotalQtrs()
  {
    return totalQtrs;
  }

  public int getCurQtrs()
  {
    return curQtrs;
  }

  public boolean getAllowVend()
  {
    return allowVend;
  }

  public boolean isAllowVend()
  {
    return allowVend;
  }
  
  // state reporter methods

  public String getStateFullName()
  {
    String answer = state.toString();
    return answer;
  }

  public State getState()
  {
    return state;
  }
  
  // events

  public boolean returnQtrs()
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case empty:
        setState(State.empty);
        wasEventProcessed = true;
        break;
      case notAllowed:
        curQtrs = 0;
        setState(State.empty);
        wasEventProcessed = true;
        break;
      case allowed:
        curQtrs = 0;
        setState(State.empty);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean addQtr()
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case empty:
        curQtrs = curQtrs + 1;
        setState(State.notAllowed);
        wasEventProcessed = true;
        break;
      case notAllowed:
        curQtrs = curQtrs + 1;
        allowVend = true;
        setState(State.allowed);
        wasEventProcessed = true;
        break;
      case allowed:
        curQtrs = curQtrs + 1;
        setState(State.notAllowed);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean reset()
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case empty:
        totalQtrs = 0;
        setState(State.empty);
        wasEventProcessed = true;
        break;
      case notAllowed:
        totalQtrs = 0;
        curQtrs = 0;
        setState(State.empty);
        wasEventProcessed = true;
        break;
      case allowed:
        totalQtrs = 0;
        curQtrs = 0;
        setState(State.empty);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean vend()
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case allowed:
        if (getCurQtrs()==2)
        {
          curQtrs = 0;
          totalQtrs = totalQtrs + 2;
          allowVend = false;
          setState(State.empty);
          wasEventProcessed = true;
          break;
        }
        if (getCurQtrs()==3)
        {
          curQtrs = 1;
          totalQtrs = totalQtrs + 2;
          allowVend = false;
          setState(State.notAllowed);
          wasEventProcessed = true;
          break;
        }
        if (getCurQtrs()>3)
        {
          curQtrs = 2;
          totalQtrs = totalQtrs + 2;
          setState(State.allowed);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setState(State aState)
  {
    state = aState;
  }

  public void delete()
  {}


  public String toString()
  {
	  String outputString = "";
    return super.toString() + "["+
            "totalQtrs" + ":" + getTotalQtrs()+ "," +
            "curQtrs" + ":" + getCurQtrs()+ "," +
            "allowVend" + ":" + getAllowVend()+ "]"
     + outputString;
  }
}