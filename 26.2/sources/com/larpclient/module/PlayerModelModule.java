package com.larpclient.module;

import com.larpclient.ca;
import com.larpclient.cx;
import com.larpclient.gn;
import com.larpclient.hh;
import com.larpclient.k5;
import com.larpclient.kh;
import com.larpclient.p_;
import com.larpclient.w5;
import com.larpclient.wa;
import com.larpclient.wf;
import com.larpclient.z_;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class PlayerModelModule extends Module {
   private static PlayerModelModule p;
   private final l K = this.addSetting(
      e()
         ? new l("Character", "Which character replaces your body", "Tung Tung Tung Sahur", "Tung Tung Tung Sahur", "Banana")
         : new l("Character", "Which character replaces your body", "Tung Tung Tung Sahur", "Tung Tung Tung Sahur")
   );
   private final e i = this.addSetting(new e("Scale", "Visual character size", 1.0, 0.5, 2.0, 0.05));
   private final s T = this.addSetting(new s("Show Timing", "Show CPU model submission timing", false));
   private Object s;
   private Object W;
   private long E;
   private long Z;
   private long z;
   private boolean A;
   private LivingEntityRenderState c;
   private int I = -1;
   private final Matrix4f n = new Matrix4f();
   private float m;
   private boolean e;
   private ca k;
   private w5 F;
   private final Matrix4f t = new Matrix4f();
   private final float[] o = new float[cx.B];
   private boolean v;

   public static boolean e() {
      return PlayerModelModule.class.getResource("/assets/larp-client/models/player/banana/mesh.bin") != null;
   }

   public PlayerModelModule() {
      super("Player Model", "Replace your rendered player with a character", Category.VISUALS, -1);
      p = this;
      this.K.o(var1 -> {
         this.z();
         com.larpclient.h.U("PlayerModel", "Character changed to {}", var1);
      });
   }

   public static PlayerModelModule E() {
      return p;
   }

   public List<q<?>> Y() {
      return List.of(this.K, this.i);
   }

   public List<q<?>> A() {
      return List.of(this.T);
   }

   private boolean q() {
      return "Banana".equals(this.K.getValue());
   }

   public boolean p() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.I != wa.s && (this.q() ? z_.O() && this.F != null : k5.v());
   }

   public boolean H(EntityRenderState var1) {
      return !this.isEnabled() ? false : this.p() && this.c == var1;
   }

   public void q(LivingEntityRenderState var1) {
      if (this.isEnabled()) {
         this.c = var1;
      }
   }

   @Override
   public void onEnable() {
      this.z();

      try {
         this.o();
      } catch (Exception var2) {
         com.larpclient.h.m("PlayerModel", "Mesh stage failed; retaining native body", var2);
         this.setEnabled(false);
      }
   }

   private void o() {
      if (this.q()) {
         z_.G();
         if (this.F == null || this.F.W() != z_.Z()) {
            this.F = new w5(z_.Z());
         }

         com.larpclient.h.U(
            "PlayerModel", "Banana ready: {} triangles, {} vertices, scale={}", this.F.W().triangles(), this.F.W().vertices(), this.i.getValue()
         );
      } else {
         k5.H();
         if (this.k == null || this.k.t() != k5.k()) {
            this.k = new ca(k5.k());
         }

         com.larpclient.h.U("PlayerModel", "Sahur ready: walk rig, {} triangles, scale={}", this.k.h().triangles(), this.i.getValue());
      }
   }

   @Override
   public void onDisable() {
      this.z();
   }

   public void z() {
      this.A = (boolean)0;
      this.c = null;
      this.s = this.W = null;
      this.z = this.Z = 0L;
   }

   public void M() {
      this.z();
      this.I = wa.s;
   }

   public void c(float var1, Vec3 var2) {
      if (this.isEnabled()) {
         this.A = (boolean)0;
         if (this.isRuntimeActive() && this.isInGame() && this.I != wa.s) {
            if (this.q() ? !z_.O() || this.F == null || this.F.W() != z_.Z() : !k5.v() || this.k == null || this.k.t() != k5.k()) {
               this.o();
            }

            if (this.p() && this.isInGame()) {
               LocalPlayer var3 = mc.player;
               if (this.s != var3 || this.W != mc.level) {
                  LivingEntityRenderState var4 = this.c;
                  this.z();
                  this.c = var4;
                  this.s = var3;
                  this.W = mc.level;
               }

               if (!mc.options.getCameraType().isFirstPerson() || p_.V()) {
                  if (this.c != null) {
                     this.m = this.i.getValue().floatValue();
                     this.e = var3.isCrouching();
                     if (this.q()) {
                        this.m(var3, var1, var2);
                        this.A = (boolean)1;
                     } else {
                        this.k.X(this.c.walkAnimationPos, this.c.walkAnimationSpeed);
                        wf.Z(
                           this.n,
                           k5.k(),
                           (float)(Mth.lerp(var1, var3.xOld, var3.getX()) - var2.x),
                           (float)(Mth.lerp(var1, var3.yOld, var3.getY()) - var2.y),
                           (float)(Mth.lerp(var1, var3.zOld, var3.getZ()) - var2.z),
                           (float)Math.toRadians(Feature3DUIModule.i(var3, Mth.rotLerp(var1, var3.yBodyRotO, var3.yBodyRot), var1)),
                           this.m,
                           this.e
                        );
                        this.A = (boolean)1;
                     }
                  }
               }
            }
         }
      }
   }

   private void m(LocalPlayer var1, float var2, Vec3 var3) {
      long var4 = System.nanoTime();
      float[] var6 = null;
      float var7 = 0.0F;
      if (this.c instanceof gn var8 && var8.larp$emoteWeight() > 0.0F) {
         var6 = var8.larp$emoteSample();
         var7 = var8.larp$emoteWeight();
      }

      w5.q(this.o, this.c.walkAnimationPos, this.c.walkAnimationSpeed, this.c.ageInTicks, this.c.yRot, this.c.xRot, this.e);
      if (var6 != null) {
         w5.Y(this.o, var6, var7);
      }

      this.F.n(this.o, 1.0F - var7);
      if (var6 != null && !this.v) {
         com.larpclient.h.U("PlayerModel", "Banana emote started");
      }

      this.v = (boolean)(var6 != null ? 1 : 0);
      float var10 = var6 != null ? this.c.bodyRot : Feature3DUIModule.i(var1, Mth.rotLerp(var2, var1.yBodyRotO, var1.yBodyRot), var2);
      kh.g(
         this.t,
         this.F.W(),
         (float)(Mth.lerp(var2, var1.xOld, var1.getX()) - var3.x),
         (float)(Mth.lerp(var2, var1.yOld, var1.getY()) - var3.y),
         (float)(Mth.lerp(var2, var1.zOld, var1.getZ()) - var3.z),
         (float)Math.toRadians(var10),
         this.m,
         var6,
         var7
      );
      if (this.T.getValue()) {
         this.Z = this.Z + (System.nanoTime() - var4);
      }
   }

   public void F(PoseStack var1, hh var2) {
      if (this.isEnabled()) {
         if (this.A && this.p() && this.s == mc.player && this.W == mc.level) {
            long var3 = System.nanoTime();

            try {
               if (this.q()) {
                  z_.w(var1, var2, this.t, this.F);
               } else {
                  k5.w(var1, var2, this.n, this.k.u(), this.k.h().colors());
               }
            } catch (RuntimeException var6) {
               com.larpclient.h.m("PlayerModel", "Submission failed; restoring native body", var6);
               this.setEnabled(false);
            }

            if (this.T.getValue()) {
               this.Z = this.Z + (System.nanoTime() - var3);
               this.z++;
               if (System.nanoTime() - this.E > 1000000000L) {
                  mc.player
                     .sendOverlayMessage(
                        Component.literal(String.format(Locale.ROOT, "%s: %.3f ms CPU/frame", this.K.getValue(), this.Z / 1000000.0 / Math.max(1L, this.z)))
                     );
                  this.E = System.nanoTime();
                  this.Z = this.z = 0L;
               }
            }
         }
      }
   }

   public boolean y(EntityRenderState var1, HumanoidArm var2, PoseStack var3) {
      if (this.isEnabled()) {
         if (this.H(var1) && this.A) {
            int var4 = var2 == HumanoidArm.RIGHT ? 1 : 0;
            if (this.q()) {
               float var5 = this.c instanceof gn var6 ? var6.larp$emoteWeight() : 0.0F;
               kh.j(
                  var3,
                  this.F.W(),
                  this.F.u((boolean)var4),
                  (boolean)var4,
                  this.m,
                  this.e,
                  this.c.scale,
                  var5 > 0.0F ? ((gn)this.c).larp$emoteSample() : null,
                  var5
               );
               return true;
            } else {
               wf.K(var3, k5.k(), (boolean)var4, this.k.P((boolean)var4, wf.N((boolean)var4)), this.m, this.e, this.c.scale);
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
