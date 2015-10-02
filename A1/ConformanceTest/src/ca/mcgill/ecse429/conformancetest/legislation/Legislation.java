package ca.mcgill.ecse429.conformancetest.legislation;

public class Legislation
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Legislation Attributes
  private boolean isCommonsBill;

  //Legislation State Machines
  enum State { inPreparation, inHouseOfCommons, inSenate, finalized }
  private State state;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Legislation()
  {
    isCommonsBill = true;
    setState(State.inPreparation);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setIsCommonsBill(boolean aIsCommonsBill)
  {
    boolean wasSet = false;
    isCommonsBill = aIsCommonsBill;
    wasSet = true;
    return wasSet;
  }

  public boolean getIsCommonsBill()
  {
    return isCommonsBill;
  }

  public boolean isIsCommonsBill()
  {
    return isCommonsBill;
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

  public boolean introduceInHouse()
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case inPreparation:
        setState(State.inHouseOfCommons);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean introduceInSenate()
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case inPreparation:
        isCommonsBill = false;
        setState(State.inSenate);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean voteFails()
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case inHouseOfCommons:
        setState(State.inPreparation);
        wasEventProcessed = true;
        break;
      case inSenate:
        setState(State.inPreparation);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean votePasses()
  {
    boolean wasEventProcessed = false;
    
    State aState = state;
    switch (aState)
    {
      case inHouseOfCommons:
        if (getIsCommonsBill())
        {
          setState(State.inSenate);
          wasEventProcessed = true;
          break;
        }
        if (!getIsCommonsBill())
        {
          setState(State.finalized);
          wasEventProcessed = true;
          break;
        }
        break;
      case inSenate:
        if (!getIsCommonsBill())
        {
          setState(State.inHouseOfCommons);
          wasEventProcessed = true;
          break;
        }
        if (getIsCommonsBill())
        {
          setState(State.finalized);
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
            "isCommonsBill" + ":" + getIsCommonsBill()+ "]"
     + outputString;
  }
}