package org.tarun.learning.tweetnews.trends.service;

import org.tarun.learning.tweetnews.trends.model.HashTag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.tarun.learning.tweetnews.trends.repository.HashTagRepository;

@Service
public class HashTagService {
  @Autowired
  private HashTagRepository repository;

  public List<HashTag> getAll() {
      return repository.getAll();
  }
  public List<HashTag> getTop(int top) {
      return repository.getTop(top);
  }
}
