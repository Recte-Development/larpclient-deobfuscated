package com.larpclient.module;

import com.larpclient.c1;
import com.larpclient.e_;
import com.larpclient.ez;
import com.larpclient.gv;
import com.larpclient.q8;
import com.larpclient.wc;
import com.larpclient.wi;
import com.larpclient.ww;
import java.util.List;
import net.minecraft.class_10042;
import net.minecraft.class_10055;

public final class SelfVisualsModule extends Module {
   private static SelfVisualsModule O;
   public final li Q = this.addSetting(
      new li(
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
   public final lo L = this.addSetting(new lo("Rainbow Speed", "Cycles per second", 0.18, 0.0, 2.0, 0.01));
   public final li d = this.addSetting(new li("Direction", "Gradient flow", "Downward", "Downward", "Upward", "Horizontal", "Static"));
   public final lo G = this.addSetting(new lo("Gradient Scale", "Rainbow cycles per block", 0.5, 0.1, 2.0, 0.05));
   public final lo q = this.addSetting(new lo("Saturation", "Rainbow saturation", 0.62, 0.0, 1.0, 0.02));
   public final lo C = this.addSetting(new lo("Brightness", "Rainbow brightness", 0.95, 0.1, 1.0, 0.025));
   public final lo S = this.addSetting(new lo("Model Opacity", "Body opacity", 1.0, 0.05, 1.0, 0.05));
   public final l3 a = this.addSetting(new l3("Outline", "Screen-space silhouette outline", true));
   public final l0 i = this.addSetting(new l0("Outline Color", "Silhouette border", -1));
   public final lo x = this.addSetting(new lo("Outline Thickness", "Screen pixels", 1.5, 0.5, 5.0, 0.5));
   public final lo Y = this.addSetting(new lo("Outline Opacity", "Border opacity", 0.7, 0.0, 1.0, 0.05));
   public final l3 F = this.addSetting(new l3("Outline Bloom", "Soft halo outside border", false));
   public final l3 z = this.addSetting(new l3("Model Bloom", "Explicit body glow", false));
   public final l3 R = this.addSetting(new l3("Affect Cape", "Apply Rainbow Outline to your Minecraft cape", false));
   public final lo E = this.addSetting(new lo("Comet Size", "Core radius in blocks", 0.055, 0.025, 0.12, 0.005));
   public final lo W = this.addSetting(new lo("Orbit Speed", "Orbits per second", 0.32, 0.05, 1.2, 0.01));
   public final lo s = this.addSetting(new lo("Orbit Radius", "Blocks; kept outside the current body", 0.52, 0.3, 1.0, 0.01));
   public final lo u = this.addSetting(new lo("Trail Length", "Seconds of previous orbit positions", 0.24, 0.08, 0.6, 0.01));
   public final lo g = this.addSetting(new lo("Glow Intensity", "Halo and trail brightness", 1.0, 0.2, 2.0, 0.05));
   public final l3 l = this.addSetting(new l3("Comet Bloom", "Submit comets to the shared bloom pass", true));
   public final l3 U = this.addSetting(new l3("Theme Colors", "Use current theme accents for comets", true));
   public final l0 V = this.addSetting(new l0("Comet Color", "Core and trail head", ez.a));
   public final l0 J = this.addSetting(new l0("Tail Color", "Faded trail end", ez.f));
   public final lo m = this.addSetting(new lo("Glass Opacity", "Liquid surface opacity", 0.62, 0.1, 1.0, 0.02));
   public final lo r = this.addSetting(new lo("Refraction Strength", "Screen displacement in pixels", 3.5, 0.0, 12.0, 0.25));
   public final lo h = this.addSetting(new lo("Surface Flow", "Fluid surface motion speed", 0.65, 0.0, 2.0, 0.05));
   public final lo B = this.addSetting(new lo("Gloss", "Broad soft reflections to tight highlights", 0.78, 0.0, 1.0, 0.02));
   public final lo K = this.addSetting(new lo("Edge Highlight", "Viewing-angle surface highlights", 0.8, 0.0, 1.0, 0.02));
   public final lo v = this.addSetting(new lo("Iridescence", "Mild viewing-angle interference", 0.18, 0.0, 1.0, 0.02));
   public final l3 j = this.addSetting(new l3("Enhanced Refraction", "Local scene refraction where effects quality permits", true));
   public final l3 D = this.addSetting(new l3("Theme Palette", "Use current theme accents for chams colors", false));
   public final l0 f = this.addSetting(new l0("Primary Color", "Main chams color", -15814));
   public final l0 X = this.addSetting(new l0("Secondary Color", "Pattern, vein and shadow color", -14018044));
   public final lo A = this.addSetting(new lo("Shading", "Flat color to fully lit faces", 0.6, 0.0, 1.0, 0.05));
   public final lo w = this.addSetting(new lo("Pattern Scale", "Pattern cells per skin area", 1.0, 0.25, 4.0, 0.05));
   public final lo T = this.addSetting(new lo("Pattern Speed", "Pattern drift and twinkle cycles per second", 0.25, 0.0, 2.0, 0.05));
   private static final List<String> y = List.of("Solid", "Glow", "Metallic", "Camo", "Marble", "Glitter", "Ghost");
   public final l3 I = this.addSetting(new l3("Affect Armor", "Apply material to armor", false));
   public final l3 P = this.addSetting(new l3("Affect Held Items", "Apply material to equipped items", false));
   public final l3 M = this.addSetting(new l3("First Person Arms", "Apply material to arms independently of held items", false));
   public final l3 e = this.addSetting(new l3("Third Person", "Style the local player in third person", true));

   public boolean d() {
      return this.Q.G("Liquid Glass");
   }

   public boolean P() {
      return y.contains(this.Q.getValue());
   }

   private boolean c() {
      return this.Q.G("Camo") || this.Q.G("Marble") || this.Q.G("Glitter");
   }

   public int N() {
      return this.D.getValue() ? ez.a : this.f.getValue();
   }

   public int J() {
      return this.D.getValue() ? ez.f : this.X.getValue();
   }

   public gv.y A() {
      if (this.isEnabled()) {
         return switch ((String)this.Q.getValue()) {
            case "Rainbow Outline" -> gv.y.O;
            case "Hologram" -> gv.y.I;
            case "Liquid Glass" -> gv.y.D;
            case "Solid" -> gv.y.p;
            case "Glow" -> gv.y.M;
            case "Metallic" -> gv.y.r;
            case "Camo" -> gv.y.y;
            case "Marble" -> gv.y.Z;
            case "Glitter" -> gv.y.X;
            case "Ghost" -> gv.y.x;
            default -> null;
         };
      } else {
         return null;
      }
   }

   public double S() {
      return this.d() ? this.m.getValue() : this.S.getValue();
   }

   public boolean s() {
      return !this.Q.G("Comets") && !this.Q.G("Outline");
   }

   public boolean T() {
      return this.s() || this.Q.G("Outline");
   }

   public boolean W() {
      return !this.isEnabled() ? false : this.Q.G("Rainbow Outline") && this.R.getValue();
   }

   public SelfVisualsModule() {
      super("SelfVisuals", "Style your player with colors and comets", Category.VISUALS, -1);
      O = this;

      for (lb var4 : new lb[]{this.L, this.d, this.G, this.q, this.C}) {
         var4.O(() -> this.Q.G("Rainbow Outline"));
      }

      this.L.O(() -> this.Q.G("Rainbow Outline") && !this.d.G("Static"));

      for (lb var17 : new lb[]{this.i, this.x, this.Y, this.F}) {
         var17.O(() -> this.T() && this.a.getValue());
      }

      this.R.O(() -> this.Q.G("Rainbow Outline"));
      this.D.O(this::P);
      this.f.O(() -> this.P() && !this.D.getValue());
      this.X.O(() -> (this.c() || this.Q.G("Metallic")) && !this.D.getValue());
      this.A.O(() -> this.P() && !this.Q.G("Glow") && !this.Q.G("Ghost"));
      this.w.O(this::c);
      this.T.O(this::c);

      for (lb var18 : new lb[]{this.S, this.a, this.z, this.I, this.P, this.M}) {
         var18.O(this::s);
      }

      this.S.O(() -> this.s() && !this.d());
      this.P.O(() -> this.s() && !this.d());

      for (lb var19 : new lb[]{this.m, this.h, this.B, this.K, this.v, this.j}) {
         var19.O(this::d);
      }

      this.r.O(() -> this.d() && this.j.getValue());
      this.a.O(this::T);

      for (lb var20 : new lb[]{this.E, this.W, this.s, this.u, this.g, this.l, this.U}) {
         var20.O(() -> this.Q.G("Comets"));
      }

      this.V.O(() -> this.Q.G("Comets") && !this.U.getValue());
      this.J.O(() -> this.Q.G("Comets") && !this.U.getValue());
      this.Q.a(var0 -> {
         e_.Y();
         q8.G("SelfVisuals", "Style: {}", var0);
      });
   }

   public List<lb<?>> I() {
      return List.of(this.Q, this.e, this.a, this.I, this.P, this.M, this.R);
   }

   public List<lb<?>> i() {
      return List.of(
         this.G,
         this.q,
         this.C,
         this.D,
         this.f,
         this.X,
         this.A,
         this.w,
         this.S,
         this.i,
         this.x,
         this.Y,
         this.F,
         this.z,
         this.E,
         this.g,
         this.l,
         this.U,
         this.V,
         this.J,
         this.m,
         this.B,
         this.K,
         this.v
      );
   }

   public List<lb<?>> N() {
      return List.of(this.d, this.L, this.T, this.W, this.s, this.u, this.h);
   }

   public List<lb<?>> h() {
      return List.of(this.j, this.r);
   }

   public static SelfVisualsModule o() {
      return O;
   }

   public boolean Q() {
      return !this.isEnabled()
         ? false
         : wc.l()
            && wc.U("SelfVisuals")
            && this.isOperational()
            && (!this.d() || PlayerModelModule.h() == null || !PlayerModelModule.h().X())
            && this.e.getValue()
            && this.isInGame()
            && !mc.field_1690.method_31044().method_31034();
   }

   public static boolean Q(class_10042 var0) {
      if (O != null && O.isEnabled()) {
         SelfVisualsModule var1 = O;
         return var1 != null
            && var1.Q()
            && var1.s()
            && !wi.E()
            && wc.L(var0)
            && (!var1.d() || c1.s())
            && var0 instanceof class_10055 var2
            && var2.field_53528 == getMc().field_1724.method_5628();
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      e_.Y();
      q8.G("SelfVisuals", "Style: {}; third-person: {}; first-person arms: {}", this.Q.getValue(), this.e.getValue(), this.M.getValue());
   }

   @Override
   public void onDisable() {
      e_.Y();
      gv.A();
      ww.E();
   }
}
