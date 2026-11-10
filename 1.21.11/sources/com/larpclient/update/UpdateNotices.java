package com.larpclient.update;

final class UpdateNotices {
   private UpdateNotices() {
   }

   static String key(UpdateService.Snapshot var0, String var1, String var2) {
      if (var0 == null) {
         return "";
      }

      if (var0.state() != UpdateService.State.QUEUED && var0.state() != UpdateService.State.READY && var0.state() != UpdateService.State.DOWNLOADING) {
         if (var0.installedNotice().isEmpty() || var0.installedNotice().equals(var1) && !var0.installedNotice().equals(var2)) {
            return var0.state() == UpdateService.State.AVAILABLE && var0.release() != null ? "release:" + var0.release().releaseId() : "";
         } else {
            return "installed:" + var0.installedNotice();
         }
      } else {
         return "pending:" + (var0.release() == null ? "" : var0.release().releaseId());
      }
   }

   static boolean visible(String var0, String var1, String var2) {
      return !var0.isEmpty() && !var0.equals(var1) && (!var0.startsWith("release:") || !var0.substring(8).equals(var2));
   }
}
