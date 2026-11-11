package com.larpclient.module.combat;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.CombatUtil;
import com.larpclient.util.inventory.HotbarSwapSession;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class ShieldBreakerModule extends Module {
   private static ShieldBreakerModule instance;
   private final BooleanSetting swapBack = this.addSetting(
      new BooleanSetting("Swap Back", "Return to your previous hotbar slot on the tick after the hit", true)
   );
   private final HotbarSwapSession swapSession = new HotbarSwapSession(this);
   private String status = "Ready";
   private Player target;
   private int hitTick;

   public ShieldBreakerModule() {
      super("Shield Breaker", "Axe hit swap: your hits on a raised shield go out with your best axe, then your slot returns", Category.COMBAT, -1);
      instance = this;
   }

   @Override
   public String getDisplayName() {
      return "Axe Shield Breaker";
   }

   public static void onAttack(LocalPlayer var0, Entity var1) {
      if (instance != null && instance.isEnabled()) {
         ShieldBreakerModule var2 = instance;
         if (var2 != null && var2.isRuntimeActive() && !InteractionArbiter.isAttacking() && var1 instanceof Player var3) {
            if (var0 == getMc().player && !var2.swapSession.isSwapped() && CombatUtil.isBlockingAttack(var3, var0) && !CombatUtil.isRecentTarget(var3)) {
               MaceModule var4 = MaceModule.getInstance();
               if (var4 != null && var4.shouldStunSlam(var0, var3)) {
                  var2.status = "Stun Slam by Auto Mace";
               } else {
                  int var5 = CombatUtil.findShieldBreakerSlot(var0);
                  if (var5 < 0) {
                     var2.status = "No axe in hotbar";
                  } else if (!var2.swapSession.swapTo(var0, var5)) {
                     var2.status = "Hotbar busy";
                  } else {
                     CombatUtil.markRecentTarget(var3);
                     var2.target = var3;
                     var2.hitTick = var3.tickCount;
                     var2.status = "Shield break hit";
                     LarpLog.debug(
                        "ShieldBreaker",
                        "Hit {} through a raised shield with hotbar slot {}, shield used {} ticks, ping {} ticks",
                        var3.getId(),
                        var5 + 1,
                        var3.getTicksUsingItem(),
                        CombatUtil.getPingTicks(var0)
                     );
                  }
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
      this.target = null;
      LarpLog.info("ShieldBreaker", "Enabled; swap back {}", this.swapBack.getValue());
   }

   @Override
   public void onDisable() {
      if (this.swapSession.isSwapped()) {
         this.swapSession.restore(getMc(), this.swapBack.getValue());
      }

      this.target = null;
   }

   @Override
   public void onTick() {
      this.trackShieldState();
      int var10000 = this.swapSession.isSwapped();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         this.swapSession.tick(minecraft, this.swapBack.getValue());
         int var6 = this.swapSession.isSwapped();
         boolean var4 = false;
         byte var3 = (byte)var6;
         var6 = Integer.signum(var3);
         if ((var6 | -var6) >>> 31 == 0) {
            this.status = "Ready";
         }
      }
   }

   private void trackShieldState() {
      Player var1 = this.target;
      if (var1 != null) {
         int var2 = var1.tickCount - this.hitTick;
         if (var1.isRemoved() || var1.level() != minecraft.level) {
            this.target = null;
         } else if (!var1.isUsingItem()) {
            this.target = null;
            LarpLog.debug("ShieldBreaker", "Shield of {} went down {} ticks after the axe hit", var1.getId(), var2);
         } else if (!CombatUtil.isRecentTarget(var1)) {
            this.target = null;
            this.status = "Retrying";
            LarpLog.info("ShieldBreaker", "Shield of {} still up {} ticks after the axe hit; the next hit retries", var1.getId(), var2);
         }
      }
   }
}
