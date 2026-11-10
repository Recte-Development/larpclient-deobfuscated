package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.h5;
import com.larpclient.he;
import com.larpclient.ic;
import com.larpclient.iy;
import com.larpclient.p3;
import com.larpclient.qc;
import com.larpclient.vv;
import com.larpclient.wu;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
   private final m m = this.addSetting(new m("Players", "Show for players", true));
   private final m W = this.addSetting(new m("Self Nametag", "Show your own tag in third person or a detached camera", false));
   private final m L = this.addSetting(new m("Mobs", "Show for hostile mobs", false));
   private final m U = this.addSetting(new m("Animals", "Show for animals", false));
   private final m x = this.addSetting(new m("Health", "Show current health", true));
   private final m z = this.addSetting(new m("Distance", "Show distance", true));
   private final m O = this.addSetting(new m("Items", "Show armor and held-item icons above the name", true));
   private final b Z = this.addSetting(new b("Range", "Max render range", 128.0, 8.0, 512.0, 8.0));
   private final b a = this.addSetting(new b("Scale", "Nametag scale", 1.0, 0.5, 2.0, 0.1));
   private final b S = this.addSetting(new b("Height", "Gap above the entity", 0.25, 0.1, 1.5, 0.05));
   private final b r = this.addSetting(new b("Opacity", "Background opacity", 0.65, 0.0, 1.0, 0.05));
   private final m Q = this.addSetting(new m("Background", "Draw label background", true));
   private final Set<Integer> w = new HashSet<>();
   private static final int n = -15;
   private static final int s = 4;
   private static final int K = 3;
   private static final int X = 4;
   private static final float N = 0.75F;
   private static final float i = 13.0F;
   private static final int J = -15329768;
   private static final int C = -1;
   private static final int g = -4934470;
   private static final int h = -41892;

   public NametagsModule() {
      super("Nametags", "Show names, health and gear above entities", Category.RENDER, -1);
      this.r.a(this.Q::getValue);
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "nametags"), this::o);
      this.W.w(var0 -> _q.c("Nametags", "Self nametag {} in third person or a detached camera", var0 ? "enabled" : "disabled"));
   }

   public boolean q(Entity var1) {
      return !this.isEnabled() ? false : this.isOperational() && this.w.contains(var1.getId());
   }

   public List<r<?>> A() {
      return List.of(this.m, this.W, this.L, this.U);
   }

   public List<r<?>> I() {
      return List.of(this.x, this.z, this.O);
   }

   public List<r<?>> X() {
      return List.of(this.Z, this.a, this.S, this.Q, this.r);
   }

   @Override
   public void onEnable() {
      _q.c("Nametags", "Replacement labels suppress native names and below-name scoreboard text");
   }

   @Override
   public void onDisable() {
      this.w.clear();
   }

   private boolean k(boolean var1, boolean var2) {
      return var1 ? this.W.getValue() && var2 : this.m.getValue();
   }

   private void o(GuiGraphicsExtractor var1, DeltaTracker var2) {
      this.w.clear();
      if (this.isOperational() && this.isInGame() && !mc.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         var1 = qc.h(var1, true, false);
         if (var1 != null) {
            long var3 = he.s();
            Camera var5 = mc.gameRenderer.mainCamera();
            Vec3 var6 = var5.position();
            Matrix4f var7 = var5.getViewRotationProjectionMatrix(new Matrix4f());
            Vector3f var8 = var5.rotation().transform(new Vector3f(0.0F, 0.025F * this.a.getValue().floatValue(), 0.0F));
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
                        ? this.k(var15 == mc.player, var5.isDetached())
                        : (var15 instanceof Enemy ? this.L.getValue() : var15 instanceof Animal && this.U.getValue())
                  )) {
                  double var17 = mc.player.distanceTo(var15);
                  if (!(var17 > this.Z.getValue())) {
                     Vec3 var19 = var15.getPosition(var12);
                     Vec3 var20 = var15.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, var15.getYRot(var12));
                     double var21 = Math.max(var15.getBbHeight(), var20 == null ? 0.0 : var20.y);
                     iy.a var23 = iy.A(
                        var19.add(var20 == null ? 0.0 : var20.x, var21 + this.S.getValue(), var20 == null ? 0.0 : var20.z),
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

                        String var24 = this.x.getValue() ? String.format(Locale.ROOT, "%.1f", wu.W(var16)) : null;
                        String var25 = this.z.getValue() && var15 != mc.player ? (int)var17 + "m" : null;
                        var1.pose().pushMatrix();

                        try {
                           var1.pose().translate(var23.x(), var23.y()).scale(var23.scale());
                           MutableComponent var26 = Component.empty()
                              .append(ic.D(ic.P(p3.J(var15.getUUID(), var15.getName().getString()), 160, ic.b.R), ic.b.R));
                           if (var24 != null) {
                              var26.append(V(" [", -4934470)).append(V(var24, -41892)).append(V("]", -4934470));
                           }

                           if (var25 != null) {
                              var26.append(V(" " + var25, -4934470));
                           }

                           int var27 = this.Q.getValue() ? vv.N(-15329768, this.r.getValue()) : 0;
                           this.l(var1, var26, -15, -1, var27);
                           if (this.O.getValue()) {
                              ArrayList var28 = new ArrayList(6);

                              for (EquipmentSlot var32 : new EquipmentSlot[]{
                                 EquipmentSlot.MAINHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.OFFHAND
                              }) {
                                 ItemStack var33 = var16.getItemBySlot(var32);
                                 if (!var33.isEmpty()) {
                                    var28.add(var33);
                                 }
                              }

                              float var46 = -32.0F;

                              for (int var47 = 0; var47 < var28.size(); var47++) {
                                 var1.pose().pushMatrix();

                                 try {
                                    var1.pose().translate((var47 - (var28.size() - 1) / 2.0F) * 13.0F - 6.0F, var46).scale(0.75F);
                                    var1.item(var16, (ItemStack)var28.get(var47), 0, 0, var15.getId());
                                 } finally {
                                    var1.pose().popMatrix();
                                 }
                              }
                           }

                           this.w.add(var15.getId());
                        } catch (RuntimeException var43) {
                           _q.g("Render", "Nametag submission failed", var43);
                        } finally {
                           var1.pose().popMatrix();
                        }
                     }
                  }
               }
            }

            he.T("Nametags", var3);
         }
      }
   }

   private static MutableComponent V(String var0, int var1) {
      return ic.D(var0, ic.b.c).copy().withColor(var1 & 16777215);
   }

   private void l(GuiGraphicsExtractor var1, Component var2, int var3, int var4, int var5) {
      int var6 = mc.font.width(var2);
      int var7 = -var6 / 2;
      if (var5 != 0) {
         h5.q(var1, var7 - 4, var3 - 3, var6 + 8, 15.0F, 4.0F, var5);
      }

      var1.text(mc.font, var2, var7, var3, var4, var5 == 0);
   }
}
