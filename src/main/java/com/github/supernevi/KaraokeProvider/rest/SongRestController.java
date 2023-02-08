package com.github.supernevi.KaraokeProvider.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.supernevi.KaraokeProvider.entities.transfer.TOSongInfo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/songs")
public interface SongRestController {
	
	@GetMapping
	public Flux<TOSongInfo> getAllSongs();
	
	@GetMapping(value = "/{fileId}/text")
	public ResponseEntity<byte[]> getSong(@PathVariable("fileId") String fileId);
	
	@GetMapping(value = "/{fileId}/cover")
	public ResponseEntity<byte[]> getCover(@PathVariable("fileId") String fileId);
	
	@GetMapping(value = "/{fileId}/background")
	public ResponseEntity<byte[]> getBackGround(@PathVariable("fileId") String fileId);
	
	@GetMapping("/{fileId}/video")
	public Mono<ResponseEntity<byte[]>> streamVideo(
			@RequestHeader(value = "Range", required = false) String httpRangeList,
			@PathVariable("fileId") String fileId);
	
	@GetMapping("/{fileId}/audio")
	public Mono<ResponseEntity<byte[]>> streamAudio(
			@RequestHeader(value = "Range", required = false) String httpRangeList,
			@PathVariable("fileId") String fileId);
}
