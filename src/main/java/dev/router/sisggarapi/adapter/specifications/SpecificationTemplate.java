package dev.router.sisggarapi.adapter.specifications;


import dev.router.sisggarapi.core.domain.*;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    @And({
            @Spec(path = "storageStatus", spec = Equal.class),
            @Spec(path = "description", spec = Like.class)
    })
    public interface StorageSpec extends Specification<Storage> {}

    @And({
            @Spec(path = "status", spec = Equal.class),
            @Spec(path = "phoneNumber", spec = Like.class)
    })
    public interface DeliveryManagerSpec extends Specification<DeliveryManager> {}


    public static Specification<Costumer> costumerLocationId(final UUID locationId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<Costumer> costumer = root;
            Root<Location> location = query.from(Location.class);
            Expression<Collection<Costumer>> costumerLocations = costumer.get("locations");
            return cb.and(cb.equal(location.get("locationId"), locationId), cb.isMember(costumer, costumerLocations));
        };
    }

    @Spec(path = "description", spec = Like.class)
    public interface LocationSpec extends Specification<Location> {}

    public static Specification<Location> locationStorageId(final UUID storageId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<Location> location = root;
            Root<Storage> storage = query.from(Storage.class);
            Expression<Collection<Location>> locationStorages = storage.get("locations");
            return cb.and(cb.equal(storage.get("storageId"), storageId), cb.isMember(location, locationStorages));
        };
    }

    @Or({
            @Spec(path = "costumerStatus", spec = Equal.class),
            @Spec(path = "name", spec = Like.class),
            @Spec(path = "telephone", spec = Like.class),
            @Spec(path = "nif", spec = Like.class),
            @Spec(path = "email", spec = Like.class)
    })
    public interface CostumerSpec extends Specification<Costumer> {}

    @And({
            @Spec(path = "providerStatus", spec = Equal.class),
            @Spec(path = "name", spec = Like.class),
            @Spec(path = "telephone", spec = Like.class)
    })
    public interface ProviderSpec extends Specification<Supplier> {}

    @And({
            @Spec(path = "userType", spec = Equal.class),
            @Spec(path = "userStatus", spec = Equal.class),
            @Spec(path = "email", spec = Like.class),
            @Spec(path = "fullName", spec = Like.class)
    })
    public interface UserSpec extends Specification<User> {}




}
