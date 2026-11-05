package com.larpclient.module;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public abstract class l6<T> {
   private final String b;
   private final String R;
   private T L;
   private final T x;
   private Consumer<T> e;
   private BooleanSupplier I = () -> true;
   private String V;

   public l6<T> x(BooleanSupplier var1) {
      this.I = var1;
      return this;
   }

   public boolean R() {
      return this.I.getAsBoolean();
   }

   public l6(String var1, String var2, T var3) {
      this.b = var1;
      this.R = var2;
      this.L = (T)var3;
      this.x = (T)var3;
   }

   public String k() {
      return this.b;
   }

   public String getDisplayName() {
      return this.V != null ? this.V : this.b;
   }

   public l6<T> b(String var1) {
      this.V = var1;
      return this;
   }

   public String L() {
      return this.R;
   }

   public T getValue() {
      return this.L;
   }

   public void setValue(T var1) {
      this.L = (T)var1;
      if (this.e != null) {
         this.e.accept((T)var1);
      }
   }

   public T C() {
      return this.x;
   }

   public void x() {
      this.setValue(this.x);
   }

   public l6<T> W(Consumer<T> var1) {
      this.e = var1;
      return this;
   }

   public abstract String getValueAsString();
}
