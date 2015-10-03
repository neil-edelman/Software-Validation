package ca.mcgill.ecse429.conformancetest.statemodel;

public class State
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //State Attributes
  private String name;
	
	private enum Visit { UNVISITED, VISITED };

	private Visit visit = Visit.UNVISITED;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public State(String aName)
  {
    name = aName;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public void delete()
  {}


  public String toString()
  {
	  String outputString = "";
    return super.toString() + "["+
            "name" + ":" + getName()+ "]"
     + outputString;
  }
	
	public boolean isVisited() {
		return visit == Visit.VISITED;
	}
	
	public void setVisited() {
		visit = Visit.VISITED;
	}
}
