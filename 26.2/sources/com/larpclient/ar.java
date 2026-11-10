package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import java.util.Set;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public abstract class ar extends Module {
   private String v = "Waiting";
   private Object K;
   private long l;
   private int k;
   private int N;

   protected ar(String var1, String var2) {
      super(var1, var2, Category.PLAYER, -1);
   }

   protected final com.larpclient.module.s U(String var1, String var2, boolean var3) {
      return this.addSetting(new com.larpclient.module.s(var1, var2, var3));
   }

   protected final com.larpclient.module.e Z(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new com.larpclient.module.e(var1, var2, var3, var5, var7, var9));
   }

   public final boolean I() {
      return !this.isEnabled() ? false : this.e() == null && ta.S(this);
   }

   private boolean x() {
      long var1 = mc.level.getGameTime();
      if (this.K != mc.player) {
         this.K = mc.player;
         this.l = var1;
         this.k = mc.player.tickCount;
         this.N = 0;
      } else if (var1 != this.l) {
         this.N = mc.player.tickCount == this.k ? this.N + 1 : 0;
         this.l = var1;
         this.k = mc.player.tickCount;
      }

      return this.N < 2;
   }

   protected final String e() {
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
         if (!ta.J(var1 != null, this)) {
            return "Paused while a screen is open";
         } else if (p_.V()) {
            return "Paused while Freecam is on";
         } else if (mc.gui.overlay() != null) {
            return "Waiting for the loading screen";
         } else if (!mc.player.connection.hasClientLoaded() || !this.x()) {
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
         float[] var3 = j6.s(mc.player.getEyePosition(), var1);
         double var4 = Math.max(Math.abs(Mth.wrapDegrees(var3[0] - mc.player.getYRot())), Math.abs(var3[1] - mc.player.getXRot()));
         j6.U(this, j6.l.p, var3[0], var3[1], var4 <= var2 ? 1.0 : var2 / var4);
      }
   }

   protected final void e(String var1) {
      if (!var1.equals(this.v)) {
         this.v = var1;
         h.U(this.getName(), "State: {}", var1);
      }
   }

   @Override
   public String getStatus() {
      return this.v;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<com.larpclient.module.m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.U);
   }

   @Override
   public void onEnable() {
      getMc();
      this.e("Waiting");
      h.A(
         this.getName(),
         this.allowsScreenInteractions()
            ? "World automation enabled; mining continues in screens and while unfocused; manual movement, mining, item use and freecam yield control"
            : "World automation enabled; manual mining, item use, screens and focus loss pause actions"
      );
   }

   @Override
   public void onDisable() {
      ta.s(this);
      l0.h(this);
      this.e("Waiting");
   }
}
