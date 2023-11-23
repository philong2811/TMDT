package hcmute.it.furnitureshop.Service;

import hcmute.it.furnitureshop.Entity.Category;
import hcmute.it.furnitureshop.Entity.Room;

import java.util.Optional;

public interface CategoryService {
    public Iterable<Category> getCategoriesByRoom(Integer roomId);
    public Iterable<Category> getAll();
    Optional<Category> findById(Integer categoryId);
}
