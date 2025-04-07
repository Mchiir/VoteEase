package mchiir.com.vote.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mchiir.com.vote.models.Log;
import java.util.*;

@Repository
public interface LogRepository extends JpaRepository<Log, UUID> {
    
}
