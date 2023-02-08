package com.github.supernevi.KaraokeProvider.dao;

import java.util.Collection;

import com.github.supernevi.KaraokeProvider.entities.BOSongInfo;

public interface SongDao {	
	public Collection<BOSongInfo> getAllSongs();
	
	public BOSongInfo getSongInfo(String fileId);
	public void saveSongInfo(BOSongInfo songInfo);
}
