package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient._7;
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
public abstract class qc413d1a8b2f8e952 {
   @Shadow
   private int chatScrollbarPos;
   @Shadow
   private boolean newMessageSinceScroll;
   @Shadow
   @Final
   private List<Line> trimmedMessages;
   @Unique
   private long M = -1L;

   @Shadow
   private void refreshTrimmedMessages() {
      throw new AssertionError();
   }

   @Shadow
   public abstract int getLinesPerPage();

   @Inject(method = "tick", at = @At("HEAD"))
   private void I(CallbackInfo var1) {
      if (this.M != _7.Y()) {
         this.M = _7.Y();
         int var2 = this.chatScrollbarPos;
         boolean var3 = this.newMessageSinceScroll;
         this.refreshTrimmedMessages();
         this.chatScrollbarPos = Math.max(0, Math.min(var2, this.trimmedMessages.size() - this.getLinesPerPage()));
         this.newMessageSinceScroll = var3;
      }
   }
}
