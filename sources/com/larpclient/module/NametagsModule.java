package com.larpclient.module;

import com.larpclient._o;
import com.larpclient.h_;
import com.larpclient.j_;
import com.larpclient.oh;
import com.larpclient.r4;
import com.larpclient.rv;
import com.larpclient.u_;
import com.larpclient.x2;
import com.larpclient.y8;
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
   private final ln B = this.addSetting(new ln("Players", "Show for players", true));
   private final ln M = this.addSetting(new ln("Self Nametag", "Show your own tag in third person or a detached camera", false));
   private final ln y = this.addSetting(new ln("Mobs", "Show for hostile mobs", false));
   private final ln z = this.addSetting(new ln("Animals", "Show for animals", false));
   private final ln x = this.addSetting(new ln("Health", "Show current health", true));
   private final ln p = this.addSetting(new ln("Distance", "Show distance", true));
   private final ln c = this.addSetting(new ln("Items", "Show armor and held-item icons above the name", true));
   private final ln s = this.addSetting(new ln("Ping", "Show player ping", true));
   private final lc L = this.addSetting(new lc("Range", "Max render range", 128.0, 8.0, 512.0, 8.0));
   private final lc g = this.addSetting(new lc("Scale", "Nametag scale", 1.0, 0.5, 2.0, 0.1));
   private final lc b = this.addSetting(new lc("Height", "Gap above the entity", 0.25, 0.1, 1.5, 0.05));
   private final lc N = this.addSetting(new lc("Opacity", "Background opacity", 0.65, 0.0, 1.0, 0.05));
   private final ln Z = this.addSetting(new ln("Background", "Draw label background", true));
   private final Set<Integer> r = new HashSet<>();

   public NametagsModule() {
      super("Nametags", "Show names, health and gear above entities", Category.RENDER, -1);
      this.N.x(this.Z::getValue);
      this.s.x(() -> this.B.getValue() || this.M.getValue());
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "nametags"), this::J);
      this.M.W(var0 -> r4.g("Nametags", "Self nametag {} in third person or a detached camera", var0 ? "enabled" : "disabled"));
   }

   public boolean l(Entity var1) {
      return !this.isEnabled() ? false : this.isOperational() && this.r.contains(var1.getId());
   }

   public List<l6<?>> s() {
      return List.of(this.B, this.M, this.y, this.z);
   }

   public List<l6<?>> w() {
      return List.of(this.x, this.p, this.c, this.s);
   }

   public List<l6<?>> A() {
      return List.of(this.L, this.g, this.b, this.Z, this.N);
   }

   @Override
   public void onEnable() {
      r4.g("Nametags", "Replacement labels suppress native names and below-name scoreboard text");
   }

   @Override
   public void onDisable() {
      this.r.clear();
   }

   private boolean Y(boolean var1, boolean var2) {
      return var1 ? this.M.getValue() && var2 : this.B.getValue();
   }

   private void J(GuiGraphicsExtractor var1, DeltaTracker var2) {
      this.r.clear();
      if (this.isOperational() && this.isInGame() && !mc.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         var1 = y8.U(var1, true, false);
         if (var1 != null) {
            long var3 = h_.U();
            Camera var5 = mc.gameRenderer.mainCamera();
            Vec3 var6 = var5.position();
            Matrix4f var7 = var5.getViewRotationProjectionMatrix(new Matrix4f());
            Vector3f var8 = var5.rotation().transform(new Vector3f(0.0F, 0.025F * this.g.getValue().floatValue(), 0.0F));
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
                        ? this.Y(var15 == mc.player, var5.isDetached())
                        : (var15 instanceof Enemy ? this.y.getValue() : var15 instanceof Animal && this.z.getValue())
                  )) {
                  double var17 = mc.player.distanceTo(var15);
                  if (!(var17 > this.L.getValue())) {
                     Vec3 var19 = var15.getPosition(var12);
                     Vec3 var20 = var15.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, var15.getYRot(var12));
                     double var21 = Math.max(var15.getBbHeight(), var20 == null ? 0.0 : var20.y);
                     _o.t var23 = _o.p(
                        var19.add(var20 == null ? 0.0 : var20.x, var21 + this.b.getValue(), var20 == null ? 0.0 : var20.z),
                        var6,
                        var7,
                        var8,
                        var9,
                        var10,
                        var11
                     );
                     if (var23 != null) {
                        if (++var13 > 128) {
                           break;
                        }

                        ArrayList var24 = new ArrayList();
                        if (this.x.getValue()) {
                           var24.add((int)Math.ceil(u_.B(var16)) + " HP");
                        }

                        if (this.p.getValue()) {
                           var24.add((int)var17 + "m");
                        }

                        if (this.s.getValue() && var15 instanceof Player var25 && mc.getConnection() != null) {
                           PlayerInfo var26 = mc.getConnection().getPlayerInfo(var25.getUUID());
                           if (var26 != null) {
                              var24.add(var26.getLatency() + "ms");
                           }
                        }

                        String var42 = String.join(" · ", var24);
                        var1.pose().pushMatrix();

                        try {
                           var1.pose().translate(var23.x(), var23.y()).scale(var23.scale());
                           Component var43 = j_.v(j_.Y(x2.b(var15.getUUID(), var15.getName().getString()), 160, j_.w.Q), j_.w.Q);
                           Component var27 = j_.v(j_.x(var42.trim(), 180), j_.w.H);
                           int var28 = this.Z.getValue() ? oh.i(rv.H, this.N.getValue()) : 0;
                           int var29 = var42.isEmpty() ? -16 : -30;
                           this.L(var1, var43, var29, rv.P, var28);
                           if (!var42.isEmpty()) {
                              this.L(var1, var27, -16, -3090461, var28);
                           }

                           if (this.c.getValue()) {
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

                           this.r.add(var15.getId());
                        } catch (RuntimeException var39) {
                           r4.T("Render", "Nametag submission failed", var39);
                        } finally {
                           var1.pose().popMatrix();
                        }
                     }
                  }
               }
            }

            h_.n("Nametags", var3);
         }
      }
   }

   private void L(GuiGraphicsExtractor var1, Component var2, int var3, int var4, int var5) {
      int var6 = mc.font.width(var2);
      int var7 = -var6 / 2;
      if (var5 != 0) {
         var1.fill(var7 - 1, var3 - 1, var7 + var6 + 1, var3 + 9, var5);
      }

      var1.text(mc.font, var2, var7, var3, var4, true);
   }
}
