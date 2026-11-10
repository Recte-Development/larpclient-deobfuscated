package com.larpclient.module;

import com.larp.obf.qa300483f54d804a3.q90878c7ca95059f9;
import com.larpclient.bc;
import com.larpclient.cj;
import com.larpclient.cr;
import com.larpclient.p_;
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
   private final e v = this.addSetting(new e("Speed", "Detached camera speed", 1.0, 0.1, 50.0, 0.1));
   private final e N = this.addSetting(new e("Vertical", "Vertical speed multiplier", 1.0, 0.2, 5.0, 0.05) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.2f", this.getValue());
      }
   });
   private final s G = this.addSetting(new s("Smooth", "Use smooth half-speed camera travel", true));
   private final s J = this.addSetting(new s("Body", "Show your body from the detached camera", true));
   private final s C = this.addSetting(new s("Hands", "Show first-person hands while detached", true));
   private final s Z = this.addSetting(new s("Keys Move Body", "Also steer your body with movement keys", false));
   private final s A = this.addSetting(new s("Keep Walking", "Carry movement held when Freecam starts", true));
   private final s Y = this.addSetting(new s("Scroll Speed", "Adjust camera speed with the mouse wheel", true));
   private final s O = this.addSetting(new s("Reset Scroll Speed", "Keep wheel changes temporary until Freecam ends", true));
   private final e S = this.addSetting(new e("Chunks", "Client chunk-view radius, within server-provided terrain", 12.0, 2.0, 32.0, 1.0));
   private final l h = this.addSetting(new l("Activation", "Toggle the module key or keep it held to use Freecam", "Toggle", "Toggle", "Hold"));
   private final cr U = new cr();
   private static FreecamModule e;
   private LocalPlayer H;
   private ClientLevel j;
   private cr.e X;
   private boolean m;
   private boolean x;
   private boolean t;
   private double i;

   @Override
   public Module.f getTickPhase() {
      return Module.f.N;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.K);
   }

   public FreecamModule() {
      super("Freecam", "Explore with a detached camera", Category.PLAYER, -1);
      e = this;
      this.O.P(this.Y::getValue);
      this.v.o(var1 -> this.U.L());
      this.A.o(var1 -> {
         if (!var1) {
            this.U.j();
         }
      });
      this.h.o(var1 -> {
         if (this.D() || this.t) {
            this.setEnabled(false);
         }
      });
   }

   public boolean D() {
      return this.h.O("Hold");
   }

   public void u(double var1) {
      this.i = Math.clamp(var1, 1.0, 40.0);
      com.larpclient.h.A("Freecam", "Guided camera speed {} blocks/s; Speed setting stays {}", this.i, this.v.getValue());
   }

   public void F() {
      this.i = 0.0;
   }

   public double V() {
      return this.i;
   }

   private boolean I() {
      return bc.I(getMc().getWindow().handle(), this.getKeyBind());
   }

   public boolean g() {
      if (this.isEnabled()) {
         if (this.D() && this.I()) {
            this.t = (boolean)1;
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
      if (!this.D() || this.t) {
         super.enable();
      }
   }

   @Override
   public void disable() {
      this.t = (boolean)0;
      super.disable();
   }

   @Override
   public void suspendForDisconnect() {
      if (this.D()) {
         this.disable();
      } else {
         super.suspendForDisconnect();
      }
   }

   public static void C() {
      FreecamModule var0 = e;
      if (var0 != null && var0.D() && var0.isEnabled()) {
         Minecraft var1 = getMc();
         if (var1 == null
            || var1.player == null
            || var1.level == null
            || !var1.player.isAlive()
            || var1.gui.screen() != null
            || var1.gui.overlay() != null
            || !var1.isWindowActive()
            || var0.H != null && (var0.H != var1.player || var0.j != var1.level)
            || !var0.I()) {
            var0.setEnabled(false);
         }
      }
   }

   @Override
   public void setKeyBind(int var1) {
      if (this.D() && var1 != this.getKeyBind()) {
         this.setEnabled(false);
      }

      super.setKeyBind(var1);
   }

   @Override
   public void onEnable() {
      Minecraft var1 = getMc();
      this.H = var1.player;
      this.j = var1.level;
      boolean var2 = cj.D();
      p_.p(p_.o.p);
      this.U.m(var2 ? o() : cr.q(o(), this.H.input.keyPresses), this.i(), this.A.getValue());
      this.y(true, var2);
      this.B();
      this.J();
      com.larpclient.h.A(
         "Freecam",
         "Detached camera acquired; activation={}, speed={}, smooth={}, carry={}, body keys={}",
         this.h.getValue(),
         this.v.getValue(),
         this.G.getValue(),
         this.A.getValue(),
         this.Z.getValue()
      );
      com.larpclient.h.U("Freecam", "Terrain visibility starts from loaded columns in view, including across unloaded camera chunks");
   }

   @Override
   public void onDisable() {
      Minecraft var1 = getMc();

      try {
         if (this.j != null && this.H != null) {
            ChunkPos var2 = this.H.chunkPosition();
            this.j.getChunkSource().updateViewCenter(var2.x(), var2.z());
            this.j.getChunkSource().updateViewRadius(this.J());
         }

         if (this.H == var1.player && this.j == var1.level && this.H != null && this.H.isAlive()) {
            if (this.m && this.H.getAbilities().mayfly && !this.H.getAbilities().flying) {
               this.H.getAbilities().flying = this.x;
            }

            this.H.input.keyPresses = Input.EMPTY;
         }
      } finally {
         this.t = (boolean)0;
         this.i = 0.0;
         this.m = (boolean)0;
         this.H = null;
         this.j = null;
         this.X = null;
         this.U.P();
         p_.Y(p_.o.p);
         if (var1.level != null) {
            var1.levelRenderer.sectionOcclusionGraph().invalidate();
         }

         com.larpclient.h.A("Freecam", "Released camera, body input and chunk view");
      }
   }

   @Override
   public void onTick() {
      C();
      if (this.isRuntimeActive()) {
         p_.p(p_.o.p);
         this.y(false, false);
         this.B();
         double var3;
         int var10000 = (var3 = this.i - 0.0) == 0.0 ? 0 : (var3 < 0.0 ? -1 : 1);
         boolean var2 = false;
         int var1 = var10000;
         if ((-Integer.signum(var1) >>> 31 ^ 1) == 0) {
            p_.e(this.i / 20.0, 1.0, false);
         } else {
            p_.e(this.U.Z(this.v.getValue(), this.O.getValue()), this.N.getValue(), this.G.getValue());
         }

         this.J();
      }
   }

   public static boolean K() {
      return e != null && e.isEnabled() ? p_.V() : false;
   }

   public static boolean Z() {
      return e != null && e.isEnabled() ? e != null && e.J.getValue() : false;
   }

   public static boolean F() {
      return e != null && e.isEnabled() ? e != null && e.C.getValue() : false;
   }

   public static Input T(Input var0) {
      if (e != null && e.isEnabled()) {
         FreecamModule var1 = e;
         Minecraft var2 = Minecraft.getInstance();
         return var1 != null && var2.gui.screen() == null && var2.isWindowActive() ? var1.U.g(var0, var1.Z.getValue()) : Input.EMPTY;
      } else {
         return var0;
      }
   }

   public static Input o() {
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

   public static Input f() {
      FreecamModule var0 = e;
      Minecraft var1 = Minecraft.getInstance();
      return var0 != null && var1.gui.screen() == null && var1.isWindowActive() ? var0.U.t(o(), var0.Z.getValue()) : Input.EMPTY;
   }

   private boolean i() {
      return this.H.isShiftKeyDown() && !ScaffoldModule.G(this.H);
   }

   private void y(boolean var1, boolean var2) {
      if (this.H != null) {
         float var3 = ScaffoldModule.X();
         int var4 = !var2 && Float.isNaN(var3) ? 0 : 1;
         this.U
            .F(
               var4 != 0 ? o() : cr.q(o(), this.H.input.keyPresses),
               this.A.getValue(),
               this.Z.getValue(),
               this.i(),
               var4 != 0 ? Vec2.ZERO : new Vec2(this.H.xxa, this.H.zza),
               this.H.getDeltaMovement(),
               Float.isNaN(var3) ? this.H.getYRot() : var3,
               var1
            );
      }
   }

   private void B() {
      if (this.H != null && this.H.getAbilities().mayfly) {
         if (!this.m) {
            this.x = this.H.getAbilities().flying;
            this.m = (boolean)1;
         }

         this.H.getAbilities().flying = (boolean)0;
      }
   }

   public static void f() {
      if (e != null) {
         e.m = (boolean)0;
      }
   }

   private void J() {
      if (this.j != null && this.H != null) {
         ChunkPos var1 = this.H.chunkPosition();
         cr.e var2 = cr.K(p_.S(1.0F), var1.x(), var1.z(), this.S.getValue().intValue(), this.J());
         ClientChunkCache var3 = this.j.getChunkSource();
         var3.updateViewCenter(var2.x(), var2.z());
         var3.updateViewRadius(var2.radius());
         if (!var2.equals(this.X)) {
            getMc().levelRenderer.sectionOcclusionGraph().invalidate();
            this.X = var2;
         }
      }
   }

   private int J() {
      return this.H == null ? getMc().options.getEffectiveRenderDistance() : ((q90878c7ca95059f9)this.H.connection).larpServerChunkRadius();
   }

   public static boolean R(double var0) {
      if (e != null && e.isEnabled()) {
         FreecamModule var2 = e;
         Minecraft var3 = Minecraft.getInstance();
         if (var2 == null
            || !p_.V()
            || !var2.Y.getValue() && var2.i <= 0.0
            || var3.gui.screen() != null
            || var3.gui.overlay() != null
            || !var3.isWindowActive()) {
            return false;
         }

         if (var2.i > 0.0) {
            if (Double.isFinite(var0) && var0 != 0.0) {
               var2.i = cr.a(var2.i, var0);
               com.larpclient.h.U("Freecam", "Wheel guided camera speed={} blocks/s", var2.i);
               return true;
            } else {
               return false;
            }
         } else {
            if (!var2.U.d(var0, var2.v.getValue(), var2.O.getValue())) {
               return false;
            }

            if (!var2.O.getValue()) {
               var2.v.X(var2.v.getValue() * (var0 > 0.0 ? 1.2 : 0.8333333333333334));
            }

            com.larpclient.h.U("Freecam", "Wheel camera speed={}", var2.U.Z(var2.v.getValue(), var2.O.getValue()));
            return true;
         }
      } else {
         return false;
      }
   }
}
