package com.larpclient;

import com.larpclient.module.AutoTotemModule;
import com.larpclient.module.LungeSwapModule;
import com.larpclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class hu {
   private static hu Y;
   private final Module f;
   private final uw l = new uw();
   private LocalPlayer i;
   private Object k;

   public hu(Module var1) {
      this.f = var1;
   }

   public boolean p() {
      return this.l.J();
   }

   public boolean U() {
      return this.p() && this.d() && Y == this && this.l.s(this.i.getInventory().getSelectedSlot());
   }

   public static boolean k(Module var0) {
      if (Y != null && !Y.d()) {
         Y.a();
      }

      if ((Y == null || Y.f == var0) && !AutoTotemModule.N()) {
         LarpClient var1 = LarpClient.R();
         LungeSwapModule var2 = var1 != null && var1.z() != null ? var1.z().p(LungeSwapModule.class) : null;
         return var2 == null || var2 == var0 || !var2.S();
      } else {
         return false;
      }
   }

   public boolean O(LocalPlayer var1, int var2) {
      if (var2 >= 0 && var2 < 9 && var1 != null && k(this.f)) {
         Minecraft var3 = Minecraft.getInstance();
         if (var1 == var3.player && var1.level() == var3.level) {
            if (this.p() && !this.d()) {
               this.a();
            }

            int var4 = var1.getInventory().getSelectedSlot();
            if (var4 == var2 && !this.p()) {
               return true;
            }

            this.l.h(var4, var2);
            this.i = var1;
            this.k = var3.level;
            Y = this;
            var1.getInventory().setSelectedSlot(var2);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void J(Minecraft var1, boolean var2) {
      if (this.p()) {
         if (!this.d()) {
            this.a();
         } else {
            if (AutoTotemModule.N()) {
               if (Y == this) {
                  Y = null;
               }
            } else if (var1.gui.screen() == null && !si.o(this.i)) {
               this.V(var1, var2);
            }
         }
      }
   }

   public void V(Minecraft var1, boolean var2) {
      boolean var3 = this.d();
      int var4 = this.l.n(var3 ? this.i.getInventory().getSelectedSlot() : -1, var2 && var3);
      if (var4 >= 0) {
         this.i.getInventory().setSelectedSlot(var4);
      }

      this.a();
   }

   private boolean d() {
      Minecraft var1 = Minecraft.getInstance();
      return this.i != null && this.i == var1.player && this.k == var1.level && this.i.isAlive();
   }

   private void a() {
      if (Y == this) {
         Y = null;
      }

      this.l.B();
      this.i = null;
      this.k = null;
   }
}
