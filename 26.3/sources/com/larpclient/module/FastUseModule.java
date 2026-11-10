package com.larpclient.module;

import com.larpclient._p;
import com.larpclient._q;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;

public final class FastUseModule extends Module {
   public static final int L = 4;
   private static FastUseModule q;
   private final b o = this.addSetting(new b("Use Interval", "Ticks between uses while Use is held. Vanilla is 4", 1.0, 1.0, 4.0, 1.0));
   private final m I = this.addSetting(new m("Blocks", "Place blocks faster", true));
   private final m B = this.addSetting(
      new m("Projectiles", "Throw experience bottles, pearls, snowballs, eggs, potions, wind charges and fireworks faster", true)
   );
   private final m g = this.addSetting(new m("Other Items", "Use every other item faster", true));

   public FastUseModule() {
      super("Fast Use", "Remove the delay between item uses", Category.PLAYER, -1);
      q = this;
   }

   public static int S(int var0) {
      return Math.clamp(var0, 1, 4);
   }

   public static boolean w(ItemStack var0, boolean var1, boolean var2, boolean var3) {
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

   public static int X(int var0) {
      if (q != null && q.isEnabled()) {
         FastUseModule var1 = q;
         if (var1 != null && var1.isRuntimeActive() && !_p.I()) {
            LocalPlayer var2 = getMc().player;
            if (var2 == null) {
               return var0;
            }

            boolean var3 = var1.I.getValue();
            boolean var4 = var1.B.getValue();
            boolean var5 = var1.g.getValue();
            return !w(var2.getMainHandItem(), var3, var4, var5) && !w(var2.getOffhandItem(), var3, var4, var5)
               ? var0
               : Math.min(var0, S(var1.o.getValue().intValue()));
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   @Override
   public void onEnable() {
      _q.c(
         "FastUse",
         "Enabled; use interval {} ticks (blocks {}, projectiles {}, other items {})",
         this.o.getValue().intValue(),
         this.I.getValue(),
         this.B.getValue(),
         this.g.getValue()
      );
   }

   @Override
   public String getStatus() {
      int var1 = S(this.o.getValue().intValue());
      return var1 == 1 ? "Every tick" : var1 + " ticks";
   }
}
