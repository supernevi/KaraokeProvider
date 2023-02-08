package com.github.supernevi.KaraokeProvider;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.supernevi.KaraokeProvider.dao.SongDao;
import com.github.supernevi.KaraokeProvider.entities.BOSongInfo;
import com.github.supernevi.KaraokeProvider.utilities.RuntimeParameterUtilities;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class SongLoader {
	
	private static Logger log = LoggerFactory.getLogger(SongLoader.class);
	
	@Autowired
	private SongDao songDao;
	
	private boolean stopThread;
	private boolean songLoadingFinished;
	
	private long songsLoaded;
	
	@PreDestroy
	public void destroy() {
		stopThread = true;
	}
	
	@PostConstruct
	public void loadSongsOnStartup() {
		Thread songLoaderThread = new Thread("songLoader") {
			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				
				log.info("Starting song loading");
				
				String songStorePathString = RuntimeParameterUtilities.getSongStorePath();
				Path baseSongDir = Paths.get(songStorePathString);
				int baseSongDirOffset = songStorePathString.length();
				
				try {
					AtomicLong startTimeSongsLoaded = new AtomicLong(System.currentTimeMillis());
					Files.walkFileTree(baseSongDir, new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
							long currentTimeSongsLoaded = System.currentTimeMillis();
							if(currentTimeSongsLoaded - startTimeSongsLoaded.get() >= 5000) {
								log.info("song loading still in progress... ({} songs loaded)", songsLoaded);
								startTimeSongsLoaded.set(System.currentTimeMillis());
							}
							if (!Files.isDirectory(file) 
									&& file.getFileName().toString().endsWith(".txt")) {
								createAndSaveSongInfo(file, baseSongDirOffset);
				            }
							return stopThread ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
						}
					});
				} catch(Exception ex) {
					log.error("error occured while song loading", ex);
				} finally {
					songLoadingFinished = true;
					long endTime = System.currentTimeMillis();
					Duration duration = Duration.ofMillis(endTime - startTime);
					
					String hms = String.format("%d h %02d min %02d seconds %04d millis", 
							duration.toHours(), 
							duration.toMinutesPart(), 
							duration.toSecondsPart(),
							duration.toMillisPart());
					log.info("{} songs loaded after {}", songsLoaded, hms);
				}
			}
		};
		
		songLoaderThread.start();
	}
	
	public boolean isSongLoadingFinished() {
		return songLoadingFinished;
	}
	
	private void createAndSaveSongInfo(Path pathToTextFile, int baseSongDirOffset) {
		String fileName = pathToTextFile.getFileName().toString();
		String relativePath;
		try {
			relativePath = pathToTextFile.toRealPath(LinkOption.NOFOLLOW_LINKS).toString();
			relativePath = relativePath.substring(baseSongDirOffset, relativePath.length() - fileName.length());
		} catch (IOException e) {
			log.error("Error on computing '" + fileName + "' to relative path", e);
			return;
		}
		
		Map<String, String> metaInfos = getMetaInfosOfTextFile(pathToTextFile);
		if(metaInfos.size() == 0) {
			log.debug("file does not seem to be a song file: '{}'", pathToTextFile);
			return;
		}

		BOSongInfo newSong = new BOSongInfo();
		newSong.setTextFileName(fileName);
		newSong.setRelativePath(relativePath);
		
		newSong.setArtist(metaInfos.get("ARTIST"));
		newSong.setTitle(metaInfos.get("TITLE"));
		newSong.setVideoFileName(metaInfos.get("VIDEO"));
		newSong.setCoverFileName(metaInfos.get("COVER"));
		newSong.setAudioFileName(metaInfos.get("MP3"));
		newSong.setBackgroundFileName(metaInfos.get("BACKGROUND"));
		
		if(StringUtils.isBlank(newSong.getTextFileName())) {
			log.error("No text file found for '{}'?!", pathToTextFile);
			return;
		}
		
		if(StringUtils.isBlank(newSong.getArtist())) {
			log.warn("No artist found for '{}'", pathToTextFile);
			return;
		}
		
		if(StringUtils.isBlank(newSong.getTitle())) {
			log.warn("No title found for '{}'", pathToTextFile);
			return;
		}
		
		if(StringUtils.isBlank(newSong.getAudioFileName())) {
			log.warn("No audio found for '{}'", pathToTextFile);
			return;
		}
		
		songDao.saveSongInfo(newSong);
		songsLoaded++;
	}

	private Map<String, String> getMetaInfosOfTextFile(Path pathToTextFile) {
		Map<String, String> metaInfos = new HashMap<>();
		try(RandomAccessFile file = new RandomAccessFile(pathToTextFile.toString(), "r")) {
			String str;

			while ((str = file.readLine()) != null) {
				if(str.length() == 0) {
					continue;
				}
				// stop file reading if first character is not '#' anymore as all meta infos are only on top of the file
				if(str.charAt(0) != '#') {
					break;
				}
				
				int indexOfSeparator = str.indexOf(':', 1);
				if(indexOfSeparator > 0) {
					String key = str.substring(1, indexOfSeparator).toUpperCase();
					String value = str.substring(indexOfSeparator + 1);
					metaInfos.put(key, value);
				}
			}
		} catch (Exception e) {
			log.error("error while reading '" + pathToTextFile.toString() + "'", e);
		}
		return metaInfos;
	}
}
