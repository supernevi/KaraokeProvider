package com.github.supernevi.karaokeprovider.daos;

import java.util.Collection;

import com.github.supernevi.karaokeprovider.entities.BOSongInfo;

public interface SongDao {	
	public Collection<BOSongInfo> getAllSongs();
	
	public BOSongInfo getSongInfo(String fileId);
	public void saveSongInfo(BOSongInfo songInfo);
}
