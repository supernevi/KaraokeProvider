package com.github.supernevi.karaokeprovider.services;

import java.io.IOException;
import java.util.List;

import com.github.supernevi.karaokeprovider.entities.internal.MediaFileInfo;
import com.github.supernevi.karaokeprovider.entities.transfer.TOSongInfo;
import com.github.supernevi.karaokeprovider.enums.KaraokeMediaType;

public interface SongService {
	public List<TOSongInfo> getAllSongInfos();
	
	public MediaFileInfo getMediaFileInfo(String fileId, KaraokeMediaType mediaType);
	
	public byte[] readByteRange(MediaFileInfo fileInfo, long start, long end) throws IOException;
}
