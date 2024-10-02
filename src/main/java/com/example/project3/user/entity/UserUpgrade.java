package com.example.project3.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "role_upgrade_req")
@NoArgsConstructor
@AllArgsConstructor
public class UserUpgrade extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity target;

    @Setter
    private Boolean approved;
}