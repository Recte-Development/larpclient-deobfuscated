package com.larp.obf.qa300483f54d804a3;

import com.larpclient.gc;
import com.larpclient.h;
import com.larpclient.k2;
import com.larpclient.m4;
import com.larpclient.s4;
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
public abstract class q191bfbe6593ca0a1 extends Screen {
   @Shadow
   protected EditBox input;
   @Shadow
   private CommandSuggestions commandSuggestions;
   @Shadow
   private DisplayMode displayMode;
   @Unique
   private final s4 w = new s4();
   @Unique
   private final gc p = new gc();
   @Unique
   private Object z;

   protected q191bfbe6593ca0a1(Component var1) {
      super(var1);
   }

   @Unique
   private boolean M() {
      return this.getClass() == ChatScreen.class && this.minecraft.player != null && this.minecraft.level != null;
   }

   @Unique
   private boolean g(double var1, double var3) {
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
   private void i(CallbackInfo var1) {
      this.w.q(this.minecraft.isWindowActive(), this.z == null || this.z == this.minecraft.level);
      this.w.N();
      this.p.D();
   }

   @Inject(method = "init", at = @At("TAIL"))
   private void m(CallbackInfo var1) {
      this.z = this.minecraft.level;
      if (this.M()) {
         h.U("HUD editor", "Chat mouse editing ready");
      }
   }

   @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
   private void d(MouseButtonEvent var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      this.w.q(this.minecraft.isWindowActive(), this.z == this.minecraft.level);
      if (!this.M() || !this.w.H() && !this.p.i()) {
         if (this.M() && this.g(var1.x(), var1.y())) {
            this.w.m();
            this.p.w();
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "mouseClicked", at = @At("RETURN"), cancellable = true)
   private void u(MouseButtonEvent var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      if (!(Boolean)var3.getReturnValue() && this.M() && this.minecraft.isWindowActive()) {
         List var4 = m4.I();
         boolean var5 = this.g(var1.x(), var1.y());
         if (!var5 && !this.w.b(var1.x(), var1.y(), this.width, this.height, var4) && this.p.s(var1.x(), var1.y(), k2.w(var1))) {
            this.w.H(null);
            var3.setReturnValue(true);
         } else {
            this.p.w();
            if (this.w.R(var1.x(), var1.y(), k2.w(var1), this.width, this.height, var4, var5)) {
               var3.setReturnValue(true);
            }
         }
      }
   }

   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      this.w.q(this.minecraft.isWindowActive(), this.z == this.minecraft.level);
      if (this.M() && this.p.B(var1.x(), var1.y(), k2.w(var1))) {
         return true;
      } else {
         return this.M() && this.w.J(var1.x(), var1.y(), k2.w(var1), this.width, this.height, this.minecraft.hasAltDown())
            ? true
            : super.mouseDragged(var1, var2, var4);
      }
   }

   public boolean mouseReleased(MouseButtonEvent var1) {
      this.w.q(this.minecraft.isWindowActive(), this.z == this.minecraft.level);
      if (this.M() && this.p.I(k2.w(var1))) {
         return true;
      } else {
         return this.M() && this.w.O(k2.w(var1)) ? true : super.mouseReleased(var1);
      }
   }

   @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
   private void T(double var1, double var3, double var5, double var7, CallbackInfoReturnable<Boolean> var9) {
      if (this.M() && this.minecraft.isWindowActive()) {
         boolean var10 = this.g(var1, var3);
         if (this.w.C(var1, var3, var7, var10) || !var10 && !this.w.b(var1, var3, this.width, this.height, m4.I()) && this.p.d(var1, var3, var7)) {
            var9.setReturnValue(true);
         }
      }
   }

   @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
   private void K(KeyEvent var1, CallbackInfoReturnable<Boolean> var2) {
      if (this.M() && this.minecraft.isWindowActive() && this.w.T(k2.a(var1))) {
         var2.setReturnValue(true);
      }
   }

   public boolean charTyped(CharacterEvent var1) {
      return this.M() && this.minecraft.isWindowActive() && this.w.b(var1.codepoint()) ? true : super.charTyped(var1);
   }

   public void tick() {
      super.tick();
      this.w.q(this.minecraft.isWindowActive(), this.z == this.minecraft.level);
      this.p.Q(this.minecraft.isWindowActive());
   }

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void t(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      this.w.q(this.minecraft.isWindowActive(), this.z == this.minecraft.level);
      if (this.M() && this.minecraft.isWindowActive()) {
         var1.nextStratum();
         boolean var6 = this.g(var2, var3);
         this.p.p(var1, var2, var3, var6 || this.w.b(var2, var3, this.width, this.height, m4.I()));
         this.w.I(var1, var2, var3, this.width, this.height, var6);
         var1.nextStratum();
      }
   }

   @Inject(method = "removed", at = @At("HEAD"))
   private void l(CallbackInfo var1) {
      this.w.q(true, true);
      this.w.N();
      this.p.D();
   }
}
