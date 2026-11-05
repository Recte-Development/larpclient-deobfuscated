package com.larpclient.module;

import com.larpclient.d8;
import com.larpclient.r4;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;

public final class FastUseModule extends Module {
   public static final int l = 4;
   private static FastUseModule A;
   private final lc c = this.addSetting(new lc("Use Interval", "Ticks between uses while Use is held. Vanilla is 4", 1.0, 1.0, 4.0, 1.0));
   private final ln z = this.addSetting(new ln("Blocks", "Place blocks faster", true));
   private final ln S = this.addSetting(
      new ln("Projectiles", "Throw experience bottles, pearls, snowballs, eggs, potions, wind charges and fireworks faster", true)
   );
   private final ln Z = this.addSetting(new ln("Other Items", "Use every other item faster", true));

   public FastUseModule() {
      super("Fast Use", "Remove the delay between item uses", Category.PLAYER, -1);
      A = this;
   }

   public static int u(int var0) {
      return Math.clamp(var0, 1, 4);
   }

   public static boolean r(ItemStack var0, boolean var1, boolean var2, boolean var3) {
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

   public static int I(int var0) {
      if (A != null && A.isEnabled()) {
         FastUseModule var1 = A;
         if (var1 != null && var1.isRuntimeActive() && !d8.N()) {
            LocalPlayer var2 = getMc().player;
            if (var2 == null) {
               return var0;
            }

            boolean var3 = var1.z.getValue();
            boolean var4 = var1.S.getValue();
            boolean var5 = var1.Z.getValue();
            return !r(var2.getMainHandItem(), var3, var4, var5) && !r(var2.getOffhandItem(), var3, var4, var5)
               ? var0
               : Math.min(var0, u(var1.c.getValue().intValue()));
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public void onEnable() {
      r4.g(
         "FastUse",
         "Enabled; use interval {} ticks (blocks {}, projectiles {}, other items {})",
         this.c.getValue().intValue(),
         this.z.getValue(),
         this.S.getValue(),
         this.Z.getValue()
      );
   }

   @Override
   public String getStatus() {
      int var1 = u(this.c.getValue().intValue());
      return var1 == 1 ? "Every tick" : var1 + " ticks";
   }
}
