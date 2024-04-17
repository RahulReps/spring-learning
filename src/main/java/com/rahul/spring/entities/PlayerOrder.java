package com.rahul.spring.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Builder
public class PlayerOrder {

    public PlayerOrder(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String accountRef, Account account, Set<PlayerOrderLine> playerOrderLines, PlayerOrderShipment playerOrderShipment) {
        this.id = id;
        this.version = version;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.accountRef = accountRef;
        this.setAccount(account);
        this.playerOrderLines = playerOrderLines;
        this.setPlayerOrderShipment(playerOrderShipment);
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name="UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false )
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    public boolean isNew() {
        return this.id == null;
    }

    private String accountRef;

    @ManyToOne
    private Account account;

    public void setAccount(Account account){
        this.account = account;
        account.getPlayerOrders().add(this);
    }

    public void setPlayerOrderShipment(PlayerOrderShipment playerOrderShipment){
        this.playerOrderShipment = playerOrderShipment;
        playerOrderShipment.setPlayerOrder(this);
    }

    @OneToMany(mappedBy = "playerOrder")
    private Set<PlayerOrderLine> playerOrderLines;

    @OneToOne(cascade = CascadeType.PERSIST)
    private PlayerOrderShipment playerOrderShipment;
}
