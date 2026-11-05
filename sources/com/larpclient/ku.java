package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class ku {
   private static final ku W = new ku();
   public boolean f;

   private ku() {
   }

   public static ku y() {
      return W;
   }

   public void D(int var1) {
      zw.K().queueForce(var1);
   }

   public void x(int... var1) {
      zw.K().queueForceSequence(var1);
   }

   public void c() {
      zw.K().clearQueue();
   }

   public boolean c() {
      return zw.K().getPendingValue() != -1;
   }

   public int M() {
      return zw.K().getPendingValue();
   }

   public String b() {
      return zw.K().pendingLabel();
   }

   public void d(Minecraft var1) {
      if (!this.f) {
         zw.K().clearQueue();
      } else {
         zw.K().tick(var1);
      }
   }
}
