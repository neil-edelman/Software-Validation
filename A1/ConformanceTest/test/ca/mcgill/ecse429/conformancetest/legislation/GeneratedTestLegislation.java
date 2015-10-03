/** Auto-generated by Nplus. */

package ca.mcgill.ecse429.conformancetest.legislation;

import org.junit.Test;
import org.junit.Assert;

import java.util.function.Predicate;

public class GeneratedTestLegislation {

	static final Predicate<Legislation> isStateinPreparation = (m) -> m.getState() == Legislation.State.inPreparation;
	static final Predicate<Legislation> isStateinHouseOfCommons = (m) -> m.getState() == Legislation.State.inHouseOfCommons;
	static final Predicate<Legislation> isStateinSenate = (m) -> m.getState() == Legislation.State.inSenate;
	static final Predicate<Legislation> isStatefinalized = (m) -> m.getState() == Legislation.State.finalized;

	@Test
	public void test() {
		Legislation test = new Legislation(/* assumes no input constructor is defined */);
		/* start->(start->inPreparation)->inPreparation */
		/* (unterminal) */
		/* (unvisited) */
		/* next: start->(start->inPreparation)->inPreparation */
	}

}
