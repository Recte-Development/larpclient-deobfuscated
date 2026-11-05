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

public final class me {
   public static final int E = 5;
   public static final int f = 3;
   private static final String G = "items.json";
   private static final Gson R = new GsonBuilder().setPrettyPrinting().create();

   private me() {
   }

   private static int[] A(int var0) {
      int[] var1 = new int[var0];
      Arrays.fill(var1, -1);
      return var1;
   }

   public static int[] u() {
      return A(15);
   }

   public static int[] Z(int[] var0) {
      if (var0 != null && var0.length == 15) {
         int[] var1 = (int[])var0.clone();

         for (int var2 = 0; var2 < var1.length; var2++) {
            if (var1[var2] < 1 || var1[var2] > 9) {
               var1[var2] = -1;
            }
         }

         return var1;
      } else {
         return u();
      }
   }

   private static Path v() {
      return ko.m("items.json");
   }

   public static synchronized void b(c3 var0) {
      Path var1 = v();
      if (Files.exists(var1)) {
         try {
            JsonElement var2 = JsonParser.parseString(Files.readString(var1, StandardCharsets.UTF_8));
            if (var2 == null || !var2.isJsonObject()) {
               throw new IOException("not a JSON object");
            }

            me.w var3 = (me.w)R.fromJson((JsonObject)var2, me.w.class);
            if (var3 == null) {
               throw new IOException("empty");
            }

            s(var0, var3);
         } catch (Exception var4) {
            y(var1, var4);
         }
      }
   }

   public static synchronized void j(c3 var0) {
      me.w var1 = new me.w();
      m(var0, var1);
      Path var2 = v();

      try {
         Files.createDirectories(var2.getParent());
         Path var3 = var2.resolveSibling(var2.getFileName() + ".tmp");
         Files.writeString(var3, R.toJson(var1), StandardCharsets.UTF_8);

         try {
            Files.move(var3, var2, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (IOException var5) {
            Files.move(var3, var2, StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (IOException var6) {
         mt.T("Rig", "Could not save casino game data: {}", var6.getMessage());
      }
   }

   private static void y(Path var0, Exception var1) {
      mt.T("Rig", "Casino game data was unreadable ({}); starting from defaults", var1.getMessage());

      try {
         Files.move(var0, var0.resolveSibling(var0.getFileName() + ".bad"), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException var3) {
      }
   }

   public static void m(c3 var0, me.w var1) {
      mi var2 = mi.L();
      zv var3 = zv.x();
      _u var4 = _u.O();
      var1.J = var2.l();
      var1.j = var2.N();
      var1.F = var2.p().name();
      var1.B = var2.u();
      var1.O = var2.o();
      var1.l = J(var2.h);
      var1.Q = J(var2.I);
      var1.z = Z(var2, true);
      var1.x = Z(var2, false);
      var1.q = j(var2, true);
      var1.G = j(var2, false);
      var1.X = J(var3.e);
      var1.e = J(var3.k);
      var1.n = J(var3.n);
      var1.h = var3.V.name();
      var1.o = var3.W();
      var1.L = J(var4.z);
      var1.I = J(var4.l);
      ze var5 = ze.V();
      var1.v = new int[10];

      for (int var6 = 1; var6 <= 10; var6++) {
         var1.v[var6 - 1] = C(var6) ? var5.getTemplateIdForValue(var6) : -1;
      }

      var1.P = kq.I().q();
      zu var9 = zu.v();
      int[] var7 = var9.E();
      var1.m = new int[var7.length];

      for (int var8 = 0; var8 < var7.length; var8++) {
         var1.m[var8] = var9.getTemplateIdForValue(var7[var8]);
      }

      var1.T = var9.n();
      var1.E = j(z6.u());
      var1.a = j(zw.K());
      var1.K = var0.U();
   }

   public static void s(c3 var0, me.w var1) {
      mi var2 = mi.L();
      zv var3 = zv.x();
      _u var4 = _u.O();
      var2.f(var1.B);
      var2.X(var1.O);
      var2.C(var1.F == null ? mi.c.o.name().toLowerCase() : var1.F.toLowerCase());
      var2.l(var1.j);
      var2.h = S(var1.l, var2.h);
      var2.I = S(var1.Q, var2.I);
      var2.W(var1.J);
      V(var2, var1.z, true);
      U(var2, var1.q, true);
      if (!var1.J) {
         V(var2, var1.x, false);
         U(var2, var1.G, false);
      }

      var3.e = S(var1.X, var3.e);
      var3.k = S(var1.e, var3.k);
      var3.n = S(var1.n, var3.n);
      var3.V = R(var1.h, var3.V);
      var3.j(var1.o);
      var4.z = S(var1.L, var4.z);
      var4.l = S(var1.I, var4.l);
      ze var5 = ze.V();

      for (int var6 = 1; var6 <= 10; var6++) {
         int var7 = C(var6) && var1.v != null && var1.v.length >= var6 ? var1.v[var6 - 1] : -1;
         var5.setTemplateIdForValue(var6, var7);
      }

      kq.I().E(var1.P);
      zu var9 = zu.v();
      int[] var10 = var9.E();

      for (int var8 = 0; var8 < var10.length; var8++) {
         var9.setTemplateIdForValue(var10[var8], var1.m != null && var1.m.length > var8 ? var1.m[var8] : -1);
      }

      var9.F(var1.T);
      n(z6.u(), var1.E != null ? var1.E : var1.v);
      n(zw.K(), var1.a != null ? var1.a : var1.E);
      var0.V(var1.K);
   }

   private static boolean C(int var0) {
      return var0 == 1 || var0 >= 3 && var0 <= 10;
   }

   private static int[] j(ze var0) {
      int[] var1 = new int[9];

      for (int var2 = 1; var2 <= 9; var2++) {
         var1[var2 - 1] = var0.getTemplateIdForValue(var2);
      }

      return var1;
   }

   private static void n(ze var0, int[] var1) {
      for (int var2 = 1; var2 <= 9; var2++) {
         var0.setTemplateIdForValue(var2, var1 != null && var1.length >= var2 ? var1[var2 - 1] : -1);
      }
   }

   private static Item S(String var0, Item var1) {
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

   private static String J(Item var0) {
      return BuiltInRegistries.ITEM.getKey(var0).toString();
   }

   private static zv.u R(String var0, zv.u var1) {
      if (var0 == null) {
         return var1;
      }

      try {
         return zv.u.valueOf(var0);
      } catch (IllegalArgumentException var3) {
         return var1;
      }
   }

   private static void V(mi var0, String[] var1, boolean var2) {
      if (var1 != null && var1.length >= 9) {
         for (int var3 = 0; var3 < 9; var3++) {
            var0.F(var2, var3, S(var1[var3], var0.m(var2, var3)));
         }
      }
   }

   private static String[] Z(mi var0, boolean var1) {
      String[] var2 = new String[9];

      for (int var3 = 0; var3 < 9; var3++) {
         Item var4 = var0.m(var1, var3);
         var2[var3] = J(var4 == null ? Items.PAPER : var4);
      }

      return var2;
   }

   private static void U(mi var0, String[] var1, boolean var2) {
      if (var1 != null && var1.length >= 9) {
         RegistryOps var3 = VanillaRegistries.createWorldLookup().createSerializationContext(JsonOps.INSTANCE);

         for (int var4 = 0; var4 < 9; var4++) {
            if (var1[var4] != null && !var1[var4].isBlank()) {
               try {
                  ItemStack var5 = ItemStack.CODEC.parse(var3, JsonParser.parseString(var1[var4])).result().orElse(ItemStack.EMPTY);
                  if (!var5.isEmpty()) {
                     var0.U(var2, var4, var5);
                  }
               } catch (Exception var6) {
                  mt.T("Rig", "Could not restore Paper item metadata for slot {}", var4 + 1);
               }
            }
         }
      }
   }

   private static String[] j(mi var0, boolean var1) {
      RegistryOps var2 = VanillaRegistries.createWorldLookup().createSerializationContext(JsonOps.INSTANCE);
      String[] var3 = new String[9];

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var0.m(var1, var4);
         var3[var4] = var5 != null && !var5.isEmpty() ? ItemStack.CODEC.encodeStart(var2, var5).result().<String>map(R::toJson).orElse("") : "";
      }

      return var3;
   }

   public static final class w {
      public boolean J = (boolean)1;
      public boolean j = (boolean)0;
      public String F = mi.c.o.name();
      public String B = "Host";
      public String O = "Viewer";
      public String l = "minecraft:apple";
      public String Q = "minecraft:diamond";
      public String[] z;
      public String[] x;
      public String[] q;
      public String[] G;
      public String X = "minecraft:stone";
      public String e = "minecraft:cobblestone";
      public String n = "minecraft:diamond";
      public String h = zv.u.D.name();
      public int[] o;
      public String L = "minecraft:tnt";
      public String I = "minecraft:paper";
      public int[] v = me.A(10);
      public int[] P = me.A(11);
      public int[] m = me.A(8);
      public int T = 0;
      public int[] E = me.A(9);
      public int[] a = me.A(9);
      public int[] K = me.u();
   }
}
