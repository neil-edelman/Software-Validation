package ca.mcgill.ecse429.conformancetest.statemodel;
import java.util.*;

public class StateMachine
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static StateMachine theInstance = null;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //StateMachine Attributes
  private String packageName;
  private String className;
  private State startState;

  //StateMachine Associations
  private List<State> states;
  private List<Transition> transitions;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  private StateMachine()
  {
    packageName = null;
    className = null;
    states = new ArrayList<State>();
    transitions = new ArrayList<Transition>();
  }

  public static StateMachine getInstance()
  {
    if(theInstance == null)
    {
      theInstance = new StateMachine();
    }
    return theInstance;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPackageName(String aPackageName)
  {
    boolean wasSet = false;
    packageName = aPackageName;
    wasSet = true;
    return wasSet;
  }

  public boolean setClassName(String aClassName)
  {
    boolean wasSet = false;
    className = aClassName;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartState(State aStartState)
  {
    boolean wasSet = false;
    startState = aStartState;
    wasSet = true;
    return wasSet;
  }

  public String getPackageName()
  {
    return packageName;
  }

  public String getClassName()
  {
    return className;
  }

  public State getStartState()
  {
    return startState;
  }

  public State getState(int index)
  {
    State aState = states.get(index);
    return aState;
  }

  public List<State> getStates()
  {
    List<State> newStates = Collections.unmodifiableList(states);
    return newStates;
  }

  public int numberOfStates()
  {
    int number = states.size();
    return number;
  }

  public boolean hasStates()
  {
    boolean has = states.size() > 0;
    return has;
  }

  public int indexOfState(State aState)
  {
    int index = states.indexOf(aState);
    return index;
  }

  public Transition getTransition(int index)
  {
    Transition aTransition = transitions.get(index);
    return aTransition;
  }

  public List<Transition> getTransitions()
  {
    List<Transition> newTransitions = Collections.unmodifiableList(transitions);
    return newTransitions;
  }

  public int numberOfTransitions()
  {
    int number = transitions.size();
    return number;
  }

  public boolean hasTransitions()
  {
    boolean has = transitions.size() > 0;
    return has;
  }

  public int indexOfTransition(Transition aTransition)
  {
    int index = transitions.indexOf(aTransition);
    return index;
  }

  public static int minimumNumberOfStates()
  {
    return 0;
  }

  public boolean addState(State aState)
  {
    boolean wasAdded = false;
    if (states.contains(aState)) { return false; }
    states.add(aState);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeState(State aState)
  {
    boolean wasRemoved = false;
    if (states.contains(aState))
    {
      states.remove(aState);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addStateAt(State aState, int index)
  {  
    boolean wasAdded = false;
    if(addState(aState))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStates()) { index = numberOfStates() - 1; }
      states.remove(aState);
      states.add(index, aState);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveStateAt(State aState, int index)
  {
    boolean wasAdded = false;
    if(states.contains(aState))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfStates()) { index = numberOfStates() - 1; }
      states.remove(aState);
      states.add(index, aState);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addStateAt(aState, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfTransitions()
  {
    return 0;
  }

  public boolean addTransition(Transition aTransition)
  {
    boolean wasAdded = false;
    if (transitions.contains(aTransition)) { return false; }
    transitions.add(aTransition);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTransition(Transition aTransition)
  {
    boolean wasRemoved = false;
    if (transitions.contains(aTransition))
    {
      transitions.remove(aTransition);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addTransitionAt(Transition aTransition, int index)
  {  
    boolean wasAdded = false;
    if(addTransition(aTransition))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTransitions()) { index = numberOfTransitions() - 1; }
      transitions.remove(aTransition);
      transitions.add(index, aTransition);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTransitionAt(Transition aTransition, int index)
  {
    boolean wasAdded = false;
    if(transitions.contains(aTransition))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTransitions()) { index = numberOfTransitions() - 1; }
      transitions.remove(aTransition);
      transitions.add(index, aTransition);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTransitionAt(aTransition, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    states.clear();
    transitions.clear();
  }


  public String toString()
  {
	  String outputString = "";
    return super.toString() + "["+
            "packageName" + ":" + getPackageName()+ "," +
            "className" + ":" + getClassName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startState" + "=" + (getStartState() != null ? !getStartState().equals(this)  ? getStartState().toString().replaceAll("  ","    ") : "this" : "null")
     + outputString;
  }
}