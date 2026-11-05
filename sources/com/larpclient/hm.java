package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class hm {
   private static final hm M = new hm();
   public boolean T;

   private hm() {
   }

   public static hm O() {
      return M;
   }

   public void k(int var1) {
      u3.H().queueForce(var1);
   }

   public void d(int... var1) {
      u3.H().queueForceSequence(var1);
   }

   public void P() {
      u3.H().clearQueue();
   }

   public boolean S() {
      return u3.H().getPendingValue() != -1;
   }

   public int A() {
      return u3.H().getPendingValue();
   }

   public String v() {
      return u3.H().pendingLabel();
   }

   public void Y(Minecraft var1) {
      if (!this.T) {
         u3.H().clearQueue();
         if (u3.H().isSwapActive()) {
            u3.H().tick(var1);
         }
      } else {
         u3.H().tick(var1);
      }
   }
}
