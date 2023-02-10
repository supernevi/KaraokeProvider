package com.github.supernevi.KaraokeProvider;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.github.supernevi.KaraokeProvider.services.SongServiceTest;

//@formatter:off
@Suite
@SelectClasses({
	SongServiceTest.class
})
//@formatter:on
public class AllServiceTests {

}
