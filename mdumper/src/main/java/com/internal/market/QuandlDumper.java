package com.internal.market;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;

import com.internal.market.fetcher.GoogleStockFetcher;
import com.internal.market.fetcher.PeriodicStockDataCollector;
import com.internal.market.fetcher.QuandlDataFetcher;
import com.internal.market.fetcher.internal.TechPaisaStockIndicatorFetcher;
import com.internal.market.object.BasicStockInfo;
import com.internal.market.object.GoogleStockInfoResponseObject;
import com.internal.market.utils.DumperUtils;

public class QuandlDumper {

	private static String baseUrl = "http://www.google.com/finance/info?q=";
	private static String stockMarketName = "NSE";
	private static String companyName = "sbin";
	private static String baseResourceDir = "src/test/resources/";

	public static void main(String[] args) {

		Map<String, Float> stockStrengthMap = new HashMap<String, Float>();
		Map<String, GoogleStockInfoResponseObject> stockInfoMap = new HashMap<String, GoogleStockInfoResponseObject>();

		List<String> stockList = new ArrayList<String>(Arrays.asList("bhel", "unionbank", "Arvind", "sbin",
				"crompgreav", "dishman", "voltas", "arvind", "pricol", "adanipower", "kpit", "escorts", "sintex", "ncc",
				"hindalco", "powergrid", "recltd", "apollotyre", "albk", "tatachem", "enginersin", "petronet"));
		GoogleStockFetcher stockFetcher = null;
		try {
			HttpClient webClient = (HttpClient) RestClientFactory.createRESTClient(DumperUtils.APACHE_HTTP_CLIENT);
			// webClient.getParams().setIntParameter(name, value)

			QuandlDataFetcher fetcher = new QuandlDataFetcher(webClient);

			for (String stock : stockList) {
				List<BasicStockInfo> data = (List<BasicStockInfo>) fetcher.fetchHistoricData("NSE",
						stock.toUpperCase());

				if (data != null) {
					DumperUtils.dumpToFile(data, "Quandl-hist-data-" + stock);
					System.out.println("Data dumped for stock = " + stock);
				} else {
					System.out.println("Data found null for stock = " + stock);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Map<String, Float> sortByValue(Map<String, Float> unsortMap) {

		// 1. Convert Map to List of Map
		List<Map.Entry<String, Float>> list = new LinkedList<Map.Entry<String, Float>>(unsortMap.entrySet());

		// 2. Sort list with Collections.sort(), provide a custom Comparator
		// Try switch the o1 o2 position for a different order
		Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
			public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// 3. Loop the sorted list and put it into a new insertion order Map
		// LinkedHashMap
		Map<String, Float> sortedMap = new LinkedHashMap<String, Float>();
		for (Map.Entry<String, Float> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
}