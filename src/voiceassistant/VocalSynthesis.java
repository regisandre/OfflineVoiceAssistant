package be.sioxox.voiceassistant;

import java.util.Locale;

import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

public class VocalSynthesis {
	//tts = Text-to-speech
	String tts;
	public void tts(String tts) throws Exception {
		this.tts = tts;
		
		SynthesizerModeDesc desc = new SynthesizerModeDesc(Locale.ROOT);
		Synthesizer synth = Central.createSynthesizer(desc); // new SynthesizerModeDesc(Locale.ROOT) 
		
		synth.allocate();
		synth.resume();
		
//		desc = (SynthesizerModeDesc) synth.getEngineModeDesc();
//		
//		Voice[] voices = desc.getVoices();
//        Voice voice = null;
//        for (int i = 0; i < voices.length; i++)
//        {
//            if (voices[i].getName().equals("test"))
//            {    
//                voice = voices[i];
//                break;
//            }
//        }
//		
//		synth.getSynthesizerProperties().setVoice(voice);
		
		synth.speakPlainText(tts, null);
		System.out.println("Answer: " + tts);
	 
		synth.waitEngineState(Synthesizer.QUEUE_EMPTY);
		
		synth.deallocate();
	}
	
	// On récupère le TTS
	public String getTTS() {
		return tts;
	}
}
