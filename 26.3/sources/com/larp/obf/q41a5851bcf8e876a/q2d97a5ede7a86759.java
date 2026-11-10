package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.ih;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.SessionService;
import java.util.UUID;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientHandshakePacketListenerImpl.class)
public abstract class q2d97a5ede7a86759 {
   @Shadow
   @Final
   private Connection connection;

   @Redirect(
      method = "authenticateServer",
      at = @At(value = "INVOKE", target = "Lcom/mojang/authlib/minecraft/SessionService;joinServer(Ljava/util/UUID;Ljava/lang/String;Ljava/lang/String;)V")
   )
   private void R(SessionService var1, UUID var2, String var3, String var4) throws AuthenticationException {
      ih.w(var1, ((q116cf2707f75cf3c)this.connection).larpProxyChannel(), var2, var3, var4);
   }
}
