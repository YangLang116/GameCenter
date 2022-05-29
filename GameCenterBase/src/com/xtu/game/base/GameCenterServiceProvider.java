package com.xtu.game.base;

public abstract class GameCenterServiceProvider<T extends GameCenterService> {

    private T instance;

    public T get() {
        if (instance == null) {
            instance = create();
        }
        return instance;
    }

    public abstract T create();
}
