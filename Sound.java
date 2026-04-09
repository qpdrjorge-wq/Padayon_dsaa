package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {

    Clip musicClip;
    Clip[] seClips;
    URL[] soundURL = new URL[30];

    public static final int MUSIC_TITLE    = 0;
    public static final int MUSIC_CHAPTER1 = 3;
    public static final int MUSIC_CHAPTER2 = 4;
    public static final int MUSIC_CHAPTER3 = 6;

    //sound effects

    public static final int SE_BUTTON = 1;
    public static final int SE_JUMP = 2;
    public static final int SE_WHEEL = 5;

    //master volume
    private static final float MUSIC_VOLUME = 0.1f;
    private static final float SE_VOLUME= 0.7f;

    //mutable fields
    public float musicVolume = 0.1f;
    public float seVolume = 0.7f;
    public float masterVolume = 1.0f;

    public Sound(){

        soundURL[0] = getClass().getResource("/sound/MusicSample4.wav");
        soundURL[1] = getClass().getResource("/sound/button.wav");
        soundURL[2] = getClass().getResource("/sound/pl_jump_2.wav");
        soundURL[3] = getClass().getResource("/sound/chapter1.wav");
        soundURL[4] = getClass().getResource("/sound/chapter2.wav");
        soundURL[5] = getClass().getResource("/sound/wheelSound.wav");
        soundURL[6]= getClass().getResource("/sound/chapter3.wav");

        seClips = new Clip[soundURL.length];
        preloadSE(SE_BUTTON);
        preloadSE(SE_JUMP);
        preloadSE(SE_WHEEL);
    }

    public void playMusic(int i){
        stopMusic();

        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            musicClip = AudioSystem.getClip();
            musicClip.open(ais);
            setVolume(musicClip, musicVolume * masterVolume);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stopMusic(){
        if(musicClip != null && musicClip.isRunning()){
            musicClip.stop();
            musicClip.close();
        }
    }

    private void preloadSE(int i) {
        if (soundURL[i] == null) {
            System.err.println("SE file not found at index: " + i);
            return;
        }
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            seClips[i] = AudioSystem.getClip();
            seClips[i].open(ais);
            setVolume(seClips[i], seVolume + masterVolume);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSE(int i) {
        if (seClips[i] == null) {
            System.err.println("SE not preloaded at index: " + i);
            return;
        }
        seClips[i].stop();
        seClips[i].setFramePosition(0);
        seClips[i].start();
    }

    private void setVolume(Clip clip, float volume) {
        try {
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // Convert linear 0.0-1.0 to decibel range
            float dB = (float)(Math.log10(Math.max(volume, 0.0001f)) * 20);
            dB = Math.max(gainControl.getMinimum(), Math.min(dB, gainControl.getMaximum()));
            gainControl.setValue(dB);
        } catch (Exception e) {
            // Some audio systems don't support MASTER_GAIN — safe to ignore
        }
    }

    public void setMusicVolume(float volume) {
        musicVolume = volume;
        if (musicClip != null && musicClip.isOpen()) {
            setVolume(musicClip, masterVolume * musicVolume);
        }
    }

    public void setSEVolume(float volume) {
        seVolume = volume;
        for (Clip c : seClips) {
            if (c != null && c.isOpen()) {
                setVolume(c, masterVolume * seVolume);
            }
        }
    }

    public void setMasterVolume(float volume) {
        masterVolume = volume;
        setMusicVolume(musicVolume);
        setSEVolume(seVolume);
    }
}
