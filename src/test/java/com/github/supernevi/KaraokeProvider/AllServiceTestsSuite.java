package com.github.supernevi.KaraokeProvider;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.github.supernevi.KaraokeProvider.services.SongServiceTest;
import com.github.supernevi.KaraokeProvider.utilities.RuntimeParameterUtilitiesTest;

//@formatter:off
@Suite
@SelectClasses({
	SongServiceTest.class,
	RuntimeParameterUtilitiesTest.class
})
//@formatter:on
public class AllServiceTestsSuite {

}
