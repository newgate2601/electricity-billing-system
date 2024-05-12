package com.example.electricitybillingsystem.service;

import com.example.electricitybillingsystem.filter.Filter;
import com.example.electricitybillingsystem.mapper.BillMapper;
import com.example.electricitybillingsystem.model.BillEntity;
import com.example.electricitybillingsystem.model.CustomerEntity;
import com.example.electricitybillingsystem.repository.CustomerRepository;
import com.example.electricitybillingsystem.vo.dto.BillResponse;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BillService2 {
    private final EntityManager entityManager;
    private final BillMapper billMapper;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public Page<BillResponse> getBills(String statusValue,
                                       String name,
                                       Integer month,
                                       Integer year,
                                       String order,
                                       Pageable pageable){
        if (Objects.isNull(month)){
            OffsetDateTime now = OffsetDateTime.now();
            month = now.getMonth().getValue();
            year = now.getYear();
        }
        if (Objects.isNull(order)) {
            order = "ASC";
        }
        List<Long> employeeIds = null;
        if (Objects.nonNull(name)) {
            List<CustomerEntity> employeeEntities = Filter.builder(CustomerEntity.class, entityManager)
                    .search()
                    .isContain("name", name)
                    .getPage(PageRequest.of(0, 100000))
                    .getContent();
            if (Objects.nonNull(employeeEntities)) {
                employeeIds = employeeEntities.stream().map(CustomerEntity::getId).collect(Collectors.toList());
            }
        }

        Page<BillEntity> bills = Filter.builder(BillEntity.class, entityManager)
                .search()
                .isIn("customerId", employeeIds)

                .filter()
                .isEqual("statusValue", statusValue)
                .isEqual("month", month)
                .isEqual("year", year)
                .orderBy("price", order)
                .getPage(pageable);

        Map<Long, CustomerEntity> customerEntityMap = customerRepository.findAllByIdIn(
                bills.stream().map(BillEntity::getCustomerId).distinct().collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(CustomerEntity::getId, Function.identity()));

        return bills.map(billEntity -> {
            BillResponse billResponse = billMapper.getBillResponseBy(billEntity);
            if (customerEntityMap.containsKey(billEntity.getCustomerId())) {
                billResponse.setCustomerName(customerEntityMap.get(billEntity.getCustomerId()).getName());
            }
            billResponse.setStatus(billEntity.getStatusValue());
            return billResponse;
        });
    }
}
