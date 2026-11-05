package com.larpclient.module;

import com.larpclient.de;
import com.larpclient.oc;
import com.larpclient.r4;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class IslandLyricsModule extends Module {
   private static IslandLyricsModule R;
   private final de q = new de();
   private oc x;

   public IslandLyricsModule() {
      super(
         "Island Lyrics",
         "The current Spotify lyric line in a movable pill. Song title, artist, album and duration are sent to LRCLIB to find lyrics.",
         Category.RENDER,
         -1
      );
      R = this;
   }

   public static IslandLyricsModule g() {
      return R;
   }

   public static boolean n() {
      return R != null && R.isOperational();
   }

   public static boolean e() {
      return R != null && R.isEnabled() ? R != null && R.w() : false;
   }

   @Override
   public String getStatus() {
      return this.x == null ? "Play a song in Spotify desktop" : this.x.z().status();
   }

   @Override
   public void onEnable() {
      this.v();
      this.x = oc.I(this);
      r4.V("IslandLyrics", "Island Lyrics enabled; whole cue lines in the HUD element, shared Spotify worker");
   }

   @Override
   public void onDisable() {
      this.v();
      r4.g("IslandLyrics", "Released the shared Spotify worker and reset the pill");
   }

   private void v() {
      if (this.x != null) {
         oc.m(this);
         this.x = null;
      }

      this.q.f();
   }

   public boolean w() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.x != null && this.q.T(this.x.z(), System.nanoTime());
   }

   public void w(GuiGraphicsExtractor var1, int var2, int var3, int var4, boolean var5) {
      long var6 = System.nanoTime();
      if (var5) {
         this.q.A(var1, var2, var3, var4, var6);
      } else if (this.isRuntimeActive() && this.x != null) {
         this.q.q(var1, var2, var3, var4, this.x.z(), var6);
      }
   }
}
