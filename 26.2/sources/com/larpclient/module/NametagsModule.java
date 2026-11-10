package com.larpclient.module;

import com.larpclient.bj;
import com.larpclient.ci;
import com.larpclient.k7;
import com.larpclient.kv;
import com.larpclient.p2;
import com.larpclient.uh;
import com.larpclient.zj;
import com.larpclient.zy;
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
   private final s e = this.addSetting(new s("Players", "Show for players", true));
   private final s s = this.addSetting(new s("Self Nametag", "Show your own tag in third person or a detached camera", false));
   private final s T = this.addSetting(new s("Mobs", "Show for hostile mobs", false));
   private final s F = this.addSetting(new s("Animals", "Show for animals", false));
   private final s g = this.addSetting(new s("Health", "Show current health", true));
   private final s U = this.addSetting(new s("Distance", "Show distance", true));
   private final s b = this.addSetting(new s("Items", "Show armor and held-item icons above the name", true));
   private final e a = this.addSetting(new e("Range", "Max render range", 128.0, 8.0, 512.0, 8.0));
   private final e r = this.addSetting(new e("Scale", "Nametag scale", 1.0, 0.5, 2.0, 0.1));
   private final e k = this.addSetting(new e("Height", "Gap above the entity", 0.25, 0.1, 1.5, 0.05));
   private final e l = this.addSetting(new e("Opacity", "Background opacity", 0.65, 0.0, 1.0, 0.05));
   private final s I = this.addSetting(new s("Background", "Draw label background", true));
   private final Set<Integer> L = new HashSet<>();
   private static final int h = -15;
   private static final int M = 4;
   private static final int B = 3;
   private static final int V = 4;
   private static final float O = 0.75F;
   private static final float w = 13.0F;
   private static final int x = -15329768;
   private static final int A = -1;
   private static final int E = -4934470;
   private static final int v = -41892;

   public NametagsModule() {
      super("Nametags", "Show names, health and gear above entities", Category.RENDER, -1);
      this.l.P(this.I::getValue);
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "nametags"), this::F);
      this.s.o(var0 -> com.larpclient.h.U("Nametags", "Self nametag {} in third person or a detached camera", var0 ? "enabled" : "disabled"));
   }

   public boolean D(Entity var1) {
      return !this.isEnabled() ? false : this.isOperational() && this.L.contains(var1.getId());
   }

   public List<q<?>> a() {
      return List.of(this.e, this.s, this.T, this.F);
   }

   public List<q<?>> K() {
      return List.of(this.g, this.U, this.b);
   }

   public List<q<?>> y() {
      return List.of(this.a, this.r, this.k, this.I, this.l);
   }

   @Override
   public void onEnable() {
      com.larpclient.h.U("Nametags", "Replacement labels suppress native names and below-name scoreboard text");
   }

   @Override
   public void onDisable() {
      this.L.clear();
   }

   private boolean i(boolean var1, boolean var2) {
      return var1 ? this.s.getValue() && var2 : this.e.getValue();
   }

   private void F(GuiGraphicsExtractor var1, DeltaTracker var2) {
      this.L.clear();
      if (this.isOperational() && this.isInGame() && !mc.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         var1 = p2.x(var1, true, false);
         if (var1 != null) {
            long var3 = zj.m();
            Camera var5 = mc.gameRenderer.mainCamera();
            Vec3 var6 = var5.position();
            Matrix4f var7 = var5.getViewRotationProjectionMatrix(new Matrix4f());
            Vector3f var8 = var5.rotation().transform(new Vector3f(0.0F, 0.025F * this.r.getValue().floatValue(), 0.0F));
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
                        ? this.i(var15 == mc.player, var5.isDetached())
                        : (var15 instanceof Enemy ? this.T.getValue() : var15 instanceof Animal && this.F.getValue())
                  )) {
                  double var17 = mc.player.distanceTo(var15);
                  if (!(var17 > this.a.getValue())) {
                     Vec3 var19 = var15.getPosition(var12);
                     Vec3 var20 = var15.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, var15.getYRot(var12));
                     double var21 = Math.max(var15.getBbHeight(), var20 == null ? 0.0 : var20.y);
                     k7.o var23 = k7.I(
                        var19.add(var20 == null ? 0.0 : var20.x, var21 + this.k.getValue(), var20 == null ? 0.0 : var20.z),
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

                        String var24 = this.g.getValue() ? String.format(Locale.ROOT, "%.1f", zy.f(var16)) : null;
                        String var25 = this.U.getValue() && var15 != mc.player ? (int)var17 + "m" : null;
                        var1.pose().pushMatrix();

                        try {
                           var1.pose().translate(var23.x(), var23.y()).scale(var23.scale());
                           MutableComponent var26 = Component.empty()
                              .append(bj.n(bj.v(ci.S(var15.getUUID(), var15.getName().getString()), 160, bj.j.T), bj.j.T));
                           if (var24 != null) {
                              var26.append(w(" [", -4934470)).append(w(var24, -41892)).append(w("]", -4934470));
                           }

                           if (var25 != null) {
                              var26.append(w(" " + var25, -4934470));
                           }

                           int var27 = this.I.getValue() ? uh.H(-15329768, this.l.getValue()) : 0;
                           this.f(var1, var26, -15, -1, var27);
                           if (this.b.getValue()) {
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

                           this.L.add(var15.getId());
                        } catch (RuntimeException var43) {
                           com.larpclient.h.w("Render", "Nametag submission failed", var43);
                        } finally {
                           var1.pose().popMatrix();
                        }
                     }
                  }
               }
            }

            zj.l("Nametags", var3);
         }
      }
   }

   private static MutableComponent w(String var0, int var1) {
      return bj.n(var0, bj.j.c).copy().withColor(var1 & 16777215);
   }

   private void f(GuiGraphicsExtractor var1, Component var2, int var3, int var4, int var5) {
      int var6 = mc.font.width(var2);
      int var7 = -var6 / 2;
      if (var5 != 0) {
         kv.f(var1, var7 - 4, var3 - 3, var6 + 8, 15.0F, 4.0F, var5);
      }

      var1.text(mc.font, var2, var7, var3, var4, var5 == 0);
   }
}
