package com.github.supernevi.karaokeprovider.entities.transfer;

import lombok.Data;

@Data
public class TOSongInfo {
	private String songId;
	
	private String artist;
	private String title;
	
	private String textLink;
	private String audioLink;
	private String videoLink;
	private String coverLink;
	private String backgroundLink;
}