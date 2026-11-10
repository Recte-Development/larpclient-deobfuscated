package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient._q;
import com.larpclient.a2;
import com.larpclient.aq;
import com.larpclient.b0;
import com.larpclient.hz;
import com.larpclient.qd;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;

public class AnimationsModule extends Module {
   private final o S = this.addSetting(new o("Swing", "Swing preset", "Module 6", a2.Y) {
      @Override
      public String getDisplayName() {
         return "Mode";
      }

      @Override
      public void setValue(String var1) {
         super.setValue(AnimationsModule.B(var1));
      }
   });
   private final m H = this.R("Only with aura", "Use swing presets while Kill Aura has a target", false);
   private final b Y = this.L("Legacy Strength", "Classic preset strength", 20.0, 20.0, 75.0, 0.1);
   private final b D = this.L("Swing Strength", "Preset movement strength", 8.0, 1.0, 10.0, 0.1);
   private final b a = this.L("Angle", "Module 2 and Module 4 angle", 100.0, 0.0, 360.0, 1.0);
   private final b n = this.L("Turn", "Shift and Break turn", 0.0, -90.0, 90.0, 0.5);
   private final b k = this.L("Down Strength", "Additional downward swing movement", 0.0, 0.0, 1.0, 0.05);
   private final b M = this.L("Distance", "Preset depth", 0.25, 0.0, 0.8, 0.01);
   private final m B = this.R("Smooth Motion", "Ease swing progress", true);
   private final b o = this.L("Smooth Amount", "Blend cubic easing", 0.65, 0.0, 1.0, 0.05);
   private final b C = this.L("Motion Curve", "Progress curve exponent", 1.0, 0.45, 2.5, 0.05);
   private final m V = this.R("Item 360", "Continuously rotate held items", false);
   private final o Q = this.addSetting(new o("Spin Axis", "Continuous spin axis", "X", "X", "Y", "Z"));
   private final b w = this.L("Spin Speed", "Continuous spin speed", 3.6, 0.2, 12.0, 0.1);
   private final m u = this.R("Slow", "Override local hand swing duration", false);
   private final b r = this.L("Speed", "Swing duration in ticks", 12.0, 1.0, 50.0, 1.0);
   private final a2.e J = new a2.e();
   private Object F;
   private Object g;
   private final b0 q = new b0();
   private static AnimationsModule t;
   public static boolean v;
   private static final String[] b;

   public AnimationsModule() {
      super("Animations", "Customize swing presets and held-item spin", Category.VISUALS, -1);
      t = this;
      this.Y.a(() -> this.S.w("Mode 2") || this.S.w("Mode 3") || this.S.w("Mode 4"));
      this.D.a(() -> {
         return (boolean)(switch ((String)this.S.getValue()) {
            case "Module 1", "Module 2", "Module 3", "Module 4", "Module 6", "Module 7", "Shift", "Break", "Lunge", "Spear", "Jelly", "Shake" -> 1;
            default -> 0;
         });
      });
      this.k.a(() -> {
         return (boolean)(switch ((String)this.S.getValue()) {
            case "Mode 1", "Mode 2", "Mode 3", "Mode 4", "Mode 5", "Module 8" -> 0;
            default -> 1;
         });
      });
      this.M.a(() -> {
         return (boolean)(switch ((String)this.S.getValue()) {
            case "Module 2", "Module 4", "Slant", "Triangle", "Shift", "Break", "Lunge", "Spear", "Down", "Jelly", "Shake" -> 1;
            default -> 0;
         });
      });
      this.a.a(() -> this.S.w("Module 2") || this.S.w("Module 4"));
      this.n.a(() -> this.S.w("Shift") || this.S.w("Break"));
      this.o.a(this.B::getValue);
      this.Q.a(this.V::getValue);
      this.w.a(this.V::getValue);
      this.r.a(this.u::getValue);
      this.S.w(var1 -> this.J.G());
      this.L("Duration", "Retained legacy value", 1.0, 0.25, 3.0, 0.05).a(() -> false);
      this.L("Strength", "Retained legacy value", 1.0, 0.0, 3.0, 0.05).a(() -> false);
      this.R("Only During Swing", "Retained legacy value", false).a(() -> false);
      this.R("Equip Motion", "Retained legacy value", false).a(() -> false);
      this.R("Animate Offhand", "Retained legacy value", false).a(() -> false);
   }

   private b L(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new b(var1, var2, var3, var5, var7, var9));
   }

   private m R(String var1, String var2, boolean var3) {
      return this.addSetting(new m(var1, var2, var3));
   }

   public static AnimationsModule a() {
      return t;
   }

   public List<r<?>> e() {
      return List.of(this.S, this.H, this.Y, this.D, this.a, this.n, this.k, this.M, this.B, this.u, this.r);
   }

   public List<r<?>> R() {
      return List.of(this.o, this.C, this.V, this.Q, this.w);
   }

   @Override
   public String getDisplayName() {
      return "SwingAnimation";
   }

   public String L() {
      return this.S.getValue();
   }

   private boolean e() {
      AuraModule var1 = AuraModule.t();
      return var1 != null && var1.R() != null;
   }

   public boolean L(HumanoidArm var1) {
      if (!this.isEnabled()) {
         return false;
      }

      LocalPlayer var2 = getMc().player;
      return this.isRuntimeActive() && var2 != null && var2.getMainArm() == var1;
   }

   public boolean A(HumanoidArm var1) {
      return !this.isEnabled() ? false : this.L(var1) && (!this.H.getValue() || this.e());
   }

   public boolean k() {
      return false;
   }

   public boolean r() {
      return this.isEnabled() ? this.u.getValue() : false;
   }

   public int k(int var1) {
      if (this.isEnabled()) {
         return this.u.getValue() ? this.r.getValue().intValue() : var1;
      } else {
         return var1;
      }
   }

   private void e() {
      Minecraft var1 = getMc();
      if (this.F != var1.player || this.g != var1.level) {
         this.J.G();
         this.q.a();
         this.F = var1.player;
         this.g = var1.level;
      }
   }

   public void X(PoseStack var1, HumanoidArm var2, float var3) {
      if (this.isEnabled()) {
         this.e();
         a2.a(
            var1,
            this.S.getValue(),
            var2 == HumanoidArm.LEFT,
            var3,
            new a2.u(
               this.Y.getValue().floatValue(),
               this.D.getValue().floatValue(),
               this.a.getValue().floatValue(),
               this.n.getValue().floatValue(),
               this.k.getValue().floatValue(),
               this.M.getValue().floatValue(),
               this.B.getValue(),
               this.o.getValue().floatValue(),
               this.C.getValue().floatValue()
            ),
            this.J
         );
      }
   }

   public void v(PoseStack var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.V.getValue()) {
            this.e();
            float var2 = (float)Math.toDegrees(this.q.v(hz.Q(), this.w.getValue() / 4.0) % (Math.PI * 2));

            qd.i(var1, switch ((String)this.Q.getValue()) {
               case "Y" -> Axis.YP.rotationDegrees(var2);
               case "Z" -> Axis.ZP.rotationDegrees(var2);
               default -> Axis.XP.rotationDegrees(var2);
            });
         }
      }
   }

   @Override
   public void onEnable() {
      this.Z();
      _q.z("SwingAnimation", "Activated preset {}", this.S.getValue());
   }

   @Override
   public void onDisable() {
      this.Z();
      _q.z("SwingAnimation", "Cleared swing cycle and item spin state");
   }

   private void Z() {
      this.J.G();
      this.F = this.g = null;
      this.q.a();
   }

   public static String B(String var0) {
      String var2 = var0;
      byte var3 = -1;
      int var1 = EmotesModule.l;
      switch (var2.hashCode()) {
         case -1955878649:
            String[] var4 = b;
            if (!var2.equals(var4[12])) {
               break;
            }

            var3 = 0;
            if (var1 == 0) {
               break;
            }

            aq.n = (boolean)(aq.n ? 0 : 1);
         case -1085510111:
            String[] var5 = b;
            if (!var2.equals(var5[4])) {
               break;
            }

            var3 = 1;
            if (var1 == 0) {
               break;
            }
         case 2433880:
            String[] var6 = b;
            if (!var2.equals(var6[9])) {
               break;
            }

            var3 = 2;
            if (var1 == 0) {
               break;
            }
         case 80301850:
            String[] var7 = b;
            if (!var2.equals(var7[1])) {
               break;
            }

            var3 = 3;
            if (var1 == 0) {
               break;
            }
         case -1814666802:
            String[] var8 = b;
            if (!var2.equals(var8[10])) {
               break;
            }

            var3 = 4;
            if (var1 == 0) {
               break;
            }
         case 79367:
            String[] var9 = b;
            if (!var2.equals(var9[5])) {
               break;
            }

            var3 = 5;
            if (var1 == 0) {
               break;
            }
         case -138073504:
            String[] var10 = b;
            if (!var2.equals(var10[14])) {
               break;
            }

            var3 = 6;
            if (var1 == 0) {
               break;
            }
         case 81225479:
            String[] var11 = b;
            if (!var2.equals(var11[8])) {
               break;
            }

            var3 = 7;
            if (var1 == 0) {
               break;
            }
         case 2583650:
            String[] var12 = b;
            if (var2.equals(var12[11])) {
               var3 = 8;
            }
      }
      return switch (var3) {
         case 0, 1, 2 -> {
            String[] var17 = b;
            yield var17[2];
         }
         case 3 -> {
            String[] var16 = b;
            yield var16[3];
         }
         case 4, 5 -> {
            String[] var15 = b;
            yield var15[0];
         }
         case 6 -> {
            String[] var14 = b;
            yield var14[13];
         }
         case 7 -> {
            String[] var13 = b;
            yield var13[6];
         }
         case 8 -> b[7];
         default -> var0;
      };
   }

   public static void K(JsonObject var0) {
      if (var0.has("Swing") && var0.get("Swing").isJsonPrimitive() && var0.getAsJsonPrimitive("Swing").isString()) {
         var0.addProperty("Swing", B(var0.get("Swing").getAsString()));
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
