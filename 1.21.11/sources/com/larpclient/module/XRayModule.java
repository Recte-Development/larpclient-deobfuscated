package com.larpclient.module;

import com.larpclient.ei;
import com.larpclient.l2;
import com.larpclient.q8;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2818;
import net.minecraft.class_638;
import net.minecraft.class_2338.class_2339;

public class XRayModule extends Module {
   private static volatile XRayModule q;
   private static final Map<class_2248, Integer> U = new HashMap<>();
   private static final Map<class_2248, String> O = new LinkedHashMap<>();
   private static final String s = "Base Blocks";
   private static final int f = 32;
   private static volatile boolean h = (boolean)0;
   private static volatile String p = "Both";
   private static volatile Set<class_2248> w = Set.of();
   private static volatile boolean G;
   private final li e = this.addSetting(new li("Mode", "XRay rendering mode", "Both", "See Through", "ESP Outline", "Both"));
   private final lo u = this.addSetting(new lo("Range", "Scan radius", 32.0, 8.0, 64.0, 4.0));
   private final Map<String, l3> l = new LinkedHashMap<>();
   private final l2 H;
   private final l3 z;
   private final l3 M;
   private final li a;
   private final lo T;
   private final List<XRayModule.d> m;
   private final PriorityQueue<XRayModule.d> Q;
   private Set<class_2248> R;
   private int I;
   private boolean S;
   private class_2338 Z;
   private int A;
   private int c;
   private int r;
   private class_638 y;

   private static void Z(String var0, int var1, class_2248... var2) {
      for (class_2248 var6 : var2) {
         U.put(var6, var1);
         O.put(var6, var0);
      }
   }

   public XRayModule() {
      super("XRay", "Highlight ores through blocks", Category.RENDER, -1);
      XRayModule var1 = this;
      this.H = new l2(var1x -> var1.addSetting(var1x));
      this.z = this.addSetting(new l3("Ore Colors", "Color each marker by what it is", true));
      this.M = this.addSetting(new l3("Labels", "Name, vein size and distance above each vein", true));
      this.a = this.addSetting(new li("Overlay Style", "Ore marker shape", "Box", "Box", "Corners", "Filled"));
      this.T = this.addSetting(new lo("Max Ores", "Nearest ore markers retained", 512.0, 64.0, 2048.0, 64.0));
      this.m = new ArrayList<>();
      this.Q = new PriorityQueue<>(Comparator.comparingDouble(XRayModule.d::distanceSquared).reversed());
      this.R = Set.of();
      this.I = -1;
      Set var4 = Set.of("Coal", "Copper", "Quartz");

      for (String var3 : new LinkedHashSet<>(O.values())) {
         this.l.put(var3, this.addSetting(new l3(var3, "Show " + var3.toLowerCase(Locale.ROOT), !var4.contains(var3))));
      }

      BooleanSupplier var5 = () -> !this.e.G("See Through");
      this.u.O(var5);
      this.T.O(var5);
      this.a.O(var5);
      this.z.O(var5);
      this.M.O(var5);
      this.H.N(() -> var5.getAsBoolean() && !this.z.getValue(), var5, var5, () -> var5.getAsBoolean() && this.a.G("Filled"));
      this.p();
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register(this::s);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> T());
      q = this;
   }

   @Override
   public void onEnable() {
      p = this.e.getValue();
      this.p();
      h = (boolean)1;
      T();
      this.x();
      if (y()) {
         this.e();
      }

      String var1 = FabricLoader.getInstance()
         .getModContainer("sodium")
         .map(var0 -> "Sodium " + var0.getMetadata().getVersion().getFriendlyString())
         .orElse("vanilla");
      q8.G("XRay", "Enabled in {} mode, {} block types shown, {} terrain", p, w.size(), var1);
   }

   public List<lb<?>> j() {
      return List.of(this.e, this.a, this.u);
   }

   public List<lb<?>> E() {
      return List.copyOf(this.l.values());
   }

   public List<lb<?>> g() {
      ArrayList var1 = new ArrayList<>(List.of(this.z, this.M));
      var1.addAll(this.H.v());
      var1.addAll(this.H.P());
      var1.addAll(this.H.t());
      return List.copyOf(var1);
   }

   public List<lb<?>> G() {
      return List.of(this.T);
   }

   @Override
   public void onDisable() {
      h = (boolean)0;
      T();
      this.x();
      this.e();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         String var1 = this.e.getValue();
         byte var2 = y();
         byte var3 = this.p();
         int var10000 = var1.equals(p);
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            p = var1;
            this.x();
         }

         label31: {
            byte var4 = y();
            U((boolean)var4);
            byte var8 = var4;
            byte var7 = var2;
            var10000 = Integer.compare(var7, var8);
            if ((var10000 | -var10000) >>> 31 == 0) {
               boolean var10 = false;
               byte var9 = var3;
               var10000 = Integer.signum(var9);
               if (((var10000 | -var10000) >>> 31 ^ 1) != 0 || var4 == 0) {
                  break label31;
               }
            }

            q8.v("XRay", "Rebuilding terrain: mode {}, filters changed {}", p, Boolean.valueOf((boolean)var3));
            this.e();
         }

         int var16 = S();
         boolean var12 = false;
         byte var11 = (byte)var16;
         var16 = Integer.signum(var11);
         if (((var16 | -var16) >>> 31 ^ 1) == 0) {
            this.N();
         } else if (!this.m.isEmpty() || this.Z != null) {
            this.x();
         }
      }
   }

   private boolean p() {
      int var1 = 0;
      int var2 = 0;

      for (l3 var4 : this.l.values()) {
         if (var4.getValue()) {
            var1 |= 1 << var2;
         }

         var2++;
      }

      if (var1 == this.I) {
         return false;
      }

      this.I = var1;
      HashSet var8 = new HashSet();
      HashSet var9 = new HashSet();

      for (class_2248 var6 : U.keySet()) {
         String var7 = O.get(var6);
         if (var7 == null) {
            var8.add(var6);
         } else if (this.l.get(var7).getValue()) {
            var8.add(var6);
            var9.add(var6);
         }
      }

      w = Set.copyOf(var8);
      this.R = Set.copyOf(var9);
      this.x();
      return true;
   }

   private void x() {
      this.m.clear();
      this.Q.clear();
      this.Z = null;
      this.y = null;
      this.c = this.r = 0;
      this.S = (boolean)0;
   }

   private void N() {
      int var1 = this.u.getValue().intValue();
      class_2338 var2 = mc.field_1724.method_24515();
      if (this.y != mc.field_1687 || this.Z == null || this.A != var1 || this.Z.method_10262(var2) > 64.0) {
         this.x();
         this.y = mc.field_1687;
         this.Z = var2;
         this.A = var1;
      }

      if (this.r > 0) {
         this.r--;
      } else {
         int var3 = var1 * 2 + 1;
         int var4 = var3 * var3 * var3;
         class_2339 var5 = new class_2339();

         for (int var6 = 0; var6 < 8192 && this.c < var4; this.c++) {
            int var7 = this.c % var3;
            int var8 = this.c / var3 % var3;
            int var9 = this.c / (var3 * var3);
            int var10 = K(var9);
            int var11 = K(var7);
            int var12 = K(var8);
            if (var10 * var10 + var11 * var11 + var12 * var12 <= var1 * var1) {
               var5.method_10103(this.Z.method_10263() + var10, this.Z.method_10264() + var11, this.Z.method_10260() + var12);
               class_2818 var13 = mc.field_1687.method_2935().method_21730(var5.method_10263() >> 4, var5.method_10260() >> 4);
               if (var13 != null && var5.method_10264() >= mc.field_1687.method_31607() && var5.method_10264() <= mc.field_1687.method_31600()) {
                  class_2248 var14 = var13.method_8320(var5).method_26204();
                  if (this.R.contains(var14)) {
                     int var15 = 0xFF000000 | U.get(var14);
                     this.Q.add(new XRayModule.d(var5.method_10062(), var14, var15, var5.method_10262(this.Z), 0));

                     while (this.Q.size() > this.T.getValue().intValue()) {
                        this.Q.poll();
                     }
                  }
               }
            }

            var6++;
         }

         if (!this.S || this.c >= var4) {
            this.m.clear();
            this.m.addAll(this.Q);
            this.m.sort(Comparator.comparingDouble(XRayModule.d::distanceSquared));
            this.E();
         }

         if (this.c >= var4) {
            this.S = (boolean)1;
            this.c = 0;
            this.Q.clear();
            this.r = 10;
         }
      }
   }

   private void E() {
      HashMap var1 = new HashMap(this.m.size() * 2);

      for (int var2 = 0; var2 < this.m.size(); var2++) {
         var1.put(this.m.get(var2).pos(), var2);
      }

      boolean[] var13 = new boolean[this.m.size()];
      ArrayDeque var3 = new ArrayDeque();

      for (int var4 = 0; var4 < this.m.size(); var4++) {
         if (!var13[var4]) {
            var13[var4] = (boolean)1;
            XRayModule.d var5 = this.m.get(var4);
            String var6 = y(var5.block());
            int var7 = 0;
            var3.add(var4);

            while (!var3.isEmpty()) {
               class_2338 var8 = this.m.get((Integer)var3.poll()).pos();
               var7++;

               for (int var9 = -1; var9 <= 1; var9++) {
                  for (int var10 = -1; var10 <= 1; var10++) {
                     for (int var11 = -1; var11 <= 1; var11++) {
                        Integer var12 = (Integer)var1.get(var8.method_10069(var9, var10, var11));
                        if (var12 != null && !var13[var12] && y(this.m.get(var12).block()).equals(var6)) {
                           var13[var12] = (boolean)1;
                           var3.add(var12);
                        }
                     }
                  }
               }
            }

            this.m.set(var4, var5.withVein(var7));
         }
      }
   }

   private static String y(class_2248 var0) {
      String var1 = O.get(var0);
      return "Base Blocks".equals(var1) ? var0.method_9518().getString() : var1;
   }

   private static int K(int var0) {
      return var0 == 0 ? 0 : (var0 % 2 == 1 ? (var0 + 1) / 2 : -var0 / 2);
   }

   private void s(WorldRenderContext var1) {
      if (this.isOperational() && this.isInGame() && S() && this.y == mc.field_1687) {
         try {
            int var2 = 0;
            int var3 = 0;
            double var4 = this.u.getValue();
            class_243 var6 = mc.field_1724.method_73189();

            for (XRayModule.d var8 : this.m) {
               class_2338 var9 = var8.pos();
               class_2818 var10 = mc.field_1687.method_2935().method_21730(var9.method_10263() >> 4, var9.method_10260() >> 4);
               if (var10 != null && var10.method_8320(var9).method_26204() == var8.block()) {
                  class_243 var11 = class_243.method_24953(var9);
                  double var12 = var6.method_1022(var11);
                  if (!(var12 >= var4)) {
                     if (var2++ >= this.T.getValue().intValue()) {
                        break;
                     }

                     int var14 = this.z.getValue() ? ei.R(var8.color(), l2.A(var12, var4)) : this.H.C(var8.color(), var12, var4, 1.0, var9.hashCode());
                     this.H.X(new class_238(var9).method_1014(0.003), this.a.getValue(), var14);
                     if (this.M.getValue() && var8.vein() > 0 && var3 < 32 && var14 >>> 24 != 0) {
                        var3++;
                        String var15 = y(var8.block()) + (var8.vein() > 1 ? " x" + var8.vein() : "") + " · " + Math.round(var12) + "m";
                        this.H.n(var11.method_1031(0.0, 0.85, 0.0), var15, var14);
                     }
                  }
               }
            }
         } catch (RuntimeException var16) {
            q8.O("ESP", "Ore overlay failed", var16);
         }
      }
   }

   private void e() {
      if (mc.field_1687 != null && mc.field_1769 != null) {
         mc.field_1769.method_3279();
      }
   }

   private static void T() {
      U(y());
   }

   private static void U(boolean var0) {
      if (var0 != G) {
         G = var0;
         q8.v("XRay", "Terrain see-through {} for chunk rebuilds", var0 ? "admitted" : "withdrawn");
      }
   }

   public static boolean R() {
      return q != null && q.isEnabled() ? h : false;
   }

   public static boolean y() {
      return q != null && q.isEnabled() ? h && (p.equals("See Through") || p.equals("Both")) : false;
   }

   public static boolean m() {
      return G;
   }

   public static boolean S() {
      return q != null && q.isEnabled() ? h && (p.equals("ESP Outline") || p.equals("Both")) : false;
   }

   public static boolean r(class_2248 var0) {
      return w.contains(var0);
   }

   static {
      Z("Diamond", 4910553, class_2246.field_10442, class_2246.field_29029);
      Z("Emerald", 1564002, class_2246.field_10013, class_2246.field_29220);
      Z("Gold", 16570170, class_2246.field_10571, class_2246.field_29026, class_2246.field_23077);
      Z("Iron", 14857359, class_2246.field_10212, class_2246.field_29027);
      Z("Redstone", 16724016, class_2246.field_10080, class_2246.field_29030);
      Z("Lapis", 4025343, class_2246.field_10090, class_2246.field_29028);
      Z("Copper", 15764043, class_2246.field_27120, class_2246.field_29221);
      Z("Coal", 10790052, class_2246.field_10418, class_2246.field_29219);
      Z("Ancient Debris", 12607548, class_2246.field_22109);
      Z("Quartz", 16052198, class_2246.field_10213);
      Z("Base Blocks", 4910553, class_2246.field_10201);
      Z("Base Blocks", 1564002, class_2246.field_10234);
      Z("Base Blocks", 16570170, class_2246.field_10205);
      Z("Base Blocks", 15132390, class_2246.field_10085);
      Z("Base Blocks", 10259596, class_2246.field_22108);
      Z("Base Blocks", 16757820, class_2246.field_10034);
      Z("Base Blocks", 11554047, class_2246.field_10443);
      Z("Base Blocks", 16727160, class_2246.field_10260);
      Z("Base Blocks", 16734810, class_2246.field_10375);

      for (class_2248 var3 : new class_2248[]{class_2246.field_10164, class_2246.field_10382, class_2246.field_10540, class_2246.field_9987}) {
         U.put(var3, 16777215);
      }
   }

   private record d(class_2338 pos, class_2248 block, int color, double distanceSquared, int vein) {
      XRayModule.d withVein(int var1) {
         return new XRayModule.d(this.pos, this.block, this.color, this.distanceSquared, var1);
      }
   }
}
