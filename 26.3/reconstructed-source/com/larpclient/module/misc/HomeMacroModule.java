package com.larpclient.module.misc;

import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.misc.ClickStepTimer;
import com.larpclient.util.misc.LarpLog;
import java.util.List;
import java.util.Set;
import net.minecraft.network.chat.Component;

public final class HomeMacroModule extends Module {
   private final NumberSetting homeSlot = this.addSetting(new NumberSetting("Home Slot", "Numbered home to delete and set again", 1.0, 1.0, 5.0, 1.0));
   private final BooleanSetting chatFeedback = this.addSetting(new BooleanSetting("Chat Feedback", "Confirm when the replacement command is sent", true));
   private Object startPlayer;
   private Object startLevel;
   private Object startConnection;
   private final ClickStepTimer.HomeReplaceTimer replaceTimer = new ClickStepTimer.HomeReplaceTimer();
   private int activeHome;

   public HomeMacroModule() {
      super("Home Macro", "Delete a numbered home, then set it again after one second", Category.PLAYER, -1);
   }

   @Override
   public String getDisplayName() {
      return "Home Macro";
   }

   @Override
   public List<Setting<?>> getSettings() {
      return List.of(this.homeSlot, this.chatFeedback);
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public String getStatus() {
      return this.replaceTimer.isArmed() ? "Replacing Home " + this.activeHome : "";
   }

   @Override
   public void onEnable() {
      this.onDisable();
      if (this.isInGame() && minecraft.player.connection != null) {
         this.startPlayer = minecraft.player;
         this.startLevel = minecraft.level;
         this.startConnection = minecraft.player.connection;
         this.activeHome = this.homeSlot.getValue().intValue();
         this.replaceTimer.arm(this.activeHome, System.nanoTime());
         minecraft.player.connection.sendCommand("delhome " + this.activeHome);
         LarpLog.debug("HomeMacro", "Delete sent for home {}; replacement waits one second", this.activeHome);
      } else {
         this.disable();
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.replaceTimer.isArmed();
      boolean var3 = false;
      byte var2 = (byte)var10000;
      var10000 = Integer.signum(var2);
      if ((var10000 | -var10000) >>> 31 != 0) {
         if (this.isInGame()
            && minecraft.player == this.startPlayer
            && minecraft.level == this.startLevel
            && minecraft.player.connection == this.startConnection) {
            int var11 = minecraft.player.isAlive();
            boolean var9 = false;
            byte var8 = (byte)var11;
            var11 = Integer.signum(var8);
            if ((var11 | -var11) >>> 31 != 0) {
               int var1 = this.replaceTimer.pollDue(System.nanoTime());
               boolean var7 = false;
               int var6 = var1;
               var11 = Integer.signum(var6);
               if ((var11 | -var11) >>> 31 == 0) {
                  return;
               }

               minecraft.player.connection.sendCommand("sethome " + this.activeHome);
               int var14 = this.chatFeedback.getValue();
               boolean var5 = false;
               byte var4 = (byte)var14;
               var14 = Integer.signum(var4);
               if (((var14 | -var14) >>> 31 ^ 1) == 0) {
                  minecraft.gui.chatListener().handleSystemMessage(Component.literal("[LARP] Home " + this.activeHome + " replacement command sent."), false);
               }

               LarpLog.debug("HomeMacro", "Replacement command sent for home {}", this.activeHome);
               this.disable();
               return;
            }
         }

         LarpLog.debug("HomeMacro", "Replacement cancelled because the active session changed");
         this.disable();
      }
   }

   @Override
   public void onDisable() {
      this.replaceTimer.clear();
      this.activeHome = 0;
      this.startPlayer = this.startLevel = this.startConnection = null;
   }
}
