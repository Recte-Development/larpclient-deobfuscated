package com.larpclient.module.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.mixin.SkinTextureDownloaderMixin;
import com.larpclient.network.HttpsFetcher;
import com.larpclient.render.entity.AppearanceTextures;
import com.larpclient.util.misc.LarpLog;
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
   private static SkinChangerModule instance;
   private static final ExecutorService eXECUTOR = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-SkinChanger");
      var1.setDaemon(true);
      return var1;
   });
   private final ModeSetting skin = this.addSetting(new ModeSetting("Skin", "Choose a PNG from the skins folder", "None", "None", "Default") {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(var1) || SkinChangerModule.loadSkin(var1);
      }

      @Override
      public void setValue(String var1) {
         if (!this.getModes().contains(var1) && SkinChangerModule.loadSkin(var1)) {
            ArrayList var2 = new ArrayList<>(this.getModes());
            var2.add(var1);
            this.setModes(var2);
         }

         super.setValue(var1);
      }
   });
   private final ModeSetting model = this.addSetting(new ModeSetting("Model", "Auto uses the copied player's arm type", "Auto", "Auto", "Wide", "Slim"));
   private final TextSetting copyPlayer = this.addSetting(new TextSetting("Copy Player", "Minecraft username; leave empty to use the selected skin", ""));
   private final AtomicLong loadGeneration = new AtomicLong();
   private Identifier texture;
   private boolean slim;
   private volatile boolean loading;
   private volatile String status = "Select a skin or enter a username";
   private String lastUsername = "";
   private String appliedUsername = "";
   private long usernameChangedNanos;
   private long retryAfterNanos;
   private String appliedSkin = "";
   private boolean reloadPending;
   private boolean menuPreview;

   public SkinChangerModule() {
      super("Skin Changer", "Use a custom or copied skin locally", Category.VISUALS, -1);
      instance = this;
      this.skin.visibleWhen(() -> this.copyPlayer.getValue().isBlank());
      this.model.visibleWhen(() -> !this.skin.isMode("None") || !this.copyPlayer.getValue().isBlank());
      this.skin.onChange(var1 -> {
         this.retryAfterNanos = 0L;
         if (this.isActive()) {
            this.loadSkin();
         }
      });
      this.copyPlayer.onChange(var1 -> {
         this.loadGeneration.incrementAndGet();
         this.reloadPending = (boolean)1;
         this.retryAfterNanos = 0L;
         this.lastUsername = var1.strip();
         this.usernameChangedNanos = System.nanoTime();
      });
   }

   public static SkinChangerModule getInstance() {
      return instance;
   }

   private Path getSkinsDirectory() {
      return minecraft.gameDirectory.toPath().resolve("config/larp-client/skins");
   }

   @Override
   public String getStatus() {
      return this.status;
   }

   public void openSkinsFolder() {
      Path var1 = this.getSkinsDirectory();
      eXECUTOR.execute(() -> {
         try {
            Files.createDirectories(var1);
            minecraft.execute(() -> Blaze3D.openPath(var1));
         } catch (Exception var3) {
            this.status = "Could not open skins folder";
            LarpLog.error("SkinChanger", "Folder stage failed", var3);
         }
      });
   }

   public void getInstance() {
      Path var1 = this.getSkinsDirectory();
      eXECUTOR.execute(
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
               minecraft.execute(() -> this.skin.setModes(var9));
            } catch (Exception var8) {
               this.status = "Could not read skins folder";
               LarpLog.error("SkinChanger", "Library stage failed", var8);
            }
         }
      );
   }

   @Override
   public void onEnable() {
      this.appliedUsername = "";
      this.retryAfterNanos = 0L;
      this.getInstance();
      this.loadSkin();
   }

   @Override
   public void onDisable() {
      this.loadGeneration.incrementAndGet();
      this.releaseTexture();
      this.appliedUsername = this.appliedSkin = "";
      this.status = "Original skin restored";
      LarpLog.info("SkinChanger", "Disabled; local texture released");
   }

   @Override
   public void onTick() {
      String var1 = this.copyPlayer.getValue().strip();
      long var2 = System.nanoTime();
      if (!var1.equals(this.lastUsername)) {
         this.lastUsername = var1;
         this.usernameChangedNanos = var2;
      }

      boolean var5 = false;
      byte var4 = this.loading;
      int var10000 = Integer.signum(var4);
      if ((var10000 | -var10000) >>> 31 == 0) {
         boolean var7 = false;
         byte var6 = this.reloadPending;
         var10000 = Integer.signum(var6);
         if ((var10000 | -var10000) >>> 31 == 0) {
            int var13 = var1.equals(this.appliedUsername);
            boolean var9 = false;
            byte var8 = (byte)var13;
            var13 = Integer.signum(var8);
            if (((var13 | -var13) >>> 31 ^ 1) == 0 && this.skin.getValue().equals(this.appliedSkin)) {
               return;
            }
         }

         long var16;
         var10000 = (var16 = var2 - this.usernameChangedNanos - 600000000L) == 0L ? 0 : (var16 < 0L ? -1 : 1);
         boolean var11 = false;
         int var10 = var10000;
         if (Integer.signum(var10) >>> 31 == 0 && var2 >= this.retryAfterNanos) {
            this.loadSkin();
         }
      }
   }

   public void loadSkin() {
      if (this.isEnabled()) {
         if (this.isActive()) {
            if (this.loading) {
               this.loadGeneration.incrementAndGet();
               this.reloadPending = (boolean)1;
            } else {
               this.reloadPending = (boolean)0;
               String var1 = this.copyPlayer.getValue().strip();
               String var2 = this.skin.getValue();
               if (!var1.isEmpty() && !isValidUsername(var1)) {
                  this.status = "Enter a Minecraft username (1 to 16 characters)";
               } else if (!var1.isEmpty() || !var2.equals("None") && !var2.equals("Default")) {
                  long var3 = this.loadGeneration.incrementAndGet();
                  this.loading = (boolean)1;
                  this.status = var1.isEmpty() ? "Loading " + var2 : "Looking up " + var1 + "...";
                  Path var5 = this.getSkinsDirectory();
                  eXECUTOR.execute(
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
                              JsonObject var9 = HttpsFetcher.fetchJson("https://api.mojang.com/users/profiles/minecraft/" + var1, 8192);
                              String var10 = var9.get("id").getAsString();
                              if (!var10.matches("[a-fA-F0-9]{32}")) {
                                 throw new IllegalArgumentException("Invalid player profile");
                              }

                              JsonObject var11 = HttpsFetcher.fetchJson("https://sessionserver.mojang.com/session/minecraft/profile/" + var10, 32768);
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
                              var7 = HttpsFetcher.fetchJsonBytes(var29, 131072);
                           }

                           validatePng(var7);
                           var6 = NativeImage.read(new ByteArrayInputStream(var7));
                           var6 = SkinTextureDownloaderMixin.larpProcessSkin(var6, "Skin Changer");
                           NativeImage var26 = var6;
                           var6 = null;
                           byte var28 = var8;
                           minecraft.execute(() -> {
                              if (var3 == this.loadGeneration.get() && this.isActive()) {
                                 try {
                                    Identifier var7x = AppearanceTextures.registerTexture("skin-changer-" + var3, var26);
                                    this.releaseTexture();
                                    this.texture = var7x;
                                    this.slim = var28;
                                    this.appliedUsername = var1;
                                    this.appliedSkin = var2;
                                    this.status = var1.isEmpty() ? var2 : "Copied " + var1;
                                    LarpLog.info("SkinChanger", "Skin applied: {}, model={}", this.status, this.model.getValue());
                                 } catch (RuntimeException var8x) {
                                    var26.close();
                                    this.status = "Texture registration failed";
                                    LarpLog.error("SkinChanger", "GPU registration failed; previous skin retained", var8x);
                                 }
                              } else {
                                 var26.close();
                              }
                           });
                        } catch (Exception var23) {
                           minecraft.execute(() -> {
                              if (var3 == this.loadGeneration.get() && this.isActive()) {
                                 this.status = "Skin failed: " + var23.getMessage();
                                 this.retryAfterNanos = System.nanoTime() + 30000000000L;
                                 LarpLog.error("SkinChanger", "Resolve/decode failed; original or previous skin retained", var23);
                              }
                           });
                        } finally {
                           if (var6 != null) {
                              var6.close();
                           }

                           minecraft.execute(() -> this.loading = (boolean)0);
                        }
                     }
                  );
               } else {
                  this.loadGeneration.incrementAndGet();
                  this.releaseTexture();
                  this.appliedUsername = var1;
                  this.appliedSkin = var2;
                  this.status = var2.equals("Default") ? "Default Minecraft skin" : "Original skin";
               }
            }
         }
      }
   }

   public static boolean loadSkin(String var0) {
      return var0 != null
         && var0.length() <= 128
         && var0.length() > 4
         && !var0.contains("/")
         && !var0.contains("\\")
         && var0.codePoints().noneMatch(Character::isISOControl)
         && var0.toLowerCase(Locale.ROOT).endsWith(".png");
   }

   public static boolean isValidUsername(String var0) {
      return var0 != null && var0.matches("[A-Za-z0-9_]{1,16}");
   }

   public static void validatePng(byte[] var0) {
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

   private void releaseTexture() {
      AppearanceTextures.releaseTexture(this.texture);
      this.texture = null;
   }

   private boolean isActive() {
      return this.isRuntimeActive() || this.menuPreview && this.isOperational();
   }

   public PlayerSkin overrideSkin(PlayerSkin var1) {
      if (var1 == null || this.isRuntimeActive()) {
         return this.buildSkin(var1);
      }

      if (this.isOperational()) {
         if (!this.menuPreview) {
            this.menuPreview = (boolean)1;
            this.appliedUsername = this.appliedSkin = "";
            this.retryAfterNanos = 0L;
            LarpLog.debug("SkinChanger", "Menu preview loading the saved selection without a world");
         }

         this.onTick();
         return this.buildSkin(var1);
      } else {
         if (this.menuPreview || this.texture != null) {
            this.menuPreview = (boolean)0;
            this.loadGeneration.incrementAndGet();
            this.releaseTexture();
            this.appliedUsername = this.appliedSkin = "";
            LarpLog.debug("SkinChanger", "Menu preview ended; local texture released");
         }

         return var1;
      }
   }

   public PlayerSkin overrideSkinFor(UUID var1, PlayerSkin var2) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() && var2 != null && minecraft.player != null && var1.equals(minecraft.player.getUUID()) ? this.buildSkin(var2) : var2;
      } else {
         return var2;
      }
   }

   private PlayerSkin buildSkin(PlayerSkin var1) {
      if (var1 == null) {
         return null;
      }

      if (this.skin.isMode("None") && this.copyPlayer.getValue().isBlank() && this.texture == null) {
         return var1;
      }

      Object var2 = this.texture == null
         ? (this.skin.isMode("Default") ? DefaultPlayerSkin.getDefaultSkin().body() : var1.body())
         : new ResourceTexture(this.texture, this.texture);
      PlayerModelType var3 = this.texture == null
         ? (this.skin.isMode("Default") ? DefaultPlayerSkin.getDefaultSkin().model() : var1.model())
         : (this.slim ? PlayerModelType.SLIM : PlayerModelType.WIDE);
      PlayerModelType var4 = this.model.isMode("Auto") ? var3 : (this.model.isMode("Slim") ? PlayerModelType.SLIM : PlayerModelType.WIDE);
      return new PlayerSkin((Texture)var2, var1.cape(), var1.elytra(), var4, var1.secure());
   }
}
