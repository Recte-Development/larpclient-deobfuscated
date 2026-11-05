package com.larpclient.module;

import com.larpclient._3;
import com.larpclient.dq;
import com.larpclient.m6;
import com.larpclient.mt;
import com.larpclient.p3;
import com.larpclient.tj;
import com.larpclient.xh;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ChamsModule extends Module {
   private final dh i = this.addSetting(new dh("Players", "Apply to players", true));
   private final dh j = this.addSetting(new dh("Mobs", "Apply to mobs", false));
   private final d9 G = this.addSetting(new d9("Red", "Red color component", 1.0, 0.0, 1.0, 0.05));
   private final d9 Y = this.addSetting(new d9("Green", "Green color component", 0.3, 0.0, 1.0, 0.05));
   private final d9 F = this.addSetting(new d9("Blue", "Blue color component", 0.3, 0.0, 1.0, 0.05));
   private final dh Z = this.addSetting(new dh("Theme Colors", "Use the live Larp accent instead of RGB", true));
   private final d9 R = this.addSetting(new d9("Range", "Maximum material distance", 128.0, 16.0, 512.0, 16.0));
   private final d9 m = this.addSetting(new d9("Visible Opacity", "Material strength on visible surfaces", 0.3, 0.0, 1.0, 0.05));
   private final d9 x = this.addSetting(new d9("Hidden Opacity", "Material strength behind walls", 0.55, 0.0, 1.0, 0.05));
   private final dk q = this.addSetting(new dk("Hidden Color", "Color of occluded surfaces", -1077331));
   private final dh E = this.addSetting(new dh("Through Walls", "Draw the occluded material pass", true));
   private final dh u = this.addSetting(new dh("Outline", "Add a vanilla silhouette outline", false));
   private final d9 b = this.addSetting(new d9("Max Entities", "Nearest models drawn", 32.0, 8.0, 128.0, 8.0));
   public final p3 O;
   private final dh B;
   private final dh n;
   private final m6 c;
   private static ChamsModule P;
   private List<m6.j> f;
   private p3.p A;
   private Object z;
   private Object K;
   private boolean o;

   public ChamsModule() {
      super("Chams", "Animated gradient outlines, fill and glow on entities", Category.RENDER, -1);
      ChamsModule var1 = this;
      this.O = new p3(var1x -> var1.addSetting(var1x));
      this.B = this.addSetting(new dh("Render Self", "Include your model in third person", false));
      this.n = this.addSetting(new dh("Team Colors", "Use entity team colors instead of the gradient", false));
      this.c = new m6("Chams");
      this.f = List.of();
      P = this;

      for (df var2 : List.of(this.G, this.Y, this.F, this.Z, this.m, this.x, this.q, this.u)) {
         var2.H(() -> false);
      }

      this.O.q.H(() -> !this.n.P());
      this.O.x.H(() -> !this.n.P());
   }

   public static ChamsModule u() {
      return P;
   }

   @Override
   public void onEnable() {
      mt.K("Chams", "Echo gradient enabled; nearest model surfaces, stable wall visibility and native skin retained");
   }

   public List<df<?>> X() {
      return List.of(this.i, this.j, this.B);
   }

   public List<df<?>> y() {
      return List.of(this.n, this.O.q, this.O.x);
   }

   public List<df<?>> q() {
      return List.of(this.E);
   }

   public List<df<?>> s() {
      return List.of(this.R, this.b);
   }

   public boolean U() {
      return !this.isEnabled() ? false : this.isOperational() && this.i.P();
   }

   public boolean d() {
      return !this.isEnabled() ? false : this.isOperational() && this.j.P();
   }

   public float g() {
      return this.G.P().floatValue();
   }

   public float b() {
      return this.Y.P().floatValue();
   }

   public float z() {
      return this.F.P().floatValue();
   }

   public int F(LivingEntity var1) {
      return this.isEnabled() ? 0 : 0;
   }

   public void s(float var1) {
      if (this.isEnabled()) {
         this.f = List.of();
         this.A = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            this.z = mc.level;
            this.K = mc.player;
            this.o = this.E.P();
            this.A = this.O.a(this.n.P());
            ArrayList var2 = new ArrayList();
            if (this.B.P() && !mc.options.getCameraType().isFirstPerson() && mc.player.isAlive()) {
               var2.add(new m6.j(tj.s(mc.player, var1), this.D(mc.player)));
            }

            for (Entity var4 : dq.y(
               mc.level.entitiesForRendering(),
               mc.player,
               this.R.P(),
               this.b.P().intValue() - var2.size(),
               var1x -> var1x instanceof LivingEntity var2x && var2x.isAlive() && var1x instanceof Player ? this.i.P() : this.j.P()
            )) {
               var2.add(new m6.j(tj.s(var4, var1), _3.U(this.D(var4), dq.C(mc.player.distanceTo(var4), this.R.P()))));
            }

            this.f = List.copyOf(var2);
         }
      }
   }

   public void N(PoseStack var1, xh var2, float var3) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.z == mc.level && this.K == mc.player && this.A != null) {
            var2.b();
            this.c.D(this.f, var1, this.A, this.o);
         } else {
            this.w();
         }
      }
   }

   private int D(Entity var1) {
      return this.n.P() ? 0xFF000000 | var1.getTeamColor() : -1;
   }

   public void w() {
      this.f = List.of();
      this.A = null;
      this.z = this.K = null;
      this.c.close();
      this.O.i();
   }

   @Override
   public void onDisable() {
      this.w();
   }

   @Override
   public void onTick() {
      if (!this.isInGame() || this.z != null && (this.z != mc.level || this.K != mc.player)) {
         this.w();
      }
   }
}
