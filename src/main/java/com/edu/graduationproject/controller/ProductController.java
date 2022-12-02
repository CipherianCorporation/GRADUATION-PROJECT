package com.edu.graduationproject.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.edu.graduationproject.entity.Product;
import com.edu.graduationproject.service.ProductService;

@Controller
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService service;

    @RequestMapping("/product/list")
    public String list(ModelMap model, @RequestParam("cid") Optional<Integer> cid) {
        return "product/list";
    }

    @RequestMapping("/product/detail/{id}")
    public String detail(ModelMap model, @PathVariable("id") Integer id) {
        Product item = service.findById(id);
        model.addAttribute("item", item);
        return "product/detail";
    }

    @RequestMapping("/product/favorite")
    public String favorite() {
        return "product/favorite";
    }
}