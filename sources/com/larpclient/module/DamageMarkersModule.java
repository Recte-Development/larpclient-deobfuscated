package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._3;
import com.larpclient.es;
import com.larpclient.mt;
import com.larpclient.od;
import com.larpclient.ty;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class DamageMarkersModule extends Module {
   private static DamageMarkersModule c;
   private final d9 u = this.addSetting(new d9("Size", "Damage text size", 1.0, 0.5, 2.5, 0.1));
   private final d9 a = this.addSetting(new d9("Life Time", "Marker lifetime in milliseconds", 1000.0, 300.0, 2500.0, 100.0));
   private final dh E = this.addSetting(new dh("Self Damage", "Show your own health loss", true));
   private final dh B = this.addSetting(new dh("Friend Hits", "Show damage attributed to friends", true));
   private final d6 G = this.addSetting(new d6("Color", "Color by damage or a chosen tint", "Damage", "Damage", "Custom"));
   private final dk D = this.addSetting(new dk("Custom Color", "Chosen damage marker tint", -48060));
   private final es v = new es(21463920019784013L);
   private final HashMap<LivingEntity, Double> d = new HashMap<>();
   private od A;
   private Object L;
   private Object F;
   private Vec3 e;

   public DamageMarkersModule() {
      super("Damage Markers", "Show dealt damage and your own health loss as floating numbers", Category.VISUALS, -1);
      c = this;
      this.D.H(() -> this.G.S("Custom"));
   }

   public static DamageMarkersModule U() {
      return c;
   }

   public List<df<?>> O() {
      return List.of(this.E, this.B);
   }

   public List<df<?>> s() {
      return List.of(this.u, this.a, this.G, this.D);
   }

   @Override
   public void onEnable() {
      this.b();
      mt.K("DamageMarkers", "Enabled; observed health loss, 8-block trigger range, 256 marker budget");
   }

   @Override
   public void onDisable() {
      this.b();
   }

   public void b() {
      this.v.o();
      this.d.clear();
      this.L = this.F = null;
      this.e = null;
      if (this.A != null) {
         this.A.close();
         this.A = null;
      }
   }

   private boolean e(Entity var1) {
      LarpClient var2 = LarpClient.F();
      if (var1 instanceof Player && var2 != null) {
         MCFriendModule var3 = var2.x().V(MCFriendModule.class);
         return var3 != null && var3.V(var1.getName().getString());
      } else {
         return false;
      }
   }

   private boolean v() {
      if (this.isRuntimeActive() && this.isInGame()) {
         if (this.L != getMc().level || this.F != this.getPlayer()) {
            this.b();
            this.L = getMc().level;
            this.F = this.getPlayer();
         }

         return true;
      } else {
         return false;
      }
   }

   public void G(Entity var1) {
      if (this.isEnabled()) {
         if (this.v() && var1 instanceof LivingEntity var2 && var1 != this.getPlayer()) {
            this.v.V(var1.getId(), var1, var2.getHealth(), ty.P());
         }
      }
   }

   public void v(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         if (this.v()) {
            Entity var2 = getMc().level.getEntity(var1.entityId());
            if (var2 instanceof LivingEntity var3 && (var2 != this.getPlayer() || this.E.P())) {
               Entity var4 = var1.getSource(getMc().level).getEntity();
               if (var2 == this.getPlayer() || var4 == this.getPlayer() || this.B.P() && this.e(var4)) {
                  this.v.V(var2.getId(), var2, var3.getHealth(), ty.P());
               }
            }
         }
      }
   }

   public void x(ClientboundAnimatePacket var1) {
      if (this.isEnabled()) {
         if (this.v() && this.B.P() && (var1.getAction() == 0 || var1.getAction() == 3)) {
            if (getMc().level.getEntity(var1.getId()) instanceof LivingEntity var3 && this.e(var3) && (this.d.size() < 128 || this.d.containsKey(var3))) {
               this.d.put(var3, ty.P() + 2.5);
            }
         }
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.v();
      boolean var12 = false;
      byte var11 = (byte)var10000;
      var10000 = Integer.signum(var11);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.b();
      } else {
         Vec3 var1 = this.getPlayer().position();
         if (this.e != null) {
            double var18;
            var10000 = (var18 = var1.distanceToSqr(this.e) - 256.0) == 0.0 ? 0 : (var18 < 0.0 ? -1 : 1);
            boolean var14 = false;
            int var13 = var10000;
            if ((-Integer.signum(var13) >>> 31 ^ 1) == 0) {
               this.b();
               this.L = getMc().level;
               this.F = this.getPlayer();
            }
         }

         this.e = var1;
         double var2 = ty.P();
         this.d.entrySet().removeIf(var2x -> var2 > var2x.getValue() || var2x.getKey().isRemoved());
         this.v.G(var2, var0 -> ((Entity)var0).isRemoved());

         for (Entity var5 : getMc().level.entitiesForRendering()) {
            if (var5 instanceof LivingEntity var6 && !var5.isRemoved() && !(var5.distanceToSqr(this.getPlayer()) > 64.0)) {
               Object var7 = var6.getLastHurtByMob();
               if (var7 == null) {
                  var7 = var6.getLastHurtByPlayer();
               }

               int var8 = var5 == this.getPlayer() ? this.E.P() : (var7 == this.getPlayer() ? 1 : 0);
               if (var5 != this.getPlayer() && this.B.P()) {
                  var8 |= this.e((Entity)var7);
                  if (var8 == 0) {
                     for (LivingEntity var10 : this.d.keySet()) {
                        if (var5.distanceToSqr(var10) <= 16.0) {
                           var8 = 1;
                           break;
                        }
                     }
                  }
               }

               if (var5 == this.getPlayer() && !this.E.P()) {
                  this.v.j(var5.getId());
               }

               Vec3 var15 = var5.position();
               this.v.L(var5.getId(), var5, var6.getHealth(), var15.x, var15.y, var15.z, var6.getBbHeight(), (boolean)var8, var2, this.a.P() / 1000.0);
            }
         }
      }
   }

   public void G(CameraRenderState var1) {
      if (this.isEnabled()) {
         if (this.v()) {
            if (this.A == null) {
               this.A = new od();
            }

            ArrayList var2 = new ArrayList();
            double var3 = ty.K();
            Vector3f var5 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1.orientation);
            double var6 = Math.max(0.01, Math.abs(var1.projectionMatrix.m11()));
            double var8 = Math.max(1, getMc().getWindow().getGuiScaledHeight());

            for (es.i var11 : this.v.N(var3)) {
               double var12 = var11.x() - var1.pos.x;
               double var14 = var11.y() - var1.pos.y;
               double var16 = var11.z() - var1.pos.z;
               double var18 = var12 * var5.x + var14 * var5.y + var16 * var5.z;
               if (!(var18 <= 0.05)) {
                  int var20 = this.G.S("Custom") ? this.D.P() : es.H(var11.damage());
                  double var21 = 0.5 * var18 / (var8 * var6 * 0.01890625);
                  var2.add(new od.l(var11.text(), var11.x(), var11.y(), var11.z(), var21 * this.u.P() * var11.scale(), _3.A(var20, var11.opacity())));
               }
            }

            this.A.H(var2, var1);
         }
      }
   }

   public void x(PoseStack var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.A != null && this.L == getMc().level) {
            this.A.j(var1, var2, true);
         }
      }
   }

   public void h() {
      if (this.A != null) {
         this.A.J();
      }
   }
}
