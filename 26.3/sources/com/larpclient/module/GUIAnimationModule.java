package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.fq;
import com.larpclient.ij;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.CameraType;
import net.minecraft.world.inventory.Slot;

public final class GUIAnimationModule extends Module {
   private static GUIAnimationModule g;
   private final m K = this.U("Player List", "Slide the player list in and out", true);
   private final m U = this.U("Inventory", "Animate container entrances with aligned input", true);
   private final m b = this.U("Camera Zoom", "Smooth changes in camera field of view", false);
   public final m J = this.addSetting(new m("Chunk Update", "Keep vanilla chunk appearance; turn off to animate newly visible chunks", true) {
      @Override
      public String getDisplayName() {
         return "Native Chunks";
      }
   });
   private final m h = this.U("Perspective Change", "Ease camera distance when switching perspective", false);
   private final m o = this.U("Chat Messages", "Ease received chat, typed chat and client search text into view", true);
   private final m c = this.U("Items", "Animate hovered inventory items", true);
   private final o D = this.addSetting(new o("Chunk Animation", "Curve for chunk appearance", "Quart", "Quart", "Circ", "Sine", "Cubic"));
   private final b l = this.addSetting(new b("Speed", "Inventory and chunk entrance duration in tenths of a second", 6.0, 2.0, 10.0, 1.0) {
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
   private final fq G = new fq(0.0);
   private final fq m = new fq(0.0);
   private final Map<Slot, GUIAnimationModule.k> V = new WeakHashMap<>();
   private Object t;
   private float i = Float.NaN;
   private long F;

   public GUIAnimationModule() {
      super("GUI Animation", "Animate menus, chat, camera changes and chunks", Category.RENDER, -1);
      g = this;
      this.D.a(() -> !this.J.getValue());
      this.l.a(() -> !this.J.getValue() || this.U.getValue());
   }

   private m U(String var1, String var2, boolean var3) {
      return this.addSetting(new m(var1, var2, var3));
   }

   public static GUIAnimationModule O() {
      return g;
   }

   public List<r<?>> w() {
      return List.of(this.K, this.U, this.o, this.c);
   }

   public List<r<?>> U() {
      return List.of(this.b, this.h);
   }

   public List<r<?>> F() {
      return List.of(this.J, this.D, this.l);
   }

   public boolean h() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.U.getValue();
   }

   public boolean W() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.c.getValue();
   }

   public boolean I() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.o.getValue();
   }

   public boolean O() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && !this.J.getValue();
   }

   public boolean g() {
      if (!this.isEnabled()) {
         return false;
      }

      SmoothCameraModule var1 = SmoothCameraModule.D();
      return this.isRuntimeActive() && this.h.getValue() && ij.L() == ij.r.X && (var1 == null || !var1.isRuntimeActive());
   }

   public float S() {
      if (this.isRuntimeActive() && this.K.getValue()) {
         boolean var1 = getMc().options.keyPlayerList.isDown();
         return (float)Math.clamp(this.G.n(var1 ? 1.0 : 0.0, var1 ? 240 : 190), 0.0, 1.0);
      } else {
         return getMc().options.keyPlayerList.isDown() ? 1.0F : 0.0F;
      }
   }

   public boolean t() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.K.getValue();
   }

   public long B() {
      return Math.round(this.l.getValue() * 100.0);
   }

   public float p(long var1) {
      if (this.isEnabled()) {
         if (!this.h()) {
            return 1.0F;
         }

         double var3 = Math.clamp((System.nanoTime() - var1) / (this.B() * 1000000.0), 0.0, 1.0);
         return (float)(0.86 + 0.14 * fq.y.j.q(var3));
      } else {
         return 1.0F;
      }
   }

   public double Q(double var1, double var3, float var5) {
      return var3 + (var1 - var3) / Math.max(0.01F, var5);
   }

   public float u(Slot var1, boolean var2) {
      if (this.isEnabled()) {
         long var3 = System.nanoTime();
         GUIAnimationModule.k var5 = this.V.get(var1);
         float var6 = var5 == null ? 1.0F : var5.scale;
         double var7 = var5 == null ? 0.0 : Math.clamp((var3 - var5.time) * 1.0E-9, 0.0, 0.1);
         float var9 = (float)(var6 + ((var2 ? 1.12 : 1.0) - var6) * (1.0 - Math.exp(-(var2 ? 6.5 : 5.0) * var7)));
         this.V.put(var1, new GUIAnimationModule.k(var9, var3));
         return var9;
      } else {
         return 1.0F;
      }
   }

   public double z(double var1) {
      var1 = Math.clamp(var1, 0.0, 1.0);

      return switch ((String)this.D.getValue()) {
         case "Circ" -> Math.sqrt(1.0 - (var1 - 1.0) * (var1 - 1.0));
         case "Sine" -> Math.sin(var1 * Math.PI / 2.0);
         case "Cubic" -> 1.0 - Math.pow(1.0 - var1, 3.0);
         default -> 1.0 - Math.pow(1.0 - var1, 4.0);
      };
   }

   public float g(boolean var1) {
      CameraType var2 = getMc().options.getCameraType();
      if (!this.g()) {
         this.t = var2;
         this.m.p(var1 ? 1.0 : 0.0);
         return var1 ? 1.0F : 0.0F;
      }

      if (this.t == null) {
         this.m.p(var1 ? 1.0 : 0.0);
      } else if (this.t != var2 && var1 && this.t != CameraType.FIRST_PERSON) {
         this.m.p(0.0);
      }

      this.t = var2;
      return (float)this.m.n(var1 ? 1.0 : 0.0, 320);
   }

   public float N(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.b.getValue() && ij.L() == ij.r.X) {
            long var2 = System.nanoTime();
            if (Float.isNaN(this.i)) {
               this.i = var1;
            } else {
               this.i = this.i + (var1 - this.i) * (float)(1.0 - Math.exp(-12.0 * Math.clamp((var2 - this.F) * 1.0E-9, 0.0, 0.1)));
            }

            this.F = var2;
            return this.i;
         } else {
            this.i = Float.NaN;
            return var1;
         }
      } else {
         return var1;
      }
   }

   @Override
   public void onEnable() {
      _q.c("GUI", "GUI Animation active: chat and typed input={}, native editing remains immediate", this.o.getValue());
   }

   @Override
   public void onDisable() {
      _q.c("GUI", "GUI Animation inactive; text motion settles on the next extraction");
      this.G.p(0.0);
      this.m.p(0.0);
      this.V.clear();
      this.t = null;
      this.i = Float.NaN;
      this.F = 0L;
   }

   private record k(float scale, long time) {
   }
}
