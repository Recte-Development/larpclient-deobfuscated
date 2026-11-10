package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.FakeSpawnerModule;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_124;
import net.minecraft.class_1263;
import net.minecraft.class_1268;
import net.minecraft.class_1277;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1814;
import net.minecraft.class_1893;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2343;
import net.minecraft.class_2350;
import net.minecraft.class_2388;
import net.minecraft.class_2398;
import net.minecraft.class_243;
import net.minecraft.class_2480;
import net.minecraft.class_2487;
import net.minecraft.class_2498;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2586;
import net.minecraft.class_2636;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_3489;
import net.minecraft.class_3532;
import net.minecraft.class_3917;
import net.minecraft.class_3965;
import net.minecraft.class_465;
import net.minecraft.class_5218;
import net.minecraft.class_5250;
import net.minecraft.class_5251;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_7923;
import net.minecraft.class_7924;
import net.minecraft.class_9288;
import net.minecraft.class_9290;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import net.minecraft.class_2338.class_2339;
import net.minecraft.class_481.class_483;
import net.minecraft.class_6880.class_6883;

public final class cz {
   private static final cz h = new cz();
   private static final int Q = 45;
   private static final String I = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ";
   private static final long M = 1800L;
   private static final long TL = 650L;
   private static final int E = 64648;
   private static final int C = 16711680;
   private static final double q = 75.0;
   private final Map<Long, Long> i = new HashMap<>();
   private final Map<Long, Map<class_1792, Integer>> N = new HashMap<>();
   private final Map<Long, Integer> e = new ConcurrentHashMap<>();
   private final Set<Long> v = ConcurrentHashMap.newKeySet();
   private final Map<Long, cz.w> H = new HashMap<>();
   private final Map<Long, Deque<cz.v>> y = new HashMap<>();
   private final Map<Integer, class_1799> j = new HashMap<>();
   private final Map<Long, cz.f> d = new HashMap<>();
   private volatile Map<Long, class_2636> T1 = new ConcurrentHashMap<>();
   private final Long2ObjectMap<List<class_2636>> A = new Long2ObjectOpenHashMap();
   private final Map<Long, Long> r = new HashMap<>();
   private final Map<Long, Float> n = new HashMap<>();
   private final Map<Long, Integer> Tm = new HashMap<>();
   private int O;
   private final Set<Integer> p = new HashSet<>();
   private final Map<Integer, cz.p> P = new HashMap<>();
   private class_2338 Z;
   private int V = -1;
   private final Map<Long, Integer> TN = new HashMap<>();
   private final Map<Long, Double> Y = new HashMap<>();
   private final Map<Long, Integer> W = new ConcurrentHashMap<>();
   private final Set<Long> x = new HashSet<>();
   private final Map<class_1792, Double> w = new HashMap<>();
   private final Set<Long> U = ConcurrentHashMap.newKeySet();
   private volatile class_1937 G;
   private class_746 J;
   private class_1792 TW;
   private String X;
   private volatile Map<class_1792, qh> D = Map.of();
   private volatile LongSet T = LongSets.EMPTY_SET;
   private volatile LongSet c = LongSets.EMPTY_SET;
   private final Map<Long, Long> k = new ConcurrentHashMap<>();
   private final Map<Long, qh> u = new ConcurrentHashMap<>();
   private final Map<Long, cz.a> S = new HashMap<>();
   private final List<JsonElement> l = new ArrayList<>();
   private String a;
   private boolean K = (boolean)1;
   private long L;
   private long z;
   private long t;
   private long g;
   private long m;
   private long B;
   private final Set<class_1799> b = Collections.newSetFromMap(new IdentityHashMap<>());
   private final Set<class_1799> R = Collections.newSetFromMap(new WeakHashMap<>());
   private class_1268 F;
   private int f = -1;
   private int s = -1;
   private final Set<Long> o = ConcurrentHashMap.newKeySet();
   private long Tt;

   private static Path l() {
      return FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-piles.json");
   }

   void D(class_1263 var1) {
      this.b.clear();
      this.R.clear();

      for (int var2 = 0; var2 < var1.method_5439(); var2++) {
         class_1799 var3 = var1.method_5438(var2);
         if (!var3.method_7960()) {
            this.b.add(var3);
         }
      }
   }

   public class_1799 K(class_1799 var1, class_1799 var2) {
      if (!var2.method_7960() && (this.b.contains(var1) || this.R.contains(var1))) {
         this.R.add(var2);
         if (Boolean.getBoolean("larp.debug")) {
            this.m++;
         }
      }

      return var2;
   }

   public void P() {
      class_310 var1 = class_310.method_1551();
      if (var1.method_18854() && var1.field_1724 != null) {
         this.D(var1.field_1724.method_31548());
         if (!var1.field_1724.field_7512.method_34255().method_7960()) {
            this.b.add(var1.field_1724.field_7512.method_34255());
         }

         iy.V();
      }
   }

   public void D() {
      this.W();
      if (Boolean.getBoolean("larp.debug")) {
         this.z++;
      }
   }

   public void Z(int var1, int var2) {
      if (Boolean.getBoolean("larp.debug")) {
         this.t += var1;
         this.g += var2;
      }
   }

   public boolean B(class_1799 var1) {
      if (!this.J()) {
         return false;
      }

      class_746 var2 = class_310.method_1551().field_1724;
      return var2 != null && (var1 == var2.method_6047() && this.W(var2.method_31548().method_67532()) || var1 == var2.method_6079() && this.W(40));
   }

   public void R() {
      class_310 var1 = class_310.method_1551();
      if (var1 != null && !var1.method_18854()) {
         throw new IllegalStateException("Fake spawner reset must run on the client thread");
      }

      this.V();
      wu.k("controller cleanup");
      iy.j();
      this.b.clear();
      this.R.clear();
      this.T1 = new ConcurrentHashMap<>();
      this.A.clear();
      this.T = LongSets.EMPTY_SET;
      this.c = LongSets.EMPTY_SET;
      this.G = null;
      this.V = -1;
      this.F = null;
      this.s = -1;
      this.Tm.clear();
      this.O = 0;
      this.o.clear();
      this.p.clear();
      this.P.clear();
      this.TN.clear();
      this.Y.clear();
      this.W.clear();
      this.x.clear();
      this.w.clear();
      this.U.clear();

      for (Long var3 : new ArrayList<>(this.n.keySet())) {
         this.u(class_2338.method_10092(var3));
      }

      int var6 = this.e.isEmpty() && this.v.isEmpty() ? 0 : 1;
      this.e.clear();
      this.u.clear();
      this.S.clear();
      this.l.clear();
      this.a = null;
      this.v.clear();
      this.i.clear();
      this.N.clear();
      this.H.clear();
      this.y.clear();
      this.j.clear();

      for (long var4 : this.k.keySet()) {
         this.F(class_2338.method_10092(var4));
      }

      this.k.clear();
      this.c = LongSets.EMPTY_SET;
      this.d.clear();
      this.n.clear();
      this.r.clear();
      this.Z = null;
      this.J = null;
      this.TW = null;
      if (var1 != null) {
         if (var1.field_1755 instanceof h_ || var1.field_1755 instanceof fn) {
            var1.method_1507(null);
         }

         if (var6 != 0 && var1.field_1687 != null) {
            var1.field_1769.method_3279();
         }
      }
   }

   private void N() {
      class_310 var1 = class_310.method_1551();
      if (var1 != null && !var1.method_18854()) {
         throw new IllegalStateException("Fake spawner state must be updated on the client thread");
      }

      class_638 var2 = var1 == null ? null : var1.field_1687;
      class_746 var3 = var1 == null ? null : var1.field_1724;
      class_1792 var4 = this.V();
      if (var2 != this.G || var3 != this.J || !this.d().equals(this.X) || !this.J()) {
         this.R();
         this.G = var2;
         this.J = var3;
         this.TW = var4;
         this.X = this.d();
         HashMap var5 = new HashMap();
         FakeSpawnerModule var6 = FakeSpawnerModule.i();
         if (var6 != null) {
            for (qh var10 : qh.values()) {
               String var11 = var6.y(var10.ordinal());
               if (var11 != null && !var11.isBlank()) {
                  var5.put(g(var11), var10);
               }
            }
         }

         this.D = Map.copyOf(var5);
         if (var2 != null && var3 != null && this.J()) {
            this.a = this.x(var1);
            this.z();
            this.K = (boolean)1;
         }
      }
   }

   private String x(class_310 var1) {
      String var2 = var1.method_1558() != null
         ? var1.method_1558().field_3761
         : (var1.method_1576() != null ? "singleplayer:" + var1.method_1576().method_27050(class_5218.field_24188).toAbsolutePath().normalize() : null);
      return var2 == null ? null : var2 + "|" + var1.field_1687.method_27983().method_29177() + "|" + var1.field_1724.method_5667();
   }

   private void z() {
      if (this.a != null) {
         this.s(l());
      }
   }

   private void s(Path var1) {
      if (this.a != null && Files.exists(var1)) {
         try {
            JsonObject var2 = qu.v(var1);
            if (var2.has("quarantined") && var2.get("quarantined").isJsonObject() && var2.getAsJsonObject("quarantined").has(this.a)) {
               for (JsonElement var4 : var2.getAsJsonObject("quarantined").getAsJsonArray(this.a)) {
                  this.l.add(var4.deepCopy());
               }
            }

            if (!var2.has(this.a)) {
               return;
            }

            int var20 = 0;

            for (JsonElement var5 : var2.getAsJsonArray(this.a)) {
               try {
                  JsonObject var6 = var5.getAsJsonObject();
                  long var7 = var6.get("pos").getAsLong();
                  qh var9 = qh.valueOf(var6.get("kind").getAsString());
                  int var10 = var6.get("count").getAsInt();
                  long var11 = var6.get("placedAt").getAsLong();
                  ArrayList var13 = new ArrayList();

                  for (JsonElement var15 : var6.getAsJsonArray("backing")) {
                     JsonObject var16 = var15.getAsJsonObject();
                     var13.add(
                        new cz.s(
                           var16.get("slot").getAsInt(), var16.get("item").getAsString(), var16.get("amount").getAsInt(), var16.get("reserved").getAsInt()
                        )
                     );
                  }

                  HashMap var22 = new HashMap();
                  if (var6.has("purchased")) {
                     for (Entry var24 : var6.getAsJsonObject("purchased").entrySet()) {
                        int var17 = ((JsonElement)var24.getValue()).getAsInt();
                        if (var17 > 0) {
                           var22.put((String)var24.getKey(), var17);
                        }
                     }
                  }

                  if (var10 < 1 || var10 > 100000 || var10 > 1 && var13.isEmpty() || this.S.containsKey(var7)) {
                     throw new IllegalArgumentException("Invalid or conflicting saved pile");
                  }

                  this.S.put(var7, new cz.a(var7, var9, var10, var11, var13, var22));
               } catch (RuntimeException var18) {
                  var20++;
                  this.l.add(var5.deepCopy());
               }
            }

            if (var20 > 0) {
               q8.L("FakeSpawner", "Retaining {} unmatched pile records for recovery; no inventory was restored", var20);
            }
         } catch (Exception var19) {
            q8.u("FakeSpawner", "Pile load failed; retaining the file for recovery", var19);
         }
      }
   }

   private void V() {
      if (this.a != null) {
         this.F(l());
      }
   }

   private void F(Path var1) {
      if (this.a != null) {
         try {
            JsonObject var2 = Files.exists(var1) ? qu.v(var1) : new JsonObject();
            JsonArray var3 = new JsonArray();
            if (var2.has(this.a) && !var2.get(this.a).isJsonArray()) {
               throw new IOException("Unrecognized saved pile data; original file retained");
            }

            if (var2.has("quarantined") && !var2.get("quarantined").isJsonObject()) {
               throw new IOException("Unrecognized pile quarantine data; original file retained");
            }

            JsonObject var4 = var2.has("quarantined") ? var2.getAsJsonObject("quarantined") : new JsonObject();
            if (var4.has(this.a) && !var4.get(this.a).isJsonArray()) {
               throw new IOException("Unrecognized retained pile records; original file retained");
            }

            JsonArray var5 = new JsonArray();

            for (JsonElement var7 : this.l) {
               var5.add(var7.deepCopy());
            }

            if (!var5.isEmpty()) {
               var4.add(this.a, var5);
               var2.add("quarantined", var4);
            }

            for (cz.a var18 : this.S.values()) {
               var3.add(var18.toJson());
            }

            for (Entry var19 : this.e.entrySet()) {
               long var8 = (Long)var19.getKey();
               if (!this.S.containsKey(var8) && (Integer)var19.getValue() >= 1) {
                  Deque var10 = this.y.get(var8);
                  if (var10 == null) {
                     var10 = new ArrayDeque();
                  }

                  if ((Integer)var19.getValue() <= 1 || !var10.isEmpty()) {
                     ArrayList var11 = new ArrayList();

                     for (cz.v var13 : var10) {
                        class_1799 var14 = this.j.get(var13.preferredSlot);
                        if (var14 == null || var14.method_7960()) {
                           var11.clear();
                           break;
                        }

                        var11.add(
                           new cz.s(
                              var13.preferredSlot,
                              class_7923.field_41178.method_10221(var13.stack.method_7909()).toString(),
                              var13.stack.method_7947(),
                              var14.method_7947()
                           )
                        );
                     }

                     if (!var11.isEmpty() || (Integer)var19.getValue() == 1) {
                        HashMap var20 = new HashMap();
                        this.N.getOrDefault(var8, Map.of()).forEach((var1x, var2x) -> var20.put(class_7923.field_41178.method_10221(var1x).toString(), var2x));
                        var3.add(
                           new cz.a(
                                 var8,
                                 this.w(class_2338.method_10092(var8)),
                                 (Integer)var19.getValue(),
                                 this.i.getOrDefault(var8, System.currentTimeMillis()),
                                 var11,
                                 var20
                              )
                              .toJson()
                        );
                     }
                  }
               }
            }

            var2.add(this.a, var3);
            Files.createDirectories(var1.getParent());
            qu.d(var1, var2.toString());
         } catch (Exception var15) {
            q8.u("FakeSpawner", "Pile save failed; previous file retained", var15);
         }
      }
   }

   public void W() {
      this.K = (boolean)1;
   }

   private void Q(class_310 var1) {
      if (this.K && !this.S.isEmpty()) {
         this.K = (boolean)0;
         byte var2 = 0;

         for (cz.a var4 : new ArrayList<>(this.S.values())) {
            class_2338 var5 = class_2338.method_10092(var4.pos);
            if (var1.field_1687.method_22340(var5)) {
               class_2818 var6 = var1.field_1687.method_8500(var5);
               int var7 = var6.method_31602(var5.method_10264());
               class_2680 var8 = var7 >= 0 && var7 < var6.method_12006().length
                  ? var6.method_38259(var7).method_12254(var5.method_10263() & 15, var5.method_10264() & 15, var5.method_10260() & 15)
                  : class_2246.field_10124.method_9564();
               if (this.V(var8.method_26204().method_8389()) == var4.kind) {
                  HashMap var9 = new HashMap();

                  for (cz.s var11 : var4.backing) {
                     if ((var11.slot < 0 || var11.slot >= 36) && var11.slot != 40
                        || var11.amount < 1
                        || var11.reserved < 1
                        || var11.amount > var11.reserved + 1
                        || var11.reserved > 64
                        || var9.containsKey(var11.slot)
                        || this.W(var11.slot)) {
                        break;
                     }

                     class_1799 var12 = var1.field_1724.method_31548().method_5438(var11.slot);
                     if (var12.method_7960()
                        || var12.method_7947() != var11.reserved
                        || !class_7923.field_41178.method_10221(var12.method_7909()).toString().equals(var11.item)) {
                        break;
                     }

                     var9.put(var11.slot, var12.method_7972());
                  }

                  if (var9.size() == var4.backing.size()) {
                     int var15 = var4.backing.stream().mapToInt(var0 -> Math.min(var0.amount, var0.reserved)).sum();
                     if (var15 == var4.count - 1 || var15 == var4.count) {
                        ArrayDeque var16 = new ArrayDeque();
                        iy.d();

                        for (cz.s var13 : var4.backing) {
                           class_1799 var14 = (class_1799)var9.get(var13.slot);
                           this.j.put(var13.slot, var14);
                           var16.addLast(new cz.v(var13.slot, var14.method_46651(Math.min(var13.amount, var13.reserved))));
                        }

                        this.y.put(var4.pos, var16);
                        this.e.put(var4.pos, var4.count);
                        this.u.put(var4.pos, var4.kind);
                        this.i.put(var4.pos, var4.placedAt);
                        HashMap var18 = new HashMap();
                        var4.purchased.forEach((var1x, var2x) -> {
                           class_2960 var3 = class_2960.method_12829(var1x);
                           if (var3 != null && class_7923.field_41178.method_63535(var3) != class_1802.field_8162) {
                              var18.put((class_1792)class_7923.field_41178.method_63535(var3), var2x);
                           }
                        });
                        if (!var18.isEmpty()) {
                           this.N.put(var4.pos, var18);
                        }

                        this.S.remove(var4.pos);
                        this.i(var4.pos);
                        this.u(var1.field_1687, var5);
                        this.F(var5);
                        iy.d();
                        var2 = 1;
                     }
                  }
               }
            }
         }

         if (var2 != 0) {
            this.V();
         }
      }
   }

   private cz() {
   }

   public static cz T() {
      return h;
   }

   public boolean J() {
      FakeSpawnerModule var1 = FakeSpawnerModule.i();
      return var1 != null && var1.isEnabled() && var1.isRuntimeActive();
   }

   public boolean X(class_1799 var1) {
      return this.J() && this.b(var1);
   }

   public boolean b(class_1799 var1) {
      return this.P(var1) && (this.b.contains(var1) || this.R.contains(var1) || jn.Z(var1));
   }

   private boolean P(class_1799 var1) {
      return var1 != null && !var1.method_7960() && !t0.V(var1) && this.V(var1.method_7909()) != null;
   }

   private String d() {
      FakeSpawnerModule var1 = FakeSpawnerModule.i();
      return var1.y(0) + "|" + var1.y(1) + "|" + var1.y(2);
   }

   public qh V(class_1792 var1) {
      FakeSpawnerModule var2 = FakeSpawnerModule.i();

      for (qh var6 : qh.values()) {
         String var7 = var2.y(var6.ordinal());
         if (var7 != null && !var7.isBlank() && var1 == g(var7)) {
            return var6;
         }
      }

      return null;
   }

   public qh w(class_2338 var1) {
      return var1 == null ? qh.x : this.u.getOrDefault(var1.method_10063(), qh.x);
   }

   private class_1792 y(long var1) {
      return g(FakeSpawnerModule.i().y(this.w(class_2338.method_10092(var1)).ordinal()));
   }

   public class_1792 V() {
      FakeSpawnerModule var1 = FakeSpawnerModule.i();
      return g(var1 == null ? null : var1.x());
   }

   public static class_1792 g(String var0) {
      class_2960 var1 = var0 == null ? null : class_2960.method_12829(var0);
      class_1792 var2 = var1 == null ? class_1802.field_20412 : (class_1792)class_7923.field_41178.method_63535(var1);
      return var2 instanceof class_1747 var3
            && var2 != class_1802.field_8162
            && !var3.method_7711().method_9564().method_26215()
            && !(var3.method_7711() instanceof class_2343)
            && var3.method_7711() != class_2246.field_10260
         ? var2
         : class_1802.field_20412;
   }

   private class_2248 z() {
      return this.V() instanceof class_1747 var1 ? var1.method_7711() : class_2246.field_10445;
   }

   public boolean u(class_2680 var1) {
      return this.J() && var1 != null ? this.V(var1.method_26204().method_8389()) != null || var1.method_27852(class_2246.field_10260) : false;
   }

   public void E(class_2338 var1) {
      this.N();
      this.U.add(var1.method_10063());
      this.e.remove(var1.method_10063());
      this.i(var1.method_10063());
      this.F(var1);
   }

   public class_2680 G(class_2338 var1, class_2680 var2) {
      return var1 == null ? var2 : this.O(var1.method_10063(), var2);
   }

   public class_2680 O(long var1, class_2680 var3) {
      if (!this.T.contains(var1) || !this.m() || var3 == null || this.o.contains(var1)) {
         return var3;
      }

      if (this.W.containsKey(var1)) {
         return class_2246.field_10124.method_9564();
      }

      if (this.U.contains(var1)) {
         return var3;
      }

      if (!this.v.contains(var1) || !var3.method_27852(class_2246.field_10260) && !this.D.containsKey(var3.method_26204().method_8389())) {
         int var4 = this.e.getOrDefault(var1, 0);
         if (var4 <= 0) {
            return var3;
         } else {
            return !var3.method_26215() && !var3.method_27852(class_2246.field_10260) && !this.D.containsKey(var3.method_26204().method_8389())
               ? var3
               : class_2246.field_10260.method_9564();
         }
      } else {
         return class_2246.field_10124.method_9564();
      }
   }

   public boolean m() {
      return (!this.e.isEmpty() || !this.v.isEmpty() || !this.W.isEmpty()) && this.J() && this.G == class_310.method_1551().field_1687;
   }

   private void L() {
      LongOpenHashSet var1 = new LongOpenHashSet();

      for (long var3 : this.e.keySet()) {
         var1.add(var3);
      }

      for (long var9 : this.v) {
         var1.add(var9);
      }

      for (long var10 : this.W.keySet()) {
         var1.add(var10);
      }

      this.T = LongSets.unmodifiable(var1);
      LongOpenHashSet var8 = new LongOpenHashSet();

      for (long var4 : this.k.keySet()) {
         var8.add(var4);
      }

      this.c = LongSets.unmodifiable(var8);
   }

   public void v(class_2338 var1, class_2680 var2) {
      this.N();
      if (var1 != null) {
         long var3 = var1.method_10063();
         if (this.N(var3)) {
            this.O = 0;
            this.F(var1);
            q8.v("FakeSpawner", "Final break {} at {}; server state={}", var2 != null && var2.method_26215() ? "confirmed" : "corrected", var1, var2);
         }

         if (this.U.contains(var3)) {
            if (var2 != null && !var2.method_26215()) {
               return;
            }

            u2.B(var1);
            this.U.remove(var3);
         }

         if (this.v.contains(var3)) {
            if (var2 != null && this.u(var2)) {
               this.O = 0;
            }

            this.d.put(var3, new cz.f(var1.method_10062(), System.currentTimeMillis() + 10000L));
            this.v.remove(var3);
            this.F(var1);
         }

         int var5 = this.e.getOrDefault(var3, 0);
         if (var2 != null && !var2.method_26215()) {
            if (this.J()) {
               if (this.u(var2)) {
                  if (var5 <= 0 && !this.H.containsKey(var3)) {
                     return;
                  }

                  qh var6 = this.V(var2.method_26204().method_8389());
                  if (var6 != null && this.u.put(var3, var6) != var6) {
                     this.i(var3);
                  }

                  this.i.putIfAbsent(var3, System.currentTimeMillis());
                  cz.w var7 = this.H.remove(var3);
                  int var8 = var7 == null ? this.e.getOrDefault(var3, 1) : Math.max(1, var7.stackCount);
                  this.e.put(var3, Math.max(1, var8));
                  this.s(
                     "track:" + var3, 1500L, "trackWorldState fake pos={} state={} resolvedCount={} pending={}", var1, var2.method_26204(), var8, var7 != null
                  );
                  if (var7 != null && var7.hiddenStack != null) {
                     this.l(var3, var7.hiddenStack, true);
                  }

                  if (var7 != null) {
                     this.V();
                  }

                  this.u(class_310.method_1551() == null ? null : class_310.method_1551().field_1687, var1);
                  if (var5 <= 0) {
                     this.F(var1);
                  }
               } else {
                  this.v(var1, null);
               }
            }
         } else {
            if (var5 > 0) {
               this.C("trackWorldState removed pos={} previousCount={} state={}", var1, var5, var2);
            }

            if (var5 > 0 && !this.C(var1)) {
               this.I(var1);
            } else if (this.C(var1)) {
               this.W(var3, Integer.MAX_VALUE);
               iy.V();
               this.C("Placement rejected at {}; reservations released without inventory replay", var1);
            }

            this.i.remove(var3);
            this.N.remove(var3);
            this.e.remove(var3);
            this.H.remove(var3);
            this.y.remove(var3);
            this.i(var3);
            if (var5 > 0) {
               this.F(var1);
            }

            this.n.remove(var3);
            this.r.remove(var3);
            this.Y.remove(var3);
            this.u(var1);
            if (var1.equals(this.Z)) {
               this.Z = null;
            }
         }
      }
   }

   public void Q(class_2338 var1, class_1799 var2, int var3, boolean var4) {
      this.N();
      if (this.J() && var1 != null && this.b(var2)) {
         cz.a var5 = this.S.remove(var1.method_10063());
         if (var5 != null) {
            this.l.add(var5.toJson());
            this.C("New placement at {} archives a superseded saved pile for recovery", var1);
         }

         this.u.put(var1.method_10063(), this.V(var2.method_7909()));
         class_1799 var6 = var4 ? var2.method_7972() : class_1799.field_8037;
         int var7 = var4 ? var2.method_7947() : 1;
         this.H.put(var1.method_10063(), new cz.w(var7, new cz.v(var3, var6), System.currentTimeMillis()));
      }
   }

   public void Z(class_2338 var1, class_2680 var2) {
      this.N();
      cz.w var3 = this.H.get(var1.method_10063());
      if (var3 != null && this.u(var2)) {
         this.v.remove(var1.method_10063());
         this.e.put(var1.method_10063(), var3.stackCount);
         this.i.putIfAbsent(var1.method_10063(), var3.createdAtMs);
         this.u(this.G, var1);
         this.F(var1);
      }
   }

   public void Q() {
      class_310 var1 = class_310.method_1551();
      if (this.J() && var1.field_1724 != null) {
         for (Entry var3 : new ArrayList<>(this.H.entrySet())) {
            if (this.e.containsKey(var3.getKey())) {
               cz.w var4 = (cz.w)var3.getValue();
               if (var4.hiddenStack != null && !var4.hiddenStack.stack.method_7960()) {
                  this.l((Long)var3.getKey(), var4.hiddenStack, true);
               }

               this.e.put((Long)var3.getKey(), var4.stackCount);
            }
         }
      }
   }

   public void q() {
      class_310 var1 = class_310.method_1551();
      if (var1.method_18854() && var1.field_1724 != null && this.J() && var1.field_1687 == this.G && var1.field_1724 == this.J && !wu.e()) {
         this.b(var1.field_1724.method_31548());
         this.P
            .entrySet()
            .removeIf(
               var1x -> {
                  class_1799 var2 = var1.field_1724.method_31548().method_5438(var1x.getKey());
                  cz.p var3x = var1x.getValue();
                  return var1.field_1724.field_6012 >= var3x.expires
                     || !var2.method_7960() && !var2.method_31574(var3x.stack.method_7909())
                     || var2.method_31574(var3x.stack.method_7909()) && var2.method_7947() >= var3x.expected;
               }
            );

         for (Entry var3 : this.j.entrySet()) {
            if (!this.H.values().stream().anyMatch(var1x -> var1x.hiddenStack != null && var1x.hiddenStack.preferredSlot == (Integer)var3.getKey())) {
               class_1799 var4 = var1.field_1724.method_31548().method_5438((Integer)var3.getKey());
               if (var4.method_31574(((class_1799)var3.getValue()).method_7909())
                  && var4.method_7947() > ((class_1799)var3.getValue()).method_7947()
                  && this.s((Integer)var3.getKey(), var4.method_7947() - ((class_1799)var3.getValue()).method_7947())) {
                  break;
               }
            }
         }
      }
   }

   void b(class_1263 var1) {
      Iterator var2 = this.j.entrySet().iterator();

      while (var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         int var4 = (Integer)var3.getKey();
         class_1799 var5 = var1.method_5438(var4);
         class_1799 var6 = (class_1799)var3.getValue();
         if (var5.method_31574(var6.method_7909()) && var5.method_7947() >= var6.method_7947()) {
            this.Z(var4, var5);
         } else {
            int var7 = var5.method_31574(var6.method_7909()) ? var5.method_7947() : 0;
            int var8 = var6.method_7947() - var7;
            if (var7 == 0) {
               var2.remove();
            } else {
               var3.setValue(var5.method_46651(var7));
            }

            for (Entry var10 : this.y.entrySet()) {
               ArrayDeque var11 = new ArrayDeque();

               for (cz.v var13 : (Deque)var10.getValue()) {
                  int var14 = var13.preferredSlot == var4 ? Math.min(var8, var13.stack.method_7947()) : 0;
                  var8 -= var14;
                  if (var14 > 0) {
                     this.e.computeIfPresent((Long)var10.getKey(), (var1x, var2x) -> Math.max(1, var2x - var14));
                  }

                  if (var13.stack.method_7947() > var14) {
                     var11.addLast(new cz.v(var13.preferredSlot, var13.stack.method_46651(var13.stack.method_7947() - var14)));
                  }
               }

               ((Deque)var10.getValue()).clear();
               ((Deque)var10.getValue()).addAll(var11);
            }

            q8.v(
               "FakeSpawner",
               "Reservation corrected physical={} item={} old={} available={}; real inventory retained",
               var4,
               class_7923.field_41178.method_10221(var6.method_7909()),
               var6.method_7947(),
               var7
            );
            iy.V();
         }
      }
   }

   private boolean s(int var1, int var2) {
      class_310 var3 = class_310.method_1551();
      int var4 = this.T();
      return var4 >= 0 && var3.field_1724 != null
         ? wu.D("separate reservation surplus", wu.F(var1, var4, var2, var3.field_1724.method_31548().method_5438(var1).method_7947()), iy::V, iy::V)
         : false;
   }

   public boolean C(class_2338 var1) {
      return var1 != null && this.H.containsKey(var1.method_10063());
   }

   public boolean Y(class_2338 var1) {
      return this.J() && this.G == class_310.method_1551().field_1687 && var1 != null && this.v.contains(var1.method_10063());
   }

   public void R(class_1268 var1) {
      class_746 var2 = class_310.method_1551().field_1724;
      if (var2 != null) {
         this.F = var1;
         this.f = var2.method_31548().method_67532();
         this.s = var2.field_6012 + 4;
      }
   }

   public boolean H(class_1268 var1) {
      class_746 var2 = class_310.method_1551().field_1724;
      return this.J()
         && var2 == this.J
         && var1 == this.F
         && var2.field_6012 <= this.s
         && var2.method_31548().method_67532() == this.f
         && this.b(var2.method_5998(var1));
   }

   public void l(class_2338 var1) {
      class_310 var2 = class_310.method_1551();
      if (this.J() && var2.field_1687 != null && var2.field_1687.method_8320(var1).method_26215()) {
         if (this.k.put(var1.method_10063(), var2.field_1687.method_75260() + 10L) == null) {
            this.F(var1);
         }
      }
   }

   public class_2680 i(class_2338 var1, class_2680 var2) {
      return this.r(var1.method_10063(), var2);
   }

   public class_2680 r(long var1, class_2680 var3) {
      return this.c.contains(var1) && var3.method_26215() && this.J() && this.G == class_310.method_1551().field_1687
         ? class_2246.field_10260.method_9564()
         : var3;
   }

   public boolean T(class_2338 var1, class_1799 var2, int var3, boolean var4) {
      this.N();
      if (this.J()
         && var1 != null
         && !this.C(var1)
         && this.s(var1) > 0
         && this.b(var2)
         && this.V(var2.method_7909()) == this.w(var1)
         && !this.W(var3)
         && !wu.e()) {
         long var5 = var1.method_10063();
         int var7 = var4 ? var2.method_7947() : 1;
         if (var7 < var2.method_7947()) {
            int var8 = this.T();
            if (var8 < 0) {
               return false;
            }

            class_1792 var9 = var2.method_7909();
            return wu.D("single pile addition", wu.F(var3, var8, var7, var2.method_7947()), () -> {
               class_310 var6 = class_310.method_1551();
               class_1799 var7x = var6.field_1724.method_31548().method_5438(var8);
               if (this.e.containsKey(var5) && var7x.method_31574(var9) && var7x.method_7947() == var7) {
                  this.h(var5, var8, var7x, var7);
               }
            }, iy::V);
         } else {
            return this.h(var5, var3, var2, var7);
         }
      } else {
         return false;
      }
   }

   private boolean h(long var1, int var3, class_1799 var4, int var5) {
      if (!this.l(var1, new cz.v(var3, var4.method_7972()), false)) {
         return false;
      }

      int var6 = (int)Math.min(2147483647L, (long)this.e.getOrDefault(var1, 1).intValue() + var5);
      this.e.put(var1, var6);
      this.i.putIfAbsent(var1, System.currentTimeMillis());
      this.H(class_310.method_1551().field_1687, class_2338.method_10092(var1));
      this.f("You just added another spawner! There are now " + var6 + " spawners in the stack.");
      this.V();
      return true;
   }

   public boolean E() {
      class_746 var1 = class_310.method_1551().field_1724;
      if (var1 != null && var1.method_6047().method_31573(class_3489.field_42614)) {
         class_9304 var2 = (class_9304)var1.method_6047().method_58694(class_9334.field_49633);
         class_6883 var3 = var1.method_73183().method_30349().method_30530(class_7924.field_41265).method_46747(class_1893.field_9099);
         return var2 != null && var2.method_57536(var3) > 0;
      } else {
         return false;
      }
   }

   public boolean B(class_2338 var1, class_2350 var2) {
      class_310 var3 = class_310.method_1551();
      if (!this.z(var1)) {
         return false;
      }

      long var4 = var1.method_10063();
      if (this.W.containsKey(var4)) {
         return true;
      }

      if (this.E()) {
         this.TN.remove(var4);
         this.Y.remove(var4);
         this.x.remove(var4);
         return false;
      }

      if (this.x.contains(var4)) {
         return true;
      }

      int var6 = var3.field_1724.field_6012;
      long var7 = var1.method_10063();
      if (this.Tm.getOrDefault(var7, -1) == var6) {
         return true;
      }

      this.Tm.put(var7, var6);
      this.TN.computeIfAbsent(var4, var1x -> var6);
      double var9 = var3.field_1724.method_31549().field_7477 ? 1.0 : class_2246.field_10260.method_9564().method_26165(var3.field_1724, var3.field_1687, var1);
      double var11 = Math.min(1.0, this.Y.getOrDefault(var4, 0.0) + var9);
      this.Y.put(var4, var11);
      this.B(var3, var1, var2, var11);
      if (var11 >= 1.0) {
         this.TN.remove(var4);
         this.Y.remove(var4);
         this.x.add(var4);
         this.W.put(var4, var6 + 4);
         this.u(var1);
         this.n(var1, var2);
         this.v(var1, true);
         this.F(var1);
      }

      return true;
   }

   private void T() {
      class_310 var1 = class_310.method_1551();

      for (Entry var3 : new ArrayList<>(this.W.entrySet())) {
         if (var1.field_1724.field_6012 >= (Integer)var3.getValue()) {
            this.W.remove(var3.getKey());
            this.F(class_2338.method_10092((Long)var3.getKey()));
            this.f("You cannot break spawners without a silk touch!");
         }
      }

      if (!var1.field_1690.field_1886.method_1434()) {
         for (long var6 : this.TN.keySet()) {
            this.u(class_2338.method_10092(var6));
         }

         this.TN.clear();
         this.Y.clear();
         this.x.clear();
      } else {
         this.TN.keySet().removeIf(var2 -> {
            int var3x = var1.field_1765 instanceof class_3965 var4 && var4.method_17777().method_10063() == var2 ? 0 : 1;
            if (var3x != 0) {
               this.u(class_2338.method_10092(var2));
            }

            return (boolean)var3x;
         });
      }
   }

   public boolean e(class_2338 var1) {
      if (this.B(var1, null)) {
         return true;
      }

      class_310 var2 = class_310.method_1551();
      if (this.J() && var2 != null && var2.field_1687 != null && var2.field_1724 != null && var1 != null) {
         if (this.u(var2.field_1687.method_8320(var1)) && this.s(var1) > this.R()) {
            long var3 = var1.method_10063();
            this.n.putIfAbsent(var3, 0.0F);
            this.B(var2, var1, null, 0.0);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean j(class_2338 var1, class_2350 var2) {
      if (this.B(var1, var2)) {
         return true;
      }

      class_310 var3 = class_310.method_1551();
      if (this.J() && var3 != null && var3.field_1687 != null && var3.field_1724 != null && var1 != null) {
         if (this.z(var1) && var3.field_1724.field_6012 < this.O) {
            return true;
         }

         if (this.u(var3.field_1687.method_8320(var1)) && this.s(var1) > this.R()) {
            long var4 = var1.method_10063();
            int var6 = var3.field_1724.field_6012;
            if (this.Tm.getOrDefault(var4, -1) == var6) {
               return true;
            }

            this.Tm.put(var4, var6);
            float var7 = var3.field_1724.method_31549().field_7477
               ? 1.0F
               : class_2246.field_10260.method_9564().method_26165(var3.field_1724, var3.field_1687, var1);
            float var8 = Math.min(1.0F, this.n.getOrDefault(var4, 0.0F) + var7);
            this.n.put(var4, var8);
            this.B(var3, var1, var2, var8);
            if (var8 >= 1.0F) {
               if (this.B(var1, this.R())) {
                  this.O = var6 + 5;
                  this.v(var1, true);
               }

               this.n.remove(var4);
               this.r.remove(var4);
               this.u(var1);
            }

            return true;
         } else {
            if (var1 != null) {
               this.n.remove(var1.method_10063());
               this.r.remove(var1.method_10063());
               this.u(var1);
            }

            return false;
         }
      } else {
         if (var1 != null) {
            this.n.remove(var1.method_10063());
            this.r.remove(var1.method_10063());
            this.u(var1);
         }

         return false;
      }
   }

   public void I(class_2338 var1) {
      if (var1 != null && this.U.contains(var1.method_10063())) {
         u2.B(var1);
      }

      if (var1 != null && this.v.contains(var1.method_10063())) {
         this.d.put(var1.method_10063(), new cz.f(var1.method_10062(), System.currentTimeMillis() + 10000L));
      }

      if (var1 != null && this.s(var1) > 0) {
         this.d.put(var1.method_10063(), new cz.f(var1.method_10062(), System.currentTimeMillis() + 10000L));
         int var2 = this.e.getOrDefault(var1.method_10063(), 0);
         cz.v var3 = this.W(var1.method_10063(), var2);
         this.Z(var1.method_10063(), var2);
         this.G(new class_1799(this.y(var1.method_10063()), 1), 1);
         iy.V();
         this.n.remove(var1.method_10063());
         this.r.remove(var1.method_10063());
         this.u(var1);
      }
   }

   private int R() {
      class_310 var1 = class_310.method_1551();
      return var1.field_1724 == null || !var1.field_1724.method_5715() && !var1.field_1690.field_1832.method_1434() ? 1 : 64;
   }

   public boolean n(class_2338 var1) {
      return this.B(var1, 64);
   }

   private boolean B(class_2338 var1, int var2) {
      if (this.C(var1)) {
         return false;
      }

      if (this.z(var1)) {
         long var3 = var1.method_10063();
         int var5 = this.e.getOrDefault(var3, 0);
         if (var5 <= 0) {
            return false;
         }

         int var6 = Math.min(var2, var5);
         Deque var7 = this.y.get(var3);
         if (var7 != null && !var7.isEmpty()) {
            class_1799 var8 = class_310.method_1551().field_1724.method_31548().method_5438(((cz.v)var7.getLast()).preferredSlot);
            int var9 = 0;

            for (cz.v var11 : var7.reversed()) {
               if (!class_1799.method_31577(var8, class_310.method_1551().field_1724.method_31548().method_5438(var11.preferredSlot))) {
                  break;
               }

               var9 += var11.stack.method_7947();
            }

            if (var9 < var6 && var9 < var5 - 1) {
               var6 = var9;
            }
         }

         if (!this.g(var3, var6)) {
            return false;
         }

         this.Z(var3, var6);
         return true;
      } else {
         return false;
      }
   }

   private void Z(long var1, int var3) {
      int var4 = Math.max(0, this.e.getOrDefault(var1, 0) - var3);
      class_2338 var5 = class_2338.method_10092(var1);
      if (var4 > 0) {
         this.e.put(var1, var4);
         this.i.put(var1, System.currentTimeMillis());
      } else {
         this.v.add(var1);
         this.e.remove(var1);
         this.H.remove(var1);
         this.i.remove(var1);
         this.N.remove(var1);
         this.y.remove(var1);
         this.i(var1);
         this.F(var5);
         if (var5.equals(this.Z)) {
            this.Z = null;
         }
      }

      this.d.put(var1, new cz.f(var5.method_10062(), System.currentTimeMillis() + 10000L));
      this.V();
   }

   public boolean z(class_2338 var1) {
      if (var1 == null) {
         return false;
      }

      long var2 = var1.method_10063();
      return this.e.getOrDefault(var2, 0) > 0 && !this.o.contains(var2) && this.G == class_310.method_1551().field_1687 && this.J();
   }

   public cz.l y(class_2338 var1, class_3414 var2, float var3, float var4) {
      if (var1 != null && var2 != null && this.z(var1)) {
         class_2680 var5 = ((class_1747)this.y(var1.method_10063())).method_7711().method_9564();
         if (var5 == null) {
            return null;
         } else {
            class_2498 var6 = var5.method_26231();
            class_2498 var7 = class_2246.field_10260.method_9564().method_26231();
            if (H(var2, var6.method_10598())) {
               return new cz.l(var7.method_10598(), (var7.method_10597() + 1.0F) / 2.0F, var7.method_10599() * 0.8F);
            } else if (H(var2, var6.method_10595())) {
               return new cz.l(var7.method_10595(), (var7.method_10597() + 1.0F) / 2.0F, var7.method_10599() * 0.8F);
            } else {
               return H(var2, var6.method_10596()) ? new cz.l(var7.method_10596(), (var7.method_10597() + 1.0F) / 8.0F, var7.method_10599() * 0.5F) : null;
            }
         }
      } else {
         return null;
      }
   }

   private static boolean H(class_3414 var0, class_3414 var1) {
      return var0 == var1 || var0.comp_3319().equals(var1.comp_3319());
   }

   private void H(class_1937 var1, class_2338 var2) {
      if (var1 != null && var2 != null && this.z(var2)) {
         class_2498 var3 = class_2246.field_10260.method_9564().method_26231();
         var1.method_8486(
            var2.method_10263() + 0.5,
            var2.method_10264() + 0.5,
            var2.method_10260() + 0.5,
            var3.method_10598(),
            class_3419.field_15245,
            (var3.method_10597() + 1.0F) / 2.0F,
            var3.method_10599() * 0.8F,
            false
         );
      }
   }

   private void v(class_2338 var1, boolean var2) {
      class_310 var3 = class_310.method_1551();
      if (var3 != null && var3.field_1687 != null && var1 != null && this.z(var1)) {
         class_2498 var4 = class_2246.field_10260.method_9564().method_26231();
         var3.field_1687
            .method_8486(
               var1.method_10263() + 0.5,
               var1.method_10264() + 0.5,
               var1.method_10260() + 0.5,
               var2 ? var4.method_10595() : var4.method_10596(),
               class_3419.field_15245,
               (var4.method_10597() + 1.0F) / (var2 ? 2.0F : 8.0F),
               var4.method_10599() * (var2 ? 0.8F : 0.5F),
               false
            );
      }
   }

   public class_2680 Q(class_2338 var1, class_2680 var2) {
      return this.G(var1, var2);
   }

   public class_2636 u(class_1937 var1, class_2338 var2) {
      Map var3 = this.T1;
      if (var1 != null && var1 == this.G && this.z(var2) && !this.W.containsKey(var2.method_10063())) {
         long var4 = var2.method_10063();
         class_2636 var6 = (class_2636)var3.get(var4);
         if (var6 == null && class_310.method_1551().method_18854()) {
            var6 = new class_2636(var2.method_10062(), class_2246.field_10260.method_9564());
            var6.method_46408(this.w(var2).c, var1.method_8409());
            var6.method_31662(var1);
            var6.method_11390().method_8283(var1, var2);
            var3.put(var4, var6);
            ((List)this.A.computeIfAbsent(class_1923.method_8331(var2.method_10263() >> 4, var2.method_10260() >> 4), var0 -> new ArrayList())).add(var6);
         }

         return var3 == this.T1 && var1 == this.G && this.z(var2) ? var6 : null;
      } else {
         return null;
      }
   }

   private void F(class_2338 var1) {
      this.L();
      class_310 var2 = class_310.method_1551();
      if (var2 != null && var2.field_1687 != null && var2.field_1687 == this.G) {
         this.L++;
         var2.field_1769
            .method_18146(
               var1.method_10263() - 1,
               var1.method_10264() - 1,
               var1.method_10260() - 1,
               var1.method_10263() + 1,
               var1.method_10264() + 1,
               var1.method_10260() + 1
            );
      }
   }

   public Collection<class_2636> c(class_1937 var1) {
      return var1 == this.G && this.J() ? this.T1.values() : List.of();
   }

   private void i(long var1) {
      class_2636 var3 = this.T1.remove(var1);
      if (var3 != null) {
         class_2338 var4 = var3.method_11016();
         long var5 = class_1923.method_8331(var4.method_10263() >> 4, var4.method_10260() >> 4);
         List var7 = (List)this.A.get(var5);
         if (var7 != null) {
            var7.remove(var3);
            if (var7.isEmpty()) {
               this.A.remove(var5);
            }
         }
      }
   }

   public Collection<class_2636> O(class_1937 var1, class_243 var2, int var3) {
      if (var1 == this.G && this.J() && !this.A.isEmpty()) {
         ArrayList var4 = null;
         int var5 = class_3532.method_15357(var2.field_1352 - var3) >> 4;
         int var6 = class_3532.method_15357(var2.field_1352 + var3) >> 4;
         int var7 = class_3532.method_15357(var2.field_1350 - var3) >> 4;
         int var8 = class_3532.method_15357(var2.field_1350 + var3) >> 4;

         for (int var9 = var5; var9 <= var6; var9++) {
            for (int var10 = var7; var10 <= var8; var10++) {
               List var11 = (List)this.A.get(class_1923.method_8331(var9, var10));
               if (var11 != null) {
                  if (var4 == null) {
                     var4 = new ArrayList();
                  }

                  var4.addAll(var11);
               }
            }
         }

         return var4 == null ? List.of() : var4;
      } else {
         return List.of();
      }
   }

   public boolean S(class_2586 var1) {
      if (var1 instanceof class_2636 var2) {
         class_2338 var3 = var2.method_11016();
         if (var3 == null) {
            return false;
         }

         class_2636 var4 = this.T1.get(var3.method_10063());
         return var4 == var1;
      } else {
         return false;
      }
   }

   public void T(String var1, class_2338 var2, class_2586 var3) {
      if (Boolean.getBoolean("larp.debug")) {
         if (var2 != null) {
            this.s(
               "request:" + var1 + ":" + var2.method_10063(),
               1500L,
               "{} requested fake block entity pos={} result={} count={}",
               var1,
               var2,
               var3 != null ? var3.getClass().getSimpleName() : "null",
               this.s(var2)
            );
         }
      }
   }

   public void v(String var1, class_2338 var2, String var3) {
      if (Boolean.getBoolean("larp.debug")) {
         if (var2 == null) {
            this.s("render:" + var1, 1500L, "renderer {} {}", var1, var3);
         } else {
            this.s("render:" + var1 + ":" + var2.method_10063(), 1500L, "renderer {} pos={} count={} {}", var1, var2, this.s(var2), var3);
         }
      }
   }

   public void a(class_2818 var1, Collection<class_2338> var2) {
      this.N();
      this.W();
      if (this.J() && var1 != null) {
         int var3 = var1.method_12004().field_9181;
         int var4 = var1.method_12004().field_9180;
         int var5 = var3 << 4;
         int var6 = var4 << 4;
         int var7 = var5 + 15;
         int var8 = var6 + 15;
         class_2339 var9 = new class_2339();
         HashSet var10 = new HashSet<>(this.e.keySet());
         var10.addAll(this.v);

         for (Long var12 : var10) {
            class_2338 var13 = class_2338.method_10092(var12);
            int var14 = var13.method_10263();
            int var15 = var13.method_10260();
            if (var14 >= var5 && var14 <= var7 && var15 >= var6 && var15 <= var8) {
               var9.method_10103(var14, var13.method_10264(), var15);
               if (!var1.method_12200().method_31606(var9)) {
                  this.v(
                     var9,
                     var1.method_38259(var1.method_31602(var9.method_10264()))
                        .method_12254(var9.method_10263() & 15, var9.method_10264() & 15, var9.method_10260() & 15)
                  );
               }
            }
         }

         if (var2 != null) {
            for (class_2338 var17 : new ArrayList(var2)) {
               if (var17 != null) {
                  this.v(var17, var1.method_8320(var17));
               }
            }
         }
      }
   }

   public void m() {
      long var1 = Boolean.getBoolean("larp.debug") ? System.nanoTime() : 0L;
      this.N();
      class_310 var3 = class_310.method_1551();
      if (var3 != null && var3.field_1687 != null && var3.field_1724 != null && this.J()) {
         this.Q(var3);
         this.P();

         for (Entry var5 : this.k.entrySet()) {
            if (var3.field_1687.method_75260() >= (Long)var5.getValue()) {
               this.k.remove(var5.getKey());
               this.F(class_2338.method_10092((Long)var5.getKey()));
            }
         }

         this.T();
         if (!this.d.isEmpty() || !this.n.isEmpty() || !this.v.isEmpty()) {
            for (class_1297 var6 : var3.field_1687.method_18112()) {
               if (var6 instanceof class_1542 var8) {
                  this.Q(var8);
               }
            }
         }

         this.U();
         this.d.entrySet().removeIf(var0 -> var0.getValue().untilMs < System.currentTimeMillis());
         this.p.removeIf(var1x -> !(var3.field_1687.method_8469(var1x) instanceof class_1542));
         wu.d();
         this.q();

         for (Entry var17 : new ArrayList<>(this.H.entrySet())) {
            if (!this.e.containsKey(var17.getKey()) && System.currentTimeMillis() - ((cz.w)var17.getValue()).createdAtMs > 5000L) {
               this.v(class_2338.method_10092((Long)var17.getKey()), null);
            }
         }

         if (!var3.field_1690.field_1886.method_1434()) {
            for (Long var18 : new ArrayList<>(this.n.keySet())) {
               this.u(class_2338.method_10092(var18));
            }

            this.n.clear();
            this.r.clear();
         }

         for (Long var19 : new ArrayList<>(this.n.keySet())) {
            if (!(var3.field_1765 instanceof class_3965 var22 && var22.method_17777().equals(class_2338.method_10092(var19)))) {
               this.u(class_2338.method_10092(var19));
               this.n.remove(var19);
               this.r.remove(var19);
            }
         }

         if (var3 != null && var3.field_1687 != null && var3.field_1724 != null && this.J()) {
            for (class_2586 var21 : this.O(var3.field_1687, var3.field_1724.method_73189(), 32)) {
               if (var21 instanceof class_2636 var23
                  && var23.method_11016() != null
                  && var3.field_1687.method_22340(var23.method_11016())
                  && this.z(var23.method_11016())
                  && var23.method_11016().method_19770(var3.field_1724.method_73189()) <= 1024.0) {
                  class_2636.method_31696(var3.field_1687, var23.method_11016(), var23.method_11010(), var23);
               }
            }

            long var14 = var3.field_1687.method_75260();

            for (class_2338 var26 : this.F(var3.field_1724.method_24515(), 16, 8)) {
               if ((var14 + Math.floorMod(var26.method_10063(), 3L)) % 20L == 0L) {
                  this.E(var3.field_1687, var26);
               }
            }
         } else {
            for (Long var20 : new ArrayList<>(this.n.keySet())) {
               this.u(class_2338.method_10092(var20));
            }

            this.n.clear();
            this.r.clear();
         }

         if (var1 != 0L) {
            long var15 = System.nanoTime() - var1;
            long var25 = System.currentTimeMillis();
            if (var25 >= this.B) {
               this.B = var25 + 5000L;
               this.C(
                  "Work counters: piles={} reservations={} mappingRebuilds={} terrainInvalidations={} inventoryUpdates={} renderCandidates={} renderExtractions={} displayCopies={} tickMicros={}",
                  this.e.size(),
                  this.j.size(),
                  iy.g(),
                  this.L,
                  this.z,
                  this.t,
                  this.g,
                  this.m,
                  var15 / 1000L
               );
            }

            if (var15 > 5000000L) {
               this.s("tick-spike", 2000L, "Client tick CPU spike micros={} piles={} reservations={}", var15 / 1000L, this.e.size(), this.j.size());
            }
         }
      } else {
         this.R();
      }
   }

   public int s(class_2338 var1) {
      return !this.z(var1) ? 0 : this.e.getOrDefault(var1.method_10063(), 0);
   }

   public List<class_2338> F(class_2338 var1, int var2, int var3) {
      ArrayList var4 = new ArrayList();
      if (this.J() && var1 != null && !this.e.isEmpty()) {
         int var5 = var1.method_10263();
         int var6 = var1.method_10264();
         int var7 = var1.method_10260();

         for (class_2636 var9 : this.O(this.G, class_243.method_24953(var1), var2 + 1)) {
            class_2338 var10 = var9.method_11016();
            if (this.z(var10)
               && Math.abs(var10.method_10263() - var5) <= var2
               && Math.abs(var10.method_10264() - var6) <= var3
               && Math.abs(var10.method_10260() - var7) <= var2) {
               var4.add(var10);
            }
         }

         return var4;
      } else {
         return var4;
      }
   }

   public boolean Q(class_1542 var1) {
      if (this.J() && var1 != null && this.P(var1.method_6983()) && !jn.N(var1.method_6983())) {
         long var2 = System.currentTimeMillis();
         if (this.p.contains(var1.method_5628())) {
            return true;
         }

         HashSet var4 = new HashSet<>(this.v);
         var4.addAll(this.n.keySet());

         for (long var6 : var4) {
            class_2338 var8 = class_2338.method_10092(var6);
            if (var1.method_5649(var8.method_10263() + 0.5, var8.method_10264() + 0.5, var8.method_10260() + 0.5) <= 4.84) {
               this.p.add(var1.method_5628());
               return true;
            }
         }

         this.d.entrySet().removeIf(var2x -> var2x.getValue().untilMs < var2);

         for (cz.f var14 : this.d.values()) {
            double var7 = var14.pos.method_10263() + 0.5 - var1.method_23317();
            double var9 = var14.pos.method_10264() + 0.5 - var1.method_23318();
            double var11 = var14.pos.method_10260() + 0.5 - var1.method_23321();
            if (var7 * var7 + var9 * var9 + var11 * var11 <= 4.840000000000001) {
               this.p.add(var1.method_5628());
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean D(class_1542 var1) {
      return this.Q(var1);
   }

   public class_1799 i(class_1799 var1) {
      if (!this.X(var1)) {
         return var1;
      }

      class_1799 var2 = new class_1799(class_1802.field_8849, var1.method_7947());
      var2.method_57379(class_9334.field_49631, this.Z(var1));
      var2.method_57379(class_9334.field_49632, new class_9290(List.of(J(this.V(var1.method_7909()).g, class_124.field_1054))));
      if (this.p()) {
         var2.method_57379(class_9334.field_49641, true);
      }

      return var2;
   }

   public class_1799 S(class_1799 var1) {
      return var1;
   }

   public boolean G(class_1799 var1) {
      return this.J() && var1 != null && !var1.method_7960() && !t0.V(var1) && this.j(var1) && this.R(var1) > 0L;
   }

   public class_2561 E(long var1) {
      return J("~", class_124.field_1063).method_10852(o("$ ", 65280)).method_10852(J(this.s(var1), class_124.field_1068));
   }

   public class_2561 R(class_1799 var1) {
      return this.r(this.L(var1));
   }

   public class_2561 u(class_1799 var1) {
      long var2 = this.R(var1);
      long var4 = Math.max(1L, (var2 + 63L) / 64L);
      return J("Contains: " + var4 + " stack(s)", class_124.field_1068);
   }

   public class_2561 o() {
      return J("Left Shift", class_124.field_1065).method_10852(J(": ", class_124.field_1065)).method_10852(J("view contents", class_124.field_1068));
   }

   public class_2561 M() {
      return J("Left Alt + Left Shift", class_124.field_1065)
         .method_10852(J(": ", class_124.field_1065))
         .method_10852(J("view contents", class_124.field_1068));
   }

   public class_2561 Q() {
      return J("Left Control", class_124.field_1065).method_10852(J(": ", class_124.field_1065)).method_10852(J("lock tooltip", class_124.field_1068));
   }

   public List<class_2561> Y(class_1799 var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      var3.add(var1.method_7964());
      var3.add(this.R(var1));
      if (!var2) {
         return var3;
      } else {
         boolean var4 = this.z(341);
         boolean var5 = this.z(340);
         boolean var6 = this.z(342);
         if (var4) {
            return var3;
         } else if (var5 && var6) {
            var3.add(this.Q());
            return var3;
         } else if (var5) {
            var3.add(this.u(var1));
            var3.add(this.M());
            var3.add(this.Q());
            return var3;
         } else {
            var3.add(this.u(var1));
            var3.add(this.o());
            return var3;
         }
      }
   }

   public class_2561 J() {
      FakeSpawnerModule var1 = FakeSpawnerModule.i();
      String var2 = var1 != null ? var1.H() : "§dSpawner";
      return J(var2);
   }

   public class_2561 Z(class_1799 var1) {
      class_2561 var2 = (class_2561)var1.method_58694(class_9334.field_49631);
      if (var2 != null) {
         return class_2561.method_43470(var2.getString()).method_27695(new class_124[]{class_124.field_1068, class_124.field_1056});
      }

      qh var3 = this.V(var1.method_7909());
      return J("Spawner", class_124.field_1076);
   }

   public List<class_2561> V(int var1, boolean var2, boolean var3) {
      return this.v(new class_1799(this.V(), var1), var2, var3);
   }

   public List<class_2561> v(class_1799 var1, boolean var2, boolean var3) {
      int var4 = var1.method_7947();
      ArrayList var5 = new ArrayList();
      var5.add(J(this.V(var1.method_7909()).g, class_124.field_1054));
      if (var2) {
         var5.add(J("~", class_124.field_1063).method_10852(o("$ ", 65280)).method_10852(J(this.W((long)var4), class_124.field_1068)));
      }

      var5.add(class_2561.method_43473());
      var5.add(J("Interact with Spawn Egg:", class_124.field_1080));
      var5.add(J(" Sets Mob Type", class_124.field_1078));
      if (var3) {
         var5.add(J("minecraft:spawner", class_124.field_1063));
         var5.add(J("13 component(s)", class_124.field_1063));
      }

      return var5;
   }

   public boolean q() {
      class_310 var1 = class_310.method_1551();
      if (var1 == null) {
         return false;
      } else if (var1.field_1755 instanceof class_465 var2) {
         class_2561 var4 = var2.method_25440();
         return var4 == null ? false : var4.getString().toLowerCase(Locale.ROOT).contains("shulker");
      } else {
         return false;
      }
   }

   public boolean p() {
      FakeSpawnerModule var1 = FakeSpawnerModule.i();
      return var1 != null && var1.x();
   }

   public void n() {
      this.S(this.Z);
   }

   public void S(class_2338 var1) {
      this.N();
      class_310 var2 = class_310.method_1551();
      if (this.J() && var2 != null && var2.field_1687 != null && var2.field_1724 != null) {
         this.Z = var1 == null ? null : var1.method_10062();
         long var3 = this.o(var1);
         int var5 = this.p(var1);
         qh var6 = this.w(var1);
         cz.c var7 = this.I(var3, var5, var6);
         Map var8 = var1 == null ? Map.of() : this.N.getOrDefault(var1.method_10063(), Map.of());
         var7 = new cz.c(
            Math.max(0, var7.V - var8.getOrDefault(var6.E, 0) - iv.I(var6.E)),
            Math.max(0, var7.f - var8.getOrDefault(var6.S, 0) - iv.I(var6.S)),
            var7.g,
            var7.m
         );
         String var9 = var5 + " " + p(var6.g.toLowerCase(Locale.ROOT) + " spawner" + (var5 == 1 ? "" : "s"));
         class_1277 var10 = new class_1277(54);
         int var11 = 0;
         var11 = J(var10, var11, var6.E, var7.V);
         J(var10, var11, var6.S, var7.f);
         var10.method_5447(
            48,
            M(
               class_1802.field_8695.method_7854(),
               J("Sell All", class_124.field_1068),
               List.of(J("Click to sell all loot", class_124.field_1080).method_27692(class_124.field_1056))
            )
         );
         var10.method_5447(49, M(var6.u(), o(var5 + " " + var6.g.toLowerCase(Locale.ROOT) + " spawner" + (var5 == 1 ? "" : "s"), 64648), this.x(var7, var6)));
         var10.method_5447(
            50,
            M(
               class_1802.field_8878.method_7854(),
               J("Drop Loot", class_124.field_1068),
               List.of(J("Click to drop all loot on the page", class_124.field_1080).method_27692(class_124.field_1056))
            )
         );
         class_1707 var12 = new class_1707(class_3917.field_17327, 0, var2.field_1724.method_31548(), var10, 6);
         var2.method_1507(new h_(var12, var2.field_1724.method_31548(), class_2561.method_43470(var9), var1));
      }
   }

   public void R(class_2338 var1, class_1792 var2, int var3) {
      if (var1 != null && var2 != null && var3 > 0) {
         this.N.computeIfAbsent(var1.method_10063(), var0 -> new HashMap<>()).merge(var2, var3, Integer::sum);
      }
   }

   public long L(class_1799 var1) {
      if (!this.J() || var1.method_7960() || t0.V(var1)) {
         return 0L;
      }

      if (this.P(var1)) {
         return var1.method_7947() * 1000L;
      }

      if (qh.I(var1.method_7909())) {
         return this.x(var1.method_7909(), var1.method_7947());
      }

      class_9288 var2 = (class_9288)var1.method_58694(class_9334.field_49622);
      return var2 == null ? 0L : StreamSupport.<class_1799>stream(var2.method_59715().spliterator(), false).mapToLong(this::L).sum() * var1.method_7947();
   }

   public class_2561 r(long var1) {
      double var3 = var1 >= 1000000000L ? 1.0E9 : (var1 >= 1000000L ? 1000000.0 : (var1 >= 1000L ? 1000.0 : 1.0));
      String var5 = var3 == 1.0E9 ? "B" : (var3 == 1000000.0 ? "M" : (var3 == 1000.0 ? "K" : ""));
      return J("~", class_124.field_1063).method_10852(o("$ ", 65280)).method_10852(J(H(var1 / var3, var3 == 1.0 ? 0 : 1) + var5, class_124.field_1068));
   }

   public long k(int var1) {
      return this.x(class_1802.field_8606, var1);
   }

   public long x(class_1792 var1, int var2) {
      Double var3 = FakeSpawnerModule.i().E(class_7923.field_41178.method_10221(var1).toString());
      if (var3 != null) {
         return Math.round(var3 * Math.max(0, var2));
      }

      String var4 = class_7923.field_41178.method_10221(var1).method_12832().replace('_', ' ');
      s3 var5 = s3.T();
      var5.w(var4, var0 -> {});
      double var6 = this.w.getOrDefault(var1, (double)var5.a(var4));
      return Math.round(var6 * Math.max(0, var2));
   }

   public void U() {
      class_310 var1 = class_310.method_1551();
      if (var1.field_1724 != null && var1.field_1755 != null && var1.field_1755.method_25440().getString().toLowerCase(Locale.ROOT).contains("order")) {
         class_1703 var2 = var1.field_1724.field_7512;
         HashMap var3 = new HashMap();

         for (class_1735 var5 : var2.field_7761) {
            if (var5.field_7871 != var1.field_1724.method_31548()) {
               class_1799 var6 = var5.method_7677();
               if (qh.I(var6.method_7909())) {
                  class_9290 var7 = (class_9290)var6.method_58694(class_9334.field_49632);
                  if (var7 != null) {
                     for (class_2561 var9 : var7.comp_2400()) {
                        String var10 = var9.getString().toLowerCase(Locale.ROOT);
                        if (var10.contains("each") || var10.contains("per item") || var10.contains("unit price")) {
                           Matcher var11 = Pattern.compile("\\$\\s*([0-9][0-9,]*(?:\\.[0-9]+)?)\\s*([kmb]?)").matcher(var10);
                           if (var11.find()) {
                              double var12 = Double.parseDouble(var11.group(1).replace(",", ""));

                              var12 *= switch (var11.group(2)) {
                                 case "k" -> 1000;
                                 case "m" -> 1000000;
                                 case "b" -> 1000000000;
                                 default -> 1;
                              };
                              if (Double.isFinite(var12) && var12 > 0.0) {
                                 var3.merge(var6.method_7909(), var12, Math::max);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         var3.forEach((var1x, var2x) -> this.w.merge(var1x, var2x, Math::max));
      }
   }

   public int q() {
      return this.O(class_1802.field_8606);
   }

   private boolean f(class_1799 var1) {
      if (var1.method_57826(class_9334.field_49631)) {
         return false;
      }

      FakeSpawnerModule var2 = FakeSpawnerModule.i();

      for (qh var6 : qh.values()) {
         String var7 = var2.y(var6.ordinal());
         if (var7 != null && !var7.isBlank() && var1.method_31574(var6.E)) {
            return true;
         }
      }

      return false;
   }

   private int O(class_1792 var1) {
      class_310 var2 = class_310.method_1551();
      if (this.J() && var2.field_1724 != null) {
         int var3 = -1;
         int var4 = 0;

         for (int var5 = 0; var5 < 36; var5++) {
            class_1799 var6 = var2.field_1724.method_31548().method_5438(var5);
            if (var6.method_31574(var1) && !var6.method_57826(class_9334.field_49631) && !wa.x(var6) && var6.method_7947() > var4) {
               var3 = var5;
               var4 = var6.method_7947();
            }
         }

         return this.V = var3;
      } else {
         return -1;
      }
   }

   public void e(int var1) {
   }

   public void q(class_1703 var1, class_1735 var2) {
   }

   public void C(class_1735 var1) {
   }

   public void m(class_2338 var1) {
      if (var1 != null && this.I(var1.method_10063())) {
         this.u(var1);
         this.F(var1);
         q8.v("FakeSpawner", "Final break predicted at {}; retaining pile and reservations until server acknowledgment", var1);
      }
   }

   boolean I(long var1) {
      return this.e.getOrDefault(var1, 0) > 0 && this.o.add(var1);
   }

   boolean N(long var1) {
      return this.o.remove(var1);
   }

   public class_2680 Z(class_1937 var1, class_2338 var2, class_2680 var3) {
      if (this.J() && var1 == this.G && this.e.getOrDefault(var2.method_10063(), 0) > 0 && var1 instanceof class_638 var4) {
         if (var1.method_31606(var2)) {
            return class_2246.field_10124.method_9564();
         }

         class_2818 var5 = var4.method_8500(var2);
         return var5.method_38259(var5.method_31602(var2.method_10264()))
            .method_12254(var2.method_10263() & 15, var2.method_10264() & 15, var2.method_10260() & 15);
      } else {
         return var3;
      }
   }

   public void u(Runnable var1) {
      class_310 var2 = class_310.method_1551();
      if (this.J() && var2.field_1724 != null && var2.method_18854()) {
         try {
            this.r(var2.field_1724.method_31548(), var1);
         } finally {
            this.P();
         }
      } else {
         var1.run();
      }
   }

   void r(class_1263 var1, Runnable var2) {
      try {
         var2.run();
      } finally {
         iy.V();
      }
   }

   private void Z(int var1, class_1799 var2) {
      class_1799 var3 = this.j.get(var1);
      if (var3 != null && var2.method_31574(var3.method_7909()) && var2.method_7947() >= var3.method_7947() && !class_1799.method_31577(var2, var3)) {
         this.j.put(var1, var2.method_46651(var3.method_7947()));

         for (Deque var5 : this.y.values()) {
            ArrayDeque var6 = new ArrayDeque();

            for (cz.v var8 : var5) {
               var6.addLast(
                  var8.preferredSlot == var1 && var8.stack.method_31574(var2.method_7909())
                     ? new cz.v(var1, var2.method_46651(var8.stack.method_7947()))
                     : var8
               );
            }

            var5.clear();
            var5.addAll(var6);
         }
      }
   }

   public boolean H(class_1799 var1) {
      return this.J() && !var1.method_7960() && this.j.values().stream().anyMatch(var1x -> class_1799.method_31577(var1, var1x));
   }

   public boolean J(int var1, int var2, int var3, class_1713 var4) {
      if (!this.J() || wu.C()) {
         return true;
      }

      if (wu.e()) {
         return false;
      }

      class_746 var5 = class_310.method_1551().field_1724;
      return var5 != null && var5.field_7512.field_7763 == var1 ? this.n(var5.field_7512, var2, var3, var4) : true;
   }

   public boolean n(class_1703 var1, int var2, int var3, class_1713 var4) {
      if (!this.J() || wu.C()) {
         return true;
      }

      if (wu.e()) {
         return false;
      }

      class_746 var5 = class_310.method_1551().field_1724;
      if (var5 == null) {
         return true;
      }

      if (var1 != var5.field_7512 && var1 != var5.field_7498) {
         return false;
      }

      if (var2 >= 0 && var2 < var1.field_7761.size()) {
         class_1735 var6 = var1.method_7611(var2);
         if (var6.field_7871 == var5.method_31548() && (this.W(var6.method_34266()) || iv.m(var6.method_34266()) || this.V(var6.method_34266()))) {
            return false;
         }
      }

      return var4 != class_1713.field_7791 || !this.W(var3) && !iv.m(var3) && !this.V(var3)
         ? var4 != class_1713.field_7793 || !this.H(var1.method_34255())
         : false;
   }

   public void Y(int var1, Runnable var2) {
      this.u(var2);
   }

   public boolean q(int var1, int var2) {
      class_310 var3 = class_310.method_1551();
      class_1799 var4 = this.j.get(var2);
      if (var4 == null || var4.method_7960()) {
         return false;
      }

      if (var3.field_1724 != null
         && var3.field_1761 != null
         && var1 >= 9
         && var1 < 36
         && !this.W(var1)
         && var3.field_1724.field_7498.method_34255().method_7960()
         && var3.field_1724.field_7512 == var3.field_1724.field_7498) {
         if (this.f(var3.field_1724.method_31548().method_5438(var1))) {
            this.e(var2);
         }

         wu.q(() -> var3.field_1761.method_2906(0, var1, var2, class_1713.field_7791, var3.field_1724));
         if (var4 != null && !var4.method_7960()) {
            this.j.remove(var2);
            this.j.put(var1, var4);

            for (Deque var6 : this.y.values()) {
               ArrayDeque var7 = new ArrayDeque();

               for (cz.v var9 : var6) {
                  var7.addLast(var9.preferredSlot == var2 ? new cz.v(var1, var9.stack) : var9);
               }

               var6.clear();
               var6.addAll(var7);
            }

            iy.V();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean W(int var1) {
      return this.J() && this.j.containsKey(var1) && !this.j.get(var1).method_7960();
   }

   public int m(int var1, class_1799 var2) {
      class_1799 var3 = this.j.get(var1);
      return this.J() && var3 != null && var2.method_31574(var3.method_7909()) ? Math.max(0, var2.method_7947() - var3.method_7947()) : 0;
   }

   public class_1799 T(int var1, class_1799 var2) {
      if (!this.W(var1)) {
         return var2;
      }

      int var3 = this.m(var1, var2);
      return var3 == 0 ? class_1799.field_8037 : jn.Q(var2.method_46651(var3));
   }

   public boolean R(class_1703 var1, List<wu.l> var2) {
      class_310 var3 = class_310.method_1551();
      if (var3.field_1724 != null && var1 == var3.field_1724.field_7498 && var3.field_1724.method_56992() && var3.field_1724.field_7512 instanceof class_483) {
         this.u(() -> {
            for (wu.l var4 : var2) {
               var1.method_7593(var4.slot(), var4.button(), var4.type(), var3.field_1724);
            }

            var1.method_7623();
         });
         return true;
      } else {
         return false;
      }
   }

   public boolean h(class_1703 var1, class_1735 var2, int var3) {
      class_310 var4 = class_310.method_1551();
      if (var4.field_1724 != null && var2 != null && var2.field_7871 == var4.field_1724.method_31548() && var3 >= 0 && var3 <= 1) {
         int var5 = var2.method_34266();
         class_1799 var6 = var4.field_1724.method_31548().method_5438(var5);
         int var7 = this.m(var5, var6);
         if (var7 != 0 && var1.method_34255().method_7960()) {
            int var8 = var3 == 0 ? var7 : (var7 + 1) / 2;
            ArrayList var9 = new ArrayList();
            var9.add(new wu.l(var2.field_7874, 0, class_1713.field_7790));

            for (int var10 = 0; var10 < var6.method_7947() - var8; var10++) {
               var9.add(new wu.l(var2.field_7874, 1, class_1713.field_7790));
            }

            int var11 = var6.method_7947() - var7;
            if (this.R(var1, var9)) {
               q8.v("FakeSpawner", "Creative surplus picked physical={} amount={} reserved={}", var5, var8, var11);
               return true;
            } else {
               wu.x(var1, "pick unreserved surplus", var9, var6.method_46651(var8), iy::V, iy::V);
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int T() {
      class_1661 var1 = class_310.method_1551().field_1724.method_31548();

      for (int var2 = 0; var2 < 36; var2++) {
         if (!this.W(var2) && !this.V(var2) && var1.method_5438(var2).method_7960()) {
            return var2;
         }
      }

      return -1;
   }

   public int W(int var1) {
      return this.m(class_1802.field_8606, var1);
   }

   public int m(class_1792 var1, int var2) {
      return iv.T(var1, var2);
   }

   public boolean Q(class_1799 var1) {
      return false;
   }

   public boolean w() {
      return this.H(1) == 1;
   }

   public int H(int var1) {
      return this.T(class_1802.field_8606, var1);
   }

   public int T(class_1792 var1, int var2) {
      return this.m(var1, var2);
   }

   public boolean F(int var1) {
      return var1 == 48 || var1 == 50;
   }

   public boolean g(class_2561 var1, class_1703 var2) {
      if (var1 != null && var2 != null) {
         String var3 = var1.getString().toLowerCase(Locale.ROOT);
         return var3.contains("sell") && var2.field_7761.size() >= 72;
      } else {
         return false;
      }
   }

   public class_2561 I(class_2561 var1) {
      return var1 == null ? class_2561.method_43470(p("place items in here to sell")) : class_2561.method_43470(p(var1.getString()));
   }

   public void W(class_2561 var1, class_1703 var2) {
      if (this.J() && this.g(var1, var2)) {
         long var3 = 0L;

         for (int var5 = 0; var5 < Math.max(0, var2.field_7761.size() - 36); var5++) {
            var3 += this.L(var2.method_7611(var5).method_7677());
         }

         if (var3 > 0L) {
            yp.K(var3);
         }
      }
   }

   private long o(class_2338 var1) {
      long var2 = System.currentTimeMillis();
      return var1 == null ? var2 : this.i.computeIfAbsent(var1.method_10063(), var2x -> var2);
   }

   private int p(class_2338 var1) {
      return var1 == null ? Math.max(1, FakeSpawnerModule.i().B()) : Math.max(1, this.e.getOrDefault(var1.method_10063(), 1));
   }

   private cz.c h(long var1, int var3) {
      return this.I(var1, var3, qh.x);
   }

   private cz.c I(long var1, int var3, qh var4) {
      double var5 = this.B(var3);
      double var7 = var4.S == class_1802.field_8162 ? 0.0 : Math.max(0.15, var5 * 0.33);
      long var9 = Math.max(0L, System.currentTimeMillis() - var1);
      long var11 = var9 / 60000L;
      return new cz.c(Math.max(0, (int)(var11 * var5)), Math.max(0, (int)(var11 * var7)), var5, var7);
   }

   private double B(int var1) {
      int var2 = Math.max(1, var1);
      if (var2 == 1) {
         return 1.9;
      } else {
         return var2 <= 64 ? var2 * 1.9453125 : 124.5 + (var2 - 64) * 0.88671875;
      }
   }

   private static int J(class_1277 var0, int var1, class_1792 var2, int var3) {
      int var4 = Math.max(0, var3);

      while (var4 > 0 && var1 < 45) {
         int var5 = Math.min(64, var4);
         var0.method_5447(var1++, new class_1799(var2, var5));
         var4 -= var5;
      }

      return var1;
   }

   private List<class_2561> x(cz.c var1, qh var2) {
      int var3 = var1 == null ? 0 : Math.max(0, var1.f);
      int var4 = var1 == null ? 0 : Math.max(0, var1.V);
      if (var3 <= 0 && var4 <= 0) {
         return List.of(o("(0% filled)", 64648));
      }

      ArrayList var5 = new ArrayList();
      if (var3 > 0) {
         var5.add(o(var3 + " ", 64648).method_10852(var2.S.method_7864(new class_1799(var2.S)).method_27661().method_27692(class_124.field_1068)));
      }

      if (var4 > 0) {
         var5.add(o(var4 + " ", 64648).method_10852(var2.E.method_7864(new class_1799(var2.E)).method_27661().method_27692(class_124.field_1068)));
      }

      var5.add(o("(0% filled)", 64648));
      return var5;
   }

   private boolean g(long var1, int var3) {
      class_310 var4 = class_310.method_1551();
      if (var4.field_1724 != null
         && !wu.e()
         && var4.field_1724.field_7512 == var4.field_1724.field_7498
         && var4.field_1724.field_7498.method_34255().method_7960()) {
         Deque var5 = this.y.get(var1);
         if (var5 != null && !var5.isEmpty()) {
            LinkedHashMap var6 = new LinkedHashMap();
            int var7 = var3;

            for (cz.v var9 : var5.reversed()) {
               class_1799 var10 = var4.field_1724.method_31548().method_5438(var9.preferredSlot);
               if (!var10.method_31574(var9.stack.method_7909()) || var10.method_7947() < var9.stack.method_7947()) {
                  return false;
               }

               int var11 = Math.min(var7, var9.stack.method_7947());
               var6.merge(var9.preferredSlot, var11, Integer::sum);
               var7 -= var11;
               if (var7 <= 0) {
                  break;
               }
            }

            int var13 = var3 - var7;
            int var14 = -1;

            for (Entry var17 : var6.entrySet()) {
               class_1799 var12 = this.j.get(var17.getKey());
               if (var12 != null
                  && var12.method_7947() == (Integer)var17.getValue()
                  && var4.field_1724.method_31548().method_5438((Integer)var17.getKey()).method_7947() == (Integer)var17.getValue()) {
                  var14 = (Integer)var17.getKey();
                  break;
               }
            }

            if (var14 < 0) {
               var14 = this.T();
            }

            if (var14 < 0) {
               return false;
            }

            ArrayList var16 = new ArrayList();

            for (Entry var20 : var6.entrySet()) {
               if ((Integer)var20.getKey() != var14) {
                  var16.addAll(
                     wu.F(
                        (Integer)var20.getKey(),
                        var14,
                        (Integer)var20.getValue(),
                        var4.field_1724.method_31548().method_5438((Integer)var20.getKey()).method_7947()
                     )
                  );
               }
            }

            if (var16.isEmpty()) {
               this.W(var1, var3);
               if (var7 > 0) {
                  this.G(new class_1799(this.y(var1), var7), var7);
               }

               iy.V();
               return true;
            } else {
               int var19 = var14;
               int var21 = var7;
               wu.D("mining return", var16, () -> {
                  this.W(var1, var3);
                  this.Z(var1, var3);
                  if (var21 > 0) {
                     this.G(new class_1799(this.y(var1), var21), var21);
                  }

                  this.v(class_2338.method_10092(var1), true);
                  iy.V();
                  this.C("Mining return predicted pile={} physical={} amount={} worldPending={}", var1, var19, var13, var21);
               }, () -> this.q());
               return false;
            }
         } else {
            this.G(new class_1799(this.y(var1), var3), var3);
            return true;
         }
      } else {
         return false;
      }
   }

   private void G(class_1799 var1, int var2) {
      if (var2 > 0 && !var1.method_7960()) {
         class_1661 var3 = class_310.method_1551().field_1724.method_31548();
         int var4 = -1;

         for (int var5 = 0; var5 < 36; var5++) {
            if (!this.W(var5)) {
               class_1799 var6 = var3.method_5438(var5);
               cz.p var7 = this.P.get(var5);
               if (var7 != null && class_1799.method_31577(var7.stack, var1) && var7.expected + var2 <= var1.method_7914()) {
                  var4 = var5;
                  break;
               }

               if (var7 == null && !var6.method_7960() && class_1799.method_31577(var6, var1) && var6.method_7947() + var2 <= var6.method_7914()) {
                  var4 = var5;
                  break;
               }
            }
         }

         if (var4 < 0) {
            var4 = this.T();
         }

         if (var4 >= 0) {
            int var8 = var3.method_5438(var4).method_7960() && !this.P.containsKey(var4) ? iy.f() : -1;
            this.n(var4, var1, 0, var2);
            if (var8 >= 0) {
               iy.e(var8, var4);
            }
         }
      }
   }

   private void n(int var1, class_1799 var2, int var3, int var4) {
      if (var4 > 0) {
         cz.p var5 = this.P.get(var1);
         int var6 = class_310.method_1551().field_1724.method_31548().method_5438(var1).method_7947();
         int var7 = var5 != null && class_1799.method_31577(var5.stack, var2) ? Math.max(var5.expected + var3, var6) : var6;
         this.P.put(var1, new cz.p(var2.method_46651(1), var7 + var4, class_310.method_1551().field_1724.field_6012 + 100));
         iy.d();
      }
   }

   public boolean V(int var1) {
      return this.P.containsKey(var1);
   }

   public class_1799 x(int var1, class_1799 var2) {
      cz.p var3 = this.P.get(var1);
      return var3 != null && var2.method_7960() ? jn.Q(var3.stack.method_46651(var3.expected)) : var2;
   }

   private cz.v W(long var1, int var3) {
      Deque var4 = this.y.get(var1);
      if (var4 != null && !var4.isEmpty() && var3 > 0) {
         int var5 = var3;
         int var6 = -1;
         class_1799 var7 = class_1799.field_8037;

         while (var5 > 0 && !var4.isEmpty()) {
            cz.v var8 = (cz.v)var4.removeLast();
            if (var8 != null && var8.stack != null && !var8.stack.method_7960()) {
               if (var6 < 0) {
                  var6 = var8.preferredSlot;
               }

               if (var7.method_7960()) {
                  var7 = var8.stack.method_7972();
               }

               int var9 = Math.min(var5, var8.stack.method_7947());
               class_1799 var10 = this.j.get(var8.preferredSlot);
               if (var10 != null && class_1799.method_31577(var10, var8.stack)) {
                  if (var10.method_7947() <= var9) {
                     this.j.remove(var8.preferredSlot);
                  } else {
                     this.j.put(var8.preferredSlot, var10.method_46651(var10.method_7947() - var9));
                  }
               }

               var5 -= var9;
               int var11 = var8.stack.method_7947() - var9;
               if (var11 > 0) {
                  var4.addLast(new cz.v(var8.preferredSlot, var8.stack.method_46651(var11)));
               }
            }
         }

         if (var4.isEmpty()) {
            this.y.remove(var1);
         }

         return var7.method_7960() ? null : new cz.v(var6, var7.method_46651(var3));
      } else {
         return null;
      }
   }

   private boolean l(long var1, cz.v var3, boolean var4) {
      if (var3 != null && !var3.stack.method_7960()) {
         class_310 var5 = class_310.method_1551();
         if (var5.field_1724 == null) {
            return false;
         }

         int var6 = var3.preferredSlot;
         if (var6 >= 0 && var6 < var5.field_1724.method_31548().method_5439() && !this.j.containsKey(var6)) {
            class_1799 var7 = var5.field_1724.method_31548().method_5438(var6);
            int var8 = var3.stack.method_7947();
            int var9 = var4 && !var5.field_1724.method_56992() ? var8 - 1 : var8;
            if (var9 <= 0) {
               return true;
            } else if (var7.method_31574(var3.stack.method_7909()) && var7.method_7947() >= var9) {
               this.j.put(var6, var7.method_46651(var9));
               this.y.computeIfAbsent(var1, var0 -> new ArrayDeque<>()).addFirst(new cz.v(var6, var7.method_46651(var9)));
               iy.V();
               q8.v(
                  "FakeSpawner",
                  "Reservation created pile={} physical={} item={} reserved={} real={}",
                  var1,
                  var6,
                  class_7923.field_41178.method_10221(var7.method_7909()),
                  var9,
                  var7.method_7947()
               );
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void n(class_2338 var1, class_2350 var2) {
      class_310 var3 = class_310.method_1551();
      if (var3 != null && var3.field_1713 != null && var3.field_1687 != null && var1 != null) {
         double var4 = var1.method_10263() + 0.5;
         double var6 = var1.method_10264() + 0.55;
         double var8 = var1.method_10260() + 0.5;
         if (var2 != null) {
            var4 += var2.method_10148() * 0.42;
            var6 += var2.method_10164() * 0.42;
            var8 += var2.method_10165() * 0.42;
         }

         class_2388 var10 = new class_2388(class_2398.field_11217, class_2246.field_10260.method_9564());

         for (int var11 = 0; var11 < 1; var11++) {
            var3.field_1713
               .method_3056(
                  var10,
                  var4,
                  var6,
                  var8,
                  (var3.field_1687.method_8409().method_43058() - 0.5) * 0.14,
                  var3.field_1687.method_8409().method_43058() * 0.12 + 0.02,
                  (var3.field_1687.method_8409().method_43058() - 0.5) * 0.14
               );
         }
      }
   }

   private void B(class_310 var1, class_2338 var2, class_2350 var3, double var4) {
      if (var1 != null && var1.field_1724 != null && var1.field_1769 != null && var2 != null) {
         int var6 = class_3532.method_15340((int)(var4 * 10.0), 0, 9);
         var1.field_1687.method_8517(var1.field_1724.method_5628(), var2, var6);
         if (var4 <= 0.0) {
            return;
         }

         long var7 = var2.method_10063();
         long var9 = var1.field_1724.field_6012;
         long var11 = this.r.getOrDefault(var7, 0L);
         if (var9 - var11 >= 4L) {
            this.r.put(var7, var9);
            this.n(var2, var3);
            this.v(var2, false);
         }
      }
   }

   private void u(class_2338 var1) {
      class_310 var2 = class_310.method_1551();
      if (var2 != null && var2.field_1724 != null && var2.field_1769 != null && var1 != null) {
         var2.field_1687.method_8517(var2.field_1724.method_5628(), var1, -1);
      }
   }

   private void E(class_1937 var1, class_2338 var2) {
      class_310 var3 = class_310.method_1551();
      if (var3 != null && var3.field_1713 != null && var2 != null) {
         double var4 = var2.method_10263() + 0.5 + (var1.method_8409().method_43058() - 0.5) * 0.45;
         double var6 = var2.method_10264() + 0.35 + var1.method_8409().method_43058() * 0.45;
         double var8 = var2.method_10260() + 0.5 + (var1.method_8409().method_43058() - 0.5) * 0.45;
         var3.field_1713.method_3056(class_2398.field_11240, var4, var6, var8, 0.0, 0.006, 0.0);
         if (var1.method_8409().method_43048(4) == 0) {
            var3.field_1713.method_3056(class_2398.field_11251, var4, var6 + 0.02, var8, 0.0, 0.004, 0.0);
         }
      }
   }

   private void f(String var1) {
      class_310 var2 = class_310.method_1551();
      if (var2 != null && var2.field_1724 != null && var1 != null && !var1.isBlank()) {
         b1.q(o(var1, 16711680), false);
         b1.q(o(var1, 16711680), true);
      }
   }

   private void C(String var1, Object... var2) {
      if (Boolean.getBoolean("larp.debug")) {
         q8.v("FakeSpawner", var1, var2);
      }
   }

   private void s(String var1, long var2, String var4, Object... var5) {
      if (Boolean.getBoolean("larp.debug")) {
         long var6 = System.currentTimeMillis();
         if (var6 >= this.Tt) {
            this.Tt = var6 + var2;
            this.C(var4, var5);
         }
      }
   }

   private static class_1799 M(class_1799 var0, class_2561 var1, List<class_2561> var2) {
      var0.method_57379(class_9334.field_49631, var1);
      var0.method_57379(class_9334.field_50073, class_1814.field_8906);
      if (var2 != null && !var2.isEmpty()) {
         var0.method_57379(class_9334.field_49632, new class_9290(var2));
      }

      return var0;
   }

   private static class_2561 g(String var0, class_124 var1) {
      return J(p(var0), var1);
   }

   private static class_5250 J(String var0, class_124 var1) {
      return class_2561.method_43470(var0).method_10862(class_2583.field_24360.method_27706(var1).method_10978(false));
   }

   private static class_2561 o(String var0, int var1) {
      return o(p(var0), var1);
   }

   private static class_5250 o(String var0, int var1) {
      return class_2561.method_43470(var0).method_10862(class_2583.field_24360.method_27703(class_5251.method_27717(var1)).method_10978(false));
   }

   private static String p(String var0) {
      StringBuilder var1 = new StringBuilder();

      for (int var2 = 0; var2 < var0.length(); var2++) {
         char var3 = var0.charAt(var2);
         if (var3 >= 'A' && var3 <= 'Z') {
            var1.append("ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ".charAt(var3 - 'A'));
         } else if (var3 >= 'a' && var3 <= 'z') {
            var1.append("ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ".charAt(var3 - 'a'));
         } else {
            var1.append(var3);
         }
      }

      return var1.toString();
   }

   private static String H(double var0, int var2) {
      String var3 = String.format(Locale.US, "%." + var2 + "f", var0);

      while (var3.contains(".") && var3.endsWith("0")) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      if (var3.endsWith(".")) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      return var3;
   }

   private static class_2561 J(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         class_5250 var1 = class_2561.method_43473();
         class_2583 var2 = class_2583.field_24360.method_10978(false);
         StringBuilder var3 = new StringBuilder();

         for (int var4 = 0; var4 < var0.length(); var4++) {
            char var5 = var0.charAt(var4);
            if ((var5 == 167 || var5 == '&') && var4 + 1 < var0.length()) {
               if (var5 == '&' && var4 + 7 < var0.length() && var0.charAt(var4 + 1) == '#') {
                  Integer var6 = C(var0.substring(var4 + 2, var4 + 8));
                  if (var6 != null) {
                     A(var1, var3, var2);
                     var2 = var2.method_27703(class_5251.method_27717(var6)).method_10978(false);
                     var4 += 7;
                     continue;
                  }
               }

               class_124 var7 = class_124.method_544(var0.charAt(var4 + 1));
               if (var7 != null) {
                  A(var1, var3, var2);
                  if (var7 == class_124.field_1070) {
                     var2 = class_2583.field_24360.method_10978(false);
                  } else if ("0123456789abcdef".indexOf(Character.toLowerCase(var0.charAt(var4 + 1))) >= 0) {
                     var2 = class_2583.field_24360.method_27706(var7).method_10978(false);
                  } else {
                     var2 = var2.method_27706(var7).method_10978(false);
                  }

                  var4++;
                  continue;
               }
            }

            var3.append(var5);
         }

         A(var1, var3, var2);
         return var1;
      } else {
         return class_2561.method_43473();
      }
   }

   private static void A(class_5250 var0, StringBuilder var1, class_2583 var2) {
      if (var1.length() != 0) {
         var0.method_10852(class_2561.method_43470(var1.toString()).method_10862(var2));
         var1.setLength(0);
      }
   }

   private static Integer C(String var0) {
      try {
         return Integer.parseInt(var0, 16);
      } catch (NumberFormatException var2) {
         return null;
      }
   }

   private String W(long var1) {
      long var3 = Math.max(1L, var1);
      double var5 = var3 * 1000.0;
      if (var5 >= 1.0E9) {
         return H(var5 / 1.0E9, 1) + "B";
      } else if (var5 >= 1000000.0) {
         return H(var5 / 1000000.0, 1) + "M";
      } else {
         return var5 >= 1000.0 ? H(var5 / 1000.0, 1) + "K" : H(var5, 0);
      }
   }

   private String s(long var1) {
      long var3 = Math.max(1L, var1);
      double var5 = var3 * 1000.0 + 75.0;
      if (var5 >= 1.0E9) {
         return H(var5 / 1.0E9, 1) + "B";
      } else if (var5 >= 1000000.0) {
         return H(var5 / 1000000.0, 1) + "M";
      } else {
         return var5 >= 1000.0 ? H(var5 / 1000.0, 1) + "K" : H(var5, 0);
      }
   }

   private boolean j(class_1799 var1) {
      return var1 != null && !var1.method_7960() ? var1.method_7909() instanceof class_1747 var2 && var2.method_7711() instanceof class_2480 : false;
   }

   private long R(class_1799 var1) {
      if (var1 != null && !var1.method_7960()) {
         long var2 = 0L;
         class_9288 var4 = (class_9288)var1.method_58694(class_9334.field_49622);
         if (var4 != null) {
            for (class_1799 var6 : var4.method_59714()) {
               if (var6 != null && !var6.method_7960() && this.P(var6)) {
                  var2 += var6.method_7947();
               }
            }
         }

         return var2;
      } else {
         return 0L;
      }
   }

   private int j(class_2487 var1) {
      if (var1 == null) {
         return 0;
      } else if (var1.method_10545("count")) {
         return Math.max(0, var1.method_68083("count", 0));
      } else {
         return var1.method_10545("Count") ? Math.max(0, var1.method_68083("Count", 0)) : 0;
      }
   }

   private boolean z(int var1) {
      class_310 var2 = class_310.method_1551();
      return var2 != null && var2.method_22683() != null ? s2.G(var2.method_22683(), var1) : false;
   }

   private record a(long pos, qh kind, int count, long placedAt, List<cz.s> backing, Map<String, Integer> purchased) {
      JsonObject toJson() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("pos", this.pos);
         var1.addProperty("kind", this.kind.name());
         var1.addProperty("count", this.count);
         var1.addProperty("placedAt", this.placedAt);
         JsonArray var2 = new JsonArray();

         for (cz.s var4 : this.backing) {
            JsonObject var5 = new JsonObject();
            var5.addProperty("slot", var4.slot);
            var5.addProperty("item", var4.item);
            var5.addProperty("amount", var4.amount);
            var5.addProperty("reserved", var4.reserved);
            var2.add(var5);
         }

         var1.add("backing", var2);
         JsonObject var6 = new JsonObject();
         this.purchased.forEach(var6::addProperty);
         var1.add("purchased", var6);
         return var1;
      }
   }

   public static final class c {
      public final int V;
      public final int f;
      public final double g;
      public final double m;

      public c(int var1, int var2, double var3, double var5) {
         this.V = Math.max(0, var1);
         this.f = Math.max(0, var2);
         this.g = Math.max(0.0, var3);
         this.m = Math.max(0.0, var5);
      }
   }

   private record f(class_2338 pos, long untilMs) {
   }

   public record l(class_3414 event, float volume, float pitch) {
   }

   private record p(class_1799 stack, int expected, int expires) {
   }

   private record s(int slot, String item, int amount, int reserved) {
   }

   private record v(int preferredSlot, class_1799 stack) {
   }

   private record w(int stackCount, cz.v hiddenStack, long createdAtMs) {
   }
}
