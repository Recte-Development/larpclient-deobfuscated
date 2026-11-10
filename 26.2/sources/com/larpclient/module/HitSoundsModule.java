package com.larpclient.module;

import com.larpclient.kq;
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
   private static HitSoundsModule R;
   public static final String[] L = new String[]{
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
   private static final String G = "Impact";
   private final l c = this.addSetting(n("Sound", "Choose the hit sound", "Attack Crit", Z(false)));
   private final e l = this.addSetting(
      new e(
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
            this.X(Double.parseDouble(var1.trim().replace("%", "")) / 100.0);
         }
      }
   );
   private final e Q = this.addSetting(new e("Pitch", "1 is the original sound; lower is deeper and higher is brighter", 1.0, 0.5, 2.0, 0.05));
   private final s j = this.addSetting(new s("Mob Sounds", "Also play hit sounds for mobs and other non-player entities", true));
   private final s B = this.addSetting(
      new s("Mute normal hit sounds", "Mute vanilla attack impacts, critical hits and sweeps locally; other sounds stay audible", true)
   );
   private static final Set<Identifier> Y = Set.of(
      Identifier.withDefaultNamespace("entity.player.attack.strong"),
      Identifier.withDefaultNamespace("entity.player.attack.weak"),
      Identifier.withDefaultNamespace("entity.player.attack.crit"),
      Identifier.withDefaultNamespace("entity.player.attack.sweep"),
      Identifier.withDefaultNamespace("entity.player.attack.knockback"),
      Identifier.withDefaultNamespace("entity.player.attack.nodamage")
   );
   private final e p = this.addSetting(new e("Pitch Random", "Random pitch variation around the chosen pitch", 0.0, 0.0, 1.0, 0.05));
   private final s M = this.addSetting(new s("Only Players", "Play feedback only for player targets", false));
   private final s w = this.addSetting(new s("Only Crit", "Play feedback only for critical attacks", false));
   private final s D = this.addSetting(new s("Random Sound", "Choose a random sound on each hit", false));
   private final l d = this.addSetting(n("Second Sound", "Layer an additional hit sound", "None", Z(true)));
   private SimpleSoundInstance O;
   private long f;
   private SimpleSoundInstance b;
   private ClientLevel h;
   private LocalPlayer K;
   private int H = Integer.MIN_VALUE;
   private boolean y;
   private boolean e;

   private static String[] Z(boolean var0) {
      ArrayList var1 = new ArrayList<>(List.of(L));
      if (var0) {
         var1.addFirst("None");
      } else {
         var1.addAll(List.of("Glass", "Round Hit", "Crisp Tick"));
      }

      return var1.toArray(String[]::new);
   }

   private static l n(String var0, String var1, String var2, String[] var3) {
      return new l(var0, var1, var2, var3) {
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
      R = this;
      this.j.P(() -> !this.M.getValue());
      this.c.P(() -> !this.D.getValue());
      this.B.o(var0 -> com.larpclient.h.A("HitSounds", "Vanilla attack sounds muted: {}", var0));
      this.c.o(var0 -> com.larpclient.h.A("HitSounds", "Sound selected: {}", var0));
   }

   public static HitSoundsModule v() {
      return R;
   }

   public List<q<?>> A() {
      return List.of(this.l, this.c, this.Q, this.p, this.D, this.d);
   }

   public List<q<?>> I() {
      return List.of(this.M, this.w, this.B, this.j);
   }

   public boolean E(SoundInstance var1) {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.B.getValue()
            && var1 != null
            && var1 != this.b
            && var1 != this.O
            && (this.e || System.nanoTime() <= this.f)
            && Y.contains(var1.getIdentifier());
   }

   public Identifier f() {
      return V(this.c.getValue());
   }

   public static Identifier V(String var0) {
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

   public SimpleSoundInstance D() {
      return this.I(this.c.getValue());
   }

   private SimpleSoundInstance I(String var1) {
      float var2 = (float)Math.clamp(this.Q.getValue() + ThreadLocalRandom.current().nextDouble(-1.0, 1.0) * this.p.getValue(), 0.5, 2.0);
      return new SimpleSoundInstance(
         V(var1),
         SoundSource.PLAYERS,
         this.l.getValue().floatValue(),
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

   public void V(Entity var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && var1 != null && var1 != this.getPlayer() && var1.isAlive() && !var1.isRemoved()) {
            if (!this.M.getValue() && this.j.getValue() || var1 instanceof Player) {
               if (!this.w.getValue() || kq.V(this.getPlayer(), var1)) {
                  Minecraft var2 = getMc();
                  this.E();
                  int var3 = var2.player.tickCount;
                  if (this.H != var3) {
                     this.H = var3;
                     this.f = System.nanoTime() + 800000000L;
                     if (this.l.getValue() <= 0.0) {
                        this.p("Muted by Hit Sounds Volume=0");
                     } else {
                        try {
                           this.g(false);
                        } catch (RuntimeException var5) {
                           com.larpclient.h.m("HitSounds", "Local sound playback failed; disabling Hit Sounds", var5);
                           this.setEnabled(false);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void x() {
      Minecraft var1 = getMc();
      if (var1 != null) {
         try {
            this.E();
            this.g(true);
         } catch (RuntimeException var3) {
            com.larpclient.h.m("HitSounds", "Sound preview failed; reload resources and retry", var3);
         }
      }
   }

   public void z() {
      if (this.e) {
         this.D();
      }
   }

   private void g(boolean var1) {
      Minecraft var2 = getMc();
      if (this.b != null) {
         var2.getSoundManager().stop(this.b);
      }

      if (this.O != null) {
         var2.getSoundManager().stop(this.O);
      }

      String var3 = this.D.getValue() ? L[ThreadLocalRandom.current().nextInt(L.length)] : this.c.getValue();
      this.b = this.I(var3);
      this.e = var1;
      PlayResult var4 = var2.getSoundManager().play(this.b);
      this.O = this.d.O("None") ? null : this.I(this.d.getValue());
      if (this.O != null) {
         var2.getSoundManager().play(this.O);
      }

      Sound var5 = this.b.getSound();
      String var6 = (var1 ? "Preview" : "Attack received")
         + "; sound="
         + this.f()
         + ", playback="
         + var4
         + ", moduleVolume="
         + this.l.getValue()
         + ", Players/Master gain="
         + var2.options.getFinalSoundSourceVolume(SoundSource.PLAYERS)
         + ", resolved sample="
         + (var5 == null ? "unresolved" : var5.getLocation())
         + ", resolved volume="
         + (var5 == null ? 0.0F : this.b.getVolume())
         + ", resolved pitch="
         + (var5 == null ? 0.0F : this.b.getPitch());
      if (var1) {
         com.larpclient.h.A("HitSounds", var6);
      } else {
         this.p(var6);
      }
   }

   private void p(String var1) {
      if (!this.y) {
         this.y = (boolean)1;
         com.larpclient.h.A("HitSounds", var1);
      }
   }

   @Override
   public void onEnable() {
      this.D();
      this.y = (boolean)0;
      com.larpclient.h.A(
         "HitSounds", "Local attack feedback enabled: sound={}, reference sample gain, paired local voices, Minecraft Players volume", this.c.getValue()
      );
   }

   @Override
   public void onTick() {
      this.E();
   }

   @Override
   public void onDisable() {
      this.D();
   }

   private void E() {
      Minecraft var1 = getMc();
      if (this.h != var1.level || this.K != var1.player) {
         this.D();
         this.h = var1.level;
         this.K = var1.player;
      }
   }

   private void D() {
      Minecraft var1 = getMc();
      SimpleSoundInstance var2 = this.b;
      if (this.O != null && var1 != null) {
         var1.getSoundManager().stop(this.O);
      }

      this.O = null;
      this.f = 0L;
      this.b = null;
      this.e = (boolean)0;
      this.h = null;
      this.K = null;
      this.H = Integer.MIN_VALUE;
      if (var2 != null && var1 != null) {
         var1.getSoundManager().stop(var2);
      }
   }
}
