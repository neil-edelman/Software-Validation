/** Copyright 2015 Neil Edelman, distributed under the terms of the GNU General
 Public License, see copying.txt */

package ca.mcgill.ecse429.conformancetest.nplus;

import ca.mcgill.ecse429.conformancetest.statemodel.persistence.PersistenceStateMachine;
import ca.mcgill.ecse429.conformancetest.statemodel.State;
import ca.mcgill.ecse429.conformancetest.statemodel.StateMachine;
import ca.mcgill.ecse429.conformancetest.statemodel.Transition;

import java.util.function.Predicate;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.Stack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.NumberFormatException;

import java.util.List;
import java.util.ArrayList;

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

	private enum Type {
		INT  ("integer", (a) -> "int " + a,     (a, v) -> "int " + a + " = " + v ),
		BOOL ("boolean", (a) -> "boolean " + a, (a, v) -> "boolean " + a + " = " + v );
		private final String                  typeName;
		private final Function<String,String> declare;
		private final BiFunction<String,String,String> declareValue;
		private Type(final String typeName, final Function<String,String> declare, final BiFunction<String,String,String> declareValue) {
			this.typeName     = typeName;
			this.declare      = declare;
			this.declareValue = declareValue;
		}
		public String toString() {
			return typeName;
		}
		private String declare(final String a) {
			return declare.apply(a);
		}
		private String declare(final String a, final String b) {
			return declareValue.apply(a, b);
		}
	}

	final static private class Variable {
		String name;
		String value;
		Type   type;
		Variable(final String var, final String val) {
			name  = var;
			value = val;
			try {
				Integer.parseInt(val);
				type = Type.INT;
			} catch(NumberFormatException e) {
				type = Type.BOOL;
			}
		}
		public String toString() {
			return name;
		}
		String declare() {
			return type.declare(name, value);
		}
	}

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
		final Pattern decl = Pattern.compile("\\s*(\\w*)\\s*(=\\s*(\\w*)\\s*)?;");
		final Pattern test = Pattern.compile("\\s*([^;]*);");
		Matcher matcher;
		/* dfs */
		Stack<Transition> dfs = new Stack<Transition>();
		Transition edge;
		State node, nextNode, n;
		boolean isLeaf;
		/* printing */
		Stack<Transition> toStart = new Stack<Transition>();
		Transition t;
		int testCase = 1;
		String targetClass = basicFilename(sm.getClassName());
		String testClass   = String.format("GeneratedTest%s", targetClass);
		List<Variable> vars = new ArrayList<Variable>();
		String event, action, actions[], cond;

		/* printing: create the header */
		System.out.printf("/** Auto-generated by Nplus. */\n\n");
		if(sm.getPackageName().length() > 0) {
			System.out.printf("package %s;\n\n", sm.getPackageName());
		}
		System.out.printf("import org.junit.Test;\n");
		System.out.printf("import org.junit.Assert;\n\n");
		System.out.printf("import java.util.function.Predicate;\n\n");
		System.out.printf("public class %s {\n\n", testClass);
		System.out.printf("\tstatic %s test;\n\n", targetClass);

		/* dfs: start with the start (fixme: these asserts do nothing?) */
		assert sm.getStartState() != null;
		assert sm.getStartState().getOut().size() == 1;
		edge = sm.getStartState().getOut().get(0);
		assert edge.getEvent().compareTo("@ctor") == 0;
		dfs.push(edge);

		/* variable names; supports only int and boolean;
		 very delicate, expects "foo[ = 0]; bar[ = false];" and all the args are
		 given */
		action = edge.getAction();
		System.out.printf("\t/* taken from con'r: %s */\n", action);
		matcher = decl.matcher(action);
		while(matcher.find()) {
			String var = matcher.group(1), val = "0";
			if(matcher.groupCount() == 3) val = matcher.group(3);
			vars.add(new Variable(var, val));
		}

		/* declare vars */
		for(Variable v : vars) System.out.printf("\t%s;\n", v.declare());
		System.out.printf("\n");

		/* predicates to streamline changes */
		System.out.printf("\t/* change this if you must */\n");
		for(State s : sm.getStates()) {
			if(!isValidState(s)) continue;
			System.out.printf("\tstatic final Predicate<%s> isState%s = (s) -> s.getState() == %s.State.%s;\n", targetClass, s.getName(), targetClass, s.getName());
		}
		System.out.printf("\n");

		while(!dfs.isEmpty()) {

			/* dfs */
			edge = dfs.pop();
			edge.setVisited();

			/* build up a Transition path to the start node, and isLeaf */
			isLeaf = false;
			toStart.clear();
			for(Transition e = edge; e != null; e = e.getPredicessor()) {
				toStart.push(e);
				/* you can call more functions if the path is different;
				 this is not true, but it's what we were shown in class */
				if(e.getFrom() == edge.getTo()) isLeaf = true;
			}

			/* continue piling stuff on the dfs */
			if(!isLeaf) {
				isLeaf = true;
				for(Transition e : edge.getTo().getOut()) {
					if(e.isVisited()) continue;
					isLeaf = false; /* this is less relaxed */
					e.setPredicessor(edge);
					dfs.push(e);
				}
			}

			if(!isLeaf) continue;

			/* this a leaf, output a test case */
			System.out.printf("\t@Test\n");
			System.out.printf("\tpublic void TestPath%d() {\n", testCase++);

			/* print them out in reverse order */
			while(!toStart.empty()) {
				t      = toStart.pop();
				node   = t.getTo();
				event  = t.getEvent();
				action = t.getAction();
				cond   = t.getCondition();
				/*System.out.printf("\t\t/ * %s ->%s:\"%s\"-> %s * /\n", t.getFrom(), event, cond, node);*/
				System.out.printf("\t\t/* %s ->%s-> %s */\n", t.getFrom(), event, node);
				if(cond.length() > 0) {
					System.out.printf("\t\t/* FIXME?: %s */\n", cond);
				}
				// getEvent, getCondition, getAction
				if("@ctor".compareTo(event) == 0) {
					System.out.printf("\t\ttest = new %s();\n", targetClass);
				} else {
					System.out.printf("\t\ttest.%s();\n", event);
				}

				/* tests */
				if(!isValidState(node)) continue;
				System.out.printf("\t\tAssert.assertTrue(isState%s.test(test));\n", node.getName());
				if(action.length() == 0) continue;
				matcher = test.matcher(action);
				while(matcher.find()) {
					System.out.printf("\t\tAssert.assertTrue(test.%s);\n", getFunc(matcher.group(1)));
				}
				System.out.printf("\t\t%s\n", action);
				//if(isValidState(node)) System.out.printf("\t\tAssert.assertTrue(isState%s.test(test));\n", node.getName());
				
				//actions = action.split(";");
				System.out.print("\n");
			}

			System.out.printf("\t}\n\n");

		}

	}

	/** just a guess */
	private static String getFunc(final String name) {
		String out = name.trim();
		out = "get" + out.substring(0, 1).toUpperCase() + out.substring(1);
		String s[] = out.split("(?=\\W)", 2);
		s[0] += "()";
		if(s.length != 2) return s[0];
		return s[0] + s[1].replaceFirst("=", "==");
	}

}
