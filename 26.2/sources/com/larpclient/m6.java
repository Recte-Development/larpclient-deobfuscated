package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class m6 {
   private static final m6 x = new m6();
   public boolean K;

   private m6() {
   }

   public static m6 W() {
      return x;
   }

   public void s(int var1) {
      wk.m().queueForce(var1);
   }

   public void j(int... var1) {
      wk.m().queueForceSequence(var1);
   }

   public void R() {
      wk.m().clearQueue();
   }

   public boolean I() {
      return wk.m().getPendingValue() != -1;
   }

   public int E() {
      return wk.m().getPendingValue();
   }

   public String E() {
      return wk.m().pendingLabel();
   }

   public void h(Minecraft var1) {
      if (!this.K) {
         wk.m().clearQueue();
         if (wk.m().isSwapActive()) {
            wk.m().tick(var1);
         }
      } else {
         wk.m().tick(var1);
      }
   }
}
