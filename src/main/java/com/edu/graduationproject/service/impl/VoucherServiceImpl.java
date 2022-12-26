package com.edu.graduationproject.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.graduationproject.entity.Voucher;
import com.edu.graduationproject.repository.VoucherRepository;
import com.edu.graduationproject.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    VoucherRepository voucherRepo;

    @Override
    public Optional<Voucher> findById(Integer id) {
        return voucherRepo.findById(id);
    }

    @Override
    public Optional<Voucher> findByCodeIsNotDeleted(String code) {
        return voucherRepo.findByCodeIsNotDeleted(code);
    }

    @Override
    public Voucher create(Voucher voucher) {
        if (voucher.getIsUsed() == null) {
            voucher.setIsUsed(false);
        }
        return voucherRepo.save(voucher);
    }

    @Override
    public Voucher update(Integer id, Voucher voucher) {
        voucher.setId(id);
        voucher.setUpdatedAt(new Date());
        return voucherRepo.save(voucher);
    }

    @Override
    public Voucher deleteById(Integer id) {
        Optional<Voucher> findVoucher = voucherRepo.findById(id);
        if (findVoucher.isPresent()) {
            findVoucher.get().setIsDeleted(true);
            return voucherRepo.save(findVoucher.get());
        }
        // voucherRepo.deleteById(id);
        return null;
    }

    @Override
    public List<Voucher> findAll() {
        return voucherRepo.findAll();
    }

}
