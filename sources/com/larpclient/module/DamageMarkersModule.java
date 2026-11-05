package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.ea;
import com.larpclient.oh;
import com.larpclient.ok;
import com.larpclient.r4;
import com.larpclient.ra;
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
   private static DamageMarkersModule b;
   private final lc J = this.addSetting(new lc("Size", "Damage text size", 1.0, 0.5, 2.5, 0.1));
   private final lc C = this.addSetting(new lc("Life Time", "Marker lifetime in milliseconds", 1000.0, 300.0, 2500.0, 100.0));
   private final ln c = this.addSetting(new ln("Self Damage", "Show your own health loss", true));
   private final ln U = this.addSetting(new ln("Friend Hits", "Show damage attributed to friends", true));
   private final lp i = this.addSetting(new lp("Color", "Color by damage or a chosen tint", "Damage", "Damage", "Custom"));
   private final lv h = this.addSetting(new lv("Custom Color", "Chosen damage marker tint", -48060));
   private final ra H = new ra(21463920019784013L);
   private final HashMap<LivingEntity, Double> R = new HashMap<>();
   private ok W;
   private Object j;
   private Object I;
   private Vec3 s;

   public DamageMarkersModule() {
      super("Damage Markers", "Show dealt damage and your own health loss as floating numbers", Category.VISUALS, -1);
      b = this;
      this.h.x(() -> this.i.i("Custom"));
   }

   public static DamageMarkersModule c() {
      return b;
   }

   public List<l6<?>> V() {
      return List.of(this.c, this.U);
   }

   public List<l6<?>> L() {
      return List.of(this.J, this.C, this.i, this.h);
   }

   @Override
   public void onEnable() {
      this.D();
      r4.V("DamageMarkers", "Enabled; observed health loss, 8-block trigger range, 256 marker budget");
   }

   @Override
   public void onDisable() {
      this.D();
   }

   public void D() {
      this.H.B();
      this.R.clear();
      this.j = this.I = null;
      this.s = null;
      if (this.W != null) {
         this.W.close();
         this.W = null;
      }
   }

   private boolean I(Entity var1) {
      LarpClient var2 = LarpClient.y();
      if (var1 instanceof Player && var2 != null) {
         MCFriendModule var3 = var2.e().y(MCFriendModule.class);
         return var3 != null && var3.e(var1.getName().getString());
      } else {
         return false;
      }
   }

   private boolean r() {
      if (this.isRuntimeActive() && this.isInGame()) {
         if (this.j != getMc().level || this.I != this.getPlayer()) {
            this.D();
            this.j = getMc().level;
            this.I = this.getPlayer();
         }

         return true;
      } else {
         return false;
      }
   }

   public void I(Entity var1) {
      if (this.isEnabled()) {
         if (this.r() && var1 instanceof LivingEntity var2 && var1 != this.getPlayer()) {
            this.H.H(var1.getId(), var1, var2.getHealth(), ea.t());
         }
      }
   }

   public void c(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         if (this.r()) {
            Entity var2 = getMc().level.getEntity(var1.entityId());
            if (var2 instanceof LivingEntity var3 && (var2 != this.getPlayer() || this.c.getValue())) {
               Entity var4 = var1.getSource(getMc().level).getEntity();
               if (var2 == this.getPlayer() || var4 == this.getPlayer() || this.U.getValue() && this.I(var4)) {
                  this.H.H(var2.getId(), var2, var3.getHealth(), ea.t());
               }
            }
         }
      }
   }

   public void c(ClientboundAnimatePacket var1) {
      if (this.isEnabled()) {
         if (this.r() && this.U.getValue() && (var1.getAction() == 0 || var1.getAction() == 3)) {
            if (getMc().level.getEntity(var1.getId()) instanceof LivingEntity var3 && this.I(var3) && (this.R.size() < 128 || this.R.containsKey(var3))) {
               this.R.put(var3, ea.t() + 2.5);
            }
         }
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.r();
      boolean var12 = false;
      byte var11 = (byte)var10000;
      var10000 = Integer.signum(var11);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.D();
      } else {
         Vec3 var1 = this.getPlayer().position();
         if (this.s != null) {
            double var18;
            var10000 = (var18 = var1.distanceToSqr(this.s) - 256.0) == 0.0 ? 0 : (var18 < 0.0 ? -1 : 1);
            boolean var14 = false;
            int var13 = var10000;
            if ((-Integer.signum(var13) >>> 31 ^ 1) == 0) {
               this.D();
               this.j = getMc().level;
               this.I = this.getPlayer();
            }
         }

         this.s = var1;
         double var2 = ea.t();
         this.R.entrySet().removeIf(var2x -> var2 > var2x.getValue() || var2x.getKey().isRemoved());
         this.H.U(var2, var0 -> ((Entity)var0).isRemoved());

         for (Entity var5 : getMc().level.entitiesForRendering()) {
            if (var5 instanceof LivingEntity var6 && !var5.isRemoved() && !(var5.distanceToSqr(this.getPlayer()) > 64.0)) {
               Object var7 = var6.getLastHurtByMob();
               if (var7 == null) {
                  var7 = var6.getLastHurtByPlayer();
               }

               int var8 = var5 == this.getPlayer() ? this.c.getValue() : (var7 == this.getPlayer() ? 1 : 0);
               if (var5 != this.getPlayer() && this.U.getValue()) {
                  var8 |= this.I((Entity)var7);
                  if (var8 == 0) {
                     for (LivingEntity var10 : this.R.keySet()) {
                        if (var5.distanceToSqr(var10) <= 16.0) {
                           var8 = 1;
                           break;
                        }
                     }
                  }
               }

               if (var5 == this.getPlayer() && !this.c.getValue()) {
                  this.H.H(var5.getId());
               }

               Vec3 var15 = var5.position();
               this.H.x(var5.getId(), var5, var6.getHealth(), var15.x, var15.y, var15.z, var6.getBbHeight(), (boolean)var8, var2, this.C.getValue() / 1000.0);
            }
         }
      }
   }

   public void U(CameraRenderState var1) {
      if (this.isEnabled()) {
         if (this.r()) {
            if (this.W == null) {
               this.W = new ok();
            }

            ArrayList var2 = new ArrayList();
            double var3 = ea.y();
            Vector3f var5 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1.orientation);
            double var6 = Math.max(0.01, Math.abs(var1.projectionMatrix.m11()));
            double var8 = Math.max(1, getMc().getWindow().getGuiScaledHeight());

            for (ra.j var11 : this.H.H(var3)) {
               double var12 = var11.x() - var1.pos.x;
               double var14 = var11.y() - var1.pos.y;
               double var16 = var11.z() - var1.pos.z;
               double var18 = var12 * var5.x + var14 * var5.y + var16 * var5.z;
               if (!(var18 <= 0.05)) {
                  int var20 = this.i.i("Custom") ? this.h.getValue() : ra.w(var11.damage());
                  double var21 = 0.5 * var18 / (var8 * var6 * 0.01890625);
                  var2.add(new ok.p(var11.text(), var11.x(), var11.y(), var11.z(), var21 * this.J.getValue() * var11.scale(), oh.i(var20, var11.opacity())));
               }
            }

            this.W.I(var2, var1);
         }
      }
   }

   public void f(PoseStack var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.W != null && this.j == getMc().level) {
            this.W.d(var1, var2, true);
         }
      }
   }

   public void s() {
      if (this.W != null) {
         this.W.E();
      }
   }
}
