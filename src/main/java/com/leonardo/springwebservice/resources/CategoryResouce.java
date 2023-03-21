package com.leonardo.springwebservice.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.leonardo.springwebservice.domain.Category;
import com.leonardo.springwebservice.domain.dto.CategoryDTO;
import com.leonardo.springwebservice.services.CategoryService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResouce {
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<Category> list = categoryService.findAll();
        List<CategoryDTO> listDto = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }  

    @GetMapping(value = "/{id}")
    public ResponseEntity<Category> findById(@PathVariable Integer id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok().body(category);
    }

    @GetMapping(value="/page")
    public ResponseEntity<Page<CategoryDTO>> findByPage(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
        @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
        @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<Category> list = categoryService.findByPage(page, linesPerPage, orderBy, direction);
        Page<CategoryDTO> listDto = list.map(x -> new CategoryDTO(x));
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody CategoryDTO categoryDto) {
        Category category = categoryService.fromDto(categoryDto);
        category = categoryService.insert(category);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(category.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value="/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @Valid @RequestBody CategoryDTO categoryDto) {
        Category category = categoryService.fromDto(categoryDto);
        category.setId(id);
        category = categoryService.update(category);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
