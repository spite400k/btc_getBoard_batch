package com.btc.get.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.btc.get.bean.Order;
import com.btc.get.entity.ApiKeyEntity;
import com.btc.get.entity.TrnBalanceEntity;
import com.btc.get.repository.ApiKeyRepository;
import com.btc.get.repository.TrnBalanceRepository;

import net.arnx.jsonic.JSON;

@Service
public class GetBitCoinBatchService implements CommandLineRunner {

	private static String ENDPOINTURI = "https://api.bitflyer.jp";

	private static String apiKeyCd = "get_balance";

	private static BigDecimal mBTC = BigDecimal.valueOf(0.001);
	private static BigDecimal million = new BigDecimal(1000000);

	private static Logger logger = LoggerFactory.getLogger(GetBitCoinBatchService.class);
	private static Integer SLEEP_TIME = 10 * 1000; // １処理あたりの休眠時間 秒 10秒
	private static Integer RUN_TIME = 0; // 指定の秒の間、実行

	@Autowired
	private ApiKeyRepository apiKeyRepository;

	@Autowired
	private TrnBalanceRepository trnBalanceRepository;

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
			if (nowDate2.after(DateUtils.addSeconds(nowDate1, RUN_TIME))) {
				continuos = false;
			}
			Thread.sleep(SLEEP_TIME);

			SLEEP_TIME = 1000 * 1000;
		}
	}

	/**
	 * @param nowDate2
	 * @throws IOException
	 */
	@Transactional
	private void getAndSave(Timestamp ts) throws IOException {
		sendChildOrder(ts);
	}

	/**
	 * Bitflyer BTC_JPY 板情報
	 *
	 * @param nowDate2
	 *
	 * @throws IOException
	 */
	private void sendChildOrder(Timestamp ts) throws IOException {

		String method = "POST";
		String path = "/v1/me/sendchildorder";
		String query = "";
		String body = "";
//		"{"+
//				  "'product_code': 'BTC_JPY',"+
//				  "child_order_type": "LIMIT",+
//				  "side": "BUY",+
//				  "price": 30000,+
//				  "size": 0.1,+
//				  "minute_to_expire": 10000,+
//				  "time_in_force": "GTC"+
//				}";

		String jsonData;

		Optional<ApiKeyEntity> entity = apiKeyRepository.findById(apiKeyCd);
		String apiKey = entity.get().getApi_key();
		String apiSecret = entity.get().getApi_secret();

		Order or = new Order();
		or.setProduct_code("BTC_JPY");
		or.setChild_order_type("LIMIT");
		or.setSide("BUY");
		or.setPrice(million);
		or.setSize(mBTC);
		or.setMinute_to_expire(10000);
		or.setTime_in_force("GTC");
		body = JSON.encode(or);

		jsonData = getHttpRequest(ENDPOINTURI, path, method, ts, query, apiKey, apiSecret, body);

		// 出力
		logger.info(jsonData);

		if (jsonData != null) {

			// JSONデータをクラスに変換
			ArrayList mapper = JSON.decode(jsonData, ArrayList.class);
			// BalanceObject mapper = JSON.decode(jsonData, BalanceObject.class);
			// 出力
			logger.info("balance");

			for (int i = 0; i < mapper.size(); i++) {

				HashMap map = (HashMap) mapper.get(i);
				TrnBalanceEntity re = new TrnBalanceEntity();
				re.setCurrency_code((String) map.get("currency_code"));
				re.setAmount((BigDecimal) map.get("amount"));
				re.setAvailable((BigDecimal) map.get("available"));

				re.setTimestamp(ts);

				trnBalanceRepository.save(re);
			}

		}

	}

	/**
	 *
	 * @param site_url
	 * @param pass_url
	 * @param method
	 * @param ts
	 * @param query
	 * @param apiSecret
	 * @param apiKey
	 * @param body
	 * @return
	 * @throws IOException
	 */
	public String getHttpRequest(String site_url, String path, String method, Timestamp ts, String query, String apiKey,
			String apiSecret, String body) throws IOException {

		URL url = new URL(site_url + path);

		HttpURLConnection connection = null;

		try {
			// コネクションを作成
			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("ACCESS-KEY", apiKey);
			connection.setRequestProperty("ACCESS-TIMESTAMP", ts.toString());

			String data = ts.toString() + method + path + query + body;
			String hash = SignWithHMACSHA256(data, apiSecret);
			connection.setRequestProperty("ACCESS-SIGN", hash);

			connection.setRequestMethod(method);
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Macintosh; U; Intel Mac OS X; ja-JP-mac; rv:1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.setUseCaches(true);

			// connection.set
			connection.setDoOutput(true);
			OutputStream outputStream = connection.getOutputStream();
			PrintStream ps = new PrintStream(connection.getOutputStream());
			ps.print(body);
			ps.close();
			outputStream.close();

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
				return getStr;
			}

			return getStr;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			// return null;
		}
	}

	/**
	 * HmacSHA256
	 *
	 * @param data
	 * @param secret
	 * @return
	 */
	private String SignWithHMACSHA256(String data, String secret) {

		String algo = "HmacSHA256";
		String hash = null;
		try {
			String plaintext = data;
			SecretKeySpec sk = new SecretKeySpec(secret.getBytes(), algo);
			Mac mac = Mac.getInstance(algo);
			mac.init(sk);

			byte[] mac_bytes = mac.doFinal(plaintext.getBytes());

			hash = toHexString(mac_bytes);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return hash;
	}

	/**
	 * @param mac_bytes
	 * @return
	 */
	private String toHexString(byte[] mac_bytes) {

		StringBuilder sb = new StringBuilder(2 * mac_bytes.length);
		for (byte b : mac_bytes) {
			sb.append(String.format("%02x", b & 0xff));
		}
		System.out.println(sb);
		return sb.toString();
	}
}
