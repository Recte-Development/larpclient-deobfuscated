package com.larpclient.module.render;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.world.WorldShapeRenderer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.math.Easing;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.phys.Vec3;

public final class PearlParticlesModule extends Module {
   private static PearlParticlesModule instance;
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Particle tint", -8866817));
   private final NumberSetting spawnRate = this.addNumberSetting("Spawn Rate", "Particles per pearl each tick", 10.0, 1.0, 10.0, 1.0);
   private final NumberSetting size = this.addNumberSetting("Size", "Billboard width in blocks", 0.3, 0.05, 0.5, 0.01);
   private final NumberSetting lifetime = this.addNumberSetting("Lifetime", "Seconds", 0.5, 0.5, 3.0, 0.1);
   private final NumberSetting spread = this.addNumberSetting("Spread", "Initial velocity spread", 0.2, 0.01, 0.2, 0.01);
   private final NumberSetting gravity = this.addNumberSetting("Gravity", "Downward acceleration per tick", 0.008, 0.0, 0.03, 0.001);
   private final NumberSetting fadeSpeed = this.addNumberSetting("Fade Speed", "Opacity fade exponent", 2.0, 0.3, 2.0, 0.1);
   private final BooleanSetting colorAnimation = this.addSetting(new BooleanSetting("Color Animation", "Animate the theme gradient", false));
   private final HashMap<Integer, ThrownEnderpearl> trackedPearls = new HashMap<>();
   private final ArrayList<PearlParticlesModule.Particle> particles = new ArrayList<>();
   private final Random random = new Random();
   private Object lastLevel;
   private Object lastPlayer;
   private Vec3 lastPlayerPos;
   private int particleCounter;

   public PearlParticlesModule() {
      super("Pearl Particles", "Add glowing trails to ender pearls", Category.VISUALS, -1);
      instance = this;
      this.color.visibleWhen(() -> !this.colorAnimation.getValue());
   }

   public List<Setting<?>> getEmissionSettings() {
      return List.of(this.spawnRate, this.size, this.lifetime);
   }

   public List<Setting<?>> getColorSettings() {
      return List.of(this.colorAnimation, this.color);
   }

   public List<Setting<?>> getPhysicsSettings() {
      return List.of(this.spread, this.gravity, this.fadeSpeed);
   }

   private NumberSetting addNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   public static PearlParticlesModule getInstance() {
      return instance;
   }

   public void reset() {
      this.trackedPearls.clear();
      this.particles.clear();
      this.lastLevel = this.lastPlayer = null;
      this.lastPlayerPos = null;
      this.particleCounter = 0;
   }

   @Override
   public void onEnable() {
      this.reset();
      LarpLog.info("PearlParticles", "Enabled; max 32 observed pearls, 4096 particles; source bloom sprite");
   }

   @Override
   public void onDisable() {
      this.reset();
   }

   @Override
   public void onTick() {
      if (this.isRuntimeActive() && this.isInGame()) {
         Vec3 var1;
         label62: {
            var1 = this.getPlayer().position();
            if (this.lastLevel == getMc().level && this.lastPlayer == this.getPlayer()) {
               if (this.lastPlayerPos == null) {
                  break label62;
               }

               double var14;
               int var10000 = (var14 = var1.distanceToSqr(this.lastPlayerPos) - 256.0) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
               boolean var13 = false;
               int var12 = var10000;
               if ((-Integer.signum(var12) >>> 31 ^ 1) != 0) {
                  break label62;
               }
            }

            this.reset();
         }

         this.lastLevel = getMc().level;
         this.lastPlayer = this.getPlayer();
         this.lastPlayerPos = var1;
         double var2 = TickTimer.getTickTime();
         this.trackedPearls.entrySet().removeIf(var0 -> getMc().level.getEntity(var0.getKey()) != var0.getValue() || var0.getValue().isRemoved());
         HashSet var4 = new HashSet();
         int var5 = 0;

         for (Entity var7 : getMc().level.entitiesForRendering()) {
            if (var7 instanceof ThrownEnderpearl var8 && !var8.isRemoved() && !(var8.distanceToSqr(this.getPlayer()) > 16384.0)) {
               if (++var5 > 32) {
                  break;
               }

               var4.add(var8.getId());
               if (this.trackedPearls.get(var8.getId()) == var8) {
                  double var9 = this.spread.getValue();

                  for (int var11 = 0; var11 < this.spawnRate.getValue().intValue() && this.particles.size() < 4096; var11++) {
                     this.particles
                        .add(
                           new PearlParticlesModule.Particle(
                              var8.position(),
                              new Vec3(
                                 (this.random.nextDouble() * 2.0 - 1.0) * var9,
                                 (this.random.nextDouble() * 2.0 - 1.0) * var9 * 0.25,
                                 (this.random.nextDouble() * 2.0 - 1.0) * var9
                              ),
                              this.gravity.getValue(),
                              var2,
                              this.particleCounter++
                           )
                        );
                  }
               }

               if (this.trackedPearls.size() < 32 || this.trackedPearls.containsKey(var8.getId())) {
                  this.trackedPearls.put(var8.getId(), var8);
               }
            }
         }

         this.trackedPearls.keySet().retainAll(var4);
         this.particles.removeIf(var3 -> var3.tickAndIsExpired(var2, this.lifetime.getValue()));
      } else {
         this.reset();
      }
   }

   public boolean hasVisibleParticles(double var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.lastLevel == getMc().level) {
            for (PearlParticlesModule.Particle var4 : this.particles) {
               double var5 = this.reset(var4, var1);
               if (var5 >= 0.001 && ColorUtil.multiplyAlpha(this.reset(var4, var1), var5) >>> 24 != 0) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private double reset(PearlParticlesModule.Particle var1, double var2) {
      double var4 = Math.clamp((var2 - var1.spawnTime) / this.lifetime.getValue(), 0.0, 1.0);
      return Math.pow(1.0 - var4, this.fadeSpeed.getValue());
   }

   private int reset(PearlParticlesModule.Particle var1, double var2) {
      return this.colorAnimation.getValue()
         ? ColorUtil.lerpColor(Theme.accent, Theme.accentEnd, 0.5 + 0.5 * Math.sin(var2 * 2.0 + var1.seed * 0.04))
         : this.color.getValue();
   }

   public void renderParticles(PoseStack var1, EffectBatch var2, CameraRenderState var3, double var4, boolean var6) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.lastLevel == getMc().level) {
            WorldShapeRenderer var7 = new WorldShapeRenderer(var1, var2, var3);

            for (PearlParticlesModule.Particle var9 : this.particles) {
               double var10 = this.reset(var9, var4);
               if (!(var10 < 0.001)) {
                  Vec3 var12 = var9.prevPos.lerp(var9.pos, TickTimer.getPartialTick());
                  int var13 = this.reset(var9, var4);
                  double var14 = Math.clamp((var4 - var9.spawnTime) / this.lifetime.getValue(), 0.0, 1.0);
                  var13 = ColorUtil.lerpColor(var13, var13 & 0xFF000000 | 16777215, 0.22 * Math.pow(1.0 - var14, 4.0));
                  var7.drawPearl(var12, hasVisibleParticles(this.size.getValue(), var14, var9.seed), ColorUtil.multiplyAlpha(var13, var10));
               }
            }
         }
      }
   }

   public static double hasVisibleParticles(double var0, double var2, int var4) {
      int var5 = var4 * -1640531527;
      var5 ^= var5 >>> 16;
      double var6 = 0.74 + 0.26 * (var5 >>> 8 & 65535) / 65535.0;
      return var0 * var6 * (1.0 - 0.55 * Easing.smoothstep(var2));
   }

   private static final class Particle {
      Vec3 pos;
      Vec3 prevPos;
      Vec3 velocity;
      final double gravity;
      final double spawnTime;
      final int seed;

      Particle(Vec3 var1, Vec3 var2, double var3, double var5, int var7) {
         this.pos = this.prevPos = var1;
         this.velocity = var2;
         this.gravity = var3;
         this.spawnTime = var5;
         this.seed = var7;
      }

      boolean tickAndIsExpired(double var1, double var3) {
         this.prevPos = this.pos;
         this.pos = this.pos.add(this.velocity);
         this.velocity = this.velocity.add(0.0, -this.gravity, 0.0).scale(0.96);
         return var1 - this.spawnTime >= var3;
      }
   }
}
