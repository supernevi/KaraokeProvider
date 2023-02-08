package com.github.supernevi.KaraokeProvider.enums;

public enum KaraokeMediaType {
	VIDEO("video"),
	AUDIO("audio"),
	TEXT("text"),
	COVER("image"),
	BACKGROUND("image");
	
	private String httpContentType;
	
	private KaraokeMediaType(String type) {
		httpContentType = type;
	}
	
	public String getHttpContentType() {
		return httpContentType;
	}
}
