package com.larpclient.module;

public final class NameChangerModule extends Module {
   private static NameChangerModule n;
   private final ds f = this.addSetting(new ds("Name", "Shown instead of your username on your screen. Any text works", ""));

   public NameChangerModule() {
      super("Name Changer", "Show any name in place of your username. Only you see it", Category.PLAYER, -1);
      n = this;
      this.f.C("Type any name");
   }

   public static NameChangerModule M() {
      return n;
   }

   public String t() {
      return this.isOperational() ? this.f.P().strip() : "";
   }

   @Override
   public String getStatus() {
      String var1 = this.f.P().strip();
      return var1.isEmpty() ? "Type a name to use" : "Showing as " + var1;
   }
}
