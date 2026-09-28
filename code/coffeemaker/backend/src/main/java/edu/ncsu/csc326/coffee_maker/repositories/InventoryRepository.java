package edu.ncsu.csc326.coffee_maker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc326.coffee_maker.entity.Inventory;

/**
 * InventoryRepository for working with the DB through the 
 * JpaRepository.
 *
 * @author CSC 326 Course Staff
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
