package com.larpclient.module.world;

import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.BlockBreaker;
import java.util.Set;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public abstract class WorldAutomationModule extends Module {
   private String status = "Waiting";
   private Object lastPlayer;
   private long lastGameTime;
   private int lastTickCount;
   private int stalledTicks;

   protected WorldAutomationModule(String var1, String var2) {
      super(var1, var2, Category.PLAYER, -1);
   }

   protected final BooleanSetting addToggleSetting(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting(var1, var2, var3));
   }

   protected final NumberSetting addSliderSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   public final boolean isReady() {
      return !this.isEnabled() ? false : this.getPauseReason() == null && InteractionArbiter.canModuleInteract(this);
   }

   private boolean isTicking() {
      long var1 = minecraft.level.getGameTime();
      if (this.lastPlayer != minecraft.player) {
         this.lastPlayer = minecraft.player;
         this.lastGameTime = var1;
         this.lastTickCount = minecraft.player.tickCount;
         this.stalledTicks = 0;
      } else if (var1 != this.lastGameTime) {
         this.stalledTicks = minecraft.player.tickCount == this.lastTickCount ? this.stalledTicks + 1 : 0;
         this.lastGameTime = var1;
         this.lastTickCount = minecraft.player.tickCount;
      }

      return this.stalledTicks < 2;
   }

   protected final String getPauseReason() {
      getMc();
      if (!this.isRuntimeActive()) {
         return "Waiting to start";
      } else if (minecraft.player == null || minecraft.level == null || minecraft.gameMode == null) {
         return "Waiting for the world";
      } else if (!minecraft.player.isAlive()) {
         return "Waiting to respawn";
      } else if (minecraft.player.isSpectator()) {
         return "Paused in spectator mode";
      } else {
         Screen var1 = minecraft.gui.screen();
         if (!InteractionArbiter.allowsScreenInteraction(var1 != null, this)) {
            return "Paused while a screen is open";
         } else if (ViewController.isFreecam()) {
            return "Paused while Freecam is on";
         } else if (minecraft.gui.overlay() != null) {
            return "Waiting for the loading screen";
         } else if (!minecraft.player.connection.hasClientLoaded() || !this.isTicking()) {
            return "Waiting for the world to load";
         } else if (!this.allowsScreenInteractions() && !minecraft.isWindowActive()) {
            return "Paused while the game is unfocused";
         } else if (minecraft.player.isUsingItem()) {
            return "Paused while an item is in use";
         } else {
            return minecraft.isWindowActive() && var1 == null && minecraft.options.keyAttack.isDown() ? "Paused while you hold attack" : null;
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
         float[] var3 = RotationManager.calculateAngles(minecraft.player.getEyePosition(), var1);
         double var4 = Math.max(Math.abs(Mth.wrapDegrees(var3[0] - minecraft.player.getYRot())), Math.abs(var3[1] - minecraft.player.getXRot()));
         RotationManager.requestForModule(this, RotationManager.Priority.DEFENSE, var3[0], var3[1], var4 <= var2 ? 1.0 : var2 / var4);
      }
   }

   protected final void setStatus(String var1) {
      if (!var1.equals(this.status)) {
         this.status = var1;
         LarpLog.debug(this.getName(), "State: {}", var1);
      }
   }

   @Override
   public String getStatus() {
      return this.status;
   }

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.MAIN;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public void onEnable() {
      getMc();
      this.setStatus("Waiting");
      LarpLog.info(
         this.getName(),
         this.allowsScreenInteractions()
            ? "World automation enabled; mining continues in screens and while unfocused; manual movement, mining, item use and freecam yield control"
            : "World automation enabled; manual mining, item use, screens and focus loss pause actions"
      );
   }

   @Override
   public void onDisable() {
      InteractionArbiter.findAttackBlocker(this);
      BlockBreaker.release(this);
      this.setStatus("Waiting");
   }
}
