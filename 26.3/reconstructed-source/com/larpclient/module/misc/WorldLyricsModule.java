package com.larpclient.module.misc;

import com.larpclient.gui.theme.Theme;
import com.larpclient.hud.LyricsLineSource;
import com.larpclient.render.particle.FloatingTextTrail;
import com.larpclient.render.world.WorldTextRenderer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.SpotifyLyricsService;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.world.BlockRaycast;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public final class WorldLyricsModule extends Module {
   private static final double fADE_IN_SECONDS = 0.75;
   private static WorldLyricsModule instance;
   private final ModeSetting style = this.addSetting(new ModeSetting("Style", "Fixed plane tilt for new lyric lines", "Calm", "Calm", "Floating", "Dreamy"));
   private final ModeSetting motion = this.addSetting(new ModeSetting("Motion", "How letters enter, move while sung and leave", "Pop", "Pop", "Wave", "Fade"));
   private final NumberSetting density = this.addSetting(new NumberSetting("Density", "Length and amount of fading lyric trail", 16.0, 4.0, 32.0, 1.0));
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "World text size", 1.0, 0.5, 2.0, 0.1));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Lyric tint", -724993));
   private final BooleanSetting gradient = this.addSetting(new BooleanSetting("Gradient", "Flow the lyric tint into the client accent color", true));
   private final BooleanSetting glow = this.addSetting(new BooleanSetting("Glow", "Soft text bloom", true));
   private final BooleanSetting throughWalls = this.addSetting(new BooleanSetting("Through Walls", "Keep lyrics visible behind blocks and entities", true));
   private final LyricsLineSource lineSource = new LyricsLineSource();
   private final FloatingTextTrail trail = new FloatingTextTrail();
   private final Quaterniond cameraRotation = new Quaterniond();
   private SpotifyLyricsService lyricsService;
   private WorldTextRenderer textRenderer;
   private Object level;
   private Object player;
   private boolean hasGlowLabels;
   private boolean hasLabels;
   private long lastFrameNanos;
   private double animationTime;

   public WorldLyricsModule() {
      super(
         "WorldLyrics",
         "Whole Spotify lyric lines spawn near your crosshair, travel with you and animate letter by letter. Song title, artist, album and duration are sent to LRCLIB to find lyrics.",
         Category.VISUALS,
         -1
      );
      instance = this;
   }

   public static WorldLyricsModule getInstance() {
      return instance;
   }

   @Override
   public String getDisplayName() {
      return "Spotify World Lyrics";
   }

   @Override
   public String getStatus() {
      return this.lyricsService == null ? "Play a song in Spotify desktop" : this.lyricsService.snapshot().status();
   }

   @Override
   public void onEnable() {
      this.stop();
      this.lyricsService = SpotifyLyricsService.acquire(this);
      LarpLog.info(
         "WorldLyrics",
         "Spotify World Lyrics enabled; motion="
            + this.motion.getValue()
            + ", throughWalls="
            + this.throughWalls.getValue()
            + ", lines follow the player with fixed birth orientation"
      );
   }

   @Override
   public void onDisable() {
      this.stop();
   }

   public void stop() {
      if (this.lyricsService != null) {
         SpotifyLyricsService.release(this);
         this.lyricsService = null;
      }

      this.reset();
      LarpLog.debug("WorldLyrics", "Stopped playback worker and released world text resources");
   }

   public void reset() {
      this.lineSource.reset();
      this.trail.reset();
      if (this.textRenderer != null) {
         this.textRenderer.close();
         this.textRenderer = null;
      }

      this.level = this.player = null;
      this.hasLabels = this.hasGlowLabels = (boolean)0;
      this.lastFrameNanos = 0L;
      this.animationTime = 0.0;
   }

   public void updateLabels(CameraRenderState var1) {
      if (this.isEnabled()) {
         this.hasLabels = this.hasGlowLabels = (boolean)0;
         Minecraft var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame() && this.getPlayer().isAlive()) {
            if (this.level != var2.level || this.player != var2.player) {
               this.reset();
               this.level = var2.level;
               this.player = var2.player;
               LarpLog.info(
                  "WorldLyrics",
                  "Lyric planes initialized; player-following placement, per-letter motion, "
                     + (this.throughWalls.getValue() ? "drawn through blocks" : "native scene depth with surface-aware birth")
               );
            }

            long var3 = System.nanoTime();
            double var5 = this.lastFrameNanos == 0L ? 0.0 : Math.clamp((var3 - this.lastFrameNanos) / 1.0E9, 0.0, 0.1);
            this.lastFrameNanos = var3;
            if (var2.isPaused()) {
               var5 = 0.0;
            }

            this.animationTime += var5;
            this.trail.setTrailLength(this.density.getValue().intValue());
            if (!var2.isPaused() && this.lyricsService != null) {
               boolean var7 = this.lineSource.update(this.lyricsService.snapshot(), var3);
               if (this.lineSource.hasSeeked()) {
                  this.trail.reset();
               }

               if (var7 || this.lineSource.hasSeeked()) {
                  if (!this.lineSource.getLines().isEmpty()) {
                     this.trail.setMaxDistance(this.throughWalls.getValue() ? 8.0 : this.computeMaxDistance(var1));
                  }

                  this.trail.updateWithRevision(this.lineSource.getLines(), this.lineSource.getLineIndex());
               }
            }

            if (this.textRenderer == null) {
               this.textRenderer = new WorldTextRenderer();
            }

            double var13 = this.style.isMode("Floating") ? 1.5 : (this.style.isMode("Dreamy") ? 2.2 : 0.65);
            List var9 = this.trail
               .computeFrames(
                  var1.pos.x,
                  var1.pos.y,
                  var1.pos.z,
                  this.cameraRotation.set(var1.orientation),
                  1.0F / var1.projectionMatrix.m00(),
                  1.0F / var1.projectionMatrix.m11(),
                  this.size.getValue(),
                  var13,
                  var5,
                  this.textRenderer::measureWidth
               );
            int var10 = this.motion.isMode("Pop") ? 1 : (this.motion.isMode("Wave") ? 2 : 0);
            int var11 = this.gradient.getValue() ? Theme.accent | 0xFF000000 : 0;
            List var12 = var9.stream()
               .map(
                  var3x -> new WorldTextRenderer.WorldLabel(
                     var3x.text(),
                     var3x.posX(),
                     var3x.posY(),
                     var3x.posZ(),
                     var3x.scale(),
                     ColorUtil.multiplyAlpha(this.color.getValue(), var10 == 0 ? var3x.opacity() : 1.0),
                     (float)var3x.rotX(),
                     (float)var3x.rotY(),
                     (float)var3x.rotZ(),
                     (float)var3x.rotW(),
                     true,
                     var10 == 0 && var11 == 0
                        ? null
                        : new WorldTextRenderer.LabelMotion(var10, Math.min(1.0, var3x.age() / 0.75), var3x.exit(), this.animationTime, var11)
                  )
               )
               .toList();
            this.textRenderer.submitLabels(var12, var1);
            this.hasLabels = (boolean)(!var12.isEmpty() ? 1 : 0);
            this.hasGlowLabels = (boolean)(this.hasLabels && this.glow.getValue() ? 1 : 0);
         }
      }
   }

   private double computeMaxDistance(CameraRenderState var1) {
      Vector3d var2 = new Vector3d(0.0, 0.0, -1.0).rotate(this.cameraRotation.set(var1.orientation)).normalize();
      Vec3 var3 = var1.pos.add(var2.x * 7.99, var2.y * 7.99, var2.z * 7.99);
      BlockRaycast.RayHit var4 = BlockRaycast.raycastBlock(getMc().level, var1.pos, var3);
      return var4 == null ? 8.0 : Math.max(0.6, var1.pos.distanceTo(var4.point()) - 0.35);
   }

   public boolean hasGlow() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.hasGlowLabels && this.textRenderer != null && this.textRenderer.isAvailable();
   }

   public void renderLabels(PoseStack var1, CameraRenderState var2, boolean var3) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.hasLabels && this.textRenderer != null && this.level == getMc().level) {
            if (!var3 || this.hasGlowLabels) {
               this.textRenderer.renderLabelsFaded(var1, var2, this.throughWalls.getValue(), !var3, var3 ? 0.3 : 1.0);
            }
         }
      }
   }

   public void endBatch() {
      if (this.textRenderer != null) {
         this.textRenderer.endBatch();
      }
   }
}
