package org.tukorea.libcheckout;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.tukorea.libcheckout.book.dataaccess.repository.BookRepository;
import org.tukorea.libcheckout.member.dataaccess.repository.MemberRepository;
import org.tukorea.libcheckout.loan.dataaccess.repository.LoanRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LibCheckoutApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Test
    void homePageLoads() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Library Checkout Service")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("전체 책 목록")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("대출 현황")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("유저 별 대출 통계")));
    }

    @Test
    void memberPageLoadsDashboard() throws Exception {
        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("회원 대출 현황")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("현재 대출 회원")));
    }

    @Test
    void createBookMemberAndLoanFlowWorks() throws Exception {
        mockMvc.perform(post("/books")
                        .param("title", "테스트 도서")
                        .param("author", "테스트 저자")
                        .param("isbn", "9780000000001")
                        .param("publisher", "테스트 출판사")
                        .param("publishedDate", "2024-01-01")
                        .param("totalQuantity", "3")
                        .param("status", "AVAILABLE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        mockMvc.perform(post("/members")
                        .param("name", "홍길동")
                        .param("email", "hong@example.com")
                        .param("phoneNumber", "010-1234-5678")
                        .param("status", "ACTIVE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members"));

        Long bookId = bookRepository.findAll().getFirst().getId();
        Long memberId = memberRepository.findAll().getFirst().getId();

        mockMvc.perform(post("/loans")
                        .param("bookId", bookId.toString())
                        .param("memberId", memberId.toString())
                        .param("dueDate", "2099-12-31"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/loans"));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("테스트 도서")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("홍길동")));

        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("홍길동")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("hong@example.com")));

        assertThat(bookRepository.findById(bookId)).isPresent();
        assertThat(bookRepository.findById(bookId).orElseThrow().getAvailableQuantity()).isEqualTo(2);
        assertThat(loanRepository.count()).isEqualTo(1);
    }
}
