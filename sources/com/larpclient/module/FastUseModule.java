package com.larpclient.module;

import com.larpclient._m;
import com.larpclient.mt;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;

public final class FastUseModule extends Module {
   public static final int O = 4;
   private static FastUseModule C;
   private final d9 Q = this.addSetting(new d9("Use Interval", "Ticks between uses while Use is held. Vanilla is 4", 1.0, 1.0, 4.0, 1.0));
   private final dh r = this.addSetting(new dh("Blocks", "Place blocks faster", true));
   private final dh U = this.addSetting(
      new dh("Projectiles", "Throw experience bottles, pearls, snowballs, eggs, potions, wind charges and fireworks faster", true)
   );
   private final dh G = this.addSetting(new dh("Other Items", "Use every other item faster", true));

   public FastUseModule() {
      super("Fast Use", "Remove the delay between item uses", Category.PLAYER, -1);
      C = this;
   }

   public static int k(int var0) {
      return Math.clamp(var0, 1, 4);
   }

   public static boolean V(ItemStack var0, boolean var1, boolean var2, boolean var3) {
      if (var0 != null && !var0.isEmpty()) {
         Item var4 = var0.getItem();
         if (var4 instanceof BlockItem) {
            return var1;
         } else {
            return !(var4 instanceof ProjectileItem) && !(var4 instanceof EnderpearlItem) ? var3 : var2;
         }
      } else {
         return false;
      }
   }

   public static int w(int var0) {
      if (C != null && C.isEnabled()) {
         FastUseModule var1 = C;
         if (var1 != null && var1.isRuntimeActive() && !_m.W()) {
            LocalPlayer var2 = getMc().player;
            if (var2 == null) {
               return var0;
            }

            boolean var3 = var1.r.P();
            boolean var4 = var1.U.P();
            boolean var5 = var1.G.P();
            return !V(var2.getMainHandItem(), var3, var4, var5) && !V(var2.getOffhandItem(), var3, var4, var5)
               ? var0
               : Math.min(var0, k(var1.Q.P().intValue()));
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public void onEnable() {
      mt.U("FastUse", "Enabled; use interval {} ticks (blocks {}, projectiles {}, other items {})", this.Q.P().intValue(), this.r.P(), this.U.P(), this.G.P());
   }

   @Override
   public String getStatus() {
      int var1 = k(this.Q.P().intValue());
      return var1 == 1 ? "Every tick" : var1 + " ticks";
   }
}
