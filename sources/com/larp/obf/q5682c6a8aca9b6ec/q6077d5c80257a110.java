package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient._v;
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
public abstract class q6077d5c80257a110 {
   @Shadow
   @Final
   private Connection connection;

   @Redirect(
      method = "authenticateServer",
      at = @At(value = "INVOKE", target = "Lcom/mojang/authlib/minecraft/SessionService;joinServer(Ljava/util/UUID;Ljava/lang/String;Ljava/lang/String;)V")
   )
   private void H(SessionService var1, UUID var2, String var3, String var4) throws AuthenticationException {
      _v.o(var1, ((qa128cd4260ced7f0)this.connection).larpProxyChannel(), var2, var3, var4);
   }
}
