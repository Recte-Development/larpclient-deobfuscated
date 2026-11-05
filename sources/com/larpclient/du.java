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

public final class du {
   private static final ExecutorService Z = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-Assets");
      var1.setDaemon(true);
      return var1;
   });
   private static final AtomicLong a = new AtomicLong();
   private static volatile o j = o.EMPTY;
   public static volatile String w = "Local only";
   public static volatile String F = "";
   private static boolean C;
   private static final AtomicBoolean N = new AtomicBoolean();
   private static volatile long c;

   public static boolean Q() {
      return N.get();
   }

   private static Path v() {
      return Minecraft.getInstance().gameDirectory.toPath().resolve("config/larp-appearance.json");
   }

   public static o G() {
      return j;
   }

   public static void s() {
      mt.K("Appearance", "Local appearance enabled; multiplayer sharing is removed");
      long var0 = a.incrementAndGet();
      Z.execute(() -> {
         try {
            o var2 = j;
            if (!C) {
               Path var3 = v();
               if (Files.exists(var3) && Files.size(var3) <= 360000L) {
                  var2 = o.parse(JsonParser.parseString(Files.readString(var3)).getAsJsonObject());
               }

               C = (boolean)1;
            }

            L(var2, var0, false);
         } catch (Exception var4) {
            F = "Could not load appearance: " + var4.getMessage();
         }
      });
   }

   public static void u() {
      a.incrementAndGet();
      z1.n();
      mt.U("Appearance", "Local appearance disabled; original textures restored");
   }

   public static void c(String var0) {
      long var1 = a.incrementAndGet();
      Z.execute(
         () -> {
            o var3 = j;
            H(
               new o(
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

   public static void r(boolean var0) {
      long var1 = a.incrementAndGet();
      Z.execute(() -> {
         o var3 = j;
         H(new o(var3.skin(), var3.cape(), var0, var3.hideCape(), var3.name(), new JsonObject()), var1);
      });
   }

   public static void o(String var0, boolean var1) {
      long var2 = a.incrementAndGet();
      F = "Loading PNG...";
      Z.execute(() -> {
         try {
            Path var4 = Path.of(var0);
            if (Files.size(var4) > 131072L) {
               throw new IllegalArgumentException("PNG exceeds 128 KiB");
            }

            byte[] var5;
            try (InputStream var6 = Files.newInputStream(var4)) {
               var5 = var6.readNBytes(131073);
            }

            el.S(var5, var1);
            String var12 = Base64.getEncoder().encodeToString(var5);
            o var7 = j;
            L(new o(var1 ? var12 : var7.skin(), var1 ? var7.cape() : var12, var7.slim(), var1 && var7.hideCape(), var7.name(), new JsonObject()), var2, true);
         } catch (Exception var11) {
            F = "Load failed: " + var11.getMessage();
         }
      });
   }

   public static void t(String var0) {
      if (!var0.matches("[A-Za-z0-9_]{1,16}")) {
         F = "Enter a Minecraft username";
      } else if (System.nanoTime() >= c && N.compareAndSet(false, true)) {
         c = System.nanoTime() + TimeUnit.SECONDS.toNanos(5L);
         long var1 = a.incrementAndGet();
         F = "Looking up " + var0 + "...";
         Z.execute(
            () -> {
               try {
                  JsonObject var3 = ts.T("https://api.mojang.com/users/profiles/minecraft/" + var0, 8192);
                  String var4 = var3.get("id").getAsString();
                  if (!var4.matches("[0-9a-fA-F]{32}")) {
                     throw new IllegalArgumentException("Invalid profile");
                  }

                  JsonObject var5 = ts.T("https://sessionserver.mojang.com/session/minecraft/profile/" + var4, 32768);
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
                  L(
                     new o(
                        P(var15, true),
                        var6.has("CAPE") ? P(var6.getAsJsonObject("CAPE"), false) : "",
                        (boolean)var16,
                        !var6.has("CAPE"),
                        var3.get("name").getAsString(),
                        new JsonObject()
                     ),
                     var1,
                     true
                  );
               } catch (Exception var13) {
                  F = "Import failed: " + var13.getMessage();
               } finally {
                  N.set(false);
               }
            }
         );
      } else {
         F = "Please wait before starting another lookup";
      }
   }

   private static String P(JsonObject var0, boolean var1) throws Exception {
      String var2 = var0.get("url").getAsString().replace("http://", "https://");
      if (!var2.matches("https://textures\\.minecraft\\.net/texture/[a-fA-F0-9]+")) {
         throw new IllegalArgumentException("Untrusted texture URL");
      }

      byte[] var3 = ts.A(var2, 131072);
      el.S(var3, var1);
      return Base64.getEncoder().encodeToString(var3);
   }

   private static void H(o var0, long var1) {
      try {
         L(var0, var1, true);
      } catch (Exception var4) {
         F = "Appearance failed: " + var4.getMessage();
      }
   }

   private static void L(o var0, long var1, boolean var3) throws Exception {
      NativeImage var4 = el.m(var0.skin(), true);
      NativeImage var5 = null;

      try {
         var5 = el.m(var0.cape(), false);
      } catch (Exception var8) {
         if (var4 != null) {
            var4.close();
         }

         throw var8;
      }

      if (var1 != a.get()) {
         if (var4 != null) {
            var4.close();
         }

         if (var5 != null) {
            var5.close();
         }
      } else {
         try {
            if (var3) {
               Path var6 = v();
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

         j = var0;
         NativeImage var11 = var5;
         Minecraft.getInstance().execute(() -> {
            if (var1 != a.get()) {
               if (var4 != null) {
                  var4.close();
               }

               if (var11 != null) {
                  var11.close();
               }
            } else {
               z1.n();
               LARPModule var5x = LARPModule.d();
               if (var5x != null && var5x.isOperational()) {
                  z1.z = z1.R("own", var0, var4, var11);
               } else {
                  if (var4 != null) {
                     var4.close();
                  }

                  if (var11 != null) {
                     var11.close();
                  }
               }

               F = "Appearance saved";
            }
         });
      }
   }
}
