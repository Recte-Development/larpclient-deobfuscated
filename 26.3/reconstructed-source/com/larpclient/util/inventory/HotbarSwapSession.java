package com.larpclient.util.inventory;

import com.larpclient.LarpClient;
import com.larpclient.module.combat.AutoTotemModule;
import com.larpclient.module.combat.LungeSwapModule;
import com.larpclient.module.misc.Module;
import com.larpclient.util.entity.CombatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class HotbarSwapSession {
   private static HotbarSwapSession activeSession;
   private final Module module;
   private final HotbarSwapTracker slotState = new HotbarSwapTracker();
   private LocalPlayer player;
   private Object level;

   public HotbarSwapSession(Module var1) {
      this.module = var1;
   }

   public boolean isSwapped() {
      return this.slotState.isSwapped();
   }

   public boolean isActive() {
      return this.isSwapped() && this.isContextValid() && activeSession == this && this.slotState.isSwappedSlot(this.player.getInventory().getSelectedSlot());
   }

   public static boolean canSwap(Module var0) {
      if (activeSession != null && !activeSession.isContextValid()) {
         activeSession.clear();
      }

      if ((activeSession == null || activeSession.module == var0) && !AutoTotemModule.needsHotbarSwap()) {
         LarpClient var1 = LarpClient.getInstance();
         LungeSwapModule var2 = var1 != null && var1.getModuleManager() != null ? var1.getModuleManager().getModule(LungeSwapModule.class) : null;
         return var2 == null || var2 == var0 || !var2.resolveSwapBackSlot();
      } else {
         return false;
      }
   }

   public boolean swapTo(LocalPlayer var1, int var2) {
      if (var2 >= 0 && var2 < 9 && var1 != null && canSwap(this.module)) {
         Minecraft var3 = Minecraft.getInstance();
         if (var1 == var3.player && var1.level() == var3.level) {
            if (this.isSwapped() && !this.isContextValid()) {
               this.clear();
            }

            int var4 = var1.getInventory().getSelectedSlot();
            if (var4 == var2 && !this.isSwapped()) {
               return true;
            }

            this.slotState.recordSwap(var4, var2);
            this.player = var1;
            this.level = var3.level;
            activeSession = this;
            var1.getInventory().setSelectedSlot(var2);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void tick(Minecraft var1, boolean var2) {
      if (this.isSwapped()) {
         if (!this.isContextValid()) {
            this.clear();
         } else {
            if (AutoTotemModule.needsHotbarSwap()) {
               if (activeSession == this) {
                  activeSession = null;
               }
            } else if (var1.gui.screen() == null && !CombatUtil.hasTickedThisTick(this.player)) {
               this.restore(var1, var2);
            }
         }
      }
   }

   public void restore(Minecraft var1, boolean var2) {
      boolean var3 = this.isContextValid();
      int var4 = this.slotState.consumeRestoreSlot(var3 ? this.player.getInventory().getSelectedSlot() : -1, var2 && var3);
      if (var4 >= 0) {
         this.player.getInventory().setSelectedSlot(var4);
      }

      this.clear();
   }

   private boolean isContextValid() {
      Minecraft var1 = Minecraft.getInstance();
      return this.player != null && this.player == var1.player && this.level == var1.level && this.player.isAlive();
   }

   private void clear() {
      if (activeSession == this) {
         activeSession = null;
      }

      this.slotState.reset();
      this.player = null;
      this.level = null;
   }
}
