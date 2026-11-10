package com.larpclient.module;

import com.larpclient.ba;
import com.larpclient.cw;
import com.larpclient.ei;
import com.larpclient.gv;
import com.larpclient.l2;
import com.larpclient.q8;
import com.larpclient.wd;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_4587;

public class ChamsModule extends Module {
   private final l3 X = this.addSetting(new l3("Players", "Apply to players", true));
   private final l3 K = this.addSetting(new l3("Mobs", "Apply to mobs", false));
   private final lo a = this.addSetting(new lo("Red", "Red color component", 1.0, 0.0, 1.0, 0.05));
   private final lo R = this.addSetting(new lo("Green", "Green color component", 0.3, 0.0, 1.0, 0.05));
   private final lo Z = this.addSetting(new lo("Blue", "Blue color component", 0.3, 0.0, 1.0, 0.05));
   private final l3 J = this.addSetting(new l3("Theme Colors", "Use the live Larp accent instead of RGB", true));
   private final lo I = this.addSetting(new lo("Range", "Maximum material distance", 128.0, 16.0, 512.0, 16.0));
   private final lo w = this.addSetting(new lo("Visible Opacity", "Material strength on visible surfaces", 0.3, 0.0, 1.0, 0.05));
   private final lo H = this.addSetting(new lo("Hidden Opacity", "Material strength behind walls", 0.55, 0.0, 1.0, 0.05));
   private final l0 e = this.addSetting(new l0("Hidden Color", "Color of occluded surfaces", -1077331));
   private final l3 A = this.addSetting(new l3("Through Walls", "Draw the occluded material pass", true));
   private final l3 y = this.addSetting(new l3("Outline", "Add a vanilla silhouette outline", false));
   private final lo U = this.addSetting(new lo("Max Entities", "Nearest models drawn", 32.0, 8.0, 128.0, 8.0));
   public final ba h;
   private final l3 O;
   private final l3 L;
   private final wd B;
   private static ChamsModule l;
   private List<wd.b> C;
   private ba.e V;
   private Object d;
   private Object N;
   private boolean F;

   public ChamsModule() {
      super("Chams", "Animated gradient outlines, fill and glow on entities", Category.RENDER, -1);
      ChamsModule var1 = this;
      this.h = new ba(var1x -> var1.addSetting(var1x));
      this.O = this.addSetting(new l3("Render Self", "Include your model in third person", false));
      this.L = this.addSetting(new l3("Team Colors", "Use entity team colors instead of the gradient", false));
      this.B = new wd("Chams");
      this.C = List.of();
      l = this;

      for (lb var2 : List.of(this.a, this.R, this.Z, this.J, this.w, this.H, this.e, this.y)) {
         var2.O(() -> false);
      }

      this.h.P.O(() -> !this.L.getValue());
      this.h.f.O(() -> !this.L.getValue());
   }

   public static ChamsModule K() {
      return l;
   }

   @Override
   public void onEnable() {
      q8.G("Chams", "Echo gradient enabled; nearest model surfaces, stable wall visibility and native skin retained");
   }

   public List<lb<?>> F() {
      return List.of(this.X, this.K, this.O);
   }

   public List<lb<?>> d() {
      return List.of(this.L, this.h.P, this.h.f);
   }

   public List<lb<?>> X() {
      return List.of(this.A);
   }

   public List<lb<?>> p() {
      return List.of(this.I, this.U);
   }

   public boolean d() {
      return !this.isEnabled() ? false : this.isOperational() && this.X.getValue();
   }

   public boolean J() {
      return !this.isEnabled() ? false : this.isOperational() && this.K.getValue();
   }

   public float B() {
      return this.a.getValue().floatValue();
   }

   public float v() {
      return this.R.getValue().floatValue();
   }

   public float t() {
      return this.Z.getValue().floatValue();
   }

   public int B(class_1309 var1) {
      return this.isEnabled() ? 0 : 0;
   }

   public void t(float var1) {
      if (this.isEnabled()) {
         this.C = List.of();
         this.V = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            this.d = mc.field_1687;
            this.N = mc.field_1724;
            this.F = this.A.getValue();
            this.V = this.h.g(this.L.getValue());
            ArrayList var2 = new ArrayList();
            if (this.O.getValue() && !mc.field_1690.method_31044().method_31034() && mc.field_1724.method_5805()) {
               var2.add(new wd.b(gv.i(mc.field_1724, var1), this.f(mc.field_1724)));
            }

            for (class_1297 var4 : l2.Q(
               mc.field_1687.method_18112(),
               mc.field_1724,
               this.I.getValue(),
               this.U.getValue().intValue() - var2.size(),
               var1x -> var1x instanceof class_1309 var2x && var2x.method_5805() && var1x instanceof class_1657 ? this.X.getValue() : this.K.getValue()
            )) {
               var2.add(new wd.b(gv.i(var4, var1), ei.R(this.f(var4), l2.A(mc.field_1724.method_5739(var4), this.I.getValue()))));
            }

            this.C = List.copyOf(var2);
         }
      }
   }

   public void N(class_4587 var1, cw var2, float var3) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.d == mc.field_1687 && this.N == mc.field_1724 && this.V != null) {
            var2.P();
            this.B.f(this.C, var1, this.V, this.F);
         } else {
            this.n();
         }
      }
   }

   private int f(class_1297 var1) {
      return this.L.getValue() ? 0xFF000000 | var1.method_22861() : -1;
   }

   public void n() {
      this.C = List.of();
      this.V = null;
      this.d = this.N = null;
      this.B.close();
      this.h.B();
   }

   @Override
   public void onDisable() {
      this.n();
   }

   @Override
   public void onTick() {
      if (!this.isInGame() || this.d != null && (this.d != mc.field_1687 || this.N != mc.field_1724)) {
         this.n();
      }
   }
}
