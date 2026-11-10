package com.larpclient.module;

import com.larp.obf.q41a5851bcf8e876a.q11756370fb0d6f1d;
import com.larpclient._q;
import com.larpclient.b7;
import com.larpclient.dt;
import com.larpclient.ij;
import com.larpclient.p4;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec2;

public class FreecamModule extends Module {
   private final b a = this.addSetting(new b("Speed", "Detached camera speed", 1.0, 0.1, 50.0, 0.1));
   private final b k = this.addSetting(new b("Vertical", "Vertical speed multiplier", 1.0, 0.2, 5.0, 0.05) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.2f", this.getValue());
      }
   });
   private final m H = this.addSetting(new m("Smooth", "Use smooth half-speed camera travel", true));
   private final m r = this.addSetting(new m("Body", "Show your body from the detached camera", true));
   private final m c = this.addSetting(new m("Hands", "Show first-person hands while detached", true));
   private final m p = this.addSetting(new m("Keys Move Body", "Also steer your body with movement keys", false));
   private final m n = this.addSetting(new m("Keep Walking", "Carry movement held when Freecam starts", true));
   private final m z = this.addSetting(new m("Scroll Speed", "Adjust camera speed with the mouse wheel", true));
   private final m G = this.addSetting(new m("Reset Scroll Speed", "Keep wheel changes temporary until Freecam ends", true));
   private final b d = this.addSetting(new b("Chunks", "Client chunk-view radius, within server-provided terrain", 12.0, 2.0, 32.0, 1.0));
   private final o E = this.addSetting(new o("Activation", "Toggle the module key or keep it held to use Freecam", "Toggle", "Toggle", "Hold"));
   private final b7 i = new b7();
   private static FreecamModule B;
   private LocalPlayer g;
   private ClientLevel I;
   private b7.e q;
   private boolean j;
   private boolean t;
   private boolean h;
   private double M;

   @Override
   public Module.a getTickPhase() {
      return Module.a.n;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F);
   }

   public FreecamModule() {
      super("Freecam", "Explore with a detached camera", Category.PLAYER, -1);
      B = this;
      this.G.a(this.z::getValue);
      this.a.w(var1 -> this.i.b());
      this.n.w(var1 -> {
         if (!var1) {
            this.i.F();
         }
      });
      this.E.w(var1 -> {
         if (this.E() || this.h) {
            this.setEnabled(false);
         }
      });
   }

   public boolean E() {
      return this.E.w("Hold");
   }

   public void D(double var1) {
      this.M = Math.clamp(var1, 1.0, 40.0);
      _q.z("Freecam", "Guided camera speed {} blocks/s; Speed setting stays {}", this.M, this.a.getValue());
   }

   public void Q() {
      this.M = 0.0;
   }

   public double O() {
      return this.M;
   }

   private boolean D() {
      return p4.Y(getMc().getWindow().handle(), this.getKeyBind());
   }

   public boolean O() {
      if (this.isEnabled()) {
         if (this.E() && this.D()) {
            this.h = (boolean)1;
            this.enable();
            return this.isEnabled();
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public void enable() {
      if (!this.E() || this.h) {
         super.enable();
      }
   }

   @Override
   public void disable() {
      this.h = (boolean)0;
      super.disable();
   }

   @Override
   public void suspendForDisconnect() {
      if (this.E()) {
         this.disable();
      } else {
         super.suspendForDisconnect();
      }
   }

   public static void V() {
      FreecamModule var0 = B;
      if (var0 != null && var0.E() && var0.isEnabled()) {
         Minecraft var1 = getMc();
         if (var1 == null
            || var1.player == null
            || var1.level == null
            || !var1.player.isAlive()
            || var1.gui.screen() != null
            || var1.gui.overlay() != null
            || !var1.isWindowActive()
            || var0.g != null && (var0.g != var1.player || var0.I != var1.level)
            || !var0.D()) {
            var0.setEnabled(false);
         }
      }
   }

   @Override
   public void setKeyBind(int var1) {
      if (this.E() && var1 != this.getKeyBind()) {
         this.setEnabled(false);
      }

      super.setKeyBind(var1);
   }

   @Override
   public void onEnable() {
      Minecraft var1 = getMc();
      this.g = var1.player;
      this.I = var1.level;
      boolean var2 = dt.q();
      ij.V(ij.r.f);
      this.i.a(var2 ? C() : b7.M(C(), this.g.input.keyPresses), this.j(), this.n.getValue());
      this.s(true, var2);
      this.e();
      this.x();
      _q.z(
         "Freecam",
         "Detached camera acquired; activation={}, speed={}, smooth={}, carry={}, body keys={}",
         this.E.getValue(),
         this.a.getValue(),
         this.H.getValue(),
         this.n.getValue(),
         this.p.getValue()
      );
      _q.c("Freecam", "Terrain visibility starts from loaded columns in view, including across unloaded camera chunks");
   }

   @Override
   public void onDisable() {
      Minecraft var1 = getMc();

      try {
         if (this.I != null && this.g != null) {
            ChunkPos var2 = this.g.chunkPosition();
            this.I.getChunkSource().updateViewCenter(var2.x(), var2.z());
            this.I.getChunkSource().updateViewRadius(this.A());
         }

         if (this.g == var1.player && this.I == var1.level && this.g != null && this.g.isAlive()) {
            if (this.j && this.g.getAbilities().mayfly && !this.g.getAbilities().flying) {
               this.g.getAbilities().flying = this.t;
            }

            this.g.input.keyPresses = Input.EMPTY;
         }
      } finally {
         this.h = (boolean)0;
         this.M = 0.0;
         this.j = (boolean)0;
         this.g = null;
         this.I = null;
         this.q = null;
         this.i.A();
         ij.W(ij.r.f);
         if (var1.level != null) {
            var1.levelRenderer.sectionOcclusionGraph().invalidate();
         }

         _q.z("Freecam", "Released camera, body input and chunk view");
      }
   }

   @Override
   public void onTick() {
      V();
      if (this.isRuntimeActive()) {
         ij.V(ij.r.f);
         this.s(false, false);
         this.e();
         double var3;
         int var10000 = (var3 = this.M - 0.0) == 0.0 ? 0 : (var3 < 0.0 ? -1 : 1);
         boolean var2 = false;
         int var1 = var10000;
         if ((-Integer.signum(var1) >>> 31 ^ 1) == 0) {
            ij.y(this.M / 20.0, 1.0, false);
         } else {
            ij.y(this.i.q(this.a.getValue(), this.G.getValue()), this.k.getValue(), this.H.getValue());
         }

         this.x();
      }
   }

   public static boolean L() {
      return B != null && B.isEnabled() ? ij.y() : false;
   }

   public static boolean T() {
      return B != null && B.isEnabled() ? B != null && B.r.getValue() : false;
   }

   public static boolean K() {
      return B != null && B.isEnabled() ? B != null && B.c.getValue() : false;
   }

   public static Input D(Input var0) {
      if (B != null && B.isEnabled()) {
         FreecamModule var1 = B;
         Minecraft var2 = Minecraft.getInstance();
         return var1 != null && var2.gui.screen() == null && var2.isWindowActive() ? var1.i.J(var0, var1.p.getValue()) : Input.EMPTY;
      } else {
         return var0;
      }
   }

   public static Input C() {
      Options var0 = Minecraft.getInstance().options;
      return new Input(
         var0.keyUp.isDown(),
         var0.keyDown.isDown(),
         var0.keyLeft.isDown(),
         var0.keyRight.isDown(),
         var0.keyJump.isDown(),
         var0.keyShift.isDown(),
         var0.keySprint.isDown()
      );
   }

   public static Input h() {
      FreecamModule var0 = B;
      Minecraft var1 = Minecraft.getInstance();
      return var0 != null && var1.gui.screen() == null && var1.isWindowActive() ? var0.i.B(C(), var0.p.getValue()) : Input.EMPTY;
   }

   private boolean j() {
      return this.g.isShiftKeyDown() && !ScaffoldModule.s(this.g);
   }

   private void s(boolean var1, boolean var2) {
      if (this.g != null) {
         float var3 = ScaffoldModule.Y();
         int var4 = !var2 && Float.isNaN(var3) ? 0 : 1;
         this.i
            .W(
               var4 != 0 ? C() : b7.M(C(), this.g.input.keyPresses),
               this.n.getValue(),
               this.p.getValue(),
               this.j(),
               var4 != 0 ? Vec2.ZERO : new Vec2(this.g.xxa, this.g.zza),
               this.g.getDeltaMovement(),
               Float.isNaN(var3) ? this.g.getYRot() : var3,
               var1
            );
      }
   }

   private void e() {
      if (this.g != null && this.g.getAbilities().mayfly) {
         if (!this.j) {
            this.t = this.g.getAbilities().flying;
            this.j = (boolean)1;
         }

         this.g.getAbilities().flying = (boolean)0;
      }
   }

   public static void D() {
      if (B != null) {
         B.j = (boolean)0;
      }
   }

   private void x() {
      if (this.I != null && this.g != null) {
         ChunkPos var1 = this.g.chunkPosition();
         b7.e var2 = b7.B(ij.J(1.0F), var1.x(), var1.z(), this.d.getValue().intValue(), this.A());
         ClientChunkCache var3 = this.I.getChunkSource();
         var3.updateViewCenter(var2.x(), var2.z());
         var3.updateViewRadius(var2.radius());
         if (!var2.equals(this.q)) {
            getMc().levelRenderer.sectionOcclusionGraph().invalidate();
            this.q = var2;
         }
      }
   }

   private int A() {
      return this.g == null ? getMc().options.getEffectiveRenderDistance() : ((q11756370fb0d6f1d)this.g.connection).larpServerChunkRadius();
   }

   public static boolean V(double var0) {
      if (B != null && B.isEnabled()) {
         FreecamModule var2 = B;
         Minecraft var3 = Minecraft.getInstance();
         if (var2 == null
            || !ij.y()
            || !var2.z.getValue() && var2.M <= 0.0
            || var3.gui.screen() != null
            || var3.gui.overlay() != null
            || !var3.isWindowActive()) {
            return false;
         }

         if (var2.M > 0.0) {
            if (Double.isFinite(var0) && var0 != 0.0) {
               var2.M = b7.G(var2.M, var0);
               _q.c("Freecam", "Wheel guided camera speed={} blocks/s", var2.M);
               return true;
            } else {
               return false;
            }
         } else {
            if (!var2.i.j(var0, var2.a.getValue(), var2.G.getValue())) {
               return false;
            }

            if (!var2.G.getValue()) {
               var2.a.O(var2.a.getValue() * (var0 > 0.0 ? 1.2 : 0.8333333333333334));
            }

            _q.c("Freecam", "Wheel camera speed={}", var2.i.q(var2.a.getValue(), var2.G.getValue()));
            return true;
         }
      } else {
         return false;
      }
   }
}
