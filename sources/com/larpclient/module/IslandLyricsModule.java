package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.wj;
import com.larpclient.zf;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class IslandLyricsModule extends Module {
   private static IslandLyricsModule K;
   private final zf Q = new zf();
   private wj G;

   public IslandLyricsModule() {
      super(
         "Island Lyrics",
         "The current Spotify lyric line in a movable pill. Song title, artist, album and duration are sent to LRCLIB to find lyrics.",
         Category.RENDER,
         -1
      );
      K = this;
   }

   public static IslandLyricsModule u() {
      return K;
   }

   public static boolean l() {
      return K != null && K.isOperational();
   }

   public static boolean p() {
      return K != null && K.isEnabled() ? K != null && K.c() : false;
   }

   @Override
   public String getStatus() {
      return this.G == null ? "Play a song in Spotify desktop" : this.G.U().status();
   }

   @Override
   public void onEnable() {
      this.O();
      this.G = wj.O(this);
      mt.K("IslandLyrics", "Island Lyrics enabled; whole cue lines in the HUD element, shared Spotify worker");
   }

   @Override
   public void onDisable() {
      this.O();
      mt.U("IslandLyrics", "Released the shared Spotify worker and reset the pill");
   }

   private void O() {
      if (this.G != null) {
         wj.U(this);
         this.G = null;
      }

      this.Q.i();
   }

   public boolean c() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.G != null && this.Q.d(this.G.U(), System.nanoTime());
   }

   public void b(GuiGraphicsExtractor var1, int var2, int var3, int var4, boolean var5) {
      long var6 = System.nanoTime();
      if (var5) {
         this.Q.F(var1, var2, var3, var4, var6);
      } else if (this.isRuntimeActive() && this.G != null) {
         this.Q.b(var1, var2, var3, var4, this.G.U(), var6);
      }
   }
}
