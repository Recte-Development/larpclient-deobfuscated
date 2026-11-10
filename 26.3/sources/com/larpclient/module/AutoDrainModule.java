package com.larpclient.module;

import com.larp.obf.q41a5851bcf8e876a.q9b8c1cd7fe41113a;
import com.larp.obf.q41a5851bcf8e876a.qc90ed4dd4cb15de4;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.b6;
import com.larpclient.h8;
import com.larpclient.ij;
import com.larpclient.s6;
import com.larpclient.vi;
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
   public static final long C = 120000L;
   public static final int w = 2;
   public static final long Q = 60000L;
   private static final double x = 0.4;
   private static final int F = 512;
   private static final Map<BlockPos, Long> Z = new HashMap<>();
   private final b D = this.addSetting(new b("Range", "Furthest water to scoop, capped by your reach", 4.5, 2.0, 6.0, 0.5));
   private final b m = this.addSetting(new b("Delay", "Ticks to wait between scoops", 2.0, 0.0, 20.0, 1.0));
   private final m G = this.addSetting(new m("Waterlogged", "Also drain water inside stairs, slabs and other waterlogged blocks", false));
   private final m r = this.addSetting(new m("Ignore My Water", "Leave water you emptied yourself in the last two minutes", true));
   private final s6 L = new s6(this);
   private final Map<BlockPos, int[]> h = new HashMap<>();
   private final Map<BlockPos, Long> N = new HashMap<>();
   private LocalPlayer t;
   private ClientLevel U;
   private BlockPos s;
   private Vec3 i;
   private boolean Y;
   private float I;
   private float W;
   private int z;
   private int q;
   private String J = "Ready";

   public AutoDrainModule() {
      super("Auto Drain", "Scoop up nearby still water with empty buckets", Category.PLAYER, -1);
   }

   public static int r(double var0, int var2, double var3, int var5) {
      int var6 = Double.compare(var3, var0);
      return var6 != 0 ? var6 : Integer.compare(var5, var2);
   }

   public static boolean O(int var0, boolean var1) {
      return var0 == 1 || var1;
   }

   public static boolean y(int var0) {
      return var0 >= 2;
   }

   public static void V(Player var0, InteractionHand var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var0 == var2.player && var2.level != null && var0.getItemInHand(var1).is(Items.WATER_BUCKET)) {
         Vec3 var3 = var0.getEyePosition();
         Vec3 var4 = var3.add(var0.getLookAngle().scale(var0.blockInteractionRange()));
         BlockHitResult var5 = var2.level.clip(new ClipContext(var3, var4, Block.OUTLINE, Fluid.NONE, var0));
         if (var5.getType() == Type.BLOCK) {
            long var6 = System.currentTimeMillis();
            if (Z.size() >= 512) {
               Z.clear();
            }

            Z.put(var5.getBlockPos().immutable(), var6);
            Z.put(var5.getBlockPos().relative(var5.getDirection()).immutable(), var6);
         }
      }
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F, l.k);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.J : "";
   }

   @Override
   public void onEnable() {
      this.X();
      this.t = mc.player;
      this.U = mc.level;
      this.J = "Ready";
      _q.z(
         "AutoDrain",
         "Enabled; range {}, delay {} ticks, waterlogged={}, ignore own={}",
         this.D.getValue(),
         this.m.getValue().intValue(),
         this.G.getValue(),
         this.r.getValue()
      );
   }

   @Override
   public void onDisable() {
      if (this.Y && this.t != null && this.t == getMc().player) {
         this.L.X(mc, true);
         this.t.setYRot(this.I);
         this.t.setXRot(this.W);
      }

      this.X();
      this.t = null;
      this.U = null;
   }

   private void X() {
      _p.e(this);
      this.Y = (boolean)0;
      this.s = null;
      this.i = null;
      this.z = 0;
      this.q = 0;
      this.h.clear();
      this.N.clear();
   }

   private boolean b() {
      ij.r var1 = ij.L();
      return !_p.g() || mc.gui.screen() != null || var1 != ij.r.X;
   }

   @Override
   public void onTick() {
      if (this.t != mc.player || this.U != mc.level) {
         this.L.X(getMc(), false);
         this.X();
         Z.clear();
         this.t = mc.player;
         this.U = mc.level;
      }

      if (this.t != null && this.U != null) {
         if (this.b()) {
            if (this.Y) {
               this.J = "Paused";
            }
         } else {
            this.a(System.currentTimeMillis());
            if (this.z > 0) {
               byte var7 = 1;
               int var6 = this.z;
               this.z = var6 - var7;
            } else {
               int var1 = vi.u(Items.BUCKET, true);
               byte var9 = -1;
               int var8 = var1;
               int var10000 = Integer.compare(var8, var9);
               if ((var10000 | -var10000) >>> 31 == 0) {
                  this.x("No empty buckets");
               } else {
                  byte var5 = -2;
                  int var4 = var1;
                  var10000 = Integer.compare(var4, var5);
                  ItemStack var2 = (var10000 | -var10000) >>> 31 == 0 ? this.t.getOffhandItem() : this.t.getInventory().getItem(var1);
                  var10000 = var2.getCount();
                  int var10001 = this.t.getInventory().getFreeSlot();
                  byte var11 = -1;
                  int var10 = var10001;
                  var10001 = Integer.compare(var10, var11);
                  if (!O(var10000, ((var10001 | -var10001) >>> 31 ^ 1) == 0)) {
                     this.x("Inventory full");
                  } else if (!this.e()) {
                     this.x(this.q > 0 ? "Drained " + this.q : "No water in reach");
                  } else {
                     if (!this.Y) {
                        this.Y = (boolean)1;
                        this.q = 0;
                        this.I = this.t.getYRot();
                        this.W = this.t.getXRot();
                        _q.c("AutoDrain", "Run started at {}", this.s);
                     }

                     this.J = "Draining";
                     float[] var3 = b6.R(this.t.getEyePosition(), this.i);
                     b6.P(this, b6.w.p, var3[0], var3[1], 1.0);
                     _p.w(this, this::m, this::t);
                  }
               }
            }
         }
      }
   }

   private boolean e() {
      double var1 = Math.min(this.D.getValue(), this.t.blockInteractionRange());
      Vec3 var3 = this.t.getEyePosition();
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
            if (!(var15 > var1) && (var8 == null || r(var15, var13.getY(), var10, var8.getY()) < 0) && this.S(var13, var3, var14.subtract(var3), var1)) {
               var8 = var13.immutable();
               var9 = var14;
               var10 = var15;
            }
         }
      }

      this.s = var8;
      this.i = var9;
      return var8 != null;
   }

   private boolean N(BlockPos var1, long var2) {
      if (!this.U.getFluidState(var1).isSourceOfType(Fluids.WATER)) {
         return false;
      }

      BlockState var4 = this.U.getBlockState(var1);
      if (!(var4.getBlock() instanceof BucketPickup)) {
         return false;
      }

      if (!(var4.getBlock() instanceof LiquidBlock) && !this.G.getValue()) {
         return false;
      }

      if (this.t.getBoundingBox().intersects(new AABB(var1))) {
         return false;
      }

      if (this.r.getValue()) {
         Long var5 = Z.get(var1);
         if (var5 != null && var2 - var5 < 120000L) {
            return false;
         }
      }

      int[] var6 = this.h.get(var1);
      return var6 == null || !y(var6[0]);
   }

   private boolean S(BlockPos var1, Vec3 var2, Vec3 var3, double var4) {
      Vec3 var6 = var2.add(var3.normalize().scale(var4));
      BlockHitResult var7 = this.U.clip(new ClipContext(var2, var6, Block.OUTLINE, Fluid.SOURCE_ONLY, this.t));
      return var7.getType() == Type.BLOCK && var7.getBlockPos().equals(var1);
   }

   private boolean t() {
      if (this.Y && this.s != null && !this.t.isUsingItem() && this.l()) {
         if (!this.U.getFluidState(this.s).isSourceOfType(Fluids.WATER)) {
            return false;
         }

         double var1 = Math.min(this.D.getValue(), this.t.blockInteractionRange());
         return this.S(this.s, this.t.getEyePosition(), this.t.getLookAngle(), var1);
      } else {
         return false;
      }
   }

   private boolean l() {
      int var1 = vi.u(Items.BUCKET, true);
      return var1 == -2 || var1 >= 0 && (var1 == this.t.getInventory().getSelectedSlot() || s6.L(this));
   }

   private InteractionHand f() {
      int var1 = vi.u(Items.BUCKET, true);
      if (var1 == -2) {
         return InteractionHand.OFF_HAND;
      } else if (var1 < 0) {
         return null;
      } else {
         return var1 != this.t.getInventory().getSelectedSlot() && !this.L.h(this.t, var1) ? null : InteractionHand.MAIN_HAND;
      }
   }

   private void m() {
      if (this.t()) {
         InteractionHand var1 = this.f();
         if (var1 != null) {
            BlockPos var2 = this.s;
            boolean var3 = mc.gameMode.useItem(this.t, var1).consumesAction();
            this.z = this.m.getValue().intValue();
            if (!var3) {
               this.J = "Scoop rejected";
               _q.c("AutoDrain", "Scoop at {} rejected locally", var2);
            } else {
               h8.i(this.t, var1);
               ((qc90ed4dd4cb15de4)mc).larpUseDelay(4);
               long var4 = System.currentTimeMillis();
               int[] var6 = this.h.computeIfAbsent(var2, var0 -> new int[1]);
               var6[0]++;
               this.N.put(var2, var4);
               this.q++;
               if (y(var6[0])) {
                  _q.c("AutoDrain", "{} refills; skipped for {} s", var2, 60L);
               }

               _q.c("AutoDrain", "Scooped {} ({} this run)", var2, this.q);
            }
         }
      }
   }

   private void x(String var1) {
      this.J = var1;
      if (this.Y) {
         int var2 = this.t.getInventory().getSelectedSlot();
         this.L.X(mc, true);
         if (mc.gameMode != null && var2 != this.t.getInventory().getSelectedSlot()) {
            ((q9b8c1cd7fe41113a)mc.gameMode).larpSyncCarriedItem();
         }

         b6.P(this, b6.w.p, this.I, this.W, 1.0);
         _q.c("AutoDrain", "Run ended after {} scoops: {}", this.q, var1);
         this.Y = (boolean)0;
         this.s = null;
         this.i = null;
      }
   }

   private void a(long var1) {
      Z.values().removeIf(var2 -> var1 - var2 >= 120000L);
      this.N.entrySet().removeIf(var3 -> {
         int var4 = var1 - var3.getValue() >= 60000L ? 1 : 0;
         if (var4 != 0) {
            this.h.remove(var3.getKey());
         }

         return (boolean)var4;
      });
      if (this.h.size() > 512) {
         this.h.clear();
         this.N.clear();
      }
   }
}
