package com.github.supernevi.karaokeprovider.rest;

import com.github.supernevi.karaokeprovider.entities.transfer.TOSongInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@RequestMapping("/songs")
public interface SongRestController {
	
	@GetMapping(produces = "application/json")
	public Collection<TOSongInfo> getAllSongs();
	
	@GetMapping(value = "/{fileId}/text", produces = "text/plain")
	public ResponseEntity<byte[]> getSong(@PathVariable("fileId") String fileId);
	
	@GetMapping(value = "/{fileId}/cover", produces = "image/*")
	public ResponseEntity<byte[]> getCover(@PathVariable("fileId") String fileId);
	
	@GetMapping(value = "/{fileId}/background", produces = "image/*")
	public ResponseEntity<byte[]> getBackGround(@PathVariable("fileId") String fileId);
	
	@GetMapping(value = "/{fileId}/video", produces = "video/*")
	public ResponseEntity<byte[]> streamVideo(
			@RequestHeader(value = "Range", required = false) String httpRangeList,
			@PathVariable("fileId") String fileId);
	
	@GetMapping(value = "/{fileId}/audio", produces = "audio/*")
	public ResponseEntity<byte[]> streamAudio(
			@RequestHeader(value = "Range", required = false) String httpRangeList,
			@PathVariable("fileId") String fileId);
}
