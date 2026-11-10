package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.c3;
import com.larpclient.em;
import com.larpclient.q8;
import com.larpclient.qk;
import com.larpclient.qo;
import com.larpclient.w8;
import java.util.List;
import net.minecraft.class_1306;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_746;
import net.minecraft.class_7833;

public class AnimationsModule extends Module {
   private final li g = this.addSetting(new li("Swing", "Swing preset", "Module 6", w8.K) {
      @Override
      public String getDisplayName() {
         return "Mode";
      }

      @Override
      public void setValue(String var1) {
         super.setValue(AnimationsModule.q(var1));
      }
   });
   private final l3 j = this.H("Only with aura", "Use swing presets while Kill Aura has a target", false);
   private final lo I = this.B("Legacy Strength", "Classic preset strength", 20.0, 20.0, 75.0, 0.1);
   private final lo H = this.B("Swing Strength", "Preset movement strength", 8.0, 1.0, 10.0, 0.1);
   private final lo Y = this.B("Angle", "Module 2 and Module 4 angle", 100.0, 0.0, 360.0, 1.0);
   private final lo o = this.B("Turn", "Shift and Break turn", 0.0, -90.0, 90.0, 0.5);
   private final lo w = this.B("Down Strength", "Additional downward swing movement", 0.0, 0.0, 1.0, 0.05);
   private final lo p = this.B("Distance", "Preset depth", 0.25, 0.0, 0.8, 0.01);
   private final l3 s = this.H("Smooth Motion", "Ease swing progress", true);
   private final lo R = this.B("Smooth Amount", "Blend cubic easing", 0.65, 0.0, 1.0, 0.05);
   private final lo G = this.B("Motion Curve", "Progress curve exponent", 1.0, 0.45, 2.5, 0.05);
   private final l3 P = this.H("Item 360", "Continuously rotate held items", false);
   private final li Z = this.addSetting(new li("Spin Axis", "Continuous spin axis", "X", "X", "Y", "Z"));
   private final lo h = this.B("Spin Speed", "Continuous spin speed", 3.6, 0.2, 12.0, 0.1);
   private final l3 J = this.H("Slow", "Override local hand swing duration", false);
   private final lo T = this.B("Speed", "Swing duration in ticks", 12.0, 1.0, 50.0, 1.0);
   private final w8.s n = new w8.s();
   private Object a;
   private Object t;
   private final c3 A = new c3();
   private static AnimationsModule r;
   public static int C;
   private static final String[] b;

   public AnimationsModule() {
      super("Animations", "Customize swing presets and held-item spin", Category.VISUALS, -1);
      r = this;
      this.I.O(() -> this.g.G("Mode 2") || this.g.G("Mode 3") || this.g.G("Mode 4"));
      this.H.O(() -> {
         return (boolean)(switch ((String)this.g.getValue()) {
            case "Module 1", "Module 2", "Module 3", "Module 4", "Module 6", "Module 7", "Shift", "Break", "Lunge", "Spear", "Jelly", "Shake" -> 1;
            default -> 0;
         });
      });
      this.w.O(() -> {
         return (boolean)(switch ((String)this.g.getValue()) {
            case "Mode 1", "Mode 2", "Mode 3", "Mode 4", "Mode 5", "Module 8" -> 0;
            default -> 1;
         });
      });
      this.p.O(() -> {
         return (boolean)(switch ((String)this.g.getValue()) {
            case "Module 2", "Module 4", "Slant", "Triangle", "Shift", "Break", "Lunge", "Spear", "Down", "Jelly", "Shake" -> 1;
            default -> 0;
         });
      });
      this.Y.O(() -> this.g.G("Module 2") || this.g.G("Module 4"));
      this.o.O(() -> this.g.G("Shift") || this.g.G("Break"));
      this.R.O(this.s::getValue);
      this.Z.O(this.P::getValue);
      this.h.O(this.P::getValue);
      this.T.O(this.J::getValue);
      this.g.a(var1 -> this.n.T());
      this.B("Duration", "Retained legacy value", 1.0, 0.25, 3.0, 0.05).O(() -> false);
      this.B("Strength", "Retained legacy value", 1.0, 0.0, 3.0, 0.05).O(() -> false);
      this.H("Only During Swing", "Retained legacy value", false).O(() -> false);
      this.H("Equip Motion", "Retained legacy value", false).O(() -> false);
      this.H("Animate Offhand", "Retained legacy value", false).O(() -> false);
   }

   private lo B(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lo(var1, var2, var3, var5, var7, var9));
   }

   private l3 H(String var1, String var2, boolean var3) {
      return this.addSetting(new l3(var1, var2, var3));
   }

   public static AnimationsModule W() {
      return r;
   }

   public List<lb<?>> O() {
      return List.of(this.g, this.j, this.I, this.H, this.Y, this.o, this.w, this.p, this.s, this.J, this.T);
   }

   public List<lb<?>> T() {
      return List.of(this.R, this.G, this.P, this.Z, this.h);
   }

   @Override
   public String getDisplayName() {
      return "SwingAnimation";
   }

   public String c() {
      return this.g.getValue();
   }

   private boolean g() {
      AuraModule var1 = AuraModule.R();
      return var1 != null && var1.B() != null;
   }

   public boolean A(class_1306 var1) {
      if (!this.isEnabled()) {
         return false;
      }

      class_746 var2 = getMc().field_1724;
      return this.isRuntimeActive() && var2 != null && var2.method_6068() == var1;
   }

   public boolean a(class_1306 var1) {
      return !this.isEnabled() ? false : this.A(var1) && (!this.j.getValue() || this.g());
   }

   public boolean q() {
      return false;
   }

   public boolean F() {
      return this.isEnabled() ? this.J.getValue() : false;
   }

   public int h(int var1) {
      if (this.isEnabled()) {
         return this.J.getValue() ? this.T.getValue().intValue() : var1;
      } else {
         return var1;
      }
   }

   private void O() {
      class_310 var1 = getMc();
      if (this.a != var1.field_1724 || this.t != var1.field_1687) {
         this.n.T();
         this.A.K();
         this.a = var1.field_1724;
         this.t = var1.field_1687;
      }
   }

   public void b(class_4587 var1, class_1306 var2, float var3) {
      if (this.isEnabled()) {
         this.O();
         w8.m(
            var1,
            this.g.getValue(),
            var2 == class_1306.field_6182,
            var3,
            new w8.v(
               this.I.getValue().floatValue(),
               this.H.getValue().floatValue(),
               this.Y.getValue().floatValue(),
               this.o.getValue().floatValue(),
               this.w.getValue().floatValue(),
               this.p.getValue().floatValue(),
               this.s.getValue(),
               this.R.getValue().floatValue(),
               this.G.getValue().floatValue()
            ),
            this.n
         );
      }
   }

   public void g(class_4587 var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.P.getValue()) {
            this.O();
            float var2 = (float)Math.toDegrees(this.A.u(qo.x(), this.h.getValue() / 4.0) % (Math.PI * 2));

            em.W(var1, switch ((String)this.Z.getValue()) {
               case "Y" -> class_7833.field_40716.rotationDegrees(var2);
               case "Z" -> class_7833.field_40718.rotationDegrees(var2);
               default -> class_7833.field_40714.rotationDegrees(var2);
            });
         }
      }
   }

   @Override
   public void onEnable() {
      this.y();
      q8.G("SwingAnimation", "Activated preset {}", this.g.getValue());
   }

   @Override
   public void onDisable() {
      this.y();
      q8.G("SwingAnimation", "Cleared swing cycle and item spin state");
   }

   private void y() {
      this.n.T();
      this.a = this.t = null;
      this.A.K();
   }

   public static String q(String var0) {
      String var2 = var0;
      byte var3 = -1;
      int var1 = EmotesModule.X;
      switch (var2.hashCode()) {
         case -1955878649:
            String[] var4 = b;
            if (!var2.equals(var4[1])) {
               break;
            }

            var3 = 0;
            if (var1 == 0) {
               break;
            }

            qk.F = (boolean)(qk.F ? 0 : 1);
         case -1085510111:
            String[] var5 = b;
            if (!var2.equals(var5[11])) {
               break;
            }

            var3 = 1;
            if (var1 == 0) {
               break;
            }
         case 2433880:
            String[] var6 = b;
            if (!var2.equals(var6[6])) {
               break;
            }

            var3 = 2;
            if (var1 == 0) {
               break;
            }
         case 80301850:
            String[] var7 = b;
            if (!var2.equals(var7[2])) {
               break;
            }

            var3 = 3;
            if (var1 == 0) {
               break;
            }
         case -1814666802:
            String[] var8 = b;
            if (!var2.equals(var8[4])) {
               break;
            }

            var3 = 4;
            if (var1 == 0) {
               break;
            }
         case 79367:
            String[] var9 = b;
            if (!var2.equals(var9[12])) {
               break;
            }

            var3 = 5;
            if (var1 == 0) {
               break;
            }
         case -138073504:
            String[] var10 = b;
            if (!var2.equals(var10[8])) {
               break;
            }

            var3 = 6;
            if (var1 == 0) {
               break;
            }
         case 81225479:
            String[] var11 = b;
            if (!var2.equals(var11[10])) {
               break;
            }

            var3 = 7;
            if (var1 == 0) {
               break;
            }
         case 2583650:
            String[] var12 = b;
            if (var2.equals(var12[9])) {
               var3 = 8;
            }
      }
      return switch (var3) {
         case 0, 1, 2 -> {
            String[] var17 = b;
            yield var17[13];
         }
         case 3 -> {
            String[] var16 = b;
            yield var16[3];
         }
         case 4, 5 -> {
            String[] var15 = b;
            yield var15[7];
         }
         case 6 -> {
            String[] var14 = b;
            yield var14[0];
         }
         case 7 -> {
            String[] var13 = b;
            yield var13[5];
         }
         case 8 -> b[14];
         default -> var0;
      };
   }

   public static void o(JsonObject var0) {
      if (var0.has("Swing") && var0.get("Swing").isJsonPrimitive() && var0.getAsJsonPrimitive("Swing").isString()) {
         var0.addProperty("Swing", q(var0.get("Swing").getAsString()));
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
