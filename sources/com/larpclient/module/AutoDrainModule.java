package com.larpclient.module;

import com.larp.obf.qbac0b262827a15ac.q099a584e4baa1bb9;
import com.larp.obf.qbac0b262827a15ac.qb131e08b200c8a57;
import com.larpclient._8;
import com.larpclient._d;
import com.larpclient._m;
import com.larpclient.m9;
import com.larpclient.mt;
import com.larpclient.ti;
import com.larpclient.tq;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AutoDrainModule extends Module {
   public static final long P = 120000L;
   public static final int R = 2;
   public static final long k = 60000L;
   private static final double i = 0.4;
   private static final int n = 512;
   private static final Map<BlockPos, Long> J = new HashMap<>();
   private final d9 K = this.addSetting(new d9("Range", "Furthest water to scoop, capped by your reach", 4.5, 2.0, 6.0, 0.5));
   private final d9 T = this.addSetting(new d9("Delay", "Ticks to wait between scoops", 2.0, 0.0, 20.0, 1.0));
   private final dh v = this.addSetting(new dh("Waterlogged", "Also drain water inside stairs, slabs and other waterlogged blocks", false));
   private final dh x = this.addSetting(new dh("Ignore My Water", "Leave water you emptied yourself in the last two minutes", true));
   private final ti y = new ti(this);
   private final Map<BlockPos, int[]> f = new HashMap<>();
   private final Map<BlockPos, Long> H = new HashMap<>();
   private LocalPlayer N;
   private ClientLevel G;
   private BlockPos D;
   private Vec3 E;
   private boolean q;
   private float p;
   private float W;
   private int l;
   private int F;
   private String I = "Ready";

   public AutoDrainModule() {
      super("Auto Drain", "Scoop up nearby still water with empty buckets", Category.PLAYER, -1);
   }

   public static int x(double var0, int var2, double var3, int var5) {
      int var6 = Double.compare(var3, var0);
      return var6 != 0 ? var6 : Integer.compare(var5, var2);
   }

   public static boolean F(int var0, boolean var1) {
      return var0 == 1 || var1;
   }

   public static boolean y(int var0) {
      return var0 >= 2;
   }

   public static void m(Player var0, InteractionHand var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var0 == var2.player && var2.level != null && var0.getItemInHand(var1).is(Items.WATER_BUCKET)) {
         Vec3 var3 = var0.getEyePosition();
         Vec3 var4 = var3.add(var0.getLookAngle().scale(var0.blockInteractionRange()));
         BlockHitResult var5 = var2.level.clip(new ClipContext(var3, var4, Block.OUTLINE, Fluid.NONE, var0));
         if (var5.getType() == Type.BLOCK) {
            long var6 = System.currentTimeMillis();
            if (J.size() >= 512) {
               J.clear();
            }

            J.put(var5.getBlockPos().immutable(), var6);
            J.put(var5.getBlockPos().relative(var5.getDirection()).immutable(), var6);
         }
      }
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z, dm.j);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.I : "";
   }

   @Override
   public void onEnable() {
      this.U();
      this.N = mc.player;
      this.G = mc.level;
      this.I = "Ready";
      mt.K("AutoDrain", "Enabled; range {}, delay {} ticks, waterlogged={}, ignore own={}", this.K.P(), this.T.P().intValue(), this.v.P(), this.x.P());
   }

   @Override
   public void onDisable() {
      if (this.q && this.N != null && this.N == getMc().player) {
         this.y.W(mc, true);
         this.N.setYRot(this.p);
         this.N.setXRot(this.W);
      }

      this.U();
      this.N = null;
      this.G = null;
   }

   private void U() {
      _m.w(this);
      this.q = (boolean)0;
      this.D = null;
      this.E = null;
      this.l = 0;
      this.F = 0;
      this.f.clear();
      this.H.clear();
   }

   private boolean C() {
      tq.y var1 = tq.l();
      return !_m.z() || mc.gui.screen() != null || var1 != tq.y.A;
   }

   @Override
   public void onTick() {
      if (this.N != mc.player || this.G != mc.level) {
         this.y.W(getMc(), false);
         this.U();
         J.clear();
         this.N = mc.player;
         this.G = mc.level;
      }

      if (this.N != null && this.G != null) {
         if (this.C()) {
            if (this.q) {
               this.I = "Paused";
            }
         } else {
            this.J(System.currentTimeMillis());
            if (this.l > 0) {
               byte var9 = 1;
               int var8 = this.l;
               this.l = var8 - var9;
            } else {
               int var1 = _d.B(Items.BUCKET, true);
               byte var5 = -1;
               int var4 = var1;
               int var10000 = Integer.compare(var4, var5);
               if ((var10000 | -var10000) >>> 31 == 0) {
                  this.I("No empty buckets");
               } else {
                  byte var7 = -2;
                  int var6 = var1;
                  var10000 = Integer.compare(var6, var7);
                  ItemStack var2 = (var10000 | -var10000) >>> 31 == 0 ? this.N.getOffhandItem() : this.N.getInventory().getItem(var1);
                  var10000 = var2.getCount();
                  int var10001 = this.N.getInventory().getFreeSlot();
                  byte var11 = -1;
                  int var10 = var10001;
                  var10001 = Integer.compare(var10, var11);
                  if (!F(var10000, ((var10001 | -var10001) >>> 31 ^ 1) == 0)) {
                     this.I("Inventory full");
                  } else if (!this.e()) {
                     this.I(this.F > 0 ? "Drained " + this.F : "No water in reach");
                  } else {
                     if (!this.q) {
                        this.q = (boolean)1;
                        this.F = 0;
                        this.p = this.N.getYRot();
                        this.W = this.N.getXRot();
                        mt.U("AutoDrain", "Run started at {}", this.D);
                     }

                     this.I = "Draining";
                     float[] var3 = m9.Z(this.N.getEyePosition(), this.E);
                     m9.k(this, m9.s.F, var3[0], var3[1], 1.0);
                     _m.F(this, this::u, this::L);
                  }
               }
            }
         }
      }
   }

   private boolean e() {
      double var1 = Math.min(this.K.P(), this.N.blockInteractionRange());
      Vec3 var3 = this.N.getEyePosition();
      int var4 = (int)Math.ceil(var1);
      BlockPos var5 = BlockPos.containing(var3);
      long var6 = System.currentTimeMillis();
      BlockPos var8 = null;
      Vec3 var9 = null;
      double var10 = 0.0;

      for (BlockPos var13 : BlockPos.betweenClosed(var5.offset(-var4, -var4, -var4), var5.offset(var4, var4, var4))) {
         if (this.N(var13, var6)) {
            Vec3 var14 = Vec3.atBottomCenterOf(var13).add(0.0, 0.4, 0.0);
            double var15 = var3.distanceTo(var14);
            if (!(var15 > var1) && (var8 == null || x(var15, var13.getY(), var10, var8.getY()) < 0) && this.s(var13, var3, var14.subtract(var3), var1)) {
               var8 = var13.immutable();
               var9 = var14;
               var10 = var15;
            }
         }
      }

      this.D = var8;
      this.E = var9;
      return var8 != null;
   }

   private boolean N(BlockPos var1, long var2) {
      if (!this.G.getFluidState(var1).isSourceOfType(Fluids.WATER)) {
         return false;
      }

      BlockState var4 = this.G.getBlockState(var1);
      if (!(var4.getBlock() instanceof BucketPickup)) {
         return false;
      }

      if (!(var4.getBlock() instanceof LiquidBlock) && !this.v.P()) {
         return false;
      }

      if (this.N.getBoundingBox().intersects(new AABB(var1))) {
         return false;
      }

      if (this.x.P()) {
         Long var5 = J.get(var1);
         if (var5 != null && var2 - var5 < 120000L) {
            return false;
         }
      }

      int[] var6 = this.f.get(var1);
      return var6 == null || !y(var6[0]);
   }

   private boolean s(BlockPos var1, Vec3 var2, Vec3 var3, double var4) {
      Vec3 var6 = var2.add(var3.normalize().scale(var4));
      BlockHitResult var7 = this.G.clip(new ClipContext(var2, var6, Block.OUTLINE, Fluid.SOURCE_ONLY, this.N));
      return var7.getType() == Type.BLOCK && var7.getBlockPos().equals(var1);
   }

   private boolean L() {
      if (this.q && this.D != null && !this.N.isUsingItem() && this.a()) {
         if (!this.G.getFluidState(this.D).isSourceOfType(Fluids.WATER)) {
            return false;
         }

         double var1 = Math.min(this.K.P(), this.N.blockInteractionRange());
         return this.s(this.D, this.N.getEyePosition(), this.N.getLookAngle(), var1);
      } else {
         return false;
      }
   }

   private boolean a() {
      int var1 = _d.B(Items.BUCKET, true);
      return var1 == -2 || var1 >= 0 && (var1 == this.N.getInventory().getSelectedSlot() || ti.V(this));
   }

   private InteractionHand O() {
      int var1 = _d.B(Items.BUCKET, true);
      if (var1 == -2) {
         return InteractionHand.OFF_HAND;
      } else if (var1 < 0) {
         return null;
      } else {
         return var1 != this.N.getInventory().getSelectedSlot() && !this.y.e(this.N, var1) ? null : InteractionHand.MAIN_HAND;
      }
   }

   private void u() {
      if (this.L()) {
         InteractionHand var1 = this.O();
         if (var1 != null) {
            BlockPos var2 = this.D;
            boolean var3 = mc.gameMode.useItem(this.N, var1).consumesAction();
            this.l = this.T.P().intValue();
            if (!var3) {
               this.I = "Scoop rejected";
               mt.U("AutoDrain", "Scoop at {} rejected locally", var2);
            } else {
               _8.G(this.N, var1);
               ((qb131e08b200c8a57)mc).larpUseDelay(4);
               long var4 = System.currentTimeMillis();
               int[] var6 = this.f.computeIfAbsent(var2, var0 -> new int[1]);
               var6[0]++;
               this.H.put(var2, var4);
               this.F++;
               if (y(var6[0])) {
                  mt.U("AutoDrain", "{} refills; skipped for {} s", var2, 60L);
               }

               mt.U("AutoDrain", "Scooped {} ({} this run)", var2, this.F);
            }
         }
      }
   }

   private void I(String var1) {
      this.I = var1;
      if (this.q) {
         int var2 = this.N.getInventory().getSelectedSlot();
         this.y.W(mc, true);
         if (mc.gameMode != null && var2 != this.N.getInventory().getSelectedSlot()) {
            ((q099a584e4baa1bb9)mc.gameMode).larpSyncCarriedItem();
         }

         m9.k(this, m9.s.F, this.p, this.W, 1.0);
         mt.U("AutoDrain", "Run ended after {} scoops: {}", this.F, var1);
         this.q = (boolean)0;
         this.D = null;
         this.E = null;
      }
   }

   private void J(long var1) {
      J.values().removeIf(var2 -> var1 - var2 >= 120000L);
      this.H.entrySet().removeIf(var3 -> {
         int var4 = var1 - var3.getValue() >= 60000L ? 1 : 0;
         if (var4 != 0) {
            this.f.remove(var3.getKey());
         }

         return (boolean)var4;
      });
      if (this.f.size() > 512) {
         this.f.clear();
         this.H.clear();
      }
   }
}
