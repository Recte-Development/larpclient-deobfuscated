package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.vi;
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
   private static AnchorMacroModule F;
   private final b s = this.addSetting(new b("Delay", "Milliseconds to wait after a slot switch before using the anchor", 0.0, 0.0, 500.0, 5.0));
   private final b w = this.addSetting(new b("Charge Delay", "Minimum milliseconds between glowstone charges", 0.0, 0.0, 500.0, 5.0));
   private final b d = this.addSetting(new b("Totem Slot", "Hotbar slot to move to once the anchor is charged", 1.0, 1.0, 9.0, 1.0));
   private final m C = this.addSetting(new m("Auto Explode", "Detonate the charged anchor from the totem slot", false));
   private final m l = this.addSetting(new m("Spam", "Keep working on anchors while Use stays held; off needs a new press for each one", true));
   private boolean T;
   private boolean M;
   private int Z = -1;
   private long N;
   private long R;
   private String u = "Hold Use on an anchor";

   public AnchorMacroModule() {
      super("Anchor Macro", "Hold Use on an anchor to charge it and move to your totem slot", Category.COMBAT, -1);
      F = this;
   }

   public static boolean C() {
      if (F != null && F.isEnabled()) {
         AnchorMacroModule var0 = F;
         return var0 != null && var0.isRuntimeActive() && !var0.a() ? var0.T || mc.options.keyUse.isDown() && var0.J() != null : false;
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      return this.u;
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.k);
   }

   @Override
   public void onTick() {
      vi.J(this);
      if (_p.g()) {
         int var10000 = mc.isWindowActive();
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 != 0) {
            int var14 = this.a();
            boolean var12 = false;
            byte var11 = (byte)var14;
            var14 = Integer.signum(var11);
            if (((var14 | -var14) >>> 31 ^ 1) == 0) {
               this.d("AnchorTap handles anchors while it is enabled");
               return;
            }

            if (!mc.options.keyUse.isDown()) {
               this.d("Hold Use on an anchor");
               return;
            }

            boolean var8 = false;
            byte var7 = this.T;
            var14 = Integer.signum(var7);
            if (((var14 | -var14) >>> 31 ^ 1) == 0) {
               boolean var10 = false;
               byte var9 = this.M;
               var14 = Integer.signum(var9);
               if ((var14 | -var14) >>> 31 == 0 && !this.l.getValue()) {
                  return;
               }
            }

            BlockHitResult var1 = this.J();
            if (var1 == null) {
               return;
            }

            this.T = this.M = (boolean)1;
            BlockPos var2 = var1.getBlockPos().immutable();
            long var3 = System.currentTimeMillis();
            if (this.G(var2) == 0) {
               this.D(var2, var3);
            } else {
               this.p(var2, var3);
            }

            return;
         }
      }

      this.d("Anchor interaction suspended");
   }

   private void D(BlockPos var1, long var2) {
      if (var2 - this.R < Math.round(this.w.getValue())) {
         this.u = "Waiting for Charge Delay";
      } else {
         int var4 = this.H();
         if (var4 == -1) {
            this.u = "No glowstone in hotbar";
         } else if (!this.V(var4, var2)) {
            this.u = "Waiting for Delay";
         } else {
            this.j(var1, Items.GLOWSTONE, "Charging anchor", () -> this.G(var1) == 0, var2x -> {
               this.R = System.currentTimeMillis();
               _q.z("AnchorMacro", "Charge at {}: {}", var1.toShortString(), var2x ? "sent" : "rejected");
            });
         }
      }
   }

   private void p(BlockPos var1, long var2) {
      if (!this.V(this.d.getValue().intValue() - 1, var2)) {
         this.u = "Waiting for Delay";
      } else if (!this.C.getValue()) {
         this.M = (boolean)0;
         this.u = "Holding totem slot";
      } else {
         ItemStack var4 = mc.player.getMainHandItem();
         if (!var4.is(Items.GLOWSTONE) && !mc.player.getOffhandItem().is(Items.GLOWSTONE)) {
            this.j(var1, var4.isEmpty() ? Items.AIR : var4.getItem(), "Detonating anchor", () -> this.G(var1) > 0, var2x -> {
               if (var2x) {
                  this.M = (boolean)0;
               }

               _q.z("AnchorMacro", "Detonation at {}: {}", var1.toShortString(), var2x ? "sent" : "rejected");
            });
         } else {
            this.u = "Glowstone in hand blocks detonation";
         }
      }
   }

   private void j(BlockPos var1, Item var2, String var3, BooleanSupplier var4, Consumer<Boolean> var5) {
      boolean var6 = vi.d(this, var1, var2, mc.player.blockInteractionRange(), false, false, var4, var5);
      this.u = var6 ? var3 : "Aim at the anchor within reach";
   }

   private BlockHitResult J() {
      if (_p.g()
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

   private int G(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1);
      return var2.is(Blocks.RESPAWN_ANCHOR) ? (Integer)var2.getValue(RespawnAnchorBlock.CHARGE) : 0;
   }

   private int H() {
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

   private boolean V(int var1, long var2) {
      Inventory var4 = mc.player.getInventory();
      long var5 = Math.round(this.s.getValue());
      if (var4.getSelectedSlot() != var1) {
         var4.setSelectedSlot(var1);
         this.Z = var1;
         this.N = var2;
         return var5 == 0L;
      } else {
         return var1 != this.Z || var2 - this.N >= var5;
      }
   }

   private boolean a() {
      AnchorTapModule var1 = LarpClient.N().j().y(AnchorTapModule.class);
      return var1 != null && var1.isRuntimeActive();
   }

   private void d(String var1) {
      this.T = this.M = (boolean)0;
      this.Z = -1;
      this.N = 0L;
      this.u = var1;
   }

   @Override
   public void onEnable() {
      this.R = 0L;
      this.d("Hold Use on an anchor");
      _q.z(
         "AnchorMacro",
         "Enabled; totemSlot={}, autoExplode={}, spam={}, delay={}ms, chargeDelay={}ms",
         this.d.getValue().intValue(),
         this.C.getValue(),
         this.l.getValue(),
         this.s.getValue().intValue(),
         this.w.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      vi.J(this);
      this.d("Hold Use on an anchor");
      _q.c("AnchorMacro", "Anchor intents cleared");
   }
}
