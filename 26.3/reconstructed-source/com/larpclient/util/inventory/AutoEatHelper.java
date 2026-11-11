package com.larpclient.util.inventory;

import com.larpclient.core.ViewController;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

public final class AutoEatHelper {
   private static final ItemUseController controller = new ItemUseController();

   public static void start(final ItemUseController.Owner var0, int var1, final Predicate<ItemStack> var2) {
      final Minecraft var3 = Minecraft.getInstance();
      final LocalPlayer var4 = var3.player;
      if (var4 == null) {
         controller.release(var0);
      } else {
         controller.request(
            var0,
            new ItemUseController.UseContext() {
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
                  return this.current() && !var4.isSpectator() && !ViewController.isFreecam();
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
                  return AutoEatHelper.currentScreen() == null && PlayerInputUtil.isKeyDown(var3.options.keyUse);
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
                  Screen var1x = AutoEatHelper.currentScreen();
                  return var1x != null && var3.gameMode != null && !var3.gameMode.isDestroying() && !var4.isHandsBusy() && var4.connection.hasClientLoaded()
                     ? InteractionArbiter.tryRunEating(
                        () -> {
                           InteractionResult var4x = var3.gameMode.useItem(var4, InteractionHand.MAIN_HAND);
                           LarpLog.info(
                              "Eating", "Started {} from slot {} with {} open: {}", var0, var4.getInventory().selected, var1.getClass().getSimpleName(), var4x
                           );
                        }
                     )
                     : false;
               }
            },
            var1
         );
      }
   }

   private static Screen currentScreen() {
      return Minecraft.getInstance().gui.screen();
   }

   public static void cancel(ItemUseController.Owner var0) {
      controller.release(var0);
   }

   public static void tick() {
      controller.reset();
   }

   private AutoEatHelper() {
   }
}
