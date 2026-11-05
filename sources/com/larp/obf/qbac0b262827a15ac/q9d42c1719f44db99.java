package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.c4;
import com.larpclient.mo;
import com.larpclient.mq;
import com.larpclient.mt;
import com.larpclient.uw;
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
public abstract class q9d42c1719f44db99 extends Screen {
   @Shadow
   protected EditBox input;
   @Shadow
   private CommandSuggestions commandSuggestions;
   @Shadow
   private DisplayMode displayMode;
   @Unique
   private final c4 i = new c4();
   @Unique
   private final uw o = new uw();
   @Unique
   private Object M;

   protected q9d42c1719f44db99(Component var1) {
      super(var1);
   }

   @Unique
   private boolean Z() {
      return this.getClass() == ChatScreen.class && this.minecraft.player != null && this.minecraft.level != null;
   }

   @Unique
   private boolean s(double var1, double var3) {
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
   private void J(CallbackInfo var1) {
      this.i.h(this.minecraft.isWindowActive(), this.M == null || this.M == this.minecraft.level);
      this.i.y();
      this.o.g();
   }

   @Inject(method = "init", at = @At("TAIL"))
   private void M(CallbackInfo var1) {
      this.M = this.minecraft.level;
      if (this.Z()) {
         mt.U("HUD editor", "Chat mouse editing ready");
      }
   }

   @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
   private void n(MouseButtonEvent var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      this.i.h(this.minecraft.isWindowActive(), this.M == this.minecraft.level);
      if (!this.Z() || !this.i.s() && !this.o.y()) {
         if (this.Z() && this.s(var1.x(), var1.y())) {
            this.i.C();
            this.o.O();
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "mouseClicked", at = @At("RETURN"), cancellable = true)
   private void D(MouseButtonEvent var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      if (!(Boolean)var3.getReturnValue() && this.Z() && this.minecraft.isWindowActive()) {
         List var4 = mq.q();
         boolean var5 = this.s(var1.x(), var1.y());
         if (!var5 && !this.i.a(var1.x(), var1.y(), this.width, this.height, var4) && this.o.A(var1.x(), var1.y(), mo.Z(var1))) {
            this.i.Q(null);
            var3.setReturnValue(true);
         } else {
            this.o.O();
            if (this.i.n(var1.x(), var1.y(), mo.Z(var1), this.width, this.height, var4, var5)) {
               var3.setReturnValue(true);
            }
         }
      }
   }

   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      this.i.h(this.minecraft.isWindowActive(), this.M == this.minecraft.level);
      if (this.Z() && this.o.a(var1.x(), var1.y(), mo.Z(var1))) {
         return true;
      } else {
         return this.Z() && this.i.Q(var1.x(), var1.y(), mo.Z(var1), this.width, this.height, this.minecraft.hasAltDown())
            ? true
            : super.mouseDragged(var1, var2, var4);
      }
   }

   public boolean mouseReleased(MouseButtonEvent var1) {
      this.i.h(this.minecraft.isWindowActive(), this.M == this.minecraft.level);
      if (this.Z() && this.o.p(mo.Z(var1))) {
         return true;
      } else {
         return this.Z() && this.i.a(mo.Z(var1)) ? true : super.mouseReleased(var1);
      }
   }

   @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
   private void K(double var1, double var3, double var5, double var7, CallbackInfoReturnable<Boolean> var9) {
      if (this.Z() && this.minecraft.isWindowActive()) {
         boolean var10 = this.s(var1, var3);
         if (this.i.H(var1, var3, var7, var10) || !var10 && !this.i.a(var1, var3, this.width, this.height, mq.q()) && this.o.L(var1, var3, var7)) {
            var9.setReturnValue(true);
         }
      }
   }

   @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
   private void K(KeyEvent var1, CallbackInfoReturnable<Boolean> var2) {
      if (this.Z() && this.minecraft.isWindowActive() && this.i.h(mo.O(var1))) {
         var2.setReturnValue(true);
      }
   }

   public boolean charTyped(CharacterEvent var1) {
      return this.Z() && this.minecraft.isWindowActive() && this.i.W(var1.codepoint()) ? true : super.charTyped(var1);
   }

   public void tick() {
      super.tick();
      this.i.h(this.minecraft.isWindowActive(), this.M == this.minecraft.level);
      this.o.b(this.minecraft.isWindowActive());
   }

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void E(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      this.i.h(this.minecraft.isWindowActive(), this.M == this.minecraft.level);
      if (this.Z() && this.minecraft.isWindowActive()) {
         var1.nextStratum();
         boolean var6 = this.s(var2, var3);
         this.o.A(var1, var2, var3, var6 || this.i.a(var2, var3, this.width, this.height, mq.q()));
         this.i.Z(var1, var2, var3, this.width, this.height, var6);
         var1.nextStratum();
      }
   }

   @Inject(method = "removed", at = @At("HEAD"))
   private void v(CallbackInfo var1) {
      this.i.h(true, true);
      this.i.y();
      this.o.g();
   }
}
