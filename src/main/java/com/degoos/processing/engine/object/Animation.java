package com.degoos.processing.engine.object;

import com.degoos.processing.engine.Engine;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import processing.core.PImage;

public class Animation extends Image {

	private List<Image> imageList;
	private List<Integer> imageSequence, imageMillis;
	private int currentFrame;
	private int currentMillis;
	private int millisToChange;
	private boolean ready;

	public Animation(String folder, String extension) {
		super();
		this.currentMillis = 0;
		this.currentFrame = 0;
		this.imageList = new ArrayList<>();
		for (int i = 0; ; i++) {
			InputStream stream = Engine.getResourceInputStream(folder + "/" + i + "." + extension);
			if (stream != null) imageList.add(new Image(stream, extension));
			else break;
		}
		imageSequence = new ArrayList<>();
		imageMillis = new ArrayList<>();
		InputStream animIS = Engine.getResourceInputStream(folder + "/animation.txt");
		if (animIS == null) return;
		try (Scanner scanner = new Scanner(animIS, StandardCharsets.UTF_8.name())) {
			while (scanner.hasNext()) {
				int seq = scanner.nextInt();
				int millis = scanner.nextInt();
				imageSequence.add(seq);
				imageMillis.add(millis);
			}
		}
		ready = true;
	}

	public List<Image> getImageList() {
		return imageList;
	}

	public List<Integer> getImageSequence() {
		return imageSequence;
	}

	public List<Integer> getImageMillis() {
		return imageMillis;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public Animation setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame % imageSequence.size();
		return this;
	}

	public int getCurrentMillis() {
		return currentMillis;
	}

	public Animation setCurrentMillis(int currentMillis) {
		this.currentMillis = currentMillis;
		return this;
	}

	public int getMillisToChange() {
		return millisToChange;
	}

	public Animation setMillisToChange(int millisToChange) {
		this.millisToChange = millisToChange;
		return this;
	}

	public boolean isReady() {
		return ready;
	}

	public Animation reset () {
		if(imageSequence.isEmpty() || imageMillis.isEmpty()) return this;
		currentFrame = 0;
		currentMillis = 0;
		millisToChange = imageMillis.get(currentFrame);
		return this;
	}

	public PImage getHandled() {
		if (!ready) return null;
		return imageList.isEmpty() ? null : imageList.get(imageSequence.get(currentFrame)).getHandled();
	}

	@Override
	public void onTick(long dif) {
		if (!ready) return;
		currentMillis += dif;
		while (currentMillis >= millisToChange) {
			currentMillis -= millisToChange;
			currentFrame++;
			if (currentFrame > imageSequence.size() - 1) currentFrame = 0;
			millisToChange = imageMillis.get(currentFrame);
		}
	}
}
