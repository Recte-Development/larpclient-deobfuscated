package com.larpclient.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larp.obf.q4031f93fe72647cb.q5eca7d3b6cc64742;
import com.larpclient.q8;
import com.larpclient.sp;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import net.minecraft.class_1011;
import net.minecraft.class_1068;
import net.minecraft.class_156;
import net.minecraft.class_2960;
import net.minecraft.class_7920;
import net.minecraft.class_8685;
import net.minecraft.class_12079.class_10726;
import net.minecraft.class_12079.class_12081;

public final class SkinChangerModule extends Module {
   private static SkinChangerModule f;
   private static final ExecutorService h = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-SkinChanger");
      var1.setDaemon(true);
      return var1;
   });
   private final li v = this.addSetting(new li("Skin", "Choose a PNG from the skins folder", "None", "None", "Default") {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(var1) || SkinChangerModule.Q(var1);
      }

      @Override
      public void setValue(String var1) {
         if (!this.e().contains(var1) && SkinChangerModule.Q(var1)) {
            ArrayList var2 = new ArrayList<>(this.e());
            var2.add(var1);
            this.k(var2);
         }

         super.setValue(var1);
      }
   });
   private final li z = this.addSetting(new li("Model", "Auto uses the copied player's arm type", "Auto", "Auto", "Wide", "Slim"));
   private final aj E = this.addSetting(new aj("Copy Player", "Minecraft username; leave empty to use the selected skin", ""));
   private final AtomicLong Y = new AtomicLong();
   private class_2960 N;
   private boolean X;
   private volatile boolean D;
   private volatile String u = "Select a skin or enter a username";
   private String r = "";
   private String A = "";
   private long i;
   private long C;
   private String J = "";
   private boolean w;
   private boolean B;

   public SkinChangerModule() {
      super("Skin Changer", "Use a custom or copied skin locally", Category.VISUALS, -1);
      f = this;
      this.v.O(() -> this.E.getValue().isBlank());
      this.z.O(() -> !this.v.G("None") || !this.E.getValue().isBlank());
      this.v.a(var1 -> {
         this.C = 0L;
         if (this.K()) {
            this.G();
         }
      });
      this.E.a(var1 -> {
         this.Y.incrementAndGet();
         this.w = (boolean)1;
         this.C = 0L;
         this.r = var1.strip();
         this.i = System.nanoTime();
      });
   }

   public static SkinChangerModule m() {
      return f;
   }

   private Path a() {
      return mc.field_1697.toPath().resolve("config/larp-client/skins");
   }

   @Override
   public String getStatus() {
      return this.u;
   }

   public void M() {
      Path var1 = this.a();
      h.execute(() -> {
         try {
            Files.createDirectories(var1);
            mc.execute(() -> class_156.method_668().method_60932(var1));
         } catch (Exception var3) {
            this.u = "Could not open skins folder";
            q8.u("SkinChanger", "Folder stage failed", var3);
         }
      });
   }

   public void v() {
      Path var1 = this.a();
      h.execute(
         () -> {
            try {
               Files.createDirectories(var1);

               List var2;
               try (Stream var3 = Files.list(var1)) {
                  var2 = var3.filter(var0 -> Files.isRegularFile(var0) && var0.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".png"))
                     .map(var0 -> var0.getFileName().toString())
                     .filter(var0 -> var0.length() <= 128)
                     .sorted(String.CASE_INSENSITIVE_ORDER)
                     .limit(256L)
                     .toList();
               }

               ArrayList var9 = new ArrayList<>(List.of("None", "Default"));
               var9.addAll(var2);
               mc.execute(() -> this.v.k(var9));
            } catch (Exception var8) {
               this.u = "Could not read skins folder";
               q8.u("SkinChanger", "Library stage failed", var8);
            }
         }
      );
   }

   @Override
   public void onEnable() {
      this.A = "";
      this.C = 0L;
      this.v();
      this.G();
   }

   @Override
   public void onDisable() {
      this.Y.incrementAndGet();
      this.g();
      this.A = this.J = "";
      this.u = "Original skin restored";
      q8.G("SkinChanger", "Disabled; local texture released");
   }

   @Override
   public void onTick() {
      String var1 = this.E.getValue().strip();
      long var2 = System.nanoTime();
      int var10000 = var1.equals(this.r);
      boolean var5 = false;
      byte var4 = (byte)var10000;
      var10000 = Integer.signum(var4);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.r = var1;
         this.i = var2;
      }

      boolean var7 = false;
      byte var6 = this.D;
      var10000 = Integer.signum(var6);
      if ((var10000 | -var10000) >>> 31 == 0) {
         boolean var9 = false;
         byte var8 = this.w;
         var10000 = Integer.signum(var8);
         if ((var10000 | -var10000) >>> 31 != 0 || !var1.equals(this.A) || !this.v.getValue().equals(this.J)) {
            long var16;
            var10000 = (var16 = var2 - this.i - 600000000L) == 0L ? 0 : (var16 < 0L ? -1 : 1);
            boolean var11 = false;
            int var10 = var10000;
            if (Integer.signum(var10) >>> 31 == 0 && var2 >= this.C) {
               this.G();
            }
         }
      }
   }

   public void G() {
      if (this.isEnabled()) {
         if (this.K()) {
            if (this.D) {
               this.Y.incrementAndGet();
               this.w = (boolean)1;
            } else {
               this.w = (boolean)0;
               String var1 = this.E.getValue().strip();
               String var2 = this.v.getValue();
               if (!var1.isEmpty() && !c(var1)) {
                  this.u = "Enter a Minecraft username (1 to 16 characters)";
               } else if (!var1.isEmpty() || !var2.equals("None") && !var2.equals("Default")) {
                  long var3 = this.Y.incrementAndGet();
                  this.D = (boolean)1;
                  this.u = var1.isEmpty() ? "Loading " + var2 : "Looking up " + var1 + "...";
                  Path var5 = this.a();
                  h.execute(
                     () -> {
                        class_1011 var6 = null;

                        try {
                           byte var8 = 0;
                           byte[] var7;
                           if (var1.isEmpty()) {
                              Path var25 = var5.resolve(var2).normalize();
                              if (!var25.getParent().equals(var5.normalize()) || !var2.toLowerCase(Locale.ROOT).endsWith(".png")) {
                                 throw new IllegalArgumentException("Choose a PNG from the skins folder");
                              }

                              try (InputStream var27 = Files.newInputStream(var25)) {
                                 var7 = var27.readNBytes(131073);
                              }
                           } else {
                              JsonObject var9 = com.larpclient.lh.z("https://api.mojang.com/users/profiles/minecraft/" + var1, 8192);
                              String var10 = var9.get("id").getAsString();
                              if (!var10.matches("[a-fA-F0-9]{32}")) {
                                 throw new IllegalArgumentException("Invalid player profile");
                              }

                              JsonObject var11 = com.larpclient.lh.z("https://sessionserver.mojang.com/session/minecraft/profile/" + var10, 32768);
                              JsonObject var12 = null;

                              for (JsonElement var14 : var11.getAsJsonArray("properties")) {
                                 JsonObject var15 = var14.getAsJsonObject();
                                 if (var15.get("name").getAsString().equals("textures")) {
                                    var12 = JsonParser.parseString(
                                          new String(Base64.getDecoder().decode(var15.get("value").getAsString()), StandardCharsets.UTF_8)
                                       )
                                       .getAsJsonObject()
                                       .getAsJsonObject("textures")
                                       .getAsJsonObject("SKIN");
                                 }
                              }

                              if (var12 == null) {
                                 throw new IllegalArgumentException("Player has no public skin");
                              }

                              String var29 = var12.get("url").getAsString().replace("http://", "https://");
                              if (!var29.matches("https://textures\\.minecraft\\.net/texture/[a-fA-F0-9]+")) {
                                 throw new IllegalArgumentException("Unexpected texture URL");
                              }

                              var8 = (byte)(var12.has("metadata") && "slim".equals(var12.getAsJsonObject("metadata").get("model").getAsString()) ? 1 : 0);
                              var7 = com.larpclient.lh.m(var29, 131072);
                           }

                           J(var7);
                           var6 = class_1011.method_4309(new ByteArrayInputStream(var7));
                           var6 = q5eca7d3b6cc64742.larpProcessSkin(var6, "Skin Changer");
                           class_1011 var26 = var6;
                           var6 = null;
                           byte var28 = var8;
                           mc.execute(() -> {
                              if (var3 == this.Y.get() && this.K()) {
                                 try {
                                    class_2960 var7x = sp.r("skin-changer-" + var3, var26);
                                    this.g();
                                    this.N = var7x;
                                    this.X = var28;
                                    this.A = var1;
                                    this.J = var2;
                                    this.u = var1.isEmpty() ? var2 : "Copied " + var1;
                                    q8.G("SkinChanger", "Skin applied: {}, model={}", this.u, this.z.getValue());
                                 } catch (RuntimeException var8x) {
                                    var26.close();
                                    this.u = "Texture registration failed";
                                    q8.u("SkinChanger", "GPU registration failed; previous skin retained", var8x);
                                 }
                              } else {
                                 var26.close();
                              }
                           });
                        } catch (Exception var23) {
                           mc.execute(() -> {
                              if (var3 == this.Y.get() && this.K()) {
                                 this.u = "Skin failed: " + var23.getMessage();
                                 this.C = System.nanoTime() + 30000000000L;
                                 q8.u("SkinChanger", "Resolve/decode failed; original or previous skin retained", var23);
                              }
                           });
                        } finally {
                           if (var6 != null) {
                              var6.close();
                           }

                           mc.execute(() -> this.D = (boolean)0);
                        }
                     }
                  );
               } else {
                  this.Y.incrementAndGet();
                  this.g();
                  this.A = var1;
                  this.J = var2;
                  this.u = var2.equals("Default") ? "Default Minecraft skin" : "Original skin";
               }
            }
         }
      }
   }

   public static boolean Q(String var0) {
      return var0 != null
         && var0.length() <= 128
         && var0.length() > 4
         && !var0.contains("/")
         && !var0.contains("\\")
         && var0.codePoints().noneMatch(Character::isISOControl)
         && var0.toLowerCase(Locale.ROOT).endsWith(".png");
   }

   public static boolean c(String var0) {
      return var0 != null && var0.matches("[A-Za-z0-9_]{1,16}");
   }

   public static void J(byte[] var0) {
      if (var0.length >= 33 && var0.length <= 131072) {
         ByteBuffer var1 = ByteBuffer.wrap(var0);
         if (var1.getLong() == -8552249625308161526L && var1.getInt() == 13 && var1.getInt() == 1229472850) {
            int var2 = var1.getInt();
            int var3 = var1.getInt();
            if (var2 != 64 || var3 != 32 && var3 != 64) {
               throw new IllegalArgumentException("Skin must be 64x64 or legacy 64x32");
            }
         } else {
            throw new IllegalArgumentException("Expected PNG");
         }
      } else {
         throw new IllegalArgumentException("Skin must be a PNG of at most 128 KiB");
      }
   }

   private void g() {
      sp.G(this.N);
      this.N = null;
   }

   private boolean K() {
      return this.isRuntimeActive() || this.B && this.isOperational();
   }

   public class_8685 q(class_8685 var1) {
      if (var1 == null || this.isRuntimeActive()) {
         return this.r(var1);
      }

      if (this.isOperational()) {
         if (!this.B) {
            this.B = (boolean)1;
            this.A = this.J = "";
            this.C = 0L;
            q8.v("SkinChanger", "Menu preview loading the saved selection without a world");
         }

         this.onTick();
         return this.r(var1);
      } else {
         if (this.B || this.N != null) {
            this.B = (boolean)0;
            this.Y.incrementAndGet();
            this.g();
            this.A = this.J = "";
            q8.v("SkinChanger", "Menu preview ended; local texture released");
         }

         return var1;
      }
   }

   public class_8685 q(UUID var1, class_8685 var2) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() && var2 != null && mc.field_1724 != null && var1.equals(mc.field_1724.method_5667()) ? this.r(var2) : var2;
      } else {
         return var2;
      }
   }

   private class_8685 r(class_8685 var1) {
      if (var1 == null) {
         return null;
      }

      if (this.v.G("None") && this.E.getValue().isBlank() && this.N == null) {
         return var1;
      }

      Object var2 = this.N == null ? (this.v.G("Default") ? class_1068.method_62620().comp_1626() : var1.comp_1626()) : new class_10726(this.N, this.N);
      class_7920 var3 = this.N == null
         ? (this.v.G("Default") ? class_1068.method_62620().comp_1629() : var1.comp_1629())
         : (this.X ? class_7920.field_41122 : class_7920.field_41123);
      class_7920 var4 = this.z.G("Auto") ? var3 : (this.z.G("Slim") ? class_7920.field_41122 : class_7920.field_41123);
      return new class_8685((class_12081)var2, var1.comp_1627(), var1.comp_1628(), var4, var1.comp_1630());
   }
}
