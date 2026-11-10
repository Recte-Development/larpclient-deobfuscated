package com.larpclient.module;

import com.larpclient.g8;
import com.larpclient.ho;
import com.larpclient.j4;
import com.larpclient.kz;
import com.larpclient.l_;
import com.larpclient.p2;
import com.larpclient.sm;
import java.util.List;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public final class SelfVisualsModule extends Module {
   private static SelfVisualsModule T;
   public final l n = this.addSetting(
      new l(
         "Style",
         "Player material",
         "Rainbow Outline",
         "Rainbow Outline",
         "Hologram",
         "Comets",
         "Outline",
         "Liquid Glass",
         "Solid",
         "Glow",
         "Metallic",
         "Camo",
         "Marble",
         "Glitter",
         "Ghost"
      )
   );
   public final e t = this.addSetting(new e("Rainbow Speed", "Cycles per second", 0.18, 0.0, 2.0, 0.01));
   public final l p = this.addSetting(new l("Direction", "Gradient flow", "Downward", "Downward", "Upward", "Horizontal", "Static"));
   public final e w = this.addSetting(new e("Gradient Scale", "Rainbow cycles per block", 0.5, 0.1, 2.0, 0.05));
   public final e Q = this.addSetting(new e("Saturation", "Rainbow saturation", 0.62, 0.0, 1.0, 0.02));
   public final e E = this.addSetting(new e("Brightness", "Rainbow brightness", 0.95, 0.1, 1.0, 0.025));
   public final e I = this.addSetting(new e("Model Opacity", "Body opacity", 1.0, 0.05, 1.0, 0.05));
   public final s V = this.addSetting(new s("Outline", "Screen-space silhouette outline", true));
   public final i W = this.addSetting(new i("Outline Color", "Silhouette border", -1));
   public final e u = this.addSetting(new e("Outline Thickness", "Screen pixels", 1.5, 0.5, 5.0, 0.5));
   public final e z = this.addSetting(new e("Outline Opacity", "Border opacity", 0.7, 0.0, 1.0, 0.05));
   public final s e = this.addSetting(new s("Outline Bloom", "Soft halo outside border", false));
   public final s Y = this.addSetting(new s("Model Bloom", "Explicit body glow", false));
   public final s U = this.addSetting(new s("Affect Cape", "Apply Rainbow Outline to your Minecraft cape", false));
   public final e G = this.addSetting(new e("Comet Size", "Core radius in blocks", 0.055, 0.025, 0.12, 0.005));
   public final e X = this.addSetting(new e("Orbit Speed", "Orbits per second", 0.32, 0.05, 1.2, 0.01));
   public final e S = this.addSetting(new e("Orbit Radius", "Blocks; kept outside the current body", 0.52, 0.3, 1.0, 0.01));
   public final e m = this.addSetting(new e("Trail Length", "Seconds of previous orbit positions", 0.24, 0.08, 0.6, 0.01));
   public final e l = this.addSetting(new e("Glow Intensity", "Halo and trail brightness", 1.0, 0.2, 2.0, 0.05));
   public final s s = this.addSetting(new s("Comet Bloom", "Submit comets to the shared bloom pass", true));
   public final s N = this.addSetting(new s("Theme Colors", "Use current theme accents for comets", true));
   public final i g = this.addSetting(new i("Comet Color", "Core and trail head", ho.J));
   public final i k = this.addSetting(new i("Tail Color", "Faded trail end", ho.ds));
   public final e x = this.addSetting(new e("Glass Opacity", "Liquid surface opacity", 0.62, 0.1, 1.0, 0.02));
   public final e o = this.addSetting(new e("Refraction Strength", "Screen displacement in pixels", 3.5, 0.0, 12.0, 0.25));
   public final e B = this.addSetting(new e("Surface Flow", "Fluid surface motion speed", 0.65, 0.0, 2.0, 0.05));
   public final e L = this.addSetting(new e("Gloss", "Broad soft reflections to tight highlights", 0.78, 0.0, 1.0, 0.02));
   public final e f = this.addSetting(new e("Edge Highlight", "Viewing-angle surface highlights", 0.8, 0.0, 1.0, 0.02));
   public final e F = this.addSetting(new e("Iridescence", "Mild viewing-angle interference", 0.18, 0.0, 1.0, 0.02));
   public final s M = this.addSetting(new s("Enhanced Refraction", "Local scene refraction where effects quality permits", true));
   public final s b = this.addSetting(new s("Theme Palette", "Use current theme accents for chams colors", false));
   public final i y = this.addSetting(new i("Primary Color", "Main chams color", -15814));
   public final i A = this.addSetting(new i("Secondary Color", "Pattern, vein and shadow color", -14018044));
   public final e v = this.addSetting(new e("Shading", "Flat color to fully lit faces", 0.6, 0.0, 1.0, 0.05));
   public final e r = this.addSetting(new e("Pattern Scale", "Pattern cells per skin area", 1.0, 0.25, 4.0, 0.05));
   public final e O = this.addSetting(new e("Pattern Speed", "Pattern drift and twinkle cycles per second", 0.25, 0.0, 2.0, 0.05));
   private static final List<String> a = List.of("Solid", "Glow", "Metallic", "Camo", "Marble", "Glitter", "Ghost");
   public final s D = this.addSetting(new s("Affect Armor", "Apply material to armor", false));
   public final s H = this.addSetting(new s("Affect Held Items", "Apply material to equipped items", false));
   public final s K = this.addSetting(new s("First Person Arms", "Apply material to arms independently of held items", false));
   public final s P = this.addSetting(new s("Third Person", "Style the local player in third person", true));

   public boolean P() {
      return this.n.O("Liquid Glass");
   }

   public boolean i() {
      return a.contains(this.n.getValue());
   }

   private boolean q() {
      return this.n.O("Camo") || this.n.O("Marble") || this.n.O("Glitter");
   }

   public int w() {
      return this.b.getValue() ? ho.J : this.y.getValue();
   }

   public int E() {
      return this.b.getValue() ? ho.ds : this.A.getValue();
   }

   public j4.j t() {
      if (this.isEnabled()) {
         return switch ((String)this.n.getValue()) {
            case "Rainbow Outline" -> j4.j.J;
            case "Hologram" -> j4.j.k;
            case "Liquid Glass" -> j4.j.e;
            case "Solid" -> j4.j.K;
            case "Glow" -> j4.j.u;
            case "Metallic" -> j4.j.T;
            case "Camo" -> j4.j.N;
            case "Marble" -> j4.j.r;
            case "Glitter" -> j4.j.z;
            case "Ghost" -> j4.j.F;
            default -> null;
         };
      } else {
         return null;
      }
   }

   public double o() {
      return this.P() ? this.x.getValue() : this.I.getValue();
   }

   public boolean J() {
      return !this.n.O("Comets") && !this.n.O("Outline");
   }

   public boolean Z() {
      return this.J() || this.n.O("Outline");
   }

   public boolean s() {
      return !this.isEnabled() ? false : this.n.O("Rainbow Outline") && this.U.getValue();
   }

   public SelfVisualsModule() {
      super("SelfVisuals", "Style your player with colors and comets", Category.VISUALS, -1);
      T = this;

      for (q var4 : new q[]{this.t, this.p, this.w, this.Q, this.E}) {
         var4.P(() -> this.n.O("Rainbow Outline"));
      }

      this.t.P(() -> this.n.O("Rainbow Outline") && !this.p.O("Static"));

      for (q var17 : new q[]{this.W, this.u, this.z, this.e}) {
         var17.P(() -> this.Z() && this.V.getValue());
      }

      this.U.P(() -> this.n.O("Rainbow Outline"));
      this.b.P(this::i);
      this.y.P(() -> this.i() && !this.b.getValue());
      this.A.P(() -> (this.q() || this.n.O("Metallic")) && !this.b.getValue());
      this.v.P(() -> this.i() && !this.n.O("Glow") && !this.n.O("Ghost"));
      this.r.P(this::q);
      this.O.P(this::q);

      for (q var18 : new q[]{this.I, this.V, this.Y, this.D, this.H, this.K}) {
         var18.P(this::J);
      }

      this.I.P(() -> this.J() && !this.P());
      this.H.P(() -> this.J() && !this.P());

      for (q var19 : new q[]{this.x, this.B, this.L, this.f, this.F, this.M}) {
         var19.P(this::P);
      }

      this.o.P(() -> this.P() && this.M.getValue());
      this.V.P(this::Z);

      for (q var20 : new q[]{this.G, this.X, this.S, this.m, this.l, this.s, this.N}) {
         var20.P(() -> this.n.O("Comets"));
      }

      this.g.P(() -> this.n.O("Comets") && !this.N.getValue());
      this.k.P(() -> this.n.O("Comets") && !this.N.getValue());
      this.n.o(var0 -> {
         l_.f();
         com.larpclient.h.A("SelfVisuals", "Style: {}", var0);
      });
   }

   public List<q<?>> b() {
      return List.of(this.n, this.P, this.V, this.D, this.H, this.K, this.U);
   }

   public List<q<?>> p() {
      return List.of(
         this.w,
         this.Q,
         this.E,
         this.b,
         this.y,
         this.A,
         this.v,
         this.r,
         this.I,
         this.W,
         this.u,
         this.z,
         this.e,
         this.Y,
         this.G,
         this.l,
         this.s,
         this.N,
         this.g,
         this.k,
         this.x,
         this.L,
         this.f,
         this.F
      );
   }

   public List<q<?>> M() {
      return List.of(this.p, this.t, this.O, this.X, this.S, this.m, this.B);
   }

   public List<q<?>> J() {
      return List.of(this.M, this.o);
   }

   public static SelfVisualsModule k() {
      return T;
   }

   public boolean S() {
      return !this.isEnabled()
         ? false
         : kz.x()
            && kz.u("SelfVisuals")
            && this.isOperational()
            && (!this.P() || PlayerModelModule.E() == null || !PlayerModelModule.E().p())
            && this.P.getValue()
            && this.isInGame()
            && !mc.options.getCameraType().isFirstPerson();
   }

   public static boolean H(LivingEntityRenderState var0) {
      if (T != null && T.isEnabled()) {
         SelfVisualsModule var1 = T;
         return var1 != null
            && var1.S()
            && var1.J()
            && !p2.c()
            && kz.V(var0)
            && (!var1.P() || g8.s())
            && var0 instanceof AvatarRenderState var2
            && var2.id == getMc().player.getId();
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      l_.f();
      com.larpclient.h.A("SelfVisuals", "Style: {}; third-person: {}; first-person arms: {}", this.n.getValue(), this.P.getValue(), this.K.getValue());
   }

   @Override
   public void onDisable() {
      l_.f();
      j4.r();
      sm.q();
   }
}
