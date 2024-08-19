package com.xtu.plugin.game.loader.fc.entity;

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
}