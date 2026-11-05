package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.xo;
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
public abstract class qe5e5955442e7fc00 {
   @Shadow
   @Final
   private Connection connection;

   @Redirect(
      method = "authenticateServer",
      at = @At(value = "INVOKE", target = "Lcom/mojang/authlib/minecraft/SessionService;joinServer(Ljava/util/UUID;Ljava/lang/String;Ljava/lang/String;)V")
   )
   private void W(SessionService var1, UUID var2, String var3, String var4) throws AuthenticationException {
      xo.Z(var1, ((q4ee602059669a560)this.connection).larpProxyChannel(), var2, var3, var4);
   }
}
