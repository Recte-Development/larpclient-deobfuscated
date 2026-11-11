package com.larpclient.module.render;

import com.larpclient.LarpClient;
import com.larpclient.config.ClientPreferences;
import com.larpclient.gui.screen.LarpTitleScreen;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.render.overlay.WallpaperTexture;
import com.larpclient.util.data.CoverCrop;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
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
   public static final String backgroundsDir = "config/larp-client/backgrounds";
   public static final String noneOption = "None";
   private static final long maxFileBytes = 67108864L;
   private static final long maxPixels = 40000000L;
   private static final long retryDelayNanos = 30000000000L;
   private static final int maxDimension = 8192;
   private static final int maxImages = 256;
   private static MenuBackgroundModule instance;
   private static final ExecutorService loaderExecutor = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, "Larp-MenuWallpaper");
      var1.setDaemon(true);
      return var1;
   });
   private final ModeSetting imageSetting = this.addSetting(new ModeSetting("Image", "Choose a PNG or JPG from the backgrounds folder", "None", "None") {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(var1) || MenuBackgroundModule.isSupportedImageName(var1);
      }

      @Override
      public void setValue(String var1) {
         if (!this.getModes().contains(var1) && MenuBackgroundModule.isSupportedImageName(var1)) {
            ArrayList var2 = new ArrayList<>(this.getModes());
            var2.add(var1);
            this.setModes(var2);
         }

         super.setValue(var1);
      }
   });
   private final ModeSetting fitSetting = this.addSetting(
      new ModeSetting(
         "Fit", "Fill covers the screen and crops the edges, Fit shows the whole image, Stretch ignores its shape", "Fill", "Fill", "Fit", "Stretch"
      )
   );
   private final NumberSetting dimSetting = this.addSetting(
      new NumberSetting("Dim", "Darkens the image so buttons and text stay readable, in percent", 25.0, 0.0, 80.0, 1.0)
   );
   private final ModeSetting showOnSetting = this.addSetting(
      new ModeSetting("Show On", "Main menu only, or every out-of-world client menu such as world and server lists", "Main menu", "Main menu", "All menus")
   );
   private final BooleanSetting sideWavesSetting = this.addSetting(
      new BooleanSetting("Side Waves", "Keep the menu's monochrome side waves over the image", false)
   );
   private final AtomicLong loadGeneration = new AtomicLong();
   private final Animation fadeAnimation = new Animation(0.0);
   private Identifier textureId;
   private int imageWidth;
   private int imageHeight;
   private String requestedImage = "";
   private String shownImage = "";
   private long retryAtNanos;
   private long lastScanNanos;
   private boolean libraryScanned;
   private boolean loadFailed;
   private volatile String status = "Add a PNG or JPG to the backgrounds folder";

   public MenuBackgroundModule() {
      super("Menu Background", "Use your own PNG or JPG as the main menu background", Category.VISUALS, -1);
      instance = this;
      this.imageSetting.onChange(var1 -> {
         this.retryAtNanos = 0L;
         this.loadFailed = (boolean)0;
         this.requestedImage = "";
      });
   }

   public static MenuBackgroundModule getInstance() {
      return instance;
   }

   @Override
   public String getStatus() {
      return this.status;
   }

   private static Path getBackgroundsPath() {
      return getMc().gameDirectory.toPath().resolve("config/larp-client/backgrounds");
   }

   public static boolean drawBackground(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      return instance != null && instance.isEnabled() ? instance != null && instance.draw(var0, var1, var2, var3) : false;
   }

   public static boolean wantsSideWaves() {
      return instance != null && instance.sideWavesSetting.getValue();
   }

   private boolean draw(GuiGraphicsExtractor var1, int var2, int var3, double var4) {
      if (!this.isOperational()) {
         if (this.textureId != null || !this.requestedImage.isEmpty()) {
            this.releaseBackground("Module off or not authorized");
         }

         return false;
      } else {
         if (!this.libraryScanned) {
            this.libraryScanned = (boolean)1;
            this.refreshLibrary();
         }

         String var6 = this.imageSetting.getValue();
         if (var6.equals("None")) {
            if (this.textureId != null || !this.requestedImage.isEmpty()) {
               this.releaseBackground("No image selected");
            }

            this.status = "Choose an image";
            return false;
         } else {
            if ((this.loadFailed || !var6.equals(this.requestedImage)) && System.nanoTime() >= this.retryAtNanos) {
               this.loadImage(var6);
            }

            if (this.showOnSetting.isMode("Main menu") && !(getMc().gui.screen() instanceof LarpTitleScreen)) {
               return false;
            }

            if (this.textureId != null && var2 > 0 && var3 > 0) {
               ClientPreferences var7 = LarpClient.getInstance().getConfigManager().getPreferences();
               double var8 = (var7.motion ? this.fadeAnimation.animateTo(1.0, 260) : 1.0) * Math.clamp(var4, 0.0, 1.0);
               if (var8 <= 0.001) {
                  return true;
               }

               int var10 = ColorUtil.withAlpha(-1, var8);

               try {
                  switch ((String)this.fitSetting.getValue()) {
                     case "Stretch":
                        var1.blit(
                           RenderPipelines.GUI_TEXTURED,
                           this.textureId,
                           0,
                           0,
                           0.0F,
                           0.0F,
                           var2,
                           var3,
                           this.imageWidth,
                           this.imageHeight,
                           this.imageWidth,
                           this.imageHeight,
                           var10
                        );
                        break;
                     case "Fit":
                        double var19 = Math.min((double)var2 / this.imageWidth, (double)var3 / this.imageHeight);
                        int var15 = Math.max(1, (int)Math.round(this.imageWidth * var19));
                        int var16 = Math.max(1, (int)Math.round(this.imageHeight * var19));
                        var1.blit(
                           RenderPipelines.GUI_TEXTURED,
                           this.textureId,
                           (var2 - var15) / 2,
                           (var3 - var16) / 2,
                           0.0F,
                           0.0F,
                           var15,
                           var16,
                           this.imageWidth,
                           this.imageHeight,
                           this.imageWidth,
                           this.imageHeight,
                           var10
                        );
                        break;
                     default:
                        CoverCrop var13 = CoverCrop.cover(var2, var3, this.imageWidth, this.imageHeight, 1.0, 0.0, 0.0);
                        var1.blit(
                           RenderPipelines.GUI_TEXTURED,
                           this.textureId,
                           0,
                           0,
                           var13.cropU(),
                           var13.cropV(),
                           var2,
                           var3,
                           var13.width(),
                           var13.height(),
                           this.imageWidth,
                           this.imageHeight,
                           var10
                        );
                  }
               } catch (RuntimeException var17) {
                  LarpLog.error("MenuWallpaper", "Draw failed; standard backdrop restored until a new selection", var17);
                  this.releaseBackground("Draw failed");
                  this.requestedImage = var6;
                  this.loadFailed = (boolean)1;
                  this.retryAtNanos = Long.MAX_VALUE;
                  this.status = "Could not draw " + var6;
                  return false;
               }

               double var18 = this.dimSetting.getValue() / 100.0 * var8;
               if (var18 > 0.001) {
                  DrawUtil.fillRect(var1, 0, 0, var2, var3, ColorUtil.withAlpha(-16777216, var18));
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   public void openFolder() {
      Path var1 = getBackgroundsPath();
      loaderExecutor.execute(() -> {
         try {
            Files.createDirectories(var1);
            getMc().execute(() -> Blaze3D.openPath(var1));
         } catch (Exception var3) {
            this.status = "Could not open the backgrounds folder";
            LarpLog.error("MenuWallpaper", "Folder stage failed", var3);
         }
      });
   }

   public void refreshLibraryThrottled() {
      if (this.lastScanNanos == 0L || System.nanoTime() - this.lastScanNanos >= 1000000000L) {
         this.refreshLibrary();
      }
   }

   public void refreshLibrary() {
      this.lastScanNanos = System.nanoTime();
      Path var1 = getBackgroundsPath();
      loaderExecutor.execute(
         () -> {
            try {
               Files.createDirectories(var1);

               List var2;
               try (Stream var3 = Files.list(var1)) {
                  var2 = var3.filter(var0 -> Files.isRegularFile(var0))
                     .map(var0 -> var0.getFileName().toString())
                     .filter(MenuBackgroundModule::isSupportedImageName)
                     .sorted(String.CASE_INSENSITIVE_ORDER)
                     .limit(256L)
                     .toList();
               }

               ArrayList var9 = new ArrayList();
               var9.add("None");
               var9.addAll(var2);
               getMc().execute(() -> {
                  this.imageSetting.setModes(var9);
                  if (this.requestedImage.equals(this.imageSetting.getValue()) && !this.requestedImage.equals("None")) {
                     this.requestedImage = "";
                     this.loadFailed = (boolean)0;
                     this.retryAtNanos = 0L;
                  }
               });
               LarpLog.info("MenuWallpaper", "Backgrounds folder lists {} image(s)", var2.size());
            } catch (Exception var8) {
               this.status = "Could not read the backgrounds folder";
               LarpLog.error("MenuWallpaper", "Library stage failed", var8);
            }
         }
      );
   }

   private void loadImage(String var1) {
      this.requestedImage = var1;
      this.loadFailed = (boolean)0;
      long var2 = this.loadGeneration.incrementAndGet();
      this.status = "Loading " + var1;
      Path var4 = getBackgroundsPath();
      loaderExecutor.execute(() -> {
         List var5 = List.of();

         try {
            Path var6 = var4.resolve(var1).normalize();
            if (!isSupportedImageName(var1) || !var4.normalize().equals(var6.getParent())) {
               throw new IOException("Choose a PNG or JPG from the backgrounds folder");
            }

            byte[] var7;
            try (InputStream var8 = Files.newInputStream(var6)) {
               var7 = var8.readNBytes(67108865);
            }

            long var16 = System.nanoTime();
            var5 = WallpaperTexture.buildMipmaps(decodeImage(var7));
            List var10 = var5;
            long var11 = (System.nanoTime() - var16) / 1000000L;
            getMc().execute(() -> this.onImageLoaded(var2, var1, var10, var11));
         } catch (Exception | OutOfMemoryError var15) {
            WallpaperTexture.closeAll(new ArrayList<>(var5));
            getMc().execute(() -> {
               if (var2 == this.loadGeneration.get()) {
                  this.loadFailed = (boolean)1;
                  this.retryAtNanos = System.nanoTime() + 30000000000L;
                  this.status = "Could not load " + var1 + ": " + var15.getMessage();
                  LarpLog.warn("MenuWallpaper", "Load failed for {}; standard backdrop kept, retry in 30 s or on refresh: {}", var1, var15.toString());
               }
            });
         }
      });
   }

   private void onImageLoaded(long var1, String var3, List<NativeImage> var4, long var5) {
      try {
         if (var1 == this.loadGeneration.get() && this.isOperational() && var3.equals(this.imageSetting.getValue())) {
            NativeImage var7 = (NativeImage)var4.getFirst();
            Identifier var8 = Identifier.fromNamespaceAndPath("larp-client", "menu-wallpaper/" + var1);
            WallpaperTexture var9 = new WallpaperTexture("Larp menu background " + var3, var4);
            getMc().getTextureManager().register(var8, var9);
            this.releaseBackground();
            this.textureId = var8;
            this.imageWidth = var7.getWidth();
            this.imageHeight = var7.getHeight();
            if (!var3.equals(this.shownImage)) {
               this.fadeAnimation.snapTo(0.0);
            }

            this.shownImage = var3;
            this.status = var3 + " (" + this.imageWidth + "x" + this.imageHeight + ")";
            LarpLog.info(
               "MenuWallpaper", "Showing {} at {}x{} with {} mip levels; prepared in {} ms", var3, this.imageWidth, this.imageHeight, var4.size(), var5
            );
            return;
         }
      } catch (RuntimeException var13) {
         this.loadFailed = (boolean)1;
         this.retryAtNanos = System.nanoTime() + 30000000000L;
         this.status = "Could not upload " + var3;
         LarpLog.error("MenuWallpaper", "GPU upload failed; previous background kept", var13);
         return;
      } finally {
         WallpaperTexture.closeAll(new ArrayList<>(var4));
      }
   }

   private void releaseBackground(String var1) {
      this.loadGeneration.incrementAndGet();
      int var2 = this.textureId != null ? 1 : 0;
      this.releaseBackground();
      this.requestedImage = this.shownImage = "";
      this.loadFailed = (boolean)0;
      this.retryAtNanos = 0L;
      if (var2 != 0) {
         LarpLog.info("MenuWallpaper", "Background released: {}", var1);
      }
   }

   private void releaseBackground() {
      if (this.textureId != null) {
         getMc().getTextureManager().release(this.textureId);
      }

      this.textureId = null;
      this.imageWidth = this.imageHeight = 0;
   }

   @Override
   public void onDisable() {
      this.releaseBackground("Module disabled");
      this.status = "Standard menu background";
   }

   public static boolean isSupportedImageName(String var0) {
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

   public static MenuBackgroundModule.ImageFormat detectFormat(byte[] var0) throws IOException {
      if (var0.length >= 8 && ByteBuffer.wrap(var0).getLong() == -8552249625308161526L) {
         return MenuBackgroundModule.ImageFormat.PNG;
      } else if (var0.length >= 3 && (var0[0] & 255) == 255 && (var0[1] & 255) == 216 && (var0[2] & 255) == 255) {
         return MenuBackgroundModule.ImageFormat.JPEG;
      } else {
         throw new IOException("Not a PNG or JPEG image");
      }
   }

   public static void checkImageLimits(long var0, long var2, long var4) throws IOException {
      if (var0 > 67108864L) {
         throw new IOException("Image must be at most 64 MiB");
      } else if (var2 < 1L || var4 < 1L || var2 > 8192L || var4 > 8192L || var2 * var4 > 40000000L) {
         throw new IOException("Image must be at most 8192 pixels on a side and 40 megapixels");
      }
   }

   private static NativeImage decodeImage(byte[] var0) throws IOException {
      if (var0.length > 67108864L) {
         throw new IOException("Image must be at most 64 MiB");
      }

      MenuBackgroundModule.ImageFormat var1 = detectFormat(var0);
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

               checkImageLimits(var0.length, var4.get(0), var5.get(0));
               if (var1 == MenuBackgroundModule.ImageFormat.PNG) {
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

   public enum ImageFormat {
      PNG,
      JPEG;
   }
}
