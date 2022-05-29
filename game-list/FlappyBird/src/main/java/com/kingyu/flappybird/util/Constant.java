package com.kingyu.flappybird.util;

import java.awt.*;

/**
 * 常量类
 *
 * @author Kingyu 后续优化可写入数据库或文件中，便于修改
 */

public class Constant {
    // 窗口尺寸
    public static final int FRAME_WIDTH = 420;
    public static final int FRAME_HEIGHT = 640;

    // 游戏标题
    public static final String GAME_TITLE = "Flappy Bird written by Kingyu";

    // 窗口位置
    public static final int FRAME_X = 600;
    public static final int FRAME_Y = 100;

    // 图像资源路径
    public static final String BG_IMG_PATH = "/img/background.png"; // 背景图片

    // 小鸟图片
    public static final String[][] BIRDS_IMG_PATH = {
            {"/img/0.png", "/img/1.png", "/img/2.png", "/img/3.png",
                    "/img/4.png", "/img/5.png", "/img/6.png", "/img/7.png"},
            {"/img/up.png", "/img/up.png", "/img/up.png", "/img/up.png",
                    "/img/up.png", "/img/up.png", "/img/up.png", "/img/up.png"},
            {"/img/down_0.png", "/img/down_1.png", "/img/down_2.png",
                    "/img/down_3.png", "/img/down_4.png", "/img/down_5.png",
                    "/img/down_6.png", "/img/down_7.png"},
            {"/img/dead.png", "/img/dead.png", "/img/dead.png", "/img/dead.png",
                    "/img/dead.png", "/img/dead.png", "/img/dead.png",
                    "/img/dead.png",}};

    // 云朵图片
    public static final String[] CLOUDS_IMG_PATH = {"/img/cloud_0.png", "/img/cloud_1.png"};

    // 水管图片
    public static final String[] PIPE_IMG_PATH = {"/img/pipe.png", "/img/pipe_top.png",
            "/img/pipe_bottom.png"};

    public static final String TITLE_IMG_PATH = "/img/title.png";
    public static final String NOTICE_IMG_PATH = "/img/start.png";
    public static final String SCORE_IMG_PATH = "/img/score.png";
    public static final String OVER_IMG_PATH = "/img/over.png";
    public static final String AGAIN_IMG_PATH = "/img/again.png";

    // 游戏速度（水管及背景层的移动速度）
    public static final int GAME_SPEED = 4;

    // 游戏背景色
    public static final Color BG_COLOR = new Color(0x4bc4cf);

    // 游戏刷新率
    public static final int FPS = 1000 / 30;

    // 标题栏高度
    public static final int TOP_BAR_HEIGHT = 20;

    // 地面高度
    public static final int GROUND_HEIGHT = 35;

    // 上方管道加长
    public static final int TOP_PIPE_LENGTHENING = 100;

    public static final int CLOUD_BORN_PERCENT = 6; // 云朵生成的概率，单位为百分比
    public static final int CLOUD_IMAGE_COUNT = 2; // 云朵图片的个数
    public static final int MAX_CLOUD_COUNT = 7; // 云朵的最大数量

    public static final Font CURRENT_SCORE_FONT = new Font("华文琥珀", Font.BOLD, 32);// 字体
    public static final Font SCORE_FONT = new Font("华文琥珀", Font.BOLD, 24);// 字体

}
