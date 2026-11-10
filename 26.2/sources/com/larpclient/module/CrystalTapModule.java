package com.larpclient.module;

import com.larpclient.si;
import com.larpclient.ta;
import com.larpclient.wi;
import com.larpclient.zr;
import com.larpclient.zu;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

public final class CrystalTapModule extends Module {
   private static CrystalTapModule m;
   private final e W = this.addSetting(
      new e("Delay", "Extra ticks to wait once the crystal appears; 0 hits it on the first tick it exists", 0.0, 0.0, 10.0, 1.0)
   );
   private final e f = this.addSetting(new e("Random Delay", "Adds up to this many more ticks at random to each hit", 0.0, 0.0, 5.0, 1.0));
   private final e x = this.addSetting(new e("Hit Window", "Ticks your crosshair has to reach a new crystal before it is left alone", 8.0, 1.0, 40.0, 1.0));
   private final e s = this.addSetting(new e("Range", "Hit reach in blocks, capped by your real reach", 3.0, 1.0, 6.0, 0.1));
   private final s E = this.addSetting(new s("Anti Suicide", "Leave a crystal alone when its blast would kill you", true));
   private final zr L = new zr();
   private LocalPlayer o;
   private ClientLevel O;
   private BlockPos I;
   private boolean V;
   private String X = "Place a crystal";
   private String Q = "";

   public CrystalTapModule() {
      super("Crystal Tap", "Hit a crystal the moment the one you placed appears", Category.COMBAT, -1);
      m = this;
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.X : "";
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.K, com.larpclient.module.m.U);
   }

   public static void N(LocalPlayer var0, InteractionHand var1, BlockHitResult var2) {
      if (m != null && m.isEnabled()) {
         CrystalTapModule var3 = m;
         if (var3 != null) {
            var3.I = null;
            if (var3.isRuntimeActive() && var0 == mc.player && !ta.d() && var0.getItemInHand(var1).is(Items.END_CRYSTAL)) {
               BlockState var4 = mc.level.getBlockState(var2.getBlockPos());
               if (var4.is(Blocks.OBSIDIAN) || var4.is(Blocks.BEDROCK)) {
                  var3.I = var2.getBlockPos().immutable();
               }
            }
         }
      }
   }

   public static void O(InteractionResult var0) {
      if (m != null && m.isEnabled()) {
         CrystalTapModule var1 = m;
         if (var1 != null && var1.I != null) {
            BlockPos var2 = var1.I;
            var1.I = null;
            if (var1.isRuntimeActive() && var0.consumesAction()) {
               int var3 = ThreadLocalRandom.current().nextInt(var1.f.getValue().intValue() + 1);
               var1.L.K(var2, mc.player.tickCount, var1.W.getValue().intValue() + var3);
               var1.j("Waiting for the crystal");
            }
         }
      }
   }

   public static boolean L() {
      if (m != null && m.isEnabled()) {
         CrystalTapModule var0 = m;
         return var0 != null && var0.isRuntimeActive() && !var0.L.v() && var0.o == mc.player && var0.O == mc.level && var0.m() != null;
      } else {
         return false;
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.G();
      } else {
         if (this.o != mc.player || this.O != mc.level) {
            this.G();
            this.o = mc.player;
            this.O = mc.level;
         }

         boolean var1 = mc.gameMode.isDestroying();
         int var10000 = this.L.v();
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         CrystalTapModule.f var2 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? null : this.m();
         this.V = var1;
         if (var2 == null) {
            int var10 = this.L.v();
            boolean var6 = false;
            byte var5 = (byte)var10;
            var10 = Integer.signum(var5);
            if (((var10 | -var10) >>> 31 ^ 1) == 0) {
               this.j("Place a crystal");
            }
         } else {
            double var3 = ta.c(this.s.getValue());
            ta.M(this, var2.crystal(), var3, false, () -> this.M(var2.crystal()) == null, 1, () -> this.E(var2), var1x -> this.j("Waiting: " + var1x));
         }
      }
   }

   private void E(CrystalTapModule.f var1) {
      long var2 = this.L.g(var1.base(), mc.player.tickCount);
      this.L.c(var1.base());
      this.j("Hit");
      com.larpclient.h.U("CrystalTap", "Hit crystal {} on {}, {} ticks after placing it", var1.crystal().getId(), var1.base().toShortString(), var2);
   }

   private CrystalTapModule.f m() {
      long var1 = mc.player.tickCount;
      int var3 = this.x.getValue().intValue();
      String var4 = null;

      for (BlockPos var6 : this.L.D()) {
         EndCrystal var7 = this.e(var6);
         if (this.L.h(var6, var7 != null, var1, var3) == zr.u.g) {
            String var8 = this.M(var7);
            if (var8 == null) {
               return new CrystalTapModule.f(var6, var7);
            }

            if (var4 == null) {
               var4 = var8;
            }
         }
      }

      if (var4 != null) {
         this.j(var4);
      } else if (!this.L.v()) {
         this.j("Waiting for the crystal");
      }

      return null;
   }

   private EndCrystal e(BlockPos var1) {
      BlockPos var2 = var1.above();
      List var3 = mc.level.getEntitiesOfClass(EndCrystal.class, new AABB(var2), var1x -> !var1x.isRemoved() && var1x.blockPosition().equals(var2));
      return var3.isEmpty() ? null : (EndCrystal)var3.getFirst();
   }

   private String M(EndCrystal var1) {
      LocalPlayer var2 = mc.player;
      if (!si.P(mc)) {
         return "Paused";
      } else if (!si.M(mc, var2) || this.V) {
         return "Your hands are busy";
      } else if (si.o(var2)) {
         return "You already acted this tick";
      } else {
         ItemStack var3 = var2.getMainHandItem();
         if (var3.has(DataComponents.PIERCING_WEAPON) || !var3.isItemEnabled(mc.level.enabledFeatures()) || var2.cannotAttackWithItem(var3, 0)) {
            return "This item cannot hit";
         } else if (var1.isInvulnerable()) {
            return "This crystal cannot be broken";
         } else if (!CrystalOptimizerModule.W(var2)) {
            return "Your hit deals no damage";
         } else {
            double var4 = ta.c(this.s.getValue());
            if (wi.x(var2.getEyePosition(), var2.getLookAngle(), var1.getBoundingBox(), var4).isEmpty()) {
               return "Aim at the crystal";
            } else {
               String var6 = ta.I(var1, var4, var2.getLookAngle());
               if (var6 != null) {
                  return "Blocked by " + var6;
               } else {
                  return this.E.getValue() && !zu.P(zu.R(var2, var1.position()), Double.MAX_VALUE, var2.getHealth() + var2.getAbsorptionAmount())
                     ? "This crystal would kill you"
                     : null;
               }
            }
         }
      }
   }

   private void j(String var1) {
      this.X = var1;
      if (!var1.equals(this.Q)) {
         this.Q = var1;
         com.larpclient.h.U("CrystalTap", "Status: {}", var1);
      }
   }

   private void G() {
      this.L.Z();
      ta.s(this);
      this.I = null;
      this.o = null;
      this.O = null;
      this.V = (boolean)0;
      this.X = "Place a crystal";
   }

   @Override
   public void onEnable() {
      this.G();
      com.larpclient.h.A("CrystalTap", "Enabled; hits your own placed crystals with the native attack on your real look");
   }

   @Override
   public void onDisable() {
      this.G();
      com.larpclient.h.U("CrystalTap", "Disabled; tracked placements cleared");
   }

   private record f(BlockPos base, EndCrystal crystal) {
   }
}
