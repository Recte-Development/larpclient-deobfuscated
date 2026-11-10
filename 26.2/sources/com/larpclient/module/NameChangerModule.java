package com.larpclient.module;

public final class NameChangerModule extends Module {
   private static NameChangerModule F;
   private final g i = this.addSetting(new g("Name", "Shown instead of your username on your screen. Any text works", ""));

   public NameChangerModule() {
      super("Name Changer", "Show any name in place of your username. Only you see it", Category.PLAYER, -1);
      F = this;
      this.i.F("Type any name");
   }

   public static NameChangerModule x() {
      return F;
   }

   public String c() {
      return this.isOperational() ? this.i.getValue().strip() : "";
   }

   @Override
   public String getStatus() {
      String var1 = this.i.getValue().strip();
      return var1.isEmpty() ? "Type a name to use" : "Showing as " + var1;
   }
}
