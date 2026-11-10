package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.a7;
import com.larpclient.ai;
import com.larpclient.b6;
import com.larpclient.ij;
import com.larpclient.p8;
import com.larpclient.vi;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction.Plane;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class kv extends vi.n implements ai.e {
   private final ai e = new ai(this, var1 -> this.addSetting(var1), 0.75, 0.35);
   private static kv i;
   private final o U = this.addSetting(
      new o("Mode", "Legit aims smoothly; Blatant aims directly every tick. Both keep your view free", "Legit", "Legit", "Blatant")
   );
   private final o m = this.addSetting(
      new o("Blocks", "Hotbar trap building material", "Obsidian", "Obsidian", "Crying Obsidian", "Ender Chest", "Stone", "Oak Planks", "Held Block")
   );
   private final m v = this.O("Only Standing", "Pause self trapping while standing on the ground", true);
   private final o t = this.addSetting(new o("Trap Mode", "Lower perimeter, upper perimeter or full enclosure", "Full", "Full", "Legs", "Head") {
      @Override
      public String getDisplayName() {
         return "Trap Pattern";
      }
   });
   private final m p = this.O("Trap Predict", "Predict opponent displacement before choosing the trap footprint", false);
   private final b M = this.x("Predict Ticks", "Ticks of displacement prediction", 2.0, 0.0, 10.0, 0.5);
   private final m f = this.O("Predict Sort", "Build blocks closest to the predicted opponent first", true);
   private final b A = this.x("Placement Preview", "Ticks to show the next block before placing", 6.0, 2.0, 40.0, 1.0);
   private final b w = this.x("Turn Speed", "Maximum player aim turn speed in degrees per second", 180.0, 30.0, 360.0, 15.0);
   private final p8 W = new p8();
   private final a7 K = new a7();
   private Player j;
   private Object Y;
   private Object L;
   private BlockPos Z;
   private int x;
   private Vec3 C;

   @Override
   public ai placementHighlight() {
      return this.e;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.Q();
      this.e.b();
      _q.z(
         this.getName(),
         "Placement mode={}; Legit preview={} ticks, delay={} ticks, turn cap={} degrees/s; Blatant uses direct turns and one native action per tick; mouse look stays free and movement stays manual",
         this.U.getValue(),
         this.A.getValue(),
         this.u.getValue(),
         this.w.getValue()
      );
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.Q();
      this.e.m();
   }

   private void Q() {
      ij.W(ij.r.D);
      this.W.J();
      this.K.W();
      this.j = null;
      this.Z = null;
      this.Y = this.L = null;
      this.C = null;
   }

   @Override
   protected void aim(float var1, float var2) {
      if (this.isEnabled()) {
         if (ij.a(ij.r.D)) {
            if (this.P()) {
               super.aim(var1, var2);
            } else {
               float[] var3 = this.K.k(mc.player.tickCount, mc.player.getYRot(), mc.player.getXRot(), var1, var2, this.w.getValue());
               b6.f(b6.w.F, var3[0], var3[1], 1.0);
            }

            if (b6.c(b6.w.F)) {
               ij.V(ij.r.D);
            } else {
               ij.W(ij.r.D);
            }
         }
      }
   }

   public kv() {
      super("AutoTrap", "Trap with smooth Legit or fast Blatant aim while looking freely", 6);
      this.U.w(var1 -> {
         vi.J(this);
         this.Q();
         this.e.m();
      });
      this.O.a(() -> !this.P());
      this.O.w(var1 -> {
         vi.J(this);
         this.Q();
      });
      this.u.a(() -> !this.P());
      this.A.a(() -> !this.P());
      this.w.a(() -> !this.P() && this.O.w("Visible"));
      i = this;
      this.M.a(this.p::getValue);
      this.f.a(this.p::getValue);
      this.v.a(() -> false);
   }

   public boolean P() {
      return this.U.w("Blatant");
   }

   private boolean m() {
      return this.P() || this.O.w("Visible");
   }

   public boolean Y() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.m()
            && mc.player != null
            && this.Y == mc.level
            && this.L == mc.player
            && this.j(this.j)
            && (this.W.e() != null || this.Z != null)
            && (!this.g.getValue() || !mc.player.isUsingItem())
            && (!this.o.getValue() || !mc.options.keyAttack.isDown());
   }

   public float z(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && ij.L() == ij.r.D && b6.c(b6.w.F)) {
            mc.player.setYHeadRot(mc.player.getYRot());
            return mc.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public boolean n(int var1, boolean var2, boolean var3) {
      return !this.isEnabled() ? false : this.P() || var2 && this.W.m(var1, this.A.getValue().intValue(), var3);
   }

   public List<r<?>> l() {
      return List.of(this.U, this.m, this.T, this.t);
   }

   public List<r<?>> M() {
      return List.of(this.h, this.O, this.a, this.g, this.o, this.u, this.A, this.w, this.p, this.M, this.f, this.v);
   }

   private boolean R(BlockPos var1, Item var2) {
      return vi.c(var1, var2, this.T.getValue(), this.m()) != null;
   }

   public static kv m() {
      return i;
   }

   public Item X() {
      return switch ((String)this.m.getValue()) {
         case "Crying Obsidian" -> Items.CRYING_OBSIDIAN;
         case "Ender Chest" -> Items.ENDER_CHEST;
         case "Stone" -> Items.STONE;
         case "Oak Planks" -> Items.OAK_PLANKS;
         case "Held Block" -> mc.player != null && mc.player.getMainHandItem().getItem() instanceof BlockItem
            ? mc.player.getMainHandItem().getItem()
            : Items.AIR;
         default -> Items.OBSIDIAN;
      };
   }

   public List<BlockPos> M(Player var1) {
      if (var1 == mc.player && this.v.getValue() && var1.onGround()) {
         return List.of();
      }

      Vec3 var2 = L(var1, this.p.getValue() ? this.M.getValue() : 0.0);
      ArrayList var3 = new ArrayList<>(r(r(var1, var2), this.t.getValue()));
      var3.sort(
         Comparator.comparingInt(Vec3i::getY)
            .thenComparing(Comparator.<BlockPos>comparingDouble(var0 -> Vec3.atCenterOf(var0).distanceToSqr(mc.player.getEyePosition())).reversed())
      );
      if (this.p.getValue() && this.f.getValue()) {
         var3.sort(Comparator.comparingInt(Vec3i::getY).thenComparingDouble(var1x -> Vec3.atCenterOf(var1x).distanceToSqr(var2)));
      }

      return var3;
   }

   public static List<BlockPos> r(List<BlockPos> var0, String var1) {
      LinkedHashSet var2 = new LinkedHashSet();
      if (!var1.equals("Head")) {
         for (BlockPos var4 : var0) {
            var2.add(var4.below());
         }
      }

      for (BlockPos var10 : var0) {
         for (Direction var6 : Plane.HORIZONTAL) {
            BlockPos var7 = var10.relative(var6);
            if (!var0.contains(var7)) {
               var2.add(var7);
               if (!var1.equals("Legs")) {
                  var2.add(var7.above());
               }
            }
         }
      }

      if (!var1.equals("Legs")) {
         for (BlockPos var11 : var0) {
            var2.add(var11.above(2));
         }
      }

      return List.copyOf(var2);
   }

   public static List<BlockPos> J(List<BlockPos> var0) {
      if (var0.isEmpty()) {
         return List.of();
      }

      int var1 = var0.stream().mapToInt(Vec3i::getY).max().orElse(0);
      LinkedHashSet var2 = new LinkedHashSet();

      for (BlockPos var4 : var0) {
         if (var4.getY() == var1) {
            for (Direction var6 : Plane.HORIZONTAL) {
               BlockPos var7 = var4.relative(var6);
               if (!var0.contains(var7) && var0.contains(var7.below())) {
                  var2.add(var7);
               }
            }
         }
      }

      return List.copyOf(var2);
   }

   public static List<BlockPos> U(List<BlockPos> var0, Vec3 var1) {
      return J(var0).stream().sorted(Comparator.<BlockPos>comparingDouble(var1x -> Vec3.atCenterOf(var1x).distanceToSqr(var1)).reversed()).toList();
   }

   static Vec3 L(Player var0, double var1) {
      Vec3 var3 = var0.getDeltaMovement();
      if (var3.horizontalDistanceSqr() < 1.0E-8) {
         var3 = new Vec3(var0.getX() - var0.xo, var0.getY() - var0.yo, var0.getZ() - var0.zo);
      }

      return var0.position().add(var3.scale(var1));
   }

   static List<BlockPos> r(Player var0, Vec3 var1) {
      AABB var2 = var0.getBoundingBox().move(var1.subtract(var0.position()));
      int var3 = (int)Math.floor(var1.y + 0.01);
      ArrayList var4 = new ArrayList(4);

      for (int var5 = (int)Math.floor(var2.minX + 0.001); var5 <= (int)Math.floor(var2.maxX - 0.001); var5++) {
         for (int var6 = (int)Math.floor(var2.minZ + 0.001); var6 <= (int)Math.floor(var2.maxZ - 0.001); var6++) {
            var4.add(new BlockPos(var5, var3, var6));
         }
      }

      return var4;
   }

   private boolean d(List<BlockPos> var1) {
      int var2 = var1.stream().mapToInt(Vec3i::getY).max().orElse(0);
      return !this.t.w("Legs") && var1.stream().anyMatch(var1x -> var1x.getY() == var2 && vi.i(var1x));
   }

   private boolean h(Player var1, BlockPos var2) {
      List var3 = this.M(var1);
      return var3.contains(var2) || this.d(var3) && J(var3).contains(var2);
   }

   @Override
   public void onTick() {
      this.e.H(null);
      vi.J(this);
      if (!this.Z()) {
         this.W.J();
         this.K.W();
      } else if (this.m() && ij.L() != ij.r.X && ij.L() != ij.r.D) {
         this.W.J();
         this.K.W();
         this.M("Waiting for camera control");
      } else {
         if (this.Y != mc.level || this.L != mc.player || this.C != null && this.C.distanceToSqr(mc.player.position()) > 64.0) {
            this.Q();
         }

         this.Y = mc.level;
         this.L = mc.player;
         this.C = mc.player.position();
         if (!this.j(this.j)) {
            this.j = this.P();
            this.W.J();
            this.K.W();
         }

         if (this.j == null) {
            this.M("Waiting for a target");
         } else {
            Item var1 = this.X();
            if (var1 != Items.AIR) {
               int var10000 = vi.u(var1, this.a.getValue());
               byte var12 = -1;
               int var11 = var10000;
               var10000 = Integer.compare(var11, var12);
               if ((var10000 | -var10000) >>> 31 != 0) {
                  if (this.Z != null) {
                     if (!vi.i(this.Z)) {
                        this.Z = null;
                     } else {
                        int var14 = this.x;
                        int var13 = mc.player.tickCount;
                        var10000 = var13 - var14;
                        byte var16 = 10;
                        int var15 = var10000;
                        if ((Integer.compare(var15, var16) >>> 31 ^ 1) == 0) {
                           this.e.H(this.Z);
                           this.M("Waiting for world update");
                           return;
                        }

                        this.Z = null;
                        this.W.J();
                        this.M("Block update missing; previewing a retry");
                     }
                  }

                  List var2 = this.M(this.j);
                  ArrayList var3 = new ArrayList(var2);
                  int var23 = this.d(var2);
                  boolean var10 = false;
                  byte var9 = (byte)var23;
                  var23 = Integer.signum(var9);
                  if (((var23 | -var23) >>> 31 ^ 1) == 0) {
                     var3.addAll(U(var2, mc.player.getEyePosition()));
                  }

                  BlockPos var4 = this.W.e();
                  if (var4 == null || !var3.contains(var4) || !this.R(var4, var1)) {
                     var4 = null;

                     for (BlockPos var6 : var3) {
                        if (this.R(var6, var1)) {
                           var4 = var6;
                           break;
                        }
                     }
                  }

                  if (var4 != null) {
                     BlockPos var18 = var4;
                     Player var20 = this.j;
                     this.W.q(var18, var20, var1, mc.player.tickCount);
                     this.e.H(var18);
                     BlockHitResult var7 = vi.c(var18, var1, this.T.getValue(), this.m());
                     if (var7 == null) {
                        this.W.J();
                        return;
                     }

                     boolean var8 = vi.M(
                        this,
                        var18,
                        var1,
                        this.T.getValue(),
                        this.m(),
                        this.a.getValue(),
                        () -> this.Z() && this.j == var20 && this.j(var20) && this.X() == var1 && this.h(var20, var18),
                        () -> {
                           float[] var2x = b6.R(mc.player.getEyePosition(), var7.getLocation());
                           int var3x = Math.abs(Mth.wrapDegrees(var2x[0] - mc.player.getYRot())) < 2.0F && Math.abs(var2x[1] - mc.player.getXRot()) < 2.0F
                              ? 1
                              : 0;
                           return (!this.m() || ij.L() == ij.r.D) && this.n(mc.player.tickCount, this.G(), (boolean)var3x);
                        },
                        var2x -> {
                           this.K(var2x);
                           if (var2x) {
                              this.Z = var18;
                              this.x = mc.player.tickCount;
                           } else {
                              this.W.J();
                           }
                        }
                     );
                     if (var8) {
                        this.M("Preparing trap placement");
                     }

                     return;
                  }

                  this.W.J();
                  this.K.W();
                  boolean var17 = var2.stream().noneMatch(vi::i);
                  int var19 = !this.t.w("Legs")
                        && var2.stream().filter(vi::i).allMatch(var1x -> var1x.getY() == var2.stream().mapToInt(Vec3i::getY).max().orElse(0))
                     ? 1
                     : 0;
                  this.M(
                     var17
                        ? "Trap enclosure filled"
                        : (
                           var19 != 0
                              ? "Roof needs a higher view; jump or move to expose a supported face"
                              : "Move closer or around the target to reach the remaining trap faces"
                        )
                  );
                  return;
               }
            }

            this.W.J();
            this.K.W();
            this.M("Trap blocks missing from hotbar");
         }
      }
   }
}
