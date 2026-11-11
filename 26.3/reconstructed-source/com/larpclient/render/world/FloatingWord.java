package com.larpclient.render.world;

public final class FloatingWord {
   public FloatingWord.State state = FloatingWord.State.IDLE;
   public String text = "";
   public double x;
   public double y;
   public double z;
   public double jitter;
   public double phase;
   public double speed;
   public double driftPhase;
   public double driftX;
   public double driftY;
   public double age;
   public double lifetime;
   public double stateTime;
   public double alpha;
   public double scale = 1.0;
   public double fadeStartAlpha;
   public double spawnCooldown;
   public double offsetY;
   public double offsetX;
   public double offsetZ;
   public double rotation;
   public int generation;
   public boolean active;

   public void beginFadeOut() {
      if (this.state == FloatingWord.State.FADE_IN || this.state == FloatingWord.State.VISIBLE) {
         this.fadeStartAlpha = this.alpha;
         this.stateTime = 0.0;
         this.state = FloatingWord.State.FADE_OUT;
      }
   }

   public enum State {
      FADE_IN,
      VISIBLE,
      FADE_OUT,
      IDLE;
   }
}
