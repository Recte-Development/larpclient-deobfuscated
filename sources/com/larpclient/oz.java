package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class oz {
   private static final oz t = new oz();
   public boolean r;

   private oz() {
   }

   public static oz R() {
      return t;
   }

   public void F(int var1) {
      z6.u().queueForce(var1);
   }

   public void U(int... var1) {
      z6.u().queueForceSequence(var1);
   }

   public void t() {
      z6.u().clearQueue();
   }

   public boolean I() {
      return z6.u().getPendingValue() != -1;
   }

   public int l() {
      return z6.u().getPendingValue();
   }

   public String d() {
      return z6.u().pendingLabel();
   }

   public void k(Minecraft var1) {
      if (!this.r) {
         z6.u().clearQueue();
         if (z6.u().isSwapActive()) {
            z6.u().tick(var1);
         }
      } else {
         z6.u().tick(var1);
      }
   }
}
