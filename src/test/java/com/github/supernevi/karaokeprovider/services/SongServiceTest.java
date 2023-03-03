package com.github.supernevi.karaokeprovider.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.github.supernevi.karaokeprovider.dao.SongDao;
import com.github.supernevi.karaokeprovider.entities.BOSongInfo;
import com.github.supernevi.karaokeprovider.entities.internal.MediaFileInfo;
import com.github.supernevi.karaokeprovider.entities.transfer.TOSongInfo;
import com.github.supernevi.karaokeprovider.enums.KaraokeMediaType;

public class SongServiceTest {
	
	@Mock
	private SongDao songDao;
	
	@InjectMocks
	private final SongService songervice = new SongServiceImpl();
	
	@BeforeEach
	public void install() {
		MockitoAnnotations.openMocks(this);
		
		List<BOSongInfo> songInfos = new ArrayList<>();
		songInfos.add(BOSongInfo.builder().fileId("1").build());
		songInfos.add(BOSongInfo.builder().fileId("2").build());
		
		when(songDao.getAllSongs()).thenReturn(songInfos);
		
		when(songDao.getSongInfo(nullable(String.class))).thenAnswer(new Answer<BOSongInfo>() {
			@Override
			public BOSongInfo answer(InvocationOnMock invocation) throws Throwable {
				String fileId = invocation.getArgument(0, String.class);
				return songInfos.stream().filter(s -> Objects.equals(s.getFileId(), fileId)).findAny().orElse(null);
			}
		});
	}
	
	@Test
	void testGetAllSongInfos() {
		List<TOSongInfo> resultList = songervice.getAllSongInfos();
		assertNotNull(resultList);
		assertEquals(2, resultList.size());
		
		Iterator<TOSongInfo> it = resultList.iterator();
		
		TOSongInfo result = it.next();
		assertNotNull(result);
		assertEquals("1", result.getSongId());
		
		result = it.next();
		assertNotNull(result);
		assertEquals("2", result.getSongId());
	}
	
	@Test
	void testGetMediaFile_null_null__null() {
		MediaFileInfo result = songervice.getMediaFileInfo(null, null);
		assertNull(result);
	}
	
	@Test
	void testGetMediaFile_empty_null__null() {
		MediaFileInfo result = songervice.getMediaFileInfo("", null);
		assertNull(result);
	}
	
	@Test
	void testGetMediaFile_1337_null__null() {
		MediaFileInfo result = songervice.getMediaFileInfo("1337", null);
		assertNull(result);
	}
	
	@Test
	void testGetMediaFile_1_null__null() {
		MediaFileInfo result = songervice.getMediaFileInfo("1", null);
		assertNull(result);
	}
	
	@Test
	void testGetMediaFile_1_TEXT__null() {
		MediaFileInfo result = songervice.getMediaFileInfo("1", KaraokeMediaType.TEXT);
		assertNull(result);
	}
}
