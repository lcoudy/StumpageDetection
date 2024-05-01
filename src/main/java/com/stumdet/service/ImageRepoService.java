package com.stumdet.service;

import com.stumdet.mapper.ImageRepoMapper;
import com.stumdet.pojo.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ImageRepoService implements ImageRepoMapper {

    @Autowired
    private ImageRepoMapper imageRepoMapper;

    @Override
    public ImageRepo getRepoById(String rid) {
        ImageRepo repo = this.imageRepoMapper.getRepoById(rid);
        return repo;
    }

    @Override
    public List<ImageRepo> getRepoByUser(String uid) {
        List<ImageRepo> repos = this.imageRepoMapper.getRepoByUser(uid);
        return repos;
    }

    @Override
    public void createRepo(ImageRepo repo) {
        this.imageRepoMapper.createRepo(repo);
    }

    @Override
    public void deleteRepo(String rid) {
        this.imageRepoMapper.deleteRepo(rid);
    }

    @Override
    public void updateRepoInfo(Map<String, Object> param) {
        this.imageRepoMapper.updateRepoInfo(param);
    }
}
