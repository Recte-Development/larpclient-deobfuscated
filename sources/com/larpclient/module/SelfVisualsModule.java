package com.larpclient.module;

import com.larpclient.h;
import com.larpclient.mt;
import com.larpclient.mu;
import com.larpclient.p7;
import com.larpclient.tj;
import com.larpclient.zj;
import com.larpclient.zo;
import java.util.List;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public final class SelfVisualsModule extends Module {
   private static SelfVisualsModule Q;
   public final d6 w = this.addSetting(
      new d6(
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
   public final d9 I = this.addSetting(new d9("Rainbow Speed", "Cycles per second", 0.18, 0.0, 2.0, 0.01));
   public final d6 A = this.addSetting(new d6("Direction", "Gradient flow", "Downward", "Downward", "Upward", "Horizontal", "Static"));
   public final d9 g = this.addSetting(new d9("Gradient Scale", "Rainbow cycles per block", 0.5, 0.1, 2.0, 0.05));
   public final d9 q = this.addSetting(new d9("Saturation", "Rainbow saturation", 0.62, 0.0, 1.0, 0.02));
   public final d9 S = this.addSetting(new d9("Brightness", "Rainbow brightness", 0.95, 0.1, 1.0, 0.025));
   public final d9 W = this.addSetting(new d9("Model Opacity", "Body opacity", 1.0, 0.05, 1.0, 0.05));
   public final dh y = this.addSetting(new dh("Outline", "Screen-space silhouette outline", true));
   public final dk Y = this.addSetting(new dk("Outline Color", "Silhouette border", -1));
   public final d9 p = this.addSetting(new d9("Outline Thickness", "Screen pixels", 1.5, 0.5, 5.0, 0.5));
   public final d9 B = this.addSetting(new d9("Outline Opacity", "Border opacity", 0.7, 0.0, 1.0, 0.05));
   public final dh b = this.addSetting(new dh("Outline Bloom", "Soft halo outside border", false));
   public final dh V = this.addSetting(new dh("Model Bloom", "Explicit body glow", false));
   public final dh f = this.addSetting(new dh("Affect Cape", "Apply Rainbow Outline to your Minecraft cape", false));
   public final d9 X = this.addSetting(new d9("Comet Size", "Core radius in blocks", 0.055, 0.025, 0.12, 0.005));
   public final d9 h = this.addSetting(new d9("Orbit Speed", "Orbits per second", 0.32, 0.05, 1.2, 0.01));
   public final d9 n = this.addSetting(new d9("Orbit Radius", "Blocks; kept outside the current body", 0.52, 0.3, 1.0, 0.01));
   public final d9 L = this.addSetting(new d9("Trail Length", "Seconds of previous orbit positions", 0.24, 0.08, 0.6, 0.01));
   public final d9 Z = this.addSetting(new d9("Glow Intensity", "Halo and trail brightness", 1.0, 0.2, 2.0, 0.05));
   public final dh e = this.addSetting(new dh("Comet Bloom", "Submit comets to the shared bloom pass", true));
   public final dh T = this.addSetting(new dh("Theme Colors", "Use current theme accents for comets", true));
   public final dk v = this.addSetting(new dk("Comet Color", "Core and trail head", com.larpclient.f.C));
   public final dk a = this.addSetting(new dk("Tail Color", "Faded trail end", com.larpclient.f.O));
   public final d9 U = this.addSetting(new d9("Glass Opacity", "Liquid surface opacity", 0.62, 0.1, 1.0, 0.02));
   public final d9 G = this.addSetting(new d9("Refraction Strength", "Screen displacement in pixels", 3.5, 0.0, 12.0, 0.25));
   public final d9 m = this.addSetting(new d9("Surface Flow", "Fluid surface motion speed", 0.65, 0.0, 2.0, 0.05));
   public final d9 i = this.addSetting(new d9("Gloss", "Broad soft reflections to tight highlights", 0.78, 0.0, 1.0, 0.02));
   public final d9 F = this.addSetting(new d9("Edge Highlight", "Viewing-angle surface highlights", 0.8, 0.0, 1.0, 0.02));
   public final d9 O = this.addSetting(new d9("Iridescence", "Mild viewing-angle interference", 0.18, 0.0, 1.0, 0.02));
   public final dh M = this.addSetting(new dh("Enhanced Refraction", "Local scene refraction where effects quality permits", true));
   public final dh d = this.addSetting(new dh("Theme Palette", "Use current theme accents for chams colors", false));
   public final dk k = this.addSetting(new dk("Primary Color", "Main chams color", -15814));
   public final dk o = this.addSetting(new dk("Secondary Color", "Pattern, vein and shadow color", -14018044));
   public final d9 N = this.addSetting(new d9("Shading", "Flat color to fully lit faces", 0.6, 0.0, 1.0, 0.05));
   public final d9 l = this.addSetting(new d9("Pattern Scale", "Pattern cells per skin area", 1.0, 0.25, 4.0, 0.05));
   public final d9 J = this.addSetting(new d9("Pattern Speed", "Pattern drift and twinkle cycles per second", 0.25, 0.0, 2.0, 0.05));
   private static final List<String> x = List.of("Solid", "Glow", "Metallic", "Camo", "Marble", "Glitter", "Ghost");
   public final dh D = this.addSetting(new dh("Affect Armor", "Apply material to armor", false));
   public final dh c = this.addSetting(new dh("Affect Held Items", "Apply material to equipped items", false));
   public final dh s = this.addSetting(new dh("First Person Arms", "Apply material to arms independently of held items", false));
   public final dh P = this.addSetting(new dh("Third Person", "Style the local player in third person", true));

   public boolean B() {
      return this.w.S("Liquid Glass");
   }

   public boolean I() {
      return x.contains(this.w.P());
   }

   private boolean k() {
      return this.w.S("Camo") || this.w.S("Marble") || this.w.S("Glitter");
   }

   public int n() {
      return this.d.P() ? com.larpclient.f.C : this.k.P();
   }

   public int k() {
      return this.d.P() ? com.larpclient.f.O : this.o.P();
   }

   public tj.b S() {
      if (this.isEnabled()) {
         return switch ((String)this.w.P()) {
            case "Rainbow Outline" -> tj.b.r;
            case "Hologram" -> tj.b.g;
            case "Liquid Glass" -> tj.b.X;
            case "Solid" -> tj.b.z;
            case "Glow" -> tj.b.b;
            case "Metallic" -> tj.b.I;
            case "Camo" -> tj.b.d;
            case "Marble" -> tj.b.Z;
            case "Glitter" -> tj.b.B;
            case "Ghost" -> tj.b.s;
            default -> null;
         };
      } else {
         return null;
      }
   }

   public double A() {
      return this.B() ? this.U.P() : this.W.P();
   }

   public boolean v() {
      return !this.w.S("Comets") && !this.w.S("Outline");
   }

   public boolean T() {
      return this.v() || this.w.S("Outline");
   }

   public boolean W() {
      return !this.isEnabled() ? false : this.w.S("Rainbow Outline") && this.f.P();
   }

   public SelfVisualsModule() {
      super("SelfVisuals", "Style your player with colors and comets", Category.VISUALS, -1);
      Q = this;

      for (df var4 : new df[]{this.I, this.A, this.g, this.q, this.S}) {
         var4.H(() -> this.w.S("Rainbow Outline"));
      }

      this.I.H(() -> this.w.S("Rainbow Outline") && !this.A.S("Static"));

      for (df var17 : new df[]{this.Y, this.p, this.B, this.b}) {
         var17.H(() -> this.T() && this.y.P());
      }

      this.f.H(() -> this.w.S("Rainbow Outline"));
      this.d.H(this::I);
      this.k.H(() -> this.I() && !this.d.P());
      this.o.H(() -> (this.k() || this.w.S("Metallic")) && !this.d.P());
      this.N.H(() -> this.I() && !this.w.S("Glow") && !this.w.S("Ghost"));
      this.l.H(this::k);
      this.J.H(this::k);

      for (df var18 : new df[]{this.W, this.y, this.V, this.D, this.c, this.s}) {
         var18.H(this::v);
      }

      this.W.H(() -> this.v() && !this.B());
      this.c.H(() -> this.v() && !this.B());

      for (df var19 : new df[]{this.U, this.m, this.i, this.F, this.O, this.M}) {
         var19.H(this::B);
      }

      this.G.H(() -> this.B() && this.M.P());
      this.y.H(this::T);

      for (df var20 : new df[]{this.X, this.h, this.n, this.L, this.Z, this.e, this.T}) {
         var20.H(() -> this.w.S("Comets"));
      }

      this.v.H(() -> this.w.S("Comets") && !this.T.P());
      this.a.H(() -> this.w.S("Comets") && !this.T.P());
      this.w.h(var0 -> {
         mu.s();
         mt.K("SelfVisuals", "Style: {}", var0);
      });
   }

   public List<df<?>> o() {
      return List.of(this.w, this.P, this.y, this.D, this.c, this.s, this.f);
   }

   public List<df<?>> R() {
      return List.of(
         this.g,
         this.q,
         this.S,
         this.d,
         this.k,
         this.o,
         this.N,
         this.l,
         this.W,
         this.Y,
         this.p,
         this.B,
         this.b,
         this.V,
         this.X,
         this.Z,
         this.e,
         this.T,
         this.v,
         this.a,
         this.U,
         this.i,
         this.F,
         this.O
      );
   }

   public List<df<?>> l() {
      return List.of(this.A, this.I, this.J, this.h, this.n, this.L, this.m);
   }

   public List<df<?>> m() {
      return List.of(this.M, this.G);
   }

   public static SelfVisualsModule V() {
      return Q;
   }

   public boolean M() {
      return !this.isEnabled()
         ? false
         : com.larpclient.h.V()
            && com.larpclient.h.R("SelfVisuals")
            && this.isOperational()
            && (!this.B() || PlayerModelModule.Y() == null || !PlayerModelModule.Y().u())
            && this.P.P()
            && this.isInGame()
            && !mc.options.getCameraType().isFirstPerson();
   }

   public static boolean R(LivingEntityRenderState var0) {
      if (Q != null && Q.isEnabled()) {
         SelfVisualsModule var1 = Q;
         return var1 != null
            && var1.M()
            && var1.v()
            && !zo.W()
            && com.larpclient.h.c(var0)
            && (!var1.B() || p7.B())
            && var0 instanceof AvatarRenderState var2
            && var2.id == getMc().player.getId();
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      mu.s();
      mt.K("SelfVisuals", "Style: {}; third-person: {}; first-person arms: {}", this.w.P(), this.P.P(), this.s.P());
   }

   @Override
   public void onDisable() {
      mu.s();
      tj.Q();
      zj.g();
   }
}
