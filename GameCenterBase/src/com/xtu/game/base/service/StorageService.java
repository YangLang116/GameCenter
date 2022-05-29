package com.xtu.game.base.service;

import com.xtu.game.base.GameCenterService;

/**
 * @author yanglang
 * @description: 保存、读取数据
 */
public interface StorageService extends GameCenterService {

    String read(String key);

    void save(String key, String value);

}
