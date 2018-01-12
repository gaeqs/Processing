package com.degoos.processing.engine.sound;

import com.degoos.processing.engine.Engine;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

	private Map<String, Media> medias;

	public SoundManager() {
		medias = new HashMap<>();
	}

	public MediaPlayer play(String filePath, double balance) {
		MediaPlayer mediaPlayer = getMediaPlayer(filePath);
		mediaPlayer.setBalance(balance);
		mediaPlayer.play();
		return mediaPlayer;
	}

	public MediaPlayer getMediaPlayer(String filePath) {
		return new MediaPlayer(getMedia(filePath));
	}

	public Media getMedia(String file) {
		if (medias.containsKey(file)) return medias.get(file);
		Media hit = null;
		try {
			hit = new Media(Engine.getResourceURL(file).toURI().toString());
			medias.put(file, hit);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return hit;
	}

}
