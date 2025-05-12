package com.otopark.backend.service;

import java.util.List;

public interface BlacklistService {
    /** Yeni plaka ekler, zaten varsa IllegalArgumentException fırlatır */
    String add(String plate);

    /** Verilen plakayı listeden kaldırır, yoksa IllegalArgumentException fırlatır */
    void remove(String plate);

    /** Tüm kara liste plakalarını döner */
    List<String> getAll();
}
