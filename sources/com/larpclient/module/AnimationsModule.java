package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient._5;
import com.larpclient.mt;
import com.larpclient.ty;
import com.larpclient.u;
import com.larpclient.uh;
import com.larpclient.w9;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;

public class AnimationsModule extends Module {
   private final d6 t = this.addSetting(new d6("Swing", "Swing preset", "Module 6", uh.K) {
      @Override
      public String getDisplayName() {
         return "Mode";
      }

      @Override
      public void setValue(String var1) {
         super.setValue(AnimationsModule.E(var1));
      }
   });
   private final dh n = this.M("Only with aura", "Use swing presets while Kill Aura has a target", false);
   private final d9 I = this.B("Legacy Strength", "Classic preset strength", 20.0, 20.0, 75.0, 0.1);
   private final d9 h = this.B("Swing Strength", "Preset movement strength", 8.0, 1.0, 10.0, 0.1);
   private final d9 v = this.B("Angle", "Module 2 and Module 4 angle", 100.0, 0.0, 360.0, 1.0);
   private final d9 q = this.B("Turn", "Shift and Break turn", 0.0, -90.0, 90.0, 0.5);
   private final d9 p = this.B("Down Strength", "Additional downward swing movement", 0.0, 0.0, 1.0, 0.05);
   private final d9 A = this.B("Distance", "Preset depth", 0.25, 0.0, 0.8, 0.01);
   private final dh P = this.M("Smooth Motion", "Ease swing progress", true);
   private final d9 G = this.B("Smooth Amount", "Blend cubic easing", 0.65, 0.0, 1.0, 0.05);
   private final d9 x = this.B("Motion Curve", "Progress curve exponent", 1.0, 0.45, 2.5, 0.05);
   private final dh U = this.M("Item 360", "Continuously rotate held items", false);
   private final d6 D = this.addSetting(new d6("Spin Axis", "Continuous spin axis", "X", "X", "Y", "Z"));
   private final d9 k = this.B("Spin Speed", "Continuous spin speed", 3.6, 0.2, 12.0, 0.1);
   private final dh j = this.M("Slow", "Override local hand swing duration", false);
   private final d9 i = this.B("Speed", "Swing duration in ticks", 12.0, 1.0, 50.0, 1.0);
   private final uh.o m = new uh.o();
   private Object R;
   private Object V;
   private final _5 e = new _5();
   private static AnimationsModule W;
   public static boolean r;
   private static final String[] a;

   public AnimationsModule() {
      super("Animations", "Customize swing presets and held-item spin", Category.VISUALS, -1);
      W = this;
      this.I.H(() -> this.t.S("Mode 2") || this.t.S("Mode 3") || this.t.S("Mode 4"));
      this.h.H(() -> {
         return (boolean)(switch ((String)this.t.P()) {
            case "Module 1", "Module 2", "Module 3", "Module 4", "Module 6", "Module 7", "Shift", "Break", "Lunge", "Spear", "Jelly", "Shake" -> 1;
            default -> 0;
         });
      });
      this.p.H(() -> {
         return (boolean)(switch ((String)this.t.P()) {
            case "Mode 1", "Mode 2", "Mode 3", "Mode 4", "Mode 5", "Module 8" -> 0;
            default -> 1;
         });
      });
      this.A.H(() -> {
         return (boolean)(switch ((String)this.t.P()) {
            case "Module 2", "Module 4", "Slant", "Triangle", "Shift", "Break", "Lunge", "Spear", "Down", "Jelly", "Shake" -> 1;
            default -> 0;
         });
      });
      this.v.H(() -> this.t.S("Module 2") || this.t.S("Module 4"));
      this.q.H(() -> this.t.S("Shift") || this.t.S("Break"));
      this.G.H(this.P::P);
      this.D.H(this.U::P);
      this.k.H(this.U::P);
      this.i.H(this.j::P);
      this.t.h(var1 -> this.m.j());
      this.B("Duration", "Retained legacy value", 1.0, 0.25, 3.0, 0.05).H(() -> false);
      this.B("Strength", "Retained legacy value", 1.0, 0.0, 3.0, 0.05).H(() -> false);
      this.M("Only During Swing", "Retained legacy value", false).H(() -> false);
      this.M("Equip Motion", "Retained legacy value", false).H(() -> false);
      this.M("Animate Offhand", "Retained legacy value", false).H(() -> false);
   }

   private d9 B(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new d9(var1, var2, var3, var5, var7, var9));
   }

   private dh M(String var1, String var2, boolean var3) {
      return this.addSetting(new dh(var1, var2, var3));
   }

   public static AnimationsModule g() {
      return W;
   }

   public List<df<?>> u() {
      return List.of(this.t, this.n, this.I, this.h, this.v, this.q, this.p, this.A, this.P, this.j, this.i);
   }

   public List<df<?>> I() {
      return List.of(this.G, this.x, this.U, this.D, this.k);
   }

   @Override
   public String getDisplayName() {
      return "SwingAnimation";
   }

   public String p() {
      return this.t.P();
   }

   private boolean A() {
      AuraModule var1 = AuraModule.S();
      return var1 != null && var1.l() != null;
   }

   public boolean w(HumanoidArm var1) {
      if (!this.isEnabled()) {
         return false;
      }

      LocalPlayer var2 = getMc().player;
      return this.isRuntimeActive() && var2 != null && var2.getMainArm() == var1;
   }

   public boolean M(HumanoidArm var1) {
      return !this.isEnabled() ? false : this.w(var1) && (!this.n.P() || this.A());
   }

   public boolean y() {
      return false;
   }

   public boolean P() {
      return this.isEnabled() ? this.j.P() : false;
   }

   public int t(int var1) {
      if (this.isEnabled()) {
         return this.j.P() ? this.i.P().intValue() : var1;
      } else {
         return var1;
      }
   }

   private void L() {
      Minecraft var1 = getMc();
      if (this.R != var1.player || this.V != var1.level) {
         this.m.j();
         this.e.q();
         this.R = var1.player;
         this.V = var1.level;
      }
   }

   public void O(PoseStack var1, HumanoidArm var2, float var3) {
      if (this.isEnabled()) {
         this.L();
         uh.l(
            var1,
            this.t.P(),
            var2 == HumanoidArm.LEFT,
            var3,
            new uh.z(
               this.I.P().floatValue(),
               this.h.P().floatValue(),
               this.v.P().floatValue(),
               this.q.P().floatValue(),
               this.p.P().floatValue(),
               this.A.P().floatValue(),
               this.P.P(),
               this.G.P().floatValue(),
               this.x.P().floatValue()
            ),
            this.m
         );
      }
   }

   public void z(PoseStack var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.U.P()) {
            this.L();
            float var2 = (float)Math.toDegrees(this.e.p(ty.K(), this.k.P() / 4.0) % (Math.PI * 2));

            u.v(var1, switch ((String)this.D.P()) {
               case "Y" -> Axis.YP.rotationDegrees(var2);
               case "Z" -> Axis.ZP.rotationDegrees(var2);
               default -> Axis.XP.rotationDegrees(var2);
            });
         }
      }
   }

   @Override
   public void onEnable() {
      this.K();
      mt.K("SwingAnimation", "Activated preset {}", this.t.P());
   }

   @Override
   public void onDisable() {
      this.K();
      mt.K("SwingAnimation", "Cleared swing cycle and item spin state");
   }

   private void K() {
      this.m.j();
      this.R = this.V = null;
      this.e.q();
   }

   public static String E(String var0) {
      String var2 = var0;
      boolean var1 = EmotesModule.E;
      byte var3 = -1;
      switch (var2.hashCode()) {
         case -1955878649:
            String[] var4 = a;
            if (!var2.equals(var4[3])) {
               break;
            }

            var3 = 0;
            if (!var1) {
               break;
            }

            w9.e = (boolean)(w9.e ? 0 : 1);
         case -1085510111:
            String[] var5 = a;
            if (!var2.equals(var5[0])) {
               break;
            }

            var3 = 1;
            if (!var1) {
               break;
            }
         case 2433880:
            String[] var6 = a;
            if (!var2.equals(var6[5])) {
               break;
            }

            var3 = 2;
            if (!var1) {
               break;
            }
         case 80301850:
            String[] var7 = a;
            if (!var2.equals(var7[2])) {
               break;
            }

            var3 = 3;
            if (!var1) {
               break;
            }
         case -1814666802:
            String[] var8 = a;
            if (!var2.equals(var8[1])) {
               break;
            }

            var3 = 4;
            if (!var1) {
               break;
            }
         case 79367:
            String[] var9 = a;
            if (!var2.equals(var9[8])) {
               break;
            }

            var3 = 5;
            if (!var1) {
               break;
            }
         case -138073504:
            String[] var10 = a;
            if (!var2.equals(var10[9])) {
               break;
            }

            var3 = 6;
            if (!var1) {
               break;
            }
         case 81225479:
            String[] var11 = a;
            if (!var2.equals(var11[11])) {
               break;
            }

            var3 = 7;
            if (!var1) {
               break;
            }
         case 2583650:
            String[] var12 = a;
            if (var2.equals(var12[13])) {
               var3 = 8;
            }
      }
      return switch (var3) {
         case 0, 1, 2 -> {
            String[] var17 = a;
            yield var17[7];
         }
         case 3 -> {
            String[] var16 = a;
            yield var16[6];
         }
         case 4, 5 -> {
            String[] var15 = a;
            yield var15[4];
         }
         case 6 -> {
            String[] var14 = a;
            yield var14[12];
         }
         case 7 -> {
            String[] var13 = a;
            yield var13[10];
         }
         case 8 -> a[14];
         default -> var0;
      };
   }

   public static void s(JsonObject var0) {
      if (var0.has("Swing") && var0.get("Swing").isJsonPrimitive() && var0.getAsJsonPrimitive("Swing").isString()) {
         var0.addProperty("Swing", E(var0.get("Swing").getAsString()));
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
