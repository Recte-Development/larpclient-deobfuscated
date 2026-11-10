package com.larpclient;

import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

public final class g7 {
   private static final jf t = new jf();

   public static void O(final jf.k var0, int var1, final Predicate<ItemStack> var2) {
      final Minecraft var3 = Minecraft.getInstance();
      final LocalPlayer var4 = var3.player;
      if (var4 == null) {
         t.i(var0);
      } else {
         t.d(
            var0,
            new jf.z() {
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
                  return this.current() && !var4.isSpectator() && !p_.V();
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
                  return g7.E() == null && c5.J(var3.options.keyUse);
               }

               @Override
               public void holdUse(boolean var1) {
                  var3.options.keyUse.setDown(var1);
               }

               @Override
               public void releaseUse() {
                  int var1x = var3.player != null && this.manualUse() ? 1 : 0;
                  var3.options.keyUse.setDown((boolean)var1x);
                  if (var3.player == var4 && var3.gameMode != null && var4.isUsingItem() && var1x == 0) {
                     var3.gameMode.releaseUsingItem(var4);
                  }
               }

               @Override
               public boolean startUnderScreen() {
                  Screen var1x = g7.E();
                  return var1x != null && var3.gameMode != null && !var3.gameMode.isDestroying() && !var4.isHandsBusy() && var4.connection.hasClientLoaded()
                     ? ta.g(() -> {
                        InteractionResult var4x = var3.gameMode.useItem(var4, InteractionHand.MAIN_HAND);
                        h.A("Eating", "Started {} from slot {} with {} open: {}", var0, var4.getInventory().selected, var1.getClass().getSimpleName(), var4x);
                     })
                     : false;
               }
            },
            var1
         );
      }
   }

   private static Screen E() {
      return Minecraft.getInstance().gui.screen();
   }

   public static void R(jf.k var0) {
      t.i(var0);
   }

   public static void P() {
      t.h();
   }

   private g7() {
   }
}
