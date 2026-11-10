package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.cf;
import com.larpclient.g_;
import com.larpclient.gg;
import com.larpclient.p5;
import com.larpclient.uf;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;

public class AnimationsModule extends Module {
   private final l D = this.addSetting(new l("Swing", "Swing preset", "Module 6", g_.J) {
      @Override
      public String getDisplayName() {
         return "Mode";
      }

      @Override
      public void setValue(String var1) {
         super.setValue(AnimationsModule.y(var1));
      }
   });
   private final s c = this.d("Only with aura", "Use swing presets while Kill Aura has a target", false);
   private final e Q = this.Q("Legacy Strength", "Classic preset strength", 20.0, 20.0, 75.0, 0.1);
   private final e a = this.Q("Swing Strength", "Preset movement strength", 8.0, 1.0, 10.0, 0.1);
   private final e h = this.Q("Angle", "Module 2 and Module 4 angle", 100.0, 0.0, 360.0, 1.0);
   private final e I = this.Q("Turn", "Shift and Break turn", 0.0, -90.0, 90.0, 0.5);
   private final e f = this.Q("Down Strength", "Additional downward swing movement", 0.0, 0.0, 1.0, 0.05);
   private final e O = this.Q("Distance", "Preset depth", 0.25, 0.0, 0.8, 0.01);
   private final s q = this.d("Smooth Motion", "Ease swing progress", true);
   private final e F = this.Q("Smooth Amount", "Blend cubic easing", 0.65, 0.0, 1.0, 0.05);
   private final e u = this.Q("Motion Curve", "Progress curve exponent", 1.0, 0.45, 2.5, 0.05);
   private final s A = this.d("Item 360", "Continuously rotate held items", false);
   private final l j = this.addSetting(new l("Spin Axis", "Continuous spin axis", "X", "X", "Y", "Z"));
   private final e m = this.Q("Spin Speed", "Continuous spin speed", 3.6, 0.2, 12.0, 0.1);
   private final s N = this.d("Slow", "Override local hand swing duration", false);
   private final e X = this.Q("Speed", "Swing duration in ticks", 12.0, 1.0, 50.0, 1.0);
   private final g_.t x = new g_.t();
   private Object g;
   private Object H;
   private final p5 y = new p5();
   private static AnimationsModule l;
   public static boolean E;
   private static final String[] b;

   public AnimationsModule() {
      super("Animations", "Customize swing presets and held-item spin", Category.VISUALS, -1);
      l = this;
      this.Q.P(() -> this.D.O("Mode 2") || this.D.O("Mode 3") || this.D.O("Mode 4"));
      this.a.P(() -> {
         return (boolean)(switch ((String)this.D.getValue()) {
            case "Module 1", "Module 2", "Module 3", "Module 4", "Module 6", "Module 7", "Shift", "Break", "Lunge", "Spear", "Jelly", "Shake" -> 1;
            default -> 0;
         });
      });
      this.f.P(() -> {
         return (boolean)(switch ((String)this.D.getValue()) {
            case "Mode 1", "Mode 2", "Mode 3", "Mode 4", "Mode 5", "Module 8" -> 0;
            default -> 1;
         });
      });
      this.O.P(() -> {
         return (boolean)(switch ((String)this.D.getValue()) {
            case "Module 2", "Module 4", "Slant", "Triangle", "Shift", "Break", "Lunge", "Spear", "Down", "Jelly", "Shake" -> 1;
            default -> 0;
         });
      });
      this.h.P(() -> this.D.O("Module 2") || this.D.O("Module 4"));
      this.I.P(() -> this.D.O("Shift") || this.D.O("Break"));
      this.F.P(this.q::getValue);
      this.j.P(this.A::getValue);
      this.m.P(this.A::getValue);
      this.X.P(this.N::getValue);
      this.D.o(var1 -> this.x.z());
      this.Q("Duration", "Retained legacy value", 1.0, 0.25, 3.0, 0.05).P(() -> false);
      this.Q("Strength", "Retained legacy value", 1.0, 0.0, 3.0, 0.05).P(() -> false);
      this.d("Only During Swing", "Retained legacy value", false).P(() -> false);
      this.d("Equip Motion", "Retained legacy value", false).P(() -> false);
      this.d("Animate Offhand", "Retained legacy value", false).P(() -> false);
   }

   private e Q(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new e(var1, var2, var3, var5, var7, var9));
   }

   private s d(String var1, String var2, boolean var3) {
      return this.addSetting(new s(var1, var2, var3));
   }

   public static AnimationsModule I() {
      return l;
   }

   public List<q<?>> W() {
      return List.of(this.D, this.c, this.Q, this.a, this.h, this.I, this.f, this.O, this.q, this.N, this.X);
   }

   public List<q<?>> h() {
      return List.of(this.F, this.u, this.A, this.j, this.m);
   }

   @Override
   public String getDisplayName() {
      return "SwingAnimation";
   }

   public String P() {
      return this.D.getValue();
   }

   private boolean M() {
      AuraModule var1 = AuraModule.V();
      return var1 != null && var1.u() != null;
   }

   public boolean K(HumanoidArm var1) {
      if (!this.isEnabled()) {
         return false;
      }

      LocalPlayer var2 = getMc().player;
      return this.isRuntimeActive() && var2 != null && var2.getMainArm() == var1;
   }

   public boolean Y(HumanoidArm var1) {
      return !this.isEnabled() ? false : this.K(var1) && (!this.c.getValue() || this.M());
   }

   public boolean b() {
      return false;
   }

   public boolean J() {
      return this.isEnabled() ? this.N.getValue() : false;
   }

   public int a(int var1) {
      if (this.isEnabled()) {
         return this.N.getValue() ? this.X.getValue().intValue() : var1;
      } else {
         return var1;
      }
   }

   private void x() {
      Minecraft var1 = getMc();
      if (this.g != var1.player || this.H != var1.level) {
         this.x.z();
         this.y.x();
         this.g = var1.player;
         this.H = var1.level;
      }
   }

   public void d(PoseStack var1, HumanoidArm var2, float var3) {
      if (this.isEnabled()) {
         this.x();
         g_.H(
            var1,
            this.D.getValue(),
            var2 == HumanoidArm.LEFT,
            var3,
            new g_.u(
               this.Q.getValue().floatValue(),
               this.a.getValue().floatValue(),
               this.h.getValue().floatValue(),
               this.I.getValue().floatValue(),
               this.f.getValue().floatValue(),
               this.O.getValue().floatValue(),
               this.q.getValue(),
               this.F.getValue().floatValue(),
               this.u.getValue().floatValue()
            ),
            this.x
         );
      }
   }

   public void w(PoseStack var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.A.getValue()) {
            this.x();
            float var2 = (float)Math.toDegrees(this.y.E(cf.F(), this.m.getValue() / 4.0) % (Math.PI * 2));

            uf.q(var1, switch ((String)this.j.getValue()) {
               case "Y" -> Axis.YP.rotationDegrees(var2);
               case "Z" -> Axis.ZP.rotationDegrees(var2);
               default -> Axis.XP.rotationDegrees(var2);
            });
         }
      }
   }

   @Override
   public void onEnable() {
      this.U();
      com.larpclient.h.A("SwingAnimation", "Activated preset {}", this.D.getValue());
   }

   @Override
   public void onDisable() {
      this.U();
      com.larpclient.h.A("SwingAnimation", "Cleared swing cycle and item spin state");
   }

   private void U() {
      this.x.z();
      this.g = this.H = null;
      this.y.x();
   }

   public static String y(String var0) {
      String var3 = var0;
      byte var4 = -1;
      boolean var1 = EmotesModule.h;
      switch (var3.hashCode()) {
         case -1955878649:
            String[] var5 = b;
            if (!var3.equals(var5[14])) {
               break;
            }

            var4 = 0;
            if (!var1) {
               break;
            }

            int var2 = gg.I;
            gg.I = ++var2;
         case -1085510111:
            String[] var7 = b;
            if (!var3.equals(var7[4])) {
               break;
            }

            var4 = 1;
            if (!var1) {
               break;
            }
         case 2433880:
            String[] var8 = b;
            if (!var3.equals(var8[5])) {
               break;
            }

            var4 = 2;
            if (!var1) {
               break;
            }
         case 80301850:
            String[] var9 = b;
            if (!var3.equals(var9[7])) {
               break;
            }

            var4 = 3;
            if (!var1) {
               break;
            }
         case -1814666802:
            String[] var10 = b;
            if (!var3.equals(var10[12])) {
               break;
            }

            var4 = 4;
            if (!var1) {
               break;
            }
         case 79367:
            String[] var11 = b;
            if (!var3.equals(var11[3])) {
               break;
            }

            var4 = 5;
            if (!var1) {
               break;
            }
         case -138073504:
            String[] var12 = b;
            if (!var3.equals(var12[0])) {
               break;
            }

            var4 = 6;
            if (!var1) {
               break;
            }
         case 81225479:
            String[] var13 = b;
            if (!var3.equals(var13[9])) {
               break;
            }

            var4 = 7;
            if (!var1) {
               break;
            }
         case 2583650:
            String[] var14 = b;
            if (var3.equals(var14[1])) {
               var4 = 8;
            }
      }
      return switch (var4) {
         case 0, 1, 2 -> {
            String[] var19 = b;
            yield var19[10];
         }
         case 3 -> {
            String[] var18 = b;
            yield var18[2];
         }
         case 4, 5 -> {
            String[] var17 = b;
            yield var17[6];
         }
         case 6 -> {
            String[] var16 = b;
            yield var16[13];
         }
         case 7 -> {
            String[] var15 = b;
            yield var15[11];
         }
         case 8 -> b[8];
         default -> var0;
      };
   }

   public static void a(JsonObject var0) {
      if (var0.has("Swing") && var0.get("Swing").isJsonPrimitive() && var0.getAsJsonPrimitive("Swing").isString()) {
         var0.addProperty("Swing", y(var0.get("Swing").getAsString()));
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
