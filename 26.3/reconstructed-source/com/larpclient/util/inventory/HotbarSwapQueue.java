package com.larpclient.util.inventory;

import com.larpclient.module.exploit.HotbarForceQueue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class HotbarSwapQueue {
   private static final HotbarSwapQueue iNSTANCE = new HotbarSwapQueue();
   public boolean queueEnabled;

   private HotbarSwapQueue() {
   }

   public static HotbarSwapQueue getInstance() {
      return iNSTANCE;
   }

   public void queueSlot(int var1) {
      HotbarForceQueue.getInstance().queueForce(var1);
   }

   public void queueSlots(int... var1) {
      HotbarForceQueue.getInstance().queueForceSequence(var1);
   }

   public void clearQueue() {
      HotbarForceQueue.getInstance().clearQueue();
   }

   public boolean hasPending() {
      return HotbarForceQueue.getInstance().getPendingValue() != -1;
   }

   public int getPendingSlot() {
      return HotbarForceQueue.getInstance().getPendingValue();
   }

   public String getPendingLabel() {
      return HotbarForceQueue.getInstance().pendingLabel();
   }

   public void tick(Minecraft var1) {
      if (!this.queueEnabled) {
         HotbarForceQueue.getInstance().clearQueue();
         if (HotbarForceQueue.getInstance().isSwapActive()) {
            HotbarForceQueue.getInstance().tick(var1);
         }
      } else {
         HotbarForceQueue.getInstance().tick(var1);
      }
   }
}
