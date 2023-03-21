package com.leonardo.springwebservice.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leonardo.springwebservice.domain.Product;
import com.leonardo.springwebservice.domain.dto.ProductDTO;
import com.leonardo.springwebservice.resources.utils.URL;
import com.leonardo.springwebservice.services.ProductService;

@RestController
@RequestMapping(value = "/products")
public class ProductResouce {
    
    @Autowired
    private ProductService productService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> findById(@PathVariable Integer id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> search(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "categories", defaultValue = "") String categories,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        String decodedName = URL.decodeParam(name);
        List<Integer> ids = URL.decodeIntList(categories);
        Page<Product> list = productService.search(decodedName,ids, page, linesPerPage, orderBy, direction);
        Page<ProductDTO> listDto = list.map(x -> new ProductDTO(x));
        return ResponseEntity.ok().body(listDto);
    }
}
