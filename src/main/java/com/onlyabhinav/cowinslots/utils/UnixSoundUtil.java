package com.onlyabhinav.cowinslots.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Component
public class UnixSoundUtil {

    private static Logger logger = LoggerFactory.getLogger(UnixSoundUtil.class);

    @Value("${notification.sound.file}")
    private String soundFile;

    public void playSound() {

        URL url = getClass().getResource(soundFile);
        SourceDataLine sourceLine = null;
        try {

            logger.info("UNIX SOUNDUTIL :: soundFile = {}, url = {}", soundFile, url);


            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFile));
            sourceLine = AudioSystem.getSourceDataLine(audioInputStream.getFormat());

            System.out.println("Source format: " + sourceLine.getFormat());
            sourceLine.open(audioInputStream.getFormat());

            sourceLine.start();

            do {
                Thread.sleep(100);
            } while (sourceLine.isRunning());
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
