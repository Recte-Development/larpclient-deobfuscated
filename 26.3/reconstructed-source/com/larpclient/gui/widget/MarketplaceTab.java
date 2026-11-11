package com.larpclient.gui.widget;

import com.google.gson.JsonObject;
import com.larpclient.config.ConfigListPage;
import com.larpclient.config.ProfileValidator;
import com.larpclient.network.ApiResponseValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

final class MarketplaceTab {
   private final ConfigsPanel screen;
   String query = "";
   int offset;
   int total;
   long searchAtMillis;
   boolean loaded;
   List<JsonObject> configs = List.of();
   JsonObject selected;

   MarketplaceTab(ConfigsPanel var1) {
      this.screen = var1;
   }

   void clearSelection() {
      this.selected = null;
   }

   void buildList() {
      String var1 = this.query.toLowerCase(Locale.ROOT);

      for (JsonObject var3 : this.configs) {
         String var4 = ProfileValidator.readString(var3, "name", 32);
         String var5 = ProfileValidator.readString(var3, "description", 4000);
         if ((var4 + " " + var5 + " " + ProfileValidator.readString(var3, "author", 160)).toLowerCase(Locale.ROOT).contains(var1)) {
            this.screen.addConfigCard(var4, ProfileValidator.readString(var3, "author", 160), this.selected == var3, () -> {
               this.selected = var3;
               this.screen.scrollOffset = 0;
               this.screen.build();
            });
         }
      }
   }

   void buildDetail() {
      if (this.selected != null) {
         String var1 = ProfileValidator.readString(this.selected, "author", 160);
         this.screen.setDialogText(ProfileValidator.readString(this.selected, "name", 32), var1.isBlank() ? "" : "By " + var1);
         String var2 = ProfileValidator.readString(this.selected, "description", 4000);
         if (!var2.isBlank()) {
            this.screen.text(var2);
         }

         ArrayList var3 = new ArrayList();
         String var4 = ProfileValidator.readString(this.selected, "minecraftVersion", 160);
         String var5 = ProfileValidator.readString(this.selected, "larpVersion", 160);
         if (!var4.isBlank()) {
            var3.add("Minecraft " + var4);
         }

         if (!var5.isBlank()) {
            var3.add("Larp " + var5);
         }

         if (!var3.isEmpty()) {
            this.screen.text(String.join(" · ", var3));
         }

         this.screen.addGuardedDialogButton("Preview import", () -> this.screen.myConfigsView.importByCode(ApiResponseValidator.readShareCode(this.selected))).accented = (boolean)1;
      }
   }

   void loadPage() {
      this.screen.scrollOffset = 0;
      this.selected = null;
      this.loaded = (boolean)0;
      this.configs = List.of();
      this.screen.runRequest("Loading marketplace...", this.screen.cloudApi.api.get("/api/configs?limit=30&offset=" + this.offset, false).thenApply(var0 -> {
         ConfigListPage.validate(var0);
         return (JsonObject)var0;
      }), var1 -> {
         this.configs = ConfigListPage.validate(var1).items();
         this.total = var1.get("total").getAsInt();
         this.loaded = (boolean)1;
         this.screen.statusMessage = "Search filters this loaded page (" + this.configs.size() + " of " + this.total + ").";
      });
   }

   void tickSearch() {
      if (this.searchAtMillis != 0L && System.currentTimeMillis() >= this.searchAtMillis && this.screen.isBrowsingDiscover()) {
         this.searchAtMillis = 0L;
         this.screen.addCardAction();
      }
   }
}
