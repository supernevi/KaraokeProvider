package com.github.supernevi.KaraokeProvider;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.github.supernevi.KaraokeProvider.dao.SongDaoTest;

//@formatter:off
@Suite
@SelectClasses({
	SongDaoTest.class
})
//@formatter:on
public class AllDaoTests {

}
