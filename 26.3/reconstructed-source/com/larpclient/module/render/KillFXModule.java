package com.larpclient.module.render;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.GhostPlayerRenderer;
import com.larpclient.render.entity.ItemSwingAnimator;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.model.ModelAssemblyAnimation;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.particle.KillEffect;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.render.world.TeleportEffectRenderer;
import com.larpclient.render.world.WorldTextRenderer;
import com.larpclient.social.emote.EmoteDanceAnimator;
import com.larpclient.social.pet.SahurPet;
import com.larpclient.util.data.RecentEventTracker;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.world.BlockRaycast;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class KillFXModule extends Module {
   public static final int mAX_FINISHERS = 4;
   public static final double rETIRE_SECONDS = 0.45;
   private static KillFXModule instance;
   private final ModeSetting style = this.addSetting(
      new ModeSetting(
         "Style",
         "Choose a finisher or vary each kill",
         "Random",
         "Random",
         "Execution",
         "Singularity",
         "Soul Pyre",
         "Star of David",
         "Tung Tung",
         "Tung Tung Floss",
         "Sakura",
         "Shatter",
         "Rug Pull"
      )
   );
   private final ModeSetting colors = this.addSetting(
      new ModeSetting("Colors", "Authored palettes, your theme or custom colors", "Style", "Style", "Theme", "Custom")
   );
   private final ColorSetting primaryColor = this.addSetting(new ColorSetting("Primary Color", "Main energy color", -10101505));
   private final ColorSetting secondaryColor = this.addSetting(new ColorSetting("Secondary Color", "Trail and impact color", -32547));
   private final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Use the shared world bloom pass", true));
   private final BooleanSetting sound = this.addSetting(new BooleanSetting("Sound", "Local windup and finishing cues", true));
   private final NumberSetting volume = this.addSetting(new NumberSetting("Volume", "Local sound volume", 0.55, 0.0, 1.0, 0.05));
   private final NumberSetting renderDistance = this.addSetting(new NumberSetting("Render Distance", "Maximum distance in blocks", 48.0, 8.0, 64.0, 4.0));
   private final RecentEventTracker recentEvents = new RecentEventTracker();
   private final GhostPlayerRenderer ghostRenderer = new GhostPlayerRenderer();
   private final ArrayList<KillFXModule.ActiveFinisher> finishers = new ArrayList<>();
   private List<KillFXModule.RenderEntry> renderEntries = List.of();
   private Object trackedLevel;
   private Object trackedPlayer;
   private int reloadGeneration = -1;
   private int lastStyleIndex = -1;
   private long killCounter;
   private Vec3 lastPlayerPos;
   private WorldTextRenderer textRenderer;
   private CameraRenderState cameraState;
   private static final RenderStateDataKey<KillFXModule.VictimMark> vICTIM_MARK_KEY = RenderStateDataKey.create(() -> "Larp KillFX confirmed victim");

   public KillFXModule() {
      super("KillFX", "Play cinematic finishers when a player you damaged dies", Category.VISUALS, -1);
      instance = this;
      this.primaryColor.visibleWhen(() -> this.colors.isMode("Custom"));
      this.secondaryColor.visibleWhen(() -> this.colors.isMode("Custom"));
      this.volume.visibleWhen(() -> this.sound.getValue());
      this.style.onChange(var1 -> {
         this.reset();
         LarpLog.info("KillFX", "Style changed: {}; active finishers cleared", var1);
      });
   }

   public static KillFXModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getStyleSettings() {
      return List.of(this.style, this.colors, this.primaryColor, this.secondaryColor);
   }

   public Setting<?> getStyleSetting() {
      return this.style;
   }

   public List<Setting<?>> getEffectSettings() {
      return List.of(this.bloom, this.sound, this.volume);
   }

   public List<Setting<?>> getDistanceSettings() {
      return List.of(this.renderDistance);
   }

   public String getStyleDescription() {
      return switch ((String)this.style.getValue()) {
         case "Execution" -> "Lifts the victim into a double helix, slices the body and drops the skinned pieces onto terrain.";
         case "Singularity" -> "Twists the body into a collapsing star vortex, then releases a sharp ring of light.";
         case "Soul Pyre" -> "Burns the skin away in glowing fragments while spiralling soul embers rise from the body.";
         case "Star of David" -> "Lifts the body and turns its six skinned parts into two interlocking triangles facing your view.";
         case "Tung Tung" -> "Summons a towering Tung Tung Sahur, who raises his wooden bat and slams the victim to the ground.";
         case "Tung Tung Floss" -> "Sahur crouches, jumps onto the fallen player and dances the Floss with swinging arms and counter-swaying hips.";
         case "Sakura" -> "A petal shockwave drops the victim, the body blooms into a swirling sphere under a pillar of light, then bursts and settles into a glowing yin-yang seal.";
         case "Shatter" -> "Frost climbs the frozen victim and cracks of light spread before the body shatters into ice shards and tumbling pieces that melt away.";
         case "Rug Pull" -> "A trading chart pumps from $0 to $1,000 beside the victim, then a red candle crashes through the floor, the rug is yanked away and the profit drops to -$67,000 while a headline falls from the sky and floats in front of the chart.";
         default -> "Varies between all finishers without repeating the last animation.";
      };
   }

   @Override
   public void onEnable() {
      this.reset();
      LarpLog.info("KillFX", "Enabled: {}; server death events only, four playing finishers maximum", this.style.getValue());
   }

   @Override
   public void onDisable() {
      this.reset();
   }

   public void reset() {
      this.recentEvents.clear();
      this.finishers.clear();
      this.renderEntries = List.of();
      this.ghostRenderer.reset();
      if (this.textRenderer != null) {
         this.textRenderer.close();
         this.textRenderer = null;
      }

      this.cameraState = null;
      this.trackedLevel = this.trackedPlayer = null;
      this.lastPlayerPos = null;
      this.reloadGeneration = -1;
   }

   private boolean reset() {
      Minecraft var1 = getMc();
      if (this.isRuntimeActive() && var1.level != null && var1.player != null) {
         if (this.trackedLevel != var1.level
            || this.trackedPlayer != var1.player
            || this.reloadGeneration != PostProcessing.reloadCount
            || this.lastPlayerPos != null && this.lastPlayerPos.distanceToSqr(var1.player.position()) > 256.0) {
            this.reset();
            this.trackedLevel = var1.level;
            this.trackedPlayer = var1.player;
            this.reloadGeneration = PostProcessing.reloadCount;
         }

         this.lastPlayerPos = var1.player.position();
         return true;
      } else {
         this.reset();
         return false;
      }
   }

   public void onDamageEvent(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         if (this.reset()) {
            Minecraft var2 = getMc();
            Entity var3 = var2.level.getEntity(var1.entityId());
            if (var3 instanceof Player && var3 != var2.player) {
               this.recentEvents.markPending(var3, var1.sourceCauseId() == var2.player.getId(), TickTimer.getTickTime());
            }
         }
      }
   }

   public void onEntityEvent(ClientboundEntityEventPacket var1) {
      if (this.isEnabled()) {
         if (this.reset()) {
            Minecraft var2 = getMc();
            if (var1.getEntity(var2.level) instanceof AbstractClientPlayer var4 && var4 != var2.player) {
               if (var1.getEventId() == 35) {
                  this.recentEvents.clearPending(var4);
                  this.finishers.removeIf(var1x -> var1x.victim == var4);
                  this.renderEntries = this.renderEntries.stream().filter(var1x -> var1x.owner.victim != var4).toList();
               } else if (var1.getEventId() == 3 && this.recentEvents.consume(var4, TickTimer.getTickTime())) {
                  if (!(var4.position().distanceToSqr(var2.player.position()) > this.renderDistance.getValue() * this.renderDistance.getValue())) {
                     double var5 = TickTimer.getTickTime();
                     KillEffect.Style var7 = this.pickStyle();
                     int var8 = var7 == KillEffect.Style.ASCEND ? -19633 : (var7 == KillEffect.Style.VORTEX ? -5144321 : -10101505);
                     int var9 = var7 == KillEffect.Style.ASCEND ? -8915729 : (var7 == KillEffect.Style.VORTEX ? -8328705 : -32547);
                     if (var7 == KillEffect.Style.MODEL_ASSEMBLY) {
                        var8 = -8474625;
                        var9 = -1444097;
                     }

                     if (var7 == KillEffect.Style.ITEM_SWING || var7 == KillEffect.Style.EMOTE_DANCE) {
                        var8 = -18594;
                        var9 = -5188;
                     }

                     if (var7 == KillEffect.Style.YIN_YANG) {
                        var8 = -22842;
                        var9 = -3337;
                     }

                     if (var7 == KillEffect.Style.SPIDER) {
                        var8 = -7938817;
                        var9 = -1114881;
                     }

                     if (var7 == KillEffect.Style.RUG_PULL) {
                        var8 = -13242230;
                        var9 = -50360;
                     }

                     if (this.colors.isMode("Custom")) {
                        var8 = this.primaryColor.getValue();
                        var9 = this.secondaryColor.getValue();
                     } else if (this.colors.isMode("Theme")) {
                        var8 = Theme.accent;
                        var9 = Theme.accentEnd;
                     }

                     long var10 = var4.getUUID().getMostSignificantBits() ^ var4.getUUID().getLeastSignificantBits() ^ ++this.killCounter;
                     double var12 = Math.toRadians(var4.yBodyRot);
                     KillEffect var14 = new KillEffect(var7, var4.position(), var12, var5, var10, var4.getSkin().model() == PlayerModelType.SLIM);
                     List var15 = this.finishers.stream().filter(var0 -> !var0.isRetiring()).toList();
                     if (var15.size() >= 4) {
                        ((KillFXModule.ActiveFinisher)var15.getFirst()).retire(var5);
                        LarpLog.debug(
                           "KillFX",
                           "Finisher limit reached; retiring the oldest ({}) over {}s",
                           ((KillFXModule.ActiveFinisher)var15.getFirst()).effect.style,
                           0.45
                        );
                     }

                     while (this.finishers.size() >= 8) {
                        this.finishers.removeFirst();
                     }

                     KillFXModule.ActiveFinisher var16 = new KillFXModule.ActiveFinisher(
                        var4, var14, var8, var9, this.bloom.getValue(), this.sound.getValue(), this.volume.getValue()
                     );
                     this.finishers.add(var16);
                     this.reset(
                        var16,
                        var7 == KillEffect.Style.ASCEND
                           ? SoundEvents.FIRECHARGE_USE
                           : (
                              var7 == KillEffect.Style.YIN_YANG
                                 ? SoundEvents.CHERRY_LEAVES_BREAK
                                 : (
                                    var7 == KillEffect.Style.SPIDER
                                       ? SoundEvents.PLAYER_HURT_FREEZE
                                       : (
                                          var7 == KillEffect.Style.RUG_PULL
                                             ? (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value()
                                             : (
                                                var7 == KillEffect.Style.EMOTE_DANCE
                                                   ? SoundEvents.PLAYER_ATTACK_SWEEP
                                                   : (var7 == KillEffect.Style.ITEM_SWING ? SoundEvents.IRON_GOLEM_ATTACK : SoundEvents.AMETHYST_BLOCK_RESONATE)
                                             )
                                       )
                                 )
                           ),
                        0.8F
                     );
                     LarpLog.debug("KillFX", "Confirmed player death: id={}, style={}, source=recent observed server damage", var4.getId(), var7);
                  }
               }
            }
         }
      }
   }

   private KillEffect.Style pickStyle() {
      if (!this.style.isMode("Random")) {
         return switch ((String)this.style.getValue()) {
            case "Execution" -> KillEffect.Style.SCATTER;
            case "Singularity" -> KillEffect.Style.VORTEX;
            case "Star of David" -> KillEffect.Style.MODEL_ASSEMBLY;
            case "Tung Tung" -> KillEffect.Style.ITEM_SWING;
            case "Tung Tung Floss" -> KillEffect.Style.EMOTE_DANCE;
            case "Sakura" -> KillEffect.Style.YIN_YANG;
            case "Shatter" -> KillEffect.Style.SPIDER;
            case "Rug Pull" -> KillEffect.Style.RUG_PULL;
            default -> KillEffect.Style.ASCEND;
         };
      } else {
         int var1 = KillEffect.Style.values().length;
         int var2 = Math.floorMod(this.killCounter * 31L + Double.doubleToLongBits(TickTimer.getTickTime()), var1);
         if (var2 == this.lastStyleIndex) {
            var2 = (var2 + 1) % var1;
         }

         this.lastStyleIndex = var2;
         return KillEffect.Style.values()[var2];
      }
   }

   @Override
   public void onTick() {
      if (this.isEnabled()) {
         if (this.reset()) {
            Minecraft var1 = getMc();
            double var2 = TickTimer.getTickTime();
            this.recentEvents.expire(var2, var1x -> var1x instanceof Entity var2x && !var2x.isRemoved() && var2x.level() == var1.level);
            this.finishers.removeIf(var2x -> var2x.effect.isFinished(var2) || var2x.isRetiring() && var2 - var2x.retireTime >= 0.45);

            for (KillFXModule.ActiveFinisher var5 : this.finishers) {
               if (var5.sawDeath && !var5.victim.isRemoved() && var5.victim.isAlive() && !var5.isRetiring()) {
                  var5.retire(var2);
                  LarpLog.debug("KillFX", "Victim id={} alive again; retiring its {} finisher", var5.victim.getId(), var5.effect.style);
               }

               var5.sawDeath = (boolean)(var5.sawDeath | (!var5.victim.isAlive() ? 1 : 0));
               var5.effect.update(var2, (var1x, var2x) -> BlockRaycast.raycastBlock(var1.level, var1x, var2x));
               KillFXModule.SoundCue[] var6 = getSoundCues(var5.effect.style);

               while (var5.cueIndex < var6.length && var2 - var5.effect.startTime >= var6[var5.cueIndex].time()) {
                  KillFXModule.SoundCue var7 = var6[var5.cueIndex++];
                  var5.cueFired = (boolean)1;
                  if (!var5.isRetiring()) {
                     this.reset(var5, var7.event(), var7.pitch());
                  }
               }
            }
         }
      }
   }

   private static KillFXModule.SoundCue[] getSoundCues(KillEffect.Style var0) {
      return switch (var0) {
         case SCATTER -> new KillFXModule.SoundCue[]{new KillFXModule.SoundCue(0.85, (SoundEvent)SoundEvents.TRIDENT_THUNDER.value(), 1.25F)};
         case VORTEX -> new KillFXModule.SoundCue[]{new KillFXModule.SoundCue(1.9, (SoundEvent)SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), 1.25F)};
         case EMOTE_DANCE -> new KillFXModule.SoundCue[]{new KillFXModule.SoundCue(1.62, SoundEvents.PLAYER_BIG_FALL, 1.25F)};
         case ITEM_SWING -> new KillFXModule.SoundCue[]{new KillFXModule.SoundCue(1.56, SoundEvents.ANVIL_LAND, 1.25F)};
         case MODEL_ASSEMBLY -> new KillFXModule.SoundCue[]{new KillFXModule.SoundCue(1.85, SoundEvents.AMETHYST_BLOCK_RESONATE, 1.25F)};
         case ASCEND -> new KillFXModule.SoundCue[]{new KillFXModule.SoundCue(1.25, SoundEvents.BEACON_DEACTIVATE, 1.25F)};
         case YIN_YANG -> new KillFXModule.SoundCue[]{
            new KillFXModule.SoundCue(1.0, SoundEvents.BEACON_ACTIVATE, 1.4F),
            new KillFXModule.SoundCue(3.2, (SoundEvent)SoundEvents.BREEZE_WIND_CHARGE_BURST.value(), 1.1F),
            new KillFXModule.SoundCue(5.0, SoundEvents.AMETHYST_BLOCK_CHIME, 0.9F)
         };
         case SPIDER -> new KillFXModule.SoundCue[]{
            new KillFXModule.SoundCue(0.95, SoundEvents.AMETHYST_BLOCK_CHIME, 1.6F),
            new KillFXModule.SoundCue(1.32, SoundEvents.GLASS_BREAK, 0.75F),
            new KillFXModule.SoundCue(1.87, SoundEvents.AMETHYST_CLUSTER_FALL, 1.3F)
         };
         case RUG_PULL -> new KillFXModule.SoundCue[]{
            new KillFXModule.SoundCue(0.7, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 0.75F),
            new KillFXModule.SoundCue(1.1, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 0.9F),
            new KillFXModule.SoundCue(1.45, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.05F),
            new KillFXModule.SoundCue(1.75, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.25F),
            new KillFXModule.SoundCue(2.0, (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.5F),
            new KillFXModule.SoundCue(2.3, SoundEvents.EXPERIENCE_ORB_PICKUP, 1.6F),
            new KillFXModule.SoundCue(2.6, (SoundEvent)SoundEvents.NOTE_BLOCK_BASS.value(), 0.5F),
            new KillFXModule.SoundCue(2.64, (SoundEvent)SoundEvents.NOTE_BLOCK_DIDGERIDOO.value(), 0.5F),
            new KillFXModule.SoundCue(2.82, SoundEvents.MACE_SMASH_GROUND_HEAVY, 0.8F),
            new KillFXModule.SoundCue(3.25, SoundEvents.VILLAGER_NO, 0.85F)
         };
      };
   }

   private void reset(KillFXModule.ActiveFinisher var1, SoundEvent var2, float var3) {
      if (var1.soundEnabled && !(var1.volume <= 0.0) && getMc().level != null) {
         Vec3 var4 = var1.effect.origin;
         float var10006 = (float)var1.volume;
         getMc().level.playLocalSound(var4.x, var4.y, var4.z, var2, SoundSource.PLAYERS, var10006, var3, false);
      }
   }

   public void prepareFrame(CameraRenderState var1) {
      if (this.isEnabled()) {
         Vec3 var2 = var1.pos;
         if (this.textRenderer != null) {
            this.textRenderer.endBatch();
         }

         this.cameraState = null;
         this.renderEntries = List.of();
         if (this.reset() && !this.finishers.isEmpty()) {
            ArrayList var3 = new ArrayList();
            Vector3f var4 = var1.orientation.transform(new Vector3f(1.0F, 0.0F, 0.0F));
            Vector3f var5 = var1.orientation.transform(new Vector3f(0.0F, 1.0F, 0.0F));
            Vec3 var6 = new Vec3(var4.x, var4.y, var4.z);
            Vec3 var7 = new Vec3(var5.x, var5.y, var5.z);
            this.ghostRenderer.bakeModels();
            ArrayList var8 = new ArrayList();

            for (KillFXModule.ActiveFinisher var10 : this.finishers) {
               if (!(var10.effect.origin.distanceToSqr(var2) > this.renderDistance.getValue() * this.renderDistance.getValue())) {
                  if (var10.skin == null) {
                     if (!(ModelVisualRenderer.extractEntity(var10.victim, 1.0F) instanceof AvatarRenderState var12)) {
                        LarpLog.errorOnce("KillFX", "Native avatar snapshot unavailable; leaving original corpse visible", null);
                        continue;
                     }

                     var10.skin = var12;
                  }

                  double var20 = Math.max(0.0, TickTimer.getTime() - var10.effect.startTime);
                  ModelAssemblyAnimation.Basis var13 = var10.effect.style == KillEffect.Style.MODEL_ASSEMBLY
                     ? ModelAssemblyAnimation.buildBasis(ModelAssemblyAnimation.getHoverPoint(var10.effect, var20), var2, var6, var7)
                     : (
                        var10.effect.isFinished() != null
                           ? var10.effect.isFinished().updateFacing(var2, TickTimer.getTime())
                           : ModelAssemblyAnimation.dEFAULT_BASIS
                     );
                  KillEffect.PartState[] var14 = new KillEffect.PartState[6];
                  double var15 = var10.visibility(TickTimer.getTime());
                  if (var10.effect.isFinished() != null) {
                     if (this.textRenderer == null) {
                        this.textRenderer = new WorldTextRenderer();
                     }

                     if (this.textRenderer.isAvailable()) {
                        try {
                           var10.effect.isFinished().frameForYaw(var13, var20, var15, this.textRenderer::measureWidth, var3);
                        } catch (RuntimeException var19) {
                           LarpLog.errorOnce("KillFX", "Rug Pull chart labels failed; the chart continues without text", var19);
                        }
                     }
                  }

                  for (int var17 = 0; var17 < var14.length; var17++) {
                     KillEffect.PartState var18 = var10.effect.computePartState(var17, var20, TickTimer.getPartialTick(), var13);
                     var14[var17] = var15 >= 1.0
                        ? var18
                        : new KillEffect.PartState(
                           var18.position(),
                           var18.rotX(),
                           var18.rotY(),
                           var18.rotZ(),
                           var18.scale(),
                           var18.alpha() * var15,
                           var18.dissolve(),
                           var18.landed(),
                           var18.orientation(),
                           var18.stretch()
                        );
                  }

                  ItemSwingAnimator.SwingFrame var21 = null;
                  EmoteDanceAnimator.Frame var22 = null;
                  if (var10.effect.style == KillEffect.Style.ITEM_SWING) {
                     SahurPet.prepareStrikePipeline();
                     if (var10.swingAnimator == null) {
                        var10.swingAnimator = new ItemSwingAnimator(SahurPet.getMesh());
                        LarpLog.debug("KillFX", "Tung Tung ready: shared Sahur mesh, height={} blocks, articulated bat strike at {}s", 3.6, 1.56);
                     }

                     var21 = var10.swingAnimator.buildFrame(var10.effect, var20, var2);
                  }

                  if (var10.effect.style == KillEffect.Style.EMOTE_DANCE) {
                     SahurPet.prepareStrikePipeline();
                     if (var10.danceAnimator == null) {
                        var10.danceAnimator = new EmoteDanceAnimator(SahurPet.getMesh());
                        LarpLog.debug("KillFX", "Tung Tung Floss ready: articulated jump, landing at {}s, four Floss cycles, expiry={}s", 1.62, 7.5);
                     }

                     var22 = var10.danceAnimator.animate(var10.effect, var20, var2);
                  }

                  var8.add(new KillFXModule.RenderEntry(var10, var10.skin, var14, var20, var2, var13, var21, var22, this.reloadGeneration, var15));
               }
            }

            this.renderEntries = List.copyOf(var8);
            if (this.textRenderer != null) {
               this.textRenderer.submitLabels(var3, var1);
               this.cameraState = var1;
            }
         }
      }
   }

   public boolean hasFinishers() {
      return !this.finishers.isEmpty();
   }

   public void onEntityEvent(Entity var1, EntityRenderState var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var1.level() == this.trackedLevel) {
            for (KillFXModule.ActiveFinisher var4 : this.finishers) {
               if (var4.victim == var1) {
                  var2.setData(vICTIM_MARK_KEY, new KillFXModule.VictimMark(var1, this.trackedLevel, this.reloadGeneration));
                  return;
               }
            }
         }
      }
   }

   public boolean shouldHideVictim(EntityRenderState var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isRuntimeActive() && VisualsRenderer.isRenderingEnabled() && VisualsRenderer.isStageActive("KillFX") && !ModelVisualRenderer.isProbing()) {
         KillFXModule.VictimMark var2 = (KillFXModule.VictimMark)var1.getData(vICTIM_MARK_KEY);
         if (var2 != null && var2.world == getMc().level && var2.generation == PostProcessing.reloadCount) {
            for (KillFXModule.RenderEntry var4 : this.renderEntries) {
               if (var4.owner.victim == var2.victim
                  && this.finishers.contains(var4.owner)
                  && var4.generation == var2.generation
                  && (var4.age < 0.25 || var1 instanceof LivingEntityRenderState var5 && var5.deathTime > 0.0F)) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean shouldHideVictim() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.renderEntries.stream().anyMatch(var0 -> var0.owner.bloom);
   }

   public void render(EffectsRenderer var1, PoseStack var2, EffectBatch var3, boolean var4) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            long var5 = PerfProfiler.startTimer();

            try {
               for (KillFXModule.RenderEntry var8 : this.renderEntries) {
                  if (var8.generation == PostProcessing.reloadCount && (!var4 || var8.owner.bloom)) {
                     this.ghostRenderer.render(var8.skin, var8.owner.effect, var8.parts, var8.camera, var2, var3, var8.owner.primaryColor, var4);
                     if (!var4 && var8.strike != null) {
                        SahurPet.renderFadingMesh(
                           var2, var3, var8.strike.transform(), var8.strike.vertices(), var8.strike.colors(), var8.strike.opacity() * var8.fade
                        );
                     }

                     if (!var4 && var8.floss != null) {
                        SahurPet.renderFadingMesh(
                           var2, var3, var8.floss.transform(), var8.floss.vertices(), var8.floss.colors(), var8.floss.opacity() * var8.fade
                        );
                     }

                     TeleportEffectRenderer.renderWithAlpha(
                        var1,
                        var8.owner.effect,
                        var8.parts,
                        var8.age,
                        var8.owner.primaryColor,
                        var8.owner.secondaryColor,
                        var4,
                        var8.fade,
                        var8.starPlane,
                        var8.strike,
                        var8.floss
                     );
                  }
               }

               if (!var4 && this.textRenderer != null && this.cameraState != null) {
                  this.textRenderer.renderLabelsFaded(var2, this.cameraState, false, false, 1.0);
               }
            } finally {
               PerfProfiler.endTimer("KillFX", var5);
            }
         }
      }
   }

   private static final class ActiveFinisher {
      final AbstractClientPlayer victim;
      final KillEffect effect;
      final int primaryColor;
      final int secondaryColor;
      final boolean bloom;
      final boolean soundEnabled;
      final double volume;
      AvatarRenderState skin;
      ItemSwingAnimator swingAnimator;
      EmoteDanceAnimator danceAnimator;
      boolean cueFired;
      boolean sawDeath;
      int cueIndex;
      double retireTime = Double.NaN;

      ActiveFinisher(AbstractClientPlayer var1, KillEffect var2, int var3, int var4, boolean var5, boolean var6, double var7) {
         this.victim = var1;
         this.effect = var2;
         this.primaryColor = var3;
         this.secondaryColor = var4;
         this.bloom = var5;
         this.soundEnabled = var6;
         this.volume = var7;
      }

      boolean isRetiring() {
         return !Double.isNaN(this.retireTime);
      }

      void retire(double var1) {
         if (!this.isRetiring()) {
            this.retireTime = var1;
         }
      }

      double visibility(double var1) {
         return this.isRetiring() ? 1.0 - KillEffect.ease((var1 - this.retireTime) / 0.45) : 1.0;
      }
   }

   private record RenderEntry(
      KillFXModule.ActiveFinisher owner,
      AvatarRenderState skin,
      KillEffect.PartState[] parts,
      double age,
      Vec3 camera,
      ModelAssemblyAnimation.Basis starPlane,
      ItemSwingAnimator.SwingFrame strike,
      EmoteDanceAnimator.Frame floss,
      int generation,
      double fade
   ) {
   }

   private record SoundCue(double time, SoundEvent event, float pitch) {
   }

   private record VictimMark(Object victim, Object world, int generation) {
   }
}
