package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.ImportGoodsStatus;
import com.agri_supplies_shop.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private ZonedDateTime createdDate;

    private ZonedDateTime importDate;

    private Long amount;

    private String note;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private ImportGoodsStatus importStatus;

    //Relationship
    //Receipt detail
    @OneToMany(mappedBy = "warehouseReceipt")
    private List<ReceiptDetail> receiptDetails;

    //supplier
    @ManyToOne
    private Supplier supplier;

    //warehouse
    @ManyToOne
    private Warehouse warehouse;

    //Staff
    @ManyToOne
    private Staff staff;
}
