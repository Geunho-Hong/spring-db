package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

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