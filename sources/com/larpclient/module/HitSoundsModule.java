package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.pl;
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
   private static HitSoundsModule z;
   public static final String[] i = new String[]{
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
   private static final String t = "Impact";
   private final d6 q = this.addSetting(V("Sound", "Choose the hit sound", "Attack Crit", p(false)));
   private final d9 d = this.addSetting(
      new d9(
         "Volume",
         "Custom hit sound loudness; 0% is silent, 100% is original volume, 200% boosts playback. Players and Master sliders also apply",
         1.0,
         0.0,
         2.0,
         0.05
      ) {
         @Override
         public String getEditValueAsString() {
            return Long.toString(Math.round(this.P() * 100.0));
         }

         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + "%";
         }

         @Override
         public void setFromEditString(String var1) {
            this.o(Double.parseDouble(var1.trim().replace("%", "")) / 100.0);
         }
      }
   );
   private final d9 H = this.addSetting(new d9("Pitch", "1 is the original sound; lower is deeper and higher is brighter", 1.0, 0.5, 2.0, 0.05));
   private final dh I = this.addSetting(new dh("Mob Sounds", "Also play hit sounds for mobs and other non-player entities", true));
   private final dh C = this.addSetting(
      new dh("Mute normal hit sounds", "Mute vanilla attack impacts, critical hits and sweeps locally; other sounds stay audible", true)
   );
   private static final Set<Identifier> R = Set.of(
      Identifier.withDefaultNamespace("entity.player.attack.strong"),
      Identifier.withDefaultNamespace("entity.player.attack.weak"),
      Identifier.withDefaultNamespace("entity.player.attack.crit"),
      Identifier.withDefaultNamespace("entity.player.attack.sweep"),
      Identifier.withDefaultNamespace("entity.player.attack.knockback"),
      Identifier.withDefaultNamespace("entity.player.attack.nodamage")
   );
   private final d9 w = this.addSetting(new d9("Pitch Random", "Random pitch variation around the chosen pitch", 0.0, 0.0, 1.0, 0.05));
   private final dh N = this.addSetting(new dh("Only Players", "Play feedback only for player targets", false));
   private final dh Z = this.addSetting(new dh("Only Crit", "Play feedback only for critical attacks", false));
   private final dh s = this.addSetting(new dh("Random Sound", "Choose a random sound on each hit", false));
   private final d6 x = this.addSetting(V("Second Sound", "Layer an additional hit sound", "None", p(true)));
   private SimpleSoundInstance O;
   private long r;
   private SimpleSoundInstance p;
   private ClientLevel v;
   private LocalPlayer y;
   private int B = Integer.MIN_VALUE;
   private boolean E;
   private boolean j;

   private static String[] p(boolean var0) {
      ArrayList var1 = new ArrayList<>(List.of(i));
      if (var0) {
         var1.addFirst("None");
      } else {
         var1.addAll(List.of("Glass", "Round Hit", "Crisp Tick"));
      }

      return var1.toArray(String[]::new);
   }

   private static d6 V(String var0, String var1, String var2, String[] var3) {
      return new d6(var0, var1, var2, var3) {
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
      z = this;
      this.I.H(() -> !this.N.P());
      this.q.H(() -> !this.s.P());
      this.C.h(var0 -> mt.K("HitSounds", "Vanilla attack sounds muted: {}", var0));
      this.q.h(var0 -> mt.K("HitSounds", "Sound selected: {}", var0));
   }

   public static HitSoundsModule o() {
      return z;
   }

   public List<df<?>> p() {
      return List.of(this.d, this.q, this.H, this.w, this.s, this.x);
   }

   public List<df<?>> k() {
      return List.of(this.N, this.Z, this.C, this.I);
   }

   public boolean x(SoundInstance var1) {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.C.P()
            && var1 != null
            && var1 != this.p
            && var1 != this.O
            && (this.j || System.nanoTime() <= this.r)
            && R.contains(var1.getIdentifier());
   }

   public Identifier B() {
      return f(this.q.P());
   }

   public static Identifier f(String var0) {
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

   public SimpleSoundInstance R() {
      return this.c(this.q.P());
   }

   private SimpleSoundInstance c(String var1) {
      float var2 = (float)Math.clamp(this.H.P() + ThreadLocalRandom.current().nextDouble(-1.0, 1.0) * this.w.P(), 0.5, 2.0);
      return new SimpleSoundInstance(
         f(var1), SoundSource.PLAYERS, this.d.P().floatValue(), var2, SoundInstance.createUnseededRandom(), false, 0, Attenuation.NONE, 0.0, 0.0, 0.0, true
      );
   }

   public void G(Entity var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && var1 != null && var1 != this.getPlayer() && var1.isAlive() && !var1.isRemoved()) {
            if (!this.N.P() && this.I.P() || var1 instanceof Player) {
               if (!this.Z.P() || pl.A(this.getPlayer(), var1)) {
                  Minecraft var2 = getMc();
                  this.J();
                  int var3 = var2.player.tickCount;
                  if (this.B != var3) {
                     this.B = var3;
                     this.r = System.nanoTime() + 800000000L;
                     if (this.d.P() <= 0.0) {
                        this.K("Muted by Hit Sounds Volume=0");
                     } else {
                        try {
                           this.Q(false);
                        } catch (RuntimeException var5) {
                           mt.P("HitSounds", "Local sound playback failed; disabling Hit Sounds", var5);
                           this.setEnabled(false);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void h() {
      Minecraft var1 = getMc();
      if (var1 != null) {
         try {
            this.J();
            this.Q(true);
         } catch (RuntimeException var3) {
            mt.P("HitSounds", "Sound preview failed; reload resources and retry", var3);
         }
      }
   }

   public void d() {
      if (this.j) {
         this.Y();
      }
   }

   private void Q(boolean var1) {
      Minecraft var2 = getMc();
      if (this.p != null) {
         var2.getSoundManager().stop(this.p);
      }

      if (this.O != null) {
         var2.getSoundManager().stop(this.O);
      }

      String var3 = this.s.P() ? i[ThreadLocalRandom.current().nextInt(i.length)] : this.q.P();
      this.p = this.c(var3);
      this.j = var1;
      PlayResult var4 = var2.getSoundManager().play(this.p);
      this.O = this.x.S("None") ? null : this.c(this.x.P());
      if (this.O != null) {
         var2.getSoundManager().play(this.O);
      }

      Sound var5 = this.p.getSound();
      String var6 = (var1 ? "Preview" : "Attack received")
         + "; sound="
         + this.B()
         + ", playback="
         + var4
         + ", moduleVolume="
         + this.d.P()
         + ", Players/Master gain="
         + var2.options.getFinalSoundSourceVolume(SoundSource.PLAYERS)
         + ", resolved sample="
         + (var5 == null ? "unresolved" : var5.getLocation())
         + ", resolved volume="
         + (var5 == null ? 0.0F : this.p.getVolume())
         + ", resolved pitch="
         + (var5 == null ? 0.0F : this.p.getPitch());
      if (var1) {
         mt.K("HitSounds", var6);
      } else {
         this.K(var6);
      }
   }

   private void K(String var1) {
      if (!this.E) {
         this.E = (boolean)1;
         mt.K("HitSounds", var1);
      }
   }

   @Override
   public void onEnable() {
      this.Y();
      this.E = (boolean)0;
      mt.K("HitSounds", "Local attack feedback enabled: sound={}, reference sample gain, paired local voices, Minecraft Players volume", this.q.P());
   }

   @Override
   public void onTick() {
      this.J();
   }

   @Override
   public void onDisable() {
      this.Y();
   }

   private void J() {
      Minecraft var1 = getMc();
      if (this.v != var1.level || this.y != var1.player) {
         this.Y();
         this.v = var1.level;
         this.y = var1.player;
      }
   }

   private void Y() {
      Minecraft var1 = getMc();
      SimpleSoundInstance var2 = this.p;
      if (this.O != null && var1 != null) {
         var1.getSoundManager().stop(this.O);
      }

      this.O = null;
      this.r = 0L;
      this.p = null;
      this.j = (boolean)0;
      this.v = null;
      this.y = null;
      this.B = Integer.MIN_VALUE;
      if (var2 != null && var1 != null) {
         var1.getSoundManager().stop(var2);
      }
   }
}
