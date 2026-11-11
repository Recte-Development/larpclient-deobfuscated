package com.larpclient.config;

import com.larpclient.render.particle.ParticleSystem;
import com.larpclient.util.math.Easing;
import net.minecraft.world.phys.Vec3;

public final class HitEffectConfig {
   public HitEffectConfig.Mode mode = HitEffectConfig.Mode.DEFAULT;
   public double durationOverride = Double.NaN;
   public Vec3 offset = new Vec3(0.0, 1.0, 0.0);
   public HitEffectConfig.Texture texture = HitEffectConfig.Texture.EFFECTS;
   public HitEffectConfig.Shape shape = HitEffectConfig.Shape.QUAD;
   public ParticleSystem.Style style = ParticleSystem.Style.GLOW;
   public HitEffectConfig.BlendMode blendMode = HitEffectConfig.BlendMode.ADDITIVE;
   public Easing.FadeCurve colorScheme = Easing.FadeCurve.QUICK_ATTACK_SMOOTH;
   public Easing.SizeCurve easing = Easing.SizeCurve.POP;
   public int particleCount = 8;
   public int primaryColor = -1;
   public int secondaryColor = -1;
   public int seed;
   public double size = 0.3;
   public double speed = 0.65;
   public double gravity = 0.04;
   public double spread = 0.1;
   public double scale = 1.0;
   public double stretch = 1.0;
   public double opacity = 1.0;
   public double intensity = 0.7;
   public double brightness = 1.0;
   public double glow = 0.3;
   public double lifetime = 0.4;
   public double fadeIn = 1.2;
   public double rotation;
   public double drag = 0.2;
   public double turbulence;
   public double radius = 3.0;
   public double pitchOffset;
   public double yawOffset;
   public double spin;
   public double density = 1.0;
   public double jitter = 0.035;
   public double tilt;
   public double roll;
   public double delay;
   public double falloff = 5.0;
   public int maxParticles = 2048;
   public double heightOverride = Double.NaN;
   public boolean mirrored;
   public boolean additive;
   public double globalScale = 1.0;
   public double maxDistance = Double.NaN;
   public boolean enabled = (boolean)1;

   public enum BlendMode {
      NORMAL,
      ADDITIVE,
      SCREEN;
   }

   public enum Mode {
      DEFAULT,
      BURST,
      TRAIL;
   }

   public enum Shape {
      QUAD,
      RING,
      STAR,
      SPHERE,
      CROSS;
   }

   public enum Texture {
      EFFECTS("effects.png"),
      HITFX("hitfx.png"),
      BLOOM("bloom.png");

      public final String FILENAME;

      Texture(String var3) {
         this.FILENAME = var3;
      }
   }
}
