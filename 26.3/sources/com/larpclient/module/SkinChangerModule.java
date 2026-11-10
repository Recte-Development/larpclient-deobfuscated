package com.larpclient.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larp.obf.q41a5851bcf8e876a.q592208476f60cc37;
import com.larpclient._q;
import com.larpclient.ao;
import com.larpclient.by;
import com.mojang.blaze3d.Blaze3D;
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
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;

public final class SkinChangerModule extends Module {
   private static SkinChangerModule V;
   private static final ExecutorService P = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-SkinChanger");
      var1.setDaemon(true);
      return var1;
   });
   private final o Z = this.addSetting(new o("Skin", "Choose a PNG from the skins folder", "None", "None", "Default") {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(var1) || SkinChangerModule.l(var1);
      }

      @Override
      public void setValue(String var1) {
         if (!this.j().contains(var1) && SkinChangerModule.l(var1)) {
            ArrayList var2 = new ArrayList<>(this.j());
            var2.add(var1);
            this.y(var2);
         }

         super.setValue(var1);
      }
   });
   private final o e = this.addSetting(new o("Model", "Auto uses the copied player's arm type", "Auto", "Auto", "Wide", "Slim"));
   private final e m = this.addSetting(new e("Copy Player", "Minecraft username; leave empty to use the selected skin", ""));
   private final AtomicLong w = new AtomicLong();
   private Identifier N;
   private boolean r;
   private volatile boolean S;
   private volatile String T = "Select a skin or enter a username";
   private String s = "";
   private String J = "";
   private long K;
   private long g;
   private String j = "";
   private boolean D;
   private boolean A;

   public SkinChangerModule() {
      super("Skin Changer", "Use a custom or copied skin locally", Category.VISUALS, -1);
      V = this;
      this.Z.a(() -> this.m.getValue().isBlank());
      this.e.a(() -> !this.Z.w("None") || !this.m.getValue().isBlank());
      this.Z.w(var1 -> {
         this.g = 0L;
         if (this.f()) {
            this.l();
         }
      });
      this.m.w(var1 -> {
         this.w.incrementAndGet();
         this.D = (boolean)1;
         this.g = 0L;
         this.s = var1.strip();
         this.K = System.nanoTime();
      });
   }

   public static SkinChangerModule h() {
      return V;
   }

   private Path e() {
      return mc.gameDirectory.toPath().resolve("config/larp-client/skins");
   }

   @Override
   public String getStatus() {
      return this.T;
   }

   public void Z() {
      Path var1 = this.e();
      P.execute(() -> {
         try {
            Files.createDirectories(var1);
            mc.execute(() -> Blaze3D.openPath(var1));
         } catch (Exception var3) {
            this.T = "Could not open skins folder";
            _q.o("SkinChanger", "Folder stage failed", var3);
         }
      });
   }

   public void h() {
      Path var1 = this.e();
      P.execute(
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
               mc.execute(() -> this.Z.y(var9));
            } catch (Exception var8) {
               this.T = "Could not read skins folder";
               _q.o("SkinChanger", "Library stage failed", var8);
            }
         }
      );
   }

   @Override
   public void onEnable() {
      this.J = "";
      this.g = 0L;
      this.h();
      this.l();
   }

   @Override
   public void onDisable() {
      this.w.incrementAndGet();
      this.j();
      this.J = this.j = "";
      this.T = "Original skin restored";
      _q.z("SkinChanger", "Disabled; local texture released");
   }

   @Override
   public void onTick() {
      String var1 = this.m.getValue().strip();
      long var2 = System.nanoTime();
      if (!var1.equals(this.s)) {
         this.s = var1;
         this.K = var2;
      }

      boolean var5 = false;
      byte var4 = this.S;
      int var10000 = Integer.signum(var4);
      if ((var10000 | -var10000) >>> 31 == 0) {
         boolean var7 = false;
         byte var6 = this.D;
         var10000 = Integer.signum(var6);
         if ((var10000 | -var10000) >>> 31 == 0) {
            int var13 = var1.equals(this.J);
            boolean var9 = false;
            byte var8 = (byte)var13;
            var13 = Integer.signum(var8);
            if (((var13 | -var13) >>> 31 ^ 1) == 0 && this.Z.getValue().equals(this.j)) {
               return;
            }
         }

         long var16;
         var10000 = (var16 = var2 - this.K - 600000000L) == 0L ? 0 : (var16 < 0L ? -1 : 1);
         boolean var11 = false;
         int var10 = var10000;
         if (Integer.signum(var10) >>> 31 == 0 && var2 >= this.g) {
            this.l();
         }
      }
   }

   public void l() {
      if (this.isEnabled()) {
         if (this.f()) {
            if (this.S) {
               this.w.incrementAndGet();
               this.D = (boolean)1;
            } else {
               this.D = (boolean)0;
               String var1 = this.m.getValue().strip();
               String var2 = this.Z.getValue();
               if (!var1.isEmpty() && !X(var1)) {
                  this.T = "Enter a Minecraft username (1 to 16 characters)";
               } else if (!var1.isEmpty() || !var2.equals("None") && !var2.equals("Default")) {
                  long var3 = this.w.incrementAndGet();
                  this.S = (boolean)1;
                  this.T = var1.isEmpty() ? "Loading " + var2 : "Looking up " + var1 + "...";
                  Path var5 = this.e();
                  P.execute(
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
                              JsonObject var9 = by.M("https://api.mojang.com/users/profiles/minecraft/" + var1, 8192);
                              String var10 = var9.get("id").getAsString();
                              if (!var10.matches("[a-fA-F0-9]{32}")) {
                                 throw new IllegalArgumentException("Invalid player profile");
                              }

                              JsonObject var11 = by.M("https://sessionserver.mojang.com/session/minecraft/profile/" + var10, 32768);
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
                              var7 = by.a(var29, 131072);
                           }

                           c(var7);
                           var6 = NativeImage.read(new ByteArrayInputStream(var7));
                           var6 = q592208476f60cc37.larpProcessSkin(var6, "Skin Changer");
                           NativeImage var26 = var6;
                           var6 = null;
                           byte var28 = var8;
                           mc.execute(() -> {
                              if (var3 == this.w.get() && this.f()) {
                                 try {
                                    Identifier var7x = ao.P("skin-changer-" + var3, var26);
                                    this.j();
                                    this.N = var7x;
                                    this.r = var28;
                                    this.J = var1;
                                    this.j = var2;
                                    this.T = var1.isEmpty() ? var2 : "Copied " + var1;
                                    _q.z("SkinChanger", "Skin applied: {}, model={}", this.T, this.e.getValue());
                                 } catch (RuntimeException var8x) {
                                    var26.close();
                                    this.T = "Texture registration failed";
                                    _q.o("SkinChanger", "GPU registration failed; previous skin retained", var8x);
                                 }
                              } else {
                                 var26.close();
                              }
                           });
                        } catch (Exception var23) {
                           mc.execute(() -> {
                              if (var3 == this.w.get() && this.f()) {
                                 this.T = "Skin failed: " + var23.getMessage();
                                 this.g = System.nanoTime() + 30000000000L;
                                 _q.o("SkinChanger", "Resolve/decode failed; original or previous skin retained", var23);
                              }
                           });
                        } finally {
                           if (var6 != null) {
                              var6.close();
                           }

                           mc.execute(() -> this.S = (boolean)0);
                        }
                     }
                  );
               } else {
                  this.w.incrementAndGet();
                  this.j();
                  this.J = var1;
                  this.j = var2;
                  this.T = var2.equals("Default") ? "Default Minecraft skin" : "Original skin";
               }
            }
         }
      }
   }

   public static boolean l(String var0) {
      return var0 != null
         && var0.length() <= 128
         && var0.length() > 4
         && !var0.contains("/")
         && !var0.contains("\\")
         && var0.codePoints().noneMatch(Character::isISOControl)
         && var0.toLowerCase(Locale.ROOT).endsWith(".png");
   }

   public static boolean X(String var0) {
      return var0 != null && var0.matches("[A-Za-z0-9_]{1,16}");
   }

   public static void c(byte[] var0) {
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

   private void j() {
      ao.z(this.N);
      this.N = null;
   }

   private boolean f() {
      return this.isRuntimeActive() || this.A && this.isOperational();
   }

   public PlayerSkin b(PlayerSkin var1) {
      if (var1 == null || this.isRuntimeActive()) {
         return this.s(var1);
      }

      if (this.isOperational()) {
         if (!this.A) {
            this.A = (boolean)1;
            this.J = this.j = "";
            this.g = 0L;
            _q.c("SkinChanger", "Menu preview loading the saved selection without a world");
         }

         this.onTick();
         return this.s(var1);
      } else {
         if (this.A || this.N != null) {
            this.A = (boolean)0;
            this.w.incrementAndGet();
            this.j();
            this.J = this.j = "";
            _q.c("SkinChanger", "Menu preview ended; local texture released");
         }

         return var1;
      }
   }

   public PlayerSkin n(UUID var1, PlayerSkin var2) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() && var2 != null && mc.player != null && var1.equals(mc.player.getUUID()) ? this.s(var2) : var2;
      } else {
         return var2;
      }
   }

   private PlayerSkin s(PlayerSkin var1) {
      if (var1 == null) {
         return null;
      }

      if (this.Z.w("None") && this.m.getValue().isBlank() && this.N == null) {
         return var1;
      }

      Object var2 = this.N == null ? (this.Z.w("Default") ? DefaultPlayerSkin.getDefaultSkin().body() : var1.body()) : new ResourceTexture(this.N, this.N);
      PlayerModelType var3 = this.N == null
         ? (this.Z.w("Default") ? DefaultPlayerSkin.getDefaultSkin().model() : var1.model())
         : (this.r ? PlayerModelType.SLIM : PlayerModelType.WIDE);
      PlayerModelType var4 = this.e.w("Auto") ? var3 : (this.e.w("Slim") ? PlayerModelType.SLIM : PlayerModelType.WIDE);
      return new PlayerSkin((Texture)var2, var1.cape(), var1.elytra(), var4, var1.secure());
   }
}
