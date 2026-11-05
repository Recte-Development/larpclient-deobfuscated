package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.module.LARPModule;
import com.mojang.blaze3d.platform.NativeImage;
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
import net.minecraft.client.Minecraft;

public final class em {
   private static final ExecutorService D = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-Assets");
      var1.setDaemon(true);
      return var1;
   });
   private static final AtomicLong C = new AtomicLong();
   private static volatile _9 U = _9.EMPTY;
   public static volatile String h = "Local only";
   public static volatile String R = "";
   private static boolean I;
   private static final AtomicBoolean N = new AtomicBoolean();
   private static volatile long B;

   public static boolean C() {
      return N.get();
   }

   private static Path E() {
      return Minecraft.getInstance().gameDirectory.toPath().resolve("config/larp-appearance.json");
   }

   public static _9 V() {
      return U;
   }

   public static void i() {
      r4.V("Appearance", "Local appearance enabled; multiplayer sharing is removed");
      long var0 = C.incrementAndGet();
      D.execute(() -> {
         try {
            _9 var2 = U;
            if (!I) {
               Path var3 = E();
               if (Files.exists(var3) && Files.size(var3) <= 360000L) {
                  var2 = _9.parse(JsonParser.parseString(Files.readString(var3)).getAsJsonObject());
               }

               I = (boolean)1;
            }

            w(var2, var0, false);
         } catch (Exception var4) {
            R = "Could not load appearance: " + var4.getMessage();
         }
      });
   }

   public static void z() {
      C.incrementAndGet();
      y_.p();
      r4.g("Appearance", "Local appearance disabled; original textures restored");
   }

   public static void k(String var0) {
      long var1 = C.incrementAndGet();
      D.execute(
         () -> {
            _9 var3 = U;
            n(
               new _9(
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

   public static void e(boolean var0) {
      long var1 = C.incrementAndGet();
      D.execute(() -> {
         _9 var3 = U;
         n(new _9(var3.skin(), var3.cape(), var0, var3.hideCape(), var3.name(), new JsonObject()), var1);
      });
   }

   public static void k(String var0, boolean var1) {
      long var2 = C.incrementAndGet();
      R = "Loading PNG...";
      D.execute(() -> {
         try {
            Path var4 = Path.of(var0);
            if (Files.size(var4) > 131072L) {
               throw new IllegalArgumentException("PNG exceeds 128 KiB");
            }

            byte[] var5;
            try (InputStream var6 = Files.newInputStream(var4)) {
               var5 = var6.readNBytes(131073);
            }

            om.s(var5, var1);
            String var12 = Base64.getEncoder().encodeToString(var5);
            _9 var7 = U;
            w(new _9(var1 ? var12 : var7.skin(), var1 ? var7.cape() : var12, var7.slim(), var1 && var7.hideCape(), var7.name(), new JsonObject()), var2, true);
         } catch (Exception var11) {
            R = "Load failed: " + var11.getMessage();
         }
      });
   }

   public static void A(String var0) {
      if (!var0.matches("[A-Za-z0-9_]{1,16}")) {
         R = "Enter a Minecraft username";
      } else if (System.nanoTime() >= B && N.compareAndSet(false, true)) {
         B = System.nanoTime() + TimeUnit.SECONDS.toNanos(5L);
         long var1 = C.incrementAndGet();
         R = "Looking up " + var0 + "...";
         D.execute(
            () -> {
               try {
                  JsonObject var3 = el.h("https://api.mojang.com/users/profiles/minecraft/" + var0, 8192);
                  String var4 = var3.get("id").getAsString();
                  if (!var4.matches("[0-9a-fA-F]{32}")) {
                     throw new IllegalArgumentException("Invalid profile");
                  }

                  JsonObject var5 = el.h("https://sessionserver.mojang.com/session/minecraft/profile/" + var4, 32768);
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
                  w(
                     new _9(
                        I(var15, true),
                        var6.has("CAPE") ? I(var6.getAsJsonObject("CAPE"), false) : "",
                        (boolean)var16,
                        !var6.has("CAPE"),
                        var3.get("name").getAsString(),
                        new JsonObject()
                     ),
                     var1,
                     true
                  );
               } catch (Exception var13) {
                  R = "Import failed: " + var13.getMessage();
               } finally {
                  N.set(false);
               }
            }
         );
      } else {
         R = "Please wait before starting another lookup";
      }
   }

   private static String I(JsonObject var0, boolean var1) throws Exception {
      String var2 = var0.get("url").getAsString().replace("http://", "https://");
      if (!var2.matches("https://textures\\.minecraft\\.net/texture/[a-fA-F0-9]+")) {
         throw new IllegalArgumentException("Untrusted texture URL");
      }

      byte[] var3 = el.g(var2, 131072);
      om.s(var3, var1);
      return Base64.getEncoder().encodeToString(var3);
   }

   private static void n(_9 var0, long var1) {
      try {
         w(var0, var1, true);
      } catch (Exception var4) {
         R = "Appearance failed: " + var4.getMessage();
      }
   }

   private static void w(_9 var0, long var1, boolean var3) throws Exception {
      NativeImage var4 = om.x(var0.skin(), true);
      NativeImage var5 = null;

      try {
         var5 = om.x(var0.cape(), false);
      } catch (Exception var8) {
         if (var4 != null) {
            var4.close();
         }

         throw var8;
      }

      if (var1 != C.get()) {
         if (var4 != null) {
            var4.close();
         }

         if (var5 != null) {
            var5.close();
         }
      } else {
         try {
            if (var3) {
               Path var6 = E();
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

         U = var0;
         NativeImage var11 = var5;
         Minecraft.getInstance().execute(() -> {
            if (var1 != C.get()) {
               if (var4 != null) {
                  var4.close();
               }

               if (var11 != null) {
                  var11.close();
               }
            } else {
               y_.p();
               LARPModule var5x = LARPModule.C();
               if (var5x != null && var5x.isOperational()) {
                  y_.X = y_.i("own", var0, var4, var11);
               } else {
                  if (var4 != null) {
                     var4.close();
                  }

                  if (var11 != null) {
                     var11.close();
                  }
               }

               R = "Appearance saved";
            }
         });
      }
   }
}
