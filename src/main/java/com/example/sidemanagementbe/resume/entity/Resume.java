package com.example.sidemanagementbe.resume.entity;


import com.example.sidemanagementbe.common.entity.BaseEntity;
import com.example.sidemanagementbe.resume.entity.value.ResumeContent;
import com.example.sidemanagementbe.resume.entity.value.TechnologyStacks;
import com.example.sidemanagementbe.resume.infrastructure.converter.TechnologyStackConverter;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "saida_resume")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@PrimaryKeyJoinColumn(name = "resume_id")
public class Resume extends BaseEntity {

    @Lob
    private String introduction;


    @Convert(converter = TechnologyStackConverter.class)
    private TechnologyStacks technologyStacks;

    @Embedded
    private ResumeContent resumeContent;
}
