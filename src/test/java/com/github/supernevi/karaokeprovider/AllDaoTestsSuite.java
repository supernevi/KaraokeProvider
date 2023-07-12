package com.github.supernevi.karaokeprovider;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.github.supernevi.karaokeprovider.daos.SongDaoTest;

//@formatter:off
@Suite
@SelectClasses({
	SongDaoTest.class
})
//@formatter:on
public class AllDaoTestsSuite {

}
