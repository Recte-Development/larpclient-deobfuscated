package com.larpclient;

public final class u6 extends RuntimeException {
   private final int I;
   private final String H;

   public u6(int var1, String var2) {
      super(t(var1, var2));
      this.I = var1;
      this.H = var2;
   }

   public int M() {
      return this.I;
   }

   public String X() {
      return this.H;
   }

   private static String t(int var0, String var1) {
      if (var1.equals("TIMEOUT")) {
         return "Larp took too long to respond. Try again shortly.";
      }

      if (var1.equals("NETWORK")) {
         return "Could not reach Larp. Check your connection and try again.";
      }

      if (var1.equals("INVALID_RESPONSE")) {
         return "Larp returned an invalid response. No changes were applied.";
      }

      return switch (var0) {
         case 400 -> "This data is not supported by the platform. Its module manifest or profile schema may need updating.";
         case 401 -> "Your Larp session is no longer valid. Re-authenticate from the Larp login screen.";
         case 403 -> "You do not have access to this feature.";
         case 404 -> "No item was found. The share code may be invalid or revoked.";
         case 409 -> var1.equals("REVISION_CONFLICT")
            ? "This cloud item changed. It has been refreshed; review it before trying again."
            : (
               var1.equals("DISCORD_LIMIT")
                  ? "You already have the most configs posted in Discord. Turn one off first, or use Share to just copy the code."
                  : "The operation conflicts with a server limit or the item's current state."
            );
         case 413 -> "The config or report is too large.";
         case 429 -> "Too many requests. Try again shortly.";
         case 503 -> "Larp authentication is temporarily unavailable.";
         case 507 -> "Larp's storage is full. Try again later.";
         default -> "Larp's service could not complete the request.";
      };
   }

   public static u6 V() {
      return new u6(0, "INVALID_RESPONSE");
   }
}
