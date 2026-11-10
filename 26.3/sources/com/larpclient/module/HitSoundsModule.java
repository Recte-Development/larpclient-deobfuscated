package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.q1;
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
   private static HitSoundsModule c;
   public static final String[] a = new String[]{
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
   private static final String r = "Impact";
   private final o H = this.addSetting(i("Sound", "Choose the hit sound", "Attack Crit", f(false)));
   private final b u = this.addSetting(
      new b(
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
            this.O(Double.parseDouble(var1.trim().replace("%", "")) / 100.0);
         }
      }
   );
   private final b N = this.addSetting(new b("Pitch", "1 is the original sound; lower is deeper and higher is brighter", 1.0, 0.5, 2.0, 0.05));
   private final m d = this.addSetting(new m("Mob Sounds", "Also play hit sounds for mobs and other non-player entities", true));
   private final m Z = this.addSetting(
      new m("Mute normal hit sounds", "Mute vanilla attack impacts, critical hits and sweeps locally; other sounds stay audible", true)
   );
   private static final Set<Identifier> W = Set.of(
      Identifier.withDefaultNamespace("entity.player.attack.strong"),
      Identifier.withDefaultNamespace("entity.player.attack.weak"),
      Identifier.withDefaultNamespace("entity.player.attack.crit"),
      Identifier.withDefaultNamespace("entity.player.attack.sweep"),
      Identifier.withDefaultNamespace("entity.player.attack.knockback"),
      Identifier.withDefaultNamespace("entity.player.attack.nodamage")
   );
   private final b h = this.addSetting(new b("Pitch Random", "Random pitch variation around the chosen pitch", 0.0, 0.0, 1.0, 0.05));
   private final m b = this.addSetting(new m("Only Players", "Play feedback only for player targets", false));
   private final m I = this.addSetting(new m("Only Crit", "Play feedback only for critical attacks", false));
   private final m t = this.addSetting(new m("Random Sound", "Choose a random sound on each hit", false));
   private final o A = this.addSetting(i("Second Sound", "Layer an additional hit sound", "None", f(true)));
   private SimpleSoundInstance v;
   private long E;
   private SimpleSoundInstance L;
   private ClientLevel k;
   private LocalPlayer o;
   private int D = Integer.MIN_VALUE;
   private boolean U;
   private boolean x;

   private static String[] f(boolean var0) {
      ArrayList var1 = new ArrayList<>(List.of(a));
      if (var0) {
         var1.addFirst("None");
      } else {
         var1.addAll(List.of("Glass", "Round Hit", "Crisp Tick"));
      }

      return var1.toArray(String[]::new);
   }

   private static o i(String var0, String var1, String var2, String[] var3) {
      return new o(var0, var1, var2, var3) {
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
      c = this;
      this.d.a(() -> !this.b.getValue());
      this.H.a(() -> !this.t.getValue());
      this.Z.w(var0 -> _q.z("HitSounds", "Vanilla attack sounds muted: {}", var0));
      this.H.w(var0 -> _q.z("HitSounds", "Sound selected: {}", var0));
   }

   public static HitSoundsModule k() {
      return c;
   }

   public List<r<?>> G() {
      return List.of(this.u, this.H, this.N, this.h, this.t, this.A);
   }

   public List<r<?>> d() {
      return List.of(this.b, this.I, this.Z, this.d);
   }

   public boolean S(SoundInstance var1) {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.Z.getValue()
            && var1 != null
            && var1 != this.L
            && var1 != this.v
            && (this.x || System.nanoTime() <= this.E)
            && W.contains(var1.getIdentifier());
   }

   public Identifier W() {
      return h(this.H.getValue());
   }

   public static Identifier h(String var0) {
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

   public SimpleSoundInstance f() {
      return this.P(this.H.getValue());
   }

   private SimpleSoundInstance P(String var1) {
      float var2 = (float)Math.clamp(this.N.getValue() + ThreadLocalRandom.current().nextDouble(-1.0, 1.0) * this.h.getValue(), 0.5, 2.0);
      return new SimpleSoundInstance(
         h(var1),
         SoundSource.PLAYERS,
         this.u.getValue().floatValue(),
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

   public void P(Entity var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && var1 != null && var1 != this.getPlayer() && var1.isAlive() && !var1.isRemoved()) {
            if (!this.b.getValue() && this.d.getValue() || var1 instanceof Player) {
               if (!this.I.getValue() || q1.t(this.getPlayer(), var1)) {
                  Minecraft var2 = getMc();
                  this.N();
                  int var3 = var2.player.tickCount;
                  if (this.D != var3) {
                     this.D = var3;
                     this.E = System.nanoTime() + 800000000L;
                     if (this.u.getValue() <= 0.0) {
                        this.q("Muted by Hit Sounds Volume=0");
                     } else {
                        try {
                           this.Z(false);
                        } catch (RuntimeException var5) {
                           _q.o("HitSounds", "Local sound playback failed; disabling Hit Sounds", var5);
                           this.setEnabled(false);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void I() {
      Minecraft var1 = getMc();
      if (var1 != null) {
         try {
            this.N();
            this.Z(true);
         } catch (RuntimeException var3) {
            _q.o("HitSounds", "Sound preview failed; reload resources and retry", var3);
         }
      }
   }

   public void t() {
      if (this.x) {
         this.Y();
      }
   }

   private void Z(boolean var1) {
      Minecraft var2 = getMc();
      if (this.L != null) {
         var2.getSoundManager().stop(this.L);
      }

      if (this.v != null) {
         var2.getSoundManager().stop(this.v);
      }

      String var3 = this.t.getValue() ? a[ThreadLocalRandom.current().nextInt(a.length)] : this.H.getValue();
      this.L = this.P(var3);
      this.x = var1;
      PlayResult var4 = var2.getSoundManager().play(this.L);
      this.v = this.A.w("None") ? null : this.P(this.A.getValue());
      if (this.v != null) {
         var2.getSoundManager().play(this.v);
      }

      Sound var5 = this.L.getSound();
      String var6 = (var1 ? "Preview" : "Attack received")
         + "; sound="
         + this.W()
         + ", playback="
         + var4
         + ", moduleVolume="
         + this.u.getValue()
         + ", Players/Master gain="
         + var2.options.getFinalSoundSourceVolume(SoundSource.PLAYERS)
         + ", resolved sample="
         + (var5 == null ? "unresolved" : var5.getLocation())
         + ", resolved volume="
         + (var5 == null ? 0.0F : this.L.getVolume())
         + ", resolved pitch="
         + (var5 == null ? 0.0F : this.L.getPitch());
      if (var1) {
         _q.z("HitSounds", var6);
      } else {
         this.q(var6);
      }
   }

   private void q(String var1) {
      if (!this.U) {
         this.U = (boolean)1;
         _q.z("HitSounds", var1);
      }
   }

   @Override
   public void onEnable() {
      this.Y();
      this.U = (boolean)0;
      _q.z("HitSounds", "Local attack feedback enabled: sound={}, reference sample gain, paired local voices, Minecraft Players volume", this.H.getValue());
   }

   @Override
   public void onTick() {
      this.N();
   }

   @Override
   public void onDisable() {
      this.Y();
   }

   private void N() {
      Minecraft var1 = getMc();
      if (this.k != var1.level || this.o != var1.player) {
         this.Y();
         this.k = var1.level;
         this.o = var1.player;
      }
   }

   private void Y() {
      Minecraft var1 = getMc();
      SimpleSoundInstance var2 = this.L;
      if (this.v != null && var1 != null) {
         var1.getSoundManager().stop(this.v);
      }

      this.v = null;
      this.E = 0L;
      this.L = null;
      this.x = (boolean)0;
      this.k = null;
      this.o = null;
      this.D = Integer.MIN_VALUE;
      if (var2 != null && var1 != null) {
         var1.getSoundManager().stop(var2);
      }
   }
}
