package com.larpclient.module;

import com.larpclient.cn;
import com.larpclient.pn;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class IslandLyricsModule extends Module {
   private static IslandLyricsModule K;
   private final pn V = new pn();
   private cn R;

   public IslandLyricsModule() {
      super(
         "Island Lyrics",
         "The current Spotify lyric line in a movable pill. Song title, artist, album and duration are sent to LRCLIB to find lyrics.",
         Category.RENDER,
         -1
      );
      K = this;
   }

   public static IslandLyricsModule K() {
      return K;
   }

   public static boolean Z() {
      return K != null && K.isOperational();
   }

   public static boolean W() {
      return K != null && K.isEnabled() ? K != null && K.m() : false;
   }

   @Override
   public String getStatus() {
      return this.R == null ? "Play a song in Spotify desktop" : this.R.g().status();
   }

   @Override
   public void onEnable() {
      this.N();
      this.R = cn.L(this);
      com.larpclient.h.A("IslandLyrics", "Island Lyrics enabled; whole cue lines in the HUD element, shared Spotify worker");
   }

   @Override
   public void onDisable() {
      this.N();
      com.larpclient.h.U("IslandLyrics", "Released the shared Spotify worker and reset the pill");
   }

   private void N() {
      if (this.R != null) {
         cn.D(this);
         this.R = null;
      }

      this.V.l();
   }

   public boolean m() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.R != null && this.V.h(this.R.g(), System.nanoTime());
   }

   public void N(GuiGraphicsExtractor var1, int var2, int var3, int var4, boolean var5) {
      long var6 = System.nanoTime();
      if (var5) {
         this.V.R(var1, var2, var3, var4, var6);
      } else if (this.isRuntimeActive() && this.R != null) {
         this.V.n(var1, var2, var3, var4, this.R.g(), var6);
      }
   }
}
