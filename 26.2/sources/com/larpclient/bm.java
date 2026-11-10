package com.larpclient;

import com.larpclient.module.WingsModule;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerCapeModel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import org.lwjgl.glfw.GLFW;

public final class bm {
   static final double Z = 1.45;
   private static final double V = 4.5;
   private static final double F = 0.09;
   private final tx M;
   private final c4 N = new c4();
   private g3 k;
   private g3 O;
   private PlayerCapeModel u;
   private int A = -1;
   private int j = -1;
   private tx.k Q;
   private double b;
   private tx.a C;
   private tx.m H;
   private g3 J;
   private Identifier K;
   private double U;
   private long e;

   public bm(tx var1) {
      this.M = var1;
   }

   public static tx.m R(uz var0) {
      return tx.J(var0, c4.L(c4.j(), c4.W()));
   }

   public void v(GuiGraphicsExtractor var1, uz var2, m7 var3, b6 var4, int var5, int var6, boolean var7, boolean var8) {
      Minecraft var9 = Minecraft.getInstance();
      this.C = null;
      if (this.M.O() && GLFW.glfwGetMouseButton(var9.getWindow().handle(), 0) != 1) {
         this.M.w();
      }

      List var10 = c4.j();
      WingsModule.h var11 = c4.W();
      tx.m var12 = tx.J(var2, c4.L(var10, var11));
      int var13 = var12.visible() && var9.gui.overlay() == null && var9.isWindowActive() ? 1 : 0;
      long var14 = System.nanoTime();
      tx.a var16 = this.M.d(var14, (boolean)var13, var7, var8, var3.l(), var12, var2.width(), var5, var6);
      this.b = var16.nameOpacity();
      this.g(var16);
      if (var12.visible() && var16.stage() != tx.k.d) {
         tx.e var17 = var16.toy();
         if (var17 != null) {
            this.T(var9, var17, var14);
         }

         p1.e var18 = var12.model();
         int var19 = Math.round(var12.centerX() + (float)var16.xOffset());
         double var20 = c4.O(var10, var11);
         int var22 = Math.max(var12.renderWidth(), (int)Math.ceil(2.0 * var20 * var12.scale()));
         int var23 = var19 - var22 / 2;
         int var24 = tx.i(var12, c4.L(var10, var11));
         if (var17 == null) {
            var1.enableScissor(var2.navigation(0).x() + var2.menuWidth() + 8, var24, var2.width(), var18.y() + var18.height() + 8);
         }

         try {
            if (var17 == null) {
               L(var1, var19, var12.floorY(), Math.min(var18.width() * 0.3F, var12.scale() * 0.62F), 1.0);
            }

            if (!this.a(var9)) {
               if (var17 == null) {
                  int var31 = Math.min(48, var18.width() / 2);
                  var4.c(var1, var19 - var31 / 2, var18.y() + var18.height() / 3, var31);
               }

               return;
            }

            PlayerSkin var25 = c4.V(var9.getGameProfile().id(), var4.S());
            g3 var26 = var25.model() == PlayerModelType.SLIM ? this.O : this.k;
            var26.F(var16.pose(), var17 == null ? null : var17.limbs());
            l8 var27 = c4.V();
            if (var27 != null && !wg.H()) {
               var27 = null;
            }

            var26.S(this.N.E(c4.W(var25), var10, var11, c4.a(), var27, var16.pose(), this.u));
            if (var17 == null) {
               var26.r(0.0F, 0.0F, 0.0F);
               var1.skin(
                  var26,
                  var25.body().texturePath(),
                  var12.scale(),
                  0.0F,
                  var16.pose().bodyYaw(),
                  -1.0625F,
                  var23,
                  var24,
                  var23 + var22,
                  var18.y() + var18.height()
               );
               return;
            }

            this.C = var16;
            this.H = var12;
            this.J = var26;
            this.K = var25.body().texturePath();
            this.U = Math.max(1.45, Math.max(0.85 + c4.L(var10, var11) + 0.1, Math.hypot(var20, 0.6) + 0.05));
         } finally {
            if (var17 == null) {
               var1.disableScissor();
            }
         }
      }
   }

   public void L(GuiGraphicsExtractor var1) {
      if (this.C != null) {
         tx.e var2 = this.C.toy();
         tx.m var3 = this.H;
         float var4 = var3.scale();
         var1.nextStratum();
         double var5 = Math.abs(Math.sin(var2.roll()));
         double var7 = 1.0 / (1.0 + var2.lift() * 0.9);
         L(
            var1,
            (float)var2.x(),
            var3.floorY(),
            (float)(Math.min(var3.model().width() * 0.3F, var4 * 0.62F) * (1.0 + 0.9 * var5) / (1.0 + var2.lift() * 0.5)),
            var7
         );
         double var9 = this.C.pose().ageInTicks() / 20.0;
         L(var1, var2, var4, false, var9);
         tx.s var11 = tx.s.of(var2, var4, this.U);
         this.J.r(var2.roll(), var11.shiftX(), var11.shiftY());
         var1.skin(
            this.J, this.K, var4, 0.0F, this.C.pose().bodyYaw(), -1.0625F, var11.left(), var11.top(), var11.left() + var11.side(), var11.top() + var11.side()
         );
         var1.nextStratum();
         L(var1, var2, var4, true, var9);
      }
   }

   public double Y() {
      return this.b;
   }

   public boolean S(double var1, double var3) {
      return this.M.C(var1, var3);
   }

   public boolean w() {
      return this.M.O();
   }

   public void c(double var1, double var3) {
      this.M.n(var1, var3);
   }

   public boolean j(double var1, double var3) {
      if (!this.M.p(var1, var3)) {
         return false;
      }

      this.M.n(var1, var3);
      return true;
   }

   public boolean c() {
      return this.M.w();
   }

   private void g(tx.a var1) {
      tx.k var2 = var1.stage();
      if (var2 != this.Q) {
         tx.e var3 = var1.toy();
         switch (var2) {
            case m:
               if (this.Q == tx.k.d) {
                  h.A("Menu", "Account walk-on started after menu reveal; native left-facing stride");
               }
               break;
            case O:
               if (this.Q == tx.k.T) {
                  h.A("Menu", "Account walk-on settled; native idle and pointer tracking active");
               } else if (this.Q == tx.k.F) {
                  h.A("Menu", "Account model back home; account switcher restored");
               } else if (this.Q != null && this.Q.ordinal() > tx.k.O.ordinal()) {
                  h.A("Menu", "Account model reset to its resting composition from {}", this.Q);
               }
               break;
            case P:
               h.A("Menu", "Account model picked up from {}; account switcher hidden", this.Q);
               break;
            case Y:
               h.A(
                  "Menu",
                  "Account model thrown at {} blocks/s, spin {} rad/s",
                  String.format(Locale.ROOT, "%.1f", Math.hypot(var3.vx(), var3.vy())),
                  String.format(Locale.ROOT, "%.1f", var3.spin())
               );
               break;
            case K:
               h.A("Menu", "Account model knocked down at roll {} deg{}", Math.round(Math.toDegrees(var3.roll())), var3.dizzy() > 0.0F ? ", dazed" : "");
               break;
            case R:
               if (this.Q == tx.k.Y) {
                  h.A("Menu", "Account model landed on its feet");
               }
               break;
            case Q:
               h.A("Menu", "Account model running home from {} px away", Math.round(Math.abs(var1.xOffset())));
         }

         this.Q = var2;
         h.U("Menu", "Account preview: {}", this.Q);
      }
   }

   private void T(Minecraft var1, tx.e var2, long var3) {
      if (!(var2.impact() < 4.5) && !(var3 - this.e < 9.0E7)) {
         this.e = var3;
         SoundEvent var5 = var2.impact() >= 12.0F ? SoundEvents.PLAYER_BIG_FALL : SoundEvents.PLAYER_SMALL_FALL;
         var1.getSoundManager().play(SimpleSoundInstance.forUI(var5, 1.0F, (float)Math.clamp(var2.impact() / 16.0F, 0.18, 0.7)));
      }
   }

   private static void L(GuiGraphicsExtractor var0, tx.e var1, float var2, boolean var3, double var4) {
      if (!(var1.dizzy() <= 0.0F)) {
         double var6 = Math.cos(var1.roll());
         double var8 = Math.sin(var1.roll());

         for (int var10 = 0; var10 < 3; var10++) {
            double var11 = var4 * 4.2 + var10 * Math.PI * 2.0 / 3.0;
            double var13 = Math.sin(var11);
            if ((var13 > 0.0 ? 1 : 0) == var3) {
               double var15 = Math.cos(var11) * 0.42;
               double var17 = -0.99 + var13 * 0.08;
               float var19 = (float)(var1.x() + (var15 * var6 - var17 * var8) * var2);
               float var20 = (float)(var1.y() + (var15 * var8 + var17 * var6) * var2);
               float var21 = (float)(var2 * (0.13 + 0.03 * var13) / 64.0);
               var0.pose().pushMatrix();

               try {
                  var0.pose().translate(var19, var20);
                  var0.pose().rotate((float)(var4 * 3.0 + var10));
                  var0.pose().scale(var21, var21);
                  var0.blit(
                     RenderPipelines.GUI_TEXTURED, gr.V(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, uh.H(-855310, var1.dizzy() * (0.75 + 0.25 * var13))
                  );
               } finally {
                  var0.pose().popMatrix();
               }
            }
         }
      }
   }

   private boolean a(Minecraft var1) {
      int var2 = wa.s;
      if (this.j == var2) {
         return false;
      }

      if (this.A == var2 && this.k != null) {
         return true;
      }

      try {
         EntityModelSet var3 = var1.getEntityModels();
         this.k = new g3(var3.bakeLayer(ModelLayers.PLAYER));
         this.O = new g3(var3.bakeLayer(ModelLayers.PLAYER_SLIM));
         this.u = new PlayerCapeModel(var3.bakeLayer(ModelLayers.PLAYER_CAPE));
         this.A = var2;
         h.A("Menu", "Account skin preview ready; native wide/slim models and cape, resource generation {}", var2);
         return true;
      } catch (RuntimeException var4) {
         this.k = this.O = null;
         this.u = null;
         this.j = var2;
         h.m("Menu", "Account model bake failed; showing the account portrait until reopen or resource reload", var4);
         return false;
      }
   }

   private static void L(GuiGraphicsExtractor var0, float var1, int var2, float var3, double var4) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var1, var2);
         var0.pose().scale(1.0F, 0.13F);

         for (int var6 = 8; var6 >= 1; var6--) {
            kv.Q(var0, 0, 0, Math.max(1, Math.round(var3 * var6 / 8.0F)), uh.F(110663832, var4));
         }
      } finally {
         var0.pose().popMatrix();
      }
   }

   public void H() {
      this.M.Z();
   }

   public void f(boolean var1) {
      this.k = this.O = null;
      this.u = null;
      this.C = null;
      this.J = null;
      this.A = this.j = -1;
      this.M.A(var1);
   }
}
