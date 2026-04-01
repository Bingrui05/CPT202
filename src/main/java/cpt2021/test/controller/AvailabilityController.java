package cpt2021.test.controller;

import cpt2021.test.entity.AvailabilitySlot;
import cpt2021.test.repository.AvailabilitySlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/availability")
public class AvailabilityController {

    @Autowired
    private AvailabilitySlotRepository repository;

    @GetMapping
    public String listSlots(Model model) {
        // 从数据库抓取数据
        List<AvailabilitySlot> slots = repository.findAll();
        // 传给 HTML
        model.addAttribute("slots", slots);
        return "availability-list"; // 对应 templates 下的文件名
    }
}
