package hcmute.it.furnitureshop.Service;

import hcmute.it.furnitureshop.DTO.ProductDTO;
import hcmute.it.furnitureshop.DTO.ProductDetailDTO;
import hcmute.it.furnitureshop.Entity.Category;
import hcmute.it.furnitureshop.Entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public Iterable<Product> getTop8Product();
    public Optional<Product> getProductById(Integer productId);
    public Iterable<Product> getProductByNameContaining(String name);
    Iterable<Product> getProductsByCategory(Integer categoryId);
    Iterable<Product> getProductByCategoryAndPriceDesc(Category category);
    Iterable<Product> getProductByCategoryAndPriceAsc(Category category);
    Iterable<Product> findProductByRoomDesc(Integer roomId);
    Iterable<Product> findProductByRoomAsc(Integer roomId);
    Optional<Product> findById(Integer productId);
    Iterable<Product> getAll();
    Optional<Product> findProductByOrderId(Integer orderId);
    public <S extends Product> void save(Product product);
    public List<ProductDetailDTO> getAllProductsWithCategoryName();
    public Iterable<Product> getProductByRoom(Integer roomId);
    public Iterable<Product> getProductsByCategoryAndDiscount(Integer categoryId);
    public Iterable<Product> getProductSaleByRoom(Integer roomId);
    String updateProduct(ProductDetailDTO productDTO);
    String deleteProduct(Integer productId);
    Product createProduct(ProductDetailDTO createProductDTO);
}
