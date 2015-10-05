/** Auto-generated by Nplus. */

package ca.mcgill.ecse429.conformancetest.legislation;

import org.junit.Test;
import org.junit.Assert;

import java.util.function.Predicate;

public class GeneratedTestLegislation {

	static final Predicate<Legislation> isStateinPreparation = (s) -> s.getState() == Legislation.State.inPreparation;
	static final Predicate<Legislation> isStateinHouseOfCommons = (s) -> s.getState() == Legislation.State.inHouseOfCommons;
	static final Predicate<Legislation> isStateinSenate = (s) -> s.getState() == Legislation.State.inSenate;
	static final Predicate<Legislation> isStatefinalized = (s) -> s.getState() == Legislation.State.finalized;

	static Legislation test;

	@Test
	public void TestPath1() {
		/* start ->@ctor-> inPreparation */
		test = new Legislation();
		Assert.assertTrue(isStateinPreparation.test(test));

		/* inPreparation ->introduceInHouse-> inHouseOfCommons */
		test.introduceInHouse();
		Assert.assertTrue(isStateinHouseOfCommons.test(test));

		/* inHouseOfCommons ->votePasses-> finalized */
		test.votePasses();
		Assert.assertTrue(isStatefinalized.test(test));

	}

	@Test
	public void TestPath2() {
		/* start ->@ctor-> inPreparation */
		test = new Legislation();
		Assert.assertTrue(isStateinPreparation.test(test));

		/* inPreparation ->introduceInHouse-> inHouseOfCommons */
		test.introduceInHouse();
		Assert.assertTrue(isStateinHouseOfCommons.test(test));

		/* inHouseOfCommons ->votePasses-> inSenate */
		test.votePasses();
		Assert.assertTrue(isStateinSenate.test(test));

		/* inSenate ->votePasses-> finalized */
		test.votePasses();
		Assert.assertTrue(isStatefinalized.test(test));

	}

	@Test
	public void TestPath3() {
		/* start ->@ctor-> inPreparation */
		test = new Legislation();
		Assert.assertTrue(isStateinPreparation.test(test));

		/* inPreparation ->introduceInHouse-> inHouseOfCommons */
		test.introduceInHouse();
		Assert.assertTrue(isStateinHouseOfCommons.test(test));

		/* inHouseOfCommons ->votePasses-> inSenate */
		test.votePasses();
		Assert.assertTrue(isStateinSenate.test(test));

		/* inSenate ->votePasses-> inHouseOfCommons */
		test.votePasses();
		Assert.assertTrue(isStateinHouseOfCommons.test(test));

	}

	@Test
	public void TestPath4() {
		/* start ->@ctor-> inPreparation */
		test = new Legislation();
		Assert.assertTrue(isStateinPreparation.test(test));

		/* inPreparation ->introduceInHouse-> inHouseOfCommons */
		test.introduceInHouse();
		Assert.assertTrue(isStateinHouseOfCommons.test(test));

		/* inHouseOfCommons ->votePasses-> inSenate */
		test.votePasses();
		Assert.assertTrue(isStateinSenate.test(test));

		/* inSenate ->voteFails-> inPreparation */
		test.voteFails();
		Assert.assertTrue(isStateinPreparation.test(test));

	}

	@Test
	public void TestPath5() {
		/* start ->@ctor-> inPreparation */
		test = new Legislation();
		Assert.assertTrue(isStateinPreparation.test(test));

		/* inPreparation ->introduceInHouse-> inHouseOfCommons */
		test.introduceInHouse();
		Assert.assertTrue(isStateinHouseOfCommons.test(test));

		/* inHouseOfCommons ->voteFails-> inPreparation */
		test.voteFails();
		Assert.assertTrue(isStateinPreparation.test(test));

	}

	@Test
	public void TestPath6() {
		/* start ->@ctor-> inPreparation */
		test = new Legislation();
		Assert.assertTrue(isStateinPreparation.test(test));

		/* inPreparation ->introduceInSenate-> inSenate */
		test.introduceInSenate();
		Assert.assertTrue(isStateinSenate.test(test));

	}

}
