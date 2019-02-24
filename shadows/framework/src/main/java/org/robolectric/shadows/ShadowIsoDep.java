package org.robolectric.shadows;

import android.annotation.SuppressLint;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import java.io.IOException;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

/** Extends IsoDep to allow for testing. */
@Implements(IsoDep.class)
public class ShadowIsoDep extends ShadowBasicTagTechnology {

  @SuppressLint("PrivateApi")
  @SuppressWarnings("unchecked")
  public static IsoDep newInstance() {
    return Shadow.newInstance(IsoDep.class, new Class<?>[] {Tag.class}, new Object[] {null});
  }

  private byte[] transceiveResponse = null;
  private byte[] nextTransceiveResponse = null;
  private boolean isExtendedLengthApduSupported = true;
  private int timeout = 300; // Default timeout in AOSP
  private int maxTransceiveLength = 0xFEFF; // Default length in AOSP

  @Implementation
  protected void __constructor__(Tag tag) {}

  @Implementation
  protected byte[] transceive(byte[] data) throws IOException {
    if (nextTransceiveResponse != null) {
      try {
        return nextTransceiveResponse;
      } finally {
        nextTransceiveResponse = null;
      }
    }
    if (transceiveResponse != null) {
      return transceiveResponse;
    }
    throw new IOException();
  }

  public void setTransceiveResponse(byte[] response) {
    transceiveResponse = response;
  }

  public void setNextTransceiveResponse(byte[] response) {
    nextTransceiveResponse = response;
  }

  @Implementation
  protected void setTimeout(int timeoutMillis) {
    timeout = timeoutMillis;
  }

  public int getTimeout() {
    return timeout;
  }

  @Implementation
  protected int getMaxTransceiveLength() {
    return maxTransceiveLength;
  }

  public void setMaxTransceiveLength(int length) {
    maxTransceiveLength = length;
  }

  @Implementation
  protected boolean isExtendedLengthApduSupported() {
    return isExtendedLengthApduSupported;
  }

  public void setExtendedLengthApduSupported(boolean supported) {
    isExtendedLengthApduSupported = supported;
  }
}
