package com.btc.get;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.btc.get.batch.GetBitCoinBatchService;

@SpringBootApplication
public class GetBitCoinApplication {

	/**
	 * メインクラス
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("main()");
		try {

			ConfigurableApplicationContext context = SpringApplication.run(GetBitCoinApplication.class, args);
			GetBitCoinBatchService service = context.getBean(GetBitCoinBatchService.class);
			service.run(args);
			context.close();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
