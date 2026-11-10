package com.larpclient;

import com.larpclient.module.HUDModule;
import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_10799;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_7923;

public final class w5 {
   public static final int i = 32;
   public static final int e = 12;
   public static final int r = 13;
   public static final int k = -267645934;
   private static final Map<Module, Float> R = new IdentityHashMap<>();
   private static final Map<class_2960, Float> v = new HashMap<>();
   private static Object U;
   private static Object N;
   private static long b;
   private static long B;
   private static final Map<class_2960, w5.r> a = new HashMap<>();
   public static final int l = 196;
   public static final int f = 300;
   public static final int O = 80;
   private static final int H = -41892;
   private static final int J = -30070;
   static final float E = 1.6F;
   static final float h = 12.0F;
   private static final double j = 11.0;

   private w5() {
   }

   public static void b() {
      R.clear();
      v.clear();
      a.clear();
      N = null;
      U = null;
      B = 0L;
      b = 0L;
   }

   private static void Y() {
      class_310 var0 = class_310.method_1551();
      if (U != var0.field_1687 || N != var0.field_1724) {
         b();
         U = var0.field_1687;
         N = var0.field_1724;
      }
   }

   public static int I(String var0) {
      return A(var0, 13.0);
   }

   public static int A(String var0, double var1) {
      return (int)Math.ceil(q2.U(var0, var1));
   }

   public static String O(String var0, double var1, double var3) {
      return q2.K(var0, var1, var3);
   }

   public static int p(int var0, int var1) {
      return var0 + var1 + (int)Math.ceil(54.0);
   }

   public static int w(int var0, int var1, int var2) {
      return 26 + (int)Math.ceil(16.799999999999997) + var2 + var0 + 48 + var1;
   }

   public static float D(float var0, boolean var1) {
      return P(var0, var1, 0.016666666666666666);
   }

   public static float P(float var0, boolean var1, double var2) {
      double var4 = 1.0 - Math.pow(0.85, Math.clamp(var2, 0.0, 0.25) * 60.0);
      return (float)(var0 + ((var1 ? 1 : 0) - var0) * var4);
   }

   private static double g(long var0, long var2) {
      return var0 == 0L ? 0.016666666666666666 : Math.max(0L, var2 - var0) / 1.0E9;
   }

   public static String A(int var0, boolean var1) {
      int var2 = Math.max(0, var0 / 20);
      return var1 ? "∞" : String.format(Locale.ROOT, "%d:%02d", var2 / 60, var2 % 60);
   }

   private static w5.w H(String var0, List<w5.r> var1, boolean var2) {
      int var3 = Math.max(168, I(var0) + 28 + 24);
      double var4 = 48.0;

      for (w5.r var7 : var1) {
         var3 = Math.max(var3, var7.width());
         var4 += (var2 ? 22 : 23) * var7.alpha();
      }

      return new w5.w(var0, List.copyOf(var1), var3, (int)Math.ceil(var4), var2);
   }

   public static w5.w O(boolean var0) {
      Y();
      long var1 = System.nanoTime();
      double var3 = g(b, var1);
      if (!var0) {
         b = var1;
      }

      ArrayList var5 = new ArrayList();

      for (Module var7 : LarpClient.b().G().Y()) {
         HUDModule var8 = HUDModule.y();
         int var9 = var7.getKeyBind() < 0 || !var7.isEnabled() && (var8 == null || !var8.C.G("Always")) ? 0 : 1;
         float var10 = var0 ? (var9 != 0 ? 1 : 0) : P(R.getOrDefault(var7, 0.0F), (boolean)var9, var3);
         if (var10 < 0.05F && var9 == 0) {
            if (!var0) {
               R.remove(var7);
            }
         } else {
            if (!var0) {
               R.put(var7, var10);
            }

            if (!(var10 <= 0.05F)) {
               String var11 = var7.getDisplayName();
               String var12 = var7.getKeyBind() < 0 ? "None" : s5.S(var7.getKeyBind()).toUpperCase(Locale.ROOT);
               var5.add(new w5.r(var11, var12, null, p(I(var11), I(var12)), var10));
            }
         }
      }

      if (var0 && var5.isEmpty()) {
         var5.add(new w5.r("Backtrack", "R", null, p(I("Backtrack"), I("R")), 1.0F));
         var5.add(new w5.r("Speed", "V", null, p(I("Speed"), I("V")), 1.0F));
      }

      return H("KeyBinds", var5, true);
   }

   public static w5.w l(boolean var0) {
      com.larpclient.module.r var1 = com.larpclient.module.r.m();
      String var2 = var1 == null ? "Blackjack" : var1.getDisplayName();
      List var3 = var1 == null
         ? List.of(new String[]{"Drawing", "Dealer"}, new String[]{"Player", "18"}, new String[]{"Dealer", "6"}, new String[]{"Dealer plan", "A"})
         : var1.D();
      ArrayList var4 = new ArrayList();

      for (String[] var6 : var3) {
         var4.add(new w5.r(var6[0], var6[1], null, p(I(var6[0]), I(var6[1])), 1.0F));
      }

      w5.w var7 = H(var2, var4, true);
      return new w5.w(var7.title(), var7.rows(), var7.width(), var7.height(), true, "store");
   }

   public static w5.w F(boolean var0) {
      Y();
      long var1 = System.nanoTime();
      double var3 = g(B, var1);
      if (!var0) {
         B = var1;
      }

      class_310 var5 = class_310.method_1551();
      ArrayList var6 = new ArrayList();
      HashSet var7 = new HashSet();
      if (var5.field_1724 != null) {
         for (class_1293 var9 : var5.field_1724.method_6026()) {
            class_2960 var10 = class_7923.field_41174.method_10221((class_1291)var9.method_5579().comp_349());
            var7.add(var10);
            float var11 = var0 ? 1.0F : P(v.getOrDefault(var10, 0.0F), true, var3);
            if (!var0) {
               v.put(var10, var11);
            }

            String var12 = ((class_1291)var9.method_5579().comp_349()).method_5560().getString()
               + (var9.method_5578() > 0 ? " " + (var9.method_5578() + 1) : "");
            String var13 = A(var9.method_5584(), var9.method_48559());
            class_2960 var14 = class_2960.method_60655(var10.method_12836(), "mob_effect/" + var10.method_12832());
            w5.r var15 = new w5.r(var12, var13, var14, w(I(var12), I(var13), I(">")), var11);
            var6.add(var15);
            if (!var0) {
               a.put(var10, var15);
            }
         }
      }

      if (!var0) {
         Iterator var16 = v.entrySet().iterator();

         while (var16.hasNext()) {
            Entry var18 = (Entry)var16.next();
            if (!var7.contains(var18.getKey())) {
               float var20 = P((Float)var18.getValue(), false, var3);
               if (var20 <= 0.05F) {
                  var16.remove();
                  a.remove(var18.getKey());
               } else {
                  var18.setValue(var20);
                  w5.r var21 = a.get(var18.getKey());
                  if (var21 != null) {
                     var6.add(new w5.r(var21.label(), var21.value(), var21.icon(), var21.width(), var20));
                  }
               }
            }
         }
      }

      if (var0 && var6.isEmpty()) {
         for (String[] var19 : List.of(new String[]{"Speed 2", "0:45", "speed"}, new String[]{"Strength", "0:30", "strength"})) {
            var6.add(new w5.r(var19[0], var19[1], class_2960.method_60656("mob_effect/" + var19[2]), w(I(var19[0]), I(var19[1]), I(">")), 1.0F));
         }
      }

      var6.sort(Comparator.comparingInt(w5.r::width).reversed());
      return H("Potions", var6, false);
   }

   public static void Y(class_332 var0, String var1, double var2, double var4, int var6) {
      y(var0, var1, var2, var4, 13.0, var6);
   }

   public static void y(class_332 var0, String var1, double var2, double var4, double var6, int var8) {
      q2.n(var0, var1, var2, var4, var6, var8);
   }

   public static void l(class_332 var0, String var1, int var2, int var3, int var4, int var5) {
      class_2960 var6 = class_2960.method_60655("larp-client", "textures/gui/larp/" + var1 + ".png");
      var0.method_25293(class_10799.field_56883, var6, var2, var3, 0.0F, 0.0F, var4, var4, 16, 16, 16, 16, fp.R(var5));
   }

   public static void C(class_332 var0, String var1, int var2, int var3, int var4, int var5) {
      if (!q2.w(var0, var1, var2, var3, var4, var5)) {
         lx.N(var0, lx.n(var1), var2, var3, var4, var5);
      }
   }

   private static int u(w5.w var0) {
      return var0.keys() ? 22 : 23;
   }

   public static List<cv.z> P(w5.w var0, int var1, int var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      var4.add(new cv.z(var1, var2, var0.width(), var0.height()));
      double var5 = var2 + 39;

      for (w5.r var8 : var0.rows()) {
         int var9 = var0.keys() ? 22 : 23;
         var4.add(new cv.z(var1, (int)Math.floor(var5), var0.width(), (int)Math.ceil(var9 * var8.alpha())));
         var5 += var9 * var8.alpha();
      }

      return var4;
   }

   public static void J(class_332 var0, w5.w var1, int var2, int var3, boolean var4) {
      int var5 = var1.icon() != null ? 1 : 0;
      y4.R(var0, var2, var3, var1.width(), var1.height(), var1.title(), var5 != 0 ? var1.icon() : (var1.keys() ? "keyboard" : "flask-conical"), true);
      List var6 = P(var1, var2, var3, var4);

      for (int var7 = 0; var7 < var1.rows().size(); var7++) {
         w5.r var8 = var1.rows().get(var7);
         cv.z var9 = (cv.z)var6.get(var7 + 1);
         int var10 = var9.y();
         int var11 = var9.height();
         double var12 = var8.alpha();
         var0.method_44379(var2, var10, var2 + var1.width(), var10 + var11);

         try {
            double var14 = q2.e(var10, u(var1), 13.0) - (u(var1) - var11) / 2.0;
            int var16 = var2 + 12;
            if (!var1.keys() && var5 == 0) {
               var0.method_52707(class_10799.field_56883, var8.icon(), var16, var10 + (var11 - 15) / 2, 15, 15, ei.R(-1, var12));
               var16 += 22;
            }

            int var17 = I(var8.value());
            int var18 = var17 + 12;
            int var19 = var2 + var1.width() - 12 - var17;
            int var20 = -1250068;
            if (var1.keys() && var5 == 0) {
               com.larpclient.module.lr var21 = LarpClient.b().G();
               boolean var22 = var21.M().stream().anyMatch(var1x -> var1x.isEnabled() && var1x.getDisplayName().equals(var8.label()));
               var20 = var22 ? ez.a : -1494422292;
               gu.N(var0, var19 - 6, var10 + (var11 - 17) / 2, var18, 17.0F, 4.0F, ei.R(-1275068416, var12));
            }

            String var26 = O(var8.label(), var19 - var16 - 14, 13.0);
            Y(var0, var26, var16, var14, ei.R(var20, var12));
            Y(var0, var8.value(), var19, var14, ei.R(-4671294, var12));
         } finally {
            var0.method_44380();
         }
      }
   }

   public static float[][] l(float var0, float var1, float var2, float var3, float var4) {
      var4 = Math.max(0.0F, Math.min(var4, Math.min(var2, var3) / 2.0F));
      ArrayList var5 = new ArrayList();
      var5.add(new float[]{var0, var1 + var3 / 2.0F});
      float[][] var6 = new float[][]{
         {var0 + var4, var1 + var4, (float) Math.PI},
         {var0 + var2 - var4, var1 + var4, (float) (Math.PI * 3.0 / 2.0)},
         {var0 + var2 - var4, var1 + var3 - var4, 0.0F},
         {var0 + var4, var1 + var3 - var4, (float) (Math.PI / 2)}
      };
      int var7 = Math.max(2, (int)Math.ceil(var4 / 1.5F));

      for (float[] var11 : var6) {
         for (int var12 = 0; var12 <= var7; var12++) {
            double var13 = var11[2] + (Math.PI / 2) * var12 / var7;
            var5.add(new float[]{(float)(var11[0] + Math.cos(var13) * var4), (float)(var11[1] + Math.sin(var13) * var4)});
         }
      }

      var5.add(new float[]{var0, var1 + var3 / 2.0F});
      return var5.toArray(new float[0][]);
   }

   public static float H(float[][] var0) {
      float var1 = 0.0F;

      for (int var2 = 1; var2 < var0.length; var2++) {
         var1 += (float)Math.hypot(var0[var2][0] - var0[var2 - 1][0], var0[var2][1] - var0[var2 - 1][1]);
      }

      return var1;
   }

   private static void p(class_332 var0, int var1, int var2, int var3, int var4, double var5, int var7) {
      float var8 = 0.8F;
      ws.X(var0, var1, var2, var3, var4, 12.0F, 486539263);
      if (!(var5 <= 0.001)) {
         float[][] var9 = l(var1 + var8, var2 + var8, var3 - var8 * 2.0F, var4 - var8 * 2.0F, 12.0F - var8);
         float var10 = H(var9);
         float var11 = var10 * (float)Math.min(1.0, var5);
         float var12 = 0.0F;
         int var13 = var7 == -41892 ? var7 : ei.W(var7, ez.f | 0xFF000000, 0.6);

         for (int var14 = 1; var14 < var9.length && var12 < var11; var14++) {
            float[] var15 = var9[var14 - 1];
            float[] var16 = var9[var14];
            float var17 = (float)Math.hypot(var16[0] - var15[0], var16[1] - var15[1]);
            if (!(var17 <= 0.0F)) {
               float var18 = Math.min(1.0F, (var11 - var12) / var17);
               float var19 = var15[0] + (var16[0] - var15[0]) * var18;
               float var20 = var15[1] + (var16[1] - var15[1]) * var18;
               int var21 = ei.W(var13, var7, Math.min(1.0F, (var12 + var17 * var18 / 2.0F) / Math.max(1.0F, var11)));
               gu.o(var0, var15[0], var15[1], var19, var20, 1.6F, var21);
               var12 += var17 * var18;
            }
         }
      }
   }

   public static int k(String var0, String var1, String var2, String var3, String var4) {
      int var5 = I(var0) + 28 + 24;
      int var6 = I(var1) + I(var2) + 14 + 24;
      int var7 = A(var3, 11.0) + A(var4, 11.0) + 14 + 24;
      return Math.clamp(Math.max(var5, Math.max(var6, var7)), 196, 300);
   }

   public static void e(
      class_332 var0, int var1, int var2, int var3, String var4, String var5, String var6, String var7, String var8, String var9, double var10, boolean var12
   ) {
      y4.R(var0, var1, var2, var3, 80, var4, var5, true);
      int var13 = var1 + 12;
      int var14 = var1 + var3 - 12;
      int var15 = var12 ? -41892 : ez.a | 0xFF000000;
      double var16 = q2.e(var2 + 37, 18.0, 13.0);
      int var18 = var7.isEmpty() ? 0 : I(var7);
      y(var0, O(var6, var14 - var13 - var18 - 10, 13.0), var13, var16, 13.0, var12 ? -30070 : -1250068);
      if (!var7.isEmpty()) {
         y(var0, var7, var14 - var18, var16, 13.0, var15);
      }

      p(var0, var1, var2, var3, 80, var10, var15);
      double var19 = q2.g(var2 + 57, 14.0, 11.0);
      int var21 = var9.isEmpty() ? 0 : A(var9, 11.0);
      y(var0, O(var8, var14 - var13 - var21 - (var9.isEmpty() ? 0 : 10), 11.0), var13, var19, 11.0, -4671294);
      if (!var9.isEmpty()) {
         y(var0, var9, var14 - var21, var19, 11.0, -7697770);
      }
   }

   public record r(String label, String value, class_2960 icon, int width, float alpha) {
   }

   public record w(String title, List<w5.r> rows, int width, int height, boolean keys, String icon) {
      public w(String var1, List<w5.r> var2, int var3, int var4, boolean var5) {
         this(var1, var2, var3, var4, var5, null);
      }
   }
}
