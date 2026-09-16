package dev.ilyas.commerce.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository products;

    public ProductController(ProductRepository products) { this.products = products; }

    @GetMapping
    public List<ProductResponse> all() {
        return products.findAll().stream().map(ProductResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ProductResponse one(@PathVariable UUID id) {
        return products.findById(id).map(ProductResponse::from)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody CreateProductRequest request) {
        products.findBySku(request.sku()).ifPresent(p -> { throw new DuplicateSkuException(request.sku()); });
        return ProductResponse.from(products.save(new Product(
                request.sku(), request.name(), request.description(), request.price())));
    }

    public record CreateProductRequest(
            @NotBlank @Size(max = 64) String sku,
            @NotBlank @Size(max = 160) String name,
            @NotBlank @Size(max = 1000) String description,
            @NotNull @DecimalMin("0.01") BigDecimal price) {}

    public record ProductResponse(UUID id, String sku, String name, String description,
                                  BigDecimal price, boolean active, Instant createdAt) {
        static ProductResponse from(Product p) {
            return new ProductResponse(p.getId(), p.getSku(), p.getName(), p.getDescription(),
                    p.getPrice(), p.isActive(), p.getCreatedAt());
        }
    }
}

class ProductNotFoundException extends RuntimeException {
    ProductNotFoundException(UUID id) { super("Product not found: " + id); }
}

class DuplicateSkuException extends RuntimeException {
    DuplicateSkuException(String sku) { super("SKU already exists: " + sku); }
}
