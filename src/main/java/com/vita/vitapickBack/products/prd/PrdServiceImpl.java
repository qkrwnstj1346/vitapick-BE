package com.vita.vitapickBack.products.prd;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrdServiceImpl implements PrdService {

    private final PrdRepository prdRepository;

    @Override
    public List<Prd> getAllPrd() {
        return prdRepository.findAll();
    }
}