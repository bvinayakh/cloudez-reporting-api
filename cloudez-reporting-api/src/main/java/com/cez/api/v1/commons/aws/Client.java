package com.cez.api.v1.commons.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.cez.api.v1.auth.aws.Credentials;

public class Client
{
  public static final Logger logger = LoggerFactory.getLogger(Client.class);

  private Credentials credentials = null;

  public Client(String account, Credentials credentials)
  {
    credentials.setAccount(account);
    this.credentials = credentials;
  }

  public AmazonS3 getS3Client(String... region)
  {
    AmazonS3ClientBuilder client = AmazonS3Client.builder();
    if (region.length > 0) client.setRegion(region[0]);
    client.setCredentials(credentials);
    return client.build();
  }
}
