package com.larpclient;

import com.mojang.authlib.GameProfile;
import java.util.function.Supplier;
import net.minecraft.class_1068;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_7532;
import net.minecraft.class_8685;

public final class ht {
   private static final long E = 10000000000L;
   private Supplier<class_8685> S;
   private GameProfile p;
   private int Q = -1;
   private class_8685 w;
   private long N = -1L;

   public class_8685 z() {
      class_310 var1 = class_310.method_1551();
      int var2 = t_.g;
      GameProfile var3 = var1.method_53462();
      if (this.S == null || !var3.equals(this.p) || this.Q != var2) {
         if (this.p != null && !this.p.id().equals(var3.id())) {
            this.w = class_1068.method_52854(var3);
            this.N = System.nanoTime();
            q8.G("Accounts", "Game account changed; menu skin lookup restarted, native fallback shown until the texture loads");
         }

         this.p = var3;
         this.Q = var2;
         this.S = var1.method_1582().method_73544(this.p, false);
      }

      class_8685 var4 = this.S.get();
      if (this.N >= 0L) {
         this.M(var4);
      }

      return var4;
   }

   private void M(class_8685 var1) {
      long var2 = System.nanoTime() - this.N;
      if (!var1.equals(this.w)) {
         q8.G("Accounts", "Switched account skin ready after {} ms", var2 / 1000000L);
         this.N = -1L;
      } else if (var2 > 10000000000L) {
         q8.L(
            "Accounts",
            "Switched account still shows the default skin after {} s; it has no custom skin or the skin service is unavailable",
            var2 / 1000000000L
         );
         this.N = -1L;
      }
   }

   public void K(class_332 var1, int var2, int var3, int var4) {
      class_7532.method_44443(var1, this.z(), var2, var3, var4, fp.R(-1));
   }
}
