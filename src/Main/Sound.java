package Main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    Clip clip;
    URL soundUrl[] = new URL[30];
    long clipTimePosition;

    public Sound() {
        soundUrl[0] = getClass().getResource("/Sound/Map_Atas.wav");
        soundUrl[1] = getClass().getResource("/Sound/Door_Open.wav");
        soundUrl[2] = getClass().getResource("/Sound/Take_Item.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrl[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            clipTimePosition = clip.getMicrosecondPosition();
            clip.stop();
        }
    }

    public void resume() {
        if (clip != null) {
            clip.setMicrosecondPosition(clipTimePosition);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
