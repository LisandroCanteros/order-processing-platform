package com.example.inventoryservice.service;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public boolean reserveStock(String productId, Integer quantity) {
        log.info("Reserving stock {} qty {}", productId, quantity);

        Inventory inv = inventoryRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (inv.getQuantity() < quantity) {
            log.warn("Not enough stock for {}", productId);
            return false;
        }

        inv.setQuantity(inv.getQuantity() - quantity);
        inventoryRepository.save(inv);
        log.info("Stock reserved!");
        return true;
    }
}
