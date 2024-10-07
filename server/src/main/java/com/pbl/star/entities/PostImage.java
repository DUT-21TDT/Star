package com.pbl.star.entities;

import com.pbl.star.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "post_image")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostImage {
    @Id
    @GeneratedValue(generator = "ulid")
    @GenericGenerator(name = "ulid", type = IdGenerator.class)
    @Column(name = "post_image_id")
    private String id;

    @Column(name = "post_id")
    private String postId;

    @Column(name = "image_url")
    private String imageUrl;
}
