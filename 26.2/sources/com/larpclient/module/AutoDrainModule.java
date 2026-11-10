package com.larpclient.module;

import com.larp.obf.qa300483f54d804a3.q541491ff38fb1637;
import com.larp.obf.qa300483f54d804a3.q7ae1dbb86ec789d8;
import com.larpclient.hu;
import com.larpclient.j6;
import com.larpclient.ks;
import com.larpclient.p_;
import com.larpclient.t1;
import com.larpclient.ta;
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
   public static final long T = 120000L;
   public static final int E = 2;
   public static final long u = 60000L;
   private static final double N = 0.4;
   private static final int z = 512;
   private static final Map<BlockPos, Long> B = new HashMap<>();
   private final e f = this.addSetting(new e("Range", "Furthest water to scoop, capped by your reach", 4.5, 2.0, 6.0, 0.5));
   private final e C = this.addSetting(new e("Delay", "Ticks to wait between scoops", 2.0, 0.0, 20.0, 1.0));
   private final s y = this.addSetting(new s("Waterlogged", "Also drain water inside stairs, slabs and other waterlogged blocks", false));
   private final s g = this.addSetting(new s("Ignore My Water", "Leave water you emptied yourself in the last two minutes", true));
   private final hu v = new hu(this);
   private final Map<BlockPos, int[]> p = new HashMap<>();
   private final Map<BlockPos, Long> t = new HashMap<>();
   private LocalPlayer m;
   private ClientLevel k;
   private BlockPos s;
   private Vec3 d;
   private boolean W;
   private float F;
   private float n;
   private int o;
   private int Y;
   private String r = "Ready";

   public AutoDrainModule() {
      super("Auto Drain", "Scoop up nearby still water with empty buckets", Category.PLAYER, -1);
   }

   public static int B(double var0, int var2, double var3, int var5) {
      int var6 = Double.compare(var3, var0);
      return var6 != 0 ? var6 : Integer.compare(var5, var2);
   }

   public static boolean m(int var0, boolean var1) {
      return var0 == 1 || var1;
   }

   public static boolean v(int var0) {
      return var0 >= 2;
   }

   public static void k(Player var0, InteractionHand var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var0 == var2.player && var2.level != null && var0.getItemInHand(var1).is(Items.WATER_BUCKET)) {
         Vec3 var3 = var0.getEyePosition();
         Vec3 var4 = var3.add(var0.getLookAngle().scale(var0.blockInteractionRange()));
         BlockHitResult var5 = var2.level.clip(new ClipContext(var3, var4, Block.OUTLINE, Fluid.NONE, var0));
         if (var5.getType() == Type.BLOCK) {
            long var6 = System.currentTimeMillis();
            if (B.size() >= 512) {
               B.clear();
            }

            B.put(var5.getBlockPos().immutable(), var6);
            B.put(var5.getBlockPos().relative(var5.getDirection()).immutable(), var6);
         }
      }
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.K, com.larpclient.module.m.U);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.r : "";
   }

   @Override
   public void onEnable() {
      this.b();
      this.m = mc.player;
      this.k = mc.level;
      this.r = "Ready";
      com.larpclient.h.A(
         "AutoDrain",
         "Enabled; range {}, delay {} ticks, waterlogged={}, ignore own={}",
         this.f.getValue(),
         this.C.getValue().intValue(),
         this.y.getValue(),
         this.g.getValue()
      );
   }

   @Override
   public void onDisable() {
      if (this.W && this.m != null && this.m == getMc().player) {
         this.v.V(mc, true);
         this.m.setYRot(this.F);
         this.m.setXRot(this.n);
      }

      this.b();
      this.m = null;
      this.k = null;
   }

   private void b() {
      ta.s(this);
      this.W = (boolean)0;
      this.s = null;
      this.d = null;
      this.o = 0;
      this.Y = 0;
      this.p.clear();
      this.t.clear();
   }

   private boolean O() {
      p_.o var1 = p_.A();
      return !ta.Y() || mc.gui.screen() != null || var1 != p_.o.T;
   }

   @Override
   public void onTick() {
      if (this.m != mc.player || this.k != mc.level) {
         this.v.V(getMc(), false);
         this.b();
         B.clear();
         this.m = mc.player;
         this.k = mc.level;
      }

      if (this.m != null && this.k != null) {
         if (this.O()) {
            if (this.W) {
               this.r = "Paused";
            }
         } else {
            this.J(System.currentTimeMillis());
            if (this.o > 0) {
               byte var7 = 1;
               int var6 = this.o;
               this.o = var6 - var7;
            } else {
               int var1 = t1.M(Items.BUCKET, true);
               byte var9 = -1;
               int var8 = var1;
               int var10000 = Integer.compare(var8, var9);
               if ((var10000 | -var10000) >>> 31 == 0) {
                  this.l("No empty buckets");
               } else {
                  byte var5 = -2;
                  int var4 = var1;
                  var10000 = Integer.compare(var4, var5);
                  ItemStack var2 = (var10000 | -var10000) >>> 31 == 0 ? this.m.getOffhandItem() : this.m.getInventory().getItem(var1);
                  var10000 = var2.getCount();
                  int var10001 = this.m.getInventory().getFreeSlot();
                  byte var11 = -1;
                  int var10 = var10001;
                  var10001 = Integer.compare(var10, var11);
                  if (!m(var10000, ((var10001 | -var10001) >>> 31 ^ 1) == 0)) {
                     this.l("Inventory full");
                  } else if (!this.j()) {
                     this.l(this.Y > 0 ? "Drained " + this.Y : "No water in reach");
                  } else {
                     if (!this.W) {
                        this.W = (boolean)1;
                        this.Y = 0;
                        this.F = this.m.getYRot();
                        this.n = this.m.getXRot();
                        com.larpclient.h.U("AutoDrain", "Run started at {}", this.s);
                     }

                     this.r = "Draining";
                     float[] var3 = j6.s(this.m.getEyePosition(), this.d);
                     j6.U(this, j6.l.E, var3[0], var3[1], 1.0);
                     ta.F(this, this::t, this::v);
                  }
               }
            }
         }
      }
   }

   private boolean j() {
      double var1 = Math.min(this.f.getValue(), this.m.blockInteractionRange());
      Vec3 var3 = this.m.getEyePosition();
      int var4 = (int)Math.ceil(var1);
      BlockPos var5 = BlockPos.containing(var3);
      long var6 = System.currentTimeMillis();
      BlockPos var8 = null;
      Vec3 var9 = null;
      double var10 = 0.0;

      for (BlockPos var13 : BlockPos.betweenClosed(var5.offset(-var4, -var4, -var4), var5.offset(var4, var4, var4))) {
         if (this.G(var13, var6)) {
            Vec3 var14 = Vec3.atBottomCenterOf(var13).add(0.0, 0.4, 0.0);
            double var15 = var3.distanceTo(var14);
            if (!(var15 > var1) && (var8 == null || B(var15, var13.getY(), var10, var8.getY()) < 0) && this.X(var13, var3, var14.subtract(var3), var1)) {
               var8 = var13.immutable();
               var9 = var14;
               var10 = var15;
            }
         }
      }

      this.s = var8;
      this.d = var9;
      return var8 != null;
   }

   private boolean G(BlockPos var1, long var2) {
      if (!this.k.getFluidState(var1).isSourceOfType(Fluids.WATER)) {
         return false;
      }

      BlockState var4 = this.k.getBlockState(var1);
      if (!(var4.getBlock() instanceof BucketPickup)) {
         return false;
      }

      if (!(var4.getBlock() instanceof LiquidBlock) && !this.y.getValue()) {
         return false;
      }

      if (this.m.getBoundingBox().intersects(new AABB(var1))) {
         return false;
      }

      if (this.g.getValue()) {
         Long var5 = B.get(var1);
         if (var5 != null && var2 - var5 < 120000L) {
            return false;
         }
      }

      int[] var6 = this.p.get(var1);
      return var6 == null || !v(var6[0]);
   }

   private boolean X(BlockPos var1, Vec3 var2, Vec3 var3, double var4) {
      Vec3 var6 = var2.add(var3.normalize().scale(var4));
      BlockHitResult var7 = this.k.clip(new ClipContext(var2, var6, Block.OUTLINE, Fluid.SOURCE_ONLY, this.m));
      return var7.getType() == Type.BLOCK && var7.getBlockPos().equals(var1);
   }

   private boolean v() {
      if (this.W && this.s != null && !this.m.isUsingItem() && this.t()) {
         if (!this.k.getFluidState(this.s).isSourceOfType(Fluids.WATER)) {
            return false;
         }

         double var1 = Math.min(this.f.getValue(), this.m.blockInteractionRange());
         return this.X(this.s, this.m.getEyePosition(), this.m.getLookAngle(), var1);
      } else {
         return false;
      }
   }

   private boolean t() {
      int var1 = t1.M(Items.BUCKET, true);
      return var1 == -2 || var1 >= 0 && (var1 == this.m.getInventory().getSelectedSlot() || hu.k(this));
   }

   private InteractionHand o() {
      int var1 = t1.M(Items.BUCKET, true);
      if (var1 == -2) {
         return InteractionHand.OFF_HAND;
      } else if (var1 < 0) {
         return null;
      } else {
         return var1 != this.m.getInventory().getSelectedSlot() && !this.v.O(this.m, var1) ? null : InteractionHand.MAIN_HAND;
      }
   }

   private void t() {
      if (this.v()) {
         InteractionHand var1 = this.o();
         if (var1 != null) {
            BlockPos var2 = this.s;
            boolean var3 = mc.gameMode.useItem(this.m, var1).consumesAction();
            this.o = this.C.getValue().intValue();
            if (!var3) {
               this.r = "Scoop rejected";
               com.larpclient.h.U("AutoDrain", "Scoop at {} rejected locally", var2);
            } else {
               ks.p(this.m, var1);
               ((q7ae1dbb86ec789d8)mc).larpUseDelay(4);
               long var4 = System.currentTimeMillis();
               int[] var6 = this.p.computeIfAbsent(var2, var0 -> new int[1]);
               var6[0]++;
               this.t.put(var2, var4);
               this.Y++;
               if (v(var6[0])) {
                  com.larpclient.h.U("AutoDrain", "{} refills; skipped for {} s", var2, 60L);
               }

               com.larpclient.h.U("AutoDrain", "Scooped {} ({} this run)", var2, this.Y);
            }
         }
      }
   }

   private void l(String var1) {
      this.r = var1;
      if (this.W) {
         int var2 = this.m.getInventory().getSelectedSlot();
         this.v.V(mc, true);
         if (mc.gameMode != null && var2 != this.m.getInventory().getSelectedSlot()) {
            ((q541491ff38fb1637)mc.gameMode).larpSyncCarriedItem();
         }

         j6.U(this, j6.l.E, this.F, this.n, 1.0);
         com.larpclient.h.U("AutoDrain", "Run ended after {} scoops: {}", this.Y, var1);
         this.W = (boolean)0;
         this.s = null;
         this.d = null;
      }
   }

   private void J(long var1) {
      B.values().removeIf(var2 -> var1 - var2 >= 120000L);
      this.t.entrySet().removeIf(var3 -> {
         int var4 = var1 - var3.getValue() >= 60000L ? 1 : 0;
         if (var4 != 0) {
            this.p.remove(var3.getKey());
         }

         return (boolean)var4;
      });
      if (this.p.size() > 512) {
         this.p.clear();
         this.t.clear();
      }
   }
}
