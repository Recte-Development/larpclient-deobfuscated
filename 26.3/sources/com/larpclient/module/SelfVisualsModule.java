package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.d6;
import com.larpclient.h9;
import com.larpclient.py;
import com.larpclient.qc;
import com.larpclient.w7;
import com.larpclient.wp;
import java.util.List;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public final class SelfVisualsModule extends Module {
   private static SelfVisualsModule w;
   public final o k = this.addSetting(
      new o(
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
   public final b H = this.addSetting(new b("Rainbow Speed", "Cycles per second", 0.18, 0.0, 2.0, 0.01));
   public final o I = this.addSetting(new o("Direction", "Gradient flow", "Downward", "Downward", "Upward", "Horizontal", "Static"));
   public final b R = this.addSetting(new b("Gradient Scale", "Rainbow cycles per block", 0.5, 0.1, 2.0, 0.05));
   public final b l = this.addSetting(new b("Saturation", "Rainbow saturation", 0.62, 0.0, 1.0, 0.02));
   public final b L = this.addSetting(new b("Brightness", "Rainbow brightness", 0.95, 0.1, 1.0, 0.025));
   public final b r = this.addSetting(new b("Model Opacity", "Body opacity", 1.0, 0.05, 1.0, 0.05));
   public final m a = this.addSetting(new m("Outline", "Screen-space silhouette outline", true));
   public final h e = this.addSetting(new h("Outline Color", "Silhouette border", -1));
   public final b x = this.addSetting(new b("Outline Thickness", "Screen pixels", 1.5, 0.5, 5.0, 0.5));
   public final b E = this.addSetting(new b("Outline Opacity", "Border opacity", 0.7, 0.0, 1.0, 0.05));
   public final m c = this.addSetting(new m("Outline Bloom", "Soft halo outside border", false));
   public final m U = this.addSetting(new m("Model Bloom", "Explicit body glow", false));
   public final m O = this.addSetting(new m("Affect Cape", "Apply Rainbow Outline to your Minecraft cape", false));
   public final b W = this.addSetting(new b("Comet Size", "Core radius in blocks", 0.055, 0.025, 0.12, 0.005));
   public final b f = this.addSetting(new b("Orbit Speed", "Orbits per second", 0.32, 0.05, 1.2, 0.01));
   public final b b = this.addSetting(new b("Orbit Radius", "Blocks; kept outside the current body", 0.52, 0.3, 1.0, 0.01));
   public final b o = this.addSetting(new b("Trail Length", "Seconds of previous orbit positions", 0.24, 0.08, 0.6, 0.01));
   public final b q = this.addSetting(new b("Glow Intensity", "Halo and trail brightness", 1.0, 0.2, 2.0, 0.05));
   public final m A = this.addSetting(new m("Comet Bloom", "Submit comets to the shared bloom pass", true));
   public final m C = this.addSetting(new m("Theme Colors", "Use current theme accents for comets", true));
   public final h y = this.addSetting(new h("Comet Color", "Core and trail head", com.larpclient.c.y));
   public final h D = this.addSetting(new h("Tail Color", "Faded trail end", com.larpclient.c.t));
   public final b Y = this.addSetting(new b("Glass Opacity", "Liquid surface opacity", 0.62, 0.1, 1.0, 0.02));
   public final b t = this.addSetting(new b("Refraction Strength", "Screen displacement in pixels", 3.5, 0.0, 12.0, 0.25));
   public final b F = this.addSetting(new b("Surface Flow", "Fluid surface motion speed", 0.65, 0.0, 2.0, 0.05));
   public final b M = this.addSetting(new b("Gloss", "Broad soft reflections to tight highlights", 0.78, 0.0, 1.0, 0.02));
   public final b m = this.addSetting(new b("Edge Highlight", "Viewing-angle surface highlights", 0.8, 0.0, 1.0, 0.02));
   public final b Q = this.addSetting(new b("Iridescence", "Mild viewing-angle interference", 0.18, 0.0, 1.0, 0.02));
   public final m P = this.addSetting(new m("Enhanced Refraction", "Local scene refraction where effects quality permits", true));
   public final m j = this.addSetting(new m("Theme Palette", "Use current theme accents for chams colors", false));
   public final h p = this.addSetting(new h("Primary Color", "Main chams color", -15814));
   public final h B = this.addSetting(new h("Secondary Color", "Pattern, vein and shadow color", -14018044));
   public final b z = this.addSetting(new b("Shading", "Flat color to fully lit faces", 0.6, 0.0, 1.0, 0.05));
   public final b u = this.addSetting(new b("Pattern Scale", "Pattern cells per skin area", 1.0, 0.25, 4.0, 0.05));
   public final b i = this.addSetting(new b("Pattern Speed", "Pattern drift and twinkle cycles per second", 0.25, 0.0, 2.0, 0.05));
   private static final List<String> n = List.of("Solid", "Glow", "Metallic", "Camo", "Marble", "Glitter", "Ghost");
   public final m v = this.addSetting(new m("Affect Armor", "Apply material to armor", false));
   public final m S = this.addSetting(new m("Affect Held Items", "Apply material to equipped items", false));
   public final m g = this.addSetting(new m("First Person Arms", "Apply material to arms independently of held items", false));
   public final m s = this.addSetting(new m("Third Person", "Style the local player in third person", true));

   public boolean m() {
      return this.k.w("Liquid Glass");
   }

   public boolean z() {
      return n.contains(this.k.getValue());
   }

   private boolean t() {
      return this.k.w("Camo") || this.k.w("Marble") || this.k.w("Glitter");
   }

   public int v() {
      return this.j.getValue() ? com.larpclient.c.y : this.p.getValue();
   }

   public int W() {
      return this.j.getValue() ? com.larpclient.c.t : this.B.getValue();
   }

   public w7.b E() {
      if (this.isEnabled()) {
         return switch ((String)this.k.getValue()) {
            case "Rainbow Outline" -> w7.b.J;
            case "Hologram" -> w7.b.Q;
            case "Liquid Glass" -> w7.b.I;
            case "Solid" -> w7.b.h;
            case "Glow" -> w7.b.a;
            case "Metallic" -> w7.b.s;
            case "Camo" -> w7.b.U;
            case "Marble" -> w7.b.w;
            case "Glitter" -> w7.b.R;
            case "Ghost" -> w7.b.V;
            default -> null;
         };
      } else {
         return null;
      }
   }

   public double v() {
      return this.m() ? this.Y.getValue() : this.r.getValue();
   }

   public boolean l() {
      return !this.k.w("Comets") && !this.k.w("Outline");
   }

   public boolean k() {
      return this.l() || this.k.w("Outline");
   }

   public boolean E() {
      return !this.isEnabled() ? false : this.k.w("Rainbow Outline") && this.O.getValue();
   }

   public SelfVisualsModule() {
      super("SelfVisuals", "Style your player with colors and comets", Category.VISUALS, -1);
      w = this;

      for (r var4 : new r[]{this.H, this.I, this.R, this.l, this.L}) {
         var4.a(() -> this.k.w("Rainbow Outline"));
      }

      this.H.a(() -> this.k.w("Rainbow Outline") && !this.I.w("Static"));

      for (r var17 : new r[]{this.e, this.x, this.E, this.c}) {
         var17.a(() -> this.k() && this.a.getValue());
      }

      this.O.a(() -> this.k.w("Rainbow Outline"));
      this.j.a(this::z);
      this.p.a(() -> this.z() && !this.j.getValue());
      this.B.a(() -> (this.t() || this.k.w("Metallic")) && !this.j.getValue());
      this.z.a(() -> this.z() && !this.k.w("Glow") && !this.k.w("Ghost"));
      this.u.a(this::t);
      this.i.a(this::t);

      for (r var18 : new r[]{this.r, this.a, this.U, this.v, this.S, this.g}) {
         var18.a(this::l);
      }

      this.r.a(() -> this.l() && !this.m());
      this.S.a(() -> this.l() && !this.m());

      for (r var19 : new r[]{this.Y, this.F, this.M, this.m, this.Q, this.P}) {
         var19.a(this::m);
      }

      this.t.a(() -> this.m() && this.P.getValue());
      this.a.a(this::k);

      for (r var20 : new r[]{this.W, this.f, this.b, this.o, this.q, this.A, this.C}) {
         var20.a(() -> this.k.w("Comets"));
      }

      this.y.a(() -> this.k.w("Comets") && !this.C.getValue());
      this.D.a(() -> this.k.w("Comets") && !this.C.getValue());
      this.k.w(var0 -> {
         d6.N();
         _q.z("SelfVisuals", "Style: {}", var0);
      });
   }

   public List<r<?>> s() {
      return List.of(this.k, this.s, this.a, this.v, this.S, this.g, this.O);
   }

   public List<r<?>> W() {
      return List.of(
         this.R,
         this.l,
         this.L,
         this.j,
         this.p,
         this.B,
         this.z,
         this.u,
         this.r,
         this.e,
         this.x,
         this.E,
         this.c,
         this.U,
         this.W,
         this.q,
         this.A,
         this.C,
         this.y,
         this.D,
         this.Y,
         this.M,
         this.m,
         this.Q
      );
   }

   public List<r<?>> o() {
      return List.of(this.I, this.H, this.i, this.f, this.b, this.o, this.F);
   }

   public List<r<?>> A() {
      return List.of(this.P, this.t);
   }

   public static SelfVisualsModule v() {
      return w;
   }

   public boolean g() {
      return !this.isEnabled()
         ? false
         : wp.Q()
            && wp.Z("SelfVisuals")
            && this.isOperational()
            && (!this.m() || PlayerModelModule.k() == null || !PlayerModelModule.k().G())
            && this.s.getValue()
            && this.isInGame()
            && !mc.options.getCameraType().isFirstPerson();
   }

   public static boolean u(LivingEntityRenderState var0) {
      if (w != null && w.isEnabled()) {
         SelfVisualsModule var1 = w;
         return var1 != null
            && var1.g()
            && var1.l()
            && !qc.w()
            && wp.i(var0)
            && (!var1.m() || py.Q())
            && var0 instanceof AvatarRenderState var2
            && var2.id == getMc().player.getId();
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      d6.N();
      _q.z("SelfVisuals", "Style: {}; third-person: {}; first-person arms: {}", this.k.getValue(), this.s.getValue(), this.g.getValue());
   }

   @Override
   public void onDisable() {
      d6.N();
      w7.A();
      h9.L();
   }
}
