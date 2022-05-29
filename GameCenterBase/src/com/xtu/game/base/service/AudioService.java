package com.xtu.game.base.service;

import com.xtu.game.base.GameCenterService;

import java.io.InputStream;

public interface AudioService extends GameCenterService {

    void play(InputStream inputStream);
}
