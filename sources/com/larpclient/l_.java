package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class l_ {
   private static final l_ F = new l_();
   public boolean j;

   private l_() {
   }

   public static l_ F() {
      return F;
   }

   public void E(int var1) {
      uu.M().queueForce(var1);
   }

   public void q(int... var1) {
      uu.M().queueForceSequence(var1);
   }

   public void M() {
      uu.M().clearQueue();
   }

   public boolean H() {
      return uu.M().getPendingValue() != -1;
   }

   public int H() {
      return uu.M().getPendingValue();
   }

   public String z() {
      return uu.M().pendingLabel();
   }

   public void p(Minecraft var1) {
      if (!this.j) {
         uu.M().clearQueue();
      } else {
         uu.M().tick(var1);
      }
   }
}
