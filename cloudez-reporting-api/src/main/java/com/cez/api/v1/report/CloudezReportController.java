package com.cez.api.v1.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.cez.api.v1.auth.aws.Credentials;
import com.cez.api.v1.commons.aws.Client;
import com.cez.api.v1.utils.ReportUtils;

@RestController
@RequestMapping("reporting/api/v1")
public class CloudezReportController
{
  @Autowired
  private AssetRepository repository;

  @Autowired
  private Environment env;

  private Credentials credentials = null;
  private AmazonS3 s3Client = null;

  @Value("${execution.mode}")
  private String executionMode;

  public CloudezReportController()
  {
    credentials = new Credentials(executionMode);
  }

  @GetMapping("/ping")
  String ping()
  {
    return ("cloudez-reporting-api");
  }

  @GetMapping("/store/{account}/{type}")
  String store(@PathVariable String account, @PathVariable Integer type)
  {
    String uploadHash = null;
    if (type == 0)
    {
      DateTime time = new DateTime();
      String reportName = env.getProperty("reports.inventory.type") + "-" + time.getYear() + time.getMonthOfYear() + time.getDayOfMonth() + ".csv";
      s3Client = new Client(account, credentials).getS3Client();
      try
      {
        File report = new File(reportName);
        FileWriter out = new FileWriter(report);
        String header[] = {"Account", "Service", "ID", "Type", "ARN", "Region"};
        List<AWSAsset> assetList = repository.getAllAssets(account);
        CSVPrinter csv = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(header));
        for (AWSAsset asset : assetList)
        {
          csv.printRecord(asset.getAccount(), asset.getService(), asset.getUniqueIdentifier(), asset.getResourceType(), asset.getArn(), asset.getRegion());
        }
        uploadHash =
            s3Client.putObject(env.getProperty("reports.bucket.name"), env.getProperty("reports.inventory.type") + "/" + reportName, report).getContentMd5();
        report.delete();
      }
      catch (IOException e)
      {
        System.err.println(e.getMessage());
      }
    }
    return uploadHash;
  }

  @GetMapping("/list/{account}")
  Map<String, Object> list(@PathVariable String account)
  {
    Map<String, Object> output = new HashMap<String, Object>();
    s3Client = new Client(account, credentials).getS3Client();
    ListObjectsV2Result result = null;
    result = s3Client.listObjectsV2(env.getProperty("reports.bucket.name"), env.getProperty("reports.inventory.type"));
    List<S3ObjectSummary> objects = result.getObjectSummaries();
    List<Map<String, Object>> reports = new ArrayList<Map<String, Object>>();
    for (S3ObjectSummary os : objects)
    {
      Map<String, Object> reportInformation = new HashMap<String, Object>();
      String report = os.getKey();
      if (report.contains("/")) report = os.getKey().split("/")[StringUtils.split(os.getKey(), "/").length - 1];
      reportInformation.put("report", StringUtils.cleanPath(report));
      reportInformation.put("timestamp", os.getLastModified());
      reportInformation.put("size", os.getSize());
      reportInformation.put("description", env.getProperty("reports.inventory.description"));
      reports.add(reportInformation);
    }
    output.put(env.getProperty("reports.inventory.type"), reports);
    return output;
  }

  @GetMapping("/download/{account}/{report}")
  ResponseEntity<byte[]> download(@PathVariable String account, @PathVariable String report)
  {
    s3Client = new Client(account, credentials).getS3Client();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    S3Object s3object =
        s3Client.getObject(new GetObjectRequest(env.getProperty("reports.bucket.name"), env.getProperty("reports.inventory.type") + "/" + report));
    try
    {
      InputStream is = s3object.getObjectContent();
      int len;
      byte[] buffer = new byte[4096];
      while ((len = is.read(buffer, 0, buffer.length)) != -1)
      {
        outputStream.write(buffer, 0, len);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return ResponseEntity.ok().contentType(ReportUtils.contentType(report)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report + "\"")
        .body(outputStream.toByteArray());
  }

}
