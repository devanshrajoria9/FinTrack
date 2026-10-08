    package com.devansh.fintrack.entity;

    import com.devansh.fintrack.enums.CategoryType;
    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import org.hibernate.annotations.CreationTimestamp;
    import org.hibernate.annotations.UpdateTimestamp;

    import java.time.LocalDateTime;

    @Setter
    @Getter
    @NoArgsConstructor
    @Entity
    @Table(name = "categories")
    public class Category {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private String description;
        @Enumerated(EnumType.STRING)
        private CategoryType type;
        @CreationTimestamp
        private LocalDateTime createdAt;
        @UpdateTimestamp
        private LocalDateTime updatedAt;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;


    }
