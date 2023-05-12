[![Build Status](https://github.com/supernevi/KaraokeProvider/actions/workflows/maven.yml/badge.svg)](https://github.com/supernevi/KaraokeProvider/actions/workflows/maven.yml)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/e36f2e980203443d91dcb43709ae3a93)](https://www.codacy.com/gh/supernevi/KaraokeProvider/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=supernevi/KaraokeProvider&amp;utm_campaign=Badge_Grade)
<!---
[![coverage](https://github.com/supernevi/KaraokeProvider/blob/main/.github/badges/jacoco.svg)](https://github.com/supernevi/KaraokeProvider/actions/workflows/maven.yml)
-->

# KaraokeProvider
KaraokeProvider is a free and open source java based server to provide audio, video files and lyrics, cover and background images for your karaoke machine. This project was created based on [UltraStar Play](https://usplay.net/), but basically you can host your files for what you ever want.

## How to use
This project requires Java 17 and for hosting Tomcat 10.1.x.

### Get or build your war file
As long as no releases are present you have to checkout this project and build it manually with maven.
The output is a war-file in your target folder that can be used in your tomcat server.

### Starting the tomcat server
Start your tomcat with following runtime start parameter `-DsongStorePath=<absolutePathToYourSongFolder>`
The server starts scanning all your files in that directory for UltraStar based text files and then provides this text file and all accociated media files (audio, video, cover, background) as far as they are configured and exists.

### REST API
Providing happens via REST. Current specifications can be found [here](https://github.com/supernevi/KaraokeProvider/docu/api/KaraokeProvider-api.yaml)

### Limitations
1. maximum file size for streaming is 2GB (due to technical reasons)

## Contributing
I'm grateful for any contribution. So don't resitate to create or implement issues.
