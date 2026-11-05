package com.larpclient.module;

import com.larpclient.r4;
import com.larpclient.xq;
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
   private static HitSoundsModule D;
   public static final String[] J = new String[]{
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
   private static final String A = "Impact";
   private final lp M = this.addSetting(G("Sound", "Choose the hit sound", "Attack Crit", K(false)));
   private final lc W = this.addSetting(
      new lc(
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
            this.C(Double.parseDouble(var1.trim().replace("%", "")) / 100.0);
         }
      }
   );
   private final lc m = this.addSetting(new lc("Pitch", "1 is the original sound; lower is deeper and higher is brighter", 1.0, 0.5, 2.0, 0.05));
   private final ln I = this.addSetting(new ln("Mob Sounds", "Also play hit sounds for mobs and other non-player entities", true));
   private final ln i = this.addSetting(
      new ln("Mute normal hit sounds", "Mute vanilla attack impacts, critical hits and sweeps locally; other sounds stay audible", true)
   );
   private static final Set<Identifier> s = Set.of(
      Identifier.withDefaultNamespace("entity.player.attack.strong"),
      Identifier.withDefaultNamespace("entity.player.attack.weak"),
      Identifier.withDefaultNamespace("entity.player.attack.crit"),
      Identifier.withDefaultNamespace("entity.player.attack.sweep"),
      Identifier.withDefaultNamespace("entity.player.attack.knockback"),
      Identifier.withDefaultNamespace("entity.player.attack.nodamage")
   );
   private final lc K = this.addSetting(new lc("Pitch Random", "Random pitch variation around the chosen pitch", 0.0, 0.0, 1.0, 0.05));
   private final ln B = this.addSetting(new ln("Only Players", "Play feedback only for player targets", false));
   private final ln N = this.addSetting(new ln("Only Crit", "Play feedback only for critical attacks", false));
   private final ln g = this.addSetting(new ln("Random Sound", "Choose a random sound on each hit", false));
   private final lp V = this.addSetting(G("Second Sound", "Layer an additional hit sound", "None", K(true)));
   private SimpleSoundInstance p;
   private long F;
   private SimpleSoundInstance H;
   private ClientLevel E;
   private LocalPlayer v;
   private int C = Integer.MIN_VALUE;
   private boolean n;
   private boolean x;

   private static String[] K(boolean var0) {
      ArrayList var1 = new ArrayList<>(List.of(J));
      if (var0) {
         var1.addFirst("None");
      } else {
         var1.addAll(List.of("Glass", "Round Hit", "Crisp Tick"));
      }

      return var1.toArray(String[]::new);
   }

   private static lp G(String var0, String var1, String var2, String[] var3) {
      return new lp(var0, var1, var2, var3) {
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
      D = this;
      this.I.x(() -> !this.B.getValue());
      this.M.x(() -> !this.g.getValue());
      this.i.W(var0 -> r4.V("HitSounds", "Vanilla attack sounds muted: {}", var0));
      this.M.W(var0 -> r4.V("HitSounds", "Sound selected: {}", var0));
   }

   public static HitSoundsModule q() {
      return D;
   }

   public List<l6<?>> N() {
      return List.of(this.W, this.M, this.m, this.K, this.g, this.V);
   }

   public List<l6<?>> x() {
      return List.of(this.B, this.N, this.i, this.I);
   }

   public boolean H(SoundInstance var1) {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.i.getValue()
            && var1 != null
            && var1 != this.H
            && var1 != this.p
            && (this.x || System.nanoTime() <= this.F)
            && s.contains(var1.getIdentifier());
   }

   public Identifier a() {
      return F(this.M.getValue());
   }

   public static Identifier F(String var0) {
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

   public SimpleSoundInstance j() {
      return this.j(this.M.getValue());
   }

   private SimpleSoundInstance j(String var1) {
      float var2 = (float)Math.clamp(this.m.getValue() + ThreadLocalRandom.current().nextDouble(-1.0, 1.0) * this.K.getValue(), 0.5, 2.0);
      return new SimpleSoundInstance(
         F(var1),
         SoundSource.PLAYERS,
         this.W.getValue().floatValue(),
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

   public void X(Entity var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && var1 != null && var1 != this.getPlayer() && var1.isAlive() && !var1.isRemoved()) {
            if (!this.B.getValue() && this.I.getValue() || var1 instanceof Player) {
               if (!this.N.getValue() || xq.M(this.getPlayer(), var1)) {
                  Minecraft var2 = getMc();
                  this.t();
                  int var3 = var2.player.tickCount;
                  if (this.C != var3) {
                     this.C = var3;
                     this.F = System.nanoTime() + 800000000L;
                     if (this.W.getValue() <= 0.0) {
                        this.H("Muted by Hit Sounds Volume=0");
                     } else {
                        try {
                           this.X(false);
                        } catch (RuntimeException var5) {
                           r4.r("HitSounds", "Local sound playback failed; disabling Hit Sounds", var5);
                           this.setEnabled(false);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void z() {
      Minecraft var1 = getMc();
      if (var1 != null) {
         try {
            this.t();
            this.X(true);
         } catch (RuntimeException var3) {
            r4.r("HitSounds", "Sound preview failed; reload resources and retry", var3);
         }
      }
   }

   public void T() {
      if (this.x) {
         this.j();
      }
   }

   private void X(boolean var1) {
      Minecraft var2 = getMc();
      if (this.H != null) {
         var2.getSoundManager().stop(this.H);
      }

      if (this.p != null) {
         var2.getSoundManager().stop(this.p);
      }

      String var3 = this.g.getValue() ? J[ThreadLocalRandom.current().nextInt(J.length)] : this.M.getValue();
      this.H = this.j(var3);
      this.x = var1;
      PlayResult var4 = var2.getSoundManager().play(this.H);
      this.p = this.V.i("None") ? null : this.j(this.V.getValue());
      if (this.p != null) {
         var2.getSoundManager().play(this.p);
      }

      Sound var5 = this.H.getSound();
      String var6 = (var1 ? "Preview" : "Attack received")
         + "; sound="
         + this.a()
         + ", playback="
         + var4
         + ", moduleVolume="
         + this.W.getValue()
         + ", Players/Master gain="
         + var2.options.getFinalSoundSourceVolume(SoundSource.PLAYERS)
         + ", resolved sample="
         + (var5 == null ? "unresolved" : var5.getLocation())
         + ", resolved volume="
         + (var5 == null ? 0.0F : this.H.getVolume())
         + ", resolved pitch="
         + (var5 == null ? 0.0F : this.H.getPitch());
      if (var1) {
         r4.V("HitSounds", var6);
      } else {
         this.H(var6);
      }
   }

   private void H(String var1) {
      if (!this.n) {
         this.n = (boolean)1;
         r4.V("HitSounds", var1);
      }
   }

   @Override
   public void onEnable() {
      this.j();
      this.n = (boolean)0;
      r4.V("HitSounds", "Local attack feedback enabled: sound={}, reference sample gain, paired local voices, Minecraft Players volume", this.M.getValue());
   }

   @Override
   public void onTick() {
      this.t();
   }

   @Override
   public void onDisable() {
      this.j();
   }

   private void t() {
      Minecraft var1 = getMc();
      if (this.E != var1.level || this.v != var1.player) {
         this.j();
         this.E = var1.level;
         this.v = var1.player;
      }
   }

   private void j() {
      Minecraft var1 = getMc();
      SimpleSoundInstance var2 = this.H;
      if (this.p != null && var1 != null) {
         var1.getSoundManager().stop(this.p);
      }

      this.p = null;
      this.F = 0L;
      this.H = null;
      this.x = (boolean)0;
      this.E = null;
      this.v = null;
      this.C = Integer.MIN_VALUE;
      if (var2 != null && var1 != null) {
         var1.getSoundManager().stop(var2);
      }
   }
}
