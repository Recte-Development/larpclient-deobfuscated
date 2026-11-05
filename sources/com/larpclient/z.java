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
public final class z {
   private static final z X = new z();
   private static final double l = 0.5;
   private static final double s = 1.1;
   private static final double o = 0.85;
   private static final double e = 0.05;
   private static final double a = 0.99;
   private static final double C = 11.7;
   private static final double A = 12.5;
   private static final int x = 120;
   private static final double B = 10.0;
   private static final double J = 4096.0;
   private static final double O = 64.0;
   private static final double T = 4.0;
   public boolean d;
   private z.f f = z.f.t;
   private final Random R = new Random();
   private final List<z.d> P = new ArrayList<>();
   private BlockPos V;
   private BlockState b;
   private final List<z.c> h = new ArrayList<>();
   private ClientLevel Y;
   private long F = -1L;
   private long v = -1L;
   private final Map<BlockPos, BlockState> G = new LinkedHashMap<>();
   private ClientLevel L;
   private int H = -2;
   private long y = -1L;
   private boolean u;
   private final Map<String, int[]> Z = new LinkedHashMap<>();
   private final Map<String, int[]> I = new LinkedHashMap<>();
   private final Map<String, Integer> c = new LinkedHashMap<>();
   private final int[] Q = new int[36];
   private final int[] K = new int[36];
   private int N = 0;
   private int W = 0;
   private ItemStack U = null;
   private ItemStack n = null;

   private static boolean a(ItemFrame var0) {
      return var0.getDirection() == Direction.UP;
   }

   private static Vec3 j(Level var0, BlockPos var1) {
      Vec3 var2 = new Vec3(var1.getX() + 0.5, var1.getY() + 0.5, var1.getZ() + 0.5);
      BlockState var3 = var0.getBlockState(var1);
      if (!(var3.getBlock() instanceof DispenserBlock)) {
         return var2.add(0.0, 0.25, 0.0);
      }

      Direction var4 = (Direction)var3.getValue(DispenserBlock.FACING);
      return var2.add(var4.getStepX() * 0.6, var4.getStepY() * 0.6, var4.getStepZ() * 0.6);
   }

   private z() {
   }

   public static z v() {
      return X;
   }

   public void A(String var1) {
      if (var1 == null) {
         this.f = z.f.t;
      } else if ("win".equalsIgnoreCase(var1)) {
         this.f = z.f.h;
      } else if ("lose".equalsIgnoreCase(var1)) {
         this.f = z.f.l;
      } else {
         this.f = z.f.t;
      }

      if (this.f == z.f.t) {
         this.u = (boolean)0;
      }
   }

   public void A() {
      this.f = switch (this.f) {
         case t -> z.f.h;
         case h -> z.f.l;
         case l -> z.f.t;
      };
      if (this.f == z.f.t) {
         xa.q("Arrow Game", "Target off");
      } else {
         xa.q("Arrow Game", "Target " + this.f.E);
      }
   }

   public String B() {
      return this.f == null ? "off" : this.f.E;
   }

   public String E() {
      return this.f == null ? "Off" : this.f.v;
   }

   public void g() {
      this.u = (boolean)1;
   }

   public boolean c() {
      return this.u;
   }

   public void z() {
      this.u = (boolean)0;
   }

   public boolean t() {
      return tr.O().o();
   }

   public boolean o(Player var1) {
      if (var1 == null) {
         return false;
      }

      ItemStack var2 = var1.getMainHandItem();
      return var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP) ? var2.getComponents().get(DataComponents.MAP_ID) != null : false;
   }

   public void G() {
      Minecraft var1 = Minecraft.getInstance();
      LocalPlayer var2 = var1 != null ? var1.player : null;
      if (!this.o(var2)) {
         xa.q("Arrow Game", "Hold a filled map in your hand to set the X map");
      } else {
         MapId var3 = (MapId)var2.getMainHandItem().getComponents().get(DataComponents.MAP_ID);
         tr.O().L(var3.id());
         this.u = (boolean)0;
         xa.q(
            "Arrow Game",
            "X map set to map #" + var3.id() + " - frames holding this map are the LOSE target, any other item (elytra, block, ...) is the WIN target"
         );
      }
   }

   public void T() {
      tr.O().L(-1);
      this.u = (boolean)0;
      xa.q("Arrow Game", "X map cleared - falling back to filled=win, empty=lose");
   }

   private static boolean o(ItemFrame var0) {
      return var0 != null && var0.getItem() != null && !var0.getItem().isEmpty();
   }

   private static boolean G(ItemFrame var0) {
      return o(var0) && var0.getItem().is(Items.FILLED_MAP) ? var0.getItem().getComponents().get(DataComponents.MAP_ID) != null : false;
   }

   private static boolean q(ItemFrame var0) {
      if (!G(var0)) {
         return false;
      }

      int var1 = tr.O().K();
      return var1 < 0 ? false : ((MapId)var0.getItem().getComponents().get(DataComponents.MAP_ID)).id() == var1;
   }

   private static boolean K(ItemFrame var0) {
      return !o(var0) ? false : !q(var0);
   }

   public boolean Y(Minecraft var1, BlockPos var2) {
      if (!this.d || this.f == z.f.t) {
         return false;
      }

      if (var1 != null && var1.player != null && var1.level != null && var2 != null) {
         BlockState var3 = var1.level.getBlockState(var2);
         if (!(var3.getBlock() instanceof LeverBlock)) {
            return false;
         }

         BlockPos var4 = ep.H().n(ep.o.x);
         if (var4 == null) {
            return false;
         }

         if (N(var2, var4) > 4.0) {
            return false;
         }

         int var5 = !var3.getValue(BlockStateProperties.POWERED) ? 1 : 0;
         this.j(var1, var2, var3);
         if (var5 != 0) {
            this.u(var1, var4);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean e(BlockPos var1) {
      return tr.O().w(var1);
   }

   public void o(Minecraft var1, BlockPos var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         BlockState var3 = var1.level.getBlockState(var2);
         if (var3.getBlock() instanceof NoteBlock) {
            tr.O().y(var2);
            xa.q("Arrow Game", "Bound noteblock at " + var2.toShortString());
         }
      }
   }

   public void d() {
      tr.O().y(null);
   }

   public boolean D(Minecraft var1, BlockPos var2) {
      if (!this.d || this.f == z.f.t) {
         return false;
      }

      if (var1 == null || var1.player == null || var1.level == null || var2 == null) {
         return false;
      }

      if (!this.e(var2)) {
         return false;
      }

      BlockState var3 = var1.level.getBlockState(var2);
      if (!(var3.getBlock() instanceof NoteBlock)) {
         return false;
      }

      BlockPos var4 = ep.H().n(ep.o.x);
      if (var4 == null) {
         return false;
      }

      if (this.Y != var1.level) {
         this.h();
      }

      long var5 = var1.level.getGameTime();
      if (var5 < this.F) {
         return true;
      }

      this.F = var5 + 4L;
      this.J(var1, var3, var2);
      this.U(var1, var2);
      this.Y = var1.level;
      this.h.add(new z.c(var4.immutable(), this.f, var1.level.getGameTime() + 6L));
      return true;
   }

   private void J(Minecraft var1, BlockState var2, BlockPos var3) {
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

   private void U(Minecraft var1, BlockPos var2) {
      this.m(var1);
      BlockPos var3 = var2.below();
      BlockState var4 = var1.level.getBlockState(var3);
      if (var4.getBlock() instanceof ObserverBlock && var4.getValue(ObserverBlock.FACING) == Direction.UP) {
         this.L = var1.level;
         this.G.put(var3, var4);
         ArrayDeque var5 = new ArrayDeque();
         BlockPos var6 = var3.below();
         var5.add(var6);

         for (Direction var10 : Direction.values()) {
            var5.add(var6.relative(var10));
         }

         while (!var5.isEmpty() && this.G.size() < 32) {
            BlockPos var14 = (BlockPos)var5.removeFirst();
            if (!this.G.containsKey(var14) && !(N(var14, var3) > 256.0)) {
               BlockState var16 = var1.level.getBlockState(var14);
               BlockState var18 = n(var16);
               if (var18 != null) {
                  this.G.put(var14, var16);

                  for (Direction var13 : Direction.values()) {
                     var5.add(var14.relative(var13));
                  }
               }
            }
         }

         for (Entry var17 : this.G.entrySet()) {
            BlockState var19 = ((BlockState)var17.getValue()).getBlock() instanceof ObserverBlock
               ? (BlockState)((BlockState)var17.getValue()).setValue(BlockStateProperties.POWERED, true)
               : n((BlockState)var17.getValue());
            var1.level.setBlock((BlockPos)var17.getKey(), var19, 2);
         }

         this.v = var1.level.getGameTime() + 4L;
      }
   }

   static BlockState n(BlockState var0) {
      if (var0.getBlock() instanceof RedstoneWireBlock) {
         return (BlockState)var0.setValue(RedstoneWireBlock.POWER, 15);
      } else if (var0.getBlock() instanceof RepeaterBlock) {
         return (BlockState)var0.setValue(BlockStateProperties.POWERED, true);
      } else {
         return var0.getBlock() instanceof RedstoneLampBlock ? (BlockState)var0.setValue(BlockStateProperties.LIT, true) : null;
      }
   }

   private void m(Minecraft var1) {
      if (var1.level != null && var1.level == this.L) {
         for (Entry var3 : this.G.entrySet()) {
            BlockState var4 = ((BlockState)var3.getValue()).getBlock() instanceof ObserverBlock
               ? (BlockState)((BlockState)var3.getValue()).setValue(BlockStateProperties.POWERED, true)
               : n((BlockState)var3.getValue());
            if (var4 != null && var1.level.getBlockState((BlockPos)var3.getKey()).equals(var4)) {
               var1.level.setBlock((BlockPos)var3.getKey(), (BlockState)var3.getValue(), 2);
            }
         }
      }

      this.G.clear();
      this.L = null;
      this.v = -1L;
   }

   private static AABB y(Vec3 var0) {
      return new AABB(var0.x - 64.0, var0.y - 64.0, var0.z - 64.0, var0.x + 64.0, var0.y + 64.0, var0.z + 64.0);
   }

   private Vec3 k(ItemFrame var1) {
      AABB var2 = var1.getBoundingBox();
      double var3 = 0.2;
      double var5 = Math.max(0.0, var2.getXsize() - 2.0 * var3);
      double var7 = Math.max(0.0, var2.getYsize() - 2.0 * var3);
      double var9 = Math.max(0.0, var2.getZsize() - 2.0 * var3);
      return new Vec3(
         var2.minX + var3 + this.R.nextDouble() * var5, var2.minY + var3 + this.R.nextDouble() * var7, var2.minZ + var3 + this.R.nextDouble() * var9
      );
   }

   public void I(Minecraft var1) {
      _a.z(var1);
      if (this.Y != var1.level) {
         this.h();
         this.Y = var1.level;
      }

      if (var1.level != null) {
         if (this.v >= 0L && var1.level.getGameTime() >= this.v) {
            this.m(var1);
         }

         if (!this.d) {
            this.u = (boolean)0;
            this.C(var1);
         }

         Iterator var2 = this.h.iterator();

         while (var2.hasNext()) {
            z.c var3 = (z.c)var2.next();
            if (var1.level.getGameTime() >= var3.due()) {
               var2.remove();
               this.a(var1, var3.dispenser(), var3.target());
            }
         }

         if (!this.P.isEmpty()) {
            long var8 = var1.level.getGameTime();
            long var4 = var8 - this.y;
            if (var4 > 0L) {
               this.y = var8;

               for (long var6 = 0L; var6 < var4 && !this.P.isEmpty(); var6++) {
                  this.H(var1);
               }
            }
         }
      }
   }

   private void H(Minecraft var1) {
      Iterator var2 = this.P.iterator();

      while (var2.hasNext()) {
         z.d var3 = (z.d)var2.next();
         if (var3.D == null) {
            this.b(var1, var3);
            var2.remove();
         } else if (var3.T <= 0) {
            var3.L++;
            var3.g *= 0.99;
            var3.U = var3.U * 0.99 - 0.05;
            var3.u *= 0.99;
            var3.h = var3.h.add(var3.g, var3.U, var3.u);
            double var15 = Math.sqrt(var3.g * var3.g + var3.u * var3.u);
            var3.D.setYRot((float)Math.toDegrees(Math.atan2(var3.g, var3.u)));
            var3.D.setXRot((float)Math.toDegrees(Math.atan2(var3.U, Math.max(var15, 1.0E-4))));
            var3.D.setPos(var3.h.x, var3.h.y, var3.h.z);
            if (var3.L >= 120 || var3.b != null && I(var3.h.subtract(var3.g, var3.U, var3.u), var3.h, var3.b) <= 0.5) {
               var3.i = new Vec3(var3.h.x, var3.h.y, var3.h.z);
               var3.D.discard();
               var3.D = null;
               var2.remove();
               this.b(var1, var3);
            }
         } else if (var3.e >= var3.T) {
            var3.i = var3.h;
            var3.D.discard();
            var3.D = null;
            var2.remove();
            this.b(var1, var3);
         } else {
            Vec3 var4 = var3.h;
            var3.e = H(var3.e, var3.T);
            int var5 = Math.min((int)var3.e, var3.T - 1);
            double var6 = var3.e - var5;
            double[] var8 = var5 == 0 ? new double[3] : var3.R[var5 - 1];
            double[] var9 = var3.R[var5];
            var3.h = new Vec3(
               var3.X.x + var8[0] + (var9[0] - var8[0]) * var6,
               var3.X.y + var8[1] + (var9[1] - var8[1]) * var6,
               var3.X.z + var8[2] + (var9[2] - var8[2]) * var6
            );
            var3.g = var3.h.x - var4.x;
            var3.U = var3.h.y - var4.y;
            var3.u = var3.h.z - var4.z;
            double var10 = Math.sqrt(var3.g * var3.g + var3.u * var3.u);
            float var12 = (float)Math.toDegrees(Math.atan2(var3.g, var3.u));
            float var13 = (float)Math.toDegrees(Math.atan2(var3.U, Math.max(var10, 1.0E-4)));
            var3.B = f(var3.B, var12);
            var3.M = f(var3.M, var13);
            var3.D.setYRot(var3.B);
            var3.D.setXRot(var3.M);
            var3.D.setPos(var3.h.x, var3.h.y, var3.h.z);
            var3.L++;
            int var14 = !(var3.e >= var3.T) && (var3.b == null || !(I(var4, var3.h, var3.b) <= 0.5)) ? 0 : 1;
            if (var14 != 0) {
               var3.i = var3.h;
               var3.D.discard();
               var3.D = null;
               var2.remove();
               this.b(var1, var3);
            }
         }
      }
   }

   static double H(double var0, int var2) {
      return Math.min(var0 + 0.85, var2);
   }

   private void u(Minecraft var1, BlockPos var2) {
      this.a(var1, var2, this.f);
   }

   private void a(Minecraft var1, BlockPos var2, z.f var3) {
      this.Y = var1.level;
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      Vec3 var6 = new Vec3(var2.getX() + 0.5, var2.getY() + 0.5, var2.getZ() + 0.5);
      AABB var7 = y(var6);
      tr var8 = tr.O();
      boolean var9 = var8.o();
      xa.i("Arrow beginShot: dispenser " + var2 + ", target " + var3.E + ", xMapSet=" + var9 + ", xMapId=" + var8.K() + ", serverKey=" + var8.F());
      if (var1.level != null) {
         for (ItemFrame var11 : var1.level.getEntitiesOfClass(ItemFrame.class, var7, var0 -> !var0.isRemoved() && a(var0))) {
            if (!(var11.distanceToSqr(var6) > 4096.0)) {
               byte var12;
               String var13;
               if (var9) {
                  if (q(var11)) {
                     var12 = 0;
                     var13 = "xMap";
                  } else {
                     if (!o(var11)) {
                        xa.i("Arrow frame " + var11.blockPosition() + " skipped (empty)");
                        continue;
                     }

                     var12 = 1;
                     var13 = "item";
                  }
               } else {
                  byte var14 = o(var11);
                  var12 = var14;
                  var13 = "filled=" + var14;
               }

               xa.i(
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

      ArrayList var17 = var3 == z.f.h ? var4 : var5;
      if (var17.isEmpty()) {
         xa.q(
            "Arrow Game",
            "No "
               + var3.E
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

         for (z.d var22 : this.P) {
            var19.add(var22.A);
         }

         ArrayList var21 = new ArrayList();

         for (ItemFrame var25 : var17) {
            if (var19.contains(var25.getId())) {
               xa.i("Arrow frame " + var25.getId() + " already targeted, not re-firing");
            } else {
               var21.add(var25);
            }
         }

         if (var21.isEmpty()) {
            xa.q("Arrow Game", "Every reachable " + var3.E + " frame already has an arrow in flight; skipping this flick");
         } else {
            var17 = var21;
            int var24 = 0;
            if (var1.level != null) {
               double var26 = 64.0;
               AABB var16 = new AABB(var6.x - var26, var6.y - var26, var6.z - var26, var6.x + var26, var6.y + var26, var6.z + var26);
               var24 = var1.level.getEntitiesOfClass(ItemFrame.class, var16, var0 -> !var0.isRemoved() && a(var0)).size();
            }

            xa.q(
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
                  + var3.E
            );
            Vec3 var27 = j(var1.level, var2);
            ItemFrame var15 = (ItemFrame)var17.get(this.R.nextInt(var17.size()));
            z.d var28 = new z.d();
            var28.A = var15.getId();
            var28.h = var27;
            var28.b = this.k(var15);
            var28.X = var28.h;
            if (var1.level != null) {
               var28.D = (Arrow)EntityTypes.ARROW.create(var1.level, EntitySpawnReason.COMMAND);
               if (var28.D != null) {
                  var28.D.setId(this.H--);
                  var28.D.setNoGravity(true);
                  var28.D.setNoPhysics(true);
                  var28.D.setPos(var28.h.x, var28.h.y, var28.h.z);
                  this.j(var28);
                  var1.level.addEntity(var28.D);
               }

               var1.level
                  .playSound(null, var28.h.x, var28.h.y, var28.h.z, SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 1.0F, 0.4F + this.R.nextFloat() * 1.2F);
            }

            this.P.add(var28);
            this.P(var1, var2);
            if (var1.level != null) {
               this.y = var1.level.getGameTime();
            }

            xa.q("Arrow Game", "Firing at " + var3.E + " frame " + var28.A);
         }
      }
   }

   private void j(z.d var1) {
      if (var1.b != null) {
         var1.X = var1.h;
         double var2 = 11.7 + this.R.nextDouble() * 0.8000000000000007;
         var1.C = var2;
         if (this.e(var1)) {
            var1.e = 0.0;
            this.V(var1, var1.y[0][0], var1.y[0][1], var1.y[0][2]);
            xa.i(
               "Arrow trajectory targetCrest="
                  + String.format("%.1f", var1.C)
                  + " speed="
                  + String.format("%.2f", var1.f)
                  + " pitch="
                  + String.format("%.1f", var1.d)
                  + "deg crest="
                  + String.format("%.1f", var1.q)
                  + " ticks="
                  + var1.T
                  + " to "
                  + var1.b
            );
         } else {
            Vec3 var4 = var1.b.subtract(var1.h).normalize();
            var1.g = var4.x * 1.1;
            var1.U = var4.y * 1.1;
            var1.u = var4.z * 1.1;
            this.V(var1, var1.g, var1.U, var1.u);
            xa.i("Arrow trajectory FAILED -> straight shot from " + var1.h + " to " + var1.b);
         }
      }
   }

   private void V(z.d var1, double var2, double var4, double var6) {
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      float var10 = (float)Math.toDegrees(Math.atan2(var2, var6));
      float var11 = (float)Math.toDegrees(Math.atan2(var4, var8));
      var1.B = var10;
      var1.M = var11;
      if (var1.D != null) {
         var1.D.setYRot(var10);
         var1.D.setXRot(var11);
         var1.D.yRotO = var10;
         var1.D.xRotO = var11;
      }
   }

   private boolean e(z.d var1) {
      double var2 = var1.b.x - var1.h.x;
      double var4 = var1.b.y - var1.h.y;
      double var6 = var1.b.z - var1.h.z;
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      if (var8 < 0.001) {
         return false;
      }

      z.u.f var10 = z.u.e(var2, var4, var6, var1.C);
      if (var10 == null) {
         return false;
      }

      var1.R = var10.U;
      var1.y = var10.p;
      var1.T = var10.Q;
      var1.d = var10.T;
      var1.f = var10.V;
      var1.r = 0.0;
      var1.q = var10.F;
      var1.S = var10.Q;
      var1.g = var10.M;
      var1.U = var10.r;
      var1.u = var10.d;
      return true;
   }

   private static double I(Vec3 var0, Vec3 var1, Vec3 var2) {
      Vec3 var3 = var1.subtract(var0);
      Vec3 var4 = var2.subtract(var0);
      double var5 = var3.dot(var3);
      double var7 = var5 > 1.0E-9 ? var4.dot(var3) / var5 : 0.0;
      var7 = Math.max(0.0, Math.min(1.0, var7));
      return var2.distanceTo(var0.add(var3.scale(var7)));
   }

   private static float f(float var0, float var1) {
      float var2 = ((var1 - var0) % 360.0F + 540.0F) % 360.0F - 180.0F;
      return var0 + var2 * 0.2F;
   }

   private void b(Minecraft var1, z.d var2) {
      if (var1 != null && var1.level != null) {
         Vec3 var3 = var2.i != null ? var2.i : var2.h;
         ItemFrame var5 = var1.level.getEntity(var2.A) instanceof ItemFrame var6 && !var6.isRemoved() ? var6 : null;
         if (var5 == null) {
            xa.q("Arrow Game", "Arrow target unloaded before impact; no visual item dropped");
         } else {
            xa.i(
               "Arrow landing target: id="
                  + var5.getId()
                  + " at "
                  + var5.blockPosition()
                  + " (arrow landed at "
                  + String.format("%.1f,%.1f,%.1f", var3.x, var3.y, var3.z)
                  + ")"
            );
            this.s(var1, var5, var3);
            _a.O(var1, var5);
            xa.q("Arrow Game", "Visual hit: item popped from frame " + var5.getId());
         }
      }
   }

   private void s(Minecraft var1, ItemFrame var2, Vec3 var3) {
      if (var1 != null && var1.level != null && var2 != null) {
         Vec3 var4 = var3 != null ? var3 : var2.getBoundingBox().getCenter();
         if (var1.getSoundManager() != null) {
            var1.getSoundManager()
               .play(
                  new SimpleSoundInstance(
                     SoundEvents.ARROW_HIT.location(),
                     SoundSource.NEUTRAL,
                     2.0F,
                     0.8F + this.R.nextFloat() * 0.4F,
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

   private boolean j(ItemFrame var1) {
      if (var1 == null) {
         return false;
      }

      if (tr.O().o()) {
         return this.f == z.f.h ? K(var1) : q(var1);
      }

      byte var2 = o(var1);
      return (boolean)(this.f == z.f.h ? var2 : var2 == 0);
   }

   private void j(Minecraft var1, BlockPos var2, BlockState var3) {
      this.V = var2.immutable();
      if (this.b == null) {
         this.b = var3;
      }

      int var4 = !var3.getValue(BlockStateProperties.POWERED) ? 1 : 0;
      if (var1.level != null) {
         var1.level.setBlockAndUpdate(var2, (BlockState)var3.setValue(BlockStateProperties.POWERED, Boolean.valueOf((boolean)var4)));
      }

      if (var1.player != null) {
         var1.player.playSound(SoundEvents.LEVER_CLICK, 0.3F, var4 != 0 ? 0.6F : 0.5F);
      }
   }

   private void C(Minecraft var1) {
      if (var1 != null && var1.level != null && this.V != null) {
         if (this.b != null) {
            var1.level.setBlockAndUpdate(this.V, this.b);
         }

         this.V = null;
         this.b = null;
      }
   }

   public void h() {
      this.u = (boolean)0;
      this.m(Minecraft.getInstance());

      for (z.d var2 : this.P) {
         if (var2.D != null) {
            var2.D.discard();
         }
      }

      this.P.clear();
      this.h.clear();
      this.F = -1L;
      this.y = -1L;
      Minecraft var3 = Minecraft.getInstance();
      this.C(var3);
   }

   public void P(Minecraft var1, BlockPos var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         String var3 = ep.H().m(var1, var2);
         int[] var4 = this.I.get(var3);
         if (var4 == null) {
            this.c.merge(var3, 1, Integer::sum);
         } else {
            ArrayList var5 = new ArrayList();

            for (int var6 = 0; var6 < var4.length; var6++) {
               if (var4[var6] > 0) {
                  var5.add(var6);
               }
            }

            if (!var5.isEmpty()) {
               int var7 = (Integer)var5.get(this.R.nextInt(var5.size()));
               var4[var7] = Math.max(0, var4[var7] - 1);
            }
         }
      }
   }

   public void x(Minecraft var1, DispenserMenu var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         this.N = 0;
         this.W = 0;
         int[] var3 = new int[9];
         byte var4 = 0;

         for (Slot var6 : var2.slots) {
            if (!(var6.container instanceof Inventory) && var6.getContainerSlot() >= 0 && var6.getContainerSlot() <= 8 && var6.hasItem()) {
               ItemStack var7 = var6.getItem();
               if (r(var7)) {
                  var3[var6.getContainerSlot()] = var7.getCount();
                  var4 = 1;
               }
            }
         }

         if (var4 != 0) {
            String var12 = ep.H().b();
            if (var12 != null) {
               int[] var13 = this.Z.get(var12);
               this.Z.put(var12, var3);
               int[] var14 = this.I.get(var12);
               if (var14 == null) {
                  var14 = (int[])var3.clone();
                  this.I.put(var12, var14);
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

               Integer var15 = this.c.remove(var12);

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

                  int var16 = (Integer)var10.get(this.R.nextInt(var10.size()));
                  var14[var16] = Math.max(0, var14[var16] - 1);
               }
            }
         }
      }
   }

   public void n(String var1, int var2, ItemStack var3) {
      if (var1 != null) {
         this.U = null;
         this.n = null;
         int var4 = r(var3) ? var3.getCount() : 0;
         if (var2 >= 0 && var2 <= 8) {
            int[] var10 = this.Z.get(var1);
            int[] var11 = this.I.get(var1);
            if (var10 == null || var11 == null) {
               return;
            }

            int var12 = var10[var2];
            int var13 = var11[var2];
            int var9 = this.t(var12, var13, var4);
            var10[var2] = var4;
            var11[var2] = var9;
         } else if (var2 >= 9 && var2 <= 44) {
            int var5 = var2 >= 36 ? var2 - 36 : var2;
            if (var5 < 0 || var5 >= 36) {
               return;
            }

            int var6 = this.K[var5];
            int var7 = this.Q[var5];
            int var8 = this.t(var6, var7, var4);
            this.K[var5] = var4;
            this.Q[var5] = var8;
         }
      }
   }

   public void S(int var1, ItemStack var2) {
      if (var1 >= 0 && var1 < 36) {
         this.U = null;
         this.n = null;
         if (this.Q[var1] != 0 || this.K[var1] != 0 || this.N != 0) {
            int var3 = r(var2) ? var2.getCount() : 0;
            int var4 = this.K[var1];
            int var5 = this.Q[var1];
            this.K[var1] = var3;
            this.Q[var1] = this.t(var4, var5, var3);
         }
      }
   }

   private int t(int var1, int var2, int var3) {
      if (var3 <= 0) {
         if (var1 > 0 && var2 > 0) {
            this.N += var1;
            this.W = this.W + Math.max(0, var1 - var2);
         }

         return 0;
      } else {
         if (var1 == var3) {
            return Math.min(var2, var3);
         }

         if (var3 > var1) {
            int var8 = var3 - var1;
            int var9 = var8;
            if (this.N > 0) {
               int var6 = Math.min(var8, this.N);
               int var7 = Math.round((float)((long)this.W * var6 / Math.max(1, this.N)));
               var9 = var8 - var7;
               this.N -= var6;
               this.W = Math.max(0, this.W - var7);
            }

            return Math.min(var2 + Math.max(0, var9), var3);
         } else {
            int var4 = var1 - var3;
            int var5 = Math.round((float)((long)var2 * var4 / Math.max(1, var1)));
            this.N += var4;
            this.W = this.W + Math.max(0, var4 - Math.min(var5, var4));
            return Math.max(0, var2 - var5);
         }
      }
   }

   public ItemStack L(Minecraft var1, int var2, ItemStack var3) {
      if (var1 == null || var1.level == null || var3 == null || var3.isEmpty()) {
         return null;
      }

      if (var2 >= 0 && var2 <= 8) {
         ep var4 = ep.H();
         String var5 = var4.b();
         if (var5 == null) {
            return null;
         } else {
            int[] var6 = this.I.get(var5);
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

   public ItemStack U(int var1, ItemStack var2) {
      if (var2 == null || var2.isEmpty() || !r(var2)) {
         return null;
      } else if (var1 >= 0 && var1 < 36) {
         int var3 = this.Q[var1];
         return var3 > 0 && var3 < var2.getCount() ? var2.copyWithCount(var3) : null;
      } else {
         return null;
      }
   }

   public ItemStack d(ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && r(var1)) {
         if (this.n != null && !this.n.isEmpty() && this.U != null && var1.is(this.U.getItem()) && var1.getCount() == this.U.getCount()) {
            return this.n.copy();
         }

         if (this.N > 0 && this.W > 0) {
            int var2 = var1.getCount() - this.W;
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

   public void F(ItemStack var1, ItemStack var2) {
      if (var1 != null && !var1.isEmpty()) {
         this.U = var1;
         this.n = var2 != null && !var2.isEmpty() ? var2 : var1;
      } else {
         this.U = null;
         this.n = null;
      }
   }

   public boolean Q() {
      return this.d && this.f != z.f.t;
   }

   private static boolean r(ItemStack var0) {
      return var0.is(Items.ARROW) || var0.is(Items.SPECTRAL_ARROW) || var0.is(Items.TIPPED_ARROW);
   }

   private static double N(BlockPos var0, BlockPos var1) {
      int var2 = var0.getX() - var1.getX();
      int var3 = var0.getY() - var1.getY();
      int var4 = var0.getZ() - var1.getZ();
      return var2 * var2 + var3 * var3 + var4 * var4;
   }

   private record c(BlockPos dispenser, z.f target, long due) {
   }

   private static final class d {
      Vec3 h;
      Vec3 b;
      Vec3 i;
      int A;
      int L;
      float B;
      float M;
      double g;
      double U;
      double u;
      Arrow D;
      double[][] R;
      double[][] y;
      int T;
      double e;
      Vec3 X;
      double C;
      double d;
      double f;
      double r;
      double q;
      int S;
   }

   public enum f {
      t("off", "Off"),
      h("win", "Win (filled frames)"),
      l("lose", "Lose (empty frames)");

      public final String E;
      public final String v;

      f(String var3, String var4) {
         this.E = var3;
         this.v = var4;
      }
   }

   private static final class u {
      static final int X = 24;
      static final int x = 80;

      private static double F(int var0) {
         return 0.99 * (1.0 - Math.pow(0.99, var0)) / 0.010000000000000009;
      }

      static z.u.f e(double var0, double var2, double var4, double var6) {
         double var8 = Math.sqrt(var0 * var0 + var4 * var4);
         if (var8 < 0.001) {
            return null;
         }

         int var10 = 24;
         double var11 = Double.MAX_VALUE;
         double var13 = 0.0;

         for (int var15 = 24; var15 <= 80; var15++) {
            double var16 = F(var15);
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
         double var43 = F(var42);
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
         return new z.u.f(var42, var47, var25, var44, var46, var45, var13, var50, var40);
      }

      static final class f {
         final int Q;
         final double[][] U;
         final double[][] p;
         final double M;
         final double r;
         final double d;
         final double F;
         final double T;
         final double V;

         f(int var1, double[][] var2, double[][] var3, double var4, double var6, double var8, double var10, double var12, double var14) {
            this.Q = var1;
            this.U = var2;
            this.p = var3;
            this.M = var4;
            this.r = var6;
            this.d = var8;
            this.F = var10;
            this.T = var12;
            this.V = var14;
         }
      }
   }
}
