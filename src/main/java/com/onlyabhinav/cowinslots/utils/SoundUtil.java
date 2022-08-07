package com.onlyabhinav.cowinslots.utils;

import com.onlyabhinav.cowinslots.configs.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

@Component
public class SoundUtil {

    private static Logger logger = LoggerFactory.getLogger(SoundUtil.class);

    @Autowired
    private UnixSoundUtil unixSoundUtil;

    @Autowired
    private AppConfig appConfig;

    @Value("${notification.sound.file}")
    private String soundFile;

    public void setSoundFile(String soundFile) {
        logger.info("sound file = {}", soundFile);
        this.soundFile = soundFile;
    }

    public static void main(String a[]) {
        new SoundUtil().playSound();
    }

    public void playSound() {
        try {
            String os = System.getProperty("os.name");
            // logger.info("OS INFO = {}", os);

            if (os.toLowerCase().startsWith("windows")) {
                playClip(ResourceUtils.getFile(soundFile));
            } else {
                try {
                    unixSoundUtil.playSound();
                } catch (Exception e) {
                    logger.info("UNABLE TO PLAY SOUND");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void playClip(File clipFile) throws IOException,
            UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        class AudioListener implements LineListener {
            private boolean done = false;

            @Override
            public synchronized void update(LineEvent event) {
                LineEvent.Type eventType = event.getType();
                if (eventType == LineEvent.Type.STOP || eventType == LineEvent.Type.CLOSE) {
                    done = true;
                    notifyAll();
                }
            }

            public synchronized void waitUntilDone() throws InterruptedException {
                while (!done) {
                    wait();
                }
            }
        }
        AudioListener listener = new AudioListener();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clipFile);
        try {
            Clip clip = AudioSystem.getClip();
            clip.addLineListener(listener);
            clip.open(audioInputStream);
            try {
                clip.start();
                listener.waitUntilDone();
            } finally {
                clip.close();
            }
        } finally {
            audioInputStream.close();
        }
    }
}
