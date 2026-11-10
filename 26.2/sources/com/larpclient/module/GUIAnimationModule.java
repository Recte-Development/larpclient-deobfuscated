package com.larpclient.module;

import com.larpclient.jp;
import com.larpclient.p_;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.CameraType;
import net.minecraft.world.inventory.Slot;

public final class GUIAnimationModule extends Module {
   private static GUIAnimationModule i;
   private final s N = this.n("Player List", "Slide the player list in and out", true);
   private final s r = this.n("Inventory", "Animate container entrances with aligned input", true);
   private final s E = this.n("Camera Zoom", "Smooth changes in camera field of view", false);
   public final s h = this.addSetting(new s("Chunk Update", "Keep vanilla chunk appearance; turn off to animate newly visible chunks", true) {
      @Override
      public String getDisplayName() {
         return "Native Chunks";
      }
   });
   private final s e = this.n("Perspective Change", "Ease camera distance when switching perspective", false);
   private final s y = this.n("Chat Messages", "Ease received chat, typed chat and client search text into view", true);
   private final s L = this.n("Items", "Animate hovered inventory items", true);
   private final l j = this.addSetting(new l("Chunk Animation", "Curve for chunk appearance", "Quart", "Quart", "Circ", "Sine", "Cubic"));
   private final e S = this.addSetting(new e("Speed", "Inventory and chunk entrance duration in tenths of a second", 6.0, 2.0, 10.0, 1.0) {
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
   private final jp q = new jp(0.0);
   private final jp z = new jp(0.0);
   private final Map<Slot, GUIAnimationModule.c> A = new WeakHashMap<>();
   private Object n;
   private float s = Float.NaN;
   private long Q;

   public GUIAnimationModule() {
      super("GUI Animation", "Animate menus, chat, camera changes and chunks", Category.RENDER, -1);
      i = this;
      this.j.P(() -> !this.h.getValue());
      this.S.P(() -> !this.h.getValue() || this.r.getValue());
   }

   private s n(String var1, String var2, boolean var3) {
      return this.addSetting(new s(var1, var2, var3));
   }

   public static GUIAnimationModule a() {
      return i;
   }

   public List<q<?>> m() {
      return List.of(this.N, this.r, this.y, this.L);
   }

   public List<q<?>> w() {
      return List.of(this.E, this.e);
   }

   public List<q<?>> K() {
      return List.of(this.h, this.j, this.S);
   }

   public boolean w() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.r.getValue();
   }

   public boolean V() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.L.getValue();
   }

   public boolean b() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.y.getValue();
   }

   public boolean u() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && !this.h.getValue();
   }

   public boolean j() {
      if (!this.isEnabled()) {
         return false;
      }

      SmoothCameraModule var1 = SmoothCameraModule.P();
      return this.isRuntimeActive() && this.e.getValue() && p_.A() == p_.o.T && (var1 == null || !var1.isRuntimeActive());
   }

   public float u() {
      if (this.isRuntimeActive() && this.N.getValue()) {
         boolean var1 = getMc().options.keyPlayerList.isDown();
         return (float)Math.clamp(this.q.k(var1 ? 1.0 : 0.0, var1 ? 240 : 190), 0.0, 1.0);
      } else {
         return getMc().options.keyPlayerList.isDown() ? 1.0F : 0.0F;
      }
   }

   public boolean K() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.N.getValue();
   }

   public long n() {
      return Math.round(this.S.getValue() * 100.0);
   }

   public float f(long var1) {
      if (this.isEnabled()) {
         if (!this.w()) {
            return 1.0F;
         }

         double var3 = Math.clamp((System.nanoTime() - var1) / (this.n() * 1000000.0), 0.0, 1.0);
         return (float)(0.86 + 0.14 * jp.m.A.s(var3));
      } else {
         return 1.0F;
      }
   }

   public double d(double var1, double var3, float var5) {
      return var3 + (var1 - var3) / Math.max(0.01F, var5);
   }

   public float D(Slot var1, boolean var2) {
      if (this.isEnabled()) {
         long var3 = System.nanoTime();
         GUIAnimationModule.c var5 = this.A.get(var1);
         float var6 = var5 == null ? 1.0F : var5.scale;
         double var7 = var5 == null ? 0.0 : Math.clamp((var3 - var5.time) * 1.0E-9, 0.0, 0.1);
         float var9 = (float)(var6 + ((var2 ? 1.12 : 1.0) - var6) * (1.0 - Math.exp(-(var2 ? 6.5 : 5.0) * var7)));
         this.A.put(var1, new GUIAnimationModule.c(var9, var3));
         return var9;
      } else {
         return 1.0F;
      }
   }

   public double R(double var1) {
      var1 = Math.clamp(var1, 0.0, 1.0);

      return switch ((String)this.j.getValue()) {
         case "Circ" -> Math.sqrt(1.0 - (var1 - 1.0) * (var1 - 1.0));
         case "Sine" -> Math.sin(var1 * Math.PI / 2.0);
         case "Cubic" -> 1.0 - Math.pow(1.0 - var1, 3.0);
         default -> 1.0 - Math.pow(1.0 - var1, 4.0);
      };
   }

   public float g(boolean var1) {
      CameraType var2 = getMc().options.getCameraType();
      if (!this.j()) {
         this.n = var2;
         this.z.F(var1 ? 1.0 : 0.0);
         return var1 ? 1.0F : 0.0F;
      }

      if (this.n == null) {
         this.z.F(var1 ? 1.0 : 0.0);
      } else if (this.n != var2 && var1 && this.n != CameraType.FIRST_PERSON) {
         this.z.F(0.0);
      }

      this.n = var2;
      return (float)this.z.k(var1 ? 1.0 : 0.0, 320);
   }

   public float M(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.E.getValue() && p_.A() == p_.o.T) {
            long var2 = System.nanoTime();
            if (Float.isNaN(this.s)) {
               this.s = var1;
            } else {
               this.s = this.s + (var1 - this.s) * (float)(1.0 - Math.exp(-12.0 * Math.clamp((var2 - this.Q) * 1.0E-9, 0.0, 0.1)));
            }

            this.Q = var2;
            return this.s;
         } else {
            this.s = Float.NaN;
            return var1;
         }
      } else {
         return var1;
      }
   }

   @Override
   public void onEnable() {
      com.larpclient.h.U("GUI", "GUI Animation active: chat and typed input={}, native editing remains immediate", this.y.getValue());
   }

   @Override
   public void onDisable() {
      com.larpclient.h.U("GUI", "GUI Animation inactive; text motion settles on the next extraction");
      this.q.F(0.0);
      this.z.F(0.0);
      this.A.clear();
      this.n = null;
      this.s = Float.NaN;
      this.Q = 0L;
   }

   private record c(float scale, long time) {
   }
}
