package com.larpclient.module;

import com.larpclient.d8;
import com.larpclient.mh;
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
   private final lc E = this.addSetting(new lc("Combat", "Combat interaction range", 3.5, 3.0, 6.0, 0.1));
   private final lc X = this.addSetting(new lc("Block", "Block interaction range", 4.5, 4.5, 8.0, 0.1));
   private final lp N = this.addSetting(new lp("Mode", "Fixed or nearby-target-limited range", "Normal", "Normal", "Dynamic"));
   private final ln m = this.addSetting(new ln("OnlySword", "Only while holding a supported weapon (includes axes, trident and mace)", false));
   private static ReachModule M;
   private final mh v = new mh();
   private final mh D = new mh();
   private ReachModule.y q;

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   public ReachModule() {
      super("Reach", "Adjust attack and block reach", Category.COMBAT, -1);
      M = this;
   }

   public static ReachModule I() {
      return M;
   }

   public static boolean s() {
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

   private boolean f() {
      return this.isInGame();
   }

   private boolean i() {
      return this.isRuntimeActive() && this.f() && (!this.m.getValue() || s());
   }

   @Override
   public String getStatus() {
      return this.i() ? "Configured range active" : "Waiting for player or weapon condition";
   }

   @Override
   public void onTick() {
      int var10000 = this.i();
      boolean var11 = false;
      byte var10 = (byte)var10000;
      var10000 = Integer.signum(var10);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.onDisable();
      } else {
         double var1 = this.E.getValue();
         int var18 = this.N.i("Dynamic");
         boolean var13 = false;
         byte var12 = (byte)var18;
         var18 = Integer.signum(var12);
         if (((var18 | -var18) >>> 31 ^ 1) == 0) {
            double var3 = var1;

            for (Entity var6 : mc.level.entitiesForRendering()) {
               if (var6 != mc.player && var6.isAlive() && var6.isPickable()) {
                  var3 = Math.min(var3, d8.L(var6) + 0.5);
               }
            }

            var1 = Math.max(3.0, var3);
         }

         this.v.e(mc.player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE), var1);
         this.D.e(mc.player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE), this.X.getValue());
         IntegratedServer var14 = mc.getSingleplayerServer();
         if (var14 == null) {
            this.e();
         } else {
            if (this.q == null || this.q.H != var14) {
               this.e();
               this.q = new ReachModule.y(var14);
            }

            ReachModule.y var4 = this.q;
            UUID var15 = mc.player.getUUID();
            double var16 = var1;
            double var8 = this.X.getValue();
            var14.execute(() -> {
               ServerPlayer var7 = var14.getPlayerList().getPlayer(var15);
               if (var4.u && var7 != null && var7.isAlive()) {
                  var4.Q.e(var7.getAttribute(Attributes.ENTITY_INTERACTION_RANGE), var16);
                  var4.I.e(var7.getAttribute(Attributes.BLOCK_INTERACTION_RANGE), var8);
               } else {
                  var4.U();
               }
            });
         }
      }
   }

   @Override
   public void onDisable() {
      this.v.e();
      this.D.e();
      this.e();
   }

   private void e() {
      ReachModule.y var1 = this.q;
      this.q = null;
      if (var1 != null) {
         var1.u = (boolean)0;
         var1.H.execute(var1::U);
      }
   }

   public void V(AttributeInstance var1) {
      this.v.v(var1);
      this.D.v(var1);
   }

   public double P() {
      return getMc().player == null ? 3.0 : getMc().player.entityInteractionRange();
   }

   public double Q() {
      return this.P();
   }

   public double D() {
      return this.P();
   }

   public double K() {
      return getMc().player == null ? 4.5 : getMc().player.blockInteractionRange();
   }

   public boolean s(Entity var1) {
      return d8.M(var1, this.P());
   }

   private static final class y {
      final MinecraftServer H;
      final mh Q = new mh();
      final mh I = new mh();
      volatile boolean u = (boolean)1;

      y(MinecraftServer var1) {
         this.H = var1;
      }

      void U() {
         this.Q.e();
         this.I.e();
      }
   }
}
