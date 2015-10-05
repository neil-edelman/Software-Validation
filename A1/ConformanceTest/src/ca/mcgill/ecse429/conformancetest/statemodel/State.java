package ca.mcgill.ecse429.conformancetest.statemodel;

import java.util.ArrayList;
import java.util.List;

public class State
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //State Attributes
  private String name;

	private boolean isVisited  = false;
	private boolean isFinished = false;

	private ArrayList<Transition> out;// = new ArrayList<Transition>();
	// something's very fishy
	
	private Transition predicessor;

  //------------------------
  // CONSTRUCTOR
  //------------------------
	/* Neil: never gets called??? */

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
    wasSet = true; // that's useless
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
	/*  String outputString = "";
    return super.toString() + "["+
            "name" + ":" + getName()+ "]"
     + outputString;*/
	//  return name + "[" + isVisited + "]";
	  return name;
  }
	
	public void addOut(final Transition transition) {
		if(out == null) out = new ArrayList<Transition>();
		out.add(transition);
		//System.err.printf("****addOut: Added %s to %s.\n", transition, this);
	}
	
	public List<Transition> getOut() {
		return out;
	}
	
	public boolean isVisited() {
		return isVisited;
	}
	
	public void setVisited() {
		isVisited = true;
	}

	public boolean getPredicessor() {
		return predicessor;
	}

	public void setPredicessor(final Predicessor p) {
		predicessor = p;
	}
}
