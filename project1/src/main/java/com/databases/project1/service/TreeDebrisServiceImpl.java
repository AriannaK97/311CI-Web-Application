package com.databases.project1.service;

import com.databases.project1.entity.TreeDebris;
import com.databases.project1.repository.TreeDebrisRepository;
import org.springframework.stereotype.Service;

@Service
public class TreeDebrisServiceImpl implements TreeDebrisService {

    TreeDebrisRepository treeDebrisRepository;

    public boolean saveTreeDebris(TreeDebris treeDebris) {
        return (treeDebrisRepository.save(treeDebris) != null);
    }

}
