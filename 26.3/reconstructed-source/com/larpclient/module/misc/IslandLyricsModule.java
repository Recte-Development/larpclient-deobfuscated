package com.larpclient.module.misc;

import com.larpclient.hud.IslandLyricsHud;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.SpotifyLyricsService;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class IslandLyricsModule extends Module {
   private static IslandLyricsModule instance;
   private final IslandLyricsHud hud = new IslandLyricsHud();
   private SpotifyLyricsService lyricsService;

   public IslandLyricsModule() {
      super(
         "Island Lyrics",
         "The current Spotify lyric line in a movable pill. Song title, artist, album and duration are sent to LRCLIB to find lyrics.",
         Category.RENDER,
         -1
      );
      instance = this;
   }

   public static IslandLyricsModule getInstance() {
      return instance;
   }

   public static boolean isOperationalInstance() {
      return instance != null && instance.isOperational();
   }

   public static boolean isHudVisible() {
      return instance != null && instance.isEnabled() ? instance != null && instance.isVisible() : false;
   }

   @Override
   public String getStatus() {
      return this.lyricsService == null ? "Play a song in Spotify desktop" : this.lyricsService.snapshot().status();
   }

   @Override
   public void onEnable() {
      this.releaseService();
      this.lyricsService = SpotifyLyricsService.acquire(this);
      LarpLog.info("IslandLyrics", "Island Lyrics enabled; whole cue lines in the HUD element, shared Spotify worker");
   }

   @Override
   public void onDisable() {
      this.releaseService();
      LarpLog.debug("IslandLyrics", "Released the shared Spotify worker and reset the pill");
   }

   private void releaseService() {
      if (this.lyricsService != null) {
         SpotifyLyricsService.release(this);
         this.lyricsService = null;
      }

      this.hud.reset();
   }

   public boolean isVisible() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.lyricsService != null && this.hud.isVisible(this.lyricsService.snapshot(), System.nanoTime());
   }

   public void renderHud(GuiGraphicsExtractor var1, int var2, int var3, int var4, boolean var5) {
      long var6 = System.nanoTime();
      if (var5) {
         this.hud.renderPreview(var1, var2, var3, var4, var6);
      } else if (this.isRuntimeActive() && this.lyricsService != null) {
         this.hud.render(var1, var2, var3, var4, this.lyricsService.snapshot(), var6);
      }
   }
}
