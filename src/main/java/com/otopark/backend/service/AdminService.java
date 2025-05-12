package com.otopark.backend.service;

import com.otopark.backend.entity.Transaction;
import com.otopark.backend.entity.Vehicle;

import java.util.List;

public interface AdminService {
    List<Vehicle> getAllVehicles();
    List<Transaction> getAllTransactions();
}
