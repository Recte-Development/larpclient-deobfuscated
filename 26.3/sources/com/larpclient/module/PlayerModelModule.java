package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.b2;
import com.larpclient.bo;
import com.larpclient.fn;
import com.larpclient.ij;
import com.larpclient.nl;
import com.larpclient.pv;
import com.larpclient.px;
import com.larpclient.vp;
import com.larpclient.wh;
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
   private static PlayerModelModule o;
   private final o B = this.addSetting(
      t()
         ? new o("Character", "Which character replaces your body", "Tung Tung Tung Sahur", "Tung Tung Tung Sahur", "Banana")
         : new o("Character", "Which character replaces your body", "Tung Tung Tung Sahur", "Tung Tung Tung Sahur")
   );
   private final b l = this.addSetting(new b("Scale", "Visual character size", 1.0, 0.5, 2.0, 0.05));
   private final m W = this.addSetting(new m("Show Timing", "Show CPU model submission timing", false));
   private Object L;
   private Object a;
   private long S;
   private long t;
   private long u;
   private boolean g;
   private LivingEntityRenderState Z;
   private int c = -1;
   private final Matrix4f D = new Matrix4f();
   private float x;
   private boolean K;
   private pv q;
   private b2 A;
   private final Matrix4f n = new Matrix4f();
   private final float[] w = new float[bo.s];
   private boolean G;

   public static boolean t() {
      return PlayerModelModule.class.getResource("/assets/larp-client/models/player/banana/mesh.bin") != null;
   }

   public PlayerModelModule() {
      super("Player Model", "Replace your rendered player with a character", Category.VISUALS, -1);
      o = this;
      this.B.w(var1 -> {
         this.Y();
         _q.c("PlayerModel", "Character changed to {}", var1);
      });
   }

   public static PlayerModelModule k() {
      return o;
   }

   public List<r<?>> A() {
      return List.of(this.B, this.l);
   }

   public List<r<?>> m() {
      return List.of(this.W);
   }

   private boolean x() {
      return "Banana".equals(this.B.getValue());
   }

   public boolean G() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.c != nl.q && (this.x() ? px.I() && this.A != null : fn.p());
   }

   public boolean m(EntityRenderState var1) {
      return !this.isEnabled() ? false : this.G() && this.Z == var1;
   }

   public void J(LivingEntityRenderState var1) {
      if (this.isEnabled()) {
         this.Z = var1;
      }
   }

   @Override
   public void onEnable() {
      this.Y();

      try {
         this.J();
      } catch (Exception var2) {
         _q.o("PlayerModel", "Mesh stage failed; retaining native body", var2);
         this.setEnabled(false);
      }
   }

   private void J() {
      if (this.x()) {
         px.R();
         if (this.A == null || this.A.O() != px.O()) {
            this.A = new b2(px.O());
         }

         _q.c("PlayerModel", "Banana ready: {} triangles, {} vertices, scale={}", this.A.O().triangles(), this.A.O().vertices(), this.l.getValue());
      } else {
         fn.G();
         if (this.q == null || this.q.i() != fn.e()) {
            this.q = new pv(fn.e());
         }

         _q.c("PlayerModel", "Sahur ready: walk rig, {} triangles, scale={}", this.q.t().triangles(), this.l.getValue());
      }
   }

   @Override
   public void onDisable() {
      this.Y();
   }

   public void Y() {
      this.g = (boolean)0;
      this.Z = null;
      this.L = this.a = null;
      this.u = this.t = 0L;
   }

   public void m() {
      this.Y();
      this.c = nl.q;
   }

   public void W(float var1, Vec3 var2) {
      if (this.isEnabled()) {
         this.g = (boolean)0;
         if (this.isRuntimeActive() && this.isInGame() && this.c != nl.q) {
            if (this.x() ? !px.I() || this.A == null || this.A.O() != px.O() : !fn.p() || this.q == null || this.q.i() != fn.e()) {
               this.J();
            }

            if (this.G() && this.isInGame()) {
               LocalPlayer var3 = mc.player;
               if (this.L != var3 || this.a != mc.level) {
                  LivingEntityRenderState var4 = this.Z;
                  this.Y();
                  this.Z = var4;
                  this.L = var3;
                  this.a = mc.level;
               }

               if (!mc.options.getCameraType().isFirstPerson() || ij.y()) {
                  if (this.Z != null) {
                     this.x = this.l.getValue().floatValue();
                     this.K = var3.isCrouching();
                     if (this.x()) {
                        this.l(var3, var1, var2);
                        this.g = (boolean)1;
                     } else {
                        this.q.l(this.Z.walkAnimationPos, this.Z.walkAnimationSpeed);
                        com.larpclient.e.j(
                           this.D,
                           fn.e(),
                           (float)(Mth.lerp(var1, var3.xOld, var3.getX()) - var2.x),
                           (float)(Mth.lerp(var1, var3.yOld, var3.getY()) - var2.y),
                           (float)(Mth.lerp(var1, var3.zOld, var3.getZ()) - var2.z),
                           (float)Math.toRadians(Feature3DUIModule.C(var3, Mth.rotLerp(var1, var3.yBodyRotO, var3.yBodyRot), var1)),
                           this.x,
                           this.K
                        );
                        this.g = (boolean)1;
                     }
                  }
               }
            }
         }
      }
   }

   private void l(LocalPlayer var1, float var2, Vec3 var3) {
      long var4 = System.nanoTime();
      float[] var6 = null;
      float var7 = 0.0F;
      if (this.Z instanceof com.larpclient.f0 var8 && var8.larp$emoteWeight() > 0.0F) {
         var6 = var8.larp$emoteSample();
         var7 = var8.larp$emoteWeight();
      }

      b2.m(this.w, this.Z.walkAnimationPos, this.Z.walkAnimationSpeed, this.Z.ageInTicks, this.Z.yRot, this.Z.xRot, this.K);
      if (var6 != null) {
         b2.k(this.w, var6, var7);
      }

      this.A.h(this.w, 1.0F - var7);
      if (var6 != null && !this.G) {
         _q.c("PlayerModel", "Banana emote started");
      }

      this.G = (boolean)(var6 != null ? 1 : 0);
      float var10 = var6 != null ? this.Z.bodyRot : Feature3DUIModule.C(var1, Mth.rotLerp(var2, var1.yBodyRotO, var1.yBodyRot), var2);
      vp.D(
         this.n,
         this.A.O(),
         (float)(Mth.lerp(var2, var1.xOld, var1.getX()) - var3.x),
         (float)(Mth.lerp(var2, var1.yOld, var1.getY()) - var3.y),
         (float)(Mth.lerp(var2, var1.zOld, var1.getZ()) - var3.z),
         (float)Math.toRadians(var10),
         this.x,
         var6,
         var7
      );
      if (this.W.getValue()) {
         this.t = this.t + (System.nanoTime() - var4);
      }
   }

   public void E(PoseStack var1, wh var2) {
      if (this.isEnabled()) {
         if (this.g && this.G() && this.L == mc.player && this.a == mc.level) {
            long var3 = System.nanoTime();

            try {
               if (this.x()) {
                  px.T(var1, var2, this.n, this.A);
               } else {
                  fn.J(var1, var2, this.D, this.q.J(), this.q.t().colors());
               }
            } catch (RuntimeException var6) {
               _q.o("PlayerModel", "Submission failed; restoring native body", var6);
               this.setEnabled(false);
            }

            if (this.W.getValue()) {
               this.t = this.t + (System.nanoTime() - var3);
               this.u++;
               if (System.nanoTime() - this.S > 1000000000L) {
                  mc.player
                     .sendOverlayMessage(
                        Component.literal(String.format(Locale.ROOT, "%s: %.3f ms CPU/frame", this.B.getValue(), this.t / 1000000.0 / Math.max(1L, this.u)))
                     );
                  this.S = System.nanoTime();
                  this.t = this.u = 0L;
               }
            }
         }
      }
   }

   public boolean N(EntityRenderState var1, HumanoidArm var2, PoseStack var3) {
      if (this.isEnabled()) {
         if (this.m(var1) && this.g) {
            int var4 = var2 == HumanoidArm.RIGHT ? 1 : 0;
            if (this.x()) {
               float var5 = this.Z instanceof com.larpclient.f0 var6 ? var6.larp$emoteWeight() : 0.0F;
               vp.K(
                  var3,
                  this.A.O(),
                  this.A.k((boolean)var4),
                  (boolean)var4,
                  this.x,
                  this.K,
                  this.Z.scale,
                  var5 > 0.0F ? ((com.larpclient.f0)this.Z).larp$emoteSample() : null,
                  var5
               );
               return true;
            } else {
               com.larpclient.e.K(var3, fn.e(), (boolean)var4, this.q.p((boolean)var4, com.larpclient.e.x((boolean)var4)), this.x, this.K, this.Z.scale);
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
