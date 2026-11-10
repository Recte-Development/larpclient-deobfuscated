package com.larpclient.update;

import com.larp.updater.Host;
import com.larp.updater.Identity;
import com.larp.updater.UpdateManifest;
import com.larpclient.LarpClient;
import com.larpclient.ja;
import com.larpclient.q8;
import com.larpclient.qk;
import com.larpclient.sc;
import com.larpclient.y0;
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
import net.minecraft.class_156;
import net.minecraft.class_310;
import net.minecraft.class_437;

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
            Identity var1 = new Identity(sc.L, sc.J, sc.E, sc.Y, sc.g, sc.K, sc.v, sc.A);
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
            UpdateTransport var7 = new UpdateTransport(ja::V);
            service = new UpdateService(var1, var2, var3, var6, var7, class_310.method_1551()::execute, Clock.systemUTC(), (var0x, var1x) -> {
               if (var1x == null) {
                  q8.G("Updates", var0x);
               } else {
                  q8.u("Updates", var0x, var1x);
               }
            });
            service.initialize(preferences().N);
            q8.G(
               "Updates",
               "Updater initialized for {}/{}/{}/{}; public metadata only until a download is approved",
               var1.edition(),
               var1.minecraft(),
               var1.channel(),
               var1.flavor()
            );
         } catch (Exception var10) {
            q8.u("Updates", "Update initialization failed; official manual download remains available", var10);
         }
      }
   }

   public static UpdateService service() {
      return service;
   }

   public static String unavailable() {
      return unavailable;
   }

   private static y0 preferences() {
      return LarpClient.b().e().F();
   }

   public static void open(class_437 var0) {
      class_310.method_1551().method_1507(new UpdateScreen(var0));
   }

   public static void officialDownloads() {
      class_156.method_668().method_673(URI.create("https://larpclient.com/download"));
   }

   public static boolean automatic(boolean var0) {
      qk var1 = LarpClient.b().e();
      boolean var2 = preferences().N;
      preferences().N = var0;
      if (!var1.N()) {
         preferences().N = var2;
         return false;
      }

      if (var0 && service != null) {
         service.check();
      }

      return true;
   }

   public static boolean skip() {
      if (service != null && service.snapshot().release() != null) {
         qk var0 = LarpClient.b().e();
         String var1 = preferences().o;
         preferences().o = service.snapshot().release().releaseId();
         if (!var0.N()) {
            preferences().o = var1;
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
         ? !service.snapshot().release().releaseId().equals(preferences().o)
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
      return UpdateNotices.visible(noticeKey(), dismissed, preferences().o);
   }

   private static String noticeKey() {
      return UpdateNotices.key(service == null ? null : service.snapshot(), preferences().x, shownReceipt);
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
            if (!var0.equals(preferences().x)) {
               qk var1 = LarpClient.b().e();
               String var2 = preferences().x;
               preferences().x = var0;
               if (!var1.N()) {
                  preferences().x = var2;
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
               q8.G(
                  "Updates",
                  "Installer started; version {} installs after Minecraft exits (config/larp-client/updates/installer.log)",
                  service.snapshot().release().version()
               );
            } catch (Exception var6) {
               q8.u("Updates", "Could not start the installer; the approved update stays queued for the next close", var6);
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
