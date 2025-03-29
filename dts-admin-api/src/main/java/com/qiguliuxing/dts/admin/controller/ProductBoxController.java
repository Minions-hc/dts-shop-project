package com.qiguliuxing.dts.admin.controller;

import com.qiguliuxing.dts.db.service.IProductBoxService;
import com.qiguliuxing.dts.vo.ProductBoxVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/productBox")
public class ProductBoxController {

    @Autowired
    private IProductBoxService productBoxService;

    @PostMapping
    public void addProductBox(@RequestBody ProductBoxVO productBox) {
        productBoxService.addProductBox(productBox);
    }

    @DeleteMapping("/{seriesId}/{boxNumber}/{productId}")
    public void deleteProductBox(@PathVariable Integer seriesId, @PathVariable String boxNumber, @PathVariable Integer productId) {
        productBoxService.deleteProductBox(seriesId, boxNumber, productId);
    }

    @PutMapping
    public void updateProductBox(@RequestBody ProductBoxVO productBox) {
        productBoxService.updateProductBox(productBox);
    }

    @GetMapping("/{seriesId}/{boxNumber}/{productId}")
    public ProductBoxVO getProductBoxById(@PathVariable Integer seriesId, @PathVariable String boxNumber, @PathVariable Integer productId) {
        return productBoxService.getProductBoxById(seriesId, boxNumber, productId);
    }

    @GetMapping("/{seriesId}")
    public List<ProductBoxVO> getProductBoxesBySeriesId(@PathVariable Integer seriesId) {
        return productBoxService.getProductBoxesBySeriesId(seriesId);
    }
}