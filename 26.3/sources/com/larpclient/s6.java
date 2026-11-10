package com.larpclient;

import com.larpclient.module.AutoTotemModule;
import com.larpclient.module.LungeSwapModule;
import com.larpclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class s6 {
   private static s6 L;
   private final Module X;
   private final s C = new s();
   private LocalPlayer J;
   private Object u;

   public s6(Module var1) {
      this.X = var1;
   }

   public boolean j() {
      return this.C.R();
   }

   public boolean l() {
      return this.j() && this.n() && L == this && this.C.y(this.J.getInventory().getSelectedSlot());
   }

   public static boolean L(Module var0) {
      if (L != null && !L.n()) {
         L.F();
      }

      if ((L == null || L.X == var0) && !AutoTotemModule.U()) {
         LarpClient var1 = LarpClient.N();
         LungeSwapModule var2 = var1 != null && var1.j() != null ? var1.j().y(LungeSwapModule.class) : null;
         return var2 == null || var2 == var0 || !var2.c();
      } else {
         return false;
      }
   }

   public boolean h(LocalPlayer var1, int var2) {
      if (var2 >= 0 && var2 < 9 && var1 != null && L(this.X)) {
         Minecraft var3 = Minecraft.getInstance();
         if (var1 == var3.player && var1.level() == var3.level) {
            if (this.j() && !this.n()) {
               this.F();
            }

            int var4 = var1.getInventory().getSelectedSlot();
            if (var4 == var2 && !this.j()) {
               return true;
            }

            this.C.s(var4, var2);
            this.J = var1;
            this.u = var3.level;
            L = this;
            var1.getInventory().setSelectedSlot(var2);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void G(Minecraft var1, boolean var2) {
      if (this.j()) {
         if (!this.n()) {
            this.F();
         } else {
            if (AutoTotemModule.U()) {
               if (L == this) {
                  L = null;
               }
            } else if (var1.gui.screen() == null && !qg.Y(this.J)) {
               this.X(var1, var2);
            }
         }
      }
   }

   public void X(Minecraft var1, boolean var2) {
      boolean var3 = this.n();
      int var4 = this.C.h(var3 ? this.J.getInventory().getSelectedSlot() : -1, var2 && var3);
      if (var4 >= 0) {
         this.J.getInventory().setSelectedSlot(var4);
      }

      this.F();
   }

   private boolean n() {
      Minecraft var1 = Minecraft.getInstance();
      return this.J != null && this.J == var1.player && this.u == var1.level && this.J.isAlive();
   }

   private void F() {
      if (L == this) {
         L = null;
      }

      this.C.J();
      this.J = null;
      this.u = null;
   }
}
