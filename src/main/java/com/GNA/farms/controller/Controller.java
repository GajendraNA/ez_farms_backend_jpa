package com.GNA.farms.controller;

import com.GNA.farms.dao.*;
import com.GNA.farms.dto.*;

import com.GNA.farms.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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
    OrderDao orderDao;

    @Autowired
    InventoryDao inventoryDao;

    @Autowired
    PaymentDao paymentDao;

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

    @GetMapping("farmer/{id}")
    public ResponseEntity<Farmer> getFarmerById(@PathVariable Long id){
        Farmer farmer=farmerDao.findById(id).orElseThrow(()->new RuntimeException("no user found"));
        System.out.println(farmer);
        return ResponseEntity.ok(farmer);
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








    @PostMapping("inventory")
    public ResponseEntity<String> uploadInventory(@RequestParam("file") MultipartFile file, @RequestParam("data") String inventoryData) {
        System.out.println("Hit inventory upload");

        try {
            System.out.println("Description: " + inventoryData);
            System.out.println("File: " + file.getOriginalFilename());

            ObjectMapper objectMapper = new ObjectMapper();
            InventoryRequestDto inventoryRequestDto = objectMapper.readValue(inventoryData, InventoryRequestDto.class);

            String uploadDirectory = new File("src/main/resources/static").getAbsolutePath();
            File uploadDir = new File(uploadDirectory);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String originalImgname = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, originalImgname);

            Files.write(filePath, file.getBytes());

            Farmer farmer = farmerDao.findById(inventoryRequestDto.getFarmerId()).orElseThrow(() -> new RuntimeException("Farmer not found"));

            Inventory inventorySaved = new Inventory();
            inventorySaved.setName(originalImgname);
            inventorySaved.setFarmer(farmer);
            inventorySaved.setDescription(inventoryRequestDto.getDescription());
            inventorySaved.setCategory(inventoryRequestDto.getCategory());
            inventorySaved.setItemName(inventoryRequestDto.getItemName());
            inventorySaved.setWeight(inventoryRequestDto.getWeight());
            inventorySaved.setRemaining_weight(inventoryRequestDto.getRemaining_weight());
            inventorySaved.setFinal_rate_per_kg(inventoryRequestDto.getFinal_rate_per_kg());

            inventoryDao.save(inventorySaved);

            return ResponseEntity.ok("File uploaded successfully: " + originalImgname);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON data: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Farmer not found: " + e.getMessage());
        }
    }
    @PutMapping("fInventory/{id}")
    public ResponseEntity<String> fUpdateInventory(@RequestParam("file") MultipartFile file, @RequestParam("data") String inventoryData,@PathVariable Long id) {
        System.out.println("Hit inventory upload");

        try {
            System.out.println("Description: " + inventoryData);
            System.out.println("File: " + file.getOriginalFilename());

            ObjectMapper objectMapper = new ObjectMapper();
            InventoryRequestDto inventoryRequestDto = objectMapper.readValue(inventoryData, InventoryRequestDto.class);

            String uploadDirectory = new File("src/main/resources/static").getAbsolutePath();
            File uploadDir = new File(uploadDirectory);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String originalImgname = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, originalImgname);

            Files.write(filePath, file.getBytes());

//            Farmer farmer = farmerDao.findById(inventoryRequestDto.getFarmerId()).orElseThrow(() -> new RuntimeException("Farmer not found"));

            Inventory inventorySaved = inventoryDao.findById(id).orElseThrow(()->new RuntimeException("No inventory found"));
            inventorySaved.setName(originalImgname);
            inventorySaved.setDescription(inventoryRequestDto.getDescription());
            inventorySaved.setCategory(inventoryRequestDto.getCategory());
            inventorySaved.setItemName(inventoryRequestDto.getItemName());
            inventorySaved.setWeight(inventoryRequestDto.getWeight());
            inventorySaved.setRemaining_weight(inventoryRequestDto.getRemaining_weight());
            inventorySaved.setFinal_rate_per_kg(inventoryRequestDto.getFinal_rate_per_kg());

            inventoryDao.save(inventorySaved);

            return ResponseEntity.ok("File updated successfully: " + originalImgname);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON data: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Farmer not found: " + e.getMessage());
        }
    }

    @PutMapping("bInventory/{id}")
    public ResponseEntity<Inventory> updateWeight (@RequestBody InventoryDto inventoryDto,@PathVariable Long id){
        Inventory inventory=inventoryDao.findById(id).orElseThrow(()->new RuntimeException("no inventory found"));
        inventory.setRemaining_weight(inventoryDto.getRemaining_weight());
        inventoryDao.save(inventory);
        return ResponseEntity.ok(inventory);
    }



    @GetMapping("inventory")
    public ResponseEntity<List<InventoryDto>> getAllInventories() {
        List<Inventory> inventoryList = inventoryDao.findAll();
        List<InventoryDto> inventoryDtoList = inventoryList.stream().map(inventory -> {
            InventoryDto dto = new InventoryDto();
            dto.setId(inventory.getId());
            dto.setName(inventory.getName());
            dto.setFarmer_id(inventory.getFarmer().getId());
            dto.setWeight(inventory.getWeight());
            dto.setRemaining_weight(inventory.getRemaining_weight());
            dto.setFinal_rate_per_kg(inventory.getFinal_rate_per_kg());
            dto.setFarmer_name(inventory.getFarmer().getName());
            dto.setItem_name(inventory.getItemName());
            dto.setItem_description(inventory.getDescription());
            dto.setItem_category(inventory.getCategory());
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
            dto.setWeight(inventory.getWeight());
            dto.setRemaining_weight(inventory.getRemaining_weight());
            dto.setFinal_rate_per_kg(inventory.getFinal_rate_per_kg());
            dto.setFarmer_name(inventory.getFarmer().getName());
            dto.setItem_name(inventory.getItemName());
            dto.setItem_description(inventory.getDescription());
            dto.setItem_category(inventory.getCategory());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(inventoryDtoList);
    }



    @GetMapping("inventory/{id}")
    public ResponseEntity<InventoryDto> getInventoryById(@PathVariable Long id) {
        Inventory inventory = inventoryDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        InventoryDto inventoryDto=new InventoryDto();
        inventoryDto.setName(inventory.getName());
        inventoryDto.setId(inventory.getId());
        inventoryDto.setFarmer_id(inventory.getFarmer().getId());
        inventoryDto.setWeight(inventory.getWeight());
        inventoryDto.setRemaining_weight(inventory.getRemaining_weight());
        inventoryDto.setFinal_rate_per_kg(inventory.getFinal_rate_per_kg());
        inventoryDto.setFarmer_name(inventory.getFarmer().getName());
        inventoryDto.setItem_name(inventory.getItemName());
        inventoryDto.setItem_description(inventory.getDescription());
        inventoryDto.setItem_category(inventory.getCategory());
        return ResponseEntity.ok(inventoryDto);
    }

    @DeleteMapping("inventory/{id}")
    public ResponseEntity<Inventory> deleteInventory(@PathVariable Long id) {
        Inventory inventory = inventoryDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        List<Order> orders=orderDao.findAllByInventoryId(id);

for (Order order:orders){
    Long i=order.getId();
    List<Payment> payments =paymentDao.findAllByOrderId(i);
    paymentDao.deleteAll(payments);
}orderDao.deleteAll(orders);
        inventoryDao.deleteById(id);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("orders")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto orderDto){
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
        List<Order> orders = orderDao.findAll();

        List<OrderDto> orderDtos = orders.stream().map(order -> {
            OrderDto dto = new OrderDto();
            dto.setId(order.getId());
            dto.setInventory_id(order.getInventory().getId());
            dto.setFarmer_id(order.getFarmer().getId());
            dto.setBuyer_id(order.getBuyer().getId());
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
            dto.setPicture(order.getInventory().getName());
            dto.setItem_rate(order.getInventory().getFinal_rate_per_kg());

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
            dto.setItem_name(order.getInventory().getItemName());
            dto.setItem_rate(order.getInventory().getFinal_rate_per_kg());
            dto.setPicture(order.getInventory().getName());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(orderDtos);

    }
    @GetMapping("bOrders/{id}")
    public ResponseEntity<List<OrderDto>> getAllOrdersByBuyerId(@PathVariable Long id){
        List<Order> orders=orderDao.findAllByBuyerId(id);
        System.out.println("Received request for buyerId: " + id);

        List<OrderDto> orderDtos = orders.stream().map(order -> {
            OrderDto dto = new OrderDto();
            dto.setId(order.getId());
            dto.setInventory_id(order.getInventory().getId());
            dto.setFarmer_id(order.getFarmer().getId());
            dto.setBuyer_id(order.getBuyer().getId());
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
            dto.setItem_name(order.getInventory().getItemName());
            dto.setItem_rate(order.getInventory().getFinal_rate_per_kg());
            dto.setPicture(order.getInventory().getName());
            return dto;
        }).collect(Collectors.toList());


        return ResponseEntity.ok(orderDtos);

    }

    @PutMapping("orders/{id}")
    public ResponseEntity<Order> updateOrder(@RequestBody OrderDto orderDto, @PathVariable Long id) {
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


    @PostMapping("payment")
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto){

        Buyer buyer=buyerDao.findById(paymentDto.getBuyer_id())
                .orElseThrow(()->new RuntimeException("no buyer found"));
        Farmer farmer=farmerDao.findById(paymentDto.getFarmer_id())
                .orElseThrow(()->new RuntimeException("no farmer found"));
        Order order=orderDao.findById(paymentDto.getOrder_id())
                .orElseThrow(()->new RuntimeException("no order found"));
        Payment payment=new Payment();
        payment.setBuyer(buyer);
        payment.setFarmer(farmer);
        payment.setOrder(order);
        payment.setMethod(paymentDto.getMethod());
        payment.setOrder_amount(order.getOrder_amount());
        payment.setFinal_amount(order.getFinal_amount());
        payment.setPayment_date(order.getEst_delivery_date());
        payment.setFinal_amount(paymentDto.getFinal_amount());
        payment.setPayment_amount(paymentDto.getPayment_amount());
        paymentDao.save(payment);
        return ResponseEntity.ok(paymentDto);
    }



    @GetMapping("fPayment/{id}")
    public ResponseEntity<List<PaymentDto>> getAllPaymentsByFarmerId(@PathVariable Long id){
        List<Payment> payments=paymentDao.findAllByFarmerId(id);
        List<PaymentDto> paymentDtos=payments.stream().map(payment -> {
            PaymentDto paymentDto=new PaymentDto();
            paymentDto.setOrder_id(payment.getOrder().getId());
            paymentDto.setBuyer_id(payment.getBuyer().getId());
            paymentDto.setFarmer_id(payment.getFarmer().getId());
            paymentDto.setPayment_date(payment.getPayment_date());
            paymentDto.setMethod(payment.getMethod());
            paymentDto.setOrder_amount(payment.getOrder_amount());
            paymentDto.setBuyerName(payment.getBuyer().getName());
            paymentDto.setFarmerName(payment.getFarmer().getName());

            paymentDto.setFinal_amount(payment.getFinal_amount());
            paymentDto.setPayment_amount(payment.getPayment_amount());
            return paymentDto;
        }).collect(Collectors.toList());
    return ResponseEntity.ok(paymentDtos);
    }

    @GetMapping("bPayment/{id}")
    public ResponseEntity<List<PaymentDto>> getAllPaymentsByBuyerId(@PathVariable Long id){
        List<Payment> payments=paymentDao.findAllByBuyerId(id);
        List<PaymentDto> paymentDtos=payments.stream().map(payment -> {
            PaymentDto paymentDto=new PaymentDto();
            paymentDto.setOrder_id(payment.getOrder().getId());
            paymentDto.setBuyer_id(payment.getBuyer().getId());
            paymentDto.setFarmer_id(payment.getFarmer().getId());
            paymentDto.setPayment_date(payment.getPayment_date());
            paymentDto.setMethod(payment.getMethod());
            paymentDto.setOrder_amount(payment.getOrder_amount());
            paymentDto.setFarmerName(payment.getFarmer().getName());
            paymentDto.setBuyerName(payment.getBuyer().getName());
            paymentDto.setFinal_amount(payment.getFinal_amount());
            paymentDto.setPayment_amount(payment.getPayment_amount());
            return paymentDto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(paymentDtos);
    }

    @GetMapping("payment")
    public ResponseEntity<List<PaymentDto>> getAllPayments(){
        List<Payment> payments=paymentDao.findAll();
        List<PaymentDto> paymentDtos=payments.stream().map(payment -> {
            PaymentDto paymentDto=new PaymentDto();
            paymentDto.setOrder_id(payment.getOrder().getId());
            paymentDto.setBuyer_id(payment.getBuyer().getId());
            paymentDto.setFarmer_id(payment.getFarmer().getId());
            paymentDto.setPayment_date(payment.getPayment_date());
            paymentDto.setMethod(payment.getMethod());
            paymentDto.setOrder_amount(payment.getOrder_amount());
            paymentDto.setFarmerName(payment.getFarmer().getName());
            paymentDto.setBuyerName(payment.getBuyer().getName());
            paymentDto.setFinal_amount(payment.getFinal_amount());
            paymentDto.setPayment_amount(payment.getPayment_amount());
            return paymentDto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(paymentDtos);

    }





}
