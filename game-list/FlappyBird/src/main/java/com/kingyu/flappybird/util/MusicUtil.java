package com.kingyu.flappybird.util;

import com.xtu.game.base.GameCenterFacade;
import com.xtu.game.base.service.AudioService;

import java.io.InputStream;

/**
 * @author yanglang
 * 兼容java11 播放音频
 */
public class MusicUtil {

    private static void playAudio(String assetPath) {
        GameCenterFacade<?> gameCenterFacade = GameCenterFacade.getInstance();
        AudioService audioService = gameCenterFacade.getAudioService();
        if (audioService != null) {
            InputStream audioStream = MusicUtil.class.getResourceAsStream(assetPath);
            audioService.play(audioStream);
        }
    }

    // wav播放
    public static void playFly() {
        playAudio("/wav/fly.wav");
    }

    public static void playCrash() {
        playAudio("/wav/crash.wav");
    }

    public static void playScore() {
        playAudio("/wav/score.wav");
    }
}
