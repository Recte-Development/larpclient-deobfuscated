package com.larpclient.module;

import com.larpclient.mt;
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
   private static AntiTrapModule A;
   private final dh M = this.r("Paintings", true);
   private final dh F = this.r("Item Frames", true);
   private final dh g = this.r("Minecarts", true);
   private final dh r = this.r("Falling Blocks", false);
   private final dh D = this.r("Armor Stands", false);
   private final dh W = this.r("Banners", false);
   private final dh m = this.r("Villagers", false);
   private final dh n = this.r("Horses", false);
   private final dh a = this.r("Signs", false);
   private final dh h = this.r("Boats", false);

   public AntiTrapModule() {
      super("AntiTrap", "Skip interactions with selected trap objects", Category.COMBAT, -1);
      A = this;
   }

   private dh r(String var1, boolean var2) {
      return this.addSetting(new dh(var1, "Ignore " + var1.toLowerCase() + " when interacting", var2));
   }

   public static boolean l(Entity var0) {
      if (A != null && A.isEnabled()) {
         AntiTrapModule var1 = A;
         return var1 != null
            && var1.isRuntimeActive()
            && var0 != null
            && (
               var0 instanceof Painting && var1.M.P()
                  || var0 instanceof ItemFrame && var1.F.P()
                  || var0 instanceof AbstractMinecart && var1.g.P()
                  || var0 instanceof FallingBlockEntity && var1.r.P()
                  || var0 instanceof ArmorStand && var1.D.P()
                  || var0 instanceof Villager && var1.m.P()
                  || var0 instanceof AbstractHorse && var1.n.P()
                  || var0 instanceof AbstractBoat && var1.h.P()
            );
      } else {
         return false;
      }
   }

   public static boolean z(BlockState var0) {
      if (A != null && A.isEnabled()) {
         AntiTrapModule var1 = A;
         return var1 != null
            && var1.isRuntimeActive()
            && var0 != null
            && (var0.getBlock() instanceof AbstractBannerBlock && var1.W.P() || var0.getBlock() instanceof SignBlock && var1.a.P());
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      mt.U("AntiTrap", "Interaction trap filters active");
   }

   @Override
   public void onDisable() {
      mt.U("AntiTrap", "Interaction trap filters released");
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z);
   }
}
