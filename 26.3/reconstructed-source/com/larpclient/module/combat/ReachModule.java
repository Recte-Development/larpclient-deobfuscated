package com.larpclient.module.combat;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.AttributeOverride;
import com.larpclient.util.misc.InteractionArbiter;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ReachModule extends Module {
   private final NumberSetting combatRange = this.addSetting(new NumberSetting("Combat", "Combat interaction range", 3.5, 3.0, 6.0, 0.1));
   private final NumberSetting blockRange = this.addSetting(new NumberSetting("Block", "Block interaction range", 4.5, 4.5, 8.0, 0.1));
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "Fixed or nearby-target-limited range", "Normal", "Normal", "Dynamic"));
   private final BooleanSetting onlySword = this.addSetting(
      new BooleanSetting("OnlySword", "Only while holding a supported weapon (includes axes, trident and mace)", false)
   );
   private static ReachModule instance;
   private final AttributeOverride entityRangeOverride = new AttributeOverride();
   private final AttributeOverride blockRangeOverride = new AttributeOverride();
   private ReachModule.ServerReachState serverState;

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   public ReachModule() {
      super("Reach", "Adjust attack and block reach", Category.COMBAT, -1);
      instance = this;
   }

   public static ReachModule getInstance() {
      return instance;
   }

   public static boolean isHoldingWeapon() {
      LocalPlayer var0 = getMc().player;
      if (var0 == null) {
         return false;
      }

      ItemStack var1 = var0.getMainHandItem();
      return var1.is(Items.WOODEN_SWORD)
         || var1.is(Items.STONE_SWORD)
         || var1.is(Items.IRON_SWORD)
         || var1.is(Items.GOLDEN_SWORD)
         || var1.is(Items.DIAMOND_SWORD)
         || var1.is(Items.NETHERITE_SWORD)
         || var1.is(Items.WOODEN_AXE)
         || var1.is(Items.STONE_AXE)
         || var1.is(Items.IRON_AXE)
         || var1.is(Items.GOLDEN_AXE)
         || var1.is(Items.DIAMOND_AXE)
         || var1.is(Items.NETHERITE_AXE)
         || var1.is(Items.TRIDENT)
         || var1.is(Items.MACE);
   }

   private boolean getAttackRange() {
      return this.isInGame();
   }

   private boolean isReachActive() {
      return this.isRuntimeActive() && this.getAttackRange() && (!this.onlySword.getValue() || isHoldingWeapon());
   }

   @Override
   public String getStatus() {
      return this.isReachActive() ? "Configured range active" : "Waiting for player or weapon condition";
   }

   @Override
   public void onTick() {
      int var10000 = this.isReachActive();
      boolean var11 = false;
      byte var10 = (byte)var10000;
      var10000 = Integer.signum(var10);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.onDisable();
      } else {
         double var1 = this.combatRange.getValue();
         int var18 = this.mode.isMode("Dynamic");
         boolean var13 = false;
         byte var12 = (byte)var18;
         var18 = Integer.signum(var12);
         if (((var18 | -var18) >>> 31 ^ 1) == 0) {
            double var3 = var1;

            for (Entity var6 : minecraft.level.entitiesForRendering()) {
               if (var6 != minecraft.player && var6.isAlive() && var6.isPickable()) {
                  var3 = Math.min(var3, InteractionArbiter.claimManualInteraction(var6) + 0.5);
               }
            }

            var1 = Math.max(3.0, var3);
         }

         this.entityRangeOverride.apply(minecraft.player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE), var1);
         this.blockRangeOverride.apply(minecraft.player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE), this.blockRange.getValue());
         IntegratedServer var14 = minecraft.getSingleplayerServer();
         if (var14 == null) {
            this.releaseServerState();
         } else {
            if (this.serverState == null || this.serverState.server != var14) {
               this.releaseServerState();
               this.serverState = new ReachModule.ServerReachState(var14);
            }

            ReachModule.ServerReachState var4 = this.serverState;
            UUID var15 = minecraft.player.getUUID();
            double var16 = var1;
            double var8 = this.blockRange.getValue();
            var14.execute(() -> {
               ServerPlayer var7 = var14.getPlayerList().getPlayer(var15);
               if (var4.active && var7 != null && var7.isAlive()) {
                  var4.blockRangeOverride.apply(var7.getAttribute(Attributes.ENTITY_INTERACTION_RANGE), var16);
                  var4.entityRangeOverride.apply(var7.getAttribute(Attributes.BLOCK_INTERACTION_RANGE), var8);
               } else {
                  var4.restore();
               }
            });
         }
      }
   }

   @Override
   public void onDisable() {
      this.entityRangeOverride.restore();
      this.blockRangeOverride.restore();
      this.releaseServerState();
   }

   private void releaseServerState() {
      ReachModule.ServerReachState var1 = this.serverState;
      this.serverState = null;
      if (var1 != null) {
         var1.active = (boolean)0;
         var1.server.execute(var1::restore);
      }
   }

   public void detachAttribute(AttributeInstance var1) {
      this.entityRangeOverride.detach(var1);
      this.blockRangeOverride.detach(var1);
   }

   public double getEntityRange() {
      return getMc().player == null ? 3.0 : getMc().player.entityInteractionRange();
   }

   public double getAttackRange() {
      return this.getEntityRange();
   }

   public double releaseServerState() {
      return this.getEntityRange();
   }

   public double getBlockRange() {
      return getMc().player == null ? 4.5 : getMc().player.blockInteractionRange();
   }

   public boolean isLookingAt(Entity var1) {
      return InteractionArbiter.isLookingAt(var1, this.getEntityRange());
   }

   private static final class ServerReachState {
      final MinecraftServer server;
      final AttributeOverride blockRangeOverride = new AttributeOverride();
      final AttributeOverride entityRangeOverride = new AttributeOverride();
      volatile boolean active = (boolean)1;

      ServerReachState(MinecraftServer var1) {
         this.server = var1;
      }

      void restore() {
         this.blockRangeOverride.restore();
         this.entityRangeOverride.restore();
      }
   }
}
