package com.packtpub.libgdx.canyonbunny.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * @auther SHI Zhancheng
 * @create 2021-05-10 17:34
 */
public class AudioManager {
    public static final AudioManager instance = new AudioManager();

    private Music playingMusic;

    // 单例类：阻止用户在其它类中创建实例
    // singleton: prevent instantiation from other classes
    private AudioManager () {
    }

    public void play (Sound sound) {
        play(sound, 1);
    }

    public void play (Sound sound, float volume) {
        play(sound, volume, 1);
    }

    public void play (Sound sound, float volume, float pitch) {
        play(sound, volume, pitch, 0);
    }

    public void play (Sound sound, float volume, float pitch, float pan) {
        if (!GamePreferences.instance.sound) return;
        sound.play(GamePreferences.instance.volSound * volume, pitch, pan);
    }

    public void play (Music music) {
        playingMusic = music;
        if (GamePreferences.instance.music) {
            music.setLooping(true);
            music.setVolume(GamePreferences.instance.volMusic);
            music.play();
        }
    }

    public void stopMusic () {
        if (playingMusic != null) playingMusic.stop();
    }

    public Music getPlayingMusic () {
        return playingMusic;
    }

    public void onSettingsUpdated () {
        if (playingMusic == null) return;
        playingMusic.setVolume(GamePreferences.instance.volMusic);
        if (GamePreferences.instance.music) {
            if (!playingMusic.isPlaying()) playingMusic.play();
        } else {
            playingMusic.pause();
        }
    }
}
