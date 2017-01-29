package com.gani.lib.http;

import com.gani.lib.R;
import com.gani.lib.logging.GLog;
import com.gani.lib.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;

public abstract class GHttpResponse<RR extends GRestResponse> implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private byte[] binary;
  private String string;
//  private GRestResponse restReponse;
  private RR restReponse;
  private String url;
  private GHttpError error;

  protected GHttpResponse(String url) {
    this.url = url;
//    this.error = new GHttpError(this);
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
  
  public byte[] asBinary() {
    return binary;
  }
  
  public String asString() {
    return string;
  }
  
  public RR asRestResponse() {
    if (restReponse == null) {
      GLog.d(getClass(), "Result of " + url + ": " + string);
//      restReponse = new GRestResponse(string, this);
      restReponse = createRestResponse(string);
    }
    GLog.d(getClass(), "REST RESPONSE1: " + restReponse + " -- " + string);
    return restReponse;
  }

  protected abstract RR createRestResponse(String jsonString);
  protected abstract GHttpError createError();

  void extractFrom(HttpURLConnection connection) throws IOException {
    int code = connection.getResponseCode();
//    if (code == HttpURLConnection.HTTP_OK) {
    if (code < 300) {  // Includes the standard 200, the less common 201, etc.
      this.binary = readByteArray(connection.getInputStream(),
          getContentLengthForBufferring(connection));
      this.string = new String(binary);
    }
    else {
      GLog.t(getClass(), "HTTP Code: " + code);
      error.markForCode(code);

      GLog.t(getClass(), "Reading data1 ... ");
      this.binary = readByteArray(connection.getErrorStream(),
          getContentLengthForBufferring(connection));
      GLog.t(getClass(), "Reading data2 ... ");
      this.string = new String(binary);
      GLog.t(getClass(), "Finished reading data: " + string);
    }
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
}