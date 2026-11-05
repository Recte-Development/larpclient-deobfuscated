package com.larpclient.module;

import com.larpclient._m;
import com.larpclient.xq;
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
   private final d9 t = this.addSetting(new d9("Combat", "Combat interaction range", 3.5, 3.0, 6.0, 0.1));
   private final d9 C = this.addSetting(new d9("Block", "Block interaction range", 4.5, 4.5, 8.0, 0.1));
   private final d6 U = this.addSetting(new d6("Mode", "Fixed or nearby-target-limited range", "Normal", "Normal", "Dynamic"));
   private final dh B = this.addSetting(new dh("OnlySword", "Only while holding a supported weapon (includes axes, trident and mace)", false));
   private static ReachModule m;
   private final xq w = new xq();
   private final xq X = new xq();
   private ReachModule.a p;

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   public ReachModule() {
      super("Reach", "Adjust attack and block reach", Category.COMBAT, -1);
      m = this;
   }

   public static ReachModule w() {
      return m;
   }

   public static boolean p() {
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

   private boolean H() {
      return this.isInGame();
   }

   private boolean I() {
      return this.isRuntimeActive() && this.H() && (!this.B.P() || p());
   }

   @Override
   public String getStatus() {
      return this.I() ? "Configured range active" : "Waiting for player or weapon condition";
   }

   @Override
   public void onTick() {
      int var10000 = this.I();
      boolean var11 = false;
      byte var10 = (byte)var10000;
      var10000 = Integer.signum(var10);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.onDisable();
      } else {
         double var1 = this.t.P();
         int var18 = this.U.S("Dynamic");
         boolean var13 = false;
         byte var12 = (byte)var18;
         var18 = Integer.signum(var12);
         if (((var18 | -var18) >>> 31 ^ 1) == 0) {
            double var3 = var1;

            for (Entity var6 : mc.level.entitiesForRendering()) {
               if (var6 != mc.player && var6.isAlive() && var6.isPickable()) {
                  var3 = Math.min(var3, _m.p(var6) + 0.5);
               }
            }

            var1 = Math.max(3.0, var3);
         }

         this.w.E(mc.player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE), var1);
         this.X.E(mc.player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE), this.C.P());
         IntegratedServer var14 = mc.getSingleplayerServer();
         if (var14 == null) {
            this.b();
         } else {
            if (this.p == null || this.p.n != var14) {
               this.b();
               this.p = new ReachModule.a(var14);
            }

            ReachModule.a var4 = this.p;
            UUID var15 = mc.player.getUUID();
            double var16 = var1;
            double var8 = this.C.P();
            var14.execute(() -> {
               ServerPlayer var7 = var14.getPlayerList().getPlayer(var15);
               if (var4.y && var7 != null && var7.isAlive()) {
                  var4.p.E(var7.getAttribute(Attributes.ENTITY_INTERACTION_RANGE), var16);
                  var4.L.E(var7.getAttribute(Attributes.BLOCK_INTERACTION_RANGE), var8);
               } else {
                  var4.R();
               }
            });
         }
      }
   }

   @Override
   public void onDisable() {
      this.w.e();
      this.X.e();
      this.b();
   }

   private void b() {
      ReachModule.a var1 = this.p;
      this.p = null;
      if (var1 != null) {
         var1.y = (boolean)0;
         var1.n.execute(var1::R);
      }
   }

   public void W(AttributeInstance var1) {
      this.w.Q(var1);
      this.X.Q(var1);
   }

   public double y() {
      return getMc().player == null ? 3.0 : getMc().player.entityInteractionRange();
   }

   public double L() {
      return this.y();
   }

   public double N() {
      return this.y();
   }

   public double D() {
      return getMc().player == null ? 4.5 : getMc().player.blockInteractionRange();
   }

   public boolean m(Entity var1) {
      return _m.b(var1, this.y());
   }

   private static final class a {
      final MinecraftServer n;
      final xq p = new xq();
      final xq L = new xq();
      volatile boolean y = (boolean)1;

      a(MinecraftServer var1) {
         this.n = var1;
      }

      void R() {
         this.p.e();
         this.L.e();
      }
   }
}
