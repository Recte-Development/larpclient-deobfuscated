package com.larpclient.update;

import com.larp.updater.UpdateManifest;
import com.larpclient.LarpClient;
import com.larpclient.p;
import com.larpclient.s2;
import com.larpclient.uw;
import com.larpclient.ws;
import com.larpclient.y0;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_5250;
import net.minecraft.class_5481;

public final class UpdateScreen extends class_437 {
   private final class_437 parent;
   private UpdateLayout.Dialog layout;
   private class_4185 primary;
   private class_4185 secondary;
   private class_4185 skip;
   private class_4185 automatic;
   private int scroll;
   private List<class_5481> lines = List.of();
   private String renderedNotes = "";
   private final UpdateConsentKeys consentKeys = new UpdateConsentKeys();

   public UpdateScreen(class_437 var1) {
      super(class_2561.method_43470("Client updates"));
      this.parent = var1;
   }

   protected void method_25426() {
      this.consentKeys.begin(var1x -> s2.G(this.field_22787.method_22683(), var1x));
      this.layout = UpdateLayout.dialog(this.field_22789, this.field_22790);
      int var1 = this.layout.x() + 12;
      int var2 = this.layout.width() - 24;
      int var3 = this.layout.actionsY();
      byte var4 = 6;
      int var5 = Math.max(1, (var2 - var4) / 2);
      this.primary = this.button("Check for updates", var1, var3, var5, 22, UpdateRuntime::primary);
      this.secondary = this.button("Official downloads", var1 + var5 + var4, var3, Math.max(1, var2 - var5 - var4), 22, UpdateRuntime::secondary);
      this.skip = this.button("Skip this release", var1, var3 + 24, var5, 22, () -> {
         if (UpdateRuntime.skip()) {
            this.method_25419();
         }
      });
      this.button("Later", var1 + var5 + var4, var3 + 24, Math.max(1, var2 - var5 - var4), 22, this::method_25419);
      this.automatic = this.button("Automatic checks", var1, var3 + 48, Math.max(1, var2), 20, () -> {
         y0 var0 = LarpClient.b().e().F();
         UpdateRuntime.automatic(!var0.N);
      });
      this.sync();
   }

   private class_4185 button(String var1, int var2, int var3, int var4, int var5, Runnable var6) {
      return (class_4185)this.method_37063(
         new class_4185(var2, var3, var4, var5, class_2561.method_43470(var1), var1x -> var6.run(), var0 -> (class_5250)var0.get()) {
            public void method_25348(class_11909 var1, boolean var2x) {
               if (!var2x) {
                  super.method_25348(var1, false);
               }
            }

            protected void method_75752(class_332 var1, int var2x, int var3x, float var4x) {
               boolean var5x = this.method_25367();
               int var6x = this.field_22763 ? (var5x ? -13224394 : -14474461) : -15132391;
               ws.r(var1, this.method_46426(), this.method_46427(), this.method_25368(), this.method_25364(), 3.0F, var6x);
               if (this.method_25370()) {
                  p.C(var1, this.method_46426(), this.method_46427(), this.method_25368(), this.method_25364(), 3);
               }

               uw.N(
                  var1,
                  uw.R(this.method_25369().getString(), this.method_25368() - 12),
                  this.method_46426() + this.method_25368() / 2,
                  this.method_46427() + (this.method_25364() - 9) / 2,
                  this.field_22763 ? -1842205 : -8947849
               );
            }
         }
      );
   }

   private void sync() {
      UpdateService var1 = UpdateRuntime.service();
      UpdateService.Snapshot var2 = var1 == null ? null : var1.snapshot();
      this.primary.method_25355(class_2561.method_43470(UpdateRuntime.primaryLabel()));
      this.primary.field_22763 = UpdateRuntime.primaryEnabled();
      this.secondary.method_25355(class_2561.method_43470(UpdateRuntime.secondaryLabel()));
      this.secondary.field_22763 = UpdateRuntime.secondaryEnabled();
      this.skip.field_22764 = var2 != null && var2.state() == UpdateService.State.AVAILABLE;
      this.skip.field_22763 = this.skip.field_22764;
      boolean var3 = LarpClient.b().e().F().N;
      this.automatic.method_25355(class_2561.method_43470("Automatic checks: " + (var3 ? "On" : "Off")));
   }

   public void method_25393() {
      this.consentKeys.reconcile(var1 -> s2.G(this.field_22787.method_22683(), var1));
      this.sync();
   }

   public void method_25394(class_332 var1, int var2, int var3, float var4) {
      this.sync();
      int var5 = this.layout.x();
      int var6 = this.layout.y();
      int var7 = this.layout.width();
      ws.r(var1, var5, var6, var7, this.layout.height(), 7.0F, -182904551);
      uw.D(var1, "Client updates", var5 + 14, var6 + 13, -1447447, uw.r.M);
      UpdateService var8 = UpdateRuntime.service();
      UpdateService.Snapshot var9 = var8 == null ? null : var8.snapshot();
      String var10 = var8 == null
         ? "Larp Client"
         : var8.identity().edition() + " / Minecraft " + var8.identity().minecraft() + " / " + var8.identity().channel();
      uw.z(var1, uw.R(var10, var7 - 28), var5 + 14, var6 + 29, -5592406);
      String var11 = var8 == null ? "" : "Installed " + var8.identity().version() + (var9.release() == null ? "" : "  |  Release " + var9.release().version());
      uw.z(var1, uw.R(var11, var7 - 28), var5 + 14, var6 + 45, -3487030);
      String var12 = this.notes(var8, var9);
      if (!var12.equals(this.renderedNotes)) {
         this.renderedNotes = var12;
         this.lines = this.field_22787.field_1772.method_1728(uw.v(var12, uw.r.n), Math.max(1, var7 - 36));
         this.scroll = 0;
      }

      int var13 = Math.max(0, this.lines.size() * 11 - this.layout.textHeight());
      this.scroll = Math.clamp(this.scroll, 0, var13);
      var1.method_44379(var5 + 12, this.layout.textTop(), var5 + var7 - 12, this.layout.textBottom());

      try {
         for (int var14 = Math.max(0, this.scroll / 11);
            var14 < this.lines.size() && this.layout.textTop() + var14 * 11 - this.scroll < this.layout.textBottom();
            var14++
         ) {
            var1.method_51430(this.field_22787.field_1772, this.lines.get(var14), var5 + 16, this.layout.textTop() + var14 * 11 - this.scroll, -4473925, false);
         }
      } finally {
         var1.method_44380();
      }

      if (var13 > 0) {
         int var19 = this.layout.textHeight();
         int var15 = Math.max(12, var19 * var19 / (this.lines.size() * 11));
         int var16 = this.layout.textTop() + (var19 - var15) * this.scroll / var13;
         var1.method_25294(var5 + var7 - 10, var16, var5 + var7 - 8, var16 + var15, -8947849);
      }

      if (var9 != null && var9.state() == UpdateService.State.DOWNLOADING && var9.release() != null) {
         int var20 = (int)((var7 - 28) * Math.min(1.0, (double)var9.downloaded() / var9.release().size()));
         var1.method_25294(var5 + 14, this.layout.actionsY() - 8, var5 + var7 - 14, this.layout.actionsY() - 5, -13092808);
         var1.method_25294(var5 + 14, this.layout.actionsY() - 8, var5 + 14 + var20, this.layout.actionsY() - 5, -3552823);
      }

      super.method_25394(var1, var2, var3, var4);
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

   public boolean method_25401(double var1, double var3, double var5, double var7) {
      if (var1 >= this.layout.x() + 12 && var1 < this.layout.x() + this.layout.width() - 12 && var3 >= this.layout.textTop() && var3 < this.layout.textBottom()
         )
       {
         this.scroll = Math.clamp(this.scroll - (int)(var7 * 22.0), 0, Math.max(0, this.lines.size() * 11 - this.layout.textHeight()));
         return true;
      } else {
         return super.method_25401(var1, var3, var5, var7);
      }
   }

   public boolean method_25404(class_11908 var1) {
      int var2 = s2.i(var1);
      if (var2 == 256) {
         this.method_25419();
         return true;
      } else if (var2 == 267 || var2 == 266) {
         this.scroll = Math.clamp(
            this.scroll + (var2 == 267 ? 1 : -1) * this.layout.textHeight(), 0, Math.max(0, this.lines.size() * 11 - this.layout.textHeight())
         );
         return true;
      } else {
         return !this.consentKeys.press(var2) ? true : super.method_25404(var1);
      }
   }

   public boolean method_16803(class_11908 var1) {
      this.consentKeys.release(s2.i(var1));
      return super.method_16803(var1);
   }

   public void method_25419() {
      UpdateRuntime.later();
      this.field_22787.method_1507(this.parent);
   }

   public boolean method_25421() {
      return false;
   }
}
