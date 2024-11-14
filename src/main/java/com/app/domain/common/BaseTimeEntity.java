package com.app.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class}) //엔티티의 생성 및 변경이 일어났을 때, 해당 엔티티의 특정 필드에 값을 자동으로 설정하는 역할
@MappedSuperclass //상속받은 엔티티 클래스의 테이블에 해당 필드를 자동으로 추가하는 역할
@Getter
/**
 * abstract 추상클래스
 * 다른 엔티티 클래스들이 상속받아 공통적인 시간 관련 필드와 기능을 제공하기 위해서
 * 별도의 객체선언이 불가능함
 */
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createTime;

    @LastModifiedDate // 엔티티가 수정될 때마다 자동으로 값이 설정됨
    private LocalDateTime updateTime;
}
