package com.larp.obf.qa300483f54d804a3;

import com.larpclient.zt;
import java.util.List;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage.Line;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public abstract class q255b18de5ef3e5d6 {
   @Shadow
   private int chatScrollbarPos;
   @Shadow
   private boolean newMessageSinceScroll;
   @Shadow
   @Final
   private List<Line> trimmedMessages;
   @Unique
   private long m = -1L;

   @Shadow
   private void refreshTrimmedMessages() {
      throw new AssertionError();
   }

   @Shadow
   public abstract int getLinesPerPage();

   @Inject(method = "tick", at = @At("HEAD"))
   private void U(CallbackInfo var1) {
      if (this.m != zt.L()) {
         this.m = zt.L();
         int var2 = this.chatScrollbarPos;
         boolean var3 = this.newMessageSinceScroll;
         this.refreshTrimmedMessages();
         this.chatScrollbarPos = Math.max(0, Math.min(var2, this.trimmedMessages.size() - this.getLinesPerPage()));
         this.newMessageSinceScroll = var3;
      }
   }
}
