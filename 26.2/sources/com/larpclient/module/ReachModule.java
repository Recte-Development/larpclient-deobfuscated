package com.larpclient.module;

import com.larpclient.t9;
import com.larpclient.ta;
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
   private final e f = this.addSetting(new e("Combat", "Combat interaction range", 3.5, 3.0, 6.0, 0.1));
   private final e E = this.addSetting(new e("Block", "Block interaction range", 4.5, 4.5, 8.0, 0.1));
   private final l d = this.addSetting(new l("Mode", "Fixed or nearby-target-limited range", "Normal", "Normal", "Dynamic"));
   private final s j = this.addSetting(new s("OnlySword", "Only while holding a supported weapon (includes axes, trident and mace)", false));
   private static ReachModule C;
   private final t9 O = new t9();
   private final t9 a = new t9();
   private ReachModule.o X;

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   public ReachModule() {
      super("Reach", "Adjust attack and block reach", Category.COMBAT, -1);
      C = this;
   }

   public static ReachModule i() {
      return C;
   }

   public static boolean x() {
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

   private boolean p() {
      return this.isInGame();
   }

   private boolean L() {
      return this.isRuntimeActive() && this.p() && (!this.j.getValue() || x());
   }

   @Override
   public String getStatus() {
      return this.L() ? "Configured range active" : "Waiting for player or weapon condition";
   }

   @Override
   public void onTick() {
      int var10000 = this.L();
      boolean var11 = false;
      byte var10 = (byte)var10000;
      var10000 = Integer.signum(var10);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.onDisable();
      } else {
         double var1 = this.f.getValue();
         int var18 = this.d.O("Dynamic");
         boolean var13 = false;
         byte var12 = (byte)var18;
         var18 = Integer.signum(var12);
         if (((var18 | -var18) >>> 31 ^ 1) == 0) {
            double var3 = var1;

            for (Entity var6 : mc.level.entitiesForRendering()) {
               if (var6 != mc.player && var6.isAlive() && var6.isPickable()) {
                  var3 = Math.min(var3, ta.M(var6) + 0.5);
               }
            }

            var1 = Math.max(3.0, var3);
         }

         this.O.y(mc.player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE), var1);
         this.a.y(mc.player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE), this.E.getValue());
         IntegratedServer var14 = mc.getSingleplayerServer();
         if (var14 == null) {
            this.S();
         } else {
            if (this.X == null || this.X.H != var14) {
               this.S();
               this.X = new ReachModule.o(var14);
            }

            ReachModule.o var4 = this.X;
            UUID var15 = mc.player.getUUID();
            double var16 = var1;
            double var8 = this.E.getValue();
            var14.execute(() -> {
               ServerPlayer var7 = var14.getPlayerList().getPlayer(var15);
               if (var4.e && var7 != null && var7.isAlive()) {
                  var4.S.y(var7.getAttribute(Attributes.ENTITY_INTERACTION_RANGE), var16);
                  var4.J.y(var7.getAttribute(Attributes.BLOCK_INTERACTION_RANGE), var8);
               } else {
                  var4.f();
               }
            });
         }
      }
   }

   @Override
   public void onDisable() {
      this.O.v();
      this.a.v();
      this.S();
   }

   private void S() {
      ReachModule.o var1 = this.X;
      this.X = null;
      if (var1 != null) {
         var1.e = (boolean)0;
         var1.H.execute(var1::f);
      }
   }

   public void D(AttributeInstance var1) {
      this.O.u(var1);
      this.a.u(var1);
   }

   public double X() {
      return getMc().player == null ? 3.0 : getMc().player.entityInteractionRange();
   }

   public double E() {
      return this.X();
   }

   public double p() {
      return this.X();
   }

   public double m() {
      return getMc().player == null ? 4.5 : getMc().player.blockInteractionRange();
   }

   public boolean V(Entity var1) {
      return ta.q(var1, this.X());
   }

   private static final class o {
      final MinecraftServer H;
      final t9 S = new t9();
      final t9 J = new t9();
      volatile boolean e = (boolean)1;

      o(MinecraftServer var1) {
         this.H = var1;
      }

      void f() {
         this.S.v();
         this.J.v();
      }
   }
}
