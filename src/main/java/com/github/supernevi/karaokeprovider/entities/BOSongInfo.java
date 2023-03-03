package com.github.supernevi.karaokeprovider.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
