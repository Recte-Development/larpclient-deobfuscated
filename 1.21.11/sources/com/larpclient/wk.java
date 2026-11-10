package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.module.LARPModule;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import net.minecraft.class_1011;
import net.minecraft.class_310;

public final class wk {
   private static final ExecutorService U = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-Assets");
      var1.setDaemon(true);
      return var1;
   });
   private static final AtomicLong G = new AtomicLong();
   private static volatile uc s = uc.EMPTY;
   public static volatile String H = "Local only";
   public static volatile String C = "";
   private static boolean R;
   private static final AtomicBoolean D = new AtomicBoolean();
   private static volatile long k;

   public static boolean E() {
      return D.get();
   }

   private static Path r() {
      return class_310.method_1551().field_1697.toPath().resolve("config/larp-appearance.json");
   }

   public static uc F() {
      return s;
   }

   public static void b() {
      q8.G("Appearance", "Local appearance enabled; multiplayer sharing is removed");
      long var0 = G.incrementAndGet();
      U.execute(() -> {
         try {
            uc var2 = s;
            if (!R) {
               Path var3 = r();
               if (Files.exists(var3) && Files.size(var3) <= 360000L) {
                  var2 = uc.parse(JsonParser.parseString(Files.readString(var3)).getAsJsonObject());
               }

               R = (boolean)1;
            }

            m(var2, var0, false);
         } catch (Exception var4) {
            C = "Could not load appearance: " + var4.getMessage();
         }
      });
   }

   public static void V() {
      G.incrementAndGet();
      ub.l();
      q8.v("Appearance", "Local appearance disabled; original textures restored");
   }

   public static void v(String var0) {
      long var1 = G.incrementAndGet();
      U.execute(
         () -> {
            uc var3 = s;
            B(
               new uc(
                  !var0.equals("skin") && !var0.equals("all") ? var3.skin() : "",
                  !var0.equals("cape") && !var0.equals("all") ? var3.cape() : "",
                  var3.slim(),
                  !var0.equals("cape") && !var0.equals("all") ? var3.hideCape() : false,
                  !var0.equals("name") && !var0.equals("all") ? var3.name() : "",
                  new JsonObject()
               ),
               var1
            );
         }
      );
   }

   public static void s(boolean var0) {
      long var1 = G.incrementAndGet();
      U.execute(() -> {
         uc var3 = s;
         B(new uc(var3.skin(), var3.cape(), var0, var3.hideCape(), var3.name(), new JsonObject()), var1);
      });
   }

   public static void b(String var0, boolean var1) {
      long var2 = G.incrementAndGet();
      C = "Loading PNG...";
      U.execute(() -> {
         try {
            Path var4 = Path.of(var0);
            if (Files.size(var4) > 131072L) {
               throw new IllegalArgumentException("PNG exceeds 128 KiB");
            }

            byte[] var5;
            try (InputStream var6 = Files.newInputStream(var4)) {
               var5 = var6.readNBytes(131073);
            }

            sp.I(var5, var1);
            String var12 = Base64.getEncoder().encodeToString(var5);
            uc var7 = s;
            m(new uc(var1 ? var12 : var7.skin(), var1 ? var7.cape() : var12, var7.slim(), var1 && var7.hideCape(), var7.name(), new JsonObject()), var2, true);
         } catch (Exception var11) {
            C = "Load failed: " + var11.getMessage();
         }
      });
   }

   public static void d(String var0) {
      if (!var0.matches("[A-Za-z0-9_]{1,16}")) {
         C = "Enter a Minecraft username";
      } else if (System.nanoTime() >= k && D.compareAndSet(false, true)) {
         k = System.nanoTime() + TimeUnit.SECONDS.toNanos(5L);
         long var1 = G.incrementAndGet();
         C = "Looking up " + var0 + "...";
         U.execute(
            () -> {
               try {
                  JsonObject var3 = lh.z("https://api.mojang.com/users/profiles/minecraft/" + var0, 8192);
                  String var4 = var3.get("id").getAsString();
                  if (!var4.matches("[0-9a-fA-F]{32}")) {
                     throw new IllegalArgumentException("Invalid profile");
                  }

                  JsonObject var5 = lh.z("https://sessionserver.mojang.com/session/minecraft/profile/" + var4, 32768);
                  JsonObject var6 = null;

                  for (JsonElement var8 : var5.getAsJsonArray("properties")) {
                     JsonObject var9 = var8.getAsJsonObject();
                     if (var9.get("name").getAsString().equals("textures")) {
                        var6 = JsonParser.parseString(new String(Base64.getDecoder().decode(var9.get("value").getAsString()), StandardCharsets.UTF_8))
                           .getAsJsonObject()
                           .getAsJsonObject("textures");
                     }
                  }

                  if (var6 == null || !var6.has("SKIN")) {
                     throw new IllegalArgumentException("No public skin");
                  }

                  JsonObject var15 = var6.getAsJsonObject("SKIN");
                  int var16 = var15.has("metadata") && "slim".equals(var15.getAsJsonObject("metadata").get("model").getAsString()) ? 1 : 0;
                  m(
                     new uc(
                        K(var15, true),
                        var6.has("CAPE") ? K(var6.getAsJsonObject("CAPE"), false) : "",
                        (boolean)var16,
                        !var6.has("CAPE"),
                        var3.get("name").getAsString(),
                        new JsonObject()
                     ),
                     var1,
                     true
                  );
               } catch (Exception var13) {
                  C = "Import failed: " + var13.getMessage();
               } finally {
                  D.set(false);
               }
            }
         );
      } else {
         C = "Please wait before starting another lookup";
      }
   }

   private static String K(JsonObject var0, boolean var1) throws Exception {
      String var2 = var0.get("url").getAsString().replace("http://", "https://");
      if (!var2.matches("https://textures\\.minecraft\\.net/texture/[a-fA-F0-9]+")) {
         throw new IllegalArgumentException("Untrusted texture URL");
      }

      byte[] var3 = lh.m(var2, 131072);
      sp.I(var3, var1);
      return Base64.getEncoder().encodeToString(var3);
   }

   private static void B(uc var0, long var1) {
      try {
         m(var0, var1, true);
      } catch (Exception var4) {
         C = "Appearance failed: " + var4.getMessage();
      }
   }

   private static void m(uc var0, long var1, boolean var3) throws Exception {
      class_1011 var4 = sp.h(var0.skin(), true);
      class_1011 var5 = null;

      try {
         var5 = sp.h(var0.cape(), false);
      } catch (Exception var8) {
         if (var4 != null) {
            var4.close();
         }

         throw var8;
      }

      if (var1 != G.get()) {
         if (var4 != null) {
            var4.close();
         }

         if (var5 != null) {
            var5.close();
         }
      } else {
         try {
            if (var3) {
               Path var6 = r();
               Files.createDirectories(var6.getParent());
               Path var7 = var6.resolveSibling(var6.getFileName() + ".tmp");
               Files.writeString(var7, var0.json().toString());
               Files.move(var7, var6, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            }
         } catch (Exception var9) {
            if (var4 != null) {
               var4.close();
            }

            if (var5 != null) {
               var5.close();
            }

            throw var9;
         }

         s = var0;
         class_1011 var11 = var5;
         class_310.method_1551().execute(() -> {
            if (var1 != G.get()) {
               if (var4 != null) {
                  var4.close();
               }

               if (var11 != null) {
                  var11.close();
               }
            } else {
               ub.l();
               LARPModule var5x = LARPModule.f();
               if (var5x != null && var5x.isOperational()) {
                  ub.p = ub.x("own", var0, var4, var11);
               } else {
                  if (var4 != null) {
                     var4.close();
                  }

                  if (var11 != null) {
                     var11.close();
                  }
               }

               C = "Appearance saved";
            }
         });
      }
   }
}
