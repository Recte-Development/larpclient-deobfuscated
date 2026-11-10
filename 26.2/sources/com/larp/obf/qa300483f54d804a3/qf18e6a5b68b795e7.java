package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p6;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import java.util.UUID;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientHandshakePacketListenerImpl.class)
public abstract class qf18e6a5b68b795e7 {
   @Shadow
   @Final
   private Connection connection;

   @Redirect(
      method = "authenticateServer",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/authlib/minecraft/MinecraftSessionService;joinServer(Ljava/util/UUID;Ljava/lang/String;Ljava/lang/String;)V"
      )
   )
   private void d(MinecraftSessionService var1, UUID var2, String var3, String var4) throws AuthenticationException {
      p6.I(var1, ((q95783080261f9ad6)this.connection).larpProxyChannel(), var2, var3, var4);
   }
}
