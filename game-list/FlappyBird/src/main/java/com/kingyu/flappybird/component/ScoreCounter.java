package com.kingyu.flappybird.component;

import com.kingyu.flappybird.util.MusicUtil;
import com.xtu.game.base.GameCenterFacade;
import com.xtu.game.base.GameCenterService;
import com.xtu.game.base.service.StorageService;

import java.util.Objects;

/**
 * 游戏计时器, 使用静态内部类实现了单例模式
 *
 * @author Kingyu
 */
public class ScoreCounter {

    private static final class ScoreCounterHolder {
        private static final ScoreCounter scoreCounter = new ScoreCounter();
    }

    public static ScoreCounter getInstance() {
        return ScoreCounterHolder.scoreCounter;
    }

    private int score; // 分数
    private int bestScore; // 最高分数

    private ScoreCounter() {
        this.score = 0;
        this.bestScore = loadBestScore();
    }

    // 装载最高纪录
    private int loadBestScore() {
        GameCenterFacade<? extends GameCenterService> gameCenterFacade = GameCenterFacade.getInstance();
        StorageService storageService = gameCenterFacade.getStorageService();
        if (storageService == null) return 0;
        String saveScore = storageService.read(getClass().getSimpleName());
        if (saveScore == null || Objects.equals(saveScore.trim(), "")) return 0;
        try {
            return Integer.parseInt(saveScore);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void saveScore() {
        this.bestScore = Math.max(this.bestScore, getCurrentScore());
        GameCenterFacade<? extends GameCenterService> gameCenterFacade = GameCenterFacade.getInstance();
        StorageService storageService = gameCenterFacade.getStorageService();
        if (storageService != null) {
            storageService.save(getClass().getSimpleName(), String.valueOf(bestScore));
        }
    }

    public void score(Bird bird) {
        if (!bird.isDead()) {
            MusicUtil.playScore();
            this.score += 1;
        }
    }

    public int getBestScore() {
        return this.bestScore;
    }

    public int getCurrentScore() {
        return this.score;
    }

    public void reset() {
        this.score = 0;
    }

}