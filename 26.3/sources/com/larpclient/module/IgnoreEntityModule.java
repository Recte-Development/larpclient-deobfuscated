package com.larpclient.module;

import com.larpclient._q;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class IgnoreEntityModule extends Module {
   private static IgnoreEntityModule W;

   public IgnoreEntityModule() {
      super("IgnoreEntity", "Place blocks, crystals and fluids through entities", Category.COMBAT, -1);
      W = this;
   }

   public static boolean u(Entity var0) {
      if (W != null && W.isEnabled()) {
         Minecraft var1 = Minecraft.getInstance();
         return W != null
            && W.isRuntimeActive()
            && var0 != null
            && var1.player != null
            && var1.level != null
            && var1.options.keyUse.isDown()
            && (F(var1.player.getMainHandItem()) || F(var1.player.getOffhandItem()));
      } else {
         return false;
      }
   }

   private static boolean F(ItemStack var0) {
      return !var0.isEmpty()
         && (var0.getItem() instanceof BlockItem || var0.is(Items.END_CRYSTAL) || var0.is(Items.WATER_BUCKET) || var0.is(Items.LAVA_BUCKET));
   }

   @Override
   public void onEnable() {
      _q.c("IgnoreEntity", "Placement entity pick filter active");
   }

   @Override
   public void onDisable() {
      _q.c("IgnoreEntity", "Placement entity pick filter released");
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F);
   }
}
