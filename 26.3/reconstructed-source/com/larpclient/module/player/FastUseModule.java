package com.larpclient.module.player;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;

public final class FastUseModule extends Module {
   public static final int vANILLA_INTERVAL = 4;
   private static FastUseModule instance;
   private final NumberSetting useInterval = this.addSetting(
      new NumberSetting("Use Interval", "Ticks between uses while Use is held. Vanilla is 4", 1.0, 1.0, 4.0, 1.0)
   );
   private final BooleanSetting blocks = this.addSetting(new BooleanSetting("Blocks", "Place blocks faster", true));
   private final BooleanSetting projectiles = this.addSetting(
      new BooleanSetting("Projectiles", "Throw experience bottles, pearls, snowballs, eggs, potions, wind charges and fireworks faster", true)
   );
   private final BooleanSetting otherItems = this.addSetting(new BooleanSetting("Other Items", "Use every other item faster", true));

   public FastUseModule() {
      super("Fast Use", "Remove the delay between item uses", Category.PLAYER, -1);
      instance = this;
   }

   public static int clampInterval(int var0) {
      return Math.clamp(var0, 1, 4);
   }

   public static boolean shouldSpeedUp(ItemStack var0, boolean var1, boolean var2, boolean var3) {
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

   public static int getUseDelay(int var0) {
      if (instance != null && instance.isEnabled()) {
         FastUseModule var1 = instance;
         if (var1 != null && var1.isRuntimeActive() && !InteractionArbiter.isModuleInteracting()) {
            LocalPlayer var2 = getMc().player;
            if (var2 == null) {
               return var0;
            }

            boolean var3 = var1.blocks.getValue();
            boolean var4 = var1.projectiles.getValue();
            boolean var5 = var1.otherItems.getValue();
            return !shouldSpeedUp(var2.getMainHandItem(), var3, var4, var5) && !shouldSpeedUp(var2.getOffhandItem(), var3, var4, var5)
               ? var0
               : Math.min(var0, clampInterval(var1.useInterval.getValue().intValue()));
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public void onEnable() {
      LarpLog.debug(
         "FastUse",
         "Enabled; use interval {} ticks (blocks {}, projectiles {}, other items {})",
         this.useInterval.getValue().intValue(),
         this.blocks.getValue(),
         this.projectiles.getValue(),
         this.otherItems.getValue()
      );
   }

   @Override
   public String getStatus() {
      int var1 = clampInterval(this.useInterval.getValue().intValue());
      return var1 == 1 ? "Every tick" : var1 + " ticks";
   }
}
