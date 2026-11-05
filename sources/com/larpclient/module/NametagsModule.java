package com.larpclient.module;

import com.larpclient._3;
import com.larpclient.k6;
import com.larpclient.kx;
import com.larpclient.mt;
import com.larpclient.t3;
import com.larpclient.t7;
import com.larpclient.zo;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class NametagsModule extends Module {
   private final dh p = this.addSetting(new dh("Players", "Show for players", true));
   private final dh Y = this.addSetting(new dh("Self Nametag", "Show your own tag in third person or a detached camera", false));
   private final dh r = this.addSetting(new dh("Mobs", "Show for hostile mobs", false));
   private final dh z = this.addSetting(new dh("Animals", "Show for animals", false));
   private final dh D = this.addSetting(new dh("Health", "Show current health", true));
   private final dh O = this.addSetting(new dh("Distance", "Show distance", true));
   private final dh T = this.addSetting(new dh("Items", "Show armor and held-item icons above the name", true));
   private final dh b = this.addSetting(new dh("Ping", "Show player ping", true));
   private final d9 k = this.addSetting(new d9("Range", "Max render range", 128.0, 8.0, 512.0, 8.0));
   private final d9 G = this.addSetting(new d9("Scale", "Nametag scale", 1.0, 0.5, 2.0, 0.1));
   private final d9 i = this.addSetting(new d9("Height", "Gap above the entity", 0.25, 0.1, 1.5, 0.05));
   private final d9 K = this.addSetting(new d9("Opacity", "Background opacity", 0.65, 0.0, 1.0, 0.05));
   private final dh S = this.addSetting(new dh("Background", "Draw label background", true));
   private final Set<Integer> h = new HashSet<>();

   public NametagsModule() {
      super("Nametags", "Show names, health and gear above entities", Category.RENDER, -1);
      this.K.H(this.S::P);
      this.b.H(() -> this.p.P() || this.Y.P());
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "nametags"), this::P);
      this.Y.h(var0 -> mt.U("Nametags", "Self nametag {} in third person or a detached camera", var0 ? "enabled" : "disabled"));
   }

   public boolean y(Entity var1) {
      return !this.isEnabled() ? false : this.isOperational() && this.h.contains(var1.getId());
   }

   public List<df<?>> a() {
      return List.of(this.p, this.Y, this.r, this.z);
   }

   public List<df<?>> z() {
      return List.of(this.D, this.O, this.T, this.b);
   }

   public List<df<?>> t() {
      return List.of(this.k, this.G, this.i, this.S, this.K);
   }

   @Override
   public void onEnable() {
      mt.U("Nametags", "Replacement labels suppress native names and below-name scoreboard text");
   }

   @Override
   public void onDisable() {
      this.h.clear();
   }

   private boolean P(boolean var1, boolean var2) {
      return var1 ? this.Y.P() && var2 : this.p.P();
   }

   private void P(GuiGraphicsExtractor var1, DeltaTracker var2) {
      this.h.clear();
      if (this.isOperational() && this.isInGame() && !mc.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         var1 = zo.R(var1, true, false);
         if (var1 != null) {
            long var3 = kx.G();
            Camera var5 = mc.gameRenderer.mainCamera();
            Vec3 var6 = var5.position();
            Matrix4f var7 = var5.getViewRotationProjectionMatrix(new Matrix4f());
            Vector3f var8 = var5.rotation().transform(new Vector3f(0.0F, 0.025F * this.G.P().floatValue(), 0.0F));
            int var9 = mc.getWindow().getGuiScaledWidth();
            int var10 = mc.getWindow().getGuiScaledHeight();
            boolean var11 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
            float var12 = var2.getGameTimeDeltaPartialTick(false);
            int var13 = 0;

            for (Entity var15 : mc.level.entitiesForRendering()) {
               if (var15 instanceof LivingEntity var16
                  && var16.isAlive()
                  && (
                     var15 instanceof Player
                        ? this.P(var15 == mc.player, var5.isDetached())
                        : (var15 instanceof Enemy ? this.r.P() : var15 instanceof Animal && this.z.P())
                  )) {
                  double var17 = mc.player.distanceTo(var15);
                  if (!(var17 > this.k.P())) {
                     Vec3 var19 = var15.getPosition(var12);
                     Vec3 var20 = var15.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, var15.getYRot(var12));
                     double var21 = Math.max(var15.getBbHeight(), var20 == null ? 0.0 : var20.y);
                     com.larpclient.di.a var23 = com.larpclient.di.k(
                        var19.add(var20 == null ? 0.0 : var20.x, var21 + this.i.P(), var20 == null ? 0.0 : var20.z), var6, var7, var8, var9, var10, var11
                     );
                     if (var23 != null) {
                        if (++var13 > 128) {
                           break;
                        }

                        ArrayList var24 = new ArrayList();
                        if (this.D.P()) {
                           var24.add((int)Math.ceil(t3.Z(var16)) + " HP");
                        }

                        if (this.O.P()) {
                           var24.add((int)var17 + "m");
                        }

                        if (this.b.P() && var15 instanceof Player var25 && mc.getConnection() != null) {
                           PlayerInfo var26 = mc.getConnection().getPlayerInfo(var25.getUUID());
                           if (var26 != null) {
                              var24.add(var26.getLatency() + "ms");
                           }
                        }

                        String var42 = String.join(" · ", var24);
                        var1.pose().pushMatrix();

                        try {
                           var1.pose().translate(var23.x(), var23.y()).scale(var23.scale());
                           Component var43 = k6.X(k6.Q(t7.A(var15.getUUID(), var15.getName().getString()), 160, k6.v.B), k6.v.B);
                           Component var27 = k6.X(k6.n(var42.trim(), 180), k6.v.K);
                           int var28 = this.S.P() ? _3.A(com.larpclient.f.i, this.K.P()) : 0;
                           int var29 = var42.isEmpty() ? -16 : -30;
                           this.Q(var1, var43, var29, com.larpclient.f.J, var28);
                           if (!var42.isEmpty()) {
                              this.Q(var1, var27, -16, -3090461, var28);
                           }

                           if (this.T.P()) {
                              ArrayList var30 = new ArrayList(6);

                              for (EquipmentSlot var34 : new EquipmentSlot[]{
                                 EquipmentSlot.MAINHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.OFFHAND
                              }) {
                                 ItemStack var35 = var16.getItemBySlot(var34);
                                 if (!var35.isEmpty()) {
                                    var30.add(var35);
                                 }
                              }

                              int var44 = var29 - 20;

                              for (int var45 = 0; var45 < var30.size(); var45++) {
                                 var1.item(
                                    var16, (ItemStack)var30.get(var45), Math.round((var45 - (var30.size() - 1) / 2.0F) * 18.0F) - 8, var44, var15.getId()
                                 );
                              }
                           }

                           this.h.add(var15.getId());
                        } catch (RuntimeException var39) {
                           mt.B("Render", "Nametag submission failed", var39);
                        } finally {
                           var1.pose().popMatrix();
                        }
                     }
                  }
               }
            }

            kx.u("Nametags", var3);
         }
      }
   }

   private void Q(GuiGraphicsExtractor var1, Component var2, int var3, int var4, int var5) {
      int var6 = mc.font.width(var2);
      int var7 = -var6 / 2;
      if (var5 != 0) {
         var1.fill(var7 - 1, var3 - 1, var7 + var6 + 1, var3 + 9, var5);
      }

      var1.text(mc.font, var2, var7, var3, var4, true);
   }
}
