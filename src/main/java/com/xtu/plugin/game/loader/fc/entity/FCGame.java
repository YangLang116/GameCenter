package com.xtu.plugin.game.loader.fc.entity;

import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord")
public final class FCGame {

    public final String name;
    public final String desc;
    public final String icon;
    public final String url;

    public FCGame(String name, String desc, String icon, String url) {
        this.name = name;
        this.desc = desc;
        this.icon = icon;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FCGame fcGame = (FCGame) o;
        return Objects.equals(url, fcGame.url);
    }


    @Override
    public String toString() {
        return "FCGame{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", icon='" + icon + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}