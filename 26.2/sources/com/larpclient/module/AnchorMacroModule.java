package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.t1;
import com.larpclient.ta;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;

public final class AnchorMacroModule extends Module {
   private static AnchorMacroModule N;
   private final e D = this.addSetting(new e("Delay", "Milliseconds to wait after a slot switch before using the anchor", 0.0, 0.0, 500.0, 5.0));
   private final e J = this.addSetting(new e("Charge Delay", "Minimum milliseconds between glowstone charges", 0.0, 0.0, 500.0, 5.0));
   private final e I = this.addSetting(new e("Totem Slot", "Hotbar slot to move to once the anchor is charged", 1.0, 1.0, 9.0, 1.0));
   private final s s = this.addSetting(new s("Auto Explode", "Detonate the charged anchor from the totem slot", false));
   private final s t = this.addSetting(new s("Spam", "Keep working on anchors while Use stays held; off needs a new press for each one", true));
   private boolean K;
   private boolean k;
   private int P = -1;
   private long S;
   private long z;
   private String G = "Hold Use on an anchor";

   public AnchorMacroModule() {
      super("Anchor Macro", "Hold Use on an anchor to charge it and move to your totem slot", Category.COMBAT, -1);
      N = this;
   }

   public static boolean K() {
      if (N != null && N.isEnabled()) {
         AnchorMacroModule var0 = N;
         return var0 != null && var0.isRuntimeActive() && !var0.M() ? var0.K || mc.options.keyUse.isDown() && var0.I() != null : false;
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      return this.G;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   @Override
   public void onTick() {
      t1.v(this);
      if (ta.Y()) {
         int var10000 = mc.isWindowActive();
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 != 0) {
            if (this.M()) {
               this.j("AnchorTap handles anchors while it is enabled");
               return;
            }

            if (!mc.options.keyUse.isDown()) {
               this.j("Hold Use on an anchor");
               return;
            }

            boolean var10 = false;
            byte var9 = this.K;
            var10000 = Integer.signum(var9);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               boolean var12 = false;
               byte var11 = this.k;
               var10000 = Integer.signum(var11);
               if ((var10000 | -var10000) >>> 31 == 0 && !this.t.getValue()) {
                  return;
               }
            }

            BlockHitResult var1 = this.I();
            if (var1 == null) {
               return;
            }

            this.K = this.k = (boolean)1;
            BlockPos var2 = var1.getBlockPos().immutable();
            long var3 = System.currentTimeMillis();
            var10000 = this.r(var2);
            boolean var8 = false;
            int var7 = var10000;
            var10000 = Integer.signum(var7);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.L(var2, var3);
            } else {
               this.K(var2, var3);
            }

            return;
         }
      }

      this.j("Anchor interaction suspended");
   }

   private void L(BlockPos var1, long var2) {
      if (var2 - this.z < Math.round(this.J.getValue())) {
         this.G = "Waiting for Charge Delay";
      } else {
         int var4 = this.f();
         if (var4 == -1) {
            this.G = "No glowstone in hotbar";
         } else if (!this.M(var4, var2)) {
            this.G = "Waiting for Delay";
         } else {
            this.w(var1, Items.GLOWSTONE, "Charging anchor", () -> this.r(var1) == 0, var2x -> {
               this.z = System.currentTimeMillis();
               com.larpclient.h.A("AnchorMacro", "Charge at {}: {}", var1.toShortString(), var2x ? "sent" : "rejected");
            });
         }
      }
   }

   private void K(BlockPos var1, long var2) {
      if (!this.M(this.I.getValue().intValue() - 1, var2)) {
         this.G = "Waiting for Delay";
      } else if (!this.s.getValue()) {
         this.k = (boolean)0;
         this.G = "Holding totem slot";
      } else {
         ItemStack var4 = mc.player.getMainHandItem();
         if (!var4.is(Items.GLOWSTONE) && !mc.player.getOffhandItem().is(Items.GLOWSTONE)) {
            this.w(var1, var4.isEmpty() ? Items.AIR : var4.getItem(), "Detonating anchor", () -> this.r(var1) > 0, var2x -> {
               if (var2x) {
                  this.k = (boolean)0;
               }

               com.larpclient.h.A("AnchorMacro", "Detonation at {}: {}", var1.toShortString(), var2x ? "sent" : "rejected");
            });
         } else {
            this.G = "Glowstone in hand blocks detonation";
         }
      }
   }

   private void w(BlockPos var1, Item var2, String var3, BooleanSupplier var4, Consumer<Boolean> var5) {
      boolean var6 = t1.G(this, var1, var2, mc.player.blockInteractionRange(), false, false, var4, var5);
      this.G = var6 ? var3 : "Aim at the anchor within reach";
   }

   private BlockHitResult I() {
      if (ta.Y()
         && mc.isWindowActive()
         && !mc.player.isUsingItem()
         && !mc.player.isSecondaryUseActive()
         && mc.hitResult instanceof BlockHitResult var1
         && var1.getType() == Type.BLOCK) {
         BlockPos var3 = var1.getBlockPos();
         return mc.level.getBlockState(var3).is(Blocks.RESPAWN_ANCHOR)
               && !Boolean.TRUE.equals(mc.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, var3))
            ? var1
            : null;
      } else {
         return null;
      }
   }

   private int r(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1);
      return var2.is(Blocks.RESPAWN_ANCHOR) ? (Integer)var2.getValue(RespawnAnchorBlock.CHARGE) : 0;
   }

   private int f() {
      Inventory var1 = mc.player.getInventory();
      if (mc.player.getMainHandItem().is(Items.GLOWSTONE)) {
         return var1.getSelectedSlot();
      }

      for (int var2 = 0; var2 < 9; var2++) {
         if (var1.getItem(var2).is(Items.GLOWSTONE)) {
            return var2;
         }
      }

      return -1;
   }

   private boolean M(int var1, long var2) {
      Inventory var4 = mc.player.getInventory();
      long var5 = Math.round(this.D.getValue());
      if (var4.getSelectedSlot() != var1) {
         var4.setSelectedSlot(var1);
         this.P = var1;
         this.S = var2;
         return var5 == 0L;
      } else {
         return var1 != this.P || var2 - this.S >= var5;
      }
   }

   private boolean M() {
      AnchorTapModule var1 = LarpClient.R().z().p(AnchorTapModule.class);
      return var1 != null && var1.isRuntimeActive();
   }

   private void j(String var1) {
      this.K = this.k = (boolean)0;
      this.P = -1;
      this.S = 0L;
      this.G = var1;
   }

   @Override
   public void onEnable() {
      this.z = 0L;
      this.j("Hold Use on an anchor");
      com.larpclient.h.A(
         "AnchorMacro",
         "Enabled; totemSlot={}, autoExplode={}, spam={}, delay={}ms, chargeDelay={}ms",
         this.I.getValue().intValue(),
         this.s.getValue(),
         this.t.getValue(),
         this.D.getValue().intValue(),
         this.J.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      t1.v(this);
      this.j("Hold Use on an anchor");
      com.larpclient.h.U("AnchorMacro", "Anchor intents cleared");
   }
}
