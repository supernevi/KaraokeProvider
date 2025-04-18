package com.github.supernevi.karaokeprovider.rest;

import com.github.supernevi.karaokeprovider.entities.internal.MediaFileInfo;
import com.github.supernevi.karaokeprovider.entities.transfer.TOSongInfo;
import com.github.supernevi.karaokeprovider.enums.KaraokeMediaType;
import com.github.supernevi.karaokeprovider.services.SongService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Collection;

@RestController
@Log4j2
public class SongRestControllerImpl implements SongRestController {

	@Autowired
	private SongService songService;

	@Override
	public Collection<TOSongInfo> getAllSongs() {
		return songService.getAllSongInfos();
	}
	
	@Override
	public ResponseEntity<byte[]> getSong(String fileId) {
		return getContent(fileId, null, KaraokeMediaType.TEXT);
	}

	@Override
	public ResponseEntity<byte[]> getCover(String fileId) {
		return getContent(fileId, null, KaraokeMediaType.COVER);
	}
	
	@Override
	public ResponseEntity<byte[]> getBackGround(String fileId) {
		return getContent(fileId, null, KaraokeMediaType.BACKGROUND);
	}

	@Override
	public ResponseEntity<byte[]> streamVideo(String httpRangeList, String fileId) {
		return getContent(fileId, httpRangeList, KaraokeMediaType.VIDEO);
	}

	@Override
	public ResponseEntity<byte[]> streamAudio(String httpRangeList, String fileId) {
		return getContent(fileId, httpRangeList, KaraokeMediaType.AUDIO);
	}

	private ResponseEntity<byte[]> getContent(String fileName, String range, KaraokeMediaType contentTypePrefix) {
		MediaFileInfo fileInfo = songService.getMediaFileInfo(fileName, contentTypePrefix);
		if(fileInfo == null) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.build();
		}
		
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String mimeType = fileNameMap.getContentTypeFor(fileInfo.getFileName());
		
		long rangeStart = 0;
		long rangeEnd;
		byte[] data;
		long fileSize = fileInfo.getFileSize();
		try {
			if (range == null) {
				return ResponseEntity
						.status(HttpStatus.OK)
						.header("Content-Type", mimeType)
						.header("Content-Length", String.valueOf(fileSize))
						.body(songService.readByteRange(fileInfo, rangeStart, fileSize - 1));
			}
			String[] ranges = range.split("-");
			rangeStart = Long.parseLong(ranges[0].substring(6));
			if (ranges.length > 1) {
				rangeEnd = Long.parseLong(ranges[1]);
			} else {
				rangeEnd = fileSize - 1;
			}
			if (fileSize < rangeEnd) {
				rangeEnd = fileSize - 1;
			}
			data = songService.readByteRange(fileInfo, rangeStart, rangeEnd);
		} catch (IOException e) {
			log.error("an error occured while getting content of '" + fileName + "'", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.header("Content-Type", mimeType)
				.header("Accept-Ranges", "bytes")
				.header("Content-Length", contentLength)
				.header("Content-Range", "bytes" + " " + rangeStart + "-" + rangeEnd + "/" + fileSize).body(data);
	}
}
