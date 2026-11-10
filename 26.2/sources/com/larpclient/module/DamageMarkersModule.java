package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.b0;
import com.larpclient.cf;
import com.larpclient.jz;
import com.larpclient.uh;
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
   private static DamageMarkersModule T;
   private final e w = this.addSetting(new e("Size", "Damage text size", 1.0, 0.5, 2.5, 0.1));
   private final e A = this.addSetting(new e("Life Time", "Marker lifetime in milliseconds", 1000.0, 300.0, 2500.0, 100.0));
   private final s m = this.addSetting(new s("Self Damage", "Show your own health loss", true));
   private final s g = this.addSetting(new s("Friend Hits", "Show damage attributed to friends", true));
   private final l J = this.addSetting(new l("Color", "Color by damage or a chosen tint", "Damage", "Damage", "Custom"));
   private final i q = this.addSetting(new i("Custom Color", "Chosen damage marker tint", -48060));
   private final jz o = new jz(21463920019784013L);
   private final HashMap<LivingEntity, Double> t = new HashMap<>();
   private b0 u;
   private Object p;
   private Object Q;
   private Vec3 d;

   public DamageMarkersModule() {
      super("Damage Markers", "Show dealt damage and your own health loss as floating numbers", Category.VISUALS, -1);
      T = this;
      this.q.P(() -> this.J.O("Custom"));
   }

   public static DamageMarkersModule t() {
      return T;
   }

   public List<q<?>> B() {
      return List.of(this.m, this.g);
   }

   public List<q<?>> D() {
      return List.of(this.w, this.A, this.J, this.q);
   }

   @Override
   public void onEnable() {
      this.M();
      com.larpclient.h.A("DamageMarkers", "Enabled; observed health loss, 8-block trigger range, 256 marker budget");
   }

   @Override
   public void onDisable() {
      this.M();
   }

   public void M() {
      this.o.f();
      this.t.clear();
      this.p = this.Q = null;
      this.d = null;
      if (this.u != null) {
         this.u.close();
         this.u = null;
      }
   }

   private boolean c(Entity var1) {
      LarpClient var2 = LarpClient.R();
      if (var1 instanceof Player && var2 != null) {
         MCFriendModule var3 = var2.z().p(MCFriendModule.class);
         return var3 != null && var3.S(var1.getName().getString());
      } else {
         return false;
      }
   }

   private boolean m() {
      if (this.isRuntimeActive() && this.isInGame()) {
         if (this.p != getMc().level || this.Q != this.getPlayer()) {
            this.M();
            this.p = getMc().level;
            this.Q = this.getPlayer();
         }

         return true;
      } else {
         return false;
      }
   }

   public void k(Entity var1) {
      if (this.isEnabled()) {
         if (this.m() && var1 instanceof LivingEntity var2 && var1 != this.getPlayer()) {
            this.o.I(var1.getId(), var1, var2.getHealth(), cf.I());
         }
      }
   }

   public void d(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         if (this.m()) {
            Entity var2 = getMc().level.getEntity(var1.entityId());
            if (var2 instanceof LivingEntity var3 && (var2 != this.getPlayer() || this.m.getValue())) {
               Entity var4 = var1.getSource(getMc().level).getEntity();
               if (var2 == this.getPlayer() || var4 == this.getPlayer() || this.g.getValue() && this.c(var4)) {
                  this.o.I(var2.getId(), var2, var3.getHealth(), cf.I());
               }
            }
         }
      }
   }

   public void W(ClientboundAnimatePacket var1) {
      if (this.isEnabled()) {
         if (this.m() && this.g.getValue() && (var1.getAction() == 0 || var1.getAction() == 3)) {
            if (getMc().level.getEntity(var1.getId()) instanceof LivingEntity var3 && this.c(var3) && (this.t.size() < 128 || this.t.containsKey(var3))) {
               this.t.put(var3, cf.I() + 2.5);
            }
         }
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.m();
      boolean var12 = false;
      byte var11 = (byte)var10000;
      var10000 = Integer.signum(var11);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.M();
      } else {
         Vec3 var1 = this.getPlayer().position();
         if (this.d != null) {
            double var18;
            var10000 = (var18 = var1.distanceToSqr(this.d) - 256.0) == 0.0 ? 0 : (var18 < 0.0 ? -1 : 1);
            boolean var14 = false;
            int var13 = var10000;
            if ((-Integer.signum(var13) >>> 31 ^ 1) == 0) {
               this.M();
               this.p = getMc().level;
               this.Q = this.getPlayer();
            }
         }

         this.d = var1;
         double var2 = cf.I();
         this.t.entrySet().removeIf(var2x -> var2 > var2x.getValue() || var2x.getKey().isRemoved());
         this.o.Q(var2, var0 -> ((Entity)var0).isRemoved());

         for (Entity var5 : getMc().level.entitiesForRendering()) {
            if (var5 instanceof LivingEntity var6 && !var5.isRemoved() && !(var5.distanceToSqr(this.getPlayer()) > 64.0)) {
               Object var7 = var6.getLastHurtByMob();
               if (var7 == null) {
                  var7 = var6.getLastHurtByPlayer();
               }

               int var8 = var5 == this.getPlayer() ? this.m.getValue() : (var7 == this.getPlayer() ? 1 : 0);
               if (var5 != this.getPlayer() && this.g.getValue()) {
                  var8 |= this.c((Entity)var7);
                  if (var8 == 0) {
                     for (LivingEntity var10 : this.t.keySet()) {
                        if (var5.distanceToSqr(var10) <= 16.0) {
                           var8 = 1;
                           break;
                        }
                     }
                  }
               }

               if (var5 == this.getPlayer() && !this.m.getValue()) {
                  this.o.N(var5.getId());
               }

               Vec3 var15 = var5.position();
               this.o.D(var5.getId(), var5, var6.getHealth(), var15.x, var15.y, var15.z, var6.getBbHeight(), (boolean)var8, var2, this.A.getValue() / 1000.0);
            }
         }
      }
   }

   public void g(CameraRenderState var1) {
      if (this.isEnabled()) {
         if (this.m()) {
            if (this.u == null) {
               this.u = new b0();
            }

            ArrayList var2 = new ArrayList();
            double var3 = cf.F();
            Vector3f var5 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1.orientation);
            double var6 = Math.max(0.01, Math.abs(var1.projectionMatrix.m11()));
            double var8 = Math.max(1, getMc().getWindow().getGuiScaledHeight());

            for (jz.o var11 : this.o.U(var3)) {
               double var12 = var11.x() - var1.pos.x;
               double var14 = var11.y() - var1.pos.y;
               double var16 = var11.z() - var1.pos.z;
               double var18 = var12 * var5.x + var14 * var5.y + var16 * var5.z;
               if (!(var18 <= 0.05)) {
                  int var20 = this.J.O("Custom") ? this.q.getValue() : jz.C(var11.damage());
                  double var21 = 0.5 * var18 / (var8 * var6 * 0.01890625);
                  var2.add(new b0.k(var11.text(), var11.x(), var11.y(), var11.z(), var21 * this.w.getValue() * var11.scale(), uh.H(var20, var11.opacity())));
               }
            }

            this.u.Q(var2, var1);
         }
      }
   }

   public void D(PoseStack var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.u != null && this.p == getMc().level) {
            this.u.P(var1, var2, true);
         }
      }
   }

   public void w() {
      if (this.u != null) {
         this.u.i();
      }
   }
}
