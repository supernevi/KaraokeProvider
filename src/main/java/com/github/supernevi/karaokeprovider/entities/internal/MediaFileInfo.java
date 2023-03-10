package com.github.supernevi.karaokeprovider.entities.internal;

import lombok.Data;

@Data
public class MediaFileInfo {
	private String fileType;
	private String fileName;
	private String absolutePath;
	private long fileSize;
}
