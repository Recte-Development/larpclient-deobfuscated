package com.larpclient.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larp.obf.qbac0b262827a15ac.q6b37d34510312db9;
import com.larpclient.el;
import com.larpclient.mt;
import com.larpclient.ts;
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
   private static SkinChangerModule o;
   private static final ExecutorService W = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-SkinChanger");
      var1.setDaemon(true);
      return var1;
   });
   private final d6 I = this.addSetting(new d6("Skin", "Choose a PNG from the skins folder", "None", "None", "Default") {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(var1) || SkinChangerModule.L(var1);
      }

      @Override
      public void setValue(String var1) {
         if (!this.N().contains(var1) && SkinChangerModule.L(var1)) {
            ArrayList var2 = new ArrayList<>(this.N());
            var2.add(var1);
            this.Y(var2);
         }

         super.setValue(var1);
      }
   });
   private final d6 l = this.addSetting(new d6("Model", "Auto uses the copied player's arm type", "Auto", "Auto", "Wide", "Slim"));
   private final ds d = this.addSetting(new ds("Copy Player", "Minecraft username; leave empty to use the selected skin", ""));
   private final AtomicLong n = new AtomicLong();
   private Identifier H;
   private boolean w;
   private volatile boolean i;
   private volatile String N = "Select a skin or enter a username";
   private String z = "";
   private String K = "";
   private long e;
   private long U;
   private String j = "";
   private boolean P;
   private boolean R;

   public SkinChangerModule() {
      super("Skin Changer", "Use a custom or copied skin locally", Category.VISUALS, -1);
      o = this;
      this.I.H(() -> this.d.P().isBlank());
      this.l.H(() -> !this.I.S("None") || !this.d.P().isBlank());
      this.I.h(var1 -> {
         this.U = 0L;
         if (this.H()) {
            this.r();
         }
      });
      this.d.h(var1 -> {
         this.n.incrementAndGet();
         this.P = (boolean)1;
         this.U = 0L;
         this.z = var1.strip();
         this.e = System.nanoTime();
      });
   }

   public static SkinChangerModule X() {
      return o;
   }

   private Path H() {
      return mc.gameDirectory.toPath().resolve("config/larp-client/skins");
   }

   @Override
   public String getStatus() {
      return this.N;
   }

   public void A() {
      Path var1 = this.H();
      W.execute(() -> {
         try {
            Files.createDirectories(var1);
            mc.execute(() -> Blaze3D.openPath(var1));
         } catch (Exception var3) {
            this.N = "Could not open skins folder";
            mt.P("SkinChanger", "Folder stage failed", var3);
         }
      });
   }

   public void T() {
      Path var1 = this.H();
      W.execute(
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
               mc.execute(() -> this.I.Y(var9));
            } catch (Exception var8) {
               this.N = "Could not read skins folder";
               mt.P("SkinChanger", "Library stage failed", var8);
            }
         }
      );
   }

   @Override
   public void onEnable() {
      this.K = "";
      this.U = 0L;
      this.T();
      this.r();
   }

   @Override
   public void onDisable() {
      this.n.incrementAndGet();
      this.z();
      this.K = this.j = "";
      this.N = "Original skin restored";
      mt.K("SkinChanger", "Disabled; local texture released");
   }

   @Override
   public void onTick() {
      String var1 = this.d.P().strip();
      long var2 = System.nanoTime();
      if (!var1.equals(this.z)) {
         this.z = var1;
         this.e = var2;
      }

      boolean var5 = false;
      byte var4 = this.i;
      int var10000 = Integer.signum(var4);
      if ((var10000 | -var10000) >>> 31 == 0) {
         boolean var7 = false;
         byte var6 = this.P;
         var10000 = Integer.signum(var6);
         if ((var10000 | -var10000) >>> 31 == 0) {
            int var13 = var1.equals(this.K);
            boolean var9 = false;
            byte var8 = (byte)var13;
            var13 = Integer.signum(var8);
            if (((var13 | -var13) >>> 31 ^ 1) == 0) {
               int var15 = this.I.P().equals(this.j);
               boolean var11 = false;
               byte var10 = (byte)var15;
               var15 = Integer.signum(var10);
               if ((var15 | -var15) >>> 31 != 0) {
                  return;
               }
            }
         }

         if (var2 - this.e >= 600000000L && var2 >= this.U) {
            this.r();
         }
      }
   }

   public void r() {
      if (this.isEnabled()) {
         if (this.H()) {
            if (this.i) {
               this.n.incrementAndGet();
               this.P = (boolean)1;
            } else {
               this.P = (boolean)0;
               String var1 = this.d.P().strip();
               String var2 = this.I.P();
               if (!var1.isEmpty() && !f(var1)) {
                  this.N = "Enter a Minecraft username (1 to 16 characters)";
               } else if (!var1.isEmpty() || !var2.equals("None") && !var2.equals("Default")) {
                  long var3 = this.n.incrementAndGet();
                  this.i = (boolean)1;
                  this.N = var1.isEmpty() ? "Loading " + var2 : "Looking up " + var1 + "...";
                  Path var5 = this.H();
                  W.execute(
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
                              JsonObject var9 = ts.T("https://api.mojang.com/users/profiles/minecraft/" + var1, 8192);
                              String var10 = var9.get("id").getAsString();
                              if (!var10.matches("[a-fA-F0-9]{32}")) {
                                 throw new IllegalArgumentException("Invalid player profile");
                              }

                              JsonObject var11 = ts.T("https://sessionserver.mojang.com/session/minecraft/profile/" + var10, 32768);
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
                              var7 = ts.A(var29, 131072);
                           }

                           A(var7);
                           var6 = NativeImage.read(new ByteArrayInputStream(var7));
                           var6 = q6b37d34510312db9.larpProcessSkin(var6, "Skin Changer");
                           NativeImage var26 = var6;
                           var6 = null;
                           byte var28 = var8;
                           mc.execute(() -> {
                              if (var3 == this.n.get() && this.H()) {
                                 try {
                                    Identifier var7x = el.r("skin-changer-" + var3, var26);
                                    this.z();
                                    this.H = var7x;
                                    this.w = var28;
                                    this.K = var1;
                                    this.j = var2;
                                    this.N = var1.isEmpty() ? var2 : "Copied " + var1;
                                    mt.K("SkinChanger", "Skin applied: {}, model={}", this.N, this.l.P());
                                 } catch (RuntimeException var8x) {
                                    var26.close();
                                    this.N = "Texture registration failed";
                                    mt.P("SkinChanger", "GPU registration failed; previous skin retained", var8x);
                                 }
                              } else {
                                 var26.close();
                              }
                           });
                        } catch (Exception var23) {
                           mc.execute(() -> {
                              if (var3 == this.n.get() && this.H()) {
                                 this.N = "Skin failed: " + var23.getMessage();
                                 this.U = System.nanoTime() + 30000000000L;
                                 mt.P("SkinChanger", "Resolve/decode failed; original or previous skin retained", var23);
                              }
                           });
                        } finally {
                           if (var6 != null) {
                              var6.close();
                           }

                           mc.execute(() -> this.i = (boolean)0);
                        }
                     }
                  );
               } else {
                  this.n.incrementAndGet();
                  this.z();
                  this.K = var1;
                  this.j = var2;
                  this.N = var2.equals("Default") ? "Default Minecraft skin" : "Original skin";
               }
            }
         }
      }
   }

   public static boolean L(String var0) {
      return var0 != null
         && var0.length() <= 128
         && var0.length() > 4
         && !var0.contains("/")
         && !var0.contains("\\")
         && var0.codePoints().noneMatch(Character::isISOControl)
         && var0.toLowerCase(Locale.ROOT).endsWith(".png");
   }

   public static boolean f(String var0) {
      return var0 != null && var0.matches("[A-Za-z0-9_]{1,16}");
   }

   public static void A(byte[] var0) {
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

   private void z() {
      el.b(this.H);
      this.H = null;
   }

   private boolean H() {
      return this.isRuntimeActive() || this.R && this.isOperational();
   }

   public PlayerSkin F(PlayerSkin var1) {
      if (var1 == null || this.isRuntimeActive()) {
         return this.b(var1);
      }

      if (this.isOperational()) {
         if (!this.R) {
            this.R = (boolean)1;
            this.K = this.j = "";
            this.U = 0L;
            mt.U("SkinChanger", "Menu preview loading the saved selection without a world");
         }

         this.onTick();
         return this.b(var1);
      } else {
         if (this.R || this.H != null) {
            this.R = (boolean)0;
            this.n.incrementAndGet();
            this.z();
            this.K = this.j = "";
            mt.U("SkinChanger", "Menu preview ended; local texture released");
         }

         return var1;
      }
   }

   public PlayerSkin L(UUID var1, PlayerSkin var2) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() && var2 != null && mc.player != null && var1.equals(mc.player.getUUID()) ? this.b(var2) : var2;
      } else {
         return var2;
      }
   }

   private PlayerSkin b(PlayerSkin var1) {
      if (var1 == null) {
         return null;
      }

      if (this.I.S("None") && this.d.P().isBlank() && this.H == null) {
         return var1;
      }

      Object var2 = this.H == null ? (this.I.S("Default") ? DefaultPlayerSkin.getDefaultSkin().body() : var1.body()) : new ResourceTexture(this.H, this.H);
      PlayerModelType var3 = this.H == null
         ? (this.I.S("Default") ? DefaultPlayerSkin.getDefaultSkin().model() : var1.model())
         : (this.w ? PlayerModelType.SLIM : PlayerModelType.WIDE);
      PlayerModelType var4 = this.l.S("Auto") ? var3 : (this.l.S("Slim") ? PlayerModelType.SLIM : PlayerModelType.WIDE);
      return new PlayerSkin((Texture)var2, var1.cape(), var1.elytra(), var4, var1.secure());
   }
}
