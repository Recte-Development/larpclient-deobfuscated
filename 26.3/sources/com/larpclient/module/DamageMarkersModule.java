package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.__;
import com.larpclient._q;
import com.larpclient.hz;
import com.larpclient.vj;
import com.larpclient.vv;
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
   private static DamageMarkersModule Z;
   private final b C = this.addSetting(new b("Size", "Damage text size", 1.0, 0.5, 2.5, 0.1));
   private final b q = this.addSetting(new b("Life Time", "Marker lifetime in milliseconds", 1000.0, 300.0, 2500.0, 100.0));
   private final m H = this.addSetting(new m("Self Damage", "Show your own health loss", true));
   private final m T = this.addSetting(new m("Friend Hits", "Show damage attributed to friends", true));
   private final o s = this.addSetting(new o("Color", "Color by damage or a chosen tint", "Damage", "Damage", "Custom"));
   private final h P = this.addSetting(new h("Custom Color", "Chosen damage marker tint", -48060));
   private final vj X = new vj(21463920019784013L);
   private final HashMap<LivingEntity, Double> t = new HashMap<>();
   private __ A;
   private Object j;
   private Object M;
   private Vec3 h;

   public DamageMarkersModule() {
      super("Damage Markers", "Show dealt damage and your own health loss as floating numbers", Category.VISUALS, -1);
      Z = this;
      this.P.a(() -> this.s.w("Custom"));
   }

   public static DamageMarkersModule D() {
      return Z;
   }

   public List<r<?>> y() {
      return List.of(this.H, this.T);
   }

   public List<r<?>> V() {
      return List.of(this.C, this.q, this.s, this.P);
   }

   @Override
   public void onEnable() {
      this.i();
      _q.z("DamageMarkers", "Enabled; observed health loss, 8-block trigger range, 256 marker budget");
   }

   @Override
   public void onDisable() {
      this.i();
   }

   public void i() {
      this.X.i();
      this.t.clear();
      this.j = this.M = null;
      this.h = null;
      if (this.A != null) {
         this.A.close();
         this.A = null;
      }
   }

   private boolean G(Entity var1) {
      LarpClient var2 = LarpClient.N();
      if (var1 instanceof Player && var2 != null) {
         MCFriendModule var3 = var2.j().y(MCFriendModule.class);
         return var3 != null && var3.M(var1.getName().getString());
      } else {
         return false;
      }
   }

   private boolean d() {
      if (this.isRuntimeActive() && this.isInGame()) {
         if (this.j != getMc().level || this.M != this.getPlayer()) {
            this.i();
            this.j = getMc().level;
            this.M = this.getPlayer();
         }

         return true;
      } else {
         return false;
      }
   }

   public void G(Entity var1) {
      if (this.isEnabled()) {
         if (this.d() && var1 instanceof LivingEntity var2 && var1 != this.getPlayer()) {
            this.X.J(var1.getId(), var1, var2.getHealth(), hz.S());
         }
      }
   }

   public void Z(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         if (this.d()) {
            Entity var2 = getMc().level.getEntity(var1.entityId());
            if (var2 instanceof LivingEntity var3 && (var2 != this.getPlayer() || this.H.getValue())) {
               Entity var4 = var1.getSource(getMc().level).getEntity();
               if (var2 == this.getPlayer() || var4 == this.getPlayer() || this.T.getValue() && this.G(var4)) {
                  this.X.J(var2.getId(), var2, var3.getHealth(), hz.S());
               }
            }
         }
      }
   }

   public void Q(ClientboundAnimatePacket var1) {
      if (this.isEnabled()) {
         if (this.d() && this.T.getValue() && (var1.getAction() == 0 || var1.getAction() == 3)) {
            if (getMc().level.getEntity(var1.getId()) instanceof LivingEntity var3 && this.G(var3) && (this.t.size() < 128 || this.t.containsKey(var3))) {
               this.t.put(var3, hz.S() + 2.5);
            }
         }
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.d();
      boolean var12 = false;
      byte var11 = (byte)var10000;
      var10000 = Integer.signum(var11);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.i();
      } else {
         Vec3 var1 = this.getPlayer().position();
         if (this.h != null) {
            double var18;
            var10000 = (var18 = var1.distanceToSqr(this.h) - 256.0) == 0.0 ? 0 : (var18 < 0.0 ? -1 : 1);
            boolean var14 = false;
            int var13 = var10000;
            if ((-Integer.signum(var13) >>> 31 ^ 1) == 0) {
               this.i();
               this.j = getMc().level;
               this.M = this.getPlayer();
            }
         }

         this.h = var1;
         double var2 = hz.S();
         this.t.entrySet().removeIf(var2x -> var2 > var2x.getValue() || var2x.getKey().isRemoved());
         this.X.b(var2, var0 -> ((Entity)var0).isRemoved());

         for (Entity var5 : getMc().level.entitiesForRendering()) {
            if (var5 instanceof LivingEntity var6 && !var5.isRemoved() && !(var5.distanceToSqr(this.getPlayer()) > 64.0)) {
               Object var7 = var6.getLastHurtByMob();
               if (var7 == null) {
                  var7 = var6.getLastHurtByPlayer();
               }

               int var8 = var5 == this.getPlayer() ? this.H.getValue() : (var7 == this.getPlayer() ? 1 : 0);
               if (var5 != this.getPlayer() && this.T.getValue()) {
                  var8 |= this.G((Entity)var7);
                  if (var8 == 0) {
                     for (LivingEntity var10 : this.t.keySet()) {
                        if (var5.distanceToSqr(var10) <= 16.0) {
                           var8 = 1;
                           break;
                        }
                     }
                  }
               }

               if (var5 == this.getPlayer() && !this.H.getValue()) {
                  this.X.z(var5.getId());
               }

               Vec3 var15 = var5.position();
               this.X.y(var5.getId(), var5, var6.getHealth(), var15.x, var15.y, var15.z, var6.getBbHeight(), (boolean)var8, var2, this.q.getValue() / 1000.0);
            }
         }
      }
   }

   public void w(CameraRenderState var1) {
      if (this.isEnabled()) {
         if (this.d()) {
            if (this.A == null) {
               this.A = new __();
            }

            ArrayList var2 = new ArrayList();
            double var3 = hz.Q();
            Vector3f var5 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1.orientation);
            double var6 = Math.max(0.01, Math.abs(var1.projectionMatrix.m11()));
            double var8 = Math.max(1, getMc().getWindow().getGuiScaledHeight());

            for (vj.c var11 : this.X.A(var3)) {
               double var12 = var11.x() - var1.pos.x;
               double var14 = var11.y() - var1.pos.y;
               double var16 = var11.z() - var1.pos.z;
               double var18 = var12 * var5.x + var14 * var5.y + var16 * var5.z;
               if (!(var18 <= 0.05)) {
                  int var20 = this.s.w("Custom") ? this.P.getValue() : vj.F(var11.damage());
                  double var21 = 0.5 * var18 / (var8 * var6 * 0.01890625);
                  var2.add(new __.n(var11.text(), var11.x(), var11.y(), var11.z(), var21 * this.C.getValue() * var11.scale(), vv.N(var20, var11.opacity())));
               }
            }

            this.A.a(var2, var1);
         }
      }
   }

   public void O(PoseStack var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.A != null && this.j == getMc().level) {
            this.A.s(var1, var2, true);
         }
      }
   }

   public void c() {
      if (this.A != null) {
         this.A.b();
      }
   }
}
