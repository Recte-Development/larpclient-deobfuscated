package com.larp.obf.qa300483f54d804a3;

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
public abstract class qb00cbefceef14729 {
   @Inject(method = "startConnecting", at = @At("HEAD"))
   private static void L(Screen var0, Minecraft var1, ServerAddress var2, ServerData var3, boolean var4, TransferState var5, CallbackInfo var6) {
      AutoReconnectModule.B(var2, var3);
   }
}
