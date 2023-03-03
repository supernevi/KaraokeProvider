package com.github.supernevi.karaokeprovider.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.supernevi.karaokeprovider.dao.SongDao;
import com.github.supernevi.karaokeprovider.entities.BOSongInfo;
import com.github.supernevi.karaokeprovider.entities.internal.MediaFileInfo;
import com.github.supernevi.karaokeprovider.entities.transfer.TOSongInfo;
import com.github.supernevi.karaokeprovider.enums.KaraokeMediaType;
import com.github.supernevi.karaokeprovider.utilities.RuntimeParameterUtilities;

import io.micrometer.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SongServiceImpl implements SongService {
	
	public static final int BYTE_RANGE = 128; // increase the byterange from here
		
	@Autowired
	private SongDao songDao;
	
	@Override
	public List<TOSongInfo> getAllSongInfos() {
		List<TOSongInfo> songs = new ArrayList<>();

		for(BOSongInfo song : songDao.getAllSongs()) {
			TOSongInfo convertedSongInfo = convertToTOSongInfo(song);
			songs.add(convertedSongInfo);
		}
		
		return songs;
	}

	@Override
	public MediaFileInfo getMediaFileInfo(String fileId, KaraokeMediaType mediaType) {
		BOSongInfo songInfo = songDao.getSongInfo(fileId);
		if(songInfo == null) {
			return null;
		}
		
		Path absolutePath = getAbsoluteFilePath(songInfo, mediaType);
		if(absolutePath == null) {
			return null;
		}
		
		File mediaFile = absolutePath.toFile();
		if(!mediaFile.exists()) {
			return null;
		}
		
		MediaFileInfo fileInfo = new MediaFileInfo();

		String fileName = absolutePath.getFileName().toString();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		
		fileInfo.setFileSize(sizeFromFile(absolutePath));
		fileInfo.setFileName(fileName);
		fileInfo.setAbsolutePath(absolutePath.toString());
		fileInfo.setFileType(fileType);
		
		return fileInfo;
	}

	@Override
	public byte[] readByteRange(MediaFileInfo fileInfo, long start, long end) throws IOException {
		if(start > Integer.MAX_VALUE || end > Integer.MAX_VALUE) {
			log.error("Only files lower than 2GB are supported: {}", fileInfo); // because array index can only be in integer value...
			return new byte[0];
		}
		
		Path path = Paths.get(fileInfo.getAbsolutePath());
		try (InputStream inputStream = (Files.newInputStream(path));
				ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
			byte[] data = new byte[BYTE_RANGE];
			int nRead;
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				bufferedOutputStream.write(data, 0, nRead);
			}
			bufferedOutputStream.flush();
			byte[] result = new byte[(int)(end - start) + 1];
			System.arraycopy(bufferedOutputStream.toByteArray(), (int)start, result, 0, result.length);
			return result;
		}
	}
	

	private Path getAbsoluteFilePath(BOSongInfo songInfo, KaraokeMediaType mediaType) {
		if(mediaType == null) {
			return null;
		}
		
		String filePathString = switch(mediaType) {
			case VIDEO -> songInfo.getVideoFileName();
			case AUDIO -> songInfo.getAudioFileName();
			case TEXT -> songInfo.getTextFileName();
			case COVER -> songInfo.getCoverFileName();
			case BACKGROUND -> songInfo.getBackgroundFileName();
		};
		
		if(StringUtils.isBlank(filePathString)) {
			return null;
		}
		
		String baseSongDir = RuntimeParameterUtilities.getSongStorePath();
		return Paths.get(baseSongDir, songInfo.getRelativePath(), filePathString);
	}

	private long sizeFromFile(Path path) {
		try {
			return Files.size(path);
		} catch (IOException ex) {
			log.error("error while ");
		}
		return 0;
	}
	
	private TOSongInfo convertToTOSongInfo(BOSongInfo source) {
		TOSongInfo result = new TOSongInfo();
		
		result.setSongId(source.getFileId());
		result.setArtist(source.getArtist());
		result.setTitle(source.getTitle());
		
		String baseLink = "/songs/" + source.getFileId() + "/";
		
		if(StringUtils.isNotBlank(source.getTextFileName())) {
			result.setTextLink(baseLink + "text");
		}
		
		if(StringUtils.isNotBlank(source.getAudioFileName())) {
			result.setAudioLink(baseLink + "audio");
		}
		
		if(StringUtils.isNotBlank(source.getVideoFileName())) {
			result.setVideoLink(baseLink + "video");
		}
		
		if(StringUtils.isNotBlank(source.getCoverFileName())) {
			result.setCoverLink(baseLink + "cover");
		}
		
		if(StringUtils.isNotBlank(source.getBackgroundFileName())) {
			result.setBackgroundLink(baseLink + "background");
		}
		
		return result;
	}
}
