package in.ashokit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.entity.UserAccountEntity;

public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Integer> {

	public UserAccountEntity findByEmailAndPwd(String email, String pwd);

	public UserAccountEntity findByEmail(String email);

}
