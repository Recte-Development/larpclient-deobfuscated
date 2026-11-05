package com.larpclient.module;

import com.larp.obf.q5682c6a8aca9b6ec.q143ef5d8ac4bd5b8;
import com.larp.obf.q5682c6a8aca9b6ec.qa1ad45ac3ead4599;
import com.larpclient.d8;
import com.larpclient.ep;
import com.larpclient.hp;
import com.larpclient.m;
import com.larpclient.o_;
import com.larpclient.r4;
import com.larpclient.rr;
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
   public static final long L = 120000L;
   public static final int N = 2;
   public static final long x = 60000L;
   private static final double a = 0.4;
   private static final int t = 512;
   private static final Map<BlockPos, Long> d = new HashMap<>();
   private final lc g = this.addSetting(new lc("Range", "Furthest water to scoop, capped by your reach", 4.5, 2.0, 6.0, 0.5));
   private final lc p = this.addSetting(new lc("Delay", "Ticks to wait between scoops", 2.0, 0.0, 20.0, 1.0));
   private final ln K = this.addSetting(new ln("Waterlogged", "Also drain water inside stairs, slabs and other waterlogged blocks", false));
   private final ln B = this.addSetting(new ln("Ignore My Water", "Leave water you emptied yourself in the last two minutes", true));
   private final o_ s = new o_(this);
   private final Map<BlockPos, int[]> T = new HashMap<>();
   private final Map<BlockPos, Long> J = new HashMap<>();
   private LocalPlayer P;
   private ClientLevel m;
   private BlockPos G;
   private Vec3 R;
   private boolean O;
   private float W;
   private float o;
   private int I;
   private int c;
   private String Q = "Ready";

   public AutoDrainModule() {
      super("Auto Drain", "Scoop up nearby still water with empty buckets", Category.PLAYER, -1);
   }

   public static int y(double var0, int var2, double var3, int var5) {
      int var6 = Double.compare(var3, var0);
      return var6 != 0 ? var6 : Integer.compare(var5, var2);
   }

   public static boolean z(int var0, boolean var1) {
      return var0 == 1 || var1;
   }

   public static boolean V(int var0) {
      return var0 >= 2;
   }

   public static void h(Player var0, InteractionHand var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var0 == var2.player && var2.level != null && var0.getItemInHand(var1).is(Items.WATER_BUCKET)) {
         Vec3 var3 = var0.getEyePosition();
         Vec3 var4 = var3.add(var0.getLookAngle().scale(var0.blockInteractionRange()));
         BlockHitResult var5 = var2.level.clip(new ClipContext(var3, var4, Block.OUTLINE, Fluid.NONE, var0));
         if (var5.getType() == Type.BLOCK) {
            long var6 = System.currentTimeMillis();
            if (d.size() >= 512) {
               d.clear();
            }

            d.put(var5.getBlockPos().immutable(), var6);
            d.put(var5.getBlockPos().relative(var5.getDirection()).immutable(), var6);
         }
      }
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a, ld.c);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.Q : "";
   }

   @Override
   public void onEnable() {
      this.F();
      this.P = mc.player;
      this.m = mc.level;
      this.Q = "Ready";
      r4.V(
         "AutoDrain",
         "Enabled; range {}, delay {} ticks, waterlogged={}, ignore own={}",
         this.g.getValue(),
         this.p.getValue().intValue(),
         this.K.getValue(),
         this.B.getValue()
      );
   }

   @Override
   public void onDisable() {
      if (this.O && this.P != null && this.P == getMc().player) {
         this.s.m(mc, true);
         this.P.setYRot(this.W);
         this.P.setXRot(this.o);
      }

      this.F();
      this.P = null;
      this.m = null;
   }

   private void F() {
      d8.K(this);
      this.O = (boolean)0;
      this.G = null;
      this.R = null;
      this.I = 0;
      this.c = 0;
      this.T.clear();
      this.J.clear();
   }

   private boolean C() {
      rr.f var1 = rr.u();
      return !d8.a() || mc.gui.screen() != null || var1 != rr.f.i;
   }

   @Override
   public void onTick() {
      if (this.P != mc.player || this.m != mc.level) {
         this.s.m(getMc(), false);
         this.F();
         d.clear();
         this.P = mc.player;
         this.m = mc.level;
      }

      if (this.P != null && this.m != null) {
         if (this.C()) {
            if (this.O) {
               this.Q = "Paused";
            }
         } else {
            this.f(System.currentTimeMillis());
            if (this.I > 0) {
               byte var11 = 1;
               int var10 = this.I;
               this.I = var10 - var11;
            } else {
               int var1 = com.larpclient.m.Q(Items.BUCKET, true);
               byte var5 = -1;
               int var4 = var1;
               int var10000 = Integer.compare(var4, var5);
               if ((var10000 | -var10000) >>> 31 == 0) {
                  this.W("No empty buckets");
               } else {
                  byte var9 = -2;
                  int var8 = var1;
                  var10000 = Integer.compare(var8, var9);
                  ItemStack var2 = (var10000 | -var10000) >>> 31 == 0 ? this.P.getOffhandItem() : this.P.getInventory().getItem(var1);
                  var10000 = var2.getCount();
                  int var10001 = this.P.getInventory().getFreeSlot();
                  byte var7 = -1;
                  int var6 = var10001;
                  var10001 = Integer.compare(var6, var7);
                  if (!z(var10000, ((var10001 | -var10001) >>> 31 ^ 1) == 0)) {
                     this.W("Inventory full");
                  } else if (!this.U()) {
                     this.W(this.c > 0 ? "Drained " + this.c : "No water in reach");
                  } else {
                     if (!this.O) {
                        this.O = (boolean)1;
                        this.c = 0;
                        this.W = this.P.getYRot();
                        this.o = this.P.getXRot();
                        r4.g("AutoDrain", "Run started at {}", this.G);
                     }

                     this.Q = "Draining";
                     float[] var3 = ep.g(this.P.getEyePosition(), this.R);
                     ep.t(this, ep.z.W, var3[0], var3[1], 1.0);
                     d8.A(this, this::b, this::b);
                  }
               }
            }
         }
      }
   }

   private boolean U() {
      double var1 = Math.min(this.g.getValue(), this.P.blockInteractionRange());
      Vec3 var3 = this.P.getEyePosition();
      int var4 = (int)Math.ceil(var1);
      BlockPos var5 = BlockPos.containing(var3);
      long var6 = System.currentTimeMillis();
      BlockPos var8 = null;
      Vec3 var9 = null;
      double var10 = 0.0;

      for (BlockPos var13 : BlockPos.betweenClosed(var5.offset(-var4, -var4, -var4), var5.offset(var4, var4, var4))) {
         if (this.Z(var13, var6)) {
            Vec3 var14 = Vec3.atBottomCenterOf(var13).add(0.0, 0.4, 0.0);
            double var15 = var3.distanceTo(var14);
            if (!(var15 > var1) && (var8 == null || y(var15, var13.getY(), var10, var8.getY()) < 0) && this.u(var13, var3, var14.subtract(var3), var1)) {
               var8 = var13.immutable();
               var9 = var14;
               var10 = var15;
            }
         }
      }

      this.G = var8;
      this.R = var9;
      return var8 != null;
   }

   private boolean Z(BlockPos var1, long var2) {
      if (!this.m.getFluidState(var1).isSourceOfType(Fluids.WATER)) {
         return false;
      }

      BlockState var4 = this.m.getBlockState(var1);
      if (!(var4.getBlock() instanceof BucketPickup)) {
         return false;
      }

      if (!(var4.getBlock() instanceof LiquidBlock) && !this.K.getValue()) {
         return false;
      }

      if (this.P.getBoundingBox().intersects(new AABB(var1))) {
         return false;
      }

      if (this.B.getValue()) {
         Long var5 = d.get(var1);
         if (var5 != null && var2 - var5 < 120000L) {
            return false;
         }
      }

      int[] var6 = this.T.get(var1);
      return var6 == null || !V(var6[0]);
   }

   private boolean u(BlockPos var1, Vec3 var2, Vec3 var3, double var4) {
      Vec3 var6 = var2.add(var3.normalize().scale(var4));
      BlockHitResult var7 = this.m.clip(new ClipContext(var2, var6, Block.OUTLINE, Fluid.SOURCE_ONLY, this.P));
      return var7.getType() == Type.BLOCK && var7.getBlockPos().equals(var1);
   }

   private boolean b() {
      if (this.O && this.G != null && !this.P.isUsingItem() && this.B()) {
         if (!this.m.getFluidState(this.G).isSourceOfType(Fluids.WATER)) {
            return false;
         }

         double var1 = Math.min(this.g.getValue(), this.P.blockInteractionRange());
         return this.u(this.G, this.P.getEyePosition(), this.P.getLookAngle(), var1);
      } else {
         return false;
      }
   }

   private boolean B() {
      int var1 = com.larpclient.m.Q(Items.BUCKET, true);
      return var1 == -2 || var1 >= 0 && (var1 == this.P.getInventory().getSelectedSlot() || o_.N(this));
   }

   private InteractionHand h() {
      int var1 = com.larpclient.m.Q(Items.BUCKET, true);
      if (var1 == -2) {
         return InteractionHand.OFF_HAND;
      } else if (var1 < 0) {
         return null;
      } else {
         return var1 != this.P.getInventory().getSelectedSlot() && !this.s.s(this.P, var1) ? null : InteractionHand.MAIN_HAND;
      }
   }

   private void b() {
      if (this.b()) {
         InteractionHand var1 = this.h();
         if (var1 != null) {
            BlockPos var2 = this.G;
            boolean var3 = mc.gameMode.useItem(this.P, var1).consumesAction();
            this.I = this.p.getValue().intValue();
            if (!var3) {
               this.Q = "Scoop rejected";
               r4.g("AutoDrain", "Scoop at {} rejected locally", var2);
            } else {
               hp.Q(this.P, var1);
               ((qa1ad45ac3ead4599)mc).larpUseDelay(4);
               long var4 = System.currentTimeMillis();
               int[] var6 = this.T.computeIfAbsent(var2, var0 -> new int[1]);
               var6[0]++;
               this.J.put(var2, var4);
               this.c++;
               if (V(var6[0])) {
                  r4.g("AutoDrain", "{} refills; skipped for {} s", var2, 60L);
               }

               r4.g("AutoDrain", "Scooped {} ({} this run)", var2, this.c);
            }
         }
      }
   }

   private void W(String var1) {
      this.Q = var1;
      if (this.O) {
         int var2 = this.P.getInventory().getSelectedSlot();
         this.s.m(mc, true);
         if (mc.gameMode != null && var2 != this.P.getInventory().getSelectedSlot()) {
            ((q143ef5d8ac4bd5b8)mc.gameMode).larpSyncCarriedItem();
         }

         ep.t(this, ep.z.W, this.W, this.o, 1.0);
         r4.g("AutoDrain", "Run ended after {} scoops: {}", this.c, var1);
         this.O = (boolean)0;
         this.G = null;
         this.R = null;
      }
   }

   private void f(long var1) {
      d.values().removeIf(var2 -> var1 - var2 >= 120000L);
      this.J.entrySet().removeIf(var3 -> {
         int var4 = var1 - var3.getValue() >= 60000L ? 1 : 0;
         if (var4 != 0) {
            this.T.remove(var3.getKey());
         }

         return (boolean)var4;
      });
      if (this.T.size() > 512) {
         this.T.clear();
         this.J.clear();
      }
   }
}
