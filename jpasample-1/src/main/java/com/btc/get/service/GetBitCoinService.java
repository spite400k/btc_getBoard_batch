package com.btc.get.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btc.get.entity.TrnRateEntity;
import com.btc.get.repository.TrnLatestRateRepository;
import com.btc.get.repository.TrnRateAskRepository;
import com.btc.get.repository.TrnRateBidRepository;
import com.btc.get.repository.TrnRateRepository;

@Service
public class GetBitCoinService {

	private static Logger logger = LoggerFactory.getLogger(GetBitCoinService.class);
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
	 * @return
	 *
	 * @return
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public List<TrnRateEntity> get(String... args) throws IOException {
		List<TrnRateEntity> entityList = getBoard();
		getTicker();
		getExecutions();

		return entityList;
	}

	/**
	 * Bitflyer BTC_JPY 板情報
	 *
	 * @param nowDate2
	 * @return
	 *
	 * @throws IOException
	 */
	private List<TrnRateEntity> getBoard() throws IOException {

		// 出力
		logger.info("trnrate");

		List<TrnRateEntity> entityList = rateRepository.findAll();

//		List<TrnRateAskEntity> askList = rateAskRepository.findAll(example);
//		List<TrnRateBidEntity> bidList = rateBidRepository.findAll();
		return entityList;
	}

	/**
	 * Bitflyer BTC_JPY 最新の価格
	 *
	 * @param nowDate2
	 *
	 * @throws IOException
	 */
	private void getTicker() throws IOException {
//		String jsonData;
//		jsonData = getHttpRequest(ENDPOINTURI, "/v1/ticker?product_code=BTC_JPY");
//
////		logger.info(jsonData);
//
//		// JSONデータをクラスに変換
//		LatestRateObject mapper = JSON.decode(jsonData, LatestRateObject.class);
//
//		// BIDとASKを取得
//		logger.info("getTicker");
////
////		logger.info(mapper.getProduct_code());
////
////		logger.info(mapper.getTotal_ask_depth());
////		logger.info(mapper.getBest_ask());
////		logger.info(mapper.getBest_ask_size());
////
////		logger.info(mapper.getBest_bid());
////		logger.info(mapper.getBest_bid_size());
////		logger.info(mapper.getTotal_bid_depth());
////
////		logger.info(mapper.getLtp());
////		logger.info(mapper.getTick_id());
////		logger.info(mapper.getTimestamp());
////
////		logger.info(mapper.getVolume());
////		logger.info(mapper.getVolume_by_product());
//
//		TrnLatestRateEntity e = new TrnLatestRateEntity();
//
//		e.setBest_ask(mapper.getBest_ask());
//		e.setBest_ask_size(mapper.getBest_ask_size());
//		e.setBest_bid(mapper.getBest_bid());
//		e.setBest_bid_size(mapper.getBest_bid_size());
//		e.setLtp(mapper.getLtp());
//		e.setProduct_code(mapper.getProduct_code());
//		e.setTick_id(mapper.getTick_id());
//		e.setTimestamp(mapper.getTimestamp());
//		e.setTotal_ask_depth(mapper.getTotal_ask_depth());
//		e.setTotal_bid_depth(mapper.getTotal_bid_depth());
//		e.setVolume(mapper.getVolume());
//		e.setVolume_by_product(mapper.getVolume_by_product());
//
//		latestRateRepository.save(e);
	}

	/**
	 * Bitflyer BTC_JPY 直近の取引情報
	 *
	 * @param nowDate2
	 *
	 * @throws IOException
	 */
	private void getExecutions() throws IOException {
//		String jsonData;
//		jsonData = getHttpRequest(ENDPOINTURI, "/v1/executions?product_code=BTC_JPY");
//
////		logger.info(jsonData);
//
//		// JSONデータをクラスに変換
//		List<ExcutionObject> mapper = JSON.decode(jsonData, new TypeReference<List<ExcutionObject>>() {
//		});
////		logger.info("getExecutions");
//
////		for (ExcutionObject eo : mapper) {
////			logger.info(eo.getId().toString());
////			logger.info(eo.getPrice().toString());
////			logger.info(eo.getSize().toString());
////			logger.info(eo.getSide().toString());
////			logger.info(eo.getExec_date().toString());
////			logger.info(eo.getBuy_child_order_acceptance_id().toString());
////			logger.info(eo.getSell_child_order_acceptance_id().toString());
////
////			TrnLatestRateEntity e = new TrnLatestRateEntity();
////
////		}
//		;

	}
}
