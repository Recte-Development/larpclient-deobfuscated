package com.larpclient.module;

import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.jz;
import com.larpclient.qg;
import com.larpclient.sw;
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
   private static CrystalTapModule o;
   private final com.larpclient.module.b Y = this.addSetting(
      new com.larpclient.module.b("Delay", "Extra ticks to wait once the crystal appears; 0 hits it on the first tick it exists", 0.0, 0.0, 10.0, 1.0)
   );
   private final com.larpclient.module.b y = this.addSetting(
      new com.larpclient.module.b("Random Delay", "Adds up to this many more ticks at random to each hit", 0.0, 0.0, 5.0, 1.0)
   );
   private final com.larpclient.module.b P = this.addSetting(
      new com.larpclient.module.b("Hit Window", "Ticks your crosshair has to reach a new crystal before it is left alone", 8.0, 1.0, 40.0, 1.0)
   );
   private final com.larpclient.module.b a = this.addSetting(
      new com.larpclient.module.b("Range", "Hit reach in blocks, capped by your real reach", 3.0, 1.0, 6.0, 0.1)
   );
   private final m c = this.addSetting(new m("Anti Suicide", "Leave a crystal alone when its blast would kill you", true));
   private final sw G = new sw();
   private LocalPlayer R;
   private ClientLevel E;
   private BlockPos x;
   private boolean F;
   private String A = "Place a crystal";
   private String s = "";

   public CrystalTapModule() {
      super("Crystal Tap", "Hit a crystal the moment the one you placed appears", Category.COMBAT, -1);
      o = this;
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.A : "";
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F, l.k);
   }

   public static void X(LocalPlayer var0, InteractionHand var1, BlockHitResult var2) {
      if (o != null && o.isEnabled()) {
         CrystalTapModule var3 = o;
         if (var3 != null) {
            var3.x = null;
            if (var3.isRuntimeActive() && var0 == mc.player && !_p.I() && var0.getItemInHand(var1).is(Items.END_CRYSTAL)) {
               BlockState var4 = mc.level.getBlockState(var2.getBlockPos());
               if (var4.is(Blocks.OBSIDIAN) || var4.is(Blocks.BEDROCK)) {
                  var3.x = var2.getBlockPos().immutable();
               }
            }
         }
      }
   }

   public static void j(InteractionResult var0) {
      if (o != null && o.isEnabled()) {
         CrystalTapModule var1 = o;
         if (var1 != null && var1.x != null) {
            BlockPos var2 = var1.x;
            var1.x = null;
            if (var1.isRuntimeActive() && var0.consumesAction()) {
               int var3 = ThreadLocalRandom.current().nextInt(var1.y.getValue().intValue() + 1);
               var1.G.C(var2, mc.player.tickCount, var1.Y.getValue().intValue() + var3);
               var1.Q("Waiting for the crystal");
            }
         }
      }
   }

   public static boolean X() {
      if (o != null && o.isEnabled()) {
         CrystalTapModule var0 = o;
         return var0 != null && var0.isRuntimeActive() && !var0.G.z() && var0.R == mc.player && var0.E == mc.level && var0.h() != null;
      } else {
         return false;
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.e();
      } else {
         if (this.R != mc.player || this.E != mc.level) {
            this.e();
            this.R = mc.player;
            this.E = mc.level;
         }

         boolean var1 = mc.gameMode.isDestroying();
         int var10000 = this.G.z();
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         CrystalTapModule.b var2 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? null : this.h();
         this.F = var1;
         if (var2 == null) {
            int var10 = this.G.z();
            boolean var6 = false;
            byte var5 = (byte)var10;
            var10 = Integer.signum(var5);
            if (((var10 | -var10) >>> 31 ^ 1) == 0) {
               this.Q("Place a crystal");
            }
         } else {
            double var3 = _p.M(this.a.getValue());
            _p.U(this, var2.crystal(), var3, false, () -> this.o(var2.crystal()) == null, 1, () -> this.C(var2), var1x -> this.Q("Waiting: " + var1x));
         }
      }
   }

   private void C(CrystalTapModule.b var1) {
      long var2 = this.G.Z(var1.base(), mc.player.tickCount);
      this.G.e(var1.base());
      this.Q("Hit");
      _q.c("CrystalTap", "Hit crystal {} on {}, {} ticks after placing it", var1.crystal().getId(), var1.base().toShortString(), var2);
   }

   private CrystalTapModule.b h() {
      long var1 = mc.player.tickCount;
      int var3 = this.P.getValue().intValue();
      String var4 = null;

      for (BlockPos var6 : this.G.z()) {
         EndCrystal var7 = this.Y(var6);
         if (this.G.f(var6, var7 != null, var1, var3) == sw.k.R) {
            String var8 = this.o(var7);
            if (var8 == null) {
               return new CrystalTapModule.b(var6, var7);
            }

            if (var4 == null) {
               var4 = var8;
            }
         }
      }

      if (var4 != null) {
         this.Q(var4);
      } else if (!this.G.z()) {
         this.Q("Waiting for the crystal");
      }

      return null;
   }

   private EndCrystal Y(BlockPos var1) {
      BlockPos var2 = var1.above();
      List var3 = mc.level.getEntitiesOfClass(EndCrystal.class, new AABB(var2), var1x -> !var1x.isRemoved() && var1x.blockPosition().equals(var2));
      return var3.isEmpty() ? null : (EndCrystal)var3.getFirst();
   }

   private String o(EndCrystal var1) {
      LocalPlayer var2 = mc.player;
      if (!qg.U(mc)) {
         return "Paused";
      } else if (!qg.g(mc, var2) || this.F) {
         return "Your hands are busy";
      } else if (qg.Y(var2)) {
         return "You already acted this tick";
      } else {
         ItemStack var3 = var2.getMainHandItem();
         if (var3.has(DataComponents.PIERCING_WEAPON) || !var3.isItemEnabled(mc.level.enabledFeatures()) || var2.cannotAttackWithItem(var3, 0)) {
            return "This item cannot hit";
         } else if (var1.isInvulnerable()) {
            return "This crystal cannot be broken";
         } else if (!CrystalOptimizerModule.B(var2)) {
            return "Your hit deals no damage";
         } else {
            double var4 = _p.M(this.a.getValue());
            if (jz.D(var2.getEyePosition(), var2.getLookAngle(), var1.getBoundingBox(), var4).isEmpty()) {
               return "Aim at the crystal";
            } else {
               String var6 = _p.e(var1, var4, var2.getLookAngle());
               if (var6 != null) {
                  return "Blocked by " + var6;
               } else {
                  return this.c.getValue()
                        && !com.larpclient.t.x(com.larpclient.t.T(var2, var1.position()), Double.MAX_VALUE, var2.getHealth() + var2.getAbsorptionAmount())
                     ? "This crystal would kill you"
                     : null;
               }
            }
         }
      }
   }

   private void Q(String var1) {
      this.A = var1;
      if (!var1.equals(this.s)) {
         this.s = var1;
         _q.c("CrystalTap", "Status: {}", var1);
      }
   }

   private void e() {
      this.G.i();
      _p.e(this);
      this.x = null;
      this.R = null;
      this.E = null;
      this.F = (boolean)0;
      this.A = "Place a crystal";
   }

   @Override
   public void onEnable() {
      this.e();
      _q.z("CrystalTap", "Enabled; hits your own placed crystals with the native attack on your real look");
   }

   @Override
   public void onDisable() {
      this.e();
      _q.c("CrystalTap", "Disabled; tracked placements cleared");
   }

   private record b(BlockPos base, EndCrystal crystal) {
   }
}
