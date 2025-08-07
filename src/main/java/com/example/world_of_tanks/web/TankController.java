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
    public AddTankDTO initTankAddModel() {
        return new AddTankDTO();
    }

    @ModelAttribute("editTankDTO")
    public EditTankDTO editTankAddModel() {
        return new EditTankDTO();
    }

    @ModelAttribute("editUserTankDTO")
    public EditUserTankDTO editUserTankAddModel() {
        return new EditUserTankDTO();
    }

    @ModelAttribute("deleteTankDTO")
    public DeleteTankDTO deleteTankAddModel() {
        return new DeleteTankDTO();
    }

    @ModelAttribute("deleteUserTankDTO")
    public DeleteUserTankDTO deleteOwnedTank() {
        return new DeleteUserTankDTO();
    }

    @ModelAttribute("searchTankDTO")
    public SearchTankDTO searchTankDTO() {
        return new SearchTankDTO();
    }

    @GetMapping("/tank/add")
    public String getTank() {
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
    public String getEdit() {
        return "tank-edit";
    }

    @PostMapping("/tank/edit")
    public String edit(@Valid EditTankDTO editTankDTO,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors() || !tankService.editTank(editTankDTO)) {
            redirectAttributes.addFlashAttribute("editTankDTO", editTankDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editTankDTO", bindingResult);
            return "redirect:/tank/edit";
        }

        return "redirect:/users/home";
    }

    @GetMapping("/user/role/tank/edit")
    public String getUserTankEdit() {
        return "user-tank-edit";
    }

    @GetMapping("/tanks/edit/{id}")
    public String editTankForm(@PathVariable Long id, Model model, Principal principal) {
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
    public String getDelete() {
        return "tank-delete";
    }

    @PostMapping("/tank/delete")
    public String delete(@Valid DeleteTankDTO deleteTankDTO,
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

    @GetMapping("/user/tank/delete")
    public String getUserDelete() {
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

        tankService.deleteUserTank(deleteUserTankDTO, userDetails);
        return "redirect:/users/home";
    }

    @GetMapping("/tanks/delete/all")
    public String getDeleteAll() {
        return "delete-all-tanks";
    }

    @PostMapping("/tanks/delete/all")
    public String deleteAllTanks(@AuthenticationPrincipal UserDetails userDetails) {
        tankService.deleteAllTUserTanks(userDetails);
        return "redirect:/tank/add";
    }

    @PostMapping("/tanks/delete/{id}")
    public String deleteById(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails user) {
        tankService.deleteTank(id, user);
        return "redirect:/tanks/info";
    }

    @GetMapping("/tanks/info")
    public String showTanks(Model model) {
        List<TankInfoDTO> allTanks = tankService.findAllTanks();
        model.addAttribute("allTanks", allTanks);
        return "tanks-info";
    }

    @GetMapping("/tanks/search")
    public String searchTankQuery(@Valid SearchTankDTO searchTankDTO,
                                  BindingResult bindingResult,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("searchTankDTO", searchTankDTO);
            model.addAttribute("org.springframework.validation.BindingResult.searchTankDTO", bindingResult);
            return "tank-search";
        }

        if (!model.containsAttribute("searchTankDTO")) {
            model.addAttribute("searchTankDTO", searchTankDTO);
        }

        if (!searchTankDTO.isEmpty()) {
            model.addAttribute("tanks", tankService.searchTanks(searchTankDTO));
        }

        return "tank-search";
    }
}
