package com.larpclient.module;

import com.larpclient.ee;
import com.larpclient.ep;
import com.larpclient.hy;
import com.larpclient.ji;
import com.larpclient.m;
import com.larpclient.r4;
import com.larpclient.rr;
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

public final class t extends m.o implements hy.c {
   private final hy y = new hy(this, var1 -> this.addSetting(var1), 0.75, 0.35);
   private static t K;
   private final lp z = this.addSetting(
      new lp("Mode", "Legit aims smoothly; Blatant aims directly every tick. Both keep your view free", "Legit", "Legit", "Blatant")
   );
   private final lp I = this.addSetting(
      new lp("Blocks", "Hotbar trap building material", "Obsidian", "Obsidian", "Crying Obsidian", "Ender Chest", "Stone", "Oak Planks", "Held Block")
   );
   private final ln P = this.h("Only Standing", "Pause self trapping while standing on the ground", true);
   private final lp f = this.addSetting(new lp("Trap Mode", "Lower perimeter, upper perimeter or full enclosure", "Full", "Full", "Legs", "Head") {
      @Override
      public String getDisplayName() {
         return "Trap Pattern";
      }
   });
   private final ln G = this.h("Trap Predict", "Predict opponent displacement before choosing the trap footprint", false);
   private final lc p = this.z("Predict Ticks", "Ticks of displacement prediction", 2.0, 0.0, 10.0, 0.5);
   private final ln a = this.h("Predict Sort", "Build blocks closest to the predicted opponent first", true);
   private final lc i = this.z("Placement Preview", "Ticks to show the next block before placing", 6.0, 2.0, 40.0, 1.0);
   private final lc J = this.z("Turn Speed", "Maximum player aim turn speed in degrees per second", 180.0, 30.0, 360.0, 15.0);
   private final ee c = new ee();
   private final ji X = new ji();
   private Player W;
   private Object T;
   private Object A;
   private BlockPos R;
   private int o;
   private Vec3 m;

   @Override
   public hy placementHighlight() {
      return this.y;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.T();
      this.y.S();
      r4.V(
         this.getName(),
         "Placement mode={}; Legit preview={} ticks, delay={} ticks, turn cap={} degrees/s; Blatant uses direct turns and one native action per tick; mouse look stays free and movement stays manual",
         this.z.getValue(),
         this.i.getValue(),
         this.S.getValue(),
         this.J.getValue()
      );
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.T();
      this.y.C();
   }

   private void T() {
      rr.N(rr.f.e);
      this.c.v();
      this.X.q();
      this.W = null;
      this.R = null;
      this.T = this.A = null;
      this.m = null;
   }

   @Override
   protected void aim(float var1, float var2) {
      if (this.isEnabled()) {
         if (rr.j(rr.f.e)) {
            if (this.p()) {
               super.aim(var1, var2);
            } else {
               float[] var3 = this.X.f(mc.player.tickCount, mc.player.getYRot(), mc.player.getXRot(), var1, var2, this.J.getValue());
               ep.C(ep.z.d, var3[0], var3[1], 1.0);
            }

            if (ep.W(ep.z.d)) {
               rr.W(rr.f.e);
            } else {
               rr.N(rr.f.e);
            }
         }
      }
   }

   public t() {
      super("AutoTrap", "Trap with smooth Legit or fast Blatant aim while looking freely", 6);
      this.z.W(var1 -> {
         com.larpclient.m.g(this);
         this.T();
         this.y.C();
      });
      this.Y.x(() -> !this.p());
      this.Y.W(var1 -> {
         com.larpclient.m.g(this);
         this.T();
      });
      this.S.x(() -> !this.p());
      this.i.x(() -> !this.p());
      this.J.x(() -> !this.p() && this.Y.i("Visible"));
      K = this;
      this.p.x(this.G::getValue);
      this.a.x(this.G::getValue);
      this.P.x(() -> false);
   }

   public boolean p() {
      return this.z.i("Blatant");
   }

   private boolean Q() {
      return this.p() || this.Y.i("Visible");
   }

   public boolean n() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.Q()
            && mc.player != null
            && this.T == mc.level
            && this.A == mc.player
            && this.b(this.W)
            && (this.c.K() != null || this.R != null)
            && (!this.w.getValue() || !mc.player.isUsingItem())
            && (!this.d.getValue() || !mc.options.keyAttack.isDown());
   }

   public float b(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && rr.u() == rr.f.e && ep.W(ep.z.d)) {
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
      return !this.isEnabled() ? false : this.p() || var2 && this.c.a(var1, this.i.getValue().intValue(), var3);
   }

   public List<l6<?>> t() {
      return List.of(this.z, this.I, this.U, this.f);
   }

   public List<l6<?>> A() {
      return List.of(this.b, this.Y, this.x, this.w, this.d, this.S, this.i, this.J, this.G, this.p, this.a, this.P);
   }

   private boolean R(BlockPos var1, Item var2) {
      return com.larpclient.m.V(var1, var2, this.U.getValue(), this.Q()) != null;
   }

   public static t d() {
      return K;
   }

   public Item l() {
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

   public List<BlockPos> Z(Player var1) {
      if (var1 == mc.player && this.P.getValue() && var1.onGround()) {
         return List.of();
      }

      Vec3 var2 = X(var1, this.G.getValue() ? this.p.getValue() : 0.0);
      ArrayList var3 = new ArrayList<>(e(S(var1, var2), this.f.getValue()));
      var3.sort(
         Comparator.comparingInt(Vec3i::getY)
            .thenComparing(Comparator.<BlockPos>comparingDouble(var0 -> Vec3.atCenterOf(var0).distanceToSqr(mc.player.getEyePosition())).reversed())
      );
      if (this.G.getValue() && this.a.getValue()) {
         var3.sort(Comparator.comparingInt(Vec3i::getY).thenComparingDouble(var1x -> Vec3.atCenterOf(var1x).distanceToSqr(var2)));
      }

      return var3;
   }

   public static List<BlockPos> e(List<BlockPos> var0, String var1) {
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

   public static List<BlockPos> t(List<BlockPos> var0) {
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

   public static List<BlockPos> G(List<BlockPos> var0, Vec3 var1) {
      return t(var0).stream().sorted(Comparator.<BlockPos>comparingDouble(var1x -> Vec3.atCenterOf(var1x).distanceToSqr(var1)).reversed()).toList();
   }

   static Vec3 X(Player var0, double var1) {
      Vec3 var3 = var0.getDeltaMovement();
      if (var3.horizontalDistanceSqr() < 1.0E-8) {
         var3 = new Vec3(var0.getX() - var0.xo, var0.getY() - var0.yo, var0.getZ() - var0.zo);
      }

      return var0.position().add(var3.scale(var1));
   }

   static List<BlockPos> S(Player var0, Vec3 var1) {
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

   private boolean N(List<BlockPos> var1) {
      int var2 = var1.stream().mapToInt(Vec3i::getY).max().orElse(0);
      return !this.f.i("Legs") && var1.stream().anyMatch(var1x -> var1x.getY() == var2 && com.larpclient.m.Q(var1x));
   }

   private boolean w(Player var1, BlockPos var2) {
      List var3 = this.Z(var1);
      return var3.contains(var2) || this.N(var3) && t(var3).contains(var2);
   }

   @Override
   public void onTick() {
      this.y.i(null);
      com.larpclient.m.g(this);
      if (!this.i()) {
         this.c.v();
         this.X.q();
      } else {
         int var10000 = this.Q();
         boolean var16 = false;
         byte var15 = (byte)var10000;
         var10000 = Integer.signum(var15);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && rr.u() != rr.f.i && rr.u() != rr.f.e) {
            this.c.v();
            this.X.q();
            this.H("Waiting for camera control");
         } else {
            if (this.T != mc.level || this.A != mc.player || this.m != null && this.m.distanceToSqr(mc.player.position()) > 64.0) {
               this.T();
            }

            this.T = mc.level;
            this.A = mc.player;
            this.m = mc.player.position();
            if (!this.b(this.W)) {
               this.W = this.o();
               this.c.v();
               this.X.q();
            }

            if (this.W == null) {
               this.H("Waiting for a target");
            } else {
               Item var1 = this.l();
               if (var1 != Items.AIR) {
                  var10000 = com.larpclient.m.Q(var1, this.x.getValue());
                  byte var10 = -1;
                  int var9 = var10000;
                  var10000 = Integer.compare(var9, var10);
                  if ((var10000 | -var10000) >>> 31 != 0) {
                     if (this.R != null) {
                        if (!com.larpclient.m.Q(this.R)) {
                           this.R = null;
                        } else {
                           int var12 = this.o;
                           int var11 = mc.player.tickCount;
                           var10000 = var11 - var12;
                           byte var14 = 10;
                           int var13 = var10000;
                           if ((Integer.compare(var13, var14) >>> 31 ^ 1) == 0) {
                              this.y.i(this.R);
                              this.H("Waiting for world update");
                              return;
                           }

                           this.R = null;
                           this.c.v();
                           this.H("Block update missing; previewing a retry");
                        }
                     }

                     List var2 = this.Z(this.W);
                     ArrayList var3 = new ArrayList(var2);
                     if (this.N(var2)) {
                        var3.addAll(G(var2, mc.player.getEyePosition()));
                     }

                     BlockPos var4 = this.c.K();
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
                        Player var20 = this.W;
                        this.c.Y(var18, var20, var1, mc.player.tickCount);
                        this.y.i(var18);
                        BlockHitResult var7 = com.larpclient.m.V(var18, var1, this.U.getValue(), this.Q());
                        if (var7 == null) {
                           this.c.v();
                           return;
                        }

                        boolean var8 = com.larpclient.m.J(
                           this,
                           var18,
                           var1,
                           this.U.getValue(),
                           this.Q(),
                           this.x.getValue(),
                           () -> this.i() && this.W == var20 && this.b(var20) && this.l() == var1 && this.w(var20, var18),
                           () -> {
                              float[] var2x = ep.g(mc.player.getEyePosition(), var7.getLocation());
                              int var3x = Math.abs(Mth.wrapDegrees(var2x[0] - mc.player.getYRot())) < 2.0F && Math.abs(var2x[1] - mc.player.getXRot()) < 2.0F
                                 ? 1
                                 : 0;
                              return (!this.Q() || rr.u() == rr.f.e) && this.U(mc.player.tickCount, this.K(), (boolean)var3x);
                           },
                           var2x -> {
                              this.a(var2x);
                              if (var2x) {
                                 this.R = var18;
                                 this.o = mc.player.tickCount;
                              } else {
                                 this.c.v();
                              }
                           }
                        );
                        if (var8) {
                           this.H("Preparing trap placement");
                        }

                        return;
                     }

                     this.c.v();
                     this.X.q();
                     boolean var17 = var2.stream().noneMatch(m::Q);
                     int var19 = !this.f.i("Legs")
                           && var2.stream().filter(m::Q).allMatch(var1x -> var1x.getY() == var2.stream().mapToInt(Vec3i::getY).max().orElse(0))
                        ? 1
                        : 0;
                     this.H(
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

               this.c.v();
               this.X.q();
               this.H("Trap blocks missing from hotbar");
            }
         }
      }
   }
}
