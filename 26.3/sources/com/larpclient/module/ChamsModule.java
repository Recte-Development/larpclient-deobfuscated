package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.i4;
import com.larpclient.j5;
import com.larpclient.q6;
import com.larpclient.vv;
import com.larpclient.w7;
import com.larpclient.wh;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ChamsModule extends Module {
   private final m T = this.addSetting(new m("Players", "Apply to players", true));
   private final m n = this.addSetting(new m("Mobs", "Apply to mobs", false));
   private final b y = this.addSetting(new b("Red", "Red color component", 1.0, 0.0, 1.0, 0.05));
   private final b u = this.addSetting(new b("Green", "Green color component", 0.3, 0.0, 1.0, 0.05));
   private final b j = this.addSetting(new b("Blue", "Blue color component", 0.3, 0.0, 1.0, 0.05));
   private final m H = this.addSetting(new m("Theme Colors", "Use the live Larp accent instead of RGB", true));
   private final b w = this.addSetting(new b("Range", "Maximum material distance", 128.0, 16.0, 512.0, 16.0));
   private final b R = this.addSetting(new b("Visible Opacity", "Material strength on visible surfaces", 0.3, 0.0, 1.0, 0.05));
   private final b Q = this.addSetting(new b("Hidden Opacity", "Material strength behind walls", 0.55, 0.0, 1.0, 0.05));
   private final h D = this.addSetting(new h("Hidden Color", "Color of occluded surfaces", -1077331));
   private final m x = this.addSetting(new m("Through Walls", "Draw the occluded material pass", true));
   private final m Y = this.addSetting(new m("Outline", "Add a vanilla silhouette outline", false));
   private final b a = this.addSetting(new b("Max Entities", "Nearest models drawn", 32.0, 8.0, 128.0, 8.0));
   public final q6 h;
   private final m C;
   private final m l;
   private final i4 K;
   private static ChamsModule f;
   private List<i4.w> t;
   private q6.u k;
   private Object N;
   private Object g;
   private boolean q;

   public ChamsModule() {
      super("Chams", "Animated gradient outlines, fill and glow on entities", Category.RENDER, -1);
      ChamsModule var1 = this;
      this.h = new q6(var1x -> var1.addSetting(var1x));
      this.C = this.addSetting(new m("Render Self", "Include your model in third person", false));
      this.l = this.addSetting(new m("Team Colors", "Use entity team colors instead of the gradient", false));
      this.K = new i4("Chams");
      this.t = List.of();
      f = this;

      for (r var2 : List.of(this.y, this.u, this.j, this.H, this.R, this.Q, this.D, this.Y)) {
         var2.a(() -> false);
      }

      this.h.o.a(() -> !this.l.getValue());
      this.h.v.a(() -> !this.l.getValue());
   }

   public static ChamsModule O() {
      return f;
   }

   @Override
   public void onEnable() {
      _q.z("Chams", "Echo gradient enabled; nearest model surfaces, stable wall visibility and native skin retained");
   }

   public List<r<?>> r() {
      return List.of(this.T, this.n, this.C);
   }

   public List<r<?>> s() {
      return List.of(this.l, this.h.o, this.h.v);
   }

   public List<r<?>> R() {
      return List.of(this.x);
   }

   public List<r<?>> q() {
      return List.of(this.w, this.a);
   }

   public boolean h() {
      return !this.isEnabled() ? false : this.isOperational() && this.T.getValue();
   }

   public boolean s() {
      return !this.isEnabled() ? false : this.isOperational() && this.n.getValue();
   }

   public float t() {
      return this.y.getValue().floatValue();
   }

   public float R() {
      return this.u.getValue().floatValue();
   }

   public float M() {
      return this.j.getValue().floatValue();
   }

   public int h(LivingEntity var1) {
      return this.isEnabled() ? 0 : 0;
   }

   public void j(float var1) {
      if (this.isEnabled()) {
         this.t = List.of();
         this.k = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            this.N = mc.level;
            this.g = mc.player;
            this.q = this.x.getValue();
            this.k = this.h.g(this.l.getValue());
            ArrayList var2 = new ArrayList();
            if (this.C.getValue() && !mc.options.getCameraType().isFirstPerson() && mc.player.isAlive()) {
               var2.add(new i4.w(w7.u(mc.player, var1), this.l(mc.player)));
            }

            for (Entity var4 : j5.n(
               mc.level.entitiesForRendering(),
               mc.player,
               this.w.getValue(),
               this.a.getValue().intValue() - var2.size(),
               var1x -> var1x instanceof LivingEntity var2x && var2x.isAlive() && var1x instanceof Player ? this.T.getValue() : this.n.getValue()
            )) {
               var2.add(new i4.w(w7.u(var4, var1), vv.O(this.l(var4), j5.K(mc.player.distanceTo(var4), this.w.getValue()))));
            }

            this.t = List.copyOf(var2);
         }
      }
   }

   public void g(PoseStack var1, wh var2, float var3) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.N == mc.level && this.g == mc.player && this.k != null) {
            var2.S();
            this.K.k(this.t, var1, this.k, this.q);
         } else {
            this.g();
         }
      }
   }

   private int l(Entity var1) {
      return this.l.getValue() ? 0xFF000000 | var1.getTeamColor() : -1;
   }

   public void g() {
      this.t = List.of();
      this.k = null;
      this.N = this.g = null;
      this.K.close();
      this.h.A();
   }

   @Override
   public void onDisable() {
      this.g();
   }

   @Override
   public void onTick() {
      if (!this.isInGame() || this.N != null && (this.N != mc.level || this.g != mc.player)) {
         this.g();
      }
   }
}
