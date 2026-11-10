package com.larpclient.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larp.obf.qa300483f54d804a3.q177213fd6a234142;
import com.larpclient.j3;
import com.larpclient.w2;
import com.mojang.blaze3d.platform.NativeImage;
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
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.ClientAsset.ResourceTexture;
import net.minecraft.core.ClientAsset.Texture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;

public final class SkinChangerModule extends Module {
   private static SkinChangerModule s;
   private static final ExecutorService Q = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-SkinChanger");
      var1.setDaemon(true);
      return var1;
   });
   private final l q = this.addSetting(new l("Skin", "Choose a PNG from the skins folder", "None", "None", "Default") {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(var1) || SkinChangerModule.h(var1);
      }

      @Override
      public void setValue(String var1) {
         if (!this.a().contains(var1) && SkinChangerModule.h(var1)) {
            ArrayList var2 = new ArrayList<>(this.a());
            var2.add(var1);
            this.G(var2);
         }

         super.setValue(var1);
      }
   });
   private final l x = this.addSetting(new l("Model", "Auto uses the copied player's arm type", "Auto", "Auto", "Wide", "Slim"));
   private final g n = this.addSetting(new g("Copy Player", "Minecraft username; leave empty to use the selected skin", ""));
   private final AtomicLong h = new AtomicLong();
   private Identifier H;
   private boolean D;
   private volatile boolean c;
   private volatile String V = "Select a skin or enter a username";
   private String o = "";
   private String t = "";
   private long R;
   private long X;
   private String I = "";
   private boolean w;
   private boolean F;

   public SkinChangerModule() {
      super("Skin Changer", "Use a custom or copied skin locally", Category.VISUALS, -1);
      s = this;
      this.q.P(() -> this.n.getValue().isBlank());
      this.x.P(() -> !this.q.O("None") || !this.n.getValue().isBlank());
      this.q.o(var1 -> {
         this.X = 0L;
         if (this.U()) {
            this.F();
         }
      });
      this.n.o(var1 -> {
         this.h.incrementAndGet();
         this.w = (boolean)1;
         this.X = 0L;
         this.o = var1.strip();
         this.R = System.nanoTime();
      });
   }

   public static SkinChangerModule X() {
      return s;
   }

   private Path p() {
      return mc.gameDirectory.toPath().resolve("config/larp-client/skins");
   }

   @Override
   public String getStatus() {
      return this.V;
   }

   public void J() {
      Path var1 = this.p();
      Q.execute(() -> {
         try {
            Files.createDirectories(var1);
            mc.execute(() -> Util.getPlatform().openPath(var1));
         } catch (Exception var3) {
            this.V = "Could not open skins folder";
            com.larpclient.h.m("SkinChanger", "Folder stage failed", var3);
         }
      });
   }

   public void r() {
      Path var1 = this.p();
      Q.execute(
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
               mc.execute(() -> this.q.G(var9));
            } catch (Exception var8) {
               this.V = "Could not read skins folder";
               com.larpclient.h.m("SkinChanger", "Library stage failed", var8);
            }
         }
      );
   }

   @Override
   public void onEnable() {
      this.t = "";
      this.X = 0L;
      this.r();
      this.F();
   }

   @Override
   public void onDisable() {
      this.h.incrementAndGet();
      this.P();
      this.t = this.I = "";
      this.V = "Original skin restored";
      com.larpclient.h.A("SkinChanger", "Disabled; local texture released");
   }

   @Override
   public void onTick() {
      String var1 = this.n.getValue().strip();
      long var2 = System.nanoTime();
      int var10000 = var1.equals(this.o);
      boolean var5 = false;
      byte var4 = (byte)var10000;
      var10000 = Integer.signum(var4);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.o = var1;
         this.R = var2;
      }

      boolean var7 = false;
      byte var6 = this.c;
      var10000 = Integer.signum(var6);
      if ((var10000 | -var10000) >>> 31 == 0) {
         boolean var9 = false;
         byte var8 = this.w;
         var10000 = Integer.signum(var8);
         if ((var10000 | -var10000) >>> 31 != 0 || !var1.equals(this.t) || !this.q.getValue().equals(this.I)) {
            long var16;
            var10000 = (var16 = var2 - this.R - 600000000L) == 0L ? 0 : (var16 < 0L ? -1 : 1);
            boolean var11 = false;
            int var10 = var10000;
            if (Integer.signum(var10) >>> 31 == 0 && var2 >= this.X) {
               this.F();
            }
         }
      }
   }

   public void F() {
      if (this.isEnabled()) {
         if (this.U()) {
            if (this.c) {
               this.h.incrementAndGet();
               this.w = (boolean)1;
            } else {
               this.w = (boolean)0;
               String var1 = this.n.getValue().strip();
               String var2 = this.q.getValue();
               if (!var1.isEmpty() && !Z(var1)) {
                  this.V = "Enter a Minecraft username (1 to 16 characters)";
               } else if (!var1.isEmpty() || !var2.equals("None") && !var2.equals("Default")) {
                  long var3 = this.h.incrementAndGet();
                  this.c = (boolean)1;
                  this.V = var1.isEmpty() ? "Loading " + var2 : "Looking up " + var1 + "...";
                  Path var5 = this.p();
                  Q.execute(
                     () -> {
                        NativeImage var6 = null;

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
                              JsonObject var9 = w2.y("https://api.mojang.com/users/profiles/minecraft/" + var1, 8192);
                              String var10 = var9.get("id").getAsString();
                              if (!var10.matches("[a-fA-F0-9]{32}")) {
                                 throw new IllegalArgumentException("Invalid player profile");
                              }

                              JsonObject var11 = w2.y("https://sessionserver.mojang.com/session/minecraft/profile/" + var10, 32768);
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
                              var7 = w2.J(var29, 131072);
                           }

                           r(var7);
                           var6 = NativeImage.read(new ByteArrayInputStream(var7));
                           var6 = q177213fd6a234142.larpProcessSkin(var6, "Skin Changer");
                           NativeImage var26 = var6;
                           var6 = null;
                           byte var28 = var8;
                           mc.execute(() -> {
                              if (var3 == this.h.get() && this.U()) {
                                 try {
                                    Identifier var7x = j3.g("skin-changer-" + var3, var26);
                                    this.P();
                                    this.H = var7x;
                                    this.D = var28;
                                    this.t = var1;
                                    this.I = var2;
                                    this.V = var1.isEmpty() ? var2 : "Copied " + var1;
                                    com.larpclient.h.A("SkinChanger", "Skin applied: {}, model={}", this.V, this.x.getValue());
                                 } catch (RuntimeException var8x) {
                                    var26.close();
                                    this.V = "Texture registration failed";
                                    com.larpclient.h.m("SkinChanger", "GPU registration failed; previous skin retained", var8x);
                                 }
                              } else {
                                 var26.close();
                              }
                           });
                        } catch (Exception var23) {
                           mc.execute(() -> {
                              if (var3 == this.h.get() && this.U()) {
                                 this.V = "Skin failed: " + var23.getMessage();
                                 this.X = System.nanoTime() + 30000000000L;
                                 com.larpclient.h.m("SkinChanger", "Resolve/decode failed; original or previous skin retained", var23);
                              }
                           });
                        } finally {
                           if (var6 != null) {
                              var6.close();
                           }

                           mc.execute(() -> this.c = (boolean)0);
                        }
                     }
                  );
               } else {
                  this.h.incrementAndGet();
                  this.P();
                  this.t = var1;
                  this.I = var2;
                  this.V = var2.equals("Default") ? "Default Minecraft skin" : "Original skin";
               }
            }
         }
      }
   }

   public static boolean h(String var0) {
      return var0 != null
         && var0.length() <= 128
         && var0.length() > 4
         && !var0.contains("/")
         && !var0.contains("\\")
         && var0.codePoints().noneMatch(Character::isISOControl)
         && var0.toLowerCase(Locale.ROOT).endsWith(".png");
   }

   public static boolean Z(String var0) {
      return var0 != null && var0.matches("[A-Za-z0-9_]{1,16}");
   }

   public static void r(byte[] var0) {
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

   private void P() {
      j3.p(this.H);
      this.H = null;
   }

   private boolean U() {
      return this.isRuntimeActive() || this.F && this.isOperational();
   }

   public PlayerSkin L(PlayerSkin var1) {
      if (var1 == null || this.isRuntimeActive()) {
         return this.Z(var1);
      }

      if (this.isOperational()) {
         if (!this.F) {
            this.F = (boolean)1;
            this.t = this.I = "";
            this.X = 0L;
            com.larpclient.h.U("SkinChanger", "Menu preview loading the saved selection without a world");
         }

         this.onTick();
         return this.Z(var1);
      } else {
         if (this.F || this.H != null) {
            this.F = (boolean)0;
            this.h.incrementAndGet();
            this.P();
            this.t = this.I = "";
            com.larpclient.h.U("SkinChanger", "Menu preview ended; local texture released");
         }

         return var1;
      }
   }

   public PlayerSkin I(UUID var1, PlayerSkin var2) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() && var2 != null && mc.player != null && var1.equals(mc.player.getUUID()) ? this.Z(var2) : var2;
      } else {
         return var2;
      }
   }

   private PlayerSkin Z(PlayerSkin var1) {
      if (var1 == null) {
         return null;
      }

      if (this.q.O("None") && this.n.getValue().isBlank() && this.H == null) {
         return var1;
      }

      Object var2 = this.H == null ? (this.q.O("Default") ? DefaultPlayerSkin.getDefaultSkin().body() : var1.body()) : new ResourceTexture(this.H, this.H);
      PlayerModelType var3 = this.H == null
         ? (this.q.O("Default") ? DefaultPlayerSkin.getDefaultSkin().model() : var1.model())
         : (this.D ? PlayerModelType.SLIM : PlayerModelType.WIDE);
      PlayerModelType var4 = this.x.O("Auto") ? var3 : (this.x.O("Slim") ? PlayerModelType.SLIM : PlayerModelType.WIDE);
      return new PlayerSkin((Texture)var2, var1.cape(), var1.elytra(), var4, var1.secure());
   }
}
