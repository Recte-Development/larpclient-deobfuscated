package com.larpclient.module;

import com.larpclient._p;
import com.larpclient.sm;
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
   private final b H = this.addSetting(new b("Combat", "Combat interaction range", 3.5, 3.0, 6.0, 0.1));
   private final b A = this.addSetting(new b("Block", "Block interaction range", 4.5, 4.5, 8.0, 0.1));
   private final o M = this.addSetting(new o("Mode", "Fixed or nearby-target-limited range", "Normal", "Normal", "Dynamic"));
   private final m O = this.addSetting(new m("OnlySword", "Only while holding a supported weapon (includes axes, trident and mace)", false));
   private static ReachModule K;
   private final sm a = new sm();
   private final sm v = new sm();
   private ReachModule.l e;

   @Override
   public Set<com.larpclient.module.l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.k);
   }

   public ReachModule() {
      super("Reach", "Adjust attack and block reach", Category.COMBAT, -1);
      K = this;
   }

   public static ReachModule N() {
      return K;
   }

   public static boolean Y() {
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

   private boolean g() {
      return this.isInGame();
   }

   private boolean x() {
      return this.isRuntimeActive() && this.g() && (!this.O.getValue() || Y());
   }

   @Override
   public String getStatus() {
      return this.x() ? "Configured range active" : "Waiting for player or weapon condition";
   }

   @Override
   public void onTick() {
      int var10000 = this.x();
      boolean var11 = false;
      byte var10 = (byte)var10000;
      var10000 = Integer.signum(var10);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.onDisable();
      } else {
         double var1 = this.H.getValue();
         int var18 = this.M.w("Dynamic");
         boolean var13 = false;
         byte var12 = (byte)var18;
         var18 = Integer.signum(var12);
         if (((var18 | -var18) >>> 31 ^ 1) == 0) {
            double var3 = var1;

            for (Entity var6 : mc.level.entitiesForRendering()) {
               if (var6 != mc.player && var6.isAlive() && var6.isPickable()) {
                  var3 = Math.min(var3, _p.y(var6) + 0.5);
               }
            }

            var1 = Math.max(3.0, var3);
         }

         this.a.T(mc.player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE), var1);
         this.v.T(mc.player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE), this.A.getValue());
         IntegratedServer var14 = mc.getSingleplayerServer();
         if (var14 == null) {
            this.o();
         } else {
            if (this.e == null || this.e.V != var14) {
               this.o();
               this.e = new ReachModule.l(var14);
            }

            ReachModule.l var4 = this.e;
            UUID var15 = mc.player.getUUID();
            double var16 = var1;
            double var8 = this.A.getValue();
            var14.execute(() -> {
               ServerPlayer var7 = var14.getPlayerList().getPlayer(var15);
               if (var4.m && var7 != null && var7.isAlive()) {
                  var4.O.T(var7.getAttribute(Attributes.ENTITY_INTERACTION_RANGE), var16);
                  var4.M.T(var7.getAttribute(Attributes.BLOCK_INTERACTION_RANGE), var8);
               } else {
                  var4.E();
               }
            });
         }
      }
   }

   @Override
   public void onDisable() {
      this.a.p();
      this.v.p();
      this.o();
   }

   private void o() {
      ReachModule.l var1 = this.e;
      this.e = null;
      if (var1 != null) {
         var1.m = (boolean)0;
         var1.V.execute(var1::E);
      }
   }

   public void z(AttributeInstance var1) {
      this.a.M(var1);
      this.v.M(var1);
   }

   public double R() {
      return getMc().player == null ? 3.0 : getMc().player.entityInteractionRange();
   }

   public double g() {
      return this.R();
   }

   public double o() {
      return this.R();
   }

   public double k() {
      return getMc().player == null ? 4.5 : getMc().player.blockInteractionRange();
   }

   public boolean d(Entity var1) {
      return _p.f(var1, this.R());
   }

   private static final class l {
      final MinecraftServer V;
      final sm O = new sm();
      final sm M = new sm();
      volatile boolean m = (boolean)1;

      l(MinecraftServer var1) {
         this.V = var1;
      }

      void E() {
         this.O.p();
         this.M.p();
      }
   }
}
