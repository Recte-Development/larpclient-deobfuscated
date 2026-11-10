package com.larpclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2960;
import net.minecraft.class_6903;
import net.minecraft.class_7887;
import net.minecraft.class_7923;

public final class in {
   public static final int f = 5;
   public static final int d = 3;
   private static final String W = "items.json";
   private static final Gson g = new GsonBuilder().setPrettyPrinting().create();

   private in() {
   }

   private static int[] M(int var0) {
      int[] var1 = new int[var0];
      Arrays.fill(var1, -1);
      return var1;
   }

   public static int[] O() {
      return M(15);
   }

   public static int[] L(int[] var0) {
      if (var0 != null && var0.length == 15) {
         int[] var1 = (int[])var0.clone();

         for (int var2 = 0; var2 < var1.length; var2++) {
            if (var1[var2] < 1 || var1[var2] > 9) {
               var1[var2] = -1;
            }
         }

         return var1;
      } else {
         return O();
      }
   }

   private static Path z() {
      return j6.l("items.json");
   }

   public static synchronized void n(q var0) {
      Path var1 = z();
      if (Files.exists(var1)) {
         try {
            JsonElement var2 = JsonParser.parseString(Files.readString(var1, StandardCharsets.UTF_8));
            if (var2 == null || !var2.isJsonObject()) {
               throw new IOException("not a JSON object");
            }

            in.t var3 = (in.t)g.fromJson((JsonObject)var2, in.t.class);
            if (var3 == null) {
               throw new IOException("empty");
            }

            C(var0, var3);
         } catch (Exception var4) {
            O(var1, var4);
         }
      }
   }

   public static synchronized void l(q var0) {
      in.t var1 = new in.t();
      u(var0, var1);
      Path var2 = z();

      try {
         Files.createDirectories(var2.getParent());
         Path var3 = var2.resolveSibling(var2.getFileName() + ".tmp");
         Files.writeString(var3, g.toJson(var1), StandardCharsets.UTF_8);

         try {
            Files.move(var3, var2, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (IOException var5) {
            Files.move(var3, var2, StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (IOException var6) {
         q8.L("Rig", "Could not save casino game data: {}", var6.getMessage());
      }
   }

   private static void O(Path var0, Exception var1) {
      q8.L("Rig", "Casino game data was unreadable ({}); starting from defaults", var1.getMessage());

      try {
         Files.move(var0, var0.resolveSibling(var0.getFileName() + ".bad"), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException var3) {
      }
   }

   public static void u(q var0, in.t var1) {
      t3 var2 = t3.c();
      ux var3 = ux.o();
      yb var4 = yb.b();
      var1.y = var2.x();
      var1.e = var2.R();
      var1.j = var2.Y().name();
      var1.V = var2.c();
      var1.X = var2.G();
      var1.Z = J(var2.v);
      var1.M = J(var2.t);
      var1.v = b(var2, true);
      var1.L = b(var2, false);
      var1.I = z(var2, true);
      var1.p = z(var2, false);
      var1.N = J(var3.Q);
      var1.P = J(var3.D);
      var1.Q = J(var3.S);
      var1.O = var3.z.name();
      var1.b = var3.o();
      var1.A = J(var4.P);
      var1.S = J(var4.Y);
      q7 var5 = q7.u();
      var1.o = new int[10];

      for (int var6 = 1; var6 <= 10; var6++) {
         var1.o[var6 - 1] = i(var6) ? var5.getTemplateIdForValue(var6) : -1;
      }

      var1.E = tw.s().p();
      qc var9 = qc.i();
      int[] var7 = var9.S();
      var1.f = new int[var7.length];

      for (int var8 = 0; var8 < var7.length; var8++) {
         var1.f[var8] = var9.getTemplateIdForValue(var7[var8]);
      }

      var1.F = var9.a();
      var1.K = V(qq.f());
      var1.r = V(ql.Y());
      var1.C = var0.n();
   }

   public static void C(q var0, in.t var1) {
      t3 var2 = t3.c();
      ux var3 = ux.o();
      yb var4 = yb.b();
      var2.b(var1.V);
      var2.f(var1.X);
      var2.O(var1.j == null ? t3.v.d.name().toLowerCase() : var1.j.toLowerCase());
      var2.e(var1.e);
      var2.v = F(var1.Z, var2.v);
      var2.t = F(var1.M, var2.t);
      var2.z(var1.y);
      j(var2, var1.v, true);
      J(var2, var1.I, true);
      if (!var1.y) {
         j(var2, var1.L, false);
         J(var2, var1.p, false);
      }

      var3.Q = F(var1.N, var3.Q);
      var3.D = F(var1.P, var3.D);
      var3.S = F(var1.Q, var3.S);
      var3.z = r(var1.O, var3.z);
      var3.g(var1.b);
      var4.P = F(var1.A, var4.P);
      var4.Y = F(var1.S, var4.Y);
      q7 var5 = q7.u();

      for (int var6 = 1; var6 <= 10; var6++) {
         int var7 = i(var6) && var1.o != null && var1.o.length >= var6 ? var1.o[var6 - 1] : -1;
         var5.setTemplateIdForValue(var6, var7);
      }

      tw.s().I(var1.E);
      qc var9 = qc.i();
      int[] var10 = var9.S();

      for (int var8 = 0; var8 < var10.length; var8++) {
         var9.setTemplateIdForValue(var10[var8], var1.f != null && var1.f.length > var8 ? var1.f[var8] : -1);
      }

      var9.p(var1.F);
      y(qq.f(), var1.K != null ? var1.K : var1.o);
      y(ql.Y(), var1.r != null ? var1.r : var1.K);
      var0.X(var1.C);
   }

   private static boolean i(int var0) {
      return var0 == 1 || var0 >= 3 && var0 <= 10;
   }

   private static int[] V(q7 var0) {
      int[] var1 = new int[9];

      for (int var2 = 1; var2 <= 9; var2++) {
         var1[var2 - 1] = var0.getTemplateIdForValue(var2);
      }

      return var1;
   }

   private static void y(q7 var0, int[] var1) {
      for (int var2 = 1; var2 <= 9; var2++) {
         var0.setTemplateIdForValue(var2, var1 != null && var1.length >= var2 ? var1[var2 - 1] : -1);
      }
   }

   private static class_1792 F(String var0, class_1792 var1) {
      if (var0 != null && !var0.isBlank()) {
         try {
            return (class_1792)class_7923.field_41178.method_63535(class_2960.method_60654(var0));
         } catch (Exception var3) {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private static String J(class_1792 var0) {
      return class_7923.field_41178.method_10221(var0).toString();
   }

   private static ux.k r(String var0, ux.k var1) {
      if (var0 == null) {
         return var1;
      }

      try {
         return ux.k.valueOf(var0);
      } catch (IllegalArgumentException var3) {
         return var1;
      }
   }

   private static void j(t3 var0, String[] var1, boolean var2) {
      if (var1 != null && var1.length >= 9) {
         for (int var3 = 0; var3 < 9; var3++) {
            var0.n(var2, var3, F(var1[var3], var0.G(var2, var3)));
         }
      }
   }

   private static String[] b(t3 var0, boolean var1) {
      String[] var2 = new String[9];

      for (int var3 = 0; var3 < 9; var3++) {
         class_1792 var4 = var0.G(var1, var3);
         var2[var3] = J(var4 == null ? class_1802.field_8407 : var4);
      }

      return var2;
   }

   private static void J(t3 var0, String[] var1, boolean var2) {
      if (var1 != null && var1.length >= 9) {
         class_6903 var3 = class_7887.method_46817().method_57093(JsonOps.INSTANCE);

         for (int var4 = 0; var4 < 9; var4++) {
            if (var1[var4] != null && !var1[var4].isBlank()) {
               try {
                  class_1799 var5 = class_1799.field_24671.parse(var3, JsonParser.parseString(var1[var4])).result().orElse(class_1799.field_8037);
                  if (!var5.method_7960()) {
                     var0.f(var2, var4, var5);
                  }
               } catch (Exception var6) {
                  q8.L("Rig", "Could not restore Paper item metadata for slot {}", var4 + 1);
               }
            }
         }
      }
   }

   private static String[] z(t3 var0, boolean var1) {
      class_6903 var2 = class_7887.method_46817().method_57093(JsonOps.INSTANCE);
      String[] var3 = new String[9];

      for (int var4 = 0; var4 < 9; var4++) {
         class_1799 var5 = var0.s(var1, var4);
         var3[var4] = var5 != null && !var5.method_7960() ? class_1799.field_24671.encodeStart(var2, var5).result().<String>map(g::toJson).orElse("") : "";
      }

      return var3;
   }

   public static final class t {
      public boolean y = (boolean)1;
      public boolean e = (boolean)0;
      public String j = t3.v.d.name();
      public String V = "Host";
      public String X = "Viewer";
      public String Z = "minecraft:apple";
      public String M = "minecraft:diamond";
      public String[] v;
      public String[] L;
      public String[] I;
      public String[] p;
      public String N = "minecraft:stone";
      public String P = "minecraft:cobblestone";
      public String Q = "minecraft:diamond";
      public String O = ux.k.L.name();
      public int[] b;
      public String A = "minecraft:tnt";
      public String S = "minecraft:paper";
      public int[] o = in.M(10);
      public int[] E = in.M(11);
      public int[] f = in.M(8);
      public int F = 0;
      public int[] K = in.M(9);
      public int[] r = in.M(9);
      public int[] C = in.O();
   }
}
