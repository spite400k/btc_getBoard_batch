package com.btc.get.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.btc.get.bean.Ask;
import com.btc.get.bean.Bid;
import com.btc.get.bean.ExcutionObject;
import com.btc.get.bean.LatestRateObject;
import com.btc.get.bean.RateObject;
import com.btc.get.entity.TrnLatestRateEntity;
import com.btc.get.entity.TrnRateAskEntity;
import com.btc.get.entity.TrnRateBidEntity;
import com.btc.get.entity.TrnRateEntity;
import com.btc.get.entity.TrnRatePK;
import com.btc.get.repository.TrnLatestRateRepository;
import com.btc.get.repository.TrnRateAskRepository;
import com.btc.get.repository.TrnRateBidRepository;
import com.btc.get.repository.TrnRateRepository;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.TypeReference;

@Service
public class GetBitCoinBatchService implements CommandLineRunner {

	private static String ENDPOINTURI = "https://api.bitflyer.jp";

	private static Logger logger = LoggerFactory.getLogger(GetBitCoinBatchService.class);
	private static Integer SLEEP_TIME = 10 * 1000; // １処理あたりの休眠時間 秒 10秒
	private static Integer RUN_TIME = 600; // 指定の秒の間、実行

	@Autowired
	private TrnLatestRateRepository latestRateRepository;

	@Autowired
	private TrnRateRepository rateRepository;

	@Autowired
	private TrnRateBidRepository rateBidRepository;

	@Autowired
	private TrnRateAskRepository rateAskRepository;

	/**
	 * ビットコイン情報取得クラス
	 *
	 * @param args
	 *
	 * @return
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	public void run(String... args) throws IOException, InterruptedException {

		boolean continuos = true;

		Date nowDate1 = new Date();
		Date nowDate2 = new Date();

		while (continuos) {
			try {

				getAndSave(new Timestamp(nowDate2.getTime()));
			} catch (Exception e) {
				e.printStackTrace();
				SLEEP_TIME = SLEEP_TIME * 10;
			}
			nowDate2 = new Date();
//			if (nowDate2.after(DateUtils.addSeconds(nowDate1, RUN_TIME))) {
//				continuos = false;
//			}
			Thread.sleep(SLEEP_TIME);

			SLEEP_TIME = 10 * 1000;
		}
	}

	/**
	 * @param nowDate2
	 * @throws IOException
	 */
	@Transactional
	private void getAndSave(Timestamp ts) throws IOException {
		getBoard(ts);
		getTicker(ts);
		getExecutions(ts);
	}

	/**
	 * Bitflyer BTC_JPY 板情報
	 *
	 * @param nowDate2
	 *
	 * @throws IOException
	 */
	private void getBoard(Timestamp ts) throws IOException {
		String jsonData;
		jsonData = getHttpRequest(ENDPOINTURI, "/v1/board?product_code=BTC_JPY");

//		logger.info(jsonData);

		// JSONデータをクラスに変換
		RateObject mapper = JSON.decode(jsonData, RateObject.class);

		// BIDとASKのLISTを取得
		// 売り
		List<Bid> bids = mapper.getBids();
		// 買い
		List<Ask> asks = mapper.getAsks();

		// BIDデータを抽出
		BigDecimal rateBid = bids.get(0).getPrice();
		// ASKデータを抽出
		BigDecimal rateAsk = asks.get(0).getPrice();

		// 出力
		logger.info("trnrate");
//		logger.info(rateBid);
//		logger.info(rateAsk);

		TrnRateEntity re = new TrnRateEntity();
		re.setMid_price(mapper.getMid_price());
		re.setCreate_timestamp(ts);

		rateRepository.save(re);

		Long autoId = rateRepository.selectMaXAutoId();
		if (autoId == null) {
			autoId = 1L;
		}

//		logger.info(autoId.toString());

		List<TrnRateAskEntity> askList = new ArrayList<TrnRateAskEntity>();
		List<TrnRateBidEntity> bidList = new ArrayList<TrnRateBidEntity>();

		Long count = 0L;

		// 売り 高い値段のものを残す
		bids.stream().sorted((p1, p2) -> {
//			int ret = p1.getPrice().compareTo(p2.getPrice());
//			return ret == 0 ? p1.getPrice().compareTo(p2.getPrice()) : ret;

			return p2.getPrice().compareTo(p1.getPrice());
		});

		logger.info("bid");
		for (Bid bid : bids) {
			count += 1;
			TrnRateBidEntity be = new TrnRateBidEntity();
			TrnRatePK pk = new TrnRatePK();
			pk.setAutoId(autoId);
			pk.setAutoIdDetail(count);

			be.setTrnRatePK(pk);
			be.setPrice(bid.getPrice());
			be.setSize(bid.getSize());

			be.setCreate_timestamp(ts);
			bidList.add(be);

			if (count >= 6) {
				break;
			}
		}
		rateBidRepository.saveAll(bidList);
		// logger.info(String.valueOf(bidList.size()));

		count = 0L;

		// 買い
		asks.stream().sorted((p1, p2) -> {
//			int ret = p1.getPrice().compareTo(p2.getPrice());
//			return ret == 0 ? p1.getPrice().compareTo(p2.getPrice()) : ret;

			return p1.getPrice().compareTo(p2.getPrice());
		});

		logger.info("ask");
		for (Ask ask : asks) {
			count += 1;
			TrnRateAskEntity ae = new TrnRateAskEntity();
			TrnRatePK pk = new TrnRatePK();
			pk.setAutoId(autoId);
			pk.setAutoIdDetail(count);

			ae.setTrnRatePK(pk);
			ae.setPrice(ask.getPrice());
			ae.setSize(ask.getSize());

			ae.setCreate_timestamp(ts);
			askList.add(ae);

			if (count >= 6) {
				break;
			}
//			logger.info(count.toString());

		}

		rateAskRepository.saveAll(askList);
//		logger.info(String.valueOf(askList.size()));

	}

	/**
	 * Bitflyer BTC_JPY 最新の価格
	 *
	 * @param nowDate2
	 *
	 * @throws IOException
	 */
	private void getTicker(Timestamp ts) throws IOException {
		String jsonData;
		jsonData = getHttpRequest(ENDPOINTURI, "/v1/ticker?product_code=BTC_JPY");

//		logger.info(jsonData);

		// JSONデータをクラスに変換
		LatestRateObject mapper = JSON.decode(jsonData, LatestRateObject.class);

		// BIDとASKを取得
		logger.info("getTicker");
//
//		logger.info(mapper.getProduct_code());
//
//		logger.info(mapper.getTotal_ask_depth());
//		logger.info(mapper.getBest_ask());
//		logger.info(mapper.getBest_ask_size());
//
//		logger.info(mapper.getBest_bid());
//		logger.info(mapper.getBest_bid_size());
//		logger.info(mapper.getTotal_bid_depth());
//
//		logger.info(mapper.getLtp());
//		logger.info(mapper.getTick_id());
//		logger.info(mapper.getTimestamp());
//
//		logger.info(mapper.getVolume());
//		logger.info(mapper.getVolume_by_product());

		TrnLatestRateEntity e = new TrnLatestRateEntity();

		e.setBest_ask(mapper.getBest_ask());
		e.setBest_ask_size(mapper.getBest_ask_size());
		e.setBest_bid(mapper.getBest_bid());
		e.setBest_bid_size(mapper.getBest_bid_size());
		e.setLtp(mapper.getLtp());
		e.setProduct_code(mapper.getProduct_code());
		e.setTick_id(mapper.getTick_id());
		e.setTimestamp(mapper.getTimestamp());
		e.setTotal_ask_depth(mapper.getTotal_ask_depth());
		e.setTotal_bid_depth(mapper.getTotal_bid_depth());
		e.setVolume(mapper.getVolume());
		e.setVolume_by_product(mapper.getVolume_by_product());

		latestRateRepository.save(e);
	}

	/**
	 * Bitflyer BTC_JPY 直近の取引情報
	 *
	 * @param nowDate2
	 *
	 * @throws IOException
	 */
	private void getExecutions(Timestamp ts) throws IOException {
		String jsonData;
		jsonData = getHttpRequest(ENDPOINTURI, "/v1/executions?product_code=BTC_JPY");

//		logger.info(jsonData);

		// JSONデータをクラスに変換
		List<ExcutionObject> mapper = JSON.decode(jsonData, new TypeReference<List<ExcutionObject>>() {
		});
//		logger.info("getExecutions");

//		for (ExcutionObject eo : mapper) {
//			logger.info(eo.getId().toString());
//			logger.info(eo.getPrice().toString());
//			logger.info(eo.getSize().toString());
//			logger.info(eo.getSide().toString());
//			logger.info(eo.getExec_date().toString());
//			logger.info(eo.getBuy_child_order_acceptance_id().toString());
//			logger.info(eo.getSell_child_order_acceptance_id().toString());
//
//			TrnLatestRateEntity e = new TrnLatestRateEntity();
//
//		}
		;

	}

	/**
	 *
	 * @param site_url
	 * @param pass_url
	 * @return
	 * @throws IOException
	 */
	public String getHttpRequest(String site_url, String pass_url) throws IOException {

		URL url = new URL(site_url + pass_url);

		HttpURLConnection connection = null;

		try {
			// コネクションを作成
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Macintosh; U; Intel Mac OS X; ja-JP-mac; rv:1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.setUseCaches(true);

			String getStr = "";

			// レスポンスを取得
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// 文字列として出力
				try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);

						BufferedReader reader = new BufferedReader(isr)) {
					String line;
					while ((line = reader.readLine()) != null) {
						getStr += line;
					}
				}
			} else {
				// エラー処理(エラーの場合はnullを返す)
				try (InputStreamReader isr = new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8);

						BufferedReader reader = new BufferedReader(isr)) {
					String line;
					while ((line = reader.readLine()) != null) {
						getStr += line;
					}
				}
				return null;
			}

			return getStr;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			// return null;
		}
	}

}
