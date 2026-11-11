package com.larpclient.module.misc;

import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.misc.LarpLog;
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
   private static final UUID nPC_UUID = UUID.fromString("32d0a964-137a-2c03-7cc9-df6700000000");
   private static final String nPC_NAME = "LARPCLIENT_NPC";
   private static final int nPC_ENTITY_ID = -2000000000;
   private static FakePlayerModule instance;
   private RemotePlayer npc;
   private long lastHitSoundMs;
   private int regenTicks;

   public FakePlayerModule() {
      super("Fake Player", "Spawns a local fake player for combat and visual-effect testing", Category.PLAYER, -1);
      instance = this;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.iDLE);
   }

   public static void onEntityAttacked(Entity var0) {
      if (instance != null && instance.isEnabled()) {
         FakePlayerModule var1 = instance;
         if (var1 != null && var1.isRuntimeActive() && var1.npc != null && var0 == var1.npc) {
            var1.handleHit();
         }
      }
   }

   @Override
   public void onEnable() {
      Minecraft var1 = getMc();
      LocalPlayer var2 = var1.player;
      ClientLevel var3 = var1.level;
      RemotePlayer var4 = new RemotePlayer(var3, new GameProfile(nPC_UUID, "LARPCLIENT_NPC"));
      var4.setId(-2000000000);
      var4.snapTo(var2.getX(), var2.getY(), var2.getZ(), var2.getYRot(), var2.getXRot());
      var4.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.NETHERITE_SWORD));
      var4.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.TOTEM_OF_UNDYING));
      var4.setHealth(20.0F);
      var4.setAbsorptionAmount(0.0F);
      var3.addEntity(var4);
      this.npc = var4;
      this.regenTicks = 0;
      LarpLog.debug("FakePlayer", "Spawned at {} {} {}", var2.getX(), var2.getY(), var2.getZ());
   }

   @Override
   public void onDisable() {
      if (this.npc != null) {
         this.npc.discard();
         LarpLog.debug("FakePlayer", "Removed");
      }

      this.npc = null;
   }

   @Override
   public void onTick() {
      LocalPlayer var1 = this.getPlayer();
      if (var1 != null && this.npc != null) {
         this.npc.setYRot(0.0F);
         this.npc.setXRot(0.0F);
         this.npc.setYHeadRot(0.0F);
         this.npc.setYBodyRot(0.0F);
         this.npc.setMainArm(var1.getMainArm());
         boolean var3 = false;
         int var2 = this.regenTicks;
         if ((-Integer.signum(var2) >>> 31 ^ 1) == 0) {
            byte var5 = 1;
            int var4 = this.regenTicks;
            int var10000 = (this.regenTicks = var4 - var5) % 20;
            boolean var7 = false;
            int var6 = var10000;
            var10000 = Integer.signum(var6);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.npc.setHealth(Math.min(this.npc.getHealth() + 1.0F, 20.0F));
            }
         }
      }
   }

   private void handleHit() {
      LocalPlayer var1 = this.getPlayer();
      if (var1 != null && !(var1.distanceToSqr(this.npc) > 36.0)) {
         float var2 = this.computeDamage(var1);
         long var3 = System.currentTimeMillis();
         if (var3 - this.lastHitSoundMs >= 100L) {
            var1.playSound(isCritical(var1) ? SoundEvents.PLAYER_ATTACK_CRIT : SoundEvents.PLAYER_ATTACK_STRONG, 1.0F, 1.0F);
            this.lastHitSoundMs = var3;
         }

         this.applyDamage(var2);
      }
   }

   private static boolean isCritical(LocalPlayer var0) {
      return var0.getDeltaMovement().y < -0.08 && !var0.onGround() && !var0.isSprinting();
   }

   private float computeDamage(LocalPlayer var1) {
      float var2 = var1.getAttackStrengthScale(0.5F);
      float var3 = (float)(var1.getAttributeValue(Attributes.ATTACK_DAMAGE) * (0.2F + var2 * var2 * 0.8F));
      if (isCritical(var1)) {
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

   private void applyDamage(float var1) {
      float var2 = this.npc.getAbsorptionAmount();
      if (var2 > 0.0F) {
         float var3 = Math.min(var2, var1);
         this.npc.setAbsorptionAmount(var2 - var3);
         var1 -= var3;
      }

      if (var1 > 0.0F) {
         this.npc.setHealth(Math.max(this.npc.getHealth() - var1, 0.5F));
      }

      this.npc.hurtTime = 10;
      this.npc.setInvulnerableTime(10);
      this.ensureEffect(MobEffects.REGENERATION, 100, 900);
      this.ensureEffect(MobEffects.ABSORPTION, 20, 100);
      if (this.npc.getHealth() <= 1.0F && this.npc.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
         this.popTotem();
      }
   }

   private void ensureEffect(Holder<MobEffect> var1, int var2, int var3) {
      MobEffectInstance var4 = this.npc.getEffect(var1);
      if (var4 == null || var4.getDuration() < var2) {
         this.npc.addEffect(new MobEffectInstance(var1, var3, 1));
      }
   }

   private void popTotem() {
      Minecraft var1 = getMc();
      var1.player.playSound(SoundEvents.TOTEM_USE, 1.0F, 1.0F);
      var1.particleEngine.createTrackingEmitter(this.npc, ParticleTypes.TOTEM_OF_UNDYING, 30);
      this.npc.removeAllEffects();
      this.npc.setHealth(4.0F);
      this.npc.setAbsorptionAmount(12.0F);
      this.regenTicks = 1400;
      LarpLog.debug("FakePlayer", "Totem popped");
   }
}
