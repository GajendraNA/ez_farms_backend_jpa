package com.GNA.farms.controller;

import com.GNA.farms.dao.*;
import com.GNA.farms.dto.*;

import com.GNA.farms.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api")
@CrossOrigin("*")
public class Controller {
    @Autowired
    BuyerDao buyerDao;
    @Autowired
    DiscountDao discountDao;

    @Autowired
    FarmerDao farmerDao;

    @Autowired
    ItemsDao itemsDao;
    @Autowired
    OrderDao orderDao;

    @Autowired
    InventoryDao inventoryDao;

    @PostMapping("/farmerLogin")
    public ResponseEntity<Farmer> verifyFarmer(@RequestBody LoginDto verificationDto) {
        Farmer farmer = farmerDao.findByEmailAndPassword(verificationDto.getEmail(), verificationDto.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        return ResponseEntity.ok(farmer);
    }
    @PostMapping("/buyerLogin")
    public ResponseEntity<Buyer> verifyCustomer(@RequestBody LoginDto verificationDto) {
        Buyer buyer = buyerDao.findByEmailAndPassword(verificationDto.getEmail(), verificationDto.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        return ResponseEntity.ok(buyer);
    }



    @PostMapping("buyer")
    public ResponseEntity<Buyer> createBuyer(@RequestBody Buyer buyer){
        buyerDao.save(buyer);
        return ResponseEntity.ok(buyer);
    }

    @GetMapping("buyer")
    public ResponseEntity<List<Buyer>>  getAllBuyer(){
        List<Buyer> buyerList=buyerDao.findAll();
        return ResponseEntity.ok(buyerList);

    }
    @PutMapping("buyer/{id}")
    public ResponseEntity<Buyer> updateBuyer(@RequestBody Buyer buyer, @PathVariable Long id){
        Buyer exBuyer=buyerDao.findById(id)
                .orElseThrow(()->new RuntimeException("no buyer found"));
        exBuyer.setAddress(buyer.getAddress());
        exBuyer.setName(buyer.getName());
        exBuyer.setPhone(buyer.getPhone());
        exBuyer.setEmail(buyer.getEmail());
    exBuyer.setPassword(buyer.getPassword());
    buyerDao.save(exBuyer);
    return
ResponseEntity.ok(exBuyer);
    }

    @DeleteMapping("buyer/{id}")
    public ResponseEntity<Buyer> deleteBuyer(@PathVariable Long id){
        Buyer exBuyer=buyerDao.findById(id)
                .orElseThrow(()->new RuntimeException("no buyer found"));
        buyerDao.deleteById(id);
        return ResponseEntity.ok(exBuyer);
    }

    public ResponseEntity<Discount> createDiscount(@RequestBody DiscountDto discountDto){
        Buyer buyer=buyerDao.findById(discountDto.getBuyer_id())
                .orElseThrow(()->new RuntimeException("no buyer found"));
        Discount discount=new Discount();
        discount.setBuyer(buyer);
        discount.setDiscount_percent(discount.getDiscount_percent());
        discount.setOrder_count(discount.getOrder_count());
        return ResponseEntity.ok(discount);
    }

    @PostMapping("farmer")
    public ResponseEntity<Farmer> createFarmer(@RequestBody Farmer farmer){
        farmerDao.save(farmer);
        return ResponseEntity.ok(farmer);
    }

    @GetMapping("farmer")
    public ResponseEntity<List<Farmer>> getAllFarmers(){
        List<Farmer> farmerList =farmerDao.findAll();
        return ResponseEntity.ok(farmerList);
    }

    @PutMapping("farmer/{id}")
    public ResponseEntity<Farmer> updateFarmer(@RequestBody Farmer farmer,@PathVariable Long id){
        Farmer farmer1=farmerDao.findById(id)
                .orElseThrow(()->new RuntimeException("no farmer found"));
        farmer1.setAddress(farmer.getAddress());
        farmer1.setName(farmer.getName());
        farmer1.setPhone(farmer.getPhone());
        farmer1.setEmail(farmer.getEmail());
        farmer1.setPassword(farmer.getPassword());
        farmerDao.save(farmer1);
        return  ResponseEntity.ok(farmer1);
    }

    @DeleteMapping("farmer/{id}")
    public ResponseEntity<Farmer> deleteFarmer(@PathVariable Long id){
        Farmer farmer1=farmerDao.findById(id)
                .orElseThrow(()->new RuntimeException("no farmer found"));
        farmerDao.deleteById(id);
        return ResponseEntity.ok(farmer1);
    }





        @PostMapping("items")
        public ResponseEntity<Items> createItem(@RequestBody Items item) {
            itemsDao.save(item);
            return ResponseEntity.ok(item);
        }

        @GetMapping("items")
        public ResponseEntity<List<Items>> getAllItems() {
            List<Items> itemsList = itemsDao.findAll();
            return ResponseEntity.ok(itemsList);
        }

        @GetMapping("items/{id}")
        public ResponseEntity<Items> getItemById(@PathVariable Long id) {
            Items item = itemsDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            return ResponseEntity.ok(item);
        }

        @PutMapping("items/{id}")
        public ResponseEntity<Items> updateItem(@RequestBody Items item, @PathVariable Long id) {
            Items existingItem = itemsDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            existingItem.setName(item.getName());
            existingItem.setDescription(item.getDescription());
            existingItem.setRate_per_kg(item.getRate_per_kg());
            existingItem.setCategory(item.getCategory());
            itemsDao.save(existingItem);
            return ResponseEntity.ok(existingItem);
        }

        @DeleteMapping("items/{id}")
        public ResponseEntity<Items> deleteItem(@PathVariable Long id) {
            Items existingItem = itemsDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            itemsDao.deleteById(id);
            return ResponseEntity.ok(existingItem);
        }


    @PostMapping("inventory/upload")
    public String uploadTicket(@RequestParam("file") MultipartFile file, @RequestParam("data") String inventoryData) throws JsonProcessingException {
        System.out.println("Hit inventory upload");

        System.out.println("Description: " + inventoryData);
        System.out.println("Description: " + file.getOriginalFilename());

        ObjectMapper objectMapper = new ObjectMapper();
        InventoryRequestDto inventoryRequestDtoDto = objectMapper.readValue(inventoryData, InventoryRequestDto.class);
        System.out.println("Hit inventory upload");
        String UPLOAD_DIRECTORY = new File("src/main/resources/static").getAbsolutePath();

        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        try {
            // Get the original filename
            String originalImgname = file.getOriginalFilename();
            // Define the path where the file will be saved
            Path filePath = Paths.get(UPLOAD_DIRECTORY, originalImgname);
            // Save the file
            Files.write(filePath, file.getBytes());


            System.out.println("Description: " + inventoryRequestDtoDto);
            Farmer farmer = farmerDao.findById(inventoryRequestDtoDto.getFarmerId()).orElseThrow(() -> new RuntimeException("no user found"));
            Items items = itemsDao.findById(inventoryRequestDtoDto.getItemId()).orElseThrow(() -> new RuntimeException("no item found"));

            Inventory inventorySaved = new Inventory();
            inventorySaved.setName(file.getOriginalFilename());
            inventorySaved.setFarmer(farmer);
            inventorySaved.setItem(items);
            inventorySaved.setWeight(inventoryRequestDtoDto.getWeight());
            inventorySaved.setRemaining_weight(inventoryRequestDtoDto.getRemaining_weight());
            inventorySaved.setFinal_rate_per_kg(inventoryRequestDtoDto.getFinal_rate_per_kg());
            inventoryDao.save(inventorySaved);

            return "File uploaded successfully: " + originalImgname;
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
            return "File upload failed: " + e.getMessage();
        }
    }


    @GetMapping("inventory")
    public ResponseEntity<List<InventoryDto>> getAllInventories() {
        List<Inventory> inventoryList = inventoryDao.findAll();
        List<InventoryDto> inventoryDtoList = inventoryList.stream().map(inventory -> {
            InventoryDto dto = new InventoryDto();
            dto.setId(inventory.getId());
            dto.setName(inventory.getName());
            dto.setFarmer_id(inventory.getFarmer().getId());
            dto.setItem_id(inventory.getItem().getId());
            dto.setWeight(inventory.getWeight());
            dto.setRemaining_weight(inventory.getRemaining_weight());
            dto.setFinal_rate_per_kg(inventory.getFinal_rate_per_kg());
            dto.setFarmer_name(inventory.getFarmer().getName());
            dto.setItem_name(inventory.getItem().getName());
            dto.setItem_description(inventory.getItem().getDescription());
            dto.setItem_category(inventory.getItem().getCategory());
            return dto;
        }).collect(Collectors.toList());
        System.out.println(inventoryDtoList);
        return ResponseEntity.ok(inventoryDtoList);
    }

    @GetMapping("fInventory/{id}")
    public ResponseEntity<List<InventoryDto>> getAllInventoriesByFarmerId(@PathVariable Long id){
        List<Inventory> inventoryList = inventoryDao.findAllByFarmerId(id);
        List<InventoryDto> inventoryDtoList = inventoryList.stream().map(inventory -> {
            InventoryDto dto = new InventoryDto();
            dto.setId(inventory.getId());
            dto.setName(inventory.getName());
            dto.setFarmer_id(inventory.getFarmer().getId());
            dto.setItem_id(inventory.getItem().getId());
            dto.setWeight(inventory.getWeight());
            dto.setRemaining_weight(inventory.getRemaining_weight());
            dto.setFinal_rate_per_kg(inventory.getFinal_rate_per_kg());
            dto.setFarmer_name(inventory.getFarmer().getName());
            dto.setItem_name(inventory.getItem().getName());
            dto.setItem_description(inventory.getItem().getDescription());
            dto.setItem_category(inventory.getItem().getCategory());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(inventoryDtoList);
    }

    @GetMapping("inventory/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        Inventory inventory = inventoryDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("inventory/{id}")
    public ResponseEntity<Inventory> updateInventory(@RequestBody InventoryDto inventoryDto, @PathVariable Long id) {
        Farmer farmer = farmerDao.findById(inventoryDto.getFarmer_id())
                .orElseThrow(() -> new RuntimeException("Farmer not found"));
        Items item = itemsDao.findById(inventoryDto.getItem_id())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Inventory inventory = inventoryDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setName(inventoryDto.getName());
        inventory.setFarmer(farmer);
        inventory.setItem(item);
        inventory.setWeight(inventoryDto.getWeight());
        inventory.setRemaining_weight(inventoryDto.getRemaining_weight());
        inventory.setFinal_rate_per_kg(inventoryDto.getFinal_rate_per_kg());

        inventoryDao.save(inventory);
        return ResponseEntity.ok(inventory);
    }

    @DeleteMapping("inventory/{id}")
    public ResponseEntity<Inventory> deleteInventory(@PathVariable Long id) {
        Inventory inventory = inventoryDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        inventoryDao.deleteById(id);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("orders")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto orderDto){
        Items existingItem = itemsDao.findById(orderDto.getItem_id())
                .orElseThrow(() -> new RuntimeException("Item not found"));
        Buyer buyer=buyerDao.findById(orderDto.getBuyer_id())
                .orElseThrow(()->new RuntimeException("no buyer found"));
        Farmer farmer=farmerDao.findById(orderDto.getFarmer_id())
                .orElseThrow(()->new RuntimeException("no farmer found"));
        Inventory inventory = inventoryDao.findById(orderDto.getInventory_id())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        Order order = new Order();
        order.setInventory(inventory);
        order.setFarmer(farmer);
        order.setBuyer(buyer);
        order.setItems(existingItem);
        order.setWeight(orderDto.getWeight());
        order.setOrdered_date(orderDto.getOrdered_date());
        order.setEst_delivery_date(orderDto.getEst_delivery_date());
        order.setOrder_amount(orderDto.getOrder_amount());
        order.setDiscount_applied(orderDto.getDiscount_applied());
        order.setAmount_pending(orderDto.getAmount_pending());
        order.setFinal_amount(orderDto.getFinal_amount());
        order.setStatus(orderDto.getStatus());
        orderDao.save(order);
        return ResponseEntity.ok(order);
    }

    @GetMapping("orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<Order> orders = orderDao.findAllWithDetails(); // Custom method to fetch with JOIN FETCH

        List<OrderDto> orderDtos = orders.stream().map(order -> {
            OrderDto dto = new OrderDto();
            dto.setId(order.getId());
            dto.setInventory_id(order.getInventory().getId());
            dto.setFarmer_id(order.getFarmer().getId());
            dto.setBuyer_id(order.getBuyer().getId());
            dto.setItem_id(order.getItems().getId());
            dto.setWeight(order.getWeight());
            dto.setOrdered_date(order.getOrdered_date());
            dto.setEst_delivery_date(order.getEst_delivery_date()); // Ensure getEst_delivery_date() returns non-null value
            dto.setOrder_amount(order.getOrder_amount()); // Ensure getOrder_amount() returns non-null value
            dto.setDiscount_applied(order.getDiscount_applied()); // Ensure getDiscount_applied() returns non-null value
            dto.setAmount_pending(order.getAmount_pending()); // Ensure getAmount_pending() returns non-null value
            dto.setFinal_amount(order.getFinal_amount()); // Ensure getFinal_amount() returns non-null value
            dto.setStatus(order.getStatus()); // Ensure getStatus() returns non-null value
            dto.setFarmer_name(order.getFarmer().getName());
            dto.setInventory_name(order.getInventory().getName());
            dto.setBuyer_name(order.getBuyer().getName());
            dto.setItem_name(order.getItems().getName());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("fOrders/{id}")
    public ResponseEntity<List<OrderDto>> getAllOrdersByFarmerId(@PathVariable Long id){
        List<Order> orders=orderDao.findAllByFarmerId(id);
        List<OrderDto> orderDtos = orders.stream().map(order -> {
            OrderDto dto = new OrderDto();
            dto.setId(order.getId());
            dto.setInventory_id(order.getInventory().getId());
            dto.setFarmer_id(order.getFarmer().getId());
            dto.setBuyer_id(order.getBuyer().getId());
            dto.setItem_id(order.getItems().getId());
            dto.setWeight(order.getWeight());
            dto.setOrdered_date(order.getOrdered_date()); // Ensure getOrdered_date() returns non-null value
            dto.setEst_delivery_date(order.getEst_delivery_date()); // Ensure getEst_delivery_date() returns non-null value
            dto.setOrder_amount(order.getOrder_amount()); // Ensure getOrder_amount() returns non-null value
            dto.setDiscount_applied(order.getDiscount_applied()); // Ensure getDiscount_applied() returns non-null value
            dto.setAmount_pending(order.getAmount_pending()); // Ensure getAmount_pending() returns non-null value
            dto.setFinal_amount(order.getFinal_amount()); // Ensure getFinal_amount() returns non-null value
            dto.setStatus(order.getStatus()); // Ensure getStatus() returns non-null value
            dto.setFarmer_name(order.getFarmer().getName());
            dto.setInventory_name(order.getInventory().getName());
            dto.setBuyer_name(order.getBuyer().getName());
            dto.setItem_name(order.getItems().getName());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(orderDtos);

    }
    @GetMapping("bOrders/{id}")
    public ResponseEntity<List<OrderDto>> getAllOrdersByBuyerId(@PathVariable Long id){
        List<Order> orders=orderDao.findAllByBuyerId(id);
        List<OrderDto> orderDtos = orders.stream().map(order -> {
            OrderDto dto = new OrderDto();
            dto.setId(order.getId());
            dto.setInventory_id(order.getInventory().getId());
            dto.setFarmer_id(order.getFarmer().getId());
            dto.setBuyer_id(order.getBuyer().getId());
            dto.setItem_id(order.getItems().getId());
            dto.setWeight(order.getWeight());
            dto.setOrdered_date(order.getOrdered_date()); // Ensure getOrdered_date() returns non-null value
            dto.setEst_delivery_date(order.getEst_delivery_date()); // Ensure getEst_delivery_date() returns non-null value
            dto.setOrder_amount(order.getOrder_amount()); // Ensure getOrder_amount() returns non-null value
            dto.setDiscount_applied(order.getDiscount_applied()); // Ensure getDiscount_applied() returns non-null value
            dto.setAmount_pending(order.getAmount_pending()); // Ensure getAmount_pending() returns non-null value
            dto.setFinal_amount(order.getFinal_amount()); // Ensure getFinal_amount() returns non-null value
            dto.setStatus(order.getStatus()); // Ensure getStatus() returns non-null value
            dto.setFarmer_name(order.getFarmer().getName());
            dto.setInventory_name(order.getInventory().getName());
            dto.setBuyer_name(order.getBuyer().getName());
            dto.setItem_name(order.getItems().getName());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(orderDtos);

    }

    @PutMapping("orders/{id}")
    public ResponseEntity<Order> updateOrder(@RequestBody OrderDto orderDto, @PathVariable Long id) {
        Items existingItem = itemsDao.findById(orderDto.getItem_id())
                .orElseThrow(() -> new RuntimeException("Item not found"));
        Inventory inventory = inventoryDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        Buyer buyer=buyerDao.findById(orderDto.getBuyer_id())
                .orElseThrow(()->new RuntimeException("no buyer found"));
        Farmer farmer=farmerDao.findById(orderDto.getFarmer_id())
                .orElseThrow(()->new RuntimeException("no farmer found"));
        Order order = orderDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setFarmer(farmer);
        order.setBuyer(buyer);
        order.setItems(existingItem);
        order.setInventory(inventory);

        order.setWeight(order.getWeight());
        order.setOrdered_date(order.getOrdered_date());
        order.setEst_delivery_date(order.getEst_delivery_date());
        order.setOrder_amount(order.getOrder_amount());
        order.setDiscount_applied(order.getDiscount_applied());
        order.setAmount_pending(order.getAmount_pending());
        order.setFinal_amount(order.getFinal_amount());
        order.setStatus(order.getStatus());
        orderDao.save(order);
        return ResponseEntity.ok(order);
    }




    @DeleteMapping("orders/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable Long id) {
        Order existingOrder = orderDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderDao.deleteById(id);
        return ResponseEntity.ok(existingOrder);
    }








}
