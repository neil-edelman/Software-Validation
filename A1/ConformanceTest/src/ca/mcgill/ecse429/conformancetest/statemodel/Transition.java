package ca.mcgill.ecse429.conformancetest.statemodel;

public class Transition
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Transition Attributes
  private String event;
  private String condition;
  private String action;

  //Transition Associations
  private State from;
  private State to;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Transition(String aEvent, String aCondition, String aAction, State aFrom, State aTo)
  {
    event = aEvent;
    condition = aCondition;
    action = aAction;
    if (!setFrom(aFrom))
    {
      throw new RuntimeException("Unable to create Transition due to aFrom");
    }
    if (!setTo(aTo))
    {
      throw new RuntimeException("Unable to create Transition due to aTo");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEvent(String aEvent)
  {
    boolean wasSet = false;
    event = aEvent;
    wasSet = true;
    return wasSet;
  }

  public boolean setCondition(String aCondition)
  {
    boolean wasSet = false;
    condition = aCondition;
    wasSet = true;
    return wasSet;
  }

  public boolean setAction(String aAction)
  {
    boolean wasSet = false;
    action = aAction;
    wasSet = true;
    return wasSet;
  }

  public String getEvent()
  {
    return event;
  }

  public String getCondition()
  {
    return condition;
  }

  public String getAction()
  {
    return action;
  }

  public State getFrom()
  {
    return from;
  }

  public State getTo()
  {
    return to;
  }

  public boolean setFrom(State aNewFrom)
  {
    boolean wasSet = false;
    if (aNewFrom != null)
    {
      from = aNewFrom;
      wasSet = true;
    }
    return wasSet;
  }

  public boolean setTo(State aNewTo)
  {
    boolean wasSet = false;
    if (aNewTo != null)
    {
      to = aNewTo;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    from = null;
    to = null;
  }


  public String toString()
  {
	  String outputString = "";
    return super.toString() + "["+
            "event" + ":" + getEvent()+ "," +
            "condition" + ":" + getCondition()+ "," +
            "action" + ":" + getAction()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "from = "+(getFrom()!=null?Integer.toHexString(System.identityHashCode(getFrom())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "to = "+(getTo()!=null?Integer.toHexString(System.identityHashCode(getTo())):"null")
     + outputString;
  }
}