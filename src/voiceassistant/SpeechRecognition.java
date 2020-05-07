package be.sioxox.voiceassistant;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.speech.Central;
import javax.speech.EngineModeDesc;
import javax.speech.recognition.DictationGrammar;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultAdapter;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultToken;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.SpeakerManager;
import javax.speech.recognition.SpeakerProfile;

import be.sioxox.utils.SentenceSimilarity;

public class SpeechRecognition extends ResultAdapter {

	static Recognizer recognizer = null;
	static VocalSynthesis vs = new VocalSynthesis();
	static SentenceSimilarity ss = new SentenceSimilarity();

	List<String> allSentenceList = new ArrayList<String>();
	List<String> resultAcceptedList = new ArrayList<String>();
	List<String> resultRejectedList = new ArrayList<String>();
	List<String> answerList = new ArrayList<String>();

	String answer = "";

	// Call à la détection d'un nouvel ordre
	public void resultCreated(ResultEvent e) {
		System.out.println("Result created");
	}

	// Call à la detection d'un nouveau mot ou syllabe?
	public void resultUpdated(ResultEvent e) {
		Result r = (Result) (e.getSource());
		System.out.println("Result updated " + r);
	}

	// Call si rien n'est trouvé (bruit ambiant...)
	public void resultRejected(ResultEvent e) {
		System.out.println("Result rejected");
	}

	// Call si match avec un élément de la grammaire
	public void resultAccepted(ResultEvent re) {
		try {
			Result res = (Result) (re.getSource());
			// On récupère le meilleur token
			ResultToken tokens[] = res.getBestTokens();

			String resultWordTokenList = "";
			String lastSentence = "";

			for (int i = 0; i < tokens.length; i++) {
				resultWordTokenList = tokens[i].getSpokenText();
				lastSentence += resultWordTokenList + " ";
			}

			lastSentence = lastSentence.substring(0, lastSentence.length() - 1);

			// Si égal à ce qu'on a dit, Francis répond
			if (ss.sentenceSimilarity(lastSentence, "T'es qu'un porc Francis") > 70
					|| ss.sentenceSimilarity(lastSentence, "T'es qu'un criminel") > 70) {
				recognizer.pause();

				vs.tts(answer = "Je retourne à la porcherie");
				
				// On ajoute le contenu aux Lists
				resultAcceptedList.add(lastSentence);
				allSentenceList.add(lastSentence);
				answerList.add(answer);

				recognizer.deallocate();
				System.exit(0);
			} else if (ss.sentenceSimilarity(lastSentence, "Bonjour Francis") > 70) {
				recognizer.pause();
				vs.tts(answer = "Bonjour maître");
				
				// On ajoute le contenu aux Lists
				resultAcceptedList.add(lastSentence);
				allSentenceList.add(lastSentence);
				answerList.add(answer);

				recognizer.resume();
			} else {
				// On met la reconnaissance en pause pour que la réponse ne
				// face pas
				// d'interférence
				recognizer.pause();
				
				// On ajoute le contenu aux Lists
				allSentenceList.add(lastSentence);
				resultRejectedList.add(lastSentence);

				// vs.tts("J'ai de la boue dans mes oreilles, veuillez m'excuser"); // TTS
				answer = "J'ai de la boue dans mes oreilles, veuillez m'excuser";

				recognizer.resume();
			}

		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
			e.printStackTrace();
		}
	}

	// On récupère toutes les phrases
	public List<String> getAllSentenceList() {
		return allSentenceList;
	}

	// On récupère toutes les phrases acceptées
	public List<String> getResultAcceptedList() {
		return resultAcceptedList;
	}

	// On récupère toutes les phrases rejetées
	public List<String> getResultRejectedList() {
		return resultRejectedList;
	}

	// On récupère la dernière réponse
	public String getLastAnswer() {
		return answer;
	}

	public void speechRecognition() {
		try {
			// On initialise le moteur de reconnaisance
			recognizer = Central.createRecognizer(new EngineModeDesc(Locale.ROOT));
			recognizer.allocate();

			// On lui indique le fichier de grammaire
			FileReader grammar = new FileReader("files/grammar.jsgf");
			RuleGrammar rg = recognizer.loadJSGF(grammar);
			rg.setEnabled(true);

			// On lui indique que le mode dictation est activé en plus de la
			// grammaire
			DictationGrammar dictation;
			dictation = recognizer.getDictationGrammar("dictation");
			dictation.setEnabled(true);

			SpeakerManager speakerManager = recognizer.getSpeakerManager();

			// On applique le profil de reconnaissance Windows
			SpeakerProfile profile = new SpeakerProfile();
			SpeakerProfile[] profs = speakerManager.listKnownSpeakers();
			for (int i = 0; i < profs.length; i++) {
				System.out.println("Found profile " + i + " = " + profs[i].getName());
				profile = profs[i];
			}

			speakerManager.setCurrentSpeaker(profile);
			System.out.println("Current profile set to " + speakerManager.getCurrentSpeaker().getName());

			// On ajoute un listener
			recognizer.addResultListener(new SpeechRecognition());

			vs.tts(answer = "Je suis disponible maître");
			
			// On ajoute le contenu à la List
			answerList.add(answer);

			recognizer.commitChanges();
			recognizer.requestFocus();
			recognizer.resume();
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
			e.printStackTrace();
			System.exit(0);
		}
	}
}
