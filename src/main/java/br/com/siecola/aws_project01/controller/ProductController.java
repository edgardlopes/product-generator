package br.com.siecola.aws_project01.controller;

import br.com.siecola.aws_project01.enuns.EventType;
import br.com.siecola.aws_project01.model.Product;
import br.com.siecola.aws_project01.repository.ProductRepository;
import br.com.siecola.aws_project01.services.ProductPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductPublisher publisher;

    @GetMapping
    public Iterable<Product> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable long id) {
        Optional<Product> product = repository.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> save(@RequestBody Product product) {
        Product created = repository.save(product);

        publisher.publishProductEvent(created, EventType.PRODUCT_CREATED, "cloe");

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@RequestBody Product product, @PathVariable long id) {
        if(!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        product.setId(id);
        repository.save(product);

        publisher.publishProductEvent(product, EventType.PRODUCT_UPDATED, "edgard");


        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        if(!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Product product = repository.findById(id).get();

        repository.delete(product);

        publisher.publishProductEvent(product, EventType.PRODUCT_DELETED, "jennifer");


        return ResponseEntity.noContent().build();
    }
    @GetMapping("/bycode")
    public ResponseEntity<Product> findById(@RequestParam String code) {
        Optional<Product> product = repository.findByCode(code);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


}
