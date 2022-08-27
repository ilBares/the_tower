package it.unibs.pajc.baresi.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

// TODO to complete https://www.youtube.com/watch?v=1O8tFKtZYTM&list=PL_QPQmz5C6WUF-pOQDsbsKbaBZqXj4qSq&index=38
public class Sound {
      public static int SOUND_TRACK = 0;
      public static int MINI_GOLEM_ATTACK = 1;
      public static int ADVENTURER_ATTACK = 2;
      public static int DRAGON_ATTACK = 3;
      public static int GOLEM_ATTACK = 4;
      public static int SKELETON_ATTACK = 5;
      public static int GHOUL_ATTACK = 6;

      private static URL[] soundURL = new URL[10];

      static {
            soundURL[SOUND_TRACK] = Sound.class.getResource("/sound/soundtrack.wav");
            soundURL[MINI_GOLEM_ATTACK] = Sound.class.getResource("/sound/mini_golem_attack.wav");
            soundURL[ADVENTURER_ATTACK] = Sound.class.getResource("/sound/adventurer_attack.wav");
            soundURL[DRAGON_ATTACK] = Sound.class.getResource("/sound/dragon_attack.wav");
            soundURL[GOLEM_ATTACK] = Sound.class.getResource("/sound/golem_attack.wav");
            soundURL[SKELETON_ATTACK] = Sound.class.getResource("/sound/skeleton_attack.wav");
            soundURL[GHOUL_ATTACK] = Sound.class.getResource("/sound/ghoul_attack.wav");
      }

      /*
      public synchronized static void setFile(int index) {
            try {
                  AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[index]);
                  clip = AudioSystem.getClip();
                  clip.open(ais);
            } catch (Exception e) {
                  throw new RuntimeException(e);
            }
      }
       */

      public static void play(int index, boolean loop) {
            AudioInputStream ais;
            Clip clip;
            try {
                  ais = AudioSystem.getAudioInputStream(soundURL[index]);
                  clip = AudioSystem.getClip();
                  clip.open(ais);
            } catch (Exception e) {
                  throw new RuntimeException(e);
            }
            clip.start();
            if (loop)
                  clip.loop(Clip.LOOP_CONTINUOUSLY);
      }

      public static void stop(int index) {

      }
}
