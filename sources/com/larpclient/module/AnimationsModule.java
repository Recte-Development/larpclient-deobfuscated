package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.ea;
import com.larpclient.fx;
import com.larpclient.hj;
import com.larpclient.mo;
import com.larpclient.r4;
import com.larpclient.x5;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;

public class AnimationsModule extends Module {
   private final lp f = this.addSetting(new lp("Swing", "Swing preset", "Module 6", x5.B) {
      @Override
      public String getDisplayName() {
         return "Mode";
      }

      @Override
      public void setValue(String var1) {
         super.setValue(AnimationsModule.r(var1));
      }
   });
   private final ln e = this.h("Only with aura", "Use swing presets while Kill Aura has a target", false);
   private final lc T = this.r("Legacy Strength", "Classic preset strength", 20.0, 20.0, 75.0, 0.1);
   private final lc Y = this.r("Swing Strength", "Preset movement strength", 8.0, 1.0, 10.0, 0.1);
   private final lc d = this.r("Angle", "Module 2 and Module 4 angle", 100.0, 0.0, 360.0, 1.0);
   private final lc j = this.r("Turn", "Shift and Break turn", 0.0, -90.0, 90.0, 0.5);
   private final lc u = this.r("Down Strength", "Additional downward swing movement", 0.0, 0.0, 1.0, 0.05);
   private final lc t = this.r("Distance", "Preset depth", 0.25, 0.0, 0.8, 0.01);
   private final ln P = this.h("Smooth Motion", "Ease swing progress", true);
   private final lc w = this.r("Smooth Amount", "Blend cubic easing", 0.65, 0.0, 1.0, 0.05);
   private final lc s = this.r("Motion Curve", "Progress curve exponent", 1.0, 0.45, 2.5, 0.05);
   private final ln Z = this.h("Item 360", "Continuously rotate held items", false);
   private final lp W = this.addSetting(new lp("Spin Axis", "Continuous spin axis", "X", "X", "Y", "Z"));
   private final lc B = this.r("Spin Speed", "Continuous spin speed", 3.6, 0.2, 12.0, 0.1);
   private final ln O = this.h("Slow", "Override local hand swing duration", false);
   private final lc g = this.r("Speed", "Swing duration in ticks", 12.0, 1.0, 50.0, 1.0);
   private final x5.z z = new x5.z();
   private Object F;
   private Object M;
   private final hj x = new hj();
   private static AnimationsModule X;
   public static int i;
   private static final String[] a;

   public AnimationsModule() {
      super("Animations", "Customize swing presets and held-item spin", Category.VISUALS, -1);
      X = this;
      this.T.x(() -> this.f.i("Mode 2") || this.f.i("Mode 3") || this.f.i("Mode 4"));
      this.Y.x(() -> {
         return (boolean)(switch ((String)this.f.getValue()) {
            case "Module 1", "Module 2", "Module 3", "Module 4", "Module 6", "Module 7", "Shift", "Break", "Lunge", "Spear", "Jelly", "Shake" -> 1;
            default -> 0;
         });
      });
      this.u.x(() -> {
         return (boolean)(switch ((String)this.f.getValue()) {
            case "Mode 1", "Mode 2", "Mode 3", "Mode 4", "Mode 5", "Module 8" -> 0;
            default -> 1;
         });
      });
      this.t.x(() -> {
         return (boolean)(switch ((String)this.f.getValue()) {
            case "Module 2", "Module 4", "Slant", "Triangle", "Shift", "Break", "Lunge", "Spear", "Down", "Jelly", "Shake" -> 1;
            default -> 0;
         });
      });
      this.d.x(() -> this.f.i("Module 2") || this.f.i("Module 4"));
      this.j.x(() -> this.f.i("Shift") || this.f.i("Break"));
      this.w.x(this.P::getValue);
      this.W.x(this.Z::getValue);
      this.B.x(this.Z::getValue);
      this.g.x(this.O::getValue);
      this.f.W(var1 -> this.z.N());
      this.r("Duration", "Retained legacy value", 1.0, 0.25, 3.0, 0.05).x(() -> false);
      this.r("Strength", "Retained legacy value", 1.0, 0.0, 3.0, 0.05).x(() -> false);
      this.h("Only During Swing", "Retained legacy value", false).x(() -> false);
      this.h("Equip Motion", "Retained legacy value", false).x(() -> false);
      this.h("Animate Offhand", "Retained legacy value", false).x(() -> false);
   }

   private lc r(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lc(var1, var2, var3, var5, var7, var9));
   }

   private ln h(String var1, String var2, boolean var3) {
      return this.addSetting(new ln(var1, var2, var3));
   }

   public static AnimationsModule K() {
      return X;
   }

   public List<l6<?>> L() {
      return List.of(this.f, this.e, this.T, this.Y, this.d, this.j, this.u, this.t, this.P, this.O, this.g);
   }

   public List<l6<?>> R() {
      return List.of(this.w, this.s, this.Z, this.W, this.B);
   }

   @Override
   public String getDisplayName() {
      return "SwingAnimation";
   }

   public String N() {
      return this.f.getValue();
   }

   private boolean q() {
      AuraModule var1 = AuraModule.G();
      return var1 != null && var1.G() != null;
   }

   public boolean y(HumanoidArm var1) {
      if (!this.isEnabled()) {
         return false;
      }

      LocalPlayer var2 = getMc().player;
      return this.isRuntimeActive() && var2 != null && var2.getMainArm() == var1;
   }

   public boolean s(HumanoidArm var1) {
      return !this.isEnabled() ? false : this.y(var1) && (!this.e.getValue() || this.q());
   }

   public boolean j() {
      return false;
   }

   public boolean U() {
      return this.isEnabled() ? this.O.getValue() : false;
   }

   public int Y(int var1) {
      if (this.isEnabled()) {
         return this.O.getValue() ? this.g.getValue().intValue() : var1;
      } else {
         return var1;
      }
   }

   private void y() {
      Minecraft var1 = getMc();
      if (this.F != var1.player || this.M != var1.level) {
         this.z.N();
         this.x.q();
         this.F = var1.player;
         this.M = var1.level;
      }
   }

   public void O(PoseStack var1, HumanoidArm var2, float var3) {
      if (this.isEnabled()) {
         this.y();
         x5.Z(
            var1,
            this.f.getValue(),
            var2 == HumanoidArm.LEFT,
            var3,
            new x5.m(
               this.T.getValue().floatValue(),
               this.Y.getValue().floatValue(),
               this.d.getValue().floatValue(),
               this.j.getValue().floatValue(),
               this.u.getValue().floatValue(),
               this.t.getValue().floatValue(),
               this.P.getValue(),
               this.w.getValue().floatValue(),
               this.s.getValue().floatValue()
            ),
            this.z
         );
      }
   }

   public void p(PoseStack var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.Z.getValue()) {
            this.y();
            float var2 = (float)Math.toDegrees(this.x.d(ea.y(), this.B.getValue() / 4.0) % (Math.PI * 2));

            fx.b(var1, switch ((String)this.W.getValue()) {
               case "Y" -> Axis.YP.rotationDegrees(var2);
               case "Z" -> Axis.ZP.rotationDegrees(var2);
               default -> Axis.XP.rotationDegrees(var2);
            });
         }
      }
   }

   @Override
   public void onEnable() {
      this.m();
      r4.V("SwingAnimation", "Activated preset {}", this.f.getValue());
   }

   @Override
   public void onDisable() {
      this.m();
      r4.V("SwingAnimation", "Cleared swing cycle and item spin state");
   }

   private void m() {
      this.z.N();
      this.F = this.M = null;
      this.x.q();
   }

   public static String r(String var0) {
      String var2 = var0;
      byte var3 = -1;
      boolean var1 = EmotesModule.d;
      switch (var2.hashCode()) {
         case -1955878649:
            String[] var4 = a;
            if (!var2.equals(var4[6])) {
               break;
            }

            var3 = 0;
            if (!var1) {
               break;
            }

            mo.A = (boolean)(mo.A ? 0 : 1);
         case -1085510111:
            String[] var5 = a;
            if (!var2.equals(var5[11])) {
               break;
            }

            var3 = 1;
            if (!var1) {
               break;
            }
         case 2433880:
            String[] var6 = a;
            if (!var2.equals(var6[8])) {
               break;
            }

            var3 = 2;
            if (!var1) {
               break;
            }
         case 80301850:
            String[] var7 = a;
            if (!var2.equals(var7[5])) {
               break;
            }

            var3 = 3;
            if (!var1) {
               break;
            }
         case -1814666802:
            String[] var8 = a;
            if (!var2.equals(var8[0])) {
               break;
            }

            var3 = 4;
            if (!var1) {
               break;
            }
         case 79367:
            String[] var9 = a;
            if (!var2.equals(var9[14])) {
               break;
            }

            var3 = 5;
            if (!var1) {
               break;
            }
         case -138073504:
            String[] var10 = a;
            if (!var2.equals(var10[3])) {
               break;
            }

            var3 = 6;
            if (!var1) {
               break;
            }
         case 81225479:
            String[] var11 = a;
            if (!var2.equals(var11[12])) {
               break;
            }

            var3 = 7;
            if (!var1) {
               break;
            }
         case 2583650:
            String[] var12 = a;
            if (var2.equals(var12[1])) {
               var3 = 8;
            }
      }
      return switch (var3) {
         case 0, 1, 2 -> {
            String[] var17 = a;
            yield var17[13];
         }
         case 3 -> {
            String[] var16 = a;
            yield var16[2];
         }
         case 4, 5 -> {
            String[] var15 = a;
            yield var15[10];
         }
         case 6 -> {
            String[] var14 = a;
            yield var14[7];
         }
         case 7 -> {
            String[] var13 = a;
            yield var13[4];
         }
         case 8 -> a[9];
         default -> var0;
      };
   }

   public static void Q(JsonObject var0) {
      if (var0.has("Swing") && var0.get("Swing").isJsonPrimitive() && var0.getAsJsonPrimitive("Swing").isString()) {
         var0.addProperty("Swing", r(var0.get("Swing").getAsString()));
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
