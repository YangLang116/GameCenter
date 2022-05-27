package com.xtu.plugin.game.lifecycle.service;

import com.xtu.game.base.service.AudioService;
import com.xtu.plugin.game.utils.StreamUtils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AudioServiceImpl implements AudioService {

    private final ExecutorService executorService = new ThreadPoolExecutor(1, 1,
            500, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    @Override
    public void play(InputStream inputStream) {
        this.executorService.submit(() -> playInner(inputStream));
    }

    private void playInner(InputStream inputStream) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtils.closeStream(inputStream);
        }
    }


}
