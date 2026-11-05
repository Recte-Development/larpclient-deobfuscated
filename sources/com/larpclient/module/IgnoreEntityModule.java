package com.larpclient.module;

import com.larpclient.mt;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class IgnoreEntityModule extends Module {
   private static IgnoreEntityModule x;

   public IgnoreEntityModule() {
      super("IgnoreEntity", "Place blocks, crystals and fluids through entities", Category.COMBAT, -1);
      x = this;
   }

   public static boolean P(Entity var0) {
      if (x != null && x.isEnabled()) {
         Minecraft var1 = Minecraft.getInstance();
         return x != null
            && x.isRuntimeActive()
            && var0 != null
            && var1.player != null
            && var1.level != null
            && var1.options.keyUse.isDown()
            && (V(var1.player.getMainHandItem()) || V(var1.player.getOffhandItem()));
      } else {
         return false;
      }
   }

   private static boolean V(ItemStack var0) {
      return !var0.isEmpty()
         && (var0.getItem() instanceof BlockItem || var0.is(Items.END_CRYSTAL) || var0.is(Items.WATER_BUCKET) || var0.is(Items.LAVA_BUCKET));
   }

   @Override
   public void onEnable() {
      mt.U("IgnoreEntity", "Placement entity pick filter active");
   }

   @Override
   public void onDisable() {
      mt.U("IgnoreEntity", "Placement entity pick filter released");
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z);
   }
}
