package com.larpclient.module;

import com.larpclient.bf;
import com.larpclient.g0;
import com.larpclient.md;
import com.larpclient.s2;
import com.larpclient.z4;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Load;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.AABB;

public final class pi extends pm {
   private final l z = this.addSetting(
      new l(
         "Detection",
         "Conservative: dedicated blocks. Balanced: storage plus utilities. Broad: more possible structures",
         "Balanced",
         "Conservative",
         "Balanced",
         "Broad"
      )
   );
   private final e n = this.addSetting(
      new e("Min Storage", "Ordinary storage required in one 16-block height band; dedicated clues bypass this", 4.0, 1.0, 64.0, 1.0)
   );
   private final e Q = this.addSetting(
      new e("Min Utilities", "Crafting/workstation blocks supporting ordinary storage in Balanced detection", 2.0, 1.0, 32.0, 1.0)
   );
   private final e R = this.addSetting(new e("Min Ender Chests", "Ender chests needed in one 16-block height band outside the End", 1.0, 1.0, 64.0, 1.0));
   private final e C = this.addSetting(new e("Min Scan Y", "Lowest received block height to inspect", -64.0, -64.0, 60.0, 1.0));
   private final e P = this.addSetting(new e("Max Scan Y", "Highest block height to inspect, capped at Y60", 60.0, -64.0, 60.0, 1.0));
   private final s Z = this.addSetting(new s("Terrain Clues", "Include deepslate placement evidence in Broad detection; never proof on its own", true));
   private final s p = this.addSetting(new s("Base Boxes", "Outline the detected building evidence as well as its chunk", true));
   private final l u = this.addSetting(new l("Base Style", "Box style for the detected building evidence", "Box", "Box", "Corners", "Filled"));
   private final s2.q O = new s2.q();
   private final s S = this.addSetting(
      new s("Visibility Diagnostic", "Experimental received-data observations in latest.log; does not recover hidden blocks", false)
   );
   private final bf k = new bf();
   private ClientLevel E;
   private long I;
   private boolean e;
   private static pi B;
   private boolean Y;
   private final s j = this.addSetting(new s("Honey Clues", "Full hives or nests after 10 seconds loaded; natural bee activity can qualify", true));
   private final Map<Long, Long> W = new HashMap<>();
   private boolean g;
   private int d;
   private int A;
   private boolean c;
   private boolean x;

   public pi() {
      super("Base Finder", "Find base clues in loaded blocks");

      for (q var2 : this.detectionSettings()) {
         var2.o(var1 -> this.n("detection changed"));
      }

      this.n.P(() -> !this.z.getValue().equals("Conservative"));
      this.Q.P(() -> this.z.getValue().equals("Balanced"));
      this.Z.P(() -> this.z.getValue().equals("Broad"));
      this.u.P(this.p::getValue);
      this.L();
      B = this;
      this.S.o(var1 -> {
         if (this.E != null) {
            this.P("setting changed");
         }

         this.x();
      });
      ClientChunkEvents.CHUNK_LOAD.register((Load)(var1, var2x) -> this.N(var1, var2x));
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2x) -> {
         if (var1 == this.E) {
            this.k.F(var2x.getPos().pack());
         }

         if (getMc() != null && var1 == getMc().level) {
            this.W.remove(var2x.getPos().pack());
         }
      });
   }

   @Override
   public List<q<?>> detectionSettings() {
      return List.of(this.z, this.n, this.Q, this.R, this.C, this.P, this.Z, this.j);
   }

   @Override
   public List<q<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.add(this.p);
      var1.add(this.u);
      return List.copyOf(var1);
   }

   public static AABB h(ChunkPos var0, g0.t var1) {
      AABB var2 = var1.base().bounds();
      return var2 == null ? null : var2.move(var0.getMinBlockX(), 0.0, var0.getMinBlockZ()).inflate(0.08);
   }

   @Override
   protected void drawEvidence(ChunkPos var1, g0.t var2, z4 var3, int var4) {
      if (this.p.getValue()) {
         AABB var5 = h(var1, var2);
         if (var5 != null) {
            var3.R(var5, this.u.getValue(), var4);
         }
      }
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return this.H(var1, 16);
   }

   private boolean H(BlockState var1, int var2) {
      if (s2.k(var1)) {
         return this.j.getValue();
      } else {
         s2.p var3 = s2.Q(var1, var2, this.Y);
         if (var3 == s2.p.R) {
            return true;
         } else {
            return this.z.getValue().equals("Conservative")
               ? false
               : var3 == s2.p.r || var3 == s2.p.v || this.z.getValue().equals("Broad") && (var3 == s2.p.d || var3 == s2.p.Q && this.Z.getValue());
         }
      }
   }

   @Override
   protected boolean sectionCandidate(LevelChunk var1, int var2) {
      LevelChunkSection var3 = var1.getSections()[var2];
      int var4 = var1.getMinY() + var2 * 16 + 15;
      return !var3.hasOnlyAir() && var3.maybeHas(var2x -> this.H(var2x, var4));
   }

   @Override
   protected int minimumY() {
      return Math.min(this.C.getValue().intValue(), this.P.getValue().intValue());
   }

   @Override
   protected int maximumY() {
      return Math.min(60, Math.max(this.C.getValue().intValue(), this.P.getValue().intValue()));
   }

   @Override
   protected void beforeScan() {
      ChunkPos var1 = getMc().player.chunkPosition();
      this.W
         .keySet()
         .removeIf(
            var2 -> {
               ChunkPos var3 = ChunkPos.unpack(var2);
               return Math.abs((long)var3.x() - var1.x()) > this.Y()
                  || Math.abs((long)var3.z() - var1.z()) > this.Y()
                  || getMc().level.getChunkSource().getChunkNow(var3.x(), var3.z()) == null;
            }
         );
   }

   @Override
   protected void beginChunk(LevelChunk var1) {
      long var2 = this.W.computeIfAbsent(var1.getPos().pack(), var1x -> this.K());
      this.g = (boolean)(this.K() - var2 >= 200L ? 1 : 0);
   }

   @Override
   protected void beginScan() {
      this.O.M();
      this.g = (boolean)0;
      this.Y = (boolean)(getMc() != null && getMc().level != null && getMc().level.dimension().equals(Level.END) ? 1 : 0);
      this.L();
   }

   private void L() {
      this.d = this.minimumY();
      this.A = this.maximumY();
      this.c = (boolean)(this.z.getValue().equals("Broad") && this.Z.getValue() ? 1 : 0);
      this.x = this.j.getValue();
   }

   @Override
   protected void count(BlockState var1, int var2) {
      this.count(var1, 0, var2, 0);
   }

   @Override
   protected void count(BlockState var1, int var2, int var3, int var4) {
      if (var3 >= this.d && var3 <= this.A) {
         s2.p var5 = this.O.B(var1, var3, this.Y);
         if (var5 != s2.p.g) {
            if (var5 != s2.p.R || !s2.k(var1) || this.x && this.g) {
               this.O.p(var5, var1, var2, var3, var4, this.Y, this.c);
            }
         }
      }
   }

   @Override
   protected g0.t evidence() {
      com.larpclient.tm var1 = this.O.A(this.z.getValue(), this.n.getValue().intValue(), this.Q.getValue().intValue(), this.R.getValue().intValue());
      return new g0.t(0, 0, 0, var1.sampleY(), var1);
   }

   @Override
   protected boolean qualifies(g0.t var1) {
      return var1.base().qualifies(this.z.getValue(), this.n.getValue().intValue(), this.Q.getValue().intValue(), this.R.getValue().intValue());
   }

   @Override
   protected String describe(g0.t var1) {
      return var1.base().describe();
   }

   @Override
   protected md alert(ChunkPos var1, g0.t var2) {
      return md.base(var1.getMinBlockX(), var1.getMinBlockZ(), var2.base());
   }

   @Override
   protected int tint() {
      return -6629451;
   }

   @Override
   protected void clearEvidence() {
      this.O.M();
      this.W.clear();
      this.g = (boolean)0;
      this.L();
      if (this.E != null) {
         this.P("scan reset or lifecycle cleanup");
      }

      this.x();
   }

   public List<q<?>> Q() {
      return List.of(this.S);
   }

   private void x() {
      this.k.F();
      this.E = null;
      this.I = 0L;
      this.e = (boolean)0;
   }

   private boolean H(ClientLevel var1) {
      if (this.S.getValue() && !this.e && this.isRuntimeActive() && this.isInGame() && getMc().level == var1 && getMc().isSameThread()) {
         if (this.E != var1) {
            if (this.E != null) {
               this.P("world changed");
            }

            this.x();
            this.E = var1;
            com.larpclient.h.A(
               "BaseFinding", "Visibility diagnostic active: received block-entity chunk snapshots and server-verified updates only; no bypass assumed"
            );
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean k(BlockPos var1) {
      if (var1.getY() >= this.minimumY() && var1.getY() <= this.maximumY()) {
         ChunkPos var2 = getMc().player.chunkPosition();
         return Math.abs((long)(var1.getX() >> 4) - var2.x()) <= this.Y() && Math.abs((long)(var1.getZ() >> 4) - var2.z()) <= this.Y();
      } else {
         return false;
      }
   }

   public static boolean Z(BlockState var0, int var1, boolean var2) {
      s2.p var3 = s2.Q(var0, var1, var2);
      return var3 != s2.p.g && var3 != s2.p.Q || var0.is(Blocks.SPAWNER);
   }

   public static boolean O(BlockState var0) {
      return var0.is(Blocks.DEEPSLATE) || var0.is(Blocks.STONE) || var0.is(Blocks.NETHERRACK) || var0.is(Blocks.END_STONE);
   }

   private void I(BlockPos var1, BlockState var2, bf.v var3) {
      if (this.k(var1)) {
         if (!this.k.p(this.I)) {
            this.k.o(var3, 1L);
         } else {
            this.k
               .G(
                  var1.asLong(),
                  ChunkPos.pack(var1.getX() >> 4, var1.getZ() >> 4),
                  var2.toString(),
                  Z(var2, var1.getY(), this.E.dimension().equals(Level.END)),
                  O(var2),
                  var3,
                  this.I
               );
         }
      }
   }

   private void N(ClientLevel var1, LevelChunk var2) {
      try {
         if (!this.H(var1)) {
            return;
         }

         ChunkPos var3 = getMc().player.chunkPosition();
         ChunkPos var4 = var2.getPos();
         if (Math.abs((long)var4.x() - var3.x()) > this.Y() || Math.abs((long)var4.z() - var3.z()) > this.Y()) {
            return;
         }

         this.k.P();

         for (long var6 : this.k.z(var4.pack())) {
            BlockPos var8 = BlockPos.of(var6);
            this.I(var8, var2.getBlockState(var8), bf.v.c);
         }

         int var10 = 0;

         for (BlockEntity var7 : var2.getBlockEntities().values()) {
            if (var10 == 256) {
               this.k.o(bf.v.c, var2.getBlockEntities().size() - var10);
               break;
            }

            var10++;
            if (!var7.isRemoved()) {
               this.I(var7.getBlockPos(), var7.getBlockState(), bf.v.c);
            }
         }
      } catch (RuntimeException var9) {
         this.S(var9);
      }
   }

   public static void R(ClientLevel var0, BlockPos var1, BlockState var2) {
      if (B != null && B.isEnabled()) {
         pi var3 = B;
         if (var3 != null) {
            try {
               if (!var3.H(var0)) {
                  return;
               }

               if (var0.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4) == null) {
                  return;
               }

               var3.I(var1, var2, bf.v.E);
            } catch (RuntimeException var5) {
               var3.S(var5);
            }
         }
      }
   }

   private void S(RuntimeException var1) {
      this.e = (boolean)1;
      com.larpclient.h.w("BaseFinding", "Visibility diagnostic failed; observations paused until reset, native updates and Base Finder remain active", var1);
   }

   private void P(String var1) {
      com.larpclient.h.A("BaseFinding", "Visibility diagnostic [{}]: {}", var1, this.k.Q());

      for (bf.d var3 : this.k.y(8)) {
         BlockPos var4 = BlockPos.of(var3.position());
         com.larpclient.h.A(
            "BaseFinding",
            "Visibility observation [{}, {}, {}] {} tick {}: {} -> {} ({})",
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            var3.source(),
            var3.tick(),
            var3.before(),
            var3.after(),
            var3.transition()
         );
      }
   }

   @Override
   public void onTick() {
      super.onTick();
      int var10000 = this.H(getMc().level);
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         this.I++;
         long var12;
         var10000 = (var12 = this.I % 20L - 0L) == 0L ? 0 : (var12 < 0L ? -1 : 1);
         boolean var4 = false;
         int var3 = var10000;
         var10000 = Integer.signum(var3);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.k.M(this.I);
         }

         long var13;
         var10000 = (var13 = this.I % 200L - 0L) == 0L ? 0 : (var13 < 0L ? -1 : 1);
         boolean var6 = false;
         int var5 = var10000;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.P("10 seconds");
         }
      }
   }

   @Override
   public String getStatus() {
      return super.getStatus() + (!this.S.getValue() ? "" : (this.e ? " · diagnostic paused" : " · diagnostic: " + this.k.Q().tracked() + " observations"));
   }
}
