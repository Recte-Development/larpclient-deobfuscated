package com.larpclient.module;

import com.larpclient.hl;
import com.larpclient.m5;
import com.larpclient.mk;
import com.larpclient.mt;
import com.larpclient.tq;
import com.larpclient.ww;
import com.larpclient.xh;
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
   private static PlayerModelModule K;
   private final d9 I = this.addSetting(new d9("Scale", "Visual character size", 1.0, 0.5, 2.0, 0.05));
   private final dh x = this.addSetting(new dh("Show Timing", "Show CPU model submission timing", false));
   private Object Q;
   private Object n;
   private long d;
   private long j;
   private long v;
   private boolean Z;
   private LivingEntityRenderState e;
   private int f = -1;
   private final Matrix4f C = new Matrix4f();
   private float u;
   private boolean D;
   private mk W;

   public PlayerModelModule() {
      super("Player Model", "Replace your rendered player with Tung Tung Tung Sahur", Category.VISUALS, -1);
      K = this;
   }

   public static PlayerModelModule Y() {
      return K;
   }

   public List<df<?>> u() {
      return List.of(this.I);
   }

   public List<df<?>> T() {
      return List.of(this.x);
   }

   public boolean u() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.f != m5.k && ww.j();
   }

   public boolean f(EntityRenderState var1) {
      return !this.isEnabled() ? false : this.u() && this.e == var1;
   }

   public void K(LivingEntityRenderState var1) {
      if (this.isEnabled()) {
         this.e = var1;
      }
   }

   @Override
   public void onEnable() {
      this.a();

      try {
         this.x();
      } catch (Exception var2) {
         mt.P("PlayerModel", "Mesh stage failed; retaining native body", var2);
         this.setEnabled(false);
      }
   }

   private void x() {
      ww.n();
      if (this.W == null || this.W.L() != ww.r()) {
         this.W = new mk(ww.r());
      }

      mt.U("PlayerModel", "Sahur ready: walk rig, {} triangles, scale={}", this.W.b().triangles(), this.I.P());
   }

   @Override
   public void onDisable() {
      this.a();
   }

   public void a() {
      this.Z = (boolean)0;
      this.e = null;
      this.Q = this.n = null;
      this.v = this.j = 0L;
   }

   public void O() {
      this.a();
      this.f = m5.k;
   }

   public void r(float var1, Vec3 var2) {
      if (this.isEnabled()) {
         this.Z = (boolean)0;
         if (this.isRuntimeActive() && this.isInGame() && this.f != m5.k) {
            if (!ww.j() || this.W == null || this.W.L() != ww.r()) {
               this.x();
            }

            if (this.u() && this.isInGame()) {
               LocalPlayer var3 = mc.player;
               if (this.Q != var3 || this.n != mc.level) {
                  LivingEntityRenderState var4 = this.e;
                  this.a();
                  this.e = var4;
                  this.Q = var3;
                  this.n = mc.level;
               }

               if (!mc.options.getCameraType().isFirstPerson() || tq.f()) {
                  if (this.e != null) {
                     this.u = this.I.P().floatValue();
                     this.D = var3.isCrouching();
                     this.W.L(this.e.walkAnimationPos, this.e.walkAnimationSpeed);
                     hl.Q(
                        this.C,
                        ww.r(),
                        (float)(Mth.lerp(var1, var3.xOld, var3.getX()) - var2.x),
                        (float)(Mth.lerp(var1, var3.yOld, var3.getY()) - var2.y),
                        (float)(Mth.lerp(var1, var3.zOld, var3.getZ()) - var2.z),
                        (float)Math.toRadians(Feature3DUIModule.j(var3, Mth.rotLerp(var1, var3.yBodyRotO, var3.yBodyRot), var1)),
                        this.u,
                        this.D
                     );
                     this.Z = (boolean)1;
                  }
               }
            }
         }
      }
   }

   public void a(PoseStack var1, xh var2) {
      if (this.isEnabled()) {
         if (this.Z && this.u() && this.Q == mc.player && this.n == mc.level) {
            long var3 = System.nanoTime();

            try {
               ww.G(var1, var2, this.C, this.W.Y(), this.W.b().colors());
            } catch (RuntimeException var6) {
               mt.P("PlayerModel", "Submission failed; restoring native body", var6);
               this.setEnabled(false);
            }

            if (this.x.P()) {
               this.j = this.j + (System.nanoTime() - var3);
               this.v++;
               if (System.nanoTime() - this.d > 1000000000L) {
                  mc.player
                     .sendOverlayMessage(
                        Component.literal(
                           String.format(Locale.ROOT, "%s: %.3f ms CPU/frame", "Tung Tung Tung Sahur", this.j / 1000000.0 / Math.max(1L, this.v))
                        )
                     );
                  this.d = System.nanoTime();
                  this.j = this.v = 0L;
               }
            }
         }
      }
   }

   public boolean g(EntityRenderState var1, HumanoidArm var2, PoseStack var3) {
      if (this.isEnabled()) {
         if (this.f(var1) && this.Z) {
            int var4 = var2 == HumanoidArm.RIGHT ? 1 : 0;
            hl.R(var3, ww.r(), (boolean)var4, this.W.w((boolean)var4, hl.G((boolean)var4)), this.u, this.D, this.e.scale);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
