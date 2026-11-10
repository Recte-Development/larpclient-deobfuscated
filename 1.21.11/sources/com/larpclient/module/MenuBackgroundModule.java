package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.ei;
import com.larpclient.gu;
import com.larpclient.hh;
import com.larpclient.it;
import com.larpclient.q8;
import com.larpclient.qb;
import com.larpclient.si;
import com.larpclient.y0;
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
import net.minecraft.class_1011;
import net.minecraft.class_10799;
import net.minecraft.class_156;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_1011.class_1012;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public final class MenuBackgroundModule extends Module {
   public static final String w = "config/larp-client/backgrounds";
   public static final String V = "None";
   private static final long b = 67108864L;
   private static final long G = 40000000L;
   private static final long A = 30000000000L;
   private static final int k = 8192;
   private static final int l = 256;
   private static MenuBackgroundModule a;
   private static final ExecutorService x = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-MenuWallpaper");
      var1.setDaemon(true);
      return var1;
   });
   private final li W = this.addSetting(new li("Image", "Choose a PNG or JPG from the backgrounds folder", "None", "None") {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(var1) || MenuBackgroundModule.k(var1);
      }

      @Override
      public void setValue(String var1) {
         if (!this.e().contains(var1) && MenuBackgroundModule.k(var1)) {
            ArrayList var2 = new ArrayList<>(this.e());
            var2.add(var1);
            this.k(var2);
         }

         super.setValue(var1);
      }
   });
   private final li Y = this.addSetting(
      new li("Fit", "Fill covers the screen and crops the edges, Fit shows the whole image, Stretch ignores its shape", "Fill", "Fill", "Fit", "Stretch")
   );
   private final lo m = this.addSetting(new lo("Dim", "Darkens the image so buttons and text stay readable, in percent", 25.0, 0.0, 80.0, 1.0));
   private final li Q = this.addSetting(
      new li("Show On", "Main menu only, or every out-of-world client menu such as world and server lists", "Main menu", "Main menu", "All menus")
   );
   private final l3 f = this.addSetting(new l3("Side Waves", "Keep the menu's monochrome side waves over the image", false));
   private final AtomicLong B = new AtomicLong();
   private final qb C = new qb(0.0);
   private class_2960 U;
   private int L;
   private int P;
   private String e = "";
   private String I = "";
   private long H;
   private long E;
   private boolean d;
   private boolean F;
   private volatile String y = "Add a PNG or JPG to the backgrounds folder";

   public MenuBackgroundModule() {
      super("Menu Background", "Use your own PNG or JPG as the main menu background", Category.VISUALS, -1);
      a = this;
      this.W.a(var1 -> {
         this.H = 0L;
         this.F = (boolean)0;
         this.e = "";
      });
   }

   public static MenuBackgroundModule o() {
      return a;
   }

   @Override
   public String getStatus() {
      return this.y;
   }

   private static Path W() {
      return getMc().field_1697.toPath().resolve("config/larp-client/backgrounds");
   }

   public static boolean i(class_332 var0, int var1, int var2, double var3) {
      return a != null && a.isEnabled() ? a != null && a.J(var0, var1, var2, var3) : false;
   }

   public static boolean Q() {
      return a != null && a.f.getValue();
   }

   private boolean J(class_332 var1, int var2, int var3, double var4) {
      if (!this.isOperational()) {
         if (this.U != null || !this.e.isEmpty()) {
            this.w("Module off or not authorized");
         }

         return false;
      } else {
         if (!this.d) {
            this.d = (boolean)1;
            this.A();
         }

         String var6 = this.W.getValue();
         if (var6.equals("None")) {
            if (this.U != null || !this.e.isEmpty()) {
               this.w("No image selected");
            }

            this.y = "Choose an image";
            return false;
         } else {
            if ((this.F || !var6.equals(this.e)) && System.nanoTime() >= this.H) {
               this.g(var6);
            }

            if (this.Q.G("Main menu") && !(getMc().field_1755 instanceof hh)) {
               return false;
            }

            if (this.U != null && var2 > 0 && var3 > 0) {
               y0 var7 = LarpClient.b().e().F();
               double var8 = (var7.k ? this.C.a(1.0, 260) : 1.0) * Math.clamp(var4, 0.0, 1.0);
               if (var8 <= 0.001) {
                  return true;
               }

               int var10 = ei.D(-1, var8);

               try {
                  switch ((String)this.Y.getValue()) {
                     case "Stretch":
                        var1.method_25293(class_10799.field_56883, this.U, 0, 0, 0.0F, 0.0F, var2, var3, this.L, this.P, this.L, this.P, var10);
                        break;
                     case "Fit":
                        double var19 = Math.min((double)var2 / this.L, (double)var3 / this.P);
                        int var15 = Math.max(1, (int)Math.round(this.L * var19));
                        int var16 = Math.max(1, (int)Math.round(this.P * var19));
                        var1.method_25293(
                           class_10799.field_56883,
                           this.U,
                           (var2 - var15) / 2,
                           (var3 - var16) / 2,
                           0.0F,
                           0.0F,
                           var15,
                           var16,
                           this.L,
                           this.P,
                           this.L,
                           this.P,
                           var10
                        );
                        break;
                     default:
                        it var13 = it.cover(var2, var3, this.L, this.P, 1.0, 0.0, 0.0);
                        var1.method_25293(
                           class_10799.field_56883, this.U, 0, 0, var13.u(), var13.v(), var2, var3, var13.width(), var13.height(), this.L, this.P, var10
                        );
                  }
               } catch (RuntimeException var17) {
                  q8.u("MenuWallpaper", "Draw failed; standard backdrop restored until a new selection", var17);
                  this.w("Draw failed");
                  this.e = var6;
                  this.F = (boolean)1;
                  this.H = Long.MAX_VALUE;
                  this.y = "Could not draw " + var6;
                  return false;
               }

               double var18 = this.m.getValue() / 100.0 * var8;
               if (var18 > 0.001) {
                  gu.Z(var1, 0, 0, var2, var3, ei.D(-16777216, var18));
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   public void u() {
      Path var1 = W();
      x.execute(() -> {
         try {
            Files.createDirectories(var1);
            getMc().execute(() -> class_156.method_668().method_60932(var1));
         } catch (Exception var3) {
            this.y = "Could not open the backgrounds folder";
            q8.u("MenuWallpaper", "Folder stage failed", var3);
         }
      });
   }

   public void X() {
      if (this.E == 0L || System.nanoTime() - this.E >= 1000000000L) {
         this.A();
      }
   }

   public void A() {
      this.E = System.nanoTime();
      Path var1 = W();
      x.execute(
         () -> {
            try {
               Files.createDirectories(var1);

               List var2;
               try (Stream var3 = Files.list(var1)) {
                  var2 = var3.filter(var0 -> Files.isRegularFile(var0))
                     .map(var0 -> var0.getFileName().toString())
                     .filter(MenuBackgroundModule::k)
                     .sorted(String.CASE_INSENSITIVE_ORDER)
                     .limit(256L)
                     .toList();
               }

               ArrayList var9 = new ArrayList();
               var9.add("None");
               var9.addAll(var2);
               getMc().execute(() -> {
                  this.W.k(var9);
                  if (this.e.equals(this.W.getValue()) && !this.e.equals("None")) {
                     this.e = "";
                     this.F = (boolean)0;
                     this.H = 0L;
                  }
               });
               q8.G("MenuWallpaper", "Backgrounds folder lists {} image(s)", var2.size());
            } catch (Exception var8) {
               this.y = "Could not read the backgrounds folder";
               q8.u("MenuWallpaper", "Library stage failed", var8);
            }
         }
      );
   }

   private void g(String var1) {
      this.e = var1;
      this.F = (boolean)0;
      long var2 = this.B.incrementAndGet();
      this.y = "Loading " + var1;
      Path var4 = W();
      x.execute(() -> {
         List var5 = List.of();

         try {
            Path var6 = var4.resolve(var1).normalize();
            if (!k(var1) || !var4.normalize().equals(var6.getParent())) {
               throw new IOException("Choose a PNG or JPG from the backgrounds folder");
            }

            byte[] var7;
            try (InputStream var8 = Files.newInputStream(var6)) {
               var7 = var8.readNBytes(67108865);
            }

            long var16 = System.nanoTime();
            var5 = si.u(I(var7));
            List var10 = var5;
            long var11 = (System.nanoTime() - var16) / 1000000L;
            getMc().execute(() -> this.I(var2, var1, var10, var11));
         } catch (Exception | OutOfMemoryError var15) {
            si.y(new ArrayList<>(var5));
            getMc().execute(() -> {
               if (var2 == this.B.get()) {
                  this.F = (boolean)1;
                  this.H = System.nanoTime() + 30000000000L;
                  this.y = "Could not load " + var1 + ": " + var15.getMessage();
                  q8.L("MenuWallpaper", "Load failed for {}; standard backdrop kept, retry in 30 s or on refresh: {}", var1, var15.toString());
               }
            });
         }
      });
   }

   private void I(long var1, String var3, List<class_1011> var4, long var5) {
      try {
         if (var1 == this.B.get() && this.isOperational() && var3.equals(this.W.getValue())) {
            class_1011 var7 = (class_1011)var4.getFirst();
            class_2960 var8 = class_2960.method_60655("larp-client", "menu-wallpaper/" + var1);
            si var9 = new si("Larp menu background " + var3, var4);
            getMc().method_1531().method_4616(var8, var9);
            this.c();
            this.U = var8;
            this.L = var7.method_4307();
            this.P = var7.method_4323();
            if (!var3.equals(this.I)) {
               this.C.w(0.0);
            }

            this.I = var3;
            this.y = var3 + " (" + this.L + "x" + this.P + ")";
            q8.G("MenuWallpaper", "Showing {} at {}x{} with {} mip levels; prepared in {} ms", var3, this.L, this.P, var4.size(), var5);
            return;
         }
      } catch (RuntimeException var13) {
         this.F = (boolean)1;
         this.H = System.nanoTime() + 30000000000L;
         this.y = "Could not upload " + var3;
         q8.u("MenuWallpaper", "GPU upload failed; previous background kept", var13);
         return;
      } finally {
         si.y(new ArrayList<>(var4));
      }
   }

   private void w(String var1) {
      this.B.incrementAndGet();
      int var2 = this.U != null ? 1 : 0;
      this.c();
      this.e = this.I = "";
      this.F = (boolean)0;
      this.H = 0L;
      if (var2 != 0) {
         q8.G("MenuWallpaper", "Background released: {}", var1);
      }
   }

   private void c() {
      if (this.U != null) {
         getMc().method_1531().method_4615(this.U);
      }

      this.U = null;
      this.L = this.P = 0;
   }

   @Override
   public void onDisable() {
      this.w("Module disabled");
      this.y = "Standard menu background";
   }

   public static boolean k(String var0) {
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

   public static MenuBackgroundModule.o Z(byte[] var0) throws IOException {
      if (var0.length >= 8 && ByteBuffer.wrap(var0).getLong() == -8552249625308161526L) {
         return MenuBackgroundModule.o.t;
      } else if (var0.length >= 3 && (var0[0] & 255) == 255 && (var0[1] & 255) == 216 && (var0[2] & 255) == 255) {
         return MenuBackgroundModule.o.W;
      } else {
         throw new IOException("Not a PNG or JPEG image");
      }
   }

   public static void o(long var0, long var2, long var4) throws IOException {
      if (var0 > 67108864L) {
         throw new IOException("Image must be at most 64 MiB");
      } else if (var2 < 1L || var4 < 1L || var2 > 8192L || var4 > 8192L || var2 * var4 > 40000000L) {
         throw new IOException("Image must be at most 8192 pixels on a side and 40 megapixels");
      }
   }

   private static class_1011 I(byte[] var0) throws IOException {
      if (var0.length > 67108864L) {
         throw new IOException("Image must be at most 64 MiB");
      }

      MenuBackgroundModule.o var1 = Z(var0);
      ByteBuffer var2 = MemoryUtil.memAlloc(var0.length);

      try {
         MemoryStack var3 = MemoryStack.stackPush();

         class_1011 var26;
         label171: {
            class_1011 var11;
            try {
               var2.put(var0).flip();
               IntBuffer var4 = var3.mallocInt(1);
               IntBuffer var5 = var3.mallocInt(1);
               IntBuffer var6 = var3.mallocInt(1);
               if (!STBImage.stbi_info_from_memory(var2, var4, var5, var6)) {
                  throw new IOException("Unreadable image: " + STBImage.stbi_failure_reason());
               }

               o(var0.length, var4.get(0), var5.get(0));
               if (var1 == MenuBackgroundModule.o.t) {
                  var26 = class_1011.method_4309(new ByteArrayInputStream(var0));
                  break label171;
               }

               ByteBuffer var7 = STBImage.stbi_load_from_memory(var2, var4, var5, var6, 4);
               if (var7 == null) {
                  throw new IOException("Unreadable JPEG: " + STBImage.stbi_failure_reason());
               }

               try {
                  int var8 = var4.get(0);
                  int var9 = var5.get(0);
                  class_1011 var10 = new class_1011(class_1012.field_4997, var8, var9, false);
                  MemoryUtil.memCopy(MemoryUtil.memAddress(var7), var10.method_67769(), (long)var8 * var9 * 4L);
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

   public enum o {
      t,
      W;
   }
}
