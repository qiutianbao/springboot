package com.jxtb.controller;

import com.jxtb.domain.City;
import com.jxtb.handler.CityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by jxtb on 2019/2/14.
 */
@Controller
public class CityController {

    @Autowired
    private CityHandler cityHandler;


    @GetMapping("/index")
    public Mono<String> hello(final Model model) {
        model.addAttribute("name", "jxtb");
        model.addAttribute("city", "上海");

        String path = "index";
        return Mono.create(monoSink -> monoSink.success(path));
    }

    private static final String CITY_LIST_PATH_NAME = "cityList";

    @GetMapping("/page/list")
    public String listPage(final Model model) {
        final Flux<City> cityFluxList = cityHandler.findAllCity();
        model.addAttribute("cityList", cityFluxList);
        return CITY_LIST_PATH_NAME;
    }

}