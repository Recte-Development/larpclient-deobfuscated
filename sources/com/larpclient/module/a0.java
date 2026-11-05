package com.larpclient.module;

import com.larpclient.ce;
import com.larpclient.dq;
import com.larpclient.mt;
import com.larpclient.xe;
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
import net.minecraft.world.phys.AABB;

public final class a0 extends az {
   private final d6 s = this.addSetting(
      new d6(
         "Detection",
         "Conservative: dedicated blocks. Balanced: storage plus utilities. Broad: more possible structures",
         "Balanced",
         "Conservative",
         "Balanced",
         "Broad"
      )
   );
   private final d9 b = this.addSetting(
      new d9("Min Storage", "Ordinary storage required in one 16-block height band; dedicated clues bypass this", 4.0, 1.0, 64.0, 1.0)
   );
   private final d9 C = this.addSetting(
      new d9("Min Utilities", "Crafting/workstation blocks supporting ordinary storage in Balanced detection", 2.0, 1.0, 32.0, 1.0)
   );
   private final d9 O = this.addSetting(new d9("Min Ender Chests", "Ender chests needed in one 16-block height band outside the End", 1.0, 1.0, 64.0, 1.0));
   private final d9 t = this.addSetting(new d9("Min Scan Y", "Lowest received block height to inspect", -64.0, -64.0, 60.0, 1.0));
   private final d9 K = this.addSetting(new d9("Max Scan Y", "Highest block height to inspect, capped at Y60", 60.0, -64.0, 60.0, 1.0));
   private final dh E = this.addSetting(new dh("Terrain Clues", "Include deepslate placement evidence in Broad detection; never proof on its own", true));
   private final dh p = this.addSetting(new dh("Base Boxes", "Outline the detected building evidence as well as its chunk", true));
   private final d6 Q = this.addSetting(new d6("Base Style", "Box style for the detected building evidence", "Box", "Box", "Corners", "Filled"));
   private final xe.l w = new xe.l();
   private final dh W = this.addSetting(
      new dh("Visibility Diagnostic", "Experimental received-data observations in latest.log; does not recover hidden blocks", false)
   );
   private final com.larpclient.dn o = new com.larpclient.dn();
   private ClientLevel d;
   private long q;
   private boolean h;
   private static a0 H;
   private boolean T;
   private final dh J = this.addSetting(new dh("Honey Clues", "Full hives or nests after 10 seconds loaded; natural bee activity can qualify", true));
   private final Map<Long, Long> M = new HashMap<>();
   private boolean a;

   public a0() {
      super("Base Finder", "Find base clues in loaded blocks");

      for (df var2 : this.detectionSettings()) {
         var2.h(var1 -> this.s("detection changed"));
      }

      this.b.H(() -> !this.s.P().equals("Conservative"));
      this.C.H(() -> this.s.P().equals("Balanced"));
      this.E.H(() -> this.s.P().equals("Broad"));
      this.Q.H(this.p::P);
      H = this;
      this.W.h(var1 -> {
         if (this.d != null) {
            this.W("setting changed");
         }

         this.f();
      });
      ClientChunkEvents.CHUNK_LOAD.register((Load)(var1, var2x) -> this.h(var1, var2x));
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2x) -> {
         if (var1 == this.d) {
            this.o.X(var2x.getPos().pack());
         }

         if (getMc() != null && var1 == getMc().level) {
            this.M.remove(var2x.getPos().pack());
         }
      });
   }

   @Override
   public List<df<?>> detectionSettings() {
      return List.of(this.s, this.b, this.C, this.O, this.t, this.K, this.E, this.J);
   }

   @Override
   public List<df<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.add(this.p);
      var1.add(this.Q);
      return List.copyOf(var1);
   }

   public static AABB H(ChunkPos var0, com.larpclient.d6.f var1) {
      AABB var2 = var1.base().bounds();
      return var2 == null ? null : var2.move(var0.getMinBlockX(), 0.0, var0.getMinBlockZ()).inflate(0.08);
   }

   @Override
   protected void drawEvidence(ChunkPos var1, com.larpclient.d6.f var2, dq var3, int var4) {
      if (this.p.P()) {
         AABB var5 = H(var1, var2);
         if (var5 != null) {
            var3.O(var5, this.Q.P(), var4);
         }
      }
   }

   @Override
   protected boolean candidate(BlockState var1) {
      if (xe.y(var1)) {
         return this.J.P();
      } else {
         xe.v var2 = xe.j(var1, 16, this.T);
         if (var2 == xe.v.Z) {
            return true;
         } else {
            return this.s.P().equals("Conservative")
               ? false
               : var2 == xe.v.t || var2 == xe.v.p || this.s.P().equals("Broad") && (var2 == xe.v.D || var2 == xe.v.Y && this.E.P());
         }
      }
   }

   @Override
   protected int minimumY() {
      return Math.min(this.t.P().intValue(), this.K.P().intValue());
   }

   @Override
   protected int maximumY() {
      return Math.min(60, Math.max(this.t.P().intValue(), this.K.P().intValue()));
   }

   @Override
   protected void beforeScan() {
      ChunkPos var1 = getMc().player.chunkPosition();
      this.M
         .keySet()
         .removeIf(
            var2 -> {
               ChunkPos var3 = ChunkPos.unpack(var2);
               return Math.abs((long)var3.x() - var1.x()) > this.y()
                  || Math.abs((long)var3.z() - var1.z()) > this.y()
                  || getMc().level.getChunkSource().getChunkNow(var3.x(), var3.z()) == null;
            }
         );
   }

   @Override
   protected void beginChunk(LevelChunk var1) {
      long var2 = this.M.computeIfAbsent(var1.getPos().pack(), var1x -> this.I());
      this.a = (boolean)(this.I() - var2 >= 200L ? 1 : 0);
   }

   @Override
   protected void beginScan() {
      this.w.O();
      this.a = (boolean)0;
      this.T = (boolean)(getMc() != null && getMc().level != null && getMc().level.dimension().equals(Level.END) ? 1 : 0);
   }

   @Override
   protected void count(BlockState var1, int var2) {
      this.count(var1, 0, var2, 0);
   }

   @Override
   protected void count(BlockState var1, int var2, int var3, int var4) {
      if (var3 >= this.minimumY() && var3 <= this.maximumY()) {
         if (!xe.y(var1) || this.J.P() && this.a) {
            this.w.j(var1, var2, var3, var4, this.T, this.s.P().equals("Broad") && this.E.P());
         }
      }
   }

   @Override
   protected com.larpclient.d6.f evidence() {
      ce var1 = this.w.O(this.s.P(), this.b.P().intValue(), this.C.P().intValue(), this.O.P().intValue());
      return new com.larpclient.d6.f(0, 0, 0, var1.sampleY(), var1);
   }

   @Override
   protected boolean qualifies(com.larpclient.d6.f var1) {
      return var1.base().qualifies(this.s.P(), this.b.P().intValue(), this.C.P().intValue(), this.O.P().intValue());
   }

   @Override
   protected String describe(com.larpclient.d6.f var1) {
      return var1.base().describe();
   }

   @Override
   protected int tint() {
      return -6629451;
   }

   @Override
   protected void clearEvidence() {
      this.w.O();
      this.M.clear();
      this.a = (boolean)0;
      if (this.d != null) {
         this.W("scan reset or lifecycle cleanup");
      }

      this.f();
   }

   public List<df<?>> d() {
      return List.of(this.W);
   }

   private void f() {
      this.o.u();
      this.d = null;
      this.q = 0L;
      this.h = (boolean)0;
   }

   private boolean g(ClientLevel var1) {
      if (this.W.P() && !this.h && this.isRuntimeActive() && this.isInGame() && getMc().level == var1 && getMc().isSameThread()) {
         if (this.d != var1) {
            if (this.d != null) {
               this.W("world changed");
            }

            this.f();
            this.d = var1;
            mt.K("BaseFinding", "Visibility diagnostic active: received block-entity chunk snapshots and server-verified updates only; no bypass assumed");
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean r(BlockPos var1) {
      if (var1.getY() >= this.minimumY() && var1.getY() <= this.maximumY()) {
         ChunkPos var2 = getMc().player.chunkPosition();
         return Math.abs((long)(var1.getX() >> 4) - var2.x()) <= this.y() && Math.abs((long)(var1.getZ() >> 4) - var2.z()) <= this.y();
      } else {
         return false;
      }
   }

   public static boolean i(BlockState var0, int var1, boolean var2) {
      xe.v var3 = xe.j(var0, var1, var2);
      return var3 != xe.v.f && var3 != xe.v.Y || var0.is(Blocks.SPAWNER);
   }

   public static boolean c(BlockState var0) {
      return var0.is(Blocks.DEEPSLATE) || var0.is(Blocks.STONE) || var0.is(Blocks.NETHERRACK) || var0.is(Blocks.END_STONE);
   }

   private void r(BlockPos var1, BlockState var2, com.larpclient.dn.b var3) {
      if (this.r(var1)) {
         if (!this.o.H(this.q)) {
            this.o.L(var3, 1L);
         } else {
            this.o
               .Q(
                  var1.asLong(),
                  ChunkPos.pack(var1.getX() >> 4, var1.getZ() >> 4),
                  var2.toString(),
                  i(var2, var1.getY(), this.d.dimension().equals(Level.END)),
                  c(var2),
                  var3,
                  this.q
               );
         }
      }
   }

   private void h(ClientLevel var1, LevelChunk var2) {
      try {
         if (!this.g(var1)) {
            return;
         }

         ChunkPos var3 = getMc().player.chunkPosition();
         ChunkPos var4 = var2.getPos();
         if (Math.abs((long)var4.x() - var3.x()) > this.y() || Math.abs((long)var4.z() - var3.z()) > this.y()) {
            return;
         }

         this.o.T();

         for (long var6 : this.o.m(var4.pack())) {
            BlockPos var8 = BlockPos.of(var6);
            this.r(var8, var2.getBlockState(var8), com.larpclient.dn.b.w);
         }

         int var10 = 0;

         for (BlockEntity var7 : var2.getBlockEntities().values()) {
            if (var10 == 256) {
               this.o.L(com.larpclient.dn.b.w, var2.getBlockEntities().size() - var10);
               break;
            }

            var10++;
            if (!var7.isRemoved()) {
               this.r(var7.getBlockPos(), var7.getBlockState(), com.larpclient.dn.b.w);
            }
         }
      } catch (RuntimeException var9) {
         this.r(var9);
      }
   }

   public static void J(ClientLevel var0, BlockPos var1, BlockState var2) {
      if (H != null && H.isEnabled()) {
         a0 var3 = H;
         if (var3 != null) {
            try {
               if (!var3.g(var0)) {
                  return;
               }

               if (var0.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4) == null) {
                  return;
               }

               var3.r(var1, var2, com.larpclient.dn.b.n);
            } catch (RuntimeException var5) {
               var3.r(var5);
            }
         }
      }
   }

   private void r(RuntimeException var1) {
      this.h = (boolean)1;
      mt.B("BaseFinding", "Visibility diagnostic failed; observations paused until reset, native updates and Base Finder remain active", var1);
   }

   private void W(String var1) {
      mt.K("BaseFinding", "Visibility diagnostic [{}]: {}", var1, this.o.I());

      for (com.larpclient.dn.s var3 : this.o.C(8)) {
         BlockPos var4 = BlockPos.of(var3.position());
         mt.K(
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
      int var10000 = this.g(getMc().level);
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         this.q++;
         long var12;
         var10000 = (var12 = this.q % 20L - 0L) == 0L ? 0 : (var12 < 0L ? -1 : 1);
         boolean var4 = false;
         int var3 = var10000;
         var10000 = Integer.signum(var3);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.o.V(this.q);
         }

         long var13;
         var10000 = (var13 = this.q % 200L - 0L) == 0L ? 0 : (var13 < 0L ? -1 : 1);
         boolean var6 = false;
         int var5 = var10000;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.W("10 seconds");
         }
      }
   }

   @Override
   public String getStatus() {
      return super.getStatus() + (!this.W.P() ? "" : (this.h ? " · diagnostic paused" : " · diagnostic: " + this.o.I().tracked() + " observations"));
   }
}
