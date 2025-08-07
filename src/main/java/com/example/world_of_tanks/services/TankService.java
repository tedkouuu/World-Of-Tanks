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

        Optional<Tank> tankToDelete = this.tankRepository.findByName(deleteTankDTO.getName());

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

    public List<TankDTO> getTanksOwnedBy(String ownerUsername) {

        List<TankDTO> ownedTanks = new ArrayList<>();

        List<Tank> tanks = this.tankRepository.findByUserUsername(ownerUsername);

        for (Tank tank : tanks) {

            TankDTO tankDto = modelMapper.map(tank, TankDTO.class);

            ownedTanks.add(tankDto);
        }

        return ownedTanks;

    }


    public List<TankDTO> getTanksOwnedByNot(String noOwnerUsername) {

        List<TankDTO> enemyTanks = new ArrayList<>();

        List<Tank> tanks = this.tankRepository.findByUserUsernameNot(noOwnerUsername);

        for (Tank tank : tanks) {

            TankDTO tankDto = modelMapper.map(tank, TankDTO.class);
            enemyTanks.add(tankDto);
        }

        return enemyTanks;
    }


    public List<TankDTO> getAllSorted() {

        List<TankDTO> sortedTanks = new ArrayList<>();

        List<Tank> tanks = this.tankRepository.findByOrderByHealthDesc();

        for (Tank tank : tanks) {

            TankDTO tankDto = modelMapper.map(tank, TankDTO.class);

            sortedTanks.add(tankDto);
        }

        return sortedTanks;
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

    public List<TankInfoDTO> findAllTanks() {

        List<Tank> allTanks = this.tankRepository.findAll();

        List<TankInfoDTO> tanksToShow = new ArrayList<>();

        for (Tank current : allTanks) {

            Optional<UserEntity> byUsername = userRepository.findByUsername(current.getUser().getUsername());

            UserEntity userEntity = byUsername.get();

            TankInfoDTO tankDTO = new TankInfoDTO()
                    .setId(current.getId())
                    .setPower(current.getPower())
                    .setCreated(current.getCreated())
                    .setHealth(current.getHealth())
                    .setName(current.getName())
                    .setCategoryName(current.getCategory().getName())
                    .setOwnerUsername(userEntity.getUsername());

            tanksToShow.add(tankDTO);

        }

        return tanksToShow;
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

    public List<TankInfoDTO> searchTanks(SearchTankDTO searchTankDTO) {
        String name = searchTankDTO.getName();
        Integer health = searchTankDTO.getHealthAsInteger();
        Integer power = searchTankDTO.getPowerAsInteger();

        List<Tank> result = tankRepository.findAll().stream()
                .filter(tank -> {
                    boolean matches = true;

                    if (name != null && !name.trim().isEmpty()) {
                        matches = matches && tank.getName().toLowerCase().contains(name.trim().toLowerCase());
                    }

                    if (health != null) {
                        matches = matches && tank.getHealth() >= health;
                    }

                    if (power != null) {
                        matches = matches && tank.getPower() <= power;
                    }

                    return matches;
                })
                .toList();

        return result.stream()
                .map(this::mapToTankInfoDTO)
                .toList();


    }

    private TankInfoDTO mapToTankInfoDTO(Tank tank) {
        return new TankInfoDTO()
                .setId(tank.getId())
                .setName(tank.getName())
                .setHealth(tank.getHealth())
                .setPower(tank.getPower());
    }
}











