package com.larpclient.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larp.obf.q5682c6a8aca9b6ec.qe213d8ff8a604d6b;
import com.larpclient.el;
import com.larpclient.om;
import com.larpclient.r4;
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
   private static SkinChangerModule b;
   private static final ExecutorService D = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-SkinChanger");
      var1.setDaemon(true);
      return var1;
   });
   private final lp g = this.addSetting(new lp("Skin", "Choose a PNG from the skins folder", "None", "None", "Default") {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(var1) || SkinChangerModule.J(var1);
      }

      @Override
      public void setValue(String var1) {
         if (!this.X().contains(var1) && SkinChangerModule.J(var1)) {
            ArrayList var2 = new ArrayList<>(this.X());
            var2.add(var1);
            this.u(var2);
         }

         super.setValue(var1);
      }
   });
   private final lp Y = this.addSetting(new lp("Model", "Auto uses the copied player's arm type", "Auto", "Auto", "Wide", "Slim"));
   private final li M = this.addSetting(new li("Copy Player", "Minecraft username; leave empty to use the selected skin", ""));
   private final AtomicLong I = new AtomicLong();
   private Identifier v;
   private boolean E;
   private volatile boolean R;
   private volatile String W = "Select a skin or enter a username";
   private String A = "";
   private String m = "";
   private long c;
   private long Q;
   private String P = "";
   private boolean l;
   private boolean a;

   public SkinChangerModule() {
      super("Skin Changer", "Use a custom or copied skin locally", Category.VISUALS, -1);
      b = this;
      this.g.x(() -> this.M.getValue().isBlank());
      this.Y.x(() -> !this.g.i("None") || !this.M.getValue().isBlank());
      this.g.W(var1 -> {
         this.Q = 0L;
         if (this.K()) {
            this.g();
         }
      });
      this.M.W(var1 -> {
         this.I.incrementAndGet();
         this.l = (boolean)1;
         this.Q = 0L;
         this.A = var1.strip();
         this.c = System.nanoTime();
      });
   }

   public static SkinChangerModule b() {
      return b;
   }

   private Path b() {
      return mc.gameDirectory.toPath().resolve("config/larp-client/skins");
   }

   @Override
   public String getStatus() {
      return this.W;
   }

   public void s() {
      Path var1 = this.b();
      D.execute(() -> {
         try {
            Files.createDirectories(var1);
            mc.execute(() -> Blaze3D.openPath(var1));
         } catch (Exception var3) {
            this.W = "Could not open skins folder";
            r4.r("SkinChanger", "Folder stage failed", var3);
         }
      });
   }

   public void h() {
      Path var1 = this.b();
      D.execute(
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
               mc.execute(() -> this.g.u(var9));
            } catch (Exception var8) {
               this.W = "Could not read skins folder";
               r4.r("SkinChanger", "Library stage failed", var8);
            }
         }
      );
   }

   @Override
   public void onEnable() {
      this.m = "";
      this.Q = 0L;
      this.h();
      this.g();
   }

   @Override
   public void onDisable() {
      this.I.incrementAndGet();
      this.l();
      this.m = this.P = "";
      this.W = "Original skin restored";
      r4.V("SkinChanger", "Disabled; local texture released");
   }

   @Override
   public void onTick() {
      String var1 = this.M.getValue().strip();
      long var2 = System.nanoTime();
      int var10000 = var1.equals(this.A);
      boolean var5 = false;
      byte var4 = (byte)var10000;
      var10000 = Integer.signum(var4);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.A = var1;
         this.c = var2;
      }

      boolean var7 = false;
      byte var6 = this.R;
      var10000 = Integer.signum(var6);
      if ((var10000 | -var10000) >>> 31 == 0) {
         boolean var9 = false;
         byte var8 = this.l;
         var10000 = Integer.signum(var8);
         if ((var10000 | -var10000) >>> 31 == 0) {
            int var15 = var1.equals(this.m);
            boolean var11 = false;
            byte var10 = (byte)var15;
            var15 = Integer.signum(var10);
            if (((var15 | -var15) >>> 31 ^ 1) == 0 && this.g.getValue().equals(this.P)) {
               return;
            }
         }

         if (var2 - this.c >= 600000000L && var2 >= this.Q) {
            this.g();
         }
      }
   }

   public void g() {
      if (this.isEnabled()) {
         if (this.K()) {
            if (this.R) {
               this.I.incrementAndGet();
               this.l = (boolean)1;
            } else {
               this.l = (boolean)0;
               String var1 = this.M.getValue().strip();
               String var2 = this.g.getValue();
               if (!var1.isEmpty() && !r(var1)) {
                  this.W = "Enter a Minecraft username (1 to 16 characters)";
               } else if (!var1.isEmpty() || !var2.equals("None") && !var2.equals("Default")) {
                  long var3 = this.I.incrementAndGet();
                  this.R = (boolean)1;
                  this.W = var1.isEmpty() ? "Loading " + var2 : "Looking up " + var1 + "...";
                  Path var5 = this.b();
                  D.execute(
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
                              JsonObject var9 = el.h("https://api.mojang.com/users/profiles/minecraft/" + var1, 8192);
                              String var10 = var9.get("id").getAsString();
                              if (!var10.matches("[a-fA-F0-9]{32}")) {
                                 throw new IllegalArgumentException("Invalid player profile");
                              }

                              JsonObject var11 = el.h("https://sessionserver.mojang.com/session/minecraft/profile/" + var10, 32768);
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
                              var7 = el.g(var29, 131072);
                           }

                           w(var7);
                           var6 = NativeImage.read(new ByteArrayInputStream(var7));
                           var6 = qe213d8ff8a604d6b.larpProcessSkin(var6, "Skin Changer");
                           NativeImage var26 = var6;
                           var6 = null;
                           byte var28 = var8;
                           mc.execute(() -> {
                              if (var3 == this.I.get() && this.K()) {
                                 try {
                                    Identifier var7x = om.W("skin-changer-" + var3, var26);
                                    this.l();
                                    this.v = var7x;
                                    this.E = var28;
                                    this.m = var1;
                                    this.P = var2;
                                    this.W = var1.isEmpty() ? var2 : "Copied " + var1;
                                    r4.V("SkinChanger", "Skin applied: {}, model={}", this.W, this.Y.getValue());
                                 } catch (RuntimeException var8x) {
                                    var26.close();
                                    this.W = "Texture registration failed";
                                    r4.r("SkinChanger", "GPU registration failed; previous skin retained", var8x);
                                 }
                              } else {
                                 var26.close();
                              }
                           });
                        } catch (Exception var23) {
                           mc.execute(() -> {
                              if (var3 == this.I.get() && this.K()) {
                                 this.W = "Skin failed: " + var23.getMessage();
                                 this.Q = System.nanoTime() + 30000000000L;
                                 r4.r("SkinChanger", "Resolve/decode failed; original or previous skin retained", var23);
                              }
                           });
                        } finally {
                           if (var6 != null) {
                              var6.close();
                           }

                           mc.execute(() -> this.R = (boolean)0);
                        }
                     }
                  );
               } else {
                  this.I.incrementAndGet();
                  this.l();
                  this.m = var1;
                  this.P = var2;
                  this.W = var2.equals("Default") ? "Default Minecraft skin" : "Original skin";
               }
            }
         }
      }
   }

   public static boolean J(String var0) {
      return var0 != null
         && var0.length() <= 128
         && var0.length() > 4
         && !var0.contains("/")
         && !var0.contains("\\")
         && var0.codePoints().noneMatch(Character::isISOControl)
         && var0.toLowerCase(Locale.ROOT).endsWith(".png");
   }

   public static boolean r(String var0) {
      return var0 != null && var0.matches("[A-Za-z0-9_]{1,16}");
   }

   public static void w(byte[] var0) {
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

   private void l() {
      om.B(this.v);
      this.v = null;
   }

   private boolean K() {
      return this.isRuntimeActive() || this.a && this.isOperational();
   }

   public PlayerSkin b(PlayerSkin var1) {
      if (var1 == null || this.isRuntimeActive()) {
         return this.m(var1);
      }

      if (this.isOperational()) {
         if (!this.a) {
            this.a = (boolean)1;
            this.m = this.P = "";
            this.Q = 0L;
            r4.g("SkinChanger", "Menu preview loading the saved selection without a world");
         }

         this.onTick();
         return this.m(var1);
      } else {
         if (this.a || this.v != null) {
            this.a = (boolean)0;
            this.I.incrementAndGet();
            this.l();
            this.m = this.P = "";
            r4.g("SkinChanger", "Menu preview ended; local texture released");
         }

         return var1;
      }
   }

   public PlayerSkin C(UUID var1, PlayerSkin var2) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() && var2 != null && mc.player != null && var1.equals(mc.player.getUUID()) ? this.m(var2) : var2;
      } else {
         return var2;
      }
   }

   private PlayerSkin m(PlayerSkin var1) {
      if (var1 == null) {
         return null;
      }

      if (this.g.i("None") && this.M.getValue().isBlank() && this.v == null) {
         return var1;
      }

      Object var2 = this.v == null ? (this.g.i("Default") ? DefaultPlayerSkin.getDefaultSkin().body() : var1.body()) : new ResourceTexture(this.v, this.v);
      PlayerModelType var3 = this.v == null
         ? (this.g.i("Default") ? DefaultPlayerSkin.getDefaultSkin().model() : var1.model())
         : (this.E ? PlayerModelType.SLIM : PlayerModelType.WIDE);
      PlayerModelType var4 = this.Y.i("Auto") ? var3 : (this.Y.i("Slim") ? PlayerModelType.SLIM : PlayerModelType.WIDE);
      return new PlayerSkin((Texture)var2, var1.cape(), var1.elytra(), var4, var1.secure());
   }
}
