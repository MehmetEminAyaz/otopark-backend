package com.otopark.backend.service;

import com.otopark.backend.entity.Transaction;
import com.otopark.backend.entity.Vehicle;
import com.otopark.backend.repository.TransactionRepository;
import com.otopark.backend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final VehicleRepository vehicleRepo;
    private final TransactionRepository txnRepo;

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepo.findAll();
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return txnRepo.findAll();
    }
}
