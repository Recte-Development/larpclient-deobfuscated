package com.larpclient;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

final class s_ {
   private final tf a;
   String q;
   String E = "";
   String n = "";
   List<JsonObject> w = List.of();
   JsonObject M;
   h.s g;
   boolean y;

   s_(tf var1) {
      this.a = var1;
      this.q = null;
   }

   void C() {
      this.M = null;
      this.g = null;
   }

   void P() {
      this.M = null;
      this.w = List.of();
   }

   lo J() {
      return this.a.w("LARP-XXXXXXXXXXXXXXXX", this.n, 32, var1 -> this.n = var1);
   }

   void Q() {
      this.a.m("Import preview", this.g.name());
      this.a
         .text(
            "Larp "
               + this.g.larpVersion()
               + " · Minecraft "
               + this.g.minecraftVersion()
               + "\n"
               + this.g.modules()
               + " modules, "
               + this.g.enabled()
               + " enabled"
         );
      this.a.text("Supported settings load. Unavailable settings stay saved. Saving does not change your active config.");
      this.a.w("Local config name", this.E, 32, var1 -> this.E = var1);
      this.a.f("Cancel", () -> {
         this.g = null;
         this.a.JT = (boolean)0;
         this.a.build();
      });
      this.a.f("Save locally", () -> this.A(false)).v = (boolean)1;
   }

   void t() {
      String var1 = this.a.U.toLowerCase(Locale.ROOT);

      for (String var3 : this.a.L.h()) {
         if (var3.toLowerCase(Locale.ROOT).contains(var1)) {
            this.a.I(var3, var3.equals(this.a.L.d()) ? "Active" : "Local", this.M == null && var3.equals(this.q), () -> {
               this.M = null;
               this.q = var3;
               this.a.f = 0;
               this.a.build();
            });
            boolean var4 = var3.equals(this.a.L.d());
            this.a.y(var4 ? "Update" : "Load", () -> {
               if (var4) {
                  this.K(var3);
               } else {
                  this.p(var3);
               }
            });
            this.a.y("Share", () -> this.X(var3));
            if (!var4) {
               this.a.y("Delete", () -> this.M(var3));
            }
         }
      }

      for (JsonObject var6 : this.w) {
         String var7 = h.f(var6, "name", 32);
         if (var7.toLowerCase(Locale.ROOT).contains(var1)) {
            this.a.I(var7, this.y ? "Experimental storage" : "Shared · " + t4.V(var6), this.M == var6, () -> {
               this.M = var6;
               this.a.f = 0;
               this.a.build();
            });
         }
      }
   }

   void Y() {
      if (this.M != null) {
         this.H();
      } else if (this.q != null) {
         String var1 = this.q;
         boolean var2 = var1.equals(this.a.L.d());
         Optional var3 = this.w.stream().filter(var1x -> h.f(var1x, "name", 32).equals(var1)).findFirst();
         String var4 = var2 ? "Active config" : "Saved config";
         if (var3.isPresent()) {
            var4 = var4 + (this.y ? " · Experimental copy" : " · Shared, " + t4.V((JsonObject)var3.get()).toLowerCase(Locale.ROOT));
         }

         this.a.m(var1, var4);
         if (var3.isPresent() && !this.y && !t4.u((JsonObject)var3.get()).isEmpty()) {
            this.a.text(t4.u((JsonObject)var3.get()));
         }

         if (!this.a.Jr.isEmpty()) {
            this.a.option("Copy share code", this.a.Jr, () -> {
               this.a.Z().field_1774.method_1455(this.a.Jr);
               this.a.JK = "Share code copied to clipboard.";
            });
         }

         this.a.option("Load", "Replace your live settings with this config", () -> this.p(var1));
         this.a.option("Save changes", "Overwrite it with your current settings", () -> this.K(var1));
         if (this.a.Jd.C() && this.y) {
            this.a
               .option(
                  "Upload experimental copy",
                  "Send the saved file to experimental storage",
                  () -> this.a.s("Upload an experimental copy?\nSaved “" + var1 + "” goes to experimental storage.", "Upload", () -> this.o(var1, true))
               );
         }

         this.a.option("Share", "Upload it and copy a share code", () -> this.X(var1));
         this.a.option("Duplicate", "Save a copy under a new name", () -> this.a.V("Duplicate", var1 + " copy"));
         this.a.option("Rename", "Change its name", () -> this.a.V("Rename", var1));
         if (!var2) {
            this.a.A();
            this.a.option("Delete", "Remove it from this computer", () -> this.M(var1));
         }
      }
   }

   private void p(String var1) {
      this.a.s("Load “" + var1 + "”?\nYour live settings are replaced with this config.", "Load", () -> {
         this.a.JK = this.a.L.G(var1) ? "Loaded " + var1 : "Load failed. Existing profile retained.";
         this.a.build();
      });
   }

   private void K(String var1) {
      this.a.s("Save changes to “" + var1 + "”?\nThe saved config is replaced with your current settings.", "Save", () -> {
         this.a.X.D();

         try {
            this.a.L.f(var1, true);
            this.a.JK = "Saved " + var1;
         } catch (IOException var3) {
            q8.L("Config", "Save failed: {}", var3.toString());
            this.a.JK = "Save failed: " + var3.getMessage();
         }

         this.a.build();
      });
   }

   private void M(String var1) {
      this.a.W("Delete “" + var1 + "”?\nThe saved file is removed from this computer. This cannot be undone.", "Delete", () -> {
         try {
            this.a.L.E(var1);
            this.q = null;
            this.a.JK = "Deleted";
         } catch (IOException var3) {
            q8.L("Config", "Delete failed: {}", var3.toString());
            this.a.JK = "Delete failed: " + var3.getMessage();
         }

         this.a.build();
      });
   }

   private void X(String var1) {
      this.a.Jr = "";
      this.a
         .V(
            "Share “"
               + var1
               + "”\nIt gets uploaded to larpclient.com and the code is copied to your clipboard. Only people with the code can open it.\nPost to Discord and the bot also puts it in the Discord server, code included, within a minute.",
            "Share",
            () -> this.K(var1, false, false),
            "Share and post to Discord",
            () -> this.K(var1, false, true)
         );
   }

   private void o(String var1, boolean var2) {
      this.K(var1, var2, false);
   }

   private void K(String var1, boolean var2, boolean var3) {
      try {
         JsonObject var4 = h.D(var1, this.a.L.Y(var1), this.a.P());
         if (var3) {
            var4.addProperty("discordSharing", true);
         }

         this.a.Jr = "";
         this.a
            .y(
               "Uploading to larpclient.com...",
               var2 ? this.a.Jd.u("POST", "/api/profiles", var4, null) : this.a.Jd.q.E("POST", "/api/configs", var4, true, null),
               var3x -> {
                  t4.a(var3x, "id");
                  t4.a(var3x, "revision");
                  if (var2) {
                     this.a.JK = "Experimental profile uploaded";
                  } else {
                     this.a.Jr = t4.F(var3x);
                     byte var4x = 1;

                     try {
                        this.a.Z().field_1774.method_1455(this.a.Jr);
                        this.a.JK = "Uploaded successfully. Share code copied to clipboard.";
                        q8.G("Config", "Saved config uploaded; share code copied to clipboard");
                     } catch (RuntimeException var6x) {
                        var4x = 0;
                        this.a.JK = "Uploaded successfully. Clipboard copy failed. Use Copy share code to retry.";
                        q8.L("Config", "Upload succeeded but clipboard copy failed: {}", var6x.toString());
                     }

                     if (var3) {
                        this.a.JK = (var4x != 0 ? "Code copied. " : "Clipboard copy failed, use Copy share code. ")
                           + "The bot posts it in Discord within a minute.";
                        q8.G("Config", "Shared config opted in to Discord posting");
                        this.c(true);
                     }
                  }
               }
            );
      } catch (IOException var5) {
         q8.L("Config", "Config operation failed: {}", var5.toString());
         this.a.JK = "Could not read the saved profile. Save it locally first.";
         this.a.build();
      } catch (IllegalArgumentException var6) {
         q8.L("Config", "Upload of {} refused before sending: {}", var1, var6.getMessage());
         this.a.JK = var6.getMessage() == null ? "This profile cannot be shared as it is." : var6.getMessage();
         this.a.build();
      }
   }

   void H() {
      if (this.y && !this.a.Jd.C()) {
         this.a.m("Experimental storage", "");
         this.a.text("Experimental access is unavailable for this session.");
      } else if (this.M != null) {
         String var1 = h.f(this.M, "name", 32);
         String var2 = "Updated " + h.f(this.M, "updatedAt", 40);
         if (!this.y) {
            this.a.m(var1, t4.V(this.M) + " · " + var2);
            String var3 = t4.u(this.M);
            if (!var3.isEmpty()) {
               this.a.text(var3);
            }

            String var4 = h.f(this.M, "rejectionReason", 500);
            if (!var4.isBlank()) {
               this.a.text("Review reason: " + var4);
            }

            this.a.option("Copy share code", t4.F(this.M), () -> {
               this.a.Z().field_1774.method_1455(t4.F(this.M));
               this.a.JK = "Copied";
            });
            this.a.option("Import / download", "Preview it before saving locally", () -> this.u(t4.F(this.M)));
            this.a
               .option(
                  "Regenerate share code",
                  "Replace the code; the old one stops working",
                  () -> this.C("regenerate", "Regenerate the share code?\nThe old code stops working immediately.", "Regenerate")
               );
            String var5 = this.M.get("marketplaceState").getAsString();
            boolean var6 = t4.S(this.M);
            if (!var6 && !var5.equals("removed")) {
               this.a.option("Post to Discord", "The bot posts it with its share code", this::S);
            }

            if (var6) {
               this.a
                  .option(
                     "Stop posting to Discord",
                     "The bot deletes its post; the code keeps working",
                     () -> this.C(
                        "discord-sharing", "Take “" + var1 + "” off Discord?\nThe bot deletes its post. Your share code keeps working.", "Take off Discord"
                     )
                  );
            }

            if (Set.of("private", "rejected").contains(var5)) {
               this.a
                  .option(
                     "Submit to Discover",
                     "Send this revision for public review",
                     () -> this.C("submit", "Submit to Discover?\nThis revision goes to public Marketplace review.", "Submit")
                  );
            }

            if (Set.of("submitted", "approved", "rejected").contains(var5)) {
               this.a
                  .option(
                     "Withdraw Discover submission",
                     "Take it out of the public Marketplace",
                     () -> this.C(
                        "withdraw", "Withdraw this Discover submission?\nIt leaves the public Marketplace. Your share code keeps working.", "Withdraw"
                     )
                  );
            }

            this.a.A();
            this.a
               .option(
                  "Delete cloud config",
                  "Remove it and revoke its share code",
                  () -> this.C("delete", "Delete “" + var1 + "” from the cloud?\nIts share code is revoked. This cannot be undone.", "Delete")
               );
         } else {
            this.a.m(var1, "Experimental storage · " + var2);
            this.a
               .option(
                  "Download preview",
                  "Review the server copy before saving it",
                  () -> this.a.y("Downloading...", this.a.Jd.u("GET", this.t(), null, null), this::M)
               );
            this.a.option("Check revision", "Compare with the server's latest revision", () -> {
               String var1x = t4.a(this.M, "revision");
               this.a.y("Checking revision...", this.a.Jd.u("GET", this.t() + "/revision", null, null), var2x -> {
                  if (!var1x.equals(t4.a(var2x, "revision"))) {
                     this.a.JK = "Server revision changed. Review the refreshed item.";
                     this.c(true);
                  } else {
                     this.a.JK = "Server revision is unchanged.";
                  }
               });
            });
            if (this.q == null) {
               this.q = this.a.L.d();
            }

            this.a
               .option(
                  "Replace server copy",
                  "From saved “" + this.q + "” (choose it in My Configs)",
                  () -> this.a.s("Replace the server copy?\nThe experimental copy is replaced with saved local config “" + this.q + "”.", "Replace", this::W)
               );
            this.a.A();
            this.a
               .option(
                  "Delete experimental profile",
                  "Remove the server copy",
                  () -> this.a
                     .W(
                        "Delete this experimental profile?\nThe server copy is removed. This cannot be undone.",
                        "Delete",
                        () -> this.a.y("Deleting...", this.a.Jd.u("DELETE", this.t(), null, t4.a(this.M, "revision")), var1x -> {
                           y(var1x);
                           this.c(false);
                        })
                     )
               );
         }
      }
   }

   private String t() {
      return "/api/profiles/" + t4.a(this.M, "id");
   }

   private void W() {
      try {
         JsonObject var1 = h.D(this.q, this.a.L.Y(this.q), this.a.P());
         var1.addProperty("revision", t4.a(this.M, "revision"));
         this.a.y("Replacing server copy...", this.a.Jd.u("PUT", this.t(), var1, null), var1x -> {
            t4.a(var1x, "revision");
            this.c(false);
         });
      } catch (IOException var2) {
         q8.L("Config", "Config operation failed: {}", var2.toString());
         this.a.JK = "Could not read local profile.";
         this.a.build();
      } catch (IllegalArgumentException var3) {
         q8.L("Config", "Replacement of {} refused before sending: {}", this.q, var3.getMessage());
         this.a.JK = var3.getMessage() == null ? "This profile cannot be shared as it is." : var3.getMessage();
         this.a.build();
      }
   }

   private CompletableFuture<JsonObject> J(JsonObject var1, String var2, Boolean var3) {
      JsonObject var4 = new JsonObject();
      var4.addProperty("action", var2);
      var4.addProperty("revision", t4.a(var1, "revision"));
      if (var3 != null) {
         var4.addProperty("discordSharing", var3);
      }

      return this.a.Jd.q.E("POST", "/api/configs/" + t4.a(var1, "id") + "/actions", var4, true, null);
   }

   private void S() {
      this.a
         .s(
            "Post “"
               + h.f(this.M, "name", 32)
               + "” in the Discord server?\nThe bot posts it with its share code within a minute, so anyone in the server can import it.",
            "Post",
            () -> this.a.y("Turning on Discord posting...", this.J(this.M, "discord-sharing", true), var1 -> {
               t4.a(var1, "revision");
               this.a.JK = "Done. The bot posts it in Discord within a minute.";
               q8.G("Config", "Discord posting enabled for shared config");
               this.c(true);
            })
         );
   }

   private void C(String var1, String var2, String var3) {
      Runnable var4 = () -> this.a.y("Updating cloud config...", this.J(this.M, var1, var1.equals("discord-sharing") ? false : null), var2x -> {
         if (var1.equals("delete")) {
            y(var2x);
         } else {
            t4.F(var2x);
            t4.V(var2x);
            t4.a(var2x, "revision");
         }

         if (var1.equals("regenerate")) {
            this.a.Jr = t4.F(var2x);
         }

         if (var1.equals("discord-sharing")) {
            this.a.JK = "Taken off Discord. The bot removes its post within a minute.";
            q8.G("Config", "Discord posting turned off for shared config");
         }

         this.c(var1.equals("discord-sharing"));
      });
      if (var1.equals("delete")) {
         this.a.W(var2, var3, var4);
      } else {
         this.a.s(var2, var3, var4);
      }
   }

   private static void y(JsonObject var0) {
      if (!h.o(var0.get("deleted"))) {
         throw u6.V();
      }
   }

   void c(boolean var1) {
      String var2 = this.a.JK;
      String var3 = this.M == null ? "" : t4.a(this.M, "id");
      this.a.y("Loading cloud items...", this.y ? this.a.Jd.v() : this.a.Jd.q.V("/api/configs/mine?limit=100", true), var4 -> {
         this.w = t4.J(var4, this.y);
         this.M = this.w.stream().filter(var1xx -> t4.a(var1xx, "id").equals(var3)).findFirst().orElse(null);
         this.a.JK = var1 ? var2 : "Cloud items refreshed";
      });
   }

   void u(String var1) {
      this.a.y("Importing...", this.a.Jd.q.V("/api/configs/import/" + var1, false), this::M);
   }

   private void M(JsonObject var1) {
      try {
         this.g = h.y(var1, this.a.P());
         this.E = this.g.name();
         this.a.f = 0;
         f.h("Config", f.q.e);
         this.a.JK = "Review before saving locally";
      } catch (IllegalArgumentException var3) {
         f.h("Config", f.q.P);
         q8.L("Config", "Import rejected: {}", var3.getMessage());
         this.a.JK = var3.getMessage();
      }
   }

   void A(boolean var1) {
      h.Y(this.E);
      if (this.a.L.X(this.E) && !var1) {
         this.a.s("Replace local “" + this.E + "”?\nYour live settings stay unchanged until you load it.", "Replace", () -> this.A(true));
      } else {
         try {
            this.a.L.c(this.E, this.g.config(), var1, this.a.P());
            String var2 = this.E;
            this.g = null;
            this.a.JT = (boolean)0;
            this.q = var2;
            this.a.U("Saved as “" + var2 + "”\nLoad it now and replace your live settings?", "Load", () -> {
               this.a.JK = this.a.L.G(var2) ? "Loaded local profile" : "Load failed. Config remains saved locally.";
               this.a.build();
            }, "Not now");
         } catch (IOException var3) {
            q8.L("Config", "Config operation failed: {}", var3.toString());
            this.a.JK = "Could not save locally. Choose a different profile name and retry.";
            this.a.build();
         }
      }
   }
}
