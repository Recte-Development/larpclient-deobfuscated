package com.larpclient.module;

import com.larpclient.e4;
import com.larpclient.q8;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1923;
import net.minecraft.class_1944;
import net.minecraft.class_238;
import net.minecraft.class_638;
import net.minecraft.class_2338.class_2339;

public final class LightDebugModule extends Module {
   private final lo W = this.addSetting(new lo("Max Y", "Highest layer to inspect, starting at Y -64", -50.0, -64.0, 16.0, 1.0));
   private final lo J = this.addSetting(new lo("Chunk Radius", "Loaded square radius around your current chunk", 8.0, 1.0, 12.0, 1.0));
   private final lo h = this.addSetting(new lo("Alpha", "Opacity of light-strength colored block volumes", 160.0, 0.0, 255.0, 1.0));
   private final lo G = this.addSetting(
      new lo("Render Limit", "Nearest chunk light runs drawn per frame; all scan data is retained", 16384.0, 1024.0, 65536.0, 1024.0)
   );
   private final Map<Long, List<e4.n>> n = new HashMap<>();
   private List<LightDebugModule.m> f = List.of();
   private class_638 k;
   private class_1923 U;
   private int e;
   private int T;
   private int m;
   private List<class_1923> t = List.of();
   private byte[] B;
   private int b;
   private int y;
   private boolean s;
   private final class_2339 A = new class_2339();
   private static final int z = 32768;

   public LightDebugModule() {
      super("Light Debug", "Show underground block light brighter than the sky", Category.RENDER, -1);
      this.W.a(var1 -> this.M());
      this.J.a(var1 -> this.M());
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.B());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.k) {
            this.n.remove(var2.method_12004().method_8324());
            this.N();
         }
      });
   }

   public List<lb<?>> b() {
      return List.of(this.W, this.J);
   }

   public List<lb<?>> V() {
      return List.of(this.h, this.G);
   }

   private void M() {
      this.n.clear();
      this.f = List.of();
      this.t = List.of();
      this.B = null;
      this.U = null;
      this.T = this.m = this.e = 0;
      this.s = (boolean)1;
   }

   @Override
   public void onEnable() {
      this.M();
      this.k = getMc().field_1687;
      q8.G("BaseFinding", "Light Debug active: block light above sky, Y -64..{}, radius {}", this.W.getValue(), this.J.getValue());
   }

   @Override
   public void onDisable() {
      q8.v("BaseFinding", "Light Debug cleanup: {} lit chunks", this.n.size());
      this.M();
      this.k = null;
   }

   private void F(class_1923 var1) {
      this.U = var1;
      this.b = Math.max(-64, this.k.method_31607());
      this.y = Math.min(this.W.getValue().intValue(), this.k.method_31600());
      int var2 = this.J.getValue().intValue();
      ArrayList var3 = new ArrayList();
      this.n
         .keySet()
         .removeIf(
            var2x -> {
               class_1923 var3x = new class_1923(var2x);
               return Math.abs((long)var3x.field_9181 - this.U.field_9181) > var2
                  || Math.abs((long)var3x.field_9180 - this.U.field_9180) > var2
                  || this.k.method_2935().method_21730(var3x.field_9181, var3x.field_9180) == null;
            }
         );

      for (int var4 = -var2; var4 <= var2; var4++) {
         for (int var5 = -var2; var5 <= var2; var5++) {
            class_1923 var6 = new class_1923(var1.field_9181 + var4, var1.field_9180 + var5);
            if (this.k.method_2935().method_21730(var6.field_9181, var6.field_9180) != null) {
               var3.add(var6);
            }
         }
      }

      var3.sort(
         Comparator.comparingLong(
            var1x -> (long)(var1x.field_9181 - var1.field_9181) * (var1x.field_9181 - var1.field_9181)
               + (long)(var1x.field_9180 - var1.field_9180) * (var1x.field_9180 - var1.field_9180)
         )
      );
      this.t = List.copyOf(var3);
      this.T = this.m = 0;
      this.B = null;
      this.s = (boolean)0;
      this.N();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.k != mc.field_1687) {
            this.M();
            this.k = mc.field_1687;
         }

         class_1923 var1 = mc.field_1724.method_31476();
         int var10000 = ++this.e % 16;
         boolean var14 = false;
         int var13 = var10000;
         var10000 = Integer.signum(var13);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.s = (boolean)1;
         }

         if (!var1.equals(this.U)) {
            this.t = List.of();
            this.B = null;
            this.F(var1);
         } else {
            var10000 = this.T;
            int var10 = this.t.size();
            int var9 = var10000;
            if (Integer.compare(var9, var10) >>> 31 == 0) {
               boolean var12 = false;
               byte var11 = this.s;
               var10000 = Integer.signum(var11);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                  this.F(var1);
               }
            }
         }

         byte var2 = 0;
         int var3 = 32768;

         while (var3 > 0 && this.T < this.t.size()) {
            class_1923 var4 = this.t.get(this.T);
            if (this.y >= this.b && this.k.method_2935().method_21730(var4.field_9181, var4.field_9180) != null) {
               if (this.B == null) {
                  this.B = new byte[(this.y - this.b + 1) * 256];
               }

               int var5 = Math.min(this.B.length, this.m + var3);
               var3 -= var5 - this.m;

               while (this.m < var5) {
                  this.A.method_10103(var4.method_8326() + (this.m & 15), this.b + (this.m >> 8), var4.method_8328() + (this.m >> 4 & 15));
                  int var6 = this.k.method_8314(class_1944.field_9282, this.A);
                  if (var6 > 0 && e4.I(var6, this.k.method_8314(class_1944.field_9284, this.A))) {
                     this.B[this.m] = (byte)var6;
                  }

                  this.m++;
               }

               if (this.m == this.B.length) {
                  List var15 = e4.o(this.B, var4.field_9181, var4.field_9180, this.b);
                  if (var15.isEmpty()) {
                     this.n.remove(var4.method_8324());
                  } else {
                     this.n.put(var4.method_8324(), var15);
                  }

                  this.T++;
                  this.m = 0;
                  this.B = null;
                  var2 = 1;
               }
            } else {
               this.n.remove(var4.method_8324());
               this.T++;
               this.m = 0;
               this.B = null;
               var2 = 1;
            }
         }

         boolean var8 = false;
         byte var7 = var2;
         var10000 = Integer.signum(var7);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.N();
         }
      }
   }

   private void N() {
      if (this.U == null) {
         this.f = List.of();
      } else {
         this.f = this.n.entrySet().stream().sorted(Comparator.<Entry<Long, List<e4.n>>>comparingDouble(var1 -> {
            class_1923 var2 = new class_1923(var1.getKey());
            double var3 = (double)var2.field_9181 - this.U.field_9181;
            double var5 = (double)var2.field_9180 - this.U.field_9180;
            return var3 * var3 + var5 * var5;
         }).thenComparingLong(Entry::getKey)).map(var0 -> new LightDebugModule.m(var0.getKey(), var0.getValue())).toList();
      }
   }

   private void B() {
      if (this.isRuntimeActive() && this.isInGame() && this.k == mc.field_1687 && !(this.h.getValue() <= 0.0)) {
         try {
            int var1 = 0;
            int var2 = this.G.getValue().intValue();

            for (LightDebugModule.m var4 : this.f) {
               class_1923 var5 = new class_1923(var4.key());
               if (this.k.method_2935().method_21730(var5.field_9181, var5.field_9180) != null) {
                  for (e4.n var7 : var4.runs()) {
                     if (var1++ >= var2) {
                        return;
                     }

                     int var8 = e4.F(var7.light(), this.h.getValue().intValue());
                     class_12180.method_75541(
                           new class_238(var7.x(), var7.y(), var7.z(), var7.x() + var7.length(), var7.y() + 1, var7.z() + 1), class_12179.method_75539(var8)
                        )
                        .method_75533();
                  }
               }
            }
         } catch (RuntimeException var9) {
            q8.O("BaseFinding", "Light Debug render failed; scan continues", var9);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.f.size() + " lit chunks";
   }

   private record m(long key, List<e4.n> runs) {
   }
}
