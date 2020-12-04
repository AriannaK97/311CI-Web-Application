package com.databases.project1.service;

import com.databases.project1.entity.TreeTrims;
import com.databases.project1.repository.TreeTrimsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TreeTrimServiceImpl implements TreeTrimService {

    @Autowired
    TreeTrimsRepository treeTrimsRepository;

    public boolean saveTreeTrim(TreeTrims treeTrims) {
        return (treeTrimsRepository.save(treeTrims) != null);
    }

}
