package com.github.supernevi.KaraokeProvider;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

//@formatter:off
@Suite
@SelectClasses({
	AllDaoTests.class,
	AllServiceTests.class
})
//@formatter:on
public class AllTests {

}
