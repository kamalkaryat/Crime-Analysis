package com.k2dev.ca.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.NamedNativeQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.k2dev.ca.model.AreaFeedCount;
import com.k2dev.ca.model.Feedback;
import com.k2dev.ca.model.FeedbackPage;
import com.k2dev.ca.model.ResultSetDto;

public interface FeedbackRepository extends JpaRepository<Feedback, String>{

	List<Feedback> findAllByUserUserId(String userId, Pageable pageable);
	Page<Feedback> findAllByUserUserIdAndIsActive(String userId, boolean b, Pageable pageable);
	Page<Feedback> findAllByIsActiveTrue(Pageable pageable);
	Page<Feedback> findAllByIsActiveFalse(Pageable pageable);

	@Query(value = "select c.crime_name,count(f.feed_id) as totFeed\r\n"
			+ "from feedback f INNER JOIN crime c\r\n"
			+ "ON f.crime_crime_id=c.crime_id\r\n"
			+ "INNER JOIN users u\r\n"
			+ "ON u.user_id=f.user_user_id\r\n"
			+ "INNER JOIN areas a\r\n"
			+ "ON u.area_area_id=a.area_id\r\n"
			+ "where a.area_name=?1 AND feed_date_time BETWEEN ?2 AND ?3\r\n"
			+ "GROUP BY c.crime_name\r\n"
			+ "ORDER BY count(f.feed_id) DESC LIMIT 20;\r\n"
			+ "",nativeQuery = true)
	List<Map<String, Object>> calculateTotalAreaFeeds(String areaName,String from, String to);
	
	@Query(value = "select c.crime_name,count(f.feed_id) as totFeed\r\n"
			+ "from feedback f INNER JOIN crime c\r\n"
			+ "ON f.crime_crime_id=c.crime_id\r\n"
			+ "INNER JOIN users u\r\n"
			+ "ON u.user_id=f.user_user_id\r\n"
			+ "INNER JOIN areas a\r\n"
			+ "ON u.area_area_id=a.area_id\r\n"
			+ "where a.area_name=?1 AND feed_date_time BETWEEN ?2 AND ?3 AND f.is_active=true\r\n"
			+ "GROUP BY c.crime_name\r\n"
			+ "ORDER BY count(f.feed_id) DESC LIMIT 20;\r\n"
			+ "",nativeQuery = true)
	List<Map<String, Object>> calculateValidAreaFeeds(String areaName,String from, String to);

	@Query(value = "select count(f.feed_id) as totFeed\r\n"
			+ "from feedback f INNER JOIN crime c\r\n"
			+ "ON f.crime_crime_id=c.crime_id\r\n"
			+ "INNER JOIN users u\r\n"
			+ "ON u.user_id=f.user_user_id\r\n"
			+ "INNER JOIN areas a\r\n"
			+ "ON u.area_area_id=a.area_id\r\n"
			+ "where a.pincode=?1 AND feed_date_time BETWEEN ?2 AND ?3 AND f.is_active=true;",
			nativeQuery = true)
	Object findTotalAreaFeeds(int pincode,String from, String to);
	
	@Query(value = "select c.crime_name,count(f.feed_id) as totFeed\r\n"
			+ "from feedback f INNER JOIN crime c\r\n"
			+ "ON f.crime_crime_id=c.crime_id\r\n"
			+ "INNER JOIN users u\r\n"
			+ "ON u.user_id=f.user_user_id\r\n"
			+ "INNER JOIN areas a\r\n"
			+ "ON u.area_area_id=a.area_id\r\n"
			+ "where a.area_name=?1 AND f.feed_date_time <= ?2 AND f.is_active=true\r\n"
			+ "GROUP BY c.crime_name\r\n"
			+ "ORDER BY count(f.feed_id) DESC LIMIT 20;\r\n"
			+ "",nativeQuery = true)
	List<Map<String, Object>> findFeedTillDate(int pincode, String date);
	
	@Query(value = "select count(f.feed_id) as totFeed\r\n"
			+ "from feedback f INNER JOIN crime c\r\n"
			+ "ON f.crime_crime_id=c.crime_id\r\n"
			+ "INNER JOIN users u\r\n"
			+ "ON u.user_id=f.user_user_id\r\n"
			+ "INNER JOIN areas a\r\n"
			+ "ON u.area_area_id=a.area_id\r\n"
			+ "where a.pincode=?1 AND f.feed_date_time <= ?2 AND f.is_active=true;",
			nativeQuery = true)
	Object findTotalFeedTillDate(int pincode, String date);

	
	//STATE-WISE query
	@Query(value = "select a.state,count(f.feed_id) as totfeed\r\n"
			+ "from feedback f INNER JOIN users u\r\n"
			+ "ON f.user_user_id=u.user_id\r\n"
			+ "INNER JOIN areas a\r\n"
			+ "ON u.area_area_id=a.area_id\r\n"
			+ "where feed_date_time BETWEEN ?1 AND ?2\r\n"
			+ "GROUP BY a.state;", nativeQuery = true)
	List<Map<String, Object>> findTotalFeedsInStates(String from, String to);
	
	@Query(value = "select a.state,count(f.feed_id) as totfeed\r\n"
			+ "from feedback f INNER JOIN users u\r\n"
			+ "ON f.user_user_id=u.user_id\r\n"
			+ "INNER JOIN areas a\r\n"
			+ "ON u.area_area_id=a.area_id\r\n"
			+ "where feed_date_time BETWEEN ?1 AND ?2 AND f.is_active=true\r\n"
			+ "GROUP BY a.state;", nativeQuery = true)
	List<Map<String, Object>> findTotalValidFeedsInStates(String from, String to);
	
	@Query(value="select a.state,count(f.feed_id) as totfeed\r\n"
			+ "from feedback f INNER JOIN users u\r\n"
			+ "ON f.user_user_id=u.user_id\r\n"
			+ "INNER JOIN areas a\r\n"
			+ "ON u.area_area_id=a.area_id\r\n"
			+ "where f.feed_date_time <= ?1 \r\n"
			+ "GROUP BY a.state;", nativeQuery = true)
	List<Map<String, Object>> findFeedTillDate(String from);
	//DISTRICT-WISE query
	
	//PINCODE-WISE query
	
	//AREA-WISE query

	
}
