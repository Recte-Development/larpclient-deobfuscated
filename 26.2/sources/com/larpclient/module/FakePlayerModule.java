package com.larpclient.module;

import com.mojang.authlib.GameProfile;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public final class FakePlayerModule extends Module {
   private static final UUID y = UUID.fromString("32d0a964-137a-2c03-7cc9-df6700000000");
   private static final String E = "LARPCLIENT_NPC";
   private static final int Z = -2000000000;
   private static FakePlayerModule p;
   private RemotePlayer o;
   private long A;
   private int Y;

   public FakePlayerModule() {
      super("Fake Player", "Spawns a local fake player for combat and visual-effect testing", Category.PLAYER, -1);
      p = this;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.k);
   }

   public static void n(Entity var0) {
      if (p != null && p.isEnabled()) {
         FakePlayerModule var1 = p;
         if (var1 != null && var1.isRuntimeActive() && var1.o != null && var0 == var1.o) {
            var1.m();
         }
      }
   }

   @Override
   public void onEnable() {
      Minecraft var1 = getMc();
      LocalPlayer var2 = var1.player;
      ClientLevel var3 = var1.level;
      RemotePlayer var4 = new RemotePlayer(var3, new GameProfile(y, "LARPCLIENT_NPC"));
      var4.setId(-2000000000);
      var4.snapTo(var2.getX(), var2.getY(), var2.getZ(), var2.getYRot(), var2.getXRot());
      var4.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.NETHERITE_SWORD));
      var4.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.TOTEM_OF_UNDYING));
      var4.setHealth(20.0F);
      var4.setAbsorptionAmount(0.0F);
      var3.addEntity(var4);
      this.o = var4;
      this.Y = 0;
      com.larpclient.h.U("FakePlayer", "Spawned at {} {} {}", var2.getX(), var2.getY(), var2.getZ());
   }

   @Override
   public void onDisable() {
      if (this.o != null) {
         this.o.discard();
         com.larpclient.h.U("FakePlayer", "Removed");
      }

      this.o = null;
   }

   @Override
   public void onTick() {
      LocalPlayer var1 = this.getPlayer();
      if (var1 != null && this.o != null) {
         this.o.setYRot(0.0F);
         this.o.setXRot(0.0F);
         this.o.setYHeadRot(0.0F);
         this.o.setYBodyRot(0.0F);
         this.o.setMainArm(var1.getMainArm());
         boolean var3 = false;
         int var2 = this.Y;
         if ((-Integer.signum(var2) >>> 31 ^ 1) == 0) {
            byte var5 = 1;
            int var4 = this.Y;
            int var10000 = (this.Y = var4 - var5) % 20;
            boolean var7 = false;
            int var6 = var10000;
            var10000 = Integer.signum(var6);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.o.setHealth(Math.min(this.o.getHealth() + 1.0F, 20.0F));
            }
         }
      }
   }

   private void m() {
      LocalPlayer var1 = this.getPlayer();
      if (var1 != null && !(var1.distanceToSqr(this.o) > 36.0)) {
         float var2 = this.A(var1);
         long var3 = System.currentTimeMillis();
         if (var3 - this.A >= 100L) {
            var1.playSound(y(var1) ? SoundEvents.PLAYER_ATTACK_CRIT : SoundEvents.PLAYER_ATTACK_STRONG, 1.0F, 1.0F);
            this.A = var3;
         }

         this.y(var2);
      }
   }

   private static boolean y(LocalPlayer var0) {
      return var0.getDeltaMovement().y < -0.08 && !var0.onGround() && !var0.isSprinting();
   }

   private float A(LocalPlayer var1) {
      float var2 = var1.getAttackStrengthScale(0.5F);
      float var3 = (float)(var1.getAttributeValue(Attributes.ATTACK_DAMAGE) * (0.2F + var2 * var2 * 0.8F));
      if (y(var1)) {
         var3 *= 1.5F;
      }

      ItemStack var4 = var1.getMainHandItem();
      int var5 = var1.level()
         .registryAccess()
         .lookupOrThrow(Registries.ENCHANTMENT)
         .get(Enchantments.SHARPNESS)
         .map(var1x -> EnchantmentHelper.getItemEnchantmentLevel(var1x, var4))
         .orElse(0);
      return var5 > 0 ? var3 + 0.5F + 0.5F * var5 : var3;
   }

   private void y(float var1) {
      float var2 = this.o.getAbsorptionAmount();
      if (var2 > 0.0F) {
         float var3 = Math.min(var2, var1);
         this.o.setAbsorptionAmount(var2 - var3);
         var1 -= var3;
      }

      if (var1 > 0.0F) {
         this.o.setHealth(Math.max(this.o.getHealth() - var1, 0.5F));
      }

      this.o.hurtTime = 10;
      this.o.invulnerableTime = 10;
      this.c(MobEffects.REGENERATION, 100, 900);
      this.c(MobEffects.ABSORPTION, 20, 100);
      if (this.o.getHealth() <= 1.0F && this.o.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
         this.K();
      }
   }

   private void c(Holder<MobEffect> var1, int var2, int var3) {
      MobEffectInstance var4 = this.o.getEffect(var1);
      if (var4 == null || var4.getDuration() < var2) {
         this.o.addEffect(new MobEffectInstance(var1, var3, 1));
      }
   }

   private void K() {
      Minecraft var1 = getMc();
      var1.player.playSound(SoundEvents.TOTEM_USE, 1.0F, 1.0F);
      var1.particleEngine.createTrackingEmitter(this.o, ParticleTypes.TOTEM_OF_UNDYING, 30);
      this.o.removeAllEffects();
      this.o.setHealth(4.0F);
      this.o.setAbsorptionAmount(12.0F);
      this.Y = 1400;
      com.larpclient.h.U("FakePlayer", "Totem popped");
   }
}
