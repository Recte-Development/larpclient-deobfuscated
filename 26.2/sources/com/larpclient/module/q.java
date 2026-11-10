package com.larpclient.module;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public abstract class q<T> {
   private final String J;
   private final String m;
   private T D;
   private final T w;
   private Consumer<T> v;
   private BooleanSupplier P = () -> true;
   private String t;

   public q<T> P(BooleanSupplier var1) {
      this.P = var1;
      return this;
   }

   public boolean W() {
      return this.P.getAsBoolean();
   }

   public q(String var1, String var2, T var3) {
      this.J = var1;
      this.m = var2;
      this.D = (T)var3;
      this.w = (T)var3;
   }

   public String e() {
      return this.J;
   }

   public String getDisplayName() {
      return this.t != null ? this.t : this.J;
   }

   public q<T> h(String var1) {
      this.t = var1;
      return this;
   }

   public String I() {
      return this.m;
   }

   public T getValue() {
      return this.D;
   }

   public void setValue(T var1) {
      this.D = (T)var1;
      if (this.v != null) {
         this.v.accept((T)var1);
      }
   }

   public T A() {
      return this.w;
   }

   public void u() {
      this.setValue(this.w);
   }

   public q<T> o(Consumer<T> var1) {
      this.v = var1;
      return this;
   }

   public abstract String getValueAsString();
}
