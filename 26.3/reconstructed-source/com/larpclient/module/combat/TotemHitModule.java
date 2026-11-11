package com.larpclient.module.combat;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.inventory.HotbarSwapSession;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;

public final class TotemHitModule extends Module {
   private static TotemHitModule instance;
   private final NumberSetting switchDelay = this.addSetting(
      new NumberSetting("Switch Delay", "Milliseconds before returning to your totem after the sword hit", 30.0, 10.0, 100.0, 1.0)
   );
   private final HotbarSwapSession swapSession = new HotbarSwapSession(this);
   private long swapTimeNanos;
   private String status = "Ready";

   public TotemHitModule() {
      super("Totem Hit", "Hit with your sword while holding a totem, then switch back", Category.COMBAT, -1);
      instance = this;
   }

   public static int findSwordSlot(LocalPlayer var0) {
      for (int var1 = 0; var1 < 9; var1++) {
         if (var0.getInventory().getItem(var1).is(ItemTags.SWORDS)) {
            return var1;
         }
      }

      return -1;
   }

   public static boolean hasElapsed(long var0, long var2, double var4) {
      return var2 - var0 >= (long)(var4 * 1000000.0);
   }

   public static void onAttack(LocalPlayer var0, Entity var1) {
      if (instance != null && instance.isEnabled()) {
         TotemHitModule var2 = instance;
         if (var2 != null && var2.isRuntimeActive() && var1 != null && !InteractionArbiter.isAttacking()) {
            Minecraft var3 = getMc();
            if (var0 == var3.player && !var2.swapSession.isSwapped() && var0.getMainHandItem().is(Items.TOTEM_OF_UNDYING)) {
               int var4 = findSwordSlot(var0);
               if (var4 < 0) {
                  var2.status = "No sword in hotbar";
               } else if (!var2.swapSession.swapTo(var0, var4)) {
                  var2.status = "Hotbar busy";
               } else {
                  var2.swapTimeNanos = System.nanoTime();
                  var2.status = "Sword hit";
                  LarpLog.debug("TotemHit", "Hit {} with hotbar sword {}", var1.getId(), var4 + 1);
               }
            }
         }
      }
   }

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.MAIN;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING, ScanPhase.dONE);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.status : "";
   }

   @Override
   public void onEnable() {
      this.status = "Ready";
      LarpLog.info("TotemHit", "Enabled; switch delay {} ms", this.switchDelay.getValue().intValue());
   }

   @Override
   public void onDisable() {
      if (this.swapSession.isSwapped()) {
         this.swapSession.restore(getMc(), true);
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.swapSession.isSwapped();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         int var6 = hasElapsed(this.swapTimeNanos, System.nanoTime(), this.switchDelay.getValue());
         boolean var4 = false;
         byte var3 = (byte)var6;
         var6 = Integer.signum(var3);
         if ((var6 | -var6) >>> 31 != 0) {
            this.swapSession.restore(minecraft, true);
            this.status = "Ready";
            return;
         }
      }
   }
}
