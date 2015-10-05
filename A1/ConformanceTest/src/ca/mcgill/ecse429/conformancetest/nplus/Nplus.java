/** Copyright 2015 Neil Edelman, distributed under the terms of the GNU General
 Public License, see copying.txt */

package ca.mcgill.ecse429.conformancetest.nplus;

import ca.mcgill.ecse429.conformancetest.statemodel.persistence.PersistenceStateMachine;
import ca.mcgill.ecse429.conformancetest.statemodel.State;
import ca.mcgill.ecse429.conformancetest.statemodel.StateMachine;
import ca.mcgill.ecse429.conformancetest.statemodel.Transition;

import java.util.function.Predicate;
import java.util.Stack;

import java.util.Iterator;
import java.io.File;

/** This automates generation of N+ test strategy given an XML file in a
 specific form representing code and the code itself. It does not generate sneak
 path test cases. (it would be really easy to do it?)

 @author	Neil
 @version	1.0, 2015-09
 @since		1.0, 2015-09 */
class Nplus /* extends PersistenceStateMachine*/ {

	static final int EXIT_FAILURE = 1;

	static StateMachine sm;

	public static void main(final String args[]) {

		/* read and manipulate args */

		if(args.length != 1) {
			System.err.printf("Wrong number of args: <xml>.\n");
			System.exit(EXIT_FAILURE);
		}

		System.err.printf("Nplus <%s>\n", args[0]);

		String xml = args[0];

		/* read xml; why doesn't it throw something?; this is so sketch . . . */
		System.err.printf("Appempting to read StateMachine from <%s>.\n", xml);

		sm = PersistenceStateMachine.loadStateMachine(xml);

		Depth(sm, null);
		/* do a bunch of dfs paths until coverage is complete */
		/*State state, nextState, nextTemp;
		boolean isLookingForUnvisited;
		while((state = sm.getStartState()).isTerminal() != true) {

			/ each path corresponds to a test /
			System.out.printf("\t@Test\n");
			System.out.printf("\tpublic void test() {\n");
			System.out.printf("\t\t%s test = new %s(/ assumes no input constructor is defined /);\n", targetClass, targetClass);

			/ a single dfs path /
			for( ; ; ) {

				if(state == null) {
					System.out.printf("/state == null!!!! :[/\n");
					break;
				}
				/ assert this node /
				if(isValidState(state)) System.out.printf("\t\tAssert.assertTrue(isState%s.test(test));\n", state.getName());
				state.setVisited();

				/ compile a list of unvisited states from the current state /
				int unvisited = 0, unterminal = 0;
				for(Transition t : state.getOut()) {
					nextTemp = t.getTo();
					if(nextTemp == state || nextTemp.isTerminal()) continue;
					System.out.printf("\t\t/ %s->%s->%s /\n", state, t, nextTemp);
					unterminal++;
					System.out.printf("\t\t/ (unterminal) /\n");
					if(nextTemp.isVisited()) continue;
					unvisited++;
					System.out.printf("\t\t/ (unvisited) /\n");
				}

				if(unterminal == 0) {
					/ leaf /
					state.setTerminal();
					System.out.printf("\t\t/ %s set to terminal /\n");
					break;
				} else if(unvisited == 0) {
					isLookingForUnvisited = false;
				} else {
					isLookingForUnvisited = true;
				}

				/ nextState /
				nextState = null;
				for(Transition t : state.getOut()) {
					nextTemp = t.getTo();
					if(nextTemp == state || nextTemp.isTerminal()) continue;
					if(isLookingForUnvisited && nextTemp.isVisited()) continue;
					System.out.printf("\t\t/ next: %s->%s->%s /\n", state, t, nextTemp);
					nextState = nextTemp;
					break;
				}
				assert(nextState != null);
				state = nextState;
				break;
			}
			System.out.printf("\t}\n\n");
			break;
		}*/

		/* footer */
		System.out.printf("}\n");

	}

	/* /dir/dir/dir/foo.bar -> foo */
	private static String basicFilename(final String filename) {
		int begin = filename.indexOf(File.separatorChar) + 1;
		int end   = filename.indexOf('.', begin);
		return (end == -1) ? filename.substring(begin) : filename.substring(begin, end);
	}

	/** /dir/dir/dir/foo.bar -> dir.dir.dir
	 @fixme	This is really delicate. */
	private static String pathToPackage(final String filename) {
		int last  = filename.lastIndexOf(File.separatorChar);
		if(last == -1) return null;
		int first = filename.indexOf(File.separatorChar);
		if(first == -1 || first == last) return null;
		return filename.substring(first + 1, last).replace(File.separatorChar, '.');
	}

	/** /dir/dir/dir/foo.bar -> foo.bar
	 @fixme	This is really delicate. */
	private static String extractFilename(final String filename) {
		int last = filename.lastIndexOf(File.separatorChar) + 1;
		return filename.substring(last);
	}

	/** /dir/dir/dir/foo.bar -> foo
	 @fixme	This is really delicate. */
	private static String extractClass(final String filename) {
		String fn = extractFilename(filename);
		int last = fn.lastIndexOf('.');
		if(last == -1) return null;
		return fn.substring(0, last);
	}

	/* 'start' state doesn't have an entry in the enum State */
	private static boolean isValidState(final State s) {
		return (s == null || "start".compareTo(s.getName()) == 0) ? false : true;
	}

	/* this will not generate the most efficient paths :[
	 also, only call it once, because it doesn't clear the flags
	 @param predicate	The predicate you wish to test. */
	static void Depth(StateMachine sm, Predicate<State> predicate) {
		/* dfs */
		Stack<State> dfs = new Stack<State>();
		State node, nextNode, n;
		boolean isLeaf;
		/* printing */
		Stack<Transition> printing = new Stack<Transition>();
		Transition t;
		int testCase = 1;
		String targetClass = basicFilename(sm.getClassName());
		String testClass   = String.format("GeneratedTest%s", targetClass);

		/* printing: create the header */
		System.out.printf("/** Auto-generated by Nplus. */\n\n");
		if(sm.getPackageName().length() > 0) {
			System.out.printf("package %s;\n\n", sm.getPackageName());
		}
		System.out.printf("import org.junit.Test;\n");
		System.out.printf("import org.junit.Assert;\n\n");
		System.out.printf("import java.util.function.Predicate;\n\n");
		System.out.printf("public class %s {\n\n", testClass);
		for(State s : sm.getStates()) {
			if(!isValidState(s)) continue;
			System.out.printf("\tstatic final Predicate<%s> isState%s = (m) -> m.getState() == %s.State.%s;\n", targetClass, s.getName(), targetClass, s.getName());
		}
		System.out.printf("\n");
		System.out.printf("\tstatic %s test;\n\n", targetClass);

		/* dfs: start with the start */
		assert sm.getStartState() != null;
		dfs.push(sm.getStartState());

		while(!dfs.isEmpty()) {

			/* debug */
			/*System.out.print("/ * ");
			for(State s : dfs) System.out.printf("%s, ", s);
			System.out.print(" * /\n");*/

			/* dfs */
			node = dfs.pop();
			node.setVisited();

			System.err.printf("**Got here 1.\n");
			isLeaf = true;
			for(Transition edge : node.getOut()) {
				if((nextNode = edge.getTo()).isVisited()) continue;
				isLeaf = false;
				nextNode.setPredicessor(edge);
				dfs.push(nextNode);
			}
			System.err.printf("**Got here 2.\n");

			/* printing */

			if(isLeaf == false) continue;

			/* this a leaf, output a test case */

			System.out.printf("\t@Test\n");
			System.out.printf("\tpublic void TestPath%d() {\n", testCase++);
			System.out.printf("\t\t/* make a new test class; assumes no-arg con'r is good;\n");
			System.out.printf("\t\t assumes con'r is the first thing called */\n");
			System.out.printf("\t\ttest = new %s();\n\n", targetClass);

			/* print them out in forward-order from the ternimal node */
			/*System.out.printf("\t\t/ *\n");
			for(Transition edge = node.getPredicessor(); edge != null; ) {
				System.out.printf("\t\t%s\n", edge);
				n = edge.getFrom();
				assert n != null;
				if((edge = n.getPredicessor()) == null) break;
			}
			System.out.printf("\t\t* /\n");*/
			/* print them out in reverse order */
			printing.clear();
			n = node;
			while(true) {
				if((t = n.getPredicessor()) == null) break;
				printing.push(t);
				n = t.getFrom();
				assert(n != null);
			}
			while(!printing.empty()) {
				t = printing.pop();
				System.out.printf("\t\t/* %s ->%s-> %s */\n", t.getFrom(), t.getEvent(), t.getTo());
				// getEvent, getCondition, getAction
				/*if(isValidState(s)) System.out.printf("\t\tAssert.assertTrue(isState%s.test(test));\n", s.getName());*/
			}

			System.out.printf("\t}\n\n");
			
			/* fixme: then backtrack! */
		}

	}
	
	/** Constructor.
	@param ex Something. */
	/*public Nplus() {
	}*/

	/** Javadocs {@link URL}.
	 <p>
	 More.
	 
	 @param p			Thing.
	 @return			Thing.
	 @throws Exception	Thing.
	 @see				package.Class#method(Type)
	 @see				#field
	 @since				1.0
	 @deprecated		Ohnoz. */

	/** @return A synecdochical {@link String}. */
	/*public String toString() {
		return "Hi";
	}*/

}
