package com.github.supernevi.KaraokeProvider.entities;

import lombok.Data;

@Data
public class BOSongInfo {
	private String fileId;
	private String relativePath;
	
	private String artist;
	private String title;
	
	private String textFileName;
	private String audioFileName;
	private String videoFileName;
	private String coverFileName;
	private String backgroundFileName;
}
