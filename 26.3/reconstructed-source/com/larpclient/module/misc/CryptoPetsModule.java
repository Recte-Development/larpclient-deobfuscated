package com.larpclient.module.misc;

import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.social.pet.CryptoCoinPets;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.joml.Quaternionf;

public final class CryptoPetsModule extends Module {
   public static final int mAX_COINS = 12;
   private static CryptoPetsModule instance;
   private final Map<CryptoCoinPets.Coin, BooleanSetting> coinSettings = new LinkedHashMap<>();
   private final NumberSetting count = this.addSetting(new NumberSetting("Count", "How many coins circle you", 5.0, 1.0, 12.0, 1.0));
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "Coin size", 1.3, 0.5, 2.5, 0.05));
   private final NumberSetting radius = this.addSetting(new NumberSetting("Radius", "Distance from you in blocks", 1.2, 0.6, 3.0, 0.05));
   private final NumberSetting orbitSpeed = this.addSetting(new NumberSetting("Orbit speed", "How fast the ring turns", 1.0, 0.0, 3.0, 0.05));
   private final BooleanSetting sparkleTrail = this.addSetting(new BooleanSetting("Sparkle trail", "Sparks follow each coin", true));
   private final BooleanSetting inFirstPerson = this.addSetting(new BooleanSetting("In first person", "Show the coins in first person", true));
   private Object level;
   private Object player;
   private final double[] angleOffsets = new double[12];
   private final double[] coinVisibility = new double[12];
   private double orbitAngle;
   private double swingProgress = 1.0;
   private double time;
   private double lastTime;
   private double sparkAccumulator;
   private boolean wasSwinging;
   private boolean initialized;
   private final ArrayDeque<CryptoPetsModule.Spark> sparks = new ArrayDeque<>();
   private final Random random = new Random(1129470288L);
   private CryptoPetsModule.Frame frame;

   public CryptoPetsModule() {
      super("Crypto Pets", "Spinning crypto coins that circle you", Category.RENDER, -1);
      instance = this;

      for (CryptoCoinPets.Coin var2 : CryptoCoinPets.cOINS) {
         this.coinSettings.put(var2, this.addSetting(new BooleanSetting(var2.style(), "Include " + var2.style() + " coins", true)));
      }
   }

   public static CryptoPetsModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getCoinSettings() {
      return List.copyOf(this.coinSettings.values());
   }

   public List<Setting<?>> getAppearanceSettings() {
      return List.of(this.count, this.size, this.radius, this.orbitSpeed, this.sparkleTrail, this.inFirstPerson);
   }

   @Override
   public void onEnable() {
      this.reset();
      LarpLog.debug("Pets", "Crypto Pets activated: {} coins", this.count.getValue().intValue());
   }

   @Override
   public void onDisable() {
      this.reset();
   }

   public void reset() {
      this.frame = null;
      this.level = this.player = null;
      this.sparks.clear();
      this.sparkAccumulator = 0.0;
      this.time = this.orbitAngle = 0.0;
      this.swingProgress = 1.0;
      this.wasSwinging = this.initialized = (boolean)0;
      Arrays.fill(this.angleOffsets, 0.0);
      Arrays.fill(this.coinVisibility, 0.0);
      this.lastTime = TickTimer.getTime();
      this.random.setSeed(1129470288L);
   }

   private List<CryptoCoinPets.Coin> getEnabledCoins() {
      ArrayList var1 = new ArrayList();
      this.coinSettings.forEach((var1x, var2) -> {
         if (var2.getValue()) {
            var1.add(var1x);
         }
      });
      return var1;
   }

   public void updateFrame(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.frame = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            Minecraft var3 = getMc();
            if (this.level != var3.level || this.player != var3.player) {
               this.reset();
               this.level = var3.level;
               this.player = var3.player;
            }

            double var4 = Math.clamp(TickTimer.getTime() - this.lastTime, 0.0, 0.1);
            this.lastTime = TickTimer.getTime();
            this.time += var4;
            List var6 = this.getEnabledCoins();
            int var7 = var6.isEmpty() ? 0 : this.count.getValue().intValue();
            double var8 = this.initialized ? 1.0 - Math.exp(-6.0 * var4) : 1.0;

            for (int var10 = 0; var10 < 12; var10++) {
               if (var10 < var7) {
                  this.angleOffsets[var10] = this.angleOffsets[var10] + ((Math.PI * 2) * var10 / var7 - this.angleOffsets[var10]) * var8;
               }

               this.coinVisibility[var10] = this.coinVisibility[var10] + ((var10 < var7 ? 1 : 0) - this.coinVisibility[var10]) * var8;
            }

            this.initialized = (boolean)1;
            this.orbitAngle = (this.orbitAngle + var4 * this.orbitSpeed.getValue() * 0.9) % (Math.PI * 2);
            if (SwingHelper.isSwinging(var3.player) && !this.wasSwinging) {
               this.swingProgress = 0.0;
            }

            this.wasSwinging = SwingHelper.isSwinging(var3.player);
            this.swingProgress = Math.min(1.0, this.swingProgress + var4 / 0.65);
            this.sparks.removeIf(var1x -> this.time - var1x.birth > var1x.lifetime);
            if (!this.sparkleTrail.getValue()) {
               this.sparks.clear();
               this.sparkAccumulator = 0.0;
            }

            int var29 = var3.options.getCameraType().isFirstPerson() && !this.inFirstPerson.getValue() ? 0 : 1;
            if (var29 != 0 && !var6.isEmpty()) {
               CryptoCoinPets.loadAssets();
               Vec3 var11 = var3.player.getEyePosition(var1);
               double var12 = this.swingProgress < 1.0 ? (1.0 - Math.pow(1.0 - this.swingProgress, 3.0)) * Math.PI * 2.0 : 0.0;
               ArrayList var14 = new ArrayList();

               for (int var15 = 0; var15 < 12; var15++) {
                  if (!(this.coinVisibility[var15] < 0.01)) {
                     CryptoCoinPets.Coin var16 = (CryptoCoinPets.Coin)var6.get(var15 % var6.size());
                     double var17 = coinScale(var16, this.size.getValue()) * this.coinVisibility[var15];
                     double var19 = this.orbitAngle + this.angleOffsets[var15];
                     Vec3 var21 = var11.add(
                        Math.cos(var19) * this.radius.getValue(),
                        -0.35 + Math.sin(this.time * 2.1 + var15 * 1.7) * 0.06,
                        Math.sin(var19) * this.radius.getValue()
                     );
                     BlockHitResult var22 = var3.level.clip(new ClipContext(var11, var21, Block.COLLIDER, Fluid.NONE, var3.player));
                     if (var22.getType() == Type.BLOCK) {
                        var21 = var22.getLocation().subtract(var21.subtract(var11).normalize().scale(0.3 * var17));
                     }

                     BlockHitResult var23 = var3.level.clip(new ClipContext(var21, var21.add(0.0, -4.5, 0.0), Block.COLLIDER, Fluid.ANY, var3.player));
                     Vec3 var24 = var23.getType() == Type.BLOCK ? var23.getLocation().add(0.0, 0.012, 0.0) : null;
                     double var25 = var24 == null ? 0.0 : Math.pow(Math.max(0.0, 1.0 - (var21.y - var24.y) / 4.5), 2.0) * 0.4 * this.coinVisibility[var15];
                     Quaternionf var27 = new Quaternionf().rotationY((float)((this.time * 1.6 + var15 * 0.9 + var12) % (Math.PI * 2)));
                     var14.add(new CryptoPetsModule.Coin(var16, var21, var27, var17, var24, var25));
                     if (this.sparkleTrail.getValue() && var4 > 0.0 && this.random.nextDouble() < var4 * 15.0) {
                        Vec3 var28 = var21.add(
                           this.random.nextGaussian() * 0.06 * var17, this.random.nextGaussian() * 0.06 * var17, this.random.nextGaussian() * 0.06 * var17
                        );
                        this.sparks
                           .addLast(
                              new CryptoPetsModule.Spark(
                                 var28, this.time, 0.8 + this.random.nextDouble() * 0.8, 0.8 + this.random.nextDouble() * 0.8, var16.spark()
                              )
                           );

                        while (this.sparks.size() > 96) {
                           this.sparks.removeFirst();
                        }
                     }
                  }
               }

               this.frame = new CryptoPetsModule.Frame(List.copyOf(var14), this.time, this.sparkleTrail.getValue() ? 0.5 : 0.0, List.copyOf(this.sparks));
            }
         }
      }
   }

   public static double coinScale(CryptoCoinPets.Coin var0, double var1) {
      return var1 * var0.scale();
   }

   public boolean hasGlow() {
      return !this.isEnabled() ? false : this.frame != null && this.frame.glowStrength > 0.0 && !this.frame.coins.isEmpty();
   }

   public void render(EffectsRenderer var1, boolean var2) {
      if (this.isEnabled()) {
         CryptoPetsModule.Frame var3 = this.frame;
         if (var3 != null && (!var2 || !(var3.glowStrength <= 0.0))) {
            for (CryptoPetsModule.Coin var5 : var3.coins) {
               if (!var2) {
                  if (var5.shadow != null) {
                     var1.drawDisc(var5.shadow, 0.18 * var5.scale, 0.16 * var5.scale, ColorUtil.withAlpha(-16777216, var5.shadowAlpha));
                  }

                  CryptoCoinPets.renderInWorld(var1, var5.kind, var5.center, var5.rotation, (float)var5.scale);
               }
            }

            for (CryptoPetsModule.Spark var9 : var3.sparks) {
               double var6 = Math.clamp(1.0 - (var3.time - var9.birth) / var9.lifetime, 0.0, 1.0);
               var1.drawSprite(
                  var9.point.x,
                  var9.point.y + 0.08 * (1.0 - var6),
                  var9.point.z,
                  0.035 * this.size.getValue() * var6 * var9.scale,
                  var3.time,
                  ColorUtil.withAlpha(var9.color, var6 * var6 * var3.glowStrength)
               );
            }
         }
      }
   }

   public record Coin(CryptoCoinPets.Coin kind, Vec3 center, Quaternionf rotation, double scale, Vec3 shadow, double shadowAlpha) {
   }

   public record Frame(List<CryptoPetsModule.Coin> coins, double time, double glowStrength, List<CryptoPetsModule.Spark> sparks) {
   }

   private record Spark(Vec3 point, double birth, double lifetime, double scale, int color) {
   }
}
