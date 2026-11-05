package com.larpclient;

import com.larpclient.module.AutoTotemModule;
import com.larpclient.module.LungeSwapModule;
import com.larpclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class o_ {
   private static o_ d;
   private final Module l;
   private final jj K = new jj();
   private LocalPlayer H;
   private Object g;

   public o_(Module var1) {
      this.l = var1;
   }

   public boolean K() {
      return this.K.p();
   }

   public boolean k() {
      return this.K() && this.y() && d == this && this.K.T(this.H.getInventory().getSelectedSlot());
   }

   public static boolean N(Module var0) {
      if (d != null && !d.y()) {
         d.U();
      }

      if ((d == null || d.l == var0) && !AutoTotemModule.j()) {
         LarpClient var1 = LarpClient.y();
         LungeSwapModule var2 = var1 != null && var1.e() != null ? var1.e().y(LungeSwapModule.class) : null;
         return var2 == null || var2 == var0 || !var2.U();
      } else {
         return false;
      }
   }

   public boolean s(LocalPlayer var1, int var2) {
      if (var2 >= 0 && var2 < 9 && var1 != null && N(this.l)) {
         Minecraft var3 = Minecraft.getInstance();
         if (var1 == var3.player && var1.level() == var3.level) {
            if (this.K() && !this.y()) {
               this.U();
            }

            int var4 = var1.getInventory().getSelectedSlot();
            if (var4 == var2 && !this.K()) {
               return true;
            }

            this.K.V(var4, var2);
            this.H = var1;
            this.g = var3.level;
            d = this;
            var1.getInventory().setSelectedSlot(var2);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void C(Minecraft var1, boolean var2) {
      if (this.K()) {
         if (!this.y()) {
            this.U();
         } else {
            if (AutoTotemModule.j()) {
               if (d == this) {
                  d = null;
               }
            } else if (var1.gui.screen() == null && !j5.n(this.H)) {
               this.m(var1, var2);
            }
         }
      }
   }

   public void m(Minecraft var1, boolean var2) {
      boolean var3 = this.y();
      int var4 = this.K.d(var3 ? this.H.getInventory().getSelectedSlot() : -1, var2 && var3);
      if (var4 >= 0) {
         this.H.getInventory().setSelectedSlot(var4);
      }

      this.U();
   }

   private boolean y() {
      Minecraft var1 = Minecraft.getInstance();
      return this.H != null && this.H == var1.player && this.g == var1.level && this.H.isAlive();
   }

   private void U() {
      if (d == this) {
         d = null;
      }

      this.K.H();
      this.H = null;
      this.g = null;
   }
}
