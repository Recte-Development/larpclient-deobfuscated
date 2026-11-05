package com.larpclient;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

final class _p {
   private final pz C;
   String e;
   String d = "";
   String T = "";
   List<JsonObject> O = List.of();
   JsonObject E;
   yj.j L;
   boolean D;

   _p(pz var1) {
      this.C = var1;
      this.e = null;
   }

   void W() {
      this.E = null;
      this.L = null;
   }

   void Z() {
      this.E = null;
      this.O = List.of();
   }

   p_ M() {
      return this.C.k("LARP-XXXXXXXXXXXXXXXX", this.T, 32, var1 -> this.T = var1);
   }

   void X() {
      this.C.t("Import preview", this.L.name());
      this.C
         .text(
            "Larp "
               + this.L.larpVersion()
               + " · Minecraft "
               + this.L.minecraftVersion()
               + "\n"
               + this.L.modules()
               + " modules, "
               + this.L.enabled()
               + " enabled"
         );
      this.C.text("Supported settings load. Unavailable settings stay saved. Saving does not change your active config.");
      this.C.k("Local config name", this.d, 32, var1 -> this.d = var1);
      this.C.V("Cancel", () -> {
         this.L = null;
         this.C.da = (boolean)0;
         this.C.build();
      });
      this.C.V("Save locally", () -> this.U(false)).d = (boolean)1;
   }

   void C() {
      String var1 = this.C.dy.toLowerCase(Locale.ROOT);

      for (String var3 : this.C.dQ.I()) {
         if (var3.toLowerCase(Locale.ROOT).contains(var1)) {
            this.C.f(var3, var3.equals(this.C.dQ.o()) ? "Active" : "Local", this.E == null && var3.equals(this.e), () -> {
               this.E = null;
               this.e = var3;
               this.C.e = 0;
               this.C.build();
            });
            boolean var4 = var3.equals(this.C.dQ.o());
            this.C.R(var4 ? "Update" : "Load", () -> {
               if (var4) {
                  this.V(var3);
               } else {
                  this.D(var3);
               }
            });
            this.C.R("Share", () -> this.k(var3));
            if (!var4) {
               this.C.R("Delete", () -> this.y(var3));
            }
         }
      }

      for (JsonObject var6 : this.O) {
         String var7 = yj.y(var6, "name", 32);
         if (var7.toLowerCase(Locale.ROOT).contains(var1)) {
            this.C.f(var7, this.D ? "Experimental storage" : "Shared · " + _4.P(var6), this.E == var6, () -> {
               this.E = var6;
               this.C.e = 0;
               this.C.build();
            });
         }
      }
   }

   void e() {
      if (this.E != null) {
         this.B();
      } else if (this.e != null) {
         String var1 = this.e;
         boolean var2 = var1.equals(this.C.dQ.o());
         Optional var3 = this.O.stream().filter(var1x -> yj.y(var1x, "name", 32).equals(var1)).findFirst();
         String var4 = var2 ? "Active config" : "Saved config";
         if (var3.isPresent()) {
            var4 = var4 + (this.D ? " · Experimental copy" : " · Shared, " + _4.P((JsonObject)var3.get()).toLowerCase(Locale.ROOT));
         }

         this.C.t(var1, var4);
         if (var3.isPresent() && !this.D && !_4.M((JsonObject)var3.get()).isEmpty()) {
            this.C.text(_4.M((JsonObject)var3.get()));
         }

         if (!this.C.dI.isEmpty()) {
            this.C.option("Copy share code", this.C.dI, () -> {
               this.C.S().keyboardHandler.setClipboard(this.C.dI);
               this.C.V = "Share code copied to clipboard.";
            });
         }

         this.C.option("Load", "Replace your live settings with this config", () -> this.D(var1));
         this.C.option("Save changes", "Overwrite it with your current settings", () -> this.V(var1));
         if (this.C.K.r() && this.D) {
            this.C
               .option(
                  "Upload experimental copy",
                  "Send the saved file to experimental storage",
                  () -> this.C.J("Upload an experimental copy?\nSaved “" + var1 + "” goes to experimental storage.", "Upload", () -> this.A(var1, true))
               );
         }

         this.C.option("Share", "Upload it and copy a share code", () -> this.k(var1));
         this.C.option("Duplicate", "Save a copy under a new name", () -> this.C.a("Duplicate", var1 + " copy"));
         this.C.option("Rename", "Change its name", () -> this.C.a("Rename", var1));
         if (!var2) {
            this.C.F();
            this.C.option("Delete", "Remove it from this computer", () -> this.y(var1));
         }
      }
   }

   private void D(String var1) {
      this.C.J("Load “" + var1 + "”?\nYour live settings are replaced with this config.", "Load", () -> {
         this.C.V = this.C.dQ.V(var1) ? "Loaded " + var1 : "Load failed. Existing profile retained.";
         this.C.build();
      });
   }

   private void V(String var1) {
      this.C.J("Save changes to “" + var1 + "”?\nThe saved config is replaced with your current settings.", "Save", () -> {
         this.C.x.i();

         try {
            this.C.dQ.H(var1, true);
            this.C.V = "Saved " + var1;
         } catch (IOException var3) {
            r4.m("Config", "Save failed: {}", var3.toString());
            this.C.V = "Save failed: " + var3.getMessage();
         }

         this.C.build();
      });
   }

   private void y(String var1) {
      this.C.L("Delete “" + var1 + "”?\nThe saved file is removed from this computer. This cannot be undone.", "Delete", () -> {
         try {
            this.C.dQ.P(var1);
            this.e = null;
            this.C.V = "Deleted";
         } catch (IOException var3) {
            r4.m("Config", "Delete failed: {}", var3.toString());
            this.C.V = "Delete failed: " + var3.getMessage();
         }

         this.C.build();
      });
   }

   private void k(String var1) {
      this.C.dI = "";
      this.C
         .c(
            "Share “"
               + var1
               + "”\nIt gets uploaded to larpclient.com and the code is copied to your clipboard. Only people with the code can open it.\nPost to Discord and the bot also puts it in the Discord server, code included, within a minute.",
            "Share",
            () -> this.q(var1, false, false),
            "Share and post to Discord",
            () -> this.q(var1, false, true)
         );
   }

   private void A(String var1, boolean var2) {
      this.q(var1, var2, false);
   }

   private void q(String var1, boolean var2, boolean var3) {
      try {
         JsonObject var4 = yj.J(var1, this.C.dQ.K(var1), this.C.W());
         if (var3) {
            var4.addProperty("discordSharing", true);
         }

         this.C.dI = "";
         this.C
            .t(
               "Uploading to larpclient.com...",
               var2 ? this.C.K.D("POST", "/api/profiles", var4, null) : this.C.K.f.M("POST", "/api/configs", var4, true, null),
               var3x -> {
                  _4.Y(var3x, "id");
                  _4.Y(var3x, "revision");
                  if (var2) {
                     this.C.V = "Experimental profile uploaded";
                  } else {
                     this.C.dI = _4.g(var3x);
                     byte var4x = 1;

                     try {
                        this.C.S().keyboardHandler.setClipboard(this.C.dI);
                        this.C.V = "Uploaded successfully. Share code copied to clipboard.";
                        r4.V("Config", "Saved config uploaded; share code copied to clipboard");
                     } catch (RuntimeException var6) {
                        var4x = 0;
                        this.C.V = "Uploaded successfully. Clipboard copy failed. Use Copy share code to retry.";
                        r4.m("Config", "Upload succeeded but clipboard copy failed: {}", var6.toString());
                     }

                     if (var3) {
                        this.C.V = (var4x != 0 ? "Code copied. " : "Clipboard copy failed, use Copy share code. ")
                           + "The bot posts it in Discord within a minute.";
                        r4.V("Config", "Shared config opted in to Discord posting");
                        this.w(true);
                     }
                  }
               }
            );
      } catch (IOException var5) {
         r4.m("Config", "Config operation failed: {}", var5.toString());
         this.C.V = "Could not read the saved profile. Save it locally first.";
         this.C.build();
      }
   }

   void B() {
      if (this.D && !this.C.K.r()) {
         this.C.t("Experimental storage", "");
         this.C.text("Experimental access is unavailable for this session.");
      } else if (this.E != null) {
         String var1 = yj.y(this.E, "name", 32);
         String var2 = "Updated " + yj.y(this.E, "updatedAt", 40);
         if (!this.D) {
            this.C.t(var1, _4.P(this.E) + " · " + var2);
            String var3 = _4.M(this.E);
            if (!var3.isEmpty()) {
               this.C.text(var3);
            }

            String var4 = yj.y(this.E, "rejectionReason", 500);
            if (!var4.isBlank()) {
               this.C.text("Review reason: " + var4);
            }

            this.C.option("Copy share code", _4.g(this.E), () -> {
               this.C.S().keyboardHandler.setClipboard(_4.g(this.E));
               this.C.V = "Copied";
            });
            this.C.option("Import / download", "Preview it before saving locally", () -> this.W(_4.g(this.E)));
            this.C
               .option(
                  "Regenerate share code",
                  "Replace the code; the old one stops working",
                  () -> this.h("regenerate", "Regenerate the share code?\nThe old code stops working immediately.", "Regenerate")
               );
            String var5 = this.E.get("marketplaceState").getAsString();
            boolean var6 = _4.F(this.E);
            if (!var6 && !var5.equals("removed")) {
               this.C.option("Post to Discord", "The bot posts it with its share code", this::i);
            }

            if (var6) {
               this.C
                  .option(
                     "Stop posting to Discord",
                     "The bot deletes its post; the code keeps working",
                     () -> this.h(
                        "discord-sharing", "Take “" + var1 + "” off Discord?\nThe bot deletes its post. Your share code keeps working.", "Take off Discord"
                     )
                  );
            }

            if (Set.of("private", "rejected").contains(var5)) {
               this.C
                  .option(
                     "Submit to Discover",
                     "Send this revision for public review",
                     () -> this.h("submit", "Submit to Discover?\nThis revision goes to public Marketplace review.", "Submit")
                  );
            }

            if (Set.of("submitted", "approved", "rejected").contains(var5)) {
               this.C
                  .option(
                     "Withdraw Discover submission",
                     "Take it out of the public Marketplace",
                     () -> this.h(
                        "withdraw", "Withdraw this Discover submission?\nIt leaves the public Marketplace. Your share code keeps working.", "Withdraw"
                     )
                  );
            }

            this.C.F();
            this.C
               .option(
                  "Delete cloud config",
                  "Remove it and revoke its share code",
                  () -> this.h("delete", "Delete “" + var1 + "” from the cloud?\nIts share code is revoked. This cannot be undone.", "Delete")
               );
         } else {
            this.C.t(var1, "Experimental storage · " + var2);
            this.C
               .option(
                  "Download preview",
                  "Review the server copy before saving it",
                  () -> this.C.t("Downloading...", this.C.K.D("GET", this.Y(), null, null), this::K)
               );
            this.C.option("Check revision", "Compare with the server's latest revision", () -> {
               String var1x = _4.Y(this.E, "revision");
               this.C.t("Checking revision...", this.C.K.D("GET", this.Y() + "/revision", null, null), var2x -> {
                  if (!var1x.equals(_4.Y(var2x, "revision"))) {
                     this.C.V = "Server revision changed. Review the refreshed item.";
                     this.w(true);
                  } else {
                     this.C.V = "Server revision is unchanged.";
                  }
               });
            });
            if (this.e == null) {
               this.e = this.C.dQ.o();
            }

            this.C
               .option(
                  "Replace server copy",
                  "From saved “" + this.e + "” (choose it in My Configs)",
                  () -> this.C.J("Replace the server copy?\nThe experimental copy is replaced with saved local config “" + this.e + "”.", "Replace", this::d)
               );
            this.C.F();
            this.C
               .option(
                  "Delete experimental profile",
                  "Remove the server copy",
                  () -> this.C
                     .L(
                        "Delete this experimental profile?\nThe server copy is removed. This cannot be undone.",
                        "Delete",
                        () -> this.C.t("Deleting...", this.C.K.D("DELETE", this.Y(), null, _4.Y(this.E, "revision")), var1x -> {
                           e(var1x);
                           this.w(false);
                        })
                     )
               );
         }
      }
   }

   private String Y() {
      return "/api/profiles/" + _4.Y(this.E, "id");
   }

   private void d() {
      try {
         JsonObject var1 = yj.J(this.e, this.C.dQ.K(this.e), this.C.W());
         var1.addProperty("revision", _4.Y(this.E, "revision"));
         this.C.t("Replacing server copy...", this.C.K.D("PUT", this.Y(), var1, null), var1x -> {
            _4.Y(var1x, "revision");
            this.w(false);
         });
      } catch (IOException var2) {
         r4.m("Config", "Config operation failed: {}", var2.toString());
         this.C.V = "Could not read local profile.";
         this.C.build();
      }
   }

   private CompletableFuture<JsonObject> J(JsonObject var1, String var2, Boolean var3) {
      JsonObject var4 = new JsonObject();
      var4.addProperty("action", var2);
      var4.addProperty("revision", _4.Y(var1, "revision"));
      if (var3 != null) {
         var4.addProperty("discordSharing", var3);
      }

      return this.C.K.f.M("POST", "/api/configs/" + _4.Y(var1, "id") + "/actions", var4, true, null);
   }

   private void i() {
      this.C
         .J(
            "Post “"
               + yj.y(this.E, "name", 32)
               + "” in the Discord server?\nThe bot posts it with its share code within a minute, so anyone in the server can import it.",
            "Post",
            () -> this.C.t("Turning on Discord posting...", this.J(this.E, "discord-sharing", true), var1 -> {
               _4.Y(var1, "revision");
               this.C.V = "Done. The bot posts it in Discord within a minute.";
               r4.V("Config", "Discord posting enabled for shared config");
               this.w(true);
            })
         );
   }

   private void h(String var1, String var2, String var3) {
      Runnable var4 = () -> this.C.t("Updating cloud config...", this.J(this.E, var1, var1.equals("discord-sharing") ? false : null), var2x -> {
         if (var1.equals("delete")) {
            e(var2x);
         } else {
            _4.g(var2x);
            _4.P(var2x);
            _4.Y(var2x, "revision");
         }

         if (var1.equals("regenerate")) {
            this.C.dI = _4.g(var2x);
         }

         if (var1.equals("discord-sharing")) {
            this.C.V = "Taken off Discord. The bot removes its post within a minute.";
            r4.V("Config", "Discord posting turned off for shared config");
         }

         this.w(var1.equals("discord-sharing"));
      });
      if (var1.equals("delete")) {
         this.C.L(var2, var3, var4);
      } else {
         this.C.J(var2, var3, var4);
      }
   }

   private static void e(JsonObject var0) {
      if (!yj.V(var0.get("deleted"))) {
         throw oj.f();
      }
   }

   void w(boolean var1) {
      String var2 = this.C.V;
      String var3 = this.E == null ? "" : _4.Y(this.E, "id");
      this.C.t("Loading cloud items...", this.D ? this.C.K.j() : this.C.K.f.X("/api/configs/mine?limit=100", true), var4 -> {
         this.O = _4.H(var4, this.D);
         this.E = this.O.stream().filter(var1xx -> _4.Y(var1xx, "id").equals(var3)).findFirst().orElse(null);
         this.C.V = var1 ? var2 : "Cloud items refreshed";
      });
   }

   void W(String var1) {
      this.C.t("Importing...", this.C.K.f.X("/api/configs/import/" + var1, false), this::K);
   }

   private void K(JsonObject var1) {
      try {
         this.L = yj.S(var1, this.C.W());
         this.d = this.L.name();
         this.C.e = 0;
         lf.w("Config", lf.t.M);
         this.C.V = "Review before saving locally";
      } catch (IllegalArgumentException var3) {
         lf.w("Config", lf.t.i);
         r4.m("Config", "Import rejected: {}", var3.getMessage());
         this.C.V = var3.getMessage();
      }
   }

   void U(boolean var1) {
      yj.S(this.d);
      if (this.C.dQ.p(this.d) && !var1) {
         this.C.J("Replace local “" + this.d + "”?\nYour live settings stay unchanged until you load it.", "Replace", () -> this.U(true));
      } else {
         try {
            this.C.dQ.O(this.d, this.L.config(), var1, this.C.W());
            String var2 = this.d;
            this.L = null;
            this.C.da = (boolean)0;
            this.e = var2;
            this.C.B("Saved as “" + var2 + "”\nLoad it now and replace your live settings?", "Load", () -> {
               this.C.V = this.C.dQ.V(var2) ? "Loaded local profile" : "Load failed. Config remains saved locally.";
               this.C.build();
            }, "Not now");
         } catch (IOException var3) {
            r4.m("Config", "Config operation failed: {}", var3.toString());
            this.C.V = "Could not save locally. Choose a different profile name and retry.";
            this.C.build();
         }
      }
   }
}
