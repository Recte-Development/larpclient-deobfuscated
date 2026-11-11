package com.larpclient.module.misc;

import com.larpclient.util.entity.CombatState;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.client.sounds.SoundEngine.PlayResult;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class HitSoundsModule extends Module {
   private static HitSoundsModule instance;
   public static final String[] sOUND_NAMES = new String[]{
      "Attack Crit",
      "Attack Strong",
      "Attack Weak",
      "Attack Sweep",
      "Attack Knockback",
      "Orb Pickup",
      "Level Up",
      "Totem Use",
      "Anvil Land",
      "Bell",
      "Anime",
      "Blade Ball",
      "Chink",
      "Click",
      "Copper Bell",
      "Crystal",
      "Glass 1",
      "Glass 2",
      "Metal Bat",
      "Neverlose",
      "Punch",
      "Slice",
      "Tap",
      "Water",
      "Rust Headshot"
   };
   private static final String iMPACT_ALIAS = "Impact";
   private final ModeSetting sound = this.addSetting(createSoundSetting("Sound", "Choose the hit sound", "Attack Crit", getSoundNames(false)));
   private final NumberSetting volume = this.addSetting(
      new NumberSetting(
         "Volume",
         "Custom hit sound loudness; 0% is silent, 100% is original volume, 200% boosts playback. Players and Master sliders also apply",
         1.0,
         0.0,
         2.0,
         0.05
      ) {
         @Override
         public String getEditValueAsString() {
            return Long.toString(Math.round(this.getValue() * 100.0));
         }

         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + "%";
         }

         @Override
         public void setFromEditString(String var1) {
            this.getDisplayValue(Double.parseDouble(var1.trim().replace("%", "")) / 100.0);
         }
      }
   );
   private final NumberSetting pitch = this.addSetting(
      new NumberSetting("Pitch", "1 is the original sound; lower is deeper and higher is brighter", 1.0, 0.5, 2.0, 0.05)
   );
   private final BooleanSetting mobSounds = this.addSetting(
      new BooleanSetting("Mob Sounds", "Also play hit sounds for mobs and other non-player entities", true)
   );
   private final BooleanSetting muteVanillaSounds = this.addSetting(
      new BooleanSetting("Mute normal hit sounds", "Mute vanilla attack impacts, critical hits and sweeps locally; other sounds stay audible", true)
   );
   private static final Set<Identifier> vANILLA_ATTACK_SOUNDS = Set.of(
      Identifier.withDefaultNamespace("entity.player.attack.strong"),
      Identifier.withDefaultNamespace("entity.player.attack.weak"),
      Identifier.withDefaultNamespace("entity.player.attack.crit"),
      Identifier.withDefaultNamespace("entity.player.attack.sweep"),
      Identifier.withDefaultNamespace("entity.player.attack.knockback"),
      Identifier.withDefaultNamespace("entity.player.attack.nodamage")
   );
   private final NumberSetting pitchRandom = this.addSetting(
      new NumberSetting("Pitch Random", "Random pitch variation around the chosen pitch", 0.0, 0.0, 1.0, 0.05)
   );
   private final BooleanSetting onlyPlayers = this.addSetting(new BooleanSetting("Only Players", "Play feedback only for player targets", false));
   private final BooleanSetting onlyCrit = this.addSetting(new BooleanSetting("Only Crit", "Play feedback only for critical attacks", false));
   private final BooleanSetting randomSound = this.addSetting(new BooleanSetting("Random Sound", "Choose a random sound on each hit", false));
   private final ModeSetting secondSound = this.addSetting(createSoundSetting("Second Sound", "Layer an additional hit sound", "None", getSoundNames(true)));
   private SimpleSoundInstance secondInstance;
   private long muteUntilNanos;
   private SimpleSoundInstance primaryInstance;
   private ClientLevel level;
   private LocalPlayer player;
   private int lastHitTick = Integer.MIN_VALUE;
   private boolean loggedPlayback;
   private boolean previewing;

   private static String[] getSoundNames(boolean var0) {
      ArrayList var1 = new ArrayList<>(List.of(sOUND_NAMES));
      if (var0) {
         var1.addFirst("None");
      } else {
         var1.addAll(List.of("Glass", "Round Hit", "Crisp Tick"));
      }

      return var1.toArray(String[]::new);
   }

   private static ModeSetting createSoundSetting(String var0, String var1, String var2, String[] var3) {
      return new ModeSetting(var0, var1, var2, var3) {
         @Override
         public boolean acceptsValue(String var1) {
            return "Impact".equals(var1) || super.acceptsValue(var1);
         }

         @Override
         public void setValue(String var1) {
            super.setValue("Impact".equals(var1) ? "Rust Headshot" : var1);
         }
      };
   }

   public HitSoundsModule() {
      super("Hit Sounds", "Play custom sounds when you hit targets", Category.VISUALS, -1);
      instance = this;
      this.mobSounds.visibleWhen(() -> !this.onlyPlayers.getValue());
      this.sound.visibleWhen(() -> !this.randomSound.getValue());
      this.muteVanillaSounds.onChange(var0 -> LarpLog.info("HitSounds", "Vanilla attack sounds muted: {}", var0));
      this.sound.onChange(var0 -> LarpLog.info("HitSounds", "Sound selected: {}", var0));
   }

   public static HitSoundsModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getSoundSettings() {
      return List.of(this.volume, this.sound, this.pitch, this.pitchRandom, this.randomSound, this.secondSound);
   }

   public List<Setting<?>> getFilterSettings() {
      return List.of(this.onlyPlayers, this.onlyCrit, this.muteVanillaSounds, this.mobSounds);
   }

   public boolean shouldMute(SoundInstance var1) {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.muteVanillaSounds.getValue()
            && var1 != null
            && var1 != this.primaryInstance
            && var1 != this.secondInstance
            && (this.previewing || System.nanoTime() <= this.muteUntilNanos)
            && vANILLA_ATTACK_SOUNDS.contains(var1.getIdentifier());
   }

   public Identifier getSelectedSoundId() {
      return getSoundId(this.sound.getValue());
   }

   public static Identifier getSoundId(String var0) {
      String var1 = switch (var0) {
         case "Attack Crit" -> "entity.player.attack.crit";
         case "Attack Strong" -> "entity.player.attack.strong";
         case "Attack Weak" -> "entity.player.attack.weak";
         case "Attack Sweep" -> "entity.player.attack.sweep";
         case "Attack Knockback" -> "entity.player.attack.knockback";
         case "Orb Pickup" -> "entity.experience_orb.pickup";
         case "Level Up" -> "entity.player.levelup";
         case "Totem Use" -> "item.totem.use";
         case "Anvil Land" -> "block.anvil.land";
         case "Bell" -> "block.bell.use";
         default -> null;
      };
      if (var1 != null) {
         return Identifier.withDefaultNamespace(var1);
      }

      String var5 = switch (var0) {
         case "Glass" -> "glass";
         case "Round Hit" -> "round_hit";
         case "Crisp Tick" -> "crisp_tick";
         case "Rust Headshot" -> "impact";
         default -> "clarity_" + var0.toLowerCase(Locale.ROOT).replace(" ", "");
      };
      return Identifier.fromNamespaceAndPath("larp-client", "hit." + var5);
   }

   public SimpleSoundInstance getSoundNames() {
      return this.createSoundInstance(this.sound.getValue());
   }

   private SimpleSoundInstance createSoundInstance(String var1) {
      float var2 = (float)Math.clamp(this.pitch.getValue() + ThreadLocalRandom.current().nextDouble(-1.0, 1.0) * this.pitchRandom.getValue(), 0.5, 2.0);
      return new SimpleSoundInstance(
         getSoundId(var1),
         SoundSource.PLAYERS,
         this.volume.getValue().floatValue(),
         var2,
         SoundInstance.createUnseededRandom(),
         false,
         0,
         Attenuation.NONE,
         0.0,
         0.0,
         0.0,
         true
      );
   }

   public void createSoundInstance(Entity var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && var1 != null && var1 != this.getPlayer() && var1.isAlive() && !var1.isRemoved()) {
            if (!this.onlyPlayers.getValue() && this.mobSounds.getValue() || var1 instanceof Player) {
               if (!this.onlyCrit.getValue() || CombatState.canCriticalClient(this.getPlayer(), var1)) {
                  Minecraft var2 = getMc();
                  this.resetIfWorldChanged();
                  int var3 = var2.player.tickCount;
                  if (this.lastHitTick != var3) {
                     this.lastHitTick = var3;
                     this.muteUntilNanos = System.nanoTime() + 800000000L;
                     if (this.volume.getValue() <= 0.0) {
                        this.logOnce("Muted by Hit Sounds Volume=0");
                     } else {
                        try {
                           this.playSounds(false);
                        } catch (RuntimeException var5) {
                           LarpLog.error("HitSounds", "Local sound playback failed; disabling Hit Sounds", var5);
                           this.setEnabled(false);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void previewSound() {
      Minecraft var1 = getMc();
      if (var1 != null) {
         try {
            this.resetIfWorldChanged();
            this.playSounds(true);
         } catch (RuntimeException var3) {
            LarpLog.error("HitSounds", "Sound preview failed; reload resources and retry", var3);
         }
      }
   }

   public void stopPreview() {
      if (this.previewing) {
         this.stopSounds();
      }
   }

   private void playSounds(boolean var1) {
      Minecraft var2 = getMc();
      if (this.primaryInstance != null) {
         var2.getSoundManager().stop(this.primaryInstance);
      }

      if (this.secondInstance != null) {
         var2.getSoundManager().stop(this.secondInstance);
      }

      String var3 = this.randomSound.getValue() ? sOUND_NAMES[ThreadLocalRandom.current().nextInt(sOUND_NAMES.length)] : this.sound.getValue();
      this.primaryInstance = this.createSoundInstance(var3);
      this.previewing = var1;
      PlayResult var4 = var2.getSoundManager().play(this.primaryInstance);
      this.secondInstance = this.secondSound.isMode("None") ? null : this.createSoundInstance(this.secondSound.getValue());
      if (this.secondInstance != null) {
         var2.getSoundManager().play(this.secondInstance);
      }

      Sound var5 = this.primaryInstance.getSound();
      String var6 = (var1 ? "Preview" : "Attack received")
         + "; sound="
         + this.getSelectedSoundId()
         + ", playback="
         + var4
         + ", moduleVolume="
         + this.volume.getValue()
         + ", Players/Master gain="
         + var2.options.getFinalSoundSourceVolume(SoundSource.PLAYERS)
         + ", resolved sample="
         + (var5 == null ? "unresolved" : var5.getLocation())
         + ", resolved volume="
         + (var5 == null ? 0.0F : this.primaryInstance.getVolume())
         + ", resolved pitch="
         + (var5 == null ? 0.0F : this.primaryInstance.getPitch());
      if (var1) {
         LarpLog.info("HitSounds", var6);
      } else {
         this.logOnce(var6);
      }
   }

   private void logOnce(String var1) {
      if (!this.loggedPlayback) {
         this.loggedPlayback = (boolean)1;
         LarpLog.info("HitSounds", var1);
      }
   }

   @Override
   public void onEnable() {
      this.stopSounds();
      this.loggedPlayback = (boolean)0;
      LarpLog.info(
         "HitSounds", "Local attack feedback enabled: sound={}, reference sample gain, paired local voices, Minecraft Players volume", this.sound.getValue()
      );
   }

   @Override
   public void onTick() {
      this.resetIfWorldChanged();
   }

   @Override
   public void onDisable() {
      this.stopSounds();
   }

   private void resetIfWorldChanged() {
      Minecraft var1 = getMc();
      if (this.level != var1.level || this.player != var1.player) {
         this.stopSounds();
         this.level = var1.level;
         this.player = var1.player;
      }
   }

   private void stopSounds() {
      Minecraft var1 = getMc();
      SimpleSoundInstance var2 = this.primaryInstance;
      if (this.secondInstance != null && var1 != null) {
         var1.getSoundManager().stop(this.secondInstance);
      }

      this.secondInstance = null;
      this.muteUntilNanos = 0L;
      this.primaryInstance = null;
      this.previewing = (boolean)0;
      this.level = null;
      this.player = null;
      this.lastHitTick = Integer.MIN_VALUE;
      if (var2 != null && var1 != null) {
         var1.getSoundManager().stop(var2);
      }
   }
}
