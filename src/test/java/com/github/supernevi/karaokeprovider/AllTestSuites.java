package com.github.supernevi.karaokeprovider;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

//@formatter:off
@Suite
@SelectClasses({
	ArchitecturalTest.class,
	AllDaoTestsSuite.class,
	AllServiceTestsSuite.class
})
//@formatter:on
public class AllTestSuites {

}
