package com.larpclient.module.misc;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public abstract class Setting<T> {
   private final String name;
   private final String description;
   private T value;
   private final T defaultValue;
   private Consumer<T> changeListener;
   private BooleanSupplier visibility = () -> true;
   private String displayName;

   public Setting<T> visibleWhen(BooleanSupplier var1) {
      this.visibility = var1;
      return this;
   }

   public boolean isVisible() {
      return this.visibility.getAsBoolean();
   }

   public Setting(String var1, String var2, T var3) {
      this.name = var1;
      this.description = var2;
      this.value = (T)var3;
      this.defaultValue = (T)var3;
   }

   public String getName() {
      return this.name;
   }

   public String getDisplayName() {
      return this.displayName != null ? this.displayName : this.name;
   }

   public Setting<T> setDisplayName(String var1) {
      this.displayName = var1;
      return this;
   }

   public String getDescription() {
      return this.description;
   }

   public T getValue() {
      return this.value;
   }

   public void setValue(T var1) {
      this.value = (T)var1;
      if (this.changeListener != null) {
         this.changeListener.accept((T)var1);
      }
   }

   public T getDefaultValue() {
      return this.defaultValue;
   }

   public void reset() {
      this.setValue(this.defaultValue);
   }

   public Setting<T> onChange(Consumer<T> var1) {
      this.changeListener = var1;
      return this;
   }

   public abstract String getValueAsString();
}
