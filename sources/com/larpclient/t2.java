package com.larpclient;

import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

public final class t2 {
   private static final eo s = new eo();

   public static void U(eo.r var0, int var1, final Predicate<ItemStack> var2) {
      final Minecraft var3 = Minecraft.getInstance();
      final LocalPlayer var4 = var3.player;
      if (var4 == null) {
         s.W(var0);
      } else {
         s.v(var0, new eo.o() {
            @Override
            public Object player() {
               return var4;
            }

            @Override
            public int tick() {
               return var4.tickCount;
            }

            @Override
            public boolean current() {
               return var3.player == var4 && var3.level == var4.level() && var4.isAlive();
            }

            @Override
            public boolean allowed() {
               return this.current() && !var4.isSpectator() && var3.gui.screen() == null && !tq.f();
            }

            @Override
            public int selected() {
               return var4.getInventory().selected;
            }

            @Override
            public void select(int var1) {
               var4.getInventory().selected = var1;
            }

            @Override
            public boolean usable(int var1) {
               ItemStack var2x = var4.getInventory().getItem(var1);
               return !var2x.isEmpty() && var2.test(var2x);
            }

            @Override
            public boolean usingItem() {
               return var4.isUsingItem();
            }

            @Override
            public boolean manualUse() {
               return te.e(var3.options.keyUse);
            }

            @Override
            public void holdUse(boolean var1) {
               var3.options.keyUse.setDown(var1);
            }

            @Override
            public void releaseUse() {
               int var1x = var3.player != null && var3.gui.screen() == null && this.manualUse() ? 1 : 0;
               var3.options.keyUse.setDown((boolean)var1x);
               if (var3.player == var4 && var3.gameMode != null && var4.isUsingItem() && var1x == 0) {
                  var3.gameMode.releaseUsingItem(var4);
               }
            }
         }, var1);
      }
   }

   public static void D(eo.r var0) {
      s.W(var0);
   }

   public static void G() {
      s.x();
   }

   private t2() {
   }
}
