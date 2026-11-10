package com.larpclient.module;

import com.larpclient.ta;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;

public final class FastUseModule extends Module {
   public static final int E = 4;
   private static FastUseModule s;
   private final e V = this.addSetting(new e("Use Interval", "Ticks between uses while Use is held. Vanilla is 4", 1.0, 1.0, 4.0, 1.0));
   private final s K = this.addSetting(new s("Blocks", "Place blocks faster", true));
   private final s R = this.addSetting(
      new s("Projectiles", "Throw experience bottles, pearls, snowballs, eggs, potions, wind charges and fireworks faster", true)
   );
   private final s M = this.addSetting(new s("Other Items", "Use every other item faster", true));

   public FastUseModule() {
      super("Fast Use", "Remove the delay between item uses", Category.PLAYER, -1);
      s = this;
   }

   public static int g(int var0) {
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

   public static int F(int var0) {
      if (s != null && s.isEnabled()) {
         FastUseModule var1 = s;
         if (var1 != null && var1.isRuntimeActive() && !ta.d()) {
            LocalPlayer var2 = getMc().player;
            if (var2 == null) {
               return var0;
            }

            boolean var3 = var1.K.getValue();
            boolean var4 = var1.R.getValue();
            boolean var5 = var1.M.getValue();
            return !V(var2.getMainHandItem(), var3, var4, var5) && !V(var2.getOffhandItem(), var3, var4, var5)
               ? var0
               : Math.min(var0, g(var1.V.getValue().intValue()));
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   @Override
   public void onEnable() {
      com.larpclient.h.U(
         "FastUse",
         "Enabled; use interval {} ticks (blocks {}, projectiles {}, other items {})",
         this.V.getValue().intValue(),
         this.K.getValue(),
         this.R.getValue(),
         this.M.getValue()
      );
   }

   @Override
   public String getStatus() {
      int var1 = g(this.V.getValue().intValue());
      return var1 == 1 ? "Every tick" : var1 + " ticks";
   }
}
