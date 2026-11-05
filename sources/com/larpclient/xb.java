package com.larpclient;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

final class xb {
   private final hx S;
   String E;
   String f = "";
   String X = "";
   List<JsonObject> T = List.of();
   JsonObject h;
   p_.x K;
   boolean R;

   xb(hx var1) {
      this.S = var1;
      this.E = null;
   }

   void a() {
      this.h = null;
      this.K = null;
   }

   void o() {
      this.h = null;
      this.T = List.of();
   }

   ur m() {
      return this.S.Q("LARP-XXXXXXXXXXXXXXXX", this.X, 32, var1 -> this.X = var1);
   }

   void I() {
      this.S.H("Import preview", this.K.name());
      this.S
         .text(
            "Larp "
               + this.K.larpVersion()
               + " · Minecraft "
               + this.K.minecraftVersion()
               + "\n"
               + this.K.modules()
               + " modules, "
               + this.K.enabled()
               + " enabled"
         );
      this.S.text("Supported settings load. Unavailable settings stay saved. Saving does not change your active config.");
      this.S.Q("Local config name", this.f, 32, var1 -> this.f = var1);
      this.S.k("Cancel", () -> {
         this.K = null;
         this.S.S = (boolean)0;
         this.S.build();
      });
      this.S.k("Save locally", () -> this.L(false)).w = (boolean)1;
   }

   void M() {
      String var1 = this.S.x.toLowerCase(Locale.ROOT);

      for (String var3 : this.S.vQ.H()) {
         if (var3.toLowerCase(Locale.ROOT).contains(var1)) {
            this.S.N(var3, var3.equals(this.S.vQ.w()) ? "Active" : "Local", this.h == null && var3.equals(this.E), () -> {
               this.h = null;
               this.E = var3;
               this.S.Z = 0;
               this.S.build();
            });
            boolean var4 = var3.equals(this.S.vQ.w());
            this.S.E(var4 ? "Update" : "Load", () -> {
               if (var4) {
                  this.g(var3);
               } else {
                  this.T(var3);
               }
            });
            this.S.E("Share", () -> this.m(var3));
            if (!var4) {
               this.S.E("Delete", () -> this.k(var3));
            }
         }
      }

      for (JsonObject var6 : this.T) {
         String var7 = p_.Q(var6, "name", 32);
         if (var7.toLowerCase(Locale.ROOT).contains(var1)) {
            this.S.N(var7, this.R ? "Experimental storage" : "Shared · " + md.U(var6), this.h == var6, () -> {
               this.h = var6;
               this.S.Z = 0;
               this.S.build();
            });
         }
      }
   }

   void V() {
      if (this.h != null) {
         this.T();
      } else if (this.E != null) {
         String var1 = this.E;
         boolean var2 = var1.equals(this.S.vQ.w());
         Optional var3 = this.T.stream().filter(var1x -> p_.Q(var1x, "name", 32).equals(var1)).findFirst();
         String var4 = var2 ? "Active config" : "Saved config";
         if (var3.isPresent()) {
            var4 = var4 + (this.R ? " · Experimental copy" : " · Shared, " + md.U((JsonObject)var3.get()).toLowerCase(Locale.ROOT));
         }

         this.S.H(var1, var4);
         if (var3.isPresent() && !this.R && !md.q((JsonObject)var3.get()).isEmpty()) {
            this.S.text(md.q((JsonObject)var3.get()));
         }

         if (!this.S.v1.isEmpty()) {
            this.S.option("Copy share code", this.S.v1, () -> {
               this.S.i().keyboardHandler.setClipboard(this.S.v1);
               this.S.A = "Share code copied to clipboard.";
            });
         }

         this.S.option("Load", "Replace your live settings with this config", () -> this.T(var1));
         this.S.option("Save changes", "Overwrite it with your current settings", () -> this.g(var1));
         if (this.S.h.R() && this.R) {
            this.S
               .option(
                  "Upload experimental copy",
                  "Send the saved file to experimental storage",
                  () -> this.S.f("Upload an experimental copy?\nSaved “" + var1 + "” goes to experimental storage.", "Upload", () -> this.x(var1, true))
               );
         }

         this.S.option("Share", "Upload it and copy a share code", () -> this.m(var1));
         this.S.option("Duplicate", "Save a copy under a new name", () -> this.S.r("Duplicate", var1 + " copy"));
         this.S.option("Rename", "Change its name", () -> this.S.r("Rename", var1));
         if (!var2) {
            this.S.r();
            this.S.option("Delete", "Remove it from this computer", () -> this.k(var1));
         }
      }
   }

   private void T(String var1) {
      this.S.f("Load “" + var1 + "”?\nYour live settings are replaced with this config.", "Load", () -> {
         this.S.A = this.S.vQ.X(var1) ? "Loaded " + var1 : "Load failed. Existing profile retained.";
         this.S.build();
      });
   }

   private void g(String var1) {
      this.S.f("Save changes to “" + var1 + "”?\nThe saved config is replaced with your current settings.", "Save", () -> {
         this.S.M.e();

         try {
            this.S.vQ.Z(var1, true);
            this.S.A = "Saved " + var1;
         } catch (IOException var3) {
            mt.T("Config", "Save failed: {}", var3.toString());
            this.S.A = "Save failed: " + var3.getMessage();
         }

         this.S.build();
      });
   }

   private void k(String var1) {
      this.S.l("Delete “" + var1 + "”?\nThe saved file is removed from this computer. This cannot be undone.", "Delete", () -> {
         try {
            this.S.vQ.h(var1);
            this.E = null;
            this.S.A = "Deleted";
         } catch (IOException var3) {
            mt.T("Config", "Delete failed: {}", var3.toString());
            this.S.A = "Delete failed: " + var3.getMessage();
         }

         this.S.build();
      });
   }

   private void m(String var1) {
      this.S.v1 = "";
      this.S
         .x(
            "Share “"
               + var1
               + "”\nIt gets uploaded to larpclient.com and the code is copied to your clipboard. Only people with the code can open it.\nPost to Discord and the bot also puts it in the Discord server, code included, within a minute.",
            "Share",
            () -> this.x(var1, false, false),
            "Share and post to Discord",
            () -> this.x(var1, false, true)
         );
   }

   private void x(String var1, boolean var2) {
      this.x(var1, var2, false);
   }

   private void x(String var1, boolean var2, boolean var3) {
      try {
         JsonObject var4 = p_.Z(var1, this.S.vQ.s(var1), this.S.i());
         if (var3) {
            var4.addProperty("discordSharing", true);
         }

         this.S.v1 = "";
         this.S
            .n(
               "Uploading to larpclient.com...",
               var2 ? this.S.h.x("POST", "/api/profiles", var4, null) : this.S.h.n.b("POST", "/api/configs", var4, true, null),
               var3x -> {
                  md.M(var3x, "id");
                  md.M(var3x, "revision");
                  if (var2) {
                     this.S.A = "Experimental profile uploaded";
                  } else {
                     this.S.v1 = md.a(var3x);
                     byte var4x = 1;

                     try {
                        this.S.i().keyboardHandler.setClipboard(this.S.v1);
                        this.S.A = "Uploaded successfully. Share code copied to clipboard.";
                        mt.K("Config", "Saved config uploaded; share code copied to clipboard");
                     } catch (RuntimeException var6) {
                        var4x = 0;
                        this.S.A = "Uploaded successfully. Clipboard copy failed. Use Copy share code to retry.";
                        mt.T("Config", "Upload succeeded but clipboard copy failed: {}", var6.toString());
                     }

                     if (var3) {
                        this.S.A = (var4x != 0 ? "Code copied. " : "Clipboard copy failed, use Copy share code. ")
                           + "The bot posts it in Discord within a minute.";
                        mt.K("Config", "Shared config opted in to Discord posting");
                        this.Q(true);
                     }
                  }
               }
            );
      } catch (IOException var5) {
         mt.T("Config", "Config operation failed: {}", var5.toString());
         this.S.A = "Could not read the saved profile. Save it locally first.";
         this.S.build();
      }
   }

   void T() {
      if (this.R && !this.S.h.R()) {
         this.S.H("Experimental storage", "");
         this.S.text("Experimental access is unavailable for this session.");
      } else if (this.h != null) {
         String var1 = p_.Q(this.h, "name", 32);
         String var2 = "Updated " + p_.Q(this.h, "updatedAt", 40);
         if (!this.R) {
            this.S.H(var1, md.U(this.h) + " · " + var2);
            String var3 = md.q(this.h);
            if (!var3.isEmpty()) {
               this.S.text(var3);
            }

            String var4 = p_.Q(this.h, "rejectionReason", 500);
            if (!var4.isBlank()) {
               this.S.text("Review reason: " + var4);
            }

            this.S.option("Copy share code", md.a(this.h), () -> {
               this.S.i().keyboardHandler.setClipboard(md.a(this.h));
               this.S.A = "Copied";
            });
            this.S.option("Import / download", "Preview it before saving locally", () -> this.S(md.a(this.h)));
            this.S
               .option(
                  "Regenerate share code",
                  "Replace the code; the old one stops working",
                  () -> this.h("regenerate", "Regenerate the share code?\nThe old code stops working immediately.", "Regenerate")
               );
            String var5 = this.h.get("marketplaceState").getAsString();
            boolean var6 = md.n(this.h);
            if (!var6 && !var5.equals("removed")) {
               this.S.option("Post to Discord", "The bot posts it with its share code", this::J);
            }

            if (var6) {
               this.S
                  .option(
                     "Stop posting to Discord",
                     "The bot deletes its post; the code keeps working",
                     () -> this.h(
                        "discord-sharing", "Take “" + var1 + "” off Discord?\nThe bot deletes its post. Your share code keeps working.", "Take off Discord"
                     )
                  );
            }

            if (Set.of("private", "rejected").contains(var5)) {
               this.S
                  .option(
                     "Submit to Discover",
                     "Send this revision for public review",
                     () -> this.h("submit", "Submit to Discover?\nThis revision goes to public Marketplace review.", "Submit")
                  );
            }

            if (Set.of("submitted", "approved", "rejected").contains(var5)) {
               this.S
                  .option(
                     "Withdraw Discover submission",
                     "Take it out of the public Marketplace",
                     () -> this.h(
                        "withdraw", "Withdraw this Discover submission?\nIt leaves the public Marketplace. Your share code keeps working.", "Withdraw"
                     )
                  );
            }

            this.S.r();
            this.S
               .option(
                  "Delete cloud config",
                  "Remove it and revoke its share code",
                  () -> this.h("delete", "Delete “" + var1 + "” from the cloud?\nIts share code is revoked. This cannot be undone.", "Delete")
               );
         } else {
            this.S.H(var1, "Experimental storage · " + var2);
            this.S
               .option(
                  "Download preview",
                  "Review the server copy before saving it",
                  () -> this.S.n("Downloading...", this.S.h.x("GET", this.K(), null, null), this::x)
               );
            this.S.option("Check revision", "Compare with the server's latest revision", () -> {
               String var1x = md.M(this.h, "revision");
               this.S.n("Checking revision...", this.S.h.x("GET", this.K() + "/revision", null, null), var2x -> {
                  if (!var1x.equals(md.M(var2x, "revision"))) {
                     this.S.A = "Server revision changed. Review the refreshed item.";
                     this.Q(true);
                  } else {
                     this.S.A = "Server revision is unchanged.";
                  }
               });
            });
            if (this.E == null) {
               this.E = this.S.vQ.w();
            }

            this.S
               .option(
                  "Replace server copy",
                  "From saved “" + this.E + "” (choose it in My Configs)",
                  () -> this.S.f("Replace the server copy?\nThe experimental copy is replaced with saved local config “" + this.E + "”.", "Replace", this::b)
               );
            this.S.r();
            this.S
               .option(
                  "Delete experimental profile",
                  "Remove the server copy",
                  () -> this.S
                     .l(
                        "Delete this experimental profile?\nThe server copy is removed. This cannot be undone.",
                        "Delete",
                        () -> this.S.n("Deleting...", this.S.h.x("DELETE", this.K(), null, md.M(this.h, "revision")), var1x -> {
                           l(var1x);
                           this.Q(false);
                        })
                     )
               );
         }
      }
   }

   private String K() {
      return "/api/profiles/" + md.M(this.h, "id");
   }

   private void b() {
      try {
         JsonObject var1 = p_.Z(this.E, this.S.vQ.s(this.E), this.S.i());
         var1.addProperty("revision", md.M(this.h, "revision"));
         this.S.n("Replacing server copy...", this.S.h.x("PUT", this.K(), var1, null), var1x -> {
            md.M(var1x, "revision");
            this.Q(false);
         });
      } catch (IOException var2) {
         mt.T("Config", "Config operation failed: {}", var2.toString());
         this.S.A = "Could not read local profile.";
         this.S.build();
      }
   }

   private CompletableFuture<JsonObject> B(JsonObject var1, String var2, Boolean var3) {
      JsonObject var4 = new JsonObject();
      var4.addProperty("action", var2);
      var4.addProperty("revision", md.M(var1, "revision"));
      if (var3 != null) {
         var4.addProperty("discordSharing", var3);
      }

      return this.S.h.n.b("POST", "/api/configs/" + md.M(var1, "id") + "/actions", var4, true, null);
   }

   private void J() {
      this.S
         .f(
            "Post “"
               + p_.Q(this.h, "name", 32)
               + "” in the Discord server?\nThe bot posts it with its share code within a minute, so anyone in the server can import it.",
            "Post",
            () -> this.S.n("Turning on Discord posting...", this.B(this.h, "discord-sharing", true), var1 -> {
               md.M(var1, "revision");
               this.S.A = "Done. The bot posts it in Discord within a minute.";
               mt.K("Config", "Discord posting enabled for shared config");
               this.Q(true);
            })
         );
   }

   private void h(String var1, String var2, String var3) {
      Runnable var4 = () -> this.S.n("Updating cloud config...", this.B(this.h, var1, var1.equals("discord-sharing") ? false : null), var2x -> {
         if (var1.equals("delete")) {
            l(var2x);
         } else {
            md.a(var2x);
            md.U(var2x);
            md.M(var2x, "revision");
         }

         if (var1.equals("regenerate")) {
            this.S.v1 = md.a(var2x);
         }

         if (var1.equals("discord-sharing")) {
            this.S.A = "Taken off Discord. The bot removes its post within a minute.";
            mt.K("Config", "Discord posting turned off for shared config");
         }

         this.Q(var1.equals("discord-sharing"));
      });
      if (var1.equals("delete")) {
         this.S.l(var2, var3, var4);
      } else {
         this.S.f(var2, var3, var4);
      }
   }

   private static void l(JsonObject var0) {
      if (!p_.v(var0.get("deleted"))) {
         throw pe.e();
      }
   }

   void Q(boolean var1) {
      String var2 = this.S.A;
      String var3 = this.h == null ? "" : md.M(this.h, "id");
      this.S.n("Loading cloud items...", this.R ? this.S.h.C() : this.S.h.n.F("/api/configs/mine?limit=100", true), var4 -> {
         this.T = md.B(var4, this.R);
         this.h = this.T.stream().filter(var1xx -> md.M(var1xx, "id").equals(var3)).findFirst().orElse(null);
         this.S.A = var1 ? var2 : "Cloud items refreshed";
      });
   }

   void S(String var1) {
      this.S.n("Importing...", this.S.h.n.F("/api/configs/import/" + var1, false), this::x);
   }

   private void x(JsonObject var1) {
      try {
         this.K = p_.g(var1, this.S.i());
         this.f = this.K.name();
         this.S.Z = 0;
         wz.e("Config", wz.j.h);
         this.S.A = "Review before saving locally";
      } catch (IllegalArgumentException var3) {
         wz.e("Config", wz.j.D);
         mt.T("Config", "Import rejected: {}", var3.getMessage());
         this.S.A = var3.getMessage();
      }
   }

   void L(boolean var1) {
      p_.g(this.f);
      if (this.S.vQ.Q(this.f) && !var1) {
         this.S.f("Replace local “" + this.f + "”?\nYour live settings stay unchanged until you load it.", "Replace", () -> this.L(true));
      } else {
         try {
            this.S.vQ.w(this.f, this.K.config(), var1, this.S.i());
            String var2 = this.f;
            this.K = null;
            this.S.S = (boolean)0;
            this.E = var2;
            this.S.z("Saved as “" + var2 + "”\nLoad it now and replace your live settings?", "Load", () -> {
               this.S.A = this.S.vQ.X(var2) ? "Loaded local profile" : "Load failed. Config remains saved locally.";
               this.S.build();
            }, "Not now");
         } catch (IOException var3) {
            mt.T("Config", "Config operation failed: {}", var3.toString());
            this.S.A = "Could not save locally. Choose a different profile name and retry.";
            this.S.build();
         }
      }
   }
}
