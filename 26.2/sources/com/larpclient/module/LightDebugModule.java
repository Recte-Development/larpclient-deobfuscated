package com.larpclient.module;

import com.larpclient.h8;
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
   private final e h = this.addSetting(new e("Max Y", "Highest layer to inspect, starting at Y -64", -50.0, -64.0, 16.0, 1.0));
   private final e L = this.addSetting(new e("Chunk Radius", "Loaded square radius around your current chunk", 8.0, 1.0, 12.0, 1.0));
   private final e f = this.addSetting(new e("Alpha", "Opacity of light-strength colored block volumes", 160.0, 0.0, 255.0, 1.0));
   private final e j = this.addSetting(
      new e("Render Limit", "Nearest chunk light runs drawn per frame; all scan data is retained", 16384.0, 1024.0, 65536.0, 1024.0)
   );
   private final Map<Long, List<h8.r>> v = new HashMap<>();
   private List<LightDebugModule.q> Y = List.of();
   private ClientLevel Z;
   private ChunkPos I;
   private int U;
   private int r;
   private int X;
   private List<ChunkPos> u = List.of();
   private byte[] c;
   private int l;
   private int k;
   private boolean W;
   private final MutableBlockPos F = new MutableBlockPos();
   private static final int R = 32768;

   public LightDebugModule() {
      super("Light Debug", "Show underground block light brighter than the sky", Category.RENDER, -1);
      this.h.o(var1 -> this.L());
      this.L.o(var1 -> this.L());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.n());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.Z) {
            this.v.remove(var2.getPos().pack());
            this.K();
         }
      });
   }

   public List<com.larpclient.module.q<?>> V() {
      return List.of(this.h, this.L);
   }

   public List<com.larpclient.module.q<?>> I() {
      return List.of(this.f, this.j);
   }

   private void L() {
      this.v.clear();
      this.Y = List.of();
      this.u = List.of();
      this.c = null;
      this.I = null;
      this.r = this.X = this.U = 0;
      this.W = (boolean)1;
   }

   @Override
   public void onEnable() {
      this.L();
      this.Z = getMc().level;
      com.larpclient.h.A("BaseFinding", "Light Debug active: block light above sky, Y -64..{}, radius {}", this.h.getValue(), this.L.getValue());
   }

   @Override
   public void onDisable() {
      com.larpclient.h.U("BaseFinding", "Light Debug cleanup: {} lit chunks", this.v.size());
      this.L();
      this.Z = null;
   }

   private void d(ChunkPos var1) {
      this.I = var1;
      this.l = Math.max(-64, this.Z.getMinY());
      this.k = Math.min(this.h.getValue().intValue(), this.Z.getMaxY());
      int var2 = this.L.getValue().intValue();
      ArrayList var3 = new ArrayList();
      this.v
         .keySet()
         .removeIf(
            var2x -> {
               ChunkPos var3x = ChunkPos.unpack(var2x);
               return Math.abs((long)var3x.x() - this.I.x()) > var2
                  || Math.abs((long)var3x.z() - this.I.z()) > var2
                  || this.Z.getChunkSource().getChunkNow(var3x.x(), var3x.z()) == null;
            }
         );

      for (int var4 = -var2; var4 <= var2; var4++) {
         for (int var5 = -var2; var5 <= var2; var5++) {
            ChunkPos var6 = new ChunkPos(var1.x() + var4, var1.z() + var5);
            if (this.Z.getChunkSource().getChunkNow(var6.x(), var6.z()) != null) {
               var3.add(var6);
            }
         }
      }

      var3.sort(
         Comparator.comparingLong(var1x -> (long)(var1x.x() - var1.x()) * (var1x.x() - var1.x()) + (long)(var1x.z() - var1.z()) * (var1x.z() - var1.z()))
      );
      this.u = List.copyOf(var3);
      this.r = this.X = 0;
      this.c = null;
      this.W = (boolean)0;
      this.K();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.Z != mc.level) {
            this.L();
            this.Z = mc.level;
         }

         ChunkPos var1 = mc.player.chunkPosition();
         byte var8 = 1;
         int var7 = this.U;
         int var10000 = (this.U = var7 + var8) % 16;
         boolean var10 = false;
         int var9 = var10000;
         var10000 = Integer.signum(var9);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.W = (boolean)1;
         }

         if (!var1.equals(this.I)) {
            this.u = List.of();
            this.c = null;
            this.d(var1);
         } else {
            var10000 = this.r;
            int var12 = this.u.size();
            int var11 = var10000;
            if (Integer.compare(var11, var12) >>> 31 == 0) {
               boolean var14 = false;
               byte var13 = this.W;
               var10000 = Integer.signum(var13);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                  this.d(var1);
               }
            }
         }

         byte var2 = 0;
         int var3 = 32768;

         while (var3 > 0 && this.r < this.u.size()) {
            ChunkPos var4 = this.u.get(this.r);
            if (this.k >= this.l && this.Z.getChunkSource().getChunkNow(var4.x(), var4.z()) != null) {
               if (this.c == null) {
                  this.c = new byte[(this.k - this.l + 1) * 256];
               }

               int var5 = Math.min(this.c.length, this.X + var3);
               var3 -= var5 - this.X;

               while (this.X < var5) {
                  this.F.set(var4.getMinBlockX() + (this.X & 15), this.l + (this.X >> 8), var4.getMinBlockZ() + (this.X >> 4 & 15));
                  int var6 = this.Z.getBrightness(LightLayer.BLOCK, this.F);
                  if (var6 > 0 && h8.U(var6, this.Z.getBrightness(LightLayer.SKY, this.F))) {
                     this.c[this.X] = (byte)var6;
                  }

                  this.X++;
               }

               if (this.X == this.c.length) {
                  List var15 = h8.d(this.c, var4.x(), var4.z(), this.l);
                  if (var15.isEmpty()) {
                     this.v.remove(var4.pack());
                  } else {
                     this.v.put(var4.pack(), var15);
                  }

                  this.r++;
                  this.X = 0;
                  this.c = null;
                  var2 = 1;
               }
            } else {
               this.v.remove(var4.pack());
               this.r++;
               this.X = 0;
               this.c = null;
               var2 = 1;
            }
         }

         if (var2 != 0) {
            this.K();
         }
      }
   }

   private void K() {
      if (this.I == null) {
         this.Y = List.of();
      } else {
         this.Y = this.v.entrySet().stream().sorted(Comparator.<Entry<Long, List<h8.r>>>comparingDouble(var1 -> {
            ChunkPos var2 = ChunkPos.unpack(var1.getKey());
            double var3 = (double)var2.x() - this.I.x();
            double var5 = (double)var2.z() - this.I.z();
            return var3 * var3 + var5 * var5;
         }).thenComparingLong(Entry::getKey)).map(var0 -> new LightDebugModule.q(var0.getKey(), var0.getValue())).toList();
      }
   }

   private void n() {
      if (this.isRuntimeActive() && this.isInGame() && this.Z == mc.level && !(this.f.getValue() <= 0.0)) {
         try {
            int var1 = 0;
            int var2 = this.j.getValue().intValue();

            for (LightDebugModule.q var4 : this.Y) {
               ChunkPos var5 = ChunkPos.unpack(var4.key());
               if (this.Z.getChunkSource().getChunkNow(var5.x(), var5.z()) != null) {
                  for (h8.r var7 : var4.runs()) {
                     if (var1++ >= var2) {
                        return;
                     }

                     int var8 = h8.x(var7.light(), this.f.getValue().intValue());
                     Gizmos.cuboid(new AABB(var7.x(), var7.y(), var7.z(), var7.x() + var7.length(), var7.y() + 1, var7.z() + 1), GizmoStyle.fill(var8))
                        .setAlwaysOnTop();
                  }
               }
            }
         } catch (RuntimeException var9) {
            com.larpclient.h.w("BaseFinding", "Light Debug render failed; scan continues", var9);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.Y.size() + " lit chunks";
   }

   private record q(long key, List<h8.r> runs) {
   }
}
