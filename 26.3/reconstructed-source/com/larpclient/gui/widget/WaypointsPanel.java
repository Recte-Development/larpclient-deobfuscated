package com.larpclient.gui.widget;

import com.larpclient.gui.clickgui.ModuleSettingsPanel;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.render.WaypointsModule;
import com.larpclient.util.data.Waypoint;
import com.larpclient.util.world.Coordinates;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

final class WaypointsPanel {
   final WaypointsModule module;
   private final ModuleSettingsPanel panel;
   private final Map<MenuButton, Waypoint> rows = new IdentityHashMap<>();
   private String worldKey;
   private String newName = "";
   private String newPosition = "";
   private String addMessage = "";
   private ColorSetting newColor;
   private String selectedId;
   private String editName = "";
   private String editPosition = "";
   private String editMessage = "";
   private ColorSetting editColor;
   private boolean confirmingDelete;

   WaypointsPanel(WaypointsModule var1, ModuleSettingsPanel var2) {
      this.module = var1;
      this.panel = var2;
      this.newColor = createColorSetting(var1.nextColor());
   }

   private static ColorSetting createColorSetting(int var0) {
      return new ColorSetting("Color", "Color of this waypoint's beam and label", var0);
   }

   void build() {
      this.rows.clear();
      this.worldKey = this.module.getStore();
      if (this.worldKey == null) {
         this.panel.text("Join a world or server to set waypoints. Each one keeps its own list.");
      } else {
         this.buildAddForm();
         this.panel.addWidget(new SectionLabel(this.module.isMultiplayer() ? "Saved on this server" : "Saved in this world"));
         List var1 = this.module.getWaypoints();
         if (var1.isEmpty()) {
            this.panel.text("Nothing saved yet.");
         }

         for (Waypoint var3 : var1) {
            MenuButton var4 = new MenuButton(var3.name(), () -> this.toggleSelection(var3));
            var4.subtitle = this.module.describeWaypoint(var3);
            var4.moduleCard = (boolean)1;
            var4.height = 34;
            var4.selected = var3.id().equals(this.selectedId);
            var4.toggleState = var3.visible();
            var4.secondaryAction = () -> {
               this.module.setWaypointVisible(var3.id(), !var3.visible());
               this.refresh();
            };
            this.rows.put(var4, var3);
            this.panel.addWidget(var4);
            if (var3.id().equals(this.selectedId)) {
               this.buildEditForm(var3);
            }
         }

         String var5 = this.module.getSaveError();
         if (!var5.isEmpty()) {
            this.panel.text(var5);
         }
      }
   }

   private void buildAddForm() {
      this.panel.addWidget(new SectionLabel("New waypoint"));
      this.addTextField("Name (optional)", this.newName, var1x -> this.newName = var1x, 32, this::addWaypoint);
      this.addTextField("X Y Z (blank uses your position)", this.newPosition, var1x -> this.newPosition = var1x, 40, this::addWaypoint);
      this.panel.addWidget(new SettingRowWidget(this.newColor));
      MenuButton var1 = this.panel.addButton("Add waypoint", this::addWaypoint);
      var1.accented = (boolean)1;
      var1.enabled = (boolean)(!this.module.isCurrentWorldFull() ? 1 : 0);
      if (!this.addMessage.isEmpty()) {
         this.panel.text(this.addMessage);
      }
   }

   private void buildEditForm(Waypoint var1) {
      this.addTextField("Name", this.editName, var1x -> this.editName = var1x, 32, () -> this.saveWaypoint(var1));
      this.addTextField("X Y Z", this.editPosition, var1x -> this.editPosition = var1x, 40, () -> this.saveWaypoint(var1));
      this.panel.addWidget(new SettingRowWidget(this.editColor));
      Object var2 = new Object();
      MenuButton var3 = this.addButton("Save", var2, () -> this.saveWaypoint(var1));
      var3.accented = (boolean)1;
      this.addButton("Use my position", var2, () -> {
         this.editPosition = this.module.getPlayerCoordinates();
         this.editMessage = "";
         this.refresh();
      });
      MenuButton var4 = this.addButton(this.confirmingDelete ? "Confirm delete" : "Delete", var2, () -> this.deleteWaypoint(var1));
      var4.danger = (boolean)1;
      var4.accented = this.confirmingDelete;
      if (!this.editMessage.isEmpty()) {
         this.panel.text(this.editMessage);
      }
   }

   private void addTextField(String var1, String var2, Consumer<String> var3, int var4, Runnable var5) {
      TextField var6 = new TextField(var1, var3, var4);
      var6.text = var2;
      var6.onSubmit(var5);
      this.panel.addWidget(var6);
   }

   private MenuButton addButton(String var1, Object var2, Runnable var3) {
      MenuButton var4 = new MenuButton(var1, var3);
      var4.pill = (boolean)1;
      var4.tag = var2;
      var4.height = 24;
      this.panel.addWidget(var4);
      return var4;
   }

   private void addWaypoint() {
      String var1 = this.module.addWaypoint(this.newName, this.newPosition, this.newColor.getValue());
      if (var1.isEmpty()) {
         this.newName = this.newPosition = "";
         this.newColor = createColorSetting(this.module.nextColor());
         this.addMessage = this.module.isEnabled() ? "Waypoint added." : "Waypoint added. Turn the module on to see it.";
      } else {
         this.addMessage = var1;
      }

      this.refresh();
   }

   private void toggleSelection(Waypoint var1) {
      this.confirmingDelete = (boolean)0;
      this.editMessage = "";
      if (var1.id().equals(this.selectedId)) {
         this.selectedId = null;
      } else {
         this.selectedId = var1.id();
         this.editName = var1.name();
         this.editPosition = Coordinates.format(var1.position());
         this.editColor = createColorSetting(var1.color());
      }

      this.refresh();
   }

   private void saveWaypoint(Waypoint var1) {
      this.confirmingDelete = (boolean)0;
      this.editMessage = this.module.editWaypoint(var1.id(), this.editName, this.editPosition, this.editColor.getValue());
      if (this.editMessage.isEmpty()) {
         this.selectedId = null;
      }

      this.refresh();
   }

   private void deleteWaypoint(Waypoint var1) {
      if (!this.confirmingDelete) {
         this.confirmingDelete = (boolean)1;
         this.refresh();
      } else {
         this.confirmingDelete = (boolean)0;
         this.selectedId = null;
         this.module.deleteWaypoint(var1.id());
         this.refresh();
      }
   }

   private void refresh() {
      this.panel.rebuildPreservingScroll();
   }

   void tick() {
      if (!Objects.equals(this.worldKey, this.module.getStore())) {
         this.selectedId = null;
         this.refresh();
      } else {
         this.rows.forEach((var1, var2) -> var1.subtitle = this.module.describeWaypoint(var2));
      }
   }
}
