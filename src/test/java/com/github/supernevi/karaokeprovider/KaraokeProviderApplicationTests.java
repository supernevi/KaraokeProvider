package com.github.supernevi.karaokeprovider;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.supernevi.karaokeprovider.services.SongService;

@SpringBootTest
class KaraokeProviderApplicationTests {

	@Autowired
	private SongService songService;
	
	@Test
	void contextLoads() {
		assertNotNull(songService);
	}
}
