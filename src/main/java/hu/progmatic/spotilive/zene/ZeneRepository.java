package hu.progmatic.spotilive.zene;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ZeneRepository extends JpaRepository<ZeneEntity, Integer> {
    ZeneEntity getZeneByCimContainingIgnoreCase(String cim);
}
