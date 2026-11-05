package com.larpclient.update;

import com.larp.updater.Host;
import com.larp.updater.Identity;
import com.larp.updater.UpdateManifest;
import com.larpclient.LarpClient;
import com.larpclient.fs;
import com.larpclient.hl;
import com.larpclient.mo;
import com.larpclient.r4;
import com.larpclient.xo;
import com.mojang.blaze3d.Blaze3D;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.Clock;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public final class UpdateRuntime {
   private static UpdateService service;
   private static String unavailable = "Updates are unavailable. Use the official downloads page.";
   private static String dismissed = "";
   private static String shownReceipt = "";
   private static final int INSTALLER_LIMIT = 8388608;

   private UpdateRuntime() {
   }

   public static synchronized void start() {
      if (service == null) {
         try {
            FabricLoader var0 = FabricLoader.getInstance();
            Identity var1 = new Identity(xo.h, xo.X, xo.D, xo.T, xo.A, xo.i, xo.y, xo.z);
            Host var2 = Host.current(((ModContainer)var0.getModContainer("fabricloader").orElseThrow()).getMetadata().getVersion().getFriendlyString());

            byte[] var3;
            try (InputStream var4 = UpdateRuntime.class.getResourceAsStream("/assets/larp-client/updates/root.json")) {
               if (var4 == null) {
                  throw new IOException("Missing packaged update root");
               }

               var3 = var4.readNBytes(131073);
               if (var3.length > 131072) {
                  throw new IOException("Update root size limit");
               }
            }

            List var11 = ((ModContainer)var0.getModContainer("larp-client").orElseThrow()).getOrigin().getPaths();
            Path var5 = var11.size() == 1 && Files.isRegularFile((Path)var11.getFirst(), LinkOption.NOFOLLOW_LINKS) ? (Path)var11.getFirst() : null;
            UpdateStore var6 = new UpdateStore(var0.getGameDir(), var5, var1);
            UpdateTransport var7 = new UpdateTransport(hl::D);
            service = new UpdateService(var1, var2, var3, var6, var7, Minecraft.getInstance()::execute, Clock.systemUTC(), (var0x, var1x) -> {
               if (var1x == null) {
                  r4.V("Updates", var0x);
               } else {
                  r4.r("Updates", var0x, var1x);
               }
            });
            service.initialize(preferences().h);
            r4.V(
               "Updates",
               "Updater initialized for {}/{}/{}/{}; public metadata only until a download is approved",
               var1.edition(),
               var1.minecraft(),
               var1.channel(),
               var1.flavor()
            );
         } catch (Exception var10) {
            r4.r("Updates", "Update initialization failed; official manual download remains available", var10);
         }
      }
   }

   public static UpdateService service() {
      return service;
   }

   public static String unavailable() {
      return unavailable;
   }

   private static fs preferences() {
      return LarpClient.y().v().E();
   }

   public static void open(Screen var0) {
      Minecraft.getInstance().gui.setScreen(new UpdateScreen(var0));
   }

   public static void officialDownloads() {
      Blaze3D.openUri(URI.create("https://larpclient.com/download"));
   }

   public static boolean automatic(boolean var0) {
      mo var1 = LarpClient.y().v();
      boolean var2 = preferences().h;
      preferences().h = var0;
      if (!var1.Y()) {
         preferences().h = var2;
         return false;
      }

      if (var0 && service != null) {
         service.check();
      }

      return true;
   }

   public static boolean skip() {
      if (service != null && service.snapshot().release() != null) {
         mo var0 = LarpClient.y().v();
         String var1 = preferences().G;
         preferences().G = service.snapshot().release().releaseId();
         if (!var0.Y()) {
            preferences().G = var1;
            return false;
         } else {
            later();
            return true;
         }
      } else {
         return false;
      }
   }

   public static void later() {
      dismissed = noticeKey();
   }

   public static boolean notice() {
      return UpdateNotices.visible(noticeKey(), dismissed, preferences().G);
   }

   private static String noticeKey() {
      return UpdateNotices.key(service == null ? null : service.snapshot(), preferences().m, shownReceipt);
   }

   public static String noticeAction() {
      return noticeKey().startsWith("installed:") ? "What's new" : "View update";
   }

   public static String noticeDetail() {
      if (noticeKey().startsWith("installed:")) {
         return "What's new in this version";
      }

      UpdateManifest.Release var0 = service == null ? null : service.snapshot().release();
      return var0 == null ? "Client updates" : "Version " + var0.version() + " / " + var0.minecraft();
   }

   public static String noticeTitle() {
      if (service == null) {
         return "Updates";
      }

      UpdateService.Snapshot var0 = service.snapshot();
      if (noticeKey().startsWith("installed:")) {
         return var0.installedNotice().startsWith("local-restore:") ? "Previous build restored" : "Updated to " + service.identity().version();
      }

      return switch (var0.state()) {
         case QUEUED -> "Updates on close";
         case READY -> "Update downloaded";
         case DOWNLOADING -> "Downloading update";
         default -> "Update available";
      };
   }

   public static void noticeShown() {
      if (noticeKey().startsWith("installed:")) {
         String var0 = service.snapshot().installedNotice();
         if (!var0.equals(shownReceipt)) {
            shownReceipt = var0;
            if (!var0.equals(preferences().m)) {
               mo var1 = LarpClient.y().v();
               String var2 = preferences().m;
               preferences().m = var0;
               if (!var1.Y()) {
                  preferences().m = var2;
               }
            }
         }
      }
   }

   public static void stop() {
      if (service != null) {
         boolean var0 = service.snapshot().state() == UpdateService.State.QUEUED;
         service.close();
         if (var0) {
            try (InputStream var1 = UpdateRuntime.class.getResourceAsStream("/assets/larp-client/updates/LarpUpdater.jar")) {
               if (var1 == null) {
                  throw new IOException("Bundled installer is missing");
               }

               byte[] var2 = var1.readNBytes(8388609);
               if (var2.length > 8388608) {
                  throw new IOException("Bundled installer size limit");
               }

               service.launchInstaller(var2, installerJava());
               r4.V(
                  "Updates",
                  "Installer started; version {} installs after Minecraft exits (config/larp-client/updates/installer.log)",
                  service.snapshot().release().version()
               );
            } catch (Exception var6) {
               r4.r("Updates", "Could not start the installer; the approved update stays queued for the next close", var6);
            }
         }
      }
   }

   static Path installerJava() {
      Path var0 = ProcessHandle.current()
         .info()
         .command()
         .map(var0x -> Path.of(var0x))
         .orElseGet(() -> Path.of(System.getProperty("java.home"), "bin", "java"));
      Path var1 = var0.resolveSibling("javaw.exe");
      return var0.getFileName().toString().equalsIgnoreCase("java.exe") && Files.isRegularFile(var1) ? var1 : var0;
   }
}
