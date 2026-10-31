package Main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    Clip clip;
    URL soundUrl[] = new URL[30];
    long clipTimePosition;

    public Sound() {
        soundUrl[0] = getClass().getResource("/Sound/Map_Atas.wav");
        soundUrl[1] = getClass().getResource("/Sound/Door_Open.wav");
        soundUrl[2] = getClass().getResource("/Sound/Take_Item.wav");
        soundUrl[3] = getClass().getResource("/Sound/Hit.wav");
        soundUrl[4] = getClass().getResource("/Sound/Sword_Slash.wav");
        soundUrl[5] = getClass().getResource("/Sound/Hitmonster.wav");
        soundUrl[6] = getClass().getResource("/Sound/Cursor.wav");
        soundUrl[7] = getClass().getResource("/Sound/YesMenu.wav");
        soundUrl[8] = getClass().getResource("/Sound/powerup.wav");
        soundUrl[9] = getClass().getResource("/Sound/gameover.wav");
        soundUrl[10] = getClass().getResource("/Sound/winning.wav");
        soundUrl[11] = getClass().getResource("/Sound/Map_Boss.wav");
        soundUrl[12] = getClass().getResource("/Sound/Map_Dungeon.wav");

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
        if (clip != null) {
            clip.stop();
        }
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

    /**
     * Set volume for the current clip. Volume is 0.0f (silent) .. 1.0f (full).
     */
    public void setVolume(float volume) {
        if (clip != null) {
            try {
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = 20f * (float) Math.log10(Math.max(volume, 0.0001f));
                volumeControl.setValue(dB);
            } catch (Exception e) {
                // Some systems may not support MASTER_GAIN; ignore safely
            }
        }
    }
}
