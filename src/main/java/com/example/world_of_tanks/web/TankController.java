package com.example.world_of_tanks.web;

import com.example.world_of_tanks.models.Tank;
import com.example.world_of_tanks.models.dto.*;
import com.example.world_of_tanks.services.TankService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class TankController {

    private final TankService tankService;

    public TankController(TankService tankService) {
        this.tankService = tankService;
    }

    @ModelAttribute("addTankDTO")
    public AddTankDTO addTankDTO() {
        return new AddTankDTO();
    }

    @ModelAttribute("editTankDTO")
    public EditTankDTO editTankDTO() {
        return new EditTankDTO();
    }

    @ModelAttribute("editUserTankDTO")
    public EditUserTankDTO editUserTankDTO() {
        return new EditUserTankDTO();
    }

    @ModelAttribute("deleteTankDTO")
    public DeleteTankDTO deleteTankDTO() {
        return new DeleteTankDTO();
    }

    @ModelAttribute("deleteUserTankDTO")
    public DeleteUserTankDTO deleteUserTankDTO() {
        return new DeleteUserTankDTO();
    }

    @ModelAttribute("searchTankDTO")
    public SearchTankDTO searchTankDTO() {
        return new SearchTankDTO();
    }

    @GetMapping("/tank/add")
    public String getAddTank() {
        return "tank-add";
    }

    @PostMapping("/tank/add")
    public String addTank(@Valid AddTankDTO addTankDTO,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addTankDTO", addTankDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addTankDTO", bindingResult);
            return "redirect:/tank/add";
        }

        tankService.addTank(addTankDTO, userDetails);
        return "redirect:/users/home";
    }

    @GetMapping("/tank/edit")
    public String getEditTank() {
        return "tank-edit";
    }

    @PostMapping("/tank/edit")
    public String editTank(@Valid EditTankDTO editTankDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors() || !tankService.editTank(editTankDTO)) {
            redirectAttributes.addFlashAttribute("editTankDTO", editTankDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editTankDTO", bindingResult);
            return "redirect:/tank/edit";
        }

        return "redirect:/users/home";
    }

    @GetMapping("/tanks/edit/{id}")
    public String getEditTankById(@PathVariable Long id, Model model, Principal principal) {
        Tank tank = tankService.getOwnedTankById(id, principal.getName());

        EditTankDTO editTankDTO = new EditTankDTO()
                .setId(tank.getId())
                .setName(tank.getName())
                .setHealth(tank.getHealth())
                .setPower(tank.getPower());

        model.addAttribute("editTankDTO", editTankDTO);
        return "tank-edit";
    }

    @PostMapping("/tanks/edit/{id}")
    public String processEditTank(@PathVariable Long id,
                                  @ModelAttribute("editTankDTO") @Valid EditTankDTO editTankDTO,
                                  BindingResult bindingResult,
                                  Principal principal) {

        if (bindingResult.hasErrors()) {
            return "tank-edit";
        }

        Tank tank = tankService.getOwnedTankById(id, principal.getName());
        tankService.updateTank(tank, editTankDTO);

        return "redirect:/tanks/info";
    }

    @GetMapping("/tank/delete")
    public String getDeleteTank() {
        return "tank-delete";
    }

    @PostMapping("/tank/delete")
    public String deleteTank(@Valid DeleteTankDTO deleteTankDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("deleteTankDTO", deleteTankDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.deleteTankDTO", bindingResult);
            return "redirect:/tank/delete";
        }

        tankService.deleteTank(deleteTankDTO);
        return "redirect:/users/home";
    }

    @PostMapping("/tanks/delete/{id}")
    public String deleteTankById(@PathVariable Long id,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        tankService.deleteTank(id, userDetails);
        return "redirect:/tanks/info";
    }

    @GetMapping("/user/tank/delete")
    public String getUserTankDelete() {
        return "user-role-tank-delete";
    }

    @PostMapping("/user/tank/delete")
    public String deleteUserTank(@Valid DeleteUserTankDTO deleteUserTankDTO,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors() || !tankService.deleteUserTank(deleteUserTankDTO, userDetails)) {
            redirectAttributes.addFlashAttribute("deleteUserTankDTO", deleteUserTankDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.deleteUserTankDTO", bindingResult);
            return "redirect:/user/tank/delete";
        }

        return "redirect:/users/home";
    }

    @GetMapping("/tanks/info")
    public String getAllTanks(Model model) {
        List<TankInfoDTO> tanks = tankService.findAllTanks();
        model.addAttribute("allTanks", tanks);
        return "tanks-info";
    }

    @GetMapping("/tanks/search")
    public String searchTankQuery(@ModelAttribute("searchTankDTO") SearchTankDTO searchTankDTO,
                                  BindingResult bindingResult,
                                  Model model) {

        if (!searchTankDTO.isEmpty()) {
            model.addAttribute("tanks", tankService.searchTanks(searchTankDTO));
        }

        return "tank-search";
    }

    @GetMapping("/user/role/tank/edit")
    public String getUserTankEdit() {
        return "user-tank-edit";
    }
}
