package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.j7;
import com.larpclient.o8;
import com.larpclient.r4;
import com.larpclient.r7;
import com.larpclient.yq;
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
public abstract class q0bada938f5bdb117 extends Screen {
   @Shadow
   protected EditBox input;
   @Shadow
   private CommandSuggestions commandSuggestions;
   @Shadow
   private DisplayMode displayMode;
   @Unique
   private final j7 U = new j7();
   @Unique
   private final yq l = new yq();
   @Unique
   private Object h;

   protected q0bada938f5bdb117(Component var1) {
      super(var1);
   }

   @Unique
   private boolean V() {
      return this.getClass() == ChatScreen.class && this.minecraft.player != null && this.minecraft.level != null;
   }

   @Unique
   private boolean x(double var1, double var3) {
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
   private void K(CallbackInfo var1) {
      this.U.I(this.minecraft.isWindowActive(), this.h == null || this.h == this.minecraft.level);
      this.U.N();
      this.l.Q();
   }

   @Inject(method = "init", at = @At("TAIL"))
   private void U(CallbackInfo var1) {
      this.h = this.minecraft.level;
      if (this.V()) {
         r4.g("HUD editor", "Chat mouse editing ready");
      }
   }

   @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
   private void d(MouseButtonEvent var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      this.U.I(this.minecraft.isWindowActive(), this.h == this.minecraft.level);
      if (!this.V() || !this.U.R() && !this.l.Z()) {
         if (this.V() && this.x(var1.x(), var1.y())) {
            this.U.O();
            this.l.a();
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "mouseClicked", at = @At("RETURN"), cancellable = true)
   private void E(MouseButtonEvent var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      if (!(Boolean)var3.getReturnValue() && this.V() && this.minecraft.isWindowActive()) {
         List var4 = o8.d();
         boolean var5 = this.x(var1.x(), var1.y());
         if (!var5 && !this.U.Q(var1.x(), var1.y(), this.width, this.height, var4) && this.l.f(var1.x(), var1.y(), r7.y(var1))) {
            this.U.i(null);
            var3.setReturnValue(true);
         } else {
            this.l.a();
            if (this.U.l(var1.x(), var1.y(), r7.y(var1), this.width, this.height, var4, var5)) {
               var3.setReturnValue(true);
            }
         }
      }
   }

   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      this.U.I(this.minecraft.isWindowActive(), this.h == this.minecraft.level);
      if (this.V() && this.l.W(var1.x(), var1.y(), r7.y(var1))) {
         return true;
      } else {
         return this.V() && this.U.b(var1.x(), var1.y(), r7.y(var1), this.width, this.height, this.minecraft.hasAltDown())
            ? true
            : super.mouseDragged(var1, var2, var4);
      }
   }

   public boolean mouseReleased(MouseButtonEvent var1) {
      this.U.I(this.minecraft.isWindowActive(), this.h == this.minecraft.level);
      if (this.V() && this.l.q(r7.y(var1))) {
         return true;
      } else {
         return this.V() && this.U.R(r7.y(var1)) ? true : super.mouseReleased(var1);
      }
   }

   @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
   private void t(double var1, double var3, double var5, double var7, CallbackInfoReturnable<Boolean> var9) {
      if (this.V() && this.minecraft.isWindowActive()) {
         boolean var10 = this.x(var1, var3);
         if (this.U.T(var1, var3, var7, var10) || !var10 && !this.U.Q(var1, var3, this.width, this.height, o8.d()) && this.l.z(var1, var3, var7)) {
            var9.setReturnValue(true);
         }
      }
   }

   @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
   private void O(KeyEvent var1, CallbackInfoReturnable<Boolean> var2) {
      if (this.V() && this.minecraft.isWindowActive() && this.U.V(r7.g(var1))) {
         var2.setReturnValue(true);
      }
   }

   public boolean charTyped(CharacterEvent var1) {
      return this.V() && this.minecraft.isWindowActive() && this.U.K(var1.codepoint()) ? true : super.charTyped(var1);
   }

   public void tick() {
      super.tick();
      this.U.I(this.minecraft.isWindowActive(), this.h == this.minecraft.level);
      this.l.j(this.minecraft.isWindowActive());
   }

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void R(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      this.U.I(this.minecraft.isWindowActive(), this.h == this.minecraft.level);
      if (this.V() && this.minecraft.isWindowActive()) {
         var1.nextStratum();
         boolean var6 = this.x(var2, var3);
         this.l.a(var1, var2, var3, var6 || this.U.Q(var2, var3, this.width, this.height, o8.d()));
         this.U.V(var1, var2, var3, this.width, this.height, var6);
         var1.nextStratum();
      }
   }

   @Inject(method = "removed", at = @At("HEAD"))
   private void g(CallbackInfo var1) {
      this.U.I(true, true);
      this.U.N();
      this.l.Q();
   }
}
