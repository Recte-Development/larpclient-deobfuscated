package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.ov;
import com.larpclient.pb;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class IslandLyricsModule extends Module {
   private static IslandLyricsModule q;
   private final pb r = new pb();
   private ov S;

   public IslandLyricsModule() {
      super(
         "Island Lyrics",
         "The current Spotify lyric line in a movable pill. Song title, artist, album and duration are sent to LRCLIB to find lyrics.",
         Category.RENDER,
         -1
      );
      q = this;
   }

   public static IslandLyricsModule r() {
      return q;
   }

   public static boolean J() {
      return q != null && q.isOperational();
   }

   public static boolean U() {
      return q != null && q.isEnabled() ? q != null && q.j() : false;
   }

   @Override
   public String getStatus() {
      return this.S == null ? "Play a song in Spotify desktop" : this.S.X().status();
   }

   @Override
   public void onEnable() {
      this.i();
      this.S = ov.Y(this);
      _q.z("IslandLyrics", "Island Lyrics enabled; whole cue lines in the HUD element, shared Spotify worker");
   }

   @Override
   public void onDisable() {
      this.i();
      _q.c("IslandLyrics", "Released the shared Spotify worker and reset the pill");
   }

   private void i() {
      if (this.S != null) {
         ov.E(this);
         this.S = null;
      }

      this.r.w();
   }

   public boolean j() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.S != null && this.r.C(this.S.X(), System.nanoTime());
   }

   public void B(GuiGraphicsExtractor var1, int var2, int var3, int var4, boolean var5) {
      long var6 = System.nanoTime();
      if (var5) {
         this.r.v(var1, var2, var3, var4, var6);
      } else if (this.isRuntimeActive() && this.S != null) {
         this.r.a(var1, var2, var3, var4, this.S.X(), var6);
      }
   }
}
