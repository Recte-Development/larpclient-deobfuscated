package com.larpclient.update;

import com.larpclient.LarpClient;
import com.larpclient.config.ClientPreferences;
import com.larpclient.config.ConfigManager;
import com.larpclient.core.BuildInfo;
import com.larpclient.network.ServiceStatusClient;
import com.larpclient.util.misc.LarpLog;
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
            Identity var1 = new Identity(
               BuildInfo.product,
               BuildInfo.edition,
               BuildInfo.minecraftVersion,
               BuildInfo.loader,
               BuildInfo.channel,
               BuildInfo.flavor,
               BuildInfo.larpVersion,
               BuildInfo.buildId
            );
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
            UpdateTransport var7 = new UpdateTransport(ServiceStatusClient::signUpdateRequest);
            service = new UpdateService(var1, var2, var3, var6, var7, Minecraft.getInstance()::execute, Clock.systemUTC(), (var0x, var1x) -> {
               if (var1x == null) {
                  LarpLog.info("Updates", var0x);
               } else {
                  LarpLog.error("Updates", var0x, var1x);
               }
            });
            service.initialize(preferences().updateAutoCheck);
            LarpLog.info(
               "Updates",
               "Updater initialized for {}/{}/{}/{}; public metadata only until a download is approved",
               var1.edition(),
               var1.minecraft(),
               var1.channel(),
               var1.flavor()
            );
         } catch (Exception var10) {
            LarpLog.error("Updates", "Update initialization failed; official manual download remains available", var10);
         }
      }
   }

   public static UpdateService service() {
      return service;
   }

   public static String unavailable() {
      return unavailable;
   }

   private static ClientPreferences preferences() {
      return LarpClient.getInstance().getConfigManager().getPreferences();
   }

   public static void open(Screen var0) {
      Minecraft.getInstance().gui.setScreen(new UpdateScreen(var0));
   }

   public static void officialDownloads() {
      Blaze3D.openUri(URI.create("https://larpclient.com/download"));
   }

   public static boolean automatic(boolean var0) {
      ConfigManager var1 = LarpClient.getInstance().getConfigManager();
      boolean var2 = preferences().updateAutoCheck;
      preferences().updateAutoCheck = var0;
      if (!var1.savePreferences()) {
         preferences().updateAutoCheck = var2;
         return false;
      }

      if (var0 && service != null) {
         service.check();
      }

      return true;
   }

   public static boolean skip() {
      if (service != null && service.snapshot().release() != null) {
         ConfigManager var0 = LarpClient.getInstance().getConfigManager();
         String var1 = preferences().updateSkippedRelease;
         preferences().updateSkippedRelease = service.snapshot().release().releaseId();
         if (!var0.savePreferences()) {
            preferences().updateSkippedRelease = var1;
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

   public static String primaryLabel() {
      return service == null ? "Official downloads" : primaryLabel(service.snapshot());
   }

   static String primaryLabel(UpdateService.Snapshot var0) {
      return switch (var0.state()) {
         case STARTING -> "Preparing updates";
         case CHECKING -> "Checking updates";
         case CANCELLING -> "Cancelling update";
         case AVAILABLE -> var0.installable() ? "Update" : "Official downloads";
         case DOWNLOADING -> "Cancel download";
         case READY -> "Update";
         case QUEUED -> "Cancel update";
         default -> "Check for updates";
      };
   }

   public static boolean primaryEnabled() {
      return service == null || primaryEnabled(service.snapshot());
   }

   static boolean primaryEnabled(UpdateService.Snapshot var0) {
      return !var0.busy() || var0.state() == UpdateService.State.DOWNLOADING;
   }

   public static void primary() {
      if (service == null) {
         officialDownloads();
      } else {
         UpdateService.Snapshot var0 = service.snapshot();
         switch (var0.state()) {
            case AVAILABLE:
               if (var0.installable()) {
                  service.download();
               } else {
                  officialDownloads();
               }
               break;
            case DOWNLOADING:
            case QUEUED:
               service.cancel();
               break;
            case READY:
               service.queue();
               break;
            default:
               service.check();
         }
      }
   }

   public static String secondaryLabel() {
      return service == null ? "Official downloads" : secondaryLabel(service.snapshot());
   }

   static String secondaryLabel(UpdateService.Snapshot var0) {
      return switch (var0.state()) {
         case AVAILABLE -> "Check again";
         default -> "Official downloads";
         case READY -> "Cancel download";
         case ERROR -> clearable(var0) ? "Clear staged update" : "Official downloads";
      };
   }

   private static boolean clearable(UpdateService.Snapshot var0) {
      return !var0.message().equals("Updates could not be checked. Try again later or use the official downloads page.");
   }

   public static boolean secondaryEnabled() {
      return service == null || service.snapshot().state() != UpdateService.State.CANCELLING;
   }

   public static void secondary() {
      if (service == null) {
         officialDownloads();
      } else {
         UpdateService.Snapshot var0 = service.snapshot();
         switch (var0.state()) {
            case AVAILABLE:
               service.check();
               break;
            case DOWNLOADING:
            case QUEUED:
            default:
               officialDownloads();
               break;
            case READY:
               service.cancel();
               break;
            case ERROR:
               if (clearable(var0)) {
                  service.cancel();
               } else {
                  officialDownloads();
               }
         }
      }
   }

   public static boolean skippable() {
      return service != null && service.snapshot().state() == UpdateService.State.AVAILABLE && service.snapshot().release() != null
         ? !service.snapshot().release().releaseId().equals(preferences().updateSkippedRelease)
         : false;
   }

   public static String offeredVersion() {
      return service == null ? "" : offeredVersion(service.snapshot());
   }

   static String offeredVersion(UpdateService.Snapshot var0) {
      if (var0.release() == null) {
         return "";
      }

      return switch (var0.state()) {
         case AVAILABLE, DOWNLOADING, READY, QUEUED -> var0.release().version();
         default -> "";
      };
   }

   public static String status() {
      return service == null ? "Unavailable" : status(service.snapshot());
   }

   static String status(UpdateService.Snapshot var0) {
      return switch (var0.state()) {
         case STARTING -> "Preparing";
         case CHECKING -> "Checking";
         case CANCELLING -> "Cancelling";
         case AVAILABLE -> "Update available";
         case DOWNLOADING -> "Downloading "
            + (var0.release() != null && var0.release().size() > 0L ? Math.min(100L, var0.downloaded() * 100L / var0.release().size()) : 0L)
            + "%";
         case READY -> "Downloaded";
         case QUEUED -> "Installs when you close Minecraft";
         case ERROR -> "Failed";
         case CURRENT -> var0.release() == null ? "Not checked yet" : "Up to date";
      };
   }

   public static String statusDetail() {
      return service == null ? unavailable : statusDetail(service.snapshot());
   }

   static String statusDetail(UpdateService.Snapshot var0) {
      if (var0.state() == UpdateService.State.ERROR) {
         return var0.message();
      }

      if (!var0.busy() && var0.state() != UpdateService.State.QUEUED) {
         String var1 = var0.message();
         if (!var1.equals("Check for a newer version")
            && !var1.equals("You are using the current version")
            && !var1.equals("A signed update is available")
            && !var1.equals("The downloaded update is verified. Press Update to install it when you close Minecraft.")) {
            return var1;
         } else if (var0.state() != UpdateService.State.AVAILABLE && var0.state() != UpdateService.State.READY) {
            return "";
         } else {
            return var0.installable()
               ? "Press Update and it installs when you close Minecraft."
               : "This install can't update itself. Use the official downloads page.";
         }
      } else {
         return "";
      }
   }

   public static String releaseNotes() {
      return service == null ? "" : releaseNotes(service.snapshot().release());
   }

   static String releaseNotes(UpdateManifest.Release var0) {
      if (var0 == null) {
         return "";
      }

      StringBuilder var1 = new StringBuilder(var0.title());
      if (!var0.summary().isBlank()) {
         var1.append('\n').append(var0.summary());
      }

      for (String var3 : var0.notes()) {
         var1.append("\n• ").append(var3);
      }

      return var1.toString();
   }

   public static boolean notice() {
      return UpdateNotices.visible(noticeKey(), dismissed, preferences().updateSkippedRelease);
   }

   private static String noticeKey() {
      return UpdateNotices.key(service == null ? null : service.snapshot(), preferences().updateSeenReceipt, shownReceipt);
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
         case DOWNLOADING -> "Downloading update";
         case READY -> "Update downloaded";
         case QUEUED -> "Updates on close";
         default -> "Update available";
      };
   }

   public static void noticeShown() {
      if (noticeKey().startsWith("installed:")) {
         String var0 = service.snapshot().installedNotice();
         if (!var0.equals(shownReceipt)) {
            shownReceipt = var0;
            if (!var0.equals(preferences().updateSeenReceipt)) {
               ConfigManager var1 = LarpClient.getInstance().getConfigManager();
               String var2 = preferences().updateSeenReceipt;
               preferences().updateSeenReceipt = var0;
               if (!var1.savePreferences()) {
                  preferences().updateSeenReceipt = var2;
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
               LarpLog.info(
                  "Updates",
                  "Installer started; version {} installs after Minecraft exits (config/larp-client/updates/installer.log)",
                  service.snapshot().release().version()
               );
            } catch (Exception var6) {
               LarpLog.error("Updates", "Could not start the installer; the approved update stays queued for the next close", var6);
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
