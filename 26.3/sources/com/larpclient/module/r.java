package com.larpclient.module;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public abstract class r<T> {
   private final String x;
   private final String r;
   private T k;
   private final T u;
   private Consumer<T> S;
   private BooleanSupplier v = () -> true;
   private String g;

   public r<T> a(BooleanSupplier var1) {
      this.v = var1;
      return this;
   }

   public boolean j() {
      return this.v.getAsBoolean();
   }

   public r(String var1, String var2, T var3) {
      this.x = var1;
      this.r = var2;
      this.k = (T)var3;
      this.u = (T)var3;
   }

   public String X() {
      return this.x;
   }

   public String getDisplayName() {
      return this.g != null ? this.g : this.x;
   }

   public r<T> B(String var1) {
      this.g = var1;
      return this;
   }

   public String I() {
      return this.r;
   }

   public T getValue() {
      return this.k;
   }

   public void setValue(T var1) {
      this.k = (T)var1;
      if (this.S != null) {
         this.S.accept((T)var1);
      }
   }

   public T f() {
      return this.u;
   }

   public void u() {
      this.setValue(this.u);
   }

   public r<T> w(Consumer<T> var1) {
      this.S = var1;
      return this;
   }

   public abstract String getValueAsString();
}
