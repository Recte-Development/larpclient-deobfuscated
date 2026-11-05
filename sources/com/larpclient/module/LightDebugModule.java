package com.larpclient.module;

import com.larpclient.mt;
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
   private final d9 I = this.addSetting(new d9("Max Y", "Highest layer to inspect, starting at Y -64", -50.0, -64.0, 16.0, 1.0));
   private final d9 K = this.addSetting(new d9("Chunk Radius", "Loaded square radius around your current chunk", 8.0, 1.0, 12.0, 1.0));
   private final d9 Q = this.addSetting(new d9("Alpha", "Opacity of light-strength colored block volumes", 160.0, 0.0, 255.0, 1.0));
   private final d9 J = this.addSetting(
      new d9("Render Limit", "Nearest chunk light runs drawn per frame; all scan data is retained", 16384.0, 1024.0, 65536.0, 1024.0)
   );
   private final Map<Long, List<com.larpclient.d.h>> S = new HashMap<>();
   private List<LightDebugModule.p> a = List.of();
   private ClientLevel e;
   private ChunkPos v;
   private int s;
   private int p;
   private int O;
   private List<ChunkPos> G = List.of();
   private byte[] U;
   private int x;
   private int o;
   private boolean u;
   private final MutableBlockPos X = new MutableBlockPos();
   private static final int c = 32768;

   public LightDebugModule() {
      super("Light Debug", "Show underground block light brighter than the sky", Category.RENDER, -1);
      this.I.h(var1 -> this.k());
      this.K.h(var1 -> this.k());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.V());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.e) {
            this.S.remove(var2.getPos().pack());
            this.q();
         }
      });
   }

   public List<df<?>> v() {
      return List.of(this.I, this.K);
   }

   public List<df<?>> J() {
      return List.of(this.Q, this.J);
   }

   private void k() {
      this.S.clear();
      this.a = List.of();
      this.G = List.of();
      this.U = null;
      this.v = null;
      this.p = this.O = this.s = 0;
      this.u = (boolean)1;
   }

   @Override
   public void onEnable() {
      this.k();
      this.e = getMc().level;
      mt.K("BaseFinding", "Light Debug active: block light above sky, Y -64..{}, radius {}", this.I.P(), this.K.P());
   }

   @Override
   public void onDisable() {
      mt.U("BaseFinding", "Light Debug cleanup: {} lit chunks", this.S.size());
      this.k();
      this.e = null;
   }

   private void O(ChunkPos var1) {
      this.v = var1;
      this.x = Math.max(-64, this.e.getMinY());
      this.o = Math.min(this.I.P().intValue(), this.e.getMaxY());
      int var2 = this.K.P().intValue();
      ArrayList var3 = new ArrayList();
      this.S
         .keySet()
         .removeIf(
            var2x -> {
               ChunkPos var3x = ChunkPos.unpack(var2x);
               return Math.abs((long)var3x.x() - this.v.x()) > var2
                  || Math.abs((long)var3x.z() - this.v.z()) > var2
                  || this.e.getChunkSource().getChunkNow(var3x.x(), var3x.z()) == null;
            }
         );

      for (int var4 = -var2; var4 <= var2; var4++) {
         for (int var5 = -var2; var5 <= var2; var5++) {
            ChunkPos var6 = new ChunkPos(var1.x() + var4, var1.z() + var5);
            if (this.e.getChunkSource().getChunkNow(var6.x(), var6.z()) != null) {
               var3.add(var6);
            }
         }
      }

      var3.sort(
         Comparator.comparingLong(var1x -> (long)(var1x.x() - var1.x()) * (var1x.x() - var1.x()) + (long)(var1x.z() - var1.z()) * (var1x.z() - var1.z()))
      );
      this.G = List.copyOf(var3);
      this.p = this.O = 0;
      this.U = null;
      this.u = (boolean)0;
      this.q();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.e != mc.level) {
            this.k();
            this.e = mc.level;
         }

         ChunkPos var1 = mc.player.chunkPosition();
         if (++this.s % 16 == 0) {
            this.u = (boolean)1;
         }

         int var10000 = var1.equals(this.v);
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.G = List.of();
            this.U = null;
            this.O(var1);
         } else {
            var10000 = this.p;
            int var10 = this.G.size();
            int var9 = var10000;
            if (Integer.compare(var9, var10) >>> 31 == 0) {
               boolean var12 = false;
               byte var11 = this.u;
               var10000 = Integer.signum(var11);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                  this.O(var1);
               }
            }
         }

         byte var2 = 0;
         int var3 = 32768;

         while (var3 > 0 && this.p < this.G.size()) {
            ChunkPos var4 = this.G.get(this.p);
            if (this.o >= this.x && this.e.getChunkSource().getChunkNow(var4.x(), var4.z()) != null) {
               if (this.U == null) {
                  this.U = new byte[(this.o - this.x + 1) * 256];
               }

               int var5 = Math.min(this.U.length, this.O + var3);
               var3 -= var5 - this.O;

               while (this.O < var5) {
                  this.X.set(var4.getMinBlockX() + (this.O & 15), this.x + (this.O >> 8), var4.getMinBlockZ() + (this.O >> 4 & 15));
                  int var6 = this.e.getBrightness(LightLayer.BLOCK, this.X);
                  if (var6 > 0 && com.larpclient.d.v(var6, this.e.getBrightness(LightLayer.SKY, this.X))) {
                     this.U[this.O] = (byte)var6;
                  }

                  this.O++;
               }

               if (this.O == this.U.length) {
                  List var15 = com.larpclient.d.V(this.U, var4.x(), var4.z(), this.x);
                  if (var15.isEmpty()) {
                     this.S.remove(var4.pack());
                  } else {
                     this.S.put(var4.pack(), var15);
                  }

                  this.p++;
                  this.O = 0;
                  this.U = null;
                  var2 = 1;
               }
            } else {
               this.S.remove(var4.pack());
               this.p++;
               this.O = 0;
               this.U = null;
               var2 = 1;
            }
         }

         boolean var14 = false;
         byte var13 = var2;
         var10000 = Integer.signum(var13);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.q();
         }
      }
   }

   private void q() {
      if (this.v == null) {
         this.a = List.of();
      } else {
         this.a = this.S.entrySet().stream().sorted(Comparator.<Entry<Long, List<com.larpclient.d.h>>>comparingDouble(var1 -> {
            ChunkPos var2 = ChunkPos.unpack(var1.getKey());
            double var3 = (double)var2.x() - this.v.x();
            double var5 = (double)var2.z() - this.v.z();
            return var3 * var3 + var5 * var5;
         }).thenComparingLong(Entry::getKey)).map(var0 -> new LightDebugModule.p(var0.getKey(), var0.getValue())).toList();
      }
   }

   private void V() {
      if (this.isRuntimeActive() && this.isInGame() && this.e == mc.level && !(this.Q.P() <= 0.0)) {
         try {
            int var1 = 0;
            int var2 = this.J.P().intValue();

            for (LightDebugModule.p var4 : this.a) {
               ChunkPos var5 = ChunkPos.unpack(var4.key());
               if (this.e.getChunkSource().getChunkNow(var5.x(), var5.z()) != null) {
                  for (com.larpclient.d.h var7 : var4.runs()) {
                     if (var1++ >= var2) {
                        return;
                     }

                     int var8 = com.larpclient.d.H(var7.light(), this.Q.P().intValue());
                     Gizmos.cuboid(new AABB(var7.x(), var7.y(), var7.z(), var7.x() + var7.length(), var7.y() + 1, var7.z() + 1), GizmoStyle.fill(var8))
                        .setAlwaysOnTop();
                  }
               }
            }
         } catch (RuntimeException var9) {
            mt.B("BaseFinding", "Light Debug render failed; scan continues", var9);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.a.size() + " lit chunks";
   }

   private record p(long key, List<com.larpclient.d.h> runs) {
   }
}
