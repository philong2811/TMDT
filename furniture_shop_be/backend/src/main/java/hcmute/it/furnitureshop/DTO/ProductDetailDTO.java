package hcmute.it.furnitureshop.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDTO {
    private int productId;
    private String name;
    private long price;
    private long quantity;
    private String image;
    private String description;
    private String material;
    private int numberProductSold;
    private String size;
    private String status;
    private String categoryName;
    private Double percentDiscount;
    private String title;
}
