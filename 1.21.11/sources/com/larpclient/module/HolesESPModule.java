package com.larpclient.module;

import com.larpclient.q8;
import com.larpclient.q9;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Load;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1923;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2818;
import net.minecraft.class_638;

public final class HolesESPModule extends Module {
   public static final int y = 8192;
   public static final int L = 2048;
   public static final int b = 65536;
   private static final int p = 8192;
   private final lo m = this.addSetting(new lo("Opacity", "Fill alpha of passage boxes, from 0 to 255", 60.0, 0.0, 255.0, 1.0));
   private final l0 r = this.addSetting(new l0("1x1 Color", "One-block-wide enclosed passages", -6283024));
   private final l0 S = this.addSetting(new l0("3x1 Color", "Three-block-wide enclosed passages", -12058494));
   private final lo B = this.addSetting(new lo("Marker Limit", "Nearest boxes submitted in dense terrain", 4096.0, 256.0, 16384.0, 256.0));
   private final Map<Long, Map<class_2338, Integer>> e = new HashMap<>();
   private final Set<Long> U = new HashSet<>();
   private final ArrayDeque<Long> F = new ArrayDeque<>();
   private final Map<Long, q9.z> I = new HashMap<>();
   private final Set<Long> J = new HashSet<>();
   private Map<class_2338, Integer> t;
   private List<HolesESPModule.t> Q = List.of();
   private class_638 s;
   private class_2818 j;
   private long M;
   private int d;
   private int V;
   private int X;
   private int T;
   private boolean a;
   private static HolesESPModule H;

   public HolesESPModule() {
      super("Holes ESP", "Find enclosed vertical passages in received terrain", Category.RENDER, -1);
      H = this;
      this.B.a(var1 -> this.G());
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.M());
      ClientChunkEvents.CHUNK_LOAD.register((Load)(var1, var2) -> {
         if (this.isRuntimeActive() && this.s == var1) {
            this.I(var2.method_12004());
         }
      });
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (this.s == var1) {
            if (!this.isInGame()) {
               this.h();
               this.s = null;
            } else {
               long var3 = var2.method_12004().method_8324();
               this.P(var3);
               this.U.remove(var3);
               this.F.remove(var3);
               if (this.j != null && this.M == var3) {
                  this.F();
               }

               this.I(var2.method_12004());
               this.G();
            }
         }
      });
   }

   public List<lb<?>> i() {
      return List.of(this.m, this.r, this.S);
   }

   public List<lb<?>> e() {
      return List.of(this.B);
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.E);
   }

   @Override
   public void onEnable() {
      this.h();
      this.s = getMc().field_1687;
      q8.G("BaseFinding", "Holes ESP active: four-level collision passages, {} reads/tick", 8192);
   }

   @Override
   public void onDisable() {
      this.h();
      this.s = null;
      q8.v("BaseFinding", "Holes ESP released scan and marker state");
   }

   private void h() {
      this.e.clear();
      this.U.clear();
      this.F.clear();
      this.I.clear();
      this.J.clear();
      this.Q = List.of();
      this.T = this.X = 0;
      this.a = (boolean)0;
      this.F();
   }

   private void F() {
      this.j = null;
      this.t = null;
      this.d = 0;
   }

   private void P(long var1) {
      Map var3 = this.e.remove(var1);
      if (var3 != null) {
         this.T = this.T - var3.size();
      }
   }

   private int i() {
      return Math.clamp(((Integer)getMc().field_1690.method_42503().method_41753()).intValue(), 1, 32);
   }

   private boolean C(class_1923 var1) {
      class_1923 var2 = getMc().field_1724.method_31476();
      int var3 = this.i();
      return Math.abs((long)var1.field_9181 - var2.field_9181) <= var3 && Math.abs((long)var1.field_9180 - var2.field_9180) <= var3;
   }

   private void f(long var1) {
      class_1923 var3 = new class_1923(var1);
      if (this.C(var3) && this.s.method_2935().method_21730(var3.field_9181, var3.field_9180) != null) {
         if (this.U.add(var1)) {
            this.F.addLast(var1);
         }
      }
   }

   private void I(class_1923 var1) {
      for (int var2 = -1; var2 <= 1; var2++) {
         for (int var3 = -1; var3 <= 1; var3++) {
            long var4 = class_1923.method_8331(var1.field_9181 + var2, var1.field_9180 + var3);
            if (this.j != null && this.M == var4) {
               this.F();
            }

            this.P(var4);
            this.f(var4);
         }
      }
   }

   public static void e(class_638 var0, class_2338 var1) {
      if (H != null && H.isEnabled()) {
         HolesESPModule var2 = H;
         if (var2 != null && var2.isRuntimeActive() && var2.s == var0 && getMc().method_18854()) {
            if (var2.J.size() < 4096) {
               var2.J.add(class_1923.method_8331(var1.method_10263() >> 4, var1.method_10260() >> 4));
            } else {
               var2.e.clear();
               var2.T = 0;
               var2.X = 0;
            }
         }
      }
   }

   private q9.z n(class_2338 var1) {
      if (this.s.method_31606(var1)) {
         return q9.z.B;
      }

      q9.z var2 = this.I.get(var1.method_10063());
      if (var2 != null) {
         return var2;
      }

      class_2818 var3 = this.s.method_2935().method_21730(var1.method_10263() >> 4, var1.method_10260() >> 4);
      if (var3 == null) {
         return q9.z.B;
      }

      this.V++;
      class_265 var4 = var3.method_8320(var1).method_26220(this.s, var1);
      q9.z var5 = var4.equals(class_259.method_1077()) ? q9.z.S : q9.z.r;
      this.I.put(var1.method_10063(), var5);
      return var5;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.s != mc.field_1687) {
            this.h();
            this.s = mc.field_1687;
         }

         int var10000 = this.X;
         byte var11 = 1;
         int var10 = this.X;
         this.X = var10 + var11;
         var10000 %= 20;
         boolean var13 = false;
         int var12 = var10000;
         var10000 = Integer.signum(var12);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.V();
         }

         if (!this.J.isEmpty()) {
            for (long var2 : this.J) {
               this.I(new class_1923(var2));
            }

            this.J.clear();
            this.G();
         }

         this.I.clear();
         this.V = 0;
         byte var18 = 0;

         for (int var19 = 0; var19 < 2048 && this.V < 8064; var19++) {
            if (this.j == null) {
               Long var3 = this.F.pollFirst();
               if (var3 == null) {
                  break;
               }

               this.U.remove(var3);
               class_1923 var4 = new class_1923(var3);
               this.j = this.s.method_2935().method_21730(var4.field_9181, var4.field_9180);
               if (this.j == null || !this.C(var4)) {
                  this.j = null;
                  continue;
               }

               this.M = var3;
               this.t = new HashMap<>();
               this.d = 0;
            }

            int var20 = this.s.method_31605();
            if (this.d >= 256 * var20) {
               this.P(this.M);
               this.e.put(this.M, this.t);
               this.T = this.T + this.t.size();
               this.F();
               var18 = 1;
            } else {
               int var21 = this.d;
               int var5 = this.s.method_31607() + var21 % var20;
               int var6 = this.j.method_31602(var5);
               if (this.j.method_12006()[var6].method_38292()) {
                  this.d = this.d + Math.min(16 - (var5 & 15), var20 - var21 % var20);
               } else {
                  this.d++;
                  int var7 = var21 / var20;
                  class_2338 var8 = new class_2338(this.j.method_12004().method_8326() + var7 / 16, var5, this.j.method_12004().method_8328() + var7 % 16);
                  int var9 = q9.z(var8, this.s.method_31600(), this::n);
                  if (var9 >= 0) {
                     if (this.t.size() < 8192 && this.T + this.t.size() < 65536) {
                        this.t.put(var8, var9);
                     } else if (!this.a) {
                        this.a = (boolean)1;
                        q8.G("BaseFinding", "Holes ESP dense-result ceiling reached; nearest retained markers remain visible");
                     }
                  }
               }
            }
         }

         this.I.clear();
         boolean var15 = false;
         byte var14 = var18;
         var10000 = Integer.signum(var14);
         if ((var10000 | -var10000) >>> 31 == 0) {
            var10000 = this.X % 20;
            boolean var17 = false;
            int var16 = var10000;
            var10000 = Integer.signum(var16);
            if ((var10000 | -var10000) >>> 31 != 0) {
               return;
            }
         }

         this.G();
      }
   }

   private void V() {
      for (long var2 : new ArrayList<>(this.e.keySet())) {
         class_1923 var4 = new class_1923(var2);
         if (!this.C(var4) || this.s.method_2935().method_21730(var4.field_9181, var4.field_9180) == null) {
            this.P(var2);
         }
      }

      if (this.j != null
         && (!this.C(this.j.method_12004()) || this.s.method_2935().method_21730(this.j.method_12004().field_9181, this.j.method_12004().field_9180) != this.j)
         )
       {
         this.F();
      }

      this.F.removeIf(var1 -> {
         class_1923 var2x = new class_1923(var1);
         int var3x = this.C(var2x) && this.s.method_2935().method_21730(var2x.field_9181, var2x.field_9180) != null ? 0 : 1;
         if (var3x != 0) {
            this.U.remove(var1);
         }

         return (boolean)var3x;
      });
      class_1923 var8 = mc.field_1724.method_31476();
      int var9 = this.i();
      ArrayList var3 = new ArrayList();

      for (int var10 = var8.field_9181 - var9; var10 <= var8.field_9181 + var9; var10++) {
         for (int var5 = var8.field_9180 - var9; var5 <= var8.field_9180 + var9; var5++) {
            long var6 = class_1923.method_8331(var10, var5);
            if (!this.e.containsKey(var6)
               && (this.j == null || this.M != var6)
               && this.s.method_2935().method_21730(var10, var5) != null
               && !this.U.contains(var6)) {
               var3.add(var6);
            }
         }
      }

      var3.sort(Comparator.comparingDouble(var1 -> {
         class_1923 var2x = new class_1923(var1);
         return Math.pow(var2x.field_9181 - var8.field_9181, 2.0) + Math.pow(var2x.field_9180 - var8.field_9180, 2.0);
      }));

      for (long var12 : var3) {
         this.f(var12);
      }
   }

   private void G() {
      if (this.s != null && getMc().field_1724 != null) {
         class_243 var1 = mc.field_1724.method_73189();
         this.Q = this.e
            .values()
            .stream()
            .flatMap(var0 -> var0.entrySet().stream())
            .map(var0 -> new HolesESPModule.t(var0.getKey(), var0.getValue()))
            .sorted(Comparator.comparingDouble(var1x -> var1x.pos().method_19770(var1)))
            .limit(this.B.getValue().longValue())
            .toList();
      } else {
         this.Q = List.of();
      }
   }

   private void M() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.s == mc.field_1687) {
            int var1 = Math.clamp(this.m.getValue().intValue(), 0, 255);
            if (var1 != 0) {
               class_1923 var2 = mc.field_1724.method_31476();
               int var3 = this.i();

               for (HolesESPModule.t var5 : this.Q) {
                  if (Math.abs((long)(var5.pos().method_10263() >> 4) - var2.field_9181) <= var3
                     && Math.abs((long)(var5.pos().method_10260() >> 4) - var2.field_9180) <= var3) {
                     int var6 = var5.kind() == 0 ? this.r.getValue() : this.S.getValue();
                     class_12180.method_75541(q9.h(var5.pos(), var5.kind()), class_12179.method_75539(var1 << 24 | var6 & 16777215)).method_75533();
                  }
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.T + " passages";
   }

   private record t(class_2338 pos, int kind) {
   }
}
