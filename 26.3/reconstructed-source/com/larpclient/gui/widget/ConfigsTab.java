package com.larpclient.gui.widget;

import com.google.gson.JsonObject;
import com.larpclient.config.ProfileValidator;
import com.larpclient.network.ApiException;
import com.larpclient.network.ApiResponseValidator;
import com.larpclient.util.misc.BugReportBuilder;
import com.larpclient.util.misc.LarpLog;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

final class ConfigsTab {
   private final ConfigsPanel screen;
   String selectedLocal;
   String importName = "";
   String shareCodeInput = "";
   List<JsonObject> cloudItems = List.of();
   JsonObject selectedCloud;
   ProfileValidator.ProfileSummary importPreview;
   boolean experimental;

   ConfigsTab(ConfigsPanel var1) {
      this.screen = var1;
      this.selectedLocal = null;
   }

   void clearImportSelection() {
      this.selectedCloud = null;
      this.importPreview = null;
   }

   void requestCloudAction() {
      this.selectedCloud = null;
      this.cloudItems = List.of();
   }

   TextField shareCodeField() {
      return this.screen.addTextField("LARP-XXXXXXXXXXXXXXXX", this.shareCodeInput, 32, var1 -> this.shareCodeInput = var1);
   }

   void renderImportPreview() {
      this.screen.setDialogText("Import preview", this.importPreview.name());
      this.screen
         .text(
            "Larp "
               + this.importPreview.larpVersion()
               + " · Minecraft "
               + this.importPreview.minecraftVersion()
               + "\n"
               + this.importPreview.modules()
               + " modules, "
               + this.importPreview.enabled()
               + " enabled"
         );
      this.screen.text("Supported settings load. Unavailable settings stay saved. Saving does not change your active config.");
      this.screen.addTextField("Local config name", this.importName, 32, var1 -> this.importName = var1);
      this.screen.addGuardedDialogButton("Cancel", () -> {
         this.importPreview = null;
         this.screen.importing = (boolean)0;
         this.screen.build();
      });
      this.screen.addGuardedDialogButton("Save locally", () -> this.confirmSave(false)).accented = (boolean)1;
   }

   void renderConfigList() {
      String var1 = this.screen.searchQuery.toLowerCase(Locale.ROOT);

      for (String var3 : this.screen.configManager.listProfiles()) {
         if (var3.toLowerCase(Locale.ROOT).contains(var1)) {
            this.screen
               .addConfigCard(
                  var3,
                  var3.equals(this.screen.configManager.getActiveProfile()) ? "Active" : "Local",
                  this.selectedCloud == null && var3.equals(this.selectedLocal),
                  () -> {
                     this.selectedCloud = null;
                     this.selectedLocal = var3;
                     this.screen.scrollOffset = 0;
                     this.screen.build();
                  }
               );
            boolean var4 = var3.equals(this.screen.configManager.getActiveProfile());
            this.screen.addCardAction(var4 ? "Update" : "Load", () -> {
               if (var4) {
                  this.confirmSave(var3);
               } else {
                  this.confirmLoad(var3);
               }
            });
            this.screen.addCardAction("Share", () -> this.confirmShare(var3));
            if (!var4) {
               this.screen.addCardAction("Delete", () -> this.confirmDelete(var3));
            }
         }
      }

      for (JsonObject var6 : this.cloudItems) {
         String var7 = ProfileValidator.readString(var6, "name", 32);
         if (var7.toLowerCase(Locale.ROOT).contains(var1)) {
            this.screen
               .addConfigCard(
                  var7,
                  this.experimental ? "Experimental storage" : "Shared · " + ApiResponseValidator.marketplaceStateLabel(var6),
                  this.selectedCloud == var6,
                  () -> {
                     this.selectedCloud = var6;
                     this.screen.scrollOffset = 0;
                     this.screen.build();
                  }
               );
         }
      }
   }

   void renderDetails() {
      if (this.selectedCloud != null) {
         this.renderCloudDetails();
      } else if (this.selectedLocal != null) {
         String var1 = this.selectedLocal;
         boolean var2 = var1.equals(this.screen.configManager.getActiveProfile());
         Optional var3 = this.cloudItems.stream().filter(var1x -> ProfileValidator.readString(var1x, "name", 32).equals(var1)).findFirst();
         String var4 = var2 ? "Active config" : "Saved config";
         if (var3.isPresent()) {
            var4 = var4
               + (
                  this.experimental
                     ? " · Experimental copy"
                     : " · Shared, " + ApiResponseValidator.marketplaceStateLabel((JsonObject)var3.get()).toLowerCase(Locale.ROOT)
               );
         }

         this.screen.setDialogText(var1, var4);
         if (var3.isPresent() && !this.experimental && !ApiResponseValidator.discordStatusLabel((JsonObject)var3.get()).isEmpty()) {
            this.screen.text(ApiResponseValidator.discordStatusLabel((JsonObject)var3.get()));
         }

         if (!this.screen.shareCode.isEmpty()) {
            this.screen.option("Copy share code", this.screen.shareCode, () -> {
               this.screen.getMinecraft().keyboardHandler.setClipboard(this.screen.shareCode);
               this.screen.statusMessage = "Share code copied to clipboard.";
            });
         }

         this.screen.option("Load", "Replace your live settings with this config", () -> this.confirmLoad(var1));
         this.screen.option("Save changes", "Overwrite it with your current settings", () -> this.confirmSave(var1));
         if (this.screen.cloudApi.isProfileReady() && this.experimental) {
            this.screen
               .option(
                  "Upload experimental copy",
                  "Send the saved file to experimental storage",
                  () -> this.screen
                     .showConfirm(
                        "Upload an experimental copy?\nSaved “" + var1 + "” goes to experimental storage.", "Upload", () -> this.uploadExperimental(var1, true)
                     )
               );
         }

         this.screen.option("Share", "Upload it and copy a share code", () -> this.confirmShare(var1));
         this.screen.option("Duplicate", "Save a copy under a new name", () -> this.screen.addConfigCard("Duplicate", var1 + " copy"));
         this.screen.option("Rename", "Change its name", () -> this.screen.addConfigCard("Rename", var1));
         if (!var2) {
            this.screen.addSeparator();
            this.screen.option("Delete", "Remove it from this computer", () -> this.confirmDelete(var1));
         }
      }
   }

   private void confirmLoad(String var1) {
      this.screen.showConfirm("Load “" + var1 + "”?\nYour live settings are replaced with this config.", "Load", () -> {
         this.screen.statusMessage = this.screen.configManager.loadProfile(var1) ? "Loaded " + var1 : "Load failed. Existing profile retained.";
         this.screen.build();
      });
   }

   private void confirmSave(String var1) {
      this.screen.showConfirm("Save changes to “" + var1 + "”?\nThe saved config is replaced with your current settings.", "Save", () -> {
         this.screen.gui.saveGuiState();

         try {
            this.screen.configManager.saveProfileAs(var1, true);
            this.screen.statusMessage = "Saved " + var1;
         } catch (IOException var3) {
            LarpLog.warn("Config", "Save failed: {}", var3.toString());
            this.screen.statusMessage = "Save failed: " + var3.getMessage();
         }

         this.screen.build();
      });
   }

   private void confirmDelete(String var1) {
      this.screen.addTextField("Delete “" + var1 + "”?\nThe saved file is removed from this computer. This cannot be undone.", "Delete", () -> {
         try {
            this.screen.configManager.deleteProfile(var1);
            this.selectedLocal = null;
            this.screen.statusMessage = "Deleted";
         } catch (IOException var3) {
            LarpLog.warn("Config", "Delete failed: {}", var3.toString());
            this.screen.statusMessage = "Delete failed: " + var3.getMessage();
         }

         this.screen.build();
      });
   }

   private void confirmShare(String var1) {
      this.screen.shareCode = "";
      this.screen
         .showDialog(
            "Share “"
               + var1
               + "”\nIt gets uploaded to larpclient.com and the code is copied to your clipboard. Only people with the code can open it.\nPost to Discord and the bot also puts it in the Discord server, code included, within a minute.",
            "Share",
            () -> this.uploadConfig(var1, false, false),
            "Share and post to Discord",
            () -> this.uploadConfig(var1, false, true)
         );
   }

   private void uploadExperimental(String var1, boolean var2) {
      this.uploadConfig(var1, var2, false);
   }

   private void uploadConfig(String var1, boolean var2, boolean var3) {
      try {
         JsonObject var4 = ProfileValidator.exportProfile(var1, this.screen.configManager.readProfile(var1), this.screen.getModules());
         if (var3) {
            var4.addProperty("discordSharing", true);
         }

         this.screen.shareCode = "";
         this.screen
            .runRequest(
               "Uploading to larpclient.com...",
               var2
                  ? this.screen.cloudApi.request("POST", "/api/profiles", var4, null)
                  : this.screen.cloudApi.api.request("POST", "/api/configs", var4, true, null),
               var3x -> {
                  ApiResponseValidator.readId(var3x, "id");
                  ApiResponseValidator.readId(var3x, "revision");
                  if (var2) {
                     this.screen.statusMessage = "Experimental profile uploaded";
                  } else {
                     this.screen.shareCode = ApiResponseValidator.readShareCode(var3x);
                     byte var4x = 1;

                     try {
                        this.screen.getMinecraft().keyboardHandler.setClipboard(this.screen.shareCode);
                        this.screen.statusMessage = "Uploaded successfully. Share code copied to clipboard.";
                        LarpLog.info("Config", "Saved config uploaded; share code copied to clipboard");
                     } catch (RuntimeException var6x) {
                        var4x = 0;
                        this.screen.statusMessage = "Uploaded successfully. Clipboard copy failed. Use Copy share code to retry.";
                        LarpLog.warn("Config", "Upload succeeded but clipboard copy failed: {}", var6x.toString());
                     }

                     if (var3) {
                        this.screen.statusMessage = (var4x != 0 ? "Code copied. " : "Clipboard copy failed, use Copy share code. ")
                           + "The bot posts it in Discord within a minute.";
                        LarpLog.info("Config", "Shared config opted in to Discord posting");
                        this.refreshCloudItems(true);
                     }
                  }
               }
            );
      } catch (IOException var5) {
         LarpLog.warn("Config", "Config operation failed: {}", var5.toString());
         this.screen.statusMessage = "Could not read the saved profile. Save it locally first.";
         this.screen.build();
      } catch (IllegalArgumentException var6) {
         LarpLog.warn("Config", "Upload of {} refused before sending: {}", var1, var6.getMessage());
         this.screen.statusMessage = var6.getMessage() == null ? "This profile cannot be shared as it is." : var6.getMessage();
         this.screen.build();
      }
   }

   void renderCloudDetails() {
      if (this.experimental && !this.screen.cloudApi.isProfileReady()) {
         this.screen.setDialogText("Experimental storage", "");
         this.screen.text("Experimental access is unavailable for this session.");
      } else if (this.selectedCloud != null) {
         String var1 = ProfileValidator.readString(this.selectedCloud, "name", 32);
         String var2 = "Updated " + ProfileValidator.readString(this.selectedCloud, "updatedAt", 40);
         if (!this.experimental) {
            this.screen.setDialogText(var1, ApiResponseValidator.marketplaceStateLabel(this.selectedCloud) + " · " + var2);
            String var3 = ApiResponseValidator.discordStatusLabel(this.selectedCloud);
            if (!var3.isEmpty()) {
               this.screen.text(var3);
            }

            String var4 = ProfileValidator.readString(this.selectedCloud, "rejectionReason", 500);
            if (!var4.isBlank()) {
               this.screen.text("Review reason: " + var4);
            }

            this.screen.option("Copy share code", ApiResponseValidator.readShareCode(this.selectedCloud), () -> {
               this.screen.getMinecraft().keyboardHandler.setClipboard(ApiResponseValidator.readShareCode(this.selectedCloud));
               this.screen.statusMessage = "Copied";
            });
            this.screen
               .option("Import / download", "Preview it before saving locally", () -> this.importByCode(ApiResponseValidator.readShareCode(this.selectedCloud)));
            this.screen
               .option(
                  "Regenerate share code",
                  "Replace the code; the old one stops working",
                  () -> this.confirmCloudAction("regenerate", "Regenerate the share code?\nThe old code stops working immediately.", "Regenerate")
               );
            String var5 = this.selectedCloud.get("marketplaceState").getAsString();
            boolean var6 = ApiResponseValidator.isDiscordShared(this.selectedCloud);
            if (!var6 && !var5.equals("removed")) {
               this.screen.option("Post to Discord", "The bot posts it with its share code", this::confirmDiscordPost);
            }

            if (var6) {
               this.screen
                  .option(
                     "Stop posting to Discord",
                     "The bot deletes its post; the code keeps working",
                     () -> this.confirmCloudAction(
                        "discord-sharing", "Take “" + var1 + "” off Discord?\nThe bot deletes its post. Your share code keeps working.", "Take off Discord"
                     )
                  );
            }

            if (Set.of("private", "rejected").contains(var5)) {
               this.screen
                  .option(
                     "Submit to Discover",
                     "Send this revision for public review",
                     () -> this.confirmCloudAction("submit", "Submit to Discover?\nThis revision goes to public Marketplace review.", "Submit")
                  );
            }

            if (Set.of("submitted", "approved", "rejected").contains(var5)) {
               this.screen
                  .option(
                     "Withdraw Discover submission",
                     "Take it out of the public Marketplace",
                     () -> this.confirmCloudAction(
                        "withdraw", "Withdraw this Discover submission?\nIt leaves the public Marketplace. Your share code keeps working.", "Withdraw"
                     )
                  );
            }

            this.screen.addSeparator();
            this.screen
               .option(
                  "Delete cloud config",
                  "Remove it and revoke its share code",
                  () -> this.confirmCloudAction("delete", "Delete “" + var1 + "” from the cloud?\nIts share code is revoked. This cannot be undone.", "Delete")
               );
         } else {
            this.screen.setDialogText(var1, "Experimental storage · " + var2);
            this.screen
               .option(
                  "Download preview",
                  "Review the server copy before saving it",
                  () -> this.screen
                     .runRequest("Downloading...", this.screen.cloudApi.request("GET", this.profileEndpoint(), null, null), this::showImportPreview)
               );
            this.screen.option("Check revision", "Compare with the server's latest revision", () -> {
               String var1x = ApiResponseValidator.readId(this.selectedCloud, "revision");
               this.screen.runRequest("Checking revision...", this.screen.cloudApi.request("GET", this.profileEndpoint() + "/revision", null, null), var2x -> {
                  if (!var1x.equals(ApiResponseValidator.readId(var2x, "revision"))) {
                     this.screen.statusMessage = "Server revision changed. Review the refreshed item.";
                     this.refreshCloudItems(true);
                  } else {
                     this.screen.statusMessage = "Server revision is unchanged.";
                  }
               });
            });
            if (this.selectedLocal == null) {
               this.selectedLocal = this.screen.configManager.getActiveProfile();
            }

            this.screen
               .option(
                  "Replace server copy",
                  "From saved “" + this.selectedLocal + "” (choose it in My Configs)",
                  () -> this.screen
                     .showConfirm(
                        "Replace the server copy?\nThe experimental copy is replaced with saved local config “" + this.selectedLocal + "”.",
                        "Replace",
                        this::replaceServerCopy
                     )
               );
            this.screen.addSeparator();
            this.screen
               .option(
                  "Delete experimental profile",
                  "Remove the server copy",
                  () -> this.screen
                     .addTextField(
                        "Delete this experimental profile?\nThe server copy is removed. This cannot be undone.",
                        "Delete",
                        () -> this.screen
                           .runRequest(
                              "Deleting...",
                              this.screen.cloudApi.request("DELETE", this.profileEndpoint(), null, ApiResponseValidator.readId(this.selectedCloud, "revision")),
                              var1x -> {
                                 requireDeleted(var1x);
                                 this.refreshCloudItems(false);
                              }
                           )
                     )
               );
         }
      }
   }

   private String profileEndpoint() {
      return "/api/profiles/" + ApiResponseValidator.readId(this.selectedCloud, "id");
   }

   private void replaceServerCopy() {
      try {
         JsonObject var1 = ProfileValidator.exportProfile(
            this.selectedLocal, this.screen.configManager.readProfile(this.selectedLocal), this.screen.getModules()
         );
         var1.addProperty("revision", ApiResponseValidator.readId(this.selectedCloud, "revision"));
         this.screen.runRequest("Replacing server copy...", this.screen.cloudApi.request("PUT", this.profileEndpoint(), var1, null), var1x -> {
            ApiResponseValidator.readId(var1x, "revision");
            this.refreshCloudItems(false);
         });
      } catch (IOException var2) {
         LarpLog.warn("Config", "Config operation failed: {}", var2.toString());
         this.screen.statusMessage = "Could not read local profile.";
         this.screen.build();
      } catch (IllegalArgumentException var3) {
         LarpLog.warn("Config", "Replacement of {} refused before sending: {}", this.selectedLocal, var3.getMessage());
         this.screen.statusMessage = var3.getMessage() == null ? "This profile cannot be shared as it is." : var3.getMessage();
         this.screen.build();
      }
   }

   private CompletableFuture<JsonObject> requestCloudAction(JsonObject var1, String var2, Boolean var3) {
      JsonObject var4 = new JsonObject();
      var4.addProperty("action", var2);
      var4.addProperty("revision", ApiResponseValidator.readId(var1, "revision"));
      if (var3 != null) {
         var4.addProperty("discordSharing", var3);
      }

      return this.screen.cloudApi.api.request("POST", "/api/configs/" + ApiResponseValidator.readId(var1, "id") + "/actions", var4, true, null);
   }

   private void confirmDiscordPost() {
      this.screen
         .showConfirm(
            "Post “"
               + ProfileValidator.readString(this.selectedCloud, "name", 32)
               + "” in the Discord server?\nThe bot posts it with its share code within a minute, so anyone in the server can import it.",
            "Post",
            () -> this.screen.runRequest("Turning on Discord posting...", this.requestCloudAction(this.selectedCloud, "discord-sharing", true), var1 -> {
               ApiResponseValidator.readId(var1, "revision");
               this.screen.statusMessage = "Done. The bot posts it in Discord within a minute.";
               LarpLog.info("Config", "Discord posting enabled for shared config");
               this.refreshCloudItems(true);
            })
         );
   }

   private void confirmCloudAction(String var1, String var2, String var3) {
      Runnable var4 = () -> this.screen
         .runRequest("Updating cloud config...", this.requestCloudAction(this.selectedCloud, var1, var1.equals("discord-sharing") ? false : null), var2x -> {
            if (var1.equals("delete")) {
               requireDeleted(var2x);
            } else {
               ApiResponseValidator.readShareCode(var2x);
               ApiResponseValidator.marketplaceStateLabel(var2x);
               ApiResponseValidator.readId(var2x, "revision");
            }

            if (var1.equals("regenerate")) {
               this.screen.shareCode = ApiResponseValidator.readShareCode(var2x);
            }

            if (var1.equals("discord-sharing")) {
               this.screen.statusMessage = "Taken off Discord. The bot removes its post within a minute.";
               LarpLog.info("Config", "Discord posting turned off for shared config");
            }

            this.refreshCloudItems(var1.equals("discord-sharing"));
         });
      if (var1.equals("delete")) {
         this.screen.addTextField(var2, var3, var4);
      } else {
         this.screen.showConfirm(var2, var3, var4);
      }
   }

   private static void requireDeleted(JsonObject var0) {
      if (!ProfileValidator.readBoolean(var0.get("deleted"))) {
         throw ApiException.invalidResponse();
      }
   }

   void refreshCloudItems(boolean var1) {
      String var2 = this.screen.statusMessage;
      String var3 = this.selectedCloud == null ? "" : ApiResponseValidator.readId(this.selectedCloud, "id");
      this.screen
         .runRequest(
            "Loading cloud items...",
            this.experimental ? this.screen.cloudApi.fetchProfiles() : this.screen.cloudApi.api.get("/api/configs/mine?limit=100", true),
            var4 -> {
               this.cloudItems = ApiResponseValidator.validateItems(var4, this.experimental);
               this.selectedCloud = this.cloudItems.stream().filter(var1xx -> ApiResponseValidator.readId(var1xx, "id").equals(var3)).findFirst().orElse(null);
               this.screen.statusMessage = var1 ? var2 : "Cloud items refreshed";
            }
         );
   }

   void importByCode(String var1) {
      this.screen.runRequest("Importing...", this.screen.cloudApi.api.get("/api/configs/import/" + var1, false), this::showImportPreview);
   }

   private void showImportPreview(JsonObject var1) {
      try {
         this.importPreview = ProfileValidator.parseProfile(var1, this.screen.getModules());
         this.importName = this.importPreview.name();
         this.screen.scrollOffset = 0;
         BugReportBuilder.logEvent("Config", BugReportBuilder.Event.IMPORT_PREVIEW_OPENED);
         this.screen.statusMessage = "Review before saving locally";
      } catch (IllegalArgumentException var3) {
         BugReportBuilder.logEvent("Config", BugReportBuilder.Event.IMPORT_REJECTED);
         LarpLog.warn("Config", "Import rejected: {}", var3.getMessage());
         this.screen.statusMessage = var3.getMessage();
      }
   }

   void confirmSave(boolean var1) {
      ProfileValidator.validateName(this.importName);
      if (this.screen.configManager.hasProfile(this.importName) && !var1) {
         this.screen
            .showConfirm(
               "Replace local “" + this.importName + "”?\nYour live settings stay unchanged until you load it.", "Replace", () -> this.confirmSave(true)
            );
      } else {
         try {
            this.screen.configManager.deleteProfile(this.importName, this.importPreview.config(), var1, this.screen.getModules());
            String var2 = this.importName;
            this.importPreview = null;
            this.screen.importing = (boolean)0;
            this.selectedLocal = var2;
            this.screen.showConfirmWithCancelLabel("Saved as “" + var2 + "”\nLoad it now and replace your live settings?", "Load", () -> {
               this.screen.statusMessage = this.screen.configManager.loadProfile(var2) ? "Loaded local profile" : "Load failed. Config remains saved locally.";
               this.screen.build();
            }, "Not now");
         } catch (IOException var3) {
            LarpLog.warn("Config", "Config operation failed: {}", var3.toString());
            this.screen.statusMessage = "Could not save locally. Choose a different profile name and retry.";
            this.screen.build();
         }
      }
   }
}
