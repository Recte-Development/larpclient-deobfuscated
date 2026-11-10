package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.FakePayModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.numbers.NumberFormat;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class q4d07e596876e8336 {
   @ModifyExpressionValue(
      method = "lambda$displayScoreboardSidebar$1",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/scores/PlayerTeam;formatNameForTeam(Lnet/minecraft/world/scores/Team;Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;"
      )
   )
   private MutableComponent A(MutableComponent var1, Scoreboard var2, NumberFormat var3, PlayerScoreEntry var4) {
      PlayerTeam var5 = var2.getPlayersTeam(var4.owner());
      return FakePayModule.k(var1, null, var4.owner(), var5 == null ? "" : var5.getName());
   }

   @ModifyExpressionValue(
      method = "lambda$displayScoreboardSidebar$1",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/scores/PlayerScoreEntry;formatValue(Lnet/minecraft/network/chat/numbers/NumberFormat;)Lnet/minecraft/network/chat/MutableComponent;"
      )
   )
   private MutableComponent L(MutableComponent var1, Scoreboard var2, NumberFormat var3, PlayerScoreEntry var4) {
      PlayerTeam var5 = var2.getPlayersTeam(var4.owner());
      return FakePayModule.k(var1, PlayerTeam.formatNameForTeam(var5, var4.ownerName()), var4.owner(), var5 == null ? "" : var5.getName());
   }

   @Inject(method = "displayScoreboardSidebar", at = @At("RETURN"))
   private void S(GuiGraphicsExtractor var1, Objective var2, CallbackInfo var3) {
      FakePayModule.Z(var2);
   }
}
