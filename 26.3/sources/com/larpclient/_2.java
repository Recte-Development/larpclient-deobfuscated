package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import java.util.Set;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public abstract class _2 extends Module {
   private String Q = "Waiting";
   private Object M;
   private long n;
   private int z;
   private int h;

   protected _2(String var1, String var2) {
      super(var1, var2, Category.PLAYER, -1);
   }

   protected final com.larpclient.module.m C(String var1, String var2, boolean var3) {
      return this.addSetting(new com.larpclient.module.m(var1, var2, var3));
   }

   protected final com.larpclient.module.b g(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new com.larpclient.module.b(var1, var2, var3, var5, var7, var9));
   }

   public final boolean Z() {
      return !this.isEnabled() ? false : this.B() == null && _p.D(this);
   }

   private boolean r() {
      long var1 = mc.level.getGameTime();
      if (this.M != mc.player) {
         this.M = mc.player;
         this.n = var1;
         this.z = mc.player.tickCount;
         this.h = 0;
      } else if (var1 != this.n) {
         this.h = mc.player.tickCount == this.z ? this.h + 1 : 0;
         this.n = var1;
         this.z = mc.player.tickCount;
      }

      return this.h < 2;
   }

   protected final String B() {
      getMc();
      if (!this.isRuntimeActive()) {
         return "Waiting to start";
      } else if (mc.player == null || mc.level == null || mc.gameMode == null) {
         return "Waiting for the world";
      } else if (!mc.player.isAlive()) {
         return "Waiting to respawn";
      } else if (mc.player.isSpectator()) {
         return "Paused in spectator mode";
      } else {
         Screen var1 = mc.gui.screen();
         if (!_p.h(var1 != null, this)) {
            return "Paused while a screen is open";
         } else if (ij.y()) {
            return "Paused while Freecam is on";
         } else if (mc.gui.overlay() != null) {
            return "Waiting for the loading screen";
         } else if (!mc.player.connection.hasClientLoaded() || !this.r()) {
            return "Waiting for the world to load";
         } else if (!this.allowsScreenInteractions() && !mc.isWindowActive()) {
            return "Paused while the game is unfocused";
         } else if (mc.player.isUsingItem()) {
            return "Paused while an item is in use";
         } else {
            return mc.isWindowActive() && var1 == null && mc.options.keyAttack.isDown() ? "Paused while you hold attack" : null;
         }
      }
   }

   protected boolean holdsToolBetweenBlocks() {
      return false;
   }

   public boolean aimsAtOpenFaces(BlockPos var1) {
      return false;
   }

   public boolean aimFaceAllowed(BlockPos var1, Direction var2) {
      return true;
   }

   public boolean aimsAtFaceCentre(BlockPos var1) {
      return false;
   }

   public void digFinished(BlockPos var1) {
   }

   protected void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         float[] var3 = b6.R(mc.player.getEyePosition(), var1);
         double var4 = Math.max(Math.abs(Mth.wrapDegrees(var3[0] - mc.player.getYRot())), Math.abs(var3[1] - mc.player.getXRot()));
         b6.P(this, b6.w.F, var3[0], var3[1], var4 <= var2 ? 1.0 : var2 / var4);
      }
   }

   protected final void P(String var1) {
      if (!var1.equals(this.Q)) {
         this.Q = var1;
         _q.c(this.getName(), "State: {}", var1);
      }
   }

   @Override
   public String getStatus() {
      return this.Q;
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<com.larpclient.module.l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.k);
   }

   @Override
   public void onEnable() {
      getMc();
      this.P("Waiting");
      _q.z(
         this.getName(),
         this.allowsScreenInteractions()
            ? "World automation enabled; mining continues in screens and while unfocused; manual movement, mining, item use and freecam yield control"
            : "World automation enabled; manual mining, item use, screens and focus loss pause actions"
      );
   }

   @Override
   public void onDisable() {
      _p.e(this);
      vs.B(this);
      this.P("Waiting");
   }
}
