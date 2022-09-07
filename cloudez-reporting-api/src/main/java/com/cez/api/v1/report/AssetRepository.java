package com.cez.api.v1.report;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@org.springframework.stereotype.Repository
public interface AssetRepository extends JpaRepository<AWSAsset, String>
{
  @Query("SELECT i FROM inventory i WHERE account = :account")
  List<AWSAsset> getAllAssets(String account);
}
