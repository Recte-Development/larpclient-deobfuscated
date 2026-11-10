package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;

@Environment(EnvType.CLIENT)
public class s1 {
   private static final s1 V = new s1();
   public boolean e;

   private s1() {
   }

   public static s1 j() {
      return V;
   }

   public void K(int var1) {
      ql.Y().queueForce(var1);
   }

   public void A(int... var1) {
      ql.Y().queueForceSequence(var1);
   }

   public void a() {
      ql.Y().clearQueue();
   }

   public boolean p() {
      return ql.Y().getPendingValue() != -1;
   }

   public int c() {
      return ql.Y().getPendingValue();
   }

   public String r() {
      return ql.Y().pendingLabel();
   }

   public void F(class_310 var1) {
      if (!this.e) {
         ql.Y().clearQueue();
      } else {
         ql.Y().tick(var1);
      }
   }
}
