package dev.router.sisggarapi.core.repository;

import dev.router.sisggarapi.core.domain.Costumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CostumerRepository extends JpaRepository<Costumer, UUID>, JpaSpecificationExecutor<Costumer> {

    Optional<Costumer> findByName(@Param("name") String name);

    @Modifying
    @Query(value = "Insert INTO tb_costumer_location(costumer_id,location_id) values(:costumerId, :locationId)", nativeQuery = true)
    void addLocation(@Param("costumerId") UUID costumerId, @Param("locationId") UUID locationId);

    @Modifying
    @Query(value = "Insert INTO tb_costumer_delivery_managers(costumer_id,delivery_manager_id) values(:costumerId, :deliveryManagerId)", nativeQuery = true)
    void addDeliveries(@Param("costumerId") UUID costumerId, @Param("deliveryManagerId") UUID deliveryManagerId);

    @Modifying
    @Query(value = "UPDATE tb_costumer set delivery_limit=:deliveryLimit where costumer_id=:costumerId",nativeQuery = true)
    void updateLimit(@Param("costumerId") UUID costumerId, Integer deliveryLimit);

    @Modifying
    @Query(value = "DELETE FROM tb_costumer_delivery_managers  where costumer_id=:costumerId AND delivery_manager_id=:deliveryManagerId",nativeQuery = true)
    void removeIntoDeliveryManager(@Param("costumerId") UUID costumerId, @Param("deliveryManagerId") UUID deliveryManagerId);

    @Modifying
    @Query(value = "DELETE FROM tb_costumer_location  where costumer_id=:costumerId AND location_id=:locationId", nativeQuery = true)
    void removeLocationIntoCostumer(@Param("costumerId") UUID costumerId, @Param("locationId") UUID locationId);

    @Query(value = "SELECT count(costumer_id) FROM tb_costumer_delivery_managers CDM " +
            "where CDM.costumer_id=:costumerId AND CDM.delivery_manager_id=:deliveryManagerId",nativeQuery = true)
    long isRelatedCostumerWithDeliveryManager(@Param("costumerId") UUID costumerId, @Param("deliveryManagerId") UUID deliveryManagerId);

    @Query(value = "SELECT count(costumer_id) FROM tb_costumer_location CL " +
            "where CL.costumer_id=:costumerId AND CL.location_id=:locationId", nativeQuery = true)
    long isRelatedCostumerWithLocation(@Param("costumerId") UUID costumerId, @Param("locationId") UUID locationId);

    @Transactional
    @Query("UPDATE Costumer c set c.costumerStatus=:status WHERE c.costumerId=:costumerId")
    void modifyStatus(@Param("status") String status, @Param("costumerId") UUID costumerId);

    @Query(value = "SELECT DISTINCT c.name,c.costumer_id, \n" +
            "c.address, \n" +
            "c.email, \n" +
            "c.telephone, \n" +
            "c.nif, \n" +
            "c.delivery_limit, \n" +
            "c.possession, \n" +
            "c.costumer_status, \n" +
            "c.creation_date,c.last_update_date\n" +
            "FROM tb_costumer c inner JOIN tb_costumer_location cl ON cl.costumer_id=c.costumer_id\n" +
            "inner JOIN tb_location l ON l.location_id=cl.location_id\n" +
            "inner JOIN tb_storage s ON s.storage_id=l.storage_id\n" +
            "WHERE s.storage_id=:storageId \n" +
            "AND (LOWER(c.name) like LOWER(CONCAT('%',:name,'%')) \n" +
            "AND c.telephone like CONCAT('%',:telephone,'%') \n" +
            "AND c.nif like CONCAT('%',:nif,'%'))\n" +
            "GROUP BY c.costumer_id", nativeQuery = true)
    List<Costumer> findAll(
            @Param("name") String name,
            @Param("nif") String nif,
            @Param("telephone") String telephone,
            @Param("storageId") UUID storageId);

    @Query(value = "SELECT DISTINCT c.name,c.costumer_id, \n" +
            "c.address, \n" +
            "c.email, \n" +
            "c.telephone, \n" +
            "c.nif, \n" +
            "c.delivery_limit, \n" +
            "c.possession, \n" +
            "c.costumer_status, \n" +
            "c.creation_date,c.last_update_date\n" +
            "FROM tb_costumer c inner JOIN tb_costumer_location cl ON cl.costumer_id=c.costumer_id\n" +
            "inner JOIN tb_location l ON l.location_id=cl.location_id\n" +
            "inner JOIN tb_storage s ON s.storage_id=l.storage_id\n" +
            "WHERE  s.storage_id=:storageId and l.location_id=:locationId \n" +
            "AND (LOWER(c.name) like LOWER(CONCAT('%',:name,'%')) \n" +
            "and c.telephone like CONCAT('%',:telephone,'%') \n" +
            "and c.nif like CONCAT('%',:nif,'%'))\n" +
            "GROUP BY c.costumer_id", nativeQuery = true)
    List<Costumer> findAllByLocation(
            @Param("name") String name,
            @Param("nif") String nif,
            @Param("telephone") String telephone,
            @Param("storageId") UUID storageId,
            @Param("locationId") UUID locationId);

    @Query(value="select case when count(tcl) > 0 THEN true ELSE false END FROM tb_costumer_location tcl WHERE tcl.costumer_id= :costumerId and tcl.location_id= :locationId",nativeQuery = true)
    boolean existCostumerAndLocation(@Param("costumerId") UUID costumerId, @Param("locationId") UUID locationId);

    @Query(value="select case when count(tcd) > 0 THEN true ELSE false END FROM tb_costumer_delivery_managers tcd WHERE tcd.costumer_id= :costumerId and tcd.delivery_manager_id= :deliveryManagerId",nativeQuery = true)
    boolean existCostumerAndDeliveryManagers(@Param("costumerId") UUID costumerId, @Param("deliveryManagerId") UUID deliveryManagerId);
}
