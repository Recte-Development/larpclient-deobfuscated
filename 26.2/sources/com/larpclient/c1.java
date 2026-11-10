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

public final class c1 {
   public static final int X = 5;
   public static final int M = 3;
   private static final String U = "items.json";
   private static final Gson s = new GsonBuilder().setPrettyPrinting().create();

   private c1() {
   }

   private static int[] J(int var0) {
      int[] var1 = new int[var0];
      Arrays.fill(var1, -1);
      return var1;
   }

   public static int[] T() {
      return J(15);
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
         return T();
      }
   }

   private static Path j() {
      return h5.G("items.json");
   }

   public static synchronized void D(sg var0) {
      Path var1 = j();
      if (Files.exists(var1)) {
         try {
            JsonElement var2 = JsonParser.parseString(Files.readString(var1, StandardCharsets.UTF_8));
            if (var2 == null || !var2.isJsonObject()) {
               throw new IOException("not a JSON object");
            }

            c1.d var3 = (c1.d)s.fromJson((JsonObject)var2, c1.d.class);
            if (var3 == null) {
               throw new IOException("empty");
            }

            O(var0, var3);
         } catch (Exception var4) {
            k(var1, var4);
         }
      }
   }

   public static synchronized void n(sg var0) {
      c1.d var1 = new c1.d();
      a(var0, var1);
      Path var2 = j();

      try {
         Files.createDirectories(var2.getParent());
         Path var3 = var2.resolveSibling(var2.getFileName() + ".tmp");
         Files.writeString(var3, s.toJson(var1), StandardCharsets.UTF_8);

         try {
            Files.move(var3, var2, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (IOException var5) {
            Files.move(var3, var2, StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (IOException var6) {
         h.g("Rig", "Could not save casino game data: {}", var6.getMessage());
      }
   }

   private static void k(Path var0, Exception var1) {
      h.g("Rig", "Casino game data was unreadable ({}); starting from defaults", var1.getMessage());

      try {
         Files.move(var0, var0.resolveSibling(var0.getFileName() + ".bad"), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException var3) {
      }
   }

   public static void a(sg var0, c1.d var1) {
      g5 var2 = g5.a();
      t5 var3 = t5.O();
      pq var4 = pq.Q();
      var1.d = var2.g();
      var1.j = var2.b();
      var1.I = var2.k().name();
      var1.b = var2.e();
      var1.F = var2.c();
      var1.O = k(var2.l);
      var1.A = k(var2.i);
      var1.y = h(var2, true);
      var1.U = h(var2, false);
      var1.C = D(var2, true);
      var1.q = D(var2, false);
      var1.e = k(var3.F);
      var1.t = k(var3.m);
      var1.f = k(var3.X);
      var1.m = var3.b.name();
      var1.D = var3.E();
      var1.N = k(var4.Y);
      var1.o = k(var4.c);
      we var5 = we.u();
      var1.L = new int[10];

      for (int var6 = 1; var6 <= 10; var6++) {
         var1.L[var6 - 1] = Y(var6) ? var5.getTemplateIdForValue(var6) : -1;
      }

      var1.x = lh.I().L();
      w3 var9 = w3.M();
      int[] var7 = var9.q();
      var1.i = new int[var7.length];

      for (int var8 = 0; var8 < var7.length; var8++) {
         var1.i[var8] = var9.getTemplateIdForValue(var7[var8]);
      }

      var1.s = var9.I();
      var1.B = B(wk.m());
      var1.c = B(w0.l());
      var1.k = var0.H();
   }

   public static void O(sg var0, c1.d var1) {
      g5 var2 = g5.a();
      t5 var3 = t5.O();
      pq var4 = pq.Q();
      var2.d(var1.b);
      var2.E(var1.F);
      var2.F(var1.I == null ? g5.q.z.name().toLowerCase() : var1.I.toLowerCase());
      var2.q(var1.j);
      var2.l = z(var1.O, var2.l);
      var2.i = z(var1.A, var2.i);
      var2.G(var1.d);
      l(var2, var1.y, true);
      Q(var2, var1.C, true);
      if (!var1.d) {
         l(var2, var1.U, false);
         Q(var2, var1.q, false);
      }

      var3.F = z(var1.e, var3.F);
      var3.m = z(var1.t, var3.m);
      var3.X = z(var1.f, var3.X);
      var3.b = e(var1.m, var3.b);
      var3.A(var1.D);
      var4.Y = z(var1.N, var4.Y);
      var4.c = z(var1.o, var4.c);
      we var5 = we.u();

      for (int var6 = 1; var6 <= 10; var6++) {
         int var7 = Y(var6) && var1.L != null && var1.L.length >= var6 ? var1.L[var6 - 1] : -1;
         var5.setTemplateIdForValue(var6, var7);
      }

      lh.I().w(var1.x);
      w3 var9 = w3.M();
      int[] var10 = var9.q();

      for (int var8 = 0; var8 < var10.length; var8++) {
         var9.setTemplateIdForValue(var10[var8], var1.i != null && var1.i.length > var8 ? var1.i[var8] : -1);
      }

      var9.s(var1.s);
      Z(wk.m(), var1.B != null ? var1.B : var1.L);
      Z(w0.l(), var1.c != null ? var1.c : var1.B);
      var0.N(var1.k);
   }

   private static boolean Y(int var0) {
      return var0 == 1 || var0 >= 3 && var0 <= 10;
   }

   private static int[] B(we var0) {
      int[] var1 = new int[9];

      for (int var2 = 1; var2 <= 9; var2++) {
         var1[var2 - 1] = var0.getTemplateIdForValue(var2);
      }

      return var1;
   }

   private static void Z(we var0, int[] var1) {
      for (int var2 = 1; var2 <= 9; var2++) {
         var0.setTemplateIdForValue(var2, var1 != null && var1.length >= var2 ? var1[var2 - 1] : -1);
      }
   }

   private static Item z(String var0, Item var1) {
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

   private static String k(Item var0) {
      return BuiltInRegistries.ITEM.getKey(var0).toString();
   }

   private static t5.o e(String var0, t5.o var1) {
      if (var0 == null) {
         return var1;
      }

      try {
         return t5.o.valueOf(var0);
      } catch (IllegalArgumentException var3) {
         return var1;
      }
   }

   private static void l(g5 var0, String[] var1, boolean var2) {
      if (var1 != null && var1.length >= 9) {
         for (int var3 = 0; var3 < 9; var3++) {
            var0.s(var2, var3, z(var1[var3], var0.a(var2, var3)));
         }
      }
   }

   private static String[] h(g5 var0, boolean var1) {
      String[] var2 = new String[9];

      for (int var3 = 0; var3 < 9; var3++) {
         Item var4 = var0.a(var1, var3);
         var2[var3] = k(var4 == null ? Items.PAPER : var4);
      }

      return var2;
   }

   private static void Q(g5 var0, String[] var1, boolean var2) {
      if (var1 != null && var1.length >= 9) {
         RegistryOps var3 = VanillaRegistries.createLookup().createSerializationContext(JsonOps.INSTANCE);

         for (int var4 = 0; var4 < 9; var4++) {
            if (var1[var4] != null && !var1[var4].isBlank()) {
               try {
                  ItemStack var5 = ItemStack.CODEC.parse(var3, JsonParser.parseString(var1[var4])).result().orElse(ItemStack.EMPTY);
                  if (!var5.isEmpty()) {
                     var0.z(var2, var4, var5);
                  }
               } catch (Exception var6) {
                  h.g("Rig", "Could not restore Paper item metadata for slot {}", var4 + 1);
               }
            }
         }
      }
   }

   private static String[] D(g5 var0, boolean var1) {
      RegistryOps var2 = VanillaRegistries.createLookup().createSerializationContext(JsonOps.INSTANCE);
      String[] var3 = new String[9];

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var0.Y(var1, var4);
         var3[var4] = var5 != null && !var5.isEmpty() ? ItemStack.CODEC.encodeStart(var2, var5).result().<String>map(s::toJson).orElse("") : "";
      }

      return var3;
   }

   public static final class d {
      public boolean d = (boolean)1;
      public boolean j = (boolean)0;
      public String I = g5.q.z.name();
      public String b = "Host";
      public String F = "Viewer";
      public String O = "minecraft:apple";
      public String A = "minecraft:diamond";
      public String[] y;
      public String[] U;
      public String[] C;
      public String[] q;
      public String e = "minecraft:stone";
      public String t = "minecraft:cobblestone";
      public String f = "minecraft:diamond";
      public String m = t5.o.i.name();
      public int[] D;
      public String N = "minecraft:tnt";
      public String o = "minecraft:paper";
      public int[] L = c1.J(10);
      public int[] x = c1.J(11);
      public int[] i = c1.J(8);
      public int s = 0;
      public int[] B = c1.J(9);
      public int[] c = c1.J(9);
      public int[] k = c1.T();
   }
}
