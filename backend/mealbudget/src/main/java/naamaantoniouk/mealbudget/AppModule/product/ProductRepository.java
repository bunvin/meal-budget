package naamaantoniouk.mealbudget.AppModule.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
    public List<Product> findByName(String name);


}
