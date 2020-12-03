package com.databases.project1.service;

import com.databases.project1.entity.GarbageCarts;
import com.databases.project1.repository.GarbageCartsRepository;
import org.springframework.stereotype.Service;

@Service
public class GarbageCartsServiceimpl implements GarbageCartsService {

    GarbageCartsRepository garbageCartsRepository;

    public boolean saveGarbageCarts(GarbageCarts garbageCarts) {
        return (garbageCartsRepository.save(garbageCarts) != null);
    }

}
