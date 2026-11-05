package com.larpclient.module;

import com.larpclient._x;
import com.larpclient.j4;
import com.larpclient.mi;
import com.larpclient.oa;
import com.larpclient.r4;
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

public final class to extends tf {
   private final lp E = this.addSetting(
      new lp(
         "Detection",
         "Conservative: dedicated blocks. Balanced: storage plus utilities. Broad: more possible structures",
         "Balanced",
         "Conservative",
         "Balanced",
         "Broad"
      )
   );
   private final lc q = this.addSetting(
      new lc("Min Storage", "Ordinary storage required in one 16-block height band; dedicated clues bypass this", 4.0, 1.0, 64.0, 1.0)
   );
   private final lc t = this.addSetting(
      new lc("Min Utilities", "Crafting/workstation blocks supporting ordinary storage in Balanced detection", 2.0, 1.0, 32.0, 1.0)
   );
   private final lc v = this.addSetting(new lc("Min Ender Chests", "Ender chests needed in one 16-block height band outside the End", 1.0, 1.0, 64.0, 1.0));
   private final lc d = this.addSetting(new lc("Min Scan Y", "Lowest received block height to inspect", -64.0, -64.0, 60.0, 1.0));
   private final lc p = this.addSetting(new lc("Max Scan Y", "Highest block height to inspect, capped at Y60", 60.0, -64.0, 60.0, 1.0));
   private final ln y = this.addSetting(new ln("Terrain Clues", "Include deepslate placement evidence in Broad detection; never proof on its own", true));
   private final ln h = this.addSetting(new ln("Base Boxes", "Outline the detected building evidence as well as its chunk", true));
   private final lp e = this.addSetting(new lp("Base Style", "Box style for the detected building evidence", "Box", "Box", "Corners", "Filled"));
   private final j4.v T = new j4.v();
   private final ln X = this.addSetting(
      new ln("Visibility Diagnostic", "Experimental received-data observations in latest.log; does not recover hidden blocks", false)
   );
   private final _x f = new _x();
   private ClientLevel M;
   private long A;
   private boolean s;
   private static to L;
   private boolean J;
   private final ln V = this.addSetting(new ln("Honey Clues", "Full hives or nests after 10 seconds loaded; natural bee activity can qualify", true));
   private final Map<Long, Long> c = new HashMap<>();
   private boolean r;

   public to() {
      super("Base Finder", "Find base clues in loaded blocks");

      for (l6 var2 : this.detectionSettings()) {
         var2.W(var1 -> this.C("detection changed"));
      }

      this.q.x(() -> !this.E.getValue().equals("Conservative"));
      this.t.x(() -> this.E.getValue().equals("Balanced"));
      this.y.x(() -> this.E.getValue().equals("Broad"));
      this.e.x(this.h::getValue);
      L = this;
      this.X.W(var1 -> {
         if (this.M != null) {
            this.N("setting changed");
         }

         this.j();
      });
      ClientChunkEvents.CHUNK_LOAD.register((Load)(var1, var2x) -> this.B(var1, var2x));
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2x) -> {
         if (var1 == this.M) {
            this.f.k(var2x.getPos().pack());
         }

         if (getMc() != null && var1 == getMc().level) {
            this.c.remove(var2x.getPos().pack());
         }
      });
   }

   @Override
   public List<l6<?>> detectionSettings() {
      return List.of(this.E, this.q, this.t, this.v, this.d, this.p, this.y, this.V);
   }

   @Override
   public List<l6<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.add(this.h);
      var1.add(this.e);
      return List.copyOf(var1);
   }

   public static AABB K(ChunkPos var0, oa.j var1) {
      AABB var2 = var1.base().bounds();
      return var2 == null ? null : var2.move(var0.getMinBlockX(), 0.0, var0.getMinBlockZ()).inflate(0.08);
   }

   @Override
   protected void drawEvidence(ChunkPos var1, oa.j var2, mi var3, int var4) {
      if (this.h.getValue()) {
         AABB var5 = K(var1, var2);
         if (var5 != null) {
            var3.I(var5, this.e.getValue(), var4);
         }
      }
   }

   @Override
   protected boolean candidate(BlockState var1) {
      if (j4.t(var1)) {
         return this.V.getValue();
      } else {
         j4.t var2 = j4.F(var1, 16, this.J);
         if (var2 == j4.t.i) {
            return true;
         } else {
            return this.E.getValue().equals("Conservative")
               ? false
               : var2 == j4.t.A || var2 == j4.t.M || this.E.getValue().equals("Broad") && (var2 == j4.t.X || var2 == j4.t.c && this.y.getValue());
         }
      }
   }

   @Override
   protected int minimumY() {
      return Math.min(this.d.getValue().intValue(), this.p.getValue().intValue());
   }

   @Override
   protected int maximumY() {
      return Math.min(60, Math.max(this.d.getValue().intValue(), this.p.getValue().intValue()));
   }

   @Override
   protected void beforeScan() {
      ChunkPos var1 = getMc().player.chunkPosition();
      this.c
         .keySet()
         .removeIf(
            var2 -> {
               ChunkPos var3 = ChunkPos.unpack(var2);
               return Math.abs((long)var3.x() - var1.x()) > this.l()
                  || Math.abs((long)var3.z() - var1.z()) > this.l()
                  || getMc().level.getChunkSource().getChunkNow(var3.x(), var3.z()) == null;
            }
         );
   }

   @Override
   protected void beginChunk(LevelChunk var1) {
      long var2 = this.c.computeIfAbsent(var1.getPos().pack(), var1x -> this.U());
      this.r = (boolean)(this.U() - var2 >= 200L ? 1 : 0);
   }

   @Override
   protected void beginScan() {
      this.T.J();
      this.r = (boolean)0;
      this.J = (boolean)(getMc() != null && getMc().level != null && getMc().level.dimension().equals(Level.END) ? 1 : 0);
   }

   @Override
   protected void count(BlockState var1, int var2) {
      this.count(var1, 0, var2, 0);
   }

   @Override
   protected void count(BlockState var1, int var2, int var3, int var4) {
      if (var3 >= this.minimumY() && var3 <= this.maximumY()) {
         if (!j4.t(var1) || this.V.getValue() && this.r) {
            this.T.A(var1, var2, var3, var4, this.J, this.E.getValue().equals("Broad") && this.y.getValue());
         }
      }
   }

   @Override
   protected oa.j evidence() {
      com.larpclient.l4 var1 = this.T.u(this.E.getValue(), this.q.getValue().intValue(), this.t.getValue().intValue(), this.v.getValue().intValue());
      return new oa.j(0, 0, 0, var1.sampleY(), var1);
   }

   @Override
   protected boolean qualifies(oa.j var1) {
      return var1.base().qualifies(this.E.getValue(), this.q.getValue().intValue(), this.t.getValue().intValue(), this.v.getValue().intValue());
   }

   @Override
   protected String describe(oa.j var1) {
      return var1.base().describe();
   }

   @Override
   protected int tint() {
      return -6629451;
   }

   @Override
   protected void clearEvidence() {
      this.T.J();
      this.c.clear();
      this.r = (boolean)0;
      if (this.M != null) {
         this.N("scan reset or lifecycle cleanup");
      }

      this.j();
   }

   public List<l6<?>> L() {
      return List.of(this.X);
   }

   private void j() {
      this.f.V();
      this.M = null;
      this.A = 0L;
      this.s = (boolean)0;
   }

   private boolean Y(ClientLevel var1) {
      if (this.X.getValue() && !this.s && this.isRuntimeActive() && this.isInGame() && getMc().level == var1 && getMc().isSameThread()) {
         if (this.M != var1) {
            if (this.M != null) {
               this.N("world changed");
            }

            this.j();
            this.M = var1;
            r4.V("BaseFinding", "Visibility diagnostic active: received block-entity chunk snapshots and server-verified updates only; no bypass assumed");
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean k(BlockPos var1) {
      if (var1.getY() >= this.minimumY() && var1.getY() <= this.maximumY()) {
         ChunkPos var2 = getMc().player.chunkPosition();
         return Math.abs((long)(var1.getX() >> 4) - var2.x()) <= this.l() && Math.abs((long)(var1.getZ() >> 4) - var2.z()) <= this.l();
      } else {
         return false;
      }
   }

   public static boolean L(BlockState var0, int var1, boolean var2) {
      j4.t var3 = j4.F(var0, var1, var2);
      return var3 != j4.t.p && var3 != j4.t.c || var0.is(Blocks.SPAWNER);
   }

   public static boolean v(BlockState var0) {
      return var0.is(Blocks.DEEPSLATE) || var0.is(Blocks.STONE) || var0.is(Blocks.NETHERRACK) || var0.is(Blocks.END_STONE);
   }

   private void L(BlockPos var1, BlockState var2, _x.i var3) {
      if (this.k(var1)) {
         if (!this.f.K(this.A)) {
            this.f.l(var3, 1L);
         } else {
            this.f
               .p(
                  var1.asLong(),
                  ChunkPos.pack(var1.getX() >> 4, var1.getZ() >> 4),
                  var2.toString(),
                  L(var2, var1.getY(), this.M.dimension().equals(Level.END)),
                  v(var2),
                  var3,
                  this.A
               );
         }
      }
   }

   private void B(ClientLevel var1, LevelChunk var2) {
      try {
         if (!this.Y(var1)) {
            return;
         }

         ChunkPos var3 = getMc().player.chunkPosition();
         ChunkPos var4 = var2.getPos();
         if (Math.abs((long)var4.x() - var3.x()) > this.l() || Math.abs((long)var4.z() - var3.z()) > this.l()) {
            return;
         }

         this.f.A();

         for (long var6 : this.f.j(var4.pack())) {
            BlockPos var8 = BlockPos.of(var6);
            this.L(var8, var2.getBlockState(var8), _x.i.M);
         }

         int var10 = 0;

         for (BlockEntity var7 : var2.getBlockEntities().values()) {
            if (var10 == 256) {
               this.f.l(_x.i.M, var2.getBlockEntities().size() - var10);
               break;
            }

            var10++;
            if (!var7.isRemoved()) {
               this.L(var7.getBlockPos(), var7.getBlockState(), _x.i.M);
            }
         }
      } catch (RuntimeException var9) {
         this.r(var9);
      }
   }

   public static void y(ClientLevel var0, BlockPos var1, BlockState var2) {
      if (L != null && L.isEnabled()) {
         to var3 = L;
         if (var3 != null) {
            try {
               if (!var3.Y(var0)) {
                  return;
               }

               if (var0.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4) == null) {
                  return;
               }

               var3.L(var1, var2, _x.i.q);
            } catch (RuntimeException var5) {
               var3.r(var5);
            }
         }
      }
   }

   private void r(RuntimeException var1) {
      this.s = (boolean)1;
      r4.T("BaseFinding", "Visibility diagnostic failed; observations paused until reset, native updates and Base Finder remain active", var1);
   }

   private void N(String var1) {
      r4.V("BaseFinding", "Visibility diagnostic [{}]: {}", var1, this.f.J());

      for (_x.t var3 : this.f.M(8)) {
         BlockPos var4 = BlockPos.of(var3.position());
         r4.V(
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
      int var10000 = this.Y(getMc().level);
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         this.A++;
         long var12;
         var10000 = (var12 = this.A % 20L - 0L) == 0L ? 0 : (var12 < 0L ? -1 : 1);
         boolean var4 = false;
         int var3 = var10000;
         var10000 = Integer.signum(var3);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.f.O(this.A);
         }

         long var13;
         var10000 = (var13 = this.A % 200L - 0L) == 0L ? 0 : (var13 < 0L ? -1 : 1);
         boolean var6 = false;
         int var5 = var10000;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.N("10 seconds");
         }
      }
   }

   @Override
   public String getStatus() {
      return super.getStatus() + (!this.X.getValue() ? "" : (this.s ? " · diagnostic paused" : " · diagnostic: " + this.f.J().tracked() + " observations"));
   }
}
