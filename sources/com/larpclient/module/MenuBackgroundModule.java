package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._3;
import com.larpclient.c7;
import com.larpclient.ef;
import com.larpclient.ho;
import com.larpclient.mt;
import com.larpclient.o2;
import com.larpclient.uj;
import com.larpclient.xv;
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
   public static final String s = "config/larp-client/backgrounds";
   public static final String W = "None";
   private static final long S = 67108864L;
   private static final long d = 40000000L;
   private static final long Q = 30000000000L;
   private static final int a = 8192;
   private static final int T = 256;
   private static MenuBackgroundModule o;
   private static final ExecutorService x = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-MenuWallpaper");
      var1.setDaemon(true);
      return var1;
   });
   private final d6 G = this.addSetting(new d6("Image", "Choose a PNG or JPG from the backgrounds folder", "None", "None") {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(var1) || MenuBackgroundModule.n(var1);
      }

      @Override
      public void setValue(String var1) {
         if (!this.N().contains(var1) && MenuBackgroundModule.n(var1)) {
            ArrayList var2 = new ArrayList<>(this.N());
            var2.add(var1);
            this.Y(var2);
         }

         super.setValue(var1);
      }
   });
   private final d6 Y = this.addSetting(
      new d6("Fit", "Fill covers the screen and crops the edges, Fit shows the whole image, Stretch ignores its shape", "Fill", "Fill", "Fit", "Stretch")
   );
   private final d9 b = this.addSetting(new d9("Dim", "Darkens the image so buttons and text stay readable, in percent", 25.0, 0.0, 80.0, 1.0));
   private final d6 l = this.addSetting(
      new d6("Show On", "Main menu only, or every out-of-world client menu such as world and server lists", "Main menu", "Main menu", "All menus")
   );
   private final dh z = this.addSetting(new dh("Side Waves", "Keep the menu's monochrome side waves over the image", false));
   private final AtomicLong n = new AtomicLong();
   private final ef L = new ef(0.0);
   private Identifier D;
   private int F;
   private int P;
   private String k = "";
   private String j = "";
   private long v;
   private long e;
   private boolean A;
   private boolean t;
   private volatile String r = "Add a PNG or JPG to the backgrounds folder";

   public MenuBackgroundModule() {
      super("Menu Background", "Use your own PNG or JPG as the main menu background", Category.VISUALS, -1);
      o = this;
      this.G.h(var1 -> {
         this.v = 0L;
         this.t = (boolean)0;
         this.k = "";
      });
   }

   public static MenuBackgroundModule R() {
      return o;
   }

   @Override
   public String getStatus() {
      return this.r;
   }

   private static Path x() {
      return getMc().gameDirectory.toPath().resolve("config/larp-client/backgrounds");
   }

   public static boolean R(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      return o != null && o.isEnabled() ? o != null && o.f(var0, var1, var2, var3) : false;
   }

   public static boolean p() {
      return o != null && o.z.P();
   }

   private boolean f(GuiGraphicsExtractor var1, int var2, int var3, double var4) {
      if (!this.isOperational()) {
         if (this.D != null || !this.k.isEmpty()) {
            this.T("Module off or not authorized");
         }

         return false;
      } else {
         if (!this.A) {
            this.A = (boolean)1;
            this.N();
         }

         String var6 = this.G.P();
         if (var6.equals("None")) {
            if (this.D != null || !this.k.isEmpty()) {
               this.T("No image selected");
            }

            this.r = "Choose an image";
            return false;
         } else {
            if ((this.t || !var6.equals(this.k)) && System.nanoTime() >= this.v) {
               this.V(var6);
            }

            if (this.l.S("Main menu") && !(getMc().gui.screen() instanceof uj)) {
               return false;
            }

            if (this.D != null && var2 > 0 && var3 > 0) {
               xv var7 = LarpClient.F().f().t();
               double var8 = (var7.V ? this.L.m(1.0, 260) : 1.0) * Math.clamp(var4, 0.0, 1.0);
               if (var8 <= 0.001) {
                  return true;
               }

               int var10 = _3.A(-1, var8);

               try {
                  switch ((String)this.Y.P()) {
                     case "Stretch":
                        var1.blit(RenderPipelines.GUI_TEXTURED, this.D, 0, 0, 0.0F, 0.0F, var2, var3, this.F, this.P, this.F, this.P, var10);
                        break;
                     case "Fit":
                        double var19 = Math.min((double)var2 / this.F, (double)var3 / this.P);
                        int var15 = Math.max(1, (int)Math.round(this.F * var19));
                        int var16 = Math.max(1, (int)Math.round(this.P * var19));
                        var1.blit(
                           RenderPipelines.GUI_TEXTURED,
                           this.D,
                           (var2 - var15) / 2,
                           (var3 - var16) / 2,
                           0.0F,
                           0.0F,
                           var15,
                           var16,
                           this.F,
                           this.P,
                           this.F,
                           this.P,
                           var10
                        );
                        break;
                     default:
                        o2 var13 = o2.cover(var2, var3, this.F, this.P, 1.0, 0.0, 0.0);
                        var1.blit(
                           RenderPipelines.GUI_TEXTURED, this.D, 0, 0, var13.u(), var13.v(), var2, var3, var13.width(), var13.height(), this.F, this.P, var10
                        );
                  }
               } catch (RuntimeException var17) {
                  mt.P("MenuWallpaper", "Draw failed; standard backdrop restored until a new selection", var17);
                  this.T("Draw failed");
                  this.k = var6;
                  this.t = (boolean)1;
                  this.v = Long.MAX_VALUE;
                  this.r = "Could not draw " + var6;
                  return false;
               }

               double var18 = this.b.P() / 100.0 * var8;
               if (var18 > 0.001) {
                  c7.s(var1, 0, 0, var2, var3, _3.A(-16777216, var18));
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   public void A() {
      Path var1 = x();
      x.execute(() -> {
         try {
            Files.createDirectories(var1);
            getMc().execute(() -> Blaze3D.openPath(var1));
         } catch (Exception var3) {
            this.r = "Could not open the backgrounds folder";
            mt.P("MenuWallpaper", "Folder stage failed", var3);
         }
      });
   }

   public void f() {
      if (this.e == 0L || System.nanoTime() - this.e >= 1000000000L) {
         this.N();
      }
   }

   public void N() {
      this.e = System.nanoTime();
      Path var1 = x();
      x.execute(
         () -> {
            try {
               Files.createDirectories(var1);

               List var2;
               try (Stream var3 = Files.list(var1)) {
                  var2 = var3.filter(var0 -> Files.isRegularFile(var0))
                     .map(var0 -> var0.getFileName().toString())
                     .filter(MenuBackgroundModule::n)
                     .sorted(String.CASE_INSENSITIVE_ORDER)
                     .limit(256L)
                     .toList();
               }

               ArrayList var9 = new ArrayList();
               var9.add("None");
               var9.addAll(var2);
               getMc().execute(() -> {
                  this.G.Y(var9);
                  if (this.k.equals(this.G.P()) && !this.k.equals("None")) {
                     this.k = "";
                     this.t = (boolean)0;
                     this.v = 0L;
                  }
               });
               mt.K("MenuWallpaper", "Backgrounds folder lists {} image(s)", var2.size());
            } catch (Exception var8) {
               this.r = "Could not read the backgrounds folder";
               mt.P("MenuWallpaper", "Library stage failed", var8);
            }
         }
      );
   }

   private void V(String var1) {
      this.k = var1;
      this.t = (boolean)0;
      long var2 = this.n.incrementAndGet();
      this.r = "Loading " + var1;
      Path var4 = x();
      x.execute(() -> {
         List var5 = List.of();

         try {
            Path var6 = var4.resolve(var1).normalize();
            if (!n(var1) || !var4.normalize().equals(var6.getParent())) {
               throw new IOException("Choose a PNG or JPG from the backgrounds folder");
            }

            byte[] var7;
            try (InputStream var8 = Files.newInputStream(var6)) {
               var7 = var8.readNBytes(67108865);
            }

            long var16 = System.nanoTime();
            var5 = ho.j(E(var7));
            List var10 = var5;
            long var11 = (System.nanoTime() - var16) / 1000000L;
            getMc().execute(() -> this.M(var2, var1, var10, var11));
         } catch (Exception | OutOfMemoryError var15) {
            ho.G(new ArrayList<>(var5));
            getMc().execute(() -> {
               if (var2 == this.n.get()) {
                  this.t = (boolean)1;
                  this.v = System.nanoTime() + 30000000000L;
                  this.r = "Could not load " + var1 + ": " + var15.getMessage();
                  mt.T("MenuWallpaper", "Load failed for {}; standard backdrop kept, retry in 30 s or on refresh: {}", var1, var15.toString());
               }
            });
         }
      });
   }

   private void M(long var1, String var3, List<NativeImage> var4, long var5) {
      try {
         if (var1 == this.n.get() && this.isOperational() && var3.equals(this.G.P())) {
            NativeImage var7 = (NativeImage)var4.getFirst();
            Identifier var8 = Identifier.fromNamespaceAndPath("larp-client", "menu-wallpaper/" + var1);
            ho var9 = new ho("Larp menu background " + var3, var4);
            getMc().getTextureManager().register(var8, var9);
            this.i();
            this.D = var8;
            this.F = var7.getWidth();
            this.P = var7.getHeight();
            if (!var3.equals(this.j)) {
               this.L.Q(0.0);
            }

            this.j = var3;
            this.r = var3 + " (" + this.F + "x" + this.P + ")";
            mt.K("MenuWallpaper", "Showing {} at {}x{} with {} mip levels; prepared in {} ms", var3, this.F, this.P, var4.size(), var5);
            return;
         }
      } catch (RuntimeException var13) {
         this.t = (boolean)1;
         this.v = System.nanoTime() + 30000000000L;
         this.r = "Could not upload " + var3;
         mt.P("MenuWallpaper", "GPU upload failed; previous background kept", var13);
         return;
      } finally {
         ho.G(new ArrayList<>(var4));
      }
   }

   private void T(String var1) {
      this.n.incrementAndGet();
      int var2 = this.D != null ? 1 : 0;
      this.i();
      this.k = this.j = "";
      this.t = (boolean)0;
      this.v = 0L;
      if (var2 != 0) {
         mt.K("MenuWallpaper", "Background released: {}", var1);
      }
   }

   private void i() {
      if (this.D != null) {
         getMc().getTextureManager().release(this.D);
      }

      this.D = null;
      this.F = this.P = 0;
   }

   @Override
   public void onDisable() {
      this.T("Module disabled");
      this.r = "Standard menu background";
   }

   public static boolean n(String var0) {
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

   public static MenuBackgroundModule.u Q(byte[] var0) throws IOException {
      if (var0.length >= 8 && ByteBuffer.wrap(var0).getLong() == -8552249625308161526L) {
         return MenuBackgroundModule.u.v;
      } else if (var0.length >= 3 && (var0[0] & 255) == 255 && (var0[1] & 255) == 216 && (var0[2] & 255) == 255) {
         return MenuBackgroundModule.u.i;
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

   private static NativeImage E(byte[] var0) throws IOException {
      if (var0.length > 67108864L) {
         throw new IOException("Image must be at most 64 MiB");
      }

      MenuBackgroundModule.u var1 = Q(var0);
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
               if (var1 == MenuBackgroundModule.u.v) {
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

   public enum u {
      v,
      i;
   }
}
