package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;

@Environment(EnvType.CLIENT)
public class v {
   private static final v C = new v();
   public boolean V;

   private v() {
   }

   public static v D() {
      return C;
   }

   public void T(int var1) {
      qq.f().queueForce(var1);
   }

   public void G(int... var1) {
      qq.f().queueForceSequence(var1);
   }

   public void T() {
      qq.f().clearQueue();
   }

   public boolean G() {
      return qq.f().getPendingValue() != -1;
   }

   public int T() {
      return qq.f().getPendingValue();
   }

   public String Y() {
      return qq.f().pendingLabel();
   }

   public void A(class_310 var1) {
      if (!this.V) {
         qq.f().clearQueue();
         if (qq.f().isSwapActive()) {
            qq.f().tick(var1);
         }
      } else {
         qq.f().tick(var1);
      }
   }
}
