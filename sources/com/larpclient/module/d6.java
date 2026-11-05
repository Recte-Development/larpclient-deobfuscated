package com.larpclient.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

public class d6 extends df<String> {
   private List<String> Y;

   public d6(String var1, String var2, String var3, String... var4) {
      super(var1, var2, var3);
      this.Y = List.of(var4);
   }

   public boolean acceptsValue(String var1) {
      return this.Y.contains(var1);
   }

   public void setValue(String var1) {
      if (!this.Y.contains(var1)) {
         throw new IllegalArgumentException("Unknown mode: " + var1);
      }

      super.setValue(var1);
   }

   public void Y(List<String> var1) {
      ArrayList var2 = new ArrayList(var1);
      if (!var2.contains(this.P())) {
         var2.add(this.P());
      }

      this.Y = List.copyOf(var2);
   }

   public List<String> N() {
      return this.Y;
   }

   public List<String> getVisibleModes() {
      return this.N();
   }

   public ItemStack getModeIcon(String var1) {
      return ItemStack.EMPTY;
   }

   public int drawModeIcon(GuiGraphicsExtractor var1, String var2, int var3, int var4) {
      return 0;
   }

   public boolean isSearchable() {
      return false;
   }

   public String getModeLabel(String var1) {
      return var1;
   }

   public List<String> W(String var1) {
      String var2 = var1.strip().toLowerCase(Locale.ROOT).replace('_', ' ');
      if (var2.isEmpty()) {
         return this.getVisibleModes();
      }

      String[] var3 = var2.split("\\s+");
      return this.getVisibleModes().stream().filter(var2x -> {
         String var3x = (this.getModeLabel(var2x) + " " + var2x).toLowerCase(Locale.ROOT).replace('_', ' ');
         return Arrays.stream(var3).allMatch(var3x::contains);
      }).sorted(Comparator.comparingInt(var2x -> this.getModeLabel(var2x).toLowerCase(Locale.ROOT).replace((char)95, (char)32).equals(var2) ? 0 : 1)).toList();
   }

   public void f() {
      List var1 = this.getVisibleModes();
      int var2 = var1.indexOf(this.P());
      int var3 = (var2 + 1) % var1.size();
      this.setValue((String)var1.get(var3));
   }

   public void r() {
      List var1 = this.getVisibleModes();
      int var2 = var1.indexOf(this.P());
      int var3 = (var2 - 1 + var1.size()) % var1.size();
      this.setValue((String)var1.get(var3));
   }

   public boolean S(String var1) {
      return this.P().equalsIgnoreCase(var1);
   }

   @Override
   public String getValueAsString() {
      return this.P();
   }
}
