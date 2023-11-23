package hcmute.it.furnitureshop.Service.Impl;

import hcmute.it.furnitureshop.DTO.CategoryDTO;
import hcmute.it.furnitureshop.Entity.Category;
import hcmute.it.furnitureshop.Entity.Room;
import hcmute.it.furnitureshop.Repository.CategoryRepository;
import hcmute.it.furnitureshop.Repository.RoomRepository;
import hcmute.it.furnitureshop.Service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository ;
    @Autowired
    RoomRepository roomRepository;


    @Override
    public Iterable<Category> getCategoriesByRoom(Integer roomId) {
        Optional<Room> room=roomRepository.findById(roomId);
        return categoryRepository.findCategoriesByRoom(room);
    }

    @Override
    public Iterable<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Integer categoryId){
        return categoryRepository.findById(categoryId);
    }
}
