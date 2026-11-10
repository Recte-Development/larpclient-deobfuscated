package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.dy;
import com.larpclient.j5;
import com.larpclient.nq;
import com.larpclient.qq;
import com.larpclient.s3;
import com.larpclient.vt;
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

public final class kq extends ke {
   private final o e = this.addSetting(
      new o(
         "Detection",
         "Conservative: dedicated blocks. Balanced: storage plus utilities. Broad: more possible structures",
         "Balanced",
         "Conservative",
         "Balanced",
         "Broad"
      )
   );
   private final b O = this.addSetting(
      new b("Min Storage", "Ordinary storage required in one 16-block height band; dedicated clues bypass this", 4.0, 1.0, 64.0, 1.0)
   );
   private final b H = this.addSetting(
      new b("Min Utilities", "Crafting/workstation blocks supporting ordinary storage in Balanced detection", 2.0, 1.0, 32.0, 1.0)
   );
   private final b o = this.addSetting(new b("Min Ender Chests", "Ender chests needed in one 16-block height band outside the End", 1.0, 1.0, 64.0, 1.0));
   private final b i = this.addSetting(new b("Min Scan Y", "Lowest received block height to inspect", -64.0, -64.0, 60.0, 1.0));
   private final b m = this.addSetting(new b("Max Scan Y", "Highest block height to inspect, capped at Y60", 60.0, -64.0, 60.0, 1.0));
   private final m I = this.addSetting(new m("Terrain Clues", "Include deepslate placement evidence in Broad detection; never proof on its own", true));
   private final m M = this.addSetting(new m("Base Boxes", "Outline the detected building evidence as well as its chunk", true));
   private final o k = this.addSetting(new o("Base Style", "Box style for the detected building evidence", "Box", "Box", "Corners", "Filled"));
   private final nq.k B = new nq.k();
   private final m Y = this.addSetting(
      new m("Visibility Diagnostic", "Experimental received-data observations in latest.log; does not recover hidden blocks", false)
   );
   private final vt S = new vt();
   private ClientLevel n;
   private long T;
   private boolean t;
   private static kq v;
   private boolean s;
   private final m g = this.addSetting(new m("Honey Clues", "Full hives or nests after 10 seconds loaded; natural bee activity can qualify", true));
   private final Map<Long, Long> b = new HashMap<>();
   private boolean q;
   private int w;
   private int P;
   private boolean a;
   private boolean x;

   public kq() {
      super("Base Finder", "Find base clues in loaded blocks");

      for (com.larpclient.module.r var2 : this.detectionSettings()) {
         var2.w(var1 -> this.T("detection changed"));
      }

      this.O.a(() -> !this.e.getValue().equals("Conservative"));
      this.H.a(() -> this.e.getValue().equals("Balanced"));
      this.I.a(() -> this.e.getValue().equals("Broad"));
      this.k.a(this.M::getValue);
      this.j();
      v = this;
      this.Y.w(var1 -> {
         if (this.n != null) {
            this.N("setting changed");
         }

         this.Q();
      });
      ClientChunkEvents.CHUNK_LOAD.register((Load)(var1, var2x) -> this.J(var1, var2x));
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2x) -> {
         if (var1 == this.n) {
            this.S.O(var2x.getPos().pack());
         }

         if (getMc() != null && var1 == getMc().level) {
            this.b.remove(var2x.getPos().pack());
         }
      });
   }

   @Override
   public List<com.larpclient.module.r<?>> detectionSettings() {
      return List.of(this.e, this.O, this.H, this.o, this.i, this.m, this.I, this.g);
   }

   @Override
   public List<com.larpclient.module.r<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.add(this.M);
      var1.add(this.k);
      return List.copyOf(var1);
   }

   public static AABB a(ChunkPos var0, dy.h var1) {
      AABB var2 = var1.base().bounds();
      return var2 == null ? null : var2.move(var0.getMinBlockX(), 0.0, var0.getMinBlockZ()).inflate(0.08);
   }

   @Override
   protected void drawEvidence(ChunkPos var1, dy.h var2, j5 var3, int var4) {
      if (this.M.getValue()) {
         AABB var5 = a(var1, var2);
         if (var5 != null) {
            var3.L(var5, this.k.getValue(), var4);
         }
      }
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return this.U(var1, 16);
   }

   private boolean U(BlockState var1, int var2) {
      if (nq.C(var1)) {
         return this.g.getValue();
      } else {
         nq.m var3 = nq.S(var1, var2, this.s);
         if (var3 == nq.m.S) {
            return true;
         } else {
            return this.e.getValue().equals("Conservative")
               ? false
               : var3 == nq.m.e || var3 == nq.m.E || this.e.getValue().equals("Broad") && (var3 == nq.m.P || var3 == nq.m.Y && this.I.getValue());
         }
      }
   }

   @Override
   protected boolean sectionCandidate(LevelChunk var1, int var2) {
      LevelChunkSection var3 = var1.getSections()[var2];
      int var4 = var1.getMinY() + var2 * 16 + 15;
      return !var3.hasOnlyAir() && var3.maybeHas(var2x -> this.U(var2x, var4));
   }

   @Override
   protected int minimumY() {
      return Math.min(this.i.getValue().intValue(), this.m.getValue().intValue());
   }

   @Override
   protected int maximumY() {
      return Math.min(60, Math.max(this.i.getValue().intValue(), this.m.getValue().intValue()));
   }

   @Override
   protected void beforeScan() {
      ChunkPos var1 = getMc().player.chunkPosition();
      this.b
         .keySet()
         .removeIf(
            var2 -> {
               ChunkPos var3 = ChunkPos.unpack(var2);
               return Math.abs((long)var3.x() - var1.x()) > this.z()
                  || Math.abs((long)var3.z() - var1.z()) > this.z()
                  || getMc().level.getChunkSource().getChunkNow(var3.x(), var3.z()) == null;
            }
         );
   }

   @Override
   protected void beginChunk(LevelChunk var1) {
      long var2 = this.b.computeIfAbsent(var1.getPos().pack(), var1x -> this.t());
      this.q = (boolean)(this.t() - var2 >= 200L ? 1 : 0);
   }

   @Override
   protected void beginScan() {
      this.B.d();
      this.q = (boolean)0;
      this.s = (boolean)(getMc() != null && getMc().level != null && getMc().level.dimension().equals(Level.END) ? 1 : 0);
      this.j();
   }

   private void j() {
      this.w = this.minimumY();
      this.P = this.maximumY();
      this.a = (boolean)(this.e.getValue().equals("Broad") && this.I.getValue() ? 1 : 0);
      this.x = this.g.getValue();
   }

   @Override
   protected void count(BlockState var1, int var2) {
      this.count(var1, 0, var2, 0);
   }

   @Override
   protected void count(BlockState var1, int var2, int var3, int var4) {
      if (var3 >= this.w && var3 <= this.P) {
         nq.m var5 = this.B.v(var1, var3, this.s);
         if (var5 != nq.m.x) {
            if (var5 != nq.m.S || !nq.C(var1) || this.x && this.q) {
               this.B.X(var5, var1, var2, var3, var4, this.s, this.a);
            }
         }
      }
   }

   @Override
   protected dy.h evidence() {
      qq var1 = this.B.Z(this.e.getValue(), this.O.getValue().intValue(), this.H.getValue().intValue(), this.o.getValue().intValue());
      return new dy.h(0, 0, 0, var1.sampleY(), var1);
   }

   @Override
   protected boolean qualifies(dy.h var1) {
      return var1.base().qualifies(this.e.getValue(), this.O.getValue().intValue(), this.H.getValue().intValue(), this.o.getValue().intValue());
   }

   @Override
   protected String describe(dy.h var1) {
      return var1.base().describe();
   }

   @Override
   protected s3 alert(ChunkPos var1, dy.h var2) {
      return s3.base(var1.getMinBlockX(), var1.getMinBlockZ(), var2.base());
   }

   @Override
   protected int tint() {
      return -6629451;
   }

   @Override
   protected void clearEvidence() {
      this.B.d();
      this.b.clear();
      this.q = (boolean)0;
      this.j();
      if (this.n != null) {
         this.N("scan reset or lifecycle cleanup");
      }

      this.Q();
   }

   public List<com.larpclient.module.r<?>> x() {
      return List.of(this.Y);
   }

   private void Q() {
      this.S.H();
      this.n = null;
      this.T = 0L;
      this.t = (boolean)0;
   }

   private boolean x(ClientLevel var1) {
      if (this.Y.getValue() && !this.t && this.isRuntimeActive() && this.isInGame() && getMc().level == var1 && getMc().isSameThread()) {
         if (this.n != var1) {
            if (this.n != null) {
               this.N("world changed");
            }

            this.Q();
            this.n = var1;
            _q.z("BaseFinding", "Visibility diagnostic active: received block-entity chunk snapshots and server-verified updates only; no bypass assumed");
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean u(BlockPos var1) {
      if (var1.getY() >= this.minimumY() && var1.getY() <= this.maximumY()) {
         ChunkPos var2 = getMc().player.chunkPosition();
         return Math.abs((long)(var1.getX() >> 4) - var2.x()) <= this.z() && Math.abs((long)(var1.getZ() >> 4) - var2.z()) <= this.z();
      } else {
         return false;
      }
   }

   public static boolean L(BlockState var0, int var1, boolean var2) {
      nq.m var3 = nq.S(var0, var1, var2);
      return var3 != nq.m.x && var3 != nq.m.Y || var0.is(Blocks.SPAWNER);
   }

   public static boolean n(BlockState var0) {
      return var0.is(Blocks.DEEPSLATE) || var0.is(Blocks.STONE) || var0.is(Blocks.NETHERRACK) || var0.is(Blocks.END_STONE);
   }

   private void b(BlockPos var1, BlockState var2, vt.d var3) {
      if (this.u(var1)) {
         if (!this.S.n(this.T)) {
            this.S.c(var3, 1L);
         } else {
            this.S
               .G(
                  var1.asLong(),
                  ChunkPos.pack(var1.getX() >> 4, var1.getZ() >> 4),
                  var2.toString(),
                  L(var2, var1.getY(), this.n.dimension().equals(Level.END)),
                  n(var2),
                  var3,
                  this.T
               );
         }
      }
   }

   private void J(ClientLevel var1, LevelChunk var2) {
      try {
         if (!this.x(var1)) {
            return;
         }

         ChunkPos var3 = getMc().player.chunkPosition();
         ChunkPos var4 = var2.getPos();
         if (Math.abs((long)var4.x() - var3.x()) > this.z() || Math.abs((long)var4.z() - var3.z()) > this.z()) {
            return;
         }

         this.S.z();

         for (long var6 : this.S.w(var4.pack())) {
            BlockPos var8 = BlockPos.of(var6);
            this.b(var8, var2.getBlockState(var8), vt.d.R);
         }

         int var10 = 0;

         for (BlockEntity var7 : var2.getBlockEntities().values()) {
            if (var10 == 256) {
               this.S.c(vt.d.R, var2.getBlockEntities().size() - var10);
               break;
            }

            var10++;
            if (!var7.isRemoved()) {
               this.b(var7.getBlockPos(), var7.getBlockState(), vt.d.R);
            }
         }
      } catch (RuntimeException var9) {
         this.Z(var9);
      }
   }

   public static void S(ClientLevel var0, BlockPos var1, BlockState var2) {
      if (v != null && v.isEnabled()) {
         kq var3 = v;
         if (var3 != null) {
            try {
               if (!var3.x(var0)) {
                  return;
               }

               if (var0.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4) == null) {
                  return;
               }

               var3.b(var1, var2, vt.d.l);
            } catch (RuntimeException var5) {
               var3.Z(var5);
            }
         }
      }
   }

   private void Z(RuntimeException var1) {
      this.t = (boolean)1;
      _q.g("BaseFinding", "Visibility diagnostic failed; observations paused until reset, native updates and Base Finder remain active", var1);
   }

   private void N(String var1) {
      _q.z("BaseFinding", "Visibility diagnostic [{}]: {}", var1, this.S.B());

      for (vt.x var3 : this.S.x(8)) {
         BlockPos var4 = BlockPos.of(var3.position());
         _q.z(
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
      int var10000 = this.x(getMc().level);
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         this.T++;
         long var12;
         var10000 = (var12 = this.T % 20L - 0L) == 0L ? 0 : (var12 < 0L ? -1 : 1);
         boolean var4 = false;
         int var3 = var10000;
         var10000 = Integer.signum(var3);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.S.W(this.T);
         }

         long var13;
         var10000 = (var13 = this.T % 200L - 0L) == 0L ? 0 : (var13 < 0L ? -1 : 1);
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
      return super.getStatus() + (!this.Y.getValue() ? "" : (this.t ? " · diagnostic paused" : " · diagnostic: " + this.S.B().tracked() + " observations"));
   }
}
