package com.larpclient.module;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public abstract class lb<T> {
   private final String q;
   private final String P;
   private T N;
   private final T f;
   private Consumer<T> G;
   private BooleanSupplier D = () -> true;
   private String d;

   public lb<T> O(BooleanSupplier var1) {
      this.D = var1;
      return this;
   }

   public boolean B() {
      return this.D.getAsBoolean();
   }

   public lb(String var1, String var2, T var3) {
      this.q = var1;
      this.P = var2;
      this.N = (T)var3;
      this.f = (T)var3;
   }

   public String R() {
      return this.q;
   }

   public String getDisplayName() {
      return this.d != null ? this.d : this.q;
   }

   public lb<T> M(String var1) {
      this.d = var1;
      return this;
   }

   public String F() {
      return this.P;
   }

   public T getValue() {
      return this.N;
   }

   public void setValue(T var1) {
      this.N = (T)var1;
      if (this.G != null) {
         this.G.accept((T)var1);
      }
   }

   public T O() {
      return this.f;
   }

   public void T() {
      this.setValue(this.f);
   }

   public lb<T> a(Consumer<T> var1) {
      this.G = var1;
      return this;
   }

   public abstract String getValueAsString();
}
