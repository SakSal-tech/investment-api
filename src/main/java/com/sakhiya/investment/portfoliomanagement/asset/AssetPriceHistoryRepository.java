package com.sakhiya.investment.portfoliomanagement.asset;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetPriceHistoryRepository extends ListCrudRepository<AssetPriceHistory, String> {
    List<AssetPriceHistory> findByAsset_AssetIdOrderByTradingDateAsc(String assetId);
List<AssetPriceHistory> findByAsset_AssetIdAndTradingDateBetweenOrderByTradingDateAsc(String assetId, LocalDate start, LocalDate end);
    // Find all price history for a given asset symbol (via asset name as symbol)
    List<AssetPriceHistory> findByAsset_NameOrderByTradingDateAsc(String symbol);
}