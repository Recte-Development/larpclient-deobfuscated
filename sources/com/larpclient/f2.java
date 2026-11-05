package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.RedstoneWireBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public final class f2 {
   private static final f2 g = new f2();
   private static final double u = 0.5;
   private static final double F = 1.1;
   private static final double r = 0.85;
   private static final double H = 0.05;
   private static final double V = 0.99;
   private static final double B = 11.7;
   private static final double k = 12.5;
   private static final int d = 120;
   private static final double b = 10.0;
   private static final double M = 4096.0;
   private static final double O = 64.0;
   private static final double s = 4.0;
   public boolean p;
   private f2.u l = f2.u.Q;
   private final Random L = new Random();
   private final List<f2.o> S = new ArrayList<>();
   private BlockPos C;
   private BlockState t;
   private final List<f2.l> e = new ArrayList<>();
   private ClientLevel v;
   private long A = -1L;
   private long x = -1L;
   private final Map<BlockPos, BlockState> f = new LinkedHashMap<>();
   private ClientLevel R;
   private int a = -2;
   private long E = -1L;
   private boolean D;
   private final Map<String, int[]> T = new LinkedHashMap<>();
   private final Map<String, int[]> n = new LinkedHashMap<>();
   private final Map<String, Integer> y = new LinkedHashMap<>();
   private final int[] Y = new int[36];
   private final int[] Q = new int[36];
   private int Z = 0;
   private int q = 0;
   private ItemStack i = null;
   private ItemStack I = null;

   private static boolean t(ItemFrame var0) {
      return var0.getDirection() == Direction.UP;
   }

   private static Vec3 P(Level var0, BlockPos var1) {
      Vec3 var2 = new Vec3(var1.getX() + 0.5, var1.getY() + 0.5, var1.getZ() + 0.5);
      BlockState var3 = var0.getBlockState(var1);
      if (!(var3.getBlock() instanceof DispenserBlock)) {
         return var2.add(0.0, 0.25, 0.0);
      }

      Direction var4 = (Direction)var3.getValue(DispenserBlock.FACING);
      return var2.add(var4.getStepX() * 0.6, var4.getStepY() * 0.6, var4.getStepZ() * 0.6);
   }

   private f2() {
   }

   public static f2 f() {
      return g;
   }

   public void X(String var1) {
      if (var1 == null) {
         this.l = f2.u.Q;
      } else if ("win".equalsIgnoreCase(var1)) {
         this.l = f2.u.f;
      } else if ("lose".equalsIgnoreCase(var1)) {
         this.l = f2.u.R;
      } else {
         this.l = f2.u.Q;
      }

      if (this.l == f2.u.Q) {
         this.D = (boolean)0;
      }
   }

   public void J() {
      this.l = switch (this.l) {
         case Q -> f2.u.f;
         case f -> f2.u.R;
         case R -> f2.u.Q;
      };
      if (this.l == f2.u.Q) {
         hh.K("Arrow Game", "Target off");
      } else {
         hh.K("Arrow Game", "Target " + this.l.J);
      }
   }

   public String v() {
      return this.l == null ? "off" : this.l.J;
   }

   public String w() {
      return this.l == null ? "Off" : this.l.T;
   }

   public void Q() {
      this.D = (boolean)1;
   }

   public boolean v() {
      return this.D;
   }

   public void O() {
      this.D = (boolean)0;
   }

   public boolean a() {
      return o7.a().g();
   }

   public boolean l(Player var1) {
      if (var1 == null) {
         return false;
      }

      ItemStack var2 = var1.getMainHandItem();
      return var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP) ? var2.getComponents().get(DataComponents.MAP_ID) != null : false;
   }

   public void s() {
      Minecraft var1 = Minecraft.getInstance();
      LocalPlayer var2 = var1 != null ? var1.player : null;
      if (!this.l(var2)) {
         hh.K("Arrow Game", "Hold a filled map in your hand to set the X map");
      } else {
         MapId var3 = (MapId)var2.getMainHandItem().getComponents().get(DataComponents.MAP_ID);
         o7.a().g(var3.id());
         this.D = (boolean)0;
         hh.K(
            "Arrow Game",
            "X map set to map #" + var3.id() + " - frames holding this map are the LOSE target, any other item (elytra, block, ...) is the WIN target"
         );
      }
   }

   public void u() {
      o7.a().g(-1);
      this.D = (boolean)0;
      hh.K("Arrow Game", "X map cleared - falling back to filled=win, empty=lose");
   }

   private static boolean P(ItemFrame var0) {
      return var0 != null && var0.getItem() != null && !var0.getItem().isEmpty();
   }

   private static boolean D(ItemFrame var0) {
      return P(var0) && var0.getItem().is(Items.FILLED_MAP) ? var0.getItem().getComponents().get(DataComponents.MAP_ID) != null : false;
   }

   private static boolean S(ItemFrame var0) {
      if (!D(var0)) {
         return false;
      }

      int var1 = o7.a().a();
      return var1 < 0 ? false : ((MapId)var0.getItem().getComponents().get(DataComponents.MAP_ID)).id() == var1;
   }

   private static boolean Q(ItemFrame var0) {
      return !P(var0) ? false : !S(var0);
   }

   public boolean P(Minecraft var1, BlockPos var2) {
      if (!this.p || this.l == f2.u.Q) {
         return false;
      }

      if (var1 != null && var1.player != null && var1.level != null && var2 != null) {
         BlockState var3 = var1.level.getBlockState(var2);
         if (!(var3.getBlock() instanceof LeverBlock)) {
            return false;
         }

         BlockPos var4 = f9.W().R(f9.b.F);
         if (var4 == null) {
            return false;
         }

         if (K(var2, var4) > 4.0) {
            return false;
         }

         int var5 = !var3.getValue(BlockStateProperties.POWERED) ? 1 : 0;
         this.t(var1, var2, var3);
         if (var5 != 0) {
            this.r(var1, var4);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean t(BlockPos var1) {
      return o7.a().o(var1);
   }

   public void Z(Minecraft var1, BlockPos var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         BlockState var3 = var1.level.getBlockState(var2);
         if (var3.getBlock() instanceof NoteBlock) {
            o7.a().o(var2);
            hh.K("Arrow Game", "Bound noteblock at " + var2.toShortString());
         }
      }
   }

   public void W() {
      o7.a().o(null);
   }

   public boolean H(Minecraft var1, BlockPos var2) {
      if (!this.p || this.l == f2.u.Q) {
         return false;
      }

      if (var1 == null || var1.player == null || var1.level == null || var2 == null) {
         return false;
      }

      if (!this.t(var2)) {
         return false;
      }

      BlockState var3 = var1.level.getBlockState(var2);
      if (!(var3.getBlock() instanceof NoteBlock)) {
         return false;
      }

      BlockPos var4 = f9.W().R(f9.b.F);
      if (var4 == null) {
         return false;
      }

      if (this.v != var1.level) {
         this.v();
      }

      long var5 = var1.level.getGameTime();
      if (var5 < this.A) {
         return true;
      }

      this.A = var5 + 4L;
      this.H(var1, var3, var2);
      this.C(var1, var2);
      this.v = var1.level;
      this.e.add(new f2.l(var4.immutable(), this.l, var1.level.getGameTime() + 6L));
      return true;
   }

   private void H(Minecraft var1, BlockState var2, BlockPos var3) {
      if (var1.level != null && var1.player != null) {
         NoteBlockInstrument var4 = (NoteBlockInstrument)var2.getValue(NoteBlock.INSTRUMENT);
         int var5 = (Integer)var2.getValue(NoteBlock.NOTE);
         float var6;
         if (var4.isTunable()) {
            var1.level.addParticle(ParticleTypes.NOTE, var3.getX() + 0.5, var3.getY() + 1.2, var3.getZ() + 0.5, var5 / 24.0, 0.0, 0.0);
            var6 = NoteBlock.getPitchFromNote(var5);
         } else {
            var6 = 1.0F;
         }

         if (!var4.hasCustomSound()) {
            Holder var7 = var4.getSoundEvent();
            var1.level.playSound(var1.player, var3, (SoundEvent)var7.value(), SoundSource.RECORDS, 3.0F, var6);
         }
      }
   }

   private void C(Minecraft var1, BlockPos var2) {
      this.h(var1);
      BlockPos var3 = var2.below();
      BlockState var4 = var1.level.getBlockState(var3);
      if (var4.getBlock() instanceof ObserverBlock && var4.getValue(ObserverBlock.FACING) == Direction.UP) {
         this.R = var1.level;
         this.f.put(var3, var4);
         ArrayDeque var5 = new ArrayDeque();
         BlockPos var6 = var3.below();
         var5.add(var6);

         for (Direction var10 : Direction.values()) {
            var5.add(var6.relative(var10));
         }

         while (!var5.isEmpty() && this.f.size() < 32) {
            BlockPos var14 = (BlockPos)var5.removeFirst();
            if (!this.f.containsKey(var14) && !(K(var14, var3) > 256.0)) {
               BlockState var16 = var1.level.getBlockState(var14);
               BlockState var18 = A(var16);
               if (var18 != null) {
                  this.f.put(var14, var16);

                  for (Direction var13 : Direction.values()) {
                     var5.add(var14.relative(var13));
                  }
               }
            }
         }

         for (Entry var17 : this.f.entrySet()) {
            BlockState var19 = ((BlockState)var17.getValue()).getBlock() instanceof ObserverBlock
               ? (BlockState)((BlockState)var17.getValue()).setValue(BlockStateProperties.POWERED, true)
               : A((BlockState)var17.getValue());
            var1.level.setBlock((BlockPos)var17.getKey(), var19, 2);
         }

         this.x = var1.level.getGameTime() + 4L;
      }
   }

   static BlockState A(BlockState var0) {
      if (var0.getBlock() instanceof RedstoneWireBlock) {
         return (BlockState)var0.setValue(RedstoneWireBlock.POWER, 15);
      } else if (var0.getBlock() instanceof RepeaterBlock) {
         return (BlockState)var0.setValue(BlockStateProperties.POWERED, true);
      } else {
         return var0.getBlock() instanceof RedstoneLampBlock ? (BlockState)var0.setValue(BlockStateProperties.LIT, true) : null;
      }
   }

   private void h(Minecraft var1) {
      if (var1.level != null && var1.level == this.R) {
         for (Entry var3 : this.f.entrySet()) {
            BlockState var4 = ((BlockState)var3.getValue()).getBlock() instanceof ObserverBlock
               ? (BlockState)((BlockState)var3.getValue()).setValue(BlockStateProperties.POWERED, true)
               : A((BlockState)var3.getValue());
            if (var4 != null && var1.level.getBlockState((BlockPos)var3.getKey()).equals(var4)) {
               var1.level.setBlock((BlockPos)var3.getKey(), (BlockState)var3.getValue(), 2);
            }
         }
      }

      this.f.clear();
      this.R = null;
      this.x = -1L;
   }

   private static AABB A(Vec3 var0) {
      return new AABB(var0.x - 64.0, var0.y - 64.0, var0.z - 64.0, var0.x + 64.0, var0.y + 64.0, var0.z + 64.0);
   }

   private Vec3 G(ItemFrame var1) {
      AABB var2 = var1.getBoundingBox();
      double var3 = 0.2;
      double var5 = Math.max(0.0, var2.getXsize() - 2.0 * var3);
      double var7 = Math.max(0.0, var2.getYsize() - 2.0 * var3);
      double var9 = Math.max(0.0, var2.getZsize() - 2.0 * var3);
      return new Vec3(
         var2.minX + var3 + this.L.nextDouble() * var5, var2.minY + var3 + this.L.nextDouble() * var7, var2.minZ + var3 + this.L.nextDouble() * var9
      );
   }

   public void n(Minecraft var1) {
      xs.r(var1);
      if (this.v != var1.level) {
         this.v();
         this.v = var1.level;
      }

      if (var1.level != null) {
         if (this.x >= 0L && var1.level.getGameTime() >= this.x) {
            this.h(var1);
         }

         if (!this.p) {
            this.D = (boolean)0;
            this.A(var1);
         }

         Iterator var2 = this.e.iterator();

         while (var2.hasNext()) {
            f2.l var3 = (f2.l)var2.next();
            if (var1.level.getGameTime() >= var3.due()) {
               var2.remove();
               this.S(var1, var3.dispenser(), var3.target());
            }
         }

         if (!this.S.isEmpty()) {
            long var8 = var1.level.getGameTime();
            long var4 = var8 - this.E;
            if (var4 > 0L) {
               this.E = var8;

               for (long var6 = 0L; var6 < var4 && !this.S.isEmpty(); var6++) {
                  this.G(var1);
               }
            }
         }
      }
   }

   private void G(Minecraft var1) {
      Iterator var2 = this.S.iterator();

      while (var2.hasNext()) {
         f2.o var3 = (f2.o)var2.next();
         if (var3.F == null) {
            this.y(var1, var3);
            var2.remove();
         } else if (var3.o <= 0) {
            var3.V++;
            var3.v *= 0.99;
            var3.H = var3.H * 0.99 - 0.05;
            var3.a *= 0.99;
            var3.I = var3.I.add(var3.v, var3.H, var3.a);
            double var15 = Math.sqrt(var3.v * var3.v + var3.a * var3.a);
            var3.F.setYRot((float)Math.toDegrees(Math.atan2(var3.v, var3.a)));
            var3.F.setXRot((float)Math.toDegrees(Math.atan2(var3.H, Math.max(var15, 1.0E-4))));
            var3.F.setPos(var3.I.x, var3.I.y, var3.I.z);
            if (var3.V >= 120 || var3.A != null && i(var3.I.subtract(var3.v, var3.H, var3.a), var3.I, var3.A) <= 0.5) {
               var3.U = new Vec3(var3.I.x, var3.I.y, var3.I.z);
               var3.F.discard();
               var3.F = null;
               var2.remove();
               this.y(var1, var3);
            }
         } else if (var3.S >= var3.o) {
            var3.U = var3.I;
            var3.F.discard();
            var3.F = null;
            var2.remove();
            this.y(var1, var3);
         } else {
            Vec3 var4 = var3.I;
            var3.S = P(var3.S, var3.o);
            int var5 = Math.min((int)var3.S, var3.o - 1);
            double var6 = var3.S - var5;
            double[] var8 = var5 == 0 ? new double[3] : var3.X[var5 - 1];
            double[] var9 = var3.X[var5];
            var3.I = new Vec3(
               var3.w.x + var8[0] + (var9[0] - var8[0]) * var6,
               var3.w.y + var8[1] + (var9[1] - var8[1]) * var6,
               var3.w.z + var8[2] + (var9[2] - var8[2]) * var6
            );
            var3.v = var3.I.x - var4.x;
            var3.H = var3.I.y - var4.y;
            var3.a = var3.I.z - var4.z;
            double var10 = Math.sqrt(var3.v * var3.v + var3.a * var3.a);
            float var12 = (float)Math.toDegrees(Math.atan2(var3.v, var3.a));
            float var13 = (float)Math.toDegrees(Math.atan2(var3.H, Math.max(var10, 1.0E-4)));
            var3.b = l(var3.b, var12);
            var3.W = l(var3.W, var13);
            var3.F.setYRot(var3.b);
            var3.F.setXRot(var3.W);
            var3.F.setPos(var3.I.x, var3.I.y, var3.I.z);
            var3.V++;
            int var14 = !(var3.S >= var3.o) && (var3.A == null || !(i(var4, var3.I, var3.A) <= 0.5)) ? 0 : 1;
            if (var14 != 0) {
               var3.U = var3.I;
               var3.F.discard();
               var3.F = null;
               var2.remove();
               this.y(var1, var3);
            }
         }
      }
   }

   static double P(double var0, int var2) {
      return Math.min(var0 + 0.85, var2);
   }

   private void r(Minecraft var1, BlockPos var2) {
      this.S(var1, var2, this.l);
   }

   private void S(Minecraft var1, BlockPos var2, f2.u var3) {
      this.v = var1.level;
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      Vec3 var6 = new Vec3(var2.getX() + 0.5, var2.getY() + 0.5, var2.getZ() + 0.5);
      AABB var7 = A(var6);
      o7 var8 = o7.a();
      boolean var9 = var8.g();
      hh.L("Arrow beginShot: dispenser " + var2 + ", target " + var3.J + ", xMapSet=" + var9 + ", xMapId=" + var8.a() + ", serverKey=" + var8.q());
      if (var1.level != null) {
         for (ItemFrame var11 : var1.level.getEntitiesOfClass(ItemFrame.class, var7, var0 -> !var0.isRemoved() && t(var0))) {
            if (!(var11.distanceToSqr(var6) > 4096.0)) {
               byte var12;
               String var13;
               if (var9) {
                  if (S(var11)) {
                     var12 = 0;
                     var13 = "xMap";
                  } else {
                     if (!P(var11)) {
                        hh.L("Arrow frame " + var11.blockPosition() + " skipped (empty)");
                        continue;
                     }

                     var12 = 1;
                     var13 = "item";
                  }
               } else {
                  byte var14 = P(var11);
                  var12 = var14;
                  var13 = "filled=" + var14;
               }

               hh.L(
                  "Arrow frame "
                     + var11.blockPosition()
                     + " id="
                     + var11.getId()
                     + " dist="
                     + String.format("%.1f", Math.sqrt(var11.distanceToSqr(var6)))
                     + " "
                     + var13
                     + " -> "
                     + (var12 != 0 ? "WIN" : "LOSE")
               );
               if (var12 != 0) {
                  var4.add(var11);
               } else {
                  var5.add(var11);
               }
            }
         }
      }

      ArrayList var17 = var3 == f2.u.f ? var4 : var5;
      if (var17.isEmpty()) {
         hh.K(
            "Arrow Game",
            "No "
               + var3.J
               + " frames found near the dispenser ("
               + var4.size()
               + (var9 ? " item" : " filled")
               + ", "
               + var5.size()
               + (var9 ? " x-map" : " empty")
               + ")"
         );
      } else {
         ArrayList var19 = new ArrayList();

         for (f2.o var22 : this.S) {
            var19.add(var22.M);
         }

         ArrayList var21 = new ArrayList();

         for (ItemFrame var25 : var17) {
            if (var19.contains(var25.getId())) {
               hh.L("Arrow frame " + var25.getId() + " already targeted, not re-firing");
            } else {
               var21.add(var25);
            }
         }

         if (var21.isEmpty()) {
            hh.K("Arrow Game", "Every reachable " + var3.J + " frame already has an arrow in flight; skipping this flick");
         } else {
            var17 = var21;
            int var24 = 0;
            if (var1.level != null) {
               double var26 = 64.0;
               AABB var16 = new AABB(var6.x - var26, var6.y - var26, var6.z - var26, var6.x + var26, var6.y + var26, var6.z + var26);
               var24 = var1.level.getEntitiesOfClass(ItemFrame.class, var16, var0 -> !var0.isRemoved() && t(var0)).size();
            }

            hh.K(
               "Arrow Game",
               "Scan found "
                  + var4.size()
                  + (var9 ? " item" : " filled")
                  + " and "
                  + var5.size()
                  + (var9 ? " x-map" : " empty")
                  + " frames in range ("
                  + (var4.size() + var5.size())
                  + " total; "
                  + var24
                  + " item frames within 64 blocks); target is "
                  + var3.J
            );
            Vec3 var27 = P(var1.level, var2);
            ItemFrame var15 = (ItemFrame)var17.get(this.L.nextInt(var17.size()));
            f2.o var28 = new f2.o();
            var28.M = var15.getId();
            var28.I = var27;
            var28.A = this.G(var15);
            var28.w = var28.I;
            if (var1.level != null) {
               var28.F = (Arrow)EntityTypes.ARROW.create(var1.level, EntitySpawnReason.COMMAND);
               if (var28.F != null) {
                  var28.F.setId(this.a--);
                  var28.F.setNoGravity(true);
                  var28.F.setNoPhysics(true);
                  var28.F.setPos(var28.I.x, var28.I.y, var28.I.z);
                  this.l(var28);
                  var1.level.addEntity(var28.F);
               }

               var1.level
                  .playSound(null, var28.I.x, var28.I.y, var28.I.z, SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 1.0F, 0.4F + this.L.nextFloat() * 1.2F);
            }

            this.S.add(var28);
            this.D(var1, var2);
            if (var1.level != null) {
               this.E = var1.level.getGameTime();
            }

            hh.K("Arrow Game", "Firing at " + var3.J + " frame " + var28.M);
         }
      }
   }

   private void l(f2.o var1) {
      if (var1.A != null) {
         var1.w = var1.I;
         double var2 = 11.7 + this.L.nextDouble() * 0.8000000000000007;
         var1.y = var2;
         if (this.f(var1)) {
            var1.S = 0.0;
            this.m(var1, var1.k[0][0], var1.k[0][1], var1.k[0][2]);
            hh.L(
               "Arrow trajectory targetCrest="
                  + String.format("%.1f", var1.y)
                  + " speed="
                  + String.format("%.2f", var1.R)
                  + " pitch="
                  + String.format("%.1f", var1.l)
                  + "deg crest="
                  + String.format("%.1f", var1.p)
                  + " ticks="
                  + var1.o
                  + " to "
                  + var1.A
            );
         } else {
            Vec3 var4 = var1.A.subtract(var1.I).normalize();
            var1.v = var4.x * 1.1;
            var1.H = var4.y * 1.1;
            var1.a = var4.z * 1.1;
            this.m(var1, var1.v, var1.H, var1.a);
            hh.L("Arrow trajectory FAILED -> straight shot from " + var1.I + " to " + var1.A);
         }
      }
   }

   private void m(f2.o var1, double var2, double var4, double var6) {
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      float var10 = (float)Math.toDegrees(Math.atan2(var2, var6));
      float var11 = (float)Math.toDegrees(Math.atan2(var4, var8));
      var1.b = var10;
      var1.W = var11;
      if (var1.F != null) {
         var1.F.setYRot(var10);
         var1.F.setXRot(var11);
         var1.F.yRotO = var10;
         var1.F.xRotO = var11;
      }
   }

   private boolean f(f2.o var1) {
      double var2 = var1.A.x - var1.I.x;
      double var4 = var1.A.y - var1.I.y;
      double var6 = var1.A.z - var1.I.z;
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      if (var8 < 0.001) {
         return false;
      }

      f2.m.d var10 = f2.m.H(var2, var4, var6, var1.y);
      if (var10 == null) {
         return false;
      }

      var1.X = var10.i;
      var1.k = var10.P;
      var1.o = var10.x;
      var1.l = var10.q;
      var1.R = var10.h;
      var1.g = 0.0;
      var1.p = var10.S;
      var1.E = var10.x;
      var1.v = var10.N;
      var1.H = var10.c;
      var1.a = var10.L;
      return true;
   }

   private static double i(Vec3 var0, Vec3 var1, Vec3 var2) {
      Vec3 var3 = var1.subtract(var0);
      Vec3 var4 = var2.subtract(var0);
      double var5 = var3.dot(var3);
      double var7 = var5 > 1.0E-9 ? var4.dot(var3) / var5 : 0.0;
      var7 = Math.max(0.0, Math.min(1.0, var7));
      return var2.distanceTo(var0.add(var3.scale(var7)));
   }

   private static float l(float var0, float var1) {
      float var2 = ((var1 - var0) % 360.0F + 540.0F) % 360.0F - 180.0F;
      return var0 + var2 * 0.2F;
   }

   private void y(Minecraft var1, f2.o var2) {
      if (var1 != null && var1.level != null) {
         Vec3 var3 = var2.U != null ? var2.U : var2.I;
         ItemFrame var5 = var1.level.getEntity(var2.M) instanceof ItemFrame var6 && !var6.isRemoved() ? var6 : null;
         if (var5 == null) {
            hh.K("Arrow Game", "Arrow target unloaded before impact; no visual item dropped");
         } else {
            hh.L(
               "Arrow landing target: id="
                  + var5.getId()
                  + " at "
                  + var5.blockPosition()
                  + " (arrow landed at "
                  + String.format("%.1f,%.1f,%.1f", var3.x, var3.y, var3.z)
                  + ")"
            );
            this.q(var1, var5, var3);
            xs.L(var1, var5);
            hh.K("Arrow Game", "Visual hit: item popped from frame " + var5.getId());
         }
      }
   }

   private void q(Minecraft var1, ItemFrame var2, Vec3 var3) {
      if (var1 != null && var1.level != null && var2 != null) {
         Vec3 var4 = var3 != null ? var3 : var2.getBoundingBox().getCenter();
         if (var1.getSoundManager() != null) {
            var1.getSoundManager()
               .play(
                  new SimpleSoundInstance(
                     SoundEvents.ARROW_HIT.location(),
                     SoundSource.NEUTRAL,
                     2.0F,
                     0.8F + this.L.nextFloat() * 0.4F,
                     RandomSource.create(),
                     false,
                     0,
                     Attenuation.NONE,
                     var4.x,
                     var4.y,
                     var4.z,
                     false
                  )
               );
         }
      }
   }

   private boolean n(ItemFrame var1) {
      if (var1 == null) {
         return false;
      }

      if (o7.a().g()) {
         return this.l == f2.u.f ? Q(var1) : S(var1);
      }

      byte var2 = P(var1);
      return (boolean)(this.l == f2.u.f ? var2 : var2 == 0);
   }

   private void t(Minecraft var1, BlockPos var2, BlockState var3) {
      this.C = var2.immutable();
      if (this.t == null) {
         this.t = var3;
      }

      int var4 = !var3.getValue(BlockStateProperties.POWERED) ? 1 : 0;
      if (var1.level != null) {
         var1.level.setBlockAndUpdate(var2, (BlockState)var3.setValue(BlockStateProperties.POWERED, Boolean.valueOf((boolean)var4)));
      }

      if (var1.player != null) {
         var1.player.playSound(SoundEvents.LEVER_CLICK, 0.3F, var4 != 0 ? 0.6F : 0.5F);
      }
   }

   private void A(Minecraft var1) {
      if (var1 != null && var1.level != null && this.C != null) {
         if (this.t != null) {
            var1.level.setBlockAndUpdate(this.C, this.t);
         }

         this.C = null;
         this.t = null;
      }
   }

   public void v() {
      this.D = (boolean)0;
      this.h(Minecraft.getInstance());

      for (f2.o var2 : this.S) {
         if (var2.F != null) {
            var2.F.discard();
         }
      }

      this.S.clear();
      this.e.clear();
      this.A = -1L;
      this.E = -1L;
      Minecraft var3 = Minecraft.getInstance();
      this.A(var3);
   }

   public void D(Minecraft var1, BlockPos var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         String var3 = f9.W().x(var1, var2);
         int[] var4 = this.n.get(var3);
         if (var4 == null) {
            this.y.merge(var3, 1, Integer::sum);
         } else {
            ArrayList var5 = new ArrayList();

            for (int var6 = 0; var6 < var4.length; var6++) {
               if (var4[var6] > 0) {
                  var5.add(var6);
               }
            }

            if (!var5.isEmpty()) {
               int var7 = (Integer)var5.get(this.L.nextInt(var5.size()));
               var4[var7] = Math.max(0, var4[var7] - 1);
            }
         }
      }
   }

   public void w(Minecraft var1, DispenserMenu var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         this.Z = 0;
         this.q = 0;
         int[] var3 = new int[9];
         byte var4 = 0;

         for (Slot var6 : var2.slots) {
            if (!(var6.container instanceof Inventory) && var6.getContainerSlot() >= 0 && var6.getContainerSlot() <= 8 && var6.hasItem()) {
               ItemStack var7 = var6.getItem();
               if (D(var7)) {
                  var3[var6.getContainerSlot()] = var7.getCount();
                  var4 = 1;
               }
            }
         }

         if (var4 != 0) {
            String var12 = f9.W().v();
            if (var12 != null) {
               int[] var13 = this.T.get(var12);
               this.T.put(var12, var3);
               int[] var14 = this.n.get(var12);
               if (var14 == null) {
                  var14 = (int[])var3.clone();
                  this.n.put(var12, var14);
               }

               for (int var8 = 0; var8 < var14.length; var8++) {
                  if (var3[var8] == 0) {
                     var14[var8] = 0;
                  } else if (var13 != null && var13[var8] != 0) {
                     var14[var8] = Math.min(var14[var8], var3[var8]);
                  } else {
                     var14[var8] = var3[var8];
                  }
               }

               Integer var15 = this.y.remove(var12);

               for (int var9 = var15 == null ? 0 : var15; var9 > 0; var9--) {
                  ArrayList var10 = new ArrayList();

                  for (int var11 = 0; var11 < var14.length; var11++) {
                     if (var14[var11] > 0) {
                        var10.add(var11);
                     }
                  }

                  if (var10.isEmpty()) {
                     break;
                  }

                  int var16 = (Integer)var10.get(this.L.nextInt(var10.size()));
                  var14[var16] = Math.max(0, var14[var16] - 1);
               }
            }
         }
      }
   }

   public void C(String var1, int var2, ItemStack var3) {
      if (var1 != null) {
         this.i = null;
         this.I = null;
         int var4 = D(var3) ? var3.getCount() : 0;
         if (var2 >= 0 && var2 <= 8) {
            int[] var10 = this.T.get(var1);
            int[] var11 = this.n.get(var1);
            if (var10 == null || var11 == null) {
               return;
            }

            int var12 = var10[var2];
            int var13 = var11[var2];
            int var9 = this.s(var12, var13, var4);
            var10[var2] = var4;
            var11[var2] = var9;
         } else if (var2 >= 9 && var2 <= 44) {
            int var5 = var2 >= 36 ? var2 - 36 : var2;
            if (var5 < 0 || var5 >= 36) {
               return;
            }

            int var6 = this.Q[var5];
            int var7 = this.Y[var5];
            int var8 = this.s(var6, var7, var4);
            this.Q[var5] = var4;
            this.Y[var5] = var8;
         }
      }
   }

   public void d(int var1, ItemStack var2) {
      if (var1 >= 0 && var1 < 36) {
         this.i = null;
         this.I = null;
         if (this.Y[var1] != 0 || this.Q[var1] != 0 || this.Z != 0) {
            int var3 = D(var2) ? var2.getCount() : 0;
            int var4 = this.Q[var1];
            int var5 = this.Y[var1];
            this.Q[var1] = var3;
            this.Y[var1] = this.s(var4, var5, var3);
         }
      }
   }

   private int s(int var1, int var2, int var3) {
      if (var3 <= 0) {
         if (var1 > 0 && var2 > 0) {
            this.Z += var1;
            this.q = this.q + Math.max(0, var1 - var2);
         }

         return 0;
      } else {
         if (var1 == var3) {
            return Math.min(var2, var3);
         }

         if (var3 > var1) {
            int var8 = var3 - var1;
            int var9 = var8;
            if (this.Z > 0) {
               int var6 = Math.min(var8, this.Z);
               int var7 = Math.round((float)((long)this.q * var6 / Math.max(1, this.Z)));
               var9 = var8 - var7;
               this.Z -= var6;
               this.q = Math.max(0, this.q - var7);
            }

            return Math.min(var2 + Math.max(0, var9), var3);
         } else {
            int var4 = var1 - var3;
            int var5 = Math.round((float)((long)var2 * var4 / Math.max(1, var1)));
            this.Z += var4;
            this.q = this.q + Math.max(0, var4 - Math.min(var5, var4));
            return Math.max(0, var2 - var5);
         }
      }
   }

   public ItemStack b(Minecraft var1, int var2, ItemStack var3) {
      if (var1 == null || var1.level == null || var3 == null || var3.isEmpty()) {
         return null;
      }

      if (var2 >= 0 && var2 <= 8) {
         f9 var4 = f9.W();
         String var5 = var4.v();
         if (var5 == null) {
            return null;
         } else {
            int[] var6 = this.n.get(var5);
            if (var6 == null) {
               return null;
            } else {
               int var7 = Math.min(var6[var2], var3.getCount());
               if (var7 == var3.getCount()) {
                  return null;
               } else {
                  return var7 <= 0 ? ItemStack.EMPTY : var3.copyWithCount(var7);
               }
            }
         }
      } else {
         return null;
      }
   }

   public ItemStack P(int var1, ItemStack var2) {
      if (var2 == null || var2.isEmpty() || !D(var2)) {
         return null;
      } else if (var1 >= 0 && var1 < 36) {
         int var3 = this.Y[var1];
         return var3 > 0 && var3 < var2.getCount() ? var2.copyWithCount(var3) : null;
      } else {
         return null;
      }
   }

   public ItemStack f(ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && D(var1)) {
         if (this.I != null && !this.I.isEmpty() && this.i != null && var1.is(this.i.getItem()) && var1.getCount() == this.i.getCount()) {
            return this.I.copy();
         }

         if (this.Z > 0 && this.q > 0) {
            int var2 = var1.getCount() - this.q;
            if (var2 >= var1.getCount()) {
               return var1;
            } else {
               return var2 <= 0 ? ItemStack.EMPTY : var1.copyWithCount(var2);
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public void i(ItemStack var1, ItemStack var2) {
      if (var1 != null && !var1.isEmpty()) {
         this.i = var1;
         this.I = var2 != null && !var2.isEmpty() ? var2 : var1;
      } else {
         this.i = null;
         this.I = null;
      }
   }

   public boolean Z() {
      return this.p && this.l != f2.u.Q;
   }

   private static boolean D(ItemStack var0) {
      return var0.is(Items.ARROW) || var0.is(Items.SPECTRAL_ARROW) || var0.is(Items.TIPPED_ARROW);
   }

   private static double K(BlockPos var0, BlockPos var1) {
      int var2 = var0.getX() - var1.getX();
      int var3 = var0.getY() - var1.getY();
      int var4 = var0.getZ() - var1.getZ();
      return var2 * var2 + var3 * var3 + var4 * var4;
   }

   private record l(BlockPos dispenser, f2.u target, long due) {
   }

   private static final class m {
      static final int n = 24;
      static final int w = 80;

      private static double P(int var0) {
         return 0.99 * (1.0 - Math.pow(0.99, var0)) / 0.010000000000000009;
      }

      static f2.m.d H(double var0, double var2, double var4, double var6) {
         double var8 = Math.sqrt(var0 * var0 + var4 * var4);
         if (var8 < 0.001) {
            return null;
         }

         int var10 = 24;
         double var11 = Double.MAX_VALUE;
         double var13 = 0.0;

         for (int var15 = 24; var15 <= 80; var15++) {
            double var16 = P(var15);
            double var18 = (var2 + 0.05 * (var15 - var16) / 0.010000000000000009) / var16;
            double var20 = 0.0;
            double var22 = var18;
            double var24 = 0.0;

            for (int var26 = 0; var26 < var15; var26++) {
               var22 = var22 * 0.99 - 0.05;
               var20 += var22;
               if (var20 > var24) {
                  var24 = var20;
               }
            }

            double var48 = Math.abs(var24 - var6);
            if (var48 < var11 - 1.0E-9 || Math.abs(var48 - var11) <= 1.0E-9 && var15 > var10) {
               var11 = var48;
               var10 = var15;
               var13 = var24;
            }
         }

         int var42 = var10;
         double var43 = P(var42);
         double var44 = var0 / var43;
         double var45 = var4 / var43;
         double var46 = (var2 + 0.05 * (var42 - var43) / 0.010000000000000009) / var43;
         double[][] var47 = new double[var42][];
         double[][] var25 = new double[var42][];
         double var49 = 0.0;
         double var28 = 0.0;
         double var30 = 0.0;
         double var32 = var44;
         double var34 = var46;
         double var36 = var45;

         for (int var38 = 0; var38 < var42; var38++) {
            var32 *= 0.99;
            var34 = var34 * 0.99 - 0.05;
            var36 *= 0.99;
            var49 += var32;
            var28 += var34;
            var30 += var36;
            var47[var38] = new double[]{var49, var28, var30};
            var25[var38] = new double[]{var32, var34, var36};
         }

         var47[var42 - 1][0] = var0;
         var47[var42 - 1][1] = var2;
         var47[var42 - 1][2] = var4;
         double var50 = Math.toDegrees(Math.atan2(var46, Math.hypot(var44, var45)));
         double var40 = Math.sqrt(var44 * var44 + var46 * var46 + var45 * var45);
         return new f2.m.d(var42, var47, var25, var44, var46, var45, var13, var50, var40);
      }

      static final class d {
         final int x;
         final double[][] i;
         final double[][] P;
         final double N;
         final double c;
         final double L;
         final double S;
         final double q;
         final double h;

         d(int var1, double[][] var2, double[][] var3, double var4, double var6, double var8, double var10, double var12, double var14) {
            this.x = var1;
            this.i = var2;
            this.P = var3;
            this.N = var4;
            this.c = var6;
            this.L = var8;
            this.S = var10;
            this.q = var12;
            this.h = var14;
         }
      }
   }

   private static final class o {
      Vec3 I;
      Vec3 A;
      Vec3 U;
      int M;
      int V;
      float b;
      float W;
      double v;
      double H;
      double a;
      Arrow F;
      double[][] X;
      double[][] k;
      int o;
      double S;
      Vec3 w;
      double y;
      double l;
      double R;
      double g;
      double p;
      int E;
   }

   public enum u {
      Q("off", "Off"),
      f("win", "Win (filled frames)"),
      R("lose", "Lose (empty frames)");

      public final String J;
      public final String T;

      u(String var3, String var4) {
         this.J = var3;
         this.T = var4;
      }
   }
}
