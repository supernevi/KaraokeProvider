package com.github.supernevi.karaokeprovider.daos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Collection;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.github.supernevi.karaokeprovider.entities.BOSongInfo;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class SongDaoTest {
	
	private final SongDao songDao = new SongDaoImpl();
	
	private static String lastSavedId;
	
	@Test
	void testA01GetAllSongs_beforeInsert__empty() {
		Collection<BOSongInfo> resultList = songDao.getAllSongs();
		assertNotNull(resultList);
		
		assertEquals(0, resultList.size());
	}
	
	@Test
	void testB01SaveSongInfo_null() {
		BOSongInfo entity = null;
		songDao.saveSongInfo(entity);
		assertEquals(null, entity);
	}
	
	@Test
	void testB02SaveSongInfo_empty() {
		BOSongInfo entity = new BOSongInfo();
		songDao.saveSongInfo(entity);
		assertNotNull(entity);
		assertNotNull(entity.getFileId());
		
		lastSavedId = entity.getFileId();
	}
	
	@Test
	void testC01GetSongInfo_null__null() {
		BOSongInfo result = songDao.getSongInfo(null);
		assertNull(result);
	}
	
	@Test
	void testC02GetSongInfo_empty__null() {
		BOSongInfo result = songDao.getSongInfo("");
		assertNull(result);
	}
	
	@Test
	void testC03GetSongInfo_IDoNotExist__null() {
		BOSongInfo result = songDao.getSongInfo("IDoNotExist");
		assertNull(result);
	}
	
	@Test
	void testC03GetSongInfo_existingId__filledObject() {
		BOSongInfo result = songDao.getSongInfo(lastSavedId);
		assertNotNull(result);
		assertEquals(lastSavedId, result.getFileId());
	}
}
