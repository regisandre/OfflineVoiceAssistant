package be.sioxox.main;

import be.sioxox.tools.ProjectScreen;
import be.sioxox.utils.Screen;
import be.sioxox.voiceassistant.SpeechRecognition;

public class Main {

	static SpeechRecognition sr = new SpeechRecognition();
	static Screen u = new Screen();
	static ProjectScreen psow = new ProjectScreen();

	public static void main(String[] args) throws Exception {
		sr.speechRecognition();
		//u.getScreen();
		//psow.projectScreen();
	}

}
