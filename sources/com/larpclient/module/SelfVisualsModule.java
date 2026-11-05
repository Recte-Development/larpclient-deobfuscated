package com.larpclient.module;

import com.larpclient._0;
import com.larpclient.f8;
import com.larpclient.my;
import com.larpclient.r4;
import com.larpclient.rv;
import com.larpclient.uv;
import com.larpclient.y8;
import java.util.List;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public final class SelfVisualsModule extends Module {
   private static SelfVisualsModule x;
   public final lp o = this.addSetting(
      new lp(
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
   public final lc O = this.addSetting(new lc("Rainbow Speed", "Cycles per second", 0.18, 0.0, 2.0, 0.01));
   public final lp Z = this.addSetting(new lp("Direction", "Gradient flow", "Downward", "Downward", "Upward", "Horizontal", "Static"));
   public final lc l = this.addSetting(new lc("Gradient Scale", "Rainbow cycles per block", 0.5, 0.1, 2.0, 0.05));
   public final lc f = this.addSetting(new lc("Saturation", "Rainbow saturation", 0.62, 0.0, 1.0, 0.02));
   public final lc X = this.addSetting(new lc("Brightness", "Rainbow brightness", 0.95, 0.1, 1.0, 0.025));
   public final lc p = this.addSetting(new lc("Model Opacity", "Body opacity", 1.0, 0.05, 1.0, 0.05));
   public final ln h = this.addSetting(new ln("Outline", "Screen-space silhouette outline", true));
   public final lv Y = this.addSetting(new lv("Outline Color", "Silhouette border", -1));
   public final lc m = this.addSetting(new lc("Outline Thickness", "Screen pixels", 1.5, 0.5, 5.0, 0.5));
   public final lc e = this.addSetting(new lc("Outline Opacity", "Border opacity", 0.7, 0.0, 1.0, 0.05));
   public final ln B = this.addSetting(new ln("Outline Bloom", "Soft halo outside border", false));
   public final ln q = this.addSetting(new ln("Model Bloom", "Explicit body glow", false));
   public final ln z = this.addSetting(new ln("Affect Cape", "Apply Rainbow Outline to your Minecraft cape", false));
   public final lc y = this.addSetting(new lc("Comet Size", "Core radius in blocks", 0.055, 0.025, 0.12, 0.005));
   public final lc i = this.addSetting(new lc("Orbit Speed", "Orbits per second", 0.32, 0.05, 1.2, 0.01));
   public final lc U = this.addSetting(new lc("Orbit Radius", "Blocks; kept outside the current body", 0.52, 0.3, 1.0, 0.01));
   public final lc W = this.addSetting(new lc("Trail Length", "Seconds of previous orbit positions", 0.24, 0.08, 0.6, 0.01));
   public final lc Q = this.addSetting(new lc("Glow Intensity", "Halo and trail brightness", 1.0, 0.2, 2.0, 0.05));
   public final ln N = this.addSetting(new ln("Comet Bloom", "Submit comets to the shared bloom pass", true));
   public final ln D = this.addSetting(new ln("Theme Colors", "Use current theme accents for comets", true));
   public final lv K = this.addSetting(new lv("Comet Color", "Core and trail head", rv.e));
   public final lv u = this.addSetting(new lv("Tail Color", "Faded trail end", rv.GQ));
   public final lc A = this.addSetting(new lc("Glass Opacity", "Liquid surface opacity", 0.62, 0.1, 1.0, 0.02));
   public final lc J = this.addSetting(new lc("Refraction Strength", "Screen displacement in pixels", 3.5, 0.0, 12.0, 0.25));
   public final lc G = this.addSetting(new lc("Surface Flow", "Fluid surface motion speed", 0.65, 0.0, 2.0, 0.05));
   public final lc P = this.addSetting(new lc("Gloss", "Broad soft reflections to tight highlights", 0.78, 0.0, 1.0, 0.02));
   public final lc j = this.addSetting(new lc("Edge Highlight", "Viewing-angle surface highlights", 0.8, 0.0, 1.0, 0.02));
   public final lc I = this.addSetting(new lc("Iridescence", "Mild viewing-angle interference", 0.18, 0.0, 1.0, 0.02));
   public final ln w = this.addSetting(new ln("Enhanced Refraction", "Local scene refraction where effects quality permits", true));
   public final ln R = this.addSetting(new ln("Theme Palette", "Use current theme accents for chams colors", false));
   public final lv M = this.addSetting(new lv("Primary Color", "Main chams color", -15814));
   public final lv C = this.addSetting(new lv("Secondary Color", "Pattern, vein and shadow color", -14018044));
   public final lc b = this.addSetting(new lc("Shading", "Flat color to fully lit faces", 0.6, 0.0, 1.0, 0.05));
   public final lc n = this.addSetting(new lc("Pattern Scale", "Pattern cells per skin area", 1.0, 0.25, 4.0, 0.05));
   public final lc t = this.addSetting(new lc("Pattern Speed", "Pattern drift and twinkle cycles per second", 0.25, 0.0, 2.0, 0.05));
   private static final List<String> c = List.of("Solid", "Glow", "Metallic", "Camo", "Marble", "Glitter", "Ghost");
   public final ln T = this.addSetting(new ln("Affect Armor", "Apply material to armor", false));
   public final ln k = this.addSetting(new ln("Affect Held Items", "Apply material to equipped items", false));
   public final ln s = this.addSetting(new ln("First Person Arms", "Apply material to arms independently of held items", false));
   public final ln a = this.addSetting(new ln("Third Person", "Style the local player in third person", true));

   public boolean U() {
      return this.o.i("Liquid Glass");
   }

   public boolean N() {
      return c.contains(this.o.getValue());
   }

   private boolean J() {
      return this.o.i("Camo") || this.o.i("Marble") || this.o.i("Glitter");
   }

   public int Z() {
      return this.R.getValue() ? rv.e : this.M.getValue();
   }

   public int P() {
      return this.R.getValue() ? rv.GQ : this.C.getValue();
   }

   public uv.w c() {
      if (this.isEnabled()) {
         return switch ((String)this.o.getValue()) {
            case "Rainbow Outline" -> uv.w.k;
            case "Hologram" -> uv.w.I;
            case "Liquid Glass" -> uv.w.w;
            case "Solid" -> uv.w.r;
            case "Glow" -> uv.w.K;
            case "Metallic" -> uv.w.x;
            case "Camo" -> uv.w.p;
            case "Marble" -> uv.w.n;
            case "Glitter" -> uv.w.g;
            case "Ghost" -> uv.w.A;
            default -> null;
         };
      } else {
         return null;
      }
   }

   public double K() {
      return this.U() ? this.A.getValue() : this.p.getValue();
   }

   public boolean w() {
      return !this.o.i("Comets") && !this.o.i("Outline");
   }

   public boolean Z() {
      return this.w() || this.o.i("Outline");
   }

   public boolean I() {
      return !this.isEnabled() ? false : this.o.i("Rainbow Outline") && this.z.getValue();
   }

   public SelfVisualsModule() {
      super("SelfVisuals", "Style your player with colors and comets", Category.VISUALS, -1);
      x = this;

      for (l6 var4 : new l6[]{this.O, this.Z, this.l, this.f, this.X}) {
         var4.x(() -> this.o.i("Rainbow Outline"));
      }

      this.O.x(() -> this.o.i("Rainbow Outline") && !this.Z.i("Static"));

      for (l6 var17 : new l6[]{this.Y, this.m, this.e, this.B}) {
         var17.x(() -> this.Z() && this.h.getValue());
      }

      this.z.x(() -> this.o.i("Rainbow Outline"));
      this.R.x(this::N);
      this.M.x(() -> this.N() && !this.R.getValue());
      this.C.x(() -> (this.J() || this.o.i("Metallic")) && !this.R.getValue());
      this.b.x(() -> this.N() && !this.o.i("Glow") && !this.o.i("Ghost"));
      this.n.x(this::J);
      this.t.x(this::J);

      for (l6 var18 : new l6[]{this.p, this.h, this.q, this.T, this.k, this.s}) {
         var18.x(this::w);
      }

      this.p.x(() -> this.w() && !this.U());
      this.k.x(() -> this.w() && !this.U());

      for (l6 var19 : new l6[]{this.A, this.G, this.P, this.j, this.I, this.w}) {
         var19.x(this::U);
      }

      this.J.x(() -> this.U() && this.w.getValue());
      this.h.x(this::Z);

      for (l6 var20 : new l6[]{this.y, this.i, this.U, this.W, this.Q, this.N, this.D}) {
         var20.x(() -> this.o.i("Comets"));
      }

      this.K.x(() -> this.o.i("Comets") && !this.D.getValue());
      this.u.x(() -> this.o.i("Comets") && !this.D.getValue());
      this.o.W(var0 -> {
         _0.q();
         r4.V("SelfVisuals", "Style: {}", var0);
      });
   }

   public List<l6<?>> q() {
      return List.of(this.o, this.a, this.h, this.T, this.k, this.s, this.z);
   }

   public List<l6<?>> O() {
      return List.of(
         this.l,
         this.f,
         this.X,
         this.R,
         this.M,
         this.C,
         this.b,
         this.n,
         this.p,
         this.Y,
         this.m,
         this.e,
         this.B,
         this.q,
         this.y,
         this.Q,
         this.N,
         this.D,
         this.K,
         this.u,
         this.A,
         this.P,
         this.j,
         this.I
      );
   }

   public List<l6<?>> Y() {
      return List.of(this.Z, this.O, this.t, this.i, this.U, this.W, this.G);
   }

   public List<l6<?>> j() {
      return List.of(this.w, this.J);
   }

   public static SelfVisualsModule f() {
      return x;
   }

   public boolean b() {
      return !this.isEnabled()
         ? false
         : f8.c()
            && f8.p("SelfVisuals")
            && this.isOperational()
            && (!this.U() || PlayerModelModule.r() == null || !PlayerModelModule.r().n())
            && this.a.getValue()
            && this.isInGame()
            && !mc.options.getCameraType().isFirstPerson();
   }

   public static boolean B(LivingEntityRenderState var0) {
      if (x != null && x.isEnabled()) {
         SelfVisualsModule var1 = x;
         return var1 != null
            && var1.b()
            && var1.w()
            && !y8.Y()
            && f8.m(var0)
            && (!var1.U() || my.f())
            && var0 instanceof AvatarRenderState var2
            && var2.id == getMc().player.getId();
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      _0.q();
      r4.V("SelfVisuals", "Style: {}; third-person: {}; first-person arms: {}", this.o.getValue(), this.a.getValue(), this.s.getValue());
   }

   @Override
   public void onDisable() {
      _0.q();
      uv.S();
      com.larpclient.la.J();
   }
}
