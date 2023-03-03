package com.github.supernevi.karaokeprovider.dao;

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
	
	private SongDao songDao = new SongDaoImpl();
	
	private static String lastSavedId;
	
	@Test
	public void testA01GetAllSongs_beforeInsert__empty() {
		Collection<BOSongInfo> resultList = songDao.getAllSongs();
		assertNotNull(resultList);
		
		assertEquals(0, resultList.size());
	}
	
	@Test
	public void testB01SaveSongInfo_null() {
		BOSongInfo entity = null;
		songDao.saveSongInfo(entity);
		assertEquals(null, entity);
	}
	
	@Test
	public void testB02SaveSongInfo_empty() {
		BOSongInfo entity = new BOSongInfo();
		songDao.saveSongInfo(entity);
		assertNotNull(entity);
		assertNotNull(entity.getFileId());
		
		lastSavedId = entity.getFileId();
	}
	
	@Test
	public void testC01GetSongInfo_null__null() {
		BOSongInfo result = songDao.getSongInfo(null);
		assertNull(result);
	}
	
	@Test
	public void testC02GetSongInfo_empty__null() {
		BOSongInfo result = songDao.getSongInfo("");
		assertNull(result);
	}
	
	@Test
	public void testC03GetSongInfo_IDoNotExist__null() {
		BOSongInfo result = songDao.getSongInfo("IDoNotExist");
		assertNull(result);
	}
	
	@Test
	public void testC03GetSongInfo_existingId__filledObject() {
		BOSongInfo result = songDao.getSongInfo(lastSavedId);
		assertNotNull(result);
		assertEquals(lastSavedId, result.getFileId());
	}
}
