package com.larpclient.module;

import com.larpclient.fy;
import com.larpclient.mi;
import com.larpclient.oh;
import com.larpclient.r4;
import com.larpclient.rg;
import com.larpclient.uv;
import com.larpclient.y2;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ChamsModule extends Module {
   private final ln v = this.addSetting(new ln("Players", "Apply to players", true));
   private final ln f = this.addSetting(new ln("Mobs", "Apply to mobs", false));
   private final lc C = this.addSetting(new lc("Red", "Red color component", 1.0, 0.0, 1.0, 0.05));
   private final lc T = this.addSetting(new lc("Green", "Green color component", 0.3, 0.0, 1.0, 0.05));
   private final lc m = this.addSetting(new lc("Blue", "Blue color component", 0.3, 0.0, 1.0, 0.05));
   private final ln Q = this.addSetting(new ln("Theme Colors", "Use the live Larp accent instead of RGB", true));
   private final lc M = this.addSetting(new lc("Range", "Maximum material distance", 128.0, 16.0, 512.0, 16.0));
   private final lc d = this.addSetting(new lc("Visible Opacity", "Material strength on visible surfaces", 0.3, 0.0, 1.0, 0.05));
   private final lc A = this.addSetting(new lc("Hidden Opacity", "Material strength behind walls", 0.55, 0.0, 1.0, 0.05));
   private final lv B = this.addSetting(new lv("Hidden Color", "Color of occluded surfaces", -1077331));
   private final ln J = this.addSetting(new ln("Through Walls", "Draw the occluded material pass", true));
   private final ln O = this.addSetting(new ln("Outline", "Add a vanilla silhouette outline", false));
   private final lc N = this.addSetting(new lc("Max Entities", "Nearest models drawn", 32.0, 8.0, 128.0, 8.0));
   public final rg V;
   private final ln Y;
   private final ln g;
   private final fy l;
   private static ChamsModule y;
   private List<fy.a> r;
   private rg.q t;
   private Object n;
   private Object X;
   private boolean c;

   public ChamsModule() {
      super("Chams", "Animated gradient outlines, fill and glow on entities", Category.RENDER, -1);
      ChamsModule var1 = this;
      this.V = new rg(var1x -> var1.addSetting(var1x));
      this.Y = this.addSetting(new ln("Render Self", "Include your model in third person", false));
      this.g = this.addSetting(new ln("Team Colors", "Use entity team colors instead of the gradient", false));
      this.l = new fy("Chams");
      this.r = List.of();
      y = this;

      for (l6 var2 : List.of(this.C, this.T, this.m, this.Q, this.d, this.A, this.B, this.O)) {
         var2.x(() -> false);
      }

      this.V.R.x(() -> !this.g.getValue());
      this.V.s.x(() -> !this.g.getValue());
   }

   public static ChamsModule A() {
      return y;
   }

   @Override
   public void onEnable() {
      r4.V("Chams", "Echo gradient enabled; nearest model surfaces, stable wall visibility and native skin retained");
   }

   public List<l6<?>> U() {
      return List.of(this.v, this.f, this.Y);
   }

   public List<l6<?>> t() {
      return List.of(this.g, this.V.R, this.V.s);
   }

   public List<l6<?>> P() {
      return List.of(this.J);
   }

   public List<l6<?>> h() {
      return List.of(this.M, this.N);
   }

   public boolean K() {
      return !this.isEnabled() ? false : this.isOperational() && this.v.getValue();
   }

   public boolean g() {
      return !this.isEnabled() ? false : this.isOperational() && this.f.getValue();
   }

   public float H() {
      return this.C.getValue().floatValue();
   }

   public float N() {
      return this.T.getValue().floatValue();
   }

   public float d() {
      return this.m.getValue().floatValue();
   }

   public int z(LivingEntity var1) {
      return this.isEnabled() ? 0 : 0;
   }

   public void b(float var1) {
      if (this.isEnabled()) {
         this.r = List.of();
         this.t = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            this.n = mc.level;
            this.X = mc.player;
            this.c = this.J.getValue();
            this.t = this.V.I(this.g.getValue());
            ArrayList var2 = new ArrayList();
            if (this.Y.getValue() && !mc.options.getCameraType().isFirstPerson() && mc.player.isAlive()) {
               var2.add(new fy.a(uv.v(mc.player, var1), this.R(mc.player)));
            }

            for (Entity var4 : mi.T(
               mc.level.entitiesForRendering(),
               mc.player,
               this.M.getValue(),
               this.N.getValue().intValue() - var2.size(),
               var1x -> var1x instanceof LivingEntity var2x && var2x.isAlive() && var1x instanceof Player ? this.v.getValue() : this.f.getValue()
            )) {
               var2.add(new fy.a(uv.v(var4, var1), oh.S(this.R(var4), mi.G(mc.player.distanceTo(var4), this.M.getValue()))));
            }

            this.r = List.copyOf(var2);
         }
      }
   }

   public void r(PoseStack var1, y2 var2, float var3) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.n == mc.level && this.X == mc.player && this.t != null) {
            var2.v();
            this.l.Q(this.r, var1, this.t, this.c);
         } else {
            this.K();
         }
      }
   }

   private int R(Entity var1) {
      return this.g.getValue() ? 0xFF000000 | var1.getTeamColor() : -1;
   }

   public void K() {
      this.r = List.of();
      this.t = null;
      this.n = this.X = null;
      this.l.close();
      this.V.C();
   }

   @Override
   public void onDisable() {
      this.K();
   }

   @Override
   public void onTick() {
      if (!this.isInGame() || this.n != null && (this.n != mc.level || this.X != mc.player)) {
         this.K();
      }
   }
}
