package com.larpclient.mixin;

import com.larpclient.hud.HudEditor;
import com.larpclient.hud.HudLayout;
import com.larpclient.social.misc.ViewModelChatEditor;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.LarpLog;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.ActiveTextCollector.ClickableStyleFinder;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ChatComponent.DisplayMode;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {
   @Shadow
   protected EditBox input;
   @Shadow
   private CommandSuggestions commandSuggestions;
   @Shadow
   private DisplayMode displayMode;
   @Unique
   private final HudEditor j = new HudEditor();
   @Unique
   private final ViewModelChatEditor Y = new ViewModelChatEditor();
   @Unique
   private Object O;

   protected ChatScreenMixin(Component var1) {
      super(var1);
   }

   @Unique
   private boolean Y() {
      return this.getClass() == ChatScreen.class && this.minecraft.player != null && this.minecraft.level != null;
   }

   @Unique
   private boolean l(double var1, double var3) {
      if (!(var3 >= this.height - 14) && !this.input.isMouseOver(var1, var3)) {
         if (this.commandSuggestions.suggestions != null && this.commandSuggestions.suggestions.rect.contains((int)var1, (int)var3)) {
            return true;
         }

         ClickableStyleFinder var5 = new ClickableStyleFinder(this.getFont(), (int)var1, (int)var3).includeInsertions(true);
         this.minecraft.gui.hud.getChat().captureClickableText(var5, this.height, this.minecraft.gui.hud.getGuiTicks(), this.displayMode);
         return var5.result() != null;
      } else {
         return true;
      }
   }

   @Inject(method = "init", at = @At("HEAD"))
   private void P(CallbackInfo var1) {
      this.j.updateChatState(this.minecraft.isWindowActive(), this.O == null || this.O == this.minecraft.level);
      this.j.close();
      this.Y.close();
   }

   @Inject(method = "init", at = @At("TAIL"))
   private void J(CallbackInfo var1) {
      this.O = this.minecraft.level;
      if (this.Y()) {
         LarpLog.debug("HUD editor", "Chat mouse editing ready");
      }
   }

   @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
   private void Z(MouseButtonEvent var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      this.j.updateChatState(this.minecraft.isWindowActive(), this.O == this.minecraft.level);
      if (!this.Y() || !this.j.isDragging() && !this.Y.isDragging()) {
         if (this.Y() && this.l(var1.x(), var1.y())) {
            this.j.closeContextMenu();
            this.Y.clearSelection();
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "mouseClicked", at = @At("RETURN"), cancellable = true)
   private void b(MouseButtonEvent var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      if (!(Boolean)var3.getReturnValue() && this.Y() && this.minecraft.isWindowActive()) {
         List var4 = HudLayout.collectVisibleBounds();
         boolean var5 = this.l(var1.x(), var1.y());
         if (!var5
            && !this.j.isOverEditorUi(var1.x(), var1.y(), this.width, this.height, var4)
            && this.Y.mouseClicked(var1.x(), var1.y(), KeyCodes.mouseButtonOf(var1))) {
            this.j.select(null);
            var3.setReturnValue(true);
         } else {
            this.Y.clearSelection();
            if (this.j.mousePressed(var1.x(), var1.y(), KeyCodes.mouseButtonOf(var1), this.width, this.height, var4, var5)) {
               var3.setReturnValue(true);
            }
         }
      }
   }

   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      this.j.updateChatState(this.minecraft.isWindowActive(), this.O == this.minecraft.level);
      if (this.Y() && this.Y.mouseDragged(var1.x(), var1.y(), KeyCodes.mouseButtonOf(var1))) {
         return true;
      } else {
         return this.Y() && this.j.commitChanges(var1.x(), var1.y(), KeyCodes.mouseButtonOf(var1), this.width, this.height, this.minecraft.hasAltDown())
            ? true
            : super.mouseDragged(var1, var2, var4);
      }
   }

   public boolean mouseReleased(MouseButtonEvent var1) {
      this.j.updateChatState(this.minecraft.isWindowActive(), this.O == this.minecraft.level);
      if (this.Y() && this.Y.mouseReleased(KeyCodes.mouseButtonOf(var1))) {
         return true;
      } else {
         return this.Y() && this.j.mouseReleased(KeyCodes.mouseButtonOf(var1)) ? true : super.mouseReleased(var1);
      }
   }

   @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
   private void d(double var1, double var3, double var5, double var7, CallbackInfoReturnable<Boolean> var9) {
      if (this.Y() && this.minecraft.isWindowActive()) {
         boolean var10 = this.l(var1, var3);
         if (this.j.mouseScrolled(var1, var3, var7, var10)
            || !var10
               && !this.j.isOverEditorUi(var1, var3, this.width, this.height, HudLayout.collectVisibleBounds())
               && this.Y.mouseScrolled(var1, var3, var7)) {
            var9.setReturnValue(true);
         }
      }
   }

   @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
   private void a(KeyEvent var1, CallbackInfoReturnable<Boolean> var2) {
      if (this.Y() && this.minecraft.isWindowActive() && this.j.mouseDragged(KeyCodes.keyOf(var1))) {
         var2.setReturnValue(true);
      }
   }

   public boolean charTyped(CharacterEvent var1) {
      return this.Y() && this.minecraft.isWindowActive() && this.j.keyReleased(var1.codepoint()) ? true : super.charTyped(var1);
   }

   public void tick() {
      super.tick();
      this.j.updateChatState(this.minecraft.isWindowActive(), this.O == this.minecraft.level);
      this.Y.setChatOpen(this.minecraft.isWindowActive());
   }

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void O(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      this.j.updateChatState(this.minecraft.isWindowActive(), this.O == this.minecraft.level);
      if (this.Y() && this.minecraft.isWindowActive()) {
         var1.nextStratum();
         boolean var6 = this.l(var2, var3);
         this.Y.render(var1, var2, var3, var6 || this.j.isOverEditorUi(var2, var3, this.width, this.height, HudLayout.collectVisibleBounds()));
         this.j.render(var1, var2, var3, this.width, this.height, var6);
         var1.nextStratum();
      }
   }

   @Inject(method = "removed", at = @At("HEAD"))
   private void C(CallbackInfo var1) {
      this.j.updateChatState(true, true);
      this.j.close();
      this.Y.close();
   }
}
