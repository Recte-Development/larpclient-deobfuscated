package com.larpclient.module.misc;

import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class IgnoreEntityModule extends Module {
   private static IgnoreEntityModule instance;

   public IgnoreEntityModule() {
      super("IgnoreEntity", "Place blocks, crystals and fluids through entities", Category.COMBAT, -1);
      instance = this;
   }

   public static boolean shouldIgnoreEntity(Entity var0) {
      if (instance != null && instance.isEnabled()) {
         Minecraft var1 = Minecraft.getInstance();
         return instance != null
            && instance.isRuntimeActive()
            && var0 != null
            && var1.player != null
            && var1.level != null
            && var1.options.keyUse.isDown()
            && (isPlaceableItem(var1.player.getMainHandItem()) || isPlaceableItem(var1.player.getOffhandItem()));
      } else {
         return false;
      }
   }

   private static boolean isPlaceableItem(ItemStack var0) {
      return !var0.isEmpty()
         && (var0.getItem() instanceof BlockItem || var0.is(Items.END_CRYSTAL) || var0.is(Items.WATER_BUCKET) || var0.is(Items.LAVA_BUCKET));
   }

   @Override
   public void onEnable() {
      LarpLog.debug("IgnoreEntity", "Placement entity pick filter active");
   }

   @Override
   public void onDisable() {
      LarpLog.debug("IgnoreEntity", "Placement entity pick filter released");
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING);
   }
}
