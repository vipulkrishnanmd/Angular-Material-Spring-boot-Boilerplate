package com.vipulkrishnanmd.workflows.repo;

import com.vipulkrishnanmd.workflows.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    
}
