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
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public final class bs {
   private static final bs Z = new bs();
   private static final double g = 0.5;
   private static final double A = 1.1;
   private static final double F = 0.85;
   private static final double d = 0.05;
   private static final double w = 0.99;
   private static final double U = 11.7;
   private static final double k = 12.5;
   private static final int t = 120;
   private static final double D = 10.0;
   private static final double n = 4096.0;
   private static final double S = 64.0;
   private static final double O = 4.0;
   public boolean l;
   private bs.c z = bs.c.B;
   private final Random o = new Random();
   private final List<bs.o> P = new ArrayList<>();
   private BlockPos Q;
   private BlockState f;
   private final List<bs.t> X = new ArrayList<>();
   private ClientLevel b;
   private long Y = -1L;
   private long v = -1L;
   private final Map<BlockPos, BlockState> y = new LinkedHashMap<>();
   private ClientLevel i;
   private int p = -2;
   private long W = -1L;
   private boolean K;
   private final Map<String, int[]> T = new LinkedHashMap<>();
   private final Map<String, int[]> J = new LinkedHashMap<>();
   private final Map<String, Integer> H = new LinkedHashMap<>();
   private final int[] c = new int[36];
   private final int[] R = new int[36];
   private int V = 0;
   private int e = 0;
   private ItemStack u = null;
   private ItemStack m = null;

   private static boolean y(ItemFrame var0) {
      return var0.getDirection() == Direction.UP;
   }

   private static Vec3 G(Level var0, BlockPos var1) {
      Vec3 var2 = new Vec3(var1.getX() + 0.5, var1.getY() + 0.5, var1.getZ() + 0.5);
      BlockState var3 = var0.getBlockState(var1);
      if (!(var3.getBlock() instanceof DispenserBlock)) {
         return var2.add(0.0, 0.25, 0.0);
      }

      Direction var4 = (Direction)var3.getValue(DispenserBlock.FACING);
      return var2.add(var4.getStepX() * 0.6, var4.getStepY() * 0.6, var4.getStepZ() * 0.6);
   }

   private bs() {
   }

   public static bs R() {
      return Z;
   }

   public void H(String var1) {
      if (var1 == null) {
         this.z = bs.c.B;
      } else if ("win".equalsIgnoreCase(var1)) {
         this.z = bs.c.D;
      } else if ("lose".equalsIgnoreCase(var1)) {
         this.z = bs.c.d;
      } else {
         this.z = bs.c.B;
      }

      if (this.z == bs.c.B) {
         this.K = (boolean)0;
      }
   }

   public void L() {
      this.z = switch (this.z) {
         case B -> bs.c.D;
         case D -> bs.c.d;
         case d -> bs.c.B;
      };
      if (this.z == bs.c.B) {
         te.Z("Arrow Game", "Target off");
      } else {
         te.Z("Arrow Game", "Target " + this.z.b);
      }
   }

   public String V() {
      return this.z == null ? "off" : this.z.b;
   }

   public String J() {
      return this.z == null ? "Off" : this.z.r;
   }

   public void B() {
      this.K = (boolean)1;
   }

   public boolean X() {
      return this.K;
   }

   public void w() {
      this.K = (boolean)0;
   }

   public boolean P() {
      return com.larpclient.b.m().W();
   }

   public boolean i(Player var1) {
      if (var1 == null) {
         return false;
      }

      ItemStack var2 = var1.getMainHandItem();
      return var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP) ? var2.getComponents().get(DataComponents.MAP_ID) != null : false;
   }

   public void i() {
      Minecraft var1 = Minecraft.getInstance();
      LocalPlayer var2 = var1 != null ? var1.player : null;
      if (!this.i(var2)) {
         te.Z("Arrow Game", "Hold a filled map in your hand to set the X map");
      } else {
         MapId var3 = (MapId)var2.getMainHandItem().getComponents().get(DataComponents.MAP_ID);
         com.larpclient.b.m().A(var3.id());
         this.K = (boolean)0;
         te.Z(
            "Arrow Game",
            "X map set to map #" + var3.id() + " - frames holding this map are the LOSE target, any other item (elytra, block, ...) is the WIN target"
         );
      }
   }

   public void A() {
      com.larpclient.b.m().A(-1);
      this.K = (boolean)0;
      te.Z("Arrow Game", "X map cleared - falling back to filled=win, empty=lose");
   }

   private static boolean Y(ItemFrame var0) {
      return var0 != null && var0.getItem() != null && !var0.getItem().isEmpty();
   }

   private static boolean A(ItemFrame var0) {
      return Y(var0) && var0.getItem().is(Items.FILLED_MAP) ? var0.getItem().getComponents().get(DataComponents.MAP_ID) != null : false;
   }

   private static boolean t(ItemFrame var0) {
      if (!A(var0)) {
         return false;
      }

      int var1 = com.larpclient.b.m().a();
      return var1 < 0 ? false : ((MapId)var0.getItem().getComponents().get(DataComponents.MAP_ID)).id() == var1;
   }

   private static boolean n(ItemFrame var0) {
      return !Y(var0) ? false : !t(var0);
   }

   public boolean W(Minecraft var1, BlockPos var2) {
      if (!this.l || this.z == bs.c.B) {
         return false;
      }

      if (var1 != null && var1.player != null && var1.level != null && var2 != null) {
         BlockState var3 = var1.level.getBlockState(var2);
         if (!(var3.getBlock() instanceof LeverBlock)) {
            return false;
         }

         BlockPos var4 = gj.c().q(gj.d.g);
         if (var4 == null) {
            return false;
         }

         if (e(var2, var4) > 4.0) {
            return false;
         }

         int var5 = !var3.getValue(BlockStateProperties.POWERED) ? 1 : 0;
         this.x(var1, var2, var3);
         if (var5 != 0) {
            this.u(var1, var4);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean o(BlockPos var1) {
      return com.larpclient.b.m().J(var1);
   }

   public void i(Minecraft var1, BlockPos var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         BlockState var3 = var1.level.getBlockState(var2);
         if (var3.getBlock() instanceof NoteBlock) {
            com.larpclient.b.m().j(var2);
            te.Z("Arrow Game", "Bound noteblock at " + var2.toShortString());
         }
      }
   }

   public void e() {
      com.larpclient.b.m().j(null);
   }

   public boolean x(Minecraft var1, BlockPos var2) {
      if (!this.l || this.z == bs.c.B) {
         return false;
      }

      if (var1 == null || var1.player == null || var1.level == null || var2 == null) {
         return false;
      }

      if (!this.o(var2)) {
         return false;
      }

      BlockState var3 = var1.level.getBlockState(var2);
      if (!(var3.getBlock() instanceof NoteBlock)) {
         return false;
      }

      BlockPos var4 = gj.c().q(gj.d.g);
      if (var4 == null) {
         return false;
      }

      if (this.b != var1.level) {
         this.d();
      }

      long var5 = var1.level.getGameTime();
      if (var5 < this.Y) {
         return true;
      }

      this.Y = var5 + 4L;
      this.U(var1, var3, var2);
      this.o(var1, var2);
      this.b = var1.level;
      this.X.add(new bs.t(var4.immutable(), this.z, var1.level.getGameTime() + 6L));
      return true;
   }

   private void U(Minecraft var1, BlockState var2, BlockPos var3) {
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

   private void o(Minecraft var1, BlockPos var2) {
      this.G(var1);
      BlockPos var3 = var2.below();
      BlockState var4 = var1.level.getBlockState(var3);
      if (var4.getBlock() instanceof ObserverBlock && var4.getValue(ObserverBlock.FACING) == Direction.UP) {
         this.i = var1.level;
         this.y.put(var3, var4);
         ArrayDeque var5 = new ArrayDeque();
         BlockPos var6 = var3.below();
         var5.add(var6);

         for (Direction var10 : Direction.values()) {
            var5.add(var6.relative(var10));
         }

         while (!var5.isEmpty() && this.y.size() < 32) {
            BlockPos var14 = (BlockPos)var5.removeFirst();
            if (!this.y.containsKey(var14) && !(e(var14, var3) > 256.0)) {
               BlockState var16 = var1.level.getBlockState(var14);
               BlockState var18 = o(var16);
               if (var18 != null) {
                  this.y.put(var14, var16);

                  for (Direction var13 : Direction.values()) {
                     var5.add(var14.relative(var13));
                  }
               }
            }
         }

         for (Entry var17 : this.y.entrySet()) {
            BlockState var19 = ((BlockState)var17.getValue()).getBlock() instanceof ObserverBlock
               ? (BlockState)((BlockState)var17.getValue()).setValue(BlockStateProperties.POWERED, true)
               : o((BlockState)var17.getValue());
            var1.level.setBlock((BlockPos)var17.getKey(), var19, 2);
         }

         this.v = var1.level.getGameTime() + 4L;
      }
   }

   static BlockState o(BlockState var0) {
      if (var0.getBlock() instanceof RedStoneWireBlock) {
         return (BlockState)var0.setValue(RedStoneWireBlock.POWER, 15);
      } else if (var0.getBlock() instanceof RepeaterBlock) {
         return (BlockState)var0.setValue(BlockStateProperties.POWERED, true);
      } else {
         return var0.getBlock() instanceof RedstoneLampBlock ? (BlockState)var0.setValue(BlockStateProperties.LIT, true) : null;
      }
   }

   private void G(Minecraft var1) {
      if (var1.level != null && var1.level == this.i) {
         for (Entry var3 : this.y.entrySet()) {
            BlockState var4 = ((BlockState)var3.getValue()).getBlock() instanceof ObserverBlock
               ? (BlockState)((BlockState)var3.getValue()).setValue(BlockStateProperties.POWERED, true)
               : o((BlockState)var3.getValue());
            if (var4 != null && var1.level.getBlockState((BlockPos)var3.getKey()).equals(var4)) {
               var1.level.setBlock((BlockPos)var3.getKey(), (BlockState)var3.getValue(), 2);
            }
         }
      }

      this.y.clear();
      this.i = null;
      this.v = -1L;
   }

   private static AABB o(Vec3 var0) {
      return new AABB(var0.x - 64.0, var0.y - 64.0, var0.z - 64.0, var0.x + 64.0, var0.y + 64.0, var0.z + 64.0);
   }

   private Vec3 j(ItemFrame var1) {
      AABB var2 = var1.getBoundingBox();
      double var3 = 0.2;
      double var5 = Math.max(0.0, var2.getXsize() - 2.0 * var3);
      double var7 = Math.max(0.0, var2.getYsize() - 2.0 * var3);
      double var9 = Math.max(0.0, var2.getZsize() - 2.0 * var3);
      return new Vec3(
         var2.minX + var3 + this.o.nextDouble() * var5, var2.minY + var3 + this.o.nextDouble() * var7, var2.minZ + var3 + this.o.nextDouble() * var9
      );
   }

   public void v(Minecraft var1) {
      sz.K(var1);
      if (this.b != var1.level) {
         this.d();
         this.b = var1.level;
      }

      if (var1.level != null) {
         if (this.v >= 0L && var1.level.getGameTime() >= this.v) {
            this.G(var1);
         }

         if (!this.l) {
            this.K = (boolean)0;
            this.p(var1);
         }

         Iterator var2 = this.X.iterator();

         while (var2.hasNext()) {
            bs.t var3 = (bs.t)var2.next();
            if (var1.level.getGameTime() >= var3.due()) {
               var2.remove();
               this.J(var1, var3.dispenser(), var3.target());
            }
         }

         if (!this.P.isEmpty()) {
            long var8 = var1.level.getGameTime();
            long var4 = var8 - this.W;
            if (var4 > 0L) {
               this.W = var8;

               for (long var6 = 0L; var6 < var4 && !this.P.isEmpty(); var6++) {
                  this.z(var1);
               }
            }
         }
      }
   }

   private void z(Minecraft var1) {
      Iterator var2 = this.P.iterator();

      while (var2.hasNext()) {
         bs.o var3 = (bs.o)var2.next();
         if (var3.Q == null) {
            this.I(var1, var3);
            var2.remove();
         } else if (var3.v <= 0) {
            var3.D++;
            var3.R *= 0.99;
            var3.y = var3.y * 0.99 - 0.05;
            var3.j *= 0.99;
            var3.J = var3.J.add(var3.R, var3.y, var3.j);
            double var15 = Math.sqrt(var3.R * var3.R + var3.j * var3.j);
            var3.Q.setYRot((float)Math.toDegrees(Math.atan2(var3.R, var3.j)));
            var3.Q.setXRot((float)Math.toDegrees(Math.atan2(var3.y, Math.max(var15, 1.0E-4))));
            var3.Q.setPos(var3.J.x, var3.J.y, var3.J.z);
            if (var3.D >= 120 || var3.h != null && R(var3.J.subtract(var3.R, var3.y, var3.j), var3.J, var3.h) <= 0.5) {
               var3.Z = new Vec3(var3.J.x, var3.J.y, var3.J.z);
               var3.Q.discard();
               var3.Q = null;
               var2.remove();
               this.I(var1, var3);
            }
         } else if (var3.m >= var3.v) {
            var3.Z = var3.J;
            var3.Q.discard();
            var3.Q = null;
            var2.remove();
            this.I(var1, var3);
         } else {
            Vec3 var4 = var3.J;
            var3.m = V(var3.m, var3.v);
            int var5 = Math.min((int)var3.m, var3.v - 1);
            double var6 = var3.m - var5;
            double[] var8 = var5 == 0 ? new double[3] : var3.O[var5 - 1];
            double[] var9 = var3.O[var5];
            var3.J = new Vec3(
               var3.P.x + var8[0] + (var9[0] - var8[0]) * var6,
               var3.P.y + var8[1] + (var9[1] - var8[1]) * var6,
               var3.P.z + var8[2] + (var9[2] - var8[2]) * var6
            );
            var3.R = var3.J.x - var4.x;
            var3.y = var3.J.y - var4.y;
            var3.j = var3.J.z - var4.z;
            double var10 = Math.sqrt(var3.R * var3.R + var3.j * var3.j);
            float var12 = (float)Math.toDegrees(Math.atan2(var3.R, var3.j));
            float var13 = (float)Math.toDegrees(Math.atan2(var3.y, Math.max(var10, 1.0E-4)));
            var3.V = B(var3.V, var12);
            var3.Y = B(var3.Y, var13);
            var3.Q.setYRot(var3.V);
            var3.Q.setXRot(var3.Y);
            var3.Q.setPos(var3.J.x, var3.J.y, var3.J.z);
            var3.D++;
            int var14 = !(var3.m >= var3.v) && (var3.h == null || !(R(var4, var3.J, var3.h) <= 0.5)) ? 0 : 1;
            if (var14 != 0) {
               var3.Z = var3.J;
               var3.Q.discard();
               var3.Q = null;
               var2.remove();
               this.I(var1, var3);
            }
         }
      }
   }

   static double V(double var0, int var2) {
      return Math.min(var0 + 0.85, var2);
   }

   private void u(Minecraft var1, BlockPos var2) {
      this.J(var1, var2, this.z);
   }

   private void J(Minecraft var1, BlockPos var2, bs.c var3) {
      this.b = var1.level;
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      Vec3 var6 = new Vec3(var2.getX() + 0.5, var2.getY() + 0.5, var2.getZ() + 0.5);
      AABB var7 = o(var6);
      b var8 = com.larpclient.b.m();
      boolean var9 = var8.W();
      te.R("Arrow beginShot: dispenser " + var2 + ", target " + var3.b + ", xMapSet=" + var9 + ", xMapId=" + var8.a() + ", serverKey=" + var8.d());
      if (var1.level != null) {
         for (ItemFrame var11 : var1.level.getEntitiesOfClass(ItemFrame.class, var7, var0 -> !var0.isRemoved() && y(var0))) {
            if (!(var11.distanceToSqr(var6) > 4096.0)) {
               byte var12;
               String var13;
               if (var9) {
                  if (t(var11)) {
                     var12 = 0;
                     var13 = "xMap";
                  } else {
                     if (!Y(var11)) {
                        te.R("Arrow frame " + var11.blockPosition() + " skipped (empty)");
                        continue;
                     }

                     var12 = 1;
                     var13 = "item";
                  }
               } else {
                  byte var14 = Y(var11);
                  var12 = var14;
                  var13 = "filled=" + var14;
               }

               te.R(
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

      ArrayList var17 = var3 == bs.c.D ? var4 : var5;
      if (var17.isEmpty()) {
         te.Z(
            "Arrow Game",
            "No "
               + var3.b
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

         for (bs.o var22 : this.P) {
            var19.add(var22.f);
         }

         ArrayList var21 = new ArrayList();

         for (ItemFrame var25 : var17) {
            if (var19.contains(var25.getId())) {
               te.R("Arrow frame " + var25.getId() + " already targeted, not re-firing");
            } else {
               var21.add(var25);
            }
         }

         if (var21.isEmpty()) {
            te.Z("Arrow Game", "Every reachable " + var3.b + " frame already has an arrow in flight; skipping this flick");
         } else {
            var17 = var21;
            int var24 = 0;
            if (var1.level != null) {
               double var26 = 64.0;
               AABB var16 = new AABB(var6.x - var26, var6.y - var26, var6.z - var26, var6.x + var26, var6.y + var26, var6.z + var26);
               var24 = var1.level.getEntitiesOfClass(ItemFrame.class, var16, var0 -> !var0.isRemoved() && y(var0)).size();
            }

            te.Z(
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
                  + var3.b
            );
            Vec3 var27 = G(var1.level, var2);
            ItemFrame var15 = (ItemFrame)var17.get(this.o.nextInt(var17.size()));
            bs.o var28 = new bs.o();
            var28.f = var15.getId();
            var28.J = var27;
            var28.h = this.j(var15);
            var28.P = var28.J;
            if (var1.level != null) {
               var28.Q = (Arrow)EntityTypes.ARROW.create(var1.level, EntitySpawnReason.COMMAND);
               if (var28.Q != null) {
                  var28.Q.setId(this.p--);
                  var28.Q.setNoGravity(true);
                  var28.Q.setNoPhysics(true);
                  var28.Q.setPos(var28.J.x, var28.J.y, var28.J.z);
                  this.s(var28);
                  var1.level.addEntity(var28.Q);
               }

               var1.level
                  .playSound(null, var28.J.x, var28.J.y, var28.J.z, SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 1.0F, 0.4F + this.o.nextFloat() * 1.2F);
            }

            this.P.add(var28);
            this.B(var1, var2);
            if (var1.level != null) {
               this.W = var1.level.getGameTime();
            }

            te.Z("Arrow Game", "Firing at " + var3.b + " frame " + var28.f);
         }
      }
   }

   private void s(bs.o var1) {
      if (var1.h != null) {
         var1.P = var1.J;
         double var2 = 11.7 + this.o.nextDouble() * 0.8000000000000007;
         var1.b = var2;
         if (this.q(var1)) {
            var1.m = 0.0;
            this.h(var1, var1.i[0][0], var1.i[0][1], var1.i[0][2]);
            te.R(
               "Arrow trajectory targetCrest="
                  + String.format("%.1f", var1.b)
                  + " speed="
                  + String.format("%.2f", var1.p)
                  + " pitch="
                  + String.format("%.1f", var1.x)
                  + "deg crest="
                  + String.format("%.1f", var1.A)
                  + " ticks="
                  + var1.v
                  + " to "
                  + var1.h
            );
         } else {
            Vec3 var4 = var1.h.subtract(var1.J).normalize();
            var1.R = var4.x * 1.1;
            var1.y = var4.y * 1.1;
            var1.j = var4.z * 1.1;
            this.h(var1, var1.R, var1.y, var1.j);
            te.R("Arrow trajectory FAILED -> straight shot from " + var1.J + " to " + var1.h);
         }
      }
   }

   private void h(bs.o var1, double var2, double var4, double var6) {
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      float var10 = (float)Math.toDegrees(Math.atan2(var2, var6));
      float var11 = (float)Math.toDegrees(Math.atan2(var4, var8));
      var1.V = var10;
      var1.Y = var11;
      if (var1.Q != null) {
         var1.Q.setYRot(var10);
         var1.Q.setXRot(var11);
         var1.Q.yRotO = var10;
         var1.Q.xRotO = var11;
      }
   }

   private boolean q(bs.o var1) {
      double var2 = var1.h.x - var1.J.x;
      double var4 = var1.h.y - var1.J.y;
      double var6 = var1.h.z - var1.J.z;
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      if (var8 < 0.001) {
         return false;
      }

      bs.x.g var10 = bs.x.n(var2, var4, var6, var1.b);
      if (var10 == null) {
         return false;
      }

      var1.O = var10.v;
      var1.i = var10.Q;
      var1.v = var10.H;
      var1.x = var10.t;
      var1.p = var10.n;
      var1.g = 0.0;
      var1.A = var10.s;
      var1.X = var10.H;
      var1.R = var10.a;
      var1.y = var10.d;
      var1.j = var10.b;
      return true;
   }

   private static double R(Vec3 var0, Vec3 var1, Vec3 var2) {
      Vec3 var3 = var1.subtract(var0);
      Vec3 var4 = var2.subtract(var0);
      double var5 = var3.dot(var3);
      double var7 = var5 > 1.0E-9 ? var4.dot(var3) / var5 : 0.0;
      var7 = Math.max(0.0, Math.min(1.0, var7));
      return var2.distanceTo(var0.add(var3.scale(var7)));
   }

   private static float B(float var0, float var1) {
      float var2 = ((var1 - var0) % 360.0F + 540.0F) % 360.0F - 180.0F;
      return var0 + var2 * 0.2F;
   }

   private void I(Minecraft var1, bs.o var2) {
      if (var1 != null && var1.level != null) {
         Vec3 var3 = var2.Z != null ? var2.Z : var2.J;
         ItemFrame var5 = var1.level.getEntity(var2.f) instanceof ItemFrame var6 && !var6.isRemoved() ? var6 : null;
         if (var5 == null) {
            te.Z("Arrow Game", "Arrow target unloaded before impact; no visual item dropped");
         } else {
            te.R(
               "Arrow landing target: id="
                  + var5.getId()
                  + " at "
                  + var5.blockPosition()
                  + " (arrow landed at "
                  + String.format("%.1f,%.1f,%.1f", var3.x, var3.y, var3.z)
                  + ")"
            );
            this.B(var1, var5, var3);
            sz.M(var1, var5);
            te.Z("Arrow Game", "Visual hit: item popped from frame " + var5.getId());
         }
      }
   }

   private void B(Minecraft var1, ItemFrame var2, Vec3 var3) {
      if (var1 != null && var1.level != null && var2 != null) {
         Vec3 var4 = var3 != null ? var3 : var2.getBoundingBox().getCenter();
         if (var1.getSoundManager() != null) {
            var1.getSoundManager()
               .play(
                  new SimpleSoundInstance(
                     SoundEvents.ARROW_HIT.location(),
                     SoundSource.NEUTRAL,
                     2.0F,
                     0.8F + this.o.nextFloat() * 0.4F,
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

   private boolean K(ItemFrame var1) {
      if (var1 == null) {
         return false;
      }

      if (com.larpclient.b.m().W()) {
         return this.z == bs.c.D ? n(var1) : t(var1);
      }

      byte var2 = Y(var1);
      return (boolean)(this.z == bs.c.D ? var2 : var2 == 0);
   }

   private void x(Minecraft var1, BlockPos var2, BlockState var3) {
      this.Q = var2.immutable();
      if (this.f == null) {
         this.f = var3;
      }

      int var4 = !var3.getValue(BlockStateProperties.POWERED) ? 1 : 0;
      if (var1.level != null) {
         var1.level.setBlockAndUpdate(var2, (BlockState)var3.setValue(BlockStateProperties.POWERED, Boolean.valueOf((boolean)var4)));
      }

      if (var1.player != null) {
         var1.player.playSound(SoundEvents.LEVER_CLICK, 0.3F, var4 != 0 ? 0.6F : 0.5F);
      }
   }

   private void p(Minecraft var1) {
      if (var1 != null && var1.level != null && this.Q != null) {
         if (this.f != null) {
            var1.level.setBlockAndUpdate(this.Q, this.f);
         }

         this.Q = null;
         this.f = null;
      }
   }

   public void d() {
      this.K = (boolean)0;
      this.G(Minecraft.getInstance());

      for (bs.o var2 : this.P) {
         if (var2.Q != null) {
            var2.Q.discard();
         }
      }

      this.P.clear();
      this.X.clear();
      this.Y = -1L;
      this.W = -1L;
      Minecraft var3 = Minecraft.getInstance();
      this.p(var3);
   }

   public void B(Minecraft var1, BlockPos var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         String var3 = gj.c().w(var1, var2);
         int[] var4 = this.J.get(var3);
         if (var4 == null) {
            this.H.merge(var3, 1, Integer::sum);
         } else {
            ArrayList var5 = new ArrayList();

            for (int var6 = 0; var6 < var4.length; var6++) {
               if (var4[var6] > 0) {
                  var5.add(var6);
               }
            }

            if (!var5.isEmpty()) {
               int var7 = (Integer)var5.get(this.o.nextInt(var5.size()));
               var4[var7] = Math.max(0, var4[var7] - 1);
            }
         }
      }
   }

   public void L(Minecraft var1, DispenserMenu var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         this.V = 0;
         this.e = 0;
         int[] var3 = new int[9];
         byte var4 = 0;

         for (Slot var6 : var2.slots) {
            if (!(var6.container instanceof Inventory) && var6.getContainerSlot() >= 0 && var6.getContainerSlot() <= 8 && var6.hasItem()) {
               ItemStack var7 = var6.getItem();
               if (j(var7)) {
                  var3[var6.getContainerSlot()] = var7.getCount();
                  var4 = 1;
               }
            }
         }

         if (var4 != 0) {
            String var12 = gj.c().C();
            if (var12 != null) {
               int[] var13 = this.T.get(var12);
               this.T.put(var12, var3);
               int[] var14 = this.J.get(var12);
               if (var14 == null) {
                  var14 = (int[])var3.clone();
                  this.J.put(var12, var14);
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

               Integer var15 = this.H.remove(var12);

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

                  int var16 = (Integer)var10.get(this.o.nextInt(var10.size()));
                  var14[var16] = Math.max(0, var14[var16] - 1);
               }
            }
         }
      }
   }

   public void E(String var1, int var2, ItemStack var3) {
      if (var1 != null) {
         this.u = null;
         this.m = null;
         int var4 = j(var3) ? var3.getCount() : 0;
         if (var2 >= 0 && var2 <= 8) {
            int[] var10 = this.T.get(var1);
            int[] var11 = this.J.get(var1);
            if (var10 == null || var11 == null) {
               return;
            }

            int var12 = var10[var2];
            int var13 = var11[var2];
            int var9 = this.h(var12, var13, var4);
            var10[var2] = var4;
            var11[var2] = var9;
         } else if (var2 >= 9 && var2 <= 44) {
            int var5 = var2 >= 36 ? var2 - 36 : var2;
            if (var5 < 0 || var5 >= 36) {
               return;
            }

            int var6 = this.R[var5];
            int var7 = this.c[var5];
            int var8 = this.h(var6, var7, var4);
            this.R[var5] = var4;
            this.c[var5] = var8;
         }
      }
   }

   public void p(int var1, ItemStack var2) {
      if (var1 >= 0 && var1 < 36) {
         this.u = null;
         this.m = null;
         if (this.c[var1] != 0 || this.R[var1] != 0 || this.V != 0) {
            int var3 = j(var2) ? var2.getCount() : 0;
            int var4 = this.R[var1];
            int var5 = this.c[var1];
            this.R[var1] = var3;
            this.c[var1] = this.h(var4, var5, var3);
         }
      }
   }

   private int h(int var1, int var2, int var3) {
      if (var3 <= 0) {
         if (var1 > 0 && var2 > 0) {
            this.V += var1;
            this.e = this.e + Math.max(0, var1 - var2);
         }

         return 0;
      } else {
         if (var1 == var3) {
            return Math.min(var2, var3);
         }

         if (var3 > var1) {
            int var8 = var3 - var1;
            int var9 = var8;
            if (this.V > 0) {
               int var6 = Math.min(var8, this.V);
               int var7 = Math.round((float)((long)this.e * var6 / Math.max(1, this.V)));
               var9 = var8 - var7;
               this.V -= var6;
               this.e = Math.max(0, this.e - var7);
            }

            return Math.min(var2 + Math.max(0, var9), var3);
         } else {
            int var4 = var1 - var3;
            int var5 = Math.round((float)((long)var2 * var4 / Math.max(1, var1)));
            this.V += var4;
            this.e = this.e + Math.max(0, var4 - Math.min(var5, var4));
            return Math.max(0, var2 - var5);
         }
      }
   }

   public ItemStack u(Minecraft var1, int var2, ItemStack var3) {
      if (var1 == null || var1.level == null || var3 == null || var3.isEmpty()) {
         return null;
      }

      if (var2 >= 0 && var2 <= 8) {
         gj var4 = gj.c();
         String var5 = var4.C();
         if (var5 == null) {
            return null;
         } else {
            int[] var6 = this.J.get(var5);
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

   public ItemStack W(int var1, ItemStack var2) {
      if (var2 == null || var2.isEmpty() || !j(var2)) {
         return null;
      } else if (var1 >= 0 && var1 < 36) {
         int var3 = this.c[var1];
         return var3 > 0 && var3 < var2.getCount() ? var2.copyWithCount(var3) : null;
      } else {
         return null;
      }
   }

   public ItemStack n(ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && j(var1)) {
         if (this.m != null && !this.m.isEmpty() && this.u != null && var1.is(this.u.getItem()) && var1.getCount() == this.u.getCount()) {
            return this.m.copy();
         }

         if (this.V > 0 && this.e > 0) {
            int var2 = var1.getCount() - this.e;
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

   public void E(ItemStack var1, ItemStack var2) {
      if (var1 != null && !var1.isEmpty()) {
         this.u = var1;
         this.m = var2 != null && !var2.isEmpty() ? var2 : var1;
      } else {
         this.u = null;
         this.m = null;
      }
   }

   public boolean r() {
      return this.l && this.z != bs.c.B;
   }

   private static boolean j(ItemStack var0) {
      return var0.is(Items.ARROW) || var0.is(Items.SPECTRAL_ARROW) || var0.is(Items.TIPPED_ARROW);
   }

   private static double e(BlockPos var0, BlockPos var1) {
      int var2 = var0.getX() - var1.getX();
      int var3 = var0.getY() - var1.getY();
      int var4 = var0.getZ() - var1.getZ();
      return var2 * var2 + var3 * var3 + var4 * var4;
   }

   public enum c {
      B("off", "Off"),
      D("win", "Win (filled frames)"),
      d("lose", "Lose (empty frames)");

      public final String b;
      public final String r;

      c(String var3, String var4) {
         this.b = var3;
         this.r = var4;
      }
   }

   private static final class o {
      Vec3 J;
      Vec3 h;
      Vec3 Z;
      int f;
      int D;
      float V;
      float Y;
      double R;
      double y;
      double j;
      Arrow Q;
      double[][] O;
      double[][] i;
      int v;
      double m;
      Vec3 P;
      double b;
      double x;
      double p;
      double g;
      double A;
      int X;
   }

   private record t(BlockPos dispenser, bs.c target, long due) {
   }

   private static final class x {
      static final int X = 24;
      static final int K = 80;

      private static double V(int var0) {
         return 0.99 * (1.0 - Math.pow(0.99, var0)) / 0.010000000000000009;
      }

      static bs.x.g n(double var0, double var2, double var4, double var6) {
         double var8 = Math.sqrt(var0 * var0 + var4 * var4);
         if (var8 < 0.001) {
            return null;
         }

         int var10 = 24;
         double var11 = Double.MAX_VALUE;
         double var13 = 0.0;

         for (int var15 = 24; var15 <= 80; var15++) {
            double var16 = V(var15);
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
         double var43 = V(var42);
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
         return new bs.x.g(var42, var47, var25, var44, var46, var45, var13, var50, var40);
      }

      static final class g {
         final int H;
         final double[][] v;
         final double[][] Q;
         final double a;
         final double d;
         final double b;
         final double s;
         final double t;
         final double n;

         g(int var1, double[][] var2, double[][] var3, double var4, double var6, double var8, double var10, double var12, double var14) {
            this.H = var1;
            this.v = var2;
            this.Q = var3;
            this.a = var4;
            this.d = var6;
            this.b = var8;
            this.s = var10;
            this.t = var12;
            this.n = var14;
         }
      }
   }
}
