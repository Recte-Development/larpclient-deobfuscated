package com.larpclient.module;

public final class NameChangerModule extends Module {
   private static NameChangerModule Q;
   private final e G = this.addSetting(new e("Name", "Shown instead of your username on your screen. Any text works", ""));

   public NameChangerModule() {
      super("Name Changer", "Show any name in place of your username. Only you see it", Category.PLAYER, -1);
      Q = this;
      this.G.L("Type any name");
   }

   public static NameChangerModule B() {
      return Q;
   }

   public String v() {
      return this.isOperational() ? this.G.getValue().strip() : "";
   }

   @Override
   public String getStatus() {
      String var1 = this.G.getValue().strip();
      return var1.isEmpty() ? "Type a name to use" : "Showing as " + var1;
   }
}
