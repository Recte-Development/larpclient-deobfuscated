package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class _u {
   private static final _u F = new _u();
   public boolean Q;

   private _u() {
   }

   public static _u b() {
      return F;
   }

   public void P(int var1) {
      vf.x().queueForce(var1);
   }

   public void A(int... var1) {
      vf.x().queueForceSequence(var1);
   }

   public void M() {
      vf.x().clearQueue();
   }

   public boolean K() {
      return vf.x().getPendingValue() != -1;
   }

   public int u() {
      return vf.x().getPendingValue();
   }

   public String t() {
      return vf.x().pendingLabel();
   }

   public void F(Minecraft var1) {
      if (!this.Q) {
         vf.x().clearQueue();
         if (vf.x().isSwapActive()) {
            vf.x().tick(var1);
         }
      } else {
         vf.x().tick(var1);
      }
   }
}
