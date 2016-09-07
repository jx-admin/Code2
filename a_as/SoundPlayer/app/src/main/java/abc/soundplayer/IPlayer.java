package abc.soundplayer;

/**
 * Created by jx on 2016/8/29.
 */
public interface IPlayer<T> {
    void play(T dataSource);

    void stop();

    boolean isPlaying();

    void pause();

    void resume();

    boolean isPaused();
}
