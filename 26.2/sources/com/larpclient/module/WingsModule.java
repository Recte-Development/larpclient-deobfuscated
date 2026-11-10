package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.cf;
import com.larpclient.cw;
import com.larpclient.hh;
import com.larpclient.ho;
import com.larpclient.j4;
import com.larpclient.kj;
import com.larpclient.l4;
import com.larpclient.uh;
import com.larpclient.zj;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class WingsModule extends Module {
   public static final String z = "Luminous";
   public static final String B = "Ender Dragon";
   private static WingsModule e;
   private final s S = this.addSetting(new s("Self", "Show your wings in third person", true));
   private final s y = this.addSetting(new s("Friends", "Show wings on friends", true));
   private final s M = this.addSetting(new s("Others", "Show wings on other players", false));
   private final e q = this.addSetting(new e("Size", "Wing size", 1.0, 0.75, 1.35, 0.05));
   private final s j = this.addSetting(new s("Custom Colors", "Cycle between your two colors", true));
   private final i k = this.addSetting(new i("Color 1", "First wing color", -52586));
   private final i t = this.addSetting(new i("Color 2", "Second wing color", -13461761));
   private final l f = this.addSetting(new l("Style", "Wing design", "Luminous", "Luminous", "Ender Dragon"));
   private final i u = this.addSetting(new i("Dragon Color", "Tints the Ender Dragon texture; white keeps it vanilla and alpha sets opacity", -1));
   public static final int p = 32;
   private List<WingsModule.c> D = List.of();
   private List<WingsModule.m> g = List.of();
   private final IdentityHashMap<Object, l4.s> U = new IdentityHashMap<>();
   private ClientLevel a;
   private ClientLevel O;
   private cw.d n;
   private final PoseStack b = new PoseStack();
   private final PoseStack i = new PoseStack();

   public WingsModule() {
      super("Wings", "Wear luminous membrane wings or Ender Dragon wings", Category.VISUALS, -1);
      e = this;
      this.j.P(() -> this.f.O("Luminous"));
      this.k.P(() -> this.f.O("Luminous") && this.j.getValue());
      this.t.P(() -> this.f.O("Luminous") && this.j.getValue());
      this.u.P(() -> this.f.O("Ender Dragon"));
      this.f.o(var0 -> com.larpclient.h.A("Wings", "Style={}", var0));
   }

   public static WingsModule D() {
      return e;
   }

   public List<q<?>> j() {
      return List.of(this.S, this.y, this.M);
   }

   public List<q<?>> N() {
      return List.of(this.f, this.q, this.j, this.k, this.t, this.u);
   }

   public boolean C() {
      return this.isEnabled() ? this.f.O("Ender Dragon") : false;
   }

   public WingsModule.h x() {
      if (this.isEnabled()) {
         if (!this.isOperational() || !this.S.getValue()) {
            return null;
         } else if (this.C()) {
            return this.u.getValue() >>> 24 == 0 ? null : new WingsModule.h(true, this.q.getValue().floatValue(), 0, 0, this.u.getValue());
         } else {
            return new WingsModule.h(
               false, this.q.getValue().floatValue(), this.j.getValue() ? this.k.getValue() : ho.J, this.j.getValue() ? this.t.getValue() : ho.ds, 0
            );
         }
      } else {
         return null;
      }
   }

   public static int g(int var0, int var1, double var2) {
      return uh.q(var0, var1, (Math.sin(var2 * 2.0) + 1.0) / 2.0);
   }

   public static int Q(int var0, int var1, double var2) {
      return uh.q(var0, var1, (Math.sin(var2 * 2.0 + 2.1) + 1.0) / 2.0);
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A(
         "Wings",
         this.C()
            ? "Ender Dragon wings enabled; vanilla dragon wing parts on the native torso, 48 quads per wearer, 32 nearby previews"
            : "Articulated membrane wings enabled on the native torso; shared bloom, 146 base quads per wearer, 32 nearby previews"
      );
   }

   @Override
   public void onDisable() {
      this.D = List.of();
      this.g = List.of();
      this.a = null;
      this.U.clear();
      this.O = null;
   }

   public void z(float var1) {
      if (this.isEnabled()) {
         this.D = List.of();
         this.g = List.of();
         this.a = null;
         if (this.isOperational() && this.isInGame()) {
            if (this.O != mc.level) {
               this.U.clear();
               this.O = mc.level;
            }

            MCFriendModule var2 = LarpClient.R().z().p(MCFriendModule.class);
            ArrayList var3 = new ArrayList();
            ArrayList var4 = new ArrayList();
            Set var5 = Collections.newSetFromMap(new IdentityHashMap());
            int var6 = 0;
            float var7 = this.q.getValue().floatValue();
            boolean var8 = this.C();
            int var9 = this.u.getValue();
            int var10 = this.j.getValue() ? this.k.getValue() : ho.J;
            int var11 = this.j.getValue() ? this.t.getValue() : ho.ds;
            int var12 = g(var10, var11, cf.F());
            int var13 = Q(var10, var11, cf.F());

            for (AbstractClientPlayer var15 : mc.level.players()) {
               if (var15 == mc.player) {
                  if (!this.S.getValue() || mc.options.getCameraType().isFirstPerson()) {
                     continue;
                  }
               } else {
                  int var16 = var2 != null && var2.S(var15.getName().getString()) ? 1 : 0;
                  if (!var16 != 0 ? this.y.getValue() : this.M.getValue() || var6 >= 32) {
                     continue;
                  }
               }

               if (var15.isAlive()
                  && !var15.isInvisible()
                  && !var15.isSpectator()
                  && !var15.isSleeping()
                  && !var15.isInWater()
                  && !var15.isVisuallyCrawling()
                  && !var15.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
                  && !(var15.distanceToSqr(mc.player) > 9216.0)) {
                  if (var15 != mc.player) {
                     var6++;
                  }

                  float var30 = Feature3DUIModule.i(var15, Mth.rotLerp(var1, var15.yBodyRotO, var15.yBodyRot), var1);
                  float var17 = var15.tickCount + var1;
                  float var18 = var15.walkAnimation.speed(var1);
                  Vec3 var19 = var15.getPosition(var1);
                  l4.s var20 = this.U.computeIfAbsent(var15, var0 -> new l4.s());
                  var20.s(var17, var15.isCrouching(), var18, var15.isFallFlying(), B(var15), var19);
                  var5.add(var15);
                  if (var8) {
                     float var31 = cw.b(var20.Z(), var15.isFallFlying(), var15.getFallFlyingTicks() + var1);
                     var20.M(cw.B(var20.b(), var20.I(), var20.h(), var31));
                     if (var9 >>> 24 != 0) {
                        cw.c var32 = cw.G(var20.R() + cw.u(var15.getId()), var20.b(), var20.I(), var20.h(), var31);
                        var4.add(new WingsModule.m(j4.d(var15, var1), var32, var7, var9));
                     }
                  } else {
                     EntityRenderState var21 = j4.d(var15, var1);
                     Pose var22 = j4.L(var21, this.i);
                     int var23 = var21 instanceof HumanoidRenderState var24 && var24.isCrouching ? 1 : 0;
                     float var33 = var22 == null ? var20.Z() : cw.b(var20.Z(), var15.isFallFlying(), var15.getFallFlyingTicks() + var1);
                     l4.u var25 = l4.m(var20.I(), var33, var15.getFallFlyingTicks() + var1, var15.getViewXRot(var1));
                     l4.o var26 = l4.I(var17, var20.b(), var20.I(), var33);
                     Vec3 var27 = var22 == null ? var19 : mc.gameRenderer.mainCamera().position();
                     Matrix4f var28 = var22 == null
                        ? l4.f(var25, var30, var17, var20.b(), var7, -1)
                        : new Matrix4f(var22.pose()).mul(l4.n(var25, var17, var20.b(), var7, -1, (boolean)var23));
                     Matrix4f var29 = var22 == null
                        ? l4.f(var25, var30, var17, var20.b(), var7, 1)
                        : new Matrix4f(var22.pose()).mul(l4.n(var25, var17, var20.b(), var7, 1, (boolean)var23));
                     var3.add(new WingsModule.c(l4.Q(var27, var28, -1, var26, var12, var13, var7), l4.Q(var27, var29, 1, var26, var12, var13, var7)));
                  }
               }
            }

            this.U.keySet().removeIf(var1x -> !var5.contains(var1x));
            this.a = mc.level;
            this.D = List.copyOf(var3);
            this.g = List.copyOf(var4);
         } else {
            this.U.clear();
            this.O = null;
         }
      }
   }

   private static boolean B(Player var0) {
      return !var0.onGround() && !var0.isPassenger() && !var0.onClimbable() && !var0.isFallFlying();
   }

   public boolean p(PoseStack var1, hh var2) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isOperational() && this.a == mc.level && !this.g.isEmpty()) {
         if (this.n == null) {
            this.n = cw.d.o();
            com.larpclient.h.A("Wings", "Ender Dragon wing parts ready from the vanilla layer definition; texture={}", cw.X);
         }

         long var3 = zj.m();
         byte var5 = 0;

         for (WingsModule.m var7 : this.g) {
            Pose var8 = j4.L(var7.state, var1);
            if (var8 != null) {
               this.b.last().set(var8);
               this.n.F(var7.angles);
               int var9 = var7.state instanceof HumanoidRenderState var10 && var10.isCrouching ? 1 : 0;
               this.n.p(this.b, var2.g(cw.L(var7.color)), var7.size, (boolean)var9, var7.state.lightCoords, var7.color);
               var5 = 1;
            }
         }

         zj.l("Wings Ender Dragon attachment and submission", var3);
         return (boolean)var5;
      } else {
         return false;
      }
   }

   public void d(kj var1, hh var2) {
      if (this.isEnabled()) {
         if (this.isOperational() && this.a == mc.level) {
            for (l4.w var6 : l4.w.values()) {
               for (WingsModule.c var8 : this.D) {
                  l4.L(var1, var8.left, var6);
                  l4.L(var1, var8.right, var6);
               }

               var2.K();
            }
         }
      }
   }

   public boolean w() {
      return !this.isEnabled() ? false : this.isOperational() && this.a == mc.level && this.D.stream().anyMatch(var0 -> l4.O(var0.left));
   }

   public void x(kj var1, hh var2) {
      if (this.isEnabled()) {
         if (this.w()) {
            for (WingsModule.c var4 : this.D) {
               l4.g(var1, var4.left);
               l4.g(var1, var4.right);
            }

            var2.K();
         }
      }
   }

   private record c(l4.q left, l4.q right) {
   }

   public record h(boolean dragon, float size, int first, int second, int dragonColor) {
   }

   private record m(EntityRenderState state, cw.c angles, float size, int color) {
   }
}
