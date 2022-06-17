package com.works.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

//Entity sınıfları miras alınmaz. O yüzden @Entity yazmadık
//admin'e extend ettik.
//HeroCompanyApplication'a geç enable
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass //base entity olması için bunu yazdık
@Data
public class Base {
    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    //tarih en iyi Integer girilir. ms falan olduğu için Long kullanacağız.
    @CreatedDate
    private Long createdDate;

    @LastModifiedDate
    private Long lastModifiedDate;
}
