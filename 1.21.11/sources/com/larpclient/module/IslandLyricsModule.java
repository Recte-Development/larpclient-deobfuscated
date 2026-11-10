package com.larpclient.module;

import com.larpclient.gl;
import com.larpclient.h0;
import com.larpclient.q8;
import net.minecraft.class_332;

public final class IslandLyricsModule extends Module {
   private static IslandLyricsModule X;
   private final gl k = new gl();
   private h0 w;

   public IslandLyricsModule() {
      super(
         "Island Lyrics",
         "The current Spotify lyric line in a movable pill. Song title, artist, album and duration are sent to LRCLIB to find lyrics.",
         Category.RENDER,
         -1
      );
      X = this;
   }

   public static IslandLyricsModule K() {
      return X;
   }

   public static boolean t() {
      return X != null && X.isOperational();
   }

   public static boolean S() {
      return X != null && X.isEnabled() ? X != null && X.j() : false;
   }

   @Override
   public String getStatus() {
      return this.w == null ? "Play a song in Spotify desktop" : this.w.v().status();
   }

   @Override
   public void onEnable() {
      this.e();
      this.w = h0.C(this);
      q8.G("IslandLyrics", "Island Lyrics enabled; whole cue lines in the HUD element, shared Spotify worker");
   }

   @Override
   public void onDisable() {
      this.e();
      q8.v("IslandLyrics", "Released the shared Spotify worker and reset the pill");
   }

   private void e() {
      if (this.w != null) {
         h0.w(this);
         this.w = null;
      }

      this.k.K();
   }

   public boolean j() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.w != null && this.k.I(this.w.v(), System.nanoTime());
   }

   public void I(class_332 var1, int var2, int var3, int var4, boolean var5) {
      long var6 = System.nanoTime();
      if (var5) {
         this.k.p(var1, var2, var3, var4, var6);
      } else if (this.isRuntimeActive() && this.w != null) {
         this.k.B(var1, var2, var3, var4, this.w.v(), var6);
      }
   }
}
