package com.github.supernevi.KaraokeProvider.utilities;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RuntimeParameterUtilities {
	enum PARAMS {
		SONG_STORE_PATH("songStorePath", "/static/songs");

		private String key;
		private String defaultValue;

		private PARAMS(String key, String defaultValue) {
			this.key = key;
			this.defaultValue = defaultValue;
		}
	}
	
	private static String getRuntimeParameter(PARAMS p) {
		return getRuntimeParameter(p.key, p.defaultValue);
	}
	
	private static String getRuntimeParameter(String key, String defaultStr) {
		RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
		List<String> arguments = runtimeMxBean.getInputArguments();
		for (String arg : arguments) {
			if (arg.startsWith("-D" + key + "=")) {
				return arg.split("=")[1].trim();
			}
		}
		return defaultStr;
	}
	
	public static String getSongStorePath() {
		return getRuntimeParameter(PARAMS.SONG_STORE_PATH);
	}
}
