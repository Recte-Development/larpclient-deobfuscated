package com.larpclient.module.world;

import com.larpclient.hud.NotificationManager;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.LightChunkScanner;
import java.util.List;
import java.util.Set;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacketData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class MotionScannerModule extends Module {
   private final NumberSetting opacitySetting = this.addSetting(new NumberSetting("Opacity", "Opacity of detected chunk volumes", 140.0, 10.0, 255.0, 1.0));
   private final NumberSetting sensitivitySetting = this.addSetting(
      new NumberSetting("Sensitivity", "Minimum nearby anomalous chunks required for a discovery", 1.0, 1.0, 5.0, 1.0)
   );
   private final BooleanSetting hudNotification = this.addSetting(new BooleanSetting("HUD Notification", "Show a local discovery notification", true));
   private final BooleanSetting soundNotification = this.addSetting(
      new BooleanSetting("Sound Notification", "Play the source note-block chime on a discovery", true)
   );
   private final BooleanSetting chatNotifications = this.addSetting(new BooleanSetting("Chat Notifications", "Write discoveries to local chat", true));
   private final BooleanSetting showCoords = this.addSetting(new BooleanSetting("Show Coords", "Include representative chunk-center coordinates in chat", true));
   private final BooleanSetting showTracers = this.addSetting(new BooleanSetting("Show Tracers", "Draw camera-to-discovery lines", false));
   private final NumberSetting tracerAlpha = this.addSetting(new NumberSetting("Tracers Alpha", "Opacity of camera-to-discovery lines", 70.0, 0.0, 255.0, 1.0));
   private final ColorSetting highlightColor = this.addSetting(
      new ColorSetting("Highlight Color", "Detected chunk and tracer color; source blue by default", -14122241)
   );
   private final LightChunkScanner scanner = new LightChunkScanner();
   private Set<Long> discoveries = Set.of();
   private ClientLevel level;
   private boolean dirty;
   private static MotionScannerModule instance;

   public MotionScannerModule() {
      super("Motion Scanner", "Detect unusual received skylight patterns below sea level", Category.RENDER, -1);
      instance = this;
      this.tracerAlpha.visibleWhen(this.showTracers::getValue);
      this.showCoords.visibleWhen(this.chatNotifications::getValue);
      this.sensitivitySetting.onChange(var1 -> this.dirty = (boolean)1);
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.level) {
            this.scanner.removeChunk(var2.getPos().pack());
            this.dirty = (boolean)1;
            this.discoveries = this.scanner.update();
         }
      });
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.renderDiscoveries());
   }

   public List<Setting<?>> getDetectionSettings() {
      return List.of(this.sensitivitySetting);
   }

   public List<Setting<?>> getNotificationSettings() {
      return List.of(this.hudNotification, this.soundNotification, this.chatNotifications, this.showCoords);
   }

   public List<Setting<?>> getRenderSettings() {
      return List.of(this.opacitySetting, this.highlightColor, this.showTracers, this.tracerAlpha);
   }

   private void resetState(ClientLevel var1) {
      this.scanner.clear();
      this.discoveries = Set.of();
      this.dirty = (boolean)0;
      this.level = var1;
      NotificationManager.getInstance().dismiss(this.getName());
   }

   @Override
   public void onEnable() {
      this.resetState(getMc().level);
      LarpLog.info(
         "BaseFinding", "Motion Scanner active: explicit zeroed sky-light sections below Y 62, cluster threshold {}", this.sensitivitySetting.getValue()
      );
   }

   @Override
   public void onDisable() {
      LarpLog.debug("BaseFinding", "Motion Scanner cleanup: {} tracked chunks", this.scanner.trackedCount());
      this.resetState(null);
   }

   public static void onLightUpdate(ClientLevel var0, int var1, int var2, ClientboundLightUpdatePacketData var3) {
      if (instance != null && instance.isEnabled()) {
         MotionScannerModule var4 = instance;
         if (var4 != null && var4.isRuntimeActive() && var0 != null && var3 != null) {
            Minecraft var5 = getMc();
            if (var5.isSameThread() && var5.level == var0 && var5.player != null && !(var5.player.getY() < 0.0)) {
               if (var4.level != var0) {
                  var4.resetState(var0);
               }

               if (var0.getChunkSource().getChunkNow(var1, var2) != null) {
                  try {
                     List var6 = var3.skyUpdates().stream().map(LightChunkScanner::classifyLight).toList();
                     boolean var7 = var4.scanner
                        .update(var1, var2, var0.getSectionsCount(), var0.getMinSectionY(), var3.skyYMask(), var3.emptySkyYMask(), var6);
                     var4.dirty |= var7;
                  } catch (RuntimeException var8) {
                     LarpLog.errorOnce("BaseFinding", "Motion Scanner packet classification failed; later light updates can recover", var8);
                  }
               }
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.level != minecraft.level) {
            this.resetState(minecraft.level);
         }

         int var10000 = this.scanner.retainChunks(var1x -> {
            ChunkPos var3x = ChunkPos.unpack(var1x);
            return this.level.getChunkSource().getChunkNow(var3x.x(), var3x.z()) != null;
         });
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.discoveries = this.scanner.update();
         }

         double var14;
         var10000 = (var14 = minecraft.player.getY() - 0.0) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
         boolean var8 = false;
         int var7 = var10000;
         if (Integer.signum(var7) >>> 31 == 0) {
            boolean var10 = false;
            byte var9 = this.dirty;
            var10000 = Integer.signum(var9);
            if ((var10000 | -var10000) >>> 31 != 0) {
               ChunkPos var1 = minecraft.player.chunkPosition();

               for (long var3 : this.scanner.findNewClusters(this.sensitivitySetting.getValue().intValue(), var1.x(), var1.z())) {
                  this.onLightUpdate(var3);
               }

               this.discoveries = this.scanner.update();
               this.dirty = (boolean)0;
               return;
            }
         }
      }
   }

   private void onLightUpdate(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      int var4 = var3.getMinBlockX() + 8;
      int var5 = var3.getMinBlockZ() + 8;
      LarpLog.debug("BaseFinding", "Motion Scanner discovery: chunk [{}, {}], center {}, {}", var3.x(), var3.z(), var4, var5);
      if (this.soundNotification.getValue()) {
         minecraft.player.playSound((SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.2F, 1.2F);
      }

      if (this.chatNotifications.getValue()) {
         minecraft.gui
            .chatListener()
            .handleSystemMessage(Component.literal("(Motion Scanner) Base detected" + (this.showCoords.getValue() ? ": " + var4 + ", " + var5 : "")), false);
      }

      if (this.hudNotification.getValue()) {
         NotificationManager.getInstance().notifyDefault(this.getName(), "Base detected", NotificationManager.Type.INFO);
      }
   }

   private void renderDiscoveries() {
      if (this.isRuntimeActive() && this.isInGame() && this.level == minecraft.level) {
         ChunkPos var1 = minecraft.player.chunkPosition();
         int var2 = this.highlightColor.getValue() & 16777215 | this.opacitySetting.getValue().intValue() << 24;
         Camera var3 = minecraft.gameRenderer.mainCamera();
         Vector3f var4 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var3.rotation());
         Vec3 var5 = var3.position().add(var4.x * 0.1, var4.y * 0.1, var4.z * 0.1);

         try {
            for (long var7 : this.discoveries) {
               ChunkPos var9 = ChunkPos.unpack(var7);
               if (Math.abs((long)var9.x() - var1.x()) <= 17L
                  && Math.abs((long)var9.z() - var1.z()) <= 17L
                  && this.level.getChunkSource().getChunkNow(var9.x(), var9.z()) != null) {
                  AABB var10 = new AABB(var9.getMinBlockX(), -64.0, var9.getMinBlockZ(), var9.getMinBlockX() + 16, 320.0, var9.getMinBlockZ() + 16);
                  Gizmos.cuboid(var10, GizmoStyle.fill(var2)).setAlwaysOnTop();
                  if (this.showTracers.getValue() && this.tracerAlpha.getValue() > 0.0) {
                     Gizmos.line(var5, var10.getCenter(), var2 & 16777215 | this.tracerAlpha.getValue().intValue() << 24, 1.0F).setAlwaysOnTop();
                  }
               }
            }
         } catch (RuntimeException var11) {
            LarpLog.errorOnce("BaseFinding", "Motion Scanner render failed; evidence retained", var11);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.discoveries.size() + " discoveries";
   }
}
