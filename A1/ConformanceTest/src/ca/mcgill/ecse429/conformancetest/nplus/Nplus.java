/** Copyright 2015 Neil Edelman, distributed under the terms of the GNU General
 Public License, see copying.txt */

package ca.mcgill.ecse429.conformancetest.nplus;

import ca.mcgill.ecse429.conformancetest.statemodel.persistence.PersistenceStateMachine;
import ca.mcgill.ecse429.conformancetest.statemodel.State;
import ca.mcgill.ecse429.conformancetest.statemodel.StateMachine;
import ca.mcgill.ecse429.conformancetest.statemodel.Transition;

import java.util.Iterator;

import java.io.File;

/** This automates generation of N+ test strategy given an XML file in a
 specific form representing code and the code itself. It does not generate sneak
 path test cases. (it would be really easy to do it?)

 @author	Neil
 @version	1.0, 2015-09
 @since		1.0, 2015-09 */
class Nplus {

	static final int EXIT_FAILURE = 1;

	public static void main(final String args[]) {

		/* read and manipulate args */

		if(args.length != 2) {
			System.err.printf("Wrong number of args: <xml> <path to file>.\n");
			System.exit(EXIT_FAILURE);
		}

		System.err.printf("Nplus <%s>, <%s>\n", args[0], args[1]);

		String xml   = args[0];
		String basic = basicFilename(args[0]);
		String pckg  = pathToPackage(args[1]);
		String clas  = extractClass(args[1]);

		if(clas == null) {
			System.err.printf("<filename> must be a valid java file.\n");
			System.exit(EXIT_FAILURE);
		}

		System.err.printf("basic:<%s>, pckg:<%s>, clas:<%s>.\n", basic, pckg, clas);
		System.err.printf("Appempting to read StateMachine from <%s>.\n", xml);

		/* read xml; why doesn't it throw something?; this is so sketch . . . */
		StateMachine sm = PersistenceStateMachine.loadStateMachine(xml);

		System.err.printf("Package: %s\n", sm.getPackageName());
		System.err.printf("Class: %s\n", sm.getClassName());

		//System.err.printf("Info xml file %s -> %s.\n", args[0], basic);
		//System.out.printf("package ca.mcgill.ecse429.conformancetest.%s;\n\n", basic);
		if(pckg != null) System.out.printf("package %s;\n\n", pckg);
		System.out.printf("import org.junit.Test;\n\n");
		System.out.printf("import org.junit.Assert;\n\n");
		System.out.printf("public class GeneratedTest%s {\n", clas);
		System.out.printf("\t@Test\n");
		System.out.printf("\tpublic void one() {\n");
		System.out.printf("\t}\n");
		System.out.printf("}\n", clas);
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

	/** Constructor.
	@param ex Something. */
	public Nplus(final String xmlName, final String sourcePath) {
	}

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
	public String toString() {
		return "Hi";
	}

}
