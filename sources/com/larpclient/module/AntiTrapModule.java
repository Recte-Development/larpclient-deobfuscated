package com.larpclient.module;

import com.larpclient.r4;
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
   private static AntiTrapModule C;
   private final ln V = this.G("Paintings", true);
   private final ln b = this.G("Item Frames", true);
   private final ln G = this.G("Minecarts", true);
   private final ln B = this.G("Falling Blocks", false);
   private final ln A = this.G("Armor Stands", false);
   private final ln r = this.G("Banners", false);
   private final ln E = this.G("Villagers", false);
   private final ln n = this.G("Horses", false);
   private final ln m = this.G("Signs", false);
   private final ln T = this.G("Boats", false);

   public AntiTrapModule() {
      super("AntiTrap", "Skip interactions with selected trap objects", Category.COMBAT, -1);
      C = this;
   }

   private ln G(String var1, boolean var2) {
      return this.addSetting(new ln(var1, "Ignore " + var1.toLowerCase() + " when interacting", var2));
   }

   public static boolean A(Entity var0) {
      if (C != null && C.isEnabled()) {
         AntiTrapModule var1 = C;
         return var1 != null
            && var1.isRuntimeActive()
            && var0 != null
            && (
               var0 instanceof Painting && var1.V.getValue()
                  || var0 instanceof ItemFrame && var1.b.getValue()
                  || var0 instanceof AbstractMinecart && var1.G.getValue()
                  || var0 instanceof FallingBlockEntity && var1.B.getValue()
                  || var0 instanceof ArmorStand && var1.A.getValue()
                  || var0 instanceof Villager && var1.E.getValue()
                  || var0 instanceof AbstractHorse && var1.n.getValue()
                  || var0 instanceof AbstractBoat && var1.T.getValue()
            );
      } else {
         return false;
      }
   }

   public static boolean V(BlockState var0) {
      if (C != null && C.isEnabled()) {
         AntiTrapModule var1 = C;
         return var1 != null
            && var1.isRuntimeActive()
            && var0 != null
            && (var0.getBlock() instanceof AbstractBannerBlock && var1.r.getValue() || var0.getBlock() instanceof SignBlock && var1.m.getValue());
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      r4.g("AntiTrap", "Interaction trap filters active");
   }

   @Override
   public void onDisable() {
      r4.g("AntiTrap", "Interaction trap filters released");
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a);
   }
}
