package com.larpclient.module;

import com.larpclient._q;
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
   private static AntiTrapModule j;
   private final m h = this.m("Paintings", true);
   private final m W = this.m("Item Frames", true);
   private final m n = this.m("Minecarts", true);
   private final m A = this.m("Falling Blocks", false);
   private final m I = this.m("Armor Stands", false);
   private final m N = this.m("Banners", false);
   private final m M = this.m("Villagers", false);
   private final m p = this.m("Horses", false);
   private final m B = this.m("Signs", false);
   private final m t = this.m("Boats", false);

   public AntiTrapModule() {
      super("AntiTrap", "Skip interactions with selected trap objects", Category.COMBAT, -1);
      j = this;
   }

   private m m(String var1, boolean var2) {
      return this.addSetting(new m(var1, "Ignore " + var1.toLowerCase() + " when interacting", var2));
   }

   public static boolean j(Entity var0) {
      if (j != null && j.isEnabled()) {
         AntiTrapModule var1 = j;
         return var1 != null
            && var1.isRuntimeActive()
            && var0 != null
            && (
               var0 instanceof Painting && var1.h.getValue()
                  || var0 instanceof ItemFrame && var1.W.getValue()
                  || var0 instanceof AbstractMinecart && var1.n.getValue()
                  || var0 instanceof FallingBlockEntity && var1.A.getValue()
                  || var0 instanceof ArmorStand && var1.I.getValue()
                  || var0 instanceof Villager && var1.M.getValue()
                  || var0 instanceof AbstractHorse && var1.p.getValue()
                  || var0 instanceof AbstractBoat && var1.t.getValue()
            );
      } else {
         return false;
      }
   }

   public static boolean I(BlockState var0) {
      if (j != null && j.isEnabled()) {
         AntiTrapModule var1 = j;
         return var1 != null
            && var1.isRuntimeActive()
            && var0 != null
            && (var0.getBlock() instanceof AbstractBannerBlock && var1.N.getValue() || var0.getBlock() instanceof SignBlock && var1.B.getValue());
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      _q.c("AntiTrap", "Interaction trap filters active");
   }

   @Override
   public void onDisable() {
      _q.c("AntiTrap", "Interaction trap filters released");
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F);
   }
}
