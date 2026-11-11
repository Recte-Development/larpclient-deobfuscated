package com.larpclient.module.render;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.render.world.ShapeBuilder;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.entity.CrystalIdAccess;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public final class CrystalChamsModule extends Module {
   private static CrystalChamsModule instance;
   private final ModeSetting model = this.addSetting(new ModeSetting("Model", "Crystal appearance", "Vanilla", "Vanilla", "Gem", "Orb", "Prism"));
   private final ModeSetting colorMode = this.addSetting(new ModeSetting("Color mode", "Crystal palette", "Theme", "Theme", "Custom", "Rainbow", "Original"));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Custom crystal tint", -1459895));
   private final NumberSetting opacity = this.addSetting(new NumberSetting("Opacity", "Crystal opacity in percent", 100.0, 10.0, 100.0, 5.0));
   private final BooleanSetting glow = this.addSetting(new BooleanSetting("Glow", "Full brightness and luminous core", true));
   private final BooleanSetting rings = this.addSetting(new BooleanSetting("Rings", "Thin orbiting rings", true));
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "Visual scale in percent", 100.0, 20.0, 300.0, 5.0));
   private final NumberSetting spinRate = this.addSetting(new NumberSetting("Spin rate", "Rotation speed in percent", 100.0, 0.0, 600.0, 10.0));
   private final NumberSetting bounce = this.addSetting(new NumberSetting("Bounce", "Bob height in percent", 100.0, 0.0, 300.0, 10.0));
   private final ModeSetting base = this.addSetting(new ModeSetting("Base", "Base plate visibility", "Default", "Default", "Always", "Never"));
   private CrystalChamsModule.Frame frame;

   public CrystalChamsModule() {
      super("Crystal Chams", "Crystal colors, motion and Gem, Orb or Prism models", Category.RENDER, -1);
      instance = this;
      this.color.visibleWhen(() -> this.colorMode.isMode("Custom"));
      this.rings.visibleWhen(() -> !this.model.isMode("Vanilla"));
   }

   public static CrystalChamsModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getAppearanceSettings() {
      return List.of(this.model, this.size, this.rings, this.base);
   }

   public List<Setting<?>> getColorSettings() {
      return List.of(this.colorMode, this.color, this.opacity, this.glow);
   }

   public List<Setting<?>> getSpinScale() {
      return List.of(this.spinRate, this.bounce);
   }

   public boolean isCustomModel() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && !this.model.isMode("Vanilla");
   }

   public float getScale() {
      return this.isRuntimeActive() ? this.size.getValue().floatValue() / 100.0F : 1.0F;
   }

   public float getSpinScale() {
      return this.isRuntimeActive() ? this.spinRate.getValue().floatValue() / 100.0F : 1.0F;
   }

   public float getBounceScale() {
      return this.isRuntimeActive() ? this.bounce.getValue().floatValue() / 100.0F : 1.0F;
   }

   public boolean isGlowEnabled() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.glow.getValue();
   }

   public boolean getAppearanceSettings() {
      return this.isEnabled() ? this.opacity.getValue() < 100.0 : false;
   }

   public boolean getScale() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && (!this.colorMode.isMode("Original") || this.getAppearanceSettings() || this.glow.getValue());
   }

   public Boolean getBaseOverride() {
      return this.isRuntimeActive() && !this.base.isMode("Default") ? this.base.isMode("Always") : null;
   }

   public int getTint(int var1) {
      double var2 = TickTimer.getTime();

      int var4 = switch ((String)this.colorMode.getValue()) {
         case "Custom" -> this.color.getValue();
         case "Original" -> -1;
         case "Rainbow" -> ColorUtil.hsbToRgb((float)((var2 * 0.12 + var1 * 0.13) % 1.0), 0.7F, 1.0F);
         default -> ColorUtil.lerpColor(Theme.accent, Theme.accentEnd, 0.5 + 0.5 * Math.sin(var2 + var1 * 0.1));
      };
      return ColorUtil.withAlpha(var4, this.opacity.getValue() / 100.0);
   }

   @Override
   public void onEnable() {
      LarpLog.debug("CrystalChams", "Native crystal appearance activated: {}", this.model.getValue());
   }

   @Override
   public void onDisable() {
      this.clearFrame();
   }

   public void clearFrame() {
      this.frame = null;
   }

   public boolean isCustomized(Object var1) {
      if (this.isEnabled()) {
         CrystalChamsModule.Frame var2 = this.frame;
         return VisualsRenderer.isRenderingEnabled() && this.isCustomModel() && var2 != null && var1 instanceof CrystalIdAccess var3
            ? var2.crystals.stream().anyMatch(var1x -> var1x.crystalId == var3.larpCrystalId())
            : false;
      } else {
         return false;
      }
   }

   public void updateFrame(float var1) {
      if (this.isEnabled()) {
         this.frame = null;
         if (this.isCustomModel() && this.isInGame()) {
            ArrayList var2 = new ArrayList();

            for (Entity var4 : getMc().level.entitiesForRendering()) {
               if (var4 instanceof EndCrystal var5 && !(var4.distanceToSqr(getMc().player) > 9216.0)) {
                  if (var2.size() >= 128) {
                     break;
                  }

                  float var6 = var5.time + var1;
                  double var7 = Math.sin(var6 * 0.2) / 2.0 + 0.5;
                  double var9 = (var7 * var7 + var7) * 0.4 * this.getBounceScale();
                  Vec3 var11 = var5.getPosition(var1);
                  Vec3 var12 = var11.add(0.0, 1.2 + var9, 0.0);
                  int var13 = this.colorMode.isMode("Original")
                     ? ColorUtil.withAlpha(-3707905, this.opacity.getValue() / 100.0)
                     : this.getTint((int)(var11.x * 7.0 + var11.z * 13.0));
                  var2.add(
                     new CrystalChamsModule.CrystalInstance(
                        var5.getId(),
                        var12,
                        var11,
                        var6,
                        Math.toRadians(var6 * 3.0F * this.getSpinScale()),
                        var13,
                        this.getBaseOverride() == null ? var5.showsBottom() : this.getBaseOverride()
                     )
                  );
               }
            }

            this.frame = new CrystalChamsModule.Frame(this.model.getValue(), this.getScale(), this.rings.getValue(), this.glow.getValue(), List.copyOf(var2));
         }
      }
   }

   public boolean isCustomized() {
      return !this.isEnabled() ? false : this.frame != null && this.frame.glow && !this.frame.crystals.isEmpty();
   }

   public void render(EffectsRenderer var1, boolean var2) {
      if (this.isEnabled()) {
         CrystalChamsModule.Frame var3 = this.frame;
         if (var3 != null && (!var2 || var3.glow)) {
            for (CrystalChamsModule.CrystalInstance var5 : var3.crystals) {
               int var7;
               ShapeBuilder var8;
               label105: {
                  int var6 = var5.tint;
                  var7 = ColorUtil.lerpColor(var6, ColorUtil.withAlpha(-1, (var6 >>> 24) / 255.0), 0.35);
                  var8 = new ShapeBuilder(var1, var5.center, new Quaternionf(), var3.scale, var2);
                  if (!var2 && var5.base) {
                     ShapeBuilder var9 = new ShapeBuilder(var1, var5.foot, new Quaternionf(), 1.0, false);
                     var9.toWorld(0.0, 0.125, 0.0, 0.375, 0.125, 0.375, -14278098);
                     var9.addRing(0.27, 0.012, new Quaternionf(), var6);
                  }

                  ShapeBuilder var15;
                  int var16;
                  int var17;
                  switch (var3.model) {
                     case "Gem":
                        var8.addBipyramid(0.13, 0.24, 0.07, -var5.angle * 1.7, var7);
                        if (!var2) {
                           var8.addBipyramid(0.36, 0.6, 0.22, var5.angle, ColorUtil.multiplyAlpha(var6, 0.85));
                        }
                        break label105;
                     case "Orb":
                        double var24 = 0.85 + 0.15 * Math.sin(var5.age * 0.25);
                        var8.setMaterial(0.0, 0.0, 0.0, 0.15 * var24, 0.15 * var24, 0.15 * var24, var7);
                        if (!var2) {
                           var8.setMaterial(0.0, 0.0, 0.0, 0.42, 0.42, 0.42, ColorUtil.multiplyAlpha(var6, 0.7));
                           var8.addRing(0.48, 0.03, new Quaternionf().rotationY((float)var5.angle).rotateX(0.35F), ColorUtil.lerpColor(var6, -1459895, 0.4));
                        }
                        break label105;
                     default:
                        float var11 = (float)Math.sqrt(0.5);
                        Quaternionf var12 = new Quaternionf().rotationY((float)var5.angle).rotateAxis((float) (Math.PI / 3), var11, 0.0F, var11);
                        Quaternionf var13 = new Quaternionf(var12)
                           .mul(new Quaternionf().setAngleAxis((float) (Math.PI / 3), var11, 0.0F, var11).rotateY((float)var5.angle * 1.4F));
                        ShapeBuilder var14 = new ShapeBuilder(var1, var5.center, var13, var3.scale, var2);
                        var14.toWorld(0.0, 0.0, 0.0, 0.126, 0.126, 0.126, var7);
                        if (var2) {
                           break label105;
                        }

                        var14.toWorld(0.0, 0.0, 0.0, 0.225, 0.225, 0.225, ColorUtil.multiplyAlpha(var6, 0.55));
                        var15 = new ShapeBuilder(var1, var5.center, var12, var3.scale, false);
                        var15.toWorld(0.0, 0.0, 0.0, 0.3, 0.3, 0.3, ColorUtil.multiplyAlpha(var6, 0.5));
                        var16 = ColorUtil.lerpColor(var6, -1459895, 0.55);
                        var17 = 0;
                  }

                  while (var17 < 3) {
                     for (byte var18 = -1; var18 <= 1; var18 += 2) {
                        for (byte var19 = -1; var19 <= 1; var19 += 2) {
                           double[] var20 = new double[3];
                           double[] var21 = new double[]{0.022, 0.022, 0.022};
                           var20[(var17 + 1) % 3] = var18 * 0.3;
                           var20[(var17 + 2) % 3] = var19 * 0.3;
                           var21[var17] = 0.322;
                           var15.toWorld(var20[0], var20[1], var20[2], var21[0], var21[1], var21[2], var16);
                        }
                     }

                     var17++;
                  }
               }

               if (var3.rings) {
                  for (int var23 = 0; var23 < 2; var23++) {
                     var8.addRing(
                        0.62 + var23 * 0.1,
                        0.008,
                        new Quaternionf().rotationY((float)(var5.angle * (var23 == 0 ? 1.6 : -1.2) + var23)).rotateX(0.5F + var23 * 0.6F),
                        ColorUtil.multiplyAlpha(var7, 0.9)
                     );
                  }
               }
            }
         }
      }
   }

   private record CrystalInstance(int id, Vec3 center, Vec3 foot, float age, double angle, int tint, boolean base) {
      private final int crystalId;

      private CrystalInstance(int id, Vec3 center, Vec3 foot, float age, double angle, int tint, boolean base) {
         this.crystalId = id;
         this.center = center;
         this.foot = foot;
         this.age = age;
         this.angle = angle;
         this.tint = tint;
         this.base = base;
      }
   }

   private record Frame(String model, double scale, boolean rings, boolean glow, List<CrystalChamsModule.CrystalInstance> crystals) {
   }
}
