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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class wb {
   public static final int k = 5;
   public static final int a = 3;
   private static final String U = "items.json";
   private static final Gson c = new GsonBuilder().setPrettyPrinting().create();

   private wb() {
   }

   private static int[] q(int var0) {
      int[] var1 = new int[var0];
      Arrays.fill(var1, -1);
      return var1;
   }

   public static int[] S() {
      return q(15);
   }

   public static int[] N(int[] var0) {
      if (var0 != null && var0.length == 15) {
         int[] var1 = (int[])var0.clone();

         for (int var2 = 0; var2 < var1.length; var2++) {
            if (var1[var2] < 1 || var1[var2] > 9) {
               var1[var2] = -1;
            }
         }

         return var1;
      } else {
         return S();
      }
   }

   private static Path R() {
      return ht.U("items.json");
   }

   public static synchronized void a(a6 var0) {
      Path var1 = R();
      if (Files.exists(var1)) {
         try {
            JsonElement var2 = JsonParser.parseString(Files.readString(var1, StandardCharsets.UTF_8));
            if (var2 == null || !var2.isJsonObject()) {
               throw new IOException("not a JSON object");
            }

            wb.p var3 = (wb.p)c.fromJson((JsonObject)var2, wb.p.class);
            if (var3 == null) {
               throw new IOException("empty");
            }

            R(var0, var3);
         } catch (Exception var4) {
            g(var1, var4);
         }
      }
   }

   public static synchronized void e(a6 var0) {
      wb.p var1 = new wb.p();
      L(var0, var1);
      Path var2 = R();

      try {
         Files.createDirectories(var2.getParent());
         Path var3 = var2.resolveSibling(var2.getFileName() + ".tmp");
         Files.writeString(var3, c.toJson(var1), StandardCharsets.UTF_8);

         try {
            Files.move(var3, var2, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (IOException var5) {
            Files.move(var3, var2, StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (IOException var6) {
         _q.R("Rig", "Could not save casino game data: {}", var6.getMessage());
      }
   }

   private static void g(Path var0, Exception var1) {
      _q.R("Rig", "Casino game data was unreadable ({}); starting from defaults", var1.getMessage());

      try {
         Files.move(var0, var0.resolveSibling(var0.getFileName() + ".bad"), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException var3) {
      }
   }

   public static void L(a6 var0, wb.p var1) {
      qo var2 = qo.Z();
      p2 var3 = p2.p();
      pp var4 = pp.A();
      var1.H = var2.a();
      var1.Z = var2.p();
      var1.x = var2.l().name();
      var1.T = var2.o();
      var1.Q = var2.R();
      var1.Y = B(var2.P);
      var1.u = B(var2.i);
      var1.A = W(var2, true);
      var1.N = W(var2, false);
      var1.j = K(var2, true);
      var1.e = K(var2, false);
      var1.r = B(var3.S);
      var1.B = B(var3.O);
      var1.m = B(var3.G);
      var1.d = var3.z.name();
      var1.c = var3.e();
      var1.J = B(var4.E);
      var1.C = B(var4.n);
      vy var5 = vy.w();
      var1.P = new int[10];

      for (int var6 = 1; var6 <= 10; var6++) {
         var1.P[var6 - 1] = c(var6) ? var5.getTemplateIdForValue(var6) : -1;
      }

      var1.M = q5.a().x();
      vz var9 = vz.E();
      int[] var7 = var9.Q();
      var1.V = new int[var7.length];

      for (int var8 = 0; var8 < var7.length; var8++) {
         var1.V[var8] = var9.getTemplateIdForValue(var7[var8]);
      }

      var1.n = var9.F();
      var1.K = p(vf.x());
      var1.L = p(v2.L());
      var1.g = var0.c();
   }

   public static void R(a6 var0, wb.p var1) {
      qo var2 = qo.Z();
      p2 var3 = p2.p();
      pp var4 = pp.A();
      var2.M(var1.T);
      var2.X(var1.Q);
      var2.X(var1.x == null ? qo.s.z.name().toLowerCase() : var1.x.toLowerCase());
      var2.B(var1.Z);
      var2.P = A(var1.Y, var2.P);
      var2.i = A(var1.u, var2.i);
      var2.Q(var1.H);
      k(var2, var1.A, true);
      M(var2, var1.j, true);
      if (!var1.H) {
         k(var2, var1.N, false);
         M(var2, var1.e, false);
      }

      var3.S = A(var1.r, var3.S);
      var3.O = A(var1.B, var3.O);
      var3.G = A(var1.m, var3.G);
      var3.z = R(var1.d, var3.z);
      var3.U(var1.c);
      var4.E = A(var1.J, var4.E);
      var4.n = A(var1.C, var4.n);
      vy var5 = vy.w();

      for (int var6 = 1; var6 <= 10; var6++) {
         int var7 = c(var6) && var1.P != null && var1.P.length >= var6 ? var1.P[var6 - 1] : -1;
         var5.setTemplateIdForValue(var6, var7);
      }

      q5.a().b(var1.M);
      vz var9 = vz.E();
      int[] var10 = var9.Q();

      for (int var8 = 0; var8 < var10.length; var8++) {
         var9.setTemplateIdForValue(var10[var8], var1.V != null && var1.V.length > var8 ? var1.V[var8] : -1);
      }

      var9.d(var1.n);
      F(vf.x(), var1.K != null ? var1.K : var1.P);
      F(v2.L(), var1.L != null ? var1.L : var1.K);
      var0.t(var1.g);
   }

   private static boolean c(int var0) {
      return var0 == 1 || var0 >= 3 && var0 <= 10;
   }

   private static int[] p(vy var0) {
      int[] var1 = new int[9];

      for (int var2 = 1; var2 <= 9; var2++) {
         var1[var2 - 1] = var0.getTemplateIdForValue(var2);
      }

      return var1;
   }

   private static void F(vy var0, int[] var1) {
      for (int var2 = 1; var2 <= 9; var2++) {
         var0.setTemplateIdForValue(var2, var1 != null && var1.length >= var2 ? var1[var2 - 1] : -1);
      }
   }

   private static Item A(String var0, Item var1) {
      if (var0 != null && !var0.isBlank()) {
         try {
            return (Item)BuiltInRegistries.ITEM.getValue(Identifier.parse(var0));
         } catch (Exception var3) {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private static String B(Item var0) {
      return BuiltInRegistries.ITEM.getKey(var0).toString();
   }

   private static p2.v R(String var0, p2.v var1) {
      if (var0 == null) {
         return var1;
      }

      try {
         return p2.v.valueOf(var0);
      } catch (IllegalArgumentException var3) {
         return var1;
      }
   }

   private static void k(qo var0, String[] var1, boolean var2) {
      if (var1 != null && var1.length >= 9) {
         for (int var3 = 0; var3 < 9; var3++) {
            var0.N(var2, var3, A(var1[var3], var0.F(var2, var3)));
         }
      }
   }

   private static String[] W(qo var0, boolean var1) {
      String[] var2 = new String[9];

      for (int var3 = 0; var3 < 9; var3++) {
         Item var4 = var0.F(var1, var3);
         var2[var3] = B(var4 == null ? Items.PAPER : var4);
      }

      return var2;
   }

   private static void M(qo var0, String[] var1, boolean var2) {
      if (var1 != null && var1.length >= 9) {
         RegistryOps var3 = VanillaRegistries.createWorldLookup().createSerializationContext(JsonOps.INSTANCE);

         for (int var4 = 0; var4 < 9; var4++) {
            if (var1[var4] != null && !var1[var4].isBlank()) {
               try {
                  ItemStack var5 = ItemStack.CODEC.parse(var3, JsonParser.parseString(var1[var4])).result().orElse(ItemStack.EMPTY);
                  if (!var5.isEmpty()) {
                     var0.l(var2, var4, var5);
                  }
               } catch (Exception var6) {
                  _q.R("Rig", "Could not restore Paper item metadata for slot {}", var4 + 1);
               }
            }
         }
      }
   }

   private static String[] K(qo var0, boolean var1) {
      RegistryOps var2 = VanillaRegistries.createWorldLookup().createSerializationContext(JsonOps.INSTANCE);
      String[] var3 = new String[9];

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var0.h(var1, var4);
         var3[var4] = var5 != null && !var5.isEmpty() ? ItemStack.CODEC.encodeStart(var2, var5).result().<String>map(c::toJson).orElse("") : "";
      }

      return var3;
   }

   public static final class p {
      public boolean H = (boolean)1;
      public boolean Z = (boolean)0;
      public String x = qo.s.z.name();
      public String T = "Host";
      public String Q = "Viewer";
      public String Y = "minecraft:apple";
      public String u = "minecraft:diamond";
      public String[] A;
      public String[] N;
      public String[] j;
      public String[] e;
      public String r = "minecraft:stone";
      public String B = "minecraft:cobblestone";
      public String m = "minecraft:diamond";
      public String d = p2.v.g.name();
      public int[] c;
      public String J = "minecraft:tnt";
      public String C = "minecraft:paper";
      public int[] P = wb.q(10);
      public int[] M = wb.q(11);
      public int[] V = wb.q(8);
      public int n = 0;
      public int[] K = wb.q(9);
      public int[] L = wb.q(9);
      public int[] g = wb.S();
   }
}
