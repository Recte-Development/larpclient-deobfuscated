package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class c9 {
   private static final c9 z = new c9();
   public boolean j;

   private c9() {
   }

   public static c9 G() {
      return z;
   }

   public void y(int var1) {
      w0.l().queueForce(var1);
   }

   public void f(int... var1) {
      w0.l().queueForceSequence(var1);
   }

   public void a() {
      w0.l().clearQueue();
   }

   public boolean A() {
      return w0.l().getPendingValue() != -1;
   }

   public int m() {
      return w0.l().getPendingValue();
   }

   public String b() {
      return w0.l().pendingLabel();
   }

   public void G(Minecraft var1) {
      if (!this.j) {
         w0.l().clearQueue();
      } else {
         w0.l().tick(var1);
      }
   }
}
