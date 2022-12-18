package com.edu.graduationproject.controller.rest;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.edu.graduationproject.entity.Voucher;
import com.edu.graduationproject.service.VoucherService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@Slf4j
@RestController
public class VoucherRestController {

    @Autowired
    VoucherService voucherService;

    @GetMapping("/rest/vouchers")
    public ResponseEntity<List<Voucher>> getAll() {
        return ResponseEntity.ok(voucherService.findAll());
    }

    @GetMapping("/rest/vouchers/id/{id}")
    public ResponseEntity<Voucher> findById(@PathVariable Integer id) {
        Optional<Voucher> findVoucher = voucherService.findById(id);
        if (findVoucher.isPresent()) {
            return ResponseEntity.ok(findVoucher.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/rest/vouchers/code/{code}")
    public ResponseEntity<Voucher> findByCode(@PathVariable String code) {
        Optional<Voucher> findVoucher = voucherService.findByCodeIsNotDeleted(code);
        if (findVoucher.isPresent()) {
            return ResponseEntity.ok(findVoucher.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/rest/vouchers")
    public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher voucher) {
        Optional<Voucher> findVoucher = voucherService.findByCodeIsNotDeleted(voucher.getCode());
        if (findVoucher.isPresent()) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }
        Voucher newVoucher = voucherService.create(voucher);
        return ResponseEntity.ok(newVoucher);
    }

    @PutMapping("/rest/vouchers/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable("id") Integer id, @RequestBody Voucher voucher) {
        Optional<Voucher> findVoucher = voucherService.findById(id);
        if (findVoucher.isPresent()) {
            Voucher updatedVoucher = voucherService.update(findVoucher.get().getId(), voucher);
            return ResponseEntity.ok(updatedVoucher);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/rest/vouchers/{id}")
    public ResponseEntity<Voucher> deleteVoucher(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(voucherService.deleteById(id));
        } catch (Exception e) {
            log.info("Not found voucher", e);
            return ResponseEntity.notFound().build();
        }
    }
}
