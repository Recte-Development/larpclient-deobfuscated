package com.larpclient.util.data;

import com.larpclient.module.exploit.HotbarSwapQueue2;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class ForceQueue {
   private static final ForceQueue instance = new ForceQueue();
   public boolean enabled;

   private ForceQueue() {
   }

   public static ForceQueue getInstance() {
      return instance;
   }

   public void queueForce(int var1) {
      HotbarSwapQueue2.getInstance().queueForce(var1);
   }

   public void queueForce(int... var1) {
      HotbarSwapQueue2.getInstance().queueForceSequence(var1);
   }

   public void clearQueue() {
      HotbarSwapQueue2.getInstance().clearQueue();
   }

   public boolean hasPending() {
      return HotbarSwapQueue2.getInstance().getPendingValue() != -1;
   }

   public int tick() {
      return HotbarSwapQueue2.getInstance().getPendingValue();
   }

   public String getPendingLabel() {
      return HotbarSwapQueue2.getInstance().pendingLabel();
   }

   public void tick(Minecraft var1) {
      if (!this.enabled) {
         HotbarSwapQueue2.getInstance().clearQueue();
      } else {
         HotbarSwapQueue2.getInstance().tick(var1);
      }
   }
}
