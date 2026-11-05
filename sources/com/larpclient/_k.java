package com.larpclient;

public record _k(String id, String title, String artist, String album, double duration, double position, boolean playing, long observedAt) {
   public _k(String id, String title, String artist, String album, double duration, double position, boolean playing, long observedAt) {
      if (id != null
         && title != null
         && artist != null
         && album != null
         && title.length() <= 512
         && artist.length() <= 512
         && album.length() <= 512
         && id.length() <= 1024
         && Double.isFinite(duration)
         && Double.isFinite(position)
         && !(duration < 0.0)
         && !(position < 0.0)) {
         this.id = id;
         this.title = title;
         this.artist = artist;
         this.album = album;
         this.duration = duration;
         this.position = position;
         this.playing = playing;
         this.observedAt = observedAt;
      } else {
         throw new IllegalArgumentException("Invalid media session metadata");
      }
   }

   public String key() {
      return this.title + "\u001f" + this.artist + "\u001f" + this.album + "\u001f" + Math.round(this.duration);
   }

   public double positionAt(long var1) {
      double var3 = this.playing ? Math.clamp((var1 - this.observedAt) / 1.0E9, 0.0, 3.0) : 0.0;
      return this.duration > 0.0 ? Math.min(this.duration, this.position + var3) : this.position + var3;
   }
}
