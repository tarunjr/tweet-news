package org.tarun.learning.tweetnews.trends.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.tarun.learning.tweetnews.trends.model.Url;
import org.tarun.learning.tweetnews.trends.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tarunrathor on 01/11/16.
 */
@Service
public class UrlService {

    @Autowired
    private final UrlRepository repository;

    public UrlService(UrlRepository repository) {
        this.repository = repository;
    }
    public List<Url> getAll() {
        return repository.getAll();
    }
    public List<Url> getTop(int top) {
        return repository.getTop(top);
    }
}
