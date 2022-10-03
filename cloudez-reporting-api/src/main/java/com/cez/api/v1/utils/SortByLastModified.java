package com.cez.api.v1.utils;

import java.util.Comparator;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class SortByLastModified implements Comparator<S3ObjectSummary>
{

  @Override
  public int compare(S3ObjectSummary o1, S3ObjectSummary o2)
  {
    return o2.getLastModified().compareTo(o1.getLastModified());
  }

}
