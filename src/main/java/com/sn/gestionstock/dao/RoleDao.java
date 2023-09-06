package com.sn.gestionstock.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sn.gestionstock.model.Roles;

@Repository
public interface RoleDao extends JpaRepository<Roles, Integer> {

}
