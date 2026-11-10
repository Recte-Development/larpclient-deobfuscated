package com.larpclient.module;

import com.larpclient.b9;
import com.larpclient.c5;
import com.larpclient.eg;
import com.larpclient.ei;
import com.larpclient.gu;
import com.larpclient.l2;
import com.larpclient.q8;
import com.larpclient.wi;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.class_1297;
import net.minecraft.class_1799;
import net.minecraft.class_1923;
import net.minecraft.class_2248;
import net.minecraft.class_2281;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2586;
import net.minecraft.class_2589;
import net.minecraft.class_2595;
import net.minecraft.class_2601;
import net.minecraft.class_2608;
import net.minecraft.class_2611;
import net.minecraft.class_2614;
import net.minecraft.class_2619;
import net.minecraft.class_2627;
import net.minecraft.class_2636;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2745;
import net.minecraft.class_2818;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3719;
import net.minecraft.class_3720;
import net.minecraft.class_3723;
import net.minecraft.class_3866;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4184;
import net.minecraft.class_4482;
import net.minecraft.class_5250;
import net.minecraft.class_638;
import net.minecraft.class_7716;
import net.minecraft.class_8172;
import net.minecraft.class_8887;
import net.minecraft.class_9779;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class StorageESPModule extends Module {
   private final l3 G = this.addSetting(new l3("Chests", "Highlight regular and trapped chests", true));
   private final l3 P = this.addSetting(new l3("Ender Chests", "Highlight ender chests", true));
   private final l3 j = this.addSetting(new l3("Shulkers", "Highlight shulker boxes", true));
   private final l3 p = this.addSetting(new l3("Barrels", "Highlight barrels", false));
   private final l3 x = this.addSetting(new l3("Spawners", "Highlight mob spawners", true));
   private final l3 N = this.addSetting(new l3("Hoppers", "Highlight hoppers", false));
   private final l3 s = this.addSetting(new l3("Furnaces", "Highlight furnaces", false));
   private final l3 a = this.addSetting(new l3("Blast Furnaces", "Highlight blast furnaces", false));
   private final l3 L = this.addSetting(new l3("Smokers", "Highlight smokers", false));
   private final l3 q = this.addSetting(new l3("Dispensers", "Highlight dispensers", false));
   private final l3 w = this.addSetting(new l3("Droppers", "Highlight droppers", false));
   private final l3 C = this.addSetting(new l3("Crafters", "Highlight crafters", false));
   private final l3 v = this.addSetting(new l3("Brewing Stands", "Highlight brewing stands", false));
   private final l3 O = this.addSetting(new l3("Beehives", "Highlight beehives", false));
   private final l3 Q = this.addSetting(new l3("Decorated Pots", "Highlight decorated pots", false));
   private final l3 B = this.addSetting(new l3("Chiseled Bookshelves", "Highlight chiseled bookshelves", false));
   private final l3 r = this.addSetting(new l3("Jukeboxes", "Highlight jukeboxes", false));
   private final lo c = this.addSetting(new lo("Range", "Loaded storage scan radius", 64.0, 16.0, 128.0, 8.0));
   private final lo Y = this.addSetting(new lo("Max Blocks", "Nearest storage blocks drawn", 256.0, 16.0, 512.0, 16.0));
   private final li I = this.addSetting(new li("Mode", "Storage highlight style", "Filled", "Box", "Corners", "Filled"));
   private final l3 y = this.addSetting(new l3("Labels", "Show storage names above highlighted blocks", true));
   private final lo u = this.addSetting(new lo("Label Range", "Maximum name-tag distance, within the storage scan range", 128.0, 16.0, 128.0, 8.0));
   private final lo E = this.addSetting(new lo("Max Labels", "Nearest storage name tags drawn", 64.0, 1.0, 128.0, 1.0));
   private final lo J = this.addSetting(new lo("Label Scale", "Name-tag size on screen, the same at every distance", 1.0, 0.5, 2.0, 0.1));
   private final l3 T = this.addSetting(new l3("Label Icons", "Show the block item beside each storage name", true));
   private final l3 R = this.addSetting(new l3("Hide Overlaps", "Skip farther name tags that would cover nearer ones", true));
   private final l3 U = this.addSetting(new l3("Boxes", "Draw storage highlight boxes", true));
   private final l3 W = this.addSetting(new l3("Tracers", "Draw lines to selected storage blocks", false));
   private final li f = this.addSetting(new li("Tracer Origin", "Start at the camera crosshair or player feet", "Crosshair", "Crosshair", "Feet"));
   private final lo M = this.addSetting(new lo("Tracer Range", "Maximum line distance, within scan range", 64.0, 16.0, 128.0, 8.0));
   private final lo h = this.addSetting(new lo("Max Tracers", "Nearest selected storage lines drawn", 64.0, 1.0, 128.0, 1.0));
   private final l2 d;
   private final List<class_2586> g;
   private final Map<class_2248, class_1799> F;
   private class_638 D;
   private int t;
   private static StorageESPModule H;
   private static boolean o;
   private static final int b = 12;
   private static final int n = 10;
   private static final int l = 160;
   private static final int Z = 1073741824;
   private static final int S = -1;
   private static final int z = 14211288;

   public StorageESPModule() {
      super("StorageESP", "Highlight storage blocks", Category.RENDER, -1);
      StorageESPModule var1 = this;
      this.d = new l2(var1x -> var1.addSetting(var1x));
      this.g = new ArrayList<>();
      this.F = new HashMap<>();

      for (lb var2 : List.of(this.u, this.E, this.J, this.T, this.R)) {
         var2.O(this.y::getValue);
      }

      for (lb var6 : List.of(this.f, this.M, this.h)) {
         var6.O(this.W::getValue);
      }

      this.I.O(this.U::getValue);
      this.d
         .N(
            () -> this.U.getValue() || this.W.getValue() || this.y.getValue(),
            () -> this.U.getValue() || this.W.getValue(),
            () -> this.U.getValue() || this.W.getValue() || this.y.getValue(),
            () -> this.U.getValue() && this.I.G("Filled")
         );

      for (lb var7 : this.X()) {
         ((l3)var7).a(var1x -> this.I());
      }

      this.c.a(var1x -> this.I());
      this.Y.a(var1x -> this.I());
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1x -> this.T());
      H = this;
      if (!o) {
         o = (boolean)1;
         HudElementRegistry.addLast(class_2960.method_60655("larp-client", "storage_esp_labels"), (var0, var1x) -> H.b(var0, var1x));
      }
   }

   @Override
   public void onEnable() {
      this.t = 0;
      this.g.clear();
      this.D = null;
      q8.v("ESP", "StorageESP enabled; received loaded storage only, tracers={}", this.W.getValue());
   }

   @Override
   public void onDisable() {
      q8.v("ESP", "StorageESP cleanup; discarded {} cached blocks", this.g.size());
      this.g.clear();
      this.F.clear();
      this.D = null;
   }

   public List<lb<?>> X() {
      return List.of(this.G, this.P, this.j, this.p, this.x, this.N, this.s, this.a, this.L, this.q, this.w, this.C, this.v, this.O, this.Q, this.B, this.r);
   }

   public List<lb<?>> f() {
      return List.of(this.c, this.Y);
   }

   public List<lb<?>> S() {
      return List.of(this.c, this.U, this.I, this.y, this.W, this.f);
   }

   public List<lb<?>> V() {
      return List.of(this.Y, this.u, this.E, this.M, this.h);
   }

   public List<lb<?>> k() {
      return List.of(this.W, this.f, this.M, this.h);
   }

   public List<lb<?>> u() {
      return List.of(this.U, this.I);
   }

   public List<lb<?>> b() {
      ArrayList var1 = new ArrayList<>(this.d.v());
      var1.addAll(this.d.P());
      var1.addAll(this.d.t());
      var1.addAll(List.of(this.J, this.T, this.R));
      return List.copyOf(var1);
   }

   private void I() {
      this.t = 0;
      this.g.clear();
   }

   public static class_243 J(class_243 var0, Quaternionf var1, class_243 var2, String var3) {
      if (var3.equals("Feet")) {
         return var2.method_1031(0.0, 0.1, 0.0);
      }

      Vector3f var4 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1);
      return var0.method_1031(var4.x * 0.5, var4.y * 0.5, var4.z * 0.5);
   }

   public List<lb<?>> C() {
      return List.of(this.y, this.u, this.E, this.J, this.T, this.R);
   }

   public static String j(class_2586 var0) {
      return var0.method_11010().method_26204().method_9518().getString();
   }

   public static class_2338 Q(class_2586 var0) {
      class_2680 var1 = var0.method_11010();
      return var1.method_26204() instanceof class_2281
            && var1.method_28498(class_2281.field_10770)
            && var1.method_11654(class_2281.field_10770) != class_2745.field_12569
         ? class_2281.method_72581(var0.method_11016(), var1)
         : null;
   }

   public static String K(class_2586 var0, boolean var1) {
      return var1 ? j(var0) + " x2" : j(var0);
   }

   public static String w(String var0, double var1) {
      String var3 = (int)var1 + "m";
      return var0 != null && !var0.isBlank() ? var0 + " · " + var3 : var3;
   }

   private boolean w(class_2586 var1) {
      if (var1 instanceof class_2608) {
         return this.w.getValue();
      } else {
         return var1 instanceof class_2601
            ? this.q.getValue()
            : var1 instanceof class_2595 && this.G.getValue()
               || var1 instanceof class_2611 && this.P.getValue()
               || var1 instanceof class_2627 && this.j.getValue()
               || var1 instanceof class_3719 && this.p.getValue()
               || var1 instanceof class_2636 && this.x.getValue()
               || var1 instanceof class_2614 && this.N.getValue()
               || var1 instanceof class_3866 && this.s.getValue()
               || var1 instanceof class_3720 && this.a.getValue()
               || var1 instanceof class_3723 && this.L.getValue()
               || var1 instanceof class_8887 && this.C.getValue()
               || var1 instanceof class_2589 && this.v.getValue()
               || var1 instanceof class_4482 && this.O.getValue()
               || var1 instanceof class_8172 && this.Q.getValue()
               || var1 instanceof class_7716 && this.B.getValue()
               || var1 instanceof class_2619 && this.r.getValue();
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.D != mc.field_1687) {
            this.g.clear();
            this.D = mc.field_1687;
            this.t = 0;
         }

         int var10000 = this.t;
         byte var11 = 1;
         int var10 = this.t;
         this.t = var10 + var11;
         var10000 %= 10;
         boolean var13 = false;
         int var12 = var10000;
         var10000 = Integer.signum(var12);
         if (((var10000 | -var10000) >>> 31 ^ 1) != 0) {
            this.g.clear();
            int var1 = (int)Math.ceil(this.c.getValue() / 16.0) + 1;
            class_1923 var2 = mc.field_1724.method_31476();
            int var9 = var1;
            int var8 = var2.field_9181;

            for (int var3 = var8 - var9; var3 <= var2.field_9181 + var1; var3++) {
               for (int var4 = var2.field_9180 - var1; var4 <= var2.field_9180 + var1; var4++) {
                  class_2818 var5 = mc.field_1687.method_2935().method_21730(var3, var4);
                  if (var5 != null) {
                     for (class_2586 var7 : var5.method_12214().values()) {
                        if (!var7.method_11015()
                           && this.w(var7)
                           && mc.field_1724.method_5707(class_243.method_24953(var7.method_11016())) <= this.c.getValue() * this.c.getValue()) {
                           this.g.add(var7);
                        }
                     }
                  }
               }
            }

            this.g.sort(Comparator.comparingDouble(var0 -> mc.field_1724.method_5707(class_243.method_24953(var0.method_11016()))));
            var10000 = this.g.size();
            int var15 = this.Y.getValue().intValue();
            int var14 = var10000;
            if ((-Integer.compare(var14, var15) >>> 31 ^ 1) == 0) {
               this.g.subList(this.Y.getValue().intValue(), this.g.size()).clear();
            }
         }
      }
   }

   private boolean p(class_2586 var1) {
      if (!var1.method_11015() && this.w(var1)) {
         class_2338 var2 = var1.method_11016();
         class_2818 var3 = mc.field_1687.method_2935().method_21730(var2.method_10263() >> 4, var2.method_10260() >> 4);
         return var3 != null && var3.method_12214().get(var2) == var1;
      } else {
         return false;
      }
   }

   private class_238 Y(class_2586 var1) {
      class_2338 var2 = var1.method_11016();
      class_265 var3 = var1.method_11010().method_26218(mc.field_1687, var2);
      return (var3.method_1110() ? new class_238(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) : var3.method_1107()).method_996(var2).method_1014(0.003);
   }

   private int u(class_2586 var1, double var2) {
      int var4 = var1 instanceof class_2636 ? -943189 : (var1 instanceof class_2611 ? -5071873 : (var1 instanceof class_2627 ? -1400362 : -1521772));
      return this.d.C(var4, var2, this.c.getValue(), 1.0, var1.method_11016().hashCode());
   }

   private void T() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.D == mc.field_1687) {
            try {
               int var1 = 0;
               int var2 = 0;
               class_4184 var3 = mc.field_1773.method_19418();
               class_243 var4 = this.W.getValue() ? J(var3.method_71156(), var3.method_23767(), mc.field_1724.method_73189(), this.f.getValue()) : null;

               for (class_2586 var6 : this.g) {
                  if (this.p(var6)) {
                     double var7 = mc.field_1724.method_73189().method_1022(class_243.method_24953(var6.method_11016()));
                     if (!(var7 >= this.c.getValue())) {
                        if (var1++ >= this.Y.getValue().intValue()) {
                           break;
                        }

                        class_238 var9 = this.Y(var6);
                        int var10 = this.u(var6, var7);
                        if (this.U.getValue()) {
                           this.d.X(var9, this.I.getValue(), var10);
                        }

                        if (var4 != null && var7 <= this.M.getValue() && var2++ < this.h.getValue().intValue() && var10 >>> 24 != 0) {
                           this.d.O(var4, var9.method_1005(), var10);
                        }
                     }
                  }
               }
            } catch (RuntimeException var11) {
               q8.O("ESP", "Storage overlay failed", var11);
            }
         }
      }
   }

   private void b(class_332 var1, class_9779 var2) {
      if (this.y.getValue() && this.isRuntimeActive() && this.isInGame() && this.D == mc.field_1687 && !mc.field_1690.field_1842) {
         var1 = wi.p(var1, true, false);
         if (var1 != null) {
            long var3 = c5.X();

            try {
               class_4184 var5 = mc.field_1773.method_19418();
               class_243 var6 = var5.method_71156();
               Matrix4f var7 = b9.n(new Matrix4f());
               Vector3f var8 = var5.method_23767().transform(new Vector3f(0.0F, 1.0F, 0.0F));
               int var9 = mc.method_22683().method_4486();
               int var10 = mc.method_22683().method_4502();
               byte var11 = 0;
               float var12 = this.J.getValue().floatValue();
               int var13 = this.E.getValue().intValue();
               HashSet var14 = new HashSet();
               ArrayList var15 = new ArrayList();
               ArrayList var16 = new ArrayList();

               for (class_2586 var18 : this.g) {
                  if (var16.size() >= var13) {
                     break;
                  }

                  if (this.p(var18)) {
                     class_2338 var19 = var18.method_11016();
                     double var20 = mc.field_1724.method_73189().method_1022(class_243.method_24953(var19));
                     if (!(var20 >= this.c.getValue()) && !(var20 > this.u.getValue())) {
                        class_2338 var22 = Q(var18);
                        class_238 var23 = this.Y(var18);
                        if (var22 != null) {
                           if (var14.contains(var22)) {
                              continue;
                           }

                           if (mc.field_1687.method_8321(var22) instanceof class_2595 var24 && this.p(var24)) {
                              var23 = var23.method_991(this.Y(var24));
                           } else {
                              var22 = null;
                           }
                        }

                        var14.add(var19);
                        class_243 var43 = new class_243(
                           (var23.field_1323 + var23.field_1320) / 2.0, var23.field_1325 + 0.25, (var23.field_1321 + var23.field_1324) / 2.0
                        );
                        eg.o var44 = eg.y(var43, var6, var7, var8, var9, var10, (boolean)var11);
                        if (var44 != null) {
                           int var26 = this.u(var18, var20);
                           double var27 = (var26 >>> 24) / 255.0;
                           if (!(var27 < 0.03) && (this.d.L() || !this.y(var6, var43, var19, var22))) {
                              String var29 = var18 instanceof class_2636 var30 ? this.G(var30) : null;
                              class_5250 var45 = class_2561.method_43470(this.c(K(var18, var22 != null)) + " ")
                                 .method_10852(class_2561.method_43470(w(var29, var20)).method_54663(14211288));
                              class_1799 var31 = this.T.getValue() ? this.p(var18) : class_1799.field_8037;
                              int var32 = 1 + (var31.method_7960() ? 0 : 12) + mc.field_1772.method_27525(var45) + 1;
                              float var33 = var44.x() - var32 * var12 / 2.0F;
                              float var34 = var44.y() - 12.0F * var12;
                              float[] var35 = new float[]{var33, var34, var33 + var32 * var12, var34 + 12.0F * var12};
                              if (!this.R.getValue()
                                 || !var15.stream()
                                    .anyMatch(
                                       var1x -> var35[0] < var1x[2] + 1.0F
                                          && var35[2] + 1.0F > var1x[0]
                                          && var35[1] < var1x[3] + 1.0F
                                          && var35[3] + 1.0F > var1x[1]
                                    )) {
                                 var15.add(var35);
                                 var16.add(new StorageESPModule.f(var44.x(), var44.y(), var32, var45, var31, var27));
                              }
                           }
                        }
                     }
                  }
               }

               for (int var42 = var16.size() - 1; var42 >= 0; var42--) {
                  this.U(var1, (StorageESPModule.f)var16.get(var42), var12);
               }
            } catch (RuntimeException var39) {
               q8.O("ESP", "Storage name tags failed", var39);
            } finally {
               c5.w("StorageESP labels", var3);
            }
         }
      }
   }

   private void U(class_332 var1, StorageESPModule.f var2, float var3) {
      var1.method_51448().pushMatrix();

      try {
         var1.method_51448().translate(this.S(var2.x() - var2.width() * var3 / 2.0F), this.S(var2.y() - 12.0F * var3)).scale(var3);
         gu.Z(var1, 0, 0, var2.width(), 12, ei.R(1073741824, var2.alpha()));
         byte var4 = 1;
         if (!var2.icon().method_7960()) {
            if (var2.alpha() > 0.5) {
               var1.method_51448().pushMatrix();
               var1.method_51448().translate(var4, 1.0F).scale(0.625F);
               var1.method_51427(var2.icon(), 0, 0);
               var1.method_51448().popMatrix();
            }

            var4 += 12;
         }

         var1.method_51439(mc.field_1772, var2.line(), var4, 2, ei.R(-1, var2.alpha()), true);
      } finally {
         var1.method_51448().popMatrix();
      }
   }

   private String c(String var1) {
      return mc.field_1772.method_1727(var1) <= 160 ? var1 : mc.field_1772.method_27523(var1, 160 - mc.field_1772.method_1727("...")) + "...";
   }

   private float S(float var1) {
      double var2 = mc.method_22683().method_4495();
      return (float)(Math.round(var1 * var2) / var2);
   }

   private boolean y(class_243 var1, class_243 var2, class_2338 var3, class_2338 var4) {
      class_3965 var5 = mc.field_1687.method_17742(new class_3959(var1, var2, class_3960.field_17558, class_242.field_1348, mc.field_1724));
      return var5.method_17783() != class_240.field_1332 ? false : !var5.method_17777().equals(var3) && !var5.method_17777().equals(var4);
   }

   private class_1799 p(class_2586 var1) {
      return this.F.computeIfAbsent(var1.method_11010().method_26204(), var0 -> new class_1799(var0.method_8389()));
   }

   private String G(class_2636 var1) {
      try {
         class_1297 var2 = var1.method_11390().method_8283(mc.field_1687, var1.method_11016());
         return var2 == null ? null : var2.method_5864().method_5897().getString();
      } catch (RuntimeException var3) {
         q8.O("ESP", "Spawner mob name unavailable; tag shows the block name", var3);
         return null;
      }
   }

   private record f(float x, float y, int width, class_2561 line, class_1799 icon, double alpha) {
   }
}
