package com.larpclient.module;

import com.larpclient.j6;
import com.larpclient.mr;
import com.larpclient.p_;
import com.larpclient.pk;
import com.larpclient.t1;
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

public final class tb extends t1.b implements mr.n {
   private final mr a = new mr(this, var1 -> this.addSetting(var1), 0.75, 0.35);
   private static tb v;
   private final l s = this.addSetting(
      new l("Mode", "Legit aims smoothly; Blatant aims directly every tick. Both keep your view free", "Legit", "Legit", "Blatant")
   );
   private final l I = this.addSetting(
      new l("Blocks", "Hotbar trap building material", "Obsidian", "Obsidian", "Crying Obsidian", "Ender Chest", "Stone", "Oak Planks", "Held Block")
   );
   private final s M = this.s("Only Standing", "Pause self trapping while standing on the ground", true);
   private final l N = this.addSetting(new l("Trap Mode", "Lower perimeter, upper perimeter or full enclosure", "Full", "Full", "Legs", "Head") {
      @Override
      public String getDisplayName() {
         return "Trap Pattern";
      }
   });
   private final s F = this.s("Trap Predict", "Predict opponent displacement before choosing the trap footprint", false);
   private final e d = this.Y("Predict Ticks", "Ticks of displacement prediction", 2.0, 0.0, 10.0, 0.5);
   private final s R = this.s("Predict Sort", "Build blocks closest to the predicted opponent first", true);
   private final e o = this.Y("Placement Preview", "Ticks to show the next block before placing", 6.0, 2.0, 40.0, 1.0);
   private final e Q = this.Y("Turn Speed", "Maximum player aim turn speed in degrees per second", 180.0, 30.0, 360.0, 15.0);
   private final com.larpclient.z n = new com.larpclient.z();
   private final pk p = new pk();
   private Player E;
   private Object x;
   private Object Z;
   private BlockPos i;
   private int V;
   private Vec3 X;

   @Override
   public mr placementHighlight() {
      return this.a;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.c();
      this.a.t();
      com.larpclient.h.A(
         this.getName(),
         "Placement mode={}; Legit preview={} ticks, delay={} ticks, turn cap={} degrees/s; Blatant uses direct turns and one native action per tick; mouse look stays free and movement stays manual",
         this.s.getValue(),
         this.o.getValue(),
         this.w.getValue(),
         this.Q.getValue()
      );
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.c();
      this.a.c();
   }

   private void c() {
      p_.Y(p_.o.G);
      this.n.T();
      this.p.s();
      this.E = null;
      this.i = null;
      this.x = this.Z = null;
      this.X = null;
   }

   @Override
   protected void aim(float var1, float var2) {
      if (this.isEnabled()) {
         if (p_.I(p_.o.G)) {
            if (this.u()) {
               super.aim(var1, var2);
            } else {
               float[] var3 = this.p.P(mc.player.tickCount, mc.player.getYRot(), mc.player.getXRot(), var1, var2, this.Q.getValue());
               j6.C(j6.l.p, var3[0], var3[1], 1.0);
            }

            if (j6.g(j6.l.p)) {
               p_.p(p_.o.G);
            } else {
               p_.Y(p_.o.G);
            }
         }
      }
   }

   public tb() {
      super("AutoTrap", "Trap with smooth Legit or fast Blatant aim while looking freely", 6);
      this.s.o(var1 -> {
         t1.v(this);
         this.c();
         this.a.c();
      });
      this.U.P(() -> !this.u());
      this.U.o(var1 -> {
         t1.v(this);
         this.c();
      });
      this.w.P(() -> !this.u());
      this.o.P(() -> !this.u());
      this.Q.P(() -> !this.u() && this.U.O("Visible"));
      v = this;
      this.d.P(this.F::getValue);
      this.R.P(this.F::getValue);
      this.M.P(() -> false);
   }

   public boolean u() {
      return this.s.O("Blatant");
   }

   private boolean G() {
      return this.u() || this.U.O("Visible");
   }

   public boolean e() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.G()
            && mc.player != null
            && this.x == mc.level
            && this.Z == mc.player
            && this.Q(this.E)
            && (this.n.b() != null || this.i != null)
            && (!this.y.getValue() || !mc.player.isUsingItem())
            && (!this.C.getValue() || !mc.options.keyAttack.isDown());
   }

   public float C(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && p_.A() == p_.o.G && j6.g(j6.l.p)) {
            mc.player.setYHeadRot(mc.player.getYRot());
            return mc.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public boolean U(int var1, boolean var2, boolean var3) {
      return !this.isEnabled() ? false : this.u() || var2 && this.n.W(var1, this.o.getValue().intValue(), var3);
   }

   public List<q<?>> u() {
      return List.of(this.s, this.I, this.e, this.N);
   }

   public List<q<?>> I() {
      return List.of(this.L, this.U, this.l, this.y, this.C, this.w, this.o, this.Q, this.F, this.d, this.R, this.M);
   }

   private boolean o(BlockPos var1, Item var2) {
      return t1.K(var1, var2, this.e.getValue(), this.G()) != null;
   }

   public static tb R() {
      return v;
   }

   public Item M() {
      return switch ((String)this.I.getValue()) {
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

   public List<BlockPos> a(Player var1) {
      if (var1 == mc.player && this.M.getValue() && var1.onGround()) {
         return List.of();
      }

      Vec3 var2 = l(var1, this.F.getValue() ? this.d.getValue() : 0.0);
      ArrayList var3 = new ArrayList<>(A(a(var1, var2), this.N.getValue()));
      var3.sort(
         Comparator.comparingInt(Vec3i::getY)
            .thenComparing(Comparator.<BlockPos>comparingDouble(var0 -> Vec3.atCenterOf(var0).distanceToSqr(mc.player.getEyePosition())).reversed())
      );
      if (this.F.getValue() && this.R.getValue()) {
         var3.sort(Comparator.comparingInt(Vec3i::getY).thenComparingDouble(var1x -> Vec3.atCenterOf(var1x).distanceToSqr(var2)));
      }

      return var3;
   }

   public static List<BlockPos> A(List<BlockPos> var0, String var1) {
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

   public static List<BlockPos> x(List<BlockPos> var0) {
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

   public static List<BlockPos> m(List<BlockPos> var0, Vec3 var1) {
      return x(var0).stream().sorted(Comparator.<BlockPos>comparingDouble(var1x -> Vec3.atCenterOf(var1x).distanceToSqr(var1)).reversed()).toList();
   }

   static Vec3 l(Player var0, double var1) {
      Vec3 var3 = var0.getDeltaMovement();
      if (var3.horizontalDistanceSqr() < 1.0E-8) {
         var3 = new Vec3(var0.getX() - var0.xo, var0.getY() - var0.yo, var0.getZ() - var0.zo);
      }

      return var0.position().add(var3.scale(var1));
   }

   static List<BlockPos> a(Player var0, Vec3 var1) {
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

   private boolean e(List<BlockPos> var1) {
      int var2 = var1.stream().mapToInt(Vec3i::getY).max().orElse(0);
      return !this.N.O("Legs") && var1.stream().anyMatch(var1x -> var1x.getY() == var2 && t1.t(var1x));
   }

   private boolean X(Player var1, BlockPos var2) {
      List var3 = this.a(var1);
      return var3.contains(var2) || this.e(var3) && x(var3).contains(var2);
   }

   @Override
   public void onTick() {
      this.a.q(null);
      t1.v(this);
      if (!this.m()) {
         this.n.T();
         this.p.s();
      } else if (this.G() && p_.A() != p_.o.T && p_.A() != p_.o.G) {
         this.n.T();
         this.p.s();
         this.U("Waiting for camera control");
      } else {
         if (this.x != mc.level || this.Z != mc.player || this.X != null && this.X.distanceToSqr(mc.player.position()) > 64.0) {
            this.c();
         }

         this.x = mc.level;
         this.Z = mc.player;
         this.X = mc.player.position();
         int var10000 = this.Q(this.E);
         boolean var16 = false;
         byte var15 = (byte)var10000;
         var10000 = Integer.signum(var15);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.E = this.L();
            this.n.T();
            this.p.s();
         }

         if (this.E == null) {
            this.U("Waiting for a target");
         } else {
            Item var1 = this.M();
            if (var1 != Items.AIR) {
               var10000 = t1.M(var1, this.l.getValue());
               byte var14 = -1;
               int var13 = var10000;
               var10000 = Integer.compare(var13, var14);
               if ((var10000 | -var10000) >>> 31 != 0) {
                  if (this.i != null) {
                     if (!t1.t(this.i)) {
                        this.i = null;
                     } else {
                        int var10 = this.V;
                        int var9 = mc.player.tickCount;
                        var10000 = var9 - var10;
                        byte var12 = 10;
                        int var11 = var10000;
                        if ((Integer.compare(var11, var12) >>> 31 ^ 1) == 0) {
                           this.a.q(this.i);
                           this.U("Waiting for world update");
                           return;
                        }

                        this.i = null;
                        this.n.T();
                        this.U("Block update missing; previewing a retry");
                     }
                  }

                  List var2 = this.a(this.E);
                  ArrayList var3 = new ArrayList(var2);
                  if (this.e(var2)) {
                     var3.addAll(m(var2, mc.player.getEyePosition()));
                  }

                  BlockPos var4 = this.n.b();
                  if (var4 == null || !var3.contains(var4) || !this.o(var4, var1)) {
                     var4 = null;

                     for (BlockPos var6 : var3) {
                        if (this.o(var6, var1)) {
                           var4 = var6;
                           break;
                        }
                     }
                  }

                  if (var4 != null) {
                     BlockPos var18 = var4;
                     Player var20 = this.E;
                     this.n.Z(var18, var20, var1, mc.player.tickCount);
                     this.a.q(var18);
                     BlockHitResult var7 = t1.K(var18, var1, this.e.getValue(), this.G());
                     if (var7 == null) {
                        this.n.T();
                        return;
                     }

                     boolean var8 = t1.W(
                        this,
                        var18,
                        var1,
                        this.e.getValue(),
                        this.G(),
                        this.l.getValue(),
                        () -> this.m() && this.E == var20 && this.Q(var20) && this.M() == var1 && this.X(var20, var18),
                        () -> {
                           float[] var2x = j6.s(mc.player.getEyePosition(), var7.getLocation());
                           int var3x = Math.abs(Mth.wrapDegrees(var2x[0] - mc.player.getYRot())) < 2.0F && Math.abs(var2x[1] - mc.player.getXRot()) < 2.0F
                              ? 1
                              : 0;
                           return (!this.G() || p_.A() == p_.o.G) && this.U(mc.player.tickCount, this.v(), (boolean)var3x);
                        },
                        var2x -> {
                           this.t(var2x);
                           if (var2x) {
                              this.i = var18;
                              this.V = mc.player.tickCount;
                           } else {
                              this.n.T();
                           }
                        }
                     );
                     if (var8) {
                        this.U("Preparing trap placement");
                     }

                     return;
                  }

                  this.n.T();
                  this.p.s();
                  boolean var17 = var2.stream().noneMatch(t1::t);
                  int var19 = !this.N.O("Legs")
                        && var2.stream().filter(t1::t).allMatch(var1x -> var1x.getY() == var2.stream().mapToInt(Vec3i::getY).max().orElse(0))
                     ? 1
                     : 0;
                  this.U(
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

            this.n.T();
            this.p.s();
            this.U("Trap blocks missing from hotbar");
         }
      }
   }
}
