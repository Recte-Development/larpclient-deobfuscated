package com.larpclient.module;

import com.larpclient.ef;
import com.larpclient.mt;
import com.larpclient.tq;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.CameraType;
import net.minecraft.world.inventory.Slot;

public final class GUIAnimationModule extends Module {
   private static GUIAnimationModule x;
   private final dh X = this.w("Player List", "Slide the player list in and out", true);
   private final dh d = this.w("Inventory", "Animate container entrances with aligned input", true);
   private final dh G = this.w("Camera Zoom", "Smooth changes in camera field of view", false);
   private final dh q = this.addSetting(new dh("Chunk Update", "Keep vanilla chunk appearance; turn off to animate newly visible chunks", true) {
      @Override
      public String getDisplayName() {
         return "Native Chunks";
      }
   });
   private final dh p = this.w("Perspective Change", "Ease camera distance when switching perspective", false);
   private final dh L = this.w("Chat Messages", "Ease received chat, typed chat and client search text into view", true);
   private final dh z = this.w("Items", "Animate hovered inventory items", true);
   private final d6 g = this.addSetting(new d6("Chunk Animation", "Curve for chunk appearance", "Quart", "Quart", "Circ", "Sine", "Cubic"));
   private final d9 T = this.addSetting(new d9("Speed", "Inventory and chunk entrance duration in tenths of a second", 6.0, 2.0, 10.0, 1.0) {
      @Override
      public String getDisplayName() {
         return "Duration";
      }

      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.1f s", this.P() / 10.0);
      }

      @Override
      public String getEditValueAsString() {
         return BigDecimal.valueOf(this.P() / 10.0).stripTrailingZeros().toPlainString();
      }

      @Override
      public void setFromEditString(String var1) {
         super.setFromEditString(Double.toString(Double.parseDouble(var1.strip().replace("s", "")) * 10.0));
      }
   });
   private final ef I = new ef(0.0);
   private final ef P = new ef(0.0);
   private final Map<Slot, GUIAnimationModule.k> R = new WeakHashMap<>();
   private Object F;
   private float E = Float.NaN;
   private long Q;

   public GUIAnimationModule() {
      super("GUI Animation", "Animate menus, chat, camera changes and chunks", Category.RENDER, -1);
      x = this;
      this.g.H(() -> !this.q.P());
      this.T.H(() -> !this.q.P() || this.d.P());
   }

   private dh w(String var1, String var2, boolean var3) {
      return this.addSetting(new dh(var1, var2, var3));
   }

   public static GUIAnimationModule F() {
      return x;
   }

   public List<df<?>> N() {
      return List.of(this.X, this.d, this.L, this.z);
   }

   public List<df<?>> q() {
      return List.of(this.G, this.p);
   }

   public List<df<?>> E() {
      return List.of(this.q, this.g, this.T);
   }

   public boolean F() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.d.P();
   }

   public boolean l() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.z.P();
   }

   public boolean e() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.L.P();
   }

   public boolean t() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && !this.q.P();
   }

   public boolean i() {
      if (!this.isEnabled()) {
         return false;
      }

      SmoothCameraModule var1 = SmoothCameraModule.d();
      return this.isRuntimeActive() && this.p.P() && tq.l() == tq.y.A && (var1 == null || !var1.isRuntimeActive());
   }

   public float I() {
      if (this.isRuntimeActive() && this.X.P()) {
         boolean var1 = getMc().options.keyPlayerList.isDown();
         return (float)Math.clamp(this.I.m(var1 ? 1.0 : 0.0, var1 ? 240 : 190), 0.0, 1.0);
      } else {
         return getMc().options.keyPlayerList.isDown() ? 1.0F : 0.0F;
      }
   }

   public boolean K() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.X.P();
   }

   public long f() {
      return Math.round(this.T.P() * 100.0);
   }

   public float l(long var1) {
      if (this.isEnabled()) {
         if (!this.F()) {
            return 1.0F;
         }

         double var3 = Math.clamp((System.nanoTime() - var1) / (this.f() * 1000000.0), 0.0, 1.0);
         return (float)(0.86 + 0.14 * ef.q.N.k(var3));
      } else {
         return 1.0F;
      }
   }

   public double j(double var1, double var3, float var5) {
      return var3 + (var1 - var3) / Math.max(0.01F, var5);
   }

   public float k(Slot var1, boolean var2) {
      if (this.isEnabled()) {
         long var3 = System.nanoTime();
         GUIAnimationModule.k var5 = this.R.get(var1);
         float var6 = var5 == null ? 1.0F : var5.scale;
         double var7 = var5 == null ? 0.0 : Math.clamp((var3 - var5.time) * 1.0E-9, 0.0, 0.1);
         float var9 = (float)(var6 + ((var2 ? 1.12 : 1.0) - var6) * (1.0 - Math.exp(-(var2 ? 6.5 : 5.0) * var7)));
         this.R.put(var1, new GUIAnimationModule.k(var9, var3));
         return var9;
      } else {
         return 1.0F;
      }
   }

   public double b(double var1) {
      var1 = Math.clamp(var1, 0.0, 1.0);

      return switch ((String)this.g.P()) {
         case "Circ" -> Math.sqrt(1.0 - (var1 - 1.0) * (var1 - 1.0));
         case "Sine" -> Math.sin(var1 * Math.PI / 2.0);
         case "Cubic" -> 1.0 - Math.pow(1.0 - var1, 3.0);
         default -> 1.0 - Math.pow(1.0 - var1, 4.0);
      };
   }

   public float b(boolean var1) {
      CameraType var2 = getMc().options.getCameraType();
      if (!this.i()) {
         this.F = var2;
         this.P.Q(var1 ? 1.0 : 0.0);
         return var1 ? 1.0F : 0.0F;
      }

      if (this.F == null) {
         this.P.Q(var1 ? 1.0 : 0.0);
      } else if (this.F != var2 && var1 && this.F != CameraType.FIRST_PERSON) {
         this.P.Q(0.0);
      }

      this.F = var2;
      return (float)this.P.m(var1 ? 1.0 : 0.0, 320);
   }

   public float J(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.G.P() && tq.l() == tq.y.A) {
            long var2 = System.nanoTime();
            if (Float.isNaN(this.E)) {
               this.E = var1;
            } else {
               this.E = this.E + (var1 - this.E) * (float)(1.0 - Math.exp(-12.0 * Math.clamp((var2 - this.Q) * 1.0E-9, 0.0, 0.1)));
            }

            this.Q = var2;
            return this.E;
         } else {
            this.E = Float.NaN;
            return var1;
         }
      } else {
         return var1;
      }
   }

   @Override
   public void onEnable() {
      mt.U("GUI", "GUI Animation active: chat and typed input={}, native editing remains immediate", this.L.P());
   }

   @Override
   public void onDisable() {
      mt.U("GUI", "GUI Animation inactive; text motion settles on the next extraction");
      this.I.Q(0.0);
      this.P.Q(0.0);
      this.R.clear();
      this.F = null;
      this.E = Float.NaN;
      this.Q = 0L;
   }

   private record k(float scale, long time) {
   }
}
