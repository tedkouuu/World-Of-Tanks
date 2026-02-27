package com.example.world_of_tanks.services;

import com.example.world_of_tanks.models.Category;
import com.example.world_of_tanks.models.Tank;
import com.example.world_of_tanks.models.UserEntity;
import com.example.world_of_tanks.models.dto.*;
import com.example.world_of_tanks.models.enums.CategoryEnum;
import com.example.world_of_tanks.mongoDbService.TankLogService;
import com.example.world_of_tanks.repositories.CategoryRepository;
import com.example.world_of_tanks.repositories.TankRepository;
import com.example.world_of_tanks.repositories.TankSpecification;
import com.example.world_of_tanks.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TankService {

    private final TankRepository tankRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public TankService(TankRepository tankRepository, CategoryRepository categoryRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.tankRepository = tankRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public void addTank(AddTankDTO addTankDTO, UserDetails userDetails) {

        CategoryEnum categoryEnum = addTankDTO.getCategory();

        Category category = this.categoryRepository.findByName(categoryEnum);

        UserEntity user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        Tank tankModelMapper = modelMapper.map(addTankDTO, Tank.class);

        tankModelMapper.setUser(user);

        tankModelMapper.setCategory(category);

        Tank saved = this.tankRepository.save(tankModelMapper);

        TankLogService.log("CREATE", saved);

    }

    public boolean editTank(EditTankDTO editTankDTO) {
        Optional<Tank> tankOpt = this.tankRepository.findById(editTankDTO.getId());

        if (tankOpt.isEmpty()) {
            return false;
        }

        Tank tankToEdit = tankOpt.get();

        tankToEdit.setHealth(editTankDTO.getHealth())
                .setPower(editTankDTO.getPower())
                .setName(editTankDTO.getName()); // ако позволяваш промяна на името

        Tank saved = this.tankRepository.save(tankToEdit);

        TankLogService.log("EDIT", saved);

        return true;
    }

    public Tank getOwnedTankById(Long id, String username) {
        Tank tank = tankRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tank not found"));

        if (!tank.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not the owner of this tank.");
        }

        return tank;
    }

    public void updateTank(Tank tank, EditTankDTO dto) {
        tank.setName(dto.getName());
        tank.setPower(dto.getPower());
        tank.setHealth(dto.getHealth());

        Tank saved = tankRepository.save(tank);

        TankLogService.log("EDIT", saved);
    }


    public void deleteTank(DeleteTankDTO deleteTankDTO) {

        Optional<Tank> tankToDelete  = this.tankRepository.findByName(deleteTankDTO.getName());

        Tank tankToEdit = tankToDelete.get();

        TankLogService.log("DELETE", tankToDelete.get());
        tankRepository.delete(tankToEdit);
    }

    public void deleteTank(Long id, UserDetails userDetails) {
        Tank tank = tankRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tank not found with id: " + id));

        String currentUsername = userDetails.getUsername();

        if (!tank.getUser().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("You are not the owner of this tank.");
        }

        TankLogService.log("DELETE", tank);
        tankRepository.delete(tank);
    }

    @Transactional(readOnly = true)
    public List<TankDTO> getTanksOwnedBy(String ownerUsername) {

        return this.tankRepository.findByUserUsername(ownerUsername).stream()
                .map(tank -> modelMapper.map(tank, TankDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TankDTO> getTanksOwnedByNot(String noOwnerUsername) {

        return this.tankRepository.findByUserUsernameNot(noOwnerUsername).stream()
                .map(tank -> modelMapper.map(tank, TankDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TankDTO> getAllSorted() {

        return this.tankRepository.findByOrderByHealthDesc().stream()
                .map(tank -> modelMapper.map(tank, TankDTO.class))
                .toList();
    }

    public List<Tank> findAll() {

        return this.tankRepository.findAll();
    }

    public void save(Tank tank) {

        this.tankRepository.save(tank);
    }

    public boolean deleteUserTank(DeleteUserTankDTO deleteUserTankDTO, UserDetails userDetails) {

        List<Tank> allUserTanks = this.tankRepository.findByUserUsername(userDetails.getUsername());

        Optional<Tank> tank = this.tankRepository.findByName(deleteUserTankDTO.getName());

        if (allUserTanks.isEmpty() || tank.isEmpty()) {

            return false;
        }

        if (!allUserTanks.contains(tank.get())) {

            return false;
        }


        this.tankRepository.delete(tank.get());

        return true;

    }

    public void deleteAllTUserTanks(UserDetails userDetails) {

        List<Tank> allUserTanks = this.tankRepository.findByUserUsername(userDetails.getUsername());

        if (allUserTanks.isEmpty()) {
            return;
        }

        this.tankRepository.deleteAll(allUserTanks);

    }

    @Transactional(readOnly = true)
    public List<TankInfoDTO> findAllTanks() {

        return this.tankRepository.findAll().stream()
                .map(this::mapToTankInfoDTOFull)
                .toList();
    }

    private TankInfoDTO mapToTankInfoDTOFull(Tank tank) {
        return new TankInfoDTO()
                .setId(tank.getId())
                .setPower(tank.getPower())
                .setCreated(tank.getCreated())
                .setHealth(tank.getHealth())
                .setName(tank.getName())
                .setCategoryName(tank.getCategory() != null ? tank.getCategory().getName() : null)
                .setOwnerUsername(tank.getUser() != null ? tank.getUser().getUsername() : null);
    }

    public TankDTO getTankById(Long id) {

        Optional<Tank> tank = this.tankRepository.findById(id);

        if (tank.isEmpty()) {
            return null;
        }

        Tank realTank = tank.get();

        return new TankDTO().setName(realTank.getName())
                .setHealth(realTank.getHealth()).setPower(realTank.getPower())
                .setCategory(realTank.getCategory()).setUser(realTank.getUser());
    }

    @Transactional(readOnly = true)
    public List<TankInfoDTO> searchTanks(SearchTankDTO searchTankDTO) {
        TankSpecification spec = new TankSpecification(searchTankDTO);

        return tankRepository.findAll(spec).stream()
                .map(this::mapToTankInfoDTOFull)
                .toList();
    }

}











