package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.bb;
import com.larpclient.hb;
import com.larpclient.jp;
import com.larpclient.kv;
import com.larpclient.mh;
import com.larpclient.uh;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.NativeImage.Format;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public final class MenuBackgroundModule extends Module {
   public static final String p = "config/larp-client/backgrounds";
   public static final String o = "None";
   private static final long f = 67108864L;
   private static final long R = 40000000L;
   private static final long O = 30000000000L;
   private static final int Z = 8192;
   private static final int J = 256;
   private static MenuBackgroundModule s;
   private static final ExecutorService x = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-MenuWallpaper");
      var1.setDaemon(true);
      return var1;
   });
   private final l d = this.addSetting(new l("Image", "Choose a PNG or JPG from the backgrounds folder", "None", "None") {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(var1) || MenuBackgroundModule.y(var1);
      }

      @Override
      public void setValue(String var1) {
         if (!this.a().contains(var1) && MenuBackgroundModule.y(var1)) {
            ArrayList var2 = new ArrayList<>(this.a());
            var2.add(var1);
            this.G(var2);
         }

         super.setValue(var1);
      }
   });
   private final l F = this.addSetting(
      new l("Fit", "Fill covers the screen and crops the edges, Fit shows the whole image, Stretch ignores its shape", "Fill", "Fill", "Fit", "Stretch")
   );
   private final e v = this.addSetting(new e("Dim", "Darkens the image so buttons and text stay readable, in percent", 25.0, 0.0, 80.0, 1.0));
   private final l P = this.addSetting(
      new l("Show On", "Main menu only, or every out-of-world client menu such as world and server lists", "Main menu", "Main menu", "All menus")
   );
   private final s M = this.addSetting(new s("Side Waves", "Keep the menu's monochrome side waves over the image", false));
   private final AtomicLong N = new AtomicLong();
   private final jp t = new jp(0.0);
   private Identifier c;
   private int C;
   private int a;
   private String Q = "";
   private String L = "";
   private long b;
   private long K;
   private boolean D;
   private boolean V;
   private volatile String Y = "Add a PNG or JPG to the backgrounds folder";

   public MenuBackgroundModule() {
      super("Menu Background", "Use your own PNG or JPG as the main menu background", Category.VISUALS, -1);
      s = this;
      this.d.o(var1 -> {
         this.b = 0L;
         this.V = (boolean)0;
         this.Q = "";
      });
   }

   public static MenuBackgroundModule d() {
      return s;
   }

   @Override
   public String getStatus() {
      return this.Y;
   }

   private static Path A() {
      return getMc().gameDirectory.toPath().resolve("config/larp-client/backgrounds");
   }

   public static boolean J(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      return s != null && s.isEnabled() ? s != null && s.a(var0, var1, var2, var3) : false;
   }

   public static boolean M() {
      return s != null && s.M.getValue();
   }

   private boolean a(GuiGraphicsExtractor var1, int var2, int var3, double var4) {
      if (!this.isOperational()) {
         if (this.c != null || !this.Q.isEmpty()) {
            this.t("Module off or not authorized");
         }

         return false;
      } else {
         if (!this.D) {
            this.D = (boolean)1;
            this.Z();
         }

         String var6 = this.d.getValue();
         if (var6.equals("None")) {
            if (this.c != null || !this.Q.isEmpty()) {
               this.t("No image selected");
            }

            this.Y = "Choose an image";
            return false;
         } else {
            if ((this.V || !var6.equals(this.Q)) && System.nanoTime() >= this.b) {
               this.f(var6);
            }

            if (this.P.O("Main menu") && !(getMc().gui.screen() instanceof mh)) {
               return false;
            }

            if (this.c != null && var2 > 0 && var3 > 0) {
               hb var7 = LarpClient.R().o().g();
               double var8 = (var7.L ? this.t.k(1.0, 260) : 1.0) * Math.clamp(var4, 0.0, 1.0);
               if (var8 <= 0.001) {
                  return true;
               }

               int var10 = uh.H(-1, var8);

               try {
                  switch ((String)this.F.getValue()) {
                     case "Stretch":
                        var1.blit(RenderPipelines.GUI_TEXTURED, this.c, 0, 0, 0.0F, 0.0F, var2, var3, this.C, this.a, this.C, this.a, var10);
                        break;
                     case "Fit":
                        double var19 = Math.min((double)var2 / this.C, (double)var3 / this.a);
                        int var15 = Math.max(1, (int)Math.round(this.C * var19));
                        int var16 = Math.max(1, (int)Math.round(this.a * var19));
                        var1.blit(
                           RenderPipelines.GUI_TEXTURED,
                           this.c,
                           (var2 - var15) / 2,
                           (var3 - var16) / 2,
                           0.0F,
                           0.0F,
                           var15,
                           var16,
                           this.C,
                           this.a,
                           this.C,
                           this.a,
                           var10
                        );
                        break;
                     default:
                        bb var13 = bb.cover(var2, var3, this.C, this.a, 1.0, 0.0, 0.0);
                        var1.blit(
                           RenderPipelines.GUI_TEXTURED, this.c, 0, 0, var13.u(), var13.v(), var2, var3, var13.width(), var13.height(), this.C, this.a, var10
                        );
                  }
               } catch (RuntimeException var17) {
                  com.larpclient.h.m("MenuWallpaper", "Draw failed; standard backdrop restored until a new selection", var17);
                  this.t("Draw failed");
                  this.Q = var6;
                  this.V = (boolean)1;
                  this.b = Long.MAX_VALUE;
                  this.Y = "Could not draw " + var6;
                  return false;
               }

               double var18 = this.v.getValue() / 100.0 * var8;
               if (var18 > 0.001) {
                  kv.d(var1, 0, 0, var2, var3, uh.H(-16777216, var18));
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   public void A() {
      Path var1 = A();
      x.execute(() -> {
         try {
            Files.createDirectories(var1);
            getMc().execute(() -> Util.getPlatform().openPath(var1));
         } catch (Exception var3) {
            this.Y = "Could not open the backgrounds folder";
            com.larpclient.h.m("MenuWallpaper", "Folder stage failed", var3);
         }
      });
   }

   public void y() {
      if (this.K == 0L || System.nanoTime() - this.K >= 1000000000L) {
         this.Z();
      }
   }

   public void Z() {
      this.K = System.nanoTime();
      Path var1 = A();
      x.execute(
         () -> {
            try {
               Files.createDirectories(var1);

               List var2;
               try (Stream var3 = Files.list(var1)) {
                  var2 = var3.filter(var0 -> Files.isRegularFile(var0))
                     .map(var0 -> var0.getFileName().toString())
                     .filter(MenuBackgroundModule::y)
                     .sorted(String.CASE_INSENSITIVE_ORDER)
                     .limit(256L)
                     .toList();
               }

               ArrayList var9 = new ArrayList();
               var9.add("None");
               var9.addAll(var2);
               getMc().execute(() -> {
                  this.d.G(var9);
                  if (this.Q.equals(this.d.getValue()) && !this.Q.equals("None")) {
                     this.Q = "";
                     this.V = (boolean)0;
                     this.b = 0L;
                  }
               });
               com.larpclient.h.A("MenuWallpaper", "Backgrounds folder lists {} image(s)", var2.size());
            } catch (Exception var8) {
               this.Y = "Could not read the backgrounds folder";
               com.larpclient.h.m("MenuWallpaper", "Library stage failed", var8);
            }
         }
      );
   }

   private void f(String var1) {
      this.Q = var1;
      this.V = (boolean)0;
      long var2 = this.N.incrementAndGet();
      this.Y = "Loading " + var1;
      Path var4 = A();
      x.execute(() -> {
         List var5 = List.of();

         try {
            Path var6 = var4.resolve(var1).normalize();
            if (!y(var1) || !var4.normalize().equals(var6.getParent())) {
               throw new IOException("Choose a PNG or JPG from the backgrounds folder");
            }

            byte[] var7;
            try (InputStream var8 = Files.newInputStream(var6)) {
               var7 = var8.readNBytes(67108865);
            }

            long var16 = System.nanoTime();
            var5 = com.larpclient.pl.v(l(var7));
            List var10 = var5;
            long var11 = (System.nanoTime() - var16) / 1000000L;
            getMc().execute(() -> this.e(var2, var1, var10, var11));
         } catch (Exception | OutOfMemoryError var15) {
            com.larpclient.pl.S(new ArrayList<>(var5));
            getMc().execute(() -> {
               if (var2 == this.N.get()) {
                  this.V = (boolean)1;
                  this.b = System.nanoTime() + 30000000000L;
                  this.Y = "Could not load " + var1 + ": " + var15.getMessage();
                  com.larpclient.h.g("MenuWallpaper", "Load failed for {}; standard backdrop kept, retry in 30 s or on refresh: {}", var1, var15.toString());
               }
            });
         }
      });
   }

   private void e(long var1, String var3, List<NativeImage> var4, long var5) {
      try {
         if (var1 == this.N.get() && this.isOperational() && var3.equals(this.d.getValue())) {
            NativeImage var7 = (NativeImage)var4.getFirst();
            Identifier var8 = Identifier.fromNamespaceAndPath("larp-client", "menu-wallpaper/" + var1);
            com.larpclient.pl var9 = new com.larpclient.pl("Larp menu background " + var3, var4);
            getMc().getTextureManager().register(var8, var9);
            this.z();
            this.c = var8;
            this.C = var7.getWidth();
            this.a = var7.getHeight();
            if (!var3.equals(this.L)) {
               this.t.F(0.0);
            }

            this.L = var3;
            this.Y = var3 + " (" + this.C + "x" + this.a + ")";
            com.larpclient.h.A("MenuWallpaper", "Showing {} at {}x{} with {} mip levels; prepared in {} ms", var3, this.C, this.a, var4.size(), var5);
            return;
         }
      } catch (RuntimeException var13) {
         this.V = (boolean)1;
         this.b = System.nanoTime() + 30000000000L;
         this.Y = "Could not upload " + var3;
         com.larpclient.h.m("MenuWallpaper", "GPU upload failed; previous background kept", var13);
         return;
      } finally {
         com.larpclient.pl.S(new ArrayList<>(var4));
      }
   }

   private void t(String var1) {
      this.N.incrementAndGet();
      int var2 = this.c != null ? 1 : 0;
      this.z();
      this.Q = this.L = "";
      this.V = (boolean)0;
      this.b = 0L;
      if (var2 != 0) {
         com.larpclient.h.A("MenuWallpaper", "Background released: {}", var1);
      }
   }

   private void z() {
      if (this.c != null) {
         getMc().getTextureManager().release(this.c);
      }

      this.c = null;
      this.C = this.a = 0;
   }

   @Override
   public void onDisable() {
      this.t("Module disabled");
      this.Y = "Standard menu background";
   }

   public static boolean y(String var0) {
      if (var0 != null
         && var0.length() > 4
         && var0.length() <= 128
         && !var0.contains("/")
         && !var0.contains("\\")
         && !var0.startsWith(".")
         && !var0.codePoints().anyMatch(Character::isISOControl)) {
         String var1 = var0.toLowerCase(Locale.ROOT);
         return var1.endsWith(".png") || var1.endsWith(".jpg") || var1.endsWith(".jpeg");
      } else {
         return false;
      }
   }

   public static MenuBackgroundModule.k w(byte[] var0) throws IOException {
      if (var0.length >= 8 && ByteBuffer.wrap(var0).getLong() == -8552249625308161526L) {
         return MenuBackgroundModule.k.T;
      } else if (var0.length >= 3 && (var0[0] & 255) == 255 && (var0[1] & 255) == 216 && (var0[2] & 255) == 255) {
         return MenuBackgroundModule.k.K;
      } else {
         throw new IOException("Not a PNG or JPEG image");
      }
   }

   public static void R(long var0, long var2, long var4) throws IOException {
      if (var0 > 67108864L) {
         throw new IOException("Image must be at most 64 MiB");
      } else if (var2 < 1L || var4 < 1L || var2 > 8192L || var4 > 8192L || var2 * var4 > 40000000L) {
         throw new IOException("Image must be at most 8192 pixels on a side and 40 megapixels");
      }
   }

   private static NativeImage l(byte[] var0) throws IOException {
      if (var0.length > 67108864L) {
         throw new IOException("Image must be at most 64 MiB");
      }

      MenuBackgroundModule.k var1 = w(var0);
      ByteBuffer var2 = MemoryUtil.memAlloc(var0.length);

      try {
         MemoryStack var3 = MemoryStack.stackPush();

         NativeImage var26;
         label171: {
            NativeImage var11;
            try {
               var2.put(var0).flip();
               IntBuffer var4 = var3.mallocInt(1);
               IntBuffer var5 = var3.mallocInt(1);
               IntBuffer var6 = var3.mallocInt(1);
               if (!STBImage.stbi_info_from_memory(var2, var4, var5, var6)) {
                  throw new IOException("Unreadable image: " + STBImage.stbi_failure_reason());
               }

               R(var0.length, var4.get(0), var5.get(0));
               if (var1 == MenuBackgroundModule.k.T) {
                  var26 = NativeImage.read(new ByteArrayInputStream(var0));
                  break label171;
               }

               ByteBuffer var7 = STBImage.stbi_load_from_memory(var2, var4, var5, var6, 4);
               if (var7 == null) {
                  throw new IOException("Unreadable JPEG: " + STBImage.stbi_failure_reason());
               }

               try {
                  int var8 = var4.get(0);
                  int var9 = var5.get(0);
                  NativeImage var10 = new NativeImage(Format.RGBA, var8, var9, false);
                  MemoryUtil.memCopy(MemoryUtil.memAddress(var7), var10.getPointer(), (long)var8 * var9 * 4L);
                  var11 = var10;
               } finally {
                  STBImage.stbi_image_free(var7);
               }
            } catch (Throwable var24) {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (Throwable var22) {
                     var24.addSuppressed(var22);
                  }
               }

               throw var24;
            }

            if (var3 != null) {
               var3.close();
            }

            return var11;
         }

         if (var3 != null) {
            var3.close();
         }

         return var26;
      } finally {
         MemoryUtil.memFree(var2);
      }
   }

   public enum k {
      T,
      K;
   }
}
