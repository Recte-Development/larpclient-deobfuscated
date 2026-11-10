package com.larpclient.module;

import com.larpclient.bl;
import com.larpclient.g1;
import com.larpclient.hh;
import com.larpclient.j4;
import com.larpclient.uh;
import com.larpclient.z4;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ChamsModule extends Module {
   private final s i = this.addSetting(new s("Players", "Apply to players", true));
   private final s Y = this.addSetting(new s("Mobs", "Apply to mobs", false));
   private final e l = this.addSetting(new e("Red", "Red color component", 1.0, 0.0, 1.0, 0.05));
   private final e f = this.addSetting(new e("Green", "Green color component", 0.3, 0.0, 1.0, 0.05));
   private final e k = this.addSetting(new e("Blue", "Blue color component", 0.3, 0.0, 1.0, 0.05));
   private final s Q = this.addSetting(new s("Theme Colors", "Use the live Larp accent instead of RGB", true));
   private final e K = this.addSetting(new e("Range", "Maximum material distance", 128.0, 16.0, 512.0, 16.0));
   private final e M = this.addSetting(new e("Visible Opacity", "Material strength on visible surfaces", 0.3, 0.0, 1.0, 0.05));
   private final e x = this.addSetting(new e("Hidden Opacity", "Material strength behind walls", 0.55, 0.0, 1.0, 0.05));
   private final i n = this.addSetting(new i("Hidden Color", "Color of occluded surfaces", -1077331));
   private final s U = this.addSetting(new s("Through Walls", "Draw the occluded material pass", true));
   private final s u = this.addSetting(new s("Outline", "Add a vanilla silhouette outline", false));
   private final e Z = this.addSetting(new e("Max Entities", "Nearest models drawn", 32.0, 8.0, 128.0, 8.0));
   public final bl E;
   private final s e;
   private final s J;
   private final g1 L;
   private static ChamsModule D;
   private List<g1.u> S;
   private bl.d t;
   private Object V;
   private Object H;
   private boolean p;

   public ChamsModule() {
      super("Chams", "Animated gradient outlines, fill and glow on entities", Category.RENDER, -1);
      ChamsModule var1 = this;
      this.E = new bl(var1x -> var1.addSetting(var1x));
      this.e = this.addSetting(new s("Render Self", "Include your model in third person", false));
      this.J = this.addSetting(new s("Team Colors", "Use entity team colors instead of the gradient", false));
      this.L = new g1("Chams");
      this.S = List.of();
      D = this;

      for (q var2 : List.of(this.l, this.f, this.k, this.Q, this.M, this.x, this.n, this.u)) {
         var2.P(() -> false);
      }

      this.E.x.P(() -> !this.J.getValue());
      this.E.P.P(() -> !this.J.getValue());
   }

   public static ChamsModule C() {
      return D;
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A("Chams", "Echo gradient enabled; nearest model surfaces, stable wall visibility and native skin retained");
   }

   public List<q<?>> H() {
      return List.of(this.i, this.Y, this.e);
   }

   public List<q<?>> E() {
      return List.of(this.J, this.E.x, this.E.P);
   }

   public List<q<?>> Z() {
      return List.of(this.U);
   }

   public List<q<?>> k() {
      return List.of(this.K, this.Z);
   }

   public boolean F() {
      return !this.isEnabled() ? false : this.isOperational() && this.i.getValue();
   }

   public boolean p() {
      return !this.isEnabled() ? false : this.isOperational() && this.Y.getValue();
   }

   public float z() {
      return this.l.getValue().floatValue();
   }

   public float Z() {
      return this.f.getValue().floatValue();
   }

   public float o() {
      return this.k.getValue().floatValue();
   }

   public int N(LivingEntity var1) {
      return this.isEnabled() ? 0 : 0;
   }

   public void O(float var1) {
      if (this.isEnabled()) {
         this.S = List.of();
         this.t = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            this.V = mc.level;
            this.H = mc.player;
            this.p = this.U.getValue();
            this.t = this.E.H(this.J.getValue());
            ArrayList var2 = new ArrayList();
            if (this.e.getValue() && !mc.options.getCameraType().isFirstPerson() && mc.player.isAlive()) {
               var2.add(new g1.u(j4.d(mc.player, var1), this.v(mc.player)));
            }

            for (Entity var4 : z4.G(
               mc.level.entitiesForRendering(),
               mc.player,
               this.K.getValue(),
               this.Z.getValue().intValue() - var2.size(),
               var1x -> var1x instanceof LivingEntity var2x && var2x.isAlive() && var1x instanceof Player ? this.i.getValue() : this.Y.getValue()
            )) {
               var2.add(new g1.u(j4.d(var4, var1), uh.F(this.v(var4), z4.M(mc.player.distanceTo(var4), this.K.getValue()))));
            }

            this.S = List.copyOf(var2);
         }
      }
   }

   public void l(PoseStack var1, hh var2, float var3) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.V == mc.level && this.H == mc.player && this.t != null) {
            var2.K();
            this.L.n(this.S, var1, this.t, this.p);
         } else {
            this.C();
         }
      }
   }

   private int v(Entity var1) {
      return this.J.getValue() ? 0xFF000000 | var1.getTeamColor() : -1;
   }

   public void C() {
      this.S = List.of();
      this.t = null;
      this.V = this.H = null;
      this.L.close();
      this.E.v();
   }

   @Override
   public void onDisable() {
      this.C();
   }

   @Override
   public void onTick() {
      if (!this.isInGame() || this.V != null && (this.V != mc.level || this.H != mc.player)) {
         this.C();
      }
   }
}
