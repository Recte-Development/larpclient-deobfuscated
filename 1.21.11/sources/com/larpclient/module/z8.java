package com.larpclient.module;

import com.larpclient.c7;
import com.larpclient.h5;
import com.larpclient.h9;
import com.larpclient.i2;
import com.larpclient.l2;
import com.larpclient.q8;
import com.larpclient.wt;
import java.util.ArrayDeque;
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
import net.minecraft.class_1923;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_2826;
import net.minecraft.class_3417;
import net.minecraft.class_638;

public abstract class z8 extends Module {
   private final lo c = this.addSetting(new lo("Scan Radius", "Loaded chunks around the player", 4.0, 1.0, 12.0, 1.0));
   private final lo g = this.addSetting(new lo("Sections Per Tick", "At most 4096 block reads per matching section", 2.0, 1.0, 8.0, 1.0));
   private final lo D = this.addSetting(new lo("Rescan Seconds", "Minimum interval between complete scans of a chunk", 2.0, 1.0, 30.0, 1.0));
   private final lo o = this.addSetting(new lo("Marker Y", "Height of the thin chunk markers", 64.0, -64.0, 320.0, 1.0));
   private final lo B = this.addSetting(new lo("Max Markers", "Nearest qualifying chunks drawn", 64.0, 1.0, 256.0, 1.0));
   private final li r = this.addSetting(new li("Marker Style", "Chunk outline or translucent slab", "Filled", "Box", "Corners", "Filled"));
   private final l3 T = this.addSetting(new l3("Labels", "Evidence labels on the nearest 24 chunk markers", true));
   private final l3 w = this.addSetting(new l3("Tracers", "Lines from the camera to chunk markers", false));
   private final l3 n = this.addSetting(new l3("Notifications", "Deduplicated discoveries, at most one alert per second", true));
   private final l3 p = this.addSetting(new l3("Chat Alerts", "Include coordinates and observed evidence in local chat", true));
   private final l3 S = this.addSetting(new l3("Alert Sound", "Play a local chime for each delivered discovery alert", false));
   private final l2 h = new l2(var1x -> this.addSetting(var1x));
   private final h9<class_2818> b = new h9<>();
   private final h5 J = new h5();
   private final ArrayDeque<z8.r> C = new ArrayDeque<>();
   private List<z8.p> K = List.of();
   private final Map<Long, Long> M = new HashMap<>();
   private class_638 q;
   private long a;
   private int E;
   private int y;
   private int U;
   private int G;
   private int e;
   private final h9.v<class_2818> u = new h9.v<class_2818>() {
      public class_2818 loaded(int var1, int var2) {
         return z8.this.q.method_2935().method_21730(var1, var2);
      }

      @Override
      public boolean due(int var1, int var2) {
         return z8.this.J.p(class_1923.method_8331(var1, var2), z8.this.a, z8.this.D.getValue().longValue() * 20L);
      }

      public int x(class_2818 var1) {
         return var1.method_12006().length;
      }

      @Override
      public void begin() {
         z8.this.E = z8.this.y = z8.this.U = 0;
         z8.this.G = Integer.MIN_VALUE;
         z8.this.beginScan();
      }

      public void c(class_2818 var1) {
         this.begin();
         z8.this.beginChunk(var1);
      }

      public boolean u(class_2818 var1, int var2) {
         int var3 = var1.method_31607() + var2 * 16;
         return var3 + 15L >= z8.this.minimumY() && var3 <= z8.this.maximumY() && z8.this.sectionCandidate(var1, var2);
      }

      public void x(class_2818 var1, int var2) {
         int var3 = var1.method_31607() + var2 * 16;
         class_2826 var4 = var1.method_12006()[var2];
         int var5 = (int)Math.min(15L, (long)z8.this.maximumY() - var3);
         int var6 = (int)Math.max(0L, (long)z8.this.minimumY() - var3);

         for (int var7 = var6; var7 <= var5; var7++) {
            for (int var8 = 0; var8 < 16; var8++) {
               for (int var9 = 0; var9 < 16; var9++) {
                  z8.this.scanBlock(var1, var4.method_12254(var8, var7, var9), var8, var3 + var7, var9);
               }
            }
         }
      }

      @Override
      public void finish(int var1, int var2) {
         h5.n var3 = z8.this.evidence();
         long var4 = class_1923.method_8331(var1, var2);
         if (z8.this.J.r(var4, var3, z8.this.qualifies(var3), z8.this.a)) {
            z8.this.S(var4, var3);
         }
      }
   };

   protected z8(String var1, String var2) {
      super(var1, var2, Category.RENDER, -1);
      this.h.N(() -> true, () -> true, () -> true, () -> this.r.G("Filled"));
      this.c.a(var1x -> this.m("radius changed"));
      this.D.a(var1x -> this.m("rescan interval changed"));
      this.n.a(var1x -> {
         this.C.clear();
         this.e = 0;
      });
      this.p.O(this.n::getValue);
      this.S.O(this.n::getValue);
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1x -> this.c());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1x, var2x) -> {
         if (var1x == this.q) {
            class_1923 var3 = var2x.method_12004();
            this.J.c(var3.method_8324());
            this.b.a(var3.field_9181, var3.field_9180);
            this.C.removeIf(var1xx -> var1xx.key() == var3.method_8324());
         }
      });
   }

   protected abstract boolean candidate(class_2680 var1);

   protected abstract boolean qualifies(h5.n var1);

   protected abstract String describe(h5.n var1);

   protected abstract i2 alert(class_1923 var1, h5.n var2);

   protected abstract int tint();

   protected int minimumY() {
      return Integer.MIN_VALUE;
   }

   protected void beginScan() {
   }

   protected void beginChunk(class_2818 var1) {
   }

   protected void beforeScan() {
   }

   protected void afterScan() {
   }

   protected void beginDraw() {
   }

   protected int zoneRadius() {
      return 0;
   }

   protected final h5 i() {
      return this.J;
   }

   protected final long H() {
      return this.a;
   }

   protected final void w(long var1) {
      class_1923 var3 = new class_1923(var1);
      this.b.a(var3.field_9181, var3.field_9180);
      this.J.P(var1);
   }

   protected final void m(long var1, h5.n var3, boolean var4) {
      if (this.J.Q(var1, var3, var4, this.a)) {
         this.S(var1, var3);
      }
   }

   protected boolean sectionCandidate(class_2818 var1, int var2) {
      class_2826 var3 = var1.method_12006()[var2];
      return !var3.method_38292() && var3.method_19523(this::candidate);
   }

   protected void scanBlock(class_2818 var1, class_2680 var2, int var3, int var4, int var5) {
      this.count(var2, var3, var4, var5);
   }

   protected void clearEvidence() {
   }

   protected h5.n evidence() {
      return new h5.n(this.E, this.y, this.U, this.G);
   }

   public List<lb<?>> detectionSettings() {
      return List.of();
   }

   protected final int v() {
      return this.c.getValue().intValue();
   }

   public List<lb<?>> S() {
      return List.of(this.c, this.g, this.D);
   }

   public List<lb<?>> markerSettings() {
      return List.of(this.o, this.B, this.r, this.T, this.w);
   }

   public List<lb<?>> j() {
      return List.of(this.n, this.p, this.S);
   }

   protected int maximumY() {
      return Integer.MAX_VALUE;
   }

   protected abstract void count(class_2680 var1, int var2);

   protected void count(class_2680 var1, int var2, int var3, int var4) {
      this.count(var1, var3);
   }

   protected void drawEvidence(class_1923 var1, h5.n var2, l2 var3, int var4) {
   }

   protected final void K(int var1) {
      this.E++;
      if (this.G == Integer.MIN_VALUE) {
         this.G = var1;
      }
   }

   protected final void s() {
      this.y++;
   }

   protected final void d(int var1) {
      this.U++;
      if (this.G == Integer.MIN_VALUE) {
         this.G = var1;
      }
   }

   protected final void m(String var1) {
      if (this.J.v() > 0 || !this.C.isEmpty()) {
         q8.v("BaseFinding", "{} cleanup: {} ({} flags)", this.getName(), var1, this.J.d());
      }

      this.b.F();
      this.clearEvidence();
      this.J.V();
      this.C.clear();
      this.M.clear();
      wt.T().w(this.getName());
      this.K = List.of();
      this.e = 0;
      this.a = 0L;
   }

   @Override
   public void onEnable() {
      this.m("activation");
      this.q = getMc().field_1687;
      q8.G("BaseFinding", "{} active: received-data scan, radius {}", this.getName(), this.c.getValue().intValue());
   }

   @Override
   public void onDisable() {
      this.m("disable or world suspension");
      this.q = null;
      q8.v("BaseFinding", "{} released world scan state", this.getName());
   }

   private void S(long var1, h5.n var3) {
      class_1923 var4 = new class_1923(var1);
      q8.v("BaseFinding", "{} found chunk [{}, {}]: {}", this.getName(), var4.field_9181, var4.field_9180, this.describe(var3));
      if (this.n.getValue()) {
         if (this.C.size() < 64) {
            this.C.addLast(new z8.r(var1, var3));
         } else {
            this.e++;
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.q != mc.field_1687) {
            this.m("world changed");
            this.q = mc.field_1687;
         }

         this.a++;
         this.M.values().removeIf(var1x -> this.a - var1x >= 1200L);
         class_1923 var1 = mc.field_1724.method_31476();
         int var2 = this.c.getValue().intValue();
         int var3 = var1.field_9181;
         int var4 = var1.field_9180;
         this.J.P(var4x -> {
            int var6x = class_1923.method_8325(var4x);
            int var7 = class_1923.method_8332(var4x);
            return Math.abs((long)var6x - var3) <= var2 && Math.abs((long)var7 - var4) <= var2 && this.q.method_2935().method_21730(var6x, var7) != null;
         }, this.a);
         this.beforeScan();
         this.b.T(this.u, var3, var4, var2, this.g.getValue().intValue());
         this.afterScan();
         Map var5 = this.J.v();
         ArrayList var6 = new ArrayList(var5.size());

         for (Entry var8 : var5.entrySet()) {
            var6.add(new z8.p((Long)var8.getKey(), (h5.n)var8.getValue(), this.k((Long)var8.getKey())));
         }

         var6.sort(Comparator.comparingDouble(var0 -> var0.A));
         int var13 = Math.min(var6.size(), this.B.getValue().intValue());
         int var12 = var6.size();
         int var11 = var13;
         int var10001 = Integer.compare(var11, var12);
         this.K = (var10001 | -var10001) >>> 31 == 0 ? var6 : List.copyOf(var6.subList(0, var13));
         long var15;
         int var10000 = (var15 = this.a % 20L - 0L) == 0L ? 0 : (var15 < 0L ? -1 : 1);
         boolean var10 = false;
         int var9 = var10000;
         var10000 = Integer.signum(var9);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.f();
         }
      }
   }

   private double k(long var1) {
      double var3 = class_1923.method_8325(var1) * 16 + 8.0 - mc.field_1724.method_23317();
      double var5 = class_1923.method_8332(var1) * 16 + 8.0 - mc.field_1724.method_23321();
      return var3 * var3 + var5 * var5;
   }

   private void f() {
      if (this.n.getValue()) {
         Map var1 = this.J.E();

         while (!this.C.isEmpty()) {
            z8.r var2 = this.C.removeFirst();
            if (var1.containsKey(var2.key())) {
               class_1923 var3 = new class_1923(var2.key());
               c7.a var4 = c7.L(var1.keySet(), this.zoneRadius()).stream().filter(var1x -> var1x.members().contains(var2.key())).findFirst().orElseThrow();
               this.C.removeIf(var1x -> var4.members().contains(var1x.key()));
               boolean var5 = var4.members().stream().anyMatch(this.M::containsKey);
               if (this.M.size() + var4.members().size() > 1024) {
                  this.M.entrySet().stream().min(Entry.comparingByValue()).ifPresent(var1x -> this.M.remove(var1x.getKey()));
               }

               for (long var7 : var4.members()) {
                  if (this.M.size() < 1024) {
                     this.M.put(var7, this.a);
                  }
               }

               if (!var5) {
                  i2 var9 = this.alert(var3, var2.evidence());
                  if (var4.members().size() > 1) {
                     var9 = var9.zone((int)Math.round(var4.x()), (int)Math.round(var4.z()), var4.members().size());
                  }

                  double var10 = Math.hypot(var9.x() + 0.5 - mc.field_1724.method_23317(), var9.z() + 0.5 - mc.field_1724.method_23321());
                  wt.T().U(this.getName(), var9.summary(var10), wt.s.R);
                  if (this.p.getValue()) {
                     mc.method_44714()
                        .method_44736(
                           var9.chatLine(this.getName(), class_1923.method_8325(var2.key()), class_1923.method_8332(var2.key()), this.tint(), var10), false
                        );
                  }

                  if (this.S.getValue()) {
                     mc.field_1724.method_5783(class_3417.field_14627, 1.0F, 1.0F);
                  }

                  return;
               }
            }
         }

         if (this.e > 0) {
            wt.T().U(this.getName(), this.e + " additional discoveries; see chunk markers", wt.s.R);
            this.e = 0;
         }
      }
   }

   private void c() {
      if (this.isRuntimeActive() && this.isInGame() && this.q == mc.field_1687) {
         try {
            Map var1 = this.J.v();
            double var2 = Math.clamp(this.o.getValue(), this.q.method_31607(), this.q.method_31600() - 1);
            double var4 = Math.sqrt(2.0) * (this.c.getValue() + 1.0) * 16.0;
            class_243 var6 = mc.field_1773.method_19418().method_71156();
            int var7 = 0;
            this.beginDraw();

            for (z8.p var9 : this.K) {
               long var10 = var9.N;
               h5.n var12 = (h5.n)var1.get(var10);
               class_1923 var13 = new class_1923(var10);
               if (var12 != null && this.q.method_2935().method_21730(var13.field_9181, var13.field_9180) != null) {
                  double var14 = Math.sqrt(this.k(var10));
                  int var16 = this.h.C(this.tint(), var14, var4, 1.0, var13.hashCode());
                  class_238 var17 = new class_238(
                     var13.method_8326(), var2, var13.method_8328(), var13.method_8326() + 16, var2 + 0.35, var13.method_8328() + 16
                  );
                  this.drawEvidence(var13, var12, this.h, var16);
                  this.h.X(var17, this.r.getValue(), var16);
                  class_243 var18 = new class_243(var13.method_8326() + 8, var2 + 0.4, var13.method_8328() + 8);
                  if (this.w.getValue()) {
                     this.h.O(var6, var18, var16);
                  }

                  if (this.T.getValue() && var7++ < 24) {
                     this.h.n(var18.method_1031(0.0, 0.8, 0.0), this.W(var9, var12), var16);
                  }
               }
            }
         } catch (RuntimeException var19) {
            q8.O("BaseFinding", this.getName() + " marker rendering failed; scan remains active", var19);
         }
      }
   }

   private String W(z8.p var1, h5.n var2) {
      if (var1.J != var2) {
         return this.describe(var2);
      }

      if (var1.p == null) {
         var1.p = this.describe(var2);
      }

      return var1.p;
   }

   @Override
   public String getStatus() {
      return this.J.d() + " chunks";
   }

   private static final class p {
      final long N;
      final h5.n J;
      final double A;
      String p;

      p(long var1, h5.n var3, double var4) {
         this.N = var1;
         this.J = var3;
         this.A = var4;
      }
   }

   private record r(long key, h5.n evidence) {
   }
}
