package hcmute.it.furnitureshop.DTO;

import hcmute.it.furnitureshop.Common.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer userId;
    private String name;
    private String phone;
    private String image;
    private String status;
    private String address;
    private String role;
}
