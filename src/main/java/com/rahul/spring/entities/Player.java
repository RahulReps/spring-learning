package com.rahul.spring.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Player {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID" , strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    UUID id;
    @NotBlank
    @NotNull
    @Size(max=50)
    @Column(length=50)
    String name;
    @Column(length=255)
    String club;
    @NotNull
    @NotBlank
    String position;
    @NotNull
    Integer jerseyNo;
    @NotNull
    @NotBlank
    String foot;
    String playStyle;

    @OneToMany(mappedBy = "player")
    private Set<PlayerOrderLine> playerOrderLines;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "player_category",
        joinColumns = @JoinColumn(name="player_id"),
        inverseJoinColumns = @JoinColumn(name="category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public void addCategory(Category category){
        this.categories.add(category);
        category.getPlayers().add(this);
    }

    public void removeCategory(Category category){
        this.categories.remove(category);
        category.getPlayers().remove(category);
    }
}
