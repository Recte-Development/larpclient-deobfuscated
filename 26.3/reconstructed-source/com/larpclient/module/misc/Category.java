package com.larpclient.module.misc;

public enum Category {
   COMBAT("Combat"),
   MOVEMENT("Movement"),
   RENDER("Render"),
   VISUALS("Visuals"),
   PLAYER("Player");

   public final String displayName;

   Category(String var3) {
      this.displayName = var3;
   }
}
