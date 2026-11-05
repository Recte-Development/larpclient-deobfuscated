package com.larpclient.module;

import com.larpclient.r4;
import com.larpclient.up;
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
   private final lc w = this.addSetting(new lc("Max Y", "Highest layer to inspect, starting at Y -64", -50.0, -64.0, 16.0, 1.0));
   private final lc Q = this.addSetting(new lc("Chunk Radius", "Loaded square radius around your current chunk", 8.0, 1.0, 12.0, 1.0));
   private final lc r = this.addSetting(new lc("Alpha", "Opacity of light-strength colored block volumes", 160.0, 0.0, 255.0, 1.0));
   private final lc B = this.addSetting(
      new lc("Render Limit", "Nearest chunk light runs drawn per frame; all scan data is retained", 16384.0, 1024.0, 65536.0, 1024.0)
   );
   private final Map<Long, List<up.t>> T = new HashMap<>();
   private List<LightDebugModule.k> P = List.of();
   private ClientLevel W;
   private ChunkPos Y;
   private int p;
   private int n;
   private int i;
   private List<ChunkPos> I = List.of();
   private byte[] O;
   private int x;
   private int E;
   private boolean S;
   private final MutableBlockPos o = new MutableBlockPos();
   private static final int z = 32768;

   public LightDebugModule() {
      super("Light Debug", "Show underground block light brighter than the sky", Category.RENDER, -1);
      this.w.W(var1 -> this.u());
      this.Q.W(var1 -> this.u());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.o());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.W) {
            this.T.remove(var2.getPos().pack());
            this.h();
         }
      });
   }

   public List<l6<?>> O() {
      return List.of(this.w, this.Q);
   }

   public List<l6<?>> w() {
      return List.of(this.r, this.B);
   }

   private void u() {
      this.T.clear();
      this.P = List.of();
      this.I = List.of();
      this.O = null;
      this.Y = null;
      this.n = this.i = this.p = 0;
      this.S = (boolean)1;
   }

   @Override
   public void onEnable() {
      this.u();
      this.W = getMc().level;
      r4.V("BaseFinding", "Light Debug active: block light above sky, Y -64..{}, radius {}", this.w.getValue(), this.Q.getValue());
   }

   @Override
   public void onDisable() {
      r4.g("BaseFinding", "Light Debug cleanup: {} lit chunks", this.T.size());
      this.u();
      this.W = null;
   }

   private void F(ChunkPos var1) {
      this.Y = var1;
      this.x = Math.max(-64, this.W.getMinY());
      this.E = Math.min(this.w.getValue().intValue(), this.W.getMaxY());
      int var2 = this.Q.getValue().intValue();
      ArrayList var3 = new ArrayList();
      this.T
         .keySet()
         .removeIf(
            var2x -> {
               ChunkPos var3x = ChunkPos.unpack(var2x);
               return Math.abs((long)var3x.x() - this.Y.x()) > var2
                  || Math.abs((long)var3x.z() - this.Y.z()) > var2
                  || this.W.getChunkSource().getChunkNow(var3x.x(), var3x.z()) == null;
            }
         );

      for (int var4 = -var2; var4 <= var2; var4++) {
         for (int var5 = -var2; var5 <= var2; var5++) {
            ChunkPos var6 = new ChunkPos(var1.x() + var4, var1.z() + var5);
            if (this.W.getChunkSource().getChunkNow(var6.x(), var6.z()) != null) {
               var3.add(var6);
            }
         }
      }

      var3.sort(
         Comparator.comparingLong(var1x -> (long)(var1x.x() - var1.x()) * (var1x.x() - var1.x()) + (long)(var1x.z() - var1.z()) * (var1x.z() - var1.z()))
      );
      this.I = List.copyOf(var3);
      this.n = this.i = 0;
      this.O = null;
      this.S = (boolean)0;
      this.h();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.W != mc.level) {
            this.u();
            this.W = mc.level;
         }

         ChunkPos var1 = mc.player.chunkPosition();
         byte var12 = 1;
         int var11 = this.p;
         if ((this.p = var11 + var12) % 16 == 0) {
            this.S = (boolean)1;
         }

         if (!var1.equals(this.Y)) {
            this.I = List.of();
            this.O = null;
            this.F(var1);
         } else {
            int var10000 = this.n;
            int var8 = this.I.size();
            int var7 = var10000;
            if (Integer.compare(var7, var8) >>> 31 == 0) {
               boolean var10 = false;
               byte var9 = this.S;
               var10000 = Integer.signum(var9);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                  this.F(var1);
               }
            }
         }

         byte var2 = 0;
         int var3 = 32768;

         while (var3 > 0 && this.n < this.I.size()) {
            ChunkPos var4 = this.I.get(this.n);
            if (this.E >= this.x && this.W.getChunkSource().getChunkNow(var4.x(), var4.z()) != null) {
               if (this.O == null) {
                  this.O = new byte[(this.E - this.x + 1) * 256];
               }

               int var5 = Math.min(this.O.length, this.i + var3);
               var3 -= var5 - this.i;

               while (this.i < var5) {
                  this.o.set(var4.getMinBlockX() + (this.i & 15), this.x + (this.i >> 8), var4.getMinBlockZ() + (this.i >> 4 & 15));
                  int var6 = this.W.getBrightness(LightLayer.BLOCK, this.o);
                  if (var6 > 0 && up.s(var6, this.W.getBrightness(LightLayer.SKY, this.o))) {
                     this.O[this.i] = (byte)var6;
                  }

                  this.i++;
               }

               if (this.i == this.O.length) {
                  List var15 = up.X(this.O, var4.x(), var4.z(), this.x);
                  if (var15.isEmpty()) {
                     this.T.remove(var4.pack());
                  } else {
                     this.T.put(var4.pack(), var15);
                  }

                  this.n++;
                  this.i = 0;
                  this.O = null;
                  var2 = 1;
               }
            } else {
               this.T.remove(var4.pack());
               this.n++;
               this.i = 0;
               this.O = null;
               var2 = 1;
            }
         }

         boolean var14 = false;
         byte var13 = var2;
         int var17 = Integer.signum(var13);
         if (((var17 | -var17) >>> 31 ^ 1) == 0) {
            this.h();
         }
      }
   }

   private void h() {
      if (this.Y == null) {
         this.P = List.of();
      } else {
         this.P = this.T.entrySet().stream().sorted(Comparator.<Entry<Long, List<up.t>>>comparingDouble(var1 -> {
            ChunkPos var2 = ChunkPos.unpack(var1.getKey());
            double var3 = (double)var2.x() - this.Y.x();
            double var5 = (double)var2.z() - this.Y.z();
            return var3 * var3 + var5 * var5;
         }).thenComparingLong(Entry::getKey)).map(var0 -> new LightDebugModule.k(var0.getKey(), var0.getValue())).toList();
      }
   }

   private void o() {
      if (this.isRuntimeActive() && this.isInGame() && this.W == mc.level && !(this.r.getValue() <= 0.0)) {
         try {
            int var1 = 0;
            int var2 = this.B.getValue().intValue();

            for (LightDebugModule.k var4 : this.P) {
               ChunkPos var5 = ChunkPos.unpack(var4.key());
               if (this.W.getChunkSource().getChunkNow(var5.x(), var5.z()) != null) {
                  for (up.t var7 : var4.runs()) {
                     if (var1++ >= var2) {
                        return;
                     }

                     int var8 = up.x(var7.light(), this.r.getValue().intValue());
                     Gizmos.cuboid(new AABB(var7.x(), var7.y(), var7.z(), var7.x() + var7.length(), var7.y() + 1, var7.z() + 1), GizmoStyle.fill(var8))
                        .setAlwaysOnTop();
                  }
               }
            }
         } catch (RuntimeException var9) {
            r4.T("BaseFinding", "Light Debug render failed; scan continues", var9);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.P.size() + " lit chunks";
   }

   private record k(long key, List<up.t> runs) {
   }
}
