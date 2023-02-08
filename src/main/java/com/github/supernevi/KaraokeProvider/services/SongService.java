package com.github.supernevi.KaraokeProvider.services;

import java.io.IOException;
import java.util.List;

import com.github.supernevi.KaraokeProvider.entities.internal.MediaFileInfo;
import com.github.supernevi.KaraokeProvider.entities.transfer.TOSongInfo;
import com.github.supernevi.KaraokeProvider.enums.KaraokeMediaType;

public interface SongService {
	public List<TOSongInfo> getAllSongInfos();
	
	public MediaFileInfo getMediaFileInfo(String fileId, KaraokeMediaType mediaType);
	
	public byte[] readByteRange(MediaFileInfo fileInfo, long start, long end) throws IOException;
}
