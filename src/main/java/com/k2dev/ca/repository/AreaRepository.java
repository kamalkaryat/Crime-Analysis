package com.k2dev.ca.repository;

import java.util.List;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.k2dev.ca.model.Area;

public interface AreaRepository extends JpaRepository<Area, Integer>{
	/**
     * Find the unique id of an area, if exists
     * @param areaName
     * @param pincode
     * @return Area object if exits otherwise null
     */
	
	@Query("select a.areaId from Area a where a.areaName=:areaName AND a.pincode=:pincode")
    int findByAreaNameAndPincode(@Param("areaName") String areaName, @Param("pincode") int pincode);

    /**
     * Find distinct state names
     * @return List of state names
     */
    @Query("select distinct a.state from Area a")
    List<String> findByDistinctState();

    /**
     * Find all the distinct districts which belongs to a state
     * @param state
     * @return List of district names
     */
    @Query("select distinct a.district from Area a where a.state=:state")
    List<String> findDistinctDistrictByState(String state);

    /**
     * Find all the distinct pincodes which belongs to a particular district
     * @param state
     * @param district
     * @return List of pincodes
     */
    @Query("select distinct a.pincode from Area a where a.state=:state AND a.district=:district")
    List<Integer> findDistinctByStateAndDistrict(@Param("state") String state, @Param("district") String district);

    /**
     * Find all the distinct area names belongs to a particular pincode
     * @param pincode
     * @return List of area names
     */
    @Query("select distinct a.areaName from Area a where a.pincode=:pincode")
    List<String> findDistinctAreaNameByPincode(int pincode); 
}
