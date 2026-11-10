package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.jg;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;

public final class LightDebugModule extends Module {
   private final b a = this.addSetting(new b("Max Y", "Highest layer to inspect, starting at Y -64", -50.0, -64.0, 16.0, 1.0));
   private final b J = this.addSetting(new b("Chunk Radius", "Loaded square radius around your current chunk", 8.0, 1.0, 12.0, 1.0));
   private final b I = this.addSetting(new b("Alpha", "Opacity of light-strength colored block volumes", 160.0, 0.0, 255.0, 1.0));
   private final b G = this.addSetting(
      new b("Render Limit", "Nearest chunk light runs drawn per frame; all scan data is retained", 16384.0, 1024.0, 65536.0, 1024.0)
   );
   private final Map<Long, List<jg.i>> i = new HashMap<>();
   private List<LightDebugModule.t> Z = List.of();
   private ClientLevel q;
   private ChunkPos p;
   private int F;
   private int M;
   private int V;
   private List<ChunkPos> v = List.of();
   private byte[] d;
   private int E;
   private int T;
   private boolean Y;
   private final MutableBlockPos D = new MutableBlockPos();
   private static final int f = 32768;

   public LightDebugModule() {
      super("Light Debug", "Show underground block light brighter than the sky", Category.RENDER, -1);
      this.a.w(var1 -> this.F());
      this.J.w(var1 -> this.F());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.W());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.q) {
            this.i.remove(var2.getPos().pack());
            this.k();
         }
      });
   }

   public List<r<?>> e() {
      return List.of(this.a, this.J);
   }

   public List<r<?>> W() {
      return List.of(this.I, this.G);
   }

   private void F() {
      this.i.clear();
      this.Z = List.of();
      this.v = List.of();
      this.d = null;
      this.p = null;
      this.M = this.V = this.F = 0;
      this.Y = (boolean)1;
   }

   @Override
   public void onEnable() {
      this.F();
      this.q = getMc().level;
      _q.z("BaseFinding", "Light Debug active: block light above sky, Y -64..{}, radius {}", this.a.getValue(), this.J.getValue());
   }

   @Override
   public void onDisable() {
      _q.c("BaseFinding", "Light Debug cleanup: {} lit chunks", this.i.size());
      this.F();
      this.q = null;
   }

   private void G(ChunkPos var1) {
      this.p = var1;
      this.E = Math.max(-64, this.q.getMinY());
      this.T = Math.min(this.a.getValue().intValue(), this.q.getMaxY());
      int var2 = this.J.getValue().intValue();
      ArrayList var3 = new ArrayList();
      this.i
         .keySet()
         .removeIf(
            var2x -> {
               ChunkPos var3x = ChunkPos.unpack(var2x);
               return Math.abs((long)var3x.x() - this.p.x()) > var2
                  || Math.abs((long)var3x.z() - this.p.z()) > var2
                  || this.q.getChunkSource().getChunkNow(var3x.x(), var3x.z()) == null;
            }
         );

      for (int var4 = -var2; var4 <= var2; var4++) {
         for (int var5 = -var2; var5 <= var2; var5++) {
            ChunkPos var6 = new ChunkPos(var1.x() + var4, var1.z() + var5);
            if (this.q.getChunkSource().getChunkNow(var6.x(), var6.z()) != null) {
               var3.add(var6);
            }
         }
      }

      var3.sort(
         Comparator.comparingLong(var1x -> (long)(var1x.x() - var1.x()) * (var1x.x() - var1.x()) + (long)(var1x.z() - var1.z()) * (var1x.z() - var1.z()))
      );
      this.v = List.copyOf(var3);
      this.M = this.V = 0;
      this.d = null;
      this.Y = (boolean)0;
      this.k();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.q != mc.level) {
            this.F();
            this.q = mc.level;
         }

         ChunkPos var1 = mc.player.chunkPosition();
         int var10000 = ++this.F % 16;
         boolean var12 = false;
         int var11 = var10000;
         var10000 = Integer.signum(var11);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.Y = (boolean)1;
         }

         int var17 = var1.equals(this.p);
         boolean var14 = false;
         byte var13 = (byte)var17;
         var17 = Integer.signum(var13);
         if ((var17 | -var17) >>> 31 == 0) {
            this.v = List.of();
            this.d = null;
            this.G(var1);
         } else {
            var17 = this.M;
            int var8 = this.v.size();
            int var7 = var17;
            if (Integer.compare(var7, var8) >>> 31 == 0) {
               boolean var10 = false;
               byte var9 = this.Y;
               var17 = Integer.signum(var9);
               if (((var17 | -var17) >>> 31 ^ 1) == 0) {
                  this.G(var1);
               }
            }
         }

         byte var2 = 0;
         int var3 = 32768;

         while (var3 > 0 && this.M < this.v.size()) {
            ChunkPos var4 = this.v.get(this.M);
            if (this.T >= this.E && this.q.getChunkSource().getChunkNow(var4.x(), var4.z()) != null) {
               if (this.d == null) {
                  this.d = new byte[(this.T - this.E + 1) * 256];
               }

               int var5 = Math.min(this.d.length, this.V + var3);
               var3 -= var5 - this.V;

               while (this.V < var5) {
                  this.D.set(var4.getMinBlockX() + (this.V & 15), this.E + (this.V >> 8), var4.getMinBlockZ() + (this.V >> 4 & 15));
                  int var6 = this.q.getBrightness(LightLayer.BLOCK, this.D);
                  if (var6 > 0 && jg.Z(var6, this.q.getBrightness(LightLayer.SKY, this.D))) {
                     this.d[this.V] = (byte)var6;
                  }

                  this.V++;
               }

               if (this.V == this.d.length) {
                  List var15 = jg.c(this.d, var4.x(), var4.z(), this.E);
                  if (var15.isEmpty()) {
                     this.i.remove(var4.pack());
                  } else {
                     this.i.put(var4.pack(), var15);
                  }

                  this.M++;
                  this.V = 0;
                  this.d = null;
                  var2 = 1;
               }
            } else {
               this.i.remove(var4.pack());
               this.M++;
               this.V = 0;
               this.d = null;
               var2 = 1;
            }
         }

         if (var2 != 0) {
            this.k();
         }
      }
   }

   private void k() {
      if (this.p == null) {
         this.Z = List.of();
      } else {
         this.Z = this.i.entrySet().stream().sorted(Comparator.<Entry<Long, List<jg.i>>>comparingDouble(var1 -> {
            ChunkPos var2 = ChunkPos.unpack(var1.getKey());
            double var3 = (double)var2.x() - this.p.x();
            double var5 = (double)var2.z() - this.p.z();
            return var3 * var3 + var5 * var5;
         }).thenComparingLong(Entry::getKey)).map(var0 -> new LightDebugModule.t(var0.getKey(), var0.getValue())).toList();
      }
   }

   private void W() {
      if (this.isRuntimeActive() && this.isInGame() && this.q == mc.level && !(this.I.getValue() <= 0.0)) {
         try {
            int var1 = 0;
            int var2 = this.G.getValue().intValue();

            for (LightDebugModule.t var4 : this.Z) {
               ChunkPos var5 = ChunkPos.unpack(var4.key());
               if (this.q.getChunkSource().getChunkNow(var5.x(), var5.z()) != null) {
                  for (jg.i var7 : var4.runs()) {
                     if (var1++ >= var2) {
                        return;
                     }

                     int var8 = jg.H(var7.light(), this.I.getValue().intValue());
                     Gizmos.cuboid(new AABB(var7.x(), var7.y(), var7.z(), var7.x() + var7.length(), var7.y() + 1, var7.z() + 1), GizmoStyle.fill(var8))
                        .setAlwaysOnTop();
                  }
               }
            }
         } catch (RuntimeException var9) {
            _q.g("BaseFinding", "Light Debug render failed; scan continues", var9);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.Z.size() + " lit chunks";
   }

   private record t(long key, List<jg.i> runs) {
   }
}
