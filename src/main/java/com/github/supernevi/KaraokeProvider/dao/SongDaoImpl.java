package com.github.supernevi.KaraokeProvider.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.github.supernevi.KaraokeProvider.entities.BOSongInfo;

@Repository
public class SongDaoImpl implements SongDao {

	private static final Map<String, BOSongInfo> store = new HashMap<>();
	
	@Override
	public Collection<BOSongInfo> getAllSongs() {
		return store.values();
	}
	
	@Override
	public BOSongInfo getSongInfo(String fileId) {
		return store.get(fileId);
	}

	@Override
	public void saveSongInfo(BOSongInfo songInfo) {
		String id = generateId(songInfo);
		songInfo.setFileId(id);
		store.put(id, songInfo);
	}
	
	private String generateId(BOSongInfo songInfo) {
		UUID uniqueKey = UUID.randomUUID();
		return uniqueKey.toString();
	}
}
