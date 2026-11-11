package com.larpclient.module.combat;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class AntiTrapModule extends Module {
   private static AntiTrapModule instance;
   private final BooleanSetting paintings = this.addToggle("Paintings", true);
   private final BooleanSetting itemFrames = this.addToggle("Item Frames", true);
   private final BooleanSetting minecarts = this.addToggle("Minecarts", true);
   private final BooleanSetting fallingBlocks = this.addToggle("Falling Blocks", false);
   private final BooleanSetting armorStands = this.addToggle("Armor Stands", false);
   private final BooleanSetting banners = this.addToggle("Banners", false);
   private final BooleanSetting villagers = this.addToggle("Villagers", false);
   private final BooleanSetting horses = this.addToggle("Horses", false);
   private final BooleanSetting signs = this.addToggle("Signs", false);
   private final BooleanSetting boats = this.addToggle("Boats", false);

   public AntiTrapModule() {
      super("AntiTrap", "Skip interactions with selected trap objects", Category.COMBAT, -1);
      instance = this;
   }

   private BooleanSetting addToggle(String var1, boolean var2) {
      return this.addSetting(new BooleanSetting(var1, "Ignore " + var1.toLowerCase() + " when interacting", var2));
   }

   public static boolean shouldIgnoreEntity(Entity var0) {
      if (instance != null && instance.isEnabled()) {
         AntiTrapModule var1 = instance;
         return var1 != null
            && var1.isRuntimeActive()
            && var0 != null
            && (
               var0 instanceof Painting && var1.paintings.getValue()
                  || var0 instanceof ItemFrame && var1.itemFrames.getValue()
                  || var0 instanceof AbstractMinecart && var1.minecarts.getValue()
                  || var0 instanceof FallingBlockEntity && var1.fallingBlocks.getValue()
                  || var0 instanceof ArmorStand && var1.armorStands.getValue()
                  || var0 instanceof Villager && var1.villagers.getValue()
                  || var0 instanceof AbstractHorse && var1.horses.getValue()
                  || var0 instanceof AbstractBoat && var1.boats.getValue()
            );
      } else {
         return false;
      }
   }

   public static boolean shouldIgnoreBlock(BlockState var0) {
      if (instance != null && instance.isEnabled()) {
         AntiTrapModule var1 = instance;
         return var1 != null
            && var1.isRuntimeActive()
            && var0 != null
            && (var0.getBlock() instanceof AbstractBannerBlock && var1.banners.getValue() || var0.getBlock() instanceof SignBlock && var1.signs.getValue());
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      LarpLog.debug("AntiTrap", "Interaction trap filters active");
   }

   @Override
   public void onDisable() {
      LarpLog.debug("AntiTrap", "Interaction trap filters released");
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING);
   }
}
