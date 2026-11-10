package com.larpclient.module;

import com.larp.obf.qa300483f54d804a3.q56b215c3695b1780;
import com.larpclient.c5;
import com.larpclient.cj;
import com.larpclient.j6;
import com.larpclient.mr;
import com.larpclient.p_;
import com.larpclient.t1;
import com.larpclient.ta;
import com.larpclient.wz;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class ScaffoldModule extends Module implements t1.s, mr.n {
   private static ScaffoldModule z;
   private final s m = this.addSetting(new s("Auto Switch", "Use suitable hotbar blocks and restore your selected slot", true));
   private final s L = this.addSetting(new s("Safe Walk", "Sneak at a ledge while grounded so you stop at the edge; jumping stays manual", true));
   private final s y = this.addSetting(new s("Keep Height", "Build at the height captured when enabled or when this setting changes", false));
   private final e o = this.addSetting(new e("Look Ahead", "Extra footprint prediction in your movement direction, in blocks", 0.35, 0.0, 0.6, 0.05));
   private final e h = this.addSetting(new e("Range", "Placement reach capped by native block reach", 4.1, 1.0, 6.0, 0.1));
   private final e O = this.addSetting(new e("Delay", "Extra ticks between placement attempts; zero allows one per tick", 0.0, 0.0, 10.0, 1.0));
   private final s Q = this.addSetting(new s("Pause Using Item", "Pause while eating or using an item", true));
   private final s E = this.addSetting(new s("Pause Mining", "Pause while the attack key is held", true));
   private final mr t = new mr(this, var1 -> this.addSetting(var1));
   private Object I;
   private Object j;
   private BlockPos C;
   private BlockPos d;
   private float[] T;
   private int S;
   private int K;
   private int u;
   private int V;
   private int W;
   private boolean X;
   private String N = "Inactive";
   private String x = "";
   private long B;
   private int i;
   private int Y;
   private int g;
   private int H;
   private int U;
   private boolean b;
   private long P;
   private float F = Float.NaN;
   private float k;
   private boolean A;
   private String p = "";
   private boolean a;
   private boolean w;
   private boolean R;
   private double c;
   private int r;
   private long v;
   private static final double n = 0.026;

   public ScaffoldModule() {
      super("Scaffold", "Bridge beneath your feet with visible player aim and free mouse look", Category.MOVEMENT, -1);
      z = this;
      this.y.o(var1 -> {
         this.h();
         if (this.isInGame()) {
            this.K = wz.e(mc.player.getY());
         }
      });
      this.m.o(var1 -> this.h());
   }

   public static ScaffoldModule l() {
      return z;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.U);
   }

   @Override
   public mr placementHighlight() {
      return this.t;
   }

   @Override
   public String getStatus() {
      return this.N;
   }

   @Override
   public boolean allowsDetachedCamera() {
      return true;
   }

   public List<q<?>> A() {
      return List.of(this.m, this.L, this.y, this.o, this.h, this.O, this.Q, this.E);
   }

   @Override
   public void onEnable() {
      getMc();
      this.h();
      this.u = 0;
      this.I = mc.level;
      this.j = mc.player;
      this.K = this.W = wz.e(mc.player.getY());
      this.N = "Ready";
      this.x = "";
      this.B = 0L;
      this.i = this.Y = this.g = this.r = this.H = this.U = 0;
      this.b = (boolean)0;
      this.P = this.v = 0L;
      this.F = Float.NaN;
      this.A = (boolean)0;
      this.a = (boolean)0;
      this.p = "";
      this.t.t();
      com.larpclient.h.A(
         this.getName(),
         "Enabled: real instant player aim, free camera (also behind Freelook and Freecam), native placement, safe walk (sneak at ledges)={}, keep height={}; clicks wait until the look already sent reaches the support; selected slot is restored",
         this.L.getValue(),
         this.y.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.h();
      this.I = this.j = null;
      this.u = 0;
      this.N = "Inactive";
      this.b = (boolean)0;
      this.a = (boolean)0;
      if (this.i > 0 || this.Y > 0 || this.g > 0 || this.r > 0) {
         com.larpclient.h.A(
            this.getName(),
            "Disabled after {} placed blocks ({} mid-air, {} of those below the feet's layer to support the jump); clicks waited {} ticks for the look already sent; Safe Walk sneaked at a ledge {} times; walked off a ledge unsneaked {} times",
            this.i,
            this.H,
            this.U,
            this.Y,
            this.g,
            this.r
         );
      }

      com.larpclient.h.U(this.getName(), "Released placement, independent camera and ledge protection");
   }

   private void h() {
      this.C = this.d = null;
      this.T = null;
      this.S = 0;
      this.X = (boolean)0;
      this.t.c();
      t1.v(this);
      p_.Y(p_.o.k);
      this.H("placement paused or ended");
   }

   private boolean b() {
      return this.isRuntimeActive()
         && mc != null
         && mc.player != null
         && mc.level != null
         && this.I == mc.level
         && this.j == mc.player
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && mc.gui.screen() == null
         && mc.isWindowActive();
   }

   private boolean s() {
      LocalPlayer var1 = mc.player;
      return !var1.isPassenger()
         && !var1.isFallFlying()
         && !var1.getAbilities().flying
         && !var1.noPhysics
         && !var1.isSleeping()
         && !var1.isMobilityRestricted()
         && !var1.onClimbable()
         && !var1.isInWater()
         && !var1.isInLava();
   }

   private static boolean K() {
      FlyModule var0 = FlyModule.S();
      AirStuckModule var1 = AirStuckModule.T();
      ElytraMotionModule var2 = ElytraMotionModule.v();
      return var0 != null && var0.f() || var1 != null && var1.I() || var2 != null && var2.K() || TargetStrafeModule.q();
   }

   private boolean h() {
      if (this.b() && this.s()) {
         LocalPlayer var1 = mc.player;
         return var1.fallDistance <= 3.0 && (!this.Q.getValue() || !var1.isUsingItem()) && (!this.E.getValue() || !mc.options.keyAttack.isDown());
      } else {
         return false;
      }
   }

   private static boolean t() {
      p_.o var0 = p_.A();
      return var0 == p_.o.t || var0 == p_.o.p;
   }

   private boolean g() {
      if (!this.h() || !ta.S(this) || ta.T()) {
         return false;
      } else {
         return !t() && !p_.I(p_.o.k) ? false : !K();
      }
   }

   private String k(Player var1) {
      if (!this.L.getValue()) {
         return "Safe Walk is off";
      } else if (!this.b()) {
         return "a screen, focus loss or session change";
      } else if (!this.s()) {
         return "vehicle, flight, climbing or liquid";
      } else if (K()) {
         return "another module drives your motion";
      } else if (!var1.onGround()) {
         return "not on the ground";
      } else {
         return !p_.V() && mc.options.keyShift.isDown() ? "you hold Shift" : null;
      }
   }

   public static float X() {
      ScaffoldModule var0 = z;
      Minecraft var1 = getMc();
      return var0 != null && !Float.isNaN(var0.F) && var1 != null && var1.player != null && var1.player == var0.j && var0.isRuntimeActive()
         ? var0.F
         : Float.NaN;
   }

   public static boolean G(Player var0) {
      if (z != null && z.isEnabled()) {
         ScaffoldModule var1 = z;
         return var1 != null && var1.b && var0 == getMc().player;
      } else {
         return false;
      }
   }

   public static void g() {
      if (z != null) {
         z.H("the outer camera ended");
      }
   }

   private void P() {
      this.F = mc.player.getYRot();
      this.k = mc.player.getXRot();
      String var1 = p_.A() == p_.o.p ? "Freecam" : "Freelook";
      if (!this.A) {
         this.A = (boolean)1;
         com.larpclient.h.A(
            this.getName(),
            "Bridging behind {}: the body turns to place while your keys keep walking toward {} degrees; it faces that way again when bridging pauses or the camera returns",
            var1,
            Math.round(Mth.wrapDegrees(this.F))
         );
      } else {
         com.larpclient.h.U(this.getName(), "Holding walking heading {} behind {}", Math.round(Mth.wrapDegrees(this.F)), var1);
      }
   }

   private void H(String var1) {
      if (!Float.isNaN(this.F)) {
         float var2 = this.F;
         float var3 = this.k;
         this.F = Float.NaN;
         LocalPlayer var4 = mc == null ? null : mc.player;
         if (var4 != null && var4 == this.j && var4.isAlive()) {
            float var5 = var4.getYRot() + Mth.wrapDegrees(var2 - var4.getYRot());
            var4.setYRot(var5);
            var4.setXRot(var3);
            var4.setYHeadRot(var5);
            var4.yRotO = var5;
            var4.xRotO = var3;
            com.larpclient.h.U(this.getName(), "Body faces the walking heading again: {}", var1);
         }
      }
   }

   public boolean n() {
      return !this.isEnabled() ? false : this.h() && (this.C != null || this.X && mc.player.tickCount - this.V >= 0 && mc.player.tickCount - this.V <= 3);
   }

   private float L() {
      return wz.J(c5.H(), mc.player.getYRot());
   }

   private Vec3 X() {
      LocalPlayer var1 = mc.player;
      Vec3 var2 = var1.getDeltaMovement();
      Vec3 var3 = wz.j(var2, c5.H(), var1.onGround() ? N(var1) : 0.026);
      double var4 = wz.J(var2.y, var1.onGround(), var1.input.keyPresses.jump(), var1.getAttributeValue(Attributes.JUMP_STRENGTH));
      return var1.getEyePosition().add(var3.x, var4, var3.z);
   }

   private float[] P(BlockPos var1) {
      Vec3 var2 = this.X();
      double var3 = this.h.getValue();
      return wz.i(
         var1,
         mc.player.getEyePosition(),
         this.L(),
         mc.player.getYRot(),
         mc.player.getXRot(),
         var5 -> t1.e(this, var1, var5, var3) != null && t1.d(this, var2, var1, var5, var3) != null,
         var4 -> t1.e(this, var1, var4, var3) != null
      );
   }

   private float[] G(BlockPos var1, Direction var2, boolean var3) {
      BlockPos var4 = var1.relative(var2);
      Vec3 var5 = this.X();
      double var6 = this.h.getValue();
      Predicate var8 = var5x -> {
         BlockHitResult var6x = t1.e(this, var1, var5x, var6);
         return var6x != null && var6x.getDirection() == var2;
      };
      return wz.Q(
         var1,
         var2,
         t1.F(var1, var2),
         mc.player.getEyePosition(),
         this.L(),
         mc.player.getYRot(),
         mc.player.getXRot(),
         var7 -> var8.test(var7) && (t1.d(this, var5, var1, var7, var6) != null || wz.x(var4, var5, var7, var6)),
         var8,
         var3
      );
   }

   private static double N(Player var0) {
      double var1 = var0.getAttributeValue(Attributes.MOVEMENT_SPEED);
      return var0.isSprinting() ? var1 : var1 * 1.3;
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (this.g() && ta.E(this)) {
            float[] var3 = this.d != null ? this.P(this.d) : this.T;
            if (var3 != null) {
               var1 = var3[0];
               var2 = var3[1];
            }

            boolean var4 = t();
            j6.U(this, j6.l.p, var1, var2, 1.0);
            if (!j6.g(j6.l.p)) {
               this.X = (boolean)0;
               if (!var4) {
                  p_.Y(p_.o.k);
               }
            } else {
               if (var4) {
                  if (Float.isNaN(this.F)) {
                     this.P();
                  }
               } else {
                  p_.p(p_.o.k);
               }

               this.X = (boolean)1;
               this.V = mc.player.tickCount;
            }
         }
      }
   }

   public float s(float var1) {
      if (!this.isEnabled()) {
         return var1;
      } else if (this.isRuntimeActive() && (p_.A() == p_.o.k || !Float.isNaN(X()))) {
         mc.player.setYHeadRot(mc.player.getYRot());
         return mc.player.getYRot();
      } else {
         return var1;
      }
   }

   public static boolean l(Player var0) {
      if (z != null && z.isEnabled()) {
         ScaffoldModule var1 = z;
         return var1 != null && var0 == getMc().player && var1.k(var0) == null;
      } else {
         return false;
      }
   }

   public static Input x(LocalPlayer var0, Input var1) {
      if (z != null && z.isEnabled()) {
         ScaffoldModule var2 = z;
         if (var2 == null) {
            return var1;
         }

         int var3 = var1.jump() && wz.Z(((q56b215c3695b1780)var0).larpNoJumpDelay()) ? 1 : 0;
         if (var0 == getMc().player && var2.isRuntimeActive()) {
            var2.a = (boolean)1;
            var2.w = var0.onGround();
            var2.R = (boolean)(!var1.shift() && var3 == 0 ? 0 : 1);
         }

         String var4 = l(var0) ? null : (var0 == getMc().player ? var2.k(var0) : "another player");
         if (!var1.shift() && var3 == 0 && var4 == null) {
            Vec2 var5 = var0.input.moveVector;
            float var6 = cj.b() && !cj.D() ? p_.z() : var0.getYRot();
            double var7 = Math.toRadians(var6);
            Vec3 var9 = new Vec3(var5.x * Math.cos(var7) - var5.y * Math.sin(var7), 0.0, var5.y * Math.cos(var7) + var5.x * Math.sin(var7));
            Vec3 var10 = wz.j(var0.getDeltaMovement(), var9, N(var0));
            boolean var11 = wz.c(var0.getBoundingBox(), var10, var0.maxUpStep(), var1x -> var0.level().noCollision(var0, var1x));
            var2.f(var11);
            var2.p = var11 ? "sneaked" : "the step looked safe";
            var2.c = var10.horizontalDistance();
            return var11 ? new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump(), true, var1.sprint()) : var1;
         } else {
            var2.b = (boolean)0;
            var2.p = var1.shift() ? "the body already sneaks" : (var3 != 0 ? "a jump" : var4);
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void f(boolean var1) {
      if (var1 && !this.b) {
         this.g++;
         long var2 = System.nanoTime();
         if (var2 - this.P >= 2000000000L) {
            this.P = var2;
            com.larpclient.h.A(
               this.getName(), "Safe Walk sneaked at a ledge ({} times since enable); vanilla sneak keeps you on the edge until a block is placed", this.g
            );
         }
      }

      this.b = var1;
   }

   public static boolean g(ItemStack var0, BlockGetter var1, BlockPos var2) {
      if (!var0.isEmpty() && var0.getItem() instanceof BlockItem var3) {
         Block var6 = var3.getBlock();
         BlockState var5 = var6.defaultBlockState();
         return !(var6 instanceof FallingBlock)
            && !(var6 instanceof TntBlock)
            && var6 != Blocks.MAGMA_BLOCK
            && var6 != Blocks.RESPAWN_ANCHOR
            && var6 != Blocks.CRAFTING_TABLE
            && var6 != Blocks.SMITHING_TABLE
            && var6 != Blocks.FLETCHING_TABLE
            && var6 != Blocks.NOTE_BLOCK
            && !var5.hasBlockEntity()
            && var5.isCollisionShapeFullBlock(var1, var2);
      } else {
         return false;
      }
   }

   private List<Item> N() {
      LinkedHashSet var1 = new LinkedHashSet();
      Inventory var2 = mc.player.getInventory();
      ItemStack var3 = mc.player.getMainHandItem();
      ItemStack var4 = mc.player.getOffhandItem();
      if (g(var3, mc.level, mc.player.blockPosition())) {
         var1.add(var3.getItem());
      }

      if (g(var4, mc.level, mc.player.blockPosition())) {
         var1.add(var4.getItem());
      }

      if (this.m.getValue()) {
         ArrayList var5 = new ArrayList();

         for (int var6 = 0; var6 < 9; var6++) {
            ItemStack var7 = var2.getItem(var6);
            if (g(var7, mc.level, mc.player.blockPosition())) {
               var5.add(var7);
            }
         }

         var5.sort(Comparator.comparingInt(ItemStack::getCount).reversed());
         var5.forEach(var1x -> var1.add(var1x.getItem()));
      }

      return List.copyOf(var1);
   }

   private List<List<BlockPos>> b() {
      Vec3 var1 = c5.H().scale(this.o.getValue());
      Vec3 var2 = mc.player.getDeltaMovement().multiply(1.0, 0.0, 1.0);
      int var3 = wz.e(mc.player.getY());
      return wz.I(
         mc.player.position(),
         mc.player.getBoundingBox(),
         var1.add(var2),
         this.y.getValue() ? List.of(this.K) : wz.s(var3, this.W),
         !mc.player.onGround(),
         t1::t,
         t1::j
      );
   }

   private boolean J(BlockPos var1) {
      return this.b().stream().anyMatch(var1x -> var1x.contains(var1));
   }

   private void o(String var1) {
      this.N = var1;
      long var2 = System.nanoTime();
      if (!var1.equals(this.x) && var2 - this.B >= 2000000000L) {
         this.x = var1;
         this.B = var2;
         com.larpclient.h.U(this.getName(), "{}; will retry when the current placement conditions permit", var1);
      }
   }

   private void Q() {
      if (this.a) {
         this.a = (boolean)0;
         LocalPlayer var1 = mc.player;
         if (var1 != null
            && var1 == this.j
            && this.w
            && !this.R
            && !var1.onGround()
            && !(var1.getDeltaMovement().y >= 0.0)
            && this.s()
            && !this.p.equals("Safe Walk is off")) {
            this.r++;
            long var2 = System.nanoTime();
            if (var2 - this.v >= 2000000000L) {
               this.v = var2;
               com.larpclient.h.A(
                  this.getName(),
                  "Walked off a ledge without the Safe Walk sneak ({} times since enable): {}{}; camera={}, status={}",
                  this.r,
                  this.p,
                  this.p.equals("the step looked safe") ? String.format(Locale.ROOT, " (travel bound %.3f blocks)", this.c) : "",
                  p_.A(),
                  this.N
               );
            }
         }
      }
   }

   @Override
   public void onTick() {
      this.Q();
      if (mc.player.onGround()) {
         this.W = wz.e(mc.player.getY());
      }

      if (!Float.isNaN(this.F) && !t()) {
         this.H("the outer camera ended");
      }

      BlockPos var1 = this.C;
      t1.v(this);
      this.C = this.d = null;
      this.T = null;
      if (!this.g()) {
         this.h();
         this.o("Paused: screen, movement, item use or another camera owns control");
      } else {
         if (!this.n()) {
            p_.Y(p_.o.k);
            this.H("no block to aim at");
         }

         int var20 = this.u;
         int var19 = mc.player.tickCount;
         if ((Integer.compare(var19, var20) >>> 31 ^ 1) == 0) {
            this.t.q(null);
            this.N = "Placement delay";
         } else {
            List var2 = this.N();
            if (var2.isEmpty()) {
               this.h();
               this.o("No suitable blocks in held hands or hotbar");
            } else {
               List var3 = this.b();
               ArrayList var4 = new ArrayList();

               label108:
               for (List var6 : var3) {
                  for (BlockPos var8 : var6) {
                     if (t1.t(var8)) {
                        for (Item var10 : var2) {
                           BlockHitResult var11 = t1.Q(this, var8, var10, this.h.getValue(), true, this.m.getValue());
                           if (var11 != null) {
                              boolean var12 = t1.U(this, var11.getBlockPos(), this.h.getValue());
                              var4.add(new ScaffoldModule.k(var8, var10, var11.getBlockPos(), var11.getDirection(), var12));
                              if (var12) {
                                 break label108;
                              }
                              break;
                           }
                        }
                     }
                  }

                  if (!var4.isEmpty()) {
                     break;
                  }
               }

               int var21 = wz.K(var4.stream().map(ScaffoldModule.k::sentAim).toList(), var4.stream().map(ScaffoldModule.k::pos).toList().indexOf(var1));
               if (var21 >= 0) {
                  ScaffoldModule.k var22 = (ScaffoldModule.k)var4.get(var21);
                  BlockPos var24 = var22.pos();
                  float[] var10000;
                  if (var22.sentAim()) {
                     BlockPos var10001 = var22.support();
                     Direction var10002 = var22.face();
                     boolean var14 = false;
                     int var13 = this.S;
                     var10000 = this.G(var10001, var10002, (-Integer.signum(var13) >>> 31 ^ 1) == 0);
                  } else {
                     var10000 = null;
                  }

                  float[] var26 = var10000;
                  int var27 = var26 != null ? 1 : 0;
                  this.C = var24;
                  boolean var28 = t1.W(
                     this,
                     var24,
                     var22.item(),
                     this.h.getValue(),
                     true,
                     this.m.getValue(),
                     () -> this.g() && var24.equals(this.C) && this.J(var24),
                     () -> var27,
                     var2x -> {
                        this.u = mc.player.tickCount + this.O.getValue().intValue() + 1;
                        if (var2x) {
                           this.i++;
                           if (!mc.player.onGround()) {
                              this.H++;
                              if (var24.getY() < wz.e(mc.player.getY())) {
                                 this.U++;
                              }
                           }
                        }

                        this.N = var2x ? "Bridging" : "Placement rejected; retrying";
                     }
                  );
                  if (var28) {
                     this.t.q(var24);
                     if (var27 != 0) {
                        this.T = var26;
                        this.S = 0;
                     } else {
                        byte var16 = 1;
                        int var15 = this.Y;
                        this.Y = var15 + var16;
                        this.d = var22.support();
                        if (var22.sentAim()) {
                           byte var18 = 1;
                           int var17 = this.S;
                           this.S = var17 + var18;
                        }
                     }

                     this.N = var27 != 0 ? "Aiming at bridge block" : "Turning before the click";
                     return;
                  }

                  this.C = null;
               }

               this.t.q(null);
               BlockPos var23 = wz.n(
                  var3.stream().flatMap(Collection::stream).toList(),
                  t1::t,
                  t1::j,
                  var1x -> t1.e(this, var1x, Vec3.atCenterOf(var1x).subtract(mc.player.getEyePosition()), this.h.getValue()) != null
               );
               if (var23 != null) {
                  this.d = var23;
                  float[] var25 = j6.s(mc.player.getEyePosition(), Vec3.atCenterOf(var23));
                  this.aimPlacement(var25[0], var25[1]);
                  this.N = "Looking at the next support";
               } else {
                  this.o("Waiting for a reachable support face beneath your path");
               }
            }
         }
      }
   }

   private record k(BlockPos pos, Item item, BlockPos support, Direction face, boolean sentAim) {
   }
}
