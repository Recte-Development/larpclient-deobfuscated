package com.larpclient.module;

import com.larpclient.r4;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class IgnoreEntityModule extends Module {
   private static IgnoreEntityModule C;

   public IgnoreEntityModule() {
      super("IgnoreEntity", "Place blocks, crystals and fluids through entities", Category.COMBAT, -1);
      C = this;
   }

   public static boolean G(Entity var0) {
      if (C != null && C.isEnabled()) {
         Minecraft var1 = Minecraft.getInstance();
         return C != null
            && C.isRuntimeActive()
            && var0 != null
            && var1.player != null
            && var1.level != null
            && var1.options.keyUse.isDown()
            && (l(var1.player.getMainHandItem()) || l(var1.player.getOffhandItem()));
      } else {
         return false;
      }
   }

   private static boolean l(ItemStack var0) {
      return !var0.isEmpty()
         && (var0.getItem() instanceof BlockItem || var0.is(Items.END_CRYSTAL) || var0.is(Items.WATER_BUCKET) || var0.is(Items.LAVA_BUCKET));
   }

   @Override
   public void onEnable() {
      r4.g("IgnoreEntity", "Placement entity pick filter active");
   }

   @Override
   public void onDisable() {
      r4.g("IgnoreEntity", "Placement entity pick filter released");
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a);
   }
}
