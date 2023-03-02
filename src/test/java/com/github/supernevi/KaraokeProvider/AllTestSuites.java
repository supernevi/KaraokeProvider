package com.github.supernevi.KaraokeProvider;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

//@formatter:off
@Suite
@SelectClasses({
	AllDaoTestsSuite.class,
	AllServiceTestsSuite.class
})
//@formatter:on
public class AllTestSuites {

}
