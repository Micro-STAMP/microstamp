package step3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import step3.entity.Context;

@Repository
public interface ContextRepository extends PagingAndSortingRepository<Context, Long> {
    Page<Context> findByContextTableId(Long contextTableId, Pageable pageable);
}
