package com.larpclient.module;

import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.u7;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.CameraType;
import net.minecraft.world.inventory.Slot;

public final class GUIAnimationModule extends Module {
   private static GUIAnimationModule j;
   private final ln r = this.E("Player List", "Slide the player list in and out", true);
   private final ln n = this.E("Inventory", "Animate container entrances with aligned input", true);
   private final ln e = this.E("Camera Zoom", "Smooth changes in camera field of view", false);
   private final ln h = this.addSetting(new ln("Chunk Update", "Keep vanilla chunk appearance; turn off to animate newly visible chunks", true) {
      @Override
      public String getDisplayName() {
         return "Native Chunks";
      }
   });
   private final ln M = this.E("Perspective Change", "Ease camera distance when switching perspective", false);
   private final ln y = this.E("Chat Messages", "Ease received chat, typed chat and client search text into view", true);
   private final ln U = this.E("Items", "Animate hovered inventory items", true);
   private final lp J = this.addSetting(new lp("Chunk Animation", "Curve for chunk appearance", "Quart", "Quart", "Circ", "Sine", "Cubic"));
   private final lc G = this.addSetting(new lc("Speed", "Inventory and chunk entrance duration in tenths of a second", 6.0, 2.0, 10.0, 1.0) {
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
   private final u7 z = new u7(0.0);
   private final u7 g = new u7(0.0);
   private final Map<Slot, GUIAnimationModule.a> W = new WeakHashMap<>();
   private Object w;
   private float R = Float.NaN;
   private long q;

   public GUIAnimationModule() {
      super("GUI Animation", "Animate menus, chat, camera changes and chunks", Category.RENDER, -1);
      j = this;
      this.J.x(() -> !this.h.getValue());
      this.G.x(() -> !this.h.getValue() || this.n.getValue());
   }

   private ln E(String var1, String var2, boolean var3) {
      return this.addSetting(new ln(var1, var2, var3));
   }

   public static GUIAnimationModule s() {
      return j;
   }

   public List<l6<?>> s() {
      return List.of(this.r, this.n, this.y, this.U);
   }

   public List<l6<?>> o() {
      return List.of(this.e, this.M);
   }

   public List<l6<?>> i() {
      return List.of(this.h, this.J, this.G);
   }

   public boolean f() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.n.getValue();
   }

   public boolean q() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.U.getValue();
   }

   public boolean n() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.y.getValue();
   }

   public boolean c() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && !this.h.getValue();
   }

   public boolean h() {
      if (!this.isEnabled()) {
         return false;
      }

      SmoothCameraModule var1 = SmoothCameraModule.G();
      return this.isRuntimeActive() && this.M.getValue() && rr.u() == rr.f.i && (var1 == null || !var1.isRuntimeActive());
   }

   public float B() {
      if (this.isRuntimeActive() && this.r.getValue()) {
         boolean var1 = getMc().options.keyPlayerList.isDown();
         return (float)Math.clamp(this.z.y(var1 ? 1.0 : 0.0, var1 ? 240 : 190), 0.0, 1.0);
      } else {
         return getMc().options.keyPlayerList.isDown() ? 1.0F : 0.0F;
      }
   }

   public boolean B() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.r.getValue();
   }

   public long T() {
      return Math.round(this.G.getValue() * 100.0);
   }

   public float H(long var1) {
      if (this.isEnabled()) {
         if (!this.f()) {
            return 1.0F;
         }

         double var3 = Math.clamp((System.nanoTime() - var1) / (this.T() * 1000000.0), 0.0, 1.0);
         return (float)(0.86 + 0.14 * u7.p.Y.m(var3));
      } else {
         return 1.0F;
      }
   }

   public double E(double var1, double var3, float var5) {
      return var3 + (var1 - var3) / Math.max(0.01F, var5);
   }

   public float I(Slot var1, boolean var2) {
      if (this.isEnabled()) {
         long var3 = System.nanoTime();
         GUIAnimationModule.a var5 = this.W.get(var1);
         float var6 = var5 == null ? 1.0F : var5.scale;
         double var7 = var5 == null ? 0.0 : Math.clamp((var3 - var5.time) * 1.0E-9, 0.0, 0.1);
         float var9 = (float)(var6 + ((var2 ? 1.12 : 1.0) - var6) * (1.0 - Math.exp(-(var2 ? 6.5 : 5.0) * var7)));
         this.W.put(var1, new GUIAnimationModule.a(var9, var3));
         return var9;
      } else {
         return 1.0F;
      }
   }

   public double s(double var1) {
      var1 = Math.clamp(var1, 0.0, 1.0);

      return switch ((String)this.J.getValue()) {
         case "Circ" -> Math.sqrt(1.0 - (var1 - 1.0) * (var1 - 1.0));
         case "Sine" -> Math.sin(var1 * Math.PI / 2.0);
         case "Cubic" -> 1.0 - Math.pow(1.0 - var1, 3.0);
         default -> 1.0 - Math.pow(1.0 - var1, 4.0);
      };
   }

   public float T(boolean var1) {
      CameraType var2 = getMc().options.getCameraType();
      if (!this.h()) {
         this.w = var2;
         this.g.c(var1 ? 1.0 : 0.0);
         return var1 ? 1.0F : 0.0F;
      }

      if (this.w == null) {
         this.g.c(var1 ? 1.0 : 0.0);
      } else if (this.w != var2 && var1 && this.w != CameraType.FIRST_PERSON) {
         this.g.c(0.0);
      }

      this.w = var2;
      return (float)this.g.y(var1 ? 1.0 : 0.0, 320);
   }

   public float y(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.e.getValue() && rr.u() == rr.f.i) {
            long var2 = System.nanoTime();
            if (Float.isNaN(this.R)) {
               this.R = var1;
            } else {
               this.R = this.R + (var1 - this.R) * (float)(1.0 - Math.exp(-12.0 * Math.clamp((var2 - this.q) * 1.0E-9, 0.0, 0.1)));
            }

            this.q = var2;
            return this.R;
         } else {
            this.R = Float.NaN;
            return var1;
         }
      } else {
         return var1;
      }
   }

   @Override
   public void onEnable() {
      r4.g("GUI", "GUI Animation active: chat and typed input={}, native editing remains immediate", this.y.getValue());
   }

   @Override
   public void onDisable() {
      r4.g("GUI", "GUI Animation inactive; text motion settles on the next extraction");
      this.z.c(0.0);
      this.g.c(0.0);
      this.W.clear();
      this.w = null;
      this.R = Float.NaN;
      this.q = 0L;
   }

   private record a(float scale, long time) {
   }
}
