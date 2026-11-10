package com.larpclient;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

final class gq {
   private final hd N;
   String j;
   String O = "";
   String p = "";
   List<JsonObject> W = List.of();
   JsonObject z;
   km.m n;
   boolean q;

   gq(hd var1) {
      this.N = var1;
      this.j = null;
   }

   void Z() {
      this.z = null;
      this.n = null;
   }

   void r() {
      this.z = null;
      this.W = List.of();
   }

   j9 v() {
      return this.N.s("LARP-XXXXXXXXXXXXXXXX", this.p, 32, var1 -> this.p = var1);
   }

   void z() {
      this.N.b("Import preview", this.n.name());
      this.N
         .text(
            "Larp "
               + this.n.larpVersion()
               + " · Minecraft "
               + this.n.minecraftVersion()
               + "\n"
               + this.n.modules()
               + " modules, "
               + this.n.enabled()
               + " enabled"
         );
      this.N.text("Supported settings load. Unavailable settings stay saved. Saving does not change your active config.");
      this.N.s("Local config name", this.O, 32, var1 -> this.O = var1);
      this.N.k("Cancel", () -> {
         this.n = null;
         this.N.c = (boolean)0;
         this.N.build();
      });
      this.N.k("Save locally", () -> this.h(false)).x = (boolean)1;
   }

   void I() {
      String var1 = this.N.e.toLowerCase(Locale.ROOT);

      for (String var3 : this.N.n.B()) {
         if (var3.toLowerCase(Locale.ROOT).contains(var1)) {
            this.N.E(var3, var3.equals(this.N.n.M()) ? "Active" : "Local", this.z == null && var3.equals(this.j), () -> {
               this.z = null;
               this.j = var3;
               this.N.Y = 0;
               this.N.build();
            });
            boolean var4 = var3.equals(this.N.n.M());
            this.N.Y(var4 ? "Update" : "Load", () -> {
               if (var4) {
                  this.E(var3);
               } else {
                  this.P(var3);
               }
            });
            this.N.Y("Share", () -> this.y(var3));
            if (!var4) {
               this.N.Y("Delete", () -> this.m(var3));
            }
         }
      }

      for (JsonObject var6 : this.W) {
         String var7 = km.a(var6, "name", 32);
         if (var7.toLowerCase(Locale.ROOT).contains(var1)) {
            this.N.E(var7, this.q ? "Experimental storage" : "Shared · " + ut.G(var6), this.z == var6, () -> {
               this.z = var6;
               this.N.Y = 0;
               this.N.build();
            });
         }
      }
   }

   void U() {
      if (this.z != null) {
         this.c();
      } else if (this.j != null) {
         String var1 = this.j;
         boolean var2 = var1.equals(this.N.n.M());
         Optional var3 = this.W.stream().filter(var1x -> km.a(var1x, "name", 32).equals(var1)).findFirst();
         String var4 = var2 ? "Active config" : "Saved config";
         if (var3.isPresent()) {
            var4 = var4 + (this.q ? " · Experimental copy" : " · Shared, " + ut.G((JsonObject)var3.get()).toLowerCase(Locale.ROOT));
         }

         this.N.b(var1, var4);
         if (var3.isPresent() && !this.q && !ut.D((JsonObject)var3.get()).isEmpty()) {
            this.N.text(ut.D((JsonObject)var3.get()));
         }

         if (!this.N.P.isEmpty()) {
            this.N.option("Copy share code", this.N.P, () -> {
               this.N.b().keyboardHandler.setClipboard(this.N.P);
               this.N.Gr = "Share code copied to clipboard.";
            });
         }

         this.N.option("Load", "Replace your live settings with this config", () -> this.P(var1));
         this.N.option("Save changes", "Overwrite it with your current settings", () -> this.E(var1));
         if (this.N.GQ.A() && this.q) {
            this.N
               .option(
                  "Upload experimental copy",
                  "Send the saved file to experimental storage",
                  () -> this.N.U("Upload an experimental copy?\nSaved “" + var1 + "” goes to experimental storage.", "Upload", () -> this.D(var1, true))
               );
         }

         this.N.option("Share", "Upload it and copy a share code", () -> this.y(var1));
         this.N.option("Duplicate", "Save a copy under a new name", () -> this.N.H("Duplicate", var1 + " copy"));
         this.N.option("Rename", "Change its name", () -> this.N.H("Rename", var1));
         if (!var2) {
            this.N.R();
            this.N.option("Delete", "Remove it from this computer", () -> this.m(var1));
         }
      }
   }

   private void P(String var1) {
      this.N.U("Load “" + var1 + "”?\nYour live settings are replaced with this config.", "Load", () -> {
         this.N.Gr = this.N.n.T(var1) ? "Loaded " + var1 : "Load failed. Existing profile retained.";
         this.N.build();
      });
   }

   private void E(String var1) {
      this.N.U("Save changes to “" + var1 + "”?\nThe saved config is replaced with your current settings.", "Save", () -> {
         this.N.O.r();

         try {
            this.N.n.M(var1, true);
            this.N.Gr = "Saved " + var1;
         } catch (IOException var3) {
            h.g("Config", "Save failed: {}", var3.toString());
            this.N.Gr = "Save failed: " + var3.getMessage();
         }

         this.N.build();
      });
   }

   private void m(String var1) {
      this.N.I("Delete “" + var1 + "”?\nThe saved file is removed from this computer. This cannot be undone.", "Delete", () -> {
         try {
            this.N.n.S(var1);
            this.j = null;
            this.N.Gr = "Deleted";
         } catch (IOException var3) {
            h.g("Config", "Delete failed: {}", var3.toString());
            this.N.Gr = "Delete failed: " + var3.getMessage();
         }

         this.N.build();
      });
   }

   private void y(String var1) {
      this.N.P = "";
      this.N
         .M(
            "Share “"
               + var1
               + "”\nIt gets uploaded to larpclient.com and the code is copied to your clipboard. Only people with the code can open it.\nPost to Discord and the bot also puts it in the Discord server, code included, within a minute.",
            "Share",
            () -> this.s(var1, false, false),
            "Share and post to Discord",
            () -> this.s(var1, false, true)
         );
   }

   private void D(String var1, boolean var2) {
      this.s(var1, var2, false);
   }

   private void s(String var1, boolean var2, boolean var3) {
      try {
         JsonObject var4 = km.e(var1, this.N.n.Z(var1), this.N.f());
         if (var3) {
            var4.addProperty("discordSharing", true);
         }

         this.N.P = "";
         this.N
            .o(
               "Uploading to larpclient.com...",
               var2 ? this.N.GQ.v("POST", "/api/profiles", var4, null) : this.N.GQ.h.D("POST", "/api/configs", var4, true, null),
               var3x -> {
                  ut.A(var3x, "id");
                  ut.A(var3x, "revision");
                  if (var2) {
                     this.N.Gr = "Experimental profile uploaded";
                  } else {
                     this.N.P = ut.o(var3x);
                     byte var4x = 1;

                     try {
                        this.N.b().keyboardHandler.setClipboard(this.N.P);
                        this.N.Gr = "Uploaded successfully. Share code copied to clipboard.";
                        h.A("Config", "Saved config uploaded; share code copied to clipboard");
                     } catch (RuntimeException var6x) {
                        var4x = 0;
                        this.N.Gr = "Uploaded successfully. Clipboard copy failed. Use Copy share code to retry.";
                        h.g("Config", "Upload succeeded but clipboard copy failed: {}", var6x.toString());
                     }

                     if (var3) {
                        this.N.Gr = (var4x != 0 ? "Code copied. " : "Clipboard copy failed, use Copy share code. ")
                           + "The bot posts it in Discord within a minute.";
                        h.A("Config", "Shared config opted in to Discord posting");
                        this.r(true);
                     }
                  }
               }
            );
      } catch (IOException var5) {
         h.g("Config", "Config operation failed: {}", var5.toString());
         this.N.Gr = "Could not read the saved profile. Save it locally first.";
         this.N.build();
      } catch (IllegalArgumentException var6) {
         h.g("Config", "Upload of {} refused before sending: {}", var1, var6.getMessage());
         this.N.Gr = var6.getMessage() == null ? "This profile cannot be shared as it is." : var6.getMessage();
         this.N.build();
      }
   }

   void c() {
      if (this.q && !this.N.GQ.A()) {
         this.N.b("Experimental storage", "");
         this.N.text("Experimental access is unavailable for this session.");
      } else if (this.z != null) {
         String var1 = km.a(this.z, "name", 32);
         String var2 = "Updated " + km.a(this.z, "updatedAt", 40);
         if (!this.q) {
            this.N.b(var1, ut.G(this.z) + " · " + var2);
            String var3 = ut.D(this.z);
            if (!var3.isEmpty()) {
               this.N.text(var3);
            }

            String var4 = km.a(this.z, "rejectionReason", 500);
            if (!var4.isBlank()) {
               this.N.text("Review reason: " + var4);
            }

            this.N.option("Copy share code", ut.o(this.z), () -> {
               this.N.b().keyboardHandler.setClipboard(ut.o(this.z));
               this.N.Gr = "Copied";
            });
            this.N.option("Import / download", "Preview it before saving locally", () -> this.g(ut.o(this.z)));
            this.N
               .option(
                  "Regenerate share code",
                  "Replace the code; the old one stops working",
                  () -> this.I("regenerate", "Regenerate the share code?\nThe old code stops working immediately.", "Regenerate")
               );
            String var5 = this.z.get("marketplaceState").getAsString();
            boolean var6 = ut.Z(this.z);
            if (!var6 && !var5.equals("removed")) {
               this.N.option("Post to Discord", "The bot posts it with its share code", this::l);
            }

            if (var6) {
               this.N
                  .option(
                     "Stop posting to Discord",
                     "The bot deletes its post; the code keeps working",
                     () -> this.I(
                        "discord-sharing", "Take “" + var1 + "” off Discord?\nThe bot deletes its post. Your share code keeps working.", "Take off Discord"
                     )
                  );
            }

            if (Set.of("private", "rejected").contains(var5)) {
               this.N
                  .option(
                     "Submit to Discover",
                     "Send this revision for public review",
                     () -> this.I("submit", "Submit to Discover?\nThis revision goes to public Marketplace review.", "Submit")
                  );
            }

            if (Set.of("submitted", "approved", "rejected").contains(var5)) {
               this.N
                  .option(
                     "Withdraw Discover submission",
                     "Take it out of the public Marketplace",
                     () -> this.I(
                        "withdraw", "Withdraw this Discover submission?\nIt leaves the public Marketplace. Your share code keeps working.", "Withdraw"
                     )
                  );
            }

            this.N.R();
            this.N
               .option(
                  "Delete cloud config",
                  "Remove it and revoke its share code",
                  () -> this.I("delete", "Delete “" + var1 + "” from the cloud?\nIts share code is revoked. This cannot be undone.", "Delete")
               );
         } else {
            this.N.b(var1, "Experimental storage · " + var2);
            this.N
               .option(
                  "Download preview",
                  "Review the server copy before saving it",
                  () -> this.N.o("Downloading...", this.N.GQ.v("GET", this.J(), null, null), this::V)
               );
            this.N.option("Check revision", "Compare with the server's latest revision", () -> {
               String var1x = ut.A(this.z, "revision");
               this.N.o("Checking revision...", this.N.GQ.v("GET", this.J() + "/revision", null, null), var2x -> {
                  if (!var1x.equals(ut.A(var2x, "revision"))) {
                     this.N.Gr = "Server revision changed. Review the refreshed item.";
                     this.r(true);
                  } else {
                     this.N.Gr = "Server revision is unchanged.";
                  }
               });
            });
            if (this.j == null) {
               this.j = this.N.n.M();
            }

            this.N
               .option(
                  "Replace server copy",
                  "From saved “" + this.j + "” (choose it in My Configs)",
                  () -> this.N.U("Replace the server copy?\nThe experimental copy is replaced with saved local config “" + this.j + "”.", "Replace", this::G)
               );
            this.N.R();
            this.N
               .option(
                  "Delete experimental profile",
                  "Remove the server copy",
                  () -> this.N
                     .I(
                        "Delete this experimental profile?\nThe server copy is removed. This cannot be undone.",
                        "Delete",
                        () -> this.N.o("Deleting...", this.N.GQ.v("DELETE", this.J(), null, ut.A(this.z, "revision")), var1x -> {
                           G(var1x);
                           this.r(false);
                        })
                     )
               );
         }
      }
   }

   private String J() {
      return "/api/profiles/" + ut.A(this.z, "id");
   }

   private void G() {
      try {
         JsonObject var1 = km.e(this.j, this.N.n.Z(this.j), this.N.f());
         var1.addProperty("revision", ut.A(this.z, "revision"));
         this.N.o("Replacing server copy...", this.N.GQ.v("PUT", this.J(), var1, null), var1x -> {
            ut.A(var1x, "revision");
            this.r(false);
         });
      } catch (IOException var2) {
         h.g("Config", "Config operation failed: {}", var2.toString());
         this.N.Gr = "Could not read local profile.";
         this.N.build();
      } catch (IllegalArgumentException var3) {
         h.g("Config", "Replacement of {} refused before sending: {}", this.j, var3.getMessage());
         this.N.Gr = var3.getMessage() == null ? "This profile cannot be shared as it is." : var3.getMessage();
         this.N.build();
      }
   }

   private CompletableFuture<JsonObject> Y(JsonObject var1, String var2, Boolean var3) {
      JsonObject var4 = new JsonObject();
      var4.addProperty("action", var2);
      var4.addProperty("revision", ut.A(var1, "revision"));
      if (var3 != null) {
         var4.addProperty("discordSharing", var3);
      }

      return this.N.GQ.h.D("POST", "/api/configs/" + ut.A(var1, "id") + "/actions", var4, true, null);
   }

   private void l() {
      this.N
         .U(
            "Post “"
               + km.a(this.z, "name", 32)
               + "” in the Discord server?\nThe bot posts it with its share code within a minute, so anyone in the server can import it.",
            "Post",
            () -> this.N.o("Turning on Discord posting...", this.Y(this.z, "discord-sharing", true), var1 -> {
               ut.A(var1, "revision");
               this.N.Gr = "Done. The bot posts it in Discord within a minute.";
               h.A("Config", "Discord posting enabled for shared config");
               this.r(true);
            })
         );
   }

   private void I(String var1, String var2, String var3) {
      Runnable var4 = () -> this.N.o("Updating cloud config...", this.Y(this.z, var1, var1.equals("discord-sharing") ? false : null), var2x -> {
         if (var1.equals("delete")) {
            G(var2x);
         } else {
            ut.o(var2x);
            ut.G(var2x);
            ut.A(var2x, "revision");
         }

         if (var1.equals("regenerate")) {
            this.N.P = ut.o(var2x);
         }

         if (var1.equals("discord-sharing")) {
            this.N.Gr = "Taken off Discord. The bot removes its post within a minute.";
            h.A("Config", "Discord posting turned off for shared config");
         }

         this.r(var1.equals("discord-sharing"));
      });
      if (var1.equals("delete")) {
         this.N.I(var2, var3, var4);
      } else {
         this.N.U(var2, var3, var4);
      }
   }

   private static void G(JsonObject var0) {
      if (!km.t(var0.get("deleted"))) {
         throw bh.o();
      }
   }

   void r(boolean var1) {
      String var2 = this.N.Gr;
      String var3 = this.z == null ? "" : ut.A(this.z, "id");
      this.N.o("Loading cloud items...", this.q ? this.N.GQ.A() : this.N.GQ.h.Y("/api/configs/mine?limit=100", true), var4 -> {
         this.W = ut.G(var4, this.q);
         this.z = this.W.stream().filter(var1xx -> ut.A(var1xx, "id").equals(var3)).findFirst().orElse(null);
         this.N.Gr = var1 ? var2 : "Cloud items refreshed";
      });
   }

   void g(String var1) {
      this.N.o("Importing...", this.N.GQ.h.Y("/api/configs/import/" + var1, false), this::V);
   }

   private void V(JsonObject var1) {
      try {
         this.n = km.s(var1, this.N.f());
         this.O = this.n.name();
         this.N.Y = 0;
         lo.C("Config", lo.z.g);
         this.N.Gr = "Review before saving locally";
      } catch (IllegalArgumentException var3) {
         lo.C("Config", lo.z.S);
         h.g("Config", "Import rejected: {}", var3.getMessage());
         this.N.Gr = var3.getMessage();
      }
   }

   void h(boolean var1) {
      km.r(this.O);
      if (this.N.n.m(this.O) && !var1) {
         this.N.U("Replace local “" + this.O + "”?\nYour live settings stay unchanged until you load it.", "Replace", () -> this.h(true));
      } else {
         try {
            this.N.n.X(this.O, this.n.config(), var1, this.N.f());
            String var2 = this.O;
            this.n = null;
            this.N.c = (boolean)0;
            this.j = var2;
            this.N.J("Saved as “" + var2 + "”\nLoad it now and replace your live settings?", "Load", () -> {
               this.N.Gr = this.N.n.T(var2) ? "Loaded local profile" : "Load failed. Config remains saved locally.";
               this.N.build();
            }, "Not now");
         } catch (IOException var3) {
            h.g("Config", "Config operation failed: {}", var3.toString());
            this.N.Gr = "Could not save locally. Choose a different profile name and retry.";
            this.N.build();
         }
      }
   }
}
