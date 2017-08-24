package com.gani.lib.http;

import com.gani.lib.R;
import com.gani.lib.logging.GLog;
import com.gani.lib.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;

public class GHttpResponse<RR extends GRestResponse> implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private byte[] binary;
  private String string;
  private RR restReponse;
  private String url;
  private GHttpError error;
  private Integer code;  // Could be null, e.g. if network times out

  protected GHttpResponse(String url) {
    this.url = url;
    this.error = createError();
  }
  
  public String getUrl() {
    return url;
  }
  
  public GHttpError getError() {
    return error;
  }
  
  void setError(GHttpError error) {
    this.error = error;
  }

  public boolean hasError() {
    return error.hasError();
  }

  public void setCode(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public byte[] asBinary() {
    return binary;
  }
  
  public String asString() {
    return string;
  }
  
  public RR asRestResponse() {
    if (restReponse == null) {
      restReponse = createRestResponse(string);
    }
    return restReponse;
  }

  // To be overridden
  protected RR createRestResponse(String jsonString) {
    return (RR) new GRestResponse(jsonString, this);
  }

  // To be overridden
  protected GHttpError createError() {
    return new GHttpError(this);
  }

  void extractFrom(HttpURLConnection connection) throws IOException {
    int code = connection.getResponseCode();
    setCode(code);

    // Redirection (e.g. 300) should have been handled prior, so we consider anything below 300 a "success".
    if (code > 300) {
      GLog.t(getClass(), "HTTP Code: " + code);
      error.markForCode(code);
    }

    InputStream inputStream = connection.getInputStream();
    if (inputStream == null) {
      inputStream = connection.getErrorStream();
    }

    if (inputStream == null) {  // Not sure if this will happen ever, but just a safe guard especially since we're dealing with API.
      this.binary = new byte[0];
    }
    else {
      this.binary = readByteArray(inputStream, getContentLengthForBufferring(connection));
    }
    this.string = new String(binary);
  }
  
  void handle(GHttpCallback callback) {
    if (error.hasError()) {
      callback.onHttpFailure(error);
    }
    else {
      callback.onHttpSuccess(this);
    }
  }

  private static int getContentLengthForBufferring(HttpURLConnection connection) {
    try {
      return Integer.parseInt(connection.getHeaderField("Content-Length"));
    }
    catch (Exception e) {
      GLog.i(GHttpResponse.class, "Using default buffer length, because we cant get content length from header");
      return Ui.integer(R.integer.data_buffer);
    }
  }

  private static byte[] readByteArray(InputStream in, int bufLen) throws IOException {
    byte[] buffer = new byte[Ui.integer(R.integer.data_buffer)];
    ByteArrayOutputStream bos = new ByteArrayOutputStream(bufLen);
    int read = in.read(buffer);
    while (read > -1) {
      bos.write(buffer, 0, read);
      try {
        read = in.read(buffer);
      }
      catch (IllegalStateException e) {
        // There has been ANRs on "java.lang.IllegalStateException: attempt to use Inflater after calling end"
        // It's not good to just let the app crash, so we should treat it like a normal IOException.
        throw new IOException(e.getMessage());
      }
    }

    bos.flush();
    byte[] data = bos.toByteArray();
    GLog.d(GHttpResponse.class, "Actual HTTP result size (in bytes): " + data.length);
    return data;
  }

  @Override
  public String toString() {
    return asString();
  }
}
