package com.btc.get;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.btc.get.entity.TrnRateEntity;
import com.btc.get.service.GetBitCoinService;

@Controller
public class HeloController {
	@Autowired
	EmployeeRepository empRepository;

	@Autowired
	GetBitCoinService getBitCoinService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		List<Employee> emplist = empRepository.findAll();
		model.addAttribute("emplist", emplist);
		return "/index";
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String get(Model model) throws IOException {
		List<TrnRateEntity> list = getBitCoinService.get();
		model.addAttribute("emplist", list);
		return "/index2";
	}
}