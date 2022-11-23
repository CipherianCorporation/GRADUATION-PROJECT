package com.edu.graduationproject.service.impl;

import com.edu.graduationproject.entity.Voucher;
import com.edu.graduationproject.repository.VoucherRepository;
import com.edu.graduationproject.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    VoucherRepository voucherRepo;
    @Override
    public Voucher create(Voucher voucher) {
        return voucherRepo.save(voucher);
    }

    @Override
    public Voucher update(Integer id, Voucher voucher) {
        voucher.setId(id);
        return voucherRepo.save(voucher);
    }

    @Override
    public Voucher deleteById(Integer id) {
        voucherRepo.deleteById(id);
        return null;
    }

    @Override
    public List<Voucher> findAll() {
        return voucherRepo.findAll();
    }

    @Override
    public Optional<Voucher> findById(Integer id) {
        Optional<Voucher> voucher = voucherRepo.findById(id);
        return voucher;


    }

    @Override
    public Voucher findByCode(String code) {
        return voucherRepo.findByCode(code);
    }

}
