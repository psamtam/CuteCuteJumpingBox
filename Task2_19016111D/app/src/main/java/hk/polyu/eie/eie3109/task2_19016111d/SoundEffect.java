package hk.polyu.eie.eie3109.task2_19016111d;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

public class SoundEffect {
    private static AudioAttributes audio;

    private static SoundPool sound;
    private static final int TOTAL_NUM_OF_SOUND = 4;

    private static int bounceSound;
    private static int scoreSound;
    private static int onFireSound;
    private static int hopSound;

    private static boolean soundOn = true;

    public SoundEffect(Context context) {
        audio = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        sound = new SoundPool.Builder().setAudioAttributes(audio)
                .setMaxStreams(TOTAL_NUM_OF_SOUND).build();

        bounceSound = sound.load(context, R.raw.bounce, 1);
        scoreSound = sound.load(context, R.raw.score, 1);
        onFireSound = sound.load(context, R.raw.fire, 1);
        hopSound = sound.load(context, R.raw.smalljump, 1);
    }

    public void setSoundOn(boolean b){
        soundOn = b;
    }

    public void boundSound(){
        if (soundOn){
            sound.play(bounceSound, 1.0f, 1.0f, 1, 0, 1);
        }
    }
    public void scoreSound(){
        if (soundOn){
            sound.play(scoreSound, 0.7f, 0.7f, 1, 0, 1);
        }
    }

    public void onFireSound(){
        if (soundOn){
            sound.play(onFireSound, 0.3f, 0.3f, 1, 0, 1);
        }
    }

    public void hopSound(){
        if (soundOn){
            sound.play(hopSound, 0.3f, 0.3f, 1, 0, 1);
        }
    }
}
