package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._q;
import com.larpclient.de;
import com.larpclient.fq;
import com.larpclient.h5;
import com.larpclient.i3;
import com.larpclient.s0;
import com.larpclient.s1;
import com.larpclient.vv;
import com.mojang.blaze3d.Blaze3D;
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
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public final class MenuBackgroundModule extends Module {
   public static final String e = "config/larp-client/backgrounds";
   public static final String d = "None";
   private static final long D = 67108864L;
   private static final long j = 40000000L;
   private static final long L = 30000000000L;
   private static final int I = 8192;
   private static final int p = 256;
   private static MenuBackgroundModule P;
   private static final ExecutorService v = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-MenuWallpaper");
      var1.setDaemon(true);
      return var1;
   });
   private final o Z = this.addSetting(new o("Image", "Choose a PNG or JPG from the backgrounds folder", "None", "None") {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(var1) || MenuBackgroundModule.t(var1);
      }

      @Override
      public void setValue(String var1) {
         if (!this.j().contains(var1) && MenuBackgroundModule.t(var1)) {
            ArrayList var2 = new ArrayList<>(this.j());
            var2.add(var1);
            this.y(var2);
         }

         super.setValue(var1);
      }
   });
   private final o l = this.addSetting(
      new o("Fit", "Fill covers the screen and crops the edges, Fit shows the whole image, Stretch ignores its shape", "Fill", "Fill", "Fit", "Stretch")
   );
   private final b t = this.addSetting(new b("Dim", "Darkens the image so buttons and text stay readable, in percent", 25.0, 0.0, 80.0, 1.0));
   private final o X = this.addSetting(
      new o("Show On", "Main menu only, or every out-of-world client menu such as world and server lists", "Main menu", "Main menu", "All menus")
   );
   private final m y = this.addSetting(new m("Side Waves", "Keep the menu's monochrome side waves over the image", false));
   private final AtomicLong F = new AtomicLong();
   private final fq J = new fq(0.0);
   private Identifier Y;
   private int G;
   private int g;
   private String H = "";
   private String m = "";
   private long a;
   private long h;
   private boolean V;
   private boolean S;
   private volatile String c = "Add a PNG or JPG to the backgrounds folder";

   public MenuBackgroundModule() {
      super("Menu Background", "Use your own PNG or JPG as the main menu background", Category.VISUALS, -1);
      P = this;
      this.Z.w(var1 -> {
         this.a = 0L;
         this.S = (boolean)0;
         this.H = "";
      });
   }

   public static MenuBackgroundModule o() {
      return P;
   }

   @Override
   public String getStatus() {
      return this.c;
   }

   private static Path z() {
      return getMc().gameDirectory.toPath().resolve("config/larp-client/backgrounds");
   }

   public static boolean E(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      return P != null && P.isEnabled() ? P != null && P.c(var0, var1, var2, var3) : false;
   }

   public static boolean U() {
      return P != null && P.y.getValue();
   }

   private boolean c(GuiGraphicsExtractor var1, int var2, int var3, double var4) {
      if (!this.isOperational()) {
         if (this.Y != null || !this.H.isEmpty()) {
            this.S("Module off or not authorized");
         }

         return false;
      } else {
         if (!this.V) {
            this.V = (boolean)1;
            this.x();
         }

         String var6 = this.Z.getValue();
         if (var6.equals("None")) {
            if (this.Y != null || !this.H.isEmpty()) {
               this.S("No image selected");
            }

            this.c = "Choose an image";
            return false;
         } else {
            if ((this.S || !var6.equals(this.H)) && System.nanoTime() >= this.a) {
               this.g(var6);
            }

            if (this.X.w("Main menu") && !(getMc().gui.screen() instanceof de)) {
               return false;
            }

            if (this.Y != null && var2 > 0 && var3 > 0) {
               i3 var7 = LarpClient.N().h().k();
               double var8 = (var7.w ? this.J.n(1.0, 260) : 1.0) * Math.clamp(var4, 0.0, 1.0);
               if (var8 <= 0.001) {
                  return true;
               }

               int var10 = vv.N(-1, var8);

               try {
                  switch ((String)this.l.getValue()) {
                     case "Stretch":
                        var1.blit(RenderPipelines.GUI_TEXTURED, this.Y, 0, 0, 0.0F, 0.0F, var2, var3, this.G, this.g, this.G, this.g, var10);
                        break;
                     case "Fit":
                        double var19 = Math.min((double)var2 / this.G, (double)var3 / this.g);
                        int var15 = Math.max(1, (int)Math.round(this.G * var19));
                        int var16 = Math.max(1, (int)Math.round(this.g * var19));
                        var1.blit(
                           RenderPipelines.GUI_TEXTURED,
                           this.Y,
                           (var2 - var15) / 2,
                           (var3 - var16) / 2,
                           0.0F,
                           0.0F,
                           var15,
                           var16,
                           this.G,
                           this.g,
                           this.G,
                           this.g,
                           var10
                        );
                        break;
                     default:
                        s1 var13 = s1.cover(var2, var3, this.G, this.g, 1.0, 0.0, 0.0);
                        var1.blit(
                           RenderPipelines.GUI_TEXTURED, this.Y, 0, 0, var13.u(), var13.v(), var2, var3, var13.width(), var13.height(), this.G, this.g, var10
                        );
                  }
               } catch (RuntimeException var17) {
                  _q.o("MenuWallpaper", "Draw failed; standard backdrop restored until a new selection", var17);
                  this.S("Draw failed");
                  this.H = var6;
                  this.S = (boolean)1;
                  this.a = Long.MAX_VALUE;
                  this.c = "Could not draw " + var6;
                  return false;
               }

               double var18 = this.t.getValue() / 100.0 * var8;
               if (var18 > 0.001) {
                  h5.N(var1, 0, 0, var2, var3, vv.N(-16777216, var18));
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   public void s() {
      Path var1 = z();
      v.execute(() -> {
         try {
            Files.createDirectories(var1);
            getMc().execute(() -> Blaze3D.openPath(var1));
         } catch (Exception var3) {
            this.c = "Could not open the backgrounds folder";
            _q.o("MenuWallpaper", "Folder stage failed", var3);
         }
      });
   }

   public void J() {
      if (this.h == 0L || System.nanoTime() - this.h >= 1000000000L) {
         this.x();
      }
   }

   public void x() {
      this.h = System.nanoTime();
      Path var1 = z();
      v.execute(
         () -> {
            try {
               Files.createDirectories(var1);

               List var2;
               try (Stream var3 = Files.list(var1)) {
                  var2 = var3.filter(var0 -> Files.isRegularFile(var0))
                     .map(var0 -> var0.getFileName().toString())
                     .filter(MenuBackgroundModule::t)
                     .sorted(String.CASE_INSENSITIVE_ORDER)
                     .limit(256L)
                     .toList();
               }

               ArrayList var9 = new ArrayList();
               var9.add("None");
               var9.addAll(var2);
               getMc().execute(() -> {
                  this.Z.y(var9);
                  if (this.H.equals(this.Z.getValue()) && !this.H.equals("None")) {
                     this.H = "";
                     this.S = (boolean)0;
                     this.a = 0L;
                  }
               });
               _q.z("MenuWallpaper", "Backgrounds folder lists {} image(s)", var2.size());
            } catch (Exception var8) {
               this.c = "Could not read the backgrounds folder";
               _q.o("MenuWallpaper", "Library stage failed", var8);
            }
         }
      );
   }

   private void g(String var1) {
      this.H = var1;
      this.S = (boolean)0;
      long var2 = this.F.incrementAndGet();
      this.c = "Loading " + var1;
      Path var4 = z();
      v.execute(() -> {
         List var5 = List.of();

         try {
            Path var6 = var4.resolve(var1).normalize();
            if (!t(var1) || !var4.normalize().equals(var6.getParent())) {
               throw new IOException("Choose a PNG or JPG from the backgrounds folder");
            }

            byte[] var7;
            try (InputStream var8 = Files.newInputStream(var6)) {
               var7 = var8.readNBytes(67108865);
            }

            long var16 = System.nanoTime();
            var5 = s0.V(m(var7));
            List var10 = var5;
            long var11 = (System.nanoTime() - var16) / 1000000L;
            getMc().execute(() -> this.i(var2, var1, var10, var11));
         } catch (Exception | OutOfMemoryError var15) {
            s0.y(new ArrayList<>(var5));
            getMc().execute(() -> {
               if (var2 == this.F.get()) {
                  this.S = (boolean)1;
                  this.a = System.nanoTime() + 30000000000L;
                  this.c = "Could not load " + var1 + ": " + var15.getMessage();
                  _q.R("MenuWallpaper", "Load failed for {}; standard backdrop kept, retry in 30 s or on refresh: {}", var1, var15.toString());
               }
            });
         }
      });
   }

   private void i(long var1, String var3, List<NativeImage> var4, long var5) {
      try {
         if (var1 == this.F.get() && this.isOperational() && var3.equals(this.Z.getValue())) {
            NativeImage var7 = (NativeImage)var4.getFirst();
            Identifier var8 = Identifier.fromNamespaceAndPath("larp-client", "menu-wallpaper/" + var1);
            s0 var9 = new s0("Larp menu background " + var3, var4);
            getMc().getTextureManager().register(var8, var9);
            this.S();
            this.Y = var8;
            this.G = var7.getWidth();
            this.g = var7.getHeight();
            if (!var3.equals(this.m)) {
               this.J.p(0.0);
            }

            this.m = var3;
            this.c = var3 + " (" + this.G + "x" + this.g + ")";
            _q.z("MenuWallpaper", "Showing {} at {}x{} with {} mip levels; prepared in {} ms", var3, this.G, this.g, var4.size(), var5);
            return;
         }
      } catch (RuntimeException var13) {
         this.S = (boolean)1;
         this.a = System.nanoTime() + 30000000000L;
         this.c = "Could not upload " + var3;
         _q.o("MenuWallpaper", "GPU upload failed; previous background kept", var13);
         return;
      } finally {
         s0.y(new ArrayList<>(var4));
      }
   }

   private void S(String var1) {
      this.F.incrementAndGet();
      int var2 = this.Y != null ? 1 : 0;
      this.S();
      this.H = this.m = "";
      this.S = (boolean)0;
      this.a = 0L;
      if (var2 != 0) {
         _q.z("MenuWallpaper", "Background released: {}", var1);
      }
   }

   private void S() {
      if (this.Y != null) {
         getMc().getTextureManager().release(this.Y);
      }

      this.Y = null;
      this.G = this.g = 0;
   }

   @Override
   public void onDisable() {
      this.S("Module disabled");
      this.c = "Standard menu background";
   }

   public static boolean t(String var0) {
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

   public static MenuBackgroundModule.q l(byte[] var0) throws IOException {
      if (var0.length >= 8 && ByteBuffer.wrap(var0).getLong() == -8552249625308161526L) {
         return MenuBackgroundModule.q.m;
      } else if (var0.length >= 3 && (var0[0] & 255) == 255 && (var0[1] & 255) == 216 && (var0[2] & 255) == 255) {
         return MenuBackgroundModule.q.U;
      } else {
         throw new IOException("Not a PNG or JPEG image");
      }
   }

   public static void H(long var0, long var2, long var4) throws IOException {
      if (var0 > 67108864L) {
         throw new IOException("Image must be at most 64 MiB");
      } else if (var2 < 1L || var4 < 1L || var2 > 8192L || var4 > 8192L || var2 * var4 > 40000000L) {
         throw new IOException("Image must be at most 8192 pixels on a side and 40 megapixels");
      }
   }

   private static NativeImage m(byte[] var0) throws IOException {
      if (var0.length > 67108864L) {
         throw new IOException("Image must be at most 64 MiB");
      }

      MenuBackgroundModule.q var1 = l(var0);
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

               H(var0.length, var4.get(0), var5.get(0));
               if (var1 == MenuBackgroundModule.q.m) {
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

   public enum q {
      m,
      U;
   }
}
