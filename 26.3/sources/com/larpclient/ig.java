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
public final class ig {
   private static final ig P = new ig();
   private static final double J = 0.5;
   private static final double O = 1.1;
   private static final double b = 0.85;
   private static final double a = 0.05;
   private static final double o = 0.99;
   private static final double U = 11.7;
   private static final double M = 12.5;
   private static final int t = 120;
   private static final double j = 10.0;
   private static final double g = 4096.0;
   private static final double E = 64.0;
   private static final double c = 4.0;
   public boolean I;
   private ig.e C = ig.e.S;
   private final Random N = new Random();
   private final List<ig.y> m = new ArrayList<>();
   private BlockPos D;
   private BlockState p;
   private final List<ig.u> H = new ArrayList<>();
   private ClientLevel h;
   private long r = -1L;
   private long S = -1L;
   private final Map<BlockPos, BlockState> n = new LinkedHashMap<>();
   private ClientLevel v;
   private int k = -2;
   private long i = -1L;
   private boolean Z;
   private final Map<String, int[]> R = new LinkedHashMap<>();
   private final Map<String, int[]> e = new LinkedHashMap<>();
   private final Map<String, Integer> y = new LinkedHashMap<>();
   private final int[] A = new int[36];
   private final int[] L = new int[36];
   private int B = 0;
   private int V = 0;
   private ItemStack l = null;
   private ItemStack W = null;

   private static boolean V(ItemFrame var0) {
      return var0.getDirection() == Direction.UP;
   }

   private static Vec3 p(Level var0, BlockPos var1) {
      Vec3 var2 = new Vec3(var1.getX() + 0.5, var1.getY() + 0.5, var1.getZ() + 0.5);
      BlockState var3 = var0.getBlockState(var1);
      if (!(var3.getBlock() instanceof DispenserBlock)) {
         return var2.add(0.0, 0.25, 0.0);
      }

      Direction var4 = (Direction)var3.getValue(DispenserBlock.FACING);
      return var2.add(var4.getStepX() * 0.6, var4.getStepY() * 0.6, var4.getStepZ() * 0.6);
   }

   private ig() {
   }

   public static ig r() {
      return P;
   }

   public void d(String var1) {
      if (var1 == null) {
         this.C = ig.e.S;
      } else if ("win".equalsIgnoreCase(var1)) {
         this.C = ig.e.p;
      } else if ("lose".equalsIgnoreCase(var1)) {
         this.C = ig.e.P;
      } else {
         this.C = ig.e.S;
      }

      if (this.C == ig.e.S) {
         this.Z = (boolean)0;
      }
   }

   public void H() {
      this.C = switch (this.C) {
         case S -> ig.e.p;
         case p -> ig.e.P;
         case P -> ig.e.S;
      };
      if (this.C == ig.e.S) {
         bn.j("Arrow Game", "Target off");
      } else {
         bn.j("Arrow Game", "Target " + this.C.C);
      }
   }

   public String Q() {
      return this.C == null ? "off" : this.C.C;
   }

   public String W() {
      return this.C == null ? "Off" : this.C.D;
   }

   public void Z() {
      this.Z = (boolean)1;
   }

   public boolean C() {
      return this.Z;
   }

   public void D() {
      this.Z = (boolean)0;
   }

   public boolean x() {
      return bf.v().c();
   }

   public boolean l(Player var1) {
      if (var1 == null) {
         return false;
      }

      ItemStack var2 = var1.getMainHandItem();
      return var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP) ? var2.getComponents().get(DataComponents.MAP_ID) != null : false;
   }

   public void I() {
      Minecraft var1 = Minecraft.getInstance();
      LocalPlayer var2 = var1 != null ? var1.player : null;
      if (!this.l(var2)) {
         bn.j("Arrow Game", "Hold a filled map in your hand to set the X map");
      } else {
         MapId var3 = (MapId)var2.getMainHandItem().getComponents().get(DataComponents.MAP_ID);
         bf.v().J(var3.id());
         this.Z = (boolean)0;
         bn.j(
            "Arrow Game",
            "X map set to map #" + var3.id() + " - frames holding this map are the LOSE target, any other item (elytra, block, ...) is the WIN target"
         );
      }
   }

   public void W() {
      bf.v().J(-1);
      this.Z = (boolean)0;
      bn.j("Arrow Game", "X map cleared - falling back to filled=win, empty=lose");
   }

   private static boolean Q(ItemFrame var0) {
      return var0 != null && var0.getItem() != null && !var0.getItem().isEmpty();
   }

   private static boolean G(ItemFrame var0) {
      return Q(var0) && var0.getItem().is(Items.FILLED_MAP) ? var0.getItem().getComponents().get(DataComponents.MAP_ID) != null : false;
   }

   private static boolean z(ItemFrame var0) {
      if (!G(var0)) {
         return false;
      }

      int var1 = bf.v().G();
      return var1 < 0 ? false : ((MapId)var0.getItem().getComponents().get(DataComponents.MAP_ID)).id() == var1;
   }

   private static boolean r(ItemFrame var0) {
      return !Q(var0) ? false : !z(var0);
   }

   public boolean O(Minecraft var1, BlockPos var2) {
      if (!this.I || this.C == ig.e.S) {
         return false;
      }

      if (var1 != null && var1.player != null && var1.level != null && var2 != null) {
         BlockState var3 = var1.level.getBlockState(var2);
         if (!(var3.getBlock() instanceof LeverBlock)) {
            return false;
         }

         BlockPos var4 = hk.e().f(hk.k.N);
         if (var4 == null) {
            return false;
         }

         if (y(var2, var4) > 4.0) {
            return false;
         }

         int var5 = !var3.getValue(BlockStateProperties.POWERED) ? 1 : 0;
         this.P(var1, var2, var3);
         if (var5 != 0) {
            this.L(var1, var4);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean l(BlockPos var1) {
      return bf.v().X(var1);
   }

   public void o(Minecraft var1, BlockPos var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         BlockState var3 = var1.level.getBlockState(var2);
         if (var3.getBlock() instanceof NoteBlock) {
            bf.v().H(var2);
            bn.j("Arrow Game", "Bound noteblock at " + var2.toShortString());
         }
      }
   }

   public void X() {
      bf.v().H(null);
   }

   public boolean M(Minecraft var1, BlockPos var2) {
      if (!this.I || this.C == ig.e.S) {
         return false;
      }

      if (var1 == null || var1.player == null || var1.level == null || var2 == null) {
         return false;
      }

      if (!this.l(var2)) {
         return false;
      }

      BlockState var3 = var1.level.getBlockState(var2);
      if (!(var3.getBlock() instanceof NoteBlock)) {
         return false;
      }

      BlockPos var4 = hk.e().f(hk.k.N);
      if (var4 == null) {
         return false;
      }

      if (this.h != var1.level) {
         this.E();
      }

      long var5 = var1.level.getGameTime();
      if (var5 < this.r) {
         return true;
      }

      this.r = var5 + 4L;
      this.i(var1, var3, var2);
      this.w(var1, var2);
      this.h = var1.level;
      this.H.add(new ig.u(var4.immutable(), this.C, var1.level.getGameTime() + 6L));
      return true;
   }

   private void i(Minecraft var1, BlockState var2, BlockPos var3) {
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

   private void w(Minecraft var1, BlockPos var2) {
      this.P(var1);
      BlockPos var3 = var2.below();
      BlockState var4 = var1.level.getBlockState(var3);
      if (var4.getBlock() instanceof ObserverBlock && var4.getValue(ObserverBlock.FACING) == Direction.UP) {
         this.v = var1.level;
         this.n.put(var3, var4);
         ArrayDeque var5 = new ArrayDeque();
         BlockPos var6 = var3.below();
         var5.add(var6);

         for (Direction var10 : Direction.values()) {
            var5.add(var6.relative(var10));
         }

         while (!var5.isEmpty() && this.n.size() < 32) {
            BlockPos var14 = (BlockPos)var5.removeFirst();
            if (!this.n.containsKey(var14) && !(y(var14, var3) > 256.0)) {
               BlockState var16 = var1.level.getBlockState(var14);
               BlockState var18 = u(var16);
               if (var18 != null) {
                  this.n.put(var14, var16);

                  for (Direction var13 : Direction.values()) {
                     var5.add(var14.relative(var13));
                  }
               }
            }
         }

         for (Entry var17 : this.n.entrySet()) {
            BlockState var19 = ((BlockState)var17.getValue()).getBlock() instanceof ObserverBlock
               ? (BlockState)((BlockState)var17.getValue()).setValue(BlockStateProperties.POWERED, true)
               : u((BlockState)var17.getValue());
            var1.level.setBlock((BlockPos)var17.getKey(), var19, 2);
         }

         this.S = var1.level.getGameTime() + 4L;
      }
   }

   static BlockState u(BlockState var0) {
      if (var0.getBlock() instanceof RedstoneWireBlock) {
         return (BlockState)var0.setValue(RedstoneWireBlock.POWER, 15);
      } else if (var0.getBlock() instanceof RepeaterBlock) {
         return (BlockState)var0.setValue(BlockStateProperties.POWERED, true);
      } else {
         return var0.getBlock() instanceof RedstoneLampBlock ? (BlockState)var0.setValue(BlockStateProperties.LIT, true) : null;
      }
   }

   private void P(Minecraft var1) {
      if (var1.level != null && var1.level == this.v) {
         for (Entry var3 : this.n.entrySet()) {
            BlockState var4 = ((BlockState)var3.getValue()).getBlock() instanceof ObserverBlock
               ? (BlockState)((BlockState)var3.getValue()).setValue(BlockStateProperties.POWERED, true)
               : u((BlockState)var3.getValue());
            if (var4 != null && var1.level.getBlockState((BlockPos)var3.getKey()).equals(var4)) {
               var1.level.setBlock((BlockPos)var3.getKey(), (BlockState)var3.getValue(), 2);
            }
         }
      }

      this.n.clear();
      this.v = null;
      this.S = -1L;
   }

   private static AABB q(Vec3 var0) {
      return new AABB(var0.x - 64.0, var0.y - 64.0, var0.z - 64.0, var0.x + 64.0, var0.y + 64.0, var0.z + 64.0);
   }

   private Vec3 a(ItemFrame var1) {
      AABB var2 = var1.getBoundingBox();
      double var3 = 0.2;
      double var5 = Math.max(0.0, var2.getXsize() - 2.0 * var3);
      double var7 = Math.max(0.0, var2.getYsize() - 2.0 * var3);
      double var9 = Math.max(0.0, var2.getZsize() - 2.0 * var3);
      return new Vec3(
         var2.minX + var3 + this.N.nextDouble() * var5, var2.minY + var3 + this.N.nextDouble() * var7, var2.minZ + var3 + this.N.nextDouble() * var9
      );
   }

   public void E(Minecraft var1) {
      vb.L(var1);
      if (this.h != var1.level) {
         this.E();
         this.h = var1.level;
      }

      if (var1.level != null) {
         if (this.S >= 0L && var1.level.getGameTime() >= this.S) {
            this.P(var1);
         }

         if (!this.I) {
            this.Z = (boolean)0;
            this.l(var1);
         }

         Iterator var2 = this.H.iterator();

         while (var2.hasNext()) {
            ig.u var3 = (ig.u)var2.next();
            if (var1.level.getGameTime() >= var3.due()) {
               var2.remove();
               this.V(var1, var3.dispenser(), var3.target());
            }
         }

         if (!this.m.isEmpty()) {
            long var8 = var1.level.getGameTime();
            long var4 = var8 - this.i;
            if (var4 > 0L) {
               this.i = var8;

               for (long var6 = 0L; var6 < var4 && !this.m.isEmpty(); var6++) {
                  this.R(var1);
               }
            }
         }
      }
   }

   private void R(Minecraft var1) {
      Iterator var2 = this.m.iterator();

      while (var2.hasNext()) {
         ig.y var3 = (ig.y)var2.next();
         if (var3.B == null) {
            this.U(var1, var3);
            var2.remove();
         } else if (var3.W <= 0) {
            var3.u++;
            var3.P *= 0.99;
            var3.R = var3.R * 0.99 - 0.05;
            var3.H *= 0.99;
            var3.n = var3.n.add(var3.P, var3.R, var3.H);
            double var15 = Math.sqrt(var3.P * var3.P + var3.H * var3.H);
            var3.B.setYRot((float)Math.toDegrees(Math.atan2(var3.P, var3.H)));
            var3.B.setXRot((float)Math.toDegrees(Math.atan2(var3.R, Math.max(var15, 1.0E-4))));
            var3.B.setPos(var3.n.x, var3.n.y, var3.n.z);
            if (var3.u >= 120 || var3.b != null && A(var3.n.subtract(var3.P, var3.R, var3.H), var3.n, var3.b) <= 0.5) {
               var3.e = new Vec3(var3.n.x, var3.n.y, var3.n.z);
               var3.B.discard();
               var3.B = null;
               var2.remove();
               this.U(var1, var3);
            }
         } else if (var3.U >= var3.W) {
            var3.e = var3.n;
            var3.B.discard();
            var3.B = null;
            var2.remove();
            this.U(var1, var3);
         } else {
            Vec3 var4 = var3.n;
            var3.U = a(var3.U, var3.W);
            int var5 = Math.min((int)var3.U, var3.W - 1);
            double var6 = var3.U - var5;
            double[] var8 = var5 == 0 ? new double[3] : var3.C[var5 - 1];
            double[] var9 = var3.C[var5];
            var3.n = new Vec3(
               var3.s.x + var8[0] + (var9[0] - var8[0]) * var6,
               var3.s.y + var8[1] + (var9[1] - var8[1]) * var6,
               var3.s.z + var8[2] + (var9[2] - var8[2]) * var6
            );
            var3.P = var3.n.x - var4.x;
            var3.R = var3.n.y - var4.y;
            var3.H = var3.n.z - var4.z;
            double var10 = Math.sqrt(var3.P * var3.P + var3.H * var3.H);
            float var12 = (float)Math.toDegrees(Math.atan2(var3.P, var3.H));
            float var13 = (float)Math.toDegrees(Math.atan2(var3.R, Math.max(var10, 1.0E-4)));
            var3.N = q(var3.N, var12);
            var3.a = q(var3.a, var13);
            var3.B.setYRot(var3.N);
            var3.B.setXRot(var3.a);
            var3.B.setPos(var3.n.x, var3.n.y, var3.n.z);
            var3.u++;
            int var14 = !(var3.U >= var3.W) && (var3.b == null || !(A(var4, var3.n, var3.b) <= 0.5)) ? 0 : 1;
            if (var14 != 0) {
               var3.e = var3.n;
               var3.B.discard();
               var3.B = null;
               var2.remove();
               this.U(var1, var3);
            }
         }
      }
   }

   static double a(double var0, int var2) {
      return Math.min(var0 + 0.85, var2);
   }

   private void L(Minecraft var1, BlockPos var2) {
      this.V(var1, var2, this.C);
   }

   private void V(Minecraft var1, BlockPos var2, ig.e var3) {
      this.h = var1.level;
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      Vec3 var6 = new Vec3(var2.getX() + 0.5, var2.getY() + 0.5, var2.getZ() + 0.5);
      AABB var7 = q(var6);
      bf var8 = bf.v();
      boolean var9 = var8.c();
      bn.b("Arrow beginShot: dispenser " + var2 + ", target " + var3.C + ", xMapSet=" + var9 + ", xMapId=" + var8.G() + ", serverKey=" + var8.S());
      if (var1.level != null) {
         for (ItemFrame var11 : var1.level.getEntitiesOfClass(ItemFrame.class, var7, var0 -> !var0.isRemoved() && V(var0))) {
            if (!(var11.distanceToSqr(var6) > 4096.0)) {
               byte var12;
               String var13;
               if (var9) {
                  if (z(var11)) {
                     var12 = 0;
                     var13 = "xMap";
                  } else {
                     if (!Q(var11)) {
                        bn.b("Arrow frame " + var11.blockPosition() + " skipped (empty)");
                        continue;
                     }

                     var12 = 1;
                     var13 = "item";
                  }
               } else {
                  byte var14 = Q(var11);
                  var12 = var14;
                  var13 = "filled=" + var14;
               }

               bn.b(
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

      ArrayList var17 = var3 == ig.e.p ? var4 : var5;
      if (var17.isEmpty()) {
         bn.j(
            "Arrow Game",
            "No "
               + var3.C
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

         for (ig.y var22 : this.m) {
            var19.add(var22.J);
         }

         ArrayList var21 = new ArrayList();

         for (ItemFrame var25 : var17) {
            if (var19.contains(var25.getId())) {
               bn.b("Arrow frame " + var25.getId() + " already targeted, not re-firing");
            } else {
               var21.add(var25);
            }
         }

         if (var21.isEmpty()) {
            bn.j("Arrow Game", "Every reachable " + var3.C + " frame already has an arrow in flight; skipping this flick");
         } else {
            var17 = var21;
            int var24 = 0;
            if (var1.level != null) {
               double var26 = 64.0;
               AABB var16 = new AABB(var6.x - var26, var6.y - var26, var6.z - var26, var6.x + var26, var6.y + var26, var6.z + var26);
               var24 = var1.level.getEntitiesOfClass(ItemFrame.class, var16, var0 -> !var0.isRemoved() && V(var0)).size();
            }

            bn.j(
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
                  + var3.C
            );
            Vec3 var27 = p(var1.level, var2);
            ItemFrame var15 = (ItemFrame)var17.get(this.N.nextInt(var17.size()));
            ig.y var28 = new ig.y();
            var28.J = var15.getId();
            var28.n = var27;
            var28.b = this.a(var15);
            var28.s = var28.n;
            if (var1.level != null) {
               var28.B = (Arrow)EntityTypes.ARROW.create(var1.level, EntitySpawnReason.COMMAND);
               if (var28.B != null) {
                  var28.B.setId(this.k--);
                  var28.B.setNoGravity(true);
                  var28.B.setNoPhysics(true);
                  var28.B.setPos(var28.n.x, var28.n.y, var28.n.z);
                  this.t(var28);
                  var1.level.addEntity(var28.B);
               }

               var1.level
                  .playSound(null, var28.n.x, var28.n.y, var28.n.z, SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 1.0F, 0.4F + this.N.nextFloat() * 1.2F);
            }

            this.m.add(var28);
            this.Y(var1, var2);
            if (var1.level != null) {
               this.i = var1.level.getGameTime();
            }

            bn.j("Arrow Game", "Firing at " + var3.C + " frame " + var28.J);
         }
      }
   }

   private void t(ig.y var1) {
      if (var1.b != null) {
         var1.s = var1.n;
         double var2 = 11.7 + this.N.nextDouble() * 0.8000000000000007;
         var1.A = var2;
         if (this.G(var1)) {
            var1.U = 0.0;
            this.p(var1, var1.c[0][0], var1.c[0][1], var1.c[0][2]);
            bn.b(
               "Arrow trajectory targetCrest="
                  + String.format("%.1f", var1.A)
                  + " speed="
                  + String.format("%.2f", var1.K)
                  + " pitch="
                  + String.format("%.1f", var1.p)
                  + "deg crest="
                  + String.format("%.1f", var1.X)
                  + " ticks="
                  + var1.W
                  + " to "
                  + var1.b
            );
         } else {
            Vec3 var4 = var1.b.subtract(var1.n).normalize();
            var1.P = var4.x * 1.1;
            var1.R = var4.y * 1.1;
            var1.H = var4.z * 1.1;
            this.p(var1, var1.P, var1.R, var1.H);
            bn.b("Arrow trajectory FAILED -> straight shot from " + var1.n + " to " + var1.b);
         }
      }
   }

   private void p(ig.y var1, double var2, double var4, double var6) {
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      float var10 = (float)Math.toDegrees(Math.atan2(var2, var6));
      float var11 = (float)Math.toDegrees(Math.atan2(var4, var8));
      var1.N = var10;
      var1.a = var11;
      if (var1.B != null) {
         var1.B.setYRot(var10);
         var1.B.setXRot(var11);
         var1.B.yRotO = var10;
         var1.B.xRotO = var11;
      }
   }

   private boolean G(ig.y var1) {
      double var2 = var1.b.x - var1.n.x;
      double var4 = var1.b.y - var1.n.y;
      double var6 = var1.b.z - var1.n.z;
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      if (var8 < 0.001) {
         return false;
      }

      ig.i.w var10 = ig.i.T(var2, var4, var6, var1.A);
      if (var10 == null) {
         return false;
      }

      var1.C = var10.a;
      var1.c = var10.l;
      var1.W = var10.v;
      var1.p = var10.p;
      var1.K = var10.E;
      var1.d = 0.0;
      var1.X = var10.U;
      var1.T = var10.v;
      var1.P = var10.X;
      var1.R = var10.D;
      var1.H = var10.S;
      return true;
   }

   private static double A(Vec3 var0, Vec3 var1, Vec3 var2) {
      Vec3 var3 = var1.subtract(var0);
      Vec3 var4 = var2.subtract(var0);
      double var5 = var3.dot(var3);
      double var7 = var5 > 1.0E-9 ? var4.dot(var3) / var5 : 0.0;
      var7 = Math.max(0.0, Math.min(1.0, var7));
      return var2.distanceTo(var0.add(var3.scale(var7)));
   }

   private static float q(float var0, float var1) {
      float var2 = ((var1 - var0) % 360.0F + 540.0F) % 360.0F - 180.0F;
      return var0 + var2 * 0.2F;
   }

   private void U(Minecraft var1, ig.y var2) {
      if (var1 != null && var1.level != null) {
         Vec3 var3 = var2.e != null ? var2.e : var2.n;
         ItemFrame var5 = var1.level.getEntity(var2.J) instanceof ItemFrame var6 && !var6.isRemoved() ? var6 : null;
         if (var5 == null) {
            bn.j("Arrow Game", "Arrow target unloaded before impact; no visual item dropped");
         } else {
            bn.b(
               "Arrow landing target: id="
                  + var5.getId()
                  + " at "
                  + var5.blockPosition()
                  + " (arrow landed at "
                  + String.format("%.1f,%.1f,%.1f", var3.x, var3.y, var3.z)
                  + ")"
            );
            this.b(var1, var5, var3);
            vb.k(var1, var5);
            bn.j("Arrow Game", "Visual hit: item popped from frame " + var5.getId());
         }
      }
   }

   private void b(Minecraft var1, ItemFrame var2, Vec3 var3) {
      if (var1 != null && var1.level != null && var2 != null) {
         Vec3 var4 = var3 != null ? var3 : var2.getBoundingBox().getCenter();
         if (var1.getSoundManager() != null) {
            var1.getSoundManager()
               .play(
                  new SimpleSoundInstance(
                     SoundEvents.ARROW_HIT.location(),
                     SoundSource.NEUTRAL,
                     2.0F,
                     0.8F + this.N.nextFloat() * 0.4F,
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

   private boolean P(ItemFrame var1) {
      if (var1 == null) {
         return false;
      }

      if (bf.v().c()) {
         return this.C == ig.e.p ? r(var1) : z(var1);
      }

      byte var2 = Q(var1);
      return (boolean)(this.C == ig.e.p ? var2 : var2 == 0);
   }

   private void P(Minecraft var1, BlockPos var2, BlockState var3) {
      this.D = var2.immutable();
      if (this.p == null) {
         this.p = var3;
      }

      int var4 = !var3.getValue(BlockStateProperties.POWERED) ? 1 : 0;
      if (var1.level != null) {
         var1.level.setBlockAndUpdate(var2, (BlockState)var3.setValue(BlockStateProperties.POWERED, Boolean.valueOf((boolean)var4)));
      }

      if (var1.player != null) {
         var1.player.playSound(SoundEvents.LEVER_CLICK, 0.3F, var4 != 0 ? 0.6F : 0.5F);
      }
   }

   private void l(Minecraft var1) {
      if (var1 != null && var1.level != null && this.D != null) {
         if (this.p != null) {
            var1.level.setBlockAndUpdate(this.D, this.p);
         }

         this.D = null;
         this.p = null;
      }
   }

   public void E() {
      this.Z = (boolean)0;
      this.P(Minecraft.getInstance());

      for (ig.y var2 : this.m) {
         if (var2.B != null) {
            var2.B.discard();
         }
      }

      this.m.clear();
      this.H.clear();
      this.r = -1L;
      this.i = -1L;
      Minecraft var3 = Minecraft.getInstance();
      this.l(var3);
   }

   public void Y(Minecraft var1, BlockPos var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         String var3 = hk.e().v(var1, var2);
         int[] var4 = this.e.get(var3);
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
               int var7 = (Integer)var5.get(this.N.nextInt(var5.size()));
               var4[var7] = Math.max(0, var4[var7] - 1);
            }
         }
      }
   }

   public void d(Minecraft var1, DispenserMenu var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         this.B = 0;
         this.V = 0;
         int[] var3 = new int[9];
         byte var4 = 0;

         for (Slot var6 : var2.slots) {
            if (!(var6.container instanceof Inventory) && var6.getContainerSlot() >= 0 && var6.getContainerSlot() <= 8 && var6.hasItem()) {
               ItemStack var7 = var6.getItem();
               if (K(var7)) {
                  var3[var6.getContainerSlot()] = var7.getCount();
                  var4 = 1;
               }
            }
         }

         if (var4 != 0) {
            String var12 = hk.e().v();
            if (var12 != null) {
               int[] var13 = this.R.get(var12);
               this.R.put(var12, var3);
               int[] var14 = this.e.get(var12);
               if (var14 == null) {
                  var14 = (int[])var3.clone();
                  this.e.put(var12, var14);
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

                  int var16 = (Integer)var10.get(this.N.nextInt(var10.size()));
                  var14[var16] = Math.max(0, var14[var16] - 1);
               }
            }
         }
      }
   }

   public void D(String var1, int var2, ItemStack var3) {
      if (var1 != null) {
         this.l = null;
         this.W = null;
         int var4 = K(var3) ? var3.getCount() : 0;
         if (var2 >= 0 && var2 <= 8) {
            int[] var10 = this.R.get(var1);
            int[] var11 = this.e.get(var1);
            if (var10 == null || var11 == null) {
               return;
            }

            int var12 = var10[var2];
            int var13 = var11[var2];
            int var9 = this.v(var12, var13, var4);
            var10[var2] = var4;
            var11[var2] = var9;
         } else if (var2 >= 9 && var2 <= 44) {
            int var5 = var2 >= 36 ? var2 - 36 : var2;
            if (var5 < 0 || var5 >= 36) {
               return;
            }

            int var6 = this.L[var5];
            int var7 = this.A[var5];
            int var8 = this.v(var6, var7, var4);
            this.L[var5] = var4;
            this.A[var5] = var8;
         }
      }
   }

   public void l(int var1, ItemStack var2) {
      if (var1 >= 0 && var1 < 36) {
         this.l = null;
         this.W = null;
         if (this.A[var1] != 0 || this.L[var1] != 0 || this.B != 0) {
            int var3 = K(var2) ? var2.getCount() : 0;
            int var4 = this.L[var1];
            int var5 = this.A[var1];
            this.L[var1] = var3;
            this.A[var1] = this.v(var4, var5, var3);
         }
      }
   }

   private int v(int var1, int var2, int var3) {
      if (var3 <= 0) {
         if (var1 > 0 && var2 > 0) {
            this.B += var1;
            this.V = this.V + Math.max(0, var1 - var2);
         }

         return 0;
      } else {
         if (var1 == var3) {
            return Math.min(var2, var3);
         }

         if (var3 > var1) {
            int var8 = var3 - var1;
            int var9 = var8;
            if (this.B > 0) {
               int var6 = Math.min(var8, this.B);
               int var7 = Math.round((float)((long)this.V * var6 / Math.max(1, this.B)));
               var9 = var8 - var7;
               this.B -= var6;
               this.V = Math.max(0, this.V - var7);
            }

            return Math.min(var2 + Math.max(0, var9), var3);
         } else {
            int var4 = var1 - var3;
            int var5 = Math.round((float)((long)var2 * var4 / Math.max(1, var1)));
            this.B += var4;
            this.V = this.V + Math.max(0, var4 - Math.min(var5, var4));
            return Math.max(0, var2 - var5);
         }
      }
   }

   public ItemStack Y(Minecraft var1, int var2, ItemStack var3) {
      if (var1 == null || var1.level == null || var3 == null || var3.isEmpty()) {
         return null;
      }

      if (var2 >= 0 && var2 <= 8) {
         hk var4 = hk.e();
         String var5 = var4.v();
         if (var5 == null) {
            return null;
         } else {
            int[] var6 = this.e.get(var5);
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

   public ItemStack q(int var1, ItemStack var2) {
      if (var2 == null || var2.isEmpty() || !K(var2)) {
         return null;
      } else if (var1 >= 0 && var1 < 36) {
         int var3 = this.A[var1];
         return var3 > 0 && var3 < var2.getCount() ? var2.copyWithCount(var3) : null;
      } else {
         return null;
      }
   }

   public ItemStack u(ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && K(var1)) {
         if (this.W != null && !this.W.isEmpty() && this.l != null && var1.is(this.l.getItem()) && var1.getCount() == this.l.getCount()) {
            return this.W.copy();
         }

         if (this.B > 0 && this.V > 0) {
            int var2 = var1.getCount() - this.V;
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

   public void U(ItemStack var1, ItemStack var2) {
      if (var1 != null && !var1.isEmpty()) {
         this.l = var1;
         this.W = var2 != null && !var2.isEmpty() ? var2 : var1;
      } else {
         this.l = null;
         this.W = null;
      }
   }

   public boolean H() {
      return this.I && this.C != ig.e.S;
   }

   private static boolean K(ItemStack var0) {
      return var0.is(Items.ARROW) || var0.is(Items.SPECTRAL_ARROW) || var0.is(Items.TIPPED_ARROW);
   }

   private static double y(BlockPos var0, BlockPos var1) {
      int var2 = var0.getX() - var1.getX();
      int var3 = var0.getY() - var1.getY();
      int var4 = var0.getZ() - var1.getZ();
      return var2 * var2 + var3 * var3 + var4 * var4;
   }

   public enum e {
      S("off", "Off"),
      p("win", "Win (filled frames)"),
      P("lose", "Lose (empty frames)");

      public final String C;
      public final String D;

      e(String var3, String var4) {
         this.C = var3;
         this.D = var4;
      }
   }

   private static final class i {
      static final int t = 24;
      static final int c = 80;

      private static double u(int var0) {
         return 0.99 * (1.0 - Math.pow(0.99, var0)) / 0.010000000000000009;
      }

      static ig.i.w T(double var0, double var2, double var4, double var6) {
         double var8 = Math.sqrt(var0 * var0 + var4 * var4);
         if (var8 < 0.001) {
            return null;
         }

         int var10 = 24;
         double var11 = Double.MAX_VALUE;
         double var13 = 0.0;

         for (int var15 = 24; var15 <= 80; var15++) {
            double var16 = u(var15);
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
         double var43 = u(var42);
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
         return new ig.i.w(var42, var47, var25, var44, var46, var45, var13, var50, var40);
      }

      static final class w {
         final int v;
         final double[][] a;
         final double[][] l;
         final double X;
         final double D;
         final double S;
         final double U;
         final double p;
         final double E;

         w(int var1, double[][] var2, double[][] var3, double var4, double var6, double var8, double var10, double var12, double var14) {
            this.v = var1;
            this.a = var2;
            this.l = var3;
            this.X = var4;
            this.D = var6;
            this.S = var8;
            this.U = var10;
            this.p = var12;
            this.E = var14;
         }
      }
   }

   private record u(BlockPos dispenser, ig.e target, long due) {
   }

   private static final class y {
      Vec3 n;
      Vec3 b;
      Vec3 e;
      int J;
      int u;
      float N;
      float a;
      double P;
      double R;
      double H;
      Arrow B;
      double[][] C;
      double[][] c;
      int W;
      double U;
      Vec3 s;
      double A;
      double p;
      double K;
      double d;
      double X;
      int T;
   }
}
