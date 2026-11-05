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

public final class a {
   public static final int J = 5;
   public static final int x = 3;
   private static final String r = "items.json";
   private static final Gson h = new GsonBuilder().setPrettyPrinting().create();

   private a() {
   }

   private static int[] t(int var0) {
      int[] var1 = new int[var0];
      Arrays.fill(var1, -1);
      return var1;
   }

   public static int[] y() {
      return t(15);
   }

   public static int[] c(int[] var0) {
      if (var0 != null && var0.length == 15) {
         int[] var1 = (int[])var0.clone();

         for (int var2 = 0; var2 < var1.length; var2++) {
            if (var1[var2] < 1 || var1[var2] > 9) {
               var1[var2] = -1;
            }
         }

         return var1;
      } else {
         return y();
      }
   }

   private static Path i() {
      return ma.h("items.json");
   }

   public static synchronized void r(l6 var0) {
      Path var1 = i();
      if (Files.exists(var1)) {
         try {
            JsonElement var2 = JsonParser.parseString(Files.readString(var1, StandardCharsets.UTF_8));
            if (var2 == null || !var2.isJsonObject()) {
               throw new IOException("not a JSON object");
            }

            a.n var3 = (a.n)h.fromJson((JsonObject)var2, a.n.class);
            if (var3 == null) {
               throw new IOException("empty");
            }

            B(var0, var3);
         } catch (Exception var4) {
            X(var1, var4);
         }
      }
   }

   public static synchronized void p(l6 var0) {
      a.n var1 = new a.n();
      L(var0, var1);
      Path var2 = i();

      try {
         Files.createDirectories(var2.getParent());
         Path var3 = var2.resolveSibling(var2.getFileName() + ".tmp");
         Files.writeString(var3, h.toJson(var1), StandardCharsets.UTF_8);

         try {
            Files.move(var3, var2, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (IOException var5) {
            Files.move(var3, var2, StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (IOException var6) {
         r4.m("Rig", "Could not save casino game data: {}", var6.getMessage());
      }
   }

   private static void X(Path var0, Exception var1) {
      r4.m("Rig", "Casino game data was unreadable ({}); starting from defaults", var1.getMessage());

      try {
         Files.move(var0, var0.resolveSibling(var0.getFileName() + ".bad"), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException var3) {
      }
   }

   public static void L(l6 var0, a.n var1) {
      oi var2 = oi.A();
      xa var3 = xa.r();
      js var4 = js.A();
      var1.f = var2.B();
      var1.F = var2.z();
      var1.m = var2.u().name();
      var1.x = var2.Z();
      var1.Q = var2.Q();
      var1.D = N(var2.g);
      var1.S = N(var2.j);
      var1.p = A(var2, true);
      var1.g = A(var2, false);
      var1.i = q(var2, true);
      var1.z = q(var2, false);
      var1.j = N(var3.c);
      var1.c = N(var3.N);
      var1.h = N(var3.b);
      var1.N = var3.Y.name();
      var1.b = var3.u();
      var1.C = N(var4.E);
      var1.W = N(var4.Z);
      ug var5 = ug.w();
      var1.k = new int[10];

      for (int var6 = 1; var6 <= 10; var6++) {
         var1.k[var6 - 1] = S(var6) ? var5.getTemplateIdForValue(var6) : -1;
      }

      var1.X = mm.W().S();
      ue var9 = ue.V();
      int[] var7 = var9.R();
      var1.T = new int[var7.length];

      for (int var8 = 0; var8 < var7.length; var8++) {
         var1.T[var8] = var9.getTemplateIdForValue(var7[var8]);
      }

      var1.A = var9.W();
      var1.n = P(u3.H());
      var1.v = P(uu.M());
      var1.a = var0.c();
   }

   public static void B(l6 var0, a.n var1) {
      oi var2 = oi.A();
      xa var3 = xa.r();
      js var4 = js.A();
      var2.r(var1.x);
      var2.L(var1.Q);
      var2.W(var1.m == null ? oi.x.m.name().toLowerCase() : var1.m.toLowerCase());
      var2.W(var1.F);
      var2.g = e(var1.D, var2.g);
      var2.j = e(var1.S, var2.j);
      var2.e(var1.f);
      u(var2, var1.p, true);
      B(var2, var1.i, true);
      if (!var1.f) {
         u(var2, var1.g, false);
         B(var2, var1.z, false);
      }

      var3.c = e(var1.j, var3.c);
      var3.N = e(var1.c, var3.N);
      var3.b = e(var1.h, var3.b);
      var3.Y = Z(var1.N, var3.Y);
      var3.Z(var1.b);
      var4.E = e(var1.C, var4.E);
      var4.Z = e(var1.W, var4.Z);
      ug var5 = ug.w();

      for (int var6 = 1; var6 <= 10; var6++) {
         int var7 = S(var6) && var1.k != null && var1.k.length >= var6 ? var1.k[var6 - 1] : -1;
         var5.setTemplateIdForValue(var6, var7);
      }

      mm.W().R(var1.X);
      ue var9 = ue.V();
      int[] var10 = var9.R();

      for (int var8 = 0; var8 < var10.length; var8++) {
         var9.setTemplateIdForValue(var10[var8], var1.T != null && var1.T.length > var8 ? var1.T[var8] : -1);
      }

      var9.X(var1.A);
      V(u3.H(), var1.n != null ? var1.n : var1.k);
      V(uu.M(), var1.v != null ? var1.v : var1.n);
      var0.L(var1.a);
   }

   private static boolean S(int var0) {
      return var0 == 1 || var0 >= 3 && var0 <= 10;
   }

   private static int[] P(ug var0) {
      int[] var1 = new int[9];

      for (int var2 = 1; var2 <= 9; var2++) {
         var1[var2 - 1] = var0.getTemplateIdForValue(var2);
      }

      return var1;
   }

   private static void V(ug var0, int[] var1) {
      for (int var2 = 1; var2 <= 9; var2++) {
         var0.setTemplateIdForValue(var2, var1 != null && var1.length >= var2 ? var1[var2 - 1] : -1);
      }
   }

   private static Item e(String var0, Item var1) {
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

   private static String N(Item var0) {
      return BuiltInRegistries.ITEM.getKey(var0).toString();
   }

   private static xa.i Z(String var0, xa.i var1) {
      if (var0 == null) {
         return var1;
      }

      try {
         return xa.i.valueOf(var0);
      } catch (IllegalArgumentException var3) {
         return var1;
      }
   }

   private static void u(oi var0, String[] var1, boolean var2) {
      if (var1 != null && var1.length >= 9) {
         for (int var3 = 0; var3 < 9; var3++) {
            var0.w(var2, var3, e(var1[var3], var0.Q(var2, var3)));
         }
      }
   }

   private static String[] A(oi var0, boolean var1) {
      String[] var2 = new String[9];

      for (int var3 = 0; var3 < 9; var3++) {
         Item var4 = var0.Q(var1, var3);
         var2[var3] = N(var4 == null ? Items.PAPER : var4);
      }

      return var2;
   }

   private static void B(oi var0, String[] var1, boolean var2) {
      if (var1 != null && var1.length >= 9) {
         RegistryOps var3 = VanillaRegistries.createWorldLookup().createSerializationContext(JsonOps.INSTANCE);

         for (int var4 = 0; var4 < 9; var4++) {
            if (var1[var4] != null && !var1[var4].isBlank()) {
               try {
                  ItemStack var5 = ItemStack.CODEC.parse(var3, JsonParser.parseString(var1[var4])).result().orElse(ItemStack.EMPTY);
                  if (!var5.isEmpty()) {
                     var0.t(var2, var4, var5);
                  }
               } catch (Exception var6) {
                  r4.m("Rig", "Could not restore Paper item metadata for slot {}", var4 + 1);
               }
            }
         }
      }
   }

   private static String[] q(oi var0, boolean var1) {
      RegistryOps var2 = VanillaRegistries.createWorldLookup().createSerializationContext(JsonOps.INSTANCE);
      String[] var3 = new String[9];

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var0.y(var1, var4);
         var3[var4] = var5 != null && !var5.isEmpty() ? ItemStack.CODEC.encodeStart(var2, var5).result().<String>map(h::toJson).orElse("") : "";
      }

      return var3;
   }

   public static final class n {
      public boolean f = (boolean)1;
      public boolean F = (boolean)0;
      public String m = oi.x.m.name();
      public String x = "Host";
      public String Q = "Viewer";
      public String D = "minecraft:apple";
      public String S = "minecraft:diamond";
      public String[] p;
      public String[] g;
      public String[] i;
      public String[] z;
      public String j = "minecraft:stone";
      public String c = "minecraft:cobblestone";
      public String h = "minecraft:diamond";
      public String N = xa.i.y.name();
      public int[] b;
      public String C = "minecraft:tnt";
      public String W = "minecraft:paper";
      public int[] k = a.t(10);
      public int[] X = a.t(11);
      public int[] T = a.t(8);
      public int A = 0;
      public int[] n = a.t(9);
      public int[] v = a.t(9);
      public int[] a = a.y();
   }
}
