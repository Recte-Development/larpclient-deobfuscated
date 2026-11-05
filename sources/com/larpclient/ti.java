package com.larpclient;

import com.larpclient.module.AutoTotemModule;
import com.larpclient.module.LungeSwapModule;
import com.larpclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class ti {
   private static ti Q;
   private final Module b;
   private final eh U = new eh();
   private LocalPlayer z;
   private Object w;

   public ti(Module var1) {
      this.b = var1;
   }

   public boolean k() {
      return this.U.c();
   }

   public boolean L() {
      return this.k() && this.Z() && Q == this && this.U.c(this.z.getInventory().getSelectedSlot());
   }

   public static boolean V(Module var0) {
      if (Q != null && !Q.Z()) {
         Q.z();
      }

      if ((Q == null || Q.b == var0) && !AutoTotemModule.A()) {
         LarpClient var1 = LarpClient.F();
         LungeSwapModule var2 = var1 != null && var1.x() != null ? var1.x().V(LungeSwapModule.class) : null;
         return var2 == null || var2 == var0 || !var2.U();
      } else {
         return false;
      }
   }

   public boolean e(LocalPlayer var1, int var2) {
      if (var2 >= 0 && var2 < 9 && var1 != null && V(this.b)) {
         Minecraft var3 = Minecraft.getInstance();
         if (var1 == var3.player && var1.level() == var3.level) {
            if (this.k() && !this.Z()) {
               this.z();
            }

            int var4 = var1.getInventory().getSelectedSlot();
            if (var4 == var2 && !this.k()) {
               return true;
            }

            this.U.q(var4, var2);
            this.z = var1;
            this.w = var3.level;
            Q = this;
            var1.getInventory().setSelectedSlot(var2);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void r(Minecraft var1, boolean var2) {
      if (this.k()) {
         if (!this.Z()) {
            this.z();
         } else {
            if (AutoTotemModule.A()) {
               if (Q == this) {
                  Q = null;
               }
            } else if (var1.gui.screen() == null && !w7.H(this.z)) {
               this.W(var1, var2);
            }
         }
      }
   }

   public void W(Minecraft var1, boolean var2) {
      boolean var3 = this.Z();
      int var4 = this.U.L(var3 ? this.z.getInventory().getSelectedSlot() : -1, var2 && var3);
      if (var4 >= 0) {
         this.z.getInventory().setSelectedSlot(var4);
      }

      this.z();
   }

   private boolean Z() {
      Minecraft var1 = Minecraft.getInstance();
      return this.z != null && this.z == var1.player && this.w == var1.level && this.z.isAlive();
   }

   private void z() {
      if (Q == this) {
         Q = null;
      }

      this.U.C();
      this.z = null;
      this.w = null;
   }
}
