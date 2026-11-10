package com.larpclient.module;

import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class IgnoreEntityModule extends Module {
   private static IgnoreEntityModule Q;

   public IgnoreEntityModule() {
      super("IgnoreEntity", "Place blocks, crystals and fluids through entities", Category.COMBAT, -1);
      Q = this;
   }

   public static boolean i(Entity var0) {
      if (Q != null && Q.isEnabled()) {
         Minecraft var1 = Minecraft.getInstance();
         return Q != null
            && Q.isRuntimeActive()
            && var0 != null
            && var1.player != null
            && var1.level != null
            && var1.options.keyUse.isDown()
            && (u(var1.player.getMainHandItem()) || u(var1.player.getOffhandItem()));
      } else {
         return false;
      }
   }

   private static boolean u(ItemStack var0) {
      return !var0.isEmpty()
         && (var0.getItem() instanceof BlockItem || var0.is(Items.END_CRYSTAL) || var0.is(Items.WATER_BUCKET) || var0.is(Items.LAVA_BUCKET));
   }

   @Override
   public void onEnable() {
      com.larpclient.h.U("IgnoreEntity", "Placement entity pick filter active");
   }

   @Override
   public void onDisable() {
      com.larpclient.h.U("IgnoreEntity", "Placement entity pick filter released");
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.K);
   }
}
