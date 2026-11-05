package com.larpclient.module;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public abstract class df<T> {
   private final String T;
   private final String I;
   private T k;
   private final T N;
   private Consumer<T> f;
   private BooleanSupplier a = () -> true;
   private String J;

   public df<T> H(BooleanSupplier var1) {
      this.a = var1;
      return this;
   }

   public boolean B() {
      return this.a.getAsBoolean();
   }

   public df(String var1, String var2, T var3) {
      this.T = var1;
      this.I = var2;
      this.k = (T)var3;
      this.N = (T)var3;
   }

   public String D() {
      return this.T;
   }

   public String getDisplayName() {
      return this.J != null ? this.J : this.T;
   }

   public df<T> n(String var1) {
      this.J = var1;
      return this;
   }

   public String A() {
      return this.I;
   }

   public T P() {
      return this.k;
   }

   public void setValue(T var1) {
      this.k = (T)var1;
      if (this.f != null) {
         this.f.accept((T)var1);
      }
   }

   public T a() {
      return this.N;
   }

   public void B() {
      this.setValue(this.N);
   }

   public df<T> h(Consumer<T> var1) {
      this.f = var1;
      return this;
   }

   public abstract String getValueAsString();
}
