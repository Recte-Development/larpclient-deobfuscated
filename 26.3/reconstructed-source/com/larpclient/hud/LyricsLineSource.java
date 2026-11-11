package com.larpclient.hud;

import com.larpclient.network.LeaderboardResult;
import com.larpclient.social.misc.RandomMessageGenerator;
import com.larpclient.social.misc.TimedLyrics;
import com.larpclient.util.misc.MediaSession;
import com.larpclient.util.misc.SpotifyLyricsService;
import java.util.List;
import java.util.random.RandomGenerator;

public final class LyricsLineSource implements RandomMessageGenerator {
   private List<String> lines = List.of();
   private String trackKey = "";
   private String currentLine = "";
   private LeaderboardResult lyrics;
   private MediaSession playback;
   private String playbackKey = "";
   private int lineIndex = -1;
   private double lastPosition;
   private double lineElapsed;
   private double lineDuration = 4.0;
   private boolean seeked;

   public boolean update(SpotifyLyricsService.PlaybackSnapshot var1, long var2) {
      MediaSession var4 = var1.playback();
      LeaderboardResult var5 = var1.lyrics();
      int var6 = var4 != null && var2 - var4.observedAt() > 4000000000L ? 1 : 0;
      if (var4 != this.playback) {
         this.playback = var4;
         this.playbackKey = var4 == null ? "" : var4.key();
      }

      String var7 = var4 != null && var6 == 0 ? this.playbackKey : "";
      int var8 = -1;
      String var9 = "";
      double var10 = var4 == null ? 0.0 : var4.positionAt(var2);
      this.lineElapsed = 0.0;
      this.lineDuration = 4.0;
      if (!var7.isEmpty() && var5 != null && var5.timed() != null) {
         TimedLyrics var12 = var5.timed();
         var8 = var12.findCueIndex(var10);
         var9 = var12.textAt(var8, var10);
         if (var8 >= 0) {
            double var13 = var12.getCues().get(var8).seconds();
            this.lineElapsed = Math.max(0.0, var10 - var13);
            double var15 = var8 + 1 < var12.getCues().size() ? var12.getCues().get(var8 + 1).seconds() : var4.duration();
            this.lineDuration = Math.clamp(var15 - var13, 0.2, 18.0);
         }
      }

      this.seeked = (boolean)(this.trackKey.equals(var7)
            && this.lyrics == var5
            && var8 >= this.lineIndex
            && var8 <= this.lineIndex + 1
            && !(Math.abs(var10 - this.lastPosition) > 1.25)
         ? 0
         : 1);
      this.lastPosition = var10;
      if (this.trackKey.equals(var7) && this.lyrics == var5 && this.lineIndex == var8 && this.currentLine.equals(var9)) {
         return false;
      }

      this.trackKey = var7;
      this.lyrics = var5;
      this.lineIndex = var8;
      this.currentLine = var9;
      if (var7.isEmpty() || var5 == null) {
         this.lines = List.of();
      } else if (var5.timed() == null) {
         this.lines = var5.plain().stream().filter(var0 -> !var0.isBlank()).limit(1L).toList();
      } else {
         this.lines = var9.isBlank() ? List.of() : List.of(var9);
      }

      return true;
   }

   public double getLineElapsed() {
      return this.lineElapsed;
   }

   public int getLineIndex() {
      return this.lineIndex;
   }

   public double getLineDuration() {
      return this.lineDuration;
   }

   public boolean hasSeeked() {
      return this.seeked;
   }

   public boolean isTimedLine() {
      return this.lyrics != null && this.lyrics.timed() != null && !this.currentLine.isBlank();
   }

   @Override
   public String next(double var1, RandomGenerator var3) {
      return this.lines.isEmpty() ? null : this.lines.get(var3.nextInt(this.lines.size()));
   }

   public List<String> getLines() {
      return this.lines;
   }

   public void reset() {
      this.lines = List.of();
      this.trackKey = this.currentLine = "";
      this.lyrics = null;
      this.lineIndex = -1;
      this.playback = null;
      this.playbackKey = "";
      this.lastPosition = this.lineElapsed = 0.0;
      this.lineDuration = 4.0;
      this.seeked = (boolean)0;
   }
}
