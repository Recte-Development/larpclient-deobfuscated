package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class ql {
   private static final ql d = new ql();
   public boolean k;

   private ql() {
   }

   public static ql S() {
      return d;
   }

   public void l(int var1) {
      v2.L().queueForce(var1);
   }

   public void l(int... var1) {
      v2.L().queueForceSequence(var1);
   }

   public void J() {
      v2.L().clearQueue();
   }

   public boolean L() {
      return v2.L().getPendingValue() != -1;
   }

   public int P() {
      return v2.L().getPendingValue();
   }

   public String U() {
      return v2.L().pendingLabel();
   }

   public void P(Minecraft var1) {
      if (!this.k) {
         v2.L().clearQueue();
      } else {
         v2.L().tick(var1);
      }
   }
}
