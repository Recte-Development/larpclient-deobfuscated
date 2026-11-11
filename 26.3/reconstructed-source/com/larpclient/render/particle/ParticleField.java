package com.larpclient.render.particle;

import java.util.Random;

public final class ParticleField {
   public static final int cOUNT = 160;
   private final ParticleField.Particle[] particles = new ParticleField.Particle[160];
   private long lastTime;
   private double phase;

   public ParticleField() {
      Random var1 = new Random(14044L);

      for (int var2 = 0; var2 < this.particles.length; var2++) {
         this.particles[var2] = new ParticleField.Particle(
            var1.nextDouble(), var1.nextDouble(), 0.25 + var1.nextDouble() * 0.75, var1.nextDouble() * Math.PI * 2.0
         );
      }
   }

   public void update(long var1, boolean var3) {
      if (var3 && this.lastTime != 0L && var1 > this.lastTime && var1 - this.lastTime <= 500000000L) {
         this.phase = this.phase + Math.min(0.1, (var1 - this.lastTime) / 1.0E9);
      }

      this.lastTime = var1;
   }

   public double getPhase() {
      return this.phase;
   }

   public ParticleField.Particle getParticle(int var1) {
      return this.particles[var1];
   }

   public static double fract(double var0) {
      return var0 - Math.floor(var0);
   }

   public record Particle(double x, double y, double depth, double phase) {
      private final double posX;
      private final double posY;

      public Particle(double x, double y, double depth, double phase) {
         this.posX = x;
         this.posY = y;
         this.depth = depth;
         this.phase = phase;
      }
   }
}
