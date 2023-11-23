package hcmute.it.furnitureshop.Service.Impl;

import hcmute.it.furnitureshop.DTO.ProductDTO;
import hcmute.it.furnitureshop.DTO.ProductDetailDTO;
import hcmute.it.furnitureshop.Entity.Category;
import hcmute.it.furnitureshop.Entity.Product;
import hcmute.it.furnitureshop.Entity.Room;
import hcmute.it.furnitureshop.Repository.CategoryRepository;
import hcmute.it.furnitureshop.Repository.ProductRepository;
import hcmute.it.furnitureshop.Repository.RoomRepository;
import hcmute.it.furnitureshop.Service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Iterable<Product> getTop8Product() {
        return productRepository.findTop8ByProductSold();
    }

    @Override
    public Optional<Product> getProductById(Integer productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Iterable<Product> getProductByNameContaining(String name) {
        return productRepository.findProductsByNameContaining(name);
    }

    public  Iterable<Product> getProductsByCategory(Integer categoryId){
        Optional<Category> category=categoryRepository.findById(categoryId);
        return productRepository.findProductsByCategory(category.get());
    }

    @Override
    public Iterable<Product> getProductByCategoryAndPriceDesc(Category category) {
        return productRepository.findProductsByCategoryOrderByPriceDesc(category);
    }

    @Override
    public Iterable<Product> getProductByCategoryAndPriceAsc(Category category) {
        return productRepository.findProductsByCategoryOrderByPriceAsc(category);
    }

    @Override
    public Iterable<Product> findProductByRoomDesc(Integer roomId) {
        return productRepository.findProductsByCategory_Room_RoomIdOrderByPriceDesc(roomId);
    }

    @Override
    public Iterable<Product> findProductByRoomAsc(Integer roomId) {
        return productRepository.findProductsByCategory_Room_RoomIdOrderByPriceAsc(roomId);
    }

    @Override
    public Optional<Product> findById(Integer productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Iterable<Product> getAll() {
        return productRepository.findProductsByStatus("inactive");
    }

    @Override
    public Optional<Product> findProductByOrderId(Integer orderId) {
        return productRepository.findProductByOrder_OrderId(orderId);
    }

    @Override
    public <S extends Product> void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public Iterable<Product> getProductByRoom(Integer roomId) {
        Optional<Room> roomById= roomRepository.findById(roomId);
        Iterable<Category> categories=categoryRepository.findCategoriesByRoom(roomById);
        ArrayList<Product> products = new ArrayList<>();
        categories.forEach(category -> {
            products.addAll((Collection<? extends Product>) productRepository.findProductsByCategory(category));
        });
        return products;
    }

    @Override
    public Iterable<Product> getProductsByCategoryAndDiscount(Integer categoryId) {
        Iterable<Product> products= getProductsByCategory(categoryId);
        ArrayList<Product> productsHaveDisCount = new ArrayList<>();
        products.forEach(product -> {
            if(product.getDiscount()!=null){
                productsHaveDisCount.add(product);
            }
        });
        return productsHaveDisCount;
    }

    @Override
    public Iterable<Product> getProductSaleByRoom(Integer roomId) {
        Iterable<Product> products= productRepository.findProductsByCategory_Room_RoomId(roomId);
        ArrayList<Product> productsHaveDisCount = new ArrayList<>();
        products.forEach(product -> {
            if(product.getDiscount()!=null){
                productsHaveDisCount.add(product);
            }
        });
        return productsHaveDisCount;
    }
    public List<ProductDetailDTO> getAllProductsWithCategoryName() {
        List<ProductDetailDTO> detailDTOList = new ArrayList<>();
        productRepository.findAllProductsWithCategoryName().forEach(productJoinCate -> {
            ProductDetailDTO productDetail = ProductDetailDTO.builder()
                    .productId(productJoinCate.getProductId())
                    .name(productJoinCate.getName())
                    .image(productJoinCate.getImage())
                    .price(productJoinCate.getPrice())
                    .size(productJoinCate.getSize())
                    .categoryName(productJoinCate.getCategory().getName())
                    .numberProductSold(productJoinCate.getNumberProductSold())
                    .description(productJoinCate.getDescription())
                    .material(productJoinCate.getMaterial())
                    .quantity(productJoinCate.getQuantity())
                    .status(productJoinCate.getStatus())
                    .build();
            detailDTOList.add(productDetail);
        });
        return detailDTOList;
    }
    @Override
    public String updateProduct(ProductDetailDTO productDTO) {
        if(productRepository.findById(productDTO.getProductId()).isPresent())
        {
            return "Đã cập nhật sản phẩm thành công";
        }
        return "Không tồn tại sản phẩm trong hệ thống";
    }

    @Override
    public String deleteProduct(Integer productId) {
        if(productRepository.findById(productId).isPresent())
        {
            productRepository.deleteById(productId);
            return "Đã xóa sản phẩm thành công";
        }
        return "Không tồn tại sản phẩm trong hệ thống";
    }

    @Override
    public Product createProduct(ProductDetailDTO createProductDTO) {
        if(productRepository.findByName(createProductDTO.getName()).isEmpty())
        {
            var product = Product.builder()
                    .productId(createProductDTO.getProductId())
                    .name(createProductDTO.getName())
                    .Image(createProductDTO.getImage())
                    .price(createProductDTO.getPrice())
                    .size(createProductDTO.getSize())
                    .category(categoryRepository.findByName(createProductDTO.getCategoryName()))
                    .numberProductSold(createProductDTO.getNumberProductSold())
                    .description(createProductDTO.getDescription())
                    .material(createProductDTO.getMaterial())
                    .quantity(createProductDTO.getQuantity())
                    .status("active")
                    .build();
            productRepository.save(product);
            return product;
        }
        else return null;
    }
}
