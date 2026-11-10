package com.larpclient.module;

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
   private static AntiTrapModule X;
   private final s c = this.X("Paintings", true);
   private final s U = this.X("Item Frames", true);
   private final s W = this.X("Minecarts", true);
   private final s l = this.X("Falling Blocks", false);
   private final s C = this.X("Armor Stands", false);
   private final s P = this.X("Banners", false);
   private final s V = this.X("Villagers", false);
   private final s x = this.X("Horses", false);
   private final s F = this.X("Signs", false);
   private final s m = this.X("Boats", false);

   public AntiTrapModule() {
      super("AntiTrap", "Skip interactions with selected trap objects", Category.COMBAT, -1);
      X = this;
   }

   private s X(String var1, boolean var2) {
      return this.addSetting(new s(var1, "Ignore " + var1.toLowerCase() + " when interacting", var2));
   }

   public static boolean K(Entity var0) {
      if (X != null && X.isEnabled()) {
         AntiTrapModule var1 = X;
         return var1 != null
            && var1.isRuntimeActive()
            && var0 != null
            && (
               var0 instanceof Painting && var1.c.getValue()
                  || var0 instanceof ItemFrame && var1.U.getValue()
                  || var0 instanceof AbstractMinecart && var1.W.getValue()
                  || var0 instanceof FallingBlockEntity && var1.l.getValue()
                  || var0 instanceof ArmorStand && var1.C.getValue()
                  || var0 instanceof Villager && var1.V.getValue()
                  || var0 instanceof AbstractHorse && var1.x.getValue()
                  || var0 instanceof AbstractBoat && var1.m.getValue()
            );
      } else {
         return false;
      }
   }

   public static boolean M(BlockState var0) {
      if (X != null && X.isEnabled()) {
         AntiTrapModule var1 = X;
         return var1 != null
            && var1.isRuntimeActive()
            && var0 != null
            && (var0.getBlock() instanceof AbstractBannerBlock && var1.P.getValue() || var0.getBlock() instanceof SignBlock && var1.F.getValue());
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      com.larpclient.h.U("AntiTrap", "Interaction trap filters active");
   }

   @Override
   public void onDisable() {
      com.larpclient.h.U("AntiTrap", "Interaction trap filters released");
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.K);
   }
}
