package com.gani.lib.screen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class LauncherHelper {
  private Context context;

  public LauncherHelper(Context context) {
    this.context = context;
  }

  public void map(String address) {
    String uri = "http://maps.google.com/maps?q=" + address;
    // String uri = "geo:0,0?q=" + object.getNullableString("location");
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
    // intent.setPackage("com.google.android.apps.maps");
    context.startActivity(intent);
  }

  public void call(String number) {
//    Intent i = new Intent(Intent.ACTION_CALL);
//    i.setData(Uri.parse("tel:" + number));
//    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//      Toast.makeText(context, "Please grant the  permission to call", Toast.LENGTH_SHORT).show();
//      ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
//    } else {
//      context.startActivity(i);
//    }

    Intent i = new Intent(Intent.ACTION_DIAL);
    i.setData(Uri.parse("tel:" + number));
    context.startActivity(i);
  }

  public void mail(String to, String subject, String message) {
    Intent i = new Intent(Intent.ACTION_SEND);
    String[] s = {to};
    i.putExtra(Intent.EXTRA_EMAIL, s);
    i.putExtra(Intent.EXTRA_SUBJECT, subject);
    i.putExtra(Intent.EXTRA_TEXT, message);
    i.setType("message/rfc822");
    Intent chooser = Intent.createChooser(i, "Launch Email");
    context.startActivity(chooser);
  }
}