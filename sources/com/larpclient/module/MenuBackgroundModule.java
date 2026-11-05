package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.fr;
import com.larpclient.fs;
import com.larpclient.oh;
import com.larpclient.pn;
import com.larpclient.pu;
import com.larpclient.r4;
import com.larpclient.rf;
import com.larpclient.u7;
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
   public static final String g = "config/larp-client/backgrounds";
   public static final String u = "None";
   private static final long F = 67108864L;
   private static final long a = 40000000L;
   private static final long i = 30000000000L;
   private static final int d = 8192;
   private static final int x = 256;
   private static MenuBackgroundModule z;
   private static final ExecutorService t = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-MenuWallpaper");
      var1.setDaemon(true);
      return var1;
   });
   private final lp m = this.addSetting(new lp("Image", "Choose a PNG or JPG from the backgrounds folder", "None", "None") {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(var1) || MenuBackgroundModule.J(var1);
      }

      @Override
      public void setValue(String var1) {
         if (!this.X().contains(var1) && MenuBackgroundModule.J(var1)) {
            ArrayList var2 = new ArrayList<>(this.X());
            var2.add(var1);
            this.u(var2);
         }

         super.setValue(var1);
      }
   });
   private final lp E = this.addSetting(
      new lp("Fit", "Fill covers the screen and crops the edges, Fit shows the whole image, Stretch ignores its shape", "Fill", "Fill", "Fit", "Stretch")
   );
   private final lc T = this.addSetting(new lc("Dim", "Darkens the image so buttons and text stay readable, in percent", 25.0, 0.0, 80.0, 1.0));
   private final lp W = this.addSetting(
      new lp("Show On", "Main menu only, or every out-of-world client menu such as world and server lists", "Main menu", "Main menu", "All menus")
   );
   private final ln c = this.addSetting(new ln("Side Waves", "Keep the menu's monochrome side waves over the image", false));
   private final AtomicLong y = new AtomicLong();
   private final u7 l = new u7(0.0);
   private Identifier N;
   private int J;
   private int V;
   private String I = "";
   private String X = "";
   private long n;
   private long q;
   private boolean k;
   private boolean A;
   private volatile String o = "Add a PNG or JPG to the backgrounds folder";

   public MenuBackgroundModule() {
      super("Menu Background", "Use your own PNG or JPG as the main menu background", Category.VISUALS, -1);
      z = this;
      this.m.W(var1 -> {
         this.n = 0L;
         this.A = (boolean)0;
         this.I = "";
      });
   }

   public static MenuBackgroundModule H() {
      return z;
   }

   @Override
   public String getStatus() {
      return this.o;
   }

   private static Path M() {
      return getMc().gameDirectory.toPath().resolve("config/larp-client/backgrounds");
   }

   public static boolean b(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      return z != null && z.isEnabled() ? z != null && z.z(var0, var1, var2, var3) : false;
   }

   public static boolean e() {
      return z != null && z.c.getValue();
   }

   private boolean z(GuiGraphicsExtractor var1, int var2, int var3, double var4) {
      if (!this.isOperational()) {
         if (this.N != null || !this.I.isEmpty()) {
            this.i("Module off or not authorized");
         }

         return false;
      } else {
         if (!this.k) {
            this.k = (boolean)1;
            this.Z();
         }

         String var6 = this.m.getValue();
         if (var6.equals("None")) {
            if (this.N != null || !this.I.isEmpty()) {
               this.i("No image selected");
            }

            this.o = "Choose an image";
            return false;
         } else {
            if ((this.A || !var6.equals(this.I)) && System.nanoTime() >= this.n) {
               this.T(var6);
            }

            if (this.W.i("Main menu") && !(getMc().gui.screen() instanceof rf)) {
               return false;
            }

            if (this.N != null && var2 > 0 && var3 > 0) {
               fs var7 = LarpClient.y().v().E();
               double var8 = (var7.o ? this.l.y(1.0, 260) : 1.0) * Math.clamp(var4, 0.0, 1.0);
               if (var8 <= 0.001) {
                  return true;
               }

               int var10 = oh.i(-1, var8);

               try {
                  switch ((String)this.E.getValue()) {
                     case "Stretch":
                        var1.blit(RenderPipelines.GUI_TEXTURED, this.N, 0, 0, 0.0F, 0.0F, var2, var3, this.J, this.V, this.J, this.V, var10);
                        break;
                     case "Fit":
                        double var19 = Math.min((double)var2 / this.J, (double)var3 / this.V);
                        int var15 = Math.max(1, (int)Math.round(this.J * var19));
                        int var16 = Math.max(1, (int)Math.round(this.V * var19));
                        var1.blit(
                           RenderPipelines.GUI_TEXTURED,
                           this.N,
                           (var2 - var15) / 2,
                           (var3 - var16) / 2,
                           0.0F,
                           0.0F,
                           var15,
                           var16,
                           this.J,
                           this.V,
                           this.J,
                           this.V,
                           var10
                        );
                        break;
                     default:
                        pu var13 = pu.cover(var2, var3, this.J, this.V, 1.0, 0.0, 0.0);
                        var1.blit(
                           RenderPipelines.GUI_TEXTURED, this.N, 0, 0, var13.u(), var13.v(), var2, var3, var13.width(), var13.height(), this.J, this.V, var10
                        );
                  }
               } catch (RuntimeException var17) {
                  r4.r("MenuWallpaper", "Draw failed; standard backdrop restored until a new selection", var17);
                  this.i("Draw failed");
                  this.I = var6;
                  this.A = (boolean)1;
                  this.n = Long.MAX_VALUE;
                  this.o = "Could not draw " + var6;
                  return false;
               }

               double var18 = this.T.getValue() / 100.0 * var8;
               if (var18 > 0.001) {
                  fr.k(var1, 0, 0, var2, var3, oh.i(-16777216, var18));
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   public void e() {
      Path var1 = M();
      t.execute(() -> {
         try {
            Files.createDirectories(var1);
            getMc().execute(() -> Blaze3D.openPath(var1));
         } catch (Exception var3) {
            this.o = "Could not open the backgrounds folder";
            r4.r("MenuWallpaper", "Folder stage failed", var3);
         }
      });
   }

   public void I() {
      if (this.q == 0L || System.nanoTime() - this.q >= 1000000000L) {
         this.Z();
      }
   }

   public void Z() {
      this.q = System.nanoTime();
      Path var1 = M();
      t.execute(
         () -> {
            try {
               Files.createDirectories(var1);

               List var2;
               try (Stream var3 = Files.list(var1)) {
                  var2 = var3.filter(var0 -> Files.isRegularFile(var0))
                     .map(var0 -> var0.getFileName().toString())
                     .filter(MenuBackgroundModule::J)
                     .sorted(String.CASE_INSENSITIVE_ORDER)
                     .limit(256L)
                     .toList();
               }

               ArrayList var9 = new ArrayList();
               var9.add("None");
               var9.addAll(var2);
               getMc().execute(() -> {
                  this.m.u(var9);
                  if (this.I.equals(this.m.getValue()) && !this.I.equals("None")) {
                     this.I = "";
                     this.A = (boolean)0;
                     this.n = 0L;
                  }
               });
               r4.V("MenuWallpaper", "Backgrounds folder lists {} image(s)", var2.size());
            } catch (Exception var8) {
               this.o = "Could not read the backgrounds folder";
               r4.r("MenuWallpaper", "Library stage failed", var8);
            }
         }
      );
   }

   private void T(String var1) {
      this.I = var1;
      this.A = (boolean)0;
      long var2 = this.y.incrementAndGet();
      this.o = "Loading " + var1;
      Path var4 = M();
      t.execute(() -> {
         List var5 = List.of();

         try {
            Path var6 = var4.resolve(var1).normalize();
            if (!J(var1) || !var4.normalize().equals(var6.getParent())) {
               throw new IOException("Choose a PNG or JPG from the backgrounds folder");
            }

            byte[] var7;
            try (InputStream var8 = Files.newInputStream(var6)) {
               var7 = var8.readNBytes(67108865);
            }

            long var16 = System.nanoTime();
            var5 = pn.U(W(var7));
            List var10 = var5;
            long var11 = (System.nanoTime() - var16) / 1000000L;
            getMc().execute(() -> this.g(var2, var1, var10, var11));
         } catch (Exception | OutOfMemoryError var15) {
            pn.e(new ArrayList<>(var5));
            getMc().execute(() -> {
               if (var2 == this.y.get()) {
                  this.A = (boolean)1;
                  this.n = System.nanoTime() + 30000000000L;
                  this.o = "Could not load " + var1 + ": " + var15.getMessage();
                  r4.m("MenuWallpaper", "Load failed for {}; standard backdrop kept, retry in 30 s or on refresh: {}", var1, var15.toString());
               }
            });
         }
      });
   }

   private void g(long var1, String var3, List<NativeImage> var4, long var5) {
      try {
         if (var1 == this.y.get() && this.isOperational() && var3.equals(this.m.getValue())) {
            NativeImage var7 = (NativeImage)var4.getFirst();
            Identifier var8 = Identifier.fromNamespaceAndPath("larp-client", "menu-wallpaper/" + var1);
            pn var9 = new pn("Larp menu background " + var3, var4);
            getMc().getTextureManager().register(var8, var9);
            this.E();
            this.N = var8;
            this.J = var7.getWidth();
            this.V = var7.getHeight();
            if (!var3.equals(this.X)) {
               this.l.c(0.0);
            }

            this.X = var3;
            this.o = var3 + " (" + this.J + "x" + this.V + ")";
            r4.V("MenuWallpaper", "Showing {} at {}x{} with {} mip levels; prepared in {} ms", var3, this.J, this.V, var4.size(), var5);
            return;
         }
      } catch (RuntimeException var13) {
         this.A = (boolean)1;
         this.n = System.nanoTime() + 30000000000L;
         this.o = "Could not upload " + var3;
         r4.r("MenuWallpaper", "GPU upload failed; previous background kept", var13);
         return;
      } finally {
         pn.e(new ArrayList<>(var4));
      }
   }

   private void i(String var1) {
      this.y.incrementAndGet();
      int var2 = this.N != null ? 1 : 0;
      this.E();
      this.I = this.X = "";
      this.A = (boolean)0;
      this.n = 0L;
      if (var2 != 0) {
         r4.V("MenuWallpaper", "Background released: {}", var1);
      }
   }

   private void E() {
      if (this.N != null) {
         getMc().getTextureManager().release(this.N);
      }

      this.N = null;
      this.J = this.V = 0;
   }

   @Override
   public void onDisable() {
      this.i("Module disabled");
      this.o = "Standard menu background";
   }

   public static boolean J(String var0) {
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

   public static MenuBackgroundModule.v G(byte[] var0) throws IOException {
      if (var0.length >= 8 && ByteBuffer.wrap(var0).getLong() == -8552249625308161526L) {
         return MenuBackgroundModule.v.a;
      } else if (var0.length >= 3 && (var0[0] & 255) == 255 && (var0[1] & 255) == 216 && (var0[2] & 255) == 255) {
         return MenuBackgroundModule.v.t;
      } else {
         throw new IOException("Not a PNG or JPEG image");
      }
   }

   public static void F(long var0, long var2, long var4) throws IOException {
      if (var0 > 67108864L) {
         throw new IOException("Image must be at most 64 MiB");
      } else if (var2 < 1L || var4 < 1L || var2 > 8192L || var4 > 8192L || var2 * var4 > 40000000L) {
         throw new IOException("Image must be at most 8192 pixels on a side and 40 megapixels");
      }
   }

   private static NativeImage W(byte[] var0) throws IOException {
      if (var0.length > 67108864L) {
         throw new IOException("Image must be at most 64 MiB");
      }

      MenuBackgroundModule.v var1 = G(var0);
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

               F(var0.length, var4.get(0), var5.get(0));
               if (var1 == MenuBackgroundModule.v.a) {
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

   public enum v {
      a,
      t;
   }
}
