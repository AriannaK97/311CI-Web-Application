package com.databases.project1.service;

import com.databases.project1.entity.GraffitiRemoval;
import com.databases.project1.repository.GraffityRemovalRepository;
import org.springframework.stereotype.Service;

@Service
public class GraffityRemovalServiceImpl implements GraffityRemovalService {

    GraffityRemovalRepository graffityRemovalRepository;

    public boolean saveGraffityRemoval(GraffitiRemoval graffitiRemoval) {
        return (graffityRemovalRepository.save(graffitiRemoval) != null);
    }

}
