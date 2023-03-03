package com.github.supernevi.karaokeprovider.utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RuntimeParameterUtilitiesTest {
	
	@Test
	public void testGetSongStorePath_defaultValue() {
		String result = RuntimeParameterUtilities.getSongStorePath();
		assertEquals("/static/songs", result);
	}
}
