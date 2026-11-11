package com.larpclient.module.render;

import com.larpclient.core.StreamProofManager;
import com.larpclient.hud.NotificationManager;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.EspRenderer;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.data.Waypoint;
import com.larpclient.util.data.WaypointStore;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import com.larpclient.util.render.ScreenProjection;
import com.larpclient.util.world.Coordinates;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class WaypointsModule extends Module {
   private static final int maxRenderedWaypoints = 64;
   private static final int[] defaultColors = new int[]{-8600321, -30070, -7347266, -14249, -4291585, -24728, -723723};
   private static final int labelTextColor = -1;
   private static final int distanceTextColor = 14211288;
   private static final int labelBackgroundColor = 1073741824;
   private static final int beamLabelOffset = 5;
   private final BooleanSetting beams = this.addSetting(new BooleanSetting("Beams", "Draw a vertical beam at each waypoint, visible through walls", true));
   private final BooleanSetting tracers = this.addSetting(new BooleanSetting("Tracers", "Draw a line from your view to each waypoint", false));
   private final BooleanSetting labels = this.addSetting(new BooleanSetting("Labels", "Show each waypoint's name on screen", true));
   private final BooleanSetting showDistance = this.addSetting(new BooleanSetting("Distance", "Show how far away each waypoint is", true));
   private final NumberSetting labelScale = this.addSetting(new NumberSetting("Label Scale", "Size of the on-screen labels", 1.0, 0.5, 2.0, 0.1));
   private final KeybindSetting addWaypointKey = this.addSetting(new KeybindSetting("Add Waypoint Key", "Press in game to add a waypoint where you stand", -1));
   private WaypointStore store;
   private boolean addKeyWasDown;
   private Object lastLevel;
   private String worldKey;

   public WaypointsModule() {
      super("Waypoints", "Save your own locations and see them in the world", Category.RENDER, -1);
      this.showDistance.visibleWhen(this.labels::getValue);
      this.labelScale.visibleWhen(this.labels::getValue);
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.renderWorldMarkers());
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "waypoints"), this::renderLabels);
   }

   public List<Setting<?>> getVisualSettings() {
      return List.of(this.beams, this.tracers, this.labels, this.showDistance, this.labelScale);
   }

   public List<Setting<?>> isCurrentWorldFull() {
      return List.of(this.addWaypointKey);
   }

   private WaypointStore getStore() {
      if (this.store == null) {
         this.store = new WaypointStore(FabricLoader.getInstance().getConfigDir().resolve("larp-client").resolve("waypoints.json"));
      }

      return this.store;
   }

   public String getStore() {
      if (!this.isInGame()) {
         this.lastLevel = null;
         this.worldKey = null;
         return null;
      }

      if (minecraft.level != this.lastLevel) {
         this.lastLevel = minecraft.level;
         IntegratedServer var1 = minecraft.getSingleplayerServer();
         if (var1 != null) {
            this.worldKey = WaypointStore.worldKey(String.valueOf(var1.getWorldPath(LevelResource.ROOT).normalize().getFileName()));
         } else {
            this.worldKey = WaypointStore.serverKey(minecraft.getCurrentServer() == null ? null : minecraft.getCurrentServer().ip);
         }
      }

      return this.worldKey;
   }

   public boolean isMultiplayer() {
      return this.isInGame() && minecraft.getSingleplayerServer() == null;
   }

   public List<Waypoint> getWaypoints() {
      String var1 = this.getStore();
      return var1 == null ? List.of() : this.getStore().getWaypoints(var1);
   }

   public Optional<Waypoint> findWaypoint(String var1) {
      String var2 = this.getStore();
      return var2 == null ? Optional.empty() : this.getStore().findWaypoint(var2, var1);
   }

   public boolean isCurrentWorldFull() {
      String var1 = this.getStore();
      return var1 != null && this.getStore().isFull(var1);
   }

   public String getSaveError() {
      return this.getStore().getSaveError();
   }

   public int nextColor() {
      return defaultColors[this.getWaypoints().size() % defaultColors.length];
   }

   public String getPlayerCoordinates() {
      return this.isInGame() ? Coordinates.format(this.getPlayerPosition()) : "";
   }

   private Coordinates.BlockPos getPlayerPosition() {
      BlockPos var1 = this.getPlayer().blockPosition();
      return new Coordinates.BlockPos(var1.getX(), var1.getY(), var1.getZ());
   }

   private String getWaypoints() {
      return minecraft.level.dimension().identifier().toString();
   }

   public String addWaypoint(String var1, String var2, int var3) {
      String var4 = this.getStore();
      if (var4 == null) {
         return "Join a world first";
      }

      try {
         Coordinates.BlockPos var5 = Coordinates.parse(var2, this.getPlayerPosition());
         String var6 = Waypoint.cleanName(var1);
         this.getStore()
            .addWaypoint(
               var4,
               new Waypoint(
                  UUID.randomUUID().toString(),
                  var6.isEmpty() ? this.nextDefaultName() : var6,
                  var5.blockX(),
                  var5.blockY(),
                  var5.blockZ(),
                  this.getWaypoints(),
                  var3,
                  true
               )
            );
         LarpLog.info("Waypoints", "Added a waypoint in {}", this.getWaypoints());
         return this.getStore().getSaveError();
      } catch (IllegalArgumentException var7) {
         return var7.getMessage();
      }
   }

   private String nextDefaultName() {
      HashSet var1 = new HashSet();

      for (Waypoint var3 : this.getWaypoints()) {
         var1.add(var3.name());
      }

      int var4 = this.getWaypoints().size() + 1;

      while (var1.contains("Waypoint " + var4)) {
         var4++;
      }

      return "Waypoint " + var4;
   }

   public String editWaypoint(String var1, String var2, String var3, int var4) {
      String var5 = this.getStore();
      Waypoint var6 = this.findWaypoint(var1).orElse(null);
      if (var5 != null && var6 != null) {
         String var7 = Waypoint.cleanName(var2);
         if (var7.isEmpty()) {
            return "Enter a name";
         }

         try {
            this.getStore().updateWaypoint(var5, var6.edited(var7, Coordinates.parse(var3, var6.position()), var4));
            LarpLog.info("Waypoints", "Edited a waypoint in {}", var6.dimension());
            return this.getStore().getSaveError();
         } catch (IllegalArgumentException var9) {
            return var9.getMessage();
         }
      } else {
         return "That waypoint no longer exists";
      }
   }

   public void setWaypointVisible(String var1, boolean var2) {
      String var3 = this.getStore();
      this.findWaypoint(var1).ifPresent(var3x -> this.getStore().updateWaypoint(var3, var3x.withVisible(var2)));
   }

   public void deleteWaypoint(String var1) {
      String var2 = this.getStore();
      if (var2 != null && this.getStore().removeWaypoint(var2, var1)) {
         LarpLog.info("Waypoints", "Deleted a waypoint");
      }
   }

   public String describeWaypoint(Waypoint var1) {
      String var2 = Coordinates.format(var1.position());
      if (!this.isInGame()) {
         return var2;
      } else {
         return !var1.dimension().equals(this.getWaypoints())
            ? var2 + "  ·  " + Waypoint.dimensionLabel(var1.dimension())
            : var2 + "  ·  " + Coordinates.parse(Math.sqrt(this.getPlayer().distanceToSqr(var1.x() + 0.5, var1.y() + 0.5, var1.z() + 0.5)));
      }
   }

   @Override
   public void onDisable() {
      this.addKeyWasDown = (boolean)0;
   }

   @Override
   public void onTick() {
      int var13;
      label23: {
         int var1 = this.addWaypointKey.getValue();
         byte var4 = -1;
         int var3 = var1;
         var13 = Integer.compare(var3, var4);
         if (((var13 | -var13) >>> 31 ^ 1) == 0 && minecraft.gui.screen() == null && minecraft.isWindowActive()) {
            int var11 = BindCodes.isPressed(minecraft.getWindow().handle(), var1);
            boolean var6 = false;
            byte var5 = (byte)var11;
            var11 = Integer.signum(var5);
            if (((var11 | -var11) >>> 31 ^ 1) == 0) {
               var13 = 1;
               break label23;
            }
         }

         var13 = 0;
      }

      byte var2 = (byte)var13;
      boolean var8 = false;
      byte var7 = var2;
      var13 = Integer.signum(var7);
      if (((var13 | -var13) >>> 31 ^ 1) == 0) {
         boolean var10 = false;
         byte var9 = this.addKeyWasDown;
         var13 = Integer.signum(var9);
         if ((var13 | -var13) >>> 31 == 0) {
            this.addWaypointAtPlayer();
         }
      }

      this.addKeyWasDown = (boolean)var2;
   }

   private void addWaypointAtPlayer() {
      int var1 = this.getWaypoints().size();
      String var2 = this.addWaypoint("", "", this.nextColor());
      NotificationManager var3 = NotificationManager.getInstance();
      if (!var2.isEmpty()) {
         var3.notifyDefault("Waypoints", var2, NotificationManager.Type.WARNING);
      } else {
         List var4 = this.getWaypoints();
         if (var4.size() > var1) {
            var3.notifyDefault("Waypoint added", ((Waypoint)var4.getLast()).name(), NotificationManager.Type.SUCCESS);
         }
      }
   }

   private List<Waypoint> getVisibleWaypoints(Vec3 var1) {
      String var2 = this.getWaypoints();
      ArrayList var3 = new ArrayList();

      for (Waypoint var5 : this.getWaypoints()) {
         if (var5.visible() && var5.dimension().equals(var2)) {
            var3.add(var5);
         }
      }

      var3.sort(Comparator.comparingDouble(var1x -> var1.distanceToSqr(var1x.x() + 0.5, var1x.y() + 0.5, var1x.z() + 0.5)));
      return var3.size() > 64 ? var3.subList(0, 64) : var3;
   }

   private static Vec3 addWaypointAtPlayer(Vec3 var0, double var1, double var3, double var5, double var7) {
      return new Vec3(Coordinates.lerp(var0.x, var1, var7), Coordinates.lerp(var0.y, var3, var7), Coordinates.lerp(var0.z, var5, var7));
   }

   private void renderWorldMarkers() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame() && (this.beams.getValue() || this.tracers.getValue())) {
            long var1 = PerfProfiler.startTimer();

            try {
               Camera var3 = minecraft.gameRenderer.mainCamera();
               Vec3 var4 = var3.position();
               Vector3f var5 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var3.rotation());
               Vec3 var6 = var4.add(var5.x * 0.5, var5.y * 0.5, var5.z * 0.5);
               int var7 = minecraft.level.getMinY();
               int var8 = minecraft.level.getMaxY() + 1;

               for (Waypoint var10 : this.getVisibleWaypoints(var4)) {
                  double var11 = var10.x() + 0.5;
                  double var13 = var10.y() + 0.5;
                  double var15 = var10.z() + 0.5;
                  double var17 = Math.sqrt(var4.distanceToSqr(var11, var13, var15));
                  int var19 = ColorUtil.withAlpha(var10.color(), Math.clamp((var17 - 1.0) / 6.0, 0.0, 1.0) * 0.85);
                  if (this.beams.getValue()) {
                     double var20 = Coordinates.distanceScale(Math.hypot(var11 - var4.x, var15 - var4.z));
                     Gizmos.line(addWaypointAtPlayer(var4, var11, var7, var15, var20), addWaypointAtPlayer(var4, var11, var8, var15, var20), var19, 2.5F)
                        .setAlwaysOnTop();
                     if (var17 <= 64.0) {
                        Gizmos.cuboid(
                              new AABB(var10.x(), var10.y(), var10.z(), var10.x() + 1, var10.y() + 1, var10.z() + 1),
                              GizmoStyle.strokeAndFill(var19, 1.5F, ColorUtil.withAlpha(var10.color(), 0.12))
                           )
                           .setAlwaysOnTop();
                     }
                  }

                  if (this.tracers.getValue()) {
                     double var23 = Coordinates.distanceScale(var17);
                     Gizmos.line(var6, addWaypointAtPlayer(var4, var11, var13, var15, var23), ColorUtil.withAlpha(var10.color(), 0.7), 1.5F).setAlwaysOnTop();
                  }
               }
            } catch (RuntimeException var22) {
               LarpLog.errorOnce("Waypoints", "World markers failed", var22);
            }

            PerfProfiler.endTimer("Waypoints", var1);
         }
      }
   }

   private void renderLabels(GuiGraphicsExtractor var1, DeltaTracker var2) {
      if (this.isEnabled()) {
         if (this.labels.getValue() && this.isOperational() && this.isInGame() && !minecraft.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
            var1 = StreamProofManager.selectGraphics(var1, false, false);
            if (var1 != null) {
               long var3 = PerfProfiler.startTimer();

               try {
                  Camera var5 = minecraft.gameRenderer.mainCamera();
                  Vec3 var6 = var5.position();
                  Matrix4f var7 = EspRenderer.buildWorldProjection(new Matrix4f());
                  Vector3f var8 = var5.rotation().transform(new Vector3f(0.0F, 0.025F, 0.0F));
                  int var9 = minecraft.getWindow().getGuiScaledWidth();
                  int var10 = minecraft.getWindow().getGuiScaledHeight();
                  boolean var11 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
                  ArrayList var12 = new ArrayList();

                  for (Waypoint var14 : this.getVisibleWaypoints(var6)) {
                     double var15 = var14.x() + 0.5;
                     double var17 = var14.y() + 1.5;
                     double var19 = var14.z() + 0.5;
                     double var21 = Math.sqrt(var6.distanceToSqr(var15, var17, var19));
                     double var23 = Coordinates.distanceScale(var21);
                     ScreenProjection.ProjectedPoint var25 = ScreenProjection.projectPoint(
                        addWaypointAtPlayer(var6, var15, var17, var19, var23), var6, var7, var8, var9, var10, var11
                     );
                     if (var25 != null) {
                        this.renderLabel(var1, var14, var25, var21, var12);
                     }
                  }
               } catch (RuntimeException var26) {
                  LarpLog.errorOnce("Waypoints", "Labels failed", var26);
               }

               PerfProfiler.endTimer("Waypoints labels", var3);
            }
         }
      }
   }

   private float getPlayerCoordinates(float var1) {
      double var2 = minecraft.getWindow().getGuiScale();
      return (float)(Math.round(var1 * var2) / var2);
   }

   private void renderLabel(GuiGraphicsExtractor var1, Waypoint var2, ScreenProjection.ProjectedPoint var3, double var4, List<float[]> var6) {
      MutableComponent var7 = Component.literal(var2.name());
      if (this.showDistance.getValue()) {
         var7.append(Component.literal(" " + Coordinates.parse(var4)).withColor(14211288));
      }

      int var8 = minecraft.font.width(var7);
      float var9 = this.labelScale.getValue().floatValue();
      float var10 = (var8 + 2) * var9 / 2.0F;
      float var11 = 5.5F * var9;
      float var12 = this.beams.getValue() ? var3.getX() + 5.0F + var10 : var3.getX();
      float var13 = Coordinates.placeLabel(var6, var12, var3.getY(), var10, var11);
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(this.getPlayerCoordinates(var12 - var10), this.getPlayerCoordinates(var13 - var11)).scale(var9);
         DrawUtil.fillRect(var1, 0, 0, var8 + 2, 11, 1073741824);
         var1.text(minecraft.font, var7, 1, 1, -1, true);
      } finally {
         var1.pose().popMatrix();
      }
   }
}
