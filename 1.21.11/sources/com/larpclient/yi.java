package com.larpclient;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

final class yi implements ByteChannel {
   private final HANDLE B;
   private final IntByReference i = new IntByReference();
   private final IntByReference f = new IntByReference();
   private volatile boolean Y = (boolean)1;

   yi(String var1) throws IOException {
      this.B = Kernel32.INSTANCE.CreateFile(var1, -1073741824, 0, null, 3, 0, null);
      if (WinBase.INVALID_HANDLE_VALUE.equals(this.B)) {
         throw G("open");
      }

      if (!Kernel32.INSTANCE.SetNamedPipeHandleState(this.B, new IntByReference(1), null, null)) {
         IOException var2 = G("nonblocking mode");
         this.close();
         throw var2;
      }
   }

   @Override
   public synchronized int read(ByteBuffer var1) throws IOException {
      if (!this.Y) {
         return -1;
      }

      if (!var1.hasRemaining()) {
         return 0;
      }

      if (!Kernel32.INSTANCE.PeekNamedPipe(this.B, null, 0, null, this.i, null)) {
         throw G("peek");
      }

      int var2 = Math.min(var1.remaining(), this.i.getValue());
      if (var2 == 0) {
         return 0;
      }

      byte[] var3 = new byte[var2];
      if (!Kernel32.INSTANCE.ReadFile(this.B, var3, var2, this.f, null)) {
         throw G("read");
      }

      var1.put(var3, 0, this.f.getValue());
      return this.f.getValue();
   }

   @Override
   public synchronized int write(ByteBuffer var1) throws IOException {
      if (!this.Y) {
         throw new IOException("Discord pipe closed");
      }

      byte[] var2 = new byte[var1.remaining()];
      var1.duplicate().get(var2);
      if (!Kernel32.INSTANCE.WriteFile(this.B, var2, var2.length, this.f, null)) {
         throw G("write");
      }

      var1.position(var1.position() + this.f.getValue());
      return this.f.getValue();
   }

   @Override
   public boolean isOpen() {
      return this.Y;
   }

   @Override
   public synchronized void close() throws IOException {
      if (this.Y) {
         this.Y = (boolean)0;
         if (!Kernel32.INSTANCE.CloseHandle(this.B)) {
            throw G("close");
         }
      }
   }

   private static IOException G(String var0) {
      return new IOException("Discord pipe " + var0 + " failed (Win32 " + Kernel32.INSTANCE.GetLastError() + ")");
   }
}
