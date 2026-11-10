package com.larpclient.module;

import com.larpclient.q8;
import com.larpclient.qb;
import com.larpclient.wp;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.class_1735;
import net.minecraft.class_5498;

public final class GUIAnimationModule extends Module {
   private static GUIAnimationModule U;
   private final l3 v = this.C("Player List", "Slide the player list in and out", true);
   private final l3 Q = this.C("Inventory", "Animate container entrances with aligned input", true);
   private final l3 r = this.C("Camera Zoom", "Smooth changes in camera field of view", false);
   public final l3 w = this.addSetting(new l3("Chunk Update", "Keep vanilla chunk appearance; turn off to animate newly visible chunks", true) {
      @Override
      public String getDisplayName() {
         return "Native Chunks";
      }
   });
   private final l3 S = this.C("Perspective Change", "Ease camera distance when switching perspective", false);
   private final l3 y = this.C("Chat Messages", "Ease received chat, typed chat and client search text into view", true);
   private final l3 n = this.C("Items", "Animate hovered inventory items", true);
   private final li e = this.addSetting(new li("Chunk Animation", "Curve for chunk appearance", "Quart", "Quart", "Circ", "Sine", "Cubic"));
   private final lo J = this.addSetting(new lo("Speed", "Inventory and chunk entrance duration in tenths of a second", 6.0, 2.0, 10.0, 1.0) {
      @Override
      public String getDisplayName() {
         return "Duration";
      }

      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.1f s", this.getValue() / 10.0);
      }

      @Override
      public String getEditValueAsString() {
         return BigDecimal.valueOf(this.getValue() / 10.0).stripTrailingZeros().toPlainString();
      }

      @Override
      public void setFromEditString(String var1) {
         super.setFromEditString(Double.toString(Double.parseDouble(var1.strip().replace("s", "")) * 10.0));
      }
   });
   private final qb F = new qb(0.0);
   private final qb o = new qb(0.0);
   private final Map<class_1735, GUIAnimationModule.v> B = new WeakHashMap<>();
   private Object d;
   private float V = Float.NaN;
   private long x;

   public GUIAnimationModule() {
      super("GUI Animation", "Animate menus, chat, camera changes and chunks", Category.RENDER, -1);
      U = this;
      this.e.O(() -> !this.w.getValue());
      this.J.O(() -> !this.w.getValue() || this.Q.getValue());
   }

   private l3 C(String var1, String var2, boolean var3) {
      return this.addSetting(new l3(var1, var2, var3));
   }

   public static GUIAnimationModule Z() {
      return U;
   }

   public List<lb<?>> a() {
      return List.of(this.v, this.Q, this.y, this.n);
   }

   public List<lb<?>> p() {
      return List.of(this.r, this.S);
   }

   public List<lb<?>> H() {
      return List.of(this.w, this.e, this.J);
   }

   public boolean w() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.Q.getValue();
   }

   public boolean L() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.n.getValue();
   }

   public boolean r() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.y.getValue();
   }

   public boolean O() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && !this.w.getValue();
   }

   public boolean N() {
      if (!this.isEnabled()) {
         return false;
      }

      SmoothCameraModule var1 = SmoothCameraModule.L();
      return this.isRuntimeActive() && this.S.getValue() && wp.o() == wp.z.D && (var1 == null || !var1.isRuntimeActive());
   }

   public float L() {
      if (this.isRuntimeActive() && this.v.getValue()) {
         boolean var1 = getMc().field_1690.field_1907.method_1434();
         return (float)Math.clamp(this.F.a(var1 ? 1.0 : 0.0, var1 ? 240 : 190), 0.0, 1.0);
      } else {
         return getMc().field_1690.field_1907.method_1434() ? 1.0F : 0.0F;
      }
   }

   public boolean c() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.v.getValue();
   }

   public long H() {
      return Math.round(this.J.getValue() * 100.0);
   }

   public float s(long var1) {
      if (this.isEnabled()) {
         if (!this.w()) {
            return 1.0F;
         }

         double var3 = Math.clamp((System.nanoTime() - var1) / (this.H() * 1000000.0), 0.0, 1.0);
         return (float)(0.86 + 0.14 * qb.y.y.f(var3));
      } else {
         return 1.0F;
      }
   }

   public double Q(double var1, double var3, float var5) {
      return var3 + (var1 - var3) / Math.max(0.01F, var5);
   }

   public float R(class_1735 var1, boolean var2) {
      if (this.isEnabled()) {
         long var3 = System.nanoTime();
         GUIAnimationModule.v var5 = this.B.get(var1);
         float var6 = var5 == null ? 1.0F : var5.scale;
         double var7 = var5 == null ? 0.0 : Math.clamp((var3 - var5.time) * 1.0E-9, 0.0, 0.1);
         float var9 = (float)(var6 + ((var2 ? 1.12 : 1.0) - var6) * (1.0 - Math.exp(-(var2 ? 6.5 : 5.0) * var7)));
         this.B.put(var1, new GUIAnimationModule.v(var9, var3));
         return var9;
      } else {
         return 1.0F;
      }
   }

   public double I(double var1) {
      var1 = Math.clamp(var1, 0.0, 1.0);

      return switch ((String)this.e.getValue()) {
         case "Circ" -> Math.sqrt(1.0 - (var1 - 1.0) * (var1 - 1.0));
         case "Sine" -> Math.sin(var1 * Math.PI / 2.0);
         case "Cubic" -> 1.0 - Math.pow(1.0 - var1, 3.0);
         default -> 1.0 - Math.pow(1.0 - var1, 4.0);
      };
   }

   public float T(boolean var1) {
      class_5498 var2 = getMc().field_1690.method_31044();
      if (!this.N()) {
         this.d = var2;
         this.o.w(var1 ? 1.0 : 0.0);
         return var1 ? 1.0F : 0.0F;
      }

      if (this.d == null) {
         this.o.w(var1 ? 1.0 : 0.0);
      } else if (this.d != var2 && var1 && this.d != class_5498.field_26664) {
         this.o.w(0.0);
      }

      this.d = var2;
      return (float)this.o.a(var1 ? 1.0 : 0.0, 320);
   }

   public float e(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.r.getValue() && wp.o() == wp.z.D) {
            long var2 = System.nanoTime();
            if (Float.isNaN(this.V)) {
               this.V = var1;
            } else {
               this.V = this.V + (var1 - this.V) * (float)(1.0 - Math.exp(-12.0 * Math.clamp((var2 - this.x) * 1.0E-9, 0.0, 0.1)));
            }

            this.x = var2;
            return this.V;
         } else {
            this.V = Float.NaN;
            return var1;
         }
      } else {
         return var1;
      }
   }

   @Override
   public void onEnable() {
      q8.v("GUI", "GUI Animation active: chat and typed input={}, native editing remains immediate", this.y.getValue());
   }

   @Override
   public void onDisable() {
      q8.v("GUI", "GUI Animation inactive; text motion settles on the next extraction");
      this.F.w(0.0);
      this.o.w(0.0);
      this.B.clear();
      this.d = null;
      this.V = Float.NaN;
      this.x = 0L;
   }

   private record v(float scale, long time) {
   }
}
