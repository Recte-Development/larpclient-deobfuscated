package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.AutoReconnectModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.TransferState;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public abstract class q3df2f324c56cd0c4 {
   @Inject(method = "startConnecting", at = @At("HEAD"))
   private static void H(Screen var0, Minecraft var1, ServerAddress var2, ServerData var3, boolean var4, TransferState var5, CallbackInfo var6) {
      AutoReconnectModule.q(var2, var3);
   }
}
