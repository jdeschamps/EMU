package de.embl.rieslab.emu.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void testIsNumeric() {
		assertFalse(utils.isNumeric(null));
		assertFalse(utils.isNumeric(""));
		assertFalse(utils.isNumeric("d"));
		assertFalse(utils.isNumeric("+h485,12"));
		assertFalse(utils.isNumeric("+485,12d"));
		
		assertTrue(utils.isNumeric("156"));
		assertTrue(utils.isNumeric("+156"));
		assertTrue(utils.isNumeric("-156"));
		assertTrue(utils.isNumeric("846.1654"));
		assertTrue(utils.isNumeric("846,1654"));
		assertTrue(utils.isNumeric("+846.1654"));
		assertTrue(utils.isNumeric("+846,1654"));
		assertTrue(utils.isNumeric("-846.1654"));
		assertTrue(utils.isNumeric("-846,1654"));
	}
	
	@Test 
	public void testIsInteger() {
		assertFalse(utils.isInteger(null));
		assertFalse(utils.isInteger(""));
		assertFalse(utils.isInteger("d"));
		assertFalse(utils.isInteger("15.46"));
		assertFalse(utils.isInteger("-15.46"));
		assertFalse(utils.isInteger("+15,46"));
		assertFalse(utils.isInteger("-15,46"));

		assertTrue(utils.isInteger("-6541"));
		assertTrue(utils.isInteger("+6541"));
		assertTrue(utils.isInteger("4581"));
	}
	
	// here should maybe change the method to accept ","
	@Test
	public void testIsFloat() {
		assertFalse(utils.isFloat(null));
		assertFalse(utils.isFloat(""));
		assertFalse(utils.isFloat("d"));
		assertFalse(utils.isFloat("+h485,12"));
		assertFalse(utils.isFloat("+485,12d"));
		
		assertTrue(utils.isFloat("156"));
		assertTrue(utils.isFloat("+156"));
		assertTrue(utils.isFloat("-156"));
		assertTrue(utils.isFloat("846.1654"));
		//assertTrue(utils.isFloat("846,165")); 
		assertTrue(utils.isFloat("+846.1654"));
		//assertTrue(utils.isFloat("+846,1654"));
		assertTrue(utils.isFloat("-846.1654"));
		//assertTrue(utils.isFloat("-846,1654"));
	}

	@Test
	public void testRound() {
		double v = 15.123456789;
		assertEquals(15, utils.round(v, 0), 1E-20);
		assertEquals(15.1, utils.round(v, 1), 1E-20);
		assertEquals(15.12, utils.round(v, 2), 1E-20);
		assertEquals(15.123, utils.round(v, 3), 1E-20);
		assertEquals(15.1235, utils.round(v, 4), 1E-20);
		assertEquals(15.12346, utils.round(v, 5), 1E-20);
	}
}
