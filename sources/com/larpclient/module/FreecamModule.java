package com.larpclient.module;

import com.larp.obf.qbac0b262827a15ac.q1ff3fe7b68a0197e;
import com.larpclient.cc;
import com.larpclient.hs;
import com.larpclient.mt;
import com.larpclient.tq;
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
   private final d9 h = this.addSetting(new d9("Speed", "Detached camera speed", 1.0, 0.1, 50.0, 0.1));
   private final d9 s = this.addSetting(new d9("Vertical", "Vertical speed multiplier", 1.0, 0.2, 5.0, 0.05) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.2f", this.P());
      }
   });
   private final dh t = this.addSetting(new dh("Smooth", "Use smooth half-speed camera travel", true));
   private final dh o = this.addSetting(new dh("Body", "Show your body from the detached camera", true));
   private final dh i = this.addSetting(new dh("Hands", "Show first-person hands while detached", true));
   private final dh p = this.addSetting(new dh("Keys Move Body", "Also steer your body with movement keys", false));
   private final dh y = this.addSetting(new dh("Keep Walking", "Carry movement held when Freecam starts", true));
   private final dh x = this.addSetting(new dh("Scroll Speed", "Adjust camera speed with the mouse wheel", true));
   private final dh A = this.addSetting(new dh("Reset Scroll Speed", "Keep wheel changes temporary until Freecam ends", true));
   private final d9 P = this.addSetting(new d9("Chunks", "Client chunk-view radius, within server-provided terrain", 12.0, 2.0, 32.0, 1.0));
   private final d6 r = this.addSetting(new d6("Activation", "Toggle the module key or keep it held to use Freecam", "Toggle", "Toggle", "Hold"));
   private final hs E = new hs();
   private static FreecamModule S;
   private LocalPlayer C;
   private ClientLevel l;
   private hs.n Z;
   private boolean D;
   private boolean b;
   private boolean Y;

   @Override
   public Module.f getTickPhase() {
      return Module.f.M;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z);
   }

   public FreecamModule() {
      super("Freecam", "Explore with a detached camera", Category.PLAYER, -1);
      S = this;
      this.A.H(this.x::P);
      this.h.h(var1 -> this.E.Q());
      this.y.h(var1 -> {
         if (!var1) {
            this.E.t();
         }
      });
      this.r.h(var1 -> {
         if (this.w() || this.Y) {
            this.setEnabled(false);
         }
      });
   }

   public boolean w() {
      return this.r.S("Hold");
   }

   private boolean H() {
      return cc.N(getMc().getWindow().handle(), this.getKeyBind());
   }

   public boolean Q() {
      if (this.isEnabled()) {
         if (this.w() && this.H()) {
            this.Y = (boolean)1;
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
      if (!this.w() || this.Y) {
         super.enable();
      }
   }

   @Override
   public void disable() {
      this.Y = (boolean)0;
      super.disable();
   }

   @Override
   public void suspendForDisconnect() {
      if (this.w()) {
         this.disable();
      } else {
         super.suspendForDisconnect();
      }
   }

   public static void o() {
      FreecamModule var0 = S;
      if (var0 != null && var0.w() && var0.isEnabled()) {
         Minecraft var1 = getMc();
         if (var1 == null
            || var1.player == null
            || var1.level == null
            || !var1.player.isAlive()
            || var1.gui.screen() != null
            || var1.gui.overlay() != null
            || !var1.isWindowActive()
            || var0.C != null && (var0.C != var1.player || var0.l != var1.level)
            || !var0.H()) {
            var0.setEnabled(false);
         }
      }
   }

   @Override
   public void setKeyBind(int var1) {
      if (this.w() && var1 != this.getKeyBind()) {
         this.setEnabled(false);
      }

      super.setKeyBind(var1);
   }

   @Override
   public void onEnable() {
      Minecraft var1 = getMc();
      this.C = var1.player;
      this.l = var1.level;
      tq.i(tq.y.F);
      this.E.b(hs.b(z(), this.C.input.keyPresses), this.C.isShiftKeyDown(), this.y.P());
      this.K(true);
      this.g();
      this.n();
      mt.K(
         "Freecam",
         "Detached camera acquired; activation={}, speed={}, smooth={}, carry={}, body keys={}",
         this.r.P(),
         this.h.P(),
         this.t.P(),
         this.y.P(),
         this.p.P()
      );
      mt.U("Freecam", "Terrain visibility starts from loaded columns in view, including across unloaded camera chunks");
   }

   @Override
   public void onDisable() {
      Minecraft var1 = getMc();

      try {
         if (this.l != null && this.C != null) {
            ChunkPos var2 = this.C.chunkPosition();
            this.l.getChunkSource().updateViewCenter(var2.x(), var2.z());
            this.l.getChunkSource().updateViewRadius(this.O());
         }

         if (this.C == var1.player && this.l == var1.level && this.C != null && this.C.isAlive()) {
            if (this.D && this.C.getAbilities().mayfly && !this.C.getAbilities().flying) {
               this.C.getAbilities().flying = this.b;
            }

            this.C.input.keyPresses = Input.EMPTY;
         }
      } finally {
         this.Y = (boolean)0;
         this.D = (boolean)0;
         this.C = null;
         this.l = null;
         this.Z = null;
         this.E.M();
         tq.A(tq.y.F);
         if (var1.level != null) {
            var1.levelRenderer.sectionOcclusionGraph().invalidate();
         }

         mt.K("Freecam", "Released camera, body input and chunk view");
      }
   }

   @Override
   public void onTick() {
      o();
      if (this.isRuntimeActive()) {
         tq.i(tq.y.F);
         this.K(false);
         this.g();
         tq.C(this.E.u(this.h.P(), this.A.P()), this.s.P(), this.t.P());
         this.n();
      }
   }

   public static boolean s() {
      return S != null && S.isEnabled() ? tq.f() : false;
   }

   public static boolean p() {
      return S != null && S.isEnabled() ? S != null && S.o.P() : false;
   }

   public static boolean L() {
      return S != null && S.isEnabled() ? S != null && S.i.P() : false;
   }

   public static Input P(Input var0) {
      if (S != null && S.isEnabled()) {
         FreecamModule var1 = S;
         Minecraft var2 = Minecraft.getInstance();
         return var1 != null && var2.gui.screen() == null && var2.isWindowActive() ? var1.E.K(var0, var1.p.P()) : Input.EMPTY;
      } else {
         return var0;
      }
   }

   public static Input z() {
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

   private void K(boolean var1) {
      if (this.C != null) {
         this.E
            .O(
               hs.b(z(), this.C.input.keyPresses),
               this.y.P(),
               this.p.P(),
               this.C.isShiftKeyDown(),
               new Vec2(this.C.xxa, this.C.zza),
               this.C.getDeltaMovement(),
               this.C.getYRot(),
               var1
            );
      }
   }

   private void g() {
      if (this.C != null && this.C.getAbilities().mayfly) {
         if (!this.D) {
            this.b = this.C.getAbilities().flying;
            this.D = (boolean)1;
         }

         this.C.getAbilities().flying = (boolean)0;
      }
   }

   public static void N() {
      if (S != null) {
         S.D = (boolean)0;
      }
   }

   private void n() {
      if (this.l != null && this.C != null) {
         ChunkPos var1 = this.C.chunkPosition();
         hs.n var2 = hs.B(tq.N(1.0F), var1.x(), var1.z(), this.P.P().intValue(), this.O());
         ClientChunkCache var3 = this.l.getChunkSource();
         var3.updateViewCenter(var2.x(), var2.z());
         var3.updateViewRadius(var2.radius());
         if (!var2.equals(this.Z)) {
            getMc().levelRenderer.sectionOcclusionGraph().invalidate();
            this.Z = var2;
         }
      }
   }

   private int O() {
      return this.C == null ? getMc().options.getEffectiveRenderDistance() : ((q1ff3fe7b68a0197e)this.C.connection).larpServerChunkRadius();
   }

   public static boolean w(double var0) {
      if (S != null && S.isEnabled()) {
         FreecamModule var2 = S;
         Minecraft var3 = Minecraft.getInstance();
         if (var2 == null || !tq.f() || !var2.x.P() || var3.gui.screen() != null || var3.gui.overlay() != null || !var3.isWindowActive()) {
            return false;
         }

         if (!var2.E.v(var0, var2.h.P(), var2.A.P())) {
            return false;
         }

         if (!var2.A.P()) {
            var2.h.o(var2.h.P() * (var0 > 0.0 ? 1.2 : 0.8333333333333334));
         }

         mt.U("Freecam", "Wheel camera speed={}", var2.E.u(var2.h.P(), var2.A.P()));
         return true;
      } else {
         return false;
      }
   }
}
