package com.larpclient.module;

import com.larpclient._q;
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
   private static final UUID a = UUID.fromString("32d0a964-137a-2c03-7cc9-df6700000000");
   private static final String G = "LARPCLIENT_NPC";
   private static final int i = -2000000000;
   private static FakePlayerModule Y;
   private RemotePlayer y;
   private long v;
   private int A;

   public FakePlayerModule() {
      super("Fake Player", "Spawns a local fake player for combat and visual-effect testing", Category.PLAYER, -1);
      Y = this;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.E);
   }

   public static void I(Entity var0) {
      if (Y != null && Y.isEnabled()) {
         FakePlayerModule var1 = Y;
         if (var1 != null && var1.isRuntimeActive() && var1.y != null && var0 == var1.y) {
            var1.w();
         }
      }
   }

   @Override
   public void onEnable() {
      Minecraft var1 = getMc();
      LocalPlayer var2 = var1.player;
      ClientLevel var3 = var1.level;
      RemotePlayer var4 = new RemotePlayer(var3, new GameProfile(a, "LARPCLIENT_NPC"));
      var4.setId(-2000000000);
      var4.snapTo(var2.getX(), var2.getY(), var2.getZ(), var2.getYRot(), var2.getXRot());
      var4.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.NETHERITE_SWORD));
      var4.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.TOTEM_OF_UNDYING));
      var4.setHealth(20.0F);
      var4.setAbsorptionAmount(0.0F);
      var3.addEntity(var4);
      this.y = var4;
      this.A = 0;
      _q.c("FakePlayer", "Spawned at {} {} {}", var2.getX(), var2.getY(), var2.getZ());
   }

   @Override
   public void onDisable() {
      if (this.y != null) {
         this.y.discard();
         _q.c("FakePlayer", "Removed");
      }

      this.y = null;
   }

   @Override
   public void onTick() {
      LocalPlayer var1 = this.getPlayer();
      if (var1 != null && this.y != null) {
         this.y.setYRot(0.0F);
         this.y.setXRot(0.0F);
         this.y.setYHeadRot(0.0F);
         this.y.setYBodyRot(0.0F);
         this.y.setMainArm(var1.getMainArm());
         boolean var3 = false;
         int var2 = this.A;
         if ((-Integer.signum(var2) >>> 31 ^ 1) == 0) {
            byte var5 = 1;
            int var4 = this.A;
            int var10000 = (this.A = var4 - var5) % 20;
            boolean var7 = false;
            int var6 = var10000;
            var10000 = Integer.signum(var6);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.y.setHealth(Math.min(this.y.getHealth() + 1.0F, 20.0F));
            }
         }
      }
   }

   private void w() {
      LocalPlayer var1 = this.getPlayer();
      if (var1 != null && !(var1.distanceToSqr(this.y) > 36.0)) {
         float var2 = this.r(var1);
         long var3 = System.currentTimeMillis();
         if (var3 - this.v >= 100L) {
            var1.playSound(M(var1) ? SoundEvents.PLAYER_ATTACK_CRIT : SoundEvents.PLAYER_ATTACK_STRONG, 1.0F, 1.0F);
            this.v = var3;
         }

         this.e(var2);
      }
   }

   private static boolean M(LocalPlayer var0) {
      return var0.getDeltaMovement().y < -0.08 && !var0.onGround() && !var0.isSprinting();
   }

   private float r(LocalPlayer var1) {
      float var2 = var1.getAttackStrengthScale(0.5F);
      float var3 = (float)(var1.getAttributeValue(Attributes.ATTACK_DAMAGE) * (0.2F + var2 * var2 * 0.8F));
      if (M(var1)) {
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

   private void e(float var1) {
      float var2 = this.y.getAbsorptionAmount();
      if (var2 > 0.0F) {
         float var3 = Math.min(var2, var1);
         this.y.setAbsorptionAmount(var2 - var3);
         var1 -= var3;
      }

      if (var1 > 0.0F) {
         this.y.setHealth(Math.max(this.y.getHealth() - var1, 0.5F));
      }

      this.y.hurtTime = 10;
      this.y.setInvulnerableTime(10);
      this.O(MobEffects.REGENERATION, 100, 900);
      this.O(MobEffects.ABSORPTION, 20, 100);
      if (this.y.getHealth() <= 1.0F && this.y.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
         this.h();
      }
   }

   private void O(Holder<MobEffect> var1, int var2, int var3) {
      MobEffectInstance var4 = this.y.getEffect(var1);
      if (var4 == null || var4.getDuration() < var2) {
         this.y.addEffect(new MobEffectInstance(var1, var3, 1));
      }
   }

   private void h() {
      Minecraft var1 = getMc();
      var1.player.playSound(SoundEvents.TOTEM_USE, 1.0F, 1.0F);
      var1.particleEngine.createTrackingEmitter(this.y, ParticleTypes.TOTEM_OF_UNDYING, 30);
      this.y.removeAllEffects();
      this.y.setHealth(4.0F);
      this.y.setAbsorptionAmount(12.0F);
      this.A = 1400;
      _q.c("FakePlayer", "Totem popped");
   }
}
