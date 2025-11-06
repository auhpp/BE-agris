package com.agri_supplies_shop.repository.custom.impl;

import com.agri_supplies_shop.dto.request.SearchProductRequest;
import com.agri_supplies_shop.entity.Product;
import com.agri_supplies_shop.repository.custom.ProductRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    private void joinTable(SearchProductRequest searchProductRequest, StringBuilder query) {
        if (searchProductRequest.getPriceFrom() != null || searchProductRequest.getPriceTo() != null) {
            query.append("  INNER JOIN product_variant_value pvv ON p.id = pvv.product_id ");
        }
        if (searchProductRequest.getCategoryName() != null) {
            query.append("  INNER JOIN category c ON p.category_id = c.id ");
        }
    }

    private void handleQuery(SearchProductRequest req, StringBuilder where, Map<String, Object> params) {
        if (req.getName() != null) {
            where.append(" AND unaccent(LOWER(p.name)) ILIKE unaccent(LOWER(:name)) ");
            params.put("name", "%" + req.getName().toLowerCase() + "%");
        }
        if (req.getCategoryName() != null) {
            where.append(" AND c.name ILIKE :categoryName ");
            params.put("categoryName", "%" + req.getCategoryName() + "%");
        }
        if (req.getCategoryId() != null && !req.getCategoryId().isEmpty()) {
            where.append(" AND p.category_id IN :categoryIds ");
            params.put("categoryIds", req.getCategoryId());
        }
        if (req.getStatus() != null && !req.getStatus().isEmpty()) {
            where.append(" AND p.status = :status ");
            params.put("status", req.getStatus());
        }
        if (req.getPriceFrom() != null) {
            where.append(" AND pvv.selling_price >= :priceFrom ");
            params.put("priceFrom", req.getPriceFrom());
        }
        if (req.getPriceTo() != null) {
            where.append(" AND pvv.selling_price <= :priceTo ");
            params.put("priceTo", req.getPriceTo());
        }
    }

    @Override
    public List<Object> findProduct(SearchProductRequest searchProductRequest, int page, int size) {
        StringBuilder query = new StringBuilder(" SELECT p.* FROM product p ");
        joinTable(searchProductRequest, query);
        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        handleQuery(searchProductRequest, where, params);
        query.append(where);
        query.append(" GROUP BY p.id");

        Query countQuery = entityManager.createNativeQuery(query.toString());
        Query dataQuery = entityManager.createNativeQuery(query.toString(), Product.class)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size);

        // Set all parameters
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
            dataQuery.setParameter(entry.getKey(), entry.getValue());
        }

        long totalElement = countQuery.getResultList().size();
        List result = dataQuery.getResultList();

        List<Object> values = new ArrayList<>();
        values.add(0, result);
        values.add(1, totalElement);
        return values;
    }
}
