package com.github.supernevi.karaokeprovider;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.github.supernevi.karaokeprovider.services.SongServiceTest;
import com.github.supernevi.karaokeprovider.utilities.RuntimeParameterUtilitiesTest;

//@formatter:off
@Suite
@SelectClasses({
	SongServiceTest.class,
	RuntimeParameterUtilitiesTest.class
})
//@formatter:on
public class AllServiceTestsSuite {

}
