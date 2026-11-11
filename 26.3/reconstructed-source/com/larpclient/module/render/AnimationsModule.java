package com.larpclient.module.render;

import com.google.gson.JsonObject;
import com.larpclient.config.ConfigManager;
import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.EmotesModule;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.SwingAnimations;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.math.PhaseAccumulator;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.PoseStackUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;

public class AnimationsModule extends Module {
   private final ModeSetting swingMode = this.addSetting(new ModeSetting("Swing", "Swing preset", "Module 6", SwingAnimations.swingModes) {
      @Override
      public String getDisplayName() {
         return "Mode";
      }

      @Override
      public void setValue(String var1) {
         super.setValue(AnimationsModule.migrateSwingMode(var1));
      }
   });
   private final BooleanSetting onlyWithAura = this.getSpinSettings("Only with aura", "Use swing presets while Kill Aura has a target", false);
   private final NumberSetting legacyStrength = this.getSwingMode("Legacy Strength", "Classic preset strength", 20.0, 20.0, 75.0, 0.1);
   private final NumberSetting swingStrength = this.getSwingMode("Swing Strength", "Preset movement strength", 8.0, 1.0, 10.0, 0.1);
   private final NumberSetting angle = this.getSwingMode("Angle", "Module 2 and Module 4 angle", 100.0, 0.0, 360.0, 1.0);
   private final NumberSetting turn = this.getSwingMode("Turn", "Shift and Break turn", 0.0, -90.0, 90.0, 0.5);
   private final NumberSetting downStrength = this.getSwingMode("Down Strength", "Additional downward swing movement", 0.0, 0.0, 1.0, 0.05);
   private final NumberSetting distance = this.getSwingMode("Distance", "Preset depth", 0.25, 0.0, 0.8, 0.01);
   private final BooleanSetting smoothMotion = this.getSpinSettings("Smooth Motion", "Ease swing progress", true);
   private final NumberSetting smoothAmount = this.getSwingMode("Smooth Amount", "Blend cubic easing", 0.65, 0.0, 1.0, 0.05);
   private final NumberSetting motionCurve = this.getSwingMode("Motion Curve", "Progress curve exponent", 1.0, 0.45, 2.5, 0.05);
   private final BooleanSetting item360 = this.getSpinSettings("Item 360", "Continuously rotate held items", false);
   private final ModeSetting spinAxis = this.addSetting(new ModeSetting("Spin Axis", "Continuous spin axis", "X", "X", "Y", "Z"));
   private final NumberSetting spinSpeed = this.getSwingMode("Spin Speed", "Continuous spin speed", 3.6, 0.2, 12.0, 0.1);
   private final BooleanSetting slowSwing = this.getSpinSettings("Slow", "Override local hand swing duration", false);
   private final NumberSetting swingSpeed = this.getSwingMode("Speed", "Swing duration in ticks", 12.0, 1.0, 50.0, 1.0);
   private final SwingAnimations.TriangleSpin triangleSpin = new SwingAnimations.TriangleSpin();
   private Object lastPlayer;
   private Object lastLevel;
   private final PhaseAccumulator spinPhase = new PhaseAccumulator();
   private static AnimationsModule instance;
   public static boolean swingActive;
   private static final String[] legacyModeNames;

   public AnimationsModule() {
      super("Animations", "Customize swing presets and held-item spin", Category.VISUALS, -1);
      instance = this;
      this.legacyStrength.visibleWhen(() -> this.swingMode.isMode("Mode 2") || this.swingMode.isMode("Mode 3") || this.swingMode.isMode("Mode 4"));
      this.swingStrength.visibleWhen(() -> {
         return (boolean)(switch ((String)this.swingMode.getValue()) {
            case "Module 1", "Module 2", "Module 3", "Module 4", "Module 6", "Module 7", "Shift", "Break", "Lunge", "Spear", "Jelly", "Shake" -> 1;
            default -> 0;
         });
      });
      this.downStrength.visibleWhen(() -> {
         return (boolean)(switch ((String)this.swingMode.getValue()) {
            case "Mode 1", "Mode 2", "Mode 3", "Mode 4", "Mode 5", "Module 8" -> 0;
            default -> 1;
         });
      });
      this.distance.visibleWhen(() -> {
         return (boolean)(switch ((String)this.swingMode.getValue()) {
            case "Module 2", "Module 4", "Slant", "Triangle", "Shift", "Break", "Lunge", "Spear", "Down", "Jelly", "Shake" -> 1;
            default -> 0;
         });
      });
      this.angle.visibleWhen(() -> this.swingMode.isMode("Module 2") || this.swingMode.isMode("Module 4"));
      this.turn.visibleWhen(() -> this.swingMode.isMode("Shift") || this.swingMode.isMode("Break"));
      this.smoothAmount.visibleWhen(this.smoothMotion::getValue);
      this.spinAxis.visibleWhen(this.item360::getValue);
      this.spinSpeed.visibleWhen(this.item360::getValue);
      this.swingSpeed.visibleWhen(this.slowSwing::getValue);
      this.swingMode.onChange(var1 -> this.triangleSpin.reset());
      this.getSwingMode("Duration", "Retained legacy value", 1.0, 0.25, 3.0, 0.05).visibleWhen(() -> false);
      this.getSwingMode("Strength", "Retained legacy value", 1.0, 0.0, 3.0, 0.05).visibleWhen(() -> false);
      this.getSpinSettings("Only During Swing", "Retained legacy value", false).visibleWhen(() -> false);
      this.getSpinSettings("Equip Motion", "Retained legacy value", false).visibleWhen(() -> false);
      this.getSpinSettings("Animate Offhand", "Retained legacy value", false).visibleWhen(() -> false);
   }

   private NumberSetting getSwingMode(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   private BooleanSetting getSpinSettings(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting(var1, var2, var3));
   }

   public static AnimationsModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getSwingSettings() {
      return List.of(
         this.swingMode,
         this.onlyWithAura,
         this.legacyStrength,
         this.swingStrength,
         this.angle,
         this.turn,
         this.downStrength,
         this.distance,
         this.smoothMotion,
         this.slowSwing,
         this.swingSpeed
      );
   }

   public List<Setting<?>> getSpinSettings() {
      return List.of(this.smoothAmount, this.motionCurve, this.item360, this.spinAxis, this.spinSpeed);
   }

   @Override
   public String getDisplayName() {
      return "SwingAnimation";
   }

   public String getSwingMode() {
      return this.swingMode.getValue();
   }

   private boolean getSwingSettings() {
      AuraModule var1 = AuraModule.getInstance();
      return var1 != null && var1.applyMoveFix() != null;
   }

   public boolean getSwingMode(HumanoidArm var1) {
      if (!this.isEnabled()) {
         return false;
      }

      LocalPlayer var2 = getMc().player;
      return this.isRuntimeActive() && var2 != null && var2.getMainArm() == var1;
   }

   public boolean shouldAnimateArm(HumanoidArm var1) {
      return !this.isEnabled() ? false : this.getSwingMode(var1) && (!this.onlyWithAura.getValue() || this.getSwingSettings());
   }

   public boolean getSwingDuration() {
      return false;
   }

   public boolean isSlowSwing() {
      return this.isEnabled() ? this.slowSwing.getValue() : false;
   }

   public int getSwingDuration(int var1) {
      if (this.isEnabled()) {
         return this.slowSwing.getValue() ? this.swingSpeed.getValue().intValue() : var1;
      } else {
         return var1;
      }
   }

   private void getSwingSettings() {
      Minecraft var1 = getMc();
      if (this.lastPlayer != var1.player || this.lastLevel != var1.level) {
         this.triangleSpin.reset();
         this.spinPhase.reset();
         this.lastPlayer = var1.player;
         this.lastLevel = var1.level;
      }
   }

   public void applySwing(PoseStack var1, HumanoidArm var2, float var3) {
      if (this.isEnabled()) {
         this.getSwingSettings();
         SwingAnimations.applySwing(
            var1,
            this.swingMode.getValue(),
            var2 == HumanoidArm.LEFT,
            var3,
            new SwingAnimations.SwingParams(
               this.legacyStrength.getValue().floatValue(),
               this.swingStrength.getValue().floatValue(),
               this.angle.getValue().floatValue(),
               this.turn.getValue().floatValue(),
               this.downStrength.getValue().floatValue(),
               this.distance.getValue().floatValue(),
               this.smoothMotion.getValue(),
               this.smoothAmount.getValue().floatValue(),
               this.motionCurve.getValue().floatValue()
            ),
            this.triangleSpin
         );
      }
   }

   public void applyItemSpin(PoseStack var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.item360.getValue()) {
            this.getSwingSettings();
            float var2 = (float)Math.toDegrees(this.spinPhase.advance(TickTimer.getTime(), this.spinSpeed.getValue() / 4.0) % (Math.PI * 2));

            PoseStackUtil.rotate(var1, switch ((String)this.spinAxis.getValue()) {
               case "Y" -> Axis.YP.rotationDegrees(var2);
               case "Z" -> Axis.ZP.rotationDegrees(var2);
               default -> Axis.XP.rotationDegrees(var2);
            });
         }
      }
   }

   @Override
   public void onEnable() {
      this.resetState();
      LarpLog.info("SwingAnimation", "Activated preset {}", this.swingMode.getValue());
   }

   @Override
   public void onDisable() {
      this.resetState();
      LarpLog.info("SwingAnimation", "Cleared swing cycle and item spin state");
   }

   private void resetState() {
      this.triangleSpin.reset();
      this.lastPlayer = this.lastLevel = null;
      this.spinPhase.reset();
   }

   public static String migrateSwingMode(String var0) {
      String var2 = var0;
      byte var3 = -1;
      int var1 = EmotesModule.counter;
      switch (var2.hashCode()) {
         case -1955878649:
            String[] var4 = legacyModeNames;
            if (!var2.equals(var4[12])) {
               break;
            }

            var3 = 0;
            if (var1 == 0) {
               break;
            }

            ConfigManager.toggle1 = (boolean)(ConfigManager.toggle1 ? 0 : 1);
         case -1085510111:
            String[] var5 = legacyModeNames;
            if (!var2.equals(var5[4])) {
               break;
            }

            var3 = 1;
            if (var1 == 0) {
               break;
            }
         case 2433880:
            String[] var6 = legacyModeNames;
            if (!var2.equals(var6[9])) {
               break;
            }

            var3 = 2;
            if (var1 == 0) {
               break;
            }
         case 80301850:
            String[] var7 = legacyModeNames;
            if (!var2.equals(var7[1])) {
               break;
            }

            var3 = 3;
            if (var1 == 0) {
               break;
            }
         case -1814666802:
            String[] var8 = legacyModeNames;
            if (!var2.equals(var8[10])) {
               break;
            }

            var3 = 4;
            if (var1 == 0) {
               break;
            }
         case 79367:
            String[] var9 = legacyModeNames;
            if (!var2.equals(var9[5])) {
               break;
            }

            var3 = 5;
            if (var1 == 0) {
               break;
            }
         case -138073504:
            String[] var10 = legacyModeNames;
            if (!var2.equals(var10[14])) {
               break;
            }

            var3 = 6;
            if (var1 == 0) {
               break;
            }
         case 81225479:
            String[] var11 = legacyModeNames;
            if (!var2.equals(var11[8])) {
               break;
            }

            var3 = 7;
            if (var1 == 0) {
               break;
            }
         case 2583650:
            String[] var12 = legacyModeNames;
            if (var2.equals(var12[11])) {
               var3 = 8;
            }
      }
      return switch (var3) {
         case 0, 1, 2 -> {
            String[] var17 = legacyModeNames;
            yield var17[2];
         }
         case 3 -> {
            String[] var16 = legacyModeNames;
            yield var16[3];
         }
         case 4, 5 -> {
            String[] var15 = legacyModeNames;
            yield var15[0];
         }
         case 6 -> {
            String[] var14 = legacyModeNames;
            yield var14[13];
         }
         case 7 -> {
            String[] var13 = legacyModeNames;
            yield var13[6];
         }
         case 8 -> legacyModeNames[7];
         default -> var0;
      };
   }

   public static void migrateConfig(JsonObject var0) {
      if (var0.has("Swing") && var0.get("Swing").isJsonPrimitive() && var0.getAsJsonPrimitive("Swing").isString()) {
         var0.addProperty("Swing", migrateSwingMode(var0.get("Swing").getAsString()));
      }

      if (var0.has("Swing Speed")) {
         if (!var0.has("Duration")) {
            try {
               double var1 = var0.get("Swing Speed").getAsDouble();
               if (Double.isFinite(var1)) {
                  var0.addProperty("Duration", Math.clamp(var1 / 6.0, 0.25, 3.0));
               }
            } catch (RuntimeException var3) {
            }
         }

         var0.remove("Swing Speed");
      }
   }
}
