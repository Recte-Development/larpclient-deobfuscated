package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.dr;
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
public abstract class q224b532a445adc28 {
   @Shadow
   private int chatScrollbarPos;
   @Shadow
   private boolean newMessageSinceScroll;
   @Shadow
   @Final
   private List<Line> trimmedMessages;
   @Unique
   private long V = -1L;

   @Shadow
   private void refreshTrimmedMessages() {
      throw new AssertionError();
   }

   @Shadow
   public abstract int getLinesPerPage();

   @Inject(method = "tick", at = @At("HEAD"))
   private void m(CallbackInfo var1) {
      if (this.V != dr.K()) {
         this.V = dr.K();
         int var2 = this.chatScrollbarPos;
         boolean var3 = this.newMessageSinceScroll;
         this.refreshTrimmedMessages();
         this.chatScrollbarPos = Math.max(0, Math.min(var2, this.trimmedMessages.size() - this.getLinesPerPage()));
         this.newMessageSinceScroll = var3;
      }
   }
}
