package com.larpclient;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

final class am {
   private final fk c;
   String f;
   String b = "";
   String x = "";
   List<JsonObject> l = List.of();
   JsonObject U;
   _4.y H;
   boolean E;

   am(fk var1) {
      this.c = var1;
      this.f = null;
   }

   void f() {
      this.U = null;
      this.H = null;
   }

   void B() {
      this.U = null;
      this.l = List.of();
   }

   sk t() {
      return this.c.F("LARP-XXXXXXXXXXXXXXXX", this.x, 32, var1 -> this.x = var1);
   }

   void w() {
      this.c.L("Import preview", this.H.name());
      this.c
         .text(
            "Larp "
               + this.H.larpVersion()
               + " · Minecraft "
               + this.H.minecraftVersion()
               + "\n"
               + this.H.modules()
               + " modules, "
               + this.H.enabled()
               + " enabled"
         );
      this.c.text("Supported settings load. Unavailable settings stay saved. Saving does not change your active config.");
      this.c.F("Local config name", this.b, 32, var1 -> this.b = var1);
      this.c.i("Cancel", () -> {
         this.H = null;
         this.c.W = (boolean)0;
         this.c.build();
      });
      this.c.i("Save locally", () -> this.E(false)).d = (boolean)1;
   }

   void b() {
      String var1 = this.c.Pt.toLowerCase(Locale.ROOT);

      for (String var3 : this.c.D.n()) {
         if (var3.toLowerCase(Locale.ROOT).contains(var1)) {
            this.c.A(var3, var3.equals(this.c.D.S()) ? "Active" : "Local", this.U == null && var3.equals(this.f), () -> {
               this.U = null;
               this.f = var3;
               this.c.L = 0;
               this.c.build();
            });
            boolean var4 = var3.equals(this.c.D.S());
            this.c.n(var4 ? "Update" : "Load", () -> {
               if (var4) {
                  this.E(var3);
               } else {
                  this.e(var3);
               }
            });
            this.c.n("Share", () -> this.a(var3));
            if (!var4) {
               this.c.n("Delete", () -> this.s(var3));
            }
         }
      }

      for (JsonObject var6 : this.l) {
         String var7 = _4.m(var6, "name", 32);
         if (var7.toLowerCase(Locale.ROOT).contains(var1)) {
            this.c.A(var7, this.E ? "Experimental storage" : "Shared · " + st.o(var6), this.U == var6, () -> {
               this.U = var6;
               this.c.L = 0;
               this.c.build();
            });
         }
      }
   }

   void H() {
      if (this.U != null) {
         this.C();
      } else if (this.f != null) {
         String var1 = this.f;
         boolean var2 = var1.equals(this.c.D.S());
         Optional var3 = this.l.stream().filter(var1x -> _4.m(var1x, "name", 32).equals(var1)).findFirst();
         String var4 = var2 ? "Active config" : "Saved config";
         if (var3.isPresent()) {
            var4 = var4 + (this.E ? " · Experimental copy" : " · Shared, " + st.o((JsonObject)var3.get()).toLowerCase(Locale.ROOT));
         }

         this.c.L(var1, var4);
         if (var3.isPresent() && !this.E && !st.l((JsonObject)var3.get()).isEmpty()) {
            this.c.text(st.l((JsonObject)var3.get()));
         }

         if (!this.c.P0.isEmpty()) {
            this.c.option("Copy share code", this.c.P0, () -> {
               this.c.J().keyboardHandler.setClipboard(this.c.P0);
               this.c.w = "Share code copied to clipboard.";
            });
         }

         this.c.option("Load", "Replace your live settings with this config", () -> this.e(var1));
         this.c.option("Save changes", "Overwrite it with your current settings", () -> this.E(var1));
         if (this.c.Pp.H() && this.E) {
            this.c
               .option(
                  "Upload experimental copy",
                  "Send the saved file to experimental storage",
                  () -> this.c.e("Upload an experimental copy?\nSaved “" + var1 + "” goes to experimental storage.", "Upload", () -> this.U(var1, true))
               );
         }

         this.c.option("Share", "Upload it and copy a share code", () -> this.a(var1));
         this.c.option("Duplicate", "Save a copy under a new name", () -> this.c.A("Duplicate", var1 + " copy"));
         this.c.option("Rename", "Change its name", () -> this.c.A("Rename", var1));
         if (!var2) {
            this.c.O();
            this.c.option("Delete", "Remove it from this computer", () -> this.s(var1));
         }
      }
   }

   private void e(String var1) {
      this.c.e("Load “" + var1 + "”?\nYour live settings are replaced with this config.", "Load", () -> {
         this.c.w = this.c.D.y(var1) ? "Loaded " + var1 : "Load failed. Existing profile retained.";
         this.c.build();
      });
   }

   private void E(String var1) {
      this.c.e("Save changes to “" + var1 + "”?\nThe saved config is replaced with your current settings.", "Save", () -> {
         this.c.T.b();

         try {
            this.c.D.s(var1, true);
            this.c.w = "Saved " + var1;
         } catch (IOException var3) {
            _q.R("Config", "Save failed: {}", var3.toString());
            this.c.w = "Save failed: " + var3.getMessage();
         }

         this.c.build();
      });
   }

   private void s(String var1) {
      this.c.F("Delete “" + var1 + "”?\nThe saved file is removed from this computer. This cannot be undone.", "Delete", () -> {
         try {
            this.c.D.v(var1);
            this.f = null;
            this.c.w = "Deleted";
         } catch (IOException var3) {
            _q.R("Config", "Delete failed: {}", var3.toString());
            this.c.w = "Delete failed: " + var3.getMessage();
         }

         this.c.build();
      });
   }

   private void a(String var1) {
      this.c.P0 = "";
      this.c
         .O(
            "Share “"
               + var1
               + "”\nIt gets uploaded to larpclient.com and the code is copied to your clipboard. Only people with the code can open it.\nPost to Discord and the bot also puts it in the Discord server, code included, within a minute.",
            "Share",
            () -> this.M(var1, false, false),
            "Share and post to Discord",
            () -> this.M(var1, false, true)
         );
   }

   private void U(String var1, boolean var2) {
      this.M(var1, var2, false);
   }

   private void M(String var1, boolean var2, boolean var3) {
      try {
         JsonObject var4 = _4.D(var1, this.c.D.m(var1), this.c.R());
         if (var3) {
            var4.addProperty("discordSharing", true);
         }

         this.c.P0 = "";
         this.c
            .H(
               "Uploading to larpclient.com...",
               var2 ? this.c.Pp.e("POST", "/api/profiles", var4, null) : this.c.Pp.E.e("POST", "/api/configs", var4, true, null),
               var3x -> {
                  st.f(var3x, "id");
                  st.f(var3x, "revision");
                  if (var2) {
                     this.c.w = "Experimental profile uploaded";
                  } else {
                     this.c.P0 = st.W(var3x);
                     byte var4x = 1;

                     try {
                        this.c.J().keyboardHandler.setClipboard(this.c.P0);
                        this.c.w = "Uploaded successfully. Share code copied to clipboard.";
                        _q.z("Config", "Saved config uploaded; share code copied to clipboard");
                     } catch (RuntimeException var6x) {
                        var4x = 0;
                        this.c.w = "Uploaded successfully. Clipboard copy failed. Use Copy share code to retry.";
                        _q.R("Config", "Upload succeeded but clipboard copy failed: {}", var6x.toString());
                     }

                     if (var3) {
                        this.c.w = (var4x != 0 ? "Code copied. " : "Clipboard copy failed, use Copy share code. ")
                           + "The bot posts it in Discord within a minute.";
                        _q.z("Config", "Shared config opted in to Discord posting");
                        this.V(true);
                     }
                  }
               }
            );
      } catch (IOException var5) {
         _q.R("Config", "Config operation failed: {}", var5.toString());
         this.c.w = "Could not read the saved profile. Save it locally first.";
         this.c.build();
      } catch (IllegalArgumentException var6) {
         _q.R("Config", "Upload of {} refused before sending: {}", var1, var6.getMessage());
         this.c.w = var6.getMessage() == null ? "This profile cannot be shared as it is." : var6.getMessage();
         this.c.build();
      }
   }

   void C() {
      if (this.E && !this.c.Pp.H()) {
         this.c.L("Experimental storage", "");
         this.c.text("Experimental access is unavailable for this session.");
      } else if (this.U != null) {
         String var1 = _4.m(this.U, "name", 32);
         String var2 = "Updated " + _4.m(this.U, "updatedAt", 40);
         if (!this.E) {
            this.c.L(var1, st.o(this.U) + " · " + var2);
            String var3 = st.l(this.U);
            if (!var3.isEmpty()) {
               this.c.text(var3);
            }

            String var4 = _4.m(this.U, "rejectionReason", 500);
            if (!var4.isBlank()) {
               this.c.text("Review reason: " + var4);
            }

            this.c.option("Copy share code", st.W(this.U), () -> {
               this.c.J().keyboardHandler.setClipboard(st.W(this.U));
               this.c.w = "Copied";
            });
            this.c.option("Import / download", "Preview it before saving locally", () -> this.y(st.W(this.U)));
            this.c
               .option(
                  "Regenerate share code",
                  "Replace the code; the old one stops working",
                  () -> this.D("regenerate", "Regenerate the share code?\nThe old code stops working immediately.", "Regenerate")
               );
            String var5 = this.U.get("marketplaceState").getAsString();
            boolean var6 = st.b(this.U);
            if (!var6 && !var5.equals("removed")) {
               this.c.option("Post to Discord", "The bot posts it with its share code", this::P);
            }

            if (var6) {
               this.c
                  .option(
                     "Stop posting to Discord",
                     "The bot deletes its post; the code keeps working",
                     () -> this.D(
                        "discord-sharing", "Take “" + var1 + "” off Discord?\nThe bot deletes its post. Your share code keeps working.", "Take off Discord"
                     )
                  );
            }

            if (Set.of("private", "rejected").contains(var5)) {
               this.c
                  .option(
                     "Submit to Discover",
                     "Send this revision for public review",
                     () -> this.D("submit", "Submit to Discover?\nThis revision goes to public Marketplace review.", "Submit")
                  );
            }

            if (Set.of("submitted", "approved", "rejected").contains(var5)) {
               this.c
                  .option(
                     "Withdraw Discover submission",
                     "Take it out of the public Marketplace",
                     () -> this.D(
                        "withdraw", "Withdraw this Discover submission?\nIt leaves the public Marketplace. Your share code keeps working.", "Withdraw"
                     )
                  );
            }

            this.c.O();
            this.c
               .option(
                  "Delete cloud config",
                  "Remove it and revoke its share code",
                  () -> this.D("delete", "Delete “" + var1 + "” from the cloud?\nIts share code is revoked. This cannot be undone.", "Delete")
               );
         } else {
            this.c.L(var1, "Experimental storage · " + var2);
            this.c
               .option(
                  "Download preview",
                  "Review the server copy before saving it",
                  () -> this.c.H("Downloading...", this.c.Pp.e("GET", this.I(), null, null), this::X)
               );
            this.c.option("Check revision", "Compare with the server's latest revision", () -> {
               String var1x = st.f(this.U, "revision");
               this.c.H("Checking revision...", this.c.Pp.e("GET", this.I() + "/revision", null, null), var2x -> {
                  if (!var1x.equals(st.f(var2x, "revision"))) {
                     this.c.w = "Server revision changed. Review the refreshed item.";
                     this.V(true);
                  } else {
                     this.c.w = "Server revision is unchanged.";
                  }
               });
            });
            if (this.f == null) {
               this.f = this.c.D.S();
            }

            this.c
               .option(
                  "Replace server copy",
                  "From saved “" + this.f + "” (choose it in My Configs)",
                  () -> this.c.e("Replace the server copy?\nThe experimental copy is replaced with saved local config “" + this.f + "”.", "Replace", this::i)
               );
            this.c.O();
            this.c
               .option(
                  "Delete experimental profile",
                  "Remove the server copy",
                  () -> this.c
                     .F(
                        "Delete this experimental profile?\nThe server copy is removed. This cannot be undone.",
                        "Delete",
                        () -> this.c.H("Deleting...", this.c.Pp.e("DELETE", this.I(), null, st.f(this.U, "revision")), var1x -> {
                           k(var1x);
                           this.V(false);
                        })
                     )
               );
         }
      }
   }

   private String I() {
      return "/api/profiles/" + st.f(this.U, "id");
   }

   private void i() {
      try {
         JsonObject var1 = _4.D(this.f, this.c.D.m(this.f), this.c.R());
         var1.addProperty("revision", st.f(this.U, "revision"));
         this.c.H("Replacing server copy...", this.c.Pp.e("PUT", this.I(), var1, null), var1x -> {
            st.f(var1x, "revision");
            this.V(false);
         });
      } catch (IOException var2) {
         _q.R("Config", "Config operation failed: {}", var2.toString());
         this.c.w = "Could not read local profile.";
         this.c.build();
      } catch (IllegalArgumentException var3) {
         _q.R("Config", "Replacement of {} refused before sending: {}", this.f, var3.getMessage());
         this.c.w = var3.getMessage() == null ? "This profile cannot be shared as it is." : var3.getMessage();
         this.c.build();
      }
   }

   private CompletableFuture<JsonObject> B(JsonObject var1, String var2, Boolean var3) {
      JsonObject var4 = new JsonObject();
      var4.addProperty("action", var2);
      var4.addProperty("revision", st.f(var1, "revision"));
      if (var3 != null) {
         var4.addProperty("discordSharing", var3);
      }

      return this.c.Pp.E.e("POST", "/api/configs/" + st.f(var1, "id") + "/actions", var4, true, null);
   }

   private void P() {
      this.c
         .e(
            "Post “"
               + _4.m(this.U, "name", 32)
               + "” in the Discord server?\nThe bot posts it with its share code within a minute, so anyone in the server can import it.",
            "Post",
            () -> this.c.H("Turning on Discord posting...", this.B(this.U, "discord-sharing", true), var1 -> {
               st.f(var1, "revision");
               this.c.w = "Done. The bot posts it in Discord within a minute.";
               _q.z("Config", "Discord posting enabled for shared config");
               this.V(true);
            })
         );
   }

   private void D(String var1, String var2, String var3) {
      Runnable var4 = () -> this.c.H("Updating cloud config...", this.B(this.U, var1, var1.equals("discord-sharing") ? false : null), var2x -> {
         if (var1.equals("delete")) {
            k(var2x);
         } else {
            st.W(var2x);
            st.o(var2x);
            st.f(var2x, "revision");
         }

         if (var1.equals("regenerate")) {
            this.c.P0 = st.W(var2x);
         }

         if (var1.equals("discord-sharing")) {
            this.c.w = "Taken off Discord. The bot removes its post within a minute.";
            _q.z("Config", "Discord posting turned off for shared config");
         }

         this.V(var1.equals("discord-sharing"));
      });
      if (var1.equals("delete")) {
         this.c.F(var2, var3, var4);
      } else {
         this.c.e(var2, var3, var4);
      }
   }

   private static void k(JsonObject var0) {
      if (!_4.Y(var0.get("deleted"))) {
         throw wr.O();
      }
   }

   void V(boolean var1) {
      String var2 = this.c.w;
      String var3 = this.U == null ? "" : st.f(this.U, "id");
      this.c.H("Loading cloud items...", this.E ? this.c.Pp.c() : this.c.Pp.E.x("/api/configs/mine?limit=100", true), var4 -> {
         this.l = st.G(var4, this.E);
         this.U = this.l.stream().filter(var1xx -> st.f(var1xx, "id").equals(var3)).findFirst().orElse(null);
         this.c.w = var1 ? var2 : "Cloud items refreshed";
      });
   }

   void y(String var1) {
      this.c.H("Importing...", this.c.Pp.E.x("/api/configs/import/" + var1, false), this::X);
   }

   private void X(JsonObject var1) {
      try {
         this.H = _4.A(var1, this.c.R());
         this.b = this.H.name();
         this.c.L = 0;
         i0.W("Config", i0.p.T);
         this.c.w = "Review before saving locally";
      } catch (IllegalArgumentException var3) {
         i0.W("Config", i0.p.p);
         _q.R("Config", "Import rejected: {}", var3.getMessage());
         this.c.w = var3.getMessage();
      }
   }

   void E(boolean var1) {
      _4.p(this.b);
      if (this.c.D.g(this.b) && !var1) {
         this.c.e("Replace local “" + this.b + "”?\nYour live settings stay unchanged until you load it.", "Replace", () -> this.E(true));
      } else {
         try {
            this.c.D.v(this.b, this.H.config(), var1, this.c.R());
            String var2 = this.b;
            this.H = null;
            this.c.W = (boolean)0;
            this.f = var2;
            this.c.E("Saved as “" + var2 + "”\nLoad it now and replace your live settings?", "Load", () -> {
               this.c.w = this.c.D.y(var2) ? "Loaded local profile" : "Load failed. Config remains saved locally.";
               this.c.build();
            }, "Not now");
         } catch (IOException var3) {
            _q.R("Config", "Config operation failed: {}", var3.toString());
            this.c.w = "Could not save locally. Choose a different profile name and retry.";
            this.c.build();
         }
      }
   }
}
