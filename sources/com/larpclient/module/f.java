package com.larpclient.module;

import com.larpclient._d;
import com.larpclient.m9;
import com.larpclient.mt;
import com.larpclient.ow;
import com.larpclient.pp;
import com.larpclient.tq;
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

public final class f extends _d.u implements pp.x {
   private final pp p = new pp(this, var1 -> this.addSetting(var1), 0.75, 0.35);
   private static com.larpclient.module.f h;
   private final d6 x = this.addSetting(
      new d6("Mode", "Legit aims smoothly; Blatant aims directly every tick. Both keep your view free", "Legit", "Legit", "Blatant")
   );
   private final d6 C = this.addSetting(
      new d6("Blocks", "Hotbar trap building material", "Obsidian", "Obsidian", "Crying Obsidian", "Ender Chest", "Stone", "Oak Planks", "Held Block")
   );
   private final dh o = this.p("Only Standing", "Pause self trapping while standing on the ground", true);
   private final d6 N = this.addSetting(new d6("Trap Mode", "Lower perimeter, upper perimeter or full enclosure", "Full", "Full", "Legs", "Head") {
      @Override
      public String getDisplayName() {
         return "Trap Pattern";
      }
   });
   private final dh r = this.p("Trap Predict", "Predict opponent displacement before choosing the trap footprint", false);
   private final d9 S = this.S("Predict Ticks", "Ticks of displacement prediction", 2.0, 0.0, 10.0, 0.5);
   private final dh a = this.p("Predict Sort", "Build blocks closest to the predicted opponent first", true);
   private final d9 B = this.S("Placement Preview", "Ticks to show the next block before placing", 6.0, 2.0, 40.0, 1.0);
   private final d9 z = this.S("Turn Speed", "Maximum player aim turn speed in degrees per second", 180.0, 30.0, 360.0, 15.0);
   private final com.larpclient.ds H = new com.larpclient.ds();
   private final ow V = new ow();
   private Player e;
   private Object O;
   private Object g;
   private BlockPos s;
   private int f;
   private Vec3 D;

   @Override
   public pp placementHighlight() {
      return this.p;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.v();
      this.p.w();
      mt.K(
         this.getName(),
         "Placement mode={}; Legit preview={} ticks, delay={} ticks, turn cap={} degrees/s; Blatant uses direct turns and one native action per tick; mouse look stays free and movement stays manual",
         this.x.P(),
         this.B.P(),
         this.P.P(),
         this.z.P()
      );
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.v();
      this.p.P();
   }

   private void v() {
      tq.A(tq.y.J);
      this.H.o();
      this.V.f();
      this.e = null;
      this.s = null;
      this.O = this.g = null;
      this.D = null;
   }

   @Override
   protected void aim(float var1, float var2) {
      if (this.isEnabled()) {
         if (tq.y(tq.y.J)) {
            if (this.v()) {
               super.aim(var1, var2);
            } else {
               float[] var3 = this.V.W(mc.player.tickCount, mc.player.getYRot(), mc.player.getXRot(), var1, var2, this.z.P());
               m9.c(m9.s.p, var3[0], var3[1], 1.0);
            }

            if (m9.F(m9.s.p)) {
               tq.i(tq.y.J);
            } else {
               tq.A(tq.y.J);
            }
         }
      }
   }

   public f() {
      super("AutoTrap", "Trap with smooth Legit or fast Blatant aim while looking freely", 6);
      this.x.h(var1 -> {
         _d.N(this);
         this.v();
         this.p.P();
      });
      this.M.H(() -> !this.v());
      this.M.h(var1 -> {
         _d.N(this);
         this.v();
      });
      this.P.H(() -> !this.v());
      this.B.H(() -> !this.v());
      this.z.H(() -> !this.v() && this.M.S("Visible"));
      h = this;
      this.S.H(this.r::P);
      this.a.H(this.r::P);
      this.o.H(() -> false);
   }

   public boolean v() {
      return this.x.S("Blatant");
   }

   private boolean B() {
      return this.v() || this.M.S("Visible");
   }

   public boolean w() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.B()
            && mc.player != null
            && this.O == mc.level
            && this.g == mc.player
            && this.p(this.e)
            && (this.H.X() != null || this.s != null)
            && (!this.w.P() || !mc.player.isUsingItem())
            && (!this.G.P() || !mc.options.keyAttack.isDown());
   }

   public float F(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && tq.l() == tq.y.J && m9.F(m9.s.p)) {
            mc.player.setYHeadRot(mc.player.getYRot());
            return mc.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public boolean Q(int var1, boolean var2, boolean var3) {
      return !this.isEnabled() ? false : this.v() || var2 && this.H.R(var1, this.B.P().intValue(), var3);
   }

   public List<df<?>> u() {
      return List.of(this.x, this.C, this.j, this.N);
   }

   public List<df<?>> S() {
      return List.of(this.l, this.M, this.d, this.w, this.G, this.P, this.B, this.z, this.r, this.S, this.a, this.o);
   }

   private boolean K(BlockPos var1, Item var2) {
      return _d.d(var1, var2, this.j.P(), this.B()) != null;
   }

   public static com.larpclient.module.f X() {
      return h;
   }

   public Item b() {
      return switch ((String)this.C.P()) {
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

   public List<BlockPos> I(Player var1) {
      if (var1 == mc.player && this.o.P() && var1.onGround()) {
         return List.of();
      }

      Vec3 var2 = D(var1, this.r.P() ? this.S.P() : 0.0);
      ArrayList var3 = new ArrayList<>(A(j(var1, var2), this.N.P()));
      var3.sort(
         Comparator.comparingInt(Vec3i::getY)
            .thenComparing(Comparator.<BlockPos>comparingDouble(var0 -> Vec3.atCenterOf(var0).distanceToSqr(mc.player.getEyePosition())).reversed())
      );
      if (this.r.P() && this.a.P()) {
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

   public static List<BlockPos> T(List<BlockPos> var0, Vec3 var1) {
      return t(var0).stream().sorted(Comparator.<BlockPos>comparingDouble(var1x -> Vec3.atCenterOf(var1x).distanceToSqr(var1)).reversed()).toList();
   }

   static Vec3 D(Player var0, double var1) {
      Vec3 var3 = var0.getDeltaMovement();
      if (var3.horizontalDistanceSqr() < 1.0E-8) {
         var3 = new Vec3(var0.getX() - var0.xo, var0.getY() - var0.yo, var0.getZ() - var0.zo);
      }

      return var0.position().add(var3.scale(var1));
   }

   static List<BlockPos> j(Player var0, Vec3 var1) {
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

   private boolean P(List<BlockPos> var1) {
      int var2 = var1.stream().mapToInt(Vec3i::getY).max().orElse(0);
      return !this.N.S("Legs") && var1.stream().anyMatch(var1x -> var1x.getY() == var2 && _d.Z(var1x));
   }

   private boolean s(Player var1, BlockPos var2) {
      List var3 = this.I(var1);
      return var3.contains(var2) || this.P(var3) && t(var3).contains(var2);
   }

   @Override
   public void onTick() {
      this.p.m(null);
      _d.N(this);
      if (!this.F()) {
         this.H.o();
         this.V.f();
      } else if (this.B() && tq.l() != tq.y.A && tq.l() != tq.y.J) {
         this.H.o();
         this.V.f();
         this.e("Waiting for camera control");
      } else {
         if (this.O != mc.level || this.g != mc.player || this.D != null && this.D.distanceToSqr(mc.player.position()) > 64.0) {
            this.v();
         }

         this.O = mc.level;
         this.g = mc.player;
         this.D = mc.player.position();
         if (!this.p(this.e)) {
            this.e = this.a();
            this.H.o();
            this.V.f();
         }

         if (this.e == null) {
            this.e("Waiting for a target");
         } else {
            Item var1 = this.b();
            if (var1 != Items.AIR) {
               int var10000 = _d.B(var1, this.d.P());
               byte var16 = -1;
               int var15 = var10000;
               var10000 = Integer.compare(var15, var16);
               if ((var10000 | -var10000) >>> 31 != 0) {
                  if (this.s != null) {
                     if (!_d.Z(this.s)) {
                        this.s = null;
                     } else {
                        int var12 = this.f;
                        int var11 = mc.player.tickCount;
                        var10000 = var11 - var12;
                        byte var14 = 10;
                        int var13 = var10000;
                        if ((Integer.compare(var13, var14) >>> 31 ^ 1) == 0) {
                           this.p.m(this.s);
                           this.e("Waiting for world update");
                           return;
                        }

                        this.s = null;
                        this.H.o();
                        this.e("Block update missing; previewing a retry");
                     }
                  }

                  List var2 = this.I(this.e);
                  ArrayList var3 = new ArrayList(var2);
                  if (this.P(var2)) {
                     var3.addAll(T(var2, mc.player.getEyePosition()));
                  }

                  BlockPos var4 = this.H.X();
                  if (var4 == null || !var3.contains(var4) || !this.K(var4, var1)) {
                     var4 = null;

                     for (BlockPos var6 : var3) {
                        if (this.K(var6, var1)) {
                           var4 = var6;
                           break;
                        }
                     }
                  }

                  if (var4 != null) {
                     BlockPos var18 = var4;
                     Player var20 = this.e;
                     this.H.S(var18, var20, var1, mc.player.tickCount);
                     this.p.m(var18);
                     BlockHitResult var7 = _d.d(var18, var1, this.j.P(), this.B());
                     if (var7 == null) {
                        this.H.o();
                        return;
                     }

                     boolean var8 = _d.F(
                        this,
                        var18,
                        var1,
                        this.j.P(),
                        this.B(),
                        this.d.P(),
                        () -> this.F() && this.e == var20 && this.p(var20) && this.b() == var1 && this.s(var20, var18),
                        () -> {
                           float[] var2x = m9.Z(mc.player.getEyePosition(), var7.getLocation());
                           int var3x = Math.abs(Mth.wrapDegrees(var2x[0] - mc.player.getYRot())) < 2.0F && Math.abs(var2x[1] - mc.player.getXRot()) < 2.0F
                              ? 1
                              : 0;
                           return (!this.B() || tq.l() == tq.y.J) && this.Q(mc.player.tickCount, this.s(), (boolean)var3x);
                        },
                        var2x -> {
                           this.L(var2x);
                           if (var2x) {
                              this.s = var18;
                              this.f = mc.player.tickCount;
                           } else {
                              this.H.o();
                           }
                        }
                     );
                     if (var8) {
                        this.e("Preparing trap placement");
                     }

                     return;
                  }

                  boolean var17;
                  label74: {
                     this.H.o();
                     this.V.f();
                     var17 = var2.stream().noneMatch(_d::Z);
                     if (!this.N.S("Legs")) {
                        int var23 = var2.stream().filter(_d::Z).allMatch(var1x -> var1x.getY() == var2.stream().mapToInt(Vec3i::getY).max().orElse(0));
                        boolean var10 = false;
                        byte var9 = (byte)var23;
                        var23 = Integer.signum(var9);
                        if (((var23 | -var23) >>> 31 ^ 1) == 0) {
                           var25 = 1;
                           break label74;
                        }
                     }

                     var25 = 0;
                  }

                  byte var19 = var25;
                  this.e(
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

            this.H.o();
            this.V.f();
            this.e("Trap blocks missing from hotbar");
         }
      }
   }
}
