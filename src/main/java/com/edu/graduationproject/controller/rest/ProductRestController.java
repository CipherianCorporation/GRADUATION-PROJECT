package com.edu.graduationproject.controller.rest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.graduationproject.entity.Product;
import com.edu.graduationproject.entity.Size;
import com.edu.graduationproject.service.ProductService;

@CrossOrigin("*")
@RestController
public class ProductRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRestController.class);

    @Autowired
    ProductService productService;

    @GetMapping("/rest/products")
    public ResponseEntity<List<Product>> getAll(@RequestParam("sort") Optional<String> sort) {
        if (sort.isPresent()) {
            if (sort.get().equalsIgnoreCase("all")) {
                return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
            }
            if (sort.get().equalsIgnoreCase("price_asc")) {
                return new ResponseEntity<>(productService.findAll(Sort.by(Direction.ASC, "price")),
                        HttpStatus.OK);
            }
            if (sort.get().equalsIgnoreCase("price_desc")) {
                return new ResponseEntity<>(productService.findAll(Sort.by(Direction.DESC, "price")),
                        HttpStatus.OK);
            }
            if (sort.get().equalsIgnoreCase("date_newest")) {
                return new ResponseEntity<>(productService.findAll(Sort.by(Direction.DESC, "createdAt")),
                        HttpStatus.OK);
            }
            if (sort.get().equalsIgnoreCase("sold_best_seller")) {
                return new ResponseEntity<>(productService.findAll(Sort.by(Direction.DESC, "sold")),
                        HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(productService.findAllIsDeletedFalse(), HttpStatus.OK);
    }

    @GetMapping("/rest/products/{id}")
    public ResponseEntity<Product> getOne(@PathVariable("id") Integer id) {
        try {
            Product product = productService.findById(id);
            return new ResponseEntity<Product>(product, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error when getting product by id", e);
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/rest/products/name/{name}")
    public ResponseEntity<List<Product>> findByName(@PathVariable("name") String name) {
        try {
            List<Product> product = productService.findByName(name);
            return new ResponseEntity<List<Product>>(product, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error when getting product by name like", e);
            return new ResponseEntity<List<Product>>(HttpStatus.NOT_FOUND);
        }
    }

    // return all products by category id
    @GetMapping("/rest/products/category/{categoryId}")
    public ResponseEntity<List<Product>> findProductByCategory(
            @PathVariable("categoryId") Optional<Integer> categoryId) {
        try {
            return ResponseEntity.ok(productService.findByCategoryId(categoryId.get()));
        } catch (Exception e) {
            LOGGER.error("Error when getting product by category id", e);
            return ResponseEntity.notFound().build();
        }
    }

    // return all products by sub-category id
    @GetMapping("/rest/products/sub-category/{subCategoryId}")
    public ResponseEntity<List<Product>> findProductBySubCategory(
            @PathVariable("subCategoryId") Optional<Integer> subCategoryId) {
        try {
            return ResponseEntity.ok(productService.findBySubCategoryId(subCategoryId.get()));
        } catch (Exception e) {
            LOGGER.error("Error when getting product by sub-category id", e);
            return ResponseEntity.notFound().build();
        }
    }

    // return all products if sale_off field isn't null or not equal to 0
    @GetMapping("/rest/products/sale-off")
    public ResponseEntity<List<Product>> findProductBySaleOff() {
        try {
            return ResponseEntity.ok(productService.findAllBySaleOff());
        } catch (Exception e) {
            LOGGER.error("Error when getting product by sale off", e);
            return ResponseEntity.notFound().build();
        }
    }

    // return all products by price range
    @GetMapping("/rest/products/price-range/{min}/{max}")
    public ResponseEntity<List<Product>> findProductByPriceRange(@PathVariable("min") Optional<Double> min,
            @PathVariable("max") Optional<Double> max) {
        Double _min = min.orElse(0d);
        Double _max = max.orElse(0d);
        if (_min < _max) {
            return ResponseEntity.ok(productService.findAllByPriceRange(_min, _max));
        }
        return ResponseEntity.badRequest().build();
    }

    // for admin only, basic CRUD

    @PostMapping("/rest/products")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        try {
            if (product.getSize() == null) {
                product.setSize(new Size(0));
            }
            if (product.getSale_off() == null || product.getSale_off() <= 0) {
                product.setSale_off((double) 0);
            }
            if (product.getPrice() == null || product.getPrice() <= 0) { 
                return ResponseEntity.badRequest().build();
            }
            if (product.getStock() == null || product.getStock() <= 0) {
                return ResponseEntity.badRequest().build();
            }
            if (product.getName() == null || product.getName().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(productService.create(product));
        } catch (Exception e) {
            LOGGER.error("Error when creating product", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/rest/products/{id}")
    public ResponseEntity<Product> update(@PathVariable("id") Integer id, @RequestBody Product product) {
        try {
            if (product.getSize() == null) {
                product.setSize(new Size(0));
            }
            if (product.getSale_off() == null || product.getSale_off() <= 0) {
                product.setSale_off((double) 0);
            }
            if (product.getPrice() == null || product.getPrice() <= 0) {
                return ResponseEntity.badRequest().build();
            }
            if (product.getStock() == null || product.getStock() <= 0) {
                return ResponseEntity.badRequest().build();
            }
            if (product.getName() == null || product.getName().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Product existProduct = productService.findById(id);
            productService.update(product);
            return new ResponseEntity<Product>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
        // return ResponseEntity.ok(productService.update(product));
    }

    @DeleteMapping("/rest/products/{id}")
    public void delete(@PathVariable("id") Integer id) {
        try {
            productService.delete(id);
        } catch (Exception e) {
            LOGGER.error("Error when deleting product", e);
        }
    }

}
