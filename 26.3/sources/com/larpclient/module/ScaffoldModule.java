package com.larpclient.module;

import com.larp.obf.q41a5851bcf8e876a.q0c224d8eb12ccb1e;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.ai;
import com.larpclient.b6;
import com.larpclient.dt;
import com.larpclient.ij;
import com.larpclient.n_;
import com.larpclient.nz;
import com.larpclient.vi;
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

public final class ScaffoldModule extends Module implements vi.d, ai.e {
   private static ScaffoldModule y;
   private final com.larpclient.module.m g = this.addSetting(
      new com.larpclient.module.m("Auto Switch", "Use suitable hotbar blocks and restore your selected slot", true)
   );
   private final com.larpclient.module.m n = this.addSetting(
      new com.larpclient.module.m("Safe Walk", "Sneak at a ledge while grounded so you stop at the edge; jumping stays manual", true)
   );
   private final com.larpclient.module.m u = this.addSetting(
      new com.larpclient.module.m("Keep Height", "Build at the height captured when enabled or when this setting changes", false)
   );
   private final b O = this.addSetting(new b("Look Ahead", "Extra footprint prediction in your movement direction, in blocks", 0.35, 0.0, 0.6, 0.05));
   private final b p = this.addSetting(new b("Range", "Placement reach capped by native block reach", 4.1, 1.0, 6.0, 0.1));
   private final b N = this.addSetting(new b("Delay", "Extra ticks between placement attempts; zero allows one per tick", 0.0, 0.0, 10.0, 1.0));
   private final com.larpclient.module.m C = this.addSetting(new com.larpclient.module.m("Pause Using Item", "Pause while eating or using an item", true));
   private final com.larpclient.module.m x = this.addSetting(new com.larpclient.module.m("Pause Mining", "Pause while the attack key is held", true));
   private final ai b = new ai(this, var1 -> this.addSetting(var1));
   private Object P;
   private Object l;
   private BlockPos A;
   private BlockPos m;
   private float[] k;
   private int e;
   private int w;
   private int z;
   private int Y;
   private int s;
   private boolean B;
   private String c = "Inactive";
   private String q = "";
   private long f;
   private int Q;
   private int t;
   private int h;
   private int j;
   private int L;
   private boolean S;
   private long i;
   private float R = Float.NaN;
   private float U;
   private boolean W;
   private String v = "";
   private boolean H;
   private boolean M;
   private boolean Z;
   private double a;
   private int d;
   private long J;
   private static final double D = 0.026;

   public ScaffoldModule() {
      super("Scaffold", "Bridge beneath your feet with visible player aim and free mouse look", Category.MOVEMENT, -1);
      y = this;
      this.u.w(var1 -> {
         this.y();
         if (this.isInGame()) {
            this.w = n_.T(mc.player.getY());
         }
      });
      this.g.w(var1 -> this.y());
   }

   public static ScaffoldModule Z() {
      return y;
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.k);
   }

   @Override
   public ai placementHighlight() {
      return this.b;
   }

   @Override
   public String getStatus() {
      return this.c;
   }

   @Override
   public boolean allowsDetachedCamera() {
      return true;
   }

   public List<r<?>> n() {
      return List.of(this.g, this.n, this.u, this.O, this.p, this.N, this.C, this.x);
   }

   @Override
   public void onEnable() {
      getMc();
      this.y();
      this.z = 0;
      this.P = mc.level;
      this.l = mc.player;
      this.w = this.s = n_.T(mc.player.getY());
      this.c = "Ready";
      this.q = "";
      this.f = 0L;
      this.Q = this.t = this.h = this.d = this.j = this.L = 0;
      this.S = (boolean)0;
      this.i = this.J = 0L;
      this.R = Float.NaN;
      this.W = (boolean)0;
      this.H = (boolean)0;
      this.v = "";
      this.b.b();
      _q.z(
         this.getName(),
         "Enabled: real instant player aim, free camera (also behind Freelook and Freecam), native placement, safe walk (sneak at ledges)={}, keep height={}; clicks wait until the look already sent reaches the support; selected slot is restored",
         this.n.getValue(),
         this.u.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.y();
      this.P = this.l = null;
      this.z = 0;
      this.c = "Inactive";
      this.S = (boolean)0;
      this.H = (boolean)0;
      if (this.Q > 0 || this.t > 0 || this.h > 0 || this.d > 0) {
         _q.z(
            this.getName(),
            "Disabled after {} placed blocks ({} mid-air, {} of those below the feet's layer to support the jump); clicks waited {} ticks for the look already sent; Safe Walk sneaked at a ledge {} times; walked off a ledge unsneaked {} times",
            this.Q,
            this.j,
            this.L,
            this.t,
            this.h,
            this.d
         );
      }

      _q.c(this.getName(), "Released placement, independent camera and ledge protection");
   }

   private void y() {
      this.A = this.m = null;
      this.k = null;
      this.e = 0;
      this.B = (boolean)0;
      this.b.m();
      vi.J(this);
      ij.W(ij.r.B);
      this.B("placement paused or ended");
   }

   private boolean M() {
      return this.isRuntimeActive()
         && mc != null
         && mc.player != null
         && mc.level != null
         && this.P == mc.level
         && this.l == mc.player
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && mc.gui.screen() == null
         && mc.isWindowActive();
   }

   private boolean r() {
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

   private static boolean h() {
      FlyModule var0 = FlyModule.Q();
      AirStuckModule var1 = AirStuckModule.Z();
      ElytraMotionModule var2 = ElytraMotionModule.n();
      return var0 != null && var0.r() || var1 != null && var1.J() || var2 != null && var2.Q() || TargetStrafeModule.t();
   }

   private boolean P() {
      if (this.M() && this.r()) {
         LocalPlayer var1 = mc.player;
         return var1.fallDistance <= 3.0 && (!this.C.getValue() || !var1.isUsingItem()) && (!this.x.getValue() || !mc.options.keyAttack.isDown());
      } else {
         return false;
      }
   }

   private static boolean J() {
      ij.r var0 = ij.L();
      return var0 == ij.r.i || var0 == ij.r.f;
   }

   private boolean T() {
      if (!this.P() || !_p.D(this) || _p.k()) {
         return false;
      } else {
         return !J() && !ij.a(ij.r.B) ? false : !h();
      }
   }

   private String R(Player var1) {
      if (!this.n.getValue()) {
         return "Safe Walk is off";
      } else if (!this.M()) {
         return "a screen, focus loss or session change";
      } else if (!this.r()) {
         return "vehicle, flight, climbing or liquid";
      } else if (h()) {
         return "another module drives your motion";
      } else if (!var1.onGround()) {
         return "not on the ground";
      } else {
         return !ij.y() && mc.options.keyShift.isDown() ? "you hold Shift" : null;
      }
   }

   public static float Y() {
      ScaffoldModule var0 = y;
      Minecraft var1 = getMc();
      return var0 != null && !Float.isNaN(var0.R) && var1 != null && var1.player != null && var1.player == var0.l && var0.isRuntimeActive()
         ? var0.R
         : Float.NaN;
   }

   public static boolean s(Player var0) {
      if (y != null && y.isEnabled()) {
         ScaffoldModule var1 = y;
         return var1 != null && var1.S && var0 == getMc().player;
      } else {
         return false;
      }
   }

   public static void x() {
      if (y != null) {
         y.B("the outer camera ended");
      }
   }

   private void Y() {
      this.R = mc.player.getYRot();
      this.U = mc.player.getXRot();
      String var1 = ij.L() == ij.r.f ? "Freecam" : "Freelook";
      if (!this.W) {
         this.W = (boolean)1;
         _q.z(
            this.getName(),
            "Bridging behind {}: the body turns to place while your keys keep walking toward {} degrees; it faces that way again when bridging pauses or the camera returns",
            var1,
            Math.round(Mth.wrapDegrees(this.R))
         );
      } else {
         _q.c(this.getName(), "Holding walking heading {} behind {}", Math.round(Mth.wrapDegrees(this.R)), var1);
      }
   }

   private void B(String var1) {
      if (!Float.isNaN(this.R)) {
         float var2 = this.R;
         float var3 = this.U;
         this.R = Float.NaN;
         LocalPlayer var4 = mc == null ? null : mc.player;
         if (var4 != null && var4 == this.l && var4.isAlive()) {
            float var5 = var4.getYRot() + Mth.wrapDegrees(var2 - var4.getYRot());
            var4.setYRot(var5);
            var4.setXRot(var3);
            var4.setYHeadRot(var5);
            var4.yRotO = var5;
            var4.xRotO = var3;
            _q.c(this.getName(), "Body faces the walking heading again: {}", var1);
         }
      }
   }

   public boolean a() {
      return !this.isEnabled() ? false : this.P() && (this.A != null || this.B && mc.player.tickCount - this.Y >= 0 && mc.player.tickCount - this.Y <= 3);
   }

   private float i() {
      return n_.B(nz.l(), mc.player.getYRot());
   }

   private Vec3 G() {
      LocalPlayer var1 = mc.player;
      Vec3 var2 = var1.getDeltaMovement();
      Vec3 var3 = n_.n(var2, nz.l(), var1.onGround() ? r(var1) : 0.026);
      double var4 = n_.h(var2.y, var1.onGround(), var1.input.keyPresses.jump(), var1.getAttributeValue(Attributes.JUMP_STRENGTH));
      return var1.getEyePosition().add(var3.x, var4, var3.z);
   }

   private float[] X(BlockPos var1) {
      Vec3 var2 = this.G();
      double var3 = this.p.getValue();
      return n_.v(
         var1,
         mc.player.getEyePosition(),
         this.i(),
         mc.player.getYRot(),
         mc.player.getXRot(),
         var5 -> vi.i(this, var1, var5, var3) != null && vi.y(this, var2, var1, var5, var3) != null,
         var4 -> vi.i(this, var1, var4, var3) != null
      );
   }

   private float[] M(BlockPos var1, Direction var2, boolean var3) {
      BlockPos var4 = var1.relative(var2);
      Vec3 var5 = this.G();
      double var6 = this.p.getValue();
      Predicate var8 = var5x -> {
         BlockHitResult var6x = vi.i(this, var1, var5x, var6);
         return var6x != null && var6x.getDirection() == var2;
      };
      return n_.z(
         var1,
         var2,
         vi.O(var1, var2),
         mc.player.getEyePosition(),
         this.i(),
         mc.player.getYRot(),
         mc.player.getXRot(),
         var7 -> var8.test(var7) && (vi.y(this, var5, var1, var7, var6) != null || n_.d(var4, var5, var7, var6)),
         var8,
         var3
      );
   }

   private static double r(Player var0) {
      double var1 = var0.getAttributeValue(Attributes.MOVEMENT_SPEED);
      return var0.isSprinting() ? var1 : var1 * 1.3;
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (this.T() && _p.Q(this)) {
            float[] var3 = this.m != null ? this.X(this.m) : this.k;
            if (var3 != null) {
               var1 = var3[0];
               var2 = var3[1];
            }

            boolean var4 = J();
            b6.P(this, b6.w.F, var1, var2, 1.0);
            if (!b6.c(b6.w.F)) {
               this.B = (boolean)0;
               if (!var4) {
                  ij.W(ij.r.B);
               }
            } else {
               if (var4) {
                  if (Float.isNaN(this.R)) {
                     this.Y();
                  }
               } else {
                  ij.V(ij.r.B);
               }

               this.B = (boolean)1;
               this.Y = mc.player.tickCount;
            }
         }
      }
   }

   public float f(float var1) {
      if (!this.isEnabled()) {
         return var1;
      } else if (this.isRuntimeActive() && (ij.L() == ij.r.B || !Float.isNaN(Y()))) {
         mc.player.setYHeadRot(mc.player.getYRot());
         return mc.player.getYRot();
      } else {
         return var1;
      }
   }

   public static boolean X(Player var0) {
      if (y != null && y.isEnabled()) {
         ScaffoldModule var1 = y;
         return var1 != null && var0 == getMc().player && var1.R(var0) == null;
      } else {
         return false;
      }
   }

   public static Input z(LocalPlayer var0, Input var1) {
      if (y != null && y.isEnabled()) {
         ScaffoldModule var2 = y;
         if (var2 == null) {
            return var1;
         }

         int var3 = var1.jump() && n_.u(((q0c224d8eb12ccb1e)var0).larpNoJumpDelay()) ? 1 : 0;
         if (var0 == getMc().player && var2.isRuntimeActive()) {
            var2.H = (boolean)1;
            var2.M = var0.onGround();
            var2.Z = (boolean)(!var1.shift() && var3 == 0 ? 0 : 1);
         }

         String var4 = X(var0) ? null : (var0 == getMc().player ? var2.R(var0) : "another player");
         if (!var1.shift() && var3 == 0 && var4 == null) {
            Vec2 var5 = var0.input.moveVector;
            float var6 = dt.x() && !dt.q() ? ij.f() : var0.getYRot();
            double var7 = Math.toRadians(var6);
            Vec3 var9 = new Vec3(var5.x * Math.cos(var7) - var5.y * Math.sin(var7), 0.0, var5.y * Math.cos(var7) + var5.x * Math.sin(var7));
            Vec3 var10 = n_.n(var0.getDeltaMovement(), var9, r(var0));
            boolean var11 = n_.l(var0.getBoundingBox(), var10, var0.maxUpStep(), var1x -> var0.level().noCollision(var0, var1x));
            var2.Y(var11);
            var2.v = var11 ? "sneaked" : "the step looked safe";
            var2.a = var10.horizontalDistance();
            return var11 ? new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump(), true, var1.sprint()) : var1;
         } else {
            var2.S = (boolean)0;
            var2.v = var1.shift() ? "the body already sneaks" : (var3 != 0 ? "a jump" : var4);
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void Y(boolean var1) {
      if (var1 && !this.S) {
         this.h++;
         long var2 = System.nanoTime();
         if (var2 - this.i >= 2000000000L) {
            this.i = var2;
            _q.z(this.getName(), "Safe Walk sneaked at a ledge ({} times since enable); vanilla sneak keeps you on the edge until a block is placed", this.h);
         }
      }

      this.S = var1;
   }

   public static boolean C(ItemStack var0, BlockGetter var1, BlockPos var2) {
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

   private List<Item> T() {
      LinkedHashSet var1 = new LinkedHashSet();
      Inventory var2 = mc.player.getInventory();
      ItemStack var3 = mc.player.getMainHandItem();
      ItemStack var4 = mc.player.getOffhandItem();
      if (C(var3, mc.level, mc.player.blockPosition())) {
         var1.add(var3.getItem());
      }

      if (C(var4, mc.level, mc.player.blockPosition())) {
         var1.add(var4.getItem());
      }

      if (this.g.getValue()) {
         ArrayList var5 = new ArrayList();

         for (int var6 = 0; var6 < 9; var6++) {
            ItemStack var7 = var2.getItem(var6);
            if (C(var7, mc.level, mc.player.blockPosition())) {
               var5.add(var7);
            }
         }

         var5.sort(Comparator.comparingInt(ItemStack::getCount).reversed());
         var5.forEach(var1x -> var1.add(var1x.getItem()));
      }

      return List.copyOf(var1);
   }

   private List<List<BlockPos>> I() {
      Vec3 var1 = nz.l().scale(this.O.getValue());
      Vec3 var2 = mc.player.getDeltaMovement().multiply(1.0, 0.0, 1.0);
      int var3 = n_.T(mc.player.getY());
      return n_.F(
         mc.player.position(),
         mc.player.getBoundingBox(),
         var1.add(var2),
         this.u.getValue() ? List.of(this.w) : n_.X(var3, this.s),
         !mc.player.onGround(),
         vi::i,
         vi::k
      );
   }

   private boolean z(BlockPos var1) {
      return this.I().stream().anyMatch(var1x -> var1x.contains(var1));
   }

   private void C(String var1) {
      this.c = var1;
      long var2 = System.nanoTime();
      if (!var1.equals(this.q) && var2 - this.f >= 2000000000L) {
         this.q = var1;
         this.f = var2;
         _q.c(this.getName(), "{}; will retry when the current placement conditions permit", var1);
      }
   }

   private void M() {
      if (this.H) {
         this.H = (boolean)0;
         LocalPlayer var1 = mc.player;
         if (var1 != null
            && var1 == this.l
            && this.M
            && !this.Z
            && !var1.onGround()
            && !(var1.getDeltaMovement().y >= 0.0)
            && this.r()
            && !this.v.equals("Safe Walk is off")) {
            this.d++;
            long var2 = System.nanoTime();
            if (var2 - this.J >= 2000000000L) {
               this.J = var2;
               _q.z(
                  this.getName(),
                  "Walked off a ledge without the Safe Walk sneak ({} times since enable): {}{}; camera={}, status={}",
                  this.d,
                  this.v,
                  this.v.equals("the step looked safe") ? String.format(Locale.ROOT, " (travel bound %.3f blocks)", this.a) : "",
                  ij.L(),
                  this.c
               );
            }
         }
      }
   }

   @Override
   public void onTick() {
      this.M();
      if (mc.player.onGround()) {
         this.s = n_.T(mc.player.getY());
      }

      if (!Float.isNaN(this.R) && !J()) {
         this.B("the outer camera ended");
      }

      BlockPos var1 = this.A;
      vi.J(this);
      this.A = this.m = null;
      this.k = null;
      if (!this.T()) {
         this.y();
         this.C("Paused: screen, movement, item use or another camera owns control");
      } else {
         if (!this.a()) {
            ij.W(ij.r.B);
            this.B("no block to aim at");
         }

         int var18 = this.z;
         int var17 = mc.player.tickCount;
         if ((Integer.compare(var17, var18) >>> 31 ^ 1) == 0) {
            this.b.H(null);
            this.c = "Placement delay";
         } else {
            List var2 = this.T();
            if (var2.isEmpty()) {
               this.y();
               this.C("No suitable blocks in held hands or hotbar");
            } else {
               List var3 = this.I();
               ArrayList var4 = new ArrayList();

               label108:
               for (List var6 : var3) {
                  for (BlockPos var8 : var6) {
                     if (vi.i(var8)) {
                        for (Item var10 : var2) {
                           BlockHitResult var11 = vi.B(this, var8, var10, this.p.getValue(), true, this.g.getValue());
                           if (var11 != null) {
                              boolean var12 = vi.V(this, var11.getBlockPos(), this.p.getValue());
                              var4.add(new ScaffoldModule.m(var8, var10, var11.getBlockPos(), var11.getDirection(), var12));
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

               int var21 = n_.j(var4.stream().map(ScaffoldModule.m::sentAim).toList(), var4.stream().map(ScaffoldModule.m::pos).toList().indexOf(var1));
               if (var21 >= 0) {
                  ScaffoldModule.m var22 = (ScaffoldModule.m)var4.get(var21);
                  BlockPos var24 = var22.pos();
                  float[] var10000;
                  if (var22.sentAim()) {
                     BlockPos var10001 = var22.support();
                     Direction var10002 = var22.face();
                     boolean var20 = false;
                     int var19 = this.e;
                     var10000 = this.M(var10001, var10002, (-Integer.signum(var19) >>> 31 ^ 1) == 0);
                  } else {
                     var10000 = null;
                  }

                  float[] var26 = var10000;
                  int var27 = var26 != null ? 1 : 0;
                  this.A = var24;
                  boolean var28 = vi.M(
                     this,
                     var24,
                     var22.item(),
                     this.p.getValue(),
                     true,
                     this.g.getValue(),
                     () -> this.T() && var24.equals(this.A) && this.z(var24),
                     () -> var27,
                     var2x -> {
                        this.z = mc.player.tickCount + this.N.getValue().intValue() + 1;
                        if (var2x) {
                           this.Q++;
                           if (!mc.player.onGround()) {
                              this.j++;
                              if (var24.getY() < n_.T(mc.player.getY())) {
                                 this.L++;
                              }
                           }
                        }

                        this.c = var2x ? "Bridging" : "Placement rejected; retrying";
                     }
                  );
                  if (var28) {
                     this.b.H(var24);
                     if (var27 != 0) {
                        this.k = var26;
                        this.e = 0;
                     } else {
                        byte var14 = 1;
                        int var13 = this.t;
                        this.t = var13 + var14;
                        this.m = var22.support();
                        if (var22.sentAim()) {
                           byte var16 = 1;
                           int var15 = this.e;
                           this.e = var15 + var16;
                        }
                     }

                     this.c = var27 != 0 ? "Aiming at bridge block" : "Turning before the click";
                     return;
                  }

                  this.A = null;
               }

               this.b.H(null);
               BlockPos var23 = n_.B(
                  var3.stream().flatMap(Collection::stream).toList(),
                  vi::i,
                  vi::k,
                  var1x -> vi.i(this, var1x, Vec3.atCenterOf(var1x).subtract(mc.player.getEyePosition()), this.p.getValue()) != null
               );
               if (var23 != null) {
                  this.m = var23;
                  float[] var25 = b6.R(mc.player.getEyePosition(), Vec3.atCenterOf(var23));
                  this.aimPlacement(var25[0], var25[1]);
                  this.c = "Looking at the next support";
               } else {
                  this.C("Waiting for a reachable support face beneath your path");
               }
            }
         }
      }
   }

   private record m(BlockPos pos, Item item, BlockPos support, Direction face, boolean sentAim) {
   }
}
