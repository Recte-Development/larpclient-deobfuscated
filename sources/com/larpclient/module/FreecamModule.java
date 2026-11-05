package com.larpclient.module;

import com.larp.obf.q5682c6a8aca9b6ec.q2b89c22912c19361;
import com.larpclient.pe;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.yw;
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
   private final lc E = this.addSetting(new lc("Speed", "Detached camera speed", 1.0, 0.1, 50.0, 0.1));
   private final lc N = this.addSetting(new lc("Vertical", "Vertical speed multiplier", 1.0, 0.2, 5.0, 0.05) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.2f", this.getValue());
      }
   });
   private final ln e = this.addSetting(new ln("Smooth", "Use smooth half-speed camera travel", true));
   private final ln a = this.addSetting(new ln("Body", "Show your body from the detached camera", true));
   private final ln R = this.addSetting(new ln("Hands", "Show first-person hands while detached", true));
   private final ln n = this.addSetting(new ln("Keys Move Body", "Also steer your body with movement keys", false));
   private final ln V = this.addSetting(new ln("Keep Walking", "Carry movement held when Freecam starts", true));
   private final ln y = this.addSetting(new ln("Scroll Speed", "Adjust camera speed with the mouse wheel", true));
   private final ln C = this.addSetting(new ln("Reset Scroll Speed", "Keep wheel changes temporary until Freecam ends", true));
   private final lc T = this.addSetting(new lc("Chunks", "Client chunk-view radius, within server-provided terrain", 12.0, 2.0, 32.0, 1.0));
   private final lp q = this.addSetting(new lp("Activation", "Toggle the module key or keep it held to use Freecam", "Toggle", "Toggle", "Hold"));
   private final yw f = new yw();
   private static FreecamModule K;
   private LocalPlayer s;
   private ClientLevel i;
   private yw.n m;
   private boolean W;
   private boolean I;
   private boolean O;

   @Override
   public Module.b getTickPhase() {
      return Module.b.m;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a);
   }

   public FreecamModule() {
      super("Freecam", "Explore with a detached camera", Category.PLAYER, -1);
      K = this;
      this.C.x(this.y::getValue);
      this.E.W(var1 -> this.f.A());
      this.V.W(var1 -> {
         if (!var1) {
            this.f.m();
         }
      });
      this.q.W(var1 -> {
         if (this.t() || this.O) {
            this.setEnabled(false);
         }
      });
   }

   public boolean t() {
      return this.q.i("Hold");
   }

   private boolean r() {
      return pe.A(getMc().getWindow().handle(), this.getKeyBind());
   }

   public boolean O() {
      if (this.isEnabled()) {
         if (this.t() && this.r()) {
            this.O = (boolean)1;
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
      if (!this.t() || this.O) {
         super.enable();
      }
   }

   @Override
   public void disable() {
      this.O = (boolean)0;
      super.disable();
   }

   @Override
   public void suspendForDisconnect() {
      if (this.t()) {
         this.disable();
      } else {
         super.suspendForDisconnect();
      }
   }

   public static void y() {
      FreecamModule var0 = K;
      if (var0 != null && var0.t() && var0.isEnabled()) {
         Minecraft var1 = getMc();
         if (var1 == null
            || var1.player == null
            || var1.level == null
            || !var1.player.isAlive()
            || var1.gui.screen() != null
            || var1.gui.overlay() != null
            || !var1.isWindowActive()
            || var0.s != null && (var0.s != var1.player || var0.i != var1.level)
            || !var0.r()) {
            var0.setEnabled(false);
         }
      }
   }

   @Override
   public void setKeyBind(int var1) {
      if (this.t() && var1 != this.getKeyBind()) {
         this.setEnabled(false);
      }

      super.setKeyBind(var1);
   }

   @Override
   public void onEnable() {
      Minecraft var1 = getMc();
      this.s = var1.player;
      this.i = var1.level;
      rr.W(rr.f.u);
      this.f.t(yw.U(w(), this.s.input.keyPresses), this.s.isShiftKeyDown(), this.V.getValue());
      this.z(true);
      this.b();
      this.o();
      r4.V(
         "Freecam",
         "Detached camera acquired; activation={}, speed={}, smooth={}, carry={}, body keys={}",
         this.q.getValue(),
         this.E.getValue(),
         this.e.getValue(),
         this.V.getValue(),
         this.n.getValue()
      );
      r4.g("Freecam", "Terrain visibility starts from loaded columns in view, including across unloaded camera chunks");
   }

   @Override
   public void onDisable() {
      Minecraft var1 = getMc();

      try {
         if (this.i != null && this.s != null) {
            ChunkPos var2 = this.s.chunkPosition();
            this.i.getChunkSource().updateViewCenter(var2.x(), var2.z());
            this.i.getChunkSource().updateViewRadius(this.D());
         }

         if (this.s == var1.player && this.i == var1.level && this.s != null && this.s.isAlive()) {
            if (this.W && this.s.getAbilities().mayfly && !this.s.getAbilities().flying) {
               this.s.getAbilities().flying = this.I;
            }

            this.s.input.keyPresses = Input.EMPTY;
         }
      } finally {
         this.O = (boolean)0;
         this.W = (boolean)0;
         this.s = null;
         this.i = null;
         this.m = null;
         this.f.S();
         rr.N(rr.f.u);
         if (var1.level != null) {
            var1.levelRenderer.sectionOcclusionGraph().invalidate();
         }

         r4.V("Freecam", "Released camera, body input and chunk view");
      }
   }

   @Override
   public void onTick() {
      y();
      if (this.isRuntimeActive()) {
         rr.W(rr.f.u);
         this.z(false);
         this.b();
         rr.S(this.f.y(this.E.getValue(), this.C.getValue()), this.N.getValue(), this.e.getValue());
         this.o();
      }
   }

   public static boolean x() {
      return K != null && K.isEnabled() ? rr.o() : false;
   }

   public static boolean j() {
      return K != null && K.isEnabled() ? K != null && K.a.getValue() : false;
   }

   public static boolean U() {
      return K != null && K.isEnabled() ? K != null && K.R.getValue() : false;
   }

   public static Input Z(Input var0) {
      if (K != null && K.isEnabled()) {
         FreecamModule var1 = K;
         Minecraft var2 = Minecraft.getInstance();
         return var1 != null && var2.gui.screen() == null && var2.isWindowActive() ? var1.f.d(var0, var1.n.getValue()) : Input.EMPTY;
      } else {
         return var0;
      }
   }

   public static Input w() {
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

   private void z(boolean var1) {
      if (this.s != null) {
         this.f
            .a(
               yw.U(w(), this.s.input.keyPresses),
               this.V.getValue(),
               this.n.getValue(),
               this.s.isShiftKeyDown(),
               new Vec2(this.s.xxa, this.s.zza),
               this.s.getDeltaMovement(),
               this.s.getYRot(),
               var1
            );
      }
   }

   private void b() {
      if (this.s != null && this.s.getAbilities().mayfly) {
         if (!this.W) {
            this.I = this.s.getAbilities().flying;
            this.W = (boolean)1;
         }

         this.s.getAbilities().flying = (boolean)0;
      }
   }

   public static void I() {
      if (K != null) {
         K.W = (boolean)0;
      }
   }

   private void o() {
      if (this.i != null && this.s != null) {
         ChunkPos var1 = this.s.chunkPosition();
         yw.n var2 = yw.a(rr.i(1.0F), var1.x(), var1.z(), this.T.getValue().intValue(), this.D());
         ClientChunkCache var3 = this.i.getChunkSource();
         var3.updateViewCenter(var2.x(), var2.z());
         var3.updateViewRadius(var2.radius());
         if (!var2.equals(this.m)) {
            getMc().levelRenderer.sectionOcclusionGraph().invalidate();
            this.m = var2;
         }
      }
   }

   private int D() {
      return this.s == null ? getMc().options.getEffectiveRenderDistance() : ((q2b89c22912c19361)this.s.connection).larpServerChunkRadius();
   }

   public static boolean Z(double var0) {
      if (K != null && K.isEnabled()) {
         FreecamModule var2 = K;
         Minecraft var3 = Minecraft.getInstance();
         if (var2 == null || !rr.o() || !var2.y.getValue() || var3.gui.screen() != null || var3.gui.overlay() != null || !var3.isWindowActive()) {
            return false;
         }

         if (!var2.f.p(var0, var2.E.getValue(), var2.C.getValue())) {
            return false;
         }

         if (!var2.C.getValue()) {
            var2.E.C(var2.E.getValue() * (var0 > 0.0 ? 1.2 : 0.8333333333333334));
         }

         r4.g("Freecam", "Wheel camera speed={}", var2.f.y(var2.E.getValue(), var2.C.getValue()));
         return true;
      } else {
         return false;
      }
   }
}
