package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionContest.*;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repository;

    // 각 테스트가 실행되기 직전에 실행된다.
    @BeforeEach
    void beforeEach() {
        //  기본 DriverManager - 항상 새로운 커넥션을 획득
        //  DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME,PASSWORD);

        // 커넥션풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPoolName(PASSWORD);

        repository = new MemberRepositoryV1(dataSource);
    }


    @Test
    void crud() throws SQLException {

        // save
        Member member = new Member("memberV8", 10000);
        repository.save(member);

        // findById
        Member findMember = repository.findById(member.getMemberId());

        log.info("findMember={}", findMember);

        /**
         * member.equals(findMember)가 true인 경우는 lombok @Data에서
         * equals와 hashCode를 재정의하기 때문이다
         */

        log.info("member == findMember {}", member == findMember);
        log.info("member == findMember {}", member.equals(findMember));

        Assertions.assertThat(findMember).isEqualTo(member);

        // update
        repository.update(member.getMemberId(), 20000);
        Member updateMember = repository.findById(member.getMemberId());
        Assertions.assertThat(updateMember.getMoney()).isEqualTo(20000);

        // delete
        repository.delete(member.getMemberId());
        Assertions.assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

    }

}