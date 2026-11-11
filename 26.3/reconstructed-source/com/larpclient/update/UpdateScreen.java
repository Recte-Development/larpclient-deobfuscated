package com.larpclient.update;

import com.larpclient.LarpClient;
import com.larpclient.config.ClientPreferences;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.util.misc.KeyCodes;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

public final class UpdateScreen extends Screen {
   private final Screen parent;
   private UpdateLayout.Dialog layout;
   private Button primary;
   private Button secondary;
   private Button skip;
   private Button automatic;
   private int scroll;
   private List<FormattedCharSequence> lines = List.of();
   private String renderedNotes = "";
   private final UpdateConsentKeys consentKeys = new UpdateConsentKeys();

   public UpdateScreen(Screen var1) {
      super(Component.literal("Client updates"));
      this.parent = var1;
   }

   protected void init() {
      this.consentKeys.begin(var1x -> KeyCodes.isKeyDown(this.minecraft.getWindow(), var1x));
      this.layout = UpdateLayout.dialog(this.width, this.height);
      int var1 = this.layout.x() + 12;
      int var2 = this.layout.width() - 24;
      int var3 = this.layout.actionsY();
      byte var4 = 6;
      int var5 = Math.max(1, (var2 - var4) / 2);
      this.primary = this.button("Check for updates", var1, var3, var5, 22, UpdateRuntime::primary);
      this.secondary = this.button("Official downloads", var1 + var5 + var4, var3, Math.max(1, var2 - var5 - var4), 22, UpdateRuntime::secondary);
      this.skip = this.button("Skip this release", var1, var3 + 24, var5, 22, () -> {
         if (UpdateRuntime.skip()) {
            this.onClose();
         }
      });
      this.button("Later", var1 + var5 + var4, var3 + 24, Math.max(1, var2 - var5 - var4), 22, this::onClose);
      this.automatic = this.button("Automatic checks", var1, var3 + 48, Math.max(1, var2), 20, () -> {
         ClientPreferences var0 = LarpClient.getInstance().getConfigManager().getPreferences();
         UpdateRuntime.automatic(!var0.updateAutoCheck);
      });
      this.sync();
   }

   private Button button(String var1, int var2, int var3, int var4, int var5, Runnable var6) {
      return (Button)this.addRenderableWidget(
         new Button(var2, var3, var4, var5, Component.literal(var1), var1x -> var6.run(), var0 -> (MutableComponent)var0.get()) {
            public void onClick(MouseButtonEvent var1, boolean var2x) {
               if (!var2x) {
                  super.onClick(var1, false);
               }
            }

            protected void extractContents(GuiGraphicsExtractor var1, int var2x, int var3x, float var4x) {
               boolean var5x = this.isHoveredOrFocused();
               int var6x = this.active ? (var5x ? -13224394 : -14474461) : -15132391;
               RoundedShapes.fillRounded(var1, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 3.0F, var6x);
               if (this.isFocused()) {
                  GlassPainter.drawFocusRing(var1, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 3);
               }

               FontRenderer.drawCenteredText(
                  var1,
                  FontRenderer.drawTextWithFace(this.getMessage().getString(), this.getWidth() - 12),
                  this.getX() + this.getWidth() / 2,
                  this.getY() + (this.getHeight() - 9) / 2,
                  this.active ? -1842205 : -8947849
               );
            }
         }
      );
   }

   private void sync() {
      UpdateService var1 = UpdateRuntime.service();
      UpdateService.Snapshot var2 = var1 == null ? null : var1.snapshot();
      this.primary.setMessage(Component.literal(UpdateRuntime.primaryLabel()));
      this.primary.active = UpdateRuntime.primaryEnabled();
      this.secondary.setMessage(Component.literal(UpdateRuntime.secondaryLabel()));
      this.secondary.active = UpdateRuntime.secondaryEnabled();
      this.skip.visible = var2 != null && var2.state() == UpdateService.State.AVAILABLE;
      this.skip.active = this.skip.visible;
      boolean var3 = LarpClient.getInstance().getConfigManager().getPreferences().updateAutoCheck;
      this.automatic.setMessage(Component.literal("Automatic checks: " + (var3 ? "On" : "Off")));
   }

   public void tick() {
      this.consentKeys.reconcile(var1 -> KeyCodes.isKeyDown(this.minecraft.getWindow(), var1));
      this.sync();
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.sync();
      int var5 = this.layout.x();
      int var6 = this.layout.y();
      int var7 = this.layout.width();
      RoundedShapes.fillRounded(var1, var5, var6, var7, this.layout.height(), 7.0F, -182904551);
      FontRenderer.drawTextRaw(var1, "Client updates", var5 + 14, var6 + 13, -1447447, FontRenderer.Face.SEMIBOLD);
      UpdateService var8 = UpdateRuntime.service();
      UpdateService.Snapshot var9 = var8 == null ? null : var8.snapshot();
      String var10 = var8 == null
         ? "Larp Client"
         : var8.identity().edition() + " / Minecraft " + var8.identity().minecraft() + " / " + var8.identity().channel();
      FontRenderer.drawDefaultText(var1, FontRenderer.drawTextWithFace(var10, var7 - 28), var5 + 14, var6 + 29, -5592406);
      String var11 = var8 == null ? "" : "Installed " + var8.identity().version() + (var9.release() == null ? "" : "  |  Release " + var9.release().version());
      FontRenderer.drawDefaultText(var1, FontRenderer.drawTextWithFace(var11, var7 - 28), var5 + 14, var6 + 45, -3487030);
      String var12 = this.notes(var8, var9);
      if (!var12.equals(this.renderedNotes)) {
         this.renderedNotes = var12;
         this.lines = this.minecraft.font.split(FontRenderer.toComponent(var12, FontRenderer.Face.REGULAR), Math.max(1, var7 - 36));
         this.scroll = 0;
      }

      int var13 = Math.max(0, this.lines.size() * 11 - this.layout.textHeight());
      this.scroll = Math.clamp(this.scroll, 0, var13);
      var1.enableScissor(var5 + 12, this.layout.textTop(), var5 + var7 - 12, this.layout.textBottom());

      try {
         for (int var14 = Math.max(0, this.scroll / 11);
            var14 < this.lines.size() && this.layout.textTop() + var14 * 11 - this.scroll < this.layout.textBottom();
            var14++
         ) {
            var1.text(this.minecraft.font, this.lines.get(var14), var5 + 16, this.layout.textTop() + var14 * 11 - this.scroll, -4473925, false);
         }
      } finally {
         var1.disableScissor();
      }

      if (var13 > 0) {
         int var19 = this.layout.textHeight();
         int var15 = Math.max(12, var19 * var19 / (this.lines.size() * 11));
         int var16 = this.layout.textTop() + (var19 - var15) * this.scroll / var13;
         var1.fill(var5 + var7 - 10, var16, var5 + var7 - 8, var16 + var15, -8947849);
      }

      if (var9 != null && var9.state() == UpdateService.State.DOWNLOADING && var9.release() != null) {
         int var20 = (int)((var7 - 28) * Math.min(1.0, (double)var9.downloaded() / var9.release().size()));
         var1.fill(var5 + 14, this.layout.actionsY() - 8, var5 + var7 - 14, this.layout.actionsY() - 5, -13092808);
         var1.fill(var5 + 14, this.layout.actionsY() - 8, var5 + 14 + var20, this.layout.actionsY() - 5, -3552823);
      }

      super.extractRenderState(var1, var2, var3, var4);
   }

   private String notes(UpdateService var1, UpdateService.Snapshot var2) {
      if (var1 == null) {
         return UpdateRuntime.unavailable();
      }

      StringBuilder var3 = new StringBuilder(var2.message());
      if (!var2.installedNotice().isEmpty()) {
         var3.append("\n\nSuccessfully loaded this installed build: ").append(var1.identity().version()).append('.');
      }

      if (var2.release() != null) {
         UpdateManifest.Release var4 = var2.release();
         var3.append("\n\n").append(var4.title()).append("\n").append(var4.summary());

         for (String var6 : var4.notes()) {
            var3.append("\n\n• ").append(var6);
         }

         var3.append("\n\n")
            .append(String.format(Locale.ROOT, "%.1f MiB", var4.size() / 1048576.0))
            .append(" / Java ")
            .append(var4.minimumJava())
            .append("+ / Fabric ")
            .append(var4.minimumLoader())
            .append("+");
      }

      if (!var2.installable()) {
         var3.append(
            "\n\nThis client can update itself on Windows and Linux when its JAR is directly in the instance's mods folder. Otherwise, use the official download page and replace the mod after Minecraft closes."
         );
      } else {
         var3.append("\n\nUpdate downloads and verifies this exact signed release. It installs when you close Minecraft, and the approval lasts 24 hours.");
      }

      if (var2.checkedAt() > 0L) {
         var3.append("\n\nLast successful check: ").append(Instant.ofEpochSecond(var2.checkedAt())).append('.');
      }

      if (var2.cached()) {
         var3.append("\n\nCached release details. An expired manifest cannot authorize installation.");
      }

      return var3.toString();
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (var1 >= this.layout.x() + 12 && var1 < this.layout.x() + this.layout.width() - 12 && var3 >= this.layout.textTop() && var3 < this.layout.textBottom()
         )
       {
         this.scroll = Math.clamp(this.scroll - (int)(var7 * 22.0), 0, Math.max(0, this.lines.size() * 11 - this.layout.textHeight()));
         return true;
      } else {
         return super.mouseScrolled(var1, var3, var5, var7);
      }
   }

   public boolean keyPressed(KeyEvent var1) {
      int var2 = KeyCodes.keyOf(var1);
      if (var2 == 256) {
         this.onClose();
         return true;
      } else if (var2 == 267 || var2 == 266) {
         this.scroll = Math.clamp(
            this.scroll + (var2 == 267 ? 1 : -1) * this.layout.textHeight(), 0, Math.max(0, this.lines.size() * 11 - this.layout.textHeight())
         );
         return true;
      } else {
         return !this.consentKeys.press(var2) ? true : super.keyPressed(var1);
      }
   }

   public boolean keyReleased(KeyEvent var1) {
      this.consentKeys.release(KeyCodes.keyOf(var1));
      return super.keyReleased(var1);
   }

   public void onClose() {
      UpdateRuntime.later();
      this.minecraft.gui.setScreen(this.parent);
   }

   public boolean isPauseScreen() {
      return false;
   }
}
