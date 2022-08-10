package com.cez.api.v1.utils;

import org.springframework.http.MediaType;

public class ReportUtils
{
  public static MediaType contentType(String filename)
  {
    String[] fileArrSplit = filename.split("\\.");
    String fileExtension = fileArrSplit[fileArrSplit.length - 1];
    switch (fileExtension)
    {
      case "txt":
        return MediaType.TEXT_PLAIN;
      case "png":
        return MediaType.IMAGE_PNG;
      case "jpg":
        return MediaType.IMAGE_JPEG;
      default:
        return MediaType.APPLICATION_OCTET_STREAM;
    }
  }
}
